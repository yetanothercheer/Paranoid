package dev.paranoid

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.*
import android.widget.*
import androidx.core.view.children
import androidx.core.view.get

class TabLayout(c: Context): LinearLayout(c) {
    lateinit var tabs: LinearLayout
    var listOfViews = mutableListOf<View>()

    init {
        orientation = LinearLayout.VERTICAL

        addView(LinearLayout(context).apply {
            tabs = this
        }, LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT))
    }

    fun add(tab: View, v: View) {
        val index = listOfViews.size
        tabs.addView(tab, LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT, 1.0f))
        tab.setOnClickListener { changeTab(index) }
        listOfViews.add(v)
        if (listOfViews.size == 1) { changeTab(0) }
    }

    private fun changeTab(index: Int) {
        if (childCount == 2) removeViewAt(1)
        addView(listOfViews[index], LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT, 1.0f))
    }
}

class ChoiceView(c: Context): androidx.appcompat.widget.AppCompatTextView(c) {
    val size = 5;
    var index = 0;

    override fun onTouchEvent(event: MotionEvent?): Boolean {
        if (event != null) {
            if (event.x > width / 2F) {
                if (index < size - 1) index++
            } else {
                if (index > 0) index--
            }
        }
        index %= size
        invalidate()
        return super.onTouchEvent(event)
    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        text = "Image %d".format(index + 1)
        canvas?.apply {
            drawLine(10F + (width - 20F) * index / size, 10F, 10F + (width - 20F) * (index + 1) / size, 10F, Paint().apply {
                color = Color.GREEN
                strokeWidth = 10F
            })
        }
    }
}

class MainChoice(c: Context): LinearLayout(c) {
    init {
        orientation = VERTICAL
        addView(ChoiceView(c), LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT, 1.0f))
        addView(LinearLayout(c).apply {
            addView(Button(c).apply { text = "no" })
            addView(Button(c).apply { text = "yes" })
        }, LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT))
    }
}

class YouWereChoosen(c: Context): LinearLayout(c) {
    init {
        // TODO: Maybe GridLayout
        addView(TextView(c).apply { text = "Not implemented!" })
    }
}

class Chat(c: Context): LinearLayout(c) {
    init {
        addView(ScrollView(c).apply {
            addView(LinearLayout(c).apply {
                orientation = VERTICAL
                addView(TextView(c).apply { text = "New Pairing!" })
                addView(TextView(c).apply { text = "PlaceHolder for parings" })
                addView(TextView(c).apply { text = "Messages" })
            })
        })
    }
}

class Profile(c: Context): LinearLayout(c) {
    init {
        addView(TextView(c).apply { text = "Not implemented!" })
    }
}

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(TabLayout(this).apply {
            add(TextView(context).apply { text = "MainChoice" }, MainChoice(context))
            add(TextView(context).apply { text = "YouWereChoosen" }, YouWereChoosen(context))
            add(TextView(context).apply { text = "Chat" }, Chat(context))
            add(TextView(context).apply { text = "Profile" }, Profile(context))
        })
    }
}
