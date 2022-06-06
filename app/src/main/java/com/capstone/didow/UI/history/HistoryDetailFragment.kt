package com.capstone.didow.UI.history

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.capstone.didow.R
import com.capstone.didow.UI.OnBoarding
import com.capstone.didow.databinding.FragmentHistoryDetailBinding

class HistoryDetailFragment : Fragment() {
    private var _binding: FragmentHistoryDetailBinding? = null
    private val binding get() = _binding!!

    private lateinit var adapter: HistoryDetailAdapter

    companion object{
        var EXTRA_DATA = "extra_data"
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentHistoryDetailBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val data = arguments?.getParcelableArrayList<WrongAnswers>(EXTRA_DATA)

        if (data!!.size == 0) {
            binding.tvBlankDetail.visibility = View.VISIBLE
        }

        super.onViewCreated(view, savedInstanceState)
        binding.rvDetailHistory.setHasFixedSize(true)
        adapter = HistoryDetailAdapter(data!!)
        showRecyclerViewList()
    }

    private fun showRecyclerViewList(){
        binding.apply {
            rvDetailHistory.layoutManager = LinearLayoutManager(this@HistoryDetailFragment.context)
            rvDetailHistory.adapter = adapter
        }
    }

}