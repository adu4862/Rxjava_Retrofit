package com.yl.rxjava_retrofit.Api;

import com.yl.rxjava_retrofit.bean.IPBean;

import retrofit2.http.GET;
import retrofit2.http.Query;
import rx.Observable;

/**
 * 定义网络访问接口
 */

public interface ApiServer {
    //// http://ip.taobao.com/service/getIpInfo.php?ip=202.178.10.23

    /**
     * 查询ip地址的方法
     * @param ip
     * @return 返回Rxjava中的被观察者对象
     */
    @GET("/service/getIpInfo.php")
    public Observable<IPBean> getLocation(@Query("ip") String ip) ;
}
