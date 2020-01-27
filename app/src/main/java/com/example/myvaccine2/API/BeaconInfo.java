package com.example.myvaccine2.API;

import com.google.gson.annotations.SerializedName;

// DB에 있는 웹 비콘 관련 정보를 받아오기 위한 DTO 클래스
public class BeaconInfo {
    @SerializedName("IDX")
    private Integer idx;
    @SerializedName("Division")
    private String division;
    @SerializedName("URL")
    private String url;
    @SerializedName("FileName")
    private String fileName;

    public Integer getIdx() {
        return idx;
    }

    public void setIdx(Integer idx) {
        this.idx = idx;
    }

    public String getDivision() {
        return division;
    }

    public void setDivision(String division) {
        this.division = division;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }
}
