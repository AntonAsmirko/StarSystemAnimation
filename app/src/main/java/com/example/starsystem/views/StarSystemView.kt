package com.example.starsystem.views

import android.annotation.SuppressLint
import android.content.Context
import android.content.res.TypedArray
import android.graphics.Canvas
import android.graphics.drawable.Drawable
import android.os.Parcel
import android.os.Parcelable
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
    var startAnimationTime = -1L

    private var namesOfPlanets: java.util.ArrayList<String> = ArrayList()

    var planets: List<Planet>? = null
        set(list) {
            field = list
            field?.forEach { it.view = this }
            if (namesOfPlanets.isEmpty())
                field?.forEach { namesOfPlanets.add(it.name) }
        }

    init {
        val a: TypedArray = context.obtainStyledAttributes(attrs, R.styleable.StarSystem)
        try {
            animationDuration = a.getInt(R.styleable.StarSystem_initialDuration, 0).toLong()
        } finally {
            a.recycle()
        }
    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        val currentTime = System.currentTimeMillis()
        planets?.forEach {
            it.draw(
                canvas!!,
                currentTime - startAnimationTime < animationDuration || isInfiniteAnimation
            )
        }
    }

    override fun onDetachedFromWindow() {
        super.onDetachedFromWindow()
        planets?.forEach { it.view = null }
    }

    override fun onLayout(changed: Boolean, left: Int, top: Int, right: Int, bottom: Int) {
        super.onLayout(changed, left, top, right, bottom)
        planets?.forEach {
            it.apply {
                centerX = parent?.initPositionX ?: this@StarSystemView.right / 2
                centerY = parent?.initPositionY ?: this@StarSystemView.bottom / 2
                initPositionX = centerX + offset
                initPositionY = centerY
            }
        }
    }

    override fun onRestoreInstanceState(state: Parcelable?) {
        val savedState = state as SavedState
        super.onRestoreInstanceState(savedState.superState)
        this.namesOfPlanets = ArrayList<String>().apply { addAll(savedState.namesOfThePlanets) }
        this.planets = savedState.planets
        this.isInfiniteAnimation = savedState.isInfiniteAnimation
        this.animationDuration = savedState.animDuration
        this.startAnimationTime = savedState.startAnimTime
    }

    override fun onSaveInstanceState(): Parcelable? {
        return SavedState(
            super.onSaveInstanceState(),
            namesOfPlanets.toArray(Array(namesOfPlanets.size) { "a" }),
            planets as MutableList<Planet>,
            isInfiniteAnimation,
            animationDuration,
            startAnimationTime
        )
    }

    class SavedState : BaseSavedState {

        var namesOfThePlanets: Array<String>
        var planets: MutableList<Planet>? = null
        var isInfiniteAnimation = false
        var animDuration = -1L
        var startAnimTime = -1L

        constructor(
            superState: Parcelable?,
            namesOfThePlanets: Array<String>,
            planets: MutableList<Planet>?,
            isInfiniteAnimation: Boolean,
            animDuration: Long,
            startAnimTime: Long
        ) : super(superState) {
            this.namesOfThePlanets = namesOfThePlanets
            this.planets = planets
            this.isInfiniteAnimation = isInfiniteAnimation
            this.animDuration = animDuration
            this.startAnimTime = startAnimTime
        }

        constructor(superState: Parcel?) : super(superState){
            val arrSize = superState!!.readInt()
            this.namesOfThePlanets = Array(arrSize){"a"}
            superState.readStringArray(this.namesOfThePlanets)
            this.planets = mutableListOf()
            for (i in 0 until arrSize)
                planets!!.add(superState.readParcelable(Planet.javaClass.classLoader)!!)
            this.isInfiniteAnimation = superState.readInt() == 1
            this.animDuration = superState.readLong()
            this.startAnimTime = superState.readLong()
        }

        override fun writeToParcel(out: Parcel?, flags: Int) {
            super.writeToParcel(out, flags)
            out!!.writeInt(namesOfThePlanets.size)
            out.writeArray(namesOfThePlanets)
            planets?.forEach { out.writeParcelable(it, 0) }
            out.writeInt(if (isInfiniteAnimation) 1 else 0)
            out.writeLong(animDuration)
            out.writeLong(startAnimTime)
        }

        companion object CREATOR : Parcelable.Creator<SavedState?> {
            override fun createFromParcel(source: Parcel?): SavedState? {
                return SavedState(source)
            }


            override fun newArray(size: Int): Array<SavedState?> {
                return Array(size) { null }
            }

        }
    }

    class Planet(
        private val imageId: Int,
        private var curAngle: Float,
        private val xSize: Int,
        private val ySize: Int,
        private val rotationSpeed: Float,
        val offset: Int,
        val name: String
    ) : Parcelable {

        private lateinit var image: Drawable

        var view: View? = null
            set(v) {
                field = v
                if (v != null)
                    image = AppCompatResources.getDrawable(v.context, imageId)!!
            }

        var parent: Planet? = null
        var initPositionX = -1
        var initPositionY = -1
        var centerX = -1
        var centerY = -1
        var curPositionX = -1
        var curPositionY = -1

        constructor(parcel: Parcel) : this(
            parcel.readInt(),
            parcel.readFloat(),
            parcel.readInt(),
            parcel.readInt(),
            parcel.readFloat(),
            parcel.readInt(),
            parcel.readString()!!
        ) {
            parent = parcel.readParcelable(Planet::class.java.classLoader)
            initPositionX = parcel.readInt()
            initPositionY = parcel.readInt()
            centerX = parcel.readInt()
            centerY = parcel.readInt()
            curPositionX = parcel.readInt()
            curPositionY = parcel.readInt()
        }

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
            view!!.invalidate()
        }

        override fun describeContents(): Int {
            return 0
        }

        override fun writeToParcel(dest: Parcel?, flags: Int) {
            dest?.writeInt(imageId)
            dest?.writeFloat(curAngle)
            dest?.writeInt(xSize)
            dest?.writeInt(ySize)
            dest?.writeFloat(rotationSpeed)
            dest?.writeInt(offset)
            dest?.writeString(name)
            dest?.writeParcelable(parent, 0)
            dest?.writeInt(initPositionX)
            dest?.writeInt(initPositionY)
            dest?.writeInt(centerX)
            dest?.writeInt(centerY)
            dest?.writeInt(curPositionX)
            dest?.writeInt(curPositionY)
        }

        companion object CREATOR : Parcelable.Creator<Planet> {
            override fun createFromParcel(parcel: Parcel): Planet {
                return Planet(parcel)
            }

            override fun newArray(size: Int): Array<Planet?> {
                return arrayOfNulls(size)
            }
        }
    }


}