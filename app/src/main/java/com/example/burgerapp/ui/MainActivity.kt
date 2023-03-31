package com.example.burgerapp.ui
import android.os.Bundle
import android.view.Menu
import android.view.View
import android.widget.SearchView
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import com.example.burgerapp.adapters.FoodTypesAdapter
import com.example.burgerapp.R
import com.example.burgerapp.model.*
import com.example.burgerapp.adapters.searchListAdapter
import com.example.burgerapp.viewModel.CartViewModel
import com.example.burgerapp.viewModel.FoodViewModel
import com.example.burgerapp.viewModel.TypeViewModel
import kotlinx.android.synthetic.main.activity_main.*



class MainActivity : AppCompatActivity() {
    private val typeViewModel : TypeViewModel by viewModels()
    private val foodViewModel   : FoodViewModel by viewModels()
    private val cartViewModel   : CartViewModel by viewModels()
    var allFoodList : MutableList<Food> = mutableListOf() // List of the full menu

    override fun onCreateOptionsMenu(menu: Menu?): Boolean { // create About button
        menuInflater.inflate(R.menu.menu,menu)
        val aboutBtn = menu?.findItem(R.id.about_btn)
        aboutBtn!!.setOnMenuItemClickListener {
            openFragment(AboutFragment()) // open About fragment
            true
        }
        return true
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        typeViewModel.prepareTypes() // get the food types from the DB
        foodViewModel.prepareFoodData() // get the food from the DB
        var userId = intent.extras!!.getInt("userId")// get the user id from the SignIn activity
        val userType = intent.extras!!.getSerializable("userType") as USER_TYPE // get the user type (admin or user )from the SignIn activity
        userTypeCheck(userType) // check if the user is admin or user

        foodViewModel.foodData.observe(this){
            allFoodList = it.toMutableList() // get the full menu and save it in allFoodList
        }

        searchViewImplementation(userId) // search view show and implementation

        typeViewModel.typesData.observe(this) {
            createFoodTypesRecyclerView(it,userId) // create the food types recycler view
        }

        onFullMenuClick(userId) // open the full menu fragment
        bottomNavBar(userId,userType) // bottom navigation bar implementation
    }

    //---------------------Recycler View---------------------------------
    private fun createFoodTypesRecyclerView(typeList: List<Type>,userId:Int) {
        val linearLayoutManager = androidx.recyclerview.widget.LinearLayoutManager(this, androidx.recyclerview.widget.LinearLayoutManager.HORIZONTAL, false)
        val adapter = FoodTypesAdapter(typeList,onTypeClick(userId))
        recyclerViewDataTypes.adapter = adapter
        recyclerViewDataTypes.layoutManager = linearLayoutManager
    }
    //---------------------Full Menu Click Listener---------------------------------

    private fun onFullMenuClick(userId: Int){
        full_menu_btn.setOnClickListener {
            openFragment(FoodListFragment(userId,Etype.OTHER,true)) // open the full menu fragment
        }
    }

    //---------------------Admin Or User---------------------------------
    private fun userTypeCheck(userType: USER_TYPE){
        if(userType == USER_TYPE.ADMIN){ // if the user is admin
            edit_menu.visibility = View.VISIBLE // show the edit menu button
            edit_menu.setOnClickListener {
                openFragment(EditMenuFragment()) // open the edit menu fragment
            }
        }
    }

    //---------------------Search View---------------------------------
    private fun searchViewImplementation (userId: Int){
        searchView.setOnQueryTextListener( object : SearchView.OnQueryTextListener{ // search view listener
            override fun onQueryTextSubmit(query: String?): Boolean {
                return false
            }
            override fun onQueryTextChange(newText: String?): Boolean {
                recyclerViewSearch.visibility = View.VISIBLE
                filterList(newText,userId) // filter the list by the search text
                return false
            }
        }
        )
    }
    private fun filterList(newText: String?, userId: Int) { // filter the list by the search text
        var filterList : MutableList<Food> = mutableListOf()
        filterList.clear()
        for (food in allFoodList){
            if (food.name.lowercase().contains(newText.toString().lowercase())){
                filterList.add(food)
            }
        }
        if (filterList.isEmpty()){ // if there is no results
            main_scroll_view.visibility = View.VISIBLE
            recyclerViewSearch.visibility = View.GONE
        }
        else{ // if there is results
            if (filterList.size != allFoodList.size){
                main_scroll_view.visibility = View.GONE
                recyclerViewSearch.visibility = View.VISIBLE
                val adapter = searchListAdapter(this,filterList,addFoodSearchToCart(),userId)
                recyclerViewSearch.adapter = adapter
                adapter.notifyDataSetChanged()
            }
            else // if search tab is empty
            {
                main_scroll_view.visibility = View.VISIBLE
                recyclerViewSearch.visibility = View.GONE
            }
        }
    }

    private fun addFoodSearchToCart() :(cart: Cart)  -> Unit = { cart-> // add food to cart (used in the adapter)
        var flag = false
        cartViewModel.cartData.observe(this){
            flag = it.contains(cart)
        }
        if(flag){
            Toast.makeText(this,"This food is already in your cart", Toast.LENGTH_LONG).show()
        }
        else {
            cartViewModel.addFoodToCart(cart)
            Toast.makeText(this, "${cart.name} Added to cart", Toast.LENGTH_SHORT).show()
        }
    }

// ---------------------Bottom Navigation Bar and Fragments management---------------------------------
    override fun onBackPressed() { // back button
        super.onBackPressed()
        if (supportFragmentManager.backStackEntryCount == 0) {
            bottomNavigationView.selectedItemId = R.id.home_nav // if the back button is pressed in the main activity
        }
    }

    private fun onTypeClick(userId: Int)  : (Type) -> Unit = { typeItem ->
        openFragment(FoodListFragment(userId,typeItem.foodType!!,false)) // open the food list fragment
    }

    private fun popAllFragments() { // pop all the fragments
        for (i in 0 until supportFragmentManager.backStackEntryCount) {
            supportFragmentManager.popBackStack()
        }
    }

    private fun openFragment(fragment: Fragment) { // open fragment by name
        supportFragmentManager.popBackStack()
        val transaction: FragmentTransaction = supportFragmentManager.beginTransaction()
        transaction.add(R.id.container, fragment)
        transaction.addToBackStack(null)
        transaction.commit()
    }

    private fun bottomNavBar(userId: Int,userType:USER_TYPE) { // bottom navigation bar listener
        bottomNavigationView.selectedItemId = R.id.home_nav
        bottomNavigationView.setOnItemSelectedListener {
            when(it.itemId){
                R.id.profile_nav -> {
                    openFragment(ProfileFragment(userId,userType))
                    true
                }
                R.id.cart_nav -> {
                    openFragment(CartFragment(userId))
                    true
                }
                R.id.home_nav -> {
                    popAllFragments()
                    true
                }
                else -> false
            }
        }
    }







}