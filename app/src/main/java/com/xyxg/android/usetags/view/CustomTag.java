package com.xyxg.android.usetags.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.text.TextPaint;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;

import com.xyxg.android.usetags.R;

/**
 * Created by YMlion on 2015/4/15.
 */
public class CustomTag extends View {
    private boolean isFavored;
    private int numBackgrount;
    private int numColor;
    private int numSize;
    private int textBackgrount;
    private int textColor;
    private int textSize;
    private String text;
    private String num;
    private float textWidth;
    private float numWidth;
    private float percent;
    private int textHeight;
    private int numHeight;

    public CustomTag(Context context) {
        this(context, null);
    }

    public CustomTag(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CustomTag(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        TypedArray a = context.getTheme().obtainStyledAttributes(attrs, R.styleable.CustomTag, defStyleAttr, 0);
        int count = a.getIndexCount();
        for (int i = 0; i < count; i++) {
            int attr = a.getIndex(i);
            switch (attr) {
                case R.styleable.CustomTag_text:
                    text = a.getString(attr);
                    break;
                case R.styleable.CustomTag_num:
                    num = "" + a.getInt(attr, 1);
                    break;
                case R.styleable.CustomTag_isFavored:
                    isFavored = a.getBoolean(attr, true);
                    break;
                case R.styleable.CustomTag_numBackground:
                    numBackgrount = a.getColor(attr, Color.GRAY);
                    break;
                case R.styleable.CustomTag_numColor:
                    numColor = a.getColor(attr, Color.WHITE);
                    break;
                case R.styleable.CustomTag_numSize:
                    numSize = a.getDimensionPixelSize(attr, 15);
                    break;
                case R.styleable.CustomTag_textBackground:
                    textBackgrount = a.getColor(attr, Color.BLACK);
                    break;
                case R.styleable.CustomTag_textColor:
                    textColor = a.getColor(attr, Color.WHITE);
                    break;
                case R.styleable.CustomTag_textSize:
                    textSize = a.getDimensionPixelSize(attr, 15);
                    break;
                default:
                    break;
            }
        }

        a.recycle();
        Log.d("ylm", "text" + text);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int sizeWidth = MeasureSpec.getSize(widthMeasureSpec);
        int modeWidth = MeasureSpec.getMode(widthMeasureSpec);
        int sizeHeight = MeasureSpec.getSize(heightMeasureSpec);
        int modeHeight = MeasureSpec.getMode(heightMeasureSpec);

        int width = 0;
        int height = 0;

        Paint paint = new Paint();
        //计算字体的宽高
        paint.setTextSize(textSize);
        textWidth = paint.measureText(text);
        Paint.FontMetrics fm = paint.getFontMetrics();
        textHeight = (int) Math.ceil(fm.descent - fm.ascent);
        paint.setTextSize(numSize);
        numWidth = paint.measureText(num);
        fm = paint.getFontMetrics();
        numHeight = (int) Math.ceil(fm.descent - fm.ascent);
        percent = textWidth / (textWidth + numWidth);

        if (modeWidth == MeasureSpec.EXACTLY) {
            width = sizeWidth;
            textWidth = width * percent;
            numWidth = width - textWidth;
        } else {
            width = (int) (textWidth + numWidth);
            width += getPaddingRight() + getPaddingLeft();
        }
        if (modeHeight == MeasureSpec.EXACTLY) {
            height = sizeHeight;
        } else {
            height = Math.max(textHeight, numHeight);
            height += getPaddingTop() + getPaddingBottom();
        }
        Log.d("ylm", "Width: " + width + ": Height: " + height);
        setMeasuredDimension(width, height);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        Paint paint = new Paint();
        paint.setColor(textBackgrount);
        canvas.drawRect(0, 0, getPaddingLeft() + textWidth, getHeight(), paint);
        paint.setColor(numBackgrount);
        canvas.drawRect(getWidth() - numWidth - getPaddingRight(), 0, getWidth(), getHeight(), paint);

        paint.setColor(textColor);
        paint.setTextSize(textSize);
        paint.setTextAlign(Paint.Align.LEFT);
        int x = getPaddingLeft();
        canvas.drawText(text, getPaddingLeft(), getHeight() - getPaddingBottom(), paint);
        paint.setColor(numColor);
        paint.setTextSize(numSize);
        canvas.drawText(num, getPaddingLeft() + textWidth, getHeight() - getPaddingBottom(), paint);
    }
}
