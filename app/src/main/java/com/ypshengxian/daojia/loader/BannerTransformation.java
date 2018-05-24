package com.ypshengxian.daojia.loader;

import android.content.Context;
import android.graphics.Bitmap;
import android.support.annotation.NonNull;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.Transformation;
import com.bumptech.glide.load.engine.Resource;
import com.bumptech.glide.load.resource.bitmap.BitmapResource;

import java.security.MessageDigest;

/**
 * glide 图片处理
 *
 * @author Yan
 * @date 2017-11-16
 * @note -  用于图片适应视图宽高固定的图片处理
 * ---------------------------------------------------------------------------------------------------------------------
 * @modified -
 * @date -
 * @note -
 */
public class BannerTransformation implements Transformation<Bitmap> {
    private ImageView view;

    public BannerTransformation(ImageView view) {
        this.view=view;
    }

    @NonNull
    @Override
    public Resource<Bitmap> transform(@NonNull Context context, @NonNull Resource<Bitmap> resource, int outWidth, int outHeight) {
        Bitmap bitmap=resource.get();
        int width=view.getWidth();
        int height=view.getHeight();
        if(0==bitmap.getWidth()||0==bitmap.getHeight()){
            return  resource;
        }
        if(0!=width&&0!=height){
            Bitmap result= Bitmap.createScaledBitmap(bitmap,width,height,false);
            return BitmapResource.obtain(result, Glide.get(context).getBitmapPool());
        }else {
            return  resource;
        }

    }

    @Override
    public void updateDiskCacheKey(MessageDigest messageDigest) {

    }
}
