package org.hits.epa_ng_android.models.responses.epa;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class EPAngPlacementMetadata implements Parcelable {

    @SerializedName("entropy")
    @Expose
    private Double entropy;

    public EPAngPlacementMetadata(Double entropy) {
        this.entropy = entropy;
    }

    public Double getEntropy() {
        return entropy;
    }

    public void setEntropy(Double entropy) {
        this.entropy = entropy;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(this.entropy);
    }

    protected EPAngPlacementMetadata(Parcel in) {
        this.entropy = (Double) in.readValue(Double.class.getClassLoader());
    }

    public static final Parcelable.Creator<EPAngPlacementMetadata> CREATOR = new Parcelable.Creator<EPAngPlacementMetadata>() {
        @Override
        public EPAngPlacementMetadata createFromParcel(Parcel source) {
            return new EPAngPlacementMetadata(source);
        }

        @Override
        public EPAngPlacementMetadata[] newArray(int size) {
            return new EPAngPlacementMetadata[size];
        }
    };
}
