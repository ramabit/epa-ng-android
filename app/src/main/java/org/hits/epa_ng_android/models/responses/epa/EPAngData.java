package org.hits.epa_ng_android.models.responses.epa;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

public class EPAngData implements Parcelable {

    @SerializedName("tree")
    @Expose
    private String tree;

    @SerializedName("placements")
    @Expose
    private List<EPAngPlacement> placements;

    @SerializedName("metadata")
    @Expose
    private EPAngMetadata metadata;

    @SerializedName("version")
    @Expose
    private int version;

    @SerializedName("fields")
    @Expose
    private List<String> fields;

    public EPAngData(String tree, List<EPAngPlacement> placements, EPAngMetadata metadata,
                     int version, List<String> fields) {
        this.tree = tree;
        this.placements = placements;
        this.metadata = metadata;
        this.version = version;
        this.fields = fields;
    }

    public String getTree() {
        return tree;
    }

    public void setTree(String tree) {
        this.tree = tree;
    }

    public List<EPAngPlacement> getPlacements() {
        return placements;
    }

    public void setPlacements(List<EPAngPlacement> placements) {
        this.placements = placements;
    }

    public EPAngMetadata getMetadata() {
        return metadata;
    }

    public void setMetadata(EPAngMetadata metadata) {
        this.metadata = metadata;
    }

    public int getVersion() {
        return version;
    }

    public void setVersion(int version) {
        this.version = version;
    }

    public List<String> getFields() {
        return fields;
    }

    public void setFields(List<String> fields) {
        this.fields = fields;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.tree);
        dest.writeList(this.placements);
        dest.writeParcelable(this.metadata, flags);
        dest.writeInt(this.version);
        dest.writeStringList(this.fields);
    }

    protected EPAngData(Parcel in) {
        this.tree = in.readString();
        this.placements = new ArrayList<EPAngPlacement>();
        in.readList(this.placements, EPAngPlacement.class.getClassLoader());
        this.metadata = in.readParcelable(EPAngMetadata.class.getClassLoader());
        this.version = in.readInt();
        this.fields = in.createStringArrayList();
    }

    public static final Parcelable.Creator<EPAngData> CREATOR = new Parcelable.Creator<EPAngData>() {
        @Override
        public EPAngData createFromParcel(Parcel source) {
            return new EPAngData(source);
        }

        @Override
        public EPAngData[] newArray(int size) {
            return new EPAngData[size];
        }
    };

}
