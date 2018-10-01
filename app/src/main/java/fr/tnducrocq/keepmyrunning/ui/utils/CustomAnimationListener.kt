package fr.tnducrocq.keepmyrunning.ui.utils

import android.view.animation.Animation

/**
 * simple class to reduce callback hell
 */
open class CustomAnimationListener : Animation.AnimationListener {

    override fun onAnimationStart(animation: Animation) {}

    override fun onAnimationEnd(animation: Animation) {}

    override fun onAnimationRepeat(animation: Animation) {}

}