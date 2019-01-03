package com.svenj.demo.fastjsonconvert;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.svenj.converterfastjson.FastJsonConverterFactory;
import com.svenj.demo.fastjsonconvert.github.GitHubService;
import com.svenj.demo.fastjsonconvert.github.Repo;

import java.util.List;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.functions.Predicate;
import io.reactivex.schedulers.Schedulers;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;

public class MainActivity extends AppCompatActivity {

    Disposable mGitHubRequest;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor(
                new HttpLoggingInterceptor.Logger() {
                    @Override
                    public void log(String message) {
                        Log.i("OkHttpLog", message);
                    }
                });
        loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);

        OkHttpClient okHttpClient = new OkHttpClient.Builder()
                .addInterceptor(loggingInterceptor)
                .build();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://api.github.com")
                .client(okHttpClient)
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addConverterFactory(FastJsonConverterFactory.create())
                .build();

        GitHubService service = retrofit.create(GitHubService.class);

        mGitHubRequest = service.getRepos("svenjung", "all")
                .flatMap(new Function<List<Repo>, Observable<Repo>>() {
                    @Override
                    public Observable<Repo> apply(List<Repo> repos) throws Exception {
                        Log.i("OkHttpLog", "repo size = " + repos.size());
                        return Observable.fromIterable(repos);
                    }
                })
                .filter(new Predicate<Repo>() {
                    @Override
                    public boolean test(Repo repo) throws Exception {
                        return true;
                    }
                })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<Repo>() {
                    @Override
                    public void accept(Repo repo) throws Exception {
                        Log.i("OkHttpLog", " -- > " + repo.toString());
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        Log.i("OkHttpFailed", "get repos failed, ", throwable);
                    }
                });
    }

    @Override
    protected void onDestroy() {
        if (mGitHubRequest != null && !mGitHubRequest.isDisposed()) {
            mGitHubRequest.dispose();
        }
        super.onDestroy();
    }
}
