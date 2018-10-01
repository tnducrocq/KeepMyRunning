package fr.tnducrocq.keepmyrunning.ui.running

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import fr.tnducrocq.keepmyrunning.model.Running
import fr.tnducrocq.keepmyrunning.provider.Outcome
import fr.tnducrocq.keepmyrunning.provider.RunningProvider
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import java.util.*

class MoodViewModel : ViewModel() {

    private val disposables = CompositeDisposable()
    private lateinit var running: Running

    val mood = MutableLiveData<Running.Mood>()
    val date = MutableLiveData<Date>()
    val saveResult = MutableLiveData<Outcome<Running>>()

    fun init(running: Running) {
        this.running = running
    }

    fun save() {
        disposables.add(RunningProvider.add(running)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({
                    saveResult.value = Outcome.success(it)
                }, {
                    saveResult.value = Outcome.failure(it)
                }))
    }

    fun toogleMood(mood: Running.Mood) {
        if (this.mood.value == mood) {
            this.mood.value = null
        } else {
            this.mood.value = mood
            this.running.mood = mood
        }
    }

    fun setDate(date: Date) {
        this.date.value = date
        this.running.date = date
    }

    override fun onCleared() {
        super.onCleared()
        disposables.clear()
    }

}
