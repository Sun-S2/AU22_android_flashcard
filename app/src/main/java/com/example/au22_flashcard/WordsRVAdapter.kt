package com.example.au22_flashcard
//Credits to https://github.com/johncodeos-blog/RoomExample used as guide for this project room databas uppgift
//Other helpful links for ideas
// https://github.com/Cleveroad/AdaptiveTableLayout
//https://github.com/android/sunflower/tree/main


import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView

class WordsRVAdapter : ListAdapter<Word, WordsRVAdapter.WordHolder>(DiffCallback()) {

    class WordHolder(view: View) : RecyclerView.ViewHolder(view)

    private lateinit var listener: RecyclerClickListener
    fun setItemListener(listener: RecyclerClickListener) {
        this.listener = listener
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): WordHolder {
        val v =
            LayoutInflater.from(parent.context).inflate(R.layout.words_row, parent, false)
        val wordHolder = WordHolder(v)

        val wordDelete = wordHolder.itemView.findViewById<ImageView>(R.id.word_delete)
        wordDelete.setOnClickListener {
            listener.onItemRemoveClick(wordHolder.adapterPosition)
        }

        val word = wordHolder.itemView.findViewById<CardView>(R.id.word)
        word.setOnClickListener {
            listener.onItemClick(wordHolder.adapterPosition)
        }

        return wordHolder
    }

    override fun onBindViewHolder(holder: WordHolder, position: Int) {
        val currentItem = getItem(position)
        val wordTextEng = holder.itemView.findViewById<TextView>(R.id.word_text_eng)
        wordTextEng.text = currentItem.english
        val wordTextSwe = holder.itemView.findViewById<TextView>(R.id.word_text_swe)
        wordTextSwe.text = currentItem.swedish
        val wordTextId = holder.itemView.findViewById<TextView>(R.id.word_text_id)
        wordTextId.text = currentItem.id.toString()
    }

    class DiffCallback : DiffUtil.ItemCallback<Word>() {
        override fun areItemsTheSame(oldItem: Word, newItem: Word) =
            //oldItem.dateAdded == newItem.dateAdded
            oldItem.dateAdded == newItem.dateAdded

       @SuppressLint("DiffUtilEquals")
        override fun areContentsTheSame(oldItem: Word, newItem: Word) =
            oldItem == newItem
    }
}