package com.example.zhiwei.readbook.RxJava_Retrofit;

import com.example.zhiwei.readbook.RxJava_Retrofit.model.GankContentResult;
import retrofit2.http.GET;
import retrofit2.http.Path;
import rx.Observable;

/**
 * Created by zhiwei on 2016/7/24.
 */
public interface GankApi {

    //http://gank.io/api/data/Android/10/1

    @GET("{type}/{number}/{page}")
    Observable<GankContentResult> getGankContent(@Path("type") String type,@Path("number") int number,@Path("page") int page);

}
