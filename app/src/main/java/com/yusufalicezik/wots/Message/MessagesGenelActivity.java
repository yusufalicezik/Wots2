package com.yusufalicezik.wots.Message;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import com.ittianyu.bottomnavigationviewex.BottomNavigationViewEx;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.yusufalicezik.wots.R;
import com.yusufalicezik.wots.Utils.BottomNavigationViewHelper;
import com.yusufalicezik.wots.Utils.UniversalImagLoader;

public class MessagesGenelActivity extends AppCompatActivity {



    private int ACTIVITY_NO=3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_messages_genel);


        BottomMenuyuAyarla();
        UniversalInit();
    }


    private void BottomMenuyuAyarla() {


        BottomNavigationViewEx bottomNavigationViewEx=findViewById(R.id.bottomNavigationView);

        BottomNavigationViewHelper bottomNavigationViewHepler=new BottomNavigationViewHelper(bottomNavigationViewEx);
        bottomNavigationViewHepler.setupNavigation(getApplicationContext(),bottomNavigationViewEx);

        Menu menu=bottomNavigationViewEx.getMenu();
        MenuItem menuItem=menu.getItem(ACTIVITY_NO);
        menuItem.setChecked(true);


    }

    private void UniversalInit() {
        UniversalImagLoader universalImageLoader = new UniversalImagLoader(getApplicationContext());
        ImageLoader.getInstance().init(universalImageLoader.getConfig());
    }
}
