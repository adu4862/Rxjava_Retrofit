package com.yl.rxjava_retrofit;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.yl.rxjava_retrofit.bean.Course;
import com.yl.rxjava_retrofit.bean.Student;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import rx.Observable;
import rx.Observer;
import rx.Subscriber;
import rx.functions.Action1;
import rx.functions.Func1;

public class MainActivity extends AppCompatActivity {


    private static final String TAG = "MainActivity";
    @BindView(R.id.btn_arr)
    Button btnArr;
    @BindView(R.id.btn_image)
    Button btnImage;
    @BindView(R.id.change)
    Button change;
    @BindView(R.id.muti_change)
    Button mutiChange;

    String[] arr = {"afdsa", "bfdsa", "cfda"};
    @BindView(R.id.imageView)
    ImageView imageView;
    @BindView(R.id.btn_jump)

    Button btnJump;
    private Student zhangsan;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        initData();

    }

    private void initData() {
        Course yuwen = new Course("语文", 1);
        Course shuxue = new Course("数学", 2);
        Course yingyu = new Course("英文", 3);
        Course lishi = new Course("历史", 4);
        Course zhengzhi = new Course("政治", 5);
        Course xila = new Course("希腊语", 6);

        ArrayList<Course> course1 = new ArrayList<>();
        course1.add(yuwen);
        course1.add(shuxue);
        course1.add(yingyu);
        course1.add(lishi);
        course1.add(zhengzhi);
        course1.add(xila);
        zhangsan = new Student("zhangsan", course1);
    }

    @OnClick({R.id.btn_arr, R.id.btn_image, R.id.change, R.id.muti_change,R.id.btn_jump})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_arr:
                test1();
                break;
            case R.id.btn_image:
                test2();
                break;
            case R.id.change:
                test3();
                break;
            case R.id.muti_change:
                test4();
                break;
            case R.id.btn_jump:
                test5();
                break;
        }
    }

    private void test5() {
        startActivity(new Intent(this,IPActivity.class));
    }

    /**
     * 数据变换：一对多；一个学生对应多个学科,打印出每个学科
     */
    private void test4() {
        Observable
                .just(zhangsan)//创建被观察者
                .flatMap(new Func1<Student, Observable<Course>>() {//一对多替换(一个Student对象转换成多个Course对象)
                    @Override
                    public Observable<Course> call(Student student) {
                        return Observable.from(student.getCourses());
                    }
                })
                .subscribe(new Action1<Course>() {
                    @Override
                    public void call(Course course) {
                        Log.e(TAG, "call: " + course.getName());
                    }
                });

    }

    /**
     * 数据变换：一对一
     */
    private void test3() {
        Observable
                .just(id)//注册被观察者
                .map(new Func1<Integer, Drawable>() {//一对一数据转换
                    @Override
                    public Drawable call(Integer integer) {
                        return getResources().getDrawable(integer);
                    }
                })
                .subscribe(new Action1<Drawable>() {
                    @Override
                    public void call(Drawable drawable) {
                        imageView.setImageDrawable(drawable);
                    }
                });
    }

    /**
     * 由指定的一个 drawable 文件 id 取得图片，并显示在 ImageView 中，并在出现异常的时候打印 Toast 报错
     */
    int id = R.mipmap.ic_launcher;

    private void test2() {
        Observable
                .create(new Observable.OnSubscribe<Drawable>() {//创建订阅者,自己调用观察者里面的逻辑
                    @Override
                    public void call(Subscriber<? super Drawable> subscriber) {
                        Drawable drawable = getResources().getDrawable(id);
                        subscriber.onNext(drawable);
                        subscriber.onCompleted();
                    }
                })
                .subscribe(new Observer<Drawable>() {//创建观察者
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable throwable) {
                        Toast.makeText(MainActivity.this, "报错", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onNext(Drawable drawable) {//处理数据
                        imageView.setImageDrawable(drawable);

                    }
                });
    }

    /**
     * 现有一个数组 String[] arr ={"afdsa", "bfdsa", "cfda"}, 把其中以字母"a"开头的字符串找出来并加上"from Alpha",最后打印出新的字符串的长度
     */
    private void test1() {
        Observable
                .from(arr)//创建被观察者,并关联数据
                .filter(new Func1<String, Boolean>() {//进行过滤
                    @Override
                    public Boolean call(String s) {
                        return s.startsWith("a");
                    }
                })
                .subscribe(new Action1<String>() {//开始订阅
                    @Override
                    public void call(String s) {
                        Log.e(TAG, "call: " + s + " from Alpha");
                    }
                });
    }

}
