package org.hits.epa_ng_android.network;

import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.logging.HttpLoggingInterceptor;
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

    public EPAngService getService() {

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

}
