package com.example.customdrugview.nestscroll

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.example.customdrugview.R

class ScrollingFragment : Fragment() {

    private var backgroundColor: Int? = null

    companion object {
        fun newInstance(color: Int) = ScrollingFragment().apply {
            arguments = Bundle().apply { putInt("color", color) }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let { backgroundColor = it.getInt("color") }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_scrolling, container, false)
        view.findViewById<TextView>(R.id.tv_content).setBackgroundColor(backgroundColor!!)
        return view
    }
}