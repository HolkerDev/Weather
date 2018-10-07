package com.example.holker.weather;

import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class MainActivity extends AppCompatActivity {

    Button mButtonCheck;
    EditText mEditTextCity;
    TextView mTextViewWeather;
    ProgressBar mProgressBar;

    public class DownloadWeather extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... strings) {

            try {
                StringBuilder result = new StringBuilder();
                URL url = new URL(strings[0]);
                HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();

                InputStream in = urlConnection.getInputStream();

                InputStreamReader reader = new InputStreamReader(in);

                int data = reader.read();
                while (data != -1) {
                    char current = (char) data;
                    result.append(current);
                    data = reader.read();
                }
                return result.toString();

            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);

            try {
                JSONObject jsonObject = new JSONObject(s);
                String weather = jsonObject.getString("weather");

                JSONArray array = new JSONArray(weather);
                StringBuilder endResult = new StringBuilder();
                for (int i = 0; i < array.length(); i++) {
                    JSONObject object = array.getJSONObject(i);

                    endResult.append(object.getString("main"));
                    endResult.append(" : ");
                    endResult.append(object.getString("description"));
                }

                mTextViewWeather.setText(endResult.toString());
                Toast.makeText(getApplicationContext(), "Successful", Toast.LENGTH_LONG).show();

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //find
        mButtonCheck = (Button) findViewById(R.id.btn_check);
        mEditTextCity = (EditText) findViewById(R.id.et_city);
        mTextViewWeather = (TextView) findViewById(R.id.tv_weather);
        mProgressBar = (ProgressBar) findViewById(R.id.progressBar);


        //Listener
        mButtonCheck.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mTextViewWeather.setVisibility(View.INVISIBLE);
                mProgressBar.setVisibility(View.VISIBLE);

                String city = mEditTextCity.getText().toString();



                DownloadWeather task = new DownloadWeather();
                task.execute("http://api.openweathermap.org/data/2.5/weather?q=" + city +
                        "&appid=b8d5f0ecb32d776f4cd96fb8d884f07b");

                mTextViewWeather.setVisibility(View.VISIBLE);

                mProgressBar.setVisibility(View.INVISIBLE);
            }
        });

    }
}
