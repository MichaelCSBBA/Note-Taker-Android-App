package com.example.NoteTaker

import android.os.Bundle
import android.speech.tts.TextToSpeech
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.google.android.material.chip.Chip
import java.util.*

// TODO: Rename parameter arguments, choose names that match

class FragmentViewScreen : Fragment(), TextToSpeech.OnInitListener {
    private val viewModel: MyViewModel by activityViewModels()
    companion object {
        var tTS: TextToSpeech? = null
    }
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?

    ): View? {

        tTS = TextToSpeech (activity, this)

        val root = inflater.inflate(R.layout.fragment_view_screen, container, false)

        // Text to Speech Object


        // Layout References
        var displaySelectedNote = viewModel.selectedNote
        val viewTitle = root.findViewById<TextView>(R.id.viewTitle)
        val viewBody = root.findViewById<TextView>(R.id.viewBody)
        val viewNoteNum = root.findViewById<TextView>(R.id.viewNoteId)
        val chipRed = root.findViewById<Chip>(R.id.chipImportant)
        val textToSpeechButton = root.findViewById<Button>(R.id.textToSpeechButton)

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

        // TextToSpeech Functionality


        textToSpeechButton.setOnClickListener{
            if (viewTitle.toString().isEmpty() && viewBody.toString().isEmpty()) {
                speakOut("Please enter text for Text To Speech to work!", tTS!!)
            } else {
                speakOut("The Title is: ${viewTitle.text} + ", tTS!!)
                speakOut("The Body is: ${viewBody.text}", tTS!!)
            }
        }

        // Highlights the note as red if it is important.
        if (displaySelectedNote.noteImportant) {
            chipRed.setChipBackgroundColorResource(R.color.lightRed)
        }
        return root
    }



    private fun speakOut (text: String, textTalk: TextToSpeech) {
        textTalk.speak(text, TextToSpeech.QUEUE_ADD, null, "")
    }

    override fun onInit(p0: Int) {
        val result = tTS!!.setLanguage(Locale.US)

        if (p0 == TextToSpeech.SUCCESS) {
            if (result == TextToSpeech.LANG_MISSING_DATA || result == TextToSpeech.LANG_NOT_SUPPORTED) {
                Log.e("TTS", "Language not supported/found!")
            }
        } else {
            Log.e("TTS", "TextToSpeech Failed to Start")
        }
    }
}