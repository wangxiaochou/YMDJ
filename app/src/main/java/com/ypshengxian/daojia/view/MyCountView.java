package com.ypshengxian.daojia.view;

import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ypshengxian.daojia.R;
import com.ypshengxian.daojia.utils.ToastUtils;
import com.ypshengxian.daojia.utils.Utils;


/**
 * 计算View
 *
 * @author Yan
 * @date 2017-10-24
 * @note -
 * ---------------------------------------------------------------------------------------------------------------------
 * @modified -
 * @date -
 * @note -
 */
public class MyCountView extends LinearLayout implements View.OnClickListener {
    /** 减 */
    private ImageButton mIbMinus;
    /** 加 */
    private ImageButton mIbPlus;
    /** 数量 */
    private TextView mNum;
    /** 布局加载工具 */
    private LayoutInflater mInflater;
    /** 上下文对象 */
    private Context mContext= Utils.getContext();
    /** 监听对象 */
    private  OnNumChangedListener mListener;
    /** 数量 */
    private  int mAmount=1;



    public MyCountView(Context context) {
        this(context,null);
    }

    public MyCountView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs,0);
    }

    public MyCountView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView();
    }

    /**
     * 初始化布局
     *
     */
    private void initView() {
        mInflater= (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View rootView=mInflater.inflate(R.layout.item_count_view,this,true);
        mIbMinus= (ImageButton) rootView.findViewById(R.id.item_count_view_minus);
        mIbPlus= (ImageButton) rootView.findViewById(R.id.item_count_view_plus);
        mNum= (TextView) rootView.findViewById(R.id.item_count_view_num);
        mNum.setText(String.valueOf(mAmount));
        mIbMinus.setOnClickListener(this);
        mIbPlus.setOnClickListener(this);
    }

    /**
     * 设置商品数量
     *
     * @param num  数量
     */
    public void setNum(int num){
        this.mAmount=num;
        mNum.setText(String.valueOf(mAmount));
    };
    /**
     * 设置监听
     *
     * @param mListener 监听对象
     */
    public void setMListener(OnNumChangedListener mListener) {
        this.mListener = mListener;
    }
    @Override
    public void onClick(View v) {
        int id=v.getId();
        switch (id){
            case R.id.item_count_view_plus:
                mAmount+=1;
                if(null!=mListener){
                    mListener.onAdd(mAmount);
                }
                break;
            case R.id.item_count_view_minus:
                if(mAmount<=1){
                    ToastUtils.showShortToast("数量不能少于1");
                    return;
                }
                mAmount-=1;
                if(null!=mListener){
                mListener.onMinus(mAmount);
            }
                break;
            default:
                break;
        }
        mNum.setText(String.valueOf(mAmount));

    }

    /**
     * 数量变化监听
     *
     */
   public interface OnNumChangedListener{
        /**
         * 加
         * @param num  数量
         */
        void onAdd(int num);

        /**
         * 减
         * @param num 数量
         */
        void onMinus(int num);
    }
}
