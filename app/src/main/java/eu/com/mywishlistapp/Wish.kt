package eu.com.mywishlistapp

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "wish_table")
data class Wish(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0L,
    @ColumnInfo(name = "wish-title")
    val title : String = "",
    @ColumnInfo(name = "wish-desc")
    val description : String= ""
)

object DummyWish{
    val wishList = listOf(
        Wish(
            title = "Apple",
            description = "Watch 6"
        ),
        Wish(
            title = "Samsung",
            description = "9 pro"
        ),
        Wish(
            title = "Google",
            description = "X series"
        )
    )
}