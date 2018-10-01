package fr.tnducrocq.keepmyrunning.ui.launcher

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.GoogleAuthProvider
import fr.tnducrocq.keepmyrunning.provider.Outcome
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import java.util.concurrent.TimeUnit

class LauncherViewModel : ViewModel() {

    val TAG = LauncherFragment::class.java.simpleName

    private var disposable = CompositeDisposable()
    val outcomeUser = MutableLiveData<Outcome<FirebaseUser>>()

    fun init(mAuth: FirebaseAuth) {
        val fetchUser = Observable.create<Outcome<FirebaseUser>> {
            if (mAuth.currentUser != null) {
                it.onNext(Outcome.success(mAuth.currentUser!!))
            } else {
                it.onNext(Outcome.failure(NullPointerException("User not connected")))
            }
            it.onComplete()
        }

        disposable.add(fetchUser
                .delay(2000, TimeUnit.MILLISECONDS)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe {
                    outcomeUser.value = it
                })
    }


    fun firebaseAuthWithGoogle(mAuth: FirebaseAuth, acct: GoogleSignInAccount) {
        Log.d(TAG, "firebaseAuthWithGoogle:" + acct.id!!)

        val credential = GoogleAuthProvider.getCredential(acct.idToken, null)
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener {
                    if (it.isSuccessful) {
                        outcomeUser.value = Outcome.success(mAuth.currentUser!!)
                    } else {
                        outcomeUser.value = Outcome.failure(it.exception!!)
                    }
                }
    }

    override fun onCleared() {
        super.onCleared()
        disposable.clear()
    }

}
