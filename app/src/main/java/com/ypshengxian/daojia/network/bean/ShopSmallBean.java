package com.ypshengxian.daojia.network.bean;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * 页面
 *
 * @author Yan
 * @date 2018-04-01
 * @note -
 * ---------------------------------------------------------------------------------------------------------------------
 * @modified -
 * @date -
 * @note -
 */


public class ShopSmallBean implements Parcelable {
    public String name;
    public double distance;
    public String id;
    public String secCityId;
    public String trdCityId;
    /** 精度 */
    public String lat;
    /** 维度 */
    public String lng;
    /** 是否选中 */
    public boolean isChecked;



    public ShopSmallBean(String name, double distance,String id, String secCityId,
                         String trdCityId,String lat,String lng) {
        this.name = name;
        this.distance = distance;
        this.id=id;
        this.secCityId=secCityId;
        this.trdCityId=trdCityId;
        this.lat=lat;
        this.lng=lng;
    }
    @Override
    public boolean equals(Object obj) {
        if(!(obj instanceof ShopSmallBean)){
            return false;
        }
        ShopSmallBean eoi = (ShopSmallBean) obj;
        if (eoi.name.equals(name)) {
            return true;
        }
        return false;
    }

    @Override
    public int hashCode() {
        return super.hashCode();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.name);
        dest.writeDouble(this.distance);
        dest.writeString(this.id);
        dest.writeString(this.secCityId);
        dest.writeString(this.trdCityId);
        dest.writeString(this.lat);
        dest.writeString(this.lng);
    }

    protected ShopSmallBean(Parcel in) {
        this.name = in.readString();
        this.distance = in.readDouble();
        this.id = in.readString();
        this.secCityId = in.readString();
        this.trdCityId = in.readString();
        this.lat = in.readString();
        this.lng = in.readString();
    }

    public static final Parcelable.Creator<ShopSmallBean> CREATOR = new Parcelable.Creator<ShopSmallBean>() {
        @Override
        public ShopSmallBean createFromParcel(Parcel source) {
            return new ShopSmallBean(source);
        }

        @Override
        public ShopSmallBean[] newArray(int size) {
            return new ShopSmallBean[size];
        }
    };
}
