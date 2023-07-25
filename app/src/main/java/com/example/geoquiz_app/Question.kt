package com.example.geoquiz_app
import androidx.annotation.StringRes

data class Question(@StringRes val textResId: Int, val answer:Boolean, var isAnswered: Boolean = false)
