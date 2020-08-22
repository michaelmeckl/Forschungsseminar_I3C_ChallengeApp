package com.example.challengecovid

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.util.DisplayMetrics
import android.view.View
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.os.bundleOf
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
fun AppCompatActivity.slideOutView(v: View) {
    val slideOutAnim: Animation = AnimationUtils.loadAnimation(this, R.anim.slide_out_right)
    v.startAnimation(slideOutAnim)

    // https://stackoverflow.com/questions/4728908/android-view-with-view-gone-still-receives-ontouch-and-onclick
    v.visibility = View.GONE
    v.clearAnimation()
}

/*
// see Google Sunflower
// TODO: Helper function for calling a share functionality.
// Should be used when user presses a share button/menu item.
@Suppress("DEPRECATION")
private fun createShareIntent() {
    val shareText = plantDetailViewModel.plant.value.let { plant ->
        if (plant == null) {
            ""
        } else {
            getString(R.string.share_text_plant, plant.name)
        }
    }
    val shareIntent = ShareCompat.IntentBuilder.from(requireActivity())
        .setText(shareText)
        .setType("text/plain")
        .createChooserIntent()
        .addFlags(Intent.FLAG_ACTIVITY_NEW_DOCUMENT or Intent.FLAG_ACTIVITY_MULTIPLE_TASK)
    startActivity(shareIntent)
}

 */

/*
fun Fragment.getViewModelFactory(): ViewModelFactory {
    val repository = (requireContext().applicationContext as ChallengeCovidApplication).challengeRepository
    return ViewModelFactory(repository, this)
}
*/

// Util-Functions to start an activity with an intent
inline fun <reified T : Activity> Context.createIntent(vararg extras: Pair<String, Any?>) =
    Intent(this, T::class.java).apply { putExtras(bundleOf(*extras)) }

inline fun <reified T: Activity> Activity.startActivity() {
    startActivity(createIntent<T>())
}


