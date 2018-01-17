package org.hits.epa_ng_android.utils;

import android.app.Application;
import android.content.Context;
import android.support.multidex.MultiDex;

public class EPAngApplication extends Application {

    private static EPAngApplication CONTEXT;

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(this);
    }

    @Override
    public void onCreate() {
        super.onCreate();
        CONTEXT = this;
    }

    public static EPAngApplication getInstance() {
        return CONTEXT;
    }

}
