package fr.tnducrocq.keepmyrunning.model

import android.os.Parcel
import android.os.Parcelable
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.firebase.auth.FirebaseUser
import java.util.*

class User : Parcelable {

    var id: String? = null
    var familyName: String? = null
    var displayName: String? = null
    var mail: String? = null
    var photoUrl: String? = null
    var colorIndex: Int = 0
    var token: String? = null

    constructor(firebaseUser: FirebaseUser) {
        familyName = firebaseUser.displayName
        mail = firebaseUser.email
        displayName = firebaseUser.displayName
        if (firebaseUser.photoUrl != null) {
            photoUrl = "https://lh3.googleusercontent.com/" + firebaseUser.photoUrl!!.encodedPath!!
        }
    }

    constructor(account: GoogleSignInAccount) {
        familyName = account.familyName
        mail = account.email
        displayName = account.displayName
        account.photoUrl?.encodedPath?.let {
            photoUrl = "https://lh3.googleusercontent.com/${it}"
        }
    }

    constructor(map: Map<String, Any?>) {
        familyName = map["familyName"] as String?
        displayName = map["displayName"] as String?
        mail = map["mail"] as String?
        photoUrl = map["photoUrl"] as String?
        colorIndex = map["colorIndex"] as Int
        token = map["token"] as String?
    }

    fun toMap(): Map<String, Any?> {
        val user = HashMap<String, Any?>()
        user["familyName"] = familyName
        user["displayName"] = displayName
        user["mail"] = mail
        user["photoUrl"] = photoUrl
        user["colorIndex"] = colorIndex
        user["token"] = token
        return user
    }


    constructor(parcel: Parcel) {
        id = parcel.readString()
        familyName = parcel.readString()
        displayName = parcel.readString()
        mail = parcel.readString()
        photoUrl = parcel.readString()
        colorIndex = parcel.readInt()
        token = parcel.readString()
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(id)
        parcel.writeString(familyName)
        parcel.writeString(displayName)
        parcel.writeString(mail)
        parcel.writeString(photoUrl)
        parcel.writeInt(colorIndex)
        parcel.writeString(token)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<User> {
        override fun createFromParcel(parcel: Parcel): User {
            return User(parcel)
        }

        override fun newArray(size: Int): Array<User?> {
            return arrayOfNulls(size)
        }
    }


}
