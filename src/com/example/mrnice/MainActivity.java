package com.example.mrnice;

import java.util.Date;

import com.example.mrnice.model.TypeOfDay;

import android.os.Bundle;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.Toast;

public class MainActivity extends Activity {


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

		
		gridview.setOnItemClickListener(new OnItemClickListener() {
			public void onItemClick(AdapterView<?> parent, View v, int position, long id){
				Toast.makeText(MainActivity.this, "grid " + (position + 1) + " was clicked! ", Toast.LENGTH_SHORT).show();
				Intent setting = new Intent(MainActivity.this, PeopleList.class);
				startActivity(setting);
			}
		});
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

}
