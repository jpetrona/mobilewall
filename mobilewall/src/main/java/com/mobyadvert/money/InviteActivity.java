package com.mobyadvert.money;

import com.mobilewall.app.R;
import com.mobyadvert.money.app.MyApplication;
import com.mobyadvert.money.lib.UserFunctions;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

@SuppressLint("NewApi") public class InviteActivity extends Activity {

	
	TextView mTxtInviteCode;
	Button btnCopy;
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		MyApplication.getInstance().trackScreenView("InviteActivity");

		setContentView(R.layout.invite_activity);
		mTxtInviteCode = (TextView)findViewById(R.id.txtCodeInvite);
		btnCopy = (Button) findViewById(R.id.btnCopy);
		
		btnCopy.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				setClipboard(getApplicationContext(),mTxtInviteCode.getText().toString());
				Toast.makeText(getApplicationContext(), "Copied to Clipboard!", Toast.LENGTH_SHORT).show();
			}
		});
	}
	
	// copy text to clipboard
	@SuppressLint("NewApi") @SuppressWarnings("deprecation")
	private void setClipboard(Context context,String text) {
	    if(android.os.Build.VERSION.SDK_INT < android.os.Build.VERSION_CODES.HONEYCOMB) {
	        android.text.ClipboardManager clipboard = (android.text.ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
	        clipboard.setText(text);
	    } else {
	        android.content.ClipboardManager clipboard = (android.content.ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
	        android.content.ClipData clip = android.content.ClipData.newPlainText("Copied Text", text);
	        clipboard.setPrimaryClip(clip);
	    }
	}
	

	@Override
	protected void onResume() {
		super.onResume();
	}

	@Override
	protected void onStart() {
		super.onStart();
		
		UserFunctions userFunctions = new UserFunctions();
		String invite_code = userFunctions.getInviteCode(getApplicationContext());
		mTxtInviteCode.setText(invite_code);
	}
	
}
