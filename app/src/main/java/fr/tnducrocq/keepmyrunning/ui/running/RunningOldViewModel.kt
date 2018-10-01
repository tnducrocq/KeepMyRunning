package fr.tnducrocq.keepmyrunning.ui.running

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import java.util.concurrent.TimeUnit

class RunningOldViewModel : ViewModel() {

    private val disposables = CompositeDisposable()

    val timer = MutableLiveData<Long>()
    val started = MutableLiveData<Boolean>()

    fun setState() {
        val run = !(started.value!!)
        started.value = run
        if (run) {
            startTimer()
        } else {
            disposables.clear()
        }
    }

    private fun startTimer() {
        timer.value = 0
        disposables.add(Observable.interval(1, TimeUnit.SECONDS)
                .observeOn(AndroidSchedulers.mainThread())
                .doOnNext {
                    timer.value = timer.value!! + 1
                }
                .takeUntil<Long> { started.value }
                /*.doOnComplete {
                    started.value = false
                }*/
                .subscribe())
    }

    override fun onCleared() {
        super.onCleared()
        disposables.clear()
    }

}
