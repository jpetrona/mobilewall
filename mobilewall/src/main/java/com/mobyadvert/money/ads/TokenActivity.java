package com.mobyadvert.money.ads;

import android.content.Intent;
import android.os.Bundle;

import com.mobilewall.app.R;
import com.mobyadvert.money.BaseActivity;
import com.mobyadvert.money.CashBase;
import com.mobyadvert.money.LoginActivity;
import com.woobi.Woobi;

public class TokenActivity extends BaseActivity implements CashBase {

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		if (userFunctions.isUserLoggedIn(getApplicationContext())) {
			setContentView(R.layout.sponsor_layout);
			Woobi.showOffers(TokenActivity.this, "15426", userFunctions.getUserID(getApplicationContext()));
		} else {
			Intent intent = new Intent(this, LoginActivity.class);
			startActivity(intent);
			finish();
		}
	}
}
