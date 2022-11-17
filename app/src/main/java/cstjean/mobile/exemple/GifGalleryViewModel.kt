package cstjean.mobile.exemple

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import cstjean.mobile.exemple.api.Gif
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import retrofit2.http.Query

class GifGalleryViewModel : ViewModel() {
    private val gifRepository = GifRepository()
    private val preferencesRepository = PreferencesRepository.get()

    private val _uiState: MutableStateFlow<GifGalleryUiState> = MutableStateFlow(GifGalleryUiState())
    val uiState: StateFlow<GifGalleryUiState>
        get() = _uiState.asStateFlow()

    init {
        viewModelScope.launch {
            preferencesRepository.storedQuery.collectLatest { storedQuery ->
                try {
                    val items = fetchGalleryItems(storedQuery)
                    _uiState.update { oldState ->
                        oldState.copy(
                            gifs = items,
                            query = storedQuery
                        )
                    }
                } catch (ex: Exception) {
                    // TODO erreur
                }
            }
        }
    }

    fun setQuery(query: String) {
        viewModelScope.launch { preferencesRepository.setStoredQuery(query)}
    }

    private suspend fun fetchGalleryItems(query: String): List<Gif> {
        return if (query.isNotEmpty()) {
            gifRepository.search(query)
        } else {
            gifRepository.fetchTrending()
        }
    }
}

data class GifGalleryUiState(
    val gifs: List<Gif> = listOf(),
    val query: String = ""
)