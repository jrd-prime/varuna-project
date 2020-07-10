package ru.jrd_prime.trainingdiary.workers

import android.os.AsyncTask
import android.util.Log
import java.io.BufferedReader
import java.io.DataOutputStream
import java.io.IOException
import java.io.InputStreamReader
import java.net.HttpURLConnection
import java.net.URL
import java.nio.charset.StandardCharsets
import java.util.*

class HttpsRequestTask :
    AsyncTask<String?, Void?, String?>() {
    override fun doInBackground(vararg params: String?): String? {
        var result: String? = null
        var conn: HttpURLConnection? = null
        val core: Map<String, String>? = null
        val url: URL
        val requestURL = params[0]
        val json = params[1]
        Log.d("TEST_TAG", "doInBackground: " + Arrays.toString(params))
        val postData = json?.toByteArray(StandardCharsets.UTF_8)
        Log.d("TEST_TAG", "doInBackground: $json")
        val postDataLength = postData?.size
        try {
            url = URL(requestURL)
            conn = url.openConnection() as HttpURLConnection
            conn.doOutput = true
            conn.doInput = true
            conn.instanceFollowRedirects = false
            conn.requestMethod = "POST"
            conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded")
            conn.setRequestProperty("charset", "utf-8")
            conn.setRequestProperty("Content-Length", postDataLength?.let { Integer.toString(it) })
            conn.useCaches = false
        } catch (e: IOException) {
            e.printStackTrace()
        }
        try {
            DataOutputStream(
                Objects.requireNonNull(
                    conn
                )!!.outputStream
            ).use { wr -> wr.write(postData) }
        } catch (e: IOException) {
            e.printStackTrace()
        }
        var strmReader: InputStreamReader? = null
        try {
            strmReader = InputStreamReader(
                Objects.requireNonNull(conn)?.inputStream
            )
            //Create a new buffered reader and String Builder
            val reader = BufferedReader(strmReader)
            val strBuilder = StringBuilder()
            //Check if the line we are reading is not null
            var inputLine: String? = null
            while (true) {
                inputLine = reader.readLine()
                if (inputLine == null) break
                strBuilder.append(inputLine)
            }
            reader.close()
            strmReader.close()
            //Set our result equal to our stringBuilder
            result = strBuilder.toString()
            Log.d("TEST_TAG", "doInBackground: $result")
        } catch (e: IOException) {
            e.printStackTrace()
        }
        // return tores;
        return result
    }

    override fun onPostExecute(result: String?) {
        super.onPostExecute(result)
    }

    internal inner class HttpGetRequest :
        AsyncTask<String?, Void?, String?>() {
        override fun doInBackground(vararg params: String?): String? {
            val stringUrl = params[0]
            var result: String?
            var inputLine: String?
            try {
                //Create a URL object holding our url
                val myUrl = URL(stringUrl)
                //Create a connection
                val conn =
                    myUrl.openConnection() as HttpURLConnection
                //Set methods and timeouts
                conn.requestMethod = Companion.REQUEST_METHOD
                conn.readTimeout = Companion.READ_TIMEOUT
                conn.connectTimeout = Companion.CONNECTION_TIMEOUT

                //Connect to our url
                conn.connect()
                //Create a new InputStreamReader
                val strmReader =
                    InputStreamReader(conn.inputStream)
                //Create a new buffered reader and String Builder
                val reader = BufferedReader(strmReader)
                val strBuilder = StringBuilder()
                //Check if the line we are reading is not null
                while (reader.readLine().also { inputLine = it } != null) {
                    strBuilder.append(inputLine)
                }
                //Close our InputStream and Buffered reader
                reader.close()
                strmReader.close()
                //Set our result equal to our stringBuilder
                result = strBuilder.toString()
            } catch (e: IOException) {
                e.printStackTrace()
                result = null
            }
            return result
        }

        override fun onPostExecute(result: String?) {
            super.onPostExecute(result)
        }
    }

    companion object {
        const val REQUEST_METHOD = "POST"
        const val READ_TIMEOUT = 1000
        const val CONNECTION_TIMEOUT = 1000
    }
}