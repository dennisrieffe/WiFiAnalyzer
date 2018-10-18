package com.rieffe.wifianalyzer;

import android.net.wifi.WifiManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.text.format.Formatter;
import android.util.Log;
import android.widget.TextView;
import android.os.AsyncTask;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.Charset;

public class NetworkInformation extends AppCompatActivity {


    public static final String LOG_TAG = MainActivity.class.getSimpleName();
    private static final String USGS_REQUEST_URL = "https://api.ipify.org/?format=json";

    //http://api.ipstack.com/137.48.255.15?access_key=d183cd125e247c6ceda65043430b4eb4


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_network_information);
        setInformation();
        TsunamiAsyncTask task = new TsunamiAsyncTask();
        task.execute();

    }

    public void setInformation() {
        WifiManager wm = (WifiManager) getApplicationContext().getSystemService(WIFI_SERVICE);
        String ip = Formatter.formatIpAddress(wm.getConnectionInfo().getIpAddress());
        int frequency = wm.getConnectionInfo().getFrequency();
        StringBuilder sb = new StringBuilder();
        sb.append(frequency);
        String frequencyString = sb.toString();
        TextView ipText = (TextView) findViewById(R.id.device_ip);
        ipText.setText(ip);
        TextView ipFrequency = (TextView) findViewById(R.id.device_frequency);
        ipFrequency.setText(frequencyString);
    }

    private void updateUi(PublicIP IP) {
        TextView titleTextView = (TextView) findViewById(R.id.device_public_ip);
        titleTextView.setText(IP.getIP());

    }

    private class TsunamiAsyncTask extends AsyncTask<URL, Void, PublicIP> {

        @Override
        protected PublicIP doInBackground(URL... urls) {
            URL url = createUrl(USGS_REQUEST_URL);
            String jsonResponse = "";
            try {
                jsonResponse = makeHttpRequest(url);
            } catch (IOException e) {
                Log.e(LOG_TAG, "Error with creating URL", e);
            }
            PublicIP IP = extractFeatureFromJson(jsonResponse);
            return IP;
        }


        @Override
        protected void onPostExecute(PublicIP IP) {
            if (IP == null) {
                return;
            }

            updateUi(IP);
        }

        private URL createUrl(String stringUrl) {
            URL url = null;
            try {
                url = new URL(stringUrl);
            } catch (MalformedURLException exception) {
                Log.e(LOG_TAG, "Error with creating URL", exception);
                return null;
            }
            return url;
        }

        private String makeHttpRequest(URL url) throws IOException {
            String jsonResponse = "";

            if (url == null) {
                return jsonResponse;
            }
            HttpURLConnection urlConnection = null;
            InputStream inputStream = null;
            try {
                urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setRequestMethod("GET");
                urlConnection.setReadTimeout(10000 /* milliseconds */);
                urlConnection.setConnectTimeout(15000 /* milliseconds */);
                urlConnection.connect();
                if (urlConnection.getResponseCode() == 200) {
                    inputStream = urlConnection.getInputStream();
                    jsonResponse = readFromStream(inputStream);
                }

            } catch (IOException e) {
                Log.e(LOG_TAG, "The message was " + urlConnection.getResponseCode());
                e.printStackTrace();
                return null;
            } finally {
                if (urlConnection != null) {
                    urlConnection.disconnect();
                }
                if (inputStream != null) {
                    inputStream.close();
                }
            }
            return jsonResponse;
        }

        private String readFromStream(InputStream inputStream) throws IOException {
            StringBuilder output = new StringBuilder();
            if (inputStream != null) {
                InputStreamReader inputStreamReader = new InputStreamReader(inputStream, Charset.forName("UTF-8"));
                BufferedReader reader = new BufferedReader(inputStreamReader);
                String line = reader.readLine();
                while (line != null) {
                    output.append(line);
                    line = reader.readLine();
                }
            }
            return output.toString();
        }

        private PublicIP extractFeatureFromJson(String IPJSON) {

            if (TextUtils.isEmpty(IPJSON)) {
                return null;
            }
            try {
                JSONObject baseJsonResponse = new JSONObject(IPJSON);
                String IP = baseJsonResponse.getString("ip");
                return new PublicIP(IP);
            } catch (JSONException e) {
                Log.e(LOG_TAG, "Problem parsing the JSON results", e);
            }
            return null;
        }
    }

    public class PublicIP {

        public final String IP;

        public PublicIP(String _ip) {
            IP = _ip;
        }

        public String getIP() {
            return IP;
        }
    }
}
