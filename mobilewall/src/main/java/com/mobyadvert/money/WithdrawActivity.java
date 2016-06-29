package com.mobyadvert.money;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;

import com.mobilewall.app.R;
import com.mobyadvert.money.lib.JSONParser;
import com.sponsorpay.utils.StringUtils;

public class WithdrawActivity extends BaseActivity implements CashBase {
	private final String TAG = "WithdrawActivity";
	private Spinner cardSpinner;

	private Button mBtnWithdraw;
	private Button mBtnRefresh;

	private String cardType = "";
	
	private TextView mWithdrawoPayType;
	private TextView mWithdrawName;
	private TextView mWithdrawAccount;
	private TextView mWithdrawPoint;
	private TextView mWithdrawMessage;
	private TextView mTxtPoint;


	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.withdraw);
		// Check Login
		isNotLogin();

		//cardSpinner = (Spinner) findViewById(R.id.card_spinner);
		mBtnWithdraw = (Button) findViewById(R.id.btn_withdraw);
		mBtnRefresh = (Button) findViewById(R.id.withdraw_refresh);

		mWithdrawName = (TextView) findViewById(R.id.withdraw_name);
		mWithdrawAccount = (TextView) findViewById(R.id.withdraw_account);
		mWithdrawPoint = (TextView) findViewById(R.id.withdraw_point);
		mTxtPoint = (TextView) findViewById(R.id.user_point);
		mWithdrawoPayType = (TextView) findViewById(R.id.txtPayType);

		mWithdrawMessage = (TextView) findViewById(R.id.withdraw_msg);

		//check value
		Bundle extras = getIntent().getExtras();
		if (extras != null) {
			String pay_type = extras.getString("pay_type");
			mWithdrawoPayType.setText(pay_type);
			cardType = pay_type;
		    String point_value = String.valueOf(extras.getInt("point_value"));
		    mWithdrawPoint.setText((String)point_value);
		}

		// Withdraw
		mBtnWithdraw.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				mWithdrawMessage.setText("");

				String name = mWithdrawName.getText().toString().trim();
				String account = mWithdrawAccount.getText().toString().trim();
				String point = mWithdrawPoint.getText().toString().trim();

				if (StringUtils.nullOrEmpty(name)) {
					mWithdrawMessage.setText("Please input name");
				} else if (StringUtils.nullOrEmpty(account)) {
					mWithdrawMessage.setText("Please input account");
				} else if (StringUtils.nullOrEmpty(point)) {
					mWithdrawMessage.setText("Please input point");
				} else if (Integer.valueOf(point) > Integer.valueOf(mTxtPoint.getText().toString().trim())) {
					mWithdrawMessage.setText("Please input point is below your current point");
				} else {
					new WithdrawTask().execute(BASE_URL + "withdraw/create", userFunctions.getUserID(getApplicationContext()), cardType, name, account, point);
				}

			};
		});

		// Update point.
		mBtnRefresh.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				Log.d("URL_POINT", BASE_URL + "users/user_point");
				new GetPointTask().execute(BASE_URL + "users/user_point", userFunctions.getUserID(getApplicationContext()));
			}
		});
	}

	class WithdrawTask extends AsyncTask<String, Void, JSONObject> {

		@Override
		protected JSONObject doInBackground(String... params) {
			JSONParser jsonParser = new JSONParser();
			List<NameValuePair> entity = new ArrayList<NameValuePair>();
			entity.add(new BasicNameValuePair("user_id", params[1]));
			entity.add(new BasicNameValuePair("card_type", params[2]));
			entity.add(new BasicNameValuePair("name", params[3]));
			entity.add(new BasicNameValuePair("account", params[4]));
			entity.add(new BasicNameValuePair("point", params[5]));

			JSONObject json = jsonParser.getJSONFromUrl(params[0], entity);
			return json;
		}

		@Override
		protected void onPostExecute(JSONObject json) {
			super.onPostExecute(json);

			try {
				if (json != null) {
					if (json.getString("SUCCESS") != null) {
						String status = json.getString("SUCCESS");
						if ("1".equalsIgnoreCase(status)) {
							mWithdrawMessage.setText("Cashout successfull!");
							new GetPointTask().execute(BASE_URL + "users/user_point", userFunctions.getUserID(getApplicationContext()));
						} else {
							mWithdrawMessage.setText("Cashout unsuccessfull!");
						}
						
					}
					mBtnWithdraw.setVisibility(4);
				}
			} catch (JSONException e) {
			}
		}
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

	@Override
	protected void onResume() {
		super.onResume();
		if (userFunctions.isUserLoggedIn(getApplicationContext())) {
			Log.d("URL_POINT", BASE_URL + "users/user_point");
			new GetPointTask().execute(BASE_URL + "users/user_point", userFunctions.getUserID(getApplicationContext()));
		}
	}
}
