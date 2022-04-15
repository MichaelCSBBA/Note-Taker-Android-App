package com.example.NoteTaker

import android.Manifest
import android.app.Activity
import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.speech.RecognitionListener
import android.speech.RecognizerIntent
import android.util.Log
import android.view.*
import android.widget.*
import androidx.activity.result.ActivityResult
import androidx.activity.result.ActivityResultLauncher
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import java.util.*

class FragmentAddScreen : Fragment() {
    companion object {
        private const val REQUEST_CODE_STT = 1
        lateinit var noteBody: EditText
        lateinit var noteTitle: EditText
    }
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
        noteTitle = root.findViewById<EditText>(R.id.viewTitle)
        noteBody = root.findViewById<EditText>(R.id.viewBody)
        val saveButton = root.findViewById<Button>(R.id.saveButton)
        val speechToText = root.findViewById<Button> (R.id.speechToTextButton)

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


        speechToText.setOnClickListener {
            // Get the Intent action
            val speechTextIntent = Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH)
            speechTextIntent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM)
            speechTextIntent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault())
            speechTextIntent.putExtra(RecognizerIntent.EXTRA_PROMPT, "Speak now!")
            try {
                startActivityForResult(speechTextIntent, REQUEST_CODE_STT)
            } catch (e: ActivityNotFoundException) {
                e.printStackTrace()
                Toast.makeText(activity, "Your device does not support STT.", Toast.LENGTH_LONG).show()
            }
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

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        when (requestCode) {
            // Handle the result for our request code.
            REQUEST_CODE_STT -> {
                // Safety checks to ensure data is available.
                if (resultCode == Activity.RESULT_OK && data != null) {
                    // Retrieve the result array.
                    val result = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS)
                    // Ensure result array is not null or empty to avoid errors.
                    if (!result.isNullOrEmpty()) {
                        // Recognized text is in the first position.
                        var recognizedText = result[0]
                        // Formatting the text into sentences..
                        var oldBodyText = noteBody.text.toString()
                        var oldTitleText = noteTitle.text.toString()
                        if (noteBody.hasFocus()) {
                            recognizedText = recognizedText.replaceFirstChar { it.uppercase() }
                            oldBodyText += recognizedText + ". "
                            noteBody.setText(oldBodyText)
                        } else {
                            recognizedText = recognizedText.split(" ")
                                .joinToString(" ") { it.replaceFirstChar { it.uppercase() } }
                            oldTitleText += recognizedText + " "
                            noteTitle.setText (oldTitleText)
                        }
                    }
                }
            }
        }
    }
}