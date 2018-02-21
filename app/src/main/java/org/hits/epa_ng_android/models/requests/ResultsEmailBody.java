package org.hits.epa_ng_android.models.requests;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ResultsEmailBody {

    @SerializedName("uuid")
    @Expose
    private String uuid;

    @SerializedName("email")
    @Expose
    private String email;

    public ResultsEmailBody(String uuid, String email) {
        this.uuid = uuid;
        this.email = email;
    }

    public String getUuid() {
        return uuid;
    }

    public String getEmail() {
        return email;
    }

}
