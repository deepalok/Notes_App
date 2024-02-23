package com.aydee.notesapp.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.aydee.notesapp.R
import com.aydee.notesapp.databinding.FragmentAddNoteBinding
import com.aydee.notesapp.db.NotesDBHelper
import com.aydee.notesapp.model.Notes

class AddNoteFragment : Fragment() {

    private lateinit var binding: FragmentAddNoteBinding
    private lateinit var db: NotesDBHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = FragmentAddNoteBinding.inflate(inflater, container, false)
        db = NotesDBHelper(requireContext())

        binding.btnAddNote.setOnClickListener {
            val title = binding.etAddTitle.text.toString()
            val content = binding.etAddNote.text.toString()
            val note = Notes(0, title, content)
            // add title and note to database
            if (content.isNotEmpty()) {
                if (db.addData(note)) {
                    Toast.makeText(requireContext(), "Note Added!!!", Toast.LENGTH_SHORT).show()
                    // replacing the fragment with the NotesFragment
                    val transaction = requireActivity().supportFragmentManager.beginTransaction()
                    transaction.replace(R.id.fragmentContainerView, NotesFragment()).commit()
                } else {
                    Toast.makeText(requireContext(), "Note is not Added!!!", Toast.LENGTH_SHORT)
                        .show()
                }
            } else {
                Toast.makeText(requireContext(), "Note is Empty!!!", Toast.LENGTH_SHORT).show()
            }
        }

        return binding.root
    }

    companion object {}
}