package com.ypshengxian.daojia.ui.activity;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.yanzhenjie.album.Action;
import com.yanzhenjie.album.Album;
import com.yanzhenjie.album.AlbumFile;
import com.yanzhenjie.album.api.widget.Widget;
import com.ypshengxian.daojia.R;
import com.ypshengxian.daojia.base.BaseMVPYpFreshActicity;
import com.ypshengxian.daojia.count.Count;
import com.ypshengxian.daojia.loader.LoaderFactory;
import com.ypshengxian.daojia.mvp.contract.IApplyAfterSalesContract;
import com.ypshengxian.daojia.mvp.presenter.ApplyAfterSalesPresenter;
import com.ypshengxian.daojia.network.base.BaseModuleApiResult;
import com.ypshengxian.daojia.network.bean.ImageBean;
import com.ypshengxian.daojia.network.bean.UploadBean;
import com.ypshengxian.daojia.utils.EncodeUtils;
import com.ypshengxian.daojia.utils.LogUtils;
import com.ypshengxian.daojia.utils.ResUtils;
import com.ypshengxian.daojia.utils.ScreenUtils;
import com.ypshengxian.daojia.utils.SpacesItemDecoration;
import com.ypshengxian.daojia.utils.TitleUtils;
import com.ypshengxian.daojia.utils.ToastUtils;
import com.ypshengxian.daojia.view.YpDialog;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.List;

import top.zibin.luban.Luban;
import top.zibin.luban.OnCompressListener;

/**
 * @author ZSH
 * @create 2018/3/20
 * @Describe 申请售后
 */

public class ApplyAfterSalesActivity extends BaseMVPYpFreshActicity<IApplyAfterSalesContract.View, ApplyAfterSalesPresenter> implements
        IApplyAfterSalesContract.View {

    //最多选择几张图片
    public static final int SELECT_COUNT = 6;

    private RecyclerView mRvAfterSales;
    /**
     * 提交
     */
    private TextView mTvConfirm;
    /** 存放图片ID */
    private List<String> ids = new ArrayList<String>();

    private BaseQuickAdapter<ImageBean, BaseViewHolder> mRvAdapter;
    private ArrayList<ImageBean> mAlbumFiles = new ArrayList<>();
    private ArrayList<AlbumFile> mAlbumCheckedList = new ArrayList<>();
    private ArrayList<String> mAlbumStringFiles = new ArrayList<>();
    /** 订单号 */
    private String mSn;
    /** 订单的金额 */
    private String mSnMoney;
    /** 头部 */
    private View mHeaderView;
    /** 底部 */
    private View mFooterView;
    /** 用户输入消息 */
    private EditText mEtMessage;
    /** 订单编号 */
    private TextView mTvSn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle bundle = getIntent().getExtras();
        if (null != bundle) {
            mSn = bundle.getString(Count.AFTER_SALE_SN);
            mSnMoney = bundle.getString(Count.AFTER_SALE_MONEY);
        }
        initView();
    }

    @Override
    public int getContentView() {
        return R.layout.activity_apply_after_sales;
    }

    @Override
    protected ApplyAfterSalesPresenter createPresenter() {
        return new ApplyAfterSalesPresenter();
    }

    private void initView() {
        mHeaderView = LayoutInflater.from(ApplyAfterSalesActivity.this).inflate(R.layout.layout_apply_after_sales_head, null);
        mEtMessage = (EditText) mHeaderView.findViewById(R.id.et_layout_after_sales_head_message);
        mTvSn = (TextView) mHeaderView.findViewById(R.id.tv_layout_after_sales_head_sn);
        mFooterView = LayoutInflater.from(ApplyAfterSalesActivity.this).inflate(R.layout.layout_apply_after_sales_footer, null);
        TitleUtils.setTitleBar(this, this.getString(R.string.apply_after_sales_title_text));
        mRvAfterSales = (RecyclerView) findViewById(R.id.rv_activity_apply_after_sales_after_sales);
        mTvConfirm = (TextView) findViewById(R.id.tv_activity_apply_after_sales_confirm);
        mTvConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String message = mEtMessage.getText().toString().trim();
                if (TextUtils.isEmpty(message)) {
                    ToastUtils.showShortToast("请输入售后原因");
                    return;
                }
                String[] imgId = new String[6];
                for (int i = 0; i < ids.size(); i++) {
                    imgId[i] = ids.get(i);
                }
                for (String s : imgId) {
                    LogUtils.e(s);
                }
                mPresenter.requestData(mSn, mSnMoney, message, imgId);
            }
        });

        initRecyclerViewData();
    }


    @Override
    public void onResponseData(boolean isSuccess, BaseModuleApiResult data) {
        if (isSuccess) {
            YpDialog dialog = new YpDialog(getBaseActivity());
            dialog.setTitle("申请成功");
            dialog.setMessage("您可以在售后查看申请进度,也可以拨打客服电话联系我们.");
            dialog.setPositiveButton("确定", true, new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    finish();
                }
            });
            dialog.show();
        }
    }

    @Override
    public void onUploadAvatar(boolean isSuccess, UploadBean data) {
        if (isSuccess) {
            ids.add(data.id);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    /**
     * rv设置manamger/adapter
     */
    private void initRecyclerViewData() {
        GridLayoutManager manager = new GridLayoutManager(this, 3);
        mRvAfterSales.setLayoutManager(manager);
        mRvAdapter = new BaseQuickAdapter<ImageBean, BaseViewHolder>(R.layout.item_apply_after_sales_iv, mAlbumFiles) {
            @Override
            protected void convert(BaseViewHolder helper, ImageBean item) {
                LinearLayout mLlNull = helper.getView(R.id.ll_item_apply_after_sales);
                RelativeLayout mRlImage = helper.getView(R.id.rl_item_apply_after_sales);
                if (item.isNUll()) {
                    ViewGroup.LayoutParams params = mLlNull.getLayoutParams();
                    params.height = (int) ((ScreenUtils.getScreenWidth() - ResUtils.getDimensionPixelSize(R.dimen.dimen_fifty)) / 3);
                    mLlNull.setLayoutParams(params);
                    mLlNull.setVisibility(View.VISIBLE);
                    mRlImage.setVisibility(View.GONE);
                } else {
                    ViewGroup.LayoutParams params = mRlImage.getLayoutParams();
                    params.height = (int) ((ScreenUtils.getScreenWidth() - ResUtils.getDimensionPixelSize(R.dimen.dimen_twenty_four)) / 3);
                    mRlImage.setLayoutParams(params);
                    mLlNull.setVisibility(View.GONE);
                    mRlImage.setVisibility(View.VISIBLE);
                    File file = new File(item.getAlbumFile().getPath());
                    if (file.exists()) {
                        LoaderFactory.getLoader().loadFile((ImageView) helper.getView(R.id.iv_item_apply_after_sales), file);
                    }
                    helper.addOnClickListener(R.id.iv_item_apply_after_sales_close);
                }
            }
        };
        mRvAfterSales.addItemDecoration(new SpacesItemDecoration((int) ResUtils.getDimensionPixelSize(R.dimen.dimen_two)));
        mRvAfterSales.setAdapter(mRvAdapter);

        mRvAdapter.addHeaderView(mHeaderView);
        mRvAdapter.addFooterView(mFooterView);
        mTvSn.setText(String.format(ResUtils.getString(R.string.pay_order_sn), mSn));

        ImageBean bean = new ImageBean();
        bean.setNUll(true);
        mAlbumFiles.add(bean);
        mRvAdapter.setNewData(mAlbumFiles);

        mRvAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                ImageBean item = (ImageBean) adapter.getData().get(position);
                if (item.isNUll() && adapter.getData().size() <= SELECT_COUNT) {
                    initAlbum();
                }
            }
        });
        mRvAdapter.setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener() {
            @Override
            public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
                if (view.getId() == R.id.iv_item_apply_after_sales_close) {
                    mAlbumCheckedList.remove(position);
                    mAlbumFiles.remove(position);
                    if (!mAlbumFiles.get(mAlbumFiles.size() - 1).isNUll() && mAlbumFiles.size() < SELECT_COUNT) {
                        ImageBean bean = new ImageBean();
                        bean.setNUll(true);
                        mAlbumFiles.add(mAlbumFiles.size(), bean);
                    }
                    mRvAdapter.notifyDataSetChanged();
                }
            }
        });
    }

    /**
     * 选择图片
     */
    private void initAlbum() {
        //选择图片
        Album.image(ApplyAfterSalesActivity.this)
                //多选模式，单选模式为：singleChoice()。
                .multipleChoice()
                // 请求码，会在listener中返回。
                .requestCode(200)
                // 是否在Item中出现相机。
                .camera(true)
                // 页面列表的列数。
                .columnCount(3)
                // 最多选择几张图片。
                .selectCount(SELECT_COUNT)
                //要反选的列表  比如选择一次再次选择，可以把上次选择的传入。
                .checkedList(mAlbumCheckedList)
                .widget(Widget.newDarkBuilder(ApplyAfterSalesActivity.this)
                        .title("选择图片")
                        .statusBarColor(ResUtils.getColor(R.color.color_theme))
                        .toolBarColor(ResUtils.getColor(R.color.color_theme))
                        // 图片或者视频选择框的选择器。
                        .mediaItemCheckSelector(ResUtils.getColor(R.color.color_page_line), ResUtils.getColor(R.color.color_theme))
                        // 切换文件夹时文件夹选择框的选择器。
                        .bucketItemCheckSelector(ResUtils.getColor(R.color.color_page_line), ResUtils.getColor(R.color.color_theme))
                        // 用来配置当没有发现图片/视频时的拍照按钮和录视频按钮的风格。
                        .buttonStyle(Widget.ButtonStyle.newDarkBuilder(ApplyAfterSalesActivity.this)
                                .setButtonSelector(Color.WHITE, Color.GRAY)
                                .build())
                        .build())
                .onResult(new Action<ArrayList<AlbumFile>>() {
                    @Override
                    public void onAction(int requestCode, @NonNull ArrayList<AlbumFile> result) {
                        mAlbumCheckedList = result;
                        mAlbumFiles.clear();
                        for (int i = 0; i < result.size(); i++) {
                            LogUtils.e("AlbumFile:" + result.get(i).getSize());
                            ImageBean bean = new ImageBean();
                            bean.setAlbumFile(result.get(i));
                            mAlbumFiles.add(bean);
                            mAlbumStringFiles.add(result.get(i).getPath());
                        }
                        if (result.size() < SELECT_COUNT) {
                            ImageBean bean = new ImageBean();
                            bean.setNUll(true);
                            mAlbumFiles.add(result.size(), bean);
                        }
                        mRvAdapter.setNewData(mAlbumFiles);

                        for (int i = 0; i < result.size(); i++) {
                            File file = new File(result.get(i).getPath());
                            FileInputStream fs = null;
                            try {
                                fs = new FileInputStream(file);
                                ByteArrayOutputStream outStream = new ByteArrayOutputStream();
                                byte[] buffer = new byte[1024];
                                int len = 0;
                                while (-1 != (len = fs.read(buffer))) {
                                    outStream.write(buffer, 0, len);
                                }
                                outStream.close();
                                fs.close();
                                String fileByte = EncodeUtils.base64Encode2String(outStream.toByteArray());
                                mPresenter.uploadAvatar(fileByte);
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }

//						initLuban(mAlbumStringFiles);
                    }
                })
                .onCancel(new Action<String>() {
                    @Override
                    public void onAction(int requestCode, @NonNull String result) {
                        LogUtils.e("取消选择");
                    }
                })
                .start();
    }

    /**
     * 压缩图片
     *
     * @param mAlbumStringFiles 文件
     */
    private void initLuban(ArrayList<String> mAlbumStringFiles) {
        Luban.with(ApplyAfterSalesActivity.this)
                .load(mAlbumStringFiles)
                .ignoreBy(100)
                .setTargetDir(null)
                .setCompressListener(new OnCompressListener() {
                    @Override
                    public void onStart() {

                    }

                    @Override
                    public void onSuccess(File file) {
                        LogUtils.e("file:" + file.getPath());
                    }

                    @Override
                    public void onError(Throwable e) {

                    }
                }).launch();
    }

}
