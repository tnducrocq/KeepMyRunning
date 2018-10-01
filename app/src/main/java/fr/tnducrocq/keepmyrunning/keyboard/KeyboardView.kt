package fr.tnducrocq.keepmyrunning.keyboard

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.widget.LinearLayout
import fr.tnducrocq.keepmyrunning.R
import fr.tnducrocq.keepmyrunning.keyboard.enums.KeyboardButtonEnum
import fr.tnducrocq.keepmyrunning.keyboard.interfaces.KeyboardButtonClickedListener
import java.util.*

/**
 * Created by stoyan and olivier on 1/13/15.
 */
class KeyboardView @JvmOverloads constructor(private val mContext: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0) : LinearLayout(mContext, attrs, defStyleAttr), View.OnClickListener {
    private var mKeyboardButtonClickedListener: KeyboardButtonClickedListener? = null

    private var mButtons: MutableList<KeyboardButtonView>? = null

    init {
        initializeView( )
    }

    private fun initializeView( ) {
        val inflater = mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val v = inflater.inflate(R.layout.view_keyboard, this)
        initKeyboardButtons(v)
    }

    /**
     * Init the keyboard buttons (onClickListener)
     */
    private fun initKeyboardButtons(view: View) {
        mButtons = ArrayList()
        mButtons!!.add(view.findViewById<View>(R.id.pin_code_button_0) as KeyboardButtonView)
        mButtons!!.add(view.findViewById<View>(R.id.pin_code_button_1) as KeyboardButtonView)
        mButtons!!.add(view.findViewById<View>(R.id.pin_code_button_2) as KeyboardButtonView)
        mButtons!!.add(view.findViewById<View>(R.id.pin_code_button_3) as KeyboardButtonView)
        mButtons!!.add(view.findViewById<View>(R.id.pin_code_button_4) as KeyboardButtonView)
        mButtons!!.add(view.findViewById<View>(R.id.pin_code_button_5) as KeyboardButtonView)
        mButtons!!.add(view.findViewById<View>(R.id.pin_code_button_6) as KeyboardButtonView)
        mButtons!!.add(view.findViewById<View>(R.id.pin_code_button_7) as KeyboardButtonView)
        mButtons!!.add(view.findViewById<View>(R.id.pin_code_button_8) as KeyboardButtonView)
        mButtons!!.add(view.findViewById<View>(R.id.pin_code_button_9) as KeyboardButtonView)

        for (button in mButtons!!) {
            button.setOnClickListener(this)
        }
    }

    override fun onClick(v: View) {
        if (mKeyboardButtonClickedListener == null) {
            return
        }

        val id = v.id
        if (id == R.id.pin_code_button_0) {
            mKeyboardButtonClickedListener!!.onKeyboardClick(KeyboardButtonEnum.BUTTON_0)
        } else if (id == R.id.pin_code_button_1) {
            mKeyboardButtonClickedListener!!.onKeyboardClick(KeyboardButtonEnum.BUTTON_1)
        } else if (id == R.id.pin_code_button_2) {
            mKeyboardButtonClickedListener!!.onKeyboardClick(KeyboardButtonEnum.BUTTON_2)
        } else if (id == R.id.pin_code_button_3) {
            mKeyboardButtonClickedListener!!.onKeyboardClick(KeyboardButtonEnum.BUTTON_3)
        } else if (id == R.id.pin_code_button_4) {
            mKeyboardButtonClickedListener!!.onKeyboardClick(KeyboardButtonEnum.BUTTON_4)
        } else if (id == R.id.pin_code_button_5) {
            mKeyboardButtonClickedListener!!.onKeyboardClick(KeyboardButtonEnum.BUTTON_5)
        } else if (id == R.id.pin_code_button_6) {
            mKeyboardButtonClickedListener!!.onKeyboardClick(KeyboardButtonEnum.BUTTON_6)
        } else if (id == R.id.pin_code_button_7) {
            mKeyboardButtonClickedListener!!.onKeyboardClick(KeyboardButtonEnum.BUTTON_7)
        } else if (id == R.id.pin_code_button_8) {
            mKeyboardButtonClickedListener!!.onKeyboardClick(KeyboardButtonEnum.BUTTON_8)
        } else if (id == R.id.pin_code_button_9) {
            mKeyboardButtonClickedListener!!.onKeyboardClick(KeyboardButtonEnum.BUTTON_9)
        }
    }

    fun setKeyboardButtonClickedListener(keyboardButtonClickedListener: (KeyboardButtonEnum) -> Unit, rippleAnimationEnd: () -> Unit) {
        val listener = KeyboardButtonClickedListenerImp(keyboardButtonClickedListener, rippleAnimationEnd)
        this.mKeyboardButtonClickedListener = listener
        for (button in mButtons!!) {
            button.setOnRippleAnimationEndListener(listener)
        }
    }

    fun setKeyboardButtonClickedListener(keyboardButtonClickedListener: (KeyboardButtonEnum) -> Unit) {
        setKeyboardButtonClickedListener(keyboardButtonClickedListener, {})
    }

    fun setKeyboardButtonClickedListener(keyboardButtonClickedListener: KeyboardButtonClickedListener) {
        this.mKeyboardButtonClickedListener = keyboardButtonClickedListener
        for (button in mButtons!!) {
            button.setOnRippleAnimationEndListener(mKeyboardButtonClickedListener!!)
        }
    }
}

data class KeyboardButtonClickedListenerImp(
        val keyboardButtonClickedListener: (KeyboardButtonEnum) -> Unit,
        val rippleAnimationEnd: () -> Unit
) : KeyboardButtonClickedListener {

    override fun onKeyboardClick(keyboardButtonEnum: KeyboardButtonEnum) {
        keyboardButtonClickedListener(keyboardButtonEnum)
    }

    override fun onRippleAnimationEnd() {
        rippleAnimationEnd()
    }
}
