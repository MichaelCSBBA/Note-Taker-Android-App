package com.example.NoteTaker

import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.Button
import android.widget.EditText
import android.widget.Switch
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController

class FragmentAddScreen : Fragment() {

    private val viewModel: MyViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        // References to Widget Elements
        val root = inflater.inflate(R.layout.fragment_add_screen, container, false)
        val addEditType = root.findViewById<TextView>(R.id.viewNoteId)
        val importantType = root.findViewById<Switch>(R.id.addEditImportant)
        val noteTitle = root.findViewById<EditText>(R.id.viewTitle)
        val noteBody = root.findViewById<EditText>(R.id.viewBody)
        val saveButton = root.findViewById<Button>(R.id.saveButton)

        // Determines if the Add/Edit View is in Add or Edit Mode
        if (viewModel.editModeOrAddMode == "Edit Mode") {
            addEditType.text = "Edit Note #" + viewModel.selectedNote.noteID
            importantType.isChecked = viewModel.selectedNote.noteImportant
            noteTitle.setText(viewModel.selectedNote.noteTitle, TextView.BufferType.EDITABLE)
            noteBody.setText(viewModel.selectedNote.noteBody, TextView.BufferType.EDITABLE)

        } else {
            // If in add mode, no need to fill out title, important_flag, body
            addEditType.text = "Add New Note"
            addEditType.text = "Add Mode"
        }

        // Listener for when save Button is clicked.
        saveButton.setOnClickListener {
            if (viewModel.editModeOrAddMode == "Edit Mode") {
                viewModel.selectedNote.noteTitle = noteTitle.text.toString()
                viewModel.selectedNote.noteBody = noteBody.text.toString()
                viewModel.selectedNote.noteImportant = importantType.isChecked
                viewModel.snackBarState = true
                viewModel.snackBarMessage.value = "Edited Note #${viewModel.selectedNote.noteID}"
                findNavController().navigate(R.id.action_edit_to_view)
            } else {
                var addNote = NoteFormat()
                addNote.noteTitle = noteTitle.text.toString()
                addNote.noteBody = noteBody.text.toString()
                addNote.noteImportant = importantType.isChecked

                if ((addNote.noteTitle.isEmpty()) && (addNote.noteBody.isEmpty())) {
                    addNote.noteBody = " "
                }
                viewModel.addNewNote(addNote)
                Log.println(Log.INFO, "Info", "Saved New Note")
                println("Saved New Note")
                viewModel.snackBarState = true
                viewModel.snackBarMessage.value = "Added Note #${viewModel.newNoteCounter-1}"
                findNavController().navigate(R.id.action_add_to_list)
            }
        }
        return root
    }
}