package com.example.burgerapp.ui

import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import com.example.burgerapp.R
import kotlinx.android.synthetic.main.fragment_about.*


class AboutFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_about, container, false)
    }

    override fun onStart() {
        super.onStart()
        exit_about.setOnClickListener {
        activity?.supportFragmentManager?.beginTransaction()?.remove(this)?.commit()
        }
    }


}