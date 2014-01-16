package com.novadart.novabill.android;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.CursorAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;

import com.novadart.novabill.android.authentication.SecurityContextManager;
import com.novadart.novabill.android.content.provider.DBSchema.ClientTbl;
import com.novadart.novabill.android.content.provider.NovabillContract;
import com.novadart.novabill.android.content.provider.UriUtils;

public class ClientsActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_clients);
		// Show the Up button in the action bar.
		setupActionBar();
		final Long userID = new SecurityContextManager(this).getSignInId();
		final Cursor cursor = getClients(userID);
		ListAdapter adapter = new SimpleCursorAdapter(this, android.R.layout.simple_list_item_1, cursor,
				new String[]{NovabillContract.Clients.NAME}, new int[]{android.R.id.text1}, CursorAdapter.FLAG_REGISTER_CONTENT_OBSERVER);
		ListView clientsListView = (ListView)findViewById(R.id.clients_list);
		clientsListView.setAdapter(adapter);
		clientsListView.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				cursor.moveToPosition(position);
				Long clientID = cursor.getLong(cursor.getColumnIndex(ClientTbl._ID));
				Intent intent = new Intent(ClientsActivity.this, ClientDetailsActivity.class);
				intent.setData(Uri.parse(UriUtils.clientIdContentUriBuilder(userID, clientID).toString()));
				startActivity(intent);
			}
		});
	}

	private Cursor getClients(Long userID){
		String clientsUriSpec = UriUtils.clientsContentUriBuilder(userID).toString();
		return getContentResolver().query(Uri.parse(clientsUriSpec), null, null, null, null);
	}
	
	/**
	 * Set up the {@link android.app.ActionBar}, if the API is available.
	 */
	@TargetApi(Build.VERSION_CODES.HONEYCOMB)
	private void setupActionBar() {
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
			getActionBar().setDisplayHomeAsUpEnabled(true);
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.clients, menu);
		return true;
	}

}
