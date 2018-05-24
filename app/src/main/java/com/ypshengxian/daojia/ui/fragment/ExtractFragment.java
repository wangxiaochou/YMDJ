package com.ypshengxian.daojia.ui.fragment;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.ypshengxian.daojia.R;
import com.ypshengxian.daojia.base.BaseMVPYpFreshFragment;
import com.ypshengxian.daojia.count.Count;
import com.ypshengxian.daojia.loader.LoaderFactory;
import com.ypshengxian.daojia.mvp.contract.IExtractContract;
import com.ypshengxian.daojia.mvp.presenter.ExtractPresenter;
import com.ypshengxian.daojia.network.bean.CodeBean;
import com.ypshengxian.daojia.ui.activity.GetGoodsOrderInfoActivity;
import com.ypshengxian.daojia.utils.ResUtils;
import com.ypshengxian.daojia.utils.ToastUtils;
import com.ypshengxian.daojia.utils.Utils;
import com.ypshengxian.daojia.view.EmptyView;
import com.ypshengxian.daojia.view.SimpleTitleBarBuilder;
import com.ypshengxian.daojia.view.recy.BannerLayout;

import java.util.ArrayList;
import java.util.List;

/**
 * 提取界面
 *
 * @author Yan
 * @date 2018-03-19
 * @note -
 * ---------------------------------------------------------------------------------------------------------------------
 * @modified -
 * @date -
 * @note -
 */
public class ExtractFragment extends BaseMVPYpFreshFragment<IExtractContract.View, ExtractPresenter> implements
        IExtractContract.View {
    /**
     * 数据源
     */
    private List<CodeBean> mData = new ArrayList<CodeBean>();
    /**
     * 适配器
     */
    private BaseQuickAdapter<CodeBean, BaseViewHolder> mAdapter;
    /**
     * 显示数量
     */
    private TextView textView;
    /** 空布局 */
    private EmptyView mEvView;

    /**
     * 单列
     *
     * @return this
     */
    public static ExtractFragment newInstance() {

        Bundle args = new Bundle();

        ExtractFragment fragment = new ExtractFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public int getContentView() {
        return R.layout.fragment_extract;
    }

    @Override
    public void loadData(boolean firstLoad) {
        super.loadData(firstLoad);
        mPresenter.requestData();

    }

    @Override
    public void initView() {

        SimpleTitleBarBuilder.attach(getBaseActivity(), getView())
                .setLeftVisible(true)
                .setTitleText("取货码")
                .setTitleColor(ResUtils.getColor(R.color.color_white))
                .setTitleSize(18)
                .setShadowHeight(0);
        BannerLayout layout = (BannerLayout) findViewById(R.id.bl_fragment_extract_count);
        //空布局
        mEvView = new EmptyView(Utils.getContext());
        mEvView.setMessage(R.string.msg_claim_goods_empty);
        textView = (TextView) findViewById(R.id.tv_fragment_extract_position);
        mAdapter = new BaseQuickAdapter<CodeBean, BaseViewHolder>(R.layout.item_extract, mData) {
            @Override
            protected void convert(BaseViewHolder helper, CodeBean item) {
                helper.setText(R.id.tv_item_extract_time, item.delivery_time);
                helper.setText(R.id.tv_item_extract_number, item.code);
                helper.setText(R.id.tv_item_extract_sn, String.format(ResUtils.getString(R.string.order_all_number), item.sn));
                if (!TextUtils.isEmpty(item.qr)) {
                    LoaderFactory.getLoader().loadNet((ImageView) helper.getView(R.id.iv_item_extract_img), item.qr);
                }
            }
        };

        mAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                if (mData.size() > 0 && !TextUtils.equals("暂无订单号", mData.get(0).sn)) {
                    Bundle bundle = new Bundle();
                    bundle.putString(Count.GET_GOODS_ORDER_SN, mData.get(position).sn);
                    startActivityEx(GetGoodsOrderInfoActivity.class, bundle);
                } else {
                    ToastUtils.showShortToast("请先购买商品~");
                }
            }
        });

        layout.setOnBannerItemClickListener(new BannerLayout.OnBannerItemClickListener() {
            @Override
            public void onItemClick(int position) {

            }

            @Override
            public void onScrollPosition(int position) {
                int size = mData.size();
                textView.setText(position + 1 + "/" + size);
            }
        });
        layout.setAdapter(mAdapter);


    }

    @Override
    protected ExtractPresenter createPresenter() {
        return new ExtractPresenter();
    }

    @Override
    public void onResponseData(boolean isSuccess, List<CodeBean> data) {
        if (isSuccess) {
            mData = data;
            mAdapter.setNewData(mData);
            int size = mData.size();
            textView.setText(1 + "/" + size);
        }
        setEmptyView();
    }

    /**
     * 设置空布局
     */
    private void setEmptyView() {
        int size = mData.size();
        if (0 == size) {
            CodeBean codeBean = new CodeBean();
            codeBean.delivery_time = "暂无取货时间";
            codeBean.sn = "暂无订单号";
            codeBean.code = "暂无取货码";
            codeBean.qr = "";
            mData.add(codeBean);
            textView.setText(1 + "/" + mData.size());
            mAdapter.setNewData(mData);
        }
    }
}
