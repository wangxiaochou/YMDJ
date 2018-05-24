package com.ypshengxian.daojia.view;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.ypshengxian.daojia.R;
import com.ypshengxian.daojia.utils.ResUtils;


/**
 * recycleView分割线
 *
 * @author Yan
 * @date 2017-10-19
 * @note -
 * ---------------------------------------------------------------------------------------------------------------------
 * @modified -
 * @date -
 * @note -
 */
public class RecyclerDivider extends RecyclerView.ItemDecoration {
    /** 画笔 */
    private Paint mPaint;
    /** drawable资源 ,可以拓展*/
    private Drawable mDivider=null;
    /** 分割线高度，默认为1px */
    private int mDividerHeight = 2;
    /** 列表的方向：LinearLayoutManager.VERTICAL或LinearLayoutManager.HORIZONTAL */
    private int mOrientation;


    /**
     * 自定义分割线
     *
     * @param orientation   列表方向
     */
    public RecyclerDivider(int orientation) {
        if (orientation != LinearLayoutManager.VERTICAL && orientation != LinearLayoutManager.HORIZONTAL) {
            throw new IllegalArgumentException("STU");
        }
        this.mOrientation=orientation;
        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mPaint.setColor(ResUtils.getColor(R.color.color_background));
        mPaint.setStyle(Paint.Style.FILL);
    }
    /**
     * 自定义分割线
     *
     * @param orientation 列表方向
     *
     * @param dividerHeight 分割线尺寸 PX
     */
    public RecyclerDivider(int orientation, int dividerHeight) {
        this(orientation);
        this.mDividerHeight=dividerHeight;
    }


    //获取分割线尺寸
    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        super.getItemOffsets(outRect, view, parent, state);
        if (mOrientation == LinearLayoutManager.VERTICAL) {
            outRect.set(0, 0, 0, mDividerHeight);
        } else {
            outRect.set(0, 0, mDividerHeight, 0);
        }

    }

    //绘制分割线
    @Override
    public void onDraw(Canvas c, RecyclerView parent, RecyclerView.State state) {
        super.onDraw(c, parent, state);
        if (mOrientation == LinearLayoutManager.VERTICAL) {
            drawVertical(c, parent);
            drawHorizontal(c, parent);
        } else {
            drawHorizontal(c, parent);
            drawVertical(c, parent);
        }
    }

    /**
     * 绘制横向分割线
     *
     * @param canvas  画布
     * @param parent  容器
     */
    private void drawHorizontal(Canvas canvas, RecyclerView parent) {
        final int left = parent.getPaddingLeft();
        final int right = parent.getMeasuredWidth() - parent.getPaddingRight();
        final int childSize = parent.getChildCount();
        for (int i = 0; i < childSize; i++) {
            final View child = parent.getChildAt(i);
            RecyclerView.LayoutParams layoutParams = (RecyclerView.LayoutParams) child.getLayoutParams();
            final int top = child.getBottom() + layoutParams.bottomMargin;
            final int bottom = top + mDividerHeight;
            if (mDivider != null) {
                mDivider.setBounds(left, top, right, bottom);
                mDivider.draw(canvas);
            }
            if (mPaint != null) {
                canvas.drawRect(left, top, right, bottom, mPaint);
            }
        }
    }
    /**
     * 绘制纵向分割线
     *
     * @param canvas  画布
     * @param parent  容器
     */

    private void drawVertical(Canvas canvas, RecyclerView parent) {
        final int top = parent.getPaddingTop();
        final int bottom = parent.getMeasuredHeight() - parent.getPaddingBottom();
        final int childSize = parent.getChildCount();
        for (int i = 0; i < childSize; i++) {
            final View child = parent.getChildAt(i);
            RecyclerView.LayoutParams layoutParams = (RecyclerView.LayoutParams) child.getLayoutParams();
            final int left = child.getRight() + layoutParams.rightMargin;
            final int right = left + mDividerHeight;
            if (mDivider != null) {
                mDivider.setBounds(left, top, right, bottom);
                mDivider.draw(canvas);
            }
            if (mPaint != null) {
                canvas.drawRect(left, top, right, bottom, mPaint);
            }
        }
    }
}
