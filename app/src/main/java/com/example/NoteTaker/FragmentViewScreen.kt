package com.example.NoteTaker

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.google.android.material.chip.Chip

// TODO: Rename parameter arguments, choose names that match

class FragmentViewScreen : Fragment() {
    private val viewModel: MyViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val root = inflater.inflate(R.layout.fragment_view_screen, container, false)

        // Layout References
        var displaySelectedNote = viewModel.selectedNote
        val viewTitle = root.findViewById<TextView>(R.id.viewTitle)
        val viewBody = root.findViewById<TextView>(R.id.viewBody)
        val viewNoteNum = root.findViewById<TextView>(R.id.viewNoteId)
        val chipRed = root.findViewById<Chip>(R.id.chipImportant)

        // Setting Default View Screen to Note's Title, Important_flag and body.
        viewTitle.text = displaySelectedNote.noteTitle
        viewBody.text = displaySelectedNote.noteBody
        viewNoteNum.text = "Note #" + displaySelectedNote.noteID.toString()

        // Edit button layout reference and it's listener
        val editButton = root.findViewById<Button>(R.id.editButton)
        editButton.setOnClickListener {
            viewModel.editModeOrAddMode = "Edit Mode"
            println ("Edit Mode")
            findNavController().navigate(R.id.action_view_to_edit)
        }

        // Highlights the note as red if it is important.
        if (displaySelectedNote.noteImportant) {
            chipRed.setChipBackgroundColorResource(R.color.lightRed)
        }
        return root
    }
}