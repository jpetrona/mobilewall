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

import com.mobilewall.app.R;
import com.foound.widget.AmazingAdapter;
import com.foound.widget.AmazingListView;
import com.mobyadvert.money.lib.JSONParser;
import com.mobyadvert.money.lib.UserFunctions;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

public class ClickHistoryActivity extends Fragment {

	AmazingListView lsComposer;
	SectionComposerAdapter adapter;
	private WeakReference<GetHistoryPoint> asyncTaskWeakRef;
	List<Pair<String, List<PointObject>>> all;
	private ProgressDialog dialog;
	
	@Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
		View view =  inflater.inflate(R.layout.clickhistorylayout, container, false);
        
        lsComposer = (AmazingListView)view.findViewById(R.id.historyPointComposer);
        
        
        dialog = new ProgressDialog(getActivity());
		dialog.setMessage("Loading...");
		
        dialog.show();
        getData();
		return view;
    }
	
	public void getData() {
		startNewAsyncTask();
	}
	
	private void startNewAsyncTask() {
		GetHistoryPoint asyncTask = new GetHistoryPoint();
	    this.asyncTaskWeakRef = new WeakReference<GetHistoryPoint >(asyncTask );
	    asyncTask.execute(CashBase.BASE_URL + "users/api_get_point_by_user");
	}
	
	class GetHistoryPoint extends AsyncTask<String, Void, JSONObject> {

		@Override
		protected JSONObject doInBackground(String... params) {
			JSONParser jsonParser = new JSONParser();
			List<NameValuePair> entity = new ArrayList<NameValuePair>();

			UserFunctions userFunctions = new UserFunctions();
			String user_id = userFunctions.getUserID(getActivity());
			entity.add(new BasicNameValuePair("user_id", user_id));

			JSONObject json = jsonParser.getJSONFromUrl(params[0], entity);
			return json;
		}

		@Override
		protected void onPostExecute(JSONObject json) {
			super.onPostExecute(json);
			UserFunctions userFunction = new UserFunctions();
			// check return data
			try {
				if (json != null) {
					if (json.getString(CashBase.KEY_SUCCESS) != null) {
							JSONArray json_user = json.getJSONArray("points");
							processData(json_user);
						} else {
							Toast.makeText(getActivity(), "get data error. please try again", Toast.LENGTH_SHORT).show();
						}
				} else {
					Toast.makeText(getActivity(), "get data error. please try again", Toast.LENGTH_SHORT).show();
				}
			} catch (JSONException e) {
				Toast.makeText(getActivity(), e.getMessage(), Toast.LENGTH_SHORT).show();
			}
		}
	}
	
	private void processData(JSONArray json_user) throws JSONException{
		String temp = "";
		ArrayList<Object> titleArray = new ArrayList<Object>();
		
		for(int i=0;i<json_user.length();i++) {
			JSONObject jsonObject = json_user.getJSONObject(i);
			temp = jsonObject.getString("ads_name").toString();
			if (titleArray.indexOf(temp) == -1) {
				titleArray.add(temp);
			}
		}
		
		//get data for each title
		ArrayList<ArrayList<PointObject>> itemsArray = new ArrayList<ArrayList<PointObject>>();
		for (int j = 0; j<titleArray.size(); j ++) {
			temp = titleArray.get(j).toString();
			ArrayList<PointObject> valueArray = new ArrayList<PointObject>();
			for(int i=0;i<json_user.length();i++) {
				JSONObject jsonObject = json_user.getJSONObject(i);
				if (temp.equals(jsonObject.getString("ads_name").toString())) {
					PointObject tem =  new PointObject(jsonObject.getString("offer_name").toString(),
							jsonObject.getString("points"),
							jsonObject.getString("create_date"));
					valueArray.add(tem);
				}
			}
			itemsArray.add(valueArray);
		}
		
		all = new ArrayList<Pair<String, List<PointObject>>>();
		
		//convert data to struct array
		for (int j = 0; j<titleArray.size(); j ++){
			ArrayList<PointObject> compo = itemsArray.get(j);
			PointObject list[] = new PointObject[compo.size()];
			list = (PointObject[])compo.toArray(list);
			
			List<PointObject> composerList = Arrays.asList(list);
			all.add(getItmes(titleArray.get(j).toString(),composerList));
		}
		
		lsComposer.setPinnedHeaderView(getActivity().getLayoutInflater().inflate(R.layout.item_composer_header, lsComposer, false));
		lsComposer.setAdapter(adapter = new SectionComposerAdapter());
		
		dialog.dismiss();
	}
	
	public Pair<String, List<PointObject>> getItmes(String title, List<PointObject> list){
		return new Pair<String, List<PointObject>>(title, list);
	}
		
	
	class SectionComposerAdapter extends AmazingAdapter {

		@Override
		public int getCount() {
			int res = 0;
			for (int i = 0; i < all.size(); i++) {
				res += all.get(i).second.size();
			}
			return res;
		}

		@Override
		public PointObject getItem(int position) {
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
			if (res == null) res = getActivity().getLayoutInflater().inflate(R.layout.item_composer_point, null);
			
			TextView point = (TextView) res.findViewById(R.id.historyPoint);
			TextView date = (TextView) res.findViewById(R.id.historyDate);
			TextView username = (TextView) res.findViewById(R.id.account);
			
			PointObject composer = getItem(position);
			point.setText(composer.point  + " Points");
			username.setText(composer.offer_name);
			date.setText(composer.create_date);
			
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
