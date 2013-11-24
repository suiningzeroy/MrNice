package com.example.mrnice;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

public class JsonGreetingParser {
	public static List<Greeting> parseGreetings(InputStream json) throws Exception {
		BufferedReader reader = new BufferedReader(new InputStreamReader(json));
		StringBuilder sb = new StringBuilder();
		
		List<Greeting> myGreetings = new ArrayList<Greeting>();

		try {
			String line = reader.readLine();
			while (line != null) {
			sb.append(line);
			line = reader.readLine();
			}
		} catch (IOException e) {
			throw e;
		} finally {
			reader.close();
		}
		JSONArray jsonReply = new JSONArray(sb.toString());
		
		Greeting Greeting = new Greeting();
		JSONObject jsonGreeting = jsonReply.getJSONObject(0);
		Greeting.setTitle(jsonGreeting.getString("first_name"));
		Greeting.setContent(jsonGreeting.getString("last_name"));
		myGreetings.add(Greeting);

		return myGreetings;
		}
}
