package com.example.starsystem

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageButton
import android.widget.TextView
import com.example.starsystem.views.StarSystemView

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val starSystemView = findViewById<StarSystemView>(R.id.star_system_animation)
        val displaySecondsTextView = findViewById<TextView>(R.id.display_seconds)
        val playButton = findViewById<ImageButton>(R.id.play_button).apply {
            starSystemView.animationDuration = displaySecondsTextView.text.toString().toInt()
        }
        val increaseByFiveButton = findViewById<ImageButton>(R.id.increase_by_five_button).apply {
            setOnClickListener {
                displaySecondsTextView.apply { text = (text.toString().toInt() + 5).toString() }
            }
        }
    }
}