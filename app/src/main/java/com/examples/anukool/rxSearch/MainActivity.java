package com.examples.anukool.rxSearch;

import android.os.Bundle;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Predicate;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity {

    public static final String BASE_URL = "https://api.github.com/";
    private RecyclerView mRecyclerView;
    private DataAdapter mAdapter;
    private TextView topTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        initViews();



    }

    private void initViews(){
        mRecyclerView = (RecyclerView)findViewById(R.id.card_recycler_view);
        topTextView = (TextView)findViewById(R.id.text_top);
        mRecyclerView.setHasFixedSize(true);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(layoutManager);
        mAdapter = new DataAdapter();
        mRecyclerView.setAdapter(mAdapter);

        initRetrofit();
    }

    RequestInterface request;
    private void initRetrofit(){
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        request = retrofit.create(RequestInterface.class);
    }
    private void  loadJSON(String query){

        Map<String, String> queryMap = new HashMap<>();
        queryMap.put("q", query);
        queryMap.put("sort", "followers");

        Call<GitJsonResponse> call = request.getJSON(queryMap);
        call.enqueue(new Callback<GitJsonResponse>() {
            @Override
            public void onResponse(Call<GitJsonResponse> call, Response<GitJsonResponse> response) {

                Log.e("Response", "test");
                if(response.body() != null){
                    GitJsonResponse jsonResponse = response.body();
                    if (jsonResponse != null) {
                        mAdapter.updateData(jsonResponse.getItems());
                    }
                }

            }

            @Override
            public void onFailure(Call<GitJsonResponse> call, Throwable t) {
                Log.d("Error",t.getMessage());
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.menu_main, menu);

        MenuItem search = menu.findItem(R.id.search);
        SearchView searchView = (SearchView) MenuItemCompat.getActionView(search);
//        search(searchView);
        rxJavaSearchwithDebounce(searchView);
        return true;
    }


    private static int apiCalls = 0;
    private void rxJavaSearchwithDebounce(SearchView searchView){
        RxSearch.fromSearchView(searchView)
                .debounce(300, TimeUnit.MILLISECONDS)
                .filter(new Predicate<String>() {
                    @Override
                    public boolean test(@NonNull String item) throws Exception {
                        return item.length() > 2;
                    }
                })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<String>() {
                    @Override
                    public void accept(@NonNull String query) throws Exception {
                        loadJSON(query);
                        topTextView.setText("API CALLS: " + ++apiCalls);
                    }
                });

    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        return super.onOptionsItemSelected(item);
    }






}
