package com.novadart.novabill.frontend.client.place;

import java.util.HashMap;
import java.util.Map;


public class HistoryUtils {
	
	public static Map<String, String> parseArguments(String url) {
		String trimmed = url.trim();
		Map<String, String> args = new HashMap<String, String>();
		
		if(trimmed.isEmpty()){
			return args;
		}
		
		String[] toks = url.split(";");
		String[] tuple;
		for (String tok : toks) {
			tuple = tok.split("=");
			if(tuple.length > 1){
				args.put(tuple[0], tuple[1]);
			}
		}
		return args;
	}
	
	public static String serialize(String name, Long value){
		return name+"="+value+";";
	}

	public static String serialize(String name, String value){
		return name+"="+value+";";
	}
	
	public static String serialize(Map<String, String> args){
		StringBuilder sb = new StringBuilder();
		for (String name : args.keySet()) {
			sb.append(serialize(name, args.get(name)));
		}
		return sb.toString();
	}
	
}
