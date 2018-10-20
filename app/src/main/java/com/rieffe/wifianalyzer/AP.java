package com.rieffe.wifianalyzer;

class AP {
    private final String BSSID;
    private final int RSSI;
    private final String SSID;

    public AP(String BSSID, int RSSI, String SSID) {
        this.BSSID = BSSID;
        this.RSSI = RSSI;
        this.SSID = SSID;
    }

    public String getBSSID() {
        return BSSID;
    }

    public String getSSID() {
        return SSID;
    }

    public int getRSSI() {
        return RSSI;
    }
}
