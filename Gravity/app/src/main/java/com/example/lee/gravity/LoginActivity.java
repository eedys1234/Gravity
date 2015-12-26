package com.example.lee.gravity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
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

public class LoginActivity extends AppCompatActivity {


    TextView txtPassword;
    TextView txtId;
    EditText txtPasswordInput;
    EditText txtIdInput;
    Button btnConfirm;
    Button btnSignUp;
    CheckBox chkAutoLogin;

    String studentNum;
    String password;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        txtPassword = (TextView)findViewById(R.id.txtPassword);
        txtId = (TextView)findViewById(R.id.txtId);
        txtPasswordInput = (EditText)findViewById(R.id.txtPasswordInput);
        txtIdInput = (EditText)findViewById(R.id.txtIdInput);
        btnConfirm = (Button)findViewById(R.id.btnConfirm);
        btnSignUp = (Button)findViewById(R.id.btnSignUp);

        chkAutoLogin = (CheckBox)findViewById(R.id.chkAutoLogin);

        btnConfirm.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {

                studentNum = txtIdInput.getText().toString().trim();
                password = txtPasswordInput.getText().toString().trim();

                new NetworkLogin().execute();
            }
        });

        btnSignUp.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {

                Toast toast = Toast.makeText(LoginActivity.this, "차후 서비스될 예정입니다.", Toast.LENGTH_SHORT);
                toast.show();
            }
        });

    }

    class NetworkLogin extends AsyncTask<String, String, Integer>
    {
        ProgressDialog pro_dialog;
        JSONObject jobject;
        private ProgressBar pro_bar = (ProgressBar)findViewById(R.id.pro_bar);

        protected void onPreExecute() {
            pro_dialog = ProgressDialog.show(LoginActivity.this, "", "잠시만 기다려주세요.", true);
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
                http_post = new HttpPost("http://54.149.51.26/develop/login.php");

                name_value.add(new BasicNameValuePair("Num", studentNum));
                name_value.add(new BasicNameValuePair("Password", password));

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

                    Intent it = new Intent(LoginActivity.this, FragActivity.class);
                    startActivity(it);
                    finish();
                }
                else if(result == 2 ){
                    Toast toast = Toast.makeText(LoginActivity.this, "학번이 틀렸습니다.", Toast.LENGTH_SHORT);
                    toast.show();
                }
                else if(result == 3 ){
                    Toast toast = Toast.makeText(LoginActivity.this, "비밀번호가 틀렸습니다.", Toast.LENGTH_SHORT);
                    toast.show();
                }
                else if(result == 4 ){
                    Toast toast = Toast.makeText(LoginActivity.this, "DB오류", Toast.LENGTH_SHORT);
                    toast.show();
                }
                else {
                    Toast toast = Toast.makeText(LoginActivity.this, "네트워크가 혼잡합니다.", Toast.LENGTH_SHORT);
                    toast.show();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

        public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_login, menu);
        return true;
    }


    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
