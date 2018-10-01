package fr.tnducrocq.keepmyrunning.provider

import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.FirebaseFirestoreSettings
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.Source
import fr.tnducrocq.keepmyrunning.model.Running
import io.reactivex.Observable
import java.util.*


class RunningProvider {

    companion object {

        fun update(runningId: String, running: Running): Observable<Running> {
            val settings = FirebaseFirestoreSettings.Builder()
                    .setPersistenceEnabled(true)
                    .build()

            return Observable.create<Running> { subscriber ->
                val db = FirebaseFirestore.getInstance()
                db.firestoreSettings = settings

                db.collection("running")
                        .document(runningId)
                        .set(running.toMap()).addOnCompleteListener { task ->
                            if (task.isComplete) {
                                if (!task.isSuccessful) {
                                    subscriber.onError(task.exception!!)
                                    subscriber.onComplete()
                                }
                                subscriber.onNext(running)
                                subscriber.onComplete()
                            }
                        }
            }
        }

        fun delete(runningId: String): Observable<Boolean> {
            val settings = FirebaseFirestoreSettings.Builder()
                    .setPersistenceEnabled(true)
                    .build()

            return Observable.create<Boolean> { subscriber ->
                val db = FirebaseFirestore.getInstance()
                db.firestoreSettings = settings

                db.collection("running")
                        .document(runningId)
                        .delete()
                        .addOnCompleteListener { task ->
                            if (task.isComplete) {
                                if (!task.isSuccessful) {
                                    subscriber.onError(task.exception!!)
                                    subscriber.onComplete()
                                }
                                subscriber.onNext(true)
                                subscriber.onComplete()
                            }
                        }
            }
        }

        fun add(running: Running): Observable<Running> {
            val settings = FirebaseFirestoreSettings.Builder()
                    .setPersistenceEnabled(true)
                    .build()

            return Observable.create<Running> { subscriber ->
                val db = FirebaseFirestore.getInstance()
                db.firestoreSettings = settings

                db.collection("running")
                        .add(running.toMap())
                        .addOnCompleteListener { task ->
                            if (task.isComplete) {
                                if (!task.isSuccessful) {
                                    subscriber.onError(task.exception!!)
                                    subscriber.onComplete()
                                }
                                running.id = task.result.id
                                subscriber.onNext(running)
                                subscriber.onComplete()
                            }
                        }
            }
        }

        fun fetchRunning(userId: String, fromCache: Boolean = false): Observable<ArrayList<Running>> {
            val settings = FirebaseFirestoreSettings.Builder()
                    .setPersistenceEnabled(true)
                    .build()

            return Observable.create<ArrayList<Running>> { subscriber ->
                val db = FirebaseFirestore.getInstance()
                db.firestoreSettings = settings
                val source = if (fromCache) Source.CACHE else Source.DEFAULT
                val collection = db.collection("running")
                collection
                        .whereEqualTo("userId", userId)
                        .orderBy("date", Query.Direction.DESCENDING)
                        .get(source)
                        .addOnCompleteListener { task ->
                            if (!task.isComplete) {
                                return@addOnCompleteListener
                            }

                            if (!task.isSuccessful) {
                                subscriber.onError(task.exception!!)
                                subscriber.onComplete()
                                return@addOnCompleteListener
                            }

                            val runningList = ArrayList<Running>()
                            for (queryDocumentSnapshot in task.result) {
                                val map = queryDocumentSnapshot.data
                                val running = Running(map)
                                running.id = queryDocumentSnapshot.id
                                runningList.add(running)
                            }
                            subscriber.onNext(runningList)
                            subscriber.onComplete()
                        }
            }
        }

    }

}
