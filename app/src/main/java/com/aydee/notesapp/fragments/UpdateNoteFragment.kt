package com.aydee.notesapp.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.aydee.notesapp.R
import com.aydee.notesapp.databinding.FragmentUpdateNoteBinding
import com.aydee.notesapp.db.NotesDBHelper
import com.aydee.notesapp.model.Notes

private const val ARG_PARAM1 = "param1"

class UpdateNoteFragment : Fragment() {
    private lateinit var binding: FragmentUpdateNoteBinding

    private var param1: Int = 0

    fun newInstance(param1: Int) =
        UpdateNoteFragment().apply {
            arguments = Bundle().apply {
                putInt(ARG_PARAM1, param1)
            }
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getInt(ARG_PARAM1)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = FragmentUpdateNoteBinding.inflate(inflater, container, false)
        val db = NotesDBHelper(requireContext())
        val note = db.getNoteById(param1)

        binding.etUpdateTitle.setText(note.title)
        binding.etUpdateNote.setText(note.content)

        binding.btnUpdateNotes.setOnClickListener {
            val newTitle = binding.etUpdateTitle.text.toString()
            val newContent = binding.etUpdateNote.text.toString()
            val newNote = Notes(param1, newTitle, newContent)

            if (db.updateNote(newNote)) {
                Toast.makeText(context, "Note Updated!!!", Toast.LENGTH_SHORT).show()
                // replacing the fragment with the NotesFragment
                val transaction = requireActivity().supportFragmentManager.beginTransaction()
                transaction.replace(R.id.fragmentContainerView, NotesFragment()).commit()
            } else {
                Toast.makeText(context, "Note Not Updated!!!", Toast.LENGTH_SHORT).show()
            }
        }

        return binding.root
    }

}