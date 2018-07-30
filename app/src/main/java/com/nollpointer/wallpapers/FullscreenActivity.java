package com.nollpointer.wallpapers;

import android.app.WallpaperManager;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.AnticipateInterpolator;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.IOException;

import static com.nollpointer.wallpapers.MainActivity.RESULT_CODE;
import static com.nollpointer.wallpapers.WallPaperConstants.FAVOURITE;
import static com.nollpointer.wallpapers.WallPaperConstants.IMAGE;
import static com.nollpointer.wallpapers.WallPaperConstants.THUMBNAIL;


public class FullscreenActivity extends AppCompatActivity {

    private static final int DURATION = 400;

    private boolean isHidden = false;
    private boolean isFavourite;

    private int thumbnailImage;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fullscreen);
        final Toolbar toolbar = findViewById(R.id.toolbar_fullscreen);
        setSupportActionBar(toolbar);

        ActionBar bar = getSupportActionBar();
        bar.setDisplayShowHomeEnabled(true);
        bar.setDisplayHomeAsUpEnabled(true);
        toolbar.setTitle("ASDASD");
        toolbar.setTitleTextColor(getResources().getColor(android.R.color.white));

        final View bottom_view = findViewById(R.id.bottom_toolbar);
        final ImageView image = findViewById(R.id.fullscreen_content);
        Intent intent = getIntent();
        final int imageSource = intent.getIntExtra(IMAGE,R.drawable.ic_favorite);
        thumbnailImage = intent.getIntExtra(THUMBNAIL,R.drawable.ic_favorite);
        isFavourite = intent.getBooleanExtra(FAVOURITE,false);
        image.setImageResource(imageSource);
        image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(isHidden) {
                    toolbar.animate().alpha(1).setDuration(DURATION).setInterpolator(new AnticipateInterpolator()).start();
                    bottom_view.animate().alpha(1).setDuration(DURATION).setInterpolator(new AnticipateInterpolator()).start();
                }else {
                    toolbar.animate().alpha(0).setDuration(DURATION).setInterpolator(new AnticipateInterpolator()).start();
                    bottom_view.animate().alpha(0).setDuration(DURATION).setInterpolator(new AnticipateInterpolator()).start();
                }
                isHidden = !isHidden;
            }
        });
        bottom_view.findViewById(R.id.set_wallpaper_full_screen).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                WallpaperManager myWallpaperManager = WallpaperManager.getInstance(getApplicationContext());
                try {
                    myWallpaperManager.setResource(imageSource);
                    Toast.makeText(FullscreenActivity.this,R.string.toast_text,Toast.LENGTH_SHORT).show();
                } catch (IOException e) {
                    Log.e("WallpaperManager",e.getMessage());
                }
            }
        });
        final ImageButton imageButton = findViewById(R.id.set_favourite_full_screen);
        setFavouriteButtonImage(imageButton);
        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isFavourite = !isFavourite;
                setFavouriteButtonImage(imageButton);
            }
        });
    }

    private void setFavouriteButtonImage(ImageView image){
        if(isFavourite)
            image.setImageResource(R.drawable.ic_favorite_picked);
        else
            image.setImageResource(R.drawable.ic_favorite_clear);
    }

    @Override
    public void onBackPressed() {
        prepareToCloseActivity();
        super.onBackPressed();
        overridePendingTransition(android.R.anim.fade_in,android.R.anim.fade_out);
    }

    private void prepareToCloseActivity(){
        Intent intent = new Intent();
        intent.putExtra(FAVOURITE,isFavourite);
        intent.putExtra(THUMBNAIL,thumbnailImage);
        setResult(RESULT_CODE,intent);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                prepareToCloseActivity();
                finish();
                overridePendingTransition(android.R.anim.fade_in,android.R.anim.fade_out);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
