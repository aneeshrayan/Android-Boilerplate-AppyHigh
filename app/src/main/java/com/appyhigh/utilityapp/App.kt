package com.appyhigh.utilityapp

import android.app.Application
import com.appyhigh.utilityapp.notifications.OneSignalNotifOpenHandler
import com.appyhigh.utilityapp.notifications.OneSignalNotificationReceivedHandler
import com.crashlytics.android.Crashlytics
import com.facebook.FacebookSdk
import com.facebook.ads.AudienceNetworkAds
import com.google.android.gms.ads.MobileAds
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.inappmessaging.FirebaseInAppMessaging
import com.google.firebase.messaging.FirebaseMessaging
import com.onesignal.OneSignal
import io.fabric.sdk.android.Fabric

class App : Application() {
    val TAG = "App"

    companion object {
        lateinit var firebaseAnalytics: FirebaseAnalytics
    }

    override fun onCreate() {
        super.onCreate()
        FacebookSdk.sdkInitialize(applicationContext)
        AudienceNetworkAds.initialize(this)
        AudienceNetworkAds.isInAdsProcess(this)
        FirebaseMessaging.getInstance().subscribeToTopic("ALLUSERS")
        FirebaseMessaging.getInstance().subscribeToTopic("UtilityApp")
        if (BuildConfig.DEBUG) {
            FirebaseMessaging.getInstance().subscribeToTopic("UtilityAppDebug")
        }
        /*FirebaseAnalytics*/
        firebaseAnalytics = FirebaseAnalytics.getInstance(this)
        /*mobile ads*/
        MobileAds.initialize(this, {})
        /*Crashlytics*/
        Fabric.with(this, Crashlytics())
        // OneSignal Initialization
        OneSignal.startInit(this)
            .setNotificationOpenedHandler(OneSignalNotifOpenHandler(applicationContext))
            .setNotificationReceivedHandler(OneSignalNotificationReceivedHandler(applicationContext))
            .inFocusDisplaying(OneSignal.OSInFocusDisplayOption.Notification)
            .unsubscribeWhenNotificationsAreDisabled(true)
            .init()
        /*FirebaseInAppMessaging*/
        FirebaseInAppMessaging.getInstance().setMessagesSuppressed(true)
    }
}