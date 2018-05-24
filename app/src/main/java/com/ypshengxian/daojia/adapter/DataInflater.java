package com.ypshengxian.daojia.adapter;

import android.view.View;

import com.alibaba.android.vlayout.DelegateAdapter;
import com.alibaba.android.vlayout.layout.SingleLayoutHelper;
import com.chad.library.adapter.base.BaseViewHolder;
import com.ypshengxian.daojia.R;

/**
 * 标题
 *
 * @author Yan
 * @date 2018-03-25
 * @note -
 * ---------------------------------------------------------------------------------------------------------------------
 * @modified -
 * @date -
 * @note -
 */
public class DataInflater {
    /**
     * 添加无图片的title
     * @param adapter 总适配器
     * @return 适配器
     */
    public static BaseDelegateAdapter<String> inflateTitleItem(DelegateAdapter adapter,String title,OnTitleClickListener listener) {
        SingleLayoutHelper helper = new SingleLayoutHelper();
        helper.setMarginTop(14);
        BaseDelegateAdapter<String> titleAdapter= new BaseDelegateAdapter<String>(title,R.layout.item_main_tiltle, helper,8) {
            @Override
            public void convert(BaseViewHolder holder, String data, int position) {
                holder.setText(R.id.tv_item_title_title,data);
                holder.getView(R.id.rl_item_title_layout).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        listener.onTitleClick(v,position,title);
                    }
                });
            }

        };
        adapter.addAdapter(titleAdapter);
        titleAdapter.setData(title);
        return  titleAdapter;
    }

    /**
     * 添加限时特惠
     * @param adapter
     * @param title
     * @return
     */
    public static BaseDelegateAdapter<String> inflateImgItem(DelegateAdapter adapter,String title,OnTitleClickListener listener) {
        SingleLayoutHelper helper = new SingleLayoutHelper();
        helper.setMarginTop(14);
        BaseDelegateAdapter<String> titleAdapter= new BaseDelegateAdapter<String>(title,R.layout.item_main_img_tiltle, helper,10) {
            @Override
            public void convert(BaseViewHolder holder, String data, int position) {
                   holder.getView(R.id.rl_item_img_title_layout).setOnClickListener(new View.OnClickListener() {
                       @Override
                       public void onClick(View v) {
                           listener.onTitleClick(v,position,title);
                       }
                   });
            }

        };

        adapter.addAdapter(titleAdapter);
        titleAdapter.setData(title);
        return  titleAdapter;
    }

    /**
     * 首页商品详情
     * @param adapter 适配器
     * @param title  标题
     * @return
     */
    public static BaseDelegateAdapter<String> inflateImgTitleItem(DelegateAdapter adapter,String title){
        SingleLayoutHelper helper = new SingleLayoutHelper();
        helper.setMarginTop(14);
        BaseDelegateAdapter<String> titleAdapter= new BaseDelegateAdapter<String>(title,R.layout.item_main_img_tiltle, helper,11) {

            @Override
            public void convert(BaseViewHolder holder, String data, int position) {

            }

        };
        adapter.addAdapter(titleAdapter);
       titleAdapter.addData(0,title);
        return  titleAdapter;
    }
   public interface OnTitleClickListener{
        /**
         * 点击事件
         * @param title title
         * @param  position 位置
         * @param  view  点击的view
         */
        void onTitleClick(View view,int position,String title);
    }
}
