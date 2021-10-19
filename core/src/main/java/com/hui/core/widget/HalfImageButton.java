package com.hui.core.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.hui.core.R;

/**
 * 图片按钮自定义控件，图片宽高为View一半自适应
 * Created by HughM
 */
public class HalfImageButton extends LinearLayout {

    private ImageView imgView;
    private Drawable src;

    public HalfImageButton(Context context){
        super(context);
    }

    public HalfImageButton(Context context, AttributeSet attrs) {
        super(context, attrs);
        LayoutInflater.from(context).inflate(R.layout.widget_half_image_button, this, true);
        TypedArray typeA = context.obtainStyledAttributes(attrs,
                R.styleable.HalfImageButton);
        src = typeA.getDrawable(R.styleable.HalfImageButton_ImgViewSrc);
        typeA.recycle();
        imgView=(ImageView)findViewById(R.id.half_img_btn);
        imgView.setImageDrawable(src);
    }

    public HalfImageButton(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

}
