package dev.paranoid

import android.app.Activity
import android.content.Context
import android.graphics.*
import android.util.Log
import android.view.*
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.get
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.map
import androidx.lifecycle.observe
import dev.paranoid.x.Tab
import dev.paranoid.x.TabView

class MyTab(c: Context, id: Int) : ImageView(c), Tab {
    init {
        setImageDrawable(resources.getDrawable(id).apply {
        })
        maxHeight = 100
        adjustViewBounds = true
        onDeselected()
    }

    override fun onSelected() {
        drawable.setTint(Color.RED)
    }

    override fun onDeselected() {
        drawable.setTint(Color.LTGRAY)
    }
}

class SelectionView(c: Context) : LinearLayout(c) {
    val vm = ViewModelProvider(c as AppCompatActivity).get(MyModel::class.java)
    var size: Int = 0
    var index = 0

    init {
        size = vm.profile.value?.images?.size!!
        vm.profile.observe(c as AppCompatActivity) { p ->
            size = p.images.size
            // I must admit this is too dangerous
            (get(0) as ViewGroup)[0].invalidate()
        }
    }

    init {
        orientation = LinearLayout.VERTICAL
        addView(object : FrameLayout(c) {
            init {
                addView(
                    object : View(c) {
                        override fun onDraw(canvas: Canvas?) {
                            super.onDraw(canvas)
                            canvas?.apply {

                                drawRoundRect(RectF(clipBounds.apply {
                                    top += 10
                                    bottom -= 10
                                    left += 10
                                    right -= 10
                                }), 20F, 20F, Paint().apply {
                                    shader = RadialGradient(
                                        -100F,
                                        100F,
                                        2000F,
                                        Color.RED,
                                        Color.YELLOW,
                                        Shader.TileMode.CLAMP
                                    )
                                })

                                drawRect(clipBounds, Paint().apply {
                                    color = Color.LTGRAY
                                    xfermode = PorterDuffXfermode(PorterDuff.Mode.DST_IN)
                                })


                                drawRoundRect(RectF().apply {
                                    top = 20F; bottom = 30F
                                    left = 30F + (width - 60F) * index / size
                                    right = 30F + (width - 60F) * (index + 1) / size
                                }, 10F, 10F, Paint().apply {
                                    color = Color.WHITE
                                })
                            }
                        }
                    },
                    FrameLayout.LayoutParams(
                        FrameLayout.LayoutParams.MATCH_PARENT,
                        FrameLayout.LayoutParams.MATCH_PARENT
                    )
                )
                addView(
                    LinearLayout(c).apply {
                        setPadding(30, 50, 30, 50)
                        orientation = VERTICAL
                        addView(
                            TextView(c).apply {
                                gravity = Gravity.BOTTOM
                                setTextColor(Color.WHITE)
                                textSize = 50F
                                text = vm.profile.value?.name
                                vm.profile.observe(c as AppCompatActivity) { p -> text = p.name }
                            },
                            LinearLayout.LayoutParams(
                                LinearLayout.LayoutParams.MATCH_PARENT,
                                0,
                                1F
                            )
                        )
                        addView(TextView(c).apply {
                            setTextColor(Color.WHITE)
                            textSize = 20F
                            text = vm.profile.value?.descriptor
                            vm.profile.observe(c as AppCompatActivity) { p -> text = p.descriptor }
                        })
                        setOnTouchListener { _, _ -> Log.e("??", "ENTER2"); false }
                    },
                    FrameLayout.LayoutParams(
                        FrameLayout.LayoutParams.MATCH_PARENT,
                        FrameLayout.LayoutParams.MATCH_PARENT
                    )
                )
            }

            override fun onTouchEvent(event: MotionEvent?): Boolean {
                Log.e("??", "ENTER")
                if (event != null) {
                    if (event.x > width / 2F) {
                        if (index < size - 1) index++
                    } else {
                        if (index > 0) index--
                    }
                }
                index %= size
                get(0).invalidate()
                return super.onTouchEvent(event)
            }
        }, LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, 0, 1F))

        addView(
            LinearLayout(c).apply {
                orientation = LinearLayout.HORIZONTAL
                setPadding(200, 10, 200, 10)
                addView(
                    object: ImageView(c) {
                        override fun onDraw(canvas: Canvas?) {
                            canvas?.apply {
                                drawCircle(measuredWidth / 2F, measuredHeight / 2F, 100F, Paint().apply {
                                    shader = RadialGradient(measuredWidth / 2F, measuredHeight / 2F, 100F, Color.GRAY, Color.WHITE, Shader.TileMode.CLAMP)
                                })
                            }
                            super.onDraw(canvas)
                        }
                    }.apply {
                        setPadding(10, 10, 10, 10)
                        setImageDrawable(resources.getDrawable(R.drawable.notlike))
                        maxHeight = 170
                        adjustViewBounds = true
                        setOnClickListener { vm.notLike() }
                    },
                    LinearLayout.LayoutParams(
                        0,
                        LinearLayout.LayoutParams.WRAP_CONTENT,
                        1F
                    )
                )
                addView(
                    object: ImageView(c) {
                        override fun onDraw(canvas: Canvas?) {
                            canvas?.apply {
                                drawCircle(measuredWidth / 2F, measuredHeight / 2F, 100F, Paint().apply {
                                    shader = RadialGradient(measuredWidth / 2F, measuredHeight / 2F, 100F, Color.GRAY, Color.WHITE, Shader.TileMode.CLAMP)
                                })
                            }
                            super.onDraw(canvas)
                        }
                    }.apply {
                        setPadding(10, 10, 10, 10)
                        setImageDrawable(resources.getDrawable(R.drawable.like))
                        maxHeight = 170
                        adjustViewBounds = true
                        setOnClickListener { vm.like() }
                    },
                    LinearLayout.LayoutParams(
                        0,
                        LinearLayout.LayoutParams.WRAP_CONTENT,
                        1F
                    )
                )
            },
            LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
            )
        )
    }
}

class RecommendView(c: Context) : FrameLayout(c) {

}

class ChatView(c: Context) : FrameLayout(c) {

}

class ProfileView(c: Context) : FrameLayout(c) {

}

class MyView(c: Context) : TabView(c) {
    override fun onRequestTabs(): Array<Pair<Tab, View>> {
        return arrayOf<Pair<Tab, View>>(
            Pair(MyTab(context, R.drawable.ic_a), SelectionView(context)),
            Pair(MyTab(context, R.drawable.ic_b), RecommendView(context)),
            Pair(MyTab(context, R.drawable.ic_c), ChatView(context)),
            Pair(MyTab(context, R.drawable.ic_d), ProfileView(context))
        )
    }
}