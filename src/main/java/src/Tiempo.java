package src;

public class Tiempo {
    private double temperatura;
    private double humedad;
    private boolean precipitacionAgua;
    private boolean precipitacionNieve;
    private boolean nublado;

    public Tiempo(double temperatura, double humedad, boolean precipitacionAgua, boolean precipitacionNieve, boolean nublado) {
        this.temperatura = temperatura;
        this.humedad = humedad;
        this.precipitacionAgua = precipitacionAgua;
        this.precipitacionNieve = precipitacionNieve;
        this.nublado = nublado;
    }

    public double getTemperatura() {
        return temperatura;
    }

    public double getHumedad() {
        return humedad;
    }

    public boolean hayPrecipitacion() {
        return precipitacionAgua || precipitacionNieve;
    }

    public boolean hayPrecipitacionAgua() {
        return precipitacionAgua;
    }

    public boolean hayPrecipitacionNieve() {
        return precipitacionNieve;
    }

    public boolean isNublado() {
        return nublado;
    }


}
