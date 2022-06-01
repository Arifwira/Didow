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

    private val listDetailHistory = ArrayList<DetailHistory>()

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
        super.onViewCreated(view, savedInstanceState)
        binding.rvDetailHistory.setHasFixedSize(true)
        listDetailHistory.clear()
        listDetailHistory.addAll(listDetaiHistoryArray)
        adapter = HistoryDetailAdapter(listDetailHistory)

        showRecyclerViewList()
    }

    private val listDetaiHistoryArray: ArrayList<DetailHistory>
    get(){
        val dataNumberSoal = resources.getIntArray(R.array.nomor_soal)
        val dataDescription = resources.getStringArray(R.array.detail_description)

        val tempDetailHistory = ArrayList<DetailHistory>()
        for(i in dataNumberSoal.indices){
            val listTempDetailHistory = DetailHistory(dataNumberSoal[i], dataDescription[i])
            tempDetailHistory.add(listTempDetailHistory)
        }
        return tempDetailHistory
    }

    private fun showRecyclerViewList(){
        binding.apply {
            rvDetailHistory.layoutManager = LinearLayoutManager(this@HistoryDetailFragment.context)

            rvDetailHistory.adapter = adapter
        }
    }

}