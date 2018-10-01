package fr.tnducrocq.keepmyrunning.provider

import android.content.Context
import android.text.TextUtils
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.FirebaseFirestore
import fr.tnducrocq.keepmyrunning.model.User
import io.reactivex.Observable
import java.util.*

class UserProvider {
    companion object {

        fun update(userId: String, user: User): Observable<User> {
            return Observable.create<User> { subscriber ->
                val db = FirebaseFirestore.getInstance()
                db.collection("users")
                        .document(userId)
                        .set(user.toMap()).addOnCompleteListener { task ->
                            if (task.isComplete) {
                                if (!task.isSuccessful) {
                                    subscriber.onError(task.exception!!)
                                    subscriber.onComplete()
                                }
                                subscriber.onNext(user)
                                subscriber.onComplete()
                            }
                        }
            }
        }

        fun connect(context: Context, firebaseUser: FirebaseUser): Observable<User> {
            return Observable.create<User> { subscriber ->
                val db = FirebaseFirestore.getInstance()
                val collection = db.collection("users")
                collection
                        .whereEqualTo("mail", firebaseUser.email)
                        .get()
                        .addOnCompleteListener { task ->
                            if (!task.isComplete) {
                                return@addOnCompleteListener
                            }

                            if (!task.isSuccessful) {
                                subscriber.onError(task.exception!!)
                                subscriber.onComplete()
                                return@addOnCompleteListener
                            }

                            val user: User
                            if (task.result.isEmpty) {
                                user = User(firebaseUser)
                                val insertTask = collection.add(user.toMap())
                                user.id = insertTask.result.id

                                UserUtils.saveCurrentUser(context, user)
                            } else {
                                val queryDocumentSnapshot = task.result.iterator().next()
                                val map = queryDocumentSnapshot.data
                                user = User(map)
                                user.id = queryDocumentSnapshot.id
                            }
                            subscriber.onNext(user)
                            subscriber.onComplete()

                        }
            }
        }

        fun fetchUsers(search: String): Observable<List<User>> {
            return Observable.create<List<User>> { subscriber ->
                val db = FirebaseFirestore.getInstance()
                val collection = db.collection("users")
                collection
                        .orderBy("family_name")
                        .get()
                        .addOnCompleteListener { task ->
                            if (!task.isComplete) {
                                return@addOnCompleteListener
                            }

                            if (!task.isSuccessful) {
                                subscriber.onError(task.exception!!)
                                subscriber.onComplete()
                                return@addOnCompleteListener
                            }

                            val users = ArrayList<User>()
                            for (queryDocumentSnapshot in task.result) {
                                val map = queryDocumentSnapshot.data
                                val user = User(map)

                                val matchDisplayName = user.displayName?.contains(search, true)
                                val matchFamilyName = user.familyName?.contains(search, true)
                                if (TextUtils.isEmpty(search)
                                        || (matchDisplayName != null && matchDisplayName)
                                        || (matchFamilyName != null && matchFamilyName)) {
                                    user.id = queryDocumentSnapshot.id
                                    users.add(user)
                                }
                            }
                            subscriber.onNext(users)
                            subscriber.onComplete()
                        }
            }
        }
        
    }
}
