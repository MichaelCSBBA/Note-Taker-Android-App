package com.example.NoteTaker

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.*
import android.widget.LinearLayout

import androidx.activity.viewModels
import androidx.appcompat.app.ActionBar
import androidx.constraintlayout.widget.ConstraintLayout
import com.google.android.material.snackbar.Snackbar

class MainActivity : AppCompatActivity() {

    // create view model using delegation
    private val viewModel: MyViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        var topActionBar = supportActionBar
        setContentView(R.layout.activity_main)

        // viewModel Observations for snackBar and subtitle NoteCounter
        viewModel.snackBarMessage.observe(this) {
            if (viewModel.snackBarState) {
                var mainView = findViewById<ConstraintLayout>(R.id.MainActivity)
                var bottomSnackBar = Snackbar.make(mainView, it, 1500)
                bottomSnackBar.show()
            }
            viewModel.snackBarState = false
        }

        viewModel.dynamicLiveNoteCounter.observe(this) {
            topActionBar!!.subtitle = it.toString()
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        // uncomment line below to add ActionBar options menu
        menuInflater.inflate(R.menu.action_menu, menu)
        // (menu is defined in "res/menus/action_menu.xml")
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        return when (item.itemId) {
            R.id.actionName -> {
                // update viewModel for this action
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }
}




