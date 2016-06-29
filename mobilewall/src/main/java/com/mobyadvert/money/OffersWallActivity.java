/**
 * 
 */
package com.mobyadvert.money;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

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

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

/**
 * @author nguyenvietthang
 *
 */
public class OffersWallActivity extends BaseActivity implements CashBase {
	TextView mTxtPoint;
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		MyApplication.getInstance().trackScreenView("OffersWallActivity");

		setContentView(R.layout.offers_wall_activity);
		mTxtPoint = (TextView)findViewById(R.id.txtName);
	}
	
	public void rowClick(View view) {
		Intent intent;
	    switch(view.getId()) {
		    case R.id.tableRow1:
		    	intent = new Intent(OffersWallActivity.this, SponsorpayActivity.class);
				startActivity(intent);
		        break;
		    case R.id.tableRow2:
		    	intent = new Intent(OffersWallActivity.this, AarkiActivity.class);
				startActivity(intent);
		        break;
		    case R.id.tableRow3:
		    	intent = new Intent(OffersWallActivity.this, SonicardActivity.class);
				startActivity(intent);
		        break;
		    case R.id.tableRow4:
		    	intent = new Intent(OffersWallActivity.this, RadiumActivity.class);
				startActivity(intent);
		        break;
		    case R.id.tableRow5:
		    	intent = new Intent(OffersWallActivity.this, MatomyActivity.class);
				startActivity(intent);
		        break;
		    case R.id.tableRow6:
		    	intent = new Intent(OffersWallActivity.this, BannerAtivity.class);
				startActivity(intent);
		        break;
		    case R.id.tableRow7:
		    	intent = new Intent(OffersWallActivity.this, TokenActivity.class);
				startActivity(intent);
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

	@Override
	protected void onStart() {
		super.onStart();
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
