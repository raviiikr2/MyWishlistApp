package eu.com.mywishlistapp

import kotlinx.coroutines.flow.Flow

class WishRepository(private val wishDao: WishDao) {
    suspend fun addWish(wish: Wish) {
        wishDao.addAWish(wish)

    }
    fun getAllWish(): Flow<List<Wish>> = wishDao.getAllWish()

    fun getAWishesByID(id: Long): Flow<Wish> {
        return wishDao.getAWishesByID(id)
    }
    suspend fun updateAWish(wish: Wish) {
        wishDao.updateAWish(wish)
    }
    suspend fun deleteAWish(wish: Wish) {
        wishDao.deleteAWish(wish)

    }
}