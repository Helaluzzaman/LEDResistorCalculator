package com.hbsoft.ledresistorcalculator.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import com.hbsoft.ledresistorcalculator.R
import com.hbsoft.ledresistorcalculator.viewModel.LaunchViewModel

class LaunchFragment : Fragment() {

    val mLaunchViewModel: LaunchViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        setHasOptionsMenu(true)
        val view =  inflater.inflate(R.layout.fragment_launch, container, false)

        return view
    }
}