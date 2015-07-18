package net.wolchesky.catfacts;

import android.app.Activity;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.HttpURLConnection;
import java.net.URL;


public class FunFactsActivity extends Activity {
    private FactBook mFactBook = new FactBook();
    private ColorWheel mColorWheel = new ColorWheel();
    public  JSONObject mFactJSON = null;

    public static final String TAG = FunFactsActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fun_facts);

        // Declare our view variables
        final TextView factLabel = (TextView) findViewById(R.id.factTextView);
        final Button showFactButton = (Button) findViewById(R.id.showFactButton);
        final RelativeLayout relLayout = (RelativeLayout) findViewById(R.id.relativeLayout);
        final TextView offlineLabel = (TextView) findViewById(R.id.offlineLabelView);
        if (isNetworkAvailable()) {
            final CatFactsActivity catFactsActivity = new CatFactsActivity();
            catFactsActivity.execute();
            offlineLabel.setVisibility(View.INVISIBLE);
        }

        View.OnClickListener listener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String fact = null;
                if (mFactJSON == null) {
                    fact = mFactBook.getFact();
                } else {
                    try {
                        JSONArray jsonArray = mFactJSON.getJSONArray("facts");
                        fact = jsonArray.getString(0);
                    } catch (JSONException e) {
                        Log.e(TAG, "Exception caught: ", e);
                        fact = mFactBook.getFact();
                    }

                }
                int color = mColorWheel.getColor();
                //Update the label with our dynamic fact
                factLabel.setText(fact);
                relLayout.setBackgroundColor(color);
                showFactButton.setTextColor(color);
                if (isNetworkAvailable()) {
                    new CatFactsActivity().execute();
                    offlineLabel.setVisibility(View.INVISIBLE);
                } else {
                    offlineLabel.setVisibility(View.VISIBLE);
                    mFactJSON = null;
                }
            }
        };
        showFactButton.setOnClickListener(listener);


        //Toast.makeText(this, "Activity successfully created.", Toast.LENGTH_LONG).show();
        //Log.d(TAG, "Logging from onCreate() method!");
    }

    private boolean isNetworkAvailable() {
        ConnectivityManager manager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo info = manager.getActiveNetworkInfo();

        boolean isAvailable = false;
        if (info != null && info.isConnected()) {
            isAvailable = true;
        }

        return isAvailable;
    }

    private class CatFactsActivity extends AsyncTask<Object, Void, JSONObject> {

        @Override
        protected JSONObject doInBackground(Object... params) {
            JSONObject jsonResponse = null;
            int responseCode = -1;

            try {
                URL requestUrl = new URL("http://catfacts-api.appspot.com/api/facts");
                HttpURLConnection connection = (HttpURLConnection) requestUrl.openConnection();
                connection.setRequestProperty("Accept-Encoding", "identity");
                connection.connect();
                responseCode = connection.getResponseCode();
                if(responseCode == HttpURLConnection.HTTP_OK) {
                    InputStream stream = connection.getInputStream();
                    Reader reader = new InputStreamReader(stream);
                    int contentLength = connection.getContentLength();
                    if (contentLength > 0) {
                        char[] buffer = new char[contentLength ];
                        reader.read(buffer);
                        //Log.d(TAG, buffer);  Used to be buffer.toString()
                        jsonResponse = new JSONObject(String.valueOf(buffer));
                    } else {
                        String jsonData = isToString(stream);
                        jsonResponse = new JSONObject(jsonData);
                    }

                    Log.d(TAG, jsonResponse.toString(2));
                    connection.disconnect();
                } else {
                    Log.e(TAG, "Status Code: " + responseCode);
                }

            }
            catch (Exception e) {
                Log.e(TAG, "Exception: ", e);
            }


            return jsonResponse;
        }

        protected void onPostExecute(JSONObject data) {
            mFactJSON = data;
        }

        protected String isToString(InputStream is) {
            ByteArrayOutputStream buffer = new ByteArrayOutputStream();
            int nRead;
            byte[] data = new byte[65536];

            try {
                while ((nRead = is.read(data, 0, data.length)) != -1) {
                    buffer.write(data, 0, nRead);
                }

                buffer.flush();
            } catch (IOException e) {
                Log.e(TAG, "Exception caught: ", e);
            }

            return buffer.toString();
        }
    }
}
