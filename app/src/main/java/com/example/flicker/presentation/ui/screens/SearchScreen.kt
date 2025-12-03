package com.example.flicker.presentation.ui.screens

import MovieCard
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.SearchBar
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.flicker.presentation.viewmodel.search.SearchViewModel
import org.koin.androidx.compose.getViewModel
import org.koin.androidx.compose.koinViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchScreen(
    navController: NavController,
    searchViewModel: SearchViewModel= koinViewModel()
) {
    val searchText by searchViewModel.searchText.collectAsState()
    val searchResults by searchViewModel.searchResults.collectAsState()
    val isSearching by searchViewModel.isSearching.collectAsState()

    Column(
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.fillMaxSize()
    ) {
        SearchBar(
            query = searchText,
            onQueryChange = searchViewModel::onSearchTextChange,
            onSearch = { searchViewModel.onSearchTextChange(it) },
            active = isSearching,
            onActiveChange = { },
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = if (isSearching) 0.dp else 10.dp),
            placeholder = { Text("Search movies") },
            leadingIcon = { Icon(Icons.Default.Search, contentDescription = "Search icon") }
        ) {

        }

        LazyVerticalGrid(
            columns = GridCells.Adaptive(120.dp),
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 12.dp),
            contentPadding = PaddingValues(horizontal = 8.dp, vertical = 8.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(searchResults) { movie ->
                MovieCard(movie = movie, navController = navController)
            }
        }
    }
}
