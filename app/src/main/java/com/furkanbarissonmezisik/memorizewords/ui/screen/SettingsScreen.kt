package com.furkanbarissonmezisik.memorizewords.ui.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.res.stringResource
import com.furkanbarissonmezisik.memorizewords.R
import com.furkanbarissonmezisik.memorizewords.ui.theme.ThemeManager
import com.furkanbarissonmezisik.memorizewords.ui.theme.ThemeMode
import com.furkanbarissonmezisik.memorizewords.ui.theme.DarkBackground
import com.furkanbarissonmezisik.memorizewords.ui.theme.DarkOnBackground
import com.furkanbarissonmezisik.memorizewords.ui.theme.LightBackground
import com.furkanbarissonmezisik.memorizewords.ui.theme.LightOnBackground
import com.furkanbarissonmezisik.memorizewords.ui.theme.Purple40
import com.furkanbarissonmezisik.memorizewords.ui.theme.Purple80
import com.furkanbarissonmezisik.memorizewords.ui.theme.LanguageManager
import com.furkanbarissonmezisik.memorizewords.ui.theme.AppLanguage
import com.furkanbarissonmezisik.memorizewords.ui.theme.ColorPalette
import com.furkanbarissonmezisik.memorizewords.ui.theme.getColorsForPalette
import com.furkanbarissonmezisik.memorizewords.notification.NotificationManager
import com.furkanbarissonmezisik.memorizewords.notification.NotificationSettingsManager
import com.furkanbarissonmezisik.memorizewords.ui.components.TimePickerDialog
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Info
import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import androidx.core.content.ContextCompat
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.compose.LocalActivityResultRegistryOwner

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsScreen(
    onNavigateBack: () -> Unit,
    themeManager: ThemeManager,
    languageManager: LanguageManager
) {
    val context = LocalContext.current
    val notificationManager = remember { NotificationManager(context) }
    val notificationSettings = remember { NotificationSettingsManager(context) }
    
    // Time picker dialog state
    var showTimePicker by remember { mutableStateOf(false) }
    
    // Check notification permission
    val hasNotificationPermission = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
        ContextCompat.checkSelfPermission(context, Manifest.permission.POST_NOTIFICATIONS) == PackageManager.PERMISSION_GRANTED
    } else {
        // For Android 12 and below, check if notifications are enabled
        val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as android.app.NotificationManager
        notificationManager.areNotificationsEnabled()
    }
    
    // Get Activity context for permission launcher
    val activity = context as? ComponentActivity
    val activityResultRegistryOwner = LocalActivityResultRegistryOwner.current
    
    // Permission launcher
    val permissionLauncher = if (activityResultRegistryOwner != null) {
        rememberLauncherForActivityResult(
            contract = ActivityResultContracts.RequestPermission()
        ) { isGranted ->
            if (isGranted) {
                // Permission granted, schedule notification if enabled
                if (notificationSettings.isEnabled) {
                    notificationManager.scheduleDailyNotification(
                        notificationSettings.reminderHour,
                        notificationSettings.reminderMinute
                    )
                }
            }
        }
    } else {
        null
    }
    
    // Get app version info
    val packageInfo = context.packageManager.getPackageInfo(context.packageName, 0)
    val appVersion = packageInfo.versionName
    val appVersionCode = packageInfo.longVersionCode
    
    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = stringResource(R.string.settings),
                        fontWeight = FontWeight.Bold,
                        fontSize = 20.sp
                    )
                },

                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(
                            imageVector = Icons.Default.ArrowBack,
                            contentDescription = stringResource(R.string.back)
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
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Settings icon
            item {
                Icon(
                    imageVector = Icons.Default.Settings,
                    contentDescription = null,
                    modifier = Modifier
                        .size(80.dp)
                        .padding(bottom = 24.dp),
                    tint = MaterialTheme.colorScheme.primary
                )
            }
            
            // App Settings title
            item {
                Text(
                    text = stringResource(R.string.app_settings),
                    style = MaterialTheme.typography.headlineMedium,
                    fontWeight = FontWeight.Bold,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.padding(bottom = 16.dp)
                )
            }
            
            // Theme Selection Card
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
                            .padding(16.dp)
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(bottom = 16.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                imageVector = Icons.Default.Star,
                                contentDescription = null,
                                tint = MaterialTheme.colorScheme.primary,
                                modifier = Modifier.padding(end = 16.dp)
                            )
                            
                            Text(
                                text = stringResource(R.string.theme),
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.Medium
                            )
                        }
                        
                        // Theme options
                        Column {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(vertical = 8.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                RadioButton(
                                    selected = themeManager.currentThemeMode == ThemeMode.LIGHT,
                                    onClick = { themeManager.setThemeMode(ThemeMode.LIGHT) }
                                )
                                Spacer(modifier = Modifier.width(8.dp))
                                Icon(
                                    imageVector = Icons.Default.Star,
                                    contentDescription = null,
                                    tint = MaterialTheme.colorScheme.onSurfaceVariant,
                                    modifier = Modifier.size(20.dp)
                                )
                                Spacer(modifier = Modifier.width(8.dp))
                                Text(
                                    text = stringResource(R.string.light_theme),
                                    style = MaterialTheme.typography.bodyMedium
                                )
                            }
                            
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(vertical = 8.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                RadioButton(
                                    selected = themeManager.currentThemeMode == ThemeMode.DARK,
                                    onClick = { themeManager.setThemeMode(ThemeMode.DARK) }
                                )
                                Spacer(modifier = Modifier.width(8.dp))
                                Icon(
                                    imageVector = Icons.Default.Info,
                                    contentDescription = null,
                                    tint = MaterialTheme.colorScheme.onSurfaceVariant,
                                    modifier = Modifier.size(20.dp)
                                )
                                Spacer(modifier = Modifier.width(8.dp))
                                Text(
                                    text = stringResource(R.string.dark_theme),
                                    style = MaterialTheme.typography.bodyMedium
                                )
                            }
                            
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(vertical = 8.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                RadioButton(
                                    selected = themeManager.currentThemeMode == ThemeMode.SYSTEM,
                                    onClick = { themeManager.setThemeMode(ThemeMode.SYSTEM) }
                                )
                                Spacer(modifier = Modifier.width(8.dp))
                                Icon(
                                    imageVector = Icons.Default.Settings,
                                    contentDescription = null,
                                    tint = MaterialTheme.colorScheme.onSurfaceVariant,
                                    modifier = Modifier.size(20.dp)
                                )
                                Spacer(modifier = Modifier.width(8.dp))
                                Text(
                                    text = stringResource(R.string.system_default),
                                    style = MaterialTheme.typography.bodyMedium
                                )
                            }
                        }
                        
                        // Theme preview section
                        Spacer(modifier = Modifier.height(16.dp))
                        Text(
                            text = stringResource(R.string.theme_preview),
                            style = MaterialTheme.typography.titleSmall,
                            fontWeight = FontWeight.Medium,
                            modifier = Modifier.padding(bottom = 8.dp)
                        )
                        
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceEvenly
                        ) {
                            // Light theme preview
                            Card(
                                modifier = Modifier
                                    .weight(1f)
                                    .padding(end = 4.dp),
                                colors = CardDefaults.cardColors(
                                    containerColor = LightBackground
                                )
                            ) {
                                Column(
                                    modifier = Modifier.padding(8.dp),
                                    horizontalAlignment = Alignment.CenterHorizontally
                                ) {
                                    Text(
                                        text = stringResource(R.string.light_theme),
                                        style = MaterialTheme.typography.labelSmall,
                                        color = LightOnBackground
                                    )
                                    Spacer(modifier = Modifier.height(4.dp))
                                    Box(
                                        modifier = Modifier
                                            .size(24.dp)
                                            .background(
                                                color = Purple40,
                                                shape = CircleShape
                                            )
                                    )
                                }
                            }
                            
                            // Dark theme preview
                            Card(
                                modifier = Modifier
                                    .weight(1f)
                                    .padding(start = 4.dp),
                                colors = CardDefaults.cardColors(
                                    containerColor = DarkBackground
                                )
                            ) {
                                Column(
                                    modifier = Modifier.padding(8.dp),
                                    horizontalAlignment = Alignment.CenterHorizontally
                                ) {
                                    Text(
                                        text = stringResource(R.string.dark_theme),
                                        style = MaterialTheme.typography.labelSmall,
                                        color = DarkOnBackground
                                    )
                                    Spacer(modifier = Modifier.height(4.dp))
                                    Box(
                                        modifier = Modifier
                                            .size(24.dp)
                                            .background(
                                                color = Purple80,
                                                shape = CircleShape
                                            )
                                    )
                                }
                            }
                        }
                    }
                }
            }
            
            // Color Palette Selection Card
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
                            .padding(16.dp)
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(bottom = 16.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                imageVector = Icons.Default.Star,
                                contentDescription = null,
                                tint = MaterialTheme.colorScheme.primary,
                                modifier = Modifier.padding(end = 16.dp)
                            )
                            
                            Text(
                                text = stringResource(R.string.color_palette),
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.Medium
                            )
                        }
                        
                        // Color palette dropdown
                        var paletteExpanded by remember { mutableStateOf(false) }
                        
                        Text(
                            text = stringResource(R.string.color_palette_description),
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            modifier = Modifier.padding(bottom = 8.dp)
                        )
                        
                        ExposedDropdownMenuBox(
                            expanded = paletteExpanded,
                            onExpandedChange = { paletteExpanded = !paletteExpanded }
                        ) {
                            OutlinedTextField(
                                value = themeManager.getColorPaletteDisplayName(themeManager.currentColorPalette, context),
                                onValueChange = {},
                                readOnly = true,
                                trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = paletteExpanded) },
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .menuAnchor(),
                                colors = ExposedDropdownMenuDefaults.outlinedTextFieldColors(),
                                textStyle = MaterialTheme.typography.bodyMedium,
                                singleLine = true
                            )
                            
                            ExposedDropdownMenu(
                                expanded = paletteExpanded,
                                onDismissRequest = { paletteExpanded = false }
                            ) {
                                themeManager.getAvailableColorPalettes().forEach { palette ->
                                    DropdownMenuItem(
                                        text = { 
                                            Text(
                                                text = themeManager.getColorPaletteDisplayName(palette, context),
                                                style = MaterialTheme.typography.bodyMedium
                                            )
                                        },
                                        onClick = {
                                            themeManager.setColorPalette(palette)
                                            paletteExpanded = false
                                        },
                                        leadingIcon = {
                                            if (themeManager.currentColorPalette == palette) {
                                                Icon(
                                                    imageVector = Icons.Default.Star,
                                                    contentDescription = null,
                                                    tint = MaterialTheme.colorScheme.primary,
                                                    modifier = Modifier.size(20.dp)
                                                )
                                            }
                                        },
                                        trailingIcon = {
                                            // Color preview circle
                                            val paletteColors = getColorsForPalette(palette)
                                            Box(
                                                modifier = Modifier
                                                    .size(20.dp)
                                                    .background(
                                                        color = paletteColors.primary,
                                                        shape = CircleShape
                                                    )
                                            )
                                        }
                                    )
                                }
                            }
                        }
                        
                        // Color palette preview section
                        Spacer(modifier = Modifier.height(16.dp))
                        Text(
                            text = stringResource(R.string.color_preview),
                            style = MaterialTheme.typography.titleSmall,
                            fontWeight = FontWeight.Medium,
                            modifier = Modifier.padding(bottom = 8.dp)
                        )
                        
                        // Current palette preview
                        val currentPaletteColors = getColorsForPalette(themeManager.currentColorPalette)
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceEvenly
                        ) {
                            listOf(
                                stringResource(R.string.primary) to currentPaletteColors.primary,
                                stringResource(R.string.secondary) to currentPaletteColors.secondary,
                                stringResource(R.string.tertiary) to currentPaletteColors.tertiary,
                                stringResource(R.string.accent) to currentPaletteColors.accent
                            ).forEach { (name, color) ->
                                Column(
                                    horizontalAlignment = Alignment.CenterHorizontally,
                                    modifier = Modifier.weight(1f)
                                ) {
                                    Box(
                                        modifier = Modifier
                                            .size(32.dp)
                                            .background(
                                                color = color,
                                                shape = CircleShape
                                            )
                                    )
                                    Spacer(modifier = Modifier.height(4.dp))
                                    Text(
                                        text = name,
                                        style = MaterialTheme.typography.labelSmall,
                                        textAlign = TextAlign.Center
                                    )
                                }
                            }
                        }
                    }
                }
            }
            
            // Notification Settings Card
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
                            .padding(16.dp)
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(bottom = 16.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                imageVector = Icons.Default.Notifications,
                                contentDescription = null,
                                tint = MaterialTheme.colorScheme.primary,
                                modifier = Modifier.padding(end = 16.dp)
                            )
                            
                            Text(
                                text = stringResource(R.string.notifications),
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.Medium
                            )
                        }
                        
                        Text(
                            text = stringResource(R.string.notification_description),
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            modifier = Modifier.padding(bottom = 16.dp)
                        )
                        
                        // Show permission status message
                        if (!hasNotificationPermission && Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                            Card(
                                colors = CardDefaults.cardColors(
                                    containerColor = MaterialTheme.colorScheme.primaryContainer
                                ),
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(bottom = 16.dp)
                            ) {
                                Row(
                                    modifier = Modifier.padding(12.dp),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.Info,
                                        contentDescription = null,
                                        tint = MaterialTheme.colorScheme.onPrimaryContainer,
                                        modifier = Modifier.padding(end = 8.dp)
                                    )
                                    Text(
                                        text = stringResource(R.string.notification_permission_required),
                                        style = MaterialTheme.typography.bodySmall,
                                        color = MaterialTheme.colorScheme.onPrimaryContainer
                                    )
                                }
                            }
                        }
                        
                        // Enable/Disable toggle
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 8.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Switch(
                                checked = notificationSettings.isEnabled,
                                onCheckedChange = { enabled ->
                                    notificationSettings.updateEnabled(enabled)
                                    if (enabled) {
                                        if (hasNotificationPermission) {
                                            notificationManager.scheduleDailyNotification(
                                                notificationSettings.reminderHour,
                                                notificationSettings.reminderMinute
                                            )
                                        } else {
                                            permissionLauncher?.launch(Manifest.permission.POST_NOTIFICATIONS)
                                        }
                                    } else {
                                        notificationManager.cancelDailyNotification()
                                    }
                                }
                            )
                            Spacer(modifier = Modifier.width(16.dp))
                            Text(
                                text = stringResource(R.string.enable_daily_reminders),
                                style = MaterialTheme.typography.bodyMedium
                            )
                        }
                        
                        // Time picker (only show if enabled)
                        if (notificationSettings.isEnabled) {
                            Spacer(modifier = Modifier.height(16.dp))
                            
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(vertical = 8.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Info,
                                    contentDescription = null,
                                    tint = MaterialTheme.colorScheme.onSurfaceVariant,
                                    modifier = Modifier.padding(end = 16.dp)
                                )
                                
                                Text(
                                    text = stringResource(R.string.reminder_time),
                                    style = MaterialTheme.typography.bodyMedium,
                                    modifier = Modifier.weight(1f)
                                )
                                
                                Text(
                                    text = notificationSettings.getFormattedTime(),
                                    style = MaterialTheme.typography.bodyMedium,
                                    color = MaterialTheme.colorScheme.primary,
                                    fontWeight = FontWeight.Medium
                                )
                            }
                            
                            // Time picker button
                            Button(
                                onClick = {
                                    showTimePicker = true
                                },
                                modifier = Modifier.fillMaxWidth(),
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = MaterialTheme.colorScheme.secondary
                                )
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Edit,
                                    contentDescription = null,
                                    modifier = Modifier.padding(end = 8.dp)
                                )
                                Text(stringResource(R.string.change_time))
                            }
                        }
                        
                        // Test notification button (for debugging)
                        if (hasNotificationPermission) {
                            Spacer(modifier = Modifier.height(16.dp))
                            
                            Button(
                                onClick = {
                                    // Show a test notification
                                    notificationManager.showNotification(languageManager.currentLanguage)
                                },
                                modifier = Modifier.fillMaxWidth(),
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = MaterialTheme.colorScheme.tertiary
                                )
                            ) {
                                Text(stringResource(R.string.test_notification))
                            }
                            
                        }
                        
                    }
                }
            }
            
            // Language Selection Card
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
                            .padding(16.dp)
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(bottom = 16.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                imageVector = Icons.Default.Info,
                                contentDescription = null,
                                tint = MaterialTheme.colorScheme.primary,
                                modifier = Modifier.padding(end = 16.dp)
                            )
                            
                            Text(
                                text = stringResource(R.string.language),
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.Medium
                            )
                        }
                        
                        // Compact language dropdown
                        var expanded by remember { mutableStateOf(false) }
                        
                        Text(
                            text = stringResource(R.string.language_selection_description),
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            modifier = Modifier.padding(bottom = 8.dp)
                        )
                        
                        ExposedDropdownMenuBox(
                            expanded = expanded,
                            onExpandedChange = { expanded = !expanded }
                        ) {
                            OutlinedTextField(
                                value = languageManager.getLanguageDisplayName(languageManager.currentLanguage),
                                onValueChange = {},
                                readOnly = true,
                                trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) },
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .menuAnchor(),
                                colors = ExposedDropdownMenuDefaults.outlinedTextFieldColors(),
                                textStyle = MaterialTheme.typography.bodyMedium,
                                singleLine = true
                            )
                            
                            ExposedDropdownMenu(
                                expanded = expanded,
                                onDismissRequest = { expanded = false }
                            ) {
                                languageManager.getAvailableLanguages().forEach { language ->
                                    DropdownMenuItem(
                                        text = { 
                                            Text(
                                                text = languageManager.getLanguageDisplayName(language),
                                                style = MaterialTheme.typography.bodyMedium
                                            )
                                        },
                                        onClick = {
                                            languageManager.setLanguage(language)
                                            expanded = false
                                        },
                                        leadingIcon = {
                                            if (languageManager.currentLanguage == language) {
                                                Icon(
                                                    imageVector = Icons.Default.Star,
                                                    contentDescription = null,
                                                    tint = MaterialTheme.colorScheme.primary,
                                                    modifier = Modifier.size(20.dp)
                                                )
                                            }
                                        }
                                    )
                                }
                            }
                        }
                    }
                }
            }
            
            // App version card
            item {
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
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = Icons.Default.Info,
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.primary,
                            modifier = Modifier.padding(end = 16.dp)
                        )
                        
                        Column(
                            modifier = Modifier.weight(1f)
                        ) {
                            Text(
                                text = stringResource(R.string.app_version),
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.Medium
                            )
                            Text(
                                text = "Version $appVersion (Build $appVersionCode)",
                                style = MaterialTheme.typography.bodyMedium,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    }
                }
            }
            
            // App info card
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
                            .padding(16.dp)
                    ) {
                        Text(
                            text = stringResource(R.string.about),
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Medium,
                            modifier = Modifier.padding(bottom = 8.dp)
                        )
                        
                        Text(
                            text = stringResource(R.string.app_description),
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            lineHeight = 20.sp
                        )
                    }
                }
            }
            
            // Features card
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
                            .padding(16.dp)
                    ) {
                        Text(
                            text = stringResource(R.string.features),
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Medium,
                            modifier = Modifier.padding(bottom = 8.dp)
                        )
                        
                        Text(
                            text = stringResource(R.string.features_description),
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            lineHeight = 20.sp
                        )
                    }
                }
            }
            
            // Back button
            item {
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
            
            // Bottom spacing
            item {
                Spacer(modifier = Modifier.height(16.dp))
            }
        }
    }
    
    // Time Picker Dialog
    if (showTimePicker) {
        TimePickerDialog(
            initialHour = notificationSettings.reminderHour,
            initialMinute = notificationSettings.reminderMinute,
            onTimeSelected = { hour, minute ->
                notificationSettings.setReminderTime(hour, minute)
                if (notificationSettings.isEnabled) {
                    notificationManager.scheduleDailyNotification(hour, minute)
                }
            },
            onDismiss = {
                showTimePicker = false
            }
        )
    }
}
