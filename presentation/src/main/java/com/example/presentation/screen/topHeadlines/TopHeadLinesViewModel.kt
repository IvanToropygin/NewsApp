package com.example.presentation.screen.topHeadlines

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.domain.entity.Article
import com.example.domain.usecase.news.GetTopHeadlinesUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class TopHeadLinesViewModel @Inject constructor(
    private val getTopHeadlinesUseCase: GetTopHeadlinesUseCase
) : ViewModel() {

    private val _state = MutableStateFlow(TopHeadlinesState())
    val state = _state.asStateFlow()

    init {
        loadTopHeadlines()
    }

    private fun loadTopHeadlines() {
        viewModelScope.launch {
            _state.update { it.copy(isLoading = true, error = null) }

            val articles = getTopHeadlinesUseCase()

            _state.update {
                it.copy(
                    articles = articles,
                    isLoading = false,
                    error = if (articles.isEmpty()) "No articles found" else null
                )
            }
        }
    }

    fun processCommand(command: TopHeadlinesCommand) {
        when (command) {
            TopHeadlinesCommand.RefreshData -> loadTopHeadlines()
        }
    }
}

sealed interface TopHeadlinesCommand {

    data object RefreshData : TopHeadlinesCommand
}

data class TopHeadlinesState(
    val articles: List<Article> = emptyList(),
    val isLoading: Boolean = false,
    val error: String? = null,
)