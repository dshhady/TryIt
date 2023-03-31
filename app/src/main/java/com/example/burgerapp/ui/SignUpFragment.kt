package com.example.burgerapp.ui
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.viewModels
import com.example.burgerapp.R
import com.example.burgerapp.managers.EncryptionManager
import com.example.burgerapp.model.User
import com.example.burgerapp.viewModel.UsersViewModel
import kotlinx.android.synthetic.main.fragment_sign_up.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class SignUpFragment (private val allUsers: List<User>): Fragment() {
    private val usersViewModel: UsersViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_sign_up, container, false)
    }
    override fun onStart() {
        super.onStart()
        sign_up_btn.setOnClickListener { //when the sign up button is clicked then check the validation
            onSignUpClick()
        }
        sign_in_button_fr.setOnClickListener { //when the sign in button is clicked then go back to sign in activity
            activity?.supportFragmentManager?.beginTransaction()?.remove(this)?.commit()
        }
    }



    //---------------------------sign up validation ---------------------------------
    private fun onSignUpClick() {
        val userName = user_name_edit_text_fr.text.toString()
        val email = email_edit_text_fr.text.toString()
        val password = password_edit_text_fr.text.toString()
        val passwordConfirm = confirm_password_edit_text_fr.text.toString()

        if (userName.isEmpty() || password.isEmpty() || passwordConfirm.isEmpty()|| email.isEmpty()) { //check if the fields are empty
            Toast.makeText(activity, "Please fill all fields", Toast.LENGTH_SHORT).show()
            return
        } else
            if (password != passwordConfirm) { //check if the password and the confirm password are the same
                Toast.makeText(activity, "Passwords do not match", Toast.LENGTH_SHORT).show()
                return
            }else
                if(!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()){ //check if the email is valid
                    Toast.makeText(activity, "Please enter a valid email", Toast.LENGTH_SHORT).show()
                    return
                }
        else
                for(user in allUsers){
                    if(user.userName == userName){ //check if the user name or the email already exists
                        Toast.makeText(activity, "User with this User name already exists", Toast.LENGTH_SHORT).show()
                        return
                    }
                    else
                        if(user.email == email){ //check if the user name or the email already exists
                            Toast.makeText(activity, "User with this Email already exists", Toast.LENGTH_SHORT).show()
                            return
                        }
                }
                        GlobalScope.launch (Dispatchers.IO){ usersViewModel.addUser(User(userName, email, EncryptionManager.encryption(password))) }//add the user to the database
                        Toast.makeText(activity, "User created Successfully", Toast.LENGTH_SHORT).show()
    }
}

