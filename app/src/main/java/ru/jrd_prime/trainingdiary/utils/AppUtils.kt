package ru.jrd_prime.trainingdiary.utils

import android.app.Activity
import android.content.ContentResolver
import android.content.Context
import android.content.ContextWrapper
import android.content.SharedPreferences
import android.content.res.Resources.NotFoundException
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.util.Log
import androidx.annotation.AnyRes
import androidx.core.graphics.drawable.RoundedBitmapDrawable
import androidx.core.graphics.drawable.RoundedBitmapDrawableFactory
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import org.threeten.bp.LocalDateTime
import org.threeten.bp.format.DateTimeFormatter
import ru.jrd_prime.trainingdiary.R
import ru.jrd_prime.trainingdiary.TrainingDiaryApp
import ru.jrd_prime.trainingdiary.utils.cfg.AppConfig
import ru.jrd_prime.trainingdiary.workers.AsyncDownloadUserPhoto
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.io.OutputStream
import java.util.concurrent.ExecutionException
import kotlin.system.exitProcess

class AppUtils(
    private val appContext: TrainingDiaryApp
) {
    private val appCont = appContext.container
    private val pref = appCont.preferences
    private val config = appCont.appConfig
    private val ctx = appContext.applicationContext

    companion object {
        const val SHOW_MENU = "ShowMenu"
    }

    /*NEW*/

    fun setPrefIsUserAuth(auth: Boolean) {
        pref.edit().putBoolean(config.getPrefIsUserAuth(), auth).apply()
    }
    /*NEW*/


    fun getMonth(): MutableList<String> {
        val now = LocalDateTime.now()
        val fmt = DateTimeFormatter.ofPattern("MM")
        return mutableListOf<String>(
            fmt.format(now.minusMonths(1)),
            fmt.format(now),
            fmt.format(now.plusMonths(1))
        )
    }


    fun setDefaultConfig(mSettings: SharedPreferences, cfg: AppConfig) {
        mSettings.edit().putBoolean(cfg.getPrefNameFirstRun(), false).apply()
    }

    // SETTINGS AFTER SIGN IN
    fun setConfigAfterSignIn(userAccount: GoogleSignInAccount?) {
        setUserAuth(true)
        setUserAvatar(userAccount)
        setSettings(userAccount)
        setShowMenu(true)
    }

    fun getUserAvatar(): RoundedBitmapDrawable? {
        return if (pref.getString(
                config.getSpNameUserPhotoOnDevice(),
                "error"
            ) == "error"
        ) {
            // NO PHOTO ON DEV
            Log.d(
                TAG,
                "getUserAvatar:// NO PHOTO ON DEV "
            )
            val bitmap =
                BitmapFactory.decodeResource(ctx.resources, R.drawable.user)
            val path =
                Uri.parse("android.resource://ru.jrd_prime.go_calories/" + R.drawable.user)
            //img2.setCornerRadius(120);
            RoundedBitmapDrawableFactory.create(
                ctx.resources,
                (R.drawable.user).toString()
            )
        } else {
            // HAS PHOTO ON DEV
            //img2.setCornerRadius(120);
            RoundedBitmapDrawableFactory.create(
                ctx.resources,
                pref.getString(
                    config.getSpNameUserPhotoOnDevice(),
                    getUriToResource(ctx, R.drawable.user)
                        .toString()
                ).toString()
            )
        }
    }

    private fun setSettings(userAccount: GoogleSignInAccount?) {
        // Пишем данные пользователя в префы
        Log.d(TAG, "setSettings:")
        pref.edit()
            .putString(config.getPrefUserName(), userAccount?.displayName)
            .putString(config.getSpNameUserID(), userAccount?.id)
            .putString(config.getPrefUserMail(), userAccount?.email)
            .putBoolean(config.getPrefIsUserAuth(), true)
            .apply()
    }

    private fun setUserAvatar(userAccount: GoogleSignInAccount?) {
        if (userAccount?.photoUrl == null) {
            // НЕТУ - Записать в превы ЕРРОР
            Log.d(TAG, "URI NULL")
            pref.edit()
                .putString(config.getSpNameUserPhotoOnDevice(), "error").apply()
        } else {
            // Получить аватар
            // обработать
            // сохранить
            // ЕСТЬ - Записать аватар в префы
            Log.d(TAG, "URI NOT NULL")
            val asyncDownloadUserPhoto = AsyncDownloadUserPhoto()
            var bitmap: Bitmap? = null
            try {
                bitmap = asyncDownloadUserPhoto.execute(userAccount.photoUrl.toString()).get()
                Log.d(TAG, "BITMAP RECIEVED")
                bitmap = Bitmap.createScaledBitmap(bitmap!!, 120, 120, false)
                Log.d(TAG, "BITMAP RESIZE")
            } catch (e: NullPointerException) {
                Log.d(
                    TAG,
                    "getSavedPhotoURI: BITMAP NULL $e"
                )
            } catch (e: ExecutionException) {
                Log.d(TAG, "ERROR ON EXEC ASYC")
                //                e.printStackTrace();
            } catch (e: InterruptedException) {
                Log.d(TAG, "ERROR ON EXEC ASYC")
            }
            val img2 =
                RoundedBitmapDrawableFactory.create(ctx.getResources(), bitmap)
            img2.cornerRadius = 120f
            bitmap = (img2 as RoundedBitmapDrawable).bitmap
            val wrapper = ContextWrapper(ctx)
            var file = wrapper.getDir("Images", Context.MODE_PRIVATE)
            file = File(file, config.getSpNameUserPhotoOnDevice().toString() + ".jpg")
            try {
                var stream: OutputStream? = null
                stream = FileOutputStream(file)
                bitmap!!.compress(Bitmap.CompressFormat.JPEG, 100, stream)
                stream.flush()
                stream.close()
            } catch (e: IOException) {
                Log.d(TAG, "ERROR ON SAVE BITMAP")
                //                e.printStackTrace();
            } catch (e: ClassCastException) {
                Log.d(TAG, "ERROR ON cast class")
            }
            Log.d(
                TAG,
                "LoadAndSaveAvatar: " + getUriToResource(
                    ctx,
                    R.drawable.user
                )
            )
            Log.d(
                TAG,
                "PHOTO SAVED: " + Uri.parse(file.absolutePath)
            )
            // Parse the gallery image url to uri
            pref.edit().putString(
                config.getSpNameUserPhotoOnDevice(),
                Uri.parse(file.absolutePath).toString()
            ).apply()
        }
    }

    @Throws(NotFoundException::class)
    fun getUriToResource(
        context: Context,
        @AnyRes resId: Int
    ): Uri {
        /** Return a Resources instance for your application's package.  */
        val res = context.resources

        /** return uri  */
        return Uri.parse(
            ContentResolver.SCHEME_ANDROID_RESOURCE +
                    "://" + res.getResourcePackageName(resId)
                    + '/' + res.getResourceTypeName(resId)
                    + '/' + res.getResourceEntryName(resId)
        )
    }

    fun clearSettings() {
        Log.d(TAG, "Setting CLEARED!")
        pref.edit().clear().apply()
    }

    private fun setUserAuth(b: Boolean) {
        pref.edit()
            .putBoolean(config.getPrefIsUserAuth(), b).apply()
        Log.d(
            TAG,
            "setUserAuth: need true : " + pref.getBoolean(
                config.getPrefIsUserAuth(),
                false
            )
        )
    }

    fun getUserAuth(): Boolean {
        return pref.getBoolean(config.getPrefIsUserAuth(), false)
    }

    // SHOW MENU
    fun setShowMenu(b: Boolean) {
        pref.edit().putBoolean(SHOW_MENU, b).apply()
    }

    fun getShowMenu(): Boolean {
        return pref.getBoolean(SHOW_MENU, false)
    }

    fun setDefaultConfig() {
        pref.edit().putBoolean(config.getPrefNameFirstRun(), false).apply()
    }

    fun closeApp(act: Activity) {
        act.finishAffinity()
        exitProcess(0)

    }
}