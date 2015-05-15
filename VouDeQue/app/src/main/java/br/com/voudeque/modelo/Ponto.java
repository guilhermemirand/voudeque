package br.com.voudeque.modelo;

public class Ponto {

    public final static String TAG_ID = "id_ponto_pk";
    public final static String TAG_ABREVIATURA = "abreviatura";
    public final static String TAG_NOME = "nome";
    public final static String TAG_LATITUDE = "latitude";
    public final static String TAG_LONGITUDE = "longitude";

    private int id;
    private String abreviatura;
    private String nome;
    private Double latitude;
    private Double longitude;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getAbreviatura() {
        return abreviatura;
    }

    public void setAbreviatura(String abreviatura) {
        if (abreviatura.toUpperCase().trim().equals("NULL")) {
            this.abreviatura = null;
        } else {
            this.abreviatura = abreviatura;
        }
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public Double getLatitude() {
        return latitude;
    }

    public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }

    public Double getLongitude() {
        return longitude;
    }

    public void setLongitude(Double longitude) {
        this.longitude = longitude;
    }

    @Override
    public String toString() {
        return this.getNome();
    }
}
