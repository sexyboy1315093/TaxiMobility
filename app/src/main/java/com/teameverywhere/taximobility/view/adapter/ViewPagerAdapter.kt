package com.teameverywhere.taximobility.view.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.viewpager.widget.PagerAdapter
import com.teameverywhere.taximobility.R
import com.teameverywhere.taximobility.model.Notice

class ViewPagerAdapter(private val notices: ArrayList<Notice>, private val onClickListener: ViewPagerAdapter.OnClickListener): PagerAdapter() {

    interface OnClickListener {
        fun onClickItem(notice: Notice)
    }

    override fun instantiateItem(container: ViewGroup, position: Int): Any {
        val view = LayoutInflater.from(container.context).inflate(R.layout.notice_item_layout, container, false)
        view.findViewById<TextView>(R.id.tvTitle).text = notices[position].title
        view.findViewById<TextView>(R.id.tvDate).text = notices[position].date
        view.findViewById<ConstraintLayout>(R.id.clMove).setOnClickListener {
            onClickListener.onClickItem(notices[position])
        }
        container.addView(view)
        return view
    }

    override fun destroyItem(container: ViewGroup, position: Int, `object`: Any) {
        container.removeView(`object` as View?)
    }

    override fun getCount(): Int {
        return notices.size
    }

    override fun isViewFromObject(view: View, `object`: Any): Boolean {
        return (view == `object`)
    }
}