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
	public static final String LNAME = "lname";
	public static final String AGE = "age";
	
	public String id;
	public String fname;
	public String lname;
	public int age;
	
	public static Person ConsumeObject(JSONObject jo) {
		Person object = null;
		if (jo != null) {
			object = new Person();
			object.id = jo.optString(ID);
			object.fname = jo.optString(FNAME);
			object.lname = jo.optString(LNAME);
			object.age = jo.optInt(AGE);
		}
		return object;
	}
	
	public static ArrayList<Person> ConsumeList(JSONArray ja) {
		ArrayList<Person> objects = new ArrayList<Person>();
		for (int i = 0; i < ja.length(); i++) {
			objects.add(Person.ConsumeObject(ja.optJSONObject(i)));
		}
		return objects;
	}
	
	public String toString() {
		return fname + " " + lname + " - " + age;
	}

}
