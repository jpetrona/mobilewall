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

import com.aarki.Aarki;
import com.mobyadvert.money.BaseActivity;
import com.mobyadvert.money.CashBase;
import com.mobyadvert.money.LoginActivity;
import com.mobilewall.app.R;
import com.mobyadvert.money.app.MyApplication;
import com.mobyadvert.money.lib.JSONParser;

public class AarkiActivity extends BaseActivity implements CashBase {

	private ProgressDialog dialog;
	private String userId;
	private String CLIENT_SECURITY_KEY = "";
	private String PLACEMENT_ID = "";
	private final String TAG = "AarkiActivity";

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		MyApplication.getInstance().trackScreenView("AarkiActivity");
		if (userFunctions.isUserLoggedIn(getApplicationContext())) {
			setContentView(R.layout.sponsor_layout);
			dialog = new ProgressDialog(AarkiActivity.this);
			dialog.setMessage("Loading...");
			userId = userFunctions.getUserID(getApplicationContext());
		} else {
			Intent intent = new Intent(this, LoginActivity.class);
			startActivity(intent);
			finish();
		}
	}

	@Override
	protected void onResume() {
		super.onResume();
		new GetConfig().execute(BASE_URL + "setting/sponsorpay", userId);
	}

	@Override
	public void onBackPressed() {
		super.onBackPressed();
		this.finish();
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
					CLIENT_SECURITY_KEY = json.getString("client_security_key");
					PLACEMENT_ID = json.getString("placement_id");
					Log.i(TAG, CLIENT_SECURITY_KEY);
					Log.i(TAG, PLACEMENT_ID);

					dialog.dismiss();
					Aarki.registerApp(AarkiActivity.this, CLIENT_SECURITY_KEY, userId);

					Aarki.showAds(AarkiActivity.this, PLACEMENT_ID, new Aarki.AarkiListener() {
						@Override
						public void onFinished(final Aarki.Status status) {
							if (status == Aarki.Status.OK) {
								Log.i(TAG, "Ad shown");
							} else if (status == Aarki.Status.AppNotRegistered) {
								Log.i(TAG, "This app was not registered in Aarki. Call registerApp to register in Aarki.");
							}
						}
					});
				}
			} catch (JSONException e) {
				Log.e("ERROR", e.getMessage() + " " + e.getCause());
			}
		}
	}
}
