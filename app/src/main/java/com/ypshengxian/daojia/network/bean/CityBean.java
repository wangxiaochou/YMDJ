package com.ypshengxian.daojia.network.bean;

import android.os.Parcel;
import android.os.Parcelable;

import com.ypshengxian.daojia.network.base.BaseModuleApiResult;

import me.yokeyword.indexablerv.IndexableEntity;

/**
 * 城市列表
 *
 * @author Yan
 * @date 2018-04-01
 * @note -
 * ---------------------------------------------------------------------------------------------------------------------
 * @modified -
 * @date -
 * @note -
 */


public class CityBean  extends BaseModuleApiResult implements IndexableEntity, Parcelable {
    /**
     * name : 重庆市
     * id : 12803
     * parentId : 0
     */

    public String name;
    public String id;
    public String parentId;
    public String pinyin;
    /** 标记是否选中 */
    public  boolean isChecked=false;


    @Override
    public String getFieldIndexBy() {
        return pinyin;
    }

    @Override
    public void setFieldIndexBy(String indexField) {
        this.pinyin=indexField;

    }

    @Override
    public void setFieldPinyinIndexBy(String pinyin) {
           this.pinyin=pinyin;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.name);
        dest.writeString(this.id);
        dest.writeString(this.parentId);
        dest.writeString(this.pinyin);

    }

    public CityBean() {
    }

    protected CityBean(Parcel in) {
        this.name = in.readString();
        this.id = in.readString();
        this.parentId = in.readString();
        this.pinyin=in.readString();
    }

    public static final Parcelable.Creator<CityBean> CREATOR = new Parcelable.Creator<CityBean>() {
        @Override
        public CityBean createFromParcel(Parcel source) {
            return new CityBean(source);
        }

        @Override
        public CityBean[] newArray(int size) {
            return new CityBean[size];
        }
    };

    @Override
    public boolean equals(Object obj) {
        if(!(obj instanceof CityBean)){
            return false;
        }
        CityBean eoi = (CityBean) obj;
        if (eoi.name.equals(name)) {
            return true;
        }
        return false;
    }

    @Override
    public int hashCode() {
        return super.hashCode();
    }
}
