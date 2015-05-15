package br.com.voudeque.listener;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.RequestFuture;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import br.com.voudeque.MapaActivity;
import br.com.voudeque.R;
import br.com.voudeque.adpter.DetalhesPontoAdpter;
import br.com.voudeque.modelo.Linha;
import br.com.voudeque.modelo.Ponto;

public class MapMarkerClickListener implements GoogleMap.OnMarkerClickListener {

    private final MapaActivity activity;
    private Ponto ponto;

    public MapMarkerClickListener(MapaActivity activity) {
        this.activity = activity;
    }

    @Override
    public boolean onMarkerClick(Marker marker) {
        boolean retorno = false;
        try {
            LatLng latLng = marker.getPosition();
            if (activity.getPontos().containsKey(latLng)) {
                ponto = activity.getPontos().get(latLng);
                String url = "http://guilhermemsilva.com.br/voudeque/linha_by_ponto.php?ponto_id=" + ponto.getId();
                new ExibirDetalhesPontoAsync(activity, ponto).execute(url);
            }
        } catch (Exception exc) {
            activity.exibirErro(MapMarkerClickListener.class.toString() + ": " + exc.getStackTrace().toString());
        }
        return retorno;
    }

    public static class ExibirDetalhesPontoAsync extends AsyncTask<String, Void, Void> {

        private final MapaActivity activity;
        private Ponto ponto;

        public ExibirDetalhesPontoAsync(MapaActivity activity, Ponto ponto) {
            this.activity = activity;
            this.ponto = ponto;
        }

        @Override
        protected Void doInBackground(String... params) {
            try {
                String url = params[0];
                RequestQueue queue = Volley.newRequestQueue(this.activity);
                StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                        new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {
                                if (response != null) {
                                    try {
                                        List<Linha> linhasDoPonto = new ArrayList<Linha>();
                                        JSONObject jsonObj = new JSONObject(response);
                                        if (!jsonObj.isNull("linhas")) {
                                            JSONArray jLinhas = jsonObj.getJSONArray("linhas");
                                            for (int i = 0; i < jLinhas.length(); i++) {
                                                JSONObject l = jLinhas.getJSONObject(i);
                                                Linha linha = new Linha();
                                                linha.setAbreviatura(l.getString(Linha.TAG_ABREVIATURA));
                                                linha.setId(l.getInt(Linha.TAG_ID));
                                                linha.setIdTipoTranporte(l.getInt(Linha.TAG_ID_TIPO_TRANSPORTE));
                                                linha.setNome(l.getString(Linha.TAG_NOME));
                                                linha.setProximaViagem(l.getString(Linha.TAG_PROXIMA_VIAGEM));
                                                linhasDoPonto.add(linha);
                                            }
                                            Log.i("LogApp", "Ponto: " + ponto.getId());
                                            activity.exibirDetalhesPonto(ponto, linhasDoPonto);
                                        } else {
                                            activity.exibirErro("Não há linhas para este ponto.");
                                            activity.ocultarDetalhesPonto();
                                        }
                                    } catch (JSONException e) {
                                        Log.e("LogApp", "Erro requisição: " + e.getLocalizedMessage());
                                    }
                                }
                            }
                        }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e("LogApp", error.toString());
                    }
                });
                queue.add(stringRequest);
            } catch (Exception exc) {
                Log.e("LogApp", exc.getLocalizedMessage());
            }
            return null;
        }
    }
}
