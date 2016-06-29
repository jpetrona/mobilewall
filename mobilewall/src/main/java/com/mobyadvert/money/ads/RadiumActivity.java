/**
 * 
 */
package com.mobyadvert.money.ads;

import android.content.Intent;
import android.os.Bundle;

import com.mobilewall.app.R;
import com.mobyadvert.money.BaseActivity;
import com.mobyadvert.money.CashBase;
import com.mobyadvert.money.LoginActivity;
import com.mobyadvert.money.app.MyApplication;
import com.radiumone.emitter.R1Emitter;
import com.radiumone.engage.mediation.R1AdServer;

/**
 * @author NgocND
 * 
 */
public class RadiumActivity extends BaseActivity implements CashBase {
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		MyApplication.getInstance().trackScreenView("RadiumActivity");

		if (userFunctions.isUserLoggedIn(getApplicationContext())) {
			R1Emitter.getInstance().connect(getApplicationContext());
			// setContentView(R.layout.sponsor_layout);
			R1AdServer.getInstance(this).setUserId(userFunctions.getUserID(getApplicationContext()));
			Bundle adUnitIds = new Bundle();
			adUnitIds.putString(R1AdServer.ADAPTER_ENGAGE, userFunctions.getUserID(getApplicationContext()));
			R1AdServer.getInstance(getApplicationContext()).showOfferwall(adUnitIds);
		} else {
			Intent intent = new Intent(this, LoginActivity.class);
			startActivity(intent);
			finish();
		}

	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		if (userFunctions.isUserLoggedIn(getApplicationContext()))
			R1AdServer.getInstance(getApplicationContext()).destroy();
	}

	@Override
	protected void onStart() {
		super.onStart();
		if (userFunctions.isUserLoggedIn(getApplicationContext()))
			R1Emitter.getInstance().onStart(this);
	}

	@Override
	protected void onStop() {
		super.onStop();
		if (userFunctions.isUserLoggedIn(getApplicationContext()))
			R1Emitter.getInstance().onStop(this);
	}
}
