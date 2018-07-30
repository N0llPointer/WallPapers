package com.nollpointer.wallpapers;

import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.util.ArraySet;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;

import java.util.Set;
import java.util.TreeMap;

import static com.nollpointer.wallpapers.WallPaperConstants.FAVOURITE;
import static com.nollpointer.wallpapers.WallPaperConstants.IMAGE;
import static com.nollpointer.wallpapers.WallPaperConstants.THUMBNAIL;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener,ImageCardsAdapter.Listener {

    public static final int RESULT_CODE=11;

    RecyclerView recyclerView;
    ImageCardsAdapter adapter;

    TreeMap<Integer,Integer> images;   // Ключ - картинка для превью, значение - само изображение
    TreeMap<Integer,Boolean> favouriteImages; //Ключ - картинка для превью, значение - добавленно ли изображение в избранное

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        navigationView.setCheckedItem(R.id.nav_new);

        images = new TreeMap<>();
        favouriteImages = new TreeMap<>();
        fillData();

        adapter = new ImageCardsAdapter(images.keySet(),this);

        recyclerView = findViewById(R.id.main_recycler);
        recyclerView.setLayoutManager(new GridLayoutManager(this,2));
        recyclerView.setItemViewCacheSize(8);
        recyclerView.setDrawingCacheEnabled(true);
        recyclerView.setDrawingCacheQuality(View.DRAWING_CACHE_QUALITY_HIGH);
        recyclerView.setAdapter(adapter);
    }

    //Заполнить списки данными
    private void fillData(){
        images.put(R.drawable.thumbnail1,R.drawable.wallpaper1);
        images.put(R.drawable.thumbnail2,R.drawable.wallpaper2);
        images.put(R.drawable.thumbnail3,R.drawable.wallpaper3);
        images.put(R.drawable.thumbnail4,R.drawable.wallpaper4);
        images.put(R.drawable.thumbnail5,R.drawable.wallpaper5);
        images.put(R.drawable.thumbnail6,R.drawable.wallpaper6);
        images.put(R.drawable.thumbnail7,R.drawable.wallpaper7);
        images.put(R.drawable.thumbnail8,R.drawable.wallpaper8);

        favouriteImages.put(R.drawable.thumbnail1,false);
        favouriteImages.put(R.drawable.thumbnail2,false);
        favouriteImages.put(R.drawable.thumbnail3,false);
        favouriteImages.put(R.drawable.thumbnail4,false);
        favouriteImages.put(R.drawable.thumbnail5,false);
        favouriteImages.put(R.drawable.thumbnail6,false);
        favouriteImages.put(R.drawable.thumbnail7,false);
        favouriteImages.put(R.drawable.thumbnail8,false);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        switch (id){
            case R.id.nav_new:
                adapter.refresh(images.keySet(),favouriteImages.values());
                break;
            case R.id.nav_favourite:
                adapter.setOnlyFavouriteImages(getFavouriteImages());
                break;
        }

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    //Выборка из favouriteImages только тех элементов, которые добавлены в избранное
    private Set<Integer> getFavouriteImages(){
        Set<Integer> set = new ArraySet<>();
        Set<Integer> keys = favouriteImages.keySet();
        for(Integer key: keys){
            if(favouriteImages.get(key))
                set.add(key);
        }
        return set;
    }

    @Override
    public void onImageClicked(int source) {
        Intent intent = new Intent(this,FullscreenActivity.class);
        intent.putExtra(IMAGE,images.get(source));
        intent.putExtra(FAVOURITE,favouriteImages.get(source));
        intent.putExtra(THUMBNAIL,source);
        startActivityForResult(intent,RESULT_CODE);
        overridePendingTransition(android.R.anim.fade_in,android.R.anim.fade_out);
    }

    @Override
    public void onFavouriteClicked(int source, boolean state) {
        favouriteImages.put(source,state);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        boolean state = data.getBooleanExtra(FAVOURITE,false);
        int thumbnail = data.getIntExtra(THUMBNAIL,R.drawable.thumbnail1);
        favouriteImages.put(thumbnail,state);
        adapter.refreshItem(thumbnail,state);
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        switch (newConfig.orientation){
            case Configuration.ORIENTATION_LANDSCAPE:
                recyclerView.setLayoutManager(new GridLayoutManager(this,3));
                break;

            case Configuration.ORIENTATION_PORTRAIT:
                recyclerView.setLayoutManager(new GridLayoutManager(this,2));
                break;
        }
    }
}
