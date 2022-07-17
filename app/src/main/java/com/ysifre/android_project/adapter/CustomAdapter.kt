package com.ysifre.android_project.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.ysifre.android_project.R


class CustomAdapter(private val mList: List<ItemsTrackViewModel>) : RecyclerView.Adapter<CustomAdapter.ViewHolder>() {

    private lateinit var context: Context
    private lateinit var onItemClickListener: OnItemClickListener

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.card_view_track, parent, false)
        context = parent.context
        return ViewHolder(view)
    }

    fun setOnItemClickListener(onItemClickListener: OnItemClickListener) {
        this.onItemClickListener = onItemClickListener
    }

    private fun getItem(position: Int): ItemsTrackViewModel {
        return mList[position]
    }

    override fun onBindViewHolder(holder: ViewHolder, @SuppressLint("RecyclerView") position: Int) {
        val itemsViewModel = mList[position]
        Glide.with(context).load(itemsViewModel.image).centerCrop().into(holder.imageView)
        holder.trackname.text = itemsViewModel.trackName
        holder.artistname.text = itemsViewModel.artistName
        holder.ranking.text = itemsViewModel.ranking

        holder.itemView.setOnClickListener(View.OnClickListener {
            onItemClickListener.onItemClick(
                position
            )
        })
    }

    class ViewHolder(ItemView: View) : RecyclerView.ViewHolder(ItemView) {
        val imageView: ImageView = itemView.findViewById(R.id.imageTrackCardView)
        val artistname: TextView = itemView.findViewById(R.id.artistNameCardView)
        val trackname: TextView = itemView.findViewById(R.id.trackNameCardView)
        val ranking: TextView = itemView.findViewById(R.id.placeTrackCardView)
    }

    interface OnItemClickListener{
        fun onItemClick(position: Int)
    }

    override fun getItemCount(): Int {
        return mList.size
    }
}
