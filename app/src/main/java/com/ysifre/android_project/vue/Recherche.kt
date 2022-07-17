package com.ysifre.android_project.vue

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.SearchView
import android.widget.TextView
import androidx.core.os.bundleOf
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.setFragmentResult
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.SimpleTarget
import com.bumptech.glide.request.transition.Transition
import com.ysifre.android_project.Album
import com.ysifre.android_project.GetDiscographyNetwork
import com.ysifre.android_project.R
import com.ysifre.android_project.adapter.AdapterAlbums
import com.ysifre.android_project.adapter.ItemAlbumViewModel
import com.ysifre.android_project.model.Artist
import com.ysifre.android_project.model.GetArtistByNameNetwork
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch


class Recherche : Fragment() {
    lateinit var searchView: SearchView
    lateinit var recyclerViewAlbum: RecyclerView
    lateinit var recyclerViewArtist: RecyclerView
    lateinit var homeButton: ImageView
    lateinit var favButton: ImageView
    lateinit var artistTextView: TextView
    lateinit var albumTextView: TextView

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.recherche, container, false).apply {
            searchView = findViewById(R.id.researchView)
            recyclerViewAlbum = findViewById(R.id.searchAlbumRecyclerView)
            recyclerViewArtist = findViewById(R.id.recyclerViewArtist)
            homeButton = findViewById(R.id.homeButton)
            favButton = findViewById(R.id.favButton)
            artistTextView = findViewById(R.id.textViewArtist)
            albumTextView = findViewById(R.id.textViewAlbum)

            recyclerViewAlbum.layoutManager = LinearLayoutManager(activity)
            recyclerViewArtist.layoutManager = LinearLayoutManager(activity)
            homeButton.setOnClickListener { findNavController().navigate(RechercheDirections.actionRecherche2ToClassement2())}
            favButton.setOnClickListener { findNavController().navigate(RechercheDirections.actionRecherche2ToFavoris2())}
            artistTextView.isVisible = false
            albumTextView.isVisible = false
            searchViewArtists()
            searchViewAlbums()

        }
    }

    private fun searchViewAlbums() {
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                searchViewArtists()
                val data = ArrayList<ItemAlbumViewModel>()
                var albums: ArrayList<Album> = ArrayList()
                GlobalScope.launch(Dispatchers.Main) {
                    try {
                        val response = GetDiscographyNetwork.getDiscographyByName(query!!)
                        Log.d("DISCOGRAPHY RESPONSE", response.toString())
                        if (response != null) {
                            for(i in response.album){
                                albums.add(i)
                            }
                        }
                        for (i in albums) {
                            if(i.strAlbumThumb != null){
                                data.add(ItemAlbumViewModel(i.strAlbumThumb, i.strAlbum, i.intYearReleased))
                            }else{
                                data.add(ItemAlbumViewModel("", i.strAlbum, i.intYearReleased))
                            }

                        }
                        val adapter = AdapterAlbums(data)
                        recyclerViewAlbum.adapter = adapter
                        albumTextView.isVisible = true
                        recyclerViewAlbum.isVisible = true
                        adapter.setOnItemClickListener(object: AdapterAlbums.OnItemClickListener{
                            override fun onItemClick(position: Int) {
                                val albumName = albums.get(position).strAlbum
                                setFragmentResult("requestKeyAlbumIdSearchView", bundleOf("bundleKeyAlbumIdSearchView" to albumName))
                                setFragmentResult("requestKeyArtistNameSearchView", bundleOf("bundleKeyArtistNameSearchView" to query))
                                findNavController().navigate(
                                    RechercheDirections.actionRecherche2ToDetailAlbum6()
                                )
                            }
                        })

                    } catch(e: Exception) {
                        albumTextView.isVisible = false
                        recyclerViewAlbum.isVisible = false
                        Log.d("CATCH DISPLAY ALBUMS", e.message.toString())
                    }
                }
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                return false
            }
        })
    }

    private fun searchViewArtists() {
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                searchViewAlbums()
                val data = ArrayList<ItemAlbumViewModel>()
                var artists: ArrayList<Artist> = ArrayList()
                GlobalScope.launch(Dispatchers.Main) {
                    try {
                        val response = GetArtistByNameNetwork.findArtistByName(query!!)
                        if (response != null) {
                            for(i in response.artists){
                                artists.add(i)
                            }
                        }
                        for (i in artists) {
                            if(i.strArtistThumb != null){
                                data.add(ItemAlbumViewModel(i.strArtistThumb, i.strArtist, ""))
                            }else{
                                data.add(ItemAlbumViewModel("", i.strArtist, ""))
                            }

                        }
                        val adapter = AdapterAlbums(data)
                        recyclerViewArtist.adapter = adapter
                        artistTextView.isVisible = true
                        recyclerViewArtist.isVisible = true
                        adapter.setOnItemClickListener(object: AdapterAlbums.OnItemClickListener{
                            override fun onItemClick(position: Int) {
                                val artistId = artists.get(position).idArtist
                                setFragmentResult("requestKeyArtistIdSearchView", bundleOf("bundleKeyArtistIdSearchView" to artistId))
                                setFragmentResult("requestKeyArtistNameSearchView", bundleOf("bundleKeyArtistNameSearchView" to query))
                                findNavController().navigate(
                                    RechercheDirections.actionRecherche2ToDetailArtiste4()
                                )
                            }
                        })

                    } catch(e: Exception) {
                        artistTextView.isVisible = false
                        recyclerViewArtist.isVisible = false
                        Log.d("CATCH DISPLAY ARTISTS", e.message.toString())
                    }
                }
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                return false
            }
        })
    }
}