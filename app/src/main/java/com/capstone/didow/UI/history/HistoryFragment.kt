package com.capstone.didow.UI.history

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import android.os.Parcelable
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.util.Pair
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.capstone.didow.R
import com.capstone.didow.databinding.HistoryFragmentBinding
import com.capstone.didow.databinding.HomeFragmentBinding
import com.google.android.material.datepicker.CalendarConstraints
import com.google.android.material.datepicker.DateValidatorPointBackward
import com.google.android.material.datepicker.DateValidatorPointForward
import com.google.android.material.datepicker.MaterialDatePicker
import com.google.firebase.auth.FirebaseAuth
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class HistoryFragment : Fragment() {

    private val historyViewModel : HistoryViewModel by activityViewModels()
    private var _binding : HistoryFragmentBinding? = null
    private val binding get() = _binding!!
    private lateinit var auth: FirebaseAuth

    companion object {
        fun newInstance() = HistoryFragment()
        const val WEEK_TIME = 604800000L
        const val TIMEZONE = 7
    }

    private lateinit var adapter: HistoryAdapter
    private var startDate: String? = null
    private var endDate: String? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = HistoryFragmentBinding.inflate(inflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        auth = FirebaseAuth.getInstance()
        val endDateTime = System.currentTimeMillis()
        val startDateTime = endDateTime - WEEK_TIME
        startDate = SimpleDateFormat("yyyy-MM-dd").format(Date(startDateTime))
        endDate = SimpleDateFormat("yyyy-MM-dd").format(Date(endDateTime))

        Log.d("endDateTime", endDate.toString())
        Log.d("startDateTime", startDate.toString())

        datePicker(startDateTime, endDateTime)
        showRecyclerViewHistory()

        historyViewModel.init(auth.currentUser?.uid!!, startDate.toString(), endDate.toString(), TIMEZONE)

        binding.rvHistory.setHasFixedSize(true)

        historyViewModel.history.observe(viewLifecycleOwner, Observer{
//            Log.d("History Data", it.last().endTime.toString())
            adapter = HistoryAdapter(it)
            binding.rvHistory.adapter = adapter
            adapter.setOnItemClickCallback(object: HistoryAdapter.OnItemClickCallback{
                override fun onItemClicked(data: History) {
                    val mDetailHistory = HistoryDetailFragment()
                    val mBundle = Bundle()
                    mBundle.putParcelableArrayList(HistoryDetailFragment.EXTRA_DATA, data.wrongAnswer as java.util.ArrayList<out Parcelable>)
                    Log.d("mBundle", mBundle.toString())
                    mDetailHistory.arguments = mBundle
                    parentFragmentManager.beginTransaction().replace(R.id.container_main, mDetailHistory).commit()
                }
            })
        })

        binding.recomContainer.visibility = View.GONE

        historyViewModel.suggestion.observe(viewLifecycleOwner, Observer { suggestion ->
            binding.recomContainer.visibility = View.VISIBLE
            binding.tvRekomendasi.text = suggestion!!
        })
    }

    private fun showRecyclerViewHistory(){
        binding.apply {
            val layoutManager = LinearLayoutManager(this@HistoryFragment.context)
            rvHistory.layoutManager = layoutManager
        }

    }

    private fun datePicker(startDateSet: Long, endDateSet: Long){
        val dateFormat = SimpleDateFormat("yyyy-MM-dd")
        binding.apply {
            tvStartDate.text = dateFormat.format(Date(startDateSet))
            tvEndDate.text = dateFormat.format(Date(endDateSet))
        }
        val constraintBuilder = CalendarConstraints.Builder()
        constraintBuilder.setValidator(DateValidatorPointBackward.now())
        val datePicker = MaterialDatePicker.Builder.dateRangePicker().setCalendarConstraints(constraintBuilder.build())
            .setSelection(
                Pair(
                    startDateSet, endDateSet
                )
            )
            .setTitleText("Select dates").build()
        binding.btnCalendar.setOnClickListener {
            datePicker.show(childFragmentManager, "Tag_picker")
            datePicker.addOnPositiveButtonClickListener {
                val datePickerFirst = datePicker.selection?.first
                val datePickerSecond = datePicker.selection?.second
                val dateFirst = dateFormat.format(Date(datePickerFirst!!))
                val dateSecond = dateFormat.format(Date(datePickerSecond!!))
                binding.apply {
                    tvStartDate.text = dateFirst
                    tvEndDate.text = dateSecond
                }
                startDate = dateFirst
                endDate = dateSecond
                historyViewModel.init(auth.currentUser?.uid!!, startDate.toString(), endDate.toString(), TIMEZONE)
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

}