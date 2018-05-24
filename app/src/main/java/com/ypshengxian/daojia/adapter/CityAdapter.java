package com.ypshengxian.daojia.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.chad.library.adapter.base.BaseViewHolder;
import com.ypshengxian.daojia.R;
import com.ypshengxian.daojia.network.bean.CityBean;

import me.yokeyword.indexablerv.IndexableAdapter;

/**
 * 城市适配器
 *
 * @author Yan
 * @date 2018-04-01
 * @note -
 * ---------------------------------------------------------------------------------------------------------------------
 * @modified -
 * @date -
 * @note -
 */
public class CityAdapter extends IndexableAdapter<CityBean> {
    private LayoutInflater mInflater;

    /**
     * 构造方法
     *
     * @param context 上下文
     */
    public CityAdapter(Context context) {
        mInflater = LayoutInflater.from(context);
    }

    @Override
    public RecyclerView.ViewHolder onCreateTitleViewHolder(ViewGroup parent) {
        return new BaseViewHolder(mInflater.inflate(R.layout.item_index_contact,parent,false));
    }

    @Override
    public RecyclerView.ViewHolder onCreateContentViewHolder(ViewGroup parent) {
        return new BaseViewHolder(mInflater.inflate(R.layout.item_contact,parent,false));
    }

    @Override
    public void onBindTitleViewHolder(RecyclerView.ViewHolder holder, String indexTitle) {
        BaseViewHolder vh = (BaseViewHolder) holder;
        vh.setText(R.id.tv_index,indexTitle);
    }
    @Override
    public void onBindContentViewHolder(RecyclerView.ViewHolder holder, CityBean entity) {
        BaseViewHolder vh = (BaseViewHolder) holder;
        vh.setText(R.id.tv_name,entity.name);
    }
}
