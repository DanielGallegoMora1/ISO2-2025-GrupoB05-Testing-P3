package src;

enum TipoActividad {
    ESQUI,
    SENDERISMO,
    ESCALADA,
    CATALOGO_PRIMAVERA_VERANO_OTONO,
    CULTURAL,
    GASTRONOMICO,
    PLAYA,
    PISCINA,
    QUEDARSE_EN_CASA,
}

public class Actividad {
    private TipoActividad tipo;
    private boolean aforoSuperado;

    public Actividad(TipoActividad tipo, boolean aforoSuperado) {
        this.tipo = tipo;
        this.aforoSuperado = aforoSuperado;
    }

    public String getTipo() {
        String tipoActividad = "";
        if (this.tipo == TipoActividad.ESQUI) {
            tipoActividad = "ESQUI";
        }
        if (this.tipo == TipoActividad.SENDERISMO) {
            tipoActividad = "SENDERISMO";
        }
        if (this.tipo == TipoActividad.ESCALADA) {
            tipoActividad = "ESCALADA";
        }
        if (this.tipo == TipoActividad.CATALOGO_PRIMAVERA_VERANO_OTONO) {
            tipoActividad = "CATALOGO_PRIMAVERA_VERANO_OTONO";
        }
        if (this.tipo == TipoActividad.CULTURAL) {
            tipoActividad = "CULTURAL";
        }
        if (this.tipo == TipoActividad.GASTRONOMICO) {
            tipoActividad = "GASTRONOMICO";
        }
        if (this.tipo == TipoActividad.PLAYA) {
            tipoActividad = "PLAYA";
        }
        if (this.tipo == TipoActividad.PISCINA) {
            tipoActividad = "PISCINA";
        }
        if (this.tipo == TipoActividad.QUEDARSE_EN_CASA) {
            tipoActividad = "QUEDARSE_EN_CASA";
        }
        return tipoActividad;
    }

    public boolean isAforoSuperado() {
        return this.aforoSuperado;
    }

}
