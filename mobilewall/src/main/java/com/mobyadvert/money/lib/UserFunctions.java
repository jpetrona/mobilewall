package com.mobyadvert.money.lib;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONObject;

import android.content.Context;

import com.mobyadvert.money.CashBase;

public class UserFunctions implements CashBase {

	private JSONParser jsonParser;
	private static String register_tag = "register";

	// constructor
	public UserFunctions() {
		jsonParser = new JSONParser();
	}

	/**
	 * function make Login Request
	 * 
	 * @param email
	 * @param password
	 * */
	public JSONObject loginUser(String email, String password) {
		// Building Parameters
		List<NameValuePair> params = new ArrayList<NameValuePair>();
		params.add(new BasicNameValuePair("email", email));
		params.add(new BasicNameValuePair("password", password));
		JSONObject json = jsonParser.getJSONFromUrl(BASE_URL + "users/login", params);
		return json;
	}

	/**
	 * function make Login Request
	 * 
	 * @param name
	 * @param email
	 * @param password
	 * */
	public JSONObject registerUser(String name, String email, String password) {
		// Building Parameters
		List<NameValuePair> params = new ArrayList<NameValuePair>();
		params.add(new BasicNameValuePair("tag", register_tag));
		params.add(new BasicNameValuePair("name", name));
		params.add(new BasicNameValuePair("email", email));
		params.add(new BasicNameValuePair("password", password));

		// getting JSON Object
		JSONObject json = jsonParser.getJSONFromUrl(BASE_URL + "users/register", params);
		// return json
		return json;
	}

	/**
	 * Function get Login status
	 * */
	public boolean isUserLoggedIn(Context context) {
		DatabaseHandler db = new DatabaseHandler(context);
		int count = db.getRowCount();
		if (count > 0) {
			// user logged in
			return true;
		}
		return false;
	}

	/**
	 * Function to logout user Reset Database
	 * */
	public boolean logoutUser(Context context) {
		DatabaseHandler db = new DatabaseHandler(context);
		db.resetTables();
		return true;
	}
	
	public String getInviteCode(Context context){
		DatabaseHandler db = new DatabaseHandler(context);
		HashMap<String, String> user_hashmap = db.getUserDetails();
		String userId = user_hashmap.get("invite_code");
		return userId;
	}

	public String getUserID(Context context) {
		DatabaseHandler db = new DatabaseHandler(context);
		HashMap<String, String> user_hashmap = db.getUserDetails();
		String userId = user_hashmap.get("uid");
		return userId;
	}
	
	public String getEmail(Context context) {
		DatabaseHandler db = new DatabaseHandler(context);
		HashMap<String, String> user_hashmap = db.getUserDetails();
		String email = user_hashmap.get("email");
		return email;
	}

}
