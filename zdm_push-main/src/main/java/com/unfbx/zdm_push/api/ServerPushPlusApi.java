package com.unfbx.zdm_push.api;

import com.github.lianjiatech.retrofit.spring.boot.annotation.RetrofitClient;
import com.unfbx.zdm_push.constant.ServerPushPlusResponse;
import retrofit2.http.Body;
import retrofit2.http.POST;
import retrofit2.http.QueryMap;

import java.util.Map;

/**
 * @Description
 * @Author Grt
 * @Date 2020-12-11
 */
@RetrofitClient(baseUrl = "http://www.pushplus.plus/")
public interface ServerPushPlusApi {
    @POST("send")
    ServerPushPlusResponse sendToServerPushPlus(@Body Map<String,String> param);
}
