package modelo;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.LinkedList;
import java.util.List;
import java.util.Stack;

public class RegistroAcciones {

    public static class Accion {
        private final String tipoAccion;
        private final Cliente cliente;
        private final LocalDateTime fechaHora;

        public Accion(String tipoAccion, Cliente cliente, LocalDateTime fechaHora) {
            this.tipoAccion = tipoAccion;
            this.cliente = cliente;
            this.fechaHora = fechaHora;
        }

        public String getTipoAccion() {
            return tipoAccion;
        }

        public Cliente getCliente() {
            return cliente;
        }

        public LocalDateTime getFechaHora() {
            return fechaHora;
        }

        @Override
        public String toString() {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");
            return "[" + fechaHora.format(formatter) + "] Acción: " + tipoAccion + ", Cliente: " + cliente.toString();
        }
    }

    private final List<Accion> historialCompleto;
    private final Stack<Accion> pilaAccionesUndo;

    public RegistroAcciones() {
        historialCompleto = new LinkedList<>();
        pilaAccionesUndo = new Stack<>();
    }

    public void registrarAccion(String tipoAccion, Cliente cliente) {
        Accion newAction = new Accion(tipoAccion, cliente, LocalDateTime.now());
        historialCompleto.add(newAction);

        if (tipoAccion.equals("agregar") || tipoAccion.equals("eliminar") || tipoAccion.equals("atender")) {
            pilaAccionesUndo.push(newAction);
        }
    }

    public Accion popUltimaAccionUndoable() {
        if (!pilaAccionesUndo.isEmpty()) {
            return pilaAccionesUndo.pop();
        }
        return null;
    }

    public List<Accion> getHistorialCompleto() {
        return historialCompleto;
    }

    public boolean hayAccionesUndoable() {
        return !pilaAccionesUndo.isEmpty();
    }
}


