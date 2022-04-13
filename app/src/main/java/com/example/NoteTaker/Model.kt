package com.example.NoteTaker

import com.example.NoteTaker.LoremIpsum.Companion.getRandomSequence
import kotlin.random.Random

class Model {

    init {
    }

    // ListOfNote stored
    val listOfNotes = mutableListOf<NoteFormat>()

    // Creates a random word, this code is same as original
    fun addRandomNote(): NoteFormat {
        val newNote = NoteFormat()
        val importantNum = Random.nextInt(1, 5)

        newNote.noteTitle = getRandomSequence(Random.nextInt(1, 4))
            .split(" ")
            .joinToString(" ") { it.replaceFirstChar { it.uppercase() } }

        for (i in 0..Random.nextInt(2, 5)) {
            val sentence = LoremIpsum.getRandomSequence(Random.nextInt(3, 10))
                .replaceFirstChar { it.uppercase() }
                .plus(". ")
            newNote.noteBody = newNote.noteBody.plus(sentence)
        }

        if (importantNum == 2) {
            newNote.noteImportant = true
        }
        return newNote
    }
}