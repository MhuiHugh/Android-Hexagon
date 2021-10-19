package com.hui.core.widget

import android.content.Context
import android.graphics.drawable.Drawable
import android.text.Editable
import android.text.InputType
import android.util.AttributeSet
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.*
import com.hui.core.R

/**
 * 仿系统TextInputLayout输入控件，自定义图标，提示等属性
 */
class TextInputView : FrameLayout {

    lateinit var et: EditText
    lateinit var img: ImageView
    lateinit var tip_tv: TextView
    lateinit var hide_tv: TextView
    lateinit var line: View
    lateinit var move_ll: LinearLayout

    var hideStr: String? = ""

    @JvmOverloads
    constructor(context: Context) : super(context, null, 0) {
    }

    @JvmOverloads
    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int = 0) :
            super(context,attrs,defStyleAttr) {
        initView(context, attrs)
    }

    private fun initView(context: Context, attrs: AttributeSet) {
        LayoutInflater.from(context).inflate(R.layout.widget_text_input, this)
        val typeA = context.obtainStyledAttributes(attrs, R.styleable.TextInputView)
        val iconImgId = typeA.getDrawable(R.styleable.TextInputView_iconImg)
        val tipStr = typeA.getString(R.styleable.TextInputView_tipStr)
        hideStr = typeA.getString(R.styleable.TextInputView_hideStr)
        val lineColor = typeA.getColor(
            R.styleable.TextInputView_lineColor,
            resources.getColor(R.color.transparent)
        )

        et = findViewById(R.id.et)
        img = findViewById(R.id.img)
        tip_tv = findViewById(R.id.tip_tv)
        hide_tv = findViewById(R.id.hide_tv)
        line = findViewById(R.id.line)
        move_ll = findViewById(R.id.move_ll)

        setValues(iconImgId, tipStr, hideStr, lineColor)
        typeA.recycle()

        et.inputType=InputType.TYPE_CLASS_TEXT
        et.setOnFocusChangeListener { v, hasFocus ->
            if (hasFocus) {
                //Log.v(TAG, "获取到焦点...")
                moveUp()
            } else {
                //Log.v(TAG, "失去焦点...")
                moveDown()
            }
        }
    }

    private fun setValues(iconImg: Drawable?, tipStr: String?, hideStr: String?, lineColor: Int) {
        if (null != iconImg) {
            img.setImageDrawable(iconImg)
        }
        if (tipStr.isNullOrEmpty()) {
            tip_tv.text = Editable.Factory.getInstance().newEditable("")
        } else {
            tip_tv.text = Editable.Factory.getInstance().newEditable(tipStr)
        }
        if (hideStr.isNullOrEmpty()) {
            hide_tv.text = Editable.Factory.getInstance().newEditable("")
        } else {
            hide_tv.text = Editable.Factory.getInstance().newEditable(hideStr)
        }
        line.setBackgroundColor(lineColor)
    }

    public fun getEtStr(): String {
        return et.text.toString().trim()
    }

   private fun moveUp() {
        val etStr = et.text
        if (!etStr.isNullOrEmpty()) {
            return
        }
        val up: Animation = AnimationUtils.loadAnimation(this.context, R.anim.input_up)
        up.fillAfter = true
        up.setAnimationListener(object : Animation.AnimationListener {
            override fun onAnimationRepeat(animation: Animation?) {

            }

            override fun onAnimationStart(animation: Animation?) {
                hide_tv.visibility = View.INVISIBLE
            }

            override fun onAnimationEnd(animation: Animation?) {
                et.hint = Editable.Factory.getInstance().newEditable(hideStr)
            }
        })
        move_ll.startAnimation(up)
    }

   private fun moveDown() {
        val etStr = et.text
        if (etStr.isNullOrEmpty()) {
            et.hint = Editable.Factory.getInstance().newEditable("")
        } else {
            return
        }

        val up: Animation = AnimationUtils.loadAnimation(this.context, R.anim.input_down)
        up.setAnimationListener(object : Animation.AnimationListener {
            override fun onAnimationRepeat(animation: Animation?) {

            }

            override fun onAnimationStart(animation: Animation?) {
            }

            override fun onAnimationEnd(animation: Animation?) {
                hide_tv.visibility = View.VISIBLE
            }
        })
        move_ll.startAnimation(up)
    }

}