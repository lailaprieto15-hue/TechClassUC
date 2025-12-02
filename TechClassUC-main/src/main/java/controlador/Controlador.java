package controlador;

import modelo.Cliente;
import modelo.GestionCliente;
import modelo.HistorialAtencion;
import modelo.RegistroAcciones;
import vista.Vista;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class Controlador {

    private final Vista vista;
    private final GestionCliente gestionCliente;
    private final HistorialAtencion historialAtencion;
    private final RegistroAcciones registroAcciones;
    private int normalClientsAttendedCounter = 0;

    public Controlador(Vista vista) {
        this.vista = vista;
        this.gestionCliente = new GestionCliente();
        this.historialAtencion = new HistorialAtencion();
        this.registroAcciones = new RegistroAcciones();

        this.vista.getBtnAgregar().addActionListener(e -> agregarCliente());
        this.vista.getBtnAtender().addActionListener(e -> atenderCliente());
        this.vista.getBtnEliminar().addActionListener(e -> eliminarCliente());
        this.vista.getBtnDeshacer().addActionListener(e -> deshacerUltimaAccion());
        this.vista.getBtnBuscar().addActionListener(e -> buscarHistorial());

        // La habilitación inicial se maneja en actualizarEstadoBotones() llamado dentro de actualizarVistaCompleta()
        actualizarVistaCompleta();
    }

    // [NUEVO MÉTODO] para controlar qué botones están activos
    private void actualizarEstadoBotones() {
        boolean hayClientesEnEspera = gestionCliente.totalClientesEnEspera() > 0;
        boolean hayClientesAtendidos = historialAtencion.getHistorial().size() > 0;
        boolean hayAccionesUndoable = registroAcciones.hayAccionesUndoable();

        // 1. Botón AGREGAR: Siempre habilitado
        vista.getBtnAgregar().setEnabled(true);

        // 2. Botón ATENDER: Habilitado si hay clientes en espera
        vista.getBtnAtender().setEnabled(hayClientesEnEspera);

        // 3. Botón ELIMINAR: Habilitado si hay clientes en espera (solo se pueden eliminar de la cola)
        vista.getBtnEliminar().setEnabled(hayClientesEnEspera);

        // 4. Botón DESHACER: Habilitado si hay acciones que se pueden deshacer (solo agregar, eliminar, atender)
        vista.getBtnDeshacer().setEnabled(hayAccionesUndoable);

        // 5. Botón BUSCAR: Habilitado si hay clientes en el historial (se busca en el historial)
        vista.getBtnBuscar().setEnabled(hayClientesAtendidos);
        vista.getTxtBuscar().setEnabled(hayClientesAtendidos);
    }

    private void agregarCliente() {
        try {
            // ... (Lógica de JOptionPane sin cambios)
            String idStr = JOptionPane.showInputDialog(vista, "Ingrese el ID del cliente:", "Agregar Cliente", JOptionPane.QUESTION_MESSAGE);
            if (idStr == null || idStr.trim().isEmpty()) return;
            int id = Integer.parseInt(idStr);

            if (gestionCliente.buscarClienteEnEspera(id) != null) {
                JOptionPane.showMessageDialog(vista, "Ya existe un cliente con este ID en espera.", "Error de ID", JOptionPane.ERROR_MESSAGE);
                return;
            }

            String nombre = JOptionPane.showInputDialog(vista, "Ingrese el nombre del cliente:", "Agregar Cliente", JOptionPane.QUESTION_MESSAGE);
            if (nombre == null || nombre.trim().isEmpty()) {
                JOptionPane.showMessageDialog(vista, "El nombre del cliente no puede estar vacío.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            String[] tipos = {"Soporte", "Mantenimiento", "Reclamo"};
            String tipo = (String) JOptionPane.showInputDialog(vista, "Seleccione el tipo de solicitud:", "Agregar Cliente", JOptionPane.QUESTION_MESSAGE, null, tipos, tipos[0]);
            if (tipo == null) return;

            String[] prioridades = {"Normal", "Urgente"};
            String prioridad = (String) JOptionPane.showInputDialog(vista, "Seleccione la prioridad:", "Agregar Cliente", JOptionPane.QUESTION_MESSAGE, null, prioridades, prioridades[0]);
            if (prioridad == null) return;

            Cliente nuevoCliente = new Cliente(id, nombre, tipo, prioridad);
            gestionCliente.agregarCliente(nuevoCliente);
            registroAcciones.registrarAccion("agregar", nuevoCliente);
            JOptionPane.showMessageDialog(vista, "Cliente agregado correctamente.", "Éxito", JOptionPane.INFORMATION_MESSAGE);
            actualizarVistaCompleta();

        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(vista, "El ID debe ser un número entero válido.", "Error de ID", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void atenderCliente() {
        Cliente atendido = null;

        if (normalClientsAttendedCounter >= 2) {
            atendido = gestionCliente.atenderClientePorPrioridad("Urgente");
            if (atendido != null) {
                normalClientsAttendedCounter = 0;
            }
        }

        if (atendido == null) {
            atendido = gestionCliente.atenderCliente();
        }

        if (atendido != null) {
            if (atendido.getPrioridad().equalsIgnoreCase("Normal")) {
                normalClientsAttendedCounter++;
            } else if (atendido.getPrioridad().equalsIgnoreCase("Urgente") && normalClientsAttendedCounter < 2) {
                normalClientsAttendedCounter = 0;
            }

            atendido.setTiempoAtencion(LocalDateTime.now());
            historialAtencion.agregarClienteAtendido(atendido);
            registroAcciones.registrarAccion("atender", atendido);
            JOptionPane.showMessageDialog(vista, "Cliente " + atendido.getId() + " atendido.", "Atención", JOptionPane.INFORMATION_MESSAGE);
        } else {
            JOptionPane.showMessageDialog(vista, "No hay clientes en espera.", "Aviso", JOptionPane.WARNING_MESSAGE);
        }
        actualizarVistaCompleta();
    }

    private void eliminarCliente() {
        try {
            String idStr = JOptionPane.showInputDialog(vista, "Ingrese el ID del cliente a eliminar:", "Eliminar Cliente", JOptionPane.QUESTION_MESSAGE);
            if (idStr == null || idStr.trim().isEmpty()) return;
            int id = Integer.parseInt(idStr);

            if (historialAtencion.buscarPorId(id) != null) {
                JOptionPane.showMessageDialog(vista, "El cliente con ID " + id + " ya fue atendido y no puede ser eliminado.", "Error de Eliminación", JOptionPane.ERROR_MESSAGE);
                return;
            }

            Cliente eliminado = gestionCliente.eliminarCliente(id);

            if (eliminado != null) {
                registroAcciones.registrarAccion("eliminar", eliminado);
                JOptionPane.showMessageDialog(vista, "Cliente " + id + " eliminado de la cola de espera.", "Éxito", JOptionPane.INFORMATION_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(vista, "No se encontró cliente con el ID " + id + " en la cola de espera.", "Aviso", JOptionPane.WARNING_MESSAGE);
            }
            actualizarVistaCompleta();
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(vista, "Ingrese un ID numérico para eliminar.", "Error de ID", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void deshacerUltimaAccion() {
        RegistroAcciones.Accion accionDeshecha = registroAcciones.popUltimaAccionUndoable();

        if (accionDeshecha == null) {
            JOptionPane.showMessageDialog(vista, "No hay acciones para deshacer.", "Aviso", JOptionPane.WARNING_MESSAGE);
            return;
        }

        String tipo = accionDeshecha.getTipoAccion();
        Cliente cliente = accionDeshecha.getCliente();
        String nuevaAccionRegistro = "";

        switch (tipo) {
            case "agregar":
                gestionCliente.eliminarCliente(cliente.getId());
                nuevaAccionRegistro = "deshacer_agregar";
                JOptionPane.showMessageDialog(vista, "Deshecha acción 'agregar'. Cliente " + cliente.getId() + " eliminado de la cola.", "Deshacer", JOptionPane.INFORMATION_MESSAGE);
                break;
            case "eliminar":
                gestionCliente.agregarCliente(cliente);
                nuevaAccionRegistro = "deshacer_eliminar";
                JOptionPane.showMessageDialog(vista, "Deshecha acción 'eliminar'. Cliente " + cliente.getId() + " restaurado a la cola.", "Deshacer", JOptionPane.INFORMATION_MESSAGE);
                break;
            case "atender":
                if (historialAtencion.removerUltimoCliente(cliente)) {
                    gestionCliente.agregarClienteAlFrente(cliente);
                    nuevaAccionRegistro = "deshacer_atender";
                    if (cliente.getPrioridad().equalsIgnoreCase("Normal")) {
                        normalClientsAttendedCounter = Math.max(0, normalClientsAttendedCounter - 1);
                    }
                    JOptionPane.showMessageDialog(vista, "Deshecha acción 'atender'. Cliente " + cliente.getId() + " retornado a la cola de espera.", "Deshacer", JOptionPane.INFORMATION_MESSAGE);
                } else {
                    JOptionPane.showMessageDialog(vista, "Error al deshacer 'atender': Cliente no encontrado en historial.", "Error Deshacer", JOptionPane.ERROR_MESSAGE);
                }
                break;
            default:
                JOptionPane.showMessageDialog(vista, "Tipo de acción no reconocido: " + tipo, "Error Deshacer", JOptionPane.ERROR_MESSAGE);
        }

        if (!nuevaAccionRegistro.isEmpty()) {
            registroAcciones.registrarAccion(nuevaAccionRegistro, cliente);
        }

        actualizarVistaCompleta();
    }

    private void buscarHistorial() {
        String textoBusqueda = vista.getTxtBuscar().getText().trim();
        List<Cliente> resultados = null;

        if (textoBusqueda.isEmpty()) {
            JOptionPane.showMessageDialog(vista, "Ingrese un ID o Tipo de Solicitud para buscar.", "Búsqueda", JOptionPane.WARNING_MESSAGE);
            return;
        }

        try {
            int id = Integer.parseInt(textoBusqueda);
            Cliente clienteEncontrado = historialAtencion.buscarPorId(id);
            if (clienteEncontrado != null) {
                resultados = List.of(clienteEncontrado);
            }
        } catch (NumberFormatException e) {
            resultados = historialAtencion.buscarPorTipoSolicitud(textoBusqueda);
        }

        mostrarResultadosBusqueda(resultados);
    }

    private void actualizarVistaCompleta() {
        actualizarEstadisticas();
        actualizarClientesEnEspera();
        actualizarClientesAtendidos();
        actualizarRegistroAcciones();
        // [MODIFICACIÓN CLAVE] Actualizar el estado de los botones después de cada acción
        actualizarEstadoBotones();
    }

    private void actualizarEstadisticas() {
        Cliente actual = gestionCliente.clienteActual();
        double promedioSegundos = historialAtencion.calcularPromedioTiempoAtencion();
        String tiempoFormateado = String.format("%.2f s", promedioSegundos);

        vista.getLblClienteActual().setText("Cliente Actual: " + (actual != null ? actual.getNombre() + " (ID: " + actual.getId() + ")" : "Ninguno"));
        vista.getLblTotalEspera().setText("Total Clientes en Espera: " + gestionCliente.totalClientesEnEspera());
        vista.getLblTotalAtendidos().setText("Total Clientes Atendidos: " + historialAtencion.getHistorial().size());
        vista.getLblPromedioTiempoAtencion().setText("Promedio Atención: " + tiempoFormateado);
    }

    private void actualizarClientesEnEspera() {
        DefaultTableModel model = (DefaultTableModel) vista.getTblEspera().getModel();
        model.setRowCount(0);

        List<Cliente> espera = gestionCliente.getClientesEnEspera();
        for (Cliente c : espera) {
            model.addRow(new Object[]{
                    c.getId(),
                    c.getNombre(),
                    c.getTipoSolicitud(),
                    c.getPrioridad()
            });
        }
    }

    private void actualizarClientesAtendidos() {
        DefaultTableModel model = (DefaultTableModel) vista.getTblAtendidos().getModel();
        model.setRowCount(0);

        List<Cliente> atendidos = historialAtencion.getHistorial();
        for (Cliente c : atendidos) {
            model.addRow(new Object[]{
                    c.getId(),
                    c.getNombre(),
                    c.getTipoSolicitud(),
                    c.getPrioridad()
            });
        }
    }

    private void mostrarResultadosBusqueda(List<Cliente> lista) {
        DefaultTableModel model = (DefaultTableModel) vista.getTblBusqueda().getModel();
        model.setRowCount(0);

        if (lista == null || lista.isEmpty()) {
            JOptionPane.showMessageDialog(vista, "No se encontraron resultados para la búsqueda.", "Búsqueda", JOptionPane.INFORMATION_MESSAGE);
            return;
        }

        for (Cliente c : lista) {
            model.addRow(new Object[]{
                    c.getId(),
                    c.getNombre(),
                    c.getTipoSolicitud(),
                    c.getPrioridad()
            });
        }
    }

    private void actualizarRegistroAcciones() {
        DefaultTableModel model = (DefaultTableModel) vista.getTblAcciones().getModel();
        model.setRowCount(0);

        List<RegistroAcciones.Accion> acciones = registroAcciones.getHistorialCompleto();

        for (int i = acciones.size() - 1; i >= 0; i--) {
            model.addRow(new Object[]{
                    acciones.get(i).getFechaHora().format(java.time.format.DateTimeFormatter.ofPattern("dd/MM HH:mm:ss")),
                    acciones.get(i).getTipoAccion().toUpperCase(),
                    acciones.get(i).getCliente().getId(),
                    acciones.get(i).getCliente().getNombre()
            });
        }
    }

    // Se elimina el método limpiarCampos() ya que no se utiliza con JOptionPane.
}