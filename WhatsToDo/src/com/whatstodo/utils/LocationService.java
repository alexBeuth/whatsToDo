package com.whatstodo.utils;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.IBinder;

public class LocationService extends Service implements LocationListener {

	private static LocationManager locationManager;
	public static Location mostRecentLocation;

	@Override
	public IBinder onBind(Intent intent) {
		return null;
	}

	@Override
	public void onCreate() {
		super.onCreate();

		locationManager = (LocationManager) this
				.getSystemService(Context.LOCATION_SERVICE);

		Criteria criteria = new Criteria();
		criteria.setAccuracy(Criteria.ACCURACY_FINE);
		String providerFine = locationManager.getBestProvider(criteria, true);

		criteria.setAccuracy(Criteria.ACCURACY_COARSE);
		String providerCoarse = locationManager.getBestProvider(criteria, true);
		if (providerFine != null)
			locationManager.requestLocationUpdates(providerFine, 0, 0, this);
		if (providerCoarse != null)
			locationManager.requestLocationUpdates(providerCoarse, 0, 0, this);
		locationManager.requestLocationUpdates(
				LocationManager.NETWORK_PROVIDER, 0, 0, this);
	}
	
	public static Location getLastKnowLocation() {
		return locationManager.getLastKnownLocation(LocationManager.PASSIVE_PROVIDER);
	}

	@Override
	public void onDestroy() {
		locationManager.removeUpdates(this);
		super.onDestroy();

	}

	@Override
	public void onLocationChanged(Location location) {
		if (mostRecentLocation == null
				|| location.getAccuracy() >= mostRecentLocation.getAccuracy()) {
			mostRecentLocation = location;
		}
	}

	@Override
	public void onStatusChanged(String provider, int status, Bundle extras) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onProviderEnabled(String provider) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onProviderDisabled(String provider) {
		// TODO Auto-generated method stub

	}
}
