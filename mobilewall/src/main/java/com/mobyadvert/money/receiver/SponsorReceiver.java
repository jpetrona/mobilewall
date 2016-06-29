
package com.mobyadvert.money.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class SponsorReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
    	com.sponsorpay.advertiser.InstallReferrerReceiver spInstallReferrerReceiver =
                new com.sponsorpay.advertiser.InstallReferrerReceiver();
        spInstallReferrerReceiver.onReceive(context, intent);
    }

}
