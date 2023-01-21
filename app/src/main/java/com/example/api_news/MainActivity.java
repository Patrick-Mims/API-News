package com.example.api_news;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class MainActivity extends AppCompatActivity {
    private static int referenceCounter = 0;
    private TextView textView;
    private static final String USER_AGENT = "Mozilla/5.0";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toast.makeText(this, "hello toast", Toast.LENGTH_SHORT).show();
        Log.i("info", "Log.i Message Here");

        Button login = (Button) findViewById(R.id.btn_login);
        Button logout = (Button) findViewById(R.id.btn_logout);
        textView = (TextView) findViewById(R.id.text_view);

        // taskRunner object created, this calls and loads the api when the app starts
        AsyncTaskRunner taskRunner = new AsyncTaskRunner();
        taskRunner.execute("https://newsapi.org/v2/top-headlines?country=us&apiKey=6a5b4f0943e447a092cc59f7fbe690ef");

        // create and execute the api call with an onclick event
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getApplicationContext(), "Login", Toast.LENGTH_LONG).show();
                Log.i("info", "CLIENT LOGGED IN WITH ASYNCTASK AT PLAY");
                /* AsyncTaskRunner calls doInBackground */
                /*
                AsyncTaskRunner taskRunner = new AsyncTaskRunner();
                taskRunner.execute("https://newsapi.org/v2/top-headlines?country=us&apiKey=6a5b4f0943e447a092cc59f7fbe690ef");
                 */
            }
        });

        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getApplicationContext(), "Logout", Toast.LENGTH_LONG).show();
                Log.i("info", "CLIENT LOGGED OUT");
            }
        });
    }


    /* AsyncTaskRunner */
    private class AsyncTaskRunner extends AsyncTask<String, String, String> {

        public JSONObject createJsonObject(String str) throws JSONException {
            return new JSONObject(str);
        }

        ProgressDialog progressDialog;

        @Override
        protected String doInBackground(String... params) {
            // assign the first param to api, remember params is an array of args passed in from taskRunner.execute()
            String api = params[0];
            String result = "Results Here...";

            String JSON_STRING = "";
            JSONObject jsonObj;

            final String REQUEST_METHOD = "GET";

            referenceCounter++;

            publishProgress("Progressing...");

            try {
                URL url = new URL(api);
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();

                connection.setRequestMethod(REQUEST_METHOD);
                connection.setRequestProperty("User-Agent", USER_AGENT);
                result = connection.getResponseCode() + " " + referenceCounter + " == " + HttpURLConnection.HTTP_OK;

                if (connection.getResponseCode() == HttpURLConnection.HTTP_OK) {
                    BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                    StringBuilder response = new StringBuilder();
                    String inputLine;

                    while ((inputLine = in.readLine()) != null) {
                        response.append(inputLine);
                    }
                    in.close();

                    JSON_STRING = response.toString();
                    jsonObj = createJsonObject(JSON_STRING);

                    JSONObject titles = jsonObj.getJSONObject("articles");

                    //Log.i("INFO", titles.getString("title"));
                    //result = titles[1].getString("title");

//                    result = title.getString("title");

                    /*
                    result = JSON_STRING;
                     */
                    result = response.toString();
                } else {
                    System.out.println("GET request did not work.");
                }
            }  catch(MalformedURLException msg) {
                System.out.println(msg);
            } catch (IOException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                throw new RuntimeException(e);
            }

            /*
            try {
                JSONObject obj = new JSONObject(JSON_STRING);

                JSONObject employee = obj.getJSONObject("url");
            } catch (JSONException e) {
                e.printStackTrace();
            }
            */
            /*
            try {
                JSONObject data = createJsonObject(JSON_STRING);

                JSONObject title = data.getJSONObject("articles");

                result = title.getString("title");

            } catch (JSONException e) {
                throw new RuntimeException(e);
            }
            */
            return result;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            /* Uncomment to show the dialog box
            progressDialog = ProgressDialog.show(MainActivity.this, "Working", "Loading Data...", false, true);
            progressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
             */
            Log.i("info", "[INFORMATION] -> protected void onPreExecute() executed");
        }

        /*  onPostExecute(String result) Runs on the UI thread after doInBackground(Params...).
         *  The specified result is the value returned by doInBackground(Params...).
         * */
        @Override
        protected void onPostExecute(String result) {
            // super.onPostExecute(result);
            Log.i("info", "[INFORMATION] -> onPostExecute()");
//            progressDialog.dismiss();
            textView.setText(result);
        }
    }
}