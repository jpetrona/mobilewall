package com.mobyadvert.money;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import com.mobilewall.app.R;
import com.mobyadvert.money.OffersWallActivity.GetPointTask;
import com.mobyadvert.money.app.MyApplication;
import com.mobyadvert.money.lib.JSONParser;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

public class RewardsActivity extends BaseActivity implements CashBase {

	TextView mTxtPoint;
	int point = 0;
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		MyApplication.getInstance().trackScreenView("RewardsActivity");

		setContentView(R.layout.rewards_activity);
		
		mTxtPoint = (TextView)findViewById(R.id.txtName);
	}
	
	public void rowClick(View view) {
		Intent intent;
		int value_1000 = 1000;
		int value_5000 = 5000;
	    switch(view.getId()) {
		    case R.id.TableRow01:
		    	if (point > 1000) {
		    		intent = new Intent(RewardsActivity.this,WithdrawActivity.class);
		    		intent.putExtra("pay_type", "Amazon Gift");
		    		intent.putExtra("point_value",value_1000);
		    		startActivity(intent);
		    	}
		    	else {
		    		Toast.makeText(this, "You don't have enough point", Toast.LENGTH_SHORT).show();
		    	}
		        break;
		    case R.id.TableRow02:
		    	if (point > 5000) {
		    		intent = new Intent(RewardsActivity.this,WithdrawActivity.class);
		    		intent.putExtra("pay_type", "Amazon Gift");
		    		intent.putExtra("point_value",value_5000);
		    		startActivity(intent);
		    	}else {
		    		Toast.makeText(this, "You don't have enough point", Toast.LENGTH_SHORT).show();
		    	}
		        break;
		    case R.id.TableRow03:
		    	if (point > 1000) {
		    		intent = new Intent(RewardsActivity.this,WithdrawActivity.class);
		    		intent.putExtra("pay_type", "Google Card");
		    		intent.putExtra("point_value",value_1000);
		    		startActivity(intent);
		    	}
		    	else {
		    		Toast.makeText(this, "You don't have enough point", Toast.LENGTH_SHORT).show();
		    	}
		        break;
		    case R.id.TableRow04:
		    	if (point > 5000) {
		    		intent = new Intent(RewardsActivity.this,WithdrawActivity.class);
		    		intent.putExtra("pay_type", "Google Card");
		    		intent.putExtra("point_value",value_5000);
		    		startActivity(intent);
		    	}else {
		    		Toast.makeText(this, "You don't have enough point", Toast.LENGTH_SHORT).show();
		    	}
		        break;
		    case R.id.TableRow05:
		    	if (point > 1000) {
		    		intent = new Intent(RewardsActivity.this,WithdrawActivity.class);
		    		intent.putExtra("pay_type", "Paypal");
		    		intent.putExtra("point_value",value_1000);
		    		startActivity(intent);
		    	}else {
		    		Toast.makeText(this, "You don't have enough point", Toast.LENGTH_SHORT).show();
		    	}
		        break;
		    case R.id.TableRow06:
		    	if (point > 5000) {
		    		intent = new Intent(RewardsActivity.this,WithdrawActivity.class);
		    		intent.putExtra("pay_type", "Paypal");
		    		intent.putExtra("point_value",value_5000);
		    		startActivity(intent);
		    	}else {
		    		Toast.makeText(this, "You don't have enough point", Toast.LENGTH_SHORT).show();
		    	}
		        break;
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
						point = json.getInt("POINT");
					}
				}
			} catch (JSONException e) {
			}
		}
	}

	@Override
	protected void onStart() {
		super.onStart();
	}
	
}
