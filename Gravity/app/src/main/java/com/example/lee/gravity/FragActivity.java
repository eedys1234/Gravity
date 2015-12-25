package com.example.lee.gravity;

import android.content.Context;
import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

import java.util.Locale;

public class FragActivity extends FragmentActivity {

    //Fragment 선택 정수
    Button btnTotalPeople;
    Button btnDepartment;
    ViewPager mViewPager;
    SectionPageAdapter mSectionsPagerAdapter;
    LinearLayout btnLayout1;
    LinearLayout btnLayout2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_frag);

        btnTotalPeople = (Button)findViewById(R.id.btnTotalPeople);
        btnDepartment = (Button)findViewById(R.id.btnDepartment);
        btnLayout1 = (LinearLayout)findViewById(R.id.btnLayout1);
        btnLayout2 = (LinearLayout)findViewById(R.id.btnLayout1);

        mSectionsPagerAdapter = new SectionPageAdapter(getSupportFragmentManager());

        mViewPager = (ViewPager)findViewById(R.id.viewPager);
        mViewPager.setAdapter(mSectionsPagerAdapter);

        btnTotalPeople.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {

                    mViewPager.setCurrentItem(0);

            }
        });

        btnDepartment.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {
                mViewPager.setCurrentItem(1);

            }
        });

        mViewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {    //아이템이 변경되면
            @Override
            public void onPageSelected(int position) {
            }

            @Override
            public void onPageScrolled(int position, float positionOffest, int positionOffsetPixels) {
            }

            @Override
            public void onPageScrollStateChanged(int state) {
            }
        });



    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {

            return  true;
        }
        //noinspection SimplifiableIfStatement
        if (id == R.id.menuAdd) {
            Intent it = new Intent(FragActivity.this, MainActivity.class);
            startActivityForResult(it, 3);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
