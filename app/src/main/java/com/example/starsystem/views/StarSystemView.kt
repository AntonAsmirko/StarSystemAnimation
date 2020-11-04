package com.example.starsystem.views

import android.annotation.SuppressLint
import android.content.Context
import android.content.res.TypedArray
import android.graphics.*
import android.graphics.drawable.Drawable
import android.util.AttributeSet
import android.view.View
import androidx.appcompat.content.res.AppCompatResources
import com.example.starsystem.R
import kotlin.math.cos
import kotlin.math.sin


@SuppressLint("CustomViewStyleable")
class StarSystemView(context: Context, attrs: AttributeSet) : View(context, attrs) {

    var isInfiniteAnimation = false

    var animationDuration = 0L
        set(value) {
            field = value
            startAnimationTime = System.currentTimeMillis()
            invalidate()
        }

    private val saturn =
        AppCompatResources.getDrawable(context, R.drawable.ic_planet_saturn_illustration_by_vexels)
    private val jupiter =
        AppCompatResources.getDrawable(context, R.drawable.ic_planet_jupiter_illustration_by_vexels)
    private val venus =
        AppCompatResources.getDrawable(context, R.drawable.ic_planet_neptune_illustration_by_vexels)
    private val earth =
        AppCompatResources.getDrawable(
            context,
            R.drawable.ic_earth_planet_illustration_earth_by_vexels
        )
    private val moon =
        AppCompatResources.getDrawable(context, R.drawable.ic_satelite_moon_illustration_by_vexels)

    private val planets = mutableListOf(
        Planet(saturn!!, 0f, 90, 90, 2f, 550),
        Planet(jupiter!!, 0f, 120, 120, 1.5f, 450),
        Planet(venus!!, 0f, 50, 50, 2.2f, 360),
        Planet(earth!!, 0f, 70, 70, 1.2f, 270)
    )

    init {
        val a: TypedArray = context.obtainStyledAttributes(attrs, R.styleable.StarSystem)
        try {
            animationDuration = a.getInt(R.styleable.StarSystem_initialDuration, 0).toLong()
        } finally {
            a.recycle()
        }
        val earth = planets[3]
        planets.add(Planet(moon!!, 0f, 30, 30, 3f, 52).apply {
            parent = earth
        })
    }

    var startAnimationTime = -1L

    private val paint = Paint().apply {
        color = Color.RED
        isAntiAlias = true
        isFilterBitmap = true
        isDither = true
    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        val currentTime = System.currentTimeMillis()
        planets.forEach {
            it.draw(
                canvas!!,
                currentTime - startAnimationTime < animationDuration || isInfiniteAnimation
            )
        }
    }

    override fun onLayout(changed: Boolean, left: Int, top: Int, right: Int, bottom: Int) {
        super.onLayout(changed, left, top, right, bottom)
        planets.forEach {
            it.apply {
                centerX = parent?.initPositionX ?: this@StarSystemView.right / 2
                centerY = parent?.initPositionY ?: this@StarSystemView.bottom / 2
                initPositionX = centerX + offset
                initPositionY = centerY
            }
        }
    }

    private inner class Planet(
        private val image: Drawable,
        private var curAngle: Float,
        private val xSize: Int,
        private val ySize: Int,
        private val rotationSpeed: Float,
        val offset: Int
    ) {

        var parent: Planet? = null

        var initPositionX = -1
        var initPositionY = -1
        var centerX = -1
        var centerY = -1
        var curPositionX = -1
        var curPositionY = -1

        fun draw(canvas: Canvas, makeMove: Boolean = false) {
            val angle = curAngle * Math.PI / 180
            val rotatedX =
                cos(angle) * (initPositionX - centerX) - sin(angle) * (initPositionY - centerY) + centerX
            val rotatedY =
                sin(angle) * (initPositionX - centerX) + cos(angle) * (initPositionY - centerY) + centerY
            curPositionX = rotatedX.toInt()
            curPositionY = rotatedY.toInt()
            image.setBounds(
                (rotatedX - xSize / 2).toInt(),
                (rotatedY - ySize / 2).toInt(),
                (rotatedX + xSize / 2).toInt(),
                (rotatedY + ySize / 2).toInt()
            )
            if (curAngle >= 360f) curAngle = 0f
            if (makeMove) curAngle += rotationSpeed
            if (parent != null) {
                centerX = parent!!.curPositionX
                centerY = parent!!.curPositionY
                initPositionX = centerX + offset
                initPositionY = centerY
            }
            image.draw(canvas)
            invalidate()
        }
    }
}