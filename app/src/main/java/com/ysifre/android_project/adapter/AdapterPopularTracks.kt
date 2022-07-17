package com.ysifre.android_project.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.recyclerview.widget.RecyclerView
import com.ysifre.android_project.R


class AdapterPopularTracks(private val mList: List<ItemsPopularTrackModel>) : RecyclerView.Adapter<AdapterPopularTracks.ViewHolder>() {

    private lateinit var onItemClickListener: OnItemClickListener

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
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
        holder.ranking.text = itemsViewModel.ranking

        holder.itemView.setOnClickListener(View.OnClickListener {
            onItemClickListener.onItemClick(
                position
            )
        })
    }

    class ViewHolder(ItemView: View) : RecyclerView.ViewHolder(ItemView) {
        val trackName: TextView = itemView.findViewById(R.id.trackNameCardViewPopular)
        val ranking: TextView = itemView.findViewById(R.id.rankingTextView)
    }

    interface OnItemClickListener{
        fun onItemClick(position: Int)
    }

    override fun getItemCount(): Int {
        return mList.size
    }

}
