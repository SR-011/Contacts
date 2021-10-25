package com.practice.contacts.utils

import android.graphics.Canvas
import android.graphics.Rect
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.practice.contacts.R
import kotlin.math.max

class RecyclerItemDecoration(
    val headerOffset: Int,
    val sticky: Boolean,
    val sectionCallBack: SectionCallBack
) : RecyclerView.ItemDecoration() {

    private lateinit var headerView: View
    private lateinit var header: TextView

    override fun getItemOffsets(
        outRect: Rect,
        view: View,
        parent: RecyclerView,
        state: RecyclerView.State
    ) {
        super.getItemOffsets(outRect, view, parent, state)
        val position = parent.getChildAdapterPosition(view)
        if (sectionCallBack.isSection(position)) {
            outRect.top = headerOffset
        }
    }

    override fun onDrawOver(c: Canvas, parent: RecyclerView, state: RecyclerView.State) {
        super.onDrawOver(c, parent, state)
        if (!this::headerView.isInitialized) {
            headerView = inflateHeaderView(parent)
            header = headerView.findViewById(R.id.section_header)
            fixLayoutSize(headerView, parent)
        }

        var previousHeader: CharSequence = ""
        for (i in 0 until parent.childCount) {
            val child = parent.getChildAt(i)
            val position = parent.getChildAdapterPosition(child)
            val title = sectionCallBack.getSectionHeader(position)
            header.text = title
            if (previousHeader != title || sectionCallBack.isSection(position)) {
                drawHeader(c, child, headerView)
                previousHeader = title
            }
        }
    }

    private fun drawHeader(c: Canvas, child: View, headerView: View) {
        c.save()
        if (sticky) {
            c.translate(0f, max(0, child.top - headerView.height).toFloat())
        }
        headerView.draw(c)
        c.restore()
    }

    private fun fixLayoutSize(view: View, parent: ViewGroup) {
        val widthSpec = View.MeasureSpec.makeMeasureSpec(
            parent.width,
            View.MeasureSpec.EXACTLY
        )
        val heightSpec = View.MeasureSpec.makeMeasureSpec(
            parent.height,
            View.MeasureSpec.UNSPECIFIED
        )
        val childWidth = ViewGroup.getChildMeasureSpec(
            widthSpec,
            parent.paddingLeft + parent.paddingRight,
            view.layoutParams.width
        )
        val childHeight = ViewGroup.getChildMeasureSpec(
            heightSpec,
            parent.paddingTop + parent.paddingBottom,
            view.layoutParams.height
        )

        view.measure(childWidth, childHeight)
        view.layout(0, 0, view.measuredWidth, view.measuredHeight)
    }

    private fun inflateHeaderView(parent: RecyclerView): View {
        return LayoutInflater.from(parent.context)
            .inflate(R.layout.section_header_row, parent, false)
    }

    interface SectionCallBack {
        fun isSection(pos: Int): Boolean
        fun getSectionHeader(position: Int): CharSequence
    }
}

