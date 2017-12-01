package org.hits.epa_ng_android.models.responses.epa;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class EPAngMetadata implements Parcelable {

    @SerializedName("invocation")
    @Expose
    private String invocation;

    public EPAngMetadata(String invocation) {
        this.invocation = invocation;
    }

    public String getInvocation() {
        return invocation;
    }

    public void setInvocation(String invocation) {
        this.invocation = invocation;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.invocation);
    }

    protected EPAngMetadata(Parcel in) {
        this.invocation = in.readString();
    }

    public static final Parcelable.Creator<EPAngMetadata> CREATOR = new Parcelable.Creator<EPAngMetadata>() {
        @Override
        public EPAngMetadata createFromParcel(Parcel source) {
            return new EPAngMetadata(source);
        }

        @Override
        public EPAngMetadata[] newArray(int size) {
            return new EPAngMetadata[size];
        }
    };
}
