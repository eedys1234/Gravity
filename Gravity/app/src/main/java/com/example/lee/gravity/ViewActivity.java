package com.example.lee.gravity;

import android.app.Activity;
import android.app.ListActivity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AbsListView;
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
import java.util.ArrayList;

public class ViewActivity extends Activity {

    ListView listView;
    TextView txtTotal;
    int cnt = 10;
    int offset = 0;
    int count = 0 ;
    ArrayList<Item> list;
    MyAdapter myAdapter;
    boolean is_scroll;
    boolean is_refresh;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view);

        txtTotal = (TextView)findViewById(R.id.txtTotal);
        listView = (ListView)findViewById(R.id.listView);
        list = new ArrayList<Item>();

        myAdapter = new MyAdapter(this, R.layout.student, list);

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
                            new NetworkSearch().execute("");

                        }
                    }
                }
            }
        });
    }

    public void init()
    {
        cnt = 10;
        count = 0;
        offset = 0;
        list.clear();
        is_scroll = true;
        is_refresh = true;
    }


    class NetworkSearch extends AsyncTask<String, String, Integer> {
        ProgressDialog pro_dialog;
        JSONObject jobject;
        private ProgressBar pro_bar = (ProgressBar)findViewById(R.id.pro_bar);

        protected void onPreExecute() {
            pro_dialog = ProgressDialog.show(ViewActivity.this, "", "잠시만 기다려주세요.", true);
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
            pro_bar.setVisibility(View.GONE);
            String user_id = null;
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

                        list.add(item);

                    }
                    myAdapter.notifyDataSetChanged();
                    is_scroll = true;

                } else {
                    Toast toast = Toast.makeText(ViewActivity.this, "네트워크가 혼잡합니다.", Toast.LENGTH_SHORT);
                    toast.show();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

//    protected void onListItemClick(ListView l, View v, int position, long id) {
//        super.onListItemClick(l, v, position, id);
//        Item item = (Item) list.get(position);
//        Intent it = new Intent(ViewActivity.this, UpdateActivity.class);
//        it.putExtra("name", item.getName());
//        it.putExtra("num", item.getNum());
//        it.putExtra("grade", item.getGrade());
//        startActivityForResult(it, 0);
//    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_view, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
