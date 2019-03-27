package com.kuyuzhiqi.bizbase.entity;

import java.io.Serializable;

/**
 * 显示图片的实体
 */
public class PhotoInfo implements Serializable {
    private String md5;
    private String url;
    private String path;
    private int showType;
    private Double longitude;
    private Double latitude;
    private String address;

    public String getMd5() {
        return md5;
    }

    public void setMd5(String md5) {
        this.md5 = md5;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public int getShowType() {
        return showType;
    }

    public void setShowType(int showType) {
        this.showType = showType;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        PhotoInfo photoInfo = (PhotoInfo) o;

        return md5 != null ? md5.equals(photoInfo.md5) : photoInfo.md5 == null;
    }

    @Override
    public int hashCode() {
        return md5 != null ? md5.hashCode() : 0;
    }

    public void setLongitude(Double longitude) {
        this.longitude = longitude;
    }

    public Double getLongitude() {
        return longitude;
    }

    public Double getLatitude() {
        return latitude;
    }

    public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }
}