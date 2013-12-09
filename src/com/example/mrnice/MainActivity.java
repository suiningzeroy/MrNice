package com.example.mrnice;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
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

import com.example.mrnice.SpecialDayList.SpecialDayAdapter;
import com.example.mrnice.model.Greeting;
import com.example.mrnice.model.SpecialDay;
import com.example.mrnice.model.TypeOfDay;
import com.xgnetwork.mrnice.net.GetGreetingTask;
import com.xgnetwork.mrnice.net.PostTask;

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
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends Activity implements Callback {
	private static final int ALLDAY = 0,UPDATE = 1,POST = 2,PEOPLE = 3;
	private static final AbstractHttpClient httpClient;
	private static final HttpRequestRetryHandler retryHandler;
	private Context mContext;
	private String param = "";
	private List<Greeting> myGreetings;
	private List<SpecialDay> closeDayList;
	private Calendar now ;
	private String today;
	private ListView closeList;
	private TextView helloText;

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
		HttpProtocolParams.setUserAgent(clientParams, "MrNice/1.0");
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
		mContext = this;
		now = 	Calendar.getInstance();
		today = String.valueOf(now.get(Calendar.YEAR)) + String.valueOf(now.get(Calendar.MONTH)+1) + String.valueOf(now.get(Calendar.DAY_OF_MONTH));

		MrNiceApp app = (MrNiceApp) this.getApplication();
		if(app.getPrefs().getBoolean("init", true)){
			initialTypeTable();
			app.setBooleanValueToSharedPreferences("init",false);
		}
		GridView gridview = (GridView) findViewById(R.id.gridView1);
		gridview.setAdapter(new ImageAdapter(this));
		closeList = (ListView)findViewById(R.id.littledaylistview);

		helloText = (TextView) findViewById(R.id.textView1);
		
		gridview.setOnItemClickListener(new OnItemClickListener() {
			public void onItemClick(AdapterView<?> parent, View v, int position, long id){
				switch(position){
					case ALLDAY:
						Intent days = new Intent(MainActivity.this, SpecialDayList.class);
						startActivity(days);
						break;
					case UPDATE:
						new GetGreetingTask(new Handler(MainActivity.this)).execute();
						break;
					case POST:
						new PostTask(new Handler(MainActivity.this)).execute();
						break;
					case PEOPLE:
						Intent setting = new Intent(MainActivity.this, PeopleList.class);
						startActivity(setting);
						break;
					default:
						Toast.makeText(MainActivity.this, "grid " + (position + 1) + " was clicked! ", Toast.LENGTH_SHORT).show();
				}
			}
		});
	}
	
	@Override
	public void onResume(){
		closeDayList = getSpecialDaysForAdapter();
		Collections.sort(closeDayList);
		final CloseSpecialDayAdapter dayAdapter = new CloseSpecialDayAdapter(closeDayList);
		closeList.setAdapter(dayAdapter);
		
		closeList.setOnItemClickListener(new OnItemClickListener(){

			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				// TODO Auto-generated method stub
				Intent guide = new Intent(MainActivity.this, SpecialDayNotificationGuideActivity.class);
				guide.putExtra(MrNiceConstant.DAY_ID, closeDayList.get(arg2).get_id());
				startActivity(guide);
			}
			
		});
		
		helloText.setText("Hello, Today is " + today + "." + "There are " + String.valueOf(closeDayList.size()) + " Special days approaching!");
		super.onResume();
	}
	
	public static HttpClient getHttpClient() {
		return httpClient;
	}
	
	private List<SpecialDay> getSpecialDaysForAdapter(){
		
		Calendar now = 	Calendar.getInstance();
		String today = String.valueOf(now.get(Calendar.YEAR)) + String.valueOf(now.get(Calendar.MONTH)+1) + String.valueOf(now.get(Calendar.DAY_OF_MONTH));
		List<SpecialDay> dayList = DBUtil.getAllSpecialDays(mContext);
		List<SpecialDay> closeList = new ArrayList<SpecialDay>(dayList);
		
		for(SpecialDay spd: dayList){
			long days = DBUtil.GetNumberOfDaysBetweenToDateStrings(today, DBUtil.getAlarmDateBaseOnSpecialDay(spd));
			if( days > (long)7 ){
				closeList.remove(spd);
			}
		}
		
		return closeList;
	}

	public class ImageAdapter extends BaseAdapter{
		private Context context;
		private Integer[] mImageIds = {
				R.drawable.allday,
				R.drawable.update,
				R.drawable.post,
				R.drawable.people,
				R.drawable.heart,
				R.drawable.heart
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
				// imageView.setLayoutParams(new GridView.LayoutParams(85, 85));
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
	
	private class CloseSpecialDayAdapter extends ArrayAdapter<SpecialDay> {
		
		
		
		public CloseSpecialDayAdapter(List<SpecialDay> dayList) {
			super(mContext, R.layout.day_list_item, dayList);
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
	
			if (convertView == null) {
				LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
				convertView = inflater.inflate(R.layout.close_day_list_item, parent, false);
			}
			TextView name = (TextView) convertView.findViewById(R.id.name);
			TextView date = (TextView) convertView.findViewById(R.id.date);
			TextView type = (TextView) convertView.findViewById(R.id.type);
			TextView howmuchtime = (TextView) convertView.findViewById(R.id.howmuchtime);
			SpecialDay day = getItem(position);
	
			if (day != null) {
				name.setText(DBUtil.getPeopleNameById(mContext, day.getPeople_id()));
				date.setText(getNewFormateDateString(DBUtil.getAlarmDateBaseOnSpecialDay(day)));
				type.setText(DBUtil.getSpecialDayType(mContext, day.getTypeId()));
				long days = DBUtil.GetNumberOfDaysBetweenToDateStrings(today, DBUtil.getAlarmDateBaseOnSpecialDay(day));
				howmuchtime.setText(getHowMuchTimeMsg(days));
			}	
			return convertView;
		}	
		
		private String getHowMuchTimeMsg(long days){
			String dayString = String.valueOf(days);
			String msg = dayString + "days left ";
			return msg;
		}
		
		private String getNewFormateDateString(String date){
			//String msg = date.substring(0, 4)+ " - " + date.substring(4,6) + " - " + date.substring(6);
			return date;
		}
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
			DBUtil.toastShow(mContext, "KEYCODE_BACK be clicked");
		}
		return super.onKeyDown(keyCode, event);
	}
	
}
