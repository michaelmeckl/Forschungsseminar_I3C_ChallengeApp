package com.example.challengecovid

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.util.DisplayMetrics
import android.view.View
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.example.challengecovid.ui.SplashActivity
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.Single
import io.reactivex.rxjava3.schedulers.Schedulers
import timber.log.Timber
import java.io.IOException
import java.net.InetSocketAddress
import java.net.Socket

/**
 * Singleton with some Utility-Functions
 */
object Utils {

    private const val DEFAULT_COLUMN_WIDTH = 200F

    /**
     * Calculates the number of columns to use for a GridLayout.
     */
    fun calculateNumberOfColumns(
        context: Context,
        columnWidthDp: Float = DEFAULT_COLUMN_WIDTH
    ): Int {
        val displayMetrics: DisplayMetrics = context.resources.displayMetrics
        val screenWidthDp = displayMetrics.widthPixels / displayMetrics.density
        return (screenWidthDp / columnWidthDp + 0.5).toInt()
    }

    /**
     * Checks if this is the first time this app was started. This may happen either because of a (re-)install or because
     * the apps' data has been deleted on the device.
     */
    fun checkFirstRun(context: Context): Boolean {
        // Get current version code
        val currentVersionCode: Int = BuildConfig.VERSION_CODE

        // Get saved version code
        val prefs = context.getSharedPreferences(Constants.SHARED_PREFS_NAME, AppCompatActivity.MODE_PRIVATE)
        val savedVersionCode = prefs.getInt(SplashActivity.PREFS_VERSION_CODE_KEY, -1)

        var isFirstRun = false

        // Check for first run or upgrade
        when {
            currentVersionCode == savedVersionCode -> {
                // This is just a normal run, don't update the shared prefs
                isFirstRun = false
                return isFirstRun
            }
            savedVersionCode == -1 -> {
                // This is a new install (or the user cleared the shared preferences)
               isFirstRun = true
            }
            /*
            currentVersionCode > savedVersionCode -> {
                // This is an upgrade; show infos about what has changed since the last version
            }*/
        }

        // Update the shared preferences with the current version code
        prefs.edit().putInt(SplashActivity.PREFS_VERSION_CODE_KEY, currentVersionCode).apply()
        return isFirstRun
    }


    /**
     * Checks if the device has a network connection.
     */
    private fun isNetworkConnected(context: Context): Boolean {
        val connectivityManager =
            context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val activeNetwork = connectivityManager.activeNetwork
        val networkCapabilities = connectivityManager.getNetworkCapabilities(activeNetwork)
        return networkCapabilities != null && networkCapabilities.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)
    }

    /**
     * Util-Method that pings a Google Server to test if the internet connection works.
     */
    private fun hasInternetConnection(): Single<Boolean> {
        //see https://stackoverflow.com/questions/1560788/how-to-check-internet-access-on-android-inetaddress-never-times-out/27312494#27312494
        return Single.fromCallable {
            try {
                // Connect to Google DNS to check for connection
                val timeoutMs = 1500
                val socket = Socket()
                val socketAddress = InetSocketAddress("8.8.8.8", 53)

                socket.connect(socketAddress, timeoutMs)
                socket.close()

                true
            } catch (e: IOException) {
                false
            }
        }
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
    }

    /**
     *  Checks if the device has an internet connection.
     */
    fun checkInternet(context: Context) {
        if (!isNetworkConnected(context)) {
            // check if connected to a network first
            showConnectionAlert(context)
            /*
            Snackbar.make(
                findViewById(android.R.id.content),
                R.string.no_internet + R.string.no_internet_warning,
                Snackbar.LENGTH_LONG
            ).show()
            */
        } else {
            // if it is, additionally check if pinging the Google DNS works
            hasInternetConnection().subscribe { hasInternet ->
                Timber.d("Internet Access: $hasInternet")
                if(!hasInternet) showConnectionAlert(context)
            }
        }
    }

    // show alert dialog when no internet connection
    private fun showConnectionAlert(context: Context) {
        AlertDialog.Builder(context)
            .setTitle(R.string.no_internet)
            .setMessage(R.string.no_internet_warning)
            .setIcon(android.R.drawable.ic_dialog_alert)
            .setPositiveButton(android.R.string.ok) { _, _ -> checkInternet(context)}
            .setNegativeButton(android.R.string.cancel) { _, _ -> }
            .show()
    }

    /*
    fun fetchCurrentStatistics(country: String) {
        // deserialize objects with custom deserializer
        Fuel.get("https://disease.sh/v2/countries/${country}")
            .responseObject(CoronaStatistics.Deserializer()) { _, _, result ->
                when (result) {
                    is Result.Failure -> {
                        // Failed request
                        val ex = result.getException()
                        println(ex)
                    }
                    is Result.Success -> {
                        // Successful request
                        val data = result.get()
                    }
                }
            }
    }

     */

    /*
    fun makeRequest(): Triple<Response, String, Body> {

        val httpAsync = Fuel.get("https://disease.sh/v2/all")
            .responseJson { request, response, result ->
                when (result) {
                    is Result.Failure -> {
                        // Failed request
                        val ex = result.getException()
                        println(ex)
                    }
                    is Result.Success -> {
                        // Successful request
                        val data = result.get().content
                        val test = result.value.obj()
                        println(data)
                        println(test)
                    }
                }
            }

        //val response = httpAsync.awaitResponse(Response)
        val response = httpAsync.join()
        return Triple(response, response.responseMessage, response.body())
    }
    */
}

/**
 * This slides a given view to the right.
 */
fun Fragment.slideOutView(v: View) {
    val slideOutAnim: Animation = AnimationUtils.loadAnimation(requireActivity(), R.anim.slide_out_right)
    v.startAnimation(slideOutAnim)

    // https://stackoverflow.com/questions/4728908/android-view-with-view-gone-still-receives-ontouch-and-onclick
    v.visibility = View.GONE
    v.clearAnimation()
}


