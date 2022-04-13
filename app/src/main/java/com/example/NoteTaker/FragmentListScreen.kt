package com.example.NoteTaker

import android.graphics.Color
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.view.*
import android.widget.*
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.view.allViews
import androidx.core.view.size
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import org.w3c.dom.Text

class FragmentListScreen : Fragment() {

    private val viewModel: MyViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        // References to Layout Widgets
        val root = inflater.inflate(R.layout.fragment_list_screen, container, false)
        val searchFilter = root.findViewById<EditText>(R.id.searchBar)
        val filterImportant = root.findViewById<Switch>(R.id.importantFilter)
        val wl = root.findViewById<LinearLayout>(R.id.noteListScroll)

        // Add Button Reference and Listener
        val buttonAdd = root.findViewById<Button>(R.id.addButton)
        buttonAdd.setOnClickListener {
            viewModel.editModeOrAddMode = "Add Mode"
            findNavController().navigate(R.id.action_list_to_add_edit)
        }

        // Random Button Reference and Listener
        val buttonRandom = root.findViewById<Button>(R.id.randomButton)
        buttonRandom.setOnClickListener {
            viewModel.addRandomNoteToList()
            searchFilter.clearFocus()
        }

        // Clear Button Reference and Listener
        val buttonClear = root.findViewById<Button>(R.id.clearButton)
        buttonClear.isEnabled = false
        buttonClear.setOnClickListener {
            viewModel.removeShownNotes()
            searchFilter.clearFocus()
        }

        // Setting of when to disable/enable clear button
        viewModel.dynamicClearLiveNoteCounter.observe(this) {
            buttonClear.isEnabled = it > 0
        }

        // Observes if the master ListofNotes has changed.
        viewModel.noteListViewModel.observe(this) { words ->
            filterImportant.isChecked = viewModel.importantFilter == "Important Filter On"
            searchFilter.setText(viewModel.searchWord)
            updateScrollList(wl, words, inflater, viewModel.importantFilter,
                viewModel.searchFilter, viewModel.searchWord)

            // Checks for the important filter change
            filterImportant.setOnCheckedChangeListener { _, isChecked ->
                if (isChecked) {
                    viewModel.importantFilter = "Important Filter On"
                    updateScrollList(wl, words, inflater, viewModel.importantFilter,
                        viewModel.searchFilter, viewModel.searchWord)
                    viewModel.subtitleNotes(viewModel.liveNoteCounter, viewModel.importantFilter, viewModel.searchFilter)
                } else {
                    viewModel.importantFilter = "Important Filter Off"
                    updateScrollList(wl, words, inflater,  viewModel.importantFilter,
                        viewModel.searchFilter, viewModel.searchWord)
                    viewModel.subtitleNotes(viewModel.liveNoteCounter, viewModel.importantFilter, viewModel.searchFilter)
                }
            }

            // Checks if anything is entered in search bar
            searchFilter.addTextChangedListener {
                if (it!!.isEmpty()) {
                    viewModel.searchFilter = "Search Filter Off"
                    viewModel.searchWord = ""
                    updateScrollList(wl, words, inflater,  viewModel.importantFilter,
                        viewModel.searchFilter, viewModel.searchWord)
                    viewModel.subtitleNotes(viewModel.liveNoteCounter, viewModel.importantFilter, viewModel.searchFilter)
                } else {
                    viewModel.searchFilter = "Search Filter On"
                    viewModel.searchWord = it.toString()
                    updateScrollList(wl, words, inflater,  viewModel.importantFilter,
                        viewModel.searchFilter, viewModel.searchWord)
                    viewModel.subtitleNotes(viewModel.liveNoteCounter, viewModel.importantFilter, viewModel.searchFilter)
                }
            }
            viewModel.subtitleNotes(viewModel.liveNoteCounter, viewModel.importantFilter, viewModel.searchFilter)
        }
        return root
    }

    // Updates the ScrollView with Notes according to Search/Important Conjunction
    fun updateScrollList (wl: LinearLayout, words: MutableList<NoteFormat>, inflater: LayoutInflater
                            ,importantOn: String, searchOn: String, searchWord: String) {
        wl.removeAllViews()
        viewModel.viewAbleNoteList.clear()
        for (w in words) {
            // inflates a view so we can create a scenegraph to add to it
            val view = inflater.inflate(R.layout.note_item, null)
            val t = view.findViewById<TextView>(R.id.noteTitle)
            val b = view.findViewById<TextView>(R.id.noteBody)
            val d = view.findViewById<Button>(R.id.deleteButton)
            val noteBar = view.findViewById<ConstraintLayout>(R.id.noteBar)
            val colourRed = resources.getColor(R.color.lightRed)

            if (w.noteImportant) {
                noteBar.setBackgroundColor(colourRed)
            }

            d.setOnClickListener {
                wl.removeView(view)
                viewModel.viewAbleNoteList.remove(w)
                viewModel.deleteNote(w)
                viewModel.noteSelected(w)
            }

            noteBar.setOnClickListener {
                println("Going to View")
                viewModel.noteSelected(w)
                findNavController().navigate(R.id.action_list_to_view)
            }
            t.text = w.noteTitle
            b.text = w.noteBody

            // Important Check
            if (importantOn == "Important Filter On") {
                if (w.noteImportant) {
                    viewModel.viewAbleNoteList.add(w)
                    wl.addView(view)
                }
            } else {
                viewModel.viewAbleNoteList.add(w)
                wl.addView(view)
            }

            // Search Check: Removes if title and body does not contain searchBar entry.
            if (searchOn == "Search Filter On") {
                if ((t.text.contains(searchWord, true)) || (b.text.contains(searchWord, true))) {
                    // do nothing view stays in list.
                } else {
                    viewModel.viewAbleNoteList.remove(w)
                    wl.removeView(view)
                }
            }
        }
        viewModel.liveNoteMatchingCounter = wl.size
    }
}