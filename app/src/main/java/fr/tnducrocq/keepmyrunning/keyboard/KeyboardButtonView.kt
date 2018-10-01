package fr.tnducrocq.keepmyrunning.keyboard

import android.content.Context
import android.util.AttributeSet
import android.util.Log
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.widget.ImageView
import android.widget.RelativeLayout
import android.widget.TextView
import com.andexert.library.RippleAnimationListener
import com.andexert.library.RippleView
import fr.tnducrocq.keepmyrunning.R
import fr.tnducrocq.keepmyrunning.keyboard.interfaces.KeyboardButtonClickedListener

/**
 * Created by stoyan and oliviergoutay on 1/13/15.
 */
class KeyboardButtonView @JvmOverloads constructor(private val mContext: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0) : RelativeLayout(mContext, attrs, defStyleAttr), RippleAnimationListener {

    private var mKeyboardButtonClickedListener: KeyboardButtonClickedListener? = null
    private var mRippleView: RippleView? = null

    init {
        if (attrs != null) {
            initializeView(attrs, defStyleAttr)
        }
    }

    private fun initializeView(attrs: AttributeSet, defStyleAttr: Int) {
        val attributes = mContext.theme.obtainStyledAttributes(attrs, R.styleable.KeyboardButtonView,
                defStyleAttr, 0)
        val text = attributes.getString(R.styleable.KeyboardButtonView_lp_keyboard_button_text)
        val image = attributes.getDrawable(R.styleable.KeyboardButtonView_lp_keyboard_button_image)
        val rippleEnabled = attributes.getBoolean(R.styleable.KeyboardButtonView_lp_keyboard_button_ripple_enabled, true)

        attributes.recycle()

        val inflater = mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val view = inflater.inflate(R.layout.view_keyboard_button, this) as KeyboardButtonView

        val textView = view.findViewById<TextView>(R.id.keyboard_button_textview)
        if (textView != null) {
            textView.text = text
        }

        if (image != null) {
            val imageView = view.findViewById<ImageView>(R.id.keyboard_button_imageview)
            if (imageView != null) {
                imageView.setImageDrawable(image)
                imageView.visibility = View.VISIBLE
            }
        }

        mRippleView = view.findViewById(R.id.pin_code_keyboard_button_ripple)
        mRippleView!!.setRippleAnimationListener(this)
        if (!rippleEnabled) {
            mRippleView!!.visibility = View.INVISIBLE
        }
    }

    fun setOnRippleAnimationEndListener(keyboardButtonClickedListener: KeyboardButtonClickedListener) {
        mKeyboardButtonClickedListener = keyboardButtonClickedListener
    }

    override fun onRippleAnimationEnd() {
        if (mKeyboardButtonClickedListener != null) {
            mKeyboardButtonClickedListener!!.onRippleAnimationEnd()
        } else {
            Log.wtf(TAG, "mKeyboardButtonClickedListener == null")
        }
    }

    /**
     * Retain touches for [com.andexert.library.RippleView].
     * Otherwise views above will not have the event.
     */
    override fun onInterceptTouchEvent(event: MotionEvent): Boolean {
        onTouchEvent(event)
        return false
    }

    companion object {
        private val TAG = KeyboardButtonView::class.java.simpleName
    }
}