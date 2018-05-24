package com.ypshengxian.daojia.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.chad.library.adapter.base.BaseViewHolder;
import com.ypshengxian.daojia.R;
import com.ypshengxian.daojia.utils.LogUtils;

import java.util.List;

import me.yokeyword.indexablerv.IndexableHeaderAdapter;

/**
 * 头部适配器
 *
 * @author Yan
 * @date 2018-04-19
 * @note -
 * ---------------------------------------------------------------------------------------------------------------------
 * @modified -
 * @date -
 * @note -
 */
public abstract class ChooseCityHeaderAdapter extends IndexableHeaderAdapter<String> {

    /** 指示器 */
    private String mIndex;
    /** 第一个title */
    private String mIndexTitle;
    /** 解析器 */
    private LayoutInflater mInflater;
    /** 上下文 */
    private Context mContext;
    /** 当前定位城市 */
    private List<String> mHotName;
    /**
     *
     * 构造方法
     * @param context 上下文
     * @param index 指示器
     * @param indexTitle 第一个title
     */
    public ChooseCityHeaderAdapter(Context context,String index, String indexTitle,List<String> hotName) {
        super(index, indexTitle, hotName);
        this.mIndex=index;
        this.mIndexTitle=indexTitle;
        this.mContext=context;
        this.mHotName=hotName;
        mInflater=LayoutInflater.from(context);
    }

    @Override
    public int getItemViewType() {
        return mHotName.hashCode();
    }

    @Override
    public RecyclerView.ViewHolder onCreateContentViewHolder(ViewGroup parent) {
        return new BaseViewHolder(mInflater.inflate(R.layout.item_city_header,parent,false));
    }

    @Override
    public void onBindContentViewHolder(RecyclerView.ViewHolder holder, String entity) {
        LogUtils.e(entity);
       BaseViewHolder helper=(BaseViewHolder) holder;
       convert(helper, entity,helper.getLayoutPosition());
    }

    /**
     * 外调方法
     * @param holder holder
     * @param data 数据
     * @param position 位置
     */
    protected abstract void convert(BaseViewHolder holder, String data, int position);
}
