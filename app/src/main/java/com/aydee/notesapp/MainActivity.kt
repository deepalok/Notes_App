package com.aydee.notesapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.aydee.notesapp.databinding.ActivityMainBinding
import com.aydee.notesapp.db.NotesDBHelper
import com.aydee.notesapp.fragments.LoginFragment
import com.aydee.notesapp.fragments.NotesFragment
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInOptions

class MainActivity : AppCompatActivity() {

    private val binding: ActivityMainBinding by lazy {
        ActivityMainBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        val account = GoogleSignIn.getLastSignedInAccount(this)

        // if account is already there then directly open NotesFragment
        if(account != null){
            openFragment(NotesFragment())
        }
        // if account is not there then open LoginFragment
        else{
            openFragment(LoginFragment())
        }
    }

    // open fragment
    private fun openFragment(fragment: Fragment){
        val transaction = this.supportFragmentManager.beginTransaction()
        transaction.replace(R.id.fragmentContainerView, fragment)
        transaction.commit()
    }

}