package dev.paranoid

import android.content.Context
import android.graphics.*
import android.graphics.drawable.ScaleDrawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.*
import android.widget.*
import androidx.core.graphics.set
import androidx.core.view.children
import androidx.core.view.get
import androidx.core.view.marginLeft

class TabLayout(c: Context): LinearLayout(c) {
    lateinit var tabs: LinearLayout
    var listOfViews = mutableListOf<View>()

    init {
        orientation = LinearLayout.VERTICAL

        addView(LinearLayout(context).apply {
            setPadding(40, 20, 40, 20)
            tabs = this
        }, LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, 150))
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
    val colors = listOf<Int>(0xFFF0022, 0xF000000, 0xF00FF00, 0xF0022FF, 0xFFF2200)

    lateinit var bitmap: Bitmap

    init {
        bitmap = Bitmap.createBitmap(1080 - 20, 1700, Bitmap.Config.ARGB_8888)
        freshBitmap()
    }

    fun freshBitmap() {
        for (i in 0..(bitmap.width - 1)) {
            for (j in 0..(bitmap.height - 1)) {
                bitmap[i, j] = colors[index]
            }
        }
    }

    override fun onTouchEvent(event: MotionEvent?): Boolean {
        if (event != null) {
            if (event.x > width / 2F) {
                if (index < size - 1) index++
            } else {
                if (index > 0) index--
            }
        }
        index %= size
        freshBitmap()
        invalidate()
        return super.onTouchEvent(event)
    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        canvas?.apply {
            drawLine(10F + (width - 20F) * index / size, 10F, 10F + (width - 20F) * (index + 1) / size, 10F, Paint().apply {
                color = Color.GREEN
                strokeWidth = 10F
            })
            drawBitmap(bitmap, 10F, 20F, Paint().apply {  })
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
            setPadding(290, 0, 0, 30)
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
            add(ImageButton(context).apply {
                setImageDrawable(
                    resources.getDrawable(R.drawable.ic_launcher_background).apply {
                        scaleX = 0.5F
                        scaleY = 0.5F
                    }
                )
            }, MainChoice(context))
            add(ImageButton(context).apply {
                setImageDrawable(
                    resources.getDrawable(R.drawable.ic_launcher_background).apply {
                        scaleX = 0.5F
                        scaleY = 0.5F
                    }
                )
            }, YouWereChoosen(context))
            add(ImageButton(context).apply {
                setImageDrawable(
                    resources.getDrawable(R.drawable.ic_launcher_background).apply {
                        scaleX = 0.5F
                        scaleY = 0.5F
                    }
                )
            }, Chat(context))
            add(ImageButton(context).apply {
                setImageDrawable(
                    resources.getDrawable(R.drawable.ic_launcher_background).apply {
                        scaleX = 0.5F
                        scaleY = 0.5F
                    }
                )
            }, Profile(context))
        })

        (window.decorView as FrameLayout).addView(TextView(this).apply {
            text = "DecorView Test"
            setTextColor(Color.BLUE)
        },
            FrameLayout.LayoutParams(FrameLayout.LayoutParams.WRAP_CONTENT, FrameLayout.LayoutParams.WRAP_CONTENT).apply {
            leftMargin = 700
            topMargin = 700
        })
    }
}
