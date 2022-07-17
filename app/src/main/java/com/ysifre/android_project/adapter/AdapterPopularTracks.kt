package com.ysifre.android_project.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.recyclerview.widget.RecyclerView
import com.ysifre.android_project.R


class AdapterPopularTracks(private val mList: List<ItemsPopularTrackModel>) : RecyclerView.Adapter<AdapterPopularTracks.ViewHolder>() {

    private lateinit var onItemClickListener: OnItemClickListener

    // create new views
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        // inflates the card_view_design view
        // that is used to hold list item
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.card_view_popular_tracks, parent, false)

        return ViewHolder(view)
    }

    fun setOnItemClickListener(onItemClickListener: OnItemClickListener) {
        this.onItemClickListener = onItemClickListener
    }

    private fun getItem(position: Int): ItemsPopularTrackModel {
        return mList[position]
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val itemsViewModel = mList[position]
        holder.trackName.text = itemsViewModel.trackName

        holder.itemView.setOnClickListener(View.OnClickListener {
            onItemClickListener.onItemClick(
                position
            )
        })
    }

    // Holds the views for adding it to image and text
    class ViewHolder(ItemView: View) : RecyclerView.ViewHolder(ItemView) {
        val trackName: TextView = itemView.findViewById(R.id.trackNameCardViewPopular)
    }

    interface OnItemClickListener{
        fun onItemClick(position: Int)
    }

    override fun getItemCount(): Int {
        return mList.size
    }

}
