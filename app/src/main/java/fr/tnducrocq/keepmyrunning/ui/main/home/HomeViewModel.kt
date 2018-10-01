package fr.tnducrocq.keepmyrunning.ui.main.home

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel;
import fr.tnducrocq.keepmyrunning.model.Running
import fr.tnducrocq.keepmyrunning.provider.Outcome
import fr.tnducrocq.keepmyrunning.provider.RunningProvider
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers

class HomeViewModel : ViewModel() {

    var runningList = MutableLiveData<Outcome<ArrayList<Running>>>()
    var deleteEvent = MutableLiveData<Outcome<Boolean>>()
    val list = ArrayList<Running>()

    private val disposables = CompositeDisposable()

    fun load(userId: String) {
        disposables.add(RunningProvider.fetchRunning(userId)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({
                    list.clear()
                    list.addAll(it)
                    runningList.value = Outcome.success(it)
                }, {
                    runningList.value = Outcome.failure(it)
                }))
    }

    fun delete(runningId: String) {
        disposables.add(RunningProvider.delete(runningId)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({
                    deleteEvent.value = Outcome.success(it)
                }, {
                    deleteEvent.value = Outcome.failure(it)
                }))
    }

    override fun onCleared() {
        super.onCleared()
        disposables.clear()
    }

}
