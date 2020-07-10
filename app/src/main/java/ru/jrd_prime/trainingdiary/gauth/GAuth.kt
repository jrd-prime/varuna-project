package ru.jrd_prime.trainingdiary.gauth

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.util.Log
import androidx.fragment.app.FragmentActivity
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.tasks.Task
import ru.jrd_prime.trainingdiary.TrainingDiaryApp
import ru.jrd_prime.trainingdiary.impl.AppContainer
import ru.jrd_prime.trainingdiary.ui.DashboardActivity
import ru.jrd_prime.trainingdiary.utils.AppUtils
import ru.jrd_prime.trainingdiary.utils.Toasts

class GAuth(private val appContext: TrainingDiaryApp) {
    val RC_SIGN_IN = 0

    val TAG: String = " / GAuth.class"
    private val appContainer: AppContainer = appContext.container
    private var context: Context = appContext.applicationContext
    private var googleSignInClient: GoogleSignInClient = getGoogleSignInClient()
    private var utils: AppUtils = appContainer.appUtils
    private var toasts: Toasts = Toasts(context)

    fun getGoogleSignInClient(): GoogleSignInClient {


        Log.d(TAG, "getGoogleSignInClient: ")
        // Configure sign-in to request the user's ID, email address, and basic
        // profile. ID and basic profile are included in DEFAULT_SIGN_IN.
        val gso =
            GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build()
        return GoogleSignIn.getClient(context, gso)
    }

    //---------- LOG IN ----------//
    fun GSignIn(activity: FragmentActivity?) {
        Log.d(TAG, "GSignIn: ")
        Log.d(TAG, "GSignIn: ${activity}")
        val googleSignInIntent = googleSignInClient.signInIntent
        activity?.startActivityForResult(googleSignInIntent, RC_SIGN_IN)
    }

    fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        Log.d(TAG, "onActivityResult: ")
        if (requestCode == RC_SIGN_IN) {
            val task =
                GoogleSignIn.getSignedInAccountFromIntent(data)
            handleSignInResult(task)
        }
    }

    fun handleSignInResult(completedTask: Task<GoogleSignInAccount?>) {
        Log.d(TAG, "handleSignInResult: ")
        try {
            val userAccount =
                completedTask.getResult(ApiException::class.java)
            // Signed in successfully, show authenticated UI.
            utils.setConfigAfterSignIn(userAccount)
            //TODO AUTH
        } catch (e: ApiException) {
            //TODO ERROR ON AUTH
            toasts.errorOnGoogleSignIn(e.statusCode)
        }
    }

    //---------- LOG OUT ----------//
    fun GSignOut() {
        Log.d(TAG, "GSignOut: ")
        googleSignInClient.signOut().addOnCompleteListener {
            Log.d(TAG, "onComplete: ")
            utils.clearSettings()
            //TODO NOT AUTH
        }
    }

    fun getLastSignedInAccount(): GoogleSignInAccount? {
        return GoogleSignIn.getLastSignedInAccount(context)
    }
}