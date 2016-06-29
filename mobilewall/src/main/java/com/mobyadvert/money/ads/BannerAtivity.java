/**
 * 
 */
package com.mobyadvert.money.ads;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;

import com.mobyadvert.money.BaseActivity;
import com.mobyadvert.money.CashBase;
import com.mobilewall.app.R;
import com.mobyadvert.money.adapter.MobileArrayAdapter;
import com.mobyadvert.money.bean.FreshBean;
import com.mobyadvert.money.lib.JSONParser;

/**
 * 
 * @author NgocND
 * 
 */
public class BannerAtivity extends BaseActivity implements CashBase {

	private static String URL = BASE_URL + "offers/android";

	private ArrayList<FreshBean> freshBeanList;

	private ListView listview;

	private ProgressDialog dialog;

	private Button btnExit;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		isNotLogin();

		setContentView(R.layout.cpa_system);
		listview = (ListView) findViewById(R.id.listView1);
		btnExit = (Button) findViewById(R.id.btn_exit);
		dialog = new ProgressDialog(BannerAtivity.this);
		btnExit.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				finish();
			}
		});
	}

	@Override
	protected void onResume() {
		super.onResume();

		new OfferTask().execute();
	}

	class OfferTask extends AsyncTask<String, Void, JSONArray> {

		@Override
		protected JSONArray doInBackground(String... arg0) {
			dialog.setMessage("Loading...");
			JSONParser jsonParser = new JSONParser();
			List<NameValuePair> entity = new ArrayList<NameValuePair>();
			JSONArray jArray = jsonParser.getJSONArrayFromUrl(URL, entity);
			return jArray;
		}

		@Override
		protected void onPostExecute(JSONArray result) {
			super.onPostExecute(result);
			dialog.dismiss();

			if (result != null) {
				freshBeanList = new ArrayList<FreshBean>();
				FreshBean freshBean = null;
				for (int i = 0; i < result.length(); i++) {
					JSONObject row;
					try {
						row = result.getJSONObject(i);
						freshBean = new FreshBean(row.getString("offer_name"), row.getString("payout"), Integer.valueOf(row.getString("offer_id")), BASE_URL
								+ "uploads/" + row.getString("offer_file"));
						freshBeanList.add(freshBean);
						MobileArrayAdapter mobileArrayAdapter = new MobileArrayAdapter(getApplicationContext(), R.id.listView1, freshBeanList);
						listview.setAdapter(mobileArrayAdapter);
						listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {

							@Override
							public void onItemClick(AdapterView<?> parent, final View view, int position, long id) {
								FreshBean freshBean = (FreshBean) parent.getItemAtPosition(position);
								Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(BASE_URL + "clicks?u_id="
										+ userFunctions.getUserID(getApplicationContext()) + "&off_id=" + freshBean.getOfferID()));
								startActivity(browserIntent);
							}
						});
					} catch (JSONException e) {
						e.printStackTrace();
					}
				}
			}

			Log.d("MainActivity", String.valueOf(result.length()));
		}
	}

}
