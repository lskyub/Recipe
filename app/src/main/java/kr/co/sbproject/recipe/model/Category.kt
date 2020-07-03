package kr.co.sbproject.recipe.model

data class Category(var category: String)

fun getCategoryList() = mutableListOf<Category>()