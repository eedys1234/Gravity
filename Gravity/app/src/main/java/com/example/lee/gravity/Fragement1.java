package com.example.lee.gravity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;


import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Member;
import java.util.ArrayList;

/**
 * Created by lee on 2015-12-24.
 */
public class Fragement1 extends Fragment {

    ListView listView;
    TextView txtTotal;
    int cnt = 10;
    int offset = 0;
    int count = 0;
    ArrayList<Item> list;
    MyAdapter myAdapter;
    boolean is_scroll;
    boolean is_refresh;
    View view;
    SwipeRefreshLayout swipe_refresh;

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.activity_view, container, false);
        txtTotal = (TextView) view.findViewById(R.id.txtTotal);
        listView = (ListView) view.findViewById(R.id.listView);
        list = new ArrayList<Item>();

        myAdapter = new MyAdapter(getActivity(), R.layout.student, list);

        listView.setAdapter(myAdapter);
        new NetworkSearch().execute();

        listView.setOnScrollListener(new AbsListView.OnScrollListener() {

            public void onScrollStateChanged(AbsListView view, int scrollState) {

            }

            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {

                if (firstVisibleItem + visibleItemCount == totalItemCount) {
                    if (count != 0 && offset % cnt == 0) {
                        if (is_scroll) {
                            is_scroll = false;
                            is_refresh = false;
                            new NetworkSearch().execute("");

                        }
                    }
                }
            }
        });
        swipe_refresh = (SwipeRefreshLayout) view.findViewById(R.id.swipe_refresh_widget);
        swipe_refresh.setColorSchemeColors(Color.GREEN, Color.RED, Color.BLUE);
        swipe_refresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {

            public void onRefresh() {

                init();
                myAdapter.notifyDataSetChanged();
                new NetworkSearch().execute("");

            }
        });

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                Item item = list.get(position);
                Intent it = new Intent(getActivity(), UpdateActivity.class);
                it.putExtra("name", item.getName());
                it.putExtra("num", item.getNum());
                it.putExtra("grade", item.getGrade());
                it.putExtra("depart", item.getDepart());
                startActivityForResult(it, 3);

            }
        });

        return view;
    }


    public void init() {
        cnt = 10;
        count = 0;
        offset = 0;
        list.clear();
        is_scroll = true;
        is_refresh = true;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == 0) {
            init();
            new NetworkSearch().execute();
        } else if (resultCode == 1) {
            init();
            new NetworkSearch().execute();
        }
    }
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

        if (id == R.id.action_settings) {
            Intent it = new Intent(getActivity(), MainActivity.class);
            startActivityForResult(it, 2);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    class NetworkSearch extends AsyncTask<String, String, Integer> {
        ProgressDialog pro_dialog;
        JSONObject jobject;
        private ProgressBar pro_bar = (ProgressBar)view.findViewById(R.id.pro_bar);

        protected void onPreExecute() {
            pro_dialog = ProgressDialog.show(getActivity(), "", "잠시만 기다려주세요.", true);
            pro_bar.setVisibility(View.VISIBLE);
            super.onPreExecute();
        }

        protected Integer doInBackground(String... params) {
            return processing();
        }

        protected Integer processing() {
            try {
                HttpClient http_Client = new DefaultHttpClient();
                http_Client.getParams().setParameter("http.connection.timeout", 7000);
                HttpPost http_post = null;

                ArrayList<NameValuePair> name_value = new ArrayList<NameValuePair>();
                http_post = new HttpPost("http://54.149.51.26/develop/memberSearch.php");

                name_value.add(new BasicNameValuePair("Offset", ""+offset));
                name_value.add(new BasicNameValuePair("Cnt", ""+cnt));


                UrlEncodedFormEntity request = new UrlEncodedFormEntity(name_value, "UTF-8");
                http_post.setEntity(request);

                HttpResponse response = http_Client.execute(http_post);

                BufferedReader reader = new BufferedReader(new InputStreamReader(response.getEntity().getContent(), "UTF-8"), 8);

                StringBuilder builder = new StringBuilder();

                for (String line = null; (line = reader.readLine()) != null; ) {
                    builder.append(line).append("\n");
                }

                jobject = new JSONObject(builder.toString());

                if (jobject.getInt("err") > 0) {
                    return jobject.getInt("err");
                }
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            } catch (ClientProtocolException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return 0;
        }

        protected void onPostExecute(Integer result) {
            super.onPostExecute(result);
            pro_dialog.cancel();
            swipe_refresh.setRefreshing(false);
            pro_bar.setVisibility(View.GONE);
            try {

                if (result == 0) {

                    offset= offset+ jobject.getInt("cnt");
                    count = jobject.getInt("cnt");


                    for(int i=0;i<jobject.getInt("cnt"); i++)
                    {
                        Item item = new Item();
                        item.setNum(jobject.getJSONArray("ret").getJSONObject(i).getString("Num"));
                        item.setGrade(jobject.getJSONArray("ret").getJSONObject(i).getString("Grade"));
                        item.setName(jobject.getJSONArray("ret").getJSONObject(i).getString("Name"));
                        item.setDepart(jobject.getJSONArray("ret").getJSONObject(i).getString("Department"));
                        item.setRegDate(jobject.getJSONArray("ret").getJSONObject(i).getString("RegDate"));

                        list.add(item);

                    }
                    myAdapter.notifyDataSetChanged();
                    is_scroll = true;

                } else {
                    Toast toast = Toast.makeText(getActivity(), "네트워크가 혼잡합니다.", Toast.LENGTH_SHORT);
                    toast.show();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }


}
