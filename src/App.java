package src;

import java.util.List;
import java.util.Map;
import java.util.ArrayList;

public class App {
    public static void main(String[] args) {
        Persona persona = new Persona(true, true);
        Tiempo tiempo = new Tiempo(25, 50, false, false, true);
        Map<TipoActividad, Actividad> actividades = Map.of(
            TipoActividad.ESQUI, new Actividad(TipoActividad.ESQUI, true),
            TipoActividad.SENDERISMO, new Actividad(TipoActividad.SENDERISMO, false),
            TipoActividad.ESCALADA, new Actividad(TipoActividad.ESCALADA, true),
            TipoActividad.CATALOGO_PRIMAVERA_VERANO_OTONO, new Actividad(TipoActividad.CATALOGO_PRIMAVERA_VERANO_OTONO, false),
            TipoActividad.CULTURAL, new Actividad(TipoActividad.CULTURAL, true),
            TipoActividad.GASTRONOMICO, new Actividad(TipoActividad.GASTRONOMICO, false),
            TipoActividad.PLAYA, new Actividad(TipoActividad.PLAYA, true),
            TipoActividad.PISCINA, new Actividad(TipoActividad.PISCINA, false),
            TipoActividad.QUEDARSE_EN_CASA, new Actividad(TipoActividad.QUEDARSE_EN_CASA, false)
        );
        try {
            Recomendacion recomendacion = recomendar(persona, tiempo, actividades);
            System.out.println("Recomendaciones de actividades:");
            for (Actividad actividad : recomendacion.getRecomendaciones()) {
                System.out.println("- " + actividad.getTipo());
            }
        } catch (NingunaActividadException | AforoSuperadoException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    public static Recomendacion recomendar(Persona p, Tiempo t, Map<TipoActividad, Actividad> actividades) throws NingunaActividadException, AforoSuperadoException {
        List<Actividad> recomendaciones = new ArrayList<>();
        Recomendacion r = new Recomendacion(recomendaciones);
        if(p.puedeRealizarActividadFisica() == false){
            if(t.getTemperatura() < 0 && t.getHumedad() < 15 && (t.hayPrecipitacion())){
                Actividad a = actividades.get(TipoActividad.QUEDARSE_EN_CASA);
                r.addRecomendacion(a);
            }
            if(t.getTemperatura() < 0 && t.getHumedad() < 15 && !(t.hayPrecipitacion())){
                Actividad a = actividades.get(TipoActividad.ESQUI);
                comprobarAforo(a);
                r.addRecomendacion(a);
            }
        
            if(t.getTemperatura() >= 0 && t.getTemperatura() <= 25 && !t.hayPrecipitacionAgua()){
                Actividad a = actividades.get(TipoActividad.SENDERISMO);
                Actividad b = actividades.get(TipoActividad.ESCALADA);
                comprobarAforo(a);
                r.addRecomendacion(a);
                comprobarAforo(b);
                r.addRecomendacion(b);
            }
        
            if(t.getTemperatura() > 15 && t.getTemperatura() <= 25 && !t.hayPrecipitacionAgua() && t.isNublado() && t.getHumedad() <= 60){
                Actividad a = actividades.get(TipoActividad.CATALOGO_PRIMAVERA_VERANO_OTONO);
                comprobarAforo(a);
                r.addRecomendacion(a);
            }
        
            if(t.getTemperatura() > 25 && t.getTemperatura() <= 35 && !t.hayPrecipitacionAgua()){
                Actividad a = actividades.get(TipoActividad.GASTRONOMICO);
                Actividad b = actividades.get(TipoActividad.CULTURAL);
                comprobarAforo(a);
                r.addRecomendacion(a);
                comprobarAforo(b);
                r.addRecomendacion(b);
            }
        
            if(t.getTemperatura() > 30 && !t.hayPrecipitacionAgua()){
                Actividad a = actividades.get(TipoActividad.PLAYA);
                Actividad b = actividades.get(TipoActividad.PISCINA);
                r.addRecomendacion(a);
                comprobarAforo(b);
                r.addRecomendacion(b);
            }
        } else {
            throw new NingunaActividadException("La persona no puede realizar actividad física.");
        }
        return r;
    }

    public static void comprobarAforo(Actividad a) throws AforoSuperadoException {
        if (a.isAforoSuperado()) {
            throw new AforoSuperadoException("Aforo de la actividad " + a.getTipo() + " superado.");
        }
    }

}