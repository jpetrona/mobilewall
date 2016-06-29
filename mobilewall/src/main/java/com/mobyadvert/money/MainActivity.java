package com.mobyadvert.money;

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
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.mobilewall.app.R;
import com.mobyadvert.money.ads.AarkiActivity;
import com.mobyadvert.money.ads.BannerAtivity;
import com.mobyadvert.money.ads.MatomyActivity;
import com.mobyadvert.money.ads.RadiumActivity;
import com.mobyadvert.money.ads.SonicardActivity;
import com.mobyadvert.money.ads.SponsorpayActivity;
import com.mobyadvert.money.ads.TokenActivity;
import com.mobyadvert.money.app.MyApplication;
import com.mobyadvert.money.lib.JSONParser;

public class MainActivity extends BaseActivity implements CashBase {

	private Button mBtnSponsor;
	private Button mBtnSonicard;
	private Button mBtnAarki;
	private Button mBtnRadiumone;
	private Button mBtnMatomy;
	private Button mBtnRefresh;
	private Button mBtnAccount;
	private Button mBtnTokenAds;
	private Button mBtnBanner;

	private Button btnCashout;
	private TextView mTxtPoint;
	private ProgressDialog dialog;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		MyApplication.getInstance().trackScreenView("MainActivity");

		dialog = new ProgressDialog(MainActivity.this);
		dialog.setMessage("Loading...");

		setContentView(R.layout.dashboard);

		// Bind view.
		mBtnSponsor = (Button) findViewById(R.id.btn_spronsor);
		mBtnSonicard = (Button) findViewById(R.id.btn_sonic);
		mBtnAarki = (Button) findViewById(R.id.btn_aarki);
		mBtnRadiumone = (Button) findViewById(R.id.btn_radium);
		mBtnMatomy = (Button) findViewById(R.id.btn_matomy);
		mBtnRefresh = (Button) findViewById(R.id.withdraw_refresh);
		mBtnAccount = (Button) findViewById(R.id.btn_account);
		mBtnBanner = (Button) findViewById(R.id.btn_banner);
		mBtnTokenAds = (Button) findViewById(R.id.btn_tokenads);

		btnCashout = (Button) findViewById(R.id.btn_cashout);
		mTxtPoint = (TextView) findViewById(R.id.txt_points);

		// Bind listener
		mBtnSponsor.setOnClickListener(new Sponsorpay());

		btnCashout.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {

				Intent intent = new Intent(MainActivity.this, WithdrawActivity.class);
				startActivity(intent);
			}
		});

		// Update point.
		mBtnRefresh.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				dialog.show();
				Log.d("URL_POINT", BASE_URL + "users/user_point");
				new GetPointTask().execute(BASE_URL + "users/user_point",
						userFunctions.getUserID(getApplicationContext()));
				dialog.dismiss();
			}
		});

		mBtnAccount.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent intent = new Intent(MainActivity.this, AccountActivity.class);
				startActivity(intent);
			}
		});

		mBtnSonicard.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent intent = new Intent(MainActivity.this, SonicardActivity.class);
				startActivity(intent);
			}
		});

		mBtnMatomy.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent intent = new Intent(MainActivity.this, MatomyActivity.class);
				startActivity(intent);
			}
		});

		mBtnAarki.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent intent = new Intent(MainActivity.this, AarkiActivity.class);
				startActivity(intent);
			}
		});

		mBtnRadiumone.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent intent = new Intent(MainActivity.this, RadiumActivity.class);
				startActivity(intent);
			}
		});

		mBtnBanner.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View arg0) {
				Intent intent = new Intent(MainActivity.this, BannerAtivity.class);
				startActivity(intent);
			}
		});

		// Woobi.init(this);
		mBtnTokenAds.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent intent = new Intent(MainActivity.this, TokenActivity.class);
				startActivity(intent);
			}
		});
	}

	@Override
	protected void onResume() {
		super.onResume();
		if (userFunctions.isUserLoggedIn(getApplicationContext())) {
			Log.d("URL_POINT", BASE_URL + "users/user_point");
			new GetPointTask().execute(BASE_URL + "users/user_point", userFunctions.getUserID(getApplicationContext()));
		}
	}

	@Override
	protected void onStart() {
		super.onStart();
	}

	class Sponsorpay implements View.OnClickListener {

		@Override
		public void onClick(View v) {
			Intent intent = new Intent(MainActivity.this, SponsorpayActivity.class);
			startActivity(intent);
		}

	}

	class Logout implements View.OnClickListener {

		@Override
		public void onClick(View v) {
			userFunctions.logoutUser(getApplicationContext());
			Intent login = new Intent(getApplicationContext(), LoginActivity.class);
			login.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
			startActivity(login);
			finish();
		}

	}

	@Override
	protected void onPause() {
		super.onPause();
	}

	@Override
	public void onBackPressed() {
		super.onBackPressed();
	}

	class GetPointTask extends AsyncTask<String, Void, JSONObject> {

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
					if (json.getString("POINT") != null) {
						mTxtPoint.setText(json.getString("POINT"));
					}
				}
			} catch (JSONException e) {
			}
		}
	}
}
