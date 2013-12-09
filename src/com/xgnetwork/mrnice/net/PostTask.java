package com.xgnetwork.mrnice.net;


import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.json.JSONObject;

import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;

import com.example.mrnice.MainActivity;
import com.example.mrnice.MrNiceConstant;

public class PostTask  extends AsyncTask<Void,  Void, String > {
	private String TAG = "HTTPPOST";
	private String postResponse;
	private Handler handler;

	public PostTask(Handler handler){
		this.handler = handler;
	}
	
	@Override
	protected String doInBackground(Void... params) {
		try {
			HttpPost httppost = new HttpPost(MrNiceConstant.POST_URL);
			//httppost.addHeader("Authorization", "your token"); //认证token
			httppost.addHeader("Content-Type", "application/json");
			httppost.addHeader("User-Agent", "mrnice");
			
			JSONObject obj = new JSONObject();
			obj.put("first_name", "post1");
			obj.put("last_name", "test1");
			obj.put("email", "post1@test.com");
			
			httppost.setEntity(new StringEntity(obj.toString())); 
			
			HttpResponse response = MainActivity.getHttpClient().execute(httppost);
			postResponse = response.getEntity().getContentType().toString();
			int statusCode = response.getStatusLine().getStatusCode();
			if (statusCode != HttpStatus.SC_OK) {
				return  response.getStatusLine().getReasonPhrase();
			}
			return "OK";
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "post end";
	}
	
	@Override
	protected void onPostExecute(String str) {
		Message message = new Message();
		Bundle data = new Bundle();
		if(str == null || postResponse == null){
			data.putString("text", "no response");
		}else{
			data.putString("text", postResponse);
		}
		message.setData(data);
		handler.sendMessage(message);
	}
}
