package com.furkanbarissonmezisik.memorizewords.ui.screen

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Info
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
import com.furkanbarissonmezisik.memorizewords.ui.viewmodel.LearnMode
import com.furkanbarissonmezisik.memorizewords.ui.viewmodel.LearnState
import com.furkanbarissonmezisik.memorizewords.ui.viewmodel.LearnViewModel
import com.furkanbarissonmezisik.memorizewords.ui.viewmodel.LearnViewModelFactory

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LearnScreen(
    listId: Long,
    mode: String,
    onNavigateBack: () -> Unit,
    viewModel: LearnViewModel = viewModel(factory = LearnViewModelFactory(LocalContext.current, listId))
) {
    val learnSession by viewModel.learnSession.collectAsState()
    val currentState by viewModel.currentState.collectAsState()
    val userInput by viewModel.userInput.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()
    val errorMessage by viewModel.errorMessage.collectAsState()
    
    var selectedMode by remember { mutableStateOf<LearnMode?>(null) }
    
    LaunchedEffect(errorMessage) {
        errorMessage?.let {
            // Handle error display if needed
        }
    }
    
    // Parse the mode parameter and set selectedMode
    LaunchedEffect(mode) {
        selectedMode = try {
            LearnMode.valueOf(mode)
        } catch (e: IllegalArgumentException) {
            LearnMode.WORD_TO_MEANING // Default fallback
        }
    }
    
    // Start learning when mode is selected
    LaunchedEffect(selectedMode) {
        selectedMode?.let { mode ->
            viewModel.startLearning(mode)
        }
    }
    
    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = when (currentState) {
                            LearnState.LOADING -> "Loading..."
                            LearnState.NO_WORDS -> "No Words"
                            LearnState.LEARNING -> "Learning"
                            LearnState.CORRECT -> "Correct!"
                            LearnState.WRONG -> "Try Again"
                            LearnState.SHOW_ANSWER -> "Answer"
                            LearnState.COMPLETED -> "Completed!"
                        },
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
        }
    ) { paddingValues ->
        when (currentState) {
            LearnState.LOADING -> {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(paddingValues),
                    contentAlignment = Alignment.Center
                ) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        CircularProgressIndicator()
                        Spacer(modifier = Modifier.height(16.dp))
                        Text("Loading words...")
                    }
                }
            }
            
            LearnState.NO_WORDS -> {
                // No words in list screen
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(paddingValues)
                        .padding(16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Text(
                        text = "📝 No Words Found",
                        style = MaterialTheme.typography.headlineLarge,
                        fontWeight = FontWeight.Bold,
                        textAlign = TextAlign.Center,
                        modifier = Modifier.padding(bottom = 16.dp)
                    )
                    
                    Text(
                        text = "This word list is empty. Please add some words first.",
                        style = MaterialTheme.typography.bodyLarge,
                        textAlign = TextAlign.Center,
                        modifier = Modifier.padding(bottom = 32.dp)
                    )
                    
                    Button(
                        onClick = onNavigateBack,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(56.dp)
                    ) {
                        Text("Back to Home")
                    }
                }
            }
            
            LearnState.COMPLETED -> {
                // Congratulations screen
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(paddingValues)
                        .padding(16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Text(
                        text = "🎉 Congratulations! 🎉",
                        style = MaterialTheme.typography.headlineLarge,
                        fontWeight = FontWeight.Bold,
                        textAlign = TextAlign.Center,
                        modifier = Modifier.padding(bottom = 16.dp)
                    )
                    
                    Text(
                        text = "You've completed the learning session!",
                        style = MaterialTheme.typography.bodyLarge,
                        textAlign = TextAlign.Center,
                        modifier = Modifier.padding(bottom = 32.dp)
                    )
                    
                    Button(
                        onClick = onNavigateBack,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(56.dp)
                    ) {
                        Text("Back to Home")
                    }
                }
            }
            
            else -> {
                // Learning interface
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(paddingValues)
                        .padding(16.dp)
                ) {
                    // Progress bar
                    learnSession?.let { session ->
                        LinearProgressIndicator(
                            progress = { session.progress },
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(bottom = 16.dp)
                        )
                        
                        Text(
                            text = "${(session.progress * 100).toInt()}% completed",
                            style = MaterialTheme.typography.bodyMedium,
                            modifier = Modifier.padding(bottom = 24.dp)
                        )
                    }
                    
                    // Question card
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(bottom = 24.dp),
                        colors = CardDefaults.cardColors(
                            containerColor = MaterialTheme.colorScheme.surfaceVariant
                        )
                    ) {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(24.dp),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Text(
                                text = when (currentState) {
                                    LearnState.SHOW_ANSWER -> "Answer:"
                                    else -> "Question:"
                                },
                                style = MaterialTheme.typography.titleMedium,
                                color = MaterialTheme.colorScheme.onSurfaceVariant,
                                modifier = Modifier.padding(bottom = 8.dp)
                            )
                            
                            Text(
                                text = when (currentState) {
                                    LearnState.SHOW_ANSWER -> viewModel.getCurrentAnswer()
                                    else -> viewModel.getCurrentQuestion()
                                },
                                style = MaterialTheme.typography.headlineMedium,
                                fontWeight = FontWeight.Bold,
                                textAlign = TextAlign.Center
                            )
                        }
                    }
                    
                    // Input field (only show during learning)
                    if (currentState == LearnState.LEARNING) {
                        OutlinedTextField(
                            value = userInput,
                            onValueChange = { viewModel.updateUserInput(it) },
                            label = { 
                                Text(
                                    when (selectedMode) {
                                        LearnMode.WORD_TO_MEANING -> "Type the meaning"
                                        LearnMode.MEANING_TO_WORD -> "Type the word"
                                        else -> "Type your answer"
                                    }
                                )
                            },
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(bottom = 24.dp)
                        )
                    }
                    
                    // Result message
                    when (currentState) {
                        LearnState.CORRECT -> {
                            Card(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(bottom = 24.dp),
                                colors = CardDefaults.cardColors(
                                    containerColor = MaterialTheme.colorScheme.primaryContainer
                                )
                            ) {
                                Text(
                                    text = "Correct! 🎉",
                                    style = MaterialTheme.typography.titleLarge,
                                    fontWeight = FontWeight.Bold,
                                    color = MaterialTheme.colorScheme.onPrimaryContainer,
                                    textAlign = TextAlign.Center,
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(16.dp)
                                )
                            }
                        }
                        LearnState.WRONG -> {
                            Card(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(bottom = 24.dp),
                                colors = CardDefaults.cardColors(
                                    containerColor = MaterialTheme.colorScheme.errorContainer
                                )
                            ) {
                                Text(
                                    text = "Wrong, try again!",
                                    style = MaterialTheme.typography.titleLarge,
                                    fontWeight = FontWeight.Bold,
                                    color = MaterialTheme.colorScheme.onErrorContainer,
                                    textAlign = TextAlign.Center,
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(16.dp)
                                )
                            }
                        }
                        else -> {}
                    }
                    
                    Spacer(modifier = Modifier.weight(1f))
                    
                    // Action buttons
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        when (currentState) {
                            LearnState.LEARNING -> {
                                // Skip button
                                OutlinedButton(
                                    onClick = { viewModel.skipWord() },
                                    modifier = Modifier.weight(1f)
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.Close,
                                        contentDescription = null,
                                        modifier = Modifier.padding(end = 4.dp)
                                    )
                                    Text("Skip")
                                }
                                
                                // Check button
                                Button(
                                    onClick = { viewModel.checkAnswer() },
                                    modifier = Modifier.weight(1f),
                                    enabled = userInput.isNotBlank()
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.Check,
                                        contentDescription = null,
                                        modifier = Modifier.padding(end = 4.dp)
                                    )
                                    Text("Check")
                                }
                                
                                // Show Answer button
                                OutlinedButton(
                                    onClick = { viewModel.showAnswer() },
                                    modifier = Modifier.weight(1f)
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.Info,
                                        contentDescription = null,
                                        modifier = Modifier.padding(end = 4.dp)
                                    )
                                    Text("Show")
                                }
                            }
                            
                            LearnState.CORRECT -> {
                                Button(
                                    onClick = { viewModel.nextWord() },
                                    modifier = Modifier.fillMaxWidth()
                                ) {
                                    Text("Next Word")
                                }
                            }
                            
                            LearnState.WRONG -> {
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                                ) {
                                    OutlinedButton(
                                        onClick = { viewModel.showAnswer() },
                                        modifier = Modifier.weight(1f)
                                    ) {
                                        Icon(
                                            imageVector = Icons.Default.Info,
                                            contentDescription = null,
                                            modifier = Modifier.padding(end = 4.dp)
                                        )
                                        Text("Show Answer")
                                    }
                                    
                                    Button(
                                        onClick = { viewModel.retryWord() },
                                        modifier = Modifier.weight(1f)
                                    ) {
                                        Text("Try Again")
                                    }
                                }
                            }
                            
                            LearnState.SHOW_ANSWER -> {
                                Button(
                                    onClick = { viewModel.nextWord() },
                                    modifier = Modifier.fillMaxWidth()
                                ) {
                                    Text("Next Word")
                                }
                            }
                            
                            else -> {}
                        }
                    }
                }
            }
        }
    }
}
