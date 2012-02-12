package com.nautilusapps.samples;

import java.util.ArrayList;
import org.json.JSONArray;
import org.json.JSONObject;

/*
 * Example Model used to consume JSON response from server
 */
public class Person {
	
	public static final String ID = "id";
	public static final String FNAME = "fname";
	
	private String fname;
	
	public static Person consumeObject(JSONObject jo) {
		Person object = null;
		if (jo != null) {
			object = new Person();
			//...
			object.fname = "Hello";
		}
		return object;
	}
	
	public static ArrayList<Person> consumeList(JSONArray ja) {
		ArrayList<Person> objects = new ArrayList<Person>();
		return objects;
	}
	
	public String toString() {
		return fname;
	}

}
