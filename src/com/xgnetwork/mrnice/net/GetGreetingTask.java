package com.xgnetwork.mrnice.net;

import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.methods.HttpGet;

import com.example.mrnice.MainActivity;
import com.example.mrnice.MrNiceConstant;
import com.example.mrnice.model.Greeting;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;


public class GetGreetingTask extends AsyncTask<Void,  Void, List<Greeting> > {

	private List<Greeting> myGreetings;
	private Handler handler;

	public GetGreetingTask(Handler handler){
		this.handler = handler;
	}
	
	@Override
	protected List<Greeting> doInBackground(Void... params) {
		try {
			HttpGet request = new HttpGet(MrNiceConstant.UPDATE_URL);
			request.setHeader("Accept", "text/html");
			HttpResponse response = MainActivity.getHttpClient().execute(request);
			int statusCode = response.getStatusLine().getStatusCode();
			if (statusCode != HttpStatus.SC_OK) {
				return null;
			}
			
			myGreetings = JsonGreetingParser.parseGreetings(response.getEntity().getContent());
			return myGreetings;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	@Override
	protected void onPostExecute(List<Greeting> greetings) {
		Message message = new Message();
		Bundle data = new Bundle();
		if(greetings == null || myGreetings == null){
			data.putString("text", "no greetings");
		}else{
			data.putString("text", myGreetings.get(0).getTitle() + "." + myGreetings.get(0).getContent());
		}
		message.setData(data);
		handler.sendMessage(message);
	}

}
