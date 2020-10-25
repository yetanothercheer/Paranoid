package dev.paranoid.x

import android.content.Context
import android.view.View
import android.widget.FrameLayout
import android.widget.LinearLayout
import androidx.core.view.get
import androidx.core.view.marginTop

interface Tab {
    fun onSelected()
    fun onDeselected()
}

abstract class TabView(c: Context) : LinearLayout(c) {

    var tabs = onRequestTabs()

    init {
        orientation = LinearLayout.VERTICAL
        addView(LinearLayout(c).apply {
            setPadding(0, 30, 0, 30)
            orientation = LinearLayout.HORIZONTAL
            tabs.forEachIndexed { index, (tab, _) ->
                addView((tab as View).apply {
                    setOnClickListener {
                        toIndex(index)
                    }
                }, LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.WRAP_CONTENT, 1F))
            }
        }, LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT))
        tabs[0].first.onSelected()
        addView(
            tabs[0].second,
            LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.MATCH_PARENT
            )
        )
    }

    var lastIndex = 0
    fun toIndex(index: Int) {
        tabs[lastIndex].first.onDeselected()
        lastIndex = index
        tabs[index].first.onSelected()
        removeViewAt(1)
        addView(
            tabs[index].second,
            LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.MATCH_PARENT
            )
        )
    }

    abstract fun onRequestTabs(): Array<Pair<Tab, View>>

}