package com.xyxg.android.usetags.view;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by YMlion on 2015/4/14.
 */
public class FlowLayout extends ViewGroup {


    public FlowLayout(Context context) {
        this(context, null);
    }

    public FlowLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public FlowLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int sizeWidth = MeasureSpec.getSize(widthMeasureSpec);
        int modeWidth = MeasureSpec.getMode(widthMeasureSpec);
        int sizeHeight = MeasureSpec.getSize(heightMeasureSpec);
        int modeHeight = MeasureSpec.getMode(heightMeasureSpec);
        //实际的宽高
        int width = 0;
        int height = 0;
        //每一行的宽高
        int lineWidth = 0;
        int lineHeight = 0;

        //TODO 提前记录下每一行的高度，然后在布局时不必再计算高度，然后也可以将比较小的子View放在center
        int childCount = getChildCount();
        for (int i = 0; i < childCount; i++) {
            View child = getChildAt(i);
            //测量子View
            measureChild(child, widthMeasureSpec, heightMeasureSpec);
            MarginLayoutParams lp = (MarginLayoutParams) child.getLayoutParams();
            //计算child加上margin的宽度和高度
            int childWidth = child.getMeasuredWidth() + lp.rightMargin + lp.leftMargin;
            int childHeight = child.getMeasuredHeight() + lp.topMargin + lp.bottomMargin;
            //判断当前行是否能够放下下一个子View
            if (lineWidth + childWidth > sizeWidth - getPaddingLeft() - getPaddingRight()) {
                //换行重置行宽高，累加总高
                lineWidth = childWidth;
                lineHeight = childHeight;
                height += lineHeight;
            } else {//不换行则累加宽度，判断高度
                lineWidth += childWidth;
//                lineHeight = Math.max(lineHeight, childHeight);
//                height = Math.max(lineHeight, height);
                if (lineHeight < childHeight) {
                    height = height + childHeight - lineHeight;
                } else {
                    height = Math.max(lineHeight, height);
                }
                lineHeight = Math.max(lineHeight, childHeight);
            }
            //计算总宽
            width = Math.max(width, lineWidth);
        }
        //最终加上padding值
        width += getPaddingRight() + getPaddingLeft();
        height += getPaddingBottom() + getPaddingTop();
//        Log.d("ylm", "width: " + width + ";  height: " + height);
        //根据mode选择最终的宽高
        setMeasuredDimension(modeWidth == MeasureSpec.EXACTLY ? sizeWidth : width, modeHeight == MeasureSpec.EXACTLY ? sizeHeight : height);
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        if (!changed) {
            return;
        }
        //对子View布局时，只需要行宽高和总高
        int lineWidth = 0;
        int lineHeight = 0;
        int height = 0;

        int childCount = getChildCount();
        for (int i = 0; i < childCount; i++) {
            View child = getChildAt(i);
            MarginLayoutParams lp = (MarginLayoutParams) child.getLayoutParams();
            //计算child加上margin的宽度和高度
            int childWidth = child.getMeasuredWidth() + lp.rightMargin + lp.leftMargin;
            int childHeight = child.getMeasuredHeight() + lp.topMargin + lp.bottomMargin;
            //判断当前行是否能够放下下一个子View
            if (lineWidth + childWidth > getWidth() - getPaddingLeft() - getPaddingRight()) {
                // 换行后，重置宽度，累加高度
                lineWidth = childWidth;
                lineHeight = childHeight;
                height += lineHeight;
            } else {//不换行，则累加宽度，第一行时，则要取最大的行高
                lineWidth += childWidth;
//                lineHeight = Math.max(lineHeight, childHeight);
//                height = Math.max(lineHeight, height);
                if (lineHeight < childHeight) {
                    height = height + childHeight - lineHeight;
                } else {
                    height = Math.max(lineHeight, height);
                }
                lineHeight = Math.max(lineHeight, childHeight);
            }

            //子View所在的位置
            int cl = lineWidth + lp.leftMargin - childWidth + getPaddingLeft();
            int ct = height + lp.topMargin - childHeight + getPaddingTop();
            int cr = lineWidth - lp.rightMargin + getPaddingLeft();
            int cb = height - lp.bottomMargin + getPaddingTop();
//            Log.e("ylm", "" + cl + " , " + ct + " , " + cr + " , " + cb);
            //TODO 最好将每一个子View放在center
            child.layout(cl, ct, cr, cb);
        }
    }


    @Override
    public LayoutParams generateLayoutParams(AttributeSet attrs) {
        return new MarginLayoutParams(getContext(), attrs);
    }
}
