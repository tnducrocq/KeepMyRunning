package fr.tnducrocq.keepmyrunning.keyboard.interfaces

import fr.tnducrocq.keepmyrunning.keyboard.enums.KeyboardButtonEnum

interface KeyboardButtonClickedListener {

    fun onKeyboardClick(keyboardButtonEnum: KeyboardButtonEnum)

    fun onRippleAnimationEnd()

}
