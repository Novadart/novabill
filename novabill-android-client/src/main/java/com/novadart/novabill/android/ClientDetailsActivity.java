package com.novadart.novabill.android;

import com.novadart.novabill.android.content.provider.DBSchema.ClientTbl;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.Menu;
import android.widget.TextView;

public class ClientDetailsActivity extends Activity {
	

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_client_details);
		Intent intent = getIntent();
		Cursor cursor = getContentResolver().query(intent.getData(), null, null, null, null);
		cursor.moveToFirst();
		TextView nameView = (TextView)findViewById(R.id.client_details_name);
		nameView.setText("Name: " + cursor.getString(cursor.getColumnIndex(ClientTbl.NAME)));
		TextView vatidView = (TextView)findViewById(R.id.client_details_vatid);
		vatidView.setText("Vat ID: " + cursor.getString(cursor.getColumnIndex(ClientTbl.VAT_ID)));
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.client_details, menu);
		return true;
	}

}
