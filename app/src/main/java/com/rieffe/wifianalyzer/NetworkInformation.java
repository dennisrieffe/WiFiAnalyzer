package com.rieffe.wifianalyzer;

import android.content.Context;
import android.net.NetworkInfo;
import android.net.wifi.WifiManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.text.format.Formatter;
import android.util.Log;
import android.widget.ListView;
import android.os.AsyncTask;

import org.json.JSONException;
import org.json.JSONObject;

import android.net.ConnectivityManager;
import android.telephony.TelephonyManager;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.stream.Collector;
import java.util.stream.Collectors;

public class NetworkInformation extends AppCompatActivity {

    public static final String LOG_TAG = MainActivity.class.getSimpleName();
    private static final String USGS_REQUEST_URL = "https://ipleak.net/json/";
    private final ArrayList<IPInfo> ipInfo = new ArrayList<>();
    Context context = this;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_network_information);

        InfoAsyncTask task = new InfoAsyncTask();
        task.execute();
    }

    public static NetworkInfo getNetworkInfo(Context context) {
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        return cm.getActiveNetworkInfo();
    }

    public static String whatConnection(int type, int subType) {
        if (type == ConnectivityManager.TYPE_WIFI) {
            return "WiFi";
        } else if (type == ConnectivityManager.TYPE_MOBILE) {
            switch (subType) {
                case TelephonyManager.NETWORK_TYPE_1xRTT:
                    return "1xRRT"; // ~ 50-100 kbps
                case TelephonyManager.NETWORK_TYPE_CDMA:
                    return "CDMA"; // ~ 14-64 kbps
                case TelephonyManager.NETWORK_TYPE_EDGE:
                    return "EDGE"; // ~ 50-100 kbps
                case TelephonyManager.NETWORK_TYPE_EVDO_0:
                    return "EVDO"; // ~ 400-1000 kbps
                case TelephonyManager.NETWORK_TYPE_EVDO_A:
                    return "EVDO"; // ~ 600-1400 kbps
                case TelephonyManager.NETWORK_TYPE_GPRS:
                    return "GPRS"; // ~ 100 kbps
                case TelephonyManager.NETWORK_TYPE_HSDPA:
                    return "HSDPA"; // ~ 2-14 Mbps
                case TelephonyManager.NETWORK_TYPE_HSPA:
                    return "HSPA"; // ~ 700-1700 kbps
                case TelephonyManager.NETWORK_TYPE_HSUPA:
                    return "HSUPA"; // ~ 1-23 Mbps
                case TelephonyManager.NETWORK_TYPE_UMTS:
                    return "UMTS"; // ~ 400-7000 kbps
                case TelephonyManager.NETWORK_TYPE_EHRPD:
                    return "EHRPD"; // ~ 1-2 Mbps
                case TelephonyManager.NETWORK_TYPE_EVDO_B:
                    return "EVDO_B"; // ~ 5 Mbps
                case TelephonyManager.NETWORK_TYPE_HSPAP:
                    return "HSPAP"; // ~ 10-20 Mbps
                case TelephonyManager.NETWORK_TYPE_IDEN:
                    return "IDEN"; // ~25 kbps
                case TelephonyManager.NETWORK_TYPE_LTE:
                    return "LTE"; // ~ 10+ Mbps
                case TelephonyManager.NETWORK_TYPE_UNKNOWN:
                default:
                    return "Unknown";
            }
        } else {
            return "Incorrect connection";
        }
    }

    private void setInformation(PublicIP IP) {
        WifiManager wm = (WifiManager) getApplicationContext().getSystemService(WIFI_SERVICE);

        if (wm == null) {
            ipInfo.add(new IPInfo("IP Address", "No valid ip found", 0));
            ipInfo.add(new IPInfo("Frequency", "No valid frequency found", 0));
            ipInfo.add(new IPInfo("DNS", "", 0));
            ipInfo.add(new IPInfo("SSID", "", 0));
            ipInfo.add(new IPInfo("BSSID", "", 0));
            ipInfo.add(new IPInfo("Gateway", "", 0));
            ipInfo.add(new IPInfo("Netmask", "", 0));
            ipInfo.add(new IPInfo("Server Address", "", 0));
            ipInfo.add(new IPInfo("RSSI", "", 0));
            ipInfo.add(new IPInfo("Connection Type", "", 0));
            ipInfo.add(new IPInfo("Connection Name", " ", 0));

        } else {
            ipInfo.add(new IPInfo("IP Address", Formatter.formatIpAddress(wm.getConnectionInfo().getIpAddress()), R.drawable.ip));
            ipInfo.add(new IPInfo("Frequency", stringBuilder(wm.getConnectionInfo().getFrequency()), R.drawable.frequency));
            ipInfo.add(new IPInfo("DNS", Formatter.formatIpAddress(wm.getDhcpInfo().dns1), R.drawable.dns));
            ipInfo.add(new IPInfo("SSID", wm.getConnectionInfo().getSSID(), R.drawable.ssid));
            ipInfo.add(new IPInfo("BSSID", wm.getConnectionInfo().getBSSID(), R.drawable.mac));
            ipInfo.add(new IPInfo("Gateway", Formatter.formatIpAddress(wm.getDhcpInfo().gateway), R.drawable.gateway));
            ipInfo.add(new IPInfo("Netmask", Formatter.formatIpAddress(wm.getDhcpInfo().netmask), R.drawable.netmask));
            ipInfo.add(new IPInfo("Server Address", Formatter.formatIpAddress(wm.getDhcpInfo().serverAddress), R.drawable.server));
            ipInfo.add(new IPInfo("RSSI", stringBuilder(wm.getConnectionInfo().getRssi()), R.drawable.rssi));
            ipInfo.add(new IPInfo("Connection Type", whatConnection(getNetworkInfo(context).getType(), getNetworkInfo(context).getSubtype()), R.drawable.connection));
            ipInfo.add(new IPInfo("Connection name", getNetworkInfo(context).getExtraInfo(), R.drawable.name));
        }
        ipInfo.add(new IPInfo("Public IP", IP.getIP(), R.drawable.public_ip));
        ipInfo.add(new IPInfo("Country", IP.getCountry(), R.drawable.country));
        ipInfo.add(new IPInfo("Region", IP.getRegion(), R.drawable.region));
        ipInfo.add(new IPInfo("City", IP.getCity(), R.drawable.city));

        ((ListView) findViewById(R.id.list_network))
                .setAdapter(new IPAdapter(this, ipInfo));
    }

    private String stringBuilder(int info) {
        return Integer.toString(info);
    }

    private class InfoAsyncTask extends AsyncTask<URL, Void, PublicIP> {

        @Override
        protected PublicIP doInBackground(URL... urls) {
            try {
                return extractFeatureFromJson(
                        makeHttpRequest(
                                createUrl()));
            } catch (IOException e) {
                Log.e(LOG_TAG, "Error with creating URL", e);
                return extractFeatureFromJson("");
            }
        }

        @Override
        protected void onPostExecute(PublicIP IP) {
            if (IP != null) {
                setInformation(IP);
            }
        }

        private URL createUrl() {
            try {
                return new URL(NetworkInformation.USGS_REQUEST_URL);
            } catch (MalformedURLException exception) {
                Log.e(LOG_TAG, "Error with creating URL", exception);
                return null;
            }
        }

        private String makeHttpRequest(URL url) throws IOException {
            if (url == null) {
                return "";
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
                    return readFromStream(inputStream);
                }
            } catch (ProtocolException e) {
                Log.e(LOG_TAG, "Unable to set GET request method.");
                e.printStackTrace();

                return null;
            } catch (IOException e) {
                Log.e(LOG_TAG, urlConnection == null
                        ? "Error. Unable to retrieve message."
                        : "The message was " + urlConnection.getResponseCode());
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

            return "";
        }

        private String readFromStream(InputStream inputStream) throws IOException {
            if (inputStream == null) {
                return "";
            }

            StringBuilder output = new StringBuilder();
            BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream, Charset.forName("UTF-8")));
            String line = reader.readLine();

            while (line != null) {
                output.append(line);
                line = reader.readLine();
            }

            return output.toString();
        }

        private PublicIP extractFeatureFromJson(String IPJSON) {
            if (TextUtils.isEmpty(IPJSON)) {
                return null;
            }

            try {
                JSONObject baseJsonResponse = new JSONObject(IPJSON);

                return new PublicIP(baseJsonResponse.getString("ip"),
                        baseJsonResponse.getString("country_name"),
                        baseJsonResponse.getString("region_name"),
                        baseJsonResponse.getString("city_name"));
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

    public static <T> Collector<T, ?, T> toSingleton() {
        return Collectors.collectingAndThen(
                Collectors.toList(),
                list -> {
                    if (list.size() != 1) {
                        throw new IllegalStateException();
                    }
                    return list.get(0);
                }
        );
    }
}
