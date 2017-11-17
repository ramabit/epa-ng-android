package org.hits.epa_ng_android.network;

import org.hits.epa_ng_android.models.responses.TreesResponse;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Query;

public interface EPAngService {

    @GET("trees")
    Call<TreesResponse> getTrees();

    @Multipart
    @POST("upload-qs")
    Call<Object> uploadQSFile(@Part("tree") RequestBody treeName,
                              @Part MultipartBody.Part qsFile);

    @GET("phylogenetic")
    Call<Object> runAnalysis(@Query("tree") String treeName,
                             @Query("qs") String qsUUID);

}
