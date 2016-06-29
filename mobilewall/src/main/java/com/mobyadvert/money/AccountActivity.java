package com.mobyadvert.money;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import com.mobilewall.app.R;

public class AccountActivity extends BaseActivity {

	private Button mBtnLogout;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.account);
		
		// Check Login
		isNotLogin();

		mBtnLogout = (Button) findViewById(R.id.btn_logout);

		mBtnLogout.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				userFunctions.logoutUser(getApplicationContext());
				Intent login = new Intent(getApplicationContext(), LoginActivity.class);
				login.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
				startActivity(login);
				finish();
			}
		});
	}
}
