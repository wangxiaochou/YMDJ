package com.ypshengxian.daojia.ui.activity;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.ypshengxian.daojia.R;
import com.ypshengxian.daojia.base.BaseMVPYpFreshActicity;
import com.ypshengxian.daojia.count.Count;
import com.ypshengxian.daojia.mvp.contract.ISearchContract;
import com.ypshengxian.daojia.mvp.presenter.SearchPresenter;
import com.ypshengxian.daojia.preference.YPPreference;
import com.ypshengxian.daojia.utils.AppUtils;
import com.ypshengxian.daojia.utils.KeyboardUtils;
import com.ypshengxian.daojia.utils.PhoneUtils;
import com.ypshengxian.daojia.utils.ResUtils;
import com.ypshengxian.daojia.utils.Utils;
import com.ypshengxian.daojia.utils.ViewUtils;
import com.ypshengxian.daojia.view.EmptyView;
import com.ypshengxian.daojia.view.ExtendEditText;
import com.ypshengxian.daojia.view.RecyclerDivider;
import com.ypshengxian.daojia.view.SimpleTitleBarBuilder;

import java.util.ArrayList;
import java.util.List;

/**
 * @author ZSH
 * @create 2018/3/20
 * @Describe 搜索
 */
public class SearchActivity extends BaseMVPYpFreshActicity<ISearchContract.View, SearchPresenter> implements
        ISearchContract.Presenter, View.OnClickListener {
    /** 内容 */
    private RecyclerView mRlSearch;
    /** 标题栏 */
    private SimpleTitleBarBuilder simpleTitleBarBuilder;
    /** 适配器 */
    private BaseQuickAdapter<String, BaseViewHolder> mAdapter;

    /** 数据源 */
    private List<String> mData = new ArrayList<String>();
    /** 清空 */
    private LinearLayout mLlHeadDelete;
    /** head */
    private View mHeaderView;
    /** 返回 */
    private ImageButton mIBtnBack;
    /** 搜索 */
    private ExtendEditText mEdSearch;
    /** 搜索名称 */
    private String mSearchText;
    /** 空数据 */
    private EmptyView mEmptyView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initView();
    }

    @Override
    public int getContentView() {
        return R.layout.activity_search;
    }

    @Override
    protected SearchPresenter createPresenter() {
        return new SearchPresenter();
    }

    @Override
    public void requestData(String text) {

    }

    /**
     * 初始化视图
     */
    private void initView() {

        simpleTitleBarBuilder = SimpleTitleBarBuilder.attach(this)
                .inflateCenterView(R.layout.title_activity_search)
                .setBackgroundColor(ResUtils.getColor(R.color.color_theme))
                .setShadowHeight(0);
        mEdSearch = simpleTitleBarBuilder.findViewById(R.id.eet_title_activity_search_input);
        mIBtnBack = simpleTitleBarBuilder.findViewById(R.id.ib_title_activity_search_back);
        ViewUtils.setTouchDelegate(mIBtnBack, 50);

        mEdSearch.setOnEditorActionListener(new TextView.OnEditorActionListener() {

            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    getSearchText();
                    KeyboardUtils.hideSoftInput(Utils.getContext(), mEdSearch);
                    switch (mSearchText) {
                        case Count.TEST:
                            if (AppUtils.isAppDebug()) {
                                YPPreference.newInstance().setDebug(Count.TEST);
                            }
                            break;
                        case Count.PRE:
                            if (AppUtils.isAppDebug()) {
                                YPPreference.newInstance().setDebug(Count.PRE);
                            }
                            break;
                        case Count.RELEASE:
                            if (AppUtils.isAppDebug()) {
                                YPPreference.newInstance().setDebug(Count.RELEASE);
                            }
                            break;
                        default:
                            YPPreference.newInstance().saveMap(PhoneUtils.getUniquePsuedoID(), mSearchText);
                            //跳转到商品列表
                            Bundle bundle = new Bundle();
                            bundle.putString(Count.PRODUCT_LIST_GOODS_ID, mSearchText);
                            bundle.putString(Count.PRODUCT_LIST_WHERE, Count.SEARCH);
                            startActivityEx(ProductListActivity.class, bundle);
                            finish();
                            return true;
                    }

                }

                return false;
            }
        });
        mIBtnBack.setOnClickListener(this);
        mHeaderView = getLayoutInflater().inflate(R.layout.layout_search_history_top, null);
        mLlHeadDelete = mHeaderView.findViewById(R.id.ll_layout_search_history_top);
        mLlHeadDelete.setOnClickListener(this);
        //数据源
        mData = YPPreference.newInstance().getOutHis(PhoneUtils.getUniquePsuedoID());

        mRlSearch = (RecyclerView) findViewById(R.id.rl_activity_search);

        //空布局
        mEmptyView = new EmptyView(Utils.getContext());
        mEmptyView.setMessage(R.string.msg_search_empty);

        mRlSearch.setLayoutManager(new LinearLayoutManager(Utils.getContext()));
        mAdapter = new BaseQuickAdapter<String, BaseViewHolder>(R.layout.item_search_history_rv, mData) {
            @Override
            protected void convert(BaseViewHolder helper, String item) {
                helper.setText(R.id.tv_item_search_history_text, item);
            }
        };
        mRlSearch.setAdapter(mAdapter);
        mAdapter.setHeaderView(mHeaderView);
        mRlSearch.addItemDecoration(new RecyclerDivider(LinearLayoutManager.VERTICAL));

        mAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                Bundle bundle = new Bundle();
                bundle.putString(Count.PRODUCT_LIST_GOODS_ID, mData.get(position));
                bundle.putString(Count.PRODUCT_LIST_WHERE, Count.SEARCH);
                startActivityEx(ProductListActivity.class, bundle);
            }
        });
        setEmptyView();

        //history  item     R.layout.item_search_history_rv;
    }


    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id) {
            case R.id.ll_layout_search_history_top:
                YPPreference.newInstance().keyDelect();
                mData = YPPreference.newInstance().getOutHis(PhoneUtils.getUniquePsuedoID());
                mAdapter.setNewData(mData);
                setEmptyView();
                break;
            case R.id.ib_title_activity_search_back:
                finish();
                break;
            default:
                break;
        }
    }

    /**
     * 设置空布局
     */
    private void setEmptyView() {
        int size = mData.size();
        if (0 == size) {
            mAdapter.setEmptyView(mEmptyView);
        }
    }

    /**
     * 获得搜索的文本
     */
    private void getSearchText() {
        mSearchText = mEdSearch.getText().toString().trim();
    }
}
