package br.com.voudeque.adpter;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

import br.com.voudeque.R;
import br.com.voudeque.modelo.Linha;
import br.com.voudeque.modelo.Ponto;

public class DetalhesPontoAdpter extends BaseAdapter {

    private static LayoutInflater inflater = null;
    private Ponto ponto;
    private List<Linha> linhas;
    Activity activity;

    public DetalhesPontoAdpter(List<Linha> linhas, Ponto ponto, Activity activity) {
        this.linhas = linhas;
        this.ponto = ponto;
        this.activity = activity;
        inflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return linhas.size();
    }

    @Override
    public Object getItem(int position) {
        return linhas.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
        View vi = convertView;

        if (convertView == null) {
            vi = inflater.inflate(R.layout.item_list_detalhes_ponto, null);
            viewHolder = new ViewHolder();
            viewHolder.setLblAbreviatura((TextView) vi.findViewById(R.id.lblAbreviatura));
            viewHolder.setLblNome((TextView) vi.findViewById(R.id.lblNome));
            viewHolder.setLblHorario((TextView) vi.findViewById(R.id.lblHorario));
            vi.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) vi.getTag();
        }

        Linha linha = linhas.get(position);
        viewHolder.getLblAbreviatura().setText(linha.getAbreviatura() == null? "" : linha.getAbreviatura());
        viewHolder.getLblNome().setText(linha.getNome());
        if (linha.getProximaViagem() == null) {
            viewHolder.getLblHorario().setText("Sem horário para hoje.");
        } else {
            viewHolder.getLblHorario().setText(linha.getFormatedProximaViagem("HH:mm"));
        }
        return vi;
    }

    public static class ViewHolder {

        private TextView lblAbreviatura;
        private TextView lblNome;
        private TextView lblHorario;

        public TextView getLblAbreviatura() {
            return lblAbreviatura;
        }

        public void setLblAbreviatura(TextView lblAbreviatura) {
            this.lblAbreviatura = lblAbreviatura;
        }

        public TextView getLblNome() {
            return lblNome;
        }

        public void setLblNome(TextView lblNome) {
            this.lblNome = lblNome;
        }

        public TextView getLblHorario() {
            return lblHorario;
        }

        public void setLblHorario(TextView lblHorario) {
            this.lblHorario = lblHorario;
        }
    }
}
