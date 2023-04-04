package com.example.au22_flashcard
//Credits to https://github.com/johncodeos-blog/RoomExample used as guide for this project room databas uppgift
//Other helpful links for ideas
// https://github.com/Cleveroad/AdaptiveTableLayout
//https://github.com/android/sunflower/tree/main


import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface WordDao {

    //@Insert
    //fun insert(word: Word)

    //Skapa fun för
    // delete
    // getAllwords
    // Annat

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun addWord(word: Word)

    @Query("SELECT * FROM word_table ORDER BY dateAdded DESC")
    fun getWords(): Flow<List<Word>>

    @Update
    suspend fun updateWord(word: Word)

    @Delete
    suspend fun deleteWord(word: Word)
    //SELECT * FROM word_table DELETE  FROM word_table WHERE id = 4
}