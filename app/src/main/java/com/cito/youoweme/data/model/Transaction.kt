package com.cito.youoweme.data.model

import android.os.Parcel
import android.os.Parcelable
import com.cito.youoweme.data.YomData
import java.text.SimpleDateFormat
import java.util.*
import kotlin.math.round

data class Transaction(
    override var id: Long? = null,
    var amount: Float,
    var contactId: Long? = null,
    var timeInMillis: Long = Calendar.getInstance().timeInMillis,
    var title: String? = null,
    var desc: String? = null,
) : YomData, Parcelable {

    val formattedDate
        get() = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(Date(timeInMillis))

    private constructor(parcel: Parcel) : this(
        parcel.readValue(Long::class.java.classLoader) as? Long,
        parcel.readFloat(),
        parcel.readValue(Long::class.java.classLoader) as? Long,
        parcel.readLong(),
        parcel.readString(),
        parcel.readString(),
    )

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeValue(id)
        parcel.writeFloat(amount)
        parcel.writeValue(contactId)
        parcel.writeLong(timeInMillis)
        parcel.writeString(title)
        parcel.writeString(desc)
    }

    override fun describeContents(): Int = 0

    companion object CREATOR : Parcelable.Creator<Transaction> {
        override fun createFromParcel(parcel: Parcel) = Transaction(parcel)
        override fun newArray(size: Int): Array<Transaction?> = arrayOfNulls(size)
    }

}

fun Float.euros(): Float = round(this*100) / 100f