package com.example.newsapp;

import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.appcompat.widget.SearchView;

import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;
import com.example.newsapp.api.ApiClient;
import com.example.newsapp.api.ApiInterFace;
import com.example.newsapp.models.Article;
import com.example.newsapp.models.News;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity implements SwipeRefreshLayout.OnRefreshListener {

    public static final String API_KEY= "2c034212f7614bac9a7778c044333f70";
    private RecyclerView recyclerView;
    private RecyclerView.LayoutManager layoutManager;
    private List<Article> articles = new ArrayList<>();
    private Adapter adapter;
    private TextView topHeadline;
    private SwipeRefreshLayout swipeRefreshLayout;
    private RelativeLayout errorLayout;
    private ImageView errorImg;
    private TextView errorMsg, errorTitle;
    private Button btnRetry;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        topHeadline = findViewById(R.id.topHeadline);

        swipeRefreshLayout = findViewById(R.id.swipe_refresh_layout);
        swipeRefreshLayout.setOnRefreshListener(this);

        recyclerView = findViewById(R.id.recyclerView);
        layoutManager = new LinearLayoutManager(MainActivity.this);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setNestedScrollingEnabled(false);

        onLoadingSwipeRefresh("");

        errorLayout=findViewById(R.id.errorLayout);
        errorImg=findViewById(R.id.errorImage);
        errorTitle=findViewById(R.id.errorTitle);
        errorMsg=findViewById(R.id.errorMsg);
        btnRetry=findViewById(R.id.btnRetry);
    }

    public void LoadJson(final String keyword){

        errorLayout.setVisibility(View.GONE);
        swipeRefreshLayout.setRefreshing(true);

        ApiInterFace apiInterFace = ApiClient.getApiClient().create(ApiInterFace.class);

        String country = Utils.getCountry();
        String language = Utils.getLanguage();

        Call<News> call;

        if (keyword.length()>0){
            call = apiInterFace.getNewsSearch(keyword,language,"publishedAt",API_KEY);
        } else {
            call = apiInterFace.getNews(country,API_KEY);

        }


        call.enqueue(new Callback<News>() {
            @Override
            public void onResponse(Call<News> call, Response<News> response) {
                if(response.isSuccessful() && response.body().getArticles()!=null){
                    if(!articles.isEmpty()){
                        articles.clear();
                    }
                    articles = response.body().getArticles();
                    adapter = new Adapter(articles, MainActivity.this);
                    recyclerView.setAdapter(adapter);
                    adapter.notifyDataSetChanged();

                    topHeadline.setVisibility(View.VISIBLE);
                    swipeRefreshLayout.setRefreshing(false);
                    initListener();
                }
                else {
                    topHeadline.setVisibility(View.INVISIBLE);
                    swipeRefreshLayout.setRefreshing(false);
                    String errorCode;
                    switch (response.code()){
                        case 404:
                            errorCode="404 Not Found";
                            break;
                        case 500:
                            errorCode="500 Server Break Down";
                            break;
                            default:
                                errorCode="Unknown Error";
                                break;
                    }
                    showErrorMessage(R.drawable.nothing,
                            "No Result",
                            "Retry again\n"
                            +errorCode);
                }
            }

            @Override
            public void onFailure(Call<News> call, Throwable t) {
                topHeadline.setVisibility(View.INVISIBLE);
                swipeRefreshLayout.setRefreshing(false);
                showErrorMessage(R.drawable.nothing,
                        "Oops..",
                        "Please check network Connection\n"+
                                t.toString());

            }
        });

    }

    private void initListener(){

        adapter.setOnItemClickListener(new Adapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                ImageView imageView = view.findViewById(R.id.img);
                Intent intent = new Intent(MainActivity.this, NewsDetailActivity.class);

                Article article = articles.get(position);
                intent.putExtra("url", article.getUrl());
                intent.putExtra("title", article.getTitle());
                intent.putExtra("img",  article.geturlToImage());
                intent.putExtra("date",  article.getPublishedAt());
                intent.putExtra("source",  article.getSource().getName());
                intent.putExtra("author",  article.getAuthor());

               startActivity(intent);

            }
        });

    }

        @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_main,menu);
        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        final SearchView searchView = (SearchView) menu.findItem(R.id.action_search).getActionView();
        MenuItem searchMenuItem = menu.findItem(R.id.action_search);
        MenuItem Exit=menu.findItem(R.id.action_Exit);



        searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));
        searchView.setQueryHint("Search");
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                if (query.length() > 2){
                    onLoadingSwipeRefresh(query);
                }
                else {
                    Toast.makeText(MainActivity.this, "Type more than two letters!", Toast.LENGTH_SHORT).show();
                }
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                
                return false;
            }
        });

        searchMenuItem.getIcon().setVisible(false, false);

        return true;
    }
    public void onExit() {
        try {
            trimCache(this);
            Log.v("Hi","onDestroy called");
        }catch (Exception e){
            e.printStackTrace();
        }
        finish();
        System.exit(0);
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.action_Exit:
                onExit();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onStop()
    {
        super.onStop();
    }

    @Override
    protected void onDestroy()
    {
        super.onDestroy();
        try {
            trimCache(this);
            Log.v("Hi","onDestroy called");
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public static void trimCache(Context context){
        try {
            File dir= context.getCacheDir();
            if(dir!=null && dir.isDirectory()){
                deleteDir(dir);
            }
        }catch (Exception e){

        }
    }

    public static boolean deleteDir(File dir)
    {
        if (dir!=null && dir.isDirectory())
        {
            String[] children = dir.list();
            for(int i =0;i<children.length;i++) {
                boolean success = deleteDir(new File(dir,children[i]));
                if(!success){
                    return false;
            }
            }
        }
        return dir.delete();

    }
    @Override
    public void onRefresh() {
        LoadJson("");
    }

    private void onLoadingSwipeRefresh(final String keyword)
    {
        swipeRefreshLayout.post(
                new Runnable() {
                    @Override
                    public void run() {
                            LoadJson(keyword);
                    }
                }
        );
    }

    private void showErrorMessage(int imageView,String title, String message){
        if(errorLayout.getVisibility()==View.GONE) {
            errorLayout.setVisibility(View.VISIBLE);
        }
        errorImg.setImageResource(imageView);
        errorTitle.setText(title);
        errorMsg.setText(message);
        btnRetry.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onLoadingSwipeRefresh("");
            }
        });
    }
}
