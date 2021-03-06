package com.ysifre.android_project.vue

import android.graphics.drawable.Drawable
import android.os.Build
import android.os.Bundle
import android.text.method.ScrollingMovementMethod
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.setFragmentResult
import androidx.fragment.app.setFragmentResultListener
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.CustomTarget
import com.bumptech.glide.request.target.SimpleTarget
import com.bumptech.glide.request.transition.Transition
import com.ysifre.android_project.GetAlbumByIdNetwork
import com.ysifre.android_project.GetAlbumsFromArtistAndAblbumNamesNetwork
import com.ysifre.android_project.R
import com.ysifre.android_project.adapter.AdapterPopularTracks
import com.ysifre.android_project.adapter.ItemsPopularTrackModel
import com.ysifre.android_project.model.GetTrackByAlbumIdNetwork
import com.ysifre.android_project.model.Track
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.util.*
import kotlin.collections.ArrayList
import kotlin.properties.Delegates


class DetailAlbum : Fragment() {
    lateinit var backButton: ImageView
    lateinit var albumCover: ImageView
    lateinit var artistNameTextView: TextView
    lateinit var albumNameTextView: TextView
    lateinit var nbTracks: TextView
    lateinit var releaseDate: TextView
    lateinit var note: TextView
    lateinit var nbVotes: TextView
    lateinit var descAlbum: TextView
    lateinit var tracksRecyclerView: RecyclerView
    var albumId by Delegates.notNull<Int>()
    lateinit var artistId: String
    lateinit var artistName: String
    lateinit var albumName: String
    lateinit var layoutImage: ConstraintLayout

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.album, container, false).apply {
            layoutImage = findViewById(R.id.relativeLayout2)
            backButton = findViewById(R.id.buttonBackAlbum)
            albumCover = findViewById(R.id.albumCoverImage)
            artistNameTextView = findViewById(R.id.artistNameAlbumText)
            albumNameTextView = findViewById(R.id.albumNameText)
            nbTracks = findViewById(R.id.nbTracksAlbumText)
            releaseDate = findViewById(R.id.releaseDateAlbum)
            note = findViewById(R.id.note)
            nbVotes = findViewById(R.id.nbVotes)
            descAlbum = findViewById(R.id.descAlbumTextView)
            descAlbum.movementMethod = ScrollingMovementMethod()
            tracksRecyclerView = findViewById(R.id.tracksAlbumRecyclerView)

            tracksRecyclerView.layoutManager = LinearLayoutManager(activity)

            setFragmentResultListener("requestKeyAlbumID") { requestKey, bundle ->
                val result = bundle.getString("bundleKeyAlbumID")
                Log.d("ALBUM ID PARAM", result.toString())
                if (result != null) {
                    albumId = result.toInt()
                    displayInfo(albumId)
                    displayRecyclerView(albumId)
                }
            }

            //nom de l'album recuper?? quand on clique sur un album de la recherche
            setFragmentResultListener("requestKeyAlbumIdSearchView") { requestKey, bundle ->
                val result = bundle.getString("bundleKeyAlbumIdSearchView")
                Log.d("ALBUM ID PARAM SEARCH", result.toString())
                if (result != null) {
                    albumName = result.toString()
                }
            }

            //nom de l'artist recuper?? quand on clique sur un album de la recherche
            setFragmentResultListener("requestKeyArtistNameSearchView") { requestKey, bundle ->
                val result = bundle.getString("bundleKeyArtistNameSearchView")
                Log.d("ALBUM ID PARAM SEARCH", result.toString())
                if (result != null) {
                    artistName = result.toString()
                    displayInfoWithArtistAndAlbumNames(artistName, albumName)
                }
            }

            backButton.setOnClickListener {
                Log.d("ALBUM ID BUTTON CLICK", artistId)
                Log.d("ALBUM ID BUTTON CLICK", artistNameTextView.text.toString())
                setFragmentResult("requestKeyAlbumIdFromDetailAlbum", bundleOf("bundleKeyAlbumIdFromDetailAlbum" to artistId))
                setFragmentResult("artistNameKeyArtistNameFromDetailAlbum", bundleOf("bundleKeyArtistNameAlbumFromDetailAlbum" to artistNameTextView.text.toString()))
                findNavController().navigate(
                    DetailAlbumDirections.actionDetailAlbum6ToDetailArtiste42()
                )
            }
        }
    }

    private fun displayInfoWithArtistAndAlbumNames(artistNameParam: String, albumNameParam: String) {
        GlobalScope.launch(Dispatchers.Main) {
            try {
                val response = GetAlbumsFromArtistAndAblbumNamesNetwork.getAlbumsInfosWithArtistAndAlbum(artistNameParam, albumNameParam)
                Log.d("INFO ALBUM", response.toString())
                if (response != null) {
                    if (response.album != null) {
                        artistId = response.album.get(0).idArtist
                        artistNameTextView.text = response.album.get(0).strArtist
                        albumNameTextView.text = response.album.get(0).strAlbum
                        releaseDate.text = response.album.get(0).intYearReleased
                        note.text = response.album.get(0).intScore
                        nbVotes.text = response.album.get(0).intScoreVotes + " votes"

                        if(response.album.get(0).strDescriptionFR != null && Locale.getDefault().displayLanguage.equals("fran??ais")){
                            descAlbum.text = response.album.get(0).strDescriptionFR
                        } else if(response.album.get(0).strDescriptionES != null && Locale.getDefault().displayLanguage.equals("espa??ol")){
                            descAlbum.text = response.album.get(0).strDescriptionES
                        } else{
                            descAlbum.text = response.album.get(0).strDescriptionEN
                        }

                        Glide.with(this@DetailAlbum).load(response.album.get(0).strAlbumThumb).centerCrop().placeholder(R.drawable.ic_placeholder_artiste).into(albumCover)

                        Glide.with(this@DetailAlbum).load(response.album.get(0).strAlbumThumb).centerCrop().placeholder(R.drawable.dark_placeholder).into(object : CustomTarget<Drawable?>() {
                            override fun onResourceReady(
                                resource: Drawable,
                                transition: Transition<in Drawable?>?
                            ) {
                                layoutImage.background = resource
                            }

                            override fun onLoadCleared(placeholder: Drawable?) {
                            }
                        })

                        displayRecyclerView(response.album.get(0).idAlbum.toInt())
                    }
                }
            } catch(e: Exception) {
                Log.d("CATCH RETURN ARTIST", e.message.toString())
            }
        }
    }

    private fun displayInfo(albumId: Int) {
        GlobalScope.launch(Dispatchers.Main) {
            try {
                val response = GetAlbumByIdNetwork.findAlbumById(albumId)
                Log.d("INFO ALBUM", response.toString())
                if (response != null) {
                    if (response.album != null) {
                        artistId = response.album.get(0).idArtist
                        artistNameTextView.text = response.album.get(0).strArtist
                        albumNameTextView.text = response.album.get(0).strAlbum
                        releaseDate.text = response.album.get(0).intYearReleased
                        note.text = response.album.get(0).intScore
                        nbVotes.text = response.album.get(0).intScoreVotes + " votes"
                        descAlbum.text = response.album.get(0).strDescriptionEN
                        Glide.with(this@DetailAlbum).load(response.album.get(0).strAlbumThumb).centerCrop().placeholder(R.drawable.ic_placeholder_album).into(albumCover)

                        Glide.with(this@DetailAlbum).load(response.album.get(0).strAlbumThumb).centerCrop().placeholder(R.drawable.dark_placeholder).into(object : CustomTarget<Drawable?>() {
                            override fun onResourceReady(
                                resource: Drawable,
                                transition: Transition<in Drawable?>?
                            ) {
                                layoutImage.background = resource
                            }

                            override fun onLoadCleared(placeholder: Drawable?) {
                            }
                        })
                    }
                }
            } catch(e: Exception) {
                Log.d("CATCH RETURN ARTIST", e.message.toString())
            }
        }
    }

    private fun displayRecyclerView(albumId: Int){
        val data = ArrayList<ItemsPopularTrackModel>()
        var tracks: ArrayList<Track> = ArrayList()
        GlobalScope.launch(Dispatchers.Main) {
            try {
                val response = GetTrackByAlbumIdNetwork.findPopularTracks(albumId)
                Log.d("TRACKS ALBUM", response.track.toString())
                if (response.track != null) {
                    for(i in response.track){
                        tracks.add(i)
                    }
                }
                var count = 1
                for (i in tracks) {
                    data.add(ItemsPopularTrackModel(count.toString(), i.strTrack))
                    count += 1
                }
                nbTracks.text = tracks.size.toString() + " chansons"
                val adapter = AdapterPopularTracks(data)
                tracksRecyclerView.adapter = adapter
                adapter.setOnItemClickListener(object: AdapterPopularTracks.OnItemClickListener{
                    override fun onItemClick(position: Int) {
                        //redirection vers paroles
                        Log.d("POSITION CLICKED 1", position.toString())
                    }
                })

            } catch(e: Exception) {
                Log.d("CATCH DISPLAY TRACK", e.message.toString())
            }
        }
    }
}