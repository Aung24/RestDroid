package com.nautilusapps.samples;

import java.util.ArrayList;
import junit.framework.Assert;
import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import com.nautilusapps.RestDroid.RestConnector;
import com.nautilusapps.RestDroid.RestDroid;
import com.nautilusapps.RestDroid.RestResponse;


public class main extends Activity {
	private static final String TAG = "RestDroid_Sample";

	public static final String SERVER = "http://10.0.2.2:3000"; // localhost:3000
																// on emulator

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);
		
		// Turn on Logging for the RestConnector
		RestConnector.EnableLogging(true);
	}

	public void start(View view) {

		// INDEX - list of people
		test_index();

		// CREATE - create a new person
		Person peep1 = test_create("Bob", "Smith", 42);
		Assert.assertNotNull(peep1);
		test_index(); // show results

		// SHOW - lookup a person record
		Person peep2 = test_show(peep1.id);
		Assert.assertEquals(peep2.fname, peep1.fname);

		// UPDATE - update a person record
		peep2.fname = "Abraham";
		peep2.lname = "Lincoln";
		peep2.age = 201;
		peep2 = test_update(peep2);
		Assert.assertNotNull(peep2);
		test_index(); // show results

		// DELETE - delete a person
		boolean result = test_delete(peep1);
		Assert.assertTrue(result);
		test_index(); // show results
		
	}

	public void test_index() {
		Log.d(TAG, "INDEX");
		RestResponse response = RestDroid.get(SERVER + "/people");

		Log.d(TAG, response.toString());
		if (!response.error && response.statusCode == 200) {
			ArrayList<Person> people = Person.ConsumeList(response.jsonArray);
			for (Person p : people)
				Log.d(TAG, "person: " + p);
		}
	}

	public Person test_create(String fname, String lname, int age) {
		Log.d(TAG, "CREATE");
		String postData = "fname=Barbados&lname=Slim&age=42";
		RestResponse response = RestDroid.post(SERVER + "/people", postData);

		Log.d(TAG, response.toString());
		if (!response.error && response.statusCode == 201) {
			Person person = Person.ConsumeObject(response.jsonArray.optJSONObject(0));
			Log.d(TAG, "Person created: " + person);
			return person;
		}
		return null;
	}

	public Person test_show(String id) {
		Log.d(TAG, "SHOW");
		RestResponse response = RestDroid.get(SERVER + "/people/" + id);
		
		Log.d(TAG, response.toString());
		if (!response.error && response.statusCode == 201) {
			Person person = Person.ConsumeObject(response.jsonArray.optJSONObject(0));
			Log.d(TAG, "Person looked up: " + person);
			return person;
		}
		return null;
	}
	
	public Person test_update(Person person) {
		Log.d(TAG, "UPDATE");
		String postData = "fname=" + person.fname;
		postData += "&lname=" + person.lname;
		postData += "&age=" + person.age;
		RestResponse response = RestDroid.put(SERVER + "/people/" + person.id, postData);
		
		Log.d(TAG, response.toString());
		if (!response.error && response.statusCode == 201) {
			person = Person.ConsumeObject(response.jsonArray.optJSONObject(0));
			Log.d(TAG, "Person updated: " + person);
			return person;
		}
		return null;
	}

	public boolean test_delete(Person person) {
		Log.d(TAG, "DELETE");
		RestResponse response = RestDroid.delete(SERVER + "/people/" + person.id);

		Log.d(TAG, response.toString());
		return (!response.error && response.statusCode == 200);
	}

}
