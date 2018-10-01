package fr.tnducrocq.keepmyrunning.ui.running

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class DistanceViewModel : ViewModel() {

    private var value = ""
    val str = MutableLiveData<String>()
    val empty = MutableLiveData<Boolean>()

    fun init() {
        str.value = if (str.value == null) " 000" else str.value
        empty.value = if (empty.value == null) true else empty.value
    }

    fun remove() {
        if (value.isEmpty()) {
            return
        }
        value = value.substring(0, value.length - 1)
        str.value = fill(value)
        empty.value = value.isEmpty()
    }

    fun add(c: Char) {
        val text = trim(value)
        if (text.length > 3) {
            return
        }
        value = "$text$c"
        str.value = fill(value)

        empty.value = value.isEmpty()
    }

    private fun fill(str: String): String {
        var tmp = str
        while (tmp.length < 3) {
            tmp = "0$tmp"
        }
        if (tmp.length < 4) {
            tmp = " $tmp"
        }
        return tmp
    }

    private fun trim(str: String): String {
        if (str.isEmpty())
            return ""
        return str.toInt().toString()
    }

    fun distance(): Long {
        val it = str.value!!.replace(' ', '0')
        val km = it.substring(0, 2).toLong()
        val kmdec = it.substring(2, 4).toLong()
        return km * 1000 + kmdec * 10
    }
}
