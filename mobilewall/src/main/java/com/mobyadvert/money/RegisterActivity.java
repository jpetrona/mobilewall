package com.mobyadvert.money;

import java.util.ArrayList;
import java.util.List;
import com.mobilewall.app.*;
import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.TextUtils;
import android.text.style.UnderlineSpan;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.mobilewall.app.R;
import com.mobyadvert.money.app.MyApplication;
import com.mobyadvert.money.lib.DatabaseHandler;
import com.mobyadvert.money.lib.JSONParser;
import com.mobyadvert.money.lib.UserFunctions;

public class RegisterActivity extends BaseActivity implements CashBase {
	Button btnRegister;
	TextView btnLinkToLogin;
	EditText inputFullName;
	EditText inputEmail;
	EditText inputPassword;
	EditText inputInviteCode;
	TextView registerErrorMsg;

	// JSON Response node names
	private static String KEY_SUCCESS = "success";
	private static String KEY_ERROR = "error";
	private static String KEY_UID = "uid";
	private static String KEY_NAME = "name";
	private static String KEY_EMAIL = "email";
	private static String KEY_INVITE_CODE = "invite_code";
	private static String KEY_CREATED_AT = "created_on";

	private ProgressDialog dialog;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.register);
		isLogin();
		MyApplication.getInstance().trackScreenView("RegisterActivity");

		// Importing all assets like buttons, text fields
		inputFullName = (EditText) findViewById(R.id.txtOldPassword);
		inputEmail = (EditText) findViewById(R.id.txtNewPassword);
		inputPassword = (EditText) findViewById(R.id.txtReNewPassword);
		btnRegister = (Button) findViewById(R.id.btnUpdate);
		btnLinkToLogin = (TextView) findViewById(R.id.btn_signup_login);
		registerErrorMsg = (TextView) findViewById(R.id.register_error);
		inputInviteCode = (EditText) findViewById(R.id.txtCodeInvite);

		SpannableString content = new SpannableString("Login Here");
		content.setSpan(new UnderlineSpan(), 0, content.length(), 0);
		btnLinkToLogin.setText(content);

		dialog = new ProgressDialog(RegisterActivity.this);
		dialog.setMessage("Loading...");

		// Register Button Click event
		btnRegister.setOnClickListener(new View.OnClickListener() {
			public void onClick(View view) {
				dialog.show();
				String name = inputFullName.getText().toString();
				String email = inputEmail.getText().toString();
				String password = inputPassword.getText().toString();
				String invite_code = inputInviteCode.getText().toString();
				if (TextUtils.isEmpty(name) || TextUtils.isEmpty(email) || TextUtils.isEmpty(password)) {
					dialog.dismiss();
					registerErrorMsg.setText("Please input email or password");
				} else {
					new RegistryTask().execute(BASE_URL + "users/regiter", name, email, password,invite_code);
				}
			}
		});

		// Link to Login Screen
		btnLinkToLogin.setOnClickListener(new View.OnClickListener() {

			public void onClick(View view) {
				Intent i = new Intent(getApplicationContext(), LoginActivity.class);
				startActivity(i);
				// Close Registration View
				finish();
			}
		});
	}

	class RegistryTask extends AsyncTask<String, Void, JSONObject> {

		@Override
		protected JSONObject doInBackground(String... params) {
			JSONParser jsonParser = new JSONParser();
			List<NameValuePair> entity = new ArrayList<NameValuePair>();

			entity.add(new BasicNameValuePair("name", params[1]));
			entity.add(new BasicNameValuePair("email", params[2]));
			entity.add(new BasicNameValuePair("password", params[3]));
			entity.add(new BasicNameValuePair("invite_code", params[4]));

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
						registerErrorMsg.setText("");
						String res = json.getString(KEY_SUCCESS);
						if (Integer.parseInt(res) == 1) {
							// user successfully registred
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
							// Close Registration Screen
							finish();
						} else {
							
							dialog.hide();
							
							String res_error = json.getString(KEY_ERROR);
							
							if(Integer.parseInt(res_error) == 400) {
								registerErrorMsg.setText("Error name exists");
							} else if (Integer.parseInt(res_error) == 401) {
								registerErrorMsg.setText("Error email exists");
							} else if (Integer.parseInt(res_error) == 402) {
								registerErrorMsg.setText("Error ip address exists");
							} else {
								registerErrorMsg.setText("Error occured in registration");
							}
							
						}
					}
				} else {
					dialog.hide();
					// Error in registration
					registerErrorMsg.setText("Error occured in registration");
				}
			} catch (JSONException e) {
				dialog.hide();
				registerErrorMsg.setText("Error occured in registration");
			}
		}
	}
}
