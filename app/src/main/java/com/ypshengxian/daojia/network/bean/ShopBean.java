package com.ypshengxian.daojia.network.bean;

import android.os.Parcel;
import android.os.Parcelable;

import com.ypshengxian.daojia.network.base.BaseModuleApiResult;

/**
 * 门店列表
 *
 * @author Yan
 * @date 2018-04-01
 * @note -
 * ---------------------------------------------------------------------------------------------------------------------
 * @modified -
 * @date -
 * @note -
 */
public class ShopBean extends BaseModuleApiResult implements Parcelable {

    /**
     * name : 固镇路店
     * rootCityId : 11588   
     * secCityId : 11589
     * trdCityId : 11591
     * lat : 31.89381424939828
     * lng : 117.2661754488945
     * id : 4
     */

    public String name;
    /** 一级 */
    public String rootCityId;
    /** 二级 */
    public String secCityId;
    /** 三级 */
    public String trdCityId;
    /** 精度 */
    public String lat;
    /** 维度 */
    public String lng;
    public String id;

    @Override
    public boolean equals(Object obj) {
        if(!(obj instanceof ShopBean)){
            return false;
        }
        ShopBean eoi = (ShopBean) obj;
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
        dest.writeString(this.rootCityId);
        dest.writeString(this.secCityId);
        dest.writeString(this.trdCityId);
        dest.writeString(this.lat);
        dest.writeString(this.lng);
        dest.writeString(this.id);
    }

    public ShopBean() {
    }

    protected ShopBean(Parcel in) {
        this.name = in.readString();
        this.rootCityId = in.readString();
        this.secCityId = in.readString();
        this.trdCityId = in.readString();
        this.lat = in.readString();
        this.lng = in.readString();
        this.id = in.readString();
    }

    public static final Parcelable.Creator<ShopBean> CREATOR = new Parcelable.Creator<ShopBean>() {
        @Override
        public ShopBean createFromParcel(Parcel source) {
            return new ShopBean(source);
        }

        @Override
        public ShopBean[] newArray(int size) {
            return new ShopBean[size];
        }
    };
}
