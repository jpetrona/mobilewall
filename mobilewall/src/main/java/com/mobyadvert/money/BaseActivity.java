package com.mobyadvert.money;

import com.mobyadvert.money.lib.UserFunctions;
import com.facebook.FacebookActivity;
import com.mobilewall.app.*;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

public class BaseActivity extends FacebookActivity {
	protected UserFunctions userFunctions;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		userFunctions = new UserFunctions();
	}

	public void isNotLogin() {
		
		if (!userFunctions.isUserLoggedIn(getApplicationContext())) {
			this.finish();
			Intent intent = new Intent(this, LoginActivity.class);
			startActivity(intent);
		}
	}

	public void isLogin() {
		if (userFunctions.isUserLoggedIn(getApplicationContext())) {
			Intent intent = new Intent(this, NavigationActivity.class);
			startActivity(intent);
			this.finish();
		}
	}
}
