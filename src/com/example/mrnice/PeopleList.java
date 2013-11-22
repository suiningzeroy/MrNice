package com.example.mrnice;

import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;

public class PeopleList extends Activity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_people_list);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_people_list, menu);
        return true;
    }
}
