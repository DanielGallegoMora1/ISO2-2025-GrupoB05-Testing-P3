package src;

import java.util.List;

public class Recomendacion {
    private List<Actividad> recomendaciones;

    public Recomendacion(List<Actividad> recomendaciones) {
        this.recomendaciones = recomendaciones;
    }

    public List<Actividad> getRecomendaciones() {
        return recomendaciones;
    }

    public void addRecomendacion(Actividad a) {
        this.recomendaciones.add(a);
    }
}
