package com.nusry.text;

import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.viewpager.widget.ViewPager;

import com.nusry.text.adapter.PageAdapter;
import com.nusry.text.animation.ZoomOutPageTransformer;
import com.nusry.text.R;


public class MainActivity extends AppCompatActivity {

    private ViewPager viewPager;
    private PageAdapter pageAdapter;

    private String tag_ResultLayout;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Window window = MainActivity.this.getWindow();
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.setStatusBarColor(ContextCompat.getColor(MainActivity.this,R.color.colorPrimary));
        setContentView(R.layout.activity_main);
        viewPager = findViewById(R.id.viewPager);

        pageAdapter = new PageAdapter(getSupportFragmentManager(), 2);
        viewPager.setAdapter(pageAdapter);

        viewPager.setPageTransformer(true, new ZoomOutPageTransformer());
    }

    public void openMainLayout(){
        viewPager.setCurrentItem(0,true);
    }

    public void openResultLayout(){
        viewPager.setCurrentItem(1,true);
    }

    public void setTag_ResultLayout(String tag_ResultLayout){
        this.tag_ResultLayout = tag_ResultLayout;
    }

    public String getTag_ResultLayout(){
        return tag_ResultLayout;
    }

    @Override
    public void onBackPressed() {
        if (viewPager.getCurrentItem() == 1){
            openMainLayout();
        }
        else{
            super.onBackPressed();
        }
    }



}
