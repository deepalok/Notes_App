package com.aydee.notesapp.fragments

import android.app.Activity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import com.aydee.notesapp.R
import com.aydee.notesapp.databinding.FragmentLoginBinding
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions

class LoginFragment : Fragment() {

    private lateinit var binding: FragmentLoginBinding
    private lateinit var googleSignInClient: GoogleSignInClient

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = FragmentLoginBinding.inflate(inflater, container, false)

        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken("335228815283-7vo99u5alledtp2a7i4kbd6mrmqvsnfc.apps.googleusercontent.com")
            .requestEmail().build()

        googleSignInClient = GoogleSignIn.getClient(requireContext(), gso)

        binding.google.setOnClickListener {
            val signInIntent = googleSignInClient.signInIntent
            launcher.launch(signInIntent)
        }

        return binding.root
    }

    val launcher = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            val task = GoogleSignIn.getSignedInAccountFromIntent(result.data)
            if (task.isSuccessful) {
                val account: GoogleSignInAccount = task.result
                login()
            } else {
                Toast.makeText(requireContext(), "Login Failed !!!", Toast.LENGTH_SHORT).show()
            }
        }
    }

    fun login() {
        val transaction = requireActivity().supportFragmentManager.beginTransaction()
        transaction.replace(R.id.fragmentContainerView, NotesFragment())
        transaction.disallowAddToBackStack()
        transaction.commit()
    }
}