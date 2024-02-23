package com.aydee.notesapp.db

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.widget.Toast
import com.aydee.notesapp.fragments.NotesFragment
import com.aydee.notesapp.model.Notes


class NotesDBHelper(private val context: Context) :
    SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    val email = NotesFragment.email

    // CREATE
    override fun onCreate(db: SQLiteDatabase?) {
        val query = "CREATE TABLE $TABLE_NAME (" +
                "$ID_COL INTEGER PRIMARY KEY, " +
                "$TITLE_COL TEXT, $CONTENT_COL TEXT, $EMAIL_COL TEXT )"

        db?.execSQL(query)
    }

    override fun onUpgrade(db: SQLiteDatabase?, p1: Int, p2: Int) {
        Toast.makeText(context, "DROP TABLE", Toast.LENGTH_SHORT).show()
        val DROP_TABLE = "DROP TABLE IF EXISTS $TABLE_NAME"
        db?.execSQL(DROP_TABLE)
        onCreate(db)
    }

    // ADD
    fun addData(notes: Notes): Boolean {
        val db = this.writableDatabase
        val values = ContentValues().apply {
            put(TITLE_COL, notes.title)
            put(CONTENT_COL, notes.content)
            put(EMAIL_COL, email)
        }
        val _success = db.insert(TABLE_NAME, null, values)
        db.close()

        return (Integer.parseInt("$_success") != -1)
    }

    // READ ALL
    fun getAllNotes(): MutableList<Notes> {
        val notesList = mutableListOf<Notes>()
        val db = readableDatabase
        val mail = email
        val query = "SELECT * FROM $TABLE_NAME WHERE $EMAIL_COL = '" + mail + "';"
        val cursor = db.rawQuery(query, null)

        while (cursor.moveToNext()) {
            val id = cursor.getInt(cursor.getColumnIndexOrThrow(ID_COL))
            val title = cursor.getString(cursor.getColumnIndexOrThrow(TITLE_COL))
            val content = cursor.getString(cursor.getColumnIndexOrThrow(CONTENT_COL))
            val note = Notes(id, title, content)
            notesList.add(note)
        }
        cursor.close()
        db.close()
        return notesList
    }

    // READ SINGLE BY ID
    fun getNoteById(noteId: Int): Notes {
        val db = readableDatabase
        val query = "SELECT * FROM $TABLE_NAME WHERE $ID_COL = $noteId"
        val cursor = db.rawQuery(query, null)
        cursor.moveToFirst()

        val title = cursor.getString(cursor.getColumnIndexOrThrow(TITLE_COL))
        val content = cursor.getString(cursor.getColumnIndexOrThrow(CONTENT_COL))

        cursor.close()
        db.close()
        return Notes(noteId, title, content)
    }

    // UPDATE
    fun updateNote(note: Notes): Boolean {
        val db = writableDatabase
        val values = ContentValues().apply {
            put(TITLE_COL, note.title)
            put(CONTENT_COL, note.content)
        }
        val whereClause = "$ID_COL = ?"
        val whereArgs = arrayOf(note.id.toString())
        val _success = db.update(TABLE_NAME, values, whereClause, whereArgs).toLong()
        db.close()
        return (Integer.parseInt("$_success") != -1)
    }

    // DELETE
    fun deleteNote(id: Int): Boolean {
        val db = writableDatabase
        val whereClause = "$ID_COL = ?"
        val whereArgs = arrayOf(id.toString())
        val _success = db.delete(TABLE_NAME, whereClause, whereArgs).toLong()
        db.close()
        return (Integer.parseInt("$_success") != -1)
    }

    companion object {
        private val DATABASE_NAME = "NOTES_DATABASE"
        val DATABASE_VERSION = 1
        var TABLE_NAME = "NotesTable"
        val ID_COL = "Id"
        val TITLE_COL = "Title"
        val CONTENT_COL = "Notes"
        val EMAIL_COL = "Email"
    }
}
//"NotesTable"