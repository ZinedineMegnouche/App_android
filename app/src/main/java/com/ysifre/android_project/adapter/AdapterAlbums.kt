package com.ysifre.android_project.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.ysifre.android_project.R


class AdapterAlbums(private val mList: List<ItemAlbumViewModel>) : RecyclerView.Adapter<AdapterAlbums.ViewHolder>() {

    private lateinit var context: Context
    private lateinit var onItemClickListener: OnItemClickListener

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.card_view_album, parent, false)
        context = parent.context
        return ViewHolder(view)
    }

    fun setOnItemClickListener(onItemClickListener: OnItemClickListener) {
        this.onItemClickListener = onItemClickListener
    }

    private fun getItem(position: Int): ItemAlbumViewModel {
        return mList[position]
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val itemsViewModel = mList[position]
        Glide.with(context).load(itemsViewModel.image).centerCrop().into(holder.image)
        holder.albumName.text = itemsViewModel.albumName
        holder.albumDate.text = itemsViewModel.albumDate

        holder.itemView.setOnClickListener(View.OnClickListener {
            onItemClickListener.onItemClick(
                position
            )
        })
    }

    class ViewHolder(ItemView: View) : RecyclerView.ViewHolder(ItemView) {
        var image: ImageView = itemView.findViewById(R.id.imageAlbumCardView)
        val albumName: TextView = itemView.findViewById(R.id.albumNameCardViewAlbum)
        val albumDate: TextView = itemView.findViewById(R.id.albumDateCardViewAlbum)
    }

    interface OnItemClickListener{
        fun onItemClick(position: Int)
    }

    override fun getItemCount(): Int {
        return mList.size
    }

}
