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
 * 用于已知宽度,高度自适应
 *
 * @author Yan
 * @date 2018-04-08
 * @note -
 * ---------------------------------------------------------------------------------------------------------------------
 * @modified -
 * @date -
 * @note -
 */


public class FloorTransformation implements Transformation<Bitmap> {
    /** View */
    private ImageView view;

    public FloorTransformation(ImageView view) {
        this.view=view;
    }

    @NonNull
    @Override
    public Resource<Bitmap> transform(@NonNull Context context, @NonNull Resource<Bitmap> resource, int outWidth, int outHeight) {
        Bitmap bitmap=resource.get();
        int weight=view.getWidth();
        if(null==bitmap||weight==0){
            return resource;
        }
        if(bitmap.getHeight()==0){

            return  resource;
        }
        double aspectRatio = (double) bitmap.getHeight() / (double) bitmap.getWidth();
        int targetHeight = (int) (weight * aspectRatio);
        if (targetHeight != 0 && weight != 0) {
            Bitmap result = Bitmap.createScaledBitmap(bitmap, weight, targetHeight, false);
            return  BitmapResource.obtain(result, Glide.get(context).getBitmapPool());
        }
        return resource;
    }

    @Override
    public void updateDiskCacheKey(@NonNull MessageDigest messageDigest) {

    }
}
