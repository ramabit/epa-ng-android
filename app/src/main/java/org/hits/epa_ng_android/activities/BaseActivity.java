package org.hits.epa_ng_android.activities;

import android.support.v7.app.AppCompatActivity;
import android.widget.Button;

public class BaseActivity extends AppCompatActivity {

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

}
