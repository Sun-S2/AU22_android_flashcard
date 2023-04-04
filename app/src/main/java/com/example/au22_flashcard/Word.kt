package com.example.au22_flashcard

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.*

@Entity(tableName = "word_table")
class Word(
    @PrimaryKey(autoGenerate = true) val id: Int,
    @ColumnInfo(name = "dateAdded")
    val dateAdded: Date,
    @ColumnInfo(name = "english") var english : String,
    @ColumnInfo(name = "swedish") var swedish : String)