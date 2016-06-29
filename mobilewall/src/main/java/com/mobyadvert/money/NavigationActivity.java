package com.mobyadvert.money;

import android.app.AlertDialog;
import android.app.TabActivity;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.TabHost;
import android.widget.TabWidget;
import android.widget.TextView;
import android.widget.Toast;

import com.aarki.AarkiActivity;
import com.mobilewall.app.R;
import com.mobyadvert.money.MainActivity.Logout;
import com.mobyadvert.money.ads.BannerAtivity;
import com.mobyadvert.money.ads.RadiumActivity;
import com.mobyadvert.money.ads.SonicardActivity;
import com.mobyadvert.money.app.MyApplication;
import com.mobyadvert.money.lib.UserFunctions;

/**
 * @author Sharjeel Haider
 *
 */
@SuppressWarnings("deprecation")
public class NavigationActivity extends TabActivity {
	TabHost tabHost;
	/** Called when the activity is first created. */
	
	
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);
		tabHost = getTabHost();
		setTabs();
		MyApplication.getInstance().trackScreenView("Navigation Activity");

	}
	private void setTabs()
	{
		addTab("Offers wall", R.drawable.tab_offerswall, OffersWallActivity.class);
		addTab("Rewards", R.drawable.tab_reward, RewardsActivity.class);
		addTab("Invite", R.drawable.tab_invite, InviteActivity.class);
		addTab("Setting", R.drawable.tab_setting, AboutActivity.class);
		
//		getTabWidget().getChildAt(3).setOnClickListener(new OnClickListener() {
//
//	        @Override
//	        public void onClick(View v) {
//	            // Add pop up code here
//	        	AlertDialog.Builder builder = new AlertDialog.Builder(NavigationActivity.this);
//	            builder.setMessage("Are you sure you want to logout?")
//	               .setCancelable(false)
//	               .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
//	                   public void onClick(DialogInterface dialog, int id) {
//	                	   UserFunctions userFunctions = new UserFunctions();
//		                	userFunctions.logoutUser(getApplicationContext());
//		           			Intent login = new Intent(getApplicationContext(), LoginActivity.class);
//		           			login.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//		           			startActivity(login);
//		           			finish();
//	                   }
//	               })
//	               .setNegativeButton("No", new DialogInterface.OnClickListener() {
//	                   public void onClick(DialogInterface dialog, int id) {
//	                        dialog.cancel();
//	                   }
//	               });
//	            AlertDialog alert = builder.create();
//	            alert.show();
//	        }
//	    });
	}
	
	private void addTab(String labelId, int drawableId, Class<?> c)
	{
		Intent intent = new Intent(NavigationActivity.this, c);
		TabHost.TabSpec spec = tabHost.newTabSpec("tab" + labelId);	
		
		View tabIndicator = LayoutInflater.from(this).inflate(R.layout.tab_indicator, getTabWidget(), false);
		TextView title = (TextView) tabIndicator.findViewById(R.id.title);
		title.setText(labelId);
		ImageView icon = (ImageView) tabIndicator.findViewById(R.id.icon);
		icon.setImageResource(drawableId);		
		spec.setIndicator(tabIndicator);
		spec.setContent(intent);
		tabHost.addTab(spec);
	}
}