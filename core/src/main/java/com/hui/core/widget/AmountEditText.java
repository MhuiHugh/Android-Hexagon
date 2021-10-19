package com.hui.core.widget;

import android.content.Context;
import android.text.Editable;
import android.text.InputFilter;
import android.text.TextWatcher;
import android.util.AttributeSet;
import androidx.appcompat.widget.AppCompatEditText;

import com.hui.core.utils.StringUtil;

/**
 * 金额输入EditText
 * Created by Menghui on 2016/8/22.
 */
public class AmountEditText extends AppCompatEditText {

    AmountInputFilter inputFilter;//输入过滤
    String lastStr = "";

    public AmountEditText(Context context) {
        super(context);
        init();
    }

    public AmountEditText(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public AmountEditText(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    /**
     * 获取输入金额，自动处理千分符
     *
     * @return
     */
    public double getMoneyDouble() {
        String str = this.getText().toString().trim();
        if(str.isEmpty()){
            return 0.0;
        }
        double money = 0.0;
        money = StringUtil.getDoubleFromMillesimal(str);
        return money;
    }

    /**
     * 设置金额，复制显示千分符方法
     *
     * @param d
     */
    public void setText(double d) {
        String temp = StringUtil.getMillesimalAvailable(d);
        //避免重复调用死循环
        if (!temp.equals(lastStr)) {
            if (d <= 0) {
                temp = "";
            }
            lastStr = temp;
            inputFilter.setNoFilter(true);
            setText(temp);
            setSelection(getText().length());
        }
    }

    /**
     * 显示金额
     */
    public void showMoney() {
        inputFilter.setNoFilter(true);
        double d = getMoneyDouble();
        String temp = StringUtil.getDoubleWithFormat(d);
        //避免重复调用死循环
        if (!temp.equals(lastStr)) {
            if (d <= 0) {
                temp = "";
            }
            lastStr = temp;
            inputFilter.setNoFilter(true);
            setText(temp);
            setSelection(getText().length());
        }
    }

    /**
     * 显示千分符
     */
    public void showPerMilleSymbolMoney() {
        double d = getMoneyDouble();
        String temp = StringUtil.getMillesimalAvailable(d);
        //LogUtil.v("showPerMilleSymbolMoney() :" + temp + " -- " + lastStr);
        //避免重复调用死循环
        if (!temp.equals(lastStr)) {
            if (d <= 0) {
                temp = "";
            }
            lastStr = temp;
            inputFilter.setNoFilter(true);
            setText(temp);
            setSelection(getText().length());
        }
    }

    private void init() {
        inputFilter = new AmountInputFilter(this, 1000000000, 2);
        //输入过滤,最大值10亿，小数点后最多2位
        InputFilter[] filters = {inputFilter};
        this.setFilters(filters);
        //输入类型小数  android:inputType="numberDecimal"
        this.setInputType(8194);
        this.setLines(1);
        //文本变化监听
        this.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void afterTextChanged(Editable editable) {
                showPerMilleSymbolMoney();
            }
        });
    }
}

