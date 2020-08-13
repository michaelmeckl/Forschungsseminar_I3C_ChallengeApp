package com.example.challengecovid

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.util.DisplayMetrics
import android.view.View
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.Single
import io.reactivex.rxjava3.schedulers.Schedulers
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
     * Checks if the device has a network connection.
     */
    fun isNetworkConnected(context: Context): Boolean {
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
        hasInternetConnection().subscribe { hasInternet ->
            println("Internet Access: $hasInternet")
            Toast.makeText(context, "Has internet connection: $hasInternet", Toast.LENGTH_SHORT).show()
        }
    }
    /*
    //TODO: use it like this for checking internet connection:
    context?.let {
        if (Utils.isNetworkConnected(it)) {
            // do stuff with internet
        } else {
            AlertDialog.Builder(it)
                .setTitle("No Internet Connection")
                .setMessage("Please check your internet connection and try again")
                .setIcon(android.R.drawable.ic_dialog_alert)
                .setPositiveButton(android.R.string.ok) { _, _ -> }
                .show()
        }
    }
    */

    /**
     * Utility-Method to create a viewmodel with custom params.
     * used like: by viewmodels (viewModelFactory { MyViewModel("parameter") } )
     */
    inline fun <VM : ViewModel> viewModelFactory(crossinline f: () -> VM) =
        object : ViewModelProvider.Factory {
            @Suppress("UNCHECKED_CAST")
            override fun <T : ViewModel> create(aClass: Class<T>):T = f() as T
        }

    /**
     * This slides a given view to the right.
     */
    fun AppCompatActivity.slideOutView(v: View) {
        val slideOutAnim: Animation = AnimationUtils.loadAnimation(this, R.anim.slide_to_right)
        v.startAnimation(slideOutAnim)

        // https://stackoverflow.com/questions/4728908/android-view-with-view-gone-still-receives-ontouch-and-onclick
        v.visibility = View.GONE
        v.clearAnimation()
    }
}


