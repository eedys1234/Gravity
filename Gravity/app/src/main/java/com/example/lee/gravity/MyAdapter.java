package com.example.lee.gravity;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Random;

/**
 * Created by lee on 2015-12-22.
 */
public class MyAdapter extends ArrayAdapter<Item> {

    Context mContext = null;
    LayoutInflater layoutInflater = null;
    int[] IntDeparts = {R.drawable.user2, R.drawable.user3,R.drawable.user4,R.drawable.user5,R.drawable.user6
            ,R.drawable.user7};

    public MyAdapter(Context context, int resource, ArrayList list) {
        super(context, resource, list);
        this.layoutInflater =  LayoutInflater.from(context);
        this.mContext = context;
    }

    public View getView(int position, View convertView, ViewGroup parent) {

        Holder holder = null;
        Random random = new Random();

        if(convertView == null)
        {
            holder = new Holder();
            convertView = layoutInflater.inflate(R.layout.student, null);
            holder.txtDate = (TextView)convertView.findViewById(R.id.strDate);
            holder.txtName = (TextView)convertView.findViewById(R.id.strName);
            holder.txtNameView = (TextView)convertView.findViewById(R.id.strNameView);
            holder.txtNum = (TextView)convertView.findViewById(R.id.strNum);
            holder.txtNumView = (TextView)convertView.findViewById(R.id.strNumView);
            holder.txtGrade = (TextView)convertView.findViewById(R.id.strGrade);
            holder.txtGradeView = (TextView)convertView.findViewById(R.id.strGradeView);
            holder.txtDepart = (TextView)convertView.findViewById(R.id.strDepart);
            holder.txtDepartView = (TextView)convertView.findViewById(R.id.strDepartView);
            holder.img = (ImageView)convertView.findViewById(R.id.imgView);
            convertView.setTag(holder);
        }

        holder =(Holder) convertView.getTag();
        Item item = getItem(position);
        holder.txtNameView.setText(item.getName());
        holder.txtNumView.setText(item.getNum());
        holder.txtGradeView.setText(item.getGrade());
        holder.txtDepartView.setText(item.getDepart());
        holder.txtDate.setText(item.getRegDate());
        holder.img.setImageResource(IntDeparts[random.nextInt(5)]);
        return convertView;

    }

    class Holder
    {
        ImageView img;
        TextView txtDate;
        TextView txtName;
        TextView txtNameView;
        TextView txtNum;
        TextView txtNumView;
        TextView txtGrade;
        TextView txtGradeView;
        TextView txtDepart;
        TextView txtDepartView;

    }
}
