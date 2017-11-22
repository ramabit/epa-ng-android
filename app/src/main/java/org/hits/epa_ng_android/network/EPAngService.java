package org.hits.epa_ng_android.network;

import org.hits.epa_ng_android.models.EPAngData;
import org.hits.epa_ng_android.models.responses.QSFileUploadResponse;
import org.hits.epa_ng_android.models.responses.TreesResponse;

import okhttp3.MultipartBody;
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
    Call<QSFileUploadResponse> uploadQSFile(@Part MultipartBody.Part qsFile);

    @GET("phylogenetic")
    Call<EPAngData> runAnalysis(@Query("tree") String treeName,
                                @Query("qs") String qsUUID);

}
