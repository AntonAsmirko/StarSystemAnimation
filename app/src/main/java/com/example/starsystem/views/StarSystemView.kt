package com.example.starsystem.views

import android.content.Context
import android.graphics.*
import android.graphics.drawable.Drawable
import android.util.AttributeSet
import android.view.View
import androidx.appcompat.content.res.AppCompatResources
import com.example.starsystem.R
import kotlin.math.cos
import kotlin.math.sin


class StarSystemView(context: Context, attrs: AttributeSet) : View(context, attrs) {

    private val paint = Paint().apply {
        color = Color.RED
        isAntiAlias = true
        isFilterBitmap = true
        isDither = true
    }
    private val sun =
        AppCompatResources.getDrawable(context, R.drawable.ic_sun_star_illustration_by_vexels)
    private val saturn =
        AppCompatResources.getDrawable(context, R.drawable.ic_planet_saturn_illustration_by_vexels)
    private val jupiter =
        AppCompatResources.getDrawable(context, R.drawable.ic_planet_jupiter_illustration_by_vexels)
    private val venus =
        AppCompatResources.getDrawable(context, R.drawable.ic_planet_neptune_illustration_by_vexels)

    private val sunXSize = 180
    private val sunYSize = 180

    private val planets = mutableListOf(
        Planet(saturn!!, 0f, 100, 100, 2f, 500),
        Planet(jupiter!!, 0f, 140, 140, 1.5f, 370),
        Planet(venus!!, 0f, 70, 70, 2.2f, 200)
    )


    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        sun!!.setBounds(
            this.right / 2 - sunXSize / 2,
            this.bottom / 2 - sunYSize / 2,
            this.right / 2 + sunXSize / 2,
            this.bottom / 2 + sunYSize / 2
        )
        sun.draw(canvas!!)
        planets.forEach { it.draw(canvas) }
        invalidate()
    }

    override fun onLayout(changed: Boolean, left: Int, top: Int, right: Int, bottom: Int) {
        super.onLayout(changed, left, top, right, bottom)
        planets.forEach {
            it.apply {
                centerX = this@StarSystemView.right / 2
                centerY = this@StarSystemView.bottom / 2
                positionX = centerX + offset
                positionY = centerY
            }
        }
    }

    private class Planet(
        private val image: Drawable,
        private var curAngle: Float,
        private val xSize: Int,
        private val ySize: Int,
        private val rotationSpeed: Float,
        val offset: Int
    ) {

        var positionX = -1
        var positionY = -1
        var centerX = -1
        var centerY = -1

        fun draw(canvas: Canvas) {
            val angle = curAngle * Math.PI / 180
            val rotatedX =
                cos(angle) * (positionX - centerX) - sin(angle) * (positionY - centerY) + centerX
            val rotatedY =
                sin(angle) * (positionX - centerX) + cos(angle) * (positionY - centerY) + centerY
            image.setBounds(
                (rotatedX - xSize / 2).toInt(),
                (rotatedY - ySize / 2).toInt(),
                (rotatedX + xSize / 2).toInt(),
                (rotatedY + ySize / 2).toInt()
            )
            if (curAngle >= 360f) curAngle = 0f
            curAngle += rotationSpeed
            image.draw(canvas)
        }
    }
}