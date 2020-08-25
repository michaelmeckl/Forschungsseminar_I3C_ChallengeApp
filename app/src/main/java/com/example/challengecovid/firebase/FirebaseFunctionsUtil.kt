package com.example.challengecovid.firebase

import com.google.android.gms.tasks.Task
import com.google.firebase.functions.FirebaseFunctions
import com.google.firebase.functions.ktx.functions
import com.google.firebase.ktx.Firebase

object FirebaseFunctionsUtil {

    private val functions: FirebaseFunctions = Firebase.functions

    fun callCloudFunctionForResult(functionName: String, data: Any): Task<String> {   //TODO: not any! make this more specific
        // Call the function and extract the operation from the result
        return functions
            .getHttpsCallable(functionName)
            .call(data)
            .continueWith { task ->
                // This continuation runs on either success or failure, but if the task
                // has failed then task.result will throw an Exception which will be
                // propagated down.
                /*
                val result = task.result?.data as Map<String, Any>
                result["operationResult"] as Int
                 */
                val result = task.result?.data as String
                result
            }
    }
    //TODO: call this like so:
    /*
    val inputMessage = "Hello!"
    callCloudFunctionForResult(inputMessage)
        .addOnCompleteListener(OnCompleteListener { task ->
            if (!task.isSuccessful) {
                val e = task.exception
                if (e is FirebaseFunctionsException) {
                    val code = e.code
                    val details = e.details
                }

                // [START_EXCLUDE]
                Log.w(TAG, "addMessage:onFailure", e)
                showSnackbar("An error occurred.")
                return@OnCompleteListener
                // [END_EXCLUDE]
            }

            // [START_EXCLUDE]
            val result = task.result
            binding.fieldMessageOutput.setText(result)
            // [END_EXCLUDE]
        })
     */

}