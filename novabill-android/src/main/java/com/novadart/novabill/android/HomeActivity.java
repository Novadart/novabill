package com.novadart.novabill.android;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.TextView;

import com.novadart.novabill.android.authentication.SecurityContextManager;

public class HomeActivity extends Activity {

	private SecurityContextManager securityContextManager;
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if(securityContextManager == null)
        	securityContextManager = new SecurityContextManager(this);
        setContentView(com.novadart.novabill.android.R.layout.activity_home);
        ((TextView)findViewById(R.id.email_view)).setText(securityContextManager.getSignInName());
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
	// Inflate the menu; this adds items to the action bar if it is present.
	getMenuInflater().inflate(com.novadart.novabill.android.R.menu.main, menu);
	return true;
    }
    
    public void signOut(View view){
    	securityContextManager.clearSignInName();
    	Intent intent = new Intent(this, DispatcherActivity.class);
    	startActivity(intent);
    	finish();
    }

}

