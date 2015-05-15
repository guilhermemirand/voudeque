package br.com.voudeque.util;

import android.app.Activity;
import android.content.Context;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;

public class LocationUtil implements LocationListener {

    private static LocationUtil instance;
    public final static int GPS_TIMEOUT = 6000;
    public final static int NETWORK_TIMEOUT = 3000;
    public final static int TOTAL_TIMEOUT = GPS_TIMEOUT + NETWORK_TIMEOUT;
    private boolean requisitandoLocalizacao = false;
    private boolean localizacaoAtualizada = false;
    private Location location = null;

    private LocationUtil() {}

    public static LocationUtil getInstance() {
        if (instance == null) {
            instance = new LocationUtil();
        }
        return instance;
    }

    public Location getLocation() {
        try {
            while(requisitandoLocalizacao) {
                Thread.sleep(100);
            }
        } catch (Exception exc) {
            Log.e("LogApp", exc.getLocalizedMessage());
        }
        return this.location;
    }

    public void setLocation(Location location) {
        this.location = location;
    }

    private LocationListener getLocationListener() {
        return this;
    }

    public void atualizaLocalizacao(Activity activity) {
        try {
            int timeout = LocationUtil.GPS_TIMEOUT;
            LocationManager lm = (LocationManager) activity.getSystemService(Context.LOCATION_SERVICE);
            requisitandoLocalizacao = true;
            lm.requestSingleUpdate(LocationManager.GPS_PROVIDER, getLocationListener(), null);
            while (!localizacaoAtualizada) {
                timeout = timeout - 100;
                if (timeout <= 0) {
                    lm.removeUpdates(this);
                    break;
                }
                Thread.sleep(100);
            }
            Location location = lm.getLastKnownLocation(LocationManager.GPS_PROVIDER);
            Log.i("LogApp", (location == null ? "NULL" : location.toString()));
            if (location == null || !localizacaoAtualizada) {
                timeout = LocationUtil.NETWORK_TIMEOUT;
                requisitandoLocalizacao = true;
                lm.requestSingleUpdate(LocationManager.NETWORK_PROVIDER, getLocationListener(), null);
                while (requisitandoLocalizacao) {
                    timeout = timeout - 100;
                    if (timeout <= 0) {
                        lm.removeUpdates(this);
                        break;
                    }
                    Thread.sleep(100);
                }
                location = lm.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
                Log.i("LogApp", (location == null ? "NULL" : location.toString()));
            }
            setLocation(location);
        } catch (Exception exc) {
            Log.e("LogApp", exc.getLocalizedMessage());
        }
        requisitandoLocalizacao = false;
    }

    @Override
    public void onLocationChanged(Location location) {
        setLocation(location);
        localizacaoAtualizada = true;
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) { }

    @Override
    public void onProviderEnabled(String provider) { }

    @Override
    public void onProviderDisabled(String provider) { }

    public boolean isRequisitandoLocalizacao() {
        return requisitandoLocalizacao;
    }
}
