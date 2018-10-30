package com.rieffe.wifianalyzer;

public class IPInfo {

    private String info;
    private String data;
    private int picture;

    public IPInfo(String _info, String _data, int _picture) {
        info = _info;
        data = _data;
        picture = _picture;
    }

    public String getInfo() {
        return info;
    }

    public String getData() {
        return data;
    }

    public int getPicture() {
        return picture;
    }
}
