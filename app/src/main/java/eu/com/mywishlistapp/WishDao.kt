package eu.com.mywishlistapp

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow
@Dao

abstract class  WishDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)

    abstract suspend fun addAWish(wishEntity: Wish)
    // load all wishes from wish table

    @Query("Select * from `wish_table`")

    abstract  fun getAllWish(): Flow <List<Wish>>

    @Update
    abstract suspend fun updateAWish(wishEntity: Wish)

    @Delete
    abstract suspend fun deleteAWish(wishEntity: Wish)

    @Query("Select * from `wish_table` where id = :id")

    abstract fun getAWishesByID(id: Long): Flow<Wish>



}