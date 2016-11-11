package com.yl.rxjava_retrofit.bean;

import okhttp3.MediaType;
import okhttp3.ResponseBody;
import okio.BufferedSource;

/**
 * Created by Administrator on 2016/11/11 0011.
 */

public class BeanDemo extends ResponseBody {
    @Override
    public MediaType contentType() {
        return null;
    }

    @Override
    public long contentLength() {
        return 0;
    }

    @Override
    public BufferedSource source() {
        return null;
    }
}
