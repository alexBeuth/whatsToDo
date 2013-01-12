package com.whatstodo.utils;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

import android.location.Address;
import android.location.Geocoder;
import android.location.Location;

import com.whatstodo.WhatsToDo;

public class LocationUtils {

	public static String[] addressFromLocation(Location location) {

		List<Address> addresses = null;
		Geocoder geocoder = new Geocoder(WhatsToDo.getContext(), Locale.GERMANY);
		try {
			addresses = geocoder.getFromLocation(location.getLatitude(),
					location.getLongitude(), 5);
			
		} catch (IOException e) {
			String[] ret = {"Es konnte keine Adresse gefunden werden."};
			return ret;
		}
		
		String[] addressStrings = new String[addresses.size()];
		int i = 0;
		for(Address address : addresses) {
			addressStrings[i++] = fromAddress(address);
		}
		return addressStrings;
	}

	public static String fromAddress(Address address) {

		StringBuilder builder = new StringBuilder();
		builder.append(address.getAddressLine(0)).append(", ")
				.append(address.getAddressLine(1));
		return builder.toString();
	}

}
