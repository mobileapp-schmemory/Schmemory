package site.jwojcik.schmemory.ui

import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.AddCircle
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import site.jwojcik.schmemory.data.Scene
import site.jwojcik.schmemory.data.Script
import site.jwojcik.schmemory.data.Speech
import site.jwojcik.schmemory.ui.theme.Blue
import site.jwojcik.schmemory.ui.theme.Green
import site.jwojcik.schmemory.ui.theme.Purple
import site.jwojcik.schmemory.ui.theme.Yellow

enum class SchmemoryListType { SCENE, SPEECH }

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ListScreen(
    listType: SchmemoryListType,
    onUpClick: () -> Boolean,
    onItemClick: (Long) -> Unit,
    onEditClick: (Long) -> Unit,
    modifier: Modifier = Modifier,
    viewModel: ListScreenViewModel = viewModel(
        factory = ListScreenViewModel.Factory
    ),
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    var searchQuery by remember { mutableStateOf("") }
    var isSearchActive by remember { mutableStateOf(false) }
    var isSelectionMode by remember { mutableStateOf(false) }
    val selectedItems = remember { mutableStateListOf<Long>() }
    var showAddDialog by remember { mutableStateOf(false) }
    var showDeleteConfirmDialog by remember { mutableStateOf(false) }
    var newItemName by remember { mutableStateOf("") }
    var importUrl by remember { mutableStateOf("") }
    var readingFor by remember { mutableStateOf("") }
    var addDialogMode by remember { mutableStateOf("create") } // "create" or "import"
    var isImporting by remember { mutableStateOf(false) }
    var importError by remember { mutableStateOf<String?>(null) }

    val fullList = if (listType == SchmemoryListType.SCENE) uiState.sceneList else uiState.speechList
    
    val displayList = if (searchQuery.isNotEmpty()) {
        fullList.filter { it.name.contains(searchQuery, ignoreCase = true) }
    } else {
        fullList
    }

    Scaffold(
        containerColor = Yellow,
        modifier = modifier,
        topBar = {
            TopAppBar(
                title = {
                    if (isSearchActive) {
                        TextField(
                            value = searchQuery,
                            onValueChange = { searchQuery = it },
                            placeholder = { Text("Search...") },
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(end = 8.dp),
                            colors = TextFieldDefaults.colors(
                                focusedContainerColor = Color.White,
                                unfocusedContainerColor = Color.White,
                                focusedIndicatorColor = Color.Transparent,
                                unfocusedIndicatorColor = Color.Transparent
                            ),
                            singleLine = true
                        )
                    } else {
                        Text(
                            text = if (listType == SchmemoryListType.SPEECH) "Speeches" else "Scenes"
                        )
                    }
                },
                navigationIcon = {
                    IconButton(onClick = { onUpClick() }) {
                        Icon(
                            Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Back",
                            tint = Color.Unspecified
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Blue
                ),
                actions = {
                    IconButton(onClick = {
                        isSearchActive = !isSearchActive
                        if (!isSearchActive) {
                            searchQuery = ""
                        }
                    }) {
                        Icon(Icons.Filled.Search, contentDescription = "Search")
                    }
                    IconButton(onClick = { showAddDialog = true }) {
                        Icon(Icons.Filled.AddCircle, contentDescription = "Add")
                    }
                    IconButton(onClick = {
                        isSelectionMode = !isSelectionMode
                        if (!isSelectionMode) {
                            selectedItems.clear()
                        }
                    }) {
                        Icon(
                            Icons.Filled.CheckCircle,
                            contentDescription = "Select",
                            tint = if (isSelectionMode) Green else Color.Unspecified
                        )
                    }
                    if (selectedItems.isNotEmpty()) {
                        IconButton(onClick = { showDeleteConfirmDialog = true }) {
                            Icon(Icons.Filled.Delete, contentDescription = "Delete")
                        }
                    }
                },
                modifier = modifier
            )
        }
    ) { innerPadding ->
        Column(
            modifier = modifier
                .fillMaxSize()
                .padding(innerPadding),
        ) {
            ItemList(
                itemList = displayList,
                listType = listType,
                onItemClick = onItemClick,
                onEditClick = onEditClick,
                isSelectionMode = isSelectionMode,
                selectedItems = selectedItems,
                viewModel = viewModel
            )
        }
    }

    if (showAddDialog) {
        AlertDialog(
            onDismissRequest = {
                if (!isImporting) {
                    showAddDialog = false
                    newItemName = ""
                    importUrl = ""
                    readingFor = ""
                    addDialogMode = "create"
                    importError = null
                }
            },
            title = {
                Column {
                    Text("Add New ${if (listType == SchmemoryListType.SPEECH) "Speech" else "Scene"}")
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 12.dp),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Button(
                            onClick = { addDialogMode = "create" },
                            modifier = Modifier.weight(1f),
                            enabled = !isImporting,
                            colors = ButtonDefaults.buttonColors(
                                containerColor = if (addDialogMode == "create") Blue else Color.LightGray
                            )
                        ) {
                            Text("Create New", color = if (addDialogMode == "create") Color.White else Color.Black)
                        }
                        Button(
                            onClick = { addDialogMode = "import" },
                            modifier = Modifier.weight(1f),
                            enabled = !isImporting,
                            colors = ButtonDefaults.buttonColors(
                                containerColor = if (addDialogMode == "import") Blue else Color.LightGray
                            )
                        ) {
                            Text("Import", color = if (addDialogMode == "import") Color.White else Color.Black)
                        }
                    }
                }
            },
            text = {
                when (addDialogMode) {
                    "create" -> {
                        Column {
                            Text("Enter a name for the new item:")
                            TextField(
                                value = newItemName,
                                onValueChange = { newItemName = it },
                                placeholder = { Text("Item name") },
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(top = 8.dp)
                            )
                            if (listType == SchmemoryListType.SCENE) {
                                Text(
                                    text = "Character you're reading for:",
                                    modifier = Modifier.padding(top = 16.dp)
                                )
                                TextField(
                                    value = readingFor,
                                    onValueChange = { readingFor = it },
                                    placeholder = { Text("Character name") },
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(top = 8.dp)
                                )
                            }
                        }
                    }
                    "import" -> {
                        Column {
                            Text("Paste a Pastebin Raw URL:")
                            TextField(
                                value = importUrl,
                                onValueChange = { importUrl = it },
                                placeholder = { Text("https://pastebin.com/raw/...") },
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(top = 8.dp),
                                isError = importError != null,
                                enabled = !isImporting
                            )
                            if (importError != null) {
                                Text(
                                    text = importError!!,
                                    color = Color.Red,
                                    style = MaterialTheme.typography.bodySmall,
                                    modifier = Modifier.padding(top = 4.dp)
                                )
                            }
                            if (isImporting) {
                                CircularProgressIndicator(
                                    modifier = Modifier
                                        .align(Alignment.CenterHorizontally)
                                        .padding(top = 16.dp)
                                )
                            }
                        }
                    }
                }
            },
            confirmButton = {
                if (addDialogMode == "create") {
                    Button(
                        onClick = {
                            if (newItemName.isNotBlank()) {
                                when (listType) {
                                    SchmemoryListType.SCENE -> {
                                        viewModel.addScene(newItemName, readingFor)
                                    }
                                    else -> {
                                        viewModel.addSpeech(newItemName)
                                    }
                                }
                                showAddDialog = false
                                newItemName = ""
                                readingFor = ""
                                addDialogMode = "create"
                            }
                        }
                    ) {
                        Text("Create")
                    }
                } else {
                    Button(
                        onClick = {
                            if (importUrl.isNotBlank()) {
                                isImporting = true
                                importError = null
                                viewModel.importFromPastebin(
                                    url = importUrl,
                                    listType = listType,
                                    onSuccess = {
                                        isImporting = false
                                        showAddDialog = false
                                        importUrl = ""
                                    },
                                    onError = { error ->
                                        isImporting = false
                                        importError = error
                                    }
                                )
                            }
                        },
                        enabled = !isImporting
                    ) {
                        Text("Import")
                    }
                }
            },
            dismissButton = {
                TextButton(
                    onClick = {
                        showAddDialog = false
                        newItemName = ""
                        readingFor = ""
                        importUrl = ""
                        addDialogMode = "create"
                        importError = null
                    },
                    enabled = !isImporting
                ) {
                    Text("Cancel")
                }
            }
        )
    }

    if (showDeleteConfirmDialog) {
        AlertDialog(
            onDismissRequest = { showDeleteConfirmDialog = false },
            title = { Text("Delete Items?") },
            text = {
                Text("Are you sure you want to delete ${selectedItems.size} item(s)? This cannot be undone.")
            },
            confirmButton = {
                Button(
                    onClick = {
                        when (listType) {
                            SchmemoryListType.SCENE -> {
                                viewModel.deleteSelectedScenes()
                            }
                            else -> {
                                viewModel.deleteSelectedSpeeches()
                            }
                        }
                        selectedItems.clear()
                        isSelectionMode = false
                        showDeleteConfirmDialog = false
                    }
                ) {
                    Text("Delete")
                }
            },
            dismissButton = {
                TextButton(onClick = { showDeleteConfirmDialog = false }) {
                    Text("Cancel")
                }
            }
        )
    }
}

@Composable
fun ItemList(
    itemList: List<Script>,
    listType: SchmemoryListType,
    onItemClick: (Long) -> Unit,
    onEditClick: (Long) -> Unit,
    isSelectionMode: Boolean = false,
    selectedItems: MutableList<Long> = mutableListOf(),
    viewModel: ListScreenViewModel
) {
    LazyColumn {
        items(
            items = itemList,
            key = { script -> script.id }
        ) { script ->
            ScriptCard(
                script = script,
                onItemClick = onItemClick,
                onEditClick = onEditClick,
                isSelectionMode = isSelectionMode,
                isSelected = script.id in selectedItems,
                onSelectionChange = { selected ->
                    if (selected) {
                        selectedItems.add(script.id)
                    } else {
                        selectedItems.remove(script.id)
                    }
                    if (listType == SchmemoryListType.SCENE) viewModel.selectScene(script as Scene)
                    else viewModel.selectSpeech(script as Speech)
                }
            )
        }
    }
}


@Composable
fun ScriptCard(
    script: Script,
    onItemClick: (Long) -> Unit,
    onEditClick: (Long) -> Unit,
    isSelectionMode: Boolean = false,
    isSelected: Boolean = false,
    onSelectionChange: (Boolean) -> Unit = {},
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
            .padding(8.dp)
            .fillMaxWidth()
            .border(width = 3.dp, color = if (isSelected) Green else Purple)
            .clickable {
                if (isSelectionMode) {
                    onSelectionChange(!isSelected)
                } else {
                    onItemClick(script.id)
                }
            },
        colors = CardDefaults.cardColors(
            containerColor = if (isSelected) Green.copy(alpha = 0.3f) else Color.White
        )
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            if (isSelectionMode) {
                Checkbox(
                    checked = isSelected,
                    onCheckedChange = { onSelectionChange(it) }
                )
            } else {
                IconButton(
                    onClick = { onItemClick(script.id) }
                ) {
                    Icon(Icons.Filled.PlayArrow, contentDescription = "Play")
                }
            }

            Text(
                text = if (script.name.length > 24) "${script.name.take(24)}..." else script.name,
                color = Color.Black,
                style = MaterialTheme.typography.bodyLarge,
                modifier = Modifier
                    .weight(1f)
                    .padding(start = 4.dp)
            )

            if (!isSelectionMode) {
                IconButton(onClick = { onEditClick(script.id) }) {
                    Icon(Icons.Filled.Edit, contentDescription = "Edit")
                }
            }
        }
    }
}
