package br.com.voudeque.modelo;

import java.util.Date;

public class ViagemPonto {

    public final static String TAG_ID_VIAGEM = "id_viagem_fk";
    public final static String TAG_ID_PONTO = "id_ponto_fk";
    public final static String TAG_HORARIO_CHEGADA = "horario_chegada";
    public final static String TAG_HORARIO_PARTIDA = "horario_partida";
    public final static String TAG_SEQUENCIA = "sequencia";

    private int idViagem;
    private int idPonto;
    private Date horarioChegada;
    private Date horarioPartida;
    private int sequencia;

    public int getIdViagem() {
        return idViagem;
    }

    public void setIdViagem(int idViagem) {
        this.idViagem = idViagem;
    }

    public int getIdPonto() {
        return idPonto;
    }

    public void setIdPonto(int idPonto) {
        this.idPonto = idPonto;
    }

    public Date getHorarioChegada() {
        return horarioChegada;
    }

    public void setHorarioChegada(Date horarioChegada) {
        this.horarioChegada = horarioChegada;
    }

    public Date getHorarioPartida() {
        return horarioPartida;
    }

    public void setHorarioPartida(Date horarioPartida) {
        this.horarioPartida = horarioPartida;
    }

    public int getSequencia() {
        return sequencia;
    }

    public void setSequencia(int sequencia) {
        this.sequencia = sequencia;
    }
}
