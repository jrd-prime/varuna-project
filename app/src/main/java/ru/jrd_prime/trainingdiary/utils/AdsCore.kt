//package ru.jrd_prime.trainingdiary.utils
//
///**
// * Creates a request for a new native ad based on the boolean parameters and calls the
// * corresponding "populate" method when one is successfully returned.
// *
// */
//private fun refreshAd() {
//    refresh_button.isEnabled = false
//
//    val builder = AdLoader.Builder(this, ADMOB_AD_UNIT_ID)
//
//    builder.forUnifiedNativeAd { unifiedNativeAd ->
//        // OnUnifiedNativeAdLoadedListener implementation.
//        val adView = layoutInflater
//            .inflate(R.layout.ad_unified, null) as UnifiedNativeAdView
//        populateUnifiedNativeAdView(unifiedNativeAd, adView)
//        ad_frame.removeAllViews()
//        ad_frame.addView(adView)
//    }
//
//    val videoOptions = VideoOptions.Builder()
//        .setStartMuted(start_muted_checkbox.isChecked)
//        .build()
//
//    val adOptions = NativeAdOptions.Builder()
//        .setVideoOptions(videoOptions)
//        .build()
//
//    builder.withNativeAdOptions(adOptions)
//
//    val adLoader = builder.withAdListener(object : AdListener() {
//        override fun onAdFailedToLoad(errorCode: Int) {
//            refresh_button.isEnabled = true
//            Toast.makeText(this@MainActivity, "Failed to load native ad: " + errorCode, Toast.LENGTH_SHORT).show()
//        }
//    }).build()
//
//    adLoader.loadAd(AdRequest.Builder().build())
//
//    videostatus_text.text = ""
//}