package com.mobyadvert.money;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import com.mobilewall.app.*;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.TextUtils;
import android.text.style.UnderlineSpan;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.mobilewall.app.R;
import com.mobyadvert.money.app.MyApplication;
import com.mobyadvert.money.lib.DatabaseHandler;
import com.mobyadvert.money.lib.JSONParser;
import com.mobyadvert.money.lib.UserFunctions;
import com.facebook.FacebookActivity;
import com.facebook.HttpMethod;
import com.facebook.Request;
import com.facebook.Request.Callback;
import com.facebook.RequestAsyncTask;
import com.facebook.Response;
import com.facebook.Session;
import com.facebook.SessionState;
import com.facebook.model.GraphObject;

public class LoginActivity extends BaseActivity implements CashBase {
	Button btnLogin;
	Button btnFbLogin;
	TextView btnLinkToRegister;
	EditText inputEmail;
	EditText inputPassword;
	TextView loginErrorMsg;

	// JSON Response node names
	private static String KEY_SUCCESS = "success";
	private static String KEY_UID = "uid";
	private static String KEY_NAME = "name";
	private static String KEY_EMAIL = "email";
	private static String KEY_INVITE_CODE = "invite_code";
	private static String KEY_CREATED_AT = "created_on";
	private static String login_tag = "login";
	private ProgressDialog dialog;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.login);
		isLogin();
		MyApplication.getInstance().trackScreenView("LoginActivity");

		// Importing all assets like buttons, text fields
		inputEmail = (EditText) findViewById(R.id.withdraw_name);
		inputPassword = (EditText) findViewById(R.id.withdraw_account);
		btnLogin = (Button) findViewById(R.id.btn_withdraw);
		btnLinkToRegister = (TextView) findViewById(R.id.btn_signup_login);
		loginErrorMsg = (TextView) findViewById(R.id.withdraw_msg);
		btnFbLogin = (Button) findViewById(R.id.btnFacebook);

		SpannableString content = new SpannableString("Don't Have an Account?");
		content.setSpan(new UnderlineSpan(), 0, content.length(), 0);
		btnLinkToRegister.setText(content);

		dialog = new ProgressDialog(LoginActivity.this);
		dialog.setMessage("Loading...");

		// Login button Click Event
		btnLogin.setOnClickListener(new View.OnClickListener() {

			public void onClick(View view) {
				dialog.show();
				String email = inputEmail.getText().toString();
				String password = inputPassword.getText().toString();
				if (TextUtils.isEmpty(email) || TextUtils.isEmpty(password)) {
					dialog.dismiss();
					loginErrorMsg.setText("Please input email or password");
				} else {
					new LoginTask().execute(BASE_URL + "users/login", email, password);
				}
			}
		});
		
		// Login facbook button Click Event
		btnFbLogin.setOnClickListener(new View.OnClickListener() {
			public void onClick(View view) {
				dialog.show();
				getUserDataFromFacebook();
			}
		});

		// Link to Register Screen
		btnLinkToRegister.setOnClickListener(new View.OnClickListener() {

			public void onClick(View view) {
				Intent i = new Intent(getApplicationContext(), RegisterActivity.class);
				startActivity(i);
				finish();
			}
		});
	}
	
	//FACEBOOK LOGIN
	private void getUserDataFromFacebook() {

		List<String> PERMISSIONS = new ArrayList<String>();
		PERMISSIONS.add("friends_about_me");
		PERMISSIONS.add("email");
		PERMISSIONS.add("user_birthday");
		PERMISSIONS.add("user_activities");

		if (!isSessionOpen()) {
			try {
				openSession();
			} catch (Exception e) {
				e.printStackTrace();
				// Utils.removeSimpleProgressDialog();
			}
		} else {
			sendReqToLoadDataFromFb();
		}
	}
	
	@Override
	protected void onSessionStateChange(SessionState state, Exception exception) {
		super.onSessionStateChange(state, exception);
		if (state.isOpened()) {
			sendReqToLoadDataFromFb();
		}
	}
	
	private void sendReqToLoadDataFromFb() {
		Callback callback = new Callback() {
			@Override
			public void onCompleted(Response response) {
				Log.w("GET FACBOOK", "Facebook Response ::" + response + "");
				loginWithFacebook(response);
			}
		};

		String graphPath = "me";
		Bundle bundle = new Bundle();
		bundle.putString("fields",
				"id,first_name,last_name");

		Request request = new Request(Session.getActiveSession(), graphPath,
				bundle, HttpMethod.GET, callback);
		RequestAsyncTask task = Request.executeBatchAsync(request);
		if (task == null) {
			Log.w("GET FACBOOK", "task is null");
		} else {
			Log.w("GET FACBOOK", task.getStatus() + "");
		}
	}
	
	
	private void loginWithFacebook(final Response response) {
		new Thread(new Runnable() {
			@Override
			public void run() {
				try {
					Log.w("TAG", response.toString());
					GraphObject graphObject = response.getGraphObject();
					JSONObject jsonObject = new JSONObject(graphObject
							.getInnerJSONObject().toString());
					Log.w("RESPONSE", jsonObject.toString());
					String fb_id = jsonObject.getString("id");
					String username = jsonObject.getString("first_name") + jsonObject.getString("last_name");
					
					//check login or register
					LoginFb(fb_id,username);
					
				} catch (JSONException e) {
					e.printStackTrace();
				}
			}
		}).start();
	}
	
	private void LoginFb(String fb_id,String username){
		new LoginTaskFb().execute(BASE_URL + "users/api_get_point_by_user", fb_id, username);
	}
	
	class LoginTaskFb extends AsyncTask<String, Void, JSONObject> {

		@Override
		protected JSONObject doInBackground(String... params) {
			JSONParser jsonParser = new JSONParser();
			List<NameValuePair> entity = new ArrayList<NameValuePair>();
			entity.add(new BasicNameValuePair("tag", login_tag));
			entity.add(new BasicNameValuePair("facebook_id", params[1]));
			entity.add(new BasicNameValuePair("username", params[2]));
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
						loginErrorMsg.setText("");
						String res = json.getString(KEY_SUCCESS);
						if (Integer.parseInt(res) == 1) {
							// user successfully logged in
							// Store user details in SQLite Database
							DatabaseHandler db = new DatabaseHandler(getApplicationContext());
							JSONObject json_user = json.getJSONObject("user");

							// Clear all previous data in database
							userFunction.logoutUser(getApplicationContext());
							db.addUser(json_user.getString(KEY_NAME), json_user.getString(KEY_EMAIL),
									json.getString(KEY_UID), json_user.getString(KEY_CREATED_AT),json_user.getString(KEY_INVITE_CODE));

							// Launch Dashboard Screen
							Intent dashboard = new Intent(getApplicationContext(), NavigationActivity.class);

							// Close all views before launching Dashboard
							dashboard.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
							startActivity(dashboard);
							finish();
						} else {
							loginErrorMsg.setText("Incorrect username/password");
						}
						dialog.dismiss();
					}
				} else {
					dialog.dismiss();
					loginErrorMsg.setText("Incorrect username/password");
				}
			} catch (JSONException e) {
				dialog.dismiss();
				e.printStackTrace();
			}
		}
	}

	class LoginTask extends AsyncTask<String, Void, JSONObject> {

		@Override
		protected JSONObject doInBackground(String... params) {
			JSONParser jsonParser = new JSONParser();
			List<NameValuePair> entity = new ArrayList<NameValuePair>();
			entity.add(new BasicNameValuePair("tag", login_tag));
			entity.add(new BasicNameValuePair("email", params[1]));
			entity.add(new BasicNameValuePair("password", params[2]));
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
						loginErrorMsg.setText("");
						String res = json.getString(KEY_SUCCESS);
						if (Integer.parseInt(res) == 1) {
							// user successfully logged in
							// Store user details in SQLite Database
							DatabaseHandler db = new DatabaseHandler(getApplicationContext());
							JSONObject json_user = json.getJSONObject("user");

							// Clear all previous data in database
							userFunction.logoutUser(getApplicationContext());
							db.addUser(json_user.getString(KEY_NAME), json_user.getString(KEY_EMAIL),
									json.getString(KEY_UID), json_user.getString(KEY_CREATED_AT),json_user.getString(KEY_INVITE_CODE));

							// Launch Dashboard Screen
							Intent dashboard = new Intent(getApplicationContext(), NavigationActivity.class);

							// Close all views before launching Dashboard
							dialog.dismiss();
							dashboard.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
							startActivity(dashboard);
							finish();
						} else {
							loginErrorMsg.setText("Incorrect username/password");
						}
					}
				} else {
					loginErrorMsg.setText("Incorrect username/password");
				}
			} catch (JSONException e) {
				e.printStackTrace();
			}
		}
	}
}
