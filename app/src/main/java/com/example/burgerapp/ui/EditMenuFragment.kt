package com.example.burgerapp.ui

import android.content.Intent
import android.os.Bundle
import android.text.InputType
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
import com.example.burgerapp.adapters.MenuAdapter
import com.example.burgerapp.R
import com.example.burgerapp.model.Etype
import com.example.burgerapp.model.Food
import com.example.burgerapp.model.ImageType
import com.example.burgerapp.viewModel.FoodViewModel
import kotlinx.android.synthetic.main.fragment_edit_menu.*


class EditMenuFragment : Fragment() {
    private val foodViewModel : FoodViewModel by viewModels()
    private var foodToAdd: Food = Food(name = "default", price = 0, imageRes = "default", foodType = Etype.OTHER, imageType = ImageType.GALLERY, calories = 0) // default food
    private val foodTypesArray = arrayOf("Burger", "Pizza", "Shwarma", "Flafel") // food types array

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_edit_menu, container, false)
    }

    override fun onStart() {
        super.onStart()
        editMenuPageImplementation()
    }

    //--------------------- Page Implementation and recycler view ----------------------------------

    private fun editMenuPageImplementation(){
        foodViewModel.foodData.observe(this){ it ->
            createFoodRecyclerView(it.toMutableList())
        }
        add_new_food_btn.setOnClickListener {
            add_new_food_btn.visibility = View.GONE
            add_new_food_layout.visibility = View.VISIBLE
            add_Food_finally_btn.visibility = View.VISIBLE
            addNewFood()
        }
        add_Food_finally_btn.setOnClickListener {
            if (foodToAdd.name == "default" || foodToAdd.price == 0 || foodToAdd.imageRes == "default" || foodToAdd.foodType == Etype.OTHER || foodToAdd.calories == 0){
                Toast.makeText(requireContext(), "Please fill all the fields", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }else{
                foodViewModel.addFood(foodToAdd)
                add_new_food_btn.visibility = View.VISIBLE
                add_new_food_layout.visibility = View.GONE
                add_Food_finally_btn.visibility = View.GONE
                Toast.makeText(requireContext(), "Food added successfully", Toast.LENGTH_SHORT).show()
                //restart the fragment to clear the fields
                foodToAdd = Food(name = "default", price = 0, imageRes = "default", foodType = Etype.OTHER, imageType = ImageType.GALLERY, calories = 0)
                requireActivity().supportFragmentManager.beginTransaction().detach(this).commit()
                requireActivity().supportFragmentManager.beginTransaction().attach(this).commit()
            }
        }
    }

    private fun createFoodRecyclerView(foodList: MutableList<Food>) {
        val adapter = MenuAdapter(requireContext(), foodList)
        edit_menu_recycler_view.adapter = adapter
    }

    //--------------------- Add New Food By filed  ----------------------------------
    private fun addNewFood() {
        var filed = ""
        food_type_to_add.setOnClickListener {
            food_type_to_add.text = ""
            filed = "type"
            addFoodTypeAlertDialogAddingList()
        }
        name_food_to_add.setOnClickListener {
            name_food_to_add.text = ""
            filed = "name"
            addFoodFieldAlertDialog(filed)
        }
        price_food_to_add.setOnClickListener {
            price_food_to_add.text = ""
            filed = "price"
            addFoodFieldAlertDialog(filed)
        }
        calories_food_to_add.setOnClickListener {
            calories_food_to_add.text = ""
            filed = "calories"
            addFoodFieldAlertDialog(filed)
        }
        image_food_to_add.setOnClickListener {
            getImageFromGallery()
        }
    }

    //--------------------- Add New Food Type  ----------------------------------
    private fun addFoodTypeAlertDialogAddingList() {
        val builder = AlertDialog.Builder(requireContext())
        builder.setTitle("Enter food type")
        builder.setItems(foodTypesArray) { dialog, which ->
            when(which){
                0 -> {
                    food_type_to_add.text = "Burger"
                    foodToAdd.foodType = Etype.BURGER
                }
                1 -> {
                    food_type_to_add.text = "Pizza"
                    foodToAdd.foodType = Etype.PIZZA
                }
                2 -> {
                    food_type_to_add.text = "Shwarma"
                    foodToAdd.foodType = Etype.SHAWARMA
                }
                3 -> {
                    food_type_to_add.text = "Falafel"
                    foodToAdd.foodType = Etype.FALAFEL
                }
            }
        }
        builder.setNegativeButton("Cancel") { dialog, which ->
            food_type_to_add.text = "Food Type"

            dialog.cancel() }
        builder.show()
    }

    //--------------------- Add New Food name, price, calories  ----------------------------------
    private fun addFoodFieldAlertDialog(field:String) {
        val builder = AlertDialog.Builder(requireContext())
        builder.setTitle("Enter food $field")
        val input = EditText(requireContext())
        when(field){
            "name" -> input.inputType = InputType.TYPE_CLASS_TEXT
            "price" -> input.inputType = InputType.TYPE_CLASS_NUMBER
            "calories" -> input.inputType = InputType.TYPE_CLASS_NUMBER
        }
        builder.setView(input)
        builder.setPositiveButton("OK") { dialog, which ->
            when(field){
                "name" -> {
                    name_food_to_add.text = input.text
                    foodToAdd.name = input.text.toString()
                }
                "price" ->{
                    price_food_to_add.text = input.text.toString()+" ₪"
                    foodToAdd.price = input.text.toString().toInt()
                }
                "calories" -> {
                    calories_food_to_add.text = input.text.toString()+" kcal"
                    foodToAdd.calories = input.text.toString().toInt()
                }
            }
        }
        builder.setNegativeButton("Cancel") { dialog, which ->
            when(field){
                "name" -> name_food_to_add.text = "Food Name"
                "price" -> price_food_to_add.text = "Price"
                "calories" -> calories_food_to_add.text = "Calories"
            }

            dialog.cancel() }
        builder.show()
    }


    //--------------------- Add New Food Image From Gallery ----------------------------------
    private val getContent =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            onImageResultFromGallery(result)
        }

    private fun onImageResultFromGallery(result: ActivityResult) {
        if (result.resultCode == AppCompatActivity.RESULT_OK) {
            val uri = result.data?.data
            if (uri != null) {
               requireActivity().contentResolver.takePersistableUriPermission(
                    uri,
                    Intent.FLAG_GRANT_READ_URI_PERMISSION)
                Glide.with(requireContext()).load(uri).into(image_food_to_add)
                foodToAdd.imageRes = uri.toString()
                image_food_to_add.imageTintMode = null
            }
        }
    }

    private fun getImageFromGallery() {
        val intent = Intent(Intent.ACTION_OPEN_DOCUMENT)
        intent.type = "image/*"
        getContent.launch(intent)
    }


}