package com.amz4seller.accessreadcode;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import static com.amz4seller.accessreadcode.UrlConst.REQUEST_URL;

/**
 * Retrofit2 rxjava2 oktthp3 构造的单例网络请求
 * <p>
 * 网络请求
 */
public class RetrofitService implements ChangeNetType {

    /**
     * 单例
     */
    private static RetrofitService Instance = null;

    /**
     * okhttp3
     */
    private OkHttpClient okHttpClient;

    /**
     * retrofit
     */
    private Retrofit retrofit;


    /**
     * 获取单例
     *
     * @return RetrofitService
     */
    public static RetrofitService getInstance() {
        if (Instance == null) {
            Instance = new RetrofitService();
        }

        return Instance;
    }


    /**
     * 构造请求
     */
    private RetrofitService() {
        okHttpClient = new OkHttpClient();
        OkHttpClient.Builder builder = new OkHttpClient.Builder();
               /*  OkHttpClient.Builder builder = new OkHttpClient.Builder();*/
        builder.connectTimeout(10, TimeUnit.SECONDS);
        builder.readTimeout(3, TimeUnit.SECONDS);
        builder.writeTimeout(3, TimeUnit.SECONDS);
        HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
        logging.setLevel(HttpLoggingInterceptor.Level.BODY);
        builder.addInterceptor(logging);

           /* builder.addInterceptor(new AddCookiesInterceptor(context)); // VERY VERY IMPORTANT
            builder.addInterceptor(new ReceivedCookiesInterceptor(context)); // VERY VERY IMPORTANT*/


        okHttpClient = builder.build();
        String url = REQUEST_URL;

        retrofit = new Retrofit.Builder()
                .baseUrl(url)
                .client(okHttpClient)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
    }

    /**
     * 创建请求
     *
     * @param clazz 模板
     * @param <T>   模板类
     * @return 不同业务的请求service
     */
    public <T> T createApi(Class<T> clazz) {
        return retrofit.create(clazz);
    }

    @Override
    public void notifyChange() {
        Instance = new RetrofitService();
    }
}
