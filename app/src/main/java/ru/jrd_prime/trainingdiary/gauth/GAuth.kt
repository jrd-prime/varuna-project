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
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import ru.jrd_prime.trainingdiary.TrainingDiaryApp
import ru.jrd_prime.trainingdiary.fb_core.FireBaseCore
import ru.jrd_prime.trainingdiary.impl.AppContainer
import ru.jrd_prime.trainingdiary.ui.DashboardActivity
import ru.jrd_prime.trainingdiary.utils.AppUtils
import ru.jrd_prime.trainingdiary.utils.Toasts

class GAuth(val appContext: TrainingDiaryApp) {
    companion object {
        const val RC_SIGN_IN = 0
        const val TAG: String = " / GAuth.class"
    }

    private val appContainer: AppContainer = appContext.container
    private var context: Context = appContext.applicationContext
    private var googleSignInClient: GoogleSignInClient = getGoogleSignInClient()
    private var utils: AppUtils = appContainer.appUtils
    private var toasts: Toasts = Toasts(context)
    private val fireAuth: FirebaseAuth = appContainer.fireAuth
    private val activity: Activity = DashboardActivity().activity
    private val fireBaseCore: FireBaseCore = FireBaseCore(appContainer)

    fun getGoogleSignInClient(): GoogleSignInClient {
        // Configure sign-in to request the user's ID, email address, and basic
        // profile. ID and basic profile are included in DEFAULT_SIGN_IN.
        val gso =
            GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken("495749066016-uebsricev7u4e67h237jco8tmhgam500.apps.googleusercontent.com")
                .requestEmail()
                .build()
        return GoogleSignIn.getClient(context, gso)
    }

    //---------- LOG IN ----------//
    fun gSignIn(activity: FragmentActivity?) {
        val googleSignInIntent = googleSignInClient.signInIntent
        activity?.startActivityForResult(googleSignInIntent, RC_SIGN_IN)
    }

    //---------- LOG OUT ----------//
    fun gSignOut() {
        Log.d(TAG, "GSignOut: ")
        googleSignInClient.signOut().addOnCompleteListener {
            Log.d(TAG, "onComplete: ")
            utils.clearSettings()
            //TODO NOT AUTH
        }
    }

    fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        Log.d(TAG, "onActivityResult: ")
        if (requestCode == RC_SIGN_IN) {
            val task = GoogleSignIn.getSignedInAccountFromIntent(data)

            handleSignInResult(task)
        }
    }

    private fun handleSignInResult(completedTask: Task<GoogleSignInAccount?>) {
        Log.d(TAG, "handleSignInResult: ")

        try {
            // Google Sign In was successful, authenticate with Firebase
            val account = completedTask.getResult(ApiException::class.java)!!
            utils.setConfigAfterSignIn(account)
           addUser(account)
            Log.d(TAG, "firebaseAuthWithGoogle:" + account.id)
            firebaseAuthWithGoogle(account.idToken!!)
        } catch (e: ApiException) {
            // Google Sign In failed, update UI appropriately
            Log.w(TAG, "Google sign in failed", e)
//            toasts.errorOnGoogleSignIn(e.statusCode)
            // ...
        }


//        try {
//            val userAccount =
//                completedTask.getResult(ApiException::class.java)
//            // Signed in successfully, show authenticated UI.
//            utils.setConfigAfterSignIn(userAccount)
////            if (userAccount != null) {
////                userAccount.id?.let {
////                    FireBaseCore(appContainer).addNewUserOnSignIn(
////                        it,
////                        userAccount.displayName,
////                        userAccount.email
////                    )
////                }
////            }
//            //TODO AUTH
//        } catch (e: ApiException) {
//            //TODO ERROR ON AUTH
//            toasts.errorOnGoogleSignIn(e.statusCode)
//        }
    }

    fun addUser(account: GoogleSignInAccount) {
        Log.d(ru.jrd_prime.trainingdiary.ui.TAG, "addUser: ")
        fireBaseCore.addNewUserOnSignIn(account.id, account.displayName, account.email)
    }

    fun firebaseAuthWithGoogle(idToken: String) {
        val credential = GoogleAuthProvider.getCredential(idToken, null)
        Log.d(ru.jrd_prime.trainingdiary.ui.TAG, "firebaseAuthWithGoogle------------: $credential")
        fireAuth.signInWithCredential(credential)
            .addOnCompleteListener(activity) { task ->
                if (task.isSuccessful) {
                    // Sign in success, update UI with the signed-in user's information
                    Log.d(GAuth.TAG, "signInWithCredential:success")
                    val user = fireAuth.currentUser
                } else {
                    // If sign in fails, display a message to the user.
                    Log.w(GAuth.TAG, "signInWithCredential:failure", task.exception)
                    // ...
                    Log.d("tag", "Authentication Failed.")
                }
            }
    }

    fun getLastSignedInAccount(): GoogleSignInAccount? {
        return GoogleSignIn.getLastSignedInAccount(context)
    }
}