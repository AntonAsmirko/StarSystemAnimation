package com.example.starsystem

import android.os.Bundle
import android.view.animation.Animation
import android.view.animation.LinearInterpolator
import android.view.animation.RotateAnimation
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.example.starsystem.views.StarSystemView


class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val starSystemView = findViewById<StarSystemView>(R.id.star_system_animation)
        val displaySecondsTextView = findViewById<TextView>(R.id.display_seconds)
        val rotate = RotateAnimation(
            0f,
            360f,
            Animation.RELATIVE_TO_SELF,
            0.5f,
            Animation.RELATIVE_TO_SELF,
            0.5f
        ).apply { interpolator = LinearInterpolator() }
        val sunImgView = findViewById<ImageView>(R.id.sun_image_view)
        displaySecondsTextView.text = starSystemView.animationDuration.toString()
        val playButton = findViewById<ImageButton>(R.id.play_button).apply {
            setOnClickListener {
                val animDuration = displaySecondsTextView.text.toString().toLong() * 1000L
                starSystemView.animationDuration = animDuration
                rotate.duration = animDuration
                sunImgView.startAnimation(rotate)
            }
        }
        val decreaseButton = findViewById<ImageButton>(R.id.decrease_button).apply {
            setOnClickListener {
                displaySecondsTextView.apply {
                    text =
                        (if (text.toString().toInt() - 1 > 0) text.toString()
                            .toInt() - 1 else 0).toString()
                }
            }
        }
        val increaseByFiveButton = findViewById<ImageButton>(R.id.increase_by_five_button).apply {
            setOnClickListener {
                displaySecondsTextView.apply { text = (text.toString().toInt() + 1).toString() }
            }
        }
        if (starSystemView.animationDuration != 0L) playButton.performClick()
    }
}