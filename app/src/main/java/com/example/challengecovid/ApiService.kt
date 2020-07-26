package com.example.challengecovid

import com.example.challengecovid.model.CoronaStatistics
import com.github.kittinunf.fuel.Fuel
import com.github.kittinunf.result.Result

/**
 * not used at the moment
 */
object ApiService {



    init {
        // set base path for all requests
        //FuelManager.instance.basePath = "https://disease.sh/v2"
    }

    //TODO: how to return value, passing the viewmodel is bad practice but returning is not possible either.
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