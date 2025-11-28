package modelo;

public class EstadisticasSistema {
    private int totalClientesEspera;
    private int totalClientesAtendidos;
    private double promedioTiempoAtencion;

    public EstadisticasSistema() {
        this.totalClientesEspera = 0;
        this.totalClientesAtendidos = 0;
        this.promedioTiempoAtencion = 0.0;
    }

    public int getTotalClientesEspera() {
        return totalClientesEspera;
    }

    public void setTotalClientesEspera(int totalClientesEspera) {
        this.totalClientesEspera = totalClientesEspera;
    }

    public int getTotalClientesAtendidos() {
        return totalClientesAtendidos;
    }

    public void setTotalClientesAtendidos(int totalClientesAtendidos) {
        this.totalClientesAtendidos = totalClientesAtendidos;
    }

    public double getPromedioTiempoAtencion() {
        return promedioTiempoAtencion;
    }

    public void setPromedioTiempoAtencion(double promedioTiempoAtencion) {
        this.promedioTiempoAtencion = promedioTiempoAtencion;
    }
}


