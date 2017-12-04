package org.hits.epa_ng_android.network;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;

import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.hits.epa_ng_android.models.QSFile;
import org.hits.epa_ng_android.models.responses.QSFileUploadResponse;
import org.hits.epa_ng_android.models.responses.TreesResponse;
import org.hits.epa_ng_android.models.responses.epa.EPAngData;
import org.hits.epa_ng_android.network.callbacks.GetSupportedTreesCallback;
import org.hits.epa_ng_android.network.callbacks.RunAnalysisWithGraphicResultCallback;
import org.hits.epa_ng_android.network.callbacks.RunAnalysisWithTextResultCallback;
import org.hits.epa_ng_android.network.callbacks.UploadQSFileCallback;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.ResponseBody;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class EPAngServiceAPI {

    public static final String BASE_URL = "http://avx.h-its.org:3000/api/";

    // TODO add username and password validation for this app
    public static final String APP_USERNAME = "";
    public static final String APP_PASSWORD = "";

    public static final int RESPONSE_OK = 200;
    public static final int NOT_FOUND = 404;

    private static EPAngServiceAPI mInstance;

    public static EPAngServiceAPI getInstance() {
        if (mInstance == null) {
            mInstance = new EPAngServiceAPI();
        }
        return mInstance;
    }

    private EPAngService getService() {
        Gson gsonConfig = new GsonBuilder()
                .setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES)
                .create();

        HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
        logging.setLevel(HttpLoggingInterceptor.Level.BODY);

        // TODO add username and password validation for this app
//        String credential = appUsername + ":" + appPassword;
//        final String encodedCredential = Base64.encodeToString(credential.getBytes(), Base64.NO_WRAP);
        Interceptor requestInterceptor = new Interceptor() {
            @Override
            public Response intercept(Chain chain) throws IOException {
                Request request = chain.request();
//                if (!TextUtils.isEmpty(encodedCredential)) {
                request = request.newBuilder()
//                            .addHeader("Content-Type", "application/json")
                        .addHeader("Accept", "application/json")
//                            .addHeader("Authorization", "Basic " + encodedCredential)
                        .build();
//                }
                return chain.proceed(request);
            }
        };

        OkHttpClient.Builder httpClient = new OkHttpClient.Builder();
        httpClient.addInterceptor(logging);
        httpClient.addInterceptor(requestInterceptor);
        httpClient.hostnameVerifier((hostname, session) -> true);

        Retrofit.Builder builder = new Retrofit.Builder();
        builder.client(httpClient.build());

        Retrofit retrofit = builder
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create(gsonConfig))
                .build();

        return retrofit.create(EPAngService.class);
    }

    public void getSupportedTrees(GetSupportedTreesCallback callback) {
        Call<TreesResponse> getTreesCall = getService().getTrees();
        getTreesCall.enqueue(new Callback<TreesResponse>() {
            @Override
            public void onResponse(Call<TreesResponse> call, retrofit2.Response<TreesResponse> response) {
                TreesResponse treesResponse = response.body();
                if (treesResponse != null) {
                    callback.onSuccess(treesResponse.getTrees());
                } else {
                    callback.onError(new Exception("Error " + response.code() + " " + response.message()));
                }
            }

            @Override
            public void onFailure(Call<TreesResponse> call, Throwable t) {
                Log.e("ERROR", t.getMessage());
                callback.onError(t);
            }
        });
    }

    public void uploadQSFile(QSFile qsFile, UploadQSFileCallback callback) {
        // create RequestBody instance from file
        RequestBody requestFile = RequestBody.create(MediaType.parse("text"), qsFile.getFile());

        // MultipartBody.Part is used to send also the actual file name
        MultipartBody.Part body =
                MultipartBody.Part.createFormData("qs", qsFile.getName(), requestFile);

        Call<QSFileUploadResponse> uploadFileCall = getService().uploadQSFile(body);
        uploadFileCall.enqueue(new Callback<QSFileUploadResponse>() {
            @Override
            public void onResponse(Call<QSFileUploadResponse> call, retrofit2.Response<QSFileUploadResponse> response) {
                QSFileUploadResponse qsFileUploadResponse = response.body();
                if (qsFileUploadResponse != null) {
                    qsFile.setUUIDToken(qsFileUploadResponse.getToken());
                    callback.onSuccess();
                } else {
                    callback.onError(new Exception("Error " + response.code() + " " + response.message()));
                }
            }

            @Override
            public void onFailure(Call<QSFileUploadResponse> call, Throwable t) {
                Log.e("ERROR", t.getMessage());
                callback.onError(t);
            }
        });
    }

    public void runAnalysisWithTextResult(String treeName, String uploadedQSFileUUID,
                                          RunAnalysisWithTextResultCallback callback) {
        Call<EPAngData> call = getService().runAnalysisWithTextResult(treeName, uploadedQSFileUUID);
        call.enqueue(new Callback<EPAngData>() {
            @Override
            public void onResponse(Call<EPAngData> call, retrofit2.Response<EPAngData> response) {
                EPAngData data = response.body();
                if (data != null) {
                    callback.onSuccess(data);
                } else {
                    callback.onError(new Exception("Error " + response.code() + " " + response.message()));
                }
            }

            @Override
            public void onFailure(Call<EPAngData> call, Throwable t) {
                Log.e("ERROR", t.getMessage());
                callback.onError(t);
            }
        });
    }

    public void runAnalysisWithGraphicResult(String treeName, String uploadedQSFileUUID,
                                             RunAnalysisWithGraphicResultCallback callback) {
        Call<ResponseBody> call = getService().runAnalysisWithGraphicalResult(treeName, uploadedQSFileUUID);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, retrofit2.Response<ResponseBody> response) {
                ResponseBody data = response.body();
                if (data != null) {
                    Bitmap bitmap = BitmapFactory.decodeStream(response.body().byteStream());
                    callback.onSuccess(bitmap);
                } else {
                    callback.onError(new Exception("Error " + response.code() + " " + response.message()));
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Log.e("ERROR", t.getMessage());
                callback.onError(t);
            }
        });
    }

}
