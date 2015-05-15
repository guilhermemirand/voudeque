package br.com.voudeque.modelo;

public class Viagem {

    public final static String TAG_ID = "id_viagem_pk";
    public final static String TAG_BIDIRECIONAL = "bidirecional";
    public final static String TAG_ACESSIBILIDADE = "acessibilidade";
    public final static String TAG_LINHA_FK = "id_linha_fk";

    private int id;
    private int acessibilidade;
    private boolean bidirecional;
    private Linha linha;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public boolean isBidirecional() {
        return bidirecional;
    }

    public void setBidirecional(boolean bidirecional) {
        this.bidirecional = bidirecional;
    }

    public Linha getLinha() {
        return linha;
    }

    public void setLinha(Linha linha) {
        this.linha = linha;
    }

    public int getAcessibilidade() {
        return acessibilidade;
    }

    public void setAcessibilidade(int acessibilidade) {
        this.acessibilidade = acessibilidade;
    }
}
