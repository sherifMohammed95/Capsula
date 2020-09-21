package com.blueMarketing.capsula.utils

import android.content.Intent
import android.content.res.Resources
import androidx.databinding.Observable
import androidx.databinding.ObservableField
import io.reactivex.Flowable
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

/**
 * assume the integer is in dp and returns the equivalent pixels value
 * @return equivalent pixels value
 */
val Int.toPx: Int
    get() = (this * Resources.getSystem().displayMetrics.density).toInt()

/**
 * assume the integer is in pixels and returns the equivalent dp value
 * @return equivalent dp value
 */
val Int.toDp: Int
    get() = (this / Resources.getSystem().displayMetrics.density).toInt()

/**
 * makes the flowable subscribe on Schedulers.io() and observe on Android main thread
 * @author Amr Saber
 */
fun <T : Any> Flowable<T>.toLinkFlowable(): Flowable<T> {
    return this
        .subscribeOn(io.reactivex.schedulers.Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread())
}

/**
 * registers a call back to the observable field and returns the OnPropertyChangedCallback object in case you wanted to remove the callback later
 *
 * @author Amr Saber
 * @param action lambda function that takes the new value of the observable field as an argument, represents the callback that is to be done
 * @return the OnPropertyChangedCallback object of the call back
 */
fun <T : Any> ObservableField<T>.addCallback(
    action: (newValue: T?) -> Unit
): Observable.OnPropertyChangedCallback {
    val callback = object : Observable.OnPropertyChangedCallback() {
        override fun onPropertyChanged(sender: Observable?, propertyId: Int) {
            val newValue = this@addCallback.get()
            action(newValue)
        }
    }
    this.addOnPropertyChangedCallback(callback)
    return callback

}


fun <T> Call<T>.enqueue(callback: CallBackKt<T>.() -> Unit) {
    val callBackKt = CallBackKt<T>()
    callback.invoke(callBackKt)
    this.enqueue(callBackKt)
}

class CallBackKt<T> : Callback<T> {

    var onResponse: ((Response<T>) -> Unit)? = null
    var onFailure: ((t: Throwable?) -> Unit)? = null

    override fun onFailure(call: Call<T>, t: Throwable) {
        onFailure?.invoke(t)
    }

    override fun onResponse(call: Call<T>, response: Response<T>) {
        onResponse?.invoke(response)
    }

}

fun <T> Single<T>.with(): Single<T> =
    subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())


inline fun <reified T : Any> Intent.addExtra(key: String, value: T) {
    when (T::class) {
        Boolean::class -> putExtra(key, value as Boolean)
        Int::class -> putExtra(key, value as Int)
        Long::class -> putExtra(key, value as Long)
        Float::class -> putExtra(key, value as Float)
        String::class -> putExtra(key, value as String)
        else -> throw UnsupportedOperationException("not supported Intent type!")
    }
}


