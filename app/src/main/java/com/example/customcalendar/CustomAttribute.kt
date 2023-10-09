package com.example.customcalendar

import android.graphics.drawable.Drawable

data class CustomAttribute(
    val isEdit : Boolean,
    val isShowCycleCount : Boolean,
    val dateToDrawableMap: HashMap<String, Drawable>
) {
}