package com.example.burgerapp.ui
import android.content.Intent
import android.content.SharedPreferences
import android.net.Uri
import android.os.Bundle
import android.text.InputType
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.Toast
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.viewModels
import com.bumptech.glide.Glide
import com.example.burgerapp.R
import com.example.burgerapp.api.ApiInterface
import com.example.burgerapp.api.ApiResponseHitsList
import com.example.burgerapp.managers.EncryptionManager
import com.example.burgerapp.model.USER_TYPE
import com.example.burgerapp.model.User
import com.example.burgerapp.viewModel.UsersViewModel
import kotlinx.android.synthetic.main.fragment_profile.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Response


class ProfileFragment(private val userId : Int, private val userType: USER_TYPE) : Fragment() {

    val usersViewModel : UsersViewModel by viewModels()
    lateinit var currentUser: User // the current user
    private lateinit var sharedPreferences: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        GlobalScope.launch(Dispatchers.IO) {
             currentUser = usersViewModel.getUserById(userId)  // get the current user by id
        }
        sharedPreferences = requireActivity().getSharedPreferences(R.string.app_name.toString(), AppCompatActivity.MODE_PRIVATE)
    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_profile, container, false)
    }

    override fun onStart() {
        super.onStart()
        showProfileData() // show the current user data
        editDataListener(userId) // edit the current user data
        logOutListener() // log out the current user
    }

    private fun logOutListener() {
        log_out_btn.setOnClickListener {
            val intent = Intent(activity, SignInActivity::class.java)
            startActivity(intent)
            (activity as AppCompatActivity).finish()
            sharedPreferences.edit().putLong("LAST_LOGIN", -1).apply()
        }

    }

    private fun showProfileData() {
        user_name_tv.text = currentUser.userName
        email_tv.text = currentUser.email
        if (currentUser.image != null) {
            Glide.with(this@ProfileFragment).load(currentUser.image).into(profile_image_iv)
        }
    }

    //------------------ on Edit Button Listeners --------------------------------//
    private fun editDataListener(userId: Int) {
        edit_user_name_btn.setOnClickListener {
            if (userType == USER_TYPE.ADMIN)
                Toast.makeText(requireActivity(), "Admin can't edit user name", Toast.LENGTH_SHORT).show()
                    else
             editProfileUserNameAlertDialog(userId)
        }
        edit_email_btn.setOnClickListener {
            if (userType == USER_TYPE.ADMIN)
                Toast.makeText(requireActivity(), "Admin can't edit email", Toast.LENGTH_SHORT).show()
            else
            editProfileEmailAlertDialog(userId)
        }
        edit_password_btn.setOnClickListener {
            if (userType == USER_TYPE.ADMIN)
                Toast.makeText(requireActivity(), "Admin can't edit password", Toast.LENGTH_SHORT).show()
            else
            editProfilePasswordAlertDialog(userId)
        }
        change_profile_image_tv.setOnClickListener {
            chooseProfileImageAPIorGallery()
        }
    }

    //------------------ Edit Profile Data Alert Dialogs --------------------------------//
    private fun editProfileEmailAlertDialog(userId: Int) {
        val builder = AlertDialog.Builder(requireActivity())
        builder.setTitle("Edit")
        builder.setMessage("Please enter your new email")
        val input = EditText(requireActivity())
        input.inputType = InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS
        builder.setView(input)
        builder.setPositiveButton("OK") { dialog, which ->
            usersViewModel.updateUserEmail(userId, input.text.toString())
            currentUser.email = input.text.toString()
            email_tv.text = input.text.toString()
            Toast.makeText(activity, "Email changed", Toast.LENGTH_SHORT).show()
        }
        builder.setNegativeButton("Cancel") { dialog, which -> dialog.cancel() }
        builder.show()
    }
    private fun editProfileUserNameAlertDialog(userId: Int) {
        val builder = AlertDialog.Builder(requireActivity())
        builder.setTitle("Edit")
        builder.setMessage("Please enter your new user name")
        val input = EditText(requireActivity())
        input.inputType = InputType.TYPE_CLASS_TEXT
        builder.setView(input)
        builder.setPositiveButton("OK") { dialog, which ->
            usersViewModel.updateUserName(userId, input.text.toString())
            currentUser.userName = input.text.toString()
            user_name_tv.text = input.text.toString()
            Toast.makeText(activity, "User name changed", Toast.LENGTH_SHORT).show()
        }
        builder.setNegativeButton("Cancel") { dialog, which -> dialog.cancel() }
        builder.show()
    }
    private fun editProfilePasswordAlertDialog(userId: Int) {
        val builder = AlertDialog.Builder(requireActivity())
        builder.setTitle("Edit")
        builder.setMessage("Please enter your new password")
        val input = EditText(requireActivity())
        input.inputType = InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_PASSWORD
        builder.setView(input)
        builder.setPositiveButton("OK") { dialog, which ->
            usersViewModel.updateUserPassword(userId, EncryptionManager.encryption(input.text.toString()))
            currentUser.password = input.text.toString()
            password_tv.text = input.text.toString()
            Toast.makeText(activity, "Password changed", Toast.LENGTH_SHORT).show()
            }
        builder.setNegativeButton("Cancel") { dialog, which -> dialog.cancel() }
        builder.show()
    }

    //------------------Choose profile image from API or Gallery--------------------------------//
    private fun chooseProfileImageAPIorGallery() {
        val builder = AlertDialog.Builder(requireActivity())
        builder.setTitle("Choose")
        builder.setMessage("Please choose your profile image from API or Gallery")
        builder.setPositiveButton("API") { dialog, which ->
            getImageFromApi()
        }
        builder.setNegativeButton("Gallery") { dialog, which ->
            getImageFromGallery()
        }
        builder.show()
    }
   //------------------Get image from API--------------------------------//
    fun getImageFromApi() {
        val retrofit = ApiInterface.create()
        retrofit.getImages("profile").enqueue(object : retrofit2.Callback<ApiResponseHitsList>{
            override fun onResponse(call: Call<ApiResponseHitsList>, response: Response<ApiResponseHitsList>) {
                val apiResponse  = response.body()
                apiResponse!!.imagesList.shuffled()
                val apiImage = apiResponse!!.imagesList.random()
                currentUser.image = apiImage.imageUrl
                usersViewModel.updateUserImage(userId, apiImage.imageUrl)
                Glide.with(this@ProfileFragment).load(apiImage.imageUrl).into(profile_image_iv)
                Log.e("api response", "onResponse: ${apiImage.imageUrl}")
            }

            override fun onFailure(call: Call<ApiResponseHitsList>, t: Throwable) {
                Log.e("Wrong api response", "onFailure: ${t.message}")
            }
        })

    }
    //------------------Get image from Gallery--------------------------------//
    val getContent = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            onImageResultFromGallery(result)
    }
    private fun onImageResultFromGallery(result: ActivityResult) {
        if (result.resultCode == AppCompatActivity.RESULT_OK) {
            val uri = result.data?.data
            if (uri != null) {
                requireActivity().contentResolver.takePersistableUriPermission(
                    uri,
                    Intent.FLAG_GRANT_READ_URI_PERMISSION)
                usersViewModel.updateUserImage(currentUser.id, uri.toString())
                profile_image_iv.setImageURI(Uri.parse(uri.toString()))
            }
        }
    }
    fun getImageFromGallery() {
        val intent = Intent(Intent.ACTION_OPEN_DOCUMENT).apply {
            type = "image/*"
        }
        getContent.launch(intent)
    }
}