package com.yl.rxjava_retrofit;

import android.app.Activity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.yl.rxjava_retrofit.Api.ApiServer;
import com.yl.rxjava_retrofit.bean.Data;
import com.yl.rxjava_retrofit.bean.IPBean;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import rx.Observer;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

import static android.content.ContentValues.TAG;

/**
 * Retrofit+Rxjava实现ip地址查询
 */

public class IPActivity extends Activity {
    @BindView(R.id.editText)
    EditText editText;
    @BindView(R.id.button)
    Button button;

    String baseUrl = "http://ip.taobao.com";
    @BindView(R.id.textView)
    TextView textView;
    @BindView(R.id.pb)
    ProgressBar pb;
    private ApiServer apiServer;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ip);
        ButterKnife.bind(this);

        Retrofit retrofit = new Retrofit
                .Builder()
                .baseUrl(baseUrl)
                .addConverterFactory(GsonConverterFactory.create())//关联gson转换工厂
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())//关联Rxjava回调适配工厂
                .build();
        //创建网络接口实例
        apiServer = retrofit.create(ApiServer.class);
    }

    @OnClick(R.id.button)
    public void onClick() {
        pb.setVisibility(View.VISIBLE);
        String s = editText.getText().toString();
        if (!TextUtils.isEmpty(s)) {
            apiServer.getLocation(s)
                    .subscribeOn(Schedulers.io())//访问网络的操作放到io线程
                    .map(new Func1<IPBean, String>() {//创建被观察者对象
                        @Override
                        public String call(IPBean ipBean) {
                            Data data = ipBean.getData();
                            return data.getCountry() + "-" + data.getArea() + "--" + data.getCity();
                        }
                    })
                    .observeOn(AndroidSchedulers.mainThread())//处理数据,更新UI的操作放在UI线程
                    .subscribe(new Observer<String>() {//通过订阅处理数据
                        @Override
                        public void onCompleted() {
                            pb.setVisibility(View.GONE);
                            Log.e(TAG, "onCompleted: 执行完毕");
                        }

                        @Override
                        public void onError(Throwable throwable) {
                            Toast.makeText(IPActivity.this, "网络异常", Toast.LENGTH_SHORT).show();
                            pb.setVisibility(View.GONE);
                        }

                        @Override
                        public void onNext(String s) {
                            textView.setText(s);
                        }
                    });
        } else {
            Toast.makeText(this, "ip不能为空", Toast.LENGTH_SHORT).show();
        }


    }
}
