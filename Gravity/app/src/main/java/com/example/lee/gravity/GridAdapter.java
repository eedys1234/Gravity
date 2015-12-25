package com.example.lee.gravity;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by lee on 2015-12-24.
 */
public class GridAdapter extends ArrayAdapter<GridItem> {

    private LayoutInflater mInflater = null;  // layinflater로 custom layout 구성
    private Context mContext = null;

    public GridAdapter(Context context, int resource, ArrayList<GridItem> list) {
        super(context, resource, list);

        mInflater = LayoutInflater.from(context);
        mContext = context;
    }

    public View getView(int position, View convertView, ViewGroup parent) {

        Holder holder;

        if(convertView==null) {
            convertView = mInflater.inflate(R.layout.depart, null);
            holder = new Holder();
            holder.imageView = (ImageView)convertView.findViewById(R.id.imgDepart);
            holder.txtDepart = (TextView)convertView.findViewById(R.id.DepartView);
            convertView.setTag(holder);
        }
        // 홀더를 이용하여 저장
        holder = (Holder) convertView.getTag();
        GridItem item =  getItem(position);
        holder.imageView.setImageResource(item.getImg());
        holder.txtDepart.setText(item.getDepart());

        return convertView;
    }


    public class Holder {

        public ImageView imageView;
        public TextView txtDepart;

    }
}
