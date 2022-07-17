package com.ysifre.android_project.vue

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.ysifre.android_project.R

class Favoris : Fragment (){

    lateinit var homeButton: ImageView
    lateinit var searchButton: ImageView

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.favoris, container, false).apply {
            homeButton = findViewById(R.id.homeButtonFav)
            searchButton = findViewById(R.id.search_buttonFav)

            homeButton.setOnClickListener {findNavController().navigate( FavorisDirections.actionFavoris2ToClassement2()) }
            searchButton.setOnClickListener { findNavController().navigate(FavorisDirections.actionFavoris2ToRecherche2()) }
        }
    }
}