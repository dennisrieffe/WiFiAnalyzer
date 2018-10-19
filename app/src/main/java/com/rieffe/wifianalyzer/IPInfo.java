package com.rieffe.wifianalyzer;

public class IPInfo {

    private String info;
    private String data;

    public IPInfo(String _info, String _data) {
        info = _info;
        data = _data;
    }

    public String getInfo() {
        return info;
    }

    public String getData() {
        return data;
    }
}
