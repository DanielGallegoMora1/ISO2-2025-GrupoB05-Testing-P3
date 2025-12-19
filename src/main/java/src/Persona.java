package src;

public class Persona {
    private boolean facultadesFisicas;
    private boolean sintomasEnfermedades;

    public Persona(boolean facultadesFisicas, boolean sintomasEnfermedades) {
        this.facultadesFisicas = facultadesFisicas;
        this.sintomasEnfermedades = sintomasEnfermedades;
    }

    public boolean puedeRealizarActividadFisica() {
        return facultadesFisicas && !sintomasEnfermedades;
    }
}

