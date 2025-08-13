package eu.com.mywishlistapp

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch

class WishViewModel(
    private val wishRepository: WishRepository
) : ViewModel() {
    var wishTitleState by mutableStateOf("")
    var wishDescriptionState by mutableStateOf("")

    fun onWishTitleChange(newTitle: String) {
        wishTitleState = newTitle

    }

    fun onWishDescriptionChange(newDescription: String) {
        wishDescriptionState = newDescription

    }
    lateinit var getAllWish: Flow<List<Wish>>

    init {
        viewModelScope.launch {
            getAllWish = wishRepository.getAllWish()
        }
    }
        fun addWish(wish: Wish) {
            viewModelScope.launch(Dispatchers.IO) {
                wishRepository.addWish(wish = wish)
        }
    }
    fun getAWishesByID(id: Long): Flow<Wish> {
        return wishRepository.getAWishesByID(id)
    }

    fun deleteAWish(wish: Wish) {
        viewModelScope.launch(Dispatchers.IO) {
            wishRepository.deleteAWish(wish = wish)
        }
        fun updateAWish(wish: Wish) {
            viewModelScope.launch(Dispatchers.IO) {
                wishRepository.updateAWish(wish = wish)
            }
        }
    }
}