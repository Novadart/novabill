package com.novadart.novabill.android;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;

public class HomeActivity extends Activity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(com.novadart.novabill.android.R.layout.activity_home);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
	// Inflate the menu; this adds items to the action bar if it is present.
	getMenuInflater().inflate(com.novadart.novabill.android.R.menu.main, menu);
	return true;
    }
    
    public void startService(View view){
    	Intent intent = new Intent(this, ManagerService.class);
        this.startService(intent);
    }

}

