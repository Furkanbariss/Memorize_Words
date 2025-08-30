package com.furkanbarissonmezisik.memorizewords.ui.screen

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Settings

import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.compose.ui.platform.LocalContext
import com.furkanbarissonmezisik.memorizewords.ui.viewmodel.HomeViewModel
import com.furkanbarissonmezisik.memorizewords.ui.viewmodel.HomeViewModelFactory
import androidx.compose.ui.res.stringResource
import com.furkanbarissonmezisik.memorizewords.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    onNavigateToAddWords: () -> Unit,
    onNavigateToLearn: (Long) -> Unit,
    onNavigateToWordList: (Long) -> Unit,
    onNavigateToSettings: () -> Unit,
    onNavigateToInfo: () -> Unit,
    viewModel: HomeViewModel = viewModel(factory = HomeViewModelFactory(LocalContext.current))
) {
    val wordLists by viewModel.wordLists.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()
    val errorMessage by viewModel.errorMessage.collectAsState()
    
    var showCreateListDialog by remember { mutableStateOf(false) }
    var newListName by remember { mutableStateOf("") }
    var showListSelectionDialog by remember { mutableStateOf(false) }
    
    LaunchedEffect(errorMessage) {
        errorMessage?.let {
            // Handle error display if needed
        }
    }
    
    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "Word Memorizer",
                        fontWeight = FontWeight.Bold,
                        fontSize = 24.sp
                    )
                },
                actions = {
                    IconButton(onClick = onNavigateToInfo) {
                        Icon(
                            imageVector = Icons.Default.Info,
                            contentDescription = "Info",
                            tint = MaterialTheme.colorScheme.onPrimaryContainer
                        )
                    }
                    IconButton(onClick = onNavigateToSettings) {
                        Icon(
                            imageVector = Icons.Default.Settings,
                            contentDescription = "Settings",
                            tint = MaterialTheme.colorScheme.onPrimaryContainer
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                    titleContentColor = MaterialTheme.colorScheme.onPrimaryContainer
                )
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp)
        ) {
            // Welcome text
            Text(
                text = stringResource(R.string.welcome_message),
                style = MaterialTheme.typography.headlineMedium,
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(bottom = 24.dp)
            )
            
            // Create List button
            Button(
                onClick = onNavigateToAddWords,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp)
                    .padding(bottom = 16.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.primary
                )
            ) {
                Text(
                    text = stringResource(R.string.create_list),
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Medium
                )
            }
            

            

            
            // Show loading state or lists
            when {
                isLoading -> {
                    // Show loading state
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(32.dp),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    ) {
                        CircularProgressIndicator(
                            modifier = Modifier.padding(bottom = 16.dp)
                        )
                        Text(
                            text = stringResource(R.string.loading_word_lists),
                            style = MaterialTheme.typography.bodyLarge,
                            textAlign = TextAlign.Center,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
                wordLists.isNotEmpty() -> {
                    // Show available lists
                    Text(
                        text = stringResource(R.string.available_lists),
                        style = MaterialTheme.typography.titleMedium,
                        modifier = Modifier.padding(bottom = 8.dp)
                    )
                    
                    LazyColumn(
                        modifier = Modifier.weight(1f),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        items(wordLists) { wordList ->
                            Card(
                                modifier = Modifier.fillMaxWidth(),
                                colors = CardDefaults.cardColors(
                                    containerColor = MaterialTheme.colorScheme.surfaceVariant
                                )
                            ) {
                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(16.dp),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Column(
                                        modifier = Modifier.weight(1f)
                                    ) {
                                        Text(
                                            text = wordList.name,
                                            style = MaterialTheme.typography.bodyLarge
                                        )
                                        Text(
                                            text = stringResource(R.string.tap_to_view_words),
                                            style = MaterialTheme.typography.bodySmall,
                                            color = MaterialTheme.colorScheme.onSurfaceVariant
                                        )
                                    }
                                    Row {
                                        Button(
                                            onClick = { onNavigateToLearn(wordList.id) },
                                            colors = ButtonDefaults.buttonColors(
                                                containerColor = MaterialTheme.colorScheme.tertiary
                                            ),
                                            modifier = Modifier.padding(end = 8.dp)
                                        ) {
                                            Text(stringResource(R.string.learn))
                                        }
                                        Button(
                                            onClick = { onNavigateToWordList(wordList.id) },
                                            colors = ButtonDefaults.buttonColors(
                                                containerColor = MaterialTheme.colorScheme.primary
                                            )
                                        ) {
                                            Text(stringResource(R.string.view))
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
                else -> {
                    // No lists message (only when not loading and no lists)
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(32.dp),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    ) {
                        // Icon or emoji
                        Text(
                            text = "📚",
                            style = MaterialTheme.typography.displayLarge,
                            modifier = Modifier.padding(bottom = 16.dp)
                        )
                        
                        Text(
                            text = stringResource(R.string.no_word_lists_found),
                            style = MaterialTheme.typography.headlineMedium,
                            fontWeight = FontWeight.Bold,
                            textAlign = TextAlign.Center,
                            modifier = Modifier.padding(bottom = 16.dp)
                        )
                        
                        Text(
                            text = stringResource(R.string.no_lists_message),
                            style = MaterialTheme.typography.bodyLarge,
                            textAlign = TextAlign.Center,
                            modifier = Modifier.padding(bottom = 32.dp),
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                        
                        Button(
                            onClick = { showCreateListDialog = true },
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(56.dp),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = MaterialTheme.colorScheme.primary
                            )
                        ) {
                            Text(
                                stringResource(R.string.create_your_first_list),
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Medium
                            )
                        }
                    }
                }
            }
        }
    }
    
    // Create new list dialog
    if (showCreateListDialog) {
        AlertDialog(
            onDismissRequest = { showCreateListDialog = false },
            title = { Text("Create New List") },
            text = {
                OutlinedTextField(
                    value = newListName,
                    onValueChange = { newListName = it },
                    label = { Text("List Name") },
                    modifier = Modifier.fillMaxWidth()
                )
            },
            confirmButton = {
                Button(
                    onClick = {
                        viewModel.createWordList(newListName) { listId ->
                            showCreateListDialog = false
                            newListName = ""
                            onNavigateToAddWords()
                        }
                    }
                ) {
                    Text("Create")
                }
            },
            dismissButton = {
                TextButton(
                    onClick = {
                        showCreateListDialog = false
                        newListName = ""
                    }
                ) {
                    Text("Cancel")
                }
            }
        )
    }
}
