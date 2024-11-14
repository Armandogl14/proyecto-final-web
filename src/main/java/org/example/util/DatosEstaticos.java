package org.example.util;

public enum DatosEstaticos {
    URL_MONGO("mongodb+srv://ajgl0001:o8xEX97KS8GPGnR0@cluster0.hadgter.mongodb.net/?retryWrites=true&w=majority&appName=Cluster0"),
    DB_NOMBRE("ProyectoFinal");

    private String valor;

    DatosEstaticos(String valor){
            this.valor =  valor;
        }

    public String getValor() {
        return valor;
    }
}
