package com.example.notekeeper

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import android.view.Menu
import android.view.MenuItem
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.Spinner
import com.example.notekeeper.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var binding: ActivityMainBinding
    private var notePosition = POSITION_NOT_SET

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        /*Alternative
        * setContentView(R.layout.activity_main*/

        setSupportActionBar(binding.toolbar)

        val navController = findNavController(R.id.nav_host_fragment_content_main)
        appBarConfiguration = AppBarConfiguration(navController.graph)
        setupActionBarWithNavController(navController, appBarConfiguration)

        //Create Spinner Adapter Layout~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
        val adapterCourses = ArrayAdapter<CourseInfo>(this,
            android.R.layout.simple_spinner_item,
            DataManager.courses.values.toList()
        )
        adapterCourses.setDropDownViewResource(
            android.R.layout.simple_spinner_dropdown_item
        )
        val spinnerCourses: Spinner = findViewById(R.id.spinnerCourses)
        spinnerCourses.adapter = adapterCourses
        //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
        //Set Home Button action
        val buttonFirst: Button = findViewById(R.id.button_first)
        buttonFirst.setOnClickListener { view ->
            val activityIntent = Intent(this, NoteListActivity::class.java)
            startActivity(activityIntent)
        }
        //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
        //determine activity intent (new note or existing note from listView)
        notePosition = savedInstanceState?.getInt(NOTE_POSITION, POSITION_NOT_SET) ?://if savedInstanceState null
            intent.getIntExtra(NOTE_POSITION, POSITION_NOT_SET)//get NOTE_POSITION from here!^
        if(notePosition != POSITION_NOT_SET)
            displayNote(spinnerCourses)
        else {
            DataManager.notes.add(NoteInfo())
            notePosition = DataManager.notes.lastIndex
        }
        //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    }

    private fun displayNote(spinnerCourses: Spinner) {
        val note = DataManager.notes[notePosition]

        val textNoteTitle: EditText = findViewById(R.id.textNoteTitle)
        textNoteTitle.setText(note.title)

        val textNoteText: EditText = findViewById(R.id.textNoteText)
        textNoteText.setText(note.text)

        val coursePosition = DataManager.courses.values.indexOf(note.course)
        spinnerCourses.setSelection(coursePosition)
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        val spinnerCourses: Spinner = findViewById(R.id.spinnerCourses)
        return when (item.itemId) {
            R.id.action_settings -> true
            R.id.action_next -> {
                moveNext(spinnerCourses)
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun moveNext(spinnerCourses: Spinner) {
        ++notePosition
        displayNote(spinnerCourses)
        invalidateOptionsMenu()
    }

    override fun onPrepareOptionsMenu(menu: Menu?): Boolean {
        if(notePosition >= DataManager.notes.lastIndex){
            val menuItem = menu?.findItem(R.id.action_next)
            if(menuItem != null){
                menuItem.icon = getDrawable(R.drawable.ic_baseline_block_white_24)
                menuItem.isEnabled = false
            }
        }
        return super.onPrepareOptionsMenu(menu)
    }

    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.nav_host_fragment_content_main)
        return navController.navigateUp(appBarConfiguration)
                || super.onSupportNavigateUp()
    }

    override fun onPause() {
        super.onPause()
        saveNote()
    }

    private fun saveNote() {
        val note = DataManager.notes[notePosition]

        val textNoteTitle: EditText = findViewById(R.id.textNoteTitle)
        note.title = textNoteTitle.text.toString()

        val textNoteText: EditText = findViewById(R.id.textNoteText)
        note.text = textNoteText.text.toString()

        val spinnerCourses: Spinner = findViewById(R.id.spinnerCourses)
        note.course = spinnerCourses.selectedItem as CourseInfo
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putInt(NOTE_POSITION, notePosition)
    }
}