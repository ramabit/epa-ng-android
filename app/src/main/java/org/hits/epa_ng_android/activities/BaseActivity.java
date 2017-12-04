package org.hits.epa_ng_android.activities;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.widget.Button;

import java.io.ByteArrayOutputStream;

public class BaseActivity extends AppCompatActivity {

    private final static String BITMAP_KEY = "Bitmap";

    protected void disableButton(Button button) {
        button.setAlpha(0.5f);
        button.setEnabled(false);
        button.setClickable(false);
    }

    protected void enableButton(Button button) {
        button.setAlpha(1f);
        button.setEnabled(true);
        button.setClickable(true);
    }

    protected void addBitmapToBundle(Bundle bundle, Bitmap bitmap) {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 50, byteArrayOutputStream);
        bundle.putByteArray(BITMAP_KEY, byteArrayOutputStream.toByteArray());
    }

    protected Bitmap getBitmapFromBundle(@NonNull Bundle bundle) {
        byte[] bytes = bundle.getByteArray(BITMAP_KEY);
        if (bytes != null) {
            return BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
        } else {
            return null;
        }
    }

}
