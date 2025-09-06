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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.compose.ui.platform.LocalContext
import com.furkanbarissonmezisik.memorizewords.ui.viewmodel.HomeViewModel
import com.furkanbarissonmezisik.memorizewords.ui.viewmodel.HomeViewModelFactory
import com.furkanbarissonmezisik.memorizewords.ui.components.AdMobBanner

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddWordsScreen(
    onNavigateToWordList: (Long, Boolean) -> Unit,
    onNavigateBack: () -> Unit,
    viewModel: HomeViewModel = viewModel(factory = HomeViewModelFactory(LocalContext.current))
) {
    val wordLists by viewModel.wordLists.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()
    val errorMessage by viewModel.errorMessage.collectAsState()
    
    var showCreateListDialog by remember { mutableStateOf(false) }
    var newListName by remember { mutableStateOf("") }
    var showDeleteConfirmationDialog by remember { mutableStateOf(false) }
    var listToDelete by remember { mutableStateOf<com.furkanbarissonmezisik.memorizewords.data.entity.WordList?>(null) }
    
    var showEditListDialog by remember { mutableStateOf(false) }
    var editingList by remember { mutableStateOf<com.furkanbarissonmezisik.memorizewords.data.entity.WordList?>(null) }
    var editListName by remember { mutableStateOf("") }
    
    // Word counts for each list
    var wordCounts by remember { mutableStateOf<Map<Long, Int>>(emptyMap()) }
    
    LaunchedEffect(errorMessage) {
        errorMessage?.let {
            // Handle error display if needed
        }
    }
    
    // Load word counts for all lists
    LaunchedEffect(wordLists) {
        if (wordLists.isNotEmpty()) {
            val counts = mutableMapOf<Long, Int>()
            wordLists.forEach { wordList ->
                val count = viewModel.getWordCountForList(wordList.id)
                counts[wordList.id] = count
            }
            wordCounts = counts
        }
    }
    
    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "Create List",
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
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                    titleContentColor = MaterialTheme.colorScheme.onPrimaryContainer,
                    navigationIconContentColor = MaterialTheme.colorScheme.onPrimaryContainer
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
                onClick = { showCreateListDialog = true },
                containerColor = MaterialTheme.colorScheme.primary
            ) {
                Text(
                    text = "+",
                    style = MaterialTheme.typography.headlineMedium,
                    color = MaterialTheme.colorScheme.onPrimary
                )
            }
        }
    ) { paddingValues ->
        when {
            isLoading && wordLists.isEmpty() -> {
                // Sadece ilk yüklemede göster
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(paddingValues),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator()
                }
            }
            wordLists.isEmpty() -> {
                // Liste yoksa mesaj göster
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(paddingValues)
                        .padding(16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Text(
                        text = "No word lists found",
                        style = MaterialTheme.typography.headlineSmall,
                        textAlign = TextAlign.Center,
                        modifier = Modifier.padding(bottom = 16.dp)
                    )

                    Text(
                        text = "Create your first word list to start memorizing words!",
                        style = MaterialTheme.typography.bodyLarge,
                        textAlign = TextAlign.Center,
                        modifier = Modifier.padding(bottom = 32.dp)
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
                        Icon(
                            imageVector = Icons.Default.Add,
                            contentDescription = null,
                            modifier = Modifier.padding(end = 8.dp)
                        )
                        Text(
                            text = "Create New List",
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Medium
                        )
                    }
                }
            }
            else -> {
                // Mevcut listeleri göster
                LazyColumn(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(paddingValues)
                        .padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    items(wordLists) { wordList ->
                        Card(
                            modifier = Modifier.fillMaxWidth(),
                            colors = CardDefaults.cardColors(
                                containerColor = MaterialTheme.colorScheme.surfaceVariant
                            ),
                            onClick = { onNavigateToWordList(wordList.id, false) }
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
                                        style = MaterialTheme.typography.titleMedium,
                                        fontWeight = FontWeight.Medium
                                    )
                                    Text(
                                        text = "Tap to add words",
                                        style = MaterialTheme.typography.bodyMedium,
                                        color = MaterialTheme.colorScheme.onSurfaceVariant
                                    )
                                }

                                Row(
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    // Word count badge
                                    Card(
                                        colors = CardDefaults.cardColors(
                                            containerColor = MaterialTheme.colorScheme.primaryContainer
                                        ),
                                        modifier = Modifier.padding(end = 8.dp)
                                    ) {
                                        Text(
                                            text = "${wordCounts[wordList.id] ?: 0}",
                                            style = MaterialTheme.typography.labelMedium,
                                            color = MaterialTheme.colorScheme.onPrimaryContainer,
                                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                                        )
                                    }
                                    
                                    // Edit button
                                    IconButton(
                                        onClick = { 
                                            editingList = wordList
                                            editListName = wordList.name
                                            showEditListDialog = true
                                        }
                                    ) {
                                        Icon(
                                            imageVector = Icons.Default.Edit,
                                            contentDescription = "Edit list",
                                            tint = MaterialTheme.colorScheme.primary
                                        )
                                    }
                                    
                                    // Delete button
                                    IconButton(
                                        onClick = { 
                                            listToDelete = wordList
                                            showDeleteConfirmationDialog = true
                                        }
                                    ) {
                                        Icon(
                                            imageVector = Icons.Default.Delete,
                                            contentDescription = "Delete list",
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
                            // Navigate to word list with add dialog open
                            onNavigateToWordList(listId, true)
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
    
    // Edit list dialog
    if (showEditListDialog && editingList != null) {
        AlertDialog(
            onDismissRequest = { 
                showEditListDialog = false 
                editingList = null
                editListName = ""
            },
            title = { Text("Edit List") },
            text = {
                OutlinedTextField(
                    value = editListName,
                    onValueChange = { editListName = it },
                    label = { Text("List Name") },
                    modifier = Modifier.fillMaxWidth()
                )
            },
            confirmButton = {
                Button(
                    onClick = {
                        editingList?.let { list ->
                            val updatedList = list.copy(name = editListName.trim())
                            viewModel.updateWordList(updatedList)
                        }
                        showEditListDialog = false
                        editingList = null
                        editListName = ""
                    }
                ) {
                    Text("Update")
                }
            },
            dismissButton = {
                TextButton(
                    onClick = {
                        showEditListDialog = false
                        editingList = null
                        editListName = ""
                    }
                ) {
                    Text("Cancel")
                }
            }
        )
    }
    
    // Delete confirmation dialog
    if (showDeleteConfirmationDialog && listToDelete != null) {
        AlertDialog(
            onDismissRequest = { 
                showDeleteConfirmationDialog = false
                listToDelete = null
            },
            title = { Text("Delete List") },
            text = { 
                Text("Are you sure you want to delete '${listToDelete!!.name}'? This action cannot be undone and will also delete all words in this list.")
            },
            confirmButton = {
                Button(
                    onClick = {
                        viewModel.deleteWordList(listToDelete!!)
                        showDeleteConfirmationDialog = false
                        listToDelete = null
                    },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.error
                    )
                ) {
                    Text("Delete")
                }
            },
            dismissButton = {
                TextButton(
                    onClick = { 
                        showDeleteConfirmationDialog = false
                        listToDelete = null
                    }
                ) {
                    Text("Cancel")
                }
            }
        )
    }
}
