package ru.jrd_prime.trainingdiary.workers

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.AsyncTask
import android.util.Log
import java.io.IOException
import java.net.HttpURLConnection
import java.net.URL

class AsyncDownloadUserPhoto : AsyncTask<String?, Void?, Bitmap?>() {

    override fun doInBackground(vararg strings: String?): Bitmap? {
        Log.d(TAG, "AsyncDownloadUserPhoto")
        return try {
            val url = URL(strings[0])
            val connection =
                url.openConnection() as HttpURLConnection
            connection.doInput = true
            connection.connect()
            val input = connection.inputStream
            BitmapFactory.decodeStream(input)
        } catch (e: IOException) {
            Log.d(TAG, "doInBackground: $e")
            //TODO бработка ошибки на загрузке
            null
        }
    }

    companion object {
        private const val TAG = "AsyncDownloadUserPhoto"
    }

}