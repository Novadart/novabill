package com.novadart.novabill.android;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.novadart.novabill.android.authentication.LoginActivity;
import com.novadart.novabill.android.authentication.NovabillAccountAuthenticator;
import com.novadart.novabill.android.authentication.SecurityContextManager;

public class SelectAccountActivity extends Activity {
	
	private enum SelectionType {ACCOUNT, OTHER}
	
	private OnClickListener radioClickListener = new OnClickListener() {
		@Override
		public void onClick(View view) {
			RadioButton radioBtn = (RadioButton)view;
			Bundle options = new Bundle();
			if(radioBtn.getTag().equals(SelectionType.ACCOUNT)){
				options.putCharSequence(LoginActivity.ARG_NAME, radioBtn.getText());
				options.putBoolean(LoginActivity.ARG_IS_NAME_EDITABLE, false);
			}
			options.putBoolean(LoginActivity.ARG_REDIRECT_TO_DISPATCHER_ACTIVITY, true);
			AccountManager.get(SelectAccountActivity.this).addAccount(NovabillAccountAuthenticator.NOVABILL_ACCOUNT_TYPE,
					NovabillAccountAuthenticator.AUTH_TOKEN_TYPE, null, options, SelectAccountActivity.this, null, null);
		}
	};
	
	private void createAndAddRadioBtn(RadioGroup group, String text, Object tag){
		RadioButton radioBtn = new RadioButton(this);
		radioBtn.setText(text);
		radioBtn.setTag(tag);
		radioBtn.setOnClickListener(radioClickListener);
		group.addView(radioBtn);
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(com.novadart.novabill.android.R.layout.activity_select_account);
		RadioGroup accountsGroup = (RadioGroup)findViewById(com.novadart.novabill.android.R.id.accounts_radio_group);
		for(Account account: new SecurityContextManager(this).getNovabillAccounts())
			createAndAddRadioBtn(accountsGroup, account.name, SelectionType.ACCOUNT);
		createAndAddRadioBtn(accountsGroup, getResources().getString(R.string.select_account_other), SelectionType.OTHER);
	}
	
	

}
