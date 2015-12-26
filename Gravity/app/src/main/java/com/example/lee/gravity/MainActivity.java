package com.example.lee.gravity;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.provider.MediaStore;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
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
import java.util.Calendar;

public class MainActivity extends AppCompatActivity {

    public static final int PICK_FROM_CAMERA = 5;
    public static final int PICK_FROM_ALBUM= 6;
    public static final int PICK_FROM_CROP = 7;


    int year, month, day;

    ImageView imgPicture;
    Button btnConfirm;
    Button btnReset;
    EditText txtNameInput;
    EditText txtNumInput;
    EditText txtGradeInput;
    EditText txtDepartInput;
    TextView txtDate;
    TextView txtNum;
    TextView txtName;
    TextView txtGrade;
    TextView txtDepart;
    Intent intent;
    Calendar canlendar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        imgPicture = (ImageView)findViewById(R.id.imgPicture);

        //입력 값 생성
        txtNameInput =  (EditText)findViewById(R.id.txtNameInput);
        txtNumInput =  (EditText)findViewById(R.id.txtNumInput);
        txtGradeInput =  (EditText)findViewById(R.id.txtGradeInput);
        txtDepartInput = (EditText)findViewById(R.id.txtDepartInput);

        //Label 생성
        txtName =  (TextView)findViewById(R.id.txtName);
        txtNum =  (TextView)findViewById(R.id.txtNum);
        txtGrade =  (TextView)findViewById(R.id.txtGrade);
        txtDepart = (TextView)findViewById(R.id.txtDepart);
        txtDate =  (TextView)findViewById(R.id.txtDate);

        btnConfirm = (Button)findViewById(R.id.btnConfirm);
        btnReset = (Button)findViewById(R.id.btnReset);

        canlendar= Calendar.getInstance();

        txtDate.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {

                new DatePickerDialog(MainActivity.this, dateSetListener, canlendar.get(Calendar.YEAR),
                        canlendar.get(Calendar.MONTH), canlendar.get(Calendar.DAY_OF_MONTH)).show();

            }
        });


        btnConfirm.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {
                if(txtNumInput.getText().toString().length()==0)
                {
                    Toast toast = Toast.makeText(MainActivity.this, "학번을 입력해주세요.", Toast.LENGTH_SHORT);
                    toast.show();

                }
                else if(txtGradeInput.getText().toString().length()==0)
                {
                    Toast toast = Toast.makeText(MainActivity.this, "학년을 입력해주세요.", Toast.LENGTH_SHORT);
                    toast.show();

                }
                else if(txtNameInput.getText().toString().length()==0)
                {
                    Toast toast = Toast.makeText(MainActivity.this, "이름을 입력해주세요.", Toast.LENGTH_SHORT);
                    toast.show();
                }
                else if(txtDepartInput.getText().toString().length()==0)
                {
                    Toast toast = Toast.makeText(MainActivity.this, "부서를 입력해주세요.", Toast.LENGTH_SHORT);
                    toast.show();

                }
                else
                {
                    new NetworkInsert().execute();
                }
            }
        });

        btnReset.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {

                Intent it = new Intent(MainActivity.this, FragActivity.class);
                setResult(2, it);
                finish();

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
       AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);

        builder.setTitle("사진 선택")
                .setItems(R.array.picture, new DialogInterface.OnClickListener()
                    {
                            public void onClick(DialogInterface dialog, int which) {

                                if(which==0)
                                {
                                    PhotoActivity();
                                }
                                else if(which==1)
                                {
                                    AlbumActivity();
                                }
                            }
                    }
                ).show();

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

    private DatePickerDialog.OnDateSetListener dateSetListener = new DatePickerDialog.OnDateSetListener() {

        public void onDateSet(DatePicker view, int year, int monthOfYear,
                              int dayOfMonth) {
            String msg = String.format("%d / %d / %d", year,monthOfYear+1, dayOfMonth);
            Toast.makeText(MainActivity.this, msg, Toast.LENGTH_SHORT).show();
            txtDate.setText(msg);
        }
    };

    class NetworkInsert extends AsyncTask<String, String, Integer> {
        ProgressDialog pro_dialog;
        JSONObject jobject;

        protected void onPreExecute() {
            pro_dialog = ProgressDialog.show(MainActivity.this, "", "잠시만 기다려주세요.", true);
            super.onPreExecute();
        }

        protected Integer doInBackground(String... params) {
            return processing();
        }

        protected Integer processing() {
            try {
                HttpClient http_Client = new DefaultHttpClient();

                HttpPost http_post = null;

                ArrayList<NameValuePair> name_value = new ArrayList<NameValuePair>();
                http_post = new HttpPost("http://54.149.51.26/develop/member.php");

                name_value.add(new BasicNameValuePair("Num", txtNumInput.getText().toString().trim()));
                name_value.add(new BasicNameValuePair("Name", txtNameInput.getText().toString().trim()));
                name_value.add(new BasicNameValuePair("Grade", txtGradeInput.getText().toString().trim()));
                name_value.add(new BasicNameValuePair("Depart", txtDepartInput.getText().toString().trim()));

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
                   intent = new Intent(MainActivity.this, FragActivity.class);
                    startActivity(intent);
                    finish();

                } else if(result == 4){
                    Toast toast = Toast.makeText(MainActivity.this, "DB오류.", Toast.LENGTH_SHORT);
                    toast.show();
                } else {
                    Toast toast = Toast.makeText(MainActivity.this, "네트워크가 혼잡합니다.", Toast.LENGTH_SHORT);
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
            getMenuInflater().inflate(R.menu.menu_main, menu);
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
