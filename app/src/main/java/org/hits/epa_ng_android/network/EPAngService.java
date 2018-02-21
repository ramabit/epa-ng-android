package org.hits.epa_ng_android.network;

import org.hits.epa_ng_android.models.requests.ResultsEmailBody;
import org.hits.epa_ng_android.models.responses.QSFileUploadResponse;
import org.hits.epa_ng_android.models.responses.TreesResponse;
import org.hits.epa_ng_android.models.responses.epa.EPAngData;

import okhttp3.MultipartBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Query;

public interface EPAngService {

    /**
     * Get the available trees supported by the server.
     *
     * @return a response with the list of trees
     */
    @GET("trees")
    Call<TreesResponse> getTrees();

    /**
     * Upload a Query Sequence (QS) file.
     *
     * @param qsFile is the file to be uploaded
     * @return a response with the token (UUID) assigned to the uploaded file
     */
    @Multipart
    @POST("upload-qs")
    Call<QSFileUploadResponse> uploadQSFile(@Part MultipartBody.Part qsFile);

    /**
     * Run an analysis and obtain a text result.
     *
     * @param treeName is the name of the supported tree to be used for the analysis
     * @param qsToken  is the token (UUID) of the previously uploaded QS file
     * @return the result data of the analysis
     */
    @GET("analysis-text-result")
    Call<EPAngData> runAnalysisWithTextResult(@Query("tree") String treeName,
                                              @Query("qs") String qsToken);

    /**
     * Run an analysis and obtain a graphical result.
     *
     * @param treeName    is the name of the supported tree to be used for the analysis
     * @param qsToken     is the token (UUID) of the previously uploaded QS file
     * @param graphicType is the desire graphic type: "horizontal" (default), "vertical" or "circular"
     * @return a response containing the graphic .png image
     */
    @GET("analysis-graphical-result")
    Call<ResponseBody> runAnalysisWithGraphicalResult(@Query("tree") String treeName,
                                                      @Query("qs") String qsToken,
                                                      @Query("graphicType") String graphicType);

    @POST("results-email")
    Call<Object> resultsEmail(@Body ResultsEmailBody body);

}
