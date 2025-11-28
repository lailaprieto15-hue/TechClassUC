package modelo;

import java.time.LocalDateTime;

public class Cliente {
    private int id;
    private String nombre;
    private String tipoSolicitud;
    private String prioridad;
    private LocalDateTime tiempoLlegada;
    private LocalDateTime tiempoAtencion;

    public Cliente(int id, String nombre, String tipoSolicitud, String prioridad) {
        this.id = id;
        this.nombre = nombre;
        this.tipoSolicitud = tipoSolicitud;
        this.prioridad = prioridad;
        this.tiempoLlegada = LocalDateTime.now();
        this.tiempoAtencion = null;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getTipoSolicitud() {
        return tipoSolicitud;
    }

    public void setTipoSolicitud(String tipoSolicitud) {
        this.tipoSolicitud = tipoSolicitud;
    }

    public String getPrioridad() {
        return prioridad;
    }

    public void setPrioridad(String prioridad) {
        this.prioridad = prioridad;
    }

    public LocalDateTime getTiempoLlegada() {
        return tiempoLlegada;
    }

    public void setTiempoLlegada(LocalDateTime tiempoLlegada) {
        this.tiempoLlegada = tiempoLlegada;
    }

    public LocalDateTime getTiempoAtencion() {
        return tiempoAtencion;
    }

    public void setTiempoAtencion(LocalDateTime tiempoAtencion) {
        this.tiempoAtencion = tiempoAtencion;
    }

    @Override
    public String toString() {
        return "Cliente{" +
                "id=" + id +
                ", nombre='" + nombre + '\'' +
                ", tipoSolicitud='" + tipoSolicitud + '\'' +
                ", prioridad='" + prioridad + '\'' +
                '}';
    }
}


