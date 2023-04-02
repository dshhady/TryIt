package com.example.burgerapp.ui
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.viewModels
import androidx.core.content.res.ResourcesCompat
import com.example.burgerapp.R
import com.example.burgerapp.managers.ValidationManager
import com.example.burgerapp.model.USER_TYPE
import com.example.burgerapp.model.User
import com.example.burgerapp.viewModel.UsersViewModel
import kotlinx.android.synthetic.main.activity_sign_in.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import www.sanju.motiontoast.MotionToast
import www.sanju.motiontoast.MotionToastStyle




class SignInActivity : AppCompatActivity() {

     private val usersViewModel : UsersViewModel by viewModels()
     var userId : Int = 0 //user id will be used to get the user data from the database
     private lateinit var userType : USER_TYPE //user type will be used to check if the user is admin or not
     private val adminUser = User("admin","admin@gmail.com",ValidationManager.encryption("1234"), userType = USER_TYPE.ADMIN) //userId will be 1
     private lateinit var sharedPreferences: SharedPreferences



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_in)
        GlobalScope.launch(Dispatchers.IO) {
            usersViewModel.addUserIfNotExist(adminUser) //add admin user automatically when the app is installed
        }
        sharedPreferences = getSharedPreferences(R.string.app_name.toString(), MODE_PRIVATE)

        calculateLastLogin() //check if the user had already signed in or not

    }
  //----------- check if the user had already signed in for the last hour or not ----------------
    private fun calculateLastLogin(){
        val lastLogin = sharedPreferences.getLong("LAST_LOGIN", -1)
        val lastId = sharedPreferences.getInt("USER_ID", -1)
        if(lastLogin != -1L &&  System.currentTimeMillis() - lastLogin < 3600000 ) //one hour
            goToMainActivityDirectly(lastId) //
        else
            setOnSingInUpListener()
    }

    private fun setOnSingInUpListener() {
        log_in_btn.setOnClickListener {
            isUserValid()  //check if the user is valid or not
        }
        sign_up_button.setOnClickListener {
            onSignUpClick() //go to sign up fragment
        }
    }


    //---------------------------go to sign up fragment ---------------------------------
    private fun onSignUpClick() { //go to sign up fragment
        usersViewModel.usersData.observe(this) {
            supportFragmentManager.beginTransaction()
                .replace(R.id.sign_up_fragment_container, SignUpFragment(it))
                .commit()
        }
    }
    //---------------------------check if the user validation ---------------------------
    private fun isUserValid() {
            usersViewModel.usersData.observe(this) {
                if (userCheck(it)) {  //if the user is valid then go to main activity
                    goToMainActivity()
                    userName_or_Email_edit_text.text.clear()
                    password_edit_text.text.clear()
                } else {
                    userName_or_Email_edit_text.text.clear() //clear the edit text if the user is not valid
                    password_edit_text.text.clear()
                }
            }
        }

    private fun userCheck(users: List<User>) : Boolean { // check if the user is valid or not
        var flag = false
        for (user in users) {
            if ((user.userName == userName_or_Email_edit_text.text.toString() ||
                        user.email == userName_or_Email_edit_text.text.toString()) &&
                ValidationManager.comparePasswordEncrypt(user.password, password_edit_text.text.toString())) {
                flag = true
                userId = user.id
            }
        }
        if (!flag)
            errorMotionToast() //show error toast if the user is not valid
        else
            successMotionToast() //show success toast if the user is valid
        return flag //return true if the user is valid
    }
    private fun goToMainActivityDirectly(lastId: Int) {
        userId = lastId
        userType = if (userId == 1) USER_TYPE.ADMIN else USER_TYPE.USER
        val intent = Intent(this, MainActivity::class.java)
        intent.putExtra("userId", userId)
        intent.putExtra("userType", userType)
        startActivity(intent)
    }

    private fun goToMainActivity() { //go to main activity
        sharedPreferences.edit().putLong("LAST_LOGIN", System.currentTimeMillis()).apply()
        sharedPreferences.edit().putInt("USER_ID", userId).apply()
        userType = if (userId == 1) USER_TYPE.ADMIN else USER_TYPE.USER
        val intent = Intent(this, MainActivity::class.java)
        intent.putExtra("userId", userId)
        intent.putExtra("userType", userType)
             startActivity(intent)
    }


    //----- Display Motion Toasts successes or fail --------------------------------------
    private fun errorMotionToast(){
        MotionToast.darkColorToast(
            this,
            getString(R.string.try_again),
            getString(R.string.signIn_error),
            MotionToastStyle.ERROR,
            MotionToast.GRAVITY_BOTTOM or MotionToast.GRAVITY_BOTTOM,
            MotionToast.LONG_DURATION,
            ResourcesCompat.getFont(this,R.font.circular)
        )
    }
    private fun successMotionToast(){
        MotionToast.darkColorToast(
            this,
            getString(R.string.success),
            getString(R.string.signIn_success),
            MotionToastStyle.SUCCESS,
            MotionToast.GRAVITY_BOTTOM or MotionToast.GRAVITY_BOTTOM,
            MotionToast.LONG_DURATION,
            ResourcesCompat.getFont(this,R.font.circular)
        )
    }

}
