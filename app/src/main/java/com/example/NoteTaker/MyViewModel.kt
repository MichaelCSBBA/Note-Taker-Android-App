package com.example.NoteTaker

import android.widget.LinearLayout
import androidx.core.view.allViews
import androidx.lifecycle.*

class MyViewModel() : ViewModel() {

    // Definition of variables, livedata, states
    val model = Model()
    val set = "Note Name 1"
    var noteListViewModel: MutableLiveData<MutableList<NoteFormat>> =
        MutableLiveData<MutableList<NoteFormat>>(model.listOfNotes)
    var viewAbleNoteList: MutableList<NoteFormat> = mutableListOf()
    val snackBarMessage: MutableLiveData<String> = MutableLiveData()
    var importantFilter = "Important Filter Off"
    var searchFilter = "Search Filter Off"
    var searchWord = ""
    var newNoteCounter = 1
    var selectedNote = NoteFormat ()
    var editModeOrAddMode = " "

    var dynamicLiveNoteCounter: MutableLiveData<String> = MutableLiveData("0 Notes")
    var dynamicClearLiveNoteCounter : MutableLiveData<Int> = MutableLiveData(0)
    var liveNoteCounter = 0
    var liveNoteMatchingCounter = 0
    var snackBarState = true

    init {

    }


    // Adds random notes to list
      fun addRandomNoteToList () {
          val newRandomNote = model.addRandomNote()
          model.listOfNotes.add(0, newRandomNote) // add to model list
          noteListViewModel.value = model.listOfNotes // update live data list
          newRandomNote.noteID = newNoteCounter
          newNoteCounter++

          // Keeping track of number of live notes
          liveNoteCounter++
          dynamicClearLiveNoteCounter.value = liveNoteCounter
          subtitleNotes(liveNoteCounter, importantFilter, searchFilter)
      }

    // Deletes note from list
    fun deleteNote (noteDelete: NoteFormat) {
        model.listOfNotes.remove(noteDelete) // delete to model list
        noteListViewModel.value = model.listOfNotes // update live data list

        // Keeping track of number of live notes
        liveNoteCounter--
        dynamicClearLiveNoteCounter.value = liveNoteCounter
        subtitleNotes(liveNoteCounter, importantFilter, searchFilter)
        snackBarState = true
        snackBarMessage.value = "Deleted Note #${selectedNote.noteID}"
    }

    // Determines selected note
    fun noteSelected (noteContent: NoteFormat) {
        selectedNote = noteContent
    }

    // Adds a new note to list
    fun addNewNote (newNote: NoteFormat) {
        model.listOfNotes.remove(selectedNote)
        newNote.noteID = newNoteCounter
        newNoteCounter++
        model.listOfNotes.add(0, newNote) // add new Note to model list
        noteListViewModel.value = model.listOfNotes // update Live data

        // Keeping track of number of live notes
        liveNoteCounter++
        dynamicClearLiveNoteCounter.value = liveNoteCounter
        subtitleNotes(liveNoteCounter, importantFilter, searchFilter)
    }

    // Updates the note subtitles under the action bar.
    fun subtitleNotes (num: Int, importantCheck: String, searchCheck: String) {
        if ((importantCheck == "Important Filter On") || (searchCheck == "Search Filter On")) {
            dynamicLiveNoteCounter.value = "(Matching $liveNoteMatchingCounter of $num Notes)"
        } else {
            dynamicLiveNoteCounter.value = "($num Notes)"
        }
    }

    // Removes the notes that are displayed.
    fun removeShownNotes () {
        snackBarState = true
        snackBarMessage.value = "Cleared ${viewAbleNoteList.size} Notes"
        for (i in viewAbleNoteList) {
            model.listOfNotes.remove(i)
            liveNoteCounter--
            liveNoteMatchingCounter--
        }

        dynamicClearLiveNoteCounter.value = liveNoteCounter
        noteListViewModel.value = model.listOfNotes // update Live data
    }
}