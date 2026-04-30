package site.jwojcik.schmemory.ui

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import site.jwojcik.schmemory.data.SceneLine
import site.jwojcik.schmemory.ui.theme.Blue
import site.jwojcik.schmemory.ui.theme.Black

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditScreen(
    scriptId: Long,
    listType: SchmemoryListType,
    onUpClick: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: EditViewModel = viewModel(factory = EditViewModel.Factory)
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    var showUnsavedDialog by remember { mutableStateOf(false) }

    LaunchedEffect(scriptId, listType) {
        viewModel.setScript(scriptId, listType)
    }

    val handleBack = {
        if (uiState.hasUnsavedChanges) {
            showUnsavedDialog = true
        } else {
            onUpClick()
        }
    }

    BackHandler(enabled = true) {
        handleBack()
    }

    if (showUnsavedDialog) {
        AlertDialog(
            onDismissRequest = { showUnsavedDialog = false },
            title = { Text("Unsaved Changes") },
            text = { Text("You have unsaved changes. Are you sure you want to discard them?") },
            confirmButton = {
                TextButton(onClick = onUpClick) {
                    Text("Discard")
                }
            },
            dismissButton = {
                TextButton(onClick = { showUnsavedDialog = false }) {
                    Text("Cancel")
                }
            }
        )
    }

    Scaffold(
        containerColor = MaterialTheme.colorScheme.background,
        topBar = {
            TopAppBar(
                title = {
                    uiState.script?.let {
                        OutlinedTextField(
                            value = it.name,
                            onValueChange = { newName -> viewModel.updateScriptName(newName) },
                            modifier = Modifier.fillMaxWidth().padding(end = 8.dp),
                            textStyle = MaterialTheme.typography.titleLarge.copy(color = Black),
                            singleLine = true,
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedTextColor = Black,
                                unfocusedTextColor = Black,
                                focusedBorderColor = Black,
                                unfocusedBorderColor = Black.copy(alpha = 0.5f),
                                focusedContainerColor = MaterialTheme.colorScheme.surfaceVariant,
                                unfocusedContainerColor = MaterialTheme.colorScheme.surfaceVariant,
                            )
                        )
                    }
                },
                navigationIcon = {
                    IconButton(onClick = handleBack) {
                        Icon(
                            Icons.AutoMirrored.Filled.ArrowBack, 
                            contentDescription = "Back",
                            tint = Black
                        )
                    }
                },
                actions = {
                    IconButton(onClick = { 
                        viewModel.save(onSuccess = onUpClick)
                    }) {
                        Icon(
                            Icons.Default.Check, 
                            contentDescription = "Save",
                            tint = Black
                        )
                    }
                    IconButton(onClick = { viewModel.addLine() }) {
                        Icon(
                            Icons.Default.Add, 
                            contentDescription = "Add Line",
                            tint = Black
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Blue,
                    titleContentColor = Black
                )
            )
        }
    ) { innerPadding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(16.dp)
        ) {
            items(uiState.lines) { line ->
                Column(modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        if (line is SceneLine) {
                            OutlinedTextField(
                                value = line.characterName,
                                onValueChange = { viewModel.updateCharacterName(line, it) },
                                label = { 
                                    Text(
                                        "Character",
                                        modifier = Modifier
                                            .background(MaterialTheme.colorScheme.surfaceVariant)
                                            .padding(horizontal = 4.dp)
                                    ) 
                                },
                                modifier = Modifier.weight(0.42f).padding(end = 8.dp),
                                singleLine = true,
                                colors = OutlinedTextFieldDefaults.colors(
                                    focusedTextColor = Black,
                                    unfocusedTextColor = Black,
                                    focusedBorderColor = Blue,
                                    focusedLabelColor = Black,
                                    unfocusedLabelColor = Black.copy(alpha = 0.6f),
                                    cursorColor = Blue,
                                    focusedContainerColor = MaterialTheme.colorScheme.surfaceVariant,
                                    unfocusedContainerColor = MaterialTheme.colorScheme.surfaceVariant,
                                )
                            )
                        }
                        OutlinedTextField(
                            value = line.text,
                            onValueChange = { viewModel.updateLineText(line, it) },
                            label = { 
                                Text(
                                    "Line Text",
                                    modifier = Modifier
                                        .background(MaterialTheme.colorScheme.surfaceVariant)
                                        .padding(horizontal = 4.dp)
                                ) 
                            },
                            modifier = Modifier.weight(0.58f),
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedTextColor = Black,
                                unfocusedTextColor = Black,
                                focusedBorderColor = Blue,
                                focusedLabelColor = Black,
                                unfocusedLabelColor = Black.copy(alpha = 0.6f),
                                cursorColor = Blue,
                                focusedContainerColor = MaterialTheme.colorScheme.surfaceVariant,
                                unfocusedContainerColor = MaterialTheme.colorScheme.surfaceVariant,
                            )
                        )
                        IconButton(onClick = { viewModel.deleteLine(line) }) {
                            Icon(Icons.Default.Delete, contentDescription = "Delete Line", tint = Color.Red)
                        }
                    }
                    HorizontalDivider(
                        modifier = Modifier.padding(top = 16.dp),
                        color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.12f)
                    )
                }
            }
        }
    }
}
