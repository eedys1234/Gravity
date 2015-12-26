package com.example.lee.gravity;

import android.app.ActionBar;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.media.Image;
import android.os.AsyncTask;
import android.preference.DialogPreference;
import android.provider.MediaStore;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
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

public class UpdateActivity extends AppCompatActivity {


    private static final int PICK_FROM_CAMERA = 5;
    private static final int PICK_FROM_ALBUM = 6;
    private static final int PICK_FROM_CROP = 7;


    Intent it;
    TextView txtNum;
    TextView txtName;
    TextView txtGrade;
    TextView txtDepart;
    EditText txtNumInput;
    EditText txtNameInput;
    EditText txtGradeInput;
    EditText txtDepartInput;
    Button btnConfirm;
    Button btnReset;
    Button btnDelete;
    String strNum;
    String strName;
    String strGrade;
    String strDepart;
    ImageView imgPicture;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update);

        imgPicture = (ImageView)findViewById(R.id.imageView);
        txtNum = (TextView)findViewById(R.id.txtNum);
        txtName = (TextView)findViewById(R.id.txtName);
        txtGrade = (TextView)findViewById(R.id.txtGrade);
        txtDepart = (TextView)findViewById(R.id.txtDepart);

        txtNumInput = (EditText)findViewById(R.id.txtNumInput);
        txtNameInput = (EditText)findViewById(R.id.txtNameInput);
        txtGradeInput = (EditText)findViewById(R.id.txtGradeInput);
        txtDepartInput = (EditText)findViewById(R.id.txtDepartInput);

        btnConfirm = (Button)findViewById(R.id.btnConfirm);
        btnReset = (Button)findViewById(R.id.btnReset);
        btnDelete = (Button)findViewById(R.id.btnDelete);

        it = getIntent();

        txtNumInput.setText(it.getStringExtra("num"));
        txtNameInput.setText(it.getStringExtra("name"));
        txtGradeInput.setText(it.getStringExtra("grade"));
        txtDepartInput.setText(it.getStringExtra("depart"));



        btnConfirm.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {

                strNum = txtNumInput.getText().toString().trim();
                strName = txtNameInput.getText().toString().trim();
                strGrade = txtGradeInput.getText().toString().trim();
                strDepart = txtDepartInput.getText().toString().trim();

                new NetworkUpdate().execute();

            }
        });


        btnReset.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {

                    finish();
            }
        });


        btnDelete.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {

                strNum = txtNumInput.getText().toString().trim();
                EditText msg = new EditText(UpdateActivity.this);
                AlertDialog.Builder builder = new AlertDialog.Builder(UpdateActivity.this);
                builder.setTitle("관리자모드")
                .setIcon(null)
                .setPositiveButton("확인", new DialogInterface.OnClickListener() {

                    public void onClick(DialogInterface dialog, int which) {

                            new NetworkDelete().execute();
                    }
                }).setNegativeButton("취소", new DialogInterface.OnClickListener() {

                    public void onClick(DialogInterface dialog, int which) {

                    }
                }).setMessage("비밀번호를 입력하세요.")
                .setView(msg).show();
            }
        });

        imgPicture.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {

                showPictureDialog();

            }
        });

    }

    public void showPictureDialog()
    {

        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        builder.setTitle("사진 선택")
                .setItems(R.array.picture, new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        if(which == 0)
                        {
                            PhotoActivity();
                        }
                        else if(which == 1)
                        {
                            AlbumActivity();
                        }
                    }
                }).show();
    }

    public void PhotoActivity()
    {

        Intent it = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(it, PICK_FROM_CAMERA);

    }

    public void AlbumActivity()
    {
        Intent it = new Intent(Intent.ACTION_PICK);
        startActivityForResult(it, PICK_FROM_ALBUM);

    }

    class NetworkUpdate extends AsyncTask<String, String, Integer> {
        ProgressDialog pro_dialog;
        JSONObject jobject;
        private ProgressBar pro_bar = (ProgressBar)findViewById(R.id.pro_bar);

        protected void onPreExecute() {
            pro_dialog = ProgressDialog.show(UpdateActivity.this, "", "잠시만 기다려주세요.", true);
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
                http_post = new HttpPost("http://54.149.51.26/develop/memberUpdate.php");

                name_value.add(new BasicNameValuePair("Num", strNum));
                name_value.add(new BasicNameValuePair("Name", strName));
                name_value.add(new BasicNameValuePair("Grade", strGrade));
                name_value.add(new BasicNameValuePair("Depart", strDepart));

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
            try {

                if (result == 0) {

                        Intent it = new Intent(UpdateActivity.this, FragmentActivity.class);
                        setResult(0,it);
                        finish();

                }
                else if(result == 4 ){
                    Toast toast = Toast.makeText(UpdateActivity.this, "DB오류", Toast.LENGTH_SHORT);
                    toast.show();
                }
                else {
                    Toast toast = Toast.makeText(UpdateActivity.this, "네트워크가 혼잡합니다.", Toast.LENGTH_SHORT);
                    toast.show();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }


    class NetworkDelete extends AsyncTask<String, String, Integer> {
        ProgressDialog pro_dialog;
        JSONObject jobject;

        protected void onPreExecute() {
            pro_dialog = ProgressDialog.show(UpdateActivity.this, "", "잠시만 기다려주세요.", true);
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
                http_post = new HttpPost("http://54.149.51.26/develop/memberDelete.php");

                name_value.add(new BasicNameValuePair("Num", strNum));

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
            try {

                if (result == 0) {

                    Intent it = new Intent(UpdateActivity.this, FragmentActivity.class);
                    setResult(1,it);
                    finish();
                }
                else if(result == 4 ){
                    Toast toast = Toast.makeText(UpdateActivity.this, "DB오류", Toast.LENGTH_SHORT);
                    toast.show();
                }
                else {
                    Toast toast = Toast.makeText(UpdateActivity.this, "네트워크가 혼잡합니다.", Toast.LENGTH_SHORT);
                    toast.show();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_update, menu);
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
