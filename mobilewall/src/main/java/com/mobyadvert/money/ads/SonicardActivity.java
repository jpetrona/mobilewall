package com.mobyadvert.money.ads;

import java.util.HashMap;
import java.util.Map;

import android.content.Intent;
import android.os.Bundle;

import com.mobyadvert.money.BaseActivity;
import com.mobyadvert.money.CashBase;
import com.mobyadvert.money.LoginActivity;
import com.mobyadvert.money.app.MyApplication;
import com.supersonicads.sdk.SSAFactory;
import com.supersonicads.sdk.SSAPublisher;
import com.supersonicads.sdk.listeners.OnOfferWallListener;

public class SonicardActivity extends BaseActivity implements CashBase, OnOfferWallListener {

	private SSAPublisher ssaPub;
	private String appKey;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		MyApplication.getInstance().trackScreenView("SonicardActivity");
		if (userFunctions.isUserLoggedIn(getApplicationContext())) {
			appKey = "3177646d"; //Replace your key
			ssaPub = SSAFactory.getPublisherInstance(this);
			Map<String, String> extraParams = new HashMap<String, String>();
			extraParams.put("pageSize", "10");
			ssaPub.showOfferWall(appKey, userFunctions.getUserID(getApplicationContext()), extraParams, this);
		} else {
			Intent intent = new Intent(this, LoginActivity.class);
			startActivity(intent);
			//finish();
		}
	}

	protected void onResume() {
		super.onResume();
		if (ssaPub != null) {
			ssaPub.onResume(this);
		}
	}

	protected void onPause() {
		super.onPause();
		if (ssaPub != null) {
			ssaPub.onPause(this);
		}
	}

	public void onGetOWCreditsFailed(String arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onOWAdClosed() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public boolean onOWAdCredited(int arg0, int arg1, boolean arg2) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void onOWGeneric(String arg0, String arg1) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onOWShowFail(String arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onOWShowSuccess() {
		// TODO Auto-generated method stub
		
	}
}
