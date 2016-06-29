package com.mobyadvert.money;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import com.mobilewall.app.R;
import com.mobyadvert.money.RegisterActivity.RegistryTask;
import com.mobyadvert.money.app.MyApplication;
import com.mobyadvert.money.lib.DatabaseHandler;
import com.mobyadvert.money.lib.JSONParser;
import com.mobyadvert.money.lib.UserFunctions;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;



public class ChangePassword extends Fragment {
	
	public final String BASE_URL = "http://reward.mobilewall.co/";
	private Button updateButton;
	private TextView oldPassword,newPassword,reNewPassword;
	// JSON Response node names
	private static String KEY_SUCCESS = "success";
	private static String KEY_ERROR = "error";
	
	private WeakReference<ChangePassworded> asyncTaskWeakRef;
	
	@Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
		View view = inflater.inflate(R.layout.changepasswordlayout, null);
		updateButton = (Button) view.findViewById(R.id.btnUpdate);
		oldPassword = (TextView) view.findViewById(R.id.txtOldPassword);
		newPassword = (TextView) view.findViewById(R.id.txtNewPassword);
		reNewPassword = (TextView) view.findViewById(R.id.txtReNewPassword);

		MyApplication.getInstance().trackScreenView("ChangePassword");

		updateButton.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				if (newPassword.getText().equals(reNewPassword.getText())) {
					Toast.makeText(getActivity(), "new passwod don't see match", Toast.LENGTH_SHORT).show();
				}
				
				//exec update password
				startNewAsyncTask();
				
				// TODO Auto-generated method stub
				Log.v("Check CLICK","onclick have click");
			}
		});
		
        return view;
    }
	
	private void startNewAsyncTask() {
		ChangePassworded asyncTask = new ChangePassworded();
	    this.asyncTaskWeakRef = new WeakReference<ChangePassworded >(asyncTask );
	    asyncTask.execute(BASE_URL + "users/api_change_password");
	}
	
	class ChangePassworded extends AsyncTask<String, Void, JSONObject> {

		@Override
		protected JSONObject doInBackground(String... params) {
			JSONParser jsonParser = new JSONParser();
			List<NameValuePair> entity = new ArrayList<NameValuePair>();

			UserFunctions userFunctions = new UserFunctions();
			String email = userFunctions.getEmail(getActivity());
			entity.add(new BasicNameValuePair("email", email));
			entity.add(new BasicNameValuePair("old_password", oldPassword.getText().toString()));
			entity.add(new BasicNameValuePair("new_password", newPassword.getText().toString()));

			JSONObject json = jsonParser.getJSONFromUrl(params[0], entity);
			return json;
		}

		@Override
		protected void onPostExecute(JSONObject json) {
			super.onPostExecute(json);
			UserFunctions userFunction = new UserFunctions();

			// check for login response
			try {
				if (json != null) {
					if (json.getString(KEY_SUCCESS) != null) {
							Toast.makeText(getActivity(), "password update successfully", Toast.LENGTH_SHORT).show();
						} else {
							Toast.makeText(getActivity(), "update error. please try again", Toast.LENGTH_SHORT).show();
						}
				} else {
					Toast.makeText(getActivity(), "update error. please try again", Toast.LENGTH_SHORT).show();
				}
			} catch (JSONException e) {
				Toast.makeText(getActivity(), e.getMessage(), Toast.LENGTH_SHORT).show();
			}
		}
	}
}
