package com.example.notekeeper

import android.os.Bundle
import com.google.android.material.snackbar.Snackbar
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import android.view.Menu
import android.view.MenuItem
import android.widget.ArrayAdapter
import android.widget.EditText
import android.widget.Spinner
import android.widget.TextView
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
        //determine activity intent (new note or existing note from listView)
        notePosition = intent.getIntExtra(EXTRA_NOTE_POSITION, POSITION_NOT_SET)
        if(notePosition != POSITION_NOT_SET)
            displayNote(spinnerCourses)
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
        return when (item.itemId) {
            R.id.action_settings -> true
            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.nav_host_fragment_content_main)
        return navController.navigateUp(appBarConfiguration)
                || super.onSupportNavigateUp()
    }
}