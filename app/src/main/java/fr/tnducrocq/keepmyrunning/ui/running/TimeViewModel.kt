package fr.tnducrocq.keepmyrunning.ui.running

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class TimeViewModel : ViewModel() {

    private var value = ""
    val str = MutableLiveData<String>()
    val empty = MutableLiveData<Boolean>()

    fun init() {
        str.value = if (str.value == null) "000000" else str.value
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
        if (text.length > 5) {
            return
        }
        value = "$text$c"
        str.value = fill(value)

        empty.value = value.isEmpty()
    }

    private fun fill(str: String): String {
        var tmp = str
        while (tmp.length < 6) {
            tmp = "0$tmp"
        }
        return tmp
    }

    private fun trim(str: String): String {
        if (str.isEmpty())
            return ""
        return str.toInt().toString()
    }

    fun time(): Long {
        val it = str.value!!
        val hour = it.substring(0, 2).toLong() * 3600L
        val min = it.substring(2, 4).toLong() * 60L
        val sec = it.substring(4, 6).toLong()
        return (hour + min + sec) * 1000
    }
}
