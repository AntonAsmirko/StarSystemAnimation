package com.example.starsystem

import android.animation.Animator
import android.animation.ObjectAnimator
import android.graphics.drawable.Drawable
import android.os.Build
import android.os.Bundle
import android.view.View
import android.view.animation.Animation
import android.view.animation.AnticipateInterpolator
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

    private lateinit var roundedCornersPurple: Drawable

    private lateinit var roundedCornersRed: Drawable

    private var isSunAnimationInfinite = false

    private val anticipateInterpolator = AnticipateInterpolator()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        roundedCornersPurple = ContextCompat.getDrawable(this, R.drawable.rounded_top_corners)!!
        roundedCornersRed =
            ContextCompat.getDrawable(this, R.drawable.drawable_rounded_corners_red)!!

        display_seconds.text = star_system_animation.animationDuration.toString()

        val animator = ObjectAnimator.ofFloat(sun_image_view, View.ROTATION, 0f, 360f).apply {
            interpolator = anticipateInterpolator
        }

        play_button.apply {
            setOnClickListener {
                val animDuration = display_seconds.text.toString().toLong() * 1000L
                star_system_animation.animationDuration = animDuration
                animator.apply {
                    duration = animDuration
                    start()
                }
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
            isSunAnimationInfinite = !isSunAnimationInfinite
            if (isSunAnimationInfinite) {
                animator.apply {
                    repeatCount = Animation.INFINITE
                    duration = 5_000L
                    start()
                }
            } else {
                animator.repeatCount = 0
            }
        }
        if (star_system_animation.animationDuration != 0L) play_button.performClick()
    }
}