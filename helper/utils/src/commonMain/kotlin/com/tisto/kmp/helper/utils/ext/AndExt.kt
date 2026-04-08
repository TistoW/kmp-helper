package com.tisto.kmp.helper.utils.ext

import androidx.lifecycle.LifecycleOwner
import kotlinx.coroutines.Runnable

//fun delayFunction(r: Runnable, duration: Long = 300) {
//    Handler(Looper.getMainLooper()).postDelayed(r, duration)
//}

//fun Bitmap.toBlackAndWhite(threshold: Int = 128): Bitmap {
//    val w = width
//    val h = height
//
//    // config bisa null (mis. dari decodeStream tertentu), pakai fallback yang aman
//    val outConfig = this.config ?: Bitmap.Config.ARGB_8888
//    val out = createBitmap(w, h, outConfig)
//    out.density = this.density
//
//    val pixels = IntArray(w * h)
//    getPixels(pixels, 0, w, 0, 0, w, h)
//
//    // pakai bobot integer agar lebih cepat dan stabil
//    // gray ~= (r*299 + g*587 + b*114) / 1000
//    for (i in pixels.indices) {
//        val p = pixels[i]
//        val a = Color.alpha(p)
//        val r = Color.red(p)
//        val g = Color.green(p)
//        val b = Color.blue(p)
//
//        val gray = (r * 299 + g * 587 + b * 114) / 1000
//        val bw = if (gray > threshold) 255 else 0
//        pixels[i] = Color.argb(a, bw, bw, bw)
//    }
//
//    out.setPixels(pixels, 0, w, 0, 0, w, h)
//    return out
//}
//
//fun Bitmap.addPaddingLeft(paddingLeft: Int): Bitmap {
//    val outputBitmap = Bitmap.createBitmap(width + paddingLeft, height, Bitmap.Config.ARGB_8888)
//    val canvas = Canvas(outputBitmap)
//    canvas.drawColor(Color.WHITE)
//    canvas.drawBitmap(this, paddingLeft.toFloat(), 0f, null)
//    return outputBitmap
//}
//
//fun String.convertUrlToBitmap(): Bitmap? {
//    var bitmap: Bitmap? = null
//    try {
//        val url = URL(this)
//        val connection = url.openConnection()
//        connection.doInput = true
//        connection.connect()
//        val input = connection.getInputStream()
//        bitmap = BitmapFactory.decodeStream(input)
//    } catch (e: Exception) {
//        e.printStackTrace()
//    }
//    return bitmap
//}
//
//fun <T> LiveData<T>.observeOnce(
//    lifecycleOwner: LifecycleOwner,
//    observer: Observer<T>
//) {
//    observe(lifecycleOwner, object : Observer<T> {
//        override fun onChanged(value: T) {
//            observer.onChanged(value)
//            removeObserver(this)
//        }
//    })
//}
