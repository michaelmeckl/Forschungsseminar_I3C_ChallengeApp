package com.example.challengecovid.model

import android.graphics.drawable.Drawable

data class Challenge (val id: String, val title: String, val description: String?, val image: Drawable?,  val points: Int, val difficulty: String, val time: Float) {

}