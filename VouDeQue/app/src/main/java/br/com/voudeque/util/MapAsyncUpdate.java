package br.com.voudeque.util;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.LatLng;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import br.com.voudeque.MapaActivity;
import br.com.voudeque.modelo.Ponto;


public class MapAsyncUpdate extends AsyncTask<String, Void, Void> {

    private final Context context;
    private final GoogleMap map;

    public MapAsyncUpdate(Context context, GoogleMap map) {
        this.context = context;
        this.map = map;
    }

    @Override
    protected Void doInBackground(String... params) {
        try {
            String url = params[0];
            RequestQueue queue = Volley.newRequestQueue(this.context);
            StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            if (response != null) {
                                try {
                                    JSONObject jsonObj = new JSONObject(response);
                                    JSONArray pontos = jsonObj.getJSONArray("pontos");
                                    for (int i = 0; i < pontos.length(); i++) {
                                        JSONObject p = pontos.getJSONObject(i);
                                        Ponto ponto = new Ponto();
                                        ponto.setAbreviatura(p.getString(Ponto.TAG_ABREVIATURA));
                                        ponto.setId(p.getInt(Ponto.TAG_ID));
                                        ponto.setNome(p.getString(Ponto.TAG_NOME));
                                        ponto.setLatitude(p.getDouble(Ponto.TAG_LATITUDE));
                                        ponto.setLongitude(p.getDouble(Ponto.TAG_LONGITUDE));
                                        ((MapaActivity) context).createMarker(ponto);
                                    }
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                        }
                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Log.e("LogApp", error.toString());
                    Toast.makeText(context, error.getLocalizedMessage(), Toast.LENGTH_LONG);
                }
            });
            // Add the request to the RequestQueue.
            queue.add(stringRequest);
        } catch (Exception exc) {
            Log.e("LogApp", exc.getLocalizedMessage());
            Toast.makeText(context, exc.getLocalizedMessage(), Toast.LENGTH_LONG);
        }
        return null;
    }
}
