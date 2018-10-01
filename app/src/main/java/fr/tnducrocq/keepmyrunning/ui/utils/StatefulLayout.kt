package fr.tnducrocq.keepmyrunning.ui.utils

import android.content.Context
import android.content.res.TypedArray
import android.text.TextUtils
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.Button
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.ProgressBar
import android.widget.TextView

import androidx.annotation.AnimRes
import androidx.annotation.StringRes
import fr.tnducrocq.keepmyrunning.R

/**
 * Android layout to show most common state templates like loading, empty, error etc. To do that all you need to is
 * wrap the target area(view) with StatefulLayout. For more information about usage look
 * [here](https://github.com/gturedi/StatefulLayout#usage)
 */
class StatefulLayout @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null) : LinearLayout(context, attrs, 0) {

    /**
     * Indicates whether to place the animation on state changes
     */
    var isAnimationEnabled: Boolean = false
    /**
     * Animation started begin of state change
     */
    var inAnimation: Animation? = null
    /**
     * Animation started end of state change
     */
    var outAnimation: Animation? = null
    /**
     * to synchronize transition animations when animation duration shorter then request of state change
     */
    private var animCounter: Int = 0

    private var content: View? = null
    private var stContainer: LinearLayout? = null
    private var stProgress: ProgressBar? = null
    private var stImage: ImageView? = null
    private var stMessage: TextView? = null
    private var stButton: Button? = null

    init {
        val array = context.theme.obtainStyledAttributes(attrs, R.styleable.stfStatefulLayout, 0, 0)
        isAnimationEnabled = array.getBoolean(R.styleable.stfStatefulLayout_stfAnimationEnabled, DEFAULT_ANIM_ENABLED)
        inAnimation = anim(array.getResourceId(R.styleable.stfStatefulLayout_stfInAnimation, DEFAULT_IN_ANIM))
        outAnimation = anim(array.getResourceId(R.styleable.stfStatefulLayout_stfOutAnimation, DEFAULT_OUT_ANIM))
        array.recycle()
    }

    fun setInAnimation(@AnimRes anim: Int) {
        inAnimation = anim(anim)
    }

    fun setOutAnimation(@AnimRes anim: Int) {
        outAnimation = anim(anim)
    }

    override fun onFinishInflate() {
        super.onFinishInflate()
        if (childCount > 1) throw IllegalStateException(MSG_ONE_CHILD)
        if (isInEditMode) return  // hide state views in designer
        orientation = LinearLayout.VERTICAL
        content = getChildAt(0) // assume first child as content
        LayoutInflater.from(context).inflate(R.layout.stf_template, this, true)
        stContainer = findViewById<View>(R.id.stContainer) as LinearLayout
        stProgress = findViewById<View>(R.id.stProgress) as ProgressBar
        stImage = findViewById<View>(R.id.stImage) as ImageView
        stMessage = findViewById<View>(R.id.stMessage) as TextView
        stButton = findViewById<View>(R.id.stButton) as Button
    }

    // content //

    fun showContent() {
        if (isAnimationEnabled) {
            stContainer!!.clearAnimation()
            content!!.clearAnimation()
            val animCounterCopy = ++animCounter
            if (stContainer!!.visibility == View.VISIBLE) {
                outAnimation!!.setAnimationListener(object : CustomAnimationListener() {
                    override fun onAnimationEnd(animation: Animation) {
                        if (animCounter != animCounterCopy) return
                        stContainer!!.visibility = View.GONE
                        content!!.visibility = View.VISIBLE
                        content!!.startAnimation(inAnimation)
                    }
                })
                stContainer!!.startAnimation(outAnimation)
            }
        } else {
            stContainer!!.visibility = View.GONE
            content!!.visibility = View.VISIBLE
        }
    }

    @JvmOverloads
    fun showLoading(@StringRes resId: Int = R.string.stfLoadingMessage) {
        showLoading(str(resId))
    }

    fun showLoading(message: String) {
        showCustom(CustomStateOptions()
                .message(message)
                .loading())
    }

    @JvmOverloads
    fun showEmpty(@StringRes resId: Int = R.string.stfEmptyMessage) {
        showEmpty(str(resId))
    }

    fun showEmpty(message: String) {
        showCustom(CustomStateOptions()
                .message(message)
                .image(R.drawable.stf_ic_empty))
    }

    // error //

    fun showError(clickListener: View.OnClickListener) {
        showError(R.string.stfErrorMessage, clickListener)
    }

    fun showError(@StringRes resId: Int, clickListener: View.OnClickListener) {
        showError(str(resId), clickListener)
    }

    fun showError(message: String, clickListener: View.OnClickListener) {
        showCustom(CustomStateOptions()
                .message(message)
                .image(R.drawable.stf_ic_error)
                .buttonText(str(R.string.stfButtonText))
                .buttonClickListener(clickListener))
    }

    // offline

    fun showOffline(clickListener: View.OnClickListener) {
        showOffline(R.string.stfOfflineMessage, clickListener)
    }

    fun showOffline(@StringRes resId: Int, clickListener: View.OnClickListener) {
        showOffline(str(resId), clickListener)
    }

    fun showOffline(message: String, clickListener: View.OnClickListener) {
        showCustom(CustomStateOptions()
                .message(message)
                .image(R.drawable.stf_ic_offline)
                .buttonText(str(R.string.stfButtonText))
                .buttonClickListener(clickListener))
    }

    // location off //

    fun showLocationOff(clickListener: View.OnClickListener) {
        showLocationOff(R.string.stfLocationOffMessage, clickListener)
    }

    fun showLocationOff(@StringRes resId: Int, clickListener: View.OnClickListener) {
        showLocationOff(str(resId), clickListener)
    }

    fun showLocationOff(message: String, clickListener: View.OnClickListener) {
        showCustom(CustomStateOptions()
                .message(message)
                .image(R.drawable.stf_ic_location_off)
                .buttonText(str(R.string.stfButtonText))
                .buttonClickListener(clickListener))
    }

    // custom //

    /**
     * Shows custom state for given options. If you do not set buttonClickListener, the button will not be shown
     *
     * @param options customization options
     * @see fr.tnducrocq.keepmyrunning.ui.utils.CustomStateOptions
     */
    fun showCustom(options: CustomStateOptions) {
        if (isAnimationEnabled) {
            stContainer!!.clearAnimation()
            content!!.clearAnimation()
            val animCounterCopy = ++animCounter
            if (stContainer!!.visibility == View.GONE) {
                outAnimation!!.setAnimationListener(object : CustomAnimationListener() {
                    override fun onAnimationEnd(animation: Animation) {
                        if (animCounterCopy != animCounter) return
                        content!!.visibility = View.GONE
                        stContainer!!.visibility = View.VISIBLE
                        stContainer!!.startAnimation(inAnimation)
                    }
                })
                content!!.startAnimation(outAnimation)
                state(options)
            } else {
                outAnimation!!.setAnimationListener(object : CustomAnimationListener() {
                    override fun onAnimationEnd(animation: Animation) {
                        if (animCounterCopy != animCounter)
                            return
                        state(options)
                        stContainer!!.startAnimation(inAnimation)
                    }
                })
                stContainer!!.startAnimation(outAnimation)
            }
        } else {
            content!!.visibility = View.GONE
            stContainer!!.visibility = View.VISIBLE
            state(options)
        }
    }

    // helper methods //

    private fun state(options: CustomStateOptions) {
        if (!TextUtils.isEmpty(options.message)) {
            stMessage!!.visibility = View.VISIBLE
            stMessage!!.text = options.message
        } else {
            stMessage!!.visibility = View.GONE
        }

        if (options.isLoading) {
            stProgress!!.visibility = View.VISIBLE
            stImage!!.visibility = View.GONE
            stButton!!.visibility = View.GONE
        } else {
            stProgress!!.visibility = View.GONE
            if (options.imageRes != 0) {
                stImage!!.visibility = View.VISIBLE
                stImage!!.setImageResource(options.imageRes)
            } else {
                stImage!!.visibility = View.GONE
            }

            if (options.clickListener != null) {
                stButton!!.visibility = View.VISIBLE
                stButton!!.setOnClickListener(options.clickListener)
                if (!TextUtils.isEmpty(options.buttonText)) {
                    stButton!!.text = options.buttonText
                }
            } else {
                stButton!!.visibility = View.GONE
            }
        }
    }

    private fun str(@StringRes resId: Int): String {
        return context.getString(resId)
    }

    private fun anim(@AnimRes resId: Int): Animation {
        return AnimationUtils.loadAnimation(context, resId)
    }

    companion object {

        private val MSG_ONE_CHILD = "StatefulLayout must have one child!"
        private val DEFAULT_ANIM_ENABLED = true
        private val DEFAULT_IN_ANIM = android.R.anim.fade_in
        private val DEFAULT_OUT_ANIM = android.R.anim.fade_out
    }

}// loading //
// empty //