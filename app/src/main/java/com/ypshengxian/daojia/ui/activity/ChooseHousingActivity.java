package com.ypshengxian.daojia.ui.activity;

import android.os.Bundle;
import android.support.v7.widget.RecyclerView;

import com.ypshengxian.daojia.R;
import com.ypshengxian.daojia.base.BaseMVPYpFreshActicity;
import com.ypshengxian.daojia.mvp.contract.IChooseHousingContract;
import com.ypshengxian.daojia.mvp.presenter.ChooseHousingPresenter;
import com.ypshengxian.daojia.utils.TitleUtils;

/**
 * @author ZSH
 * @create 2018/3/20
 * @Describe 选择小区
 */
public class ChooseHousingActivity extends BaseMVPYpFreshActicity<IChooseHousingContract.View, ChooseHousingPresenter> implements IChooseHousingContract.Presenter {

    private RecyclerView mRv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initView();
    }

    @Override
    public int getContentView() {
        return R.layout.activity_choose_housing;
    }

    @Override
    protected ChooseHousingPresenter createPresenter() {
        return new ChooseHousingPresenter();
    }

    @Override
    public void requestData() {

    }

    private void initView() {
        TitleUtils.setTitleBar(this, this.getString(R.string.choose_housing_title_text));
        mRv = (RecyclerView) findViewById(R.id.rv_activity_choose_housing);

        //item
//		LayoutInflater.from(this).inflate(R.layout.item_choose_housing_rv, null);
    }
}
