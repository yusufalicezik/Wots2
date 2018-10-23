package com.yusufalicezik.wots.Utils;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.view.MenuItem;

import com.ittianyu.bottomnavigationviewex.BottomNavigationViewEx;
import com.yusufalicezik.wots.Home.MainActivity;
import com.yusufalicezik.wots.Message.MessagesGenelActivity;
import com.yusufalicezik.wots.Profile.ProfileActivity;
import com.yusufalicezik.wots.R;
import com.yusufalicezik.wots.Search.SearchActivity;
import com.yusufalicezik.wots.Share.ShareActivity;

public class BottomNavigationViewHelper {

    public BottomNavigationViewHelper(BottomNavigationViewEx bottomNavigationViewEx)
    {
        bottomNavigationViewEx.enableAnimation(false);
        bottomNavigationViewEx.setTextVisibility(false);
        bottomNavigationViewEx.enableShiftingMode(false);
        bottomNavigationViewEx.enableItemShiftingMode(false);
    }

    public void setupNavigation(final Context context, BottomNavigationViewEx bottomNavigationViewEx)
    {
        bottomNavigationViewEx.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                switch (item.getItemId())
                {
                    case R.id.ic_home:
                    {
                        Intent intent=new Intent(context, MainActivity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);

                        context.startActivity(intent);

                        return true;
                    }
                    case R.id.ic_search:
                    {
                        Intent intent=new Intent(context, SearchActivity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);

                        context.startActivity(intent);
                        return true;
                    }
                    case R.id.ic_share:
                    {
                        Intent intent=new Intent(context, ShareActivity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);

                        context.startActivity(intent);
                        return true;
                    }
                    case R.id.ic_news:
                    {
                        Intent intent=new Intent(context, MessagesGenelActivity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);

                        context.startActivity(intent);
                        return true;
                    }
                    case R.id.ic_profile:
                    {
                        Intent intent=new Intent(context, ProfileActivity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);

                        context.startActivity(intent);
                        return true;
                    }



                }

                return false;
            }
        });
    }

}
