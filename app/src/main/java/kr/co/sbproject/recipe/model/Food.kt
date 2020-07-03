package kr.co.sbproject.recipe.model

import android.graphics.drawable.Drawable

data class Food(var image: Drawable, var title: String)

fun getFoodList() = mutableListOf<Food>()