package com.mobyadvert.money;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;

import com.mobilewall.app.R;
import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;
import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu.OnOpenListener;
import com.mobyadvert.money.app.MyApplication;
import com.mobyadvert.money.lib.UserFunctions;

public class AboutActivity extends FragmentActivity implements
OnClickListener, OnOpenListener {

//	public SlidingMenu menu;
	private LinearLayout about, terms, widthdrawhistory, clickhistory,
	changepassword, contact,logout;
	
	
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.slidmenuxamplemainactivity);
		MyApplication.getInstance().trackScreenView("SettingsActivity");

		/*menu = new SlidingMenu(this);
		menu.setMode(SlidingMenu.LEFT_RIGHT);
		menu.setTouchModeAbove(SlidingMenu.TOUCHMODE_MARGIN);
		menu.setShadowWidthRes(R.dimen.shadow_width);
		menu.setShadowDrawable(R.drawable.shadow);
		menu.setBehindOffsetRes(R.dimen.slidingmenu_offset);
		menu.setFadeDegree(0.35f);
		menu.attachToActivity(this, SlidingMenu.SLIDING_CONTENT);	
		menu.setMenu(R.layout.leftmenu);
		menu.setSecondaryMenu(R.layout.rightmenu);
		menu.setSlidingEnabled(true);

		View leftmenuview = menu.getMenu();
		View rightmenuview = menu.getSecondaryMenu();

		initLayoutComponent(leftmenuview, rightmenuview);

		menu.setSecondaryOnOpenListner(this);*/
		initLayoutComponent();

		about.setOnClickListener(this);
		terms.setOnClickListener(this);
		widthdrawhistory.setOnClickListener(this);
		clickhistory.setOnClickListener(this);
		changepassword.setOnClickListener(this);
		contact.setOnClickListener(this);
		logout.setOnClickListener(this);
	}
	
	
	private void initLayoutComponent() {
		about = (LinearLayout) findViewById(R.id.about);
		terms = (LinearLayout)  findViewById(R.id.terms);
		widthdrawhistory = (LinearLayout) findViewById(R.id.historywidthdraw);
		clickhistory = (LinearLayout) findViewById(R.id.historyclick);
		changepassword = (LinearLayout) findViewById(R.id.changepassword);
		contact = (LinearLayout) findViewById(R.id.contact);
		logout = (LinearLayout) findViewById(R.id.logout);
	}
	
	@Override
	public void onClick(View v) {

		FragmentManager fm = AboutActivity.this.getSupportFragmentManager();
		FragmentTransaction ft = fm.beginTransaction();
		Fragment fragment = null;

		if (v.getId() == R.id.about) {
				fragment = new AboutAppActivity();
				if (fragment != null) {
					ft.replace(R.id.activity_main_content_fragment, fragment);
					ft.commit();
				}
//				menu.toggle();
		} else if (v.getId() == R.id.terms) {
			
				fragment = new TermsActivity();
				if (fragment != null) {
					ft.replace(R.id.activity_main_content_fragment, fragment);
					ft.commit();
					// tvTitle.setText(selectedItem);
				}
//				menu.toggle();
		} else if (v.getId() == R.id.historywidthdraw) {
			Intent i = new Intent(getApplicationContext(), WithDrawHistoryActivity.class);
			startActivity(i);
//				menu.toggle();
		} else if (v.getId() == R.id.historyclick) {
			Intent i = new Intent(getApplicationContext(), ClickHistoryActivity.class);
			startActivity(i);
//				menu.toggle();
		} else if (v.getId() == R.id.changepassword) {
			Intent i = new Intent(getApplicationContext(), ChangePassword.class);
			startActivity(i);
//			menu.toggle();

		} else if (v.getId() == R.id.contact) {
			fragment = new ContactActivity();
			if (fragment != null) {
				ft.replace(R.id.activity_main_content_fragment, fragment);
				ft.commit();
				// tvTitle.setText(selectedItem);
			}
//			menu.toggle();

		} else if (v.getId() == R.id.logout) {
			
			AlertDialog.Builder builder = new AlertDialog.Builder(AboutActivity.this);
            builder.setMessage("Are you sure you want to logout?")
               .setCancelable(false)
               .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                   public void onClick(DialogInterface dialog, int id) {
                	   UserFunctions userFunctions = new UserFunctions();
	                	userFunctions.logoutUser(getApplicationContext());
	           			Intent login = new Intent(getApplicationContext(), LoginActivity.class);
	           			login.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
	           			startActivity(login);
	           			finish();
                   }
               })
               .setNegativeButton("No", new DialogInterface.OnClickListener() {
                   public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                   }
               });
            AlertDialog alert = builder.create();
            alert.show();

		} 
	}
	
	/*public void toggleMenu(View v) {
		menu.toggle();
	}*/

	@Override
	public void onOpen() {
		// TODO Auto-generated method stub
		
	}
	
}
