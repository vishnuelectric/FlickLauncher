package com.android.launcher3.security.fingerprint;

/**
 * Created by Michele on 20/03/2017.
 */

import android.Manifest;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.hardware.fingerprint.FingerprintManager;
import android.os.CancellationSignal;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.widget.TextView;

import com.android.launcher3.ItemInfo;
import com.android.launcher3.R;


/**
 * Created by whit3hawks on 11/16/16.
 */
@TargetApi(23)
public class FingerprintHandler extends FingerprintManager.AuthenticationCallback {


    private Context context;
    private View shortcutInfo;
    // Constructor
    public FingerprintHandler(Context mContext, View mShortcutInfo) {
        context = mContext;
        shortcutInfo = mShortcutInfo;
    }


    @TargetApi(23)
    public void startAuth(FingerprintManager manager, FingerprintManager.CryptoObject cryptoObject) {
        CancellationSignal cancellationSignal = new CancellationSignal();
        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.USE_FINGERPRINT) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        manager.authenticate(cryptoObject, cancellationSignal, 0, this, null);
    }


    @Override
    public void onAuthenticationError(int errMsgId, CharSequence errString) {
        this.update(context.getString(R.string.fingerprint_error) + "\n" + errString, false);
    }


    @Override
    public void onAuthenticationHelp(int helpMsgId, CharSequence helpString) {
        this.update(context.getString(R.string.fingerprint_help) + "\n" + helpString, false);
    }


    @Override
    public void onAuthenticationFailed() {
        this.update(context.getString(R.string.fingerprint_failed), false);
    }


    @Override
    public void onAuthenticationSucceeded(FingerprintManager.AuthenticationResult result) {
        this.update(context.getString(R.string.fingerprint_succeded), true);
    }


    public void update(String e, Boolean success){
        TextView textView = (TextView) ((Activity)context).findViewById(R.id.errorText);
        textView.setText(e);
        if(success && shortcutInfo != null){
            textView.setTextColor(ContextCompat.getColor(context, R.color.colorPrimaryDarkFinger));
            ItemInfo item = (ItemInfo) shortcutInfo.getTag();
            Intent intent = item.getIntent();
            if (intent == null) {
                throw new IllegalArgumentException("Input must have a valid intent");
            }
            FingerprintActivity.getActivity().startActivity(intent);
            FingerprintActivity.getActivity().finish();
        }
    }
}