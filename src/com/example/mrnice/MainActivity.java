package com.example.mrnice;

import java.io.IOException;
import java.util.Date;
import java.util.List;

import org.apache.http.client.HttpClient;
import org.apache.http.client.HttpRequestRetryHandler;
import org.apache.http.impl.client.DefaultHttpRequestRetryHandler;
import org.apache.http.conn.params.ConnManagerParams;
import org.apache.http.conn.params.ConnPerRouteBean;
import org.apache.http.conn.scheme.PlainSocketFactory;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.scheme.SchemeRegistry;
import org.apache.http.impl.client.AbstractHttpClient;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.conn.tsccm.ThreadSafeClientConnManager;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.params.HttpProtocolParams;
import org.apache.http.protocol.HttpContext;

import com.example.mrnice.model.TypeOfDay;

import android.os.Bundle;
import android.os.Handler;
import android.os.Handler.Callback;
import android.os.Message;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.Toast;

public class MainActivity extends Activity implements Callback {

	private static final AbstractHttpClient httpClient;
	private static final HttpRequestRetryHandler retryHandler;
	
	private String param = "";
	private List<Greeting> myGreetings;
	

	static {
		SchemeRegistry schemeRegistry = new SchemeRegistry();
		schemeRegistry.register(new Scheme("http", PlainSocketFactory
		.getSocketFactory(), 80));

		HttpParams connManagerParams = new BasicHttpParams();
		ConnManagerParams.setMaxTotalConnections(connManagerParams, 5);
		ConnManagerParams.setMaxConnectionsPerRoute(connManagerParams,
		new ConnPerRouteBean(5));
		ConnManagerParams.setTimeout(connManagerParams, 15 * 1000);

		ThreadSafeClientConnManager cm =
		new ThreadSafeClientConnManager(connManagerParams,
		schemeRegistry);

		HttpParams clientParams = new BasicHttpParams();
		HttpProtocolParams.setUserAgent(clientParams, "Greeting/1.0");
		HttpConnectionParams.setConnectionTimeout(clientParams, 15 * 1000);
		HttpConnectionParams.setSoTimeout(clientParams, 15 * 1000);
		httpClient = new DefaultHttpClient(cm, clientParams);

		retryHandler = new DefaultHttpRequestRetryHandler(5, false) {

		public boolean retryRequest(IOException exception, int executionCount,
				HttpContext context) {
			if (!super.retryRequest(exception, executionCount, context)) {
				Log.d("HTTP retry-handler", "Won't retry");
				return false;
			}
			try {
				Thread.sleep(2000);
			} catch (InterruptedException e) {
			}
			Log.d("HTTP retry-handler", "Retrying request...");
			return true;
		}
	};

		httpClient.setHttpRequestRetryHandler(retryHandler);
		}
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		MrNiceApp app = (MrNiceApp) this.getApplication();
		if(app.getPrefs().getBoolean("init", true)){
			initialTypeTable();
			app.setBooleanValueToSharedPreferences("init",false);
		}
		GridView gridview = (GridView) findViewById(R.id.gridView1);
		gridview.setAdapter(new ImageAdapter(this));
		
		new GetGreetingTask(new Handler(this)).execute();

		
		gridview.setOnItemClickListener(new OnItemClickListener() {
			public void onItemClick(AdapterView<?> parent, View v, int position, long id){
				Toast.makeText(MainActivity.this, "grid " + (position + 1) + " was clicked! ", Toast.LENGTH_SHORT).show();
				Intent setting = new Intent(MainActivity.this, PeopleList.class);
				startActivity(setting);
			}
		});
	}
	
	public static HttpClient getHttpClient() {
		return httpClient;
		}

	public class ImageAdapter extends BaseAdapter{
		private Context context;
		private Integer[] mImageIds = {
				R.drawable.pic1,
				R.drawable.pic2,
				R.drawable.pic3,
				R.drawable.pic4,
				R.drawable.pic5
		};
		
		public ImageAdapter(Context c){
			context = c;
		}
		
		public int getCount() {
			return mImageIds.length;
		}

		public Object getItem(int position) {
			return position;
		}

		public long getItemId(int position) {
			return position;
		}

		public View getView(int position, View convertView, ViewGroup parent) {
			 ImageView imageView;
			 if (convertView == null){
				 imageView = new ImageView(context);
				 imageView.setLayoutParams(new GridView.LayoutParams(125, 125));
				 imageView.setScaleType(ImageView.ScaleType.FIT_CENTER);
			 }else{
				 imageView = (ImageView) convertView;
			 }
			 imageView.setImageResource(mImageIds[position]);
			 return imageView;
		}
		
	}
	
	private void initialTypeTable(){
		Date dt = new Date();
		TypeOfDay td = new TypeOfDay();
		td.setName("Birthday");
		td.setUri("www.aaa.com");
		td.setUpdate_date(dt.getTime());
		
		TypeOfDay td1 = new TypeOfDay();
		td1.setName("Wedding");
		td1.setUri("www.aaa.com");
		td1.setUpdate_date(dt.getTime());
		
		TypeOfDay td2 = new TypeOfDay();
		td2.setName("others");
		td2.setUri("www.aaa.com");
		td2.setUpdate_date(dt.getTime());
		
		DBUtil.addTypeOfDay(this, td);
		DBUtil.addTypeOfDay(this, td1);
		DBUtil.addTypeOfDay(this, td2);
	}

	public boolean handleMessage(Message msg) {
		String jsonContent = msg.getData().getString("text");
		AlertDialog.Builder dialog = new AlertDialog.Builder(this);
		dialog.setTitle("What's new");
		dialog.setMessage(jsonContent);
		dialog.setIcon(android.R.drawable.ic_dialog_info);
		dialog.setPositiveButton(getString(android.R.string.ok),
				new OnClickListener() {
			public void onClick(DialogInterface dialog, int which) {
				dialog.dismiss();
			}
		});
		dialog.show();
		return false;
	}

}
