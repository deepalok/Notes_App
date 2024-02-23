package com.aydee.notesapp.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.aydee.notesapp.R
import com.aydee.notesapp.adapter.NotesRVAdapter
import com.aydee.notesapp.databinding.FragmentNotesBinding
import com.aydee.notesapp.db.NotesDBHelper
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInOptions

class NotesFragment : Fragment(), NotesRVAdapter.ItemClickListener {

    private lateinit var binding: FragmentNotesBinding
    private lateinit var db: NotesDBHelper
    private lateinit var notesAdapter: NotesRVAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = FragmentNotesBinding.inflate(inflater, container, false)

        email = GoogleSignIn.getLastSignedInAccount(requireContext())?.email.toString()

        db = NotesDBHelper(requireContext())

        // set Adapter
        notesAdapter = NotesRVAdapter(requireContext(), db.getAllNotes(), this)
        binding.rvNotes.layoutManager = LinearLayoutManager(requireContext())
        binding.rvNotes.adapter = notesAdapter

        // add note and display
        binding.btnAddNotes.setOnClickListener {
            // open AddNoteFragment
            val transaction = requireActivity().supportFragmentManager.beginTransaction()
            transaction.replace(R.id.fragmentContainerView, AddNoteFragment())
            transaction.addToBackStack("NotesFragment")
            transaction.setReorderingAllowed(true)
            transaction.commit()
        }

        // logout
        binding.btnLogout.setOnClickListener {
            val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id)).requestEmail().build()
            val googleSignInClient = GoogleSignIn.getClient(requireContext(), gso)

            googleSignInClient.signOut().addOnCompleteListener {
                Toast.makeText(requireContext(), "Logged Out!!!", Toast.LENGTH_SHORT).show()
                val transaction = requireActivity().supportFragmentManager.beginTransaction()
                transaction.replace(R.id.fragmentContainerView, LoginFragment())
                transaction.disallowAddToBackStack()
                transaction.commit()
            }
        }

        return binding.root
    }

    override fun onItemClick(id: Int) {
        val fragment = UpdateNoteFragment().newInstance(id)
        val transaction = requireActivity().supportFragmentManager.beginTransaction()
        transaction.replace(R.id.fragmentContainerView, fragment, "UpdateNoteFragment")
        transaction.addToBackStack(null)
        transaction.commit()
    }

    companion object{
        lateinit var email: String
    }

    override fun onResume() {
        super.onResume()
        notesAdapter.refreshData(db.getAllNotes())
    }
}