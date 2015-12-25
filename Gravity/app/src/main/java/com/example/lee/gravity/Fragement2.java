package com.example.lee.gravity;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;

import java.util.ArrayList;

/**
 * Created by lee on 2015-12-24.
 */
public class Fragement2 extends Fragment {

    GridView gridView;
    ArrayList<GridItem> list;
    GridAdapter gridAdapter;
    String[] strDeparts = {"기획", "개발", "디자인", "멘토"};
    int[] IntDeparts = {R.drawable.pm, R.drawable.develop,R.drawable.ui,R.drawable.mento};

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){

        View view = inflater.inflate(R.layout.activity_depart, container, false);
        gridView = (GridView)view.findViewById(R.id.gridView);

        list = new ArrayList<GridItem>();
        gridAdapter = new GridAdapter(getActivity(), R.layout.depart, list);

        gridView.setAdapter(gridAdapter);


        for(int i=0;i<strDeparts.length;i++) {
            GridItem item = new GridItem();
            item.setDepart(strDeparts[i]);
            item.setImg(IntDeparts[i]);
            list.add(item);
        }

        Log.i("로그", "log");
        gridAdapter.notifyDataSetChanged();

        return view;

    }

    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

        if (id == R.id.action_settings) {
            Intent it = new Intent(getActivity(), MainActivity.class);
            startActivityForResult(it, 3);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
