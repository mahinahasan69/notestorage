package com.example.notestorage

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel

class NotesViewModel : ViewModel() {
    var notes by mutableStateOf(
        listOf(
            NoteItem(1, "💡 Lab 5 Requirements", "Implement SQLite and JSON file storage with MVVM architecture."),
            NoteItem(2, "🚀 Kotlin & Compose", "Build robust UI components and handle runtime storage switching.")
        )
    )
        private set

    var titleInput by mutableStateOf("")
        private set

    var contentInput by mutableStateOf("")
        private set

    var selectedStorage by mutableStateOf("SQLite")
        private set

    var errorMessage by mutableStateOf<String?>(null)
        private set

    var selectedNote by mutableStateOf<NoteItem?>(null)
        private set

    var editingNoteId by mutableStateOf<Int?>(null)
        private set

    var menuExpanded by mutableStateOf(false)
        private set

    fun onTitleChanged(newTitle: String) {
        titleInput = newTitle
        errorMessage = null
    }

    fun onContentChanged(newContent: String) {
        contentInput = newContent
        errorMessage = null
    }

    fun onStorageSelected(storage: String) {
        selectedStorage = storage
    }

    fun onMenuExpandedChanged(expanded: Boolean) {
        menuExpanded = expanded
    }

    fun selectNote(note: NoteItem?) {
        selectedNote = note
    }

    fun appendSnippet() {
        contentInput += "\n// TODO: Implement Logic"
        menuExpanded = false
    }

    fun clearInputs() {
        titleInput = ""
        contentInput = ""
        editingNoteId = null
        errorMessage = null
        menuExpanded = false
    }

    fun editNote(note: NoteItem) {
        titleInput = note.title
        contentInput = note.content
        editingNoteId = note.id
    }

    fun deleteNote(id: Int) {
        notes = notes.filter { it.id != id }
        if (editingNoteId == id) {
            clearInputs()
        }
    }

    fun syncNotes(): String {
        notes = notes.map { it.copy(storageType = selectedStorage) }
        return "🔄 Successfully synchronized notes with $selectedStorage!"
    }

    fun saveOrUpdateNote(): Boolean {
        if (titleInput.isBlank() || contentInput.isBlank()) {
            errorMessage = "⚠️ Title and content cannot be empty!"
            return false
        }

        if (editingNoteId != null) {
            notes = notes.map {
                if (it.id == editingNoteId) {
                    it.copy(title = titleInput, content = contentInput, storageType = selectedStorage)
                } else {
                    it
                }
            }
            editingNoteId = null
        } else {
            val newNote = NoteItem(
                id = (notes.maxOfOrNull { it.id } ?: 0) + 1,
                title = titleInput,
                content = contentInput,
                storageType = selectedStorage
            )
            notes = notes + newNote
        }
        titleInput = ""
        contentInput = ""
        return true
    }
}