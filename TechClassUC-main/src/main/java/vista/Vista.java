package vista;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import javax.swing.table.DefaultTableModel;
import javax.swing.plaf.basic.BasicButtonUI;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.net.URL;
import java.util.logging.Logger;

// [NUEVA IMPORTACIÓN]
import javax.swing.ToolTipManager;

public class Vista extends JFrame {

    private final Logger logger = Logger.getLogger(Vista.class.getName());

    // Se eliminan las referencias a los JTextField y JComboBox del formulario
    private JTextField txtBuscar;
    private JButton btnAgregar, btnAtender, btnEliminar, btnDeshacer;
    private JButton btnBuscar;

    private JTable tblEspera, tblAtendidos, tblAcciones, tblBusqueda;

    private JLabel lblClienteActual, lblTotalEspera, lblTotalAtendidos, lblPromedioTiempoAtencion;

    private final Color COLOR_PRIMARIO = new Color(30, 136, 229);
    private final Color COLOR_FONDO = new Color(245, 245, 245);
    private final Font FONT_HEADER = new Font("Segoe UI", Font.BOLD, 14);
    private final Font FONT_TITULO = new Font("Segoe UI", Font.BOLD, 18);

    // Tamaño más grande de botones redondos
    private final int BUTTON_SIZE = 60;
    private final int ICON_SIZE = 28;

    // Nueva constante para el botón de Búsqueda pequeño
    private final int SMALL_BUTTON_SIZE = 40; // Tamaño reducido

    public Vista() {
        setTitle("TechClassUC - Soporte Inteligente");
        setSize(1400, 780);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        getContentPane().setBackground(COLOR_FONDO);

        // [MODIFICACIÓN CLAVE]
        // Establecer el retardo inicial del ToolTip a 1 milisegundo (prácticamente instantáneo)
        ToolTipManager.sharedInstance().setInitialDelay(1);

        initComponentes();
    }

    /**
     * Carga y escala iconos desde /iconos
     */
    private ImageIcon loadIcon(String fileName) {
        URL imageUrl = ClassLoader.getSystemResource("iconos/" + fileName);

        // Se añade un caso especial para cargar la imagen de la lupa que no está en 'iconos/',
        // asumiendo que el archivo 'lupa.png' se encuentra accesible directamente.
        if (fileName.equals("lupa.png")) {
            imageUrl = getClass().getResource("/" + fileName);
        }

        if (imageUrl == null) {
            imageUrl = getClass().getResource("/iconos/" + fileName);
        }

        if (imageUrl == null) {
            logger.warning("Icono no encontrado: " + fileName);
            return new ImageIcon(new BufferedImage(ICON_SIZE, ICON_SIZE, BufferedImage.TYPE_INT_ARGB));
        }

        ImageIcon original = new ImageIcon(imageUrl);
        Image scaled = original.getImage().getScaledInstance(ICON_SIZE, ICON_SIZE, Image.SCALE_SMOOTH);
        return new ImageIcon(scaled);
    }

    private void initComponentes() {
        JPanel panelPrincipal = new JPanel(new BorderLayout(15, 15));
        panelPrincipal.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
        panelPrincipal.setBackground(COLOR_FONDO);

        JPanel panelHeader = new JPanel(new BorderLayout(10, 10));
        panelHeader.setBackground(COLOR_FONDO);

        // Se coloca el panel de acciones a la izquierda (WEST)
        panelHeader.add(crearPanelAcciones(), BorderLayout.WEST);
        // Se mueven las estadísticas al centro (CENTER)
        panelHeader.add(crearPanelEstadisticas(), BorderLayout.CENTER);

        panelPrincipal.add(panelHeader, BorderLayout.NORTH);

        JTabbedPane tabbedPane = new JTabbedPane();
        tabbedPane.setFont(FONT_HEADER);

        tabbedPane.addTab(" Gestión y Monitoreo", crearPanelMonitoreo());
        tabbedPane.addTab(" Registro y Búsqueda", crearPanelRegistroBusqueda());
        panelPrincipal.add(tabbedPane, BorderLayout.CENTER);

        add(panelPrincipal);
    }

    // ===================== BOTONES DE ACCIONES (LADO IZQUIERDO) ==========================

    private JPanel crearPanelAcciones() {
        // Usamos FlowLayout.LEFT para asegurar el orden de izquierda a derecha (Agregar, Atender, Deshacer, Eliminar)
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.LEFT, 25, 5));
        panel.setBackground(Color.WHITE);

        // Colores de los botones según la imagen: AZUL para AGREGAR y NARANJA para ATENDER
        Color azulAgregar = new Color(30, 136, 229); // AZUL (Similar al COLOR_PRIMARIO)
        Color naranjaAtender = new Color(255, 170, 0); // NARANJA/AMARILLO
        Color grisUndo = new Color(180, 180, 180);
        Color rojoEliminar = new Color(229, 57, 53);

        // 1. AGREGAR (Círculo Azul, Icono: agregar-usuario.png)
        btnAgregar = new JButton(loadIcon("agregar-usuario.png"));
        aplicarEstiloBotonRedondo(btnAgregar, azulAgregar, "AGREGAR");
        panel.add(btnAgregar);

        // 2. ATENDER (Círculo Naranja)
        btnAtender = new JButton(loadIcon("correcto.png"));
        aplicarEstiloBotonRedondo(btnAtender, naranjaAtender, "ATENDER");
        panel.add(btnAtender);

        // 3. DESHACER
        btnDeshacer = new JButton(loadIcon("deshacer-flecha.png"));
        aplicarEstiloBotonRedondo(btnDeshacer, grisUndo, "DESHACER");
        panel.add(btnDeshacer);

        // 4. ELIMINAR
        btnEliminar = new JButton(loadIcon("eliminar.png"));
        aplicarEstiloBotonRedondo(btnEliminar, rojoEliminar, "ELIMINAR");
        panel.add(btnEliminar);

        return panel;
    }


    // ===================== ESTILO BOTONES REDONDOS ==========================
    // NOTA: Se ha agregado un parámetro 'size' para hacerlo reutilizable con SMALL_BUTTON_SIZE
    private void aplicarEstiloBotonRedondo(JButton btn, Color bgColor, String tooltipText) {
        // Usa el tamaño grande por defecto (BUTTON_SIZE)
        int size = BUTTON_SIZE;

        // Excepción para el botón de búsqueda
        if (tooltipText.equals("BUSCAR")) {
            size = SMALL_BUTTON_SIZE;
        }

        btn.setPreferredSize(new Dimension(size, size));
        btn.setToolTipText(tooltipText);
        btn.setBackground(bgColor);

        btn.setText(null);
        btn.setFocusPainted(false);
        btn.setBorderPainted(false);
        btn.setOpaque(false);
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));

        btn.setUI(new RoundedButtonUI());
    }

    // La clase RoundedButtonUI se mantiene igual

    private static class RoundedButtonUI extends BasicButtonUI {
        @Override
        public void paint(Graphics g, JComponent c) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

            AbstractButton b = (AbstractButton) c;

            g2.setColor(b.getBackground());
            g2.fillOval(0, 0, c.getWidth(), c.getHeight());

            super.paint(g2, c);
            g2.dispose();
        }

        @Override
        public boolean contains(JComponent c, int x, int y) {
            int radius = c.getWidth() / 2;
            return Math.pow(x - radius, 2) + Math.pow(y - radius, 2) < Math.pow(radius, 2);
        }
    }

    // ===================== ESTADISTICAS ==========================

    private JPanel crearPanelEstadisticas() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.LEFT, 25, 5));
        panel.setBackground(Color.WHITE);
        panel.setBorder(BorderFactory.createMatteBorder(0, 0, 2, 0, new Color(220, 220, 220)));
        // ... (rest of method, no changes) ...
        lblClienteActual = new JLabel("Cliente Actual: Ninguno");
        lblTotalEspera = new JLabel("Total Clientes en Espera: 0");
        lblTotalAtendidos = new JLabel("Total Clientes Atendidos: 0");
        lblPromedioTiempoAtencion = new JLabel("Promedio Atención: 0.00 s");

        Font statFont = new Font("Arial", Font.BOLD, 15);
        lblClienteActual.setFont(statFont);
        lblTotalEspera.setFont(statFont);
        lblTotalAtendidos.setFont(statFont);
        lblPromedioTiempoAtencion.setFont(statFont);

        panel.add(lblClienteActual);
        panel.add(crearSeparadorVertical());
        panel.add(lblTotalEspera);
        panel.add(crearSeparadorVertical());
        panel.add(lblTotalAtendidos);
        panel.add(crearSeparadorVertical());
        panel.add(lblPromedioTiempoAtencion);

        return panel;
    }

    private Component crearSeparadorVertical() {
        JSeparator separator = new JSeparator(SwingConstants.VERTICAL);
        separator.setPreferredSize(new Dimension(2, 25));
        return separator;
    }

    // ===================== MONITOREO ==========================

    private JPanel crearPanelMonitoreo() {
        JPanel panel = new JPanel(new BorderLayout(15, 15));
        panel.setBackground(COLOR_FONDO);

        // Se elimina la adición del formulario

        JPanel panelListas = new JPanel(new GridLayout(1, 2, 15, 0));
        panelListas.setBackground(COLOR_FONDO);

        panelListas.add(crearPanelClientesEnEspera());
        panelListas.add(crearPanelClientesAtendidos());

        // El panelListas ahora ocupa todo el espacio central
        panel.add(panelListas, BorderLayout.CENTER);

        return panel;
    }


    // ===================== TABLAS (Sin cambios) ==========================

    private JPanel crearPanelClientesEnEspera() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(Color.WHITE);
        panel.setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(COLOR_PRIMARIO, 2),
                "CLIENTES EN ESPERA (COLA)",
                TitledBorder.CENTER, TitledBorder.TOP,
                FONT_HEADER, COLOR_PRIMARIO));

        String[] columnas = {"ID", "Nombre", "Tipo Solicitud", "Prioridad"};
        tblEspera = new JTable(new DefaultTableModel(columnas, 0));

        panel.add(new JScrollPane(tblEspera), BorderLayout.CENTER);

        return panel;
    }

    private JPanel crearPanelClientesAtendidos() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(Color.WHITE);
        panel.setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(new Color(255, 170, 0), 2),
                "CLIENTES ATENDIDOS (HISTORIAL)",
                TitledBorder.CENTER, TitledBorder.TOP,
                FONT_HEADER, new Color(255, 170, 0)));

        String[] columnas = {"ID", "Nombre", "Tipo Solicitud", "Prioridad"};
        tblAtendidos = new JTable(new DefaultTableModel(columnas, 0));

        panel.add(new JScrollPane(tblAtendidos), BorderLayout.CENTER);

        return panel;
    }

    // ===================== REGISTRO Y BUSQUEDA (Modificación de botón Buscar) ==========================

    private JPanel crearPanelRegistroBusqueda() {
        JPanel panel = new JPanel(new GridLayout(1, 2, 15, 15));
        panel.setBackground(COLOR_FONDO);

        // registro acciones
        JPanel panelAcc = new JPanel(new BorderLayout());
        panelAcc.setBackground(Color.WHITE);
        panelAcc.setBorder(BorderFactory.createTitledBorder("REGISTRO DE ACCIONES"));

        String[] colAcc = {"Fecha", "Acción", "ID", "Nombre"};
        tblAcciones = new JTable(new DefaultTableModel(colAcc, 0));

        panelAcc.add(new JScrollPane(tblAcciones), BorderLayout.CENTER);

        panel.add(panelAcc);

        // búsqueda
        JPanel panelBusq = new JPanel(new BorderLayout());
        panelBusq.setBackground(Color.WHITE);
        panelBusq.setBorder(BorderFactory.createTitledBorder("BÚSQUEDA EN HISTORIAL"));

        JPanel ctrl = new JPanel(new FlowLayout(FlowLayout.LEFT));
        ctrl.setBackground(Color.WHITE);

        txtBuscar = new JTextField(15);

        // [MODIFICACIÓN PARA ESTILO REDONDO, TAMAÑO PEQUEÑO Y COLOR AZUL CLARO]
        Color colorBuscar = new Color(100, 180, 255); // Azul Claro
        btnBuscar = new JButton(loadIcon("lupa.png"));
        aplicarEstiloBotonRedondo(btnBuscar, colorBuscar, "BUSCAR");
        // El ajuste de tamaño se realiza dentro de aplicarEstiloBotonRedondo usando SMALL_BUTTON_SIZE

        ctrl.add(new JLabel("ID o Tipo:"));
        ctrl.add(txtBuscar);
        ctrl.add(btnBuscar);

        panelBusq.add(ctrl, BorderLayout.NORTH);

        String[] colBusq = {"ID", "Nombre", "Tipo Solicitud", "Prioridad"};
        tblBusqueda = new JTable(new DefaultTableModel(colBusq, 0));

        panelBusq.add(new JScrollPane(tblBusqueda), BorderLayout.CENTER);

        panel.add(panelBusq);

        return panel;
    }

    // ===================== GETTERS ==========================
    public JTextField getTxtId() { return null; }
    public JTextField getTxtNombre() { return null; }
    public JComboBox<String> getCbTipoSolicitud() { return null; }
    public JComboBox<String> getCbPrioridad() { return null; }
    public JButton getBtnAgregar() { return btnAgregar; }
    public JButton getBtnAtender() { return btnAtender; }
    public JButton getBtnEliminar() { return btnEliminar; }
    public JButton getBtnDeshacer() { return btnDeshacer; }
    public JButton getBtnBuscar() { return btnBuscar; }
    public JTextField getTxtBuscar() { return txtBuscar; }
    public JTable getTblEspera() { return tblEspera; }
    public JTable getTblAtendidos() { return tblAtendidos; }
    public JTable getTblAcciones() { return tblAcciones; }
    public JTable getTblBusqueda() { return tblBusqueda; }
    public JLabel getLblClienteActual() { return lblClienteActual; }
    public JLabel getLblTotalEspera() { return lblTotalEspera; }
    public JLabel getLblTotalAtendidos() { return lblTotalAtendidos; }
    public JLabel getLblPromedioTiempoAtencion() { return lblPromedioTiempoAtencion; }
}

