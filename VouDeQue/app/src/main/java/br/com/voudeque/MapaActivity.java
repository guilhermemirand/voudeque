package br.com.voudeque;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.location.Location;
import android.os.Bundle;
import android.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import br.com.voudeque.adpter.DetalhesPontoAdpter;
import br.com.voudeque.listener.MapMarkerClickListener;
import br.com.voudeque.modelo.Linha;
import br.com.voudeque.modelo.Ponto;
import br.com.voudeque.util.LocationUtil;
import br.com.voudeque.util.MapAsyncUpdate;


public class MapaActivity extends ActionBarActivity {

    MapView mapView;
    GoogleMap map;
    HashMap<LatLng, Ponto> pontos = new HashMap<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mapa);

        ActionBar ab = getActionBar();
        //ab.setDisplayHomeAsUpEnabled(true);
        //ab.setBackgroundDrawable(new ColorDrawable(Color.parseColor("#191970")));

        // Gets the MapView from the XML layout and creates it
        mapView = (MapView) findViewById(R.id.mapview);
        mapView.onCreate(savedInstanceState);
        mapView.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(GoogleMap googleMap) {
                initializeMap(googleMap);
            }
        });
    }

    private void initializeMap(GoogleMap googleMap) {
        // Gets to GoogleMap from the MapView and does initialization stuff
        map = googleMap;
        map.getUiSettings().setMyLocationButtonEnabled(false);
        map.setMyLocationEnabled(false);
        map.clear();

        // Needs to call MapsInitializer before doing any CameraUpdateFactory calls
        MapsInitializer.initialize(this);

        // Updates the location and zoom of the MapView
        CameraUpdate cameraUpdate = null;
        try {
            Location location = LocationUtil.getInstance().getLocation();
            Log.i("LogApp", location == null ? "Location is NULL" : location.toString());
            if (location != null) {
                cameraUpdate = CameraUpdateFactory.newLatLngZoom(new LatLng(location.getLatitude(), location.getLongitude()), 18);
            } else {
                throw new NullPointerException("Não foi possível encontrar a localização do dispositivo.");
            }
        } catch (Exception exc) {
            Log.e("LogApp", exc.getLocalizedMessage());
            cameraUpdate = CameraUpdateFactory.newLatLngZoom(new LatLng(0, 0), 10);
        }

        ///mapView.getOverlay().clear();
        map.setTrafficEnabled(false);
        map.animateCamera(cameraUpdate);
        map.setOnCameraChangeListener(new GoogleMap.OnCameraChangeListener() {
            @Override
            public void onCameraChange(CameraPosition cameraPosition) {
                loadMarkers(map);
            }
        });
        map.setOnMarkerClickListener(new MapMarkerClickListener(this));
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0
                && findViewById(R.id.scrollViewDetalhesPonto).getVisibility() == View.VISIBLE) {
            findViewById(R.id.scrollViewDetalhesPonto).setVisibility(View.GONE);
            return true;
        } else {
            return super.onKeyDown(keyCode, event);
        }
    }

    private void loadMarkers(GoogleMap map) {
        float zoom = map.getCameraPosition().zoom;
        if (zoom <15) {
            map.clear();
            pontos.clear();
        } else {
            LatLng northeast = this.map.getProjection().getVisibleRegion().latLngBounds.northeast;
            LatLng southwest = this.map.getProjection().getVisibleRegion().latLngBounds.southwest;
            String url = "http://guilhermemsilva.com.br/voudeque/pontos.php?" +
                    "lat_sw=" + northeast.latitude +
                    "&lon_sw=" + northeast.longitude +
                    "&lat_ne=" + southwest.latitude +
                    "&lon_ne=" + southwest.longitude;
            new MapAsyncUpdate(this, map).execute(url);
        }
    }

    public void createMarker(Ponto ponto) {
        LatLng latLng = new LatLng(ponto.getLatitude(), ponto.getLongitude());
        String title = (ponto.getAbreviatura() != null ? ponto.getAbreviatura() + " " : "") + ponto.getNome();
        if (!pontos.containsKey(latLng)) {
            this.map.addMarker(new MarkerOptions().position(latLng)
                    .title(title)
                    .icon(BitmapDescriptorFactory.fromResource(R.drawable.icon_bus)));
            this.pontos.put(latLng, ponto);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu items for use in the action bar
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_activity_actions, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onResume() {
        mapView.onResume();
        super.onResume();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mapView.onDestroy();
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mapView.onLowMemory();
    }

    public void exibirErro(String erro) {
        Toast.makeText(this, erro, Toast.LENGTH_LONG).show();
        Log.e("LogApp", erro);
    }

    public void exibirDetalhesPonto(Ponto ponto, List<Linha> linhasDoPonto) {
        Log.i("LogApp", "Exibindo detalhes");
        ListView listView = (ListView) this.findViewById(R.id.listViewDetalhesPonto);
        TextView header = new TextView(this);

        // Set header to listView
        header.setText((ponto.getAbreviatura() == null ? "" : ponto.getAbreviatura()) + ponto.getNome());
        //listView.addHeaderView(header);

        listView.setAdapter(new DetalhesPontoAdpter(linhasDoPonto, ponto, this));
        this.findViewById(R.id.scrollViewDetalhesPonto).setVisibility(View.VISIBLE);
    }

    public void ocultarDetalhesPonto() {
        findViewById(R.id.scrollViewDetalhesPonto).setVisibility(View.GONE);
    }

    public MapaActivity getActivity() {
        return this;
    }

    public HashMap<LatLng, Ponto> getPontos() {
        return pontos;
    }
}
