package com.example.notestorage

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import kotlinx.coroutines.launch

data class NoteItem(
    val id: Int,
    val title: String,
    val content: String,
    val storageType: String = "SQLite"
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NotesScreen(
    modifier: Modifier = Modifier,
    viewModel: NotesViewModel = viewModel()
) {
    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()

    Scaffold(
        modifier = modifier.fillMaxSize(),
        snackbarHost = { SnackbarHost(snackbarHostState) },
        topBar = {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color(0xFF6750A4))
                    .padding(horizontal = 16.dp, vertical = 12.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = stringResource(R.string.app_name),
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )

                    Box {
                        IconButton(onClick = { viewModel.onMenuExpandedChanged(true) }) {
                            Icon(
                                imageVector = Icons.Default.MoreVert,
                                contentDescription = "Options Menu",
                                tint = Color.White
                            )
                        }

                        DropdownMenu(
                            expanded = viewModel.menuExpanded,
                            onDismissRequest = { viewModel.onMenuExpandedChanged(false) }
                        ) {
                            DropdownMenuItem(
                                text = { Text(stringResource(R.string.menu_snippet)) },
                                onClick = { viewModel.appendSnippet() }
                            )
                            DropdownMenuItem(
                                text = { Text(stringResource(R.string.menu_clear)) },
                                onClick = { viewModel.clearInputs() }
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(8.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        FilterChip(
                            selected = viewModel.selectedStorage == "SQLite",
                            onClick = { viewModel.onStorageSelected("SQLite") },
                            label = { Text(stringResource(R.string.sqlite_chip)) },
                            colors = FilterChipDefaults.filterChipColors(
                                selectedContainerColor = Color(0xFFD0BCFF),
                                selectedLabelColor = Color(0xFF381E72),
                                containerColor = Color(0xFF4F378B),
                                labelColor = Color.White
                            )
                        )
                        FilterChip(
                            selected = viewModel.selectedStorage == "JSON File",
                            onClick = { viewModel.onStorageSelected("JSON File") },
                            label = { Text(stringResource(R.string.json_chip)) },
                            colors = FilterChipDefaults.filterChipColors(
                                selectedContainerColor = Color(0xFFD0BCFF),
                                selectedLabelColor = Color(0xFF381E72),
                                containerColor = Color(0xFF4F378B),
                                labelColor = Color.White
                            )
                        )
                    }

                    Button(
                        onClick = {
                            val message = viewModel.syncNotes()
                            scope.launch {
                                snackbarHostState.showSnackbar(message)
                            }
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFD0BCFF)),
                        contentPadding = PaddingValues(horizontal = 16.dp, vertical = 0.dp)
                    ) {
                        Text(stringResource(R.string.sync_btn), color = Color(0xFF381E72), fontWeight = FontWeight.Bold)
                    }
                }
            }
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .padding(16.dp)
                .fillMaxSize()
        ) {
            OutlinedTextField(
                value = viewModel.titleInput,
                onValueChange = { viewModel.onTitleChanged(it) },
                label = { Text(stringResource(R.string.title_hint)) },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true,
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = Color(0xFF6750A4),
                    focusedLabelColor = Color(0xFF6750A4)
                )
            )

            Spacer(modifier = Modifier.height(8.dp))

            OutlinedTextField(
                value = viewModel.contentInput,
                onValueChange = { viewModel.onContentChanged(it) },
                label = { Text(stringResource(R.string.content_hint)) },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(130.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = Color(0xFF6750A4),
                    focusedLabelColor = Color(0xFF6750A4)
                )
            )

            if (viewModel.errorMessage != null) {
                Text(
                    text = viewModel.errorMessage!!,
                    color = MaterialTheme.colorScheme.error,
                    style = MaterialTheme.typography.bodySmall,
                    modifier = Modifier.padding(top = 4.dp)
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            Button(
                onClick = { viewModel.saveOrUpdateNote() },
                modifier = Modifier.fillMaxWidth(),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF6750A4))
            ) {
                Text(
                    text = if (viewModel.editingNoteId != null) stringResource(R.string.update_btn) else stringResource(R.string.save_btn, viewModel.selectedStorage),
                    fontWeight = FontWeight.Bold
                )
            }

            Spacer(modifier = Modifier.height(16.dp))
            HorizontalDivider(color = Color(0xFFE7E0EC))
            Spacer(modifier = Modifier.height(8.dp))

            Text(stringResource(R.string.saved_notes_header, viewModel.notes.size), style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold, color = Color(0xFF49454F))

            Spacer(modifier = Modifier.height(8.dp))

            LazyColumn(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(viewModel.notes) { note ->
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable { viewModel.selectNote(note) },
                        colors = CardDefaults.cardColors(containerColor = Color(0xFFFFFBFE)),
                        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
                    ) {
                        Column(modifier = Modifier.padding(16.dp)) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Text(
                                    text = note.title,
                                    style = MaterialTheme.typography.titleLarge,
                                    color = Color(0xFF1D1B20),
                                    fontWeight = FontWeight.Bold
                                )
                                Surface(
                                    color = if (note.storageType == "SQLite") Color(0xFFE8DEF8) else Color(0xFFF1F3F4),
                                    shape = RoundedCornerShape(8.dp)
                                ) {
                                    Text(
                                        text = note.storageType,
                                        style = MaterialTheme.typography.labelSmall,
                                        color = Color(0xFF1D1B20),
                                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                                    )
                                }
                            }
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = note.content,
                                style = MaterialTheme.typography.bodyMedium,
                                color = Color(0xFF49454F),
                                maxLines = 2
                            )
                            Spacer(modifier = Modifier.height(8.dp))
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.End
                            ) {
                                TextButton(onClick = { viewModel.editNote(note) }) {
                                    Text(stringResource(R.string.edit_btn), color = Color(0xFF6750A4), fontWeight = FontWeight.Bold)
                                }
                                Spacer(modifier = Modifier.width(8.dp))
                                TextButton(onClick = { viewModel.deleteNote(note.id) }) {
                                    Text(stringResource(R.string.delete_btn), color = Color(0xFFB3261E), fontWeight = FontWeight.Bold)
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    if (viewModel.selectedNote != null) {
        AlertDialog(
            onDismissRequest = { viewModel.selectNote(null) },
            title = { Text(viewModel.selectedNote!!.title, fontWeight = FontWeight.Bold) },
            text = {
                Column {
                    Text(stringResource(R.string.dialog_storage, viewModel.selectedNote!!.storageType), style = MaterialTheme.typography.labelMedium, color = Color(0xFF6750A4))
                    Spacer(modifier = Modifier.height(12.dp))
                    Text(viewModel.selectedNote!!.content, style = MaterialTheme.typography.bodyLarge)
                }
            },
            confirmButton = {
                Button(
                    onClick = { viewModel.selectNote(null) },
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF6750A4))
                ) {
                    Text(stringResource(R.string.dialog_close))
                }
            }
        )
    }
}