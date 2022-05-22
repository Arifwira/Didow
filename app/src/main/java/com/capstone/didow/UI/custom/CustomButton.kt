package com.capstone.didow.UI.custom

import android.content.Context
import android.graphics.Canvas
import android.graphics.drawable.Drawable
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatButton
import androidx.core.content.ContextCompat
import com.capstone.didow.R

class SpeakerButton: AppCompatButton {
    private lateinit var speakerButton: Drawable

    constructor(context: Context) : super(context) {
        init()
    }

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
        init()
    }

    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
        init()
    }

    override fun onDraw(canvas: Canvas){
        super.onDraw(canvas)
        background = speakerButton
    }

    private fun init(){
        speakerButton = ContextCompat.getDrawable(context, R.drawable.speaker_custom_button) as Drawable
    }
}

class GuideButton: AppCompatButton{
    private lateinit var guideButton: Drawable

    constructor(context: Context) : super(context) {
        init()
    }

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
        init()
    }

    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
        init()
    }

    override fun onDraw(canvas: Canvas){
        super.onDraw(canvas)
        background = guideButton
    }

    private fun init(){
        guideButton = ContextCompat.getDrawable(context, R.drawable.guide_custom_button) as Drawable
    }
}