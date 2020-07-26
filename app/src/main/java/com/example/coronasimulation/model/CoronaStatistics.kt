package com.example.coronasimulation.model

import com.github.kittinunf.fuel.core.ResponseDeserializable
import com.google.gson.Gson
import com.google.gson.annotations.SerializedName

// TODO: don't make this dependent of the API!
data class CoronaStatistics(
    val country: String,
    @SerializedName("cases")
    val casesTotal: Int,
    @SerializedName("todayCases")
    val casesToday: Int,
    @SerializedName("deaths")
    val deathsTotal: Int,
    @SerializedName("todayDeaths")
    val deathsToday: Int,
    @SerializedName("recovered")
    val recoveredTotal: Int,
    @SerializedName("todayRecovered")
    val recoveredToday: Int,
    @SerializedName("tests")
    val testsTotal: Int,
    val population: Int,
    val affectedCountriesTotal: Int?   // only for global statistics
)
{

    class Deserializer : ResponseDeserializable<CoronaStatistics> {
        override fun deserialize(content: String): CoronaStatistics? =
            Gson().fromJson(content, CoronaStatistics::class.java)
    }
}