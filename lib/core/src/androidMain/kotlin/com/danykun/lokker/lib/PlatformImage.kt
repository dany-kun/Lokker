package com.danykun.lokker.lib

actual typealias Bitmap = android.graphics.Bitmap
actual typealias ImageView = android.widget.ImageView

actual fun ImageView.setImage(bitmap: Bitmap) {
    setImageBitmap(bitmap)
}