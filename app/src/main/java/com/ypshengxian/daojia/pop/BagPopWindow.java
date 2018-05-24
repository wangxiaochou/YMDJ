package com.ypshengxian.daojia.pop;

import android.app.Activity;
import android.graphics.drawable.ColorDrawable;
import android.support.annotation.FloatRange;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.ypshengxian.daojia.R;
import com.ypshengxian.daojia.event.PayOrderEvent;
import com.ypshengxian.daojia.network.bean.PayOrderBean;
import com.ypshengxian.daojia.utils.ResUtils;
import com.ypshengxian.daojia.utils.Utils;
import com.ypshengxian.daojia.view.RecyclerDivider;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;

/**
 * 购物袋
 *
 * @author 购物袋弹窗
 * @date 2018-04-02
 * @note -
 * ---------------------------------------------------------------------------------------------------------------------
 * @modified -
 * @date -
 * @note -
 */
public class BagPopWindow extends PopupWindow implements View.OnClickListener {
    private Activity mContext;
    private View mView;
    /** 数据展示 */
    private RecyclerView mRlCount;
    /** 是否需要 */
    private TextView mNeed;
    /** 数据 */
    private List<PayOrderBean.PacketsBean> mData = new ArrayList<PayOrderBean.PacketsBean>();
    /** 适配器 */
    private BaseQuickAdapter<PayOrderBean.PacketsBean, BaseViewHolder> mAdapter;
    /** 选择的钱 */
    private String mMoney;
    /** 名称 */
    private String mTitle;
    /** 购物袋Id */
    private String mId;




    public BagPopWindow(Activity context, List<PayOrderBean.PacketsBean> mData) {
        super(context);
        this.mContext = context;
        this.mData = mData;
        initView();

    }

    private void initView() {
        mView = LayoutInflater.from(mContext).inflate(R.layout.layout_order_choose_bag_pop, null);
        mNeed = mView.findViewById(R.id.tv_layout_order_choose_bag_pop_need);
        mNeed.setOnClickListener(this);
        mRlCount = mView.findViewById(R.id.rl_layout_order_choose_bag_count);
        mRlCount.setLayoutManager(new LinearLayoutManager(Utils.getContext()));
        mAdapter = new BaseQuickAdapter<PayOrderBean.PacketsBean, BaseViewHolder>(R.layout.item_pop_order, mData) {
            @Override
            protected void convert(BaseViewHolder helper, PayOrderBean.PacketsBean item) {
                helper.setText(R.id.tv_item_pop_order_name, String.format(ResUtils.getString(R.string.pay_order_money), item.title,item.price));
            }

        };
        mRlCount.setAdapter(mAdapter);
        mRlCount.addItemDecoration(new RecyclerDivider(LinearLayoutManager.VERTICAL));
        mAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                mMoney = mData.get(position).price;
                mTitle=mData.get(position).title;
                mId=mData.get(position).id;
                BagPopWindow.this.dismiss();
            }
        });
        this.setContentView(mView);
        this.setOutsideTouchable(true);
        this.setFocusable(true);
        this.setWidth(LinearLayout.LayoutParams.MATCH_PARENT);
        this.setHeight(LinearLayout.LayoutParams.WRAP_CONTENT);
        this.setAnimationStyle(R.style.pop_shop_anim);
        this.setBackgroundDrawable(new ColorDrawable(ResUtils.getColor(R.color.color_transparent)));
        black(0.5f);
        this.setOnDismissListener(new OnDismissListener() {
            @Override
            public void onDismiss() {
                EventBus.getDefault().post(new PayOrderEvent(TextUtils.isEmpty(mMoney)?"0.00":mMoney,
                        String.format(ResUtils.getString(R.string.pay_order_pass),TextUtils.isEmpty(mTitle)?"不需要":mTitle),
                                TextUtils.isEmpty(mId)?"":mId));
                black(1f);
            }
        });
    }

    /**
     * 屏幕变暗
     *
     * @param v 灰度
     */
    private void black(@FloatRange(from = 0, to = 1) float v) {
        WindowManager.LayoutParams params = mContext.getWindow().getAttributes();
        params.alpha = v;
        mContext.getWindow().setAttributes(params);
    }


    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id) {
            case R.id.tv_layout_order_choose_bag_pop_need:
                mTitle="不需要";
                mMoney="0.00";
                this.dismiss();
                break;
            default:
                break;
        }
    }


}
