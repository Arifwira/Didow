package com.capstone.didow.UI.onBoarding

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.capstone.didow.R
import com.capstone.didow.databinding.FragmentAssessmentReportBinding
import com.github.mikephil.charting.animation.Easing
import com.github.mikephil.charting.data.PieData
import com.github.mikephil.charting.data.PieDataSet
import com.github.mikephil.charting.data.PieEntry
import com.github.mikephil.charting.utils.ColorTemplate


class AssessmentReportFragment : Fragment() {
    private var _binding: FragmentAssessmentReportBinding? = null
    private val binding get() = _binding!!

    private var percentPilgan = 0F
    private var percentSusunKata = 0F
    private var percentHandWriting = 0F
    private var percentKurang = 0F
    private var percentTotal = 0F


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentAssessmentReportBinding.inflate(inflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val sharedPref = activity?.getPreferences(Context.MODE_PRIVATE) ?: return
        percentPilgan = sharedPref.getFloat("multipleChoice", 0F) * 100
        percentSusunKata = sharedPref.getFloat("scrambleWords", 0F) * 100
        percentHandWriting = sharedPref.getFloat("handwriting", 0F) * 100
        percentTotal = percentPilgan + percentSusunKata + percentHandWriting
        percentKurang = 100F - percentTotal

        setupPieChart()
        addToPieChart()
        setupPercentage()
        setupDescription(percentTotal)
        binding.apply {
            daftar.setOnClickListener{
                findNavController().navigate(R.id.action_assessmentReportFragment2_to_registerFragment)
            }
            keluar.setOnClickListener {
                activity?.finish()
            }
            tvReportScore.text = sharedPref.getInt("score", 0).toString()
        }

    }

    private fun setupDescription(total: Float){
        when(total){
            in 0F..30F -> binding.tvReportDesc.text = resources.getString(R.string.detail_report_rendah)
            in 31F..70F -> binding.tvReportDesc.text = resources.getString(R.string.detail_report_menengah)
            in 71F..100F -> binding.tvReportDesc.text = resources.getString(R.string.detail_report_tinggi)
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

        val entries = ArrayList<PieEntry>()
        entries.add(PieEntry(percentPilgan, "Pilgan"))
        entries.add(PieEntry(percentSusunKata, "Susun Kata"))
        entries.add(PieEntry(percentHandWriting, "Hand Writing"))
        entries.add(PieEntry(percentKurang, "Sisa Kurang"))

        val colors = ArrayList<Int>()
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