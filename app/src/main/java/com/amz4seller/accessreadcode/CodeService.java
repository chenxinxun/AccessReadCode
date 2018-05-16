package com.amz4seller.accessreadcode;

import java.util.LinkedList;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

/**
 * Created by cxx on 2017/12/19.
 */

public interface CodeService {
    @POST("irp/sc-code/refresh")
    Call<BaseBean> pushCodeInfos(@Body LinkedList<CodeInfo> codeInfos);
}
