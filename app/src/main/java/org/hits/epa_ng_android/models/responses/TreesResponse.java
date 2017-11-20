package org.hits.epa_ng_android.models.responses;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class TreesResponse {

    @SerializedName("trees")
    @Expose
    private List<String> trees;

    public List<String> getTrees() {
        return trees;
    }

    public void setTrees(List<String> trees) {
        this.trees = trees;
    }

}
