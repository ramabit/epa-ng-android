package org.hits.epa_ng_android.models.responses.epa;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

public class EPAngPlacement implements Parcelable {

    @SerializedName("p")
    @Expose
    private List<List<Double>> placements;

    @SerializedName("m")
    @Expose
    private EPAngPlacementMetadata metadata;

    @SerializedName("n")
    @Expose
    private List<String> names;

    public EPAngPlacement(List<List<Double>> placements, EPAngPlacementMetadata metadata, List<String> names) {
        this.placements = placements;
        this.metadata = metadata;
        this.names = names;
    }

    public List<List<Double>> getPlacements() {
        return placements;
    }

    public void setPlacements(List<List<Double>> placements) {
        this.placements = placements;
    }

    public EPAngPlacementMetadata getMetadata() {
        return metadata;
    }

    public void setMetadata(EPAngPlacementMetadata metadata) {
        this.metadata = metadata;
    }

    public List<String> getNames() {
        return names;
    }

    public void setNames(List<String> names) {
        this.names = names;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeList(this.placements);
        dest.writeParcelable(this.metadata, flags);
        dest.writeStringList(this.names);
    }

    protected EPAngPlacement(Parcel in) {
        this.placements = new ArrayList<List<Double>>();
        in.readList(this.placements, List.class.getClassLoader());
        this.metadata = in.readParcelable(EPAngPlacementMetadata.class.getClassLoader());
        this.names = in.createStringArrayList();
    }

    public static final Parcelable.Creator<EPAngPlacement> CREATOR = new Parcelable.Creator<EPAngPlacement>() {
        @Override
        public EPAngPlacement createFromParcel(Parcel source) {
            return new EPAngPlacement(source);
        }

        @Override
        public EPAngPlacement[] newArray(int size) {
            return new EPAngPlacement[size];
        }
    };
}
