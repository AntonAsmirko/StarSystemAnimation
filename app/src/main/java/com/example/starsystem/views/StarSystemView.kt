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

    private var saturnX = -1
    private var saturnY = -1

    private var jupiterX = -1
    private var jupiterY = -1

    private var curSaturnAngle = 0f
    private var curJupiterAngle = 0f

    private var prevTime = System.currentTimeMillis()

    private val sunXSize = 180
    private val sunYSize = 180

    private val saturnXSize = 100
    private val saturnYSize = 100

    private val jupiterXSize = 140
    private val jupiterYSize = 140

    private val planets = mutableListOf(
        Planet(saturn!!, 0f, 100, 100, 2f),
        Planet(jupiter!!, 0f, 140, 140, 1.5f)
    )


    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        if (saturnX == -1) saturnX = this.right / 2 + 500
        if (saturnY == -1) saturnY = this.bottom / 2

        if (jupiterX == -1) jupiterX = this.right / 2 + 370
        if (jupiterY == -1) jupiterY = this.bottom / 2

        sun!!.setBounds(
            this.right / 2 - sunXSize / 2,
            this.bottom / 2 - sunYSize / 2,
            this.right / 2 + sunXSize / 2,
            this.bottom / 2 + sunYSize / 2
        )
        sun.draw(canvas!!)

        val saturnAngle = curSaturnAngle * Math.PI / 180
        val rotatedSaturnX =
            cos(saturnAngle) * (saturnX - this.right / 2) - sin(saturnAngle) * (saturnY - this.bottom / 2) + this.right / 2
        val rotatedSaturnY =
            sin(saturnAngle) * (saturnX - this.right / 2) + cos(saturnAngle) * (saturnY - this.bottom / 2) + this.bottom / 2
        saturn!!.setBounds(
            (rotatedSaturnX - saturnXSize / 2).toInt(),
            (rotatedSaturnY - saturnYSize / 2).toInt(),
            (rotatedSaturnX + saturnXSize / 2).toInt(),
            (rotatedSaturnY + saturnYSize / 2).toInt()
        )
        if (curSaturnAngle >= 360f) {
            curSaturnAngle = 0f
        }
        val curTime = System.currentTimeMillis()
        if (curTime - prevTime >= 200) {
            curSaturnAngle += 2f
        }
        saturn.draw(canvas)

        val jupiterAngle = curJupiterAngle * Math.PI / 180
        val rotatedJupiterX =
            cos(jupiterAngle) * (jupiterX - this.right / 2) - sin(jupiterAngle) * (jupiterY - this.bottom / 2) + this.right / 2
        val rotatedJupiterY =
            sin(jupiterAngle) * (jupiterX - this.right / 2) + cos(jupiterAngle) * (jupiterY - this.bottom / 2) + this.bottom / 2
        jupiter!!.setBounds(
            (rotatedJupiterX - jupiterXSize / 2).toInt(),
            (rotatedJupiterY - jupiterYSize / 2).toInt(),
            (rotatedJupiterX + jupiterXSize / 2).toInt(),
            (rotatedJupiterY + jupiterYSize / 2).toInt()
        )
        if (curJupiterAngle >= 360f) {
            curJupiterAngle = 0f
        }
        if (curTime - prevTime >= 200) {
            curJupiterAngle += 1.5f
        }
        jupiter!!.draw(canvas)

        invalidate()
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
    }

    private class Planet(
        private val image: Drawable,
        private var curAngle: Float,
        private val xSize: Int,
        private val ySize: Int,
        private val rotationSpeed: Float
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