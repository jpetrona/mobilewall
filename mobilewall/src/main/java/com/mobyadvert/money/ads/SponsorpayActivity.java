package com.mobyadvert.money.ads;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;

import com.mobyadvert.money.BaseActivity;
import com.mobyadvert.money.CashBase;
import com.mobyadvert.money.LoginActivity;
import com.mobilewall.app.R;
import com.mobyadvert.money.app.MyApplication;
import com.mobyadvert.money.lib.JSONParser;
import com.sponsorpay.SponsorPay;
import com.sponsorpay.publisher.SponsorPayPublisher;

public class SponsorpayActivity extends BaseActivity implements CashBase {

	private String userId;
	private String appId;
	
	private String securityToken;
	private ProgressDialog dialog;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		MyApplication.getInstance().trackScreenView("SponsorpayActivity");
		if (userFunctions.isUserLoggedIn(getApplicationContext())) {
			setContentView(R.layout.sponsor_layout);
			dialog = new ProgressDialog(SponsorpayActivity.this);
			dialog.setMessage("Loading...");
			userId = userFunctions.getUserID(getApplicationContext());
			Log.i("SponsorpayActivity", BASE_URL + "setting/sponsorpay");
			new GetConfig().execute(BASE_URL + "setting/sponsorpay", userId);
		} else {
			Intent intent = new Intent(this, LoginActivity.class);
			startActivity(intent);
			finish();
		}
	}

	@Override
	protected void onResume() {
		super.onResume();
	}

	@Override
	public void onBackPressed() {
		super.onBackPressed();
	}

	class GetConfig extends AsyncTask<String, Void, JSONObject> {

		@Override
		protected JSONObject doInBackground(String... params) {
			JSONParser jsonParser = new JSONParser();
			List<NameValuePair> entity = new ArrayList<NameValuePair>();
			entity.add(new BasicNameValuePair("user_id", params[1]));
			JSONObject json = jsonParser.getJSONFromUrl(params[0], entity);
			return json;
		}

		@Override
		protected void onPostExecute(JSONObject json) {
			super.onPostExecute(json);

			try {
				if (json != null) {
					appId = json.getString("s_app_id");
					securityToken = json.getString("s_security_token");

					Log.i("SponsorpayActivity", appId);
					Log.i("SponsorpayActivity", securityToken);

					dialog.dismiss();

					SponsorPay.start(appId, userId, securityToken, SponsorpayActivity.this);
					Intent offerWallIntent = SponsorPayPublisher.getIntentForOfferWallActivity(getApplicationContext(), true);
					startActivityForResult(offerWallIntent, Integer.valueOf(appId));
				}
			} catch (JSONException e) {
				Log.e("ERROR", e.getMessage() + " " + e.getCause());
			}
		}
	}
}
