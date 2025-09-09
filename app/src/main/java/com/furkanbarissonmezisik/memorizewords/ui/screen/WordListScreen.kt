package com.furkanbarissonmezisik.memorizewords.ui.screen

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.compose.ui.platform.LocalContext
import com.furkanbarissonmezisik.memorizewords.ui.viewmodel.WordListViewModel
import com.furkanbarissonmezisik.memorizewords.ui.viewmodel.WordListViewModelFactory
import com.furkanbarissonmezisik.memorizewords.ui.components.AdMobBanner

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun WordListScreen(
    listId: Long,
    onNavigateBack: () -> Unit,
    openAddDialog: Boolean = false,
    viewModel: WordListViewModel = viewModel(factory = WordListViewModelFactory(LocalContext.current, listId))
) {
    val wordList by viewModel.wordList.collectAsState()
    val words by viewModel.words.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()
    val errorMessage by viewModel.errorMessage.collectAsState()
    

    
    var showAddWordDialog by remember { mutableStateOf(openAddDialog) }
    var newWord by remember { mutableStateOf("") }
    var newMeaning by remember { mutableStateOf("") }
    
    var showEditWordDialog by remember { mutableStateOf(false) }
    var editingWord by remember { mutableStateOf<com.furkanbarissonmezisik.memorizewords.data.entity.Word?>(null) }
    var editWordText by remember { mutableStateOf("") }
    var editMeaningText by remember { mutableStateOf("") }
    
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
                        text = wordList?.name ?: "Word List",
                        fontWeight = FontWeight.Bold,
                        fontSize = 20.sp
                    )
                },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(
                            imageVector = Icons.Default.ArrowBack,
                            contentDescription = "Back"
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color.Transparent,
                    titleContentColor = MaterialTheme.colorScheme.onBackground,
                    navigationIconContentColor = MaterialTheme.colorScheme.onBackground
                )
            )
        },
        bottomBar = {
            // AdMob Banner with padding to avoid system navigation bar
            Column {
                AdMobBanner()
                Spacer(modifier = Modifier.height(80.dp)) // Extra space for navigation bar
            }
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = { showAddWordDialog = true },
                containerColor = MaterialTheme.colorScheme.primary,
                contentColor = Color.White
            ) {
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = "Add Word",
                    tint = MaterialTheme.colorScheme.onPrimary
                )
            }
        }
    ) { paddingValues ->
        if (isLoading) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator()
            }
        } else {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
                    .padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                if (words.isEmpty()) {
                    item {
                        Card(
                            modifier = Modifier.fillMaxWidth(),
                            colors = CardDefaults.cardColors(
                                containerColor = MaterialTheme.colorScheme.surfaceVariant
                            )
                        ) {
                            Column(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(32.dp),
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                Text(
                                    text = "No words in this list yet",
                                    style = MaterialTheme.typography.titleMedium,
                                    fontWeight = FontWeight.Medium
                                )
                                Spacer(modifier = Modifier.height(8.dp))
                                Text(
                                    text = "Tap the + button below to add your first word",
                                    style = MaterialTheme.typography.bodyMedium,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                                Spacer(modifier = Modifier.height(16.dp))
                                Button(
                                    onClick = { showAddWordDialog = true },
                                    modifier = Modifier.fillMaxWidth()
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.Add,
                                        contentDescription = null,
                                        modifier = Modifier.padding(end = 8.dp)
                                    )
                                    Text("Add First Word")
                                }
                            }
                        }
                    }
                } else {
                    items(words) { word ->
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
                                        text = word.word,
                                        style = MaterialTheme.typography.titleMedium,
                                        fontWeight = FontWeight.Medium
                                    )
                                    Spacer(modifier = Modifier.height(4.dp))
                                    Text(
                                        text = word.meaning,
                                        style = MaterialTheme.typography.bodyMedium,
                                        color = MaterialTheme.colorScheme.onSurfaceVariant
                                    )
                                }
                                
                                Row {
                                    IconButton(
                                        onClick = { 
                                            editingWord = word
                                            editWordText = word.word
                                            editMeaningText = word.meaning
                                            showEditWordDialog = true
                                        }
                                    ) {
                                        Icon(
                                            imageVector = Icons.Default.Edit,
                                            contentDescription = "Edit word",
                                            tint = MaterialTheme.colorScheme.primary
                                        )
                                    }
                                    
                                    IconButton(
                                        onClick = { viewModel.deleteWord(word) }
                                    ) {
                                        Icon(
                                            imageVector = Icons.Default.Delete,
                                            contentDescription = "Delete word",
                                            tint = MaterialTheme.colorScheme.error
                                        )
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }
    
    // Add word dialog
    if (showAddWordDialog) {
        AlertDialog(
            onDismissRequest = { showAddWordDialog = false },
            title = { Text("Add New Word") },
            text = {
                Column {
                    OutlinedTextField(
                        value = newWord,
                        onValueChange = { newWord = it },
                        label = { Text("Word") },
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(bottom = 8.dp)
                    )
                    OutlinedTextField(
                        value = newMeaning,
                        onValueChange = { newMeaning = it },
                        label = { Text("Meaning") },
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        viewModel.addWord(newWord, newMeaning)
                        showAddWordDialog = false
                        newWord = ""
                        newMeaning = ""
                    }
                ) {
                    Text("Add")
                }
            },
            dismissButton = {
                TextButton(
                    onClick = {
                        showAddWordDialog = false
                        newWord = ""
                        newMeaning = ""
                    }
                ) {
                    Text("Cancel")
                }
            }
        )
    }

    // Edit word dialog
    if (showEditWordDialog && editingWord != null) {
        AlertDialog(
            onDismissRequest = { 
                showEditWordDialog = false 
                editingWord = null
                editWordText = ""
                editMeaningText = ""
            },
            title = { Text("Edit Word") },
            text = {
                Column {
                    OutlinedTextField(
                        value = editWordText,
                        onValueChange = { editWordText = it },
                        label = { Text("Word") },
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(bottom = 8.dp)
                    )
                    OutlinedTextField(
                        value = editMeaningText,
                        onValueChange = { editMeaningText = it },
                        label = { Text("Meaning") },
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        editingWord?.let { word ->
                            val updatedWord = word.copy(
                                word = editWordText.trim(),
                                meaning = editMeaningText.trim()
                            )
                            viewModel.updateWord(updatedWord)
                        }
                        showEditWordDialog = false
                        editingWord = null
                        editWordText = ""
                        editMeaningText = ""
                    }
                ) {
                    Text("Update")
                }
            },
            dismissButton = {
                TextButton(
                    onClick = {
                        showEditWordDialog = false
                        editingWord = null
                        editWordText = ""
                        editMeaningText = ""
                    }
                ) {
                    Text("Cancel")
                }
            }
        )
    }
}
