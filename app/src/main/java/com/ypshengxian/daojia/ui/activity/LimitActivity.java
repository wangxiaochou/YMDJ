package com.ypshengxian.daojia.ui.activity;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.ypshengxian.daojia.R;
import com.ypshengxian.daojia.base.BaseMVPYpFreshActicity;
import com.ypshengxian.daojia.loader.LoaderFactory;
import com.ypshengxian.daojia.mvp.contract.ILimitContract;
import com.ypshengxian.daojia.mvp.presenter.LimitPresenter;
import com.ypshengxian.daojia.network.bean.FlashBean;
import com.ypshengxian.daojia.utils.ResUtils;
import com.ypshengxian.daojia.utils.TitleUtils;
import com.ypshengxian.daojia.utils.Utils;
import com.ypshengxian.daojia.view.RecyclerDivider;
import com.ypshengxian.daojia.view.banner.MyProgressBar;

import java.util.ArrayList;
import java.util.List;

/**
 * 限时界面
 *
 * @author Yan
 * @date 2018-03-28
 * @note -
 * ---------------------------------------------------------------------------------------------------------------------
 * @modified -
 * @date -
 * @note -
 */
public class LimitActivity extends BaseMVPYpFreshActicity<ILimitContract.View, LimitPresenter> implements
        ILimitContract.View {
    /** tab */
    private TabLayout mTlTab;
    /** 内容 */
    private RecyclerView mRlCount;
    /** 数据源 */
    private List<FlashBean> mData=new ArrayList<FlashBean>();
    /** 适配器 */
    private BaseQuickAdapter<FlashBean,BaseViewHolder> mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initView();
        mPresenter.requestData();
    }

    @Override
    public int getContentView() {
        return R.layout.activity_limit;
    }

    @Override
    public void onResponseData(boolean isSuccess, List<FlashBean> data) {
          if(isSuccess){
              this.mData=data;
              mAdapter.setNewData(mData);
          }
    }

    @Override
    protected LimitPresenter createPresenter() {
        return new LimitPresenter();
    }

    private void initView() {
        TitleUtils.setTitleBar(this,"限时特惠");
        mTlTab = (TabLayout) findViewById(R.id.tl_activity_limit_tab);
        mRlCount = (RecyclerView) findViewById(R.id.rl_activity_limit_count);
        mRlCount.setLayoutManager(new LinearLayoutManager(Utils.getContext()));
        mRlCount.addItemDecoration(new RecyclerDivider(LinearLayoutManager.VERTICAL));
        mAdapter=new BaseQuickAdapter<FlashBean, BaseViewHolder>(R.layout.item_preference,mData) {
            @Override
            protected void convert(BaseViewHolder helper, FlashBean item) {
                TextView mTvTime=helper.getView(R.id.tv_preference_limit);
                int time= (int)((item.expiry*1000)-System.currentTimeMillis());


//                Observable.interval(0, 1, TimeUnit.SECONDS)
//                        .compose(getBaseActivity().applySchedulers(ActivityEvent.DESTROY))
//                        .take(time)
//                        .subscribe(new Action1<Long>() {
//                            @Override
//                            public void call(Long aLong) {
//                                int remain = item.expiry - 1 - aLong.intValue();
//                                if (remain > 0) {
//                                    mTvTime.setText(TimeUtils.millis2String(remain*1000,"HH:mm:ss"));
//                                } else {
//                                    mTvTime.setText("00:00:00");
//                                }
//                            }
//                        });
                MyProgressBar progressBar=helper.getView(R.id.mp_preference_progress);
                progressBar.setMaxProgress(item.stock);
                progressBar.setProgress(item.sold);
                progressBar.setTextToProgressView(String.valueOf((int)(((float)((float)item.sold/(float) item.stock))*100)));
                progressBar.setTextToRobView(String.valueOf(item.sold));
                helper.setText(R.id.tv_preference_name,item.title);
                helper.setText(R.id.tv_preference_money, String.format(ResUtils.getString(R.string.main_text_new_price),item.price,item.unit));
                helper.setText(R.id.tv_preference_big_money,String.format(ResUtils.getString(R.string.class_text_old),item.origin_price,item.unit));
                helper.setText(R.id.tv_preference_folder,String.format(ResUtils.getString(R.string.class_text_folder),item.discount));
                helper.setText(R.id.tv_preference_purchase,String.format(ResUtils.getString(R.string.limit_num),item.limit));
                LoaderFactory.getLoader().loadNet(helper.getView(R.id.iv_preference_image),item.img);

            }
        };
        mRlCount.setAdapter(mAdapter);

    }
}
