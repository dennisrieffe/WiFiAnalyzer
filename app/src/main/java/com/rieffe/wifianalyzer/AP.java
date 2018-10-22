package com.rieffe.wifianalyzer;

class AP {
    private final String BSSID;
    private final int RSSI;
    private String SSID;
    private String capabilities;

    public AP(String BSSID, int RSSI, String SSID, String capabilities) {
        this.BSSID = BSSID;
        this.RSSI = RSSI;
        this.SSID = SSID;
        this.capabilities = capabilities;
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

    public void setSSID(String newSSID) {
        SSID = newSSID;
    }

    public String getCapabilities() {
        return capabilities;
    }
}
