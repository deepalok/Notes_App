package com.aydee.notesapp.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.aydee.notesapp.databinding.NotesItemBinding
import com.aydee.notesapp.db.NotesDBHelper
import com.aydee.notesapp.model.Notes

class NotesRVAdapter(
    private val context: Context,
    private var notesList: MutableList<Notes>,
    private val clickListener: ItemClickListener
) : RecyclerView.Adapter<NotesRVAdapter.NotesViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NotesViewHolder {
        val binding = NotesItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return NotesViewHolder(binding)
    }

    override fun getItemCount(): Int = notesList.size

    override fun onBindViewHolder(holder: NotesViewHolder, position: Int) {
        holder.bind(notesList[position])
    }

    inner class NotesViewHolder(private val binding: NotesItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(note: Notes) {
            val db = NotesDBHelper(context)

            binding.apply {
                etTitle.setText(note.title)
                etNotes.setText(note.content)

                // update notes
                btnUpdate.setOnClickListener {
                    clickListener.onItemClick(note.id)
                }

                // delete notes
                btnDelete.setOnClickListener {
                    if (db.deleteNote(note.id)) {
                        Toast.makeText(context, "Note Deleted!!!", Toast.LENGTH_SHORT).show()
                        refreshData(db.getAllNotes())
                    } else {
                        Toast.makeText(context, "Note Not Deleted!!!", Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }
    }

    fun refreshData(newNotes: MutableList<Notes>) {
        notesList = newNotes
        notifyDataSetChanged()
    }

    interface ItemClickListener {
        fun onItemClick(id: Int)
    }
}