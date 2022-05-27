package com.capstone.didow.UI.history

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.capstone.didow.R
import com.capstone.didow.databinding.HistoryFragmentBinding
import com.capstone.didow.databinding.HomeFragmentBinding

class HistoryFragment : Fragment() {

    private var _binding : HistoryFragmentBinding? = null
    private val binding get() = _binding!!

    companion object {
        fun newInstance() = HistoryFragment()
    }

    private lateinit var viewModel: HistoryViewModel

    private lateinit var adapter: HistoryAdapter
    private val listHistory = ArrayList<History>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = HistoryFragmentBinding.inflate(inflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.rvHistory.setHasFixedSize(true)
        listHistory.clear()
        listHistory.addAll(listHistoryOptions)

        showRecyclerViewHistory()
    }

    private val listHistoryOptions: ArrayList<History>
    get(){
        val dataDate = resources.getStringArray(R.array.date_array)
        val dataJumlahSukuKata = resources.getStringArray(R.array.count_suku_kata)
        val dataJenis = resources.getStringArray(R.array.jenis_soal)

        val listHistory = ArrayList<History>()
        for(i in dataDate.indices){
            val history = History(dataDate[i], dataJumlahSukuKata[i], dataJenis[i])
            listHistory.add(history)
        }
        return listHistory
    }

    private fun showRecyclerViewHistory(){
        binding.apply {
            val layoutManager = LinearLayoutManager(this@HistoryFragment.context)
            rvHistory.layoutManager = layoutManager
            adapter = HistoryAdapter(listHistory)
            rvHistory.adapter = adapter

            adapter.setOnItemClickCallback(object: HistoryAdapter.OnItemClickCallback{
                override fun onItemClicked(data: History) {
                    parentFragmentManager.beginTransaction().replace(R.id.container_main, HistoryDetailFragment()).commit()
                }
            })
        }

    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(HistoryViewModel::class.java)
        // TODO: Use the ViewModel
    }



    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

}