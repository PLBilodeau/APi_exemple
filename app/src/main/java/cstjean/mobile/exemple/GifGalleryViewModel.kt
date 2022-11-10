package cstjean.mobile.exemple

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import cstjean.mobile.exemple.api.Gif
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class GifGalleryViewModel : ViewModel() {
    private val gifRepository = GifRepository()

    private val _galleryItems: MutableStateFlow<List<Gif>> = MutableStateFlow(emptyList())
    val galleryItems: StateFlow<List<Gif>>
        get() = _galleryItems.asStateFlow()

    init {
        viewModelScope.launch {
            try {
                val items = gifRepository.fetchTrending()
                _galleryItems.value = items
            } catch (ex: Exception) {
                // TODO erreur
            }
        }
    }
}