package org.hits.epa_ng_android.network.callbacks;

import java.util.List;

public interface GetSupportedTreesCallback {

    void onSuccess(List<String> trees);

    void onError(Throwable throwable);

}
