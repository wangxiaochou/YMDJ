package com.ypshengxian.daojia.ui.activity;

import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;

import com.ypshengxian.daojia.R;
import com.ypshengxian.daojia.adapter.SimpleViewPagerAdapter;
import com.ypshengxian.daojia.base.BaseMVPYpFreshActicity;
import com.ypshengxian.daojia.mvp.contract.IGuideContract;
import com.ypshengxian.daojia.mvp.presenter.GuidePresenter;

import java.util.ArrayList;
import java.util.List;

/**
 * 页面
 *
 * @author Yan
 * @date 2018-03-09
 * @note -
 * ---------------------------------------------------------------------------------------------------------------------
 * @modified -
 * @date -
 * @note -
 */
public class GuideActivity extends BaseMVPYpFreshActicity<IGuideContract.View, GuidePresenter> implements
        IGuideContract.View {
    /** 引导页列表 */
    private static final int[] GUIDE_IMAGE = {
            R.drawable.img_guide_1,
            R.drawable.img_guide_2,
            R.drawable.img_guide_3
    };



    @Override
    public void onResponseData(boolean isSuccess, Object data) {

    }

    @Override
    protected GuidePresenter createPresenter() {
        return new GuidePresenter();
    }

    @Override
    public int getContentView() {
        return R.layout.activity_guide_layout;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // 隐藏状态栏
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        Bundle bundle=getIntent().getExtras();
        initView();
    }

    /**
     * 加载视图
     *
     */
    private void initView() {

        ViewPager guidePager = (ViewPager) findViewById(R.id.vp_guide_pager);
        // 加载引导页面
        LayoutInflater inflater = getLayoutInflater();
        List<View> guideImageList = new ArrayList<>();
        for (int i = 0; i < GUIDE_IMAGE.length; i++) {
            ViewGroup imageLayout = (ViewGroup) inflater.inflate(R.layout.item_guide_image, guidePager, false);
            imageLayout.setBackgroundResource(GUIDE_IMAGE[i]);
            if (i == GUIDE_IMAGE.length - 1) {

                imageLayout.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                         startActivityEx(MainActivity.class);
                         finish();
                    }
                });
            }
            guideImageList.add(imageLayout);
        }
        guidePager.setAdapter(new SimpleViewPagerAdapter(guideImageList));
    }

}
