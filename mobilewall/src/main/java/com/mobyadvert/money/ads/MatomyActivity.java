package com.mobyadvert.money.ads;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.webkit.WebView;

import com.mobyadvert.money.BaseActivity;
import com.mobyadvert.money.CashBase;
import com.mobyadvert.money.LoginActivity;
import com.mobilewall.app.R;

public class MatomyActivity extends BaseActivity implements CashBase {

	@SuppressLint("SetJavaScriptEnabled")
	@SuppressWarnings("deprecation")
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		if (userFunctions.isUserLoggedIn(getApplicationContext())) {
			setContentView(R.layout.sonic_layout);
			WebView webView = (WebView) findViewById(R.id.webView1);
			webView.clearHistory();
			webView.clearFormData();
			webView.clearView();

			webView.getSettings().setJavaScriptEnabled(true);

			String sonic_url = BASE_URL + "walls/matomy/";
			webView.loadUrl(sonic_url.concat(String.valueOf(userFunctions.getUserID(getApplicationContext()))));
		} else {
			Intent intent = new Intent(this, LoginActivity.class);
			startActivity(intent);
			finish();
		}
	}
}
