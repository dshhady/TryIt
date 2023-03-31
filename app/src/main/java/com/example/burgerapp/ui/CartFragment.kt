package com.example.burgerapp.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.burgerapp.adapters.CartAdapter
import com.example.burgerapp.R
import com.example.burgerapp.model.Cart
import com.example.burgerapp.viewModel.CartViewModel
import kotlinx.android.synthetic.main.fragment_cart.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch




class CartFragment(private var userId : Int ) : Fragment() {

    private val cartViewModel : CartViewModel by viewModels()
    private var totalPrice = 0
    lateinit var cartList : LiveData<List<Cart>>
        override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_cart, container, false)
        }

    override fun onStart() {
        super.onStart()
        cartDataDisplay() // display the cart data
        btnOrder_fr.setOnClickListener {
            if (totalPrice == 0) // if the cart is empty
                Toast.makeText(requireActivity(), "Your cart is empty", Toast.LENGTH_SHORT).show()
            else
                checkOutAlertDialog()
        }
    }



    fun checkCartIsEmpty() : Boolean{
        cartList = MutableLiveData()
        GlobalScope.launch (Dispatchers.IO) {
             cartList = cartViewModel.getCartByUserId(userId)
        }
        return cartList.value.isNullOrEmpty()
    }

    //---------------------Check Out Alert Dialog---------------------------------
    private fun checkOutAlertDialog() {
        val builder = android.app.AlertDialog.Builder(requireActivity())
        builder.setTitle("Check Out")
        builder.setMessage("Are you sure you want to check out?")
        builder.setPositiveButton("Yes"){dialog, which ->
            GlobalScope.launch (Dispatchers.IO){
               cartViewModel.deleteAllCartByUserId(userId)
                val childFragment = CheckOutFragment(totalPrice)
                val transaction = requireActivity().supportFragmentManager.beginTransaction()
                transaction.replace(R.id.fragment_cart, childFragment)
                transaction.addToBackStack(null)
                transaction.commit()
            }
        }
        builder.setNegativeButton("No"){dialog,which ->
            dialog.dismiss()
        }
        val dialog: android.app.AlertDialog = builder.create()
        dialog.show()
    }

    //---------------------Cart Data Display and Recycler View---------------------------------
    private fun cartDataDisplay(){
        txtTotalPrice_fr.text = "Total Price: "+totalPrice.toString()+"₪"
        cartViewModel.getCartByUserId(userId!!).observe(this){
            createCartRecyclerView(it.toMutableList())
            totalPrice = 0
            for (cart in it){
                totalPrice += cart.price * cart.quantity
            }
            txtTotalPrice_fr.text = "Total Price: "+totalPrice.toString()+"₪"
        }
    }
    private fun createCartRecyclerView(cartList: MutableList<Cart>) {
        val layoutManager = recyclerViewDataCart_fr.layoutManager as LinearLayoutManager
        val scrollPosition = layoutManager.findFirstCompletelyVisibleItemPosition()
        val adapter = CartAdapter(requireActivity(), cartList, removeFoodFromCart(), updateCartQuantity())
        layoutManager.scrollToPosition(scrollPosition) // scroll back to the same position
        recyclerViewDataCart_fr.adapter = adapter
    }

//---------------------Cart Quantity and Remove Food From Cart (used in adapter)---------------------------------
    private fun updateCartQuantity() : (cart: Cart) -> Unit = { cart ->
        GlobalScope.launch (Dispatchers.IO){
            cartViewModel.updateCartQuantity(cart)
        }
    }
    private fun removeFoodFromCart() : (cart: Cart) -> Unit = { food ->
        GlobalScope.launch (Dispatchers.IO){ cartViewModel.removeFoodFromCart(food)
        }
    }


}








