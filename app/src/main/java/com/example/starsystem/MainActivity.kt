package com.example.starsystem

import android.os.Build
import android.os.Bundle
import android.view.animation.Animation
import android.view.animation.LinearInterpolator
import android.view.animation.RotateAnimation
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.example.starsystem.views.StarSystemView
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : AppCompatActivity() {

    private val roundedCornersPurple =
        ContextCompat.getDrawable(this, R.drawable.rounded_top_corners)

    private val roundedCornersRed =
        ContextCompat.getDrawable(this, R.drawable.drawable_rounded_corners_red)

    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val rotate = RotateAnimation(
            0f,
            360f,
            Animation.RELATIVE_TO_SELF,
            0.5f,
            Animation.RELATIVE_TO_SELF,
            0.5f
        ).apply { interpolator = LinearInterpolator() }
        display_seconds.text = star_system_animation.animationDuration.toString()
        play_button.apply {
            setOnClickListener {
                val animDuration = display_seconds.text.toString().toLong() * 1000L
                star_system_animation.animationDuration = animDuration
                rotate.duration = animDuration
                sun_image_view.startAnimation(rotate)
            }
        }
        decrease_button.apply {
            setOnClickListener {
                display_seconds.apply {
                    text =
                        (if (text.toString().toInt() - 1 > 0) text.toString()
                            .toInt() - 1 else 0).toString()
                }
            }
        }
        increase_by_five_button.apply {
            setOnClickListener {
                display_seconds.apply { text = (text.toString().toInt() + 1).toString() }
            }
        }
        make_infinite_button.setOnClickListener {
            make_infinite_button.background =
                if (star_system_animation.isInfiniteAnimation) roundedCornersPurple else roundedCornersRed
            star_system_animation.apply { isInfiniteAnimation = !isInfiniteAnimation }
        }
        if (star_system_animation.animationDuration != 0L) play_button.performClick()
    }
}