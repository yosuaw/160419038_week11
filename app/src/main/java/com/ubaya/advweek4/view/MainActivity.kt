package com.ubaya.advweek4.view

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.ubaya.advweek4.R
import com.ubaya.advweek4.util.createNotificationChannel
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.core.Observer
import io.reactivex.rxjava3.core.Scheduler
import io.reactivex.rxjava3.disposables.Disposable
import io.reactivex.rxjava3.schedulers.Schedulers
import java.util.concurrent.TimeUnit

class MainActivity : AppCompatActivity() {
    companion object {
        private var instance: MainActivity? = null

        fun showNotification(title: String, content: String, icon: Int) {
            val channelID = "${instance?.packageName}-${instance?.getString(R.string.app_name)}"

            instance?.let {
                val notificationBuilder = NotificationCompat.Builder(it.applicationContext, channelID).apply {
                    setSmallIcon(icon)
                    setContentTitle(title)
                    setContentText(content)
                    setStyle(NotificationCompat.BigTextStyle())
                    priority = NotificationCompat.PRIORITY_DEFAULT
                    setAutoCancel(true) // notifikasi akan dicancel jika user mengklik notif di panel
                }

                val notificationManager = NotificationManagerCompat.from(it.applicationContext)
                notificationManager.notify(1001, notificationBuilder.build())
            }
        }
    }
    init {
        instance = this
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        createNotificationChannel(this, NotificationManagerCompat.IMPORTANCE_DEFAULT, false,
        getString(R.string.app_name), "App notification channel")

        val observable = Observable.just("A stream of data", "Hello", "RxJava")
        val observer = object : Observer<String> {
            override fun onSubscribe(d: Disposable) {
                Log.d("rxjava", "Begin subscribe")
            }

            override fun onNext(t: String) {
                Log.d("rxjava", t)
            }

            override fun onError(e: Throwable) {
                Log.e("rxerror", e.message.toString())
            }

            override fun onComplete() {
                Log.i("rxjava", "complete")
            }
        }
        observable
            .subscribeOn(Schedulers.io()) // pada thread mana observable jalan
            .observeOn(AndroidSchedulers.mainThread()) // dimana si observer mau jalan
            .subscribe(observer) // yang mau subscribe

        Observable.just("Another stream of data", "Hello again", "RxKotlin")
            .subscribe(
                { Log.d("rxkotlin", it) },
                { Log.e("rxerror", it.message.toString()) },
                { Log.i("rxkotlin", "Finish!") }
            )

        Observable
            .timer(5, TimeUnit.SECONDS)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe {
                Log.d("delay", "I am delayed by 5 seconds")
            }
    }
}