package com.danykun.lokker.sample

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.danykun.lokker.lib.Lokker
import com.danykun.lokker.lib.error.ThrowingErrorListener
import com.danykun.lokker.lib.withLokker
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        Lokker.initWith(FuelFetcher())
        val url = "https://kotlinlang.org/assets/images/twitter-card/kotlin_800x320.png"
        imageview.withLokker(this).setImageFromUrl(url) {
            errorListener = ThrowingErrorListener()
            onCompleted = { Toast.makeText(this@MainActivity, "Completed", Toast.LENGTH_SHORT).show() }
        }
    }
}
