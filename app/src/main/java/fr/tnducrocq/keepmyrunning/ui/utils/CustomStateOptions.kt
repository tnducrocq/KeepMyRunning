package fr.tnducrocq.keepmyrunning.ui.utils

import android.view.View

import java.io.Serializable

import androidx.annotation.DrawableRes

/**
 * Model builder class to show custom state
 *
 * @see fr.tnducrocq.keepmyrunning.ui.utils.StatefulLayout.showCustom
 */
class CustomStateOptions : Serializable {

    @DrawableRes
    var imageRes: Int = 0
        private set
    var isLoading: Boolean = false
        private set
    var message: String? = null
        private set
    var buttonText: String? = null
        private set
    var clickListener: View.OnClickListener? = null
        private set

    fun image(@DrawableRes `val`: Int): CustomStateOptions {
        imageRes = `val`
        return this
    }

    fun loading(): CustomStateOptions {
        isLoading = true
        return this
    }

    fun message(`val`: String): CustomStateOptions {
        message = `val`
        return this
    }

    fun buttonText(`val`: String): CustomStateOptions {
        buttonText = `val`
        return this
    }

    fun buttonClickListener(`val`: View.OnClickListener): CustomStateOptions {
        clickListener = `val`
        return this
    }

}