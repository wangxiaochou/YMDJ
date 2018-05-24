package com.ypshengxian.daojia.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.alibaba.android.vlayout.DelegateAdapter;
import com.alibaba.android.vlayout.LayoutHelper;
import com.alibaba.android.vlayout.layout.SingleLayoutHelper;
import com.chad.library.adapter.base.BaseViewHolder;
import com.ypshengxian.daojia.utils.Utils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * 基类适配器
 *
 * @author Yan
 * @date 2018/3/28
 */

public abstract class BaseDelegateAdapter<T> extends DelegateAdapter.Adapter<BaseViewHolder> {

    private LayoutHelper mLayoutHelper;
    private int mLayoutId = -1;
    private Context mContext;
    private int mViewTypeItem = -1;
    private List<T>  mData;
    /** 监听item点击事件 */
    private BaseDelegateAdapter.OnItemClickListener<T> mOnItemClickListener;

    private boolean mIsSingleLayoutHelper;

    public BaseDelegateAdapter( List<T> data,int layoutId,LayoutHelper layoutHelper,  int viewTypeItem) {
        this.mContext = Utils.getContext();
        this.mLayoutHelper = layoutHelper;
        this.mLayoutId = layoutId;
        this.mData=data;
        this.mViewTypeItem = viewTypeItem;
        this.mIsSingleLayoutHelper=layoutHelper instanceof SingleLayoutHelper;
    }
    /**
     * 数据适配器构造函数
     *
     *
     * @param data 数据
     * @param layoutId 布局文件
     * @param helper VLayout布局帮助类
     */
    public BaseDelegateAdapter( T data, int layoutId ,LayoutHelper helper,int viewTypeItem) {
        this( data == null ? new ArrayList<T>() : new ArrayList<>(Arrays.asList(data)), layoutId,helper, viewTypeItem);
    }

    @Override
    public LayoutHelper onCreateLayoutHelper() {
        return mLayoutHelper;
    }

    @NonNull
    @Override
    public BaseViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType == mViewTypeItem) {
            return  new  BaseViewHolder(LayoutInflater.from(mContext).inflate(mLayoutId, parent, false));
        }
        return null;
    }

    /**
     * 设置item的监听事件
     *
     * @param onItemClickListener 监听器
     */
    public BaseDelegateAdapter<T> setOnItemClickListener(BaseDelegateAdapter.OnItemClickListener<T> onItemClickListener) {
        mOnItemClickListener = onItemClickListener;

        return this;
    }

    @Override
    public void onBindViewHolder(@NonNull BaseViewHolder holder, int position) {
        convert(holder,mData.get(position),position);
    }

    /**
     * 外调方法
     * @param holder holder
     * @param data 数据
     * @param position 位置
     */
    public abstract void convert(BaseViewHolder holder, T data, int position);

    /**
     * 必须重写不然会出现滑动不流畅的情况
     *
     * @param position
     * @return
     */
    @Override
    public int getItemViewType(int position) {
        return mViewTypeItem;
    }

    /**
     * 条目数量
     * @return 数量
     */
    @Override
    public int getItemCount() {
        if (mIsSingleLayoutHelper && !dataIsEmpty()) {
            return 1;
        }
        return   mData == null ? 0 : mData.size();
    }

    /**
     * 判断数据是否为空
     * @return 是否
     */

    public boolean dataIsEmpty() {
        return mData == null || mData.size() == 0;
    }
    /**
     * 追加数据
     *
     * @param data 数据
     */
    public void appendData(List<T> data) {
        if (mData == null) {
            mData = data;
        } else {
            mData.addAll(data);
        }
        notifyDataSetChanged();
    }

    /**
     * 获取数据
     *
     * @param position 位置
     * @return 数据
     */
    public T getData(int position) {
        ArrayList<T> list = new ArrayList<>();
        list.addAll(mData);
        return list.get(position);
    }

    /**
     * 数据集合
     *
     * @return 数据集合
     */
    public List<T> getData() {
        ArrayList<T> list = new ArrayList<>();
        list.addAll(mData);
        return list;
    }

    /**
     * 设置数据
     *
     * @param data 数据
     */
    public void setData(List<T> data) {
        mData = data;
        notifyDataSetChanged();
    }

    /**
     * 设置单个数据
     * @param data 数据
     */
    public void setData(T data){
        mData.add(data);
        notifyDataSetChanged();
    }
    /**
     * 在指定位置添加数据
     *
     * @param position 索引位置
     * @param item 子数据
     */
    public void addData(int position, T item) {
        mData.add(position, item);
        notifyItemInserted(position);
    }

    /**
     * 在数据尾部添加数据
     *
     * @param item 子数据
     */
    public void addDataLast(T item) {
        int position = mData.size();
        mData.add(position, item);
        notifyItemInserted(position);
    }

    /**
     * 删除数据
     */
    public void removeAllData() {
        int size = mData.size();
        mData.clear();
        notifyItemRangeRemoved(0, size);
    }

    /**
     * 在指定位置删除数据
     *
     * @param position 索引位置
     */
    public void removeData(int position) {
        mData.remove(position);
        notifyItemRemoved(position);
    }

    /**
     * 删除子数据
     *
     * @param item 子数据
     */
    public void removeData(T item) {
        int i = mData.indexOf(item);
        if (i >= 0) {
            mData.remove(i);
            notifyItemRemoved(i);
        }
    }
    /**
     * item的监听接口
     */
    public interface OnItemClickListener<T> {
        /**
         * 项目被点击
         *
         * @param view 视图
         * @param itemType 类型(参见ITEM_TYPE_NORMAL等)
         * @param position 位置
         */
        public void onItemClick(View view, int itemType, int position,T data);
    }
}
