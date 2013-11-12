package com.novadart.novabill.android.content.provider;

import android.net.Uri.Builder;

public final class UriUtils {
	
	private static Builder baseBuilder(){
		Builder builder = new Builder();
		builder.scheme("content");
		builder.authority(NovabillContract.AUTHORITY);
		return builder;
	}
	
	public static Builder clientsContentUriBuilder(Long userID){
		Builder builder = baseBuilder();
		builder.path(String.format("users/%d/clients", userID));
		return builder;
	}
	
	public static Builder clientIdContentUriBuilder(Long userID, Long clientID){
		Builder builder = clientsContentUriBuilder(userID);
		builder.appendPath(clientID.toString());
		return builder;
	}

}
