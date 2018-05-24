package com.ypshengxian.daojia.ui.activity;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bigkoo.pickerview.builder.OptionsPickerBuilder;
import com.bigkoo.pickerview.builder.TimePickerBuilder;
import com.bigkoo.pickerview.listener.OnOptionsSelectListener;
import com.bigkoo.pickerview.listener.OnTimeSelectListener;
import com.bigkoo.pickerview.view.OptionsPickerView;
import com.bigkoo.pickerview.view.TimePickerView;
import com.yanzhenjie.album.Action;
import com.yanzhenjie.album.Album;
import com.yanzhenjie.album.AlbumFile;
import com.yanzhenjie.album.api.widget.Widget;
import com.ypshengxian.daojia.R;
import com.ypshengxian.daojia.base.BaseMVPYpFreshActicity;
import com.ypshengxian.daojia.count.Count;
import com.ypshengxian.daojia.loader.LoaderFactory;
import com.ypshengxian.daojia.mvp.contract.IUserDataContract;
import com.ypshengxian.daojia.mvp.presenter.UserDataPresenter;
import com.ypshengxian.daojia.network.bean.UploadBean;
import com.ypshengxian.daojia.network.bean.UserProfileBean;
import com.ypshengxian.daojia.pop.CameraPopWindow;
import com.ypshengxian.daojia.utils.EncodeUtils;
import com.ypshengxian.daojia.utils.LogUtils;
import com.ypshengxian.daojia.utils.ResUtils;
import com.ypshengxian.daojia.utils.TimeUtils;
import com.ypshengxian.daojia.utils.TitleUtils;
import com.ypshengxian.daojia.utils.ToastUtils;
import com.ypshengxian.daojia.view.CircularImageView;
import com.ypshengxian.daojia.view.ExtendEditText;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import okhttp3.MediaType;
import okhttp3.RequestBody;

/**
 * @author ZSH
 * @create 2018/3/20
 * @Describe 个人资料
 */
public class UserDataActivity extends BaseMVPYpFreshActicity<IUserDataContract.View, UserDataPresenter> implements IUserDataContract.View,
        View.OnClickListener {

    private CircularImageView mCiHead;
    /**
     * 昵称
     */
    private ExtendEditText mTvNickname;
    private RelativeLayout mRlNickname;
    /**
     * 请选择
     */
    private TextView mTvBirth;
    private RelativeLayout mRlBirth;
    /**
     * 请选择
     */
    private TextView mTvSex;
    private RelativeLayout mRlSex;

    private ArrayList<AlbumFile> mAlbumFiles;
    private ArrayList<String> mAlbumStringFiles = new ArrayList<String>();
    /** 性别 */
    private String[] mSex = {"男", "女", "保密"};
    private List<String> mSexList;
    /** 用户头像URL */
    private String mUrl;
    /** 用户昵称 */
    private String mName;
    /** 用户性别 */
    private String mGender;
    /** 用户生日 */
    private String mBirthday;
    /** 用户头像ID */
    private String mUserImgId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle bundle = getIntent().getExtras();
        if (null != bundle) {
            mUrl = bundle.getString(Count.USER_URL);
            mName = bundle.getString(Count.USER_NAME);
            mGender = bundle.getString(Count.USER_GENDER);
            mBirthday = bundle.getString(Count.USER_BIRTHDAY);
        }
        initView();
    }

    @Override
    public int getContentView() {
        return R.layout.activity_user_data;
    }

    @Override
    protected UserDataPresenter createPresenter() {
        return new UserDataPresenter();
    }


    public void initView() {
        TitleUtils.setTitleBar(this, R.string.user_data_title_text, "保存", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //male、female、secret
                String userGender = null;
                mName = mTvNickname.getText().toString().trim();
                switch (mGender) {
                    case "男":
                        userGender = "male";
                        break;
                    case "女":
                        userGender = "female";
                        break;
                    case "保密":
                        userGender = "secret";
                        break;
                    default:
                        break;
                }
                mPresenter.updateUserProfile(mName, userGender, mBirthday, mUserImgId);
            }
        });
        mCiHead = (CircularImageView) findViewById(R.id.ci_activity_user_data_head);
        mTvNickname = (ExtendEditText) findViewById(R.id.tv_activity_user_data_nickname);
        mRlNickname = (RelativeLayout) findViewById(R.id.rl_activity_user_data_nickname);
        mTvBirth = (TextView) findViewById(R.id.tv_activity_user_data_birth);
        mRlBirth = (RelativeLayout) findViewById(R.id.rl_activity_user_data_birth);
        mTvSex = (TextView) findViewById(R.id.tv_activity_user_data_sex);
        mRlSex = (RelativeLayout) findViewById(R.id.rl_activity_user_data_sex);
        mCiHead.setOnClickListener(this);
        mRlBirth.setOnClickListener(this);
        mRlSex.setOnClickListener(this);
        initValues();
    }

    /**
     * 设置用户数据
     */
    private void initValues() {
        if (!TextUtils.isEmpty(mUrl)) {
            LoaderFactory.getLoader().loadNet(mCiHead, mUrl);
        } else {
            LoaderFactory.getLoader().loadResource(mCiHead, R.mipmap.icon_app);
        }
        if (!TextUtils.isEmpty(mBirthday)) {
            mTvBirth.setText(mBirthday);
        }
        if (!TextUtils.isEmpty(mGender)) {
            switch (mGender) {
                case "male":
                    mGender = "男";
                    break;
                case "female":
                    mGender = "女";
                    break;
                case "secret":
                    mGender = "保密";
                    break;
                default:
                    break;
            }
            mTvSex.setText(mGender);
        }
        if (!TextUtils.isEmpty(mName)) {
            mTvNickname.setText(mName);
        }

    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            default:
                break;
            case R.id.ci_activity_user_data_head:
                final CameraPopWindow popWindow = new CameraPopWindow(getBaseActivity());
                popWindow.showAtLocation(mCiHead, Gravity.BOTTOM, 0, 0);
                popWindow.setButtonClickListener(new CameraPopWindow.OnButtonClickListener() {
                    @Override
                    public void onImageClick() {
                        selectImage(mCiHead);
                        popWindow.dismiss();
                    }
                });
                break;
            case R.id.rl_activity_user_data_birth:
                //设置生日
                initTimePick();
                break;
            case R.id.rl_activity_user_data_sex:
                //选择性别
                initSexPick();
                break;
        }
    }

    /**
     * 选择性别
     */
    private void initSexPick() {
        //条件选择器
        mSexList = Arrays.asList(mSex);
        OptionsPickerView<String> mPickSex = new OptionsPickerBuilder(UserDataActivity.this, new OnOptionsSelectListener() {
            @Override
            public void onOptionsSelect(int options1, int options2, int options3, View v) {
                mGender = mSexList.get(options1);
                mTvSex.setText(mGender);
            }
        })
                .setSubmitColor(ResUtils.getColor(R.color.color_theme))
                .setCancelColor(ResUtils.getColor(R.color.color_theme))
                .build();
        mPickSex.setPicker(mSexList);
        mPickSex.show();
    }

    /**
     * 选择时间
     */
    private void initTimePick() {
        TimePickerView pvTime = new TimePickerBuilder(this, new OnTimeSelectListener() {
            @Override
            public void onTimeSelect(Date date, View v) {
                mBirthday = TimeUtils.date2String(date, "yyyy-MM-dd");
                //确定按钮
                mTvBirth.setText(mBirthday);
            }
        })
                .setType(new boolean[]{true, true, true, false, false, false})
                .isCyclic(true)
                .setSubmitColor(ResUtils.getColor(R.color.color_theme))
                .setCancelColor(ResUtils.getColor(R.color.color_theme))
                .build();
        pvTime.show();
    }

    /**
     * 从相册选择
     *
     * @param imageView View
     */
    private void selectImage(final ImageView imageView) {
        Album.image(this)
                .multipleChoice()
                .requestCode(200)
                .camera(true)
                .columnCount(4)
                .selectCount(1)
                .checkedList(mAlbumFiles)
                .widget(
                        Widget.newDarkBuilder(getBaseActivity())
                                .title("选择图片")
                                .statusBarColor(ResUtils.getColor(R.color.color_theme))
                                .navigationBarColor(ResUtils.getColor(R.color.color_theme))
                                .toolBarColor(ResUtils.getColor(R.color.color_theme))
                                .build()
                )
                .onResult(new Action<ArrayList<AlbumFile>>() {
                    @Override
                    public void onAction(int requestCode, @NonNull ArrayList<AlbumFile> result) {
                        mAlbumFiles = result;
                        File file = new File(result.get(0).getPath());
                        LoaderFactory.getLoader().loadFile(imageView, file);
                        FileInputStream fs = null;
                        try {
                            fs = new FileInputStream(result.get(0).getPath());
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
                })
                .onCancel(new Action<String>() {
                    @Override
                    public void onAction(int requestCode, @NonNull String result) {

                    }
                })
                .start();
    }

    /**
     * 拍照
     *
     * @param imageView 图片
     */
    private void takePicture(final ImageView imageView) {
        Album.camera(this)
                .image()
                .requestCode(20000)
                .onResult(new Action<String>() {
                    @Override
                    public void onAction(int requestCode, @NonNull String result) {
                        LogUtils.e(result);
                        File file = new File(result);
                        LoaderFactory.getLoader().loadFile(imageView, file);
                        RequestBody body3 = RequestBody.create(MediaType.parse("image/*"), file);
                    }
                })
                .onCancel(new Action<String>() {
                    @Override
                    public void onAction(int requestCode, @NonNull String result) {

                    }
                })
                .start();
    }


    @Override
    public void onResponseData(boolean isSuccess, Object data) {

    }

    @Override
    public void onUploadAvatar(boolean isSuccess, UploadBean data) {
        if (isSuccess) {
            mUserImgId = data.id;
            ToastUtils.showShortToast("上传成功");
        }
    }

    @Override
    public void onUpdateUserProfile(boolean isSuccess, UserProfileBean data) {
        if (isSuccess) {
            LogUtils.e(data.mediumAvatar);
            finish();
        }
    }
}
