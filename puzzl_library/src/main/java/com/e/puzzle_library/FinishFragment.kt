package com.e.puzzl_library

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kotlinx.android.synthetic.main.fragment_finish_frafment.*


class FinishFragment : Fragment() {


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_finish_frafment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        close_app.setOnClickListener { (activity as VeriffActivity).finishSuccessScreen() }
        finish.setOnClickListener { (activity as VeriffActivity).finishSuccessScreen() }
    }

}
