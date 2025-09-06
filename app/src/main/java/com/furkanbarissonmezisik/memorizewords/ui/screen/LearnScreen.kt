package com.furkanbarissonmezisik.memorizewords.ui.screen

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.BorderStroke
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Info
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.res.stringResource
import com.furkanbarissonmezisik.memorizewords.R
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.compose.ui.platform.LocalContext
import com.furkanbarissonmezisik.memorizewords.ui.viewmodel.LearnMode
import com.furkanbarissonmezisik.memorizewords.ui.viewmodel.LearnState
import com.furkanbarissonmezisik.memorizewords.ui.viewmodel.LearnViewModel
import com.furkanbarissonmezisik.memorizewords.ui.viewmodel.LearnViewModelFactory
import com.furkanbarissonmezisik.memorizewords.ui.components.AdMobBanner

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
    
    // State protection - prevent rendering with invalid data
    if (listId <= 0 || mode.isEmpty()) {
        LaunchedEffect(Unit) {
            onNavigateBack()
        }
        return
    }
    
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
    
    // Safe back navigation with state protection
    val safeNavigateBack = {
        if (currentState != LearnState.LOADING) {
            onNavigateBack()
        }
    }
    
    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = when (currentState) {
                            LearnState.LOADING -> stringResource(R.string.loading_word_lists)
                            LearnState.NO_WORDS -> stringResource(R.string.no_words_in_list)
                            LearnState.LEARNING -> stringResource(R.string.learning)
                            LearnState.CORRECT -> stringResource(R.string.correct)
                            LearnState.WRONG -> stringResource(R.string.try_again)
                            LearnState.SHOW_ANSWER -> stringResource(R.string.show)
                            LearnState.COMPLETED -> stringResource(R.string.congratulations)
                        },
                        fontWeight = FontWeight.Bold,
                        fontSize = 20.sp
                    )
                },
                navigationIcon = {
                    IconButton(onClick = safeNavigateBack) {
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
        }
    ) { paddingValues ->
        // State validation before rendering
        if (learnSession == null && currentState == LearnState.LOADING) {
            // Show loading state only when actually loading
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator()
            }
            return@Scaffold
        }
        
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
                        Text(stringResource(R.string.loading_word_lists))
                    }
                }
            }
            
            LearnState.NO_WORDS -> {
                // No words in list screen with better UX
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(paddingValues)
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
                        text = stringResource(R.string.no_words_in_list),
                        style = MaterialTheme.typography.headlineMedium,
                        fontWeight = FontWeight.Bold,
                        textAlign = TextAlign.Center,
                        modifier = Modifier.padding(bottom = 16.dp)
                    )
                    
                    Text(
                        text = stringResource(R.string.add_some_words),
                        style = MaterialTheme.typography.bodyLarge,
                        textAlign = TextAlign.Center,
                        modifier = Modifier.padding(bottom = 32.dp),
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    
                    // Action buttons
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        Button(
                            onClick = onNavigateBack,
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(56.dp),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = MaterialTheme.colorScheme.primary
                            )
                        ) {
                            Text(
                                stringResource(R.string.back_to_home),
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Medium
                            )
                        }
                        

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
                        text = stringResource(R.string.congratulations),
                        style = MaterialTheme.typography.headlineLarge,
                        fontWeight = FontWeight.Bold,
                        textAlign = TextAlign.Center,
                        modifier = Modifier.padding(bottom = 16.dp)
                    )
                    
                    Text(
                        text = stringResource(R.string.you_completed),
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
                        Text(stringResource(R.string.back_to_home))
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
                            text = stringResource(R.string.completed_percentage, (session.progress * 100).toInt()),
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
                                    LearnState.SHOW_ANSWER -> stringResource(R.string.show)
                                    else -> stringResource(R.string.question)
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
                                        LearnMode.WORD_TO_MEANING -> stringResource(R.string.type_the_word)
                                        LearnMode.MEANING_TO_WORD -> stringResource(R.string.type_the_word)
                                        else -> stringResource(R.string.type_the_word)
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
                                    text = stringResource(R.string.correct),
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
                                    text = stringResource(R.string.incorrect),
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
                                    modifier = Modifier.weight(1f),
                                    colors = ButtonDefaults.outlinedButtonColors(
                                        contentColor = Color.Red,
                                        containerColor = Color.Transparent
                                    ),
                                    border = BorderStroke(1.dp, Color.Red)
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.Close,
                                        contentDescription = null,
                                        modifier = Modifier.padding(end = 4.dp)
                                    )
                                    Text(stringResource(R.string.skip))
                                }
                                
                                // Check button
                                Button(
                                    onClick = { viewModel.checkAnswer() },
                                    modifier = Modifier.weight(1f),
                                    enabled = userInput.isNotBlank(),
                                    colors = ButtonDefaults.buttonColors(
                                        containerColor = Color(0xFF4CAF50), // Green
                                        contentColor = Color.White
                                    )
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.Check,
                                        contentDescription = null,
                                        modifier = Modifier.padding(end = 4.dp)
                                    )
                                    Text(stringResource(R.string.check))
                                }
                                
                                // Show Answer button
                                OutlinedButton(
                                    onClick = { viewModel.showAnswer() },
                                    modifier = Modifier.weight(1f),
                                    colors = ButtonDefaults.outlinedButtonColors(
                                        contentColor = Color(0xFFFFC107), // Yellow/Amber
                                        containerColor = Color.Transparent
                                    ),
                                    border = BorderStroke(1.dp, Color(0xFFFFC107))
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.Info,
                                        contentDescription = null,
                                        modifier = Modifier.padding(end = 4.dp)
                                    )
                                    Text(stringResource(R.string.show))
                                }
                            }
                            
                            LearnState.CORRECT -> {
                                Button(
                                    onClick = { viewModel.nextWord() },
                                    modifier = Modifier.fillMaxWidth()
                                ) {
                                    Text(stringResource(R.string.next))
                                }
                            }
                            
                            LearnState.WRONG -> {
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                                ) {
                                    OutlinedButton(
                                        onClick = { viewModel.showAnswer() },
                                        modifier = Modifier.weight(1f),
                                        colors = ButtonDefaults.outlinedButtonColors(
                                            contentColor = Color(0xFFFFC107), // Yellow/Amber
                                            containerColor = Color.Transparent
                                        ),
                                        border = BorderStroke(1.dp, Color(0xFFFFC107))
                                    ) {
                                        Icon(
                                            imageVector = Icons.Default.Info,
                                            contentDescription = null,
                                            modifier = Modifier.padding(end = 4.dp)
                                        )
                                        Text(stringResource(R.string.show))
                                    }
                                    
                                    Button(
                                        onClick = { viewModel.retryWord() },
                                        modifier = Modifier.weight(1f)
                                    ) {
                                        Text(stringResource(R.string.try_again))
                                    }
                                }
                            }
                            
                            LearnState.SHOW_ANSWER -> {
                                Button(
                                    onClick = { viewModel.nextWord() },
                                    modifier = Modifier.fillMaxWidth()
                                ) {
                                    Text(stringResource(R.string.next))
                                }
                            }
                            
                            else -> {}
                        }
                    }
                    
                    // AdMob Banner at the bottom
                    AdMobBanner(
                        modifier = Modifier.padding(top = 16.dp)
                    )
                }
            }
        }
    }
}
