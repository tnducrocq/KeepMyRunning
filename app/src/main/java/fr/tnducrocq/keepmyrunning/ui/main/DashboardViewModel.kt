package fr.tnducrocq.keepmyrunning.ui.main

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel;
import fr.tnducrocq.keepmyrunning.UnitUtils
import fr.tnducrocq.keepmyrunning.model.Running
import fr.tnducrocq.keepmyrunning.provider.Outcome
import fr.tnducrocq.keepmyrunning.provider.RunningProvider
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers

class DashboardViewModel : ViewModel() {

    val result = MutableLiveData<Outcome<ArrayList<Running>>>()

    var distance = MutableLiveData<Long>()
    var duration = MutableLiveData<Long>()

    var mostSpeed = MutableLiveData<String>()
    var mustDuration = MutableLiveData<Long>()
    val list = ArrayList<Running>()

    private val disposables = CompositeDisposable()

    fun load(userId: String) {
        disposables.add(RunningProvider.fetchRunning(userId, fromCache = true)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({

                    var distance = 0L
                    var time = 0L
                    for (item: Running in it) {
                        time += item.time
                        distance += item.distance
                    }
                    this.duration.value = time
                    this.distance.value = distance

                    this.mustDuration.value = it.maxBy { running -> running.time }?.time

                    val speeder = it.minBy { running -> (running.time / 60000F) / (running.distance / 1000F) }
                    speeder?.let {
                        this.mostSpeed.value = UnitUtils.minAtKm(it.time, it.distance.toInt())
                    }
                    result.value = Outcome.success(it)
                }, {
                    result.value = Outcome.failure(it)
                }))
    }

    override fun onCleared() {
        super.onCleared()
        disposables.clear()
    }

}
