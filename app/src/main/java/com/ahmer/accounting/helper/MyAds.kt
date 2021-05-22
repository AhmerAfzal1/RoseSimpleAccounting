package com.ahmer.accounting.helper

import android.app.Activity
import android.util.Log
import com.ahmer.accounting.R
import com.google.android.gms.ads.*
import com.google.android.gms.ads.interstitial.InterstitialAd
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback

class MyAds {

    companion object {

        fun loadBannerAd(activity: Activity, adView: AdView) {
            MobileAds.initialize(activity) {
                Log.v(Constants.LOG_TAG, "AdMob Sdk Initialize")
            }
            adView.adListener = object : AdListener() {
                override fun onAdClosed() {
                    super.onAdClosed()
                    Log.v(Constants.LOG_TAG, "Banner: Ad closed")
                }

                override fun onAdFailedToLoad(adError: LoadAdError) {
                    super.onAdFailedToLoad(adError)
                    Log.v(
                        Constants.LOG_TAG,
                        "Banner: Failed to load ad due to ${adError.message}, and error code is: ${adError.code}"
                    )
                }

                override fun onAdOpened() {
                    super.onAdOpened()
                    Log.v(Constants.LOG_TAG, "Banner: Ad opened and displayed")
                }

                override fun onAdLoaded() {
                    super.onAdLoaded()
                    Log.v(Constants.LOG_TAG, "Banner: Ad received and trying to load")
                }

                override fun onAdClicked() {
                    super.onAdClicked()
                    Log.v(Constants.LOG_TAG, "Banner: User clicked on ad")
                }

                override fun onAdImpression() {
                    super.onAdImpression()
                    Log.v(Constants.LOG_TAG, "Banner: Ad impression is recorded")
                }
            }
        }

        fun loadInterstitialAd(activity: Activity, turnOffAds: Boolean = false) {
            if (!turnOffAds) {
                MobileAds.initialize(activity) {
                    Log.v(Constants.LOG_TAG, "AdMob Sdk Initialize")
                }
                val mAdRequest = AdRequest.Builder().build()
                InterstitialAd.load(
                    activity,
                    activity.getString(R.string.ad_interstitial_id),
                    mAdRequest,
                    object : InterstitialAdLoadCallback() {
                        override fun onAdLoaded(interstitialAd: InterstitialAd) {
                            super.onAdLoaded(interstitialAd)
                            var mInterstitialAd: InterstitialAd?
                            mInterstitialAd = interstitialAd
                            Log.v(Constants.LOG_TAG, "Interstitial: Ad received and trying to load")
                            mInterstitialAd.fullScreenContentCallback =
                                object : FullScreenContentCallback() {
                                    override fun onAdFailedToShowFullScreenContent(adError: AdError) {
                                        super.onAdFailedToShowFullScreenContent(adError)
                                        Log.v(
                                            Constants.LOG_TAG,
                                            "Interstitial: Failed to show fullscreen content ad due to ${adError.message}, and error code is: ${adError.code}"
                                        )
                                    }

                                    override fun onAdShowedFullScreenContent() {
                                        super.onAdShowedFullScreenContent()
                                        Log.v(
                                            Constants.LOG_TAG,
                                            "Interstitial: Ad showed fullscreen content"
                                        )
                                    }

                                    override fun onAdDismissedFullScreenContent() {
                                        super.onAdDismissedFullScreenContent()
                                        mInterstitialAd = null
                                        Log.v(Constants.LOG_TAG, "Interstitial: Ad was dismissed")
                                    }

                                    override fun onAdImpression() {
                                        super.onAdImpression()
                                        Log.v(
                                            Constants.LOG_TAG,
                                            "Interstitial: Ad impression is recorded"
                                        )
                                    }
                                }
                            mInterstitialAd!!.show(activity)
                        }

                        override fun onAdFailedToLoad(adError: LoadAdError) {
                            super.onAdFailedToLoad(adError)
                            Log.v(
                                Constants.LOG_TAG,
                                "Interstitial: Failed to load ad due to ${adError.message}, and error code is: ${adError.code}"
                            )
                        }
                    }
                )
            }
        }
    }
}