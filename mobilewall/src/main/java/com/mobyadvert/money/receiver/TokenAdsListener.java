package com.mobyadvert.money.receiver;

import android.widget.Toast;

import com.mobyadvert.money.ads.RadiumActivity;
import com.tokenads.sdk.TokenAdsAgent.PopupType;
import com.tokenads.sdk.TokenAdsEventListener;

public class TokenAdsListener extends TokenAdsEventListener {
	private RadiumActivity mainApp;

	public TokenAdsListener(RadiumActivity mainApp) {
		this.mainApp = mainApp;
	}

	@Override
	public void onShowOffers() {
		Toast.makeText(mainApp, "offer wall shown ", Toast.LENGTH_SHORT).show();
	}

	@Override
	public void onCloseOffers() {
		Toast.makeText(mainApp, "offer wall closed ", Toast.LENGTH_SHORT).show();
	}

	@Override
	public void onShowPopup() {
		Toast.makeText(mainApp, "popup opened ", Toast.LENGTH_SHORT).show();
	}

	@Override
	public void onClosePopup() {
		Toast.makeText(mainApp, "popup closed ", Toast.LENGTH_SHORT).show();
	}

	@Override
	public void onOffersCount(int count) {
		Toast.makeText(mainApp, "count Offers: " + count, Toast.LENGTH_SHORT).show();
	}

	@Override
	public void onPopupCount(int count, PopupType popupType) {
		if (popupType == PopupType.APP_INSTALLs) {
			Toast.makeText(mainApp, "App-Install Count: " + count, Toast.LENGTH_SHORT).show();
		} else {
			Toast.makeText(mainApp, "Video Count: " + count, Toast.LENGTH_SHORT).show();
		}
	}

	@Override
	public void onReceivePoints(String points) {
		Toast.makeText(mainApp, "points: " + points, Toast.LENGTH_SHORT).show();
	}

	@Override
	public boolean onError(Error error) {
		String errMsg = error.toString();
		Toast.makeText(mainApp, "Error: " + errMsg, Toast.LENGTH_SHORT).show();
		return true;
	}
}