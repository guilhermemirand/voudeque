package br.com.voudeque.modelo;

import java.text.SimpleDateFormat;
import java.util.Date;

public class Linha {

    public final static String TAG_ID = "id_linha_pk";
    public final static String TAG_GTFS_ROUTE_ID = "gtfs_route_id";
    public final static String TAG_ABREVIATURA = "abreviatura";
    public final static String TAG_NOME = "nome";
    public final static String TAG_ID_AGENCIA = "id_agencia_fk";
    public final static String TAG_ID_TIPO_TRANSPORTE = "id_tipo_transporte_fk";
    public final static String TAG_PROXIMA_VIAGEM = "proxima_viagem";

    private Integer id;
    private Integer idAgencia;
    private Integer idTipoTranporte;
    private String abreviatura;
    private String nome;
    private String gtfsRouteId;
    private Date proximaViagem;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getIdAgencia() {
        return idAgencia;
    }

    public void setIdAgencia(Integer idAgencia) {
        this.idAgencia = idAgencia;
    }

    public Integer getIdTipoTranporte() {
        return idTipoTranporte;
    }

    public void setIdTipoTranporte(Integer idTipoTranporte) {
        this.idTipoTranporte = idTipoTranporte;
    }

    public String getAbreviatura() {
        return abreviatura;
    }

    public void setAbreviatura(String abreviatura) {
        this.abreviatura = abreviatura;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getGtfsRouteId() {
        return gtfsRouteId;
    }

    public void setGtfsRouteId(String gtfsRouteId) {
        this.gtfsRouteId = gtfsRouteId;
    }

    public Date getProximaViagem() {
        return proximaViagem;
    }

    public String getFormatedProximaViagem(String format) {
        if (this.getProximaViagem() == null) {
            return null;
        }
        return new SimpleDateFormat(format).format(this.getProximaViagem());
    }

    public void setProximaViagem(Date proximaViagem) {
        this.proximaViagem = proximaViagem;
    }

    public void setProximaViagem(String proximaViagem) {
        if (proximaViagem != null) {
            try {
                this.proximaViagem = new SimpleDateFormat("HH:mm:ss").parse(proximaViagem);
            } catch (Exception exc) {
                this.proximaViagem = null;
            }
        }
    }

    @Override
    public String toString() {
        return (this.getAbreviatura() == null ? "" : this.getAbreviatura()) + this.getNome();
    }
}
