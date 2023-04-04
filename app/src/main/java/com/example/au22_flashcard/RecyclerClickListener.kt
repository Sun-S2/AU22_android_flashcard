package com.example.au22_flashcard
//Credits to https://github.com/johncodeos-blog/RoomExample used as guide for this project room databas uppgift
//Other helpful links for ideas
// https://github.com/Cleveroad/AdaptiveTableLayout
//https://github.com/android/sunflower/tree/main


interface RecyclerClickListener {
    fun onItemRemoveClick(position: Int)
    fun onItemClick(position: Int)
}