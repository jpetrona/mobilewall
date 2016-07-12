package com.mobyadvert.money;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.foound.widget.AmazingAdapter;
import com.foound.widget.AmazingListView;
import com.mobilewall.app.R;
import com.mobyadvert.money.lib.JSONParser;
import com.mobyadvert.money.lib.UserFunctions;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.mobyadvert.money.CashBase;
import com.radiumone.android.volley.toolbox.JsonObjectRequest;

import android.os.*;
import android.util.*;

import java.util.*;

public class WithDrawHistoryActivity extends Activity {

	AmazingListView lsComposer;
	SectionComposerAdapter adapter;
	private WeakReference<GetWidthDrawHostory> asyncTaskWeakRef;
	List<Pair<String, List<WidthDrawObject>>> all;
	private ProgressDialog dialog;
	
	
	@Override
    public void onCreate(Bundle savedInstanceState) {
        // Inflate the layout for this fragment
		super.onCreate(savedInstanceState);
		setContentView(R.layout.widthdrawhistorylayout);

        lsComposer = (AmazingListView) findViewById(R.id.lsComposer);
		
		//lsComposer.setAdapter(adapter = new SectionComposerAdapter());

		dialog = new ProgressDialog(WithDrawHistoryActivity.this);
		dialog.setMessage("Loading...");
		
		dialog.show();
        getData();
    }
	
	public void getData() {
		startNewAsyncTask();
	}
	
	private void startNewAsyncTask() {
		GetWidthDrawHostory asyncTask = new GetWidthDrawHostory();
	    this.asyncTaskWeakRef = new WeakReference<GetWidthDrawHostory >(asyncTask );
	    asyncTask.execute(CashBase.BASE_URL + "withdraw/get_withdraw_by_user");
	}
	
	class GetWidthDrawHostory extends AsyncTask<String, Void, JSONObject> {

		@Override
		protected JSONObject doInBackground(String... params) {
			JSONParser jsonParser = new JSONParser();
			List<NameValuePair> entity = new ArrayList<NameValuePair>();

			UserFunctions userFunctions = new UserFunctions();
			String user_id = userFunctions.getUserID(WithDrawHistoryActivity.this);
			entity.add(new BasicNameValuePair("user_id", user_id));

			JSONObject json = jsonParser.getJSONFromUrl(params[0], entity);
			return json;
		}

		@Override
		protected void onPostExecute(JSONObject json) {
			super.onPostExecute(json);
			if (dialog!=null)
				dialog.dismiss();
			UserFunctions userFunction = new UserFunctions();
			// check return data
			try {
				if (json != null) {
					if (json.getString(CashBase.KEY_SUCCESS) != null) {
							JSONArray json_user = json.getJSONArray("withdraws");
							processData(json_user);
						} else {
							Toast.makeText(WithDrawHistoryActivity.this, "get data error. please try again", Toast.LENGTH_SHORT).show();
						}
				} else {
					Toast.makeText(WithDrawHistoryActivity.this, "get data error. please try again", Toast.LENGTH_SHORT).show();
				}
			} catch (JSONException e) {
				Toast.makeText(WithDrawHistoryActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
			}
		}
	}
	
	
	private void processData(JSONArray json_user) throws JSONException{
		String temp = "";
		ArrayList<Object> titleArray = new ArrayList<Object>();
		
		for(int i=0;i<json_user.length();i++) {
			JSONObject jsonObject = json_user.getJSONObject(i);
			temp = jsonObject.getString("card").toString();
			if (titleArray.indexOf(temp) == -1) {
				titleArray.add(temp);
			}
		}
		
		//get data for each title
		ArrayList<ArrayList<WidthDrawObject>> itemsArray = new ArrayList<ArrayList<WidthDrawObject>>();
		for (int j = 0; j<titleArray.size(); j ++) {
			temp = titleArray.get(j).toString();
			ArrayList<WidthDrawObject> valueArray = new ArrayList<WidthDrawObject>();
			for(int i=0;i<json_user.length();i++) {
				JSONObject jsonObject = json_user.getJSONObject(i);
				if (temp.equals(jsonObject.getString("card").toString())) {
					WidthDrawObject tem =  new WidthDrawObject(jsonObject.getString("account").toString(),
							jsonObject.getString("create_date"),
							jsonObject.getString("point"),
							jsonObject.getString("status"),
							jsonObject.getString("card"));
					valueArray.add(tem);
				}
			}
			itemsArray.add(valueArray);
		}
		
		all = new ArrayList<Pair<String, List<WidthDrawObject>>>();
		
		//convert data to struct array
		for (int j = 0; j<titleArray.size(); j ++){
			ArrayList<WidthDrawObject> compo = itemsArray.get(j);
			WidthDrawObject list[] = new WidthDrawObject[compo.size()];
			list = (WidthDrawObject[])compo.toArray(list);
			
			List<WidthDrawObject> composerList = Arrays.asList(list);
			all.add(getItmes(titleArray.get(j).toString(),composerList));
		}

		lsComposer.setPinnedHeaderView(WithDrawHistoryActivity.this.getLayoutInflater().inflate(R.layout.item_composer_header, lsComposer, false));
		lsComposer.setAdapter(adapter = new SectionComposerAdapter());
		dialog.dismiss();
	}
	
	public Pair<String, List<WidthDrawObject>> getItmes(String title, List<WidthDrawObject> list){
		return new Pair<String, List<WidthDrawObject>>(title, list);
	}
	
	private Arrays Arrays() {
		// TODO Auto-generated method stub
		return null;
	}

	class SectionComposerAdapter extends AmazingAdapter {
		//List<Pair<String, List<Composer>>> all = Data.getAllData();

		@Override
		public int getCount() {
			int res = 0;
			for (int i = 0; i < all.size(); i++) {
				res += all.get(i).second.size();
			}
			return res;
		}

		@Override
		public WidthDrawObject getItem(int position) {
			int c = 0;
			for (int i = 0; i < all.size(); i++) {
				if (position >= c && position < c + all.get(i).second.size()) {
					return all.get(i).second.get(position - c);
				}
				c += all.get(i).second.size();
			}
			return null;
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		protected void onNextPageRequested(int page) {
		}

		@Override
		protected void bindSectionHeader(View view, int position, boolean displaySectionHeader) {
			if (displaySectionHeader) {
				view.findViewById(R.id.header).setVisibility(View.VISIBLE);
				TextView lSectionTitle = (TextView) view.findViewById(R.id.header);
				lSectionTitle.setText(getSections()[getSectionForPosition(position)]);
			} else {
				view.findViewById(R.id.header).setVisibility(View.GONE);
			}
		}

		@Override
		public View getAmazingView(int position, View convertView, ViewGroup parent) {
			View res = convertView;
			if (res == null) res = WithDrawHistoryActivity.this.getLayoutInflater().inflate(R.layout.item_composer, null);
			
			TextView point = (TextView) res.findViewById(R.id.widthdrawsPoint);
			TextView status = (TextView) res.findViewById(R.id.widthdrawsDate);
			TextView username = (TextView) res.findViewById(R.id.account);
			TextView date = (TextView) res.findViewById(R.id.widthdrawDate);
			
			WidthDrawObject composer = getItem(position);
			String st = composer.status == "1" ? "pending" : "close";
			point.setText(composer.point);
			status.setText(st);
			username.setText(composer.account);
			date.setText(composer.widthdrawDate);
			
			ImageView img = (ImageView)res.findViewById(R.id.imgPayment);
			if (composer.payment.equals("Paypal")) {
				img.setImageResource(R.drawable.paypal_icon);
			} else if (composer.payment.equals("Google Card")) {
				img.setImageResource(R.drawable.google_play_icon);
			} else if (composer.payment.equals("Amazon Gift")) {
				img.setImageResource(R.drawable.amazon_icon);
			} 
			
			
			return res;
		}

		@Override
		public void configurePinnedHeader(View header, int position, int alpha) {
			TextView lSectionHeader = (TextView)header;
			lSectionHeader.setText(getSections()[getSectionForPosition(position)]);
			lSectionHeader.setBackgroundResource(R.color.lgbackground);
			lSectionHeader.setTextColor(getResources().getColorStateList(R.color.white));
		}

		@Override
		public int getPositionForSection(int section) {
			if (section < 0) section = 0;
			if (section >= all.size()) section = all.size() - 1;
			int c = 0;
			for (int i = 0; i < all.size(); i++) {
				if (section == i) { 
					return c;
				}
				c += all.get(i).second.size();
			}
			return 0;
		}

		@Override
		public int getSectionForPosition(int position) {
			int c = 0;
			for (int i = 0; i < all.size(); i++) {
				if (position >= c && position < c + all.get(i).second.size()) {
					return i;
				}
				c += all.get(i).second.size();
			}
			return -1;
		}

		@Override
		public String[] getSections() {
			String[] res = new String[all.size()];
			for (int i = 0; i < all.size(); i++) {
				res[i] = all.get(i).first;
			}
			return res;
		}
		
	}
}
