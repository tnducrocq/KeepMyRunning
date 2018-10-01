package fr.tnducrocq.keepmyrunning.model

import android.os.Parcel
import android.os.Parcelable
import java.util.*

class Running : Parcelable {

    enum class Mood {
        Good,
        Just_fine,
        Sad
    }

    var id: String? = null
    var userId: String
    var date: Date
    var time: Long = 0
    var distance: Long = 0
    var calorie: Int? = null
    var mood: Mood = Mood.Just_fine

    constructor(userId: String) {
        this.userId = userId
        this.date = Date()
    }

    constructor(map: Map<String, Any?>) {
        userId = map["userId"] as String
        date = Date(map["date"] as Long)
        calorie = (map["calorie"] as Long?)?.toInt()
        time = map["time"] as Long
        distance = map["distance"] as Long
        mood = Mood.values()[(map["mood"] as Long).toInt()]
    }

    fun toMap(): Map<String, Any?> {
        val obj = HashMap<String, Any?>()
        obj["userId"] = userId
        obj["date"] = date.time
        obj["calorie"] = calorie
        obj["time"] = time
        obj["distance"] = distance
        obj["mood"] = mood.ordinal
        return obj
    }

    constructor(parcel: Parcel) {
        id = parcel.readString()
        userId = parcel.readString()
        date = Date(parcel.readLong())
        calorie = parcel.readInt()
        time = parcel.readLong()
        distance = parcel.readLong()
        mood = Mood.values()[parcel.readInt()]
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(id)
        parcel.writeString(userId)
        parcel.writeLong(date.time)
        parcel.writeInt(if (calorie == null) 0 else calorie!!)
        parcel.writeLong(time)
        parcel.writeLong(distance)
        parcel.writeInt(mood.ordinal)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Running> {
        override fun createFromParcel(parcel: Parcel): Running {
            return Running(parcel)
        }

        override fun newArray(size: Int): Array<Running?> {
            return arrayOfNulls(size)
        }
    }

}
