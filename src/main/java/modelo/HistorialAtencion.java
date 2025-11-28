package modelo;

import java.time.temporal.ChronoUnit;
import java.util.LinkedList;
import java.util.List;

public class HistorialAtencion {

    private final LinkedList<Cliente> historial;

    public HistorialAtencion() {
        historial = new LinkedList<>();
    }

    public void agregarClienteAtendido(Cliente cliente) {
        historial.add(cliente);
    }

    public boolean removerUltimoCliente(Cliente cliente) {
        return historial.remove(cliente);
    }

    public double calcularPromedioTiempoAtencion() {
        long totalSegundos = 0;
        int clientesAtendidosConTiempo = 0;

        for (Cliente c : historial) {
            if (c.getTiempoAtencion() != null && c.getTiempoLlegada() != null) {
                totalSegundos += ChronoUnit.SECONDS.between(c.getTiempoLlegada(), c.getTiempoAtencion());
                clientesAtendidosConTiempo++;
            }
        }

        if (clientesAtendidosConTiempo == 0) {
            return 0.0;
        }

        return (double) totalSegundos / clientesAtendidosConTiempo;
    }

    public List<Cliente> getHistorial() {
        return historial;
    }

    public Cliente buscarPorId(int id) {
        return historial.stream()
                .filter(c -> c.getId() == id)
                .findFirst()
                .orElse(null);
    }

    public List<Cliente> buscarPorTipoSolicitud(String tipoSolicitud) {
        return historial.stream()
                .filter(c -> c.getTipoSolicitud().equalsIgnoreCase(tipoSolicitud))
                .toList();
    }

    public boolean estaVacio() {
        return historial.isEmpty();
    }
}


