package org.hits.epa_ng_android.models.responses;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class QSFileUploadResponse {

    @SerializedName("token")
    @Expose
    private String token;

    public QSFileUploadResponse(String token) {
        this.token = token;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

}
