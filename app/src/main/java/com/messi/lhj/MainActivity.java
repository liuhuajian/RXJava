package com.messi.lhj;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import org.json.JSONException;
import org.json.JSONObject;

import rx.Observable;
import rx.Observer;
import rx.Subscriber;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action0;
import rx.functions.Action1;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

public class MainActivity extends AppCompatActivity {

    private Observable<String> observable;
    private Observer<String> observer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        createObserver();
        createObservable();
        initSubscribe();
        initScheduler();
        initChange();
    }

    /**
     * FuncX 和 ActionX 的区别在 FuncX 包装的是有返回值的方法。
     */
    private void initChange() {
        Observable.just("http://www.baidu.com")
                .map(new Func1<String, JSONObject>() {
                    @Override
                    public JSONObject call(String s) {
                        try {
                            return new JSONObject(s);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        return null;
                    }
                })
                .subscribe(new Action1<JSONObject>() {
                    @Override
                    public void call(JSONObject jsonObject) {
                        try {
                            String nihaoa = jsonObject.getString("nihaoa");
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });
    }

    private void initScheduler() {
        //当前线程，相当于不指定线程
        Schedulers.immediate();
        //开启一个新线程，并在新线程中执行
        Schedulers.newThread();
        //io操作(读写文件，读写数据库，网络文件交互等)所使用的scheduler，比newThread更有效率
        //区别：io() 的内部实现是是用一个无数量上限的线程池，可以重用空闲的线程
        Schedulers.io();
        //计算所使用的scheduler
        Schedulers.computation();
        //指定线程为主线程
        AndroidSchedulers.mainThread();
        // * subscribeOn(): 指定 subscribe() 所发生的线程，即 Observable.OnSubscribe 被激活时所处的线程。或者叫做事件产生的线程。
        // * observeOn(): 指定 Subscriber 所运行在的线程。或者叫做事件消费的线程。
        observable.subscribeOn(Schedulers.io())
                .observeOn(Schedulers.immediate())
                .subscribe(new Action1<String>() {
                    @Override
                    public void call(String s) {

                    }
                });

    }

    /**
     * 订阅
     */
    private void initSubscribe() {
        Subscription subscribe = observable.subscribe(observer);
        //取消订阅
        subscribe.unsubscribe();

        //onNext()
        Action1<String> onNextAction = new Action1<String>() {
            @Override
            public void call(String s) {

            }
        };
        //onError
        Action1<Throwable> onErrorAction = new Action1<Throwable>() {
            @Override
            public void call(Throwable throwable) {

            }
        };
        Action0 onCompleteAction = new Action0() {
            @Override
            public void call() {

            }
        };
        observable.subscribe(onNextAction);
        observable.subscribe(onNextAction,onErrorAction);
        observable.subscribe(onNextAction,onErrorAction,onCompleteAction);
    }

    /**
     * 创建被观察者
     */
    private void createObservable() {
        //定义事件触发规则
        observable = Observable.create(new Observable.OnSubscribe<String>() {
            @Override
            public void call(Subscriber<? super String> subscriber) {
                //定义事件触发规则
                subscriber.onNext("nihao");
                subscriber.onNext("wo jiao liuhuajian");
                subscriber.onNext("I love you");
                subscriber.onCompleted();
            }
        });

        //just(T...): 将传入的参数依次发送出来。
        // 将会依次调用：
        // onNext("nihao") onNext("liuhuajian") onNext("I love you") onCompleted();
        Observable<String> observable1 = Observable.just("nihao","liuhuajian","I love you");

        //from(T[]) / from(Iterable<? extends T>) : 将传入的数组或 Iterable 拆分成具体对象后，依次发送出来。
        String[] words = {"liumang","shuaige","jianren"};
        Observable observable2 = Observable.from(words);
    }

    /**
     * 创建观察者
     */
    private void createObserver() {
        observer = new Observer<String>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onNext(String s) {

            }
        };
        Subscriber<String> subscriber = new Subscriber<String>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onNext(String s) {

            }

            @Override
            public void onStart() {
                super.onStart();
            }

        };
        //判断状态
        subscriber.isUnsubscribed();
        //取消订阅，解除引用关系，避免内存泄漏
        subscriber.unsubscribe();
    }

}
