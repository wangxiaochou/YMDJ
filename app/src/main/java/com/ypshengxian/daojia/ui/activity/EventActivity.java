package com.ypshengxian.daojia.ui.activity;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.ypshengxian.daojia.R;
import com.ypshengxian.daojia.annotation.ActivityOption;
import com.ypshengxian.daojia.base.BaseMVPYpFreshActicity;
import com.ypshengxian.daojia.count.Count;
import com.ypshengxian.daojia.loader.LoaderFactory;
import com.ypshengxian.daojia.mvp.contract.IEventContract;
import com.ypshengxian.daojia.mvp.presenter.EventPresenter;
import com.ypshengxian.daojia.network.bean.EventBean;
import com.ypshengxian.daojia.utils.LogUtils;
import com.ypshengxian.daojia.utils.TitleUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * 专题活动
 *
 * @author Yan
 * @date 2018-03-21
 * @note -
 * ---------------------------------------------------------------------------------------------------------------------
 * @modified -
 * @date -
 * @note -
 */
@ActivityOption(reqLogin = true)
public class EventActivity extends BaseMVPYpFreshActicity<IEventContract.View, EventPresenter> implements
        IEventContract.View {
    /** view */
    private RecyclerView mRlEventCount;
    /** 数据源 */
    private List<EventBean> datas = new ArrayList<EventBean>();
    /** 适配器 */
    private BaseQuickAdapter<EventBean, BaseViewHolder> adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initView();
        mPresenter.requestData();
    }

    @Override
    protected EventPresenter createPresenter() {
        return new EventPresenter();
    }

    @Override
    public int getContentView() {
        return R.layout.activity_event;
    }


    /**
     * 初始化
     */
    private void initView() {
        TitleUtils.setTitleBar(this,"专题活动");
        mRlEventCount = (RecyclerView) findViewById(R.id.rl_activity_event_count);
        mRlEventCount.setLayoutManager(new LinearLayoutManager(this));
        adapter = new BaseQuickAdapter<EventBean, BaseViewHolder>(R.layout.item_event, datas) {

            @Override
            protected void convert(BaseViewHolder helper, EventBean item) {
                helper.setText(R.id.tv_item_event_text, item.name);
                LoaderFactory.getLoader().loadNet(helper.getView(R.id.iv_item_event_img), item.img);
            }
        };
        mRlEventCount.setAdapter(adapter);
        adapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {

                Bundle bundle=new Bundle();
                bundle.putString(Count.SHOP_DETAILS_ID,String.valueOf(datas.get(position).id));
                bundle.putString(Count.TITLE_NAME,"专题详情");
                startActivityEx(ShopDetailsActivity.class,bundle);
            }
        });
    }

    @Override
    public void onResponseData(boolean isSuccess, List<EventBean> data) {
        if (isSuccess) {
            LogUtils.e(data.get(0).name);
            datas.addAll(data);
            adapter.setNewData(datas);
            adapter.notifyDataSetChanged();
        }
    }
}
