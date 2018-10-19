package com.rieffe.wifianalyzer;

import android.net.wifi.WifiManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.text.format.Formatter;
import android.util.Log;
import android.widget.ListView;
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
import java.util.ArrayList;

public class NetworkInformation extends AppCompatActivity {


    public static final String LOG_TAG = MainActivity.class.getSimpleName();
    private static final String USGS_REQUEST_URL = "https://ipleak.net/json/";
    private final ArrayList<IPInfo> ipInfo = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_network_information);

        TsunamiAsyncTask task = new TsunamiAsyncTask();
        task.execute();
    }

    private void setInformation(PublicIP IP) {
        ListView TaskListView = (ListView) findViewById(R.id.list_network);
        String ip;
        String frequencyString;
        String DNS = "";
        String SSID = "";
        String BSSID = "";
        String netmask = "";
        String gateway = "";
        String serverAdress = "";
        String RSSI = "";

        WifiManager wm = (WifiManager) getApplicationContext().getSystemService(WIFI_SERVICE);
        try {
            ip = Formatter.formatIpAddress(wm.getConnectionInfo().getIpAddress());
            DNS = Formatter.formatIpAddress(wm.getDhcpInfo().dns1);
            frequencyString = stringbuilder(wm.getConnectionInfo().getFrequency());
            SSID = wm.getConnectionInfo().getSSID();
            BSSID = wm.getConnectionInfo().getBSSID();
            netmask = Formatter.formatIpAddress(wm.getDhcpInfo().netmask);
            gateway = Formatter.formatIpAddress(wm.getDhcpInfo().gateway);
            serverAdress = Formatter.formatIpAddress(wm.getDhcpInfo().serverAddress);
            RSSI = stringbuilder(wm.getConnectionInfo().getRssi());

        } catch (Exception e) {
            ip = "No valid ip found";
            frequencyString = "No valid frequency found";
        }
        ipInfo.add(new IPInfo("Ip Adress", ip));
        ipInfo.add(new IPInfo("Frequency", frequencyString));
        ipInfo.add(new IPInfo("DNS", DNS));
        ipInfo.add(new IPInfo("SSID", SSID));
        ipInfo.add(new IPInfo("BSSID", BSSID));
        ipInfo.add(new IPInfo("Public IP", IP.getIP()));
        ipInfo.add(new IPInfo("Country", IP.getCountry()));
        ipInfo.add(new IPInfo("Region", IP.getRegion()));
        ipInfo.add(new IPInfo("City", IP.getCity()));
        ipInfo.add(new IPInfo("Gateway", gateway));
        ipInfo.add(new IPInfo("Netmask", netmask));
        ipInfo.add(new IPInfo("Server Adress", serverAdress));
        ipInfo.add(new IPInfo("RSSI", RSSI));
        IPAdapter adapter = new IPAdapter(this, ipInfo);
        TaskListView.setAdapter(adapter);

    }

    private String stringbuilder(int info) {
        String newString;
        StringBuilder sb = new StringBuilder();
        sb.append(info);
        newString = sb.toString();
        return newString;
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
            setInformation(IP);
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
                String country = baseJsonResponse.getString("country_name");
                String region = baseJsonResponse.getString("region_name");
                String city = baseJsonResponse.getString("city_name");
                return new PublicIP(IP, country, region, city);
            } catch (JSONException e) {
                Log.e(LOG_TAG, "Problem parsing the JSON results", e);
            }
            return null;
        }
    }

    public class PublicIP {

        private final String IP;
        private final String country;
        private final String region;
        private final String city;

        public PublicIP(String _ip, String _country, String _region, String _city) {
            IP = _ip;
            country = _country;
            region = _region;
            city = _city;
        }

        public String getIP() {
            return IP;
        }

        public String getCity() {
            return city;
        }

        public String getCountry() {
            return country;
        }

        public String getRegion() {
            return region;
        }
    }

}
