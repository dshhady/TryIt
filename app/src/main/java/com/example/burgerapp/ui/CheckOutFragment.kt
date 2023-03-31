package com.example.burgerapp.ui
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.burgerapp.R

import kotlinx.android.synthetic.main.fragment_check_out.*

class CheckOutFragment(private val totalPrice :Int ) : Fragment() {


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_check_out, container, false)
    }

    override fun onStart() {
        super.onStart()
        total_price_value.text = totalPrice.toString()+"₪"
         val checkOutBtn = requireActivity().findViewById<View>(R.id.btnOrder_fr)
        checkOutBtn.visibility = View.GONE
        done_btn.setOnClickListener {
            requireActivity().supportFragmentManager.popBackStack()
            checkOutBtn.visibility = View.VISIBLE
        }
    }
}