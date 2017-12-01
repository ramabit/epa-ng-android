package org.hits.epa_ng_android.network.callbacks;

import org.hits.epa_ng_android.models.responses.epa.EPAngData;

public interface RunAnalysisCallback {

    void onSuccess(EPAngData data);

    void onError(Throwable throwable);

}
