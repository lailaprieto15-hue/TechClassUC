package app;

import controlador.Controlador;
import vista.Vista;

import javax.swing.SwingUtilities;

public class Main {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            Vista vista = new Vista();
            Controlador controlador = new Controlador(vista);
            vista.setVisible(true);
        });
    }
}

