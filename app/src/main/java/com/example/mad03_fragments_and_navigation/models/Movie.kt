package com.example.mad03_fragments_and_navigation.models

import androidx.room.*
import com.example.mad03_fragments_and_navigation.R

@Entity(
    tableName = "movie_favorites_table",
    indices = [Index("id")]
)
data class Movie(
    @ColumnInfo                             // standard column declaration -> column name will be name of variable
    var title: String = "",
    @ColumnInfo(name = "movie_description") // you can specify column names yourself
    var description: String = ""
) {
    @PrimaryKey(autoGenerate = true)        // marks a variable as primary key
    var id: Long? = 0L

    var rating: Float = 0.0F
        set(value) {
            if(value in 0.0..5.0) field = value
            else throw IllegalArgumentException("Rating value must be between 0 and 5")
        }

    var imageId: Int = R.drawable.no_preview_3
    var note: String = ""

    @Ignore
    var actors: MutableList<String> = mutableListOf()
    @Ignore
    var creators: MutableList<String> = mutableListOf()
    @Ignore
    var genres: List<String>? = null
}
