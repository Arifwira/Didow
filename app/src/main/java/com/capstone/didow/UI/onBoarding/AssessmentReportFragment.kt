package com.capstone.didow.UI.onBoarding

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.capstone.didow.UI.OnBoarding
import com.capstone.didow.databinding.FragmentAssessmentReportBinding
import com.github.mikephil.charting.animation.Easing
import com.github.mikephil.charting.components.Legend
import com.github.mikephil.charting.data.PieData
import com.github.mikephil.charting.data.PieDataSet
import com.github.mikephil.charting.data.PieEntry
import com.github.mikephil.charting.formatter.PercentFormatter
import com.github.mikephil.charting.utils.ColorTemplate


class AssessmentReportFragment : Fragment() {
    private var _binding: FragmentAssessmentReportBinding? = null
    private val binding get() = _binding!!

    private val percentPilgan = 15F
    private val percentSusunKata = 15F
    private val percentHandWriting = 15F
    private val percentKurang = 55F


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentAssessmentReportBinding.inflate(inflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupPieChart()
        addToPieChart()
        setupPercentage()
        binding.apply {
            daftar.setOnClickListener{
                val intent = Intent(activity,OnBoarding::class.java)
                intent.putExtra("TARGET","RegisterFragment")
                startActivity(intent)
                activity?.finish()
            }
            keluar.setOnClickListener {
                activity?.finish()
            }
        }

    }

    private fun setupPercentage(){
        binding.apply {
            tvNilaiPilgan.text = "$percentPilgan%"
            tvNilaiSusunKata.text = "$percentSusunKata%"
            tvNilaiHandWriting.text = "$percentHandWriting%"
        }
    }

    private fun setupPieChart(){
        binding.apply {
            pchReport.isDrawHoleEnabled = true
            pchReport.setUsePercentValues(false)
            pchReport.setDrawEntryLabels(false)
            pchReport.centerText = "${percentPilgan + percentSusunKata + percentHandWriting}%"
            pchReport.setCenterTextSize(16f)
            pchReport.description.isEnabled = false


            val l = pchReport.legend
            l.setDrawInside(false)
            l.isEnabled = false
        }
    }

    private fun addToPieChart(){
//        binding.apply {
//            pchReport.addPieSlice(PieModel("Pilgan", percentPilgan, Color.parseColor("#F25738")))
//            pchReport.addPieSlice(PieModel("Susun Kata", percentSusunKata, Color.parseColor("#5BC3B8")))
//            pchReport.addPieSlice(PieModel("Tulis Tangan", percentHandWriting, Color.parseColor("#FE9F45")))
//            pchReport.addPieSlice(PieModel("Sisa Kurang", percentKurang, Color.parseColor("#EBEBEB")))
//            pchReport.startAnimation()
//        }

        var entries = ArrayList<PieEntry>()
        entries.add(PieEntry(percentPilgan, "Pilgan"))
        entries.add(PieEntry(percentSusunKata, "Susun Kata"))
        entries.add(PieEntry(percentHandWriting, "Hand Writing"))
        entries.add(PieEntry(percentKurang, "Sisa Kurang"))

        var colors = ArrayList<Int>()
        colors.add(ColorTemplate.rgb("#F25738")) // Pilgan
        colors.add(ColorTemplate.rgb("#5BC3B8")) // Susun Kata
        colors.add(ColorTemplate.rgb("#FE9F45")) // Hand Writing
        colors.add(ColorTemplate.rgb("#EBEBEB")) // Kurang
        for (color in ColorTemplate.MATERIAL_COLORS) {
            colors.add(color)
        }

        for (color in ColorTemplate.VORDIPLOM_COLORS) {
            colors.add(color)
        }

        val dataset = PieDataSet(entries, "Expense Category")
        dataset.colors = colors
        val data = PieData(dataset)

        data.setDrawValues(false)
//        data.setValueFormatter(PercentFormatter(binding.pchReport))
//        data.setValueTextSize(12f)
//        data.setValueTextColor(Color.BLACK)

        binding.pchReport.data = data
        binding.pchReport.invalidate()

        binding.pchReport.animateY(1400, Easing.EaseInOutQuad)

    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}