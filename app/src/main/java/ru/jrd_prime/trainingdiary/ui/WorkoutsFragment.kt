package ru.jrd_prime.trainingdiary.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.core.widget.NestedScrollView
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.viewpager.widget.ViewPager
import com.google.android.gms.ads.*
import com.google.android.gms.ads.formats.MediaView
import com.google.android.gms.ads.formats.NativeAdOptions
import com.google.android.gms.ads.formats.UnifiedNativeAd
import com.google.android.gms.ads.formats.UnifiedNativeAdView
import kotlinx.android.synthetic.main.a_need_auth_page.view.*
import kotlinx.android.synthetic.main.a_workout_list_pager.view.*
import kotlinx.android.synthetic.main.a_workout_list_pager_new.*
import kotlinx.android.synthetic.main.ad_uni1.view.*
import ru.jrd_prime.trainingdiary.R
import ru.jrd_prime.trainingdiary.TrainingDiaryApp
import ru.jrd_prime.trainingdiary.adapter.WorkoutListAdapter
import ru.jrd_prime.trainingdiary.databinding.ANeedAuthPageBinding
import ru.jrd_prime.trainingdiary.databinding.AWorkoutListPagerNewBinding
import ru.jrd_prime.trainingdiary.fb_core.FireBaseCore
import ru.jrd_prime.trainingdiary.impl.AppContainer
import ru.jrd_prime.trainingdiary.utils.getDatesWeekList
import ru.jrd_prime.trainingdiary.utils.getStartDateForPosition
import ru.jrd_prime.trainingdiary.utils.getWeekFromDate
import java.util.*

const val ARGUMENT_PAGE_NUMBER = "arg_page_number"
const val ARGUMENT_USER_AUTH = "arg_user_auth"
const val ARGUMENT_USER_PREMIUM = "arg_user_premium"

const val ADMOB_AD_UNIT_ID = "ca-app-pub-3940256099942544/2247696110"
var currentNativeAd: UnifiedNativeAd? = null
class WorkoutPageFragment : Fragment() {
    private var pageNumber = 0
    private val appCont: AppContainer by lazy {
        (activity?.application as TrainingDiaryApp).container
    }

    /*
    Метод newInstance создает новый экземпляр фрагмента и записывает ему в атрибуты число,
    которое пришло на вход. Это число – номер страницы, которую хочет показать ViewPager.
    По нему фрагмент будет определять, какое содержимое создавать в фрагменте. */
    companion object {
        const val TAG = "WorkoutsFragment: drops"
        fun newInstance(page: Int): WorkoutPageFragment {
            val pageFragment = WorkoutPageFragment()
            val arguments = Bundle()
            arguments.putInt(ARGUMENT_PAGE_NUMBER, page)
            pageFragment.arguments = arguments
            return pageFragment
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        pageNumber = requireArguments().getInt(ARGUMENT_PAGE_NUMBER)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val fbc = FireBaseCore(appCont)
        if (!appCont.preferences.getBoolean(appCont.appConfig.getPrefIsUserAuth(), false)) {
            /* USER NOT AUTH*/
            val binding: ANeedAuthPageBinding = DataBindingUtil.inflate(
                LayoutInflater.from(activity),
                R.layout.a_need_auth_page,
                container,
                false
            )
            val root = binding.root
            root.btnSignInOnMain.setOnClickListener {
                appCont.gAuth.gSignIn(activity) /*SIGN IN*/
            }
            return root
        } else {
            val binding: AWorkoutListPagerNewBinding = DataBindingUtil.inflate(
                LayoutInflater.from(activity),
                R.layout.a_workout_list_pager_new,
                container,
                false
            )
            val root = binding.root
            val workoutsListAdapter = WorkoutListAdapter()
            workoutsListAdapter.notifyDataSetChanged()
            root.recView.adapter = workoutsListAdapter

//            MobileAds.initialize(context)
//            val mAdView = binding.adView
//            val adRequest = AdRequest.Builder().addTestDevice(AdRequest.DEVICE_ID_EMULATOR).build()
//            mAdView.loadAd(adRequest)


            // Initialize the Mobile Ads SDK.
            MobileAds.initialize(context) {}

            refreshAd()


            val t = activity?.findViewById<TextView>(R.id.tvTodayDay)
            val workoutPager = activity?.findViewById<ViewPager>(R.id.viewPagerMainDashboard)
            t?.setOnClickListener {
                workoutPager?.setCurrentItem(START_PAGE, true)
            }


            val date = getWeekFromDate(getStartDateForPosition(pageNumber))
            val dates = getDatesWeekList(startDate = date[0])

            fbc.getWeekData(dates, workoutsListAdapter)
            fbc.listenNewData(workoutsListAdapter)


            root.recView.layoutManager = LinearLayoutManager(context)
            val scrollView = root.cont_layz as NestedScrollView
            scrollView.isFillViewport = true
            return root
        }
    }

    /**
     * Populates a [UnifiedNativeAdView] object with data from a given
     * [UnifiedNativeAd].
     *
     * @param nativeAd the object containing the ad's assets
     * @param adView the view to be populated
     */
    private fun populateUnifiedNativeAdView(nativeAd: UnifiedNativeAd, adView: UnifiedNativeAdView) {
        // You must call destroy on old ads when you are done with them,
        // otherwise you will have a memory leak.
        currentNativeAd?.destroy()
        currentNativeAd = nativeAd

        // Set other ad assets.
        adView.headlineView = adView.ad_headline
        adView.bodyView = adView.ad_body
        adView.callToActionView = adView.ad_call_to_action
        adView.iconView = adView.ad_app_icon
        adView.priceView = adView.ad_price
        adView.starRatingView = adView.ad_stars
        adView.storeView = adView.ad_store
        adView.advertiserView = adView.ad_advertiser

        // The headline and media content are guaranteed to be in every UnifiedNativeAd.
        (adView.headlineView as TextView).text = nativeAd.headline

        // These assets aren't guaranteed to be in every UnifiedNativeAd, so it's important to
        // check before trying to display them.
        if (nativeAd.body == null) {
            adView.bodyView.visibility = View.INVISIBLE
        } else {
            adView.bodyView.visibility = View.VISIBLE
            (adView.bodyView as TextView).text = nativeAd.body
        }

        if (nativeAd.callToAction == null) {
            adView.callToActionView.visibility = View.INVISIBLE
        } else {
            adView.callToActionView.visibility = View.VISIBLE
            (adView.callToActionView as Button).text = nativeAd.callToAction
        }

        if (nativeAd.icon == null) {
            adView.iconView.visibility = View.GONE
        } else {
            (adView.iconView as ImageView).setImageDrawable(
                nativeAd.icon.drawable)
            adView.iconView.visibility = View.VISIBLE
        }

        if (nativeAd.price == null) {
            adView.priceView.visibility = View.INVISIBLE
        } else {
            adView.priceView.visibility = View.VISIBLE
            (adView.priceView as TextView).text = nativeAd.price
        }

        if (nativeAd.store == null) {
            adView.storeView.visibility = View.INVISIBLE
        } else {
            adView.storeView.visibility = View.VISIBLE
            (adView.storeView as TextView).text = nativeAd.store
        }

        if (nativeAd.starRating == null) {
            adView.starRatingView.visibility = View.INVISIBLE
        } else {
            (adView.starRatingView as RatingBar).rating = nativeAd.starRating!!.toFloat()
            adView.starRatingView.visibility = View.VISIBLE
        }

        if (nativeAd.advertiser == null) {
            adView.advertiserView.visibility = View.INVISIBLE
        } else {
            (adView.advertiserView as TextView).text = nativeAd.advertiser
            adView.advertiserView.visibility = View.VISIBLE
        }

        // This method tells the Google Mobile Ads SDK that you have finished populating your
        // native ad view with this native ad.
        adView.setNativeAd(nativeAd)

//        // Get the video controller for the ad. One will always be provided, even if the ad doesn't
//        // have a video asset.
//        val vc = nativeAd.videoController

//        // Updates the UI to say whether or not this ad has a video asset.
//        if (vc.hasVideoContent()) {
//            videostatus_text.text = String.format(
//                Locale.getDefault(),
//                "Video status: Ad contains a %.2f:1 video asset.",
//                vc.aspectRatio)
//
//            // Create a new VideoLifecycleCallbacks object and pass it to the VideoController. The
//            // VideoController will call methods on this object when events occur in the video
//            // lifecycle.
//            vc.videoLifecycleCallbacks = object : VideoController.VideoLifecycleCallbacks() {
//                override fun onVideoEnd() {
//                    // Publishers should allow native ads to complete video playback before
//                    // refreshing or replacing them with another ad in the same UI location.
//                    refresh_button.isEnabled = true
//                    videostatus_text.text = "Video status: Video playback has ended."
//                    super.onVideoEnd()
//                }
//            }
//        } else {
//            videostatus_text.text = "Video status: Ad does not contain a video asset."
//            refresh_button.isEnabled = true
//        }
    }
    /**
     * Creates a request for a new native ad based on the boolean parameters and calls the
     * corresponding "populate" method when one is successfully returned.
     *
     */
    private fun refreshAd() {

        val builder = AdLoader.Builder(context, ADMOB_AD_UNIT_ID)

        builder.forUnifiedNativeAd { unifiedNativeAd ->
            // Assumes that your ad layout is in a file call ad_unified.xml
            // in the res/layout folder
            val adView = layoutInflater
                .inflate(R.layout.ad_uni1, null) as UnifiedNativeAdView
            // This method sets the text, images and the native ad, etc into the ad
            // view.
            populateUnifiedNativeAdView(unifiedNativeAd, adView)

            // Assumes you have a placeholder FrameLayout in your View layout
            // (with id ad_frame) where the ad is to be placed.
            ad_frame.removeAllViews()
            ad_frame.addView(adView)
        }



        val adLoader = builder.withAdListener(object : AdListener() {
            override fun onAdFailedToLoad(errorCode: Int) {
                Toast.makeText(context, "Failed to load native ad: " + errorCode, Toast.LENGTH_SHORT).show()
            }
        }).build()

        adLoader.loadAd(AdRequest.Builder().build())

    }
    override fun onDestroy() {
        currentNativeAd?.destroy()
        super.onDestroy()
    }
}