package org.hits.epa_ng_android.network.callbacks;

import android.graphics.Bitmap;

public interface RunAnalysisWithGraphicResultCallback {

    void onSuccess(Bitmap graphic);

    void onError(Throwable throwable);

}
