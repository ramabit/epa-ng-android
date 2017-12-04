package org.hits.epa_ng_android.network;

import org.hits.epa_ng_android.models.responses.QSFileUploadResponse;
import org.hits.epa_ng_android.models.responses.TreesResponse;
import org.hits.epa_ng_android.models.responses.epa.EPAngData;

import okhttp3.MultipartBody;
import okhttp3.ResponseBody;
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

    @GET("analysis-text-result")
    Call<EPAngData> runAnalysisWithTextResult(@Query("tree") String treeName,
                                              @Query("qs") String qsUUID);

    @GET("analysis-graphical-result")
    Call<ResponseBody> runAnalysisWithGraphicalResult(@Query("tree") String treeName,
                                                      @Query("qs") String qsUUID);

}
