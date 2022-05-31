package com.capstone.didow.UI.exercise

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.BitmapFactory
import android.net.Uri
import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import android.provider.MediaStore
import android.speech.tts.TextToSpeech
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.capstone.didow.BuildConfig
import com.capstone.didow.R
import com.capstone.didow.UI.OnBoarding
import com.capstone.didow.databinding.ExerciseHandWritingFragmentBinding
import com.capstone.didow.entities.AssessmentReport
import com.capstone.didow.entities.QuestionHandwriting
import com.capstone.didow.entities.QuestionMultipleChoice
import com.capstone.didow.entities.QuestionScrambleWords
import java.io.File
import java.util.*

class ExerciseHandWritingFragment : Fragment() {
    private var _binding: ExerciseHandWritingFragmentBinding? = null
    private val binding get() = _binding!!
    lateinit var tts : TextToSpeech

    companion object {
        fun newInstance() = ExerciseHandWritingFragment()
        private val REQUIRED_PERMISSIONS = arrayOf(Manifest.permission.CAMERA)
        private const val REQUEST_CODE_PERMISSIONS = 10
    }


    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == REQUEST_CODE_PERMISSIONS) {
            if (!allPermissionsGranted()) {
                Toast.makeText(
                    context,
                    "Tidak mendapatkan permission.",
                    Toast.LENGTH_SHORT
                ).show()
                requireActivity().finish()
            }
        }
    }

    private fun allPermissionsGranted() = REQUIRED_PERMISSIONS.all {
        requireActivity().let { it1 ->
            ContextCompat.checkSelfPermission(
                it1.baseContext,
                it
            )
        } == PackageManager.PERMISSION_GRANTED
    }

    private lateinit var viewModel: ExerciseHandWritingViewModel
    private val exerciseViewModel: ExerciseViewModel by activityViewModels()

    private var hintImg: String? = null
    private var hintHangman: String? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = ExerciseHandWritingFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(ExerciseHandWritingViewModel::class.java)
        // TODO: Use the ViewModel
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        if (!allPermissionsGranted()) {
            ActivityCompat.requestPermissions(
                requireActivity(),
                REQUIRED_PERMISSIONS,
                REQUEST_CODE_PERMISSIONS
            )
        }
        playSound()
        useHint()
        openGuide()

        binding.btnCamera.setOnClickListener {
            startTakePhoto()
            Toast.makeText(
                this@ExerciseHandWritingFragment.context,
                "You open Camera", Toast.LENGTH_SHORT
            ).show()
        }

        binding.btnGallery.setOnClickListener {
            startGallery()
            Toast.makeText(
                this@ExerciseHandWritingFragment.context,
                "You open Gallery", Toast.LENGTH_SHORT
            ).show()
        }

        binding.lanjut.setOnClickListener {
            exerciseViewModel.nextQuestion()
        }

        exerciseViewModel.currentQuestion.observe(viewLifecycleOwner, Observer {
            Log.d("ganti", it.word)
            when (it) {
                is QuestionMultipleChoice -> {
                    findNavController().navigate(R.id.action_exerciseHandWritingFragment_to_exerciseMultipleChoiceFragment)
                }
                is QuestionScrambleWords -> {
                    findNavController().navigate(R.id.action_exerciseHandWritingFragment_to_exerciseWordsScrambleFragment)
                }
                is QuestionHandwriting -> {
                    Log.d("Hangman List", it.hintHangman.toString())
                    this.hintImg = it.hintImg
                    Log.d("Hint hangman should be", hintHangman.toString())
                    this.hintHangman = it.hintHangman.joinToString(" ")

                    useHint()
                }
            }
        })

        exerciseViewModel.isRetry.observe(viewLifecycleOwner, Observer {
            if (it) {
                Toast.makeText(
                    this@ExerciseHandWritingFragment.context,
                    "Maaf jawaban kamu salah, silahkan untuk jawab ulang.", Toast.LENGTH_SHORT
                ).show()
            }
        })

        exerciseViewModel.isFinished.observe(viewLifecycleOwner, Observer {
            if (it) {
                when (exerciseViewModel.getExerciseCategory()) {
                    "auto" -> findNavController().navigate(R.id.action_exerciseHandWritingFragment_to_exerciseCompleteFragment)
                    "custom" -> findNavController().navigate(R.id.action_exerciseHandWritingFragment_to_exerciseCompleteFragment)
                    "assessment" -> {
                        val intent = makeAssessmentReportIntent(exerciseViewModel.assessmentReport.value!!)
                        activity?.setResult(110, intent)
                        activity?.finish()
                    }
                }
            }
        })
    }

    private fun makeAssessmentReportIntent(report: AssessmentReport): Intent {
        val intent = Intent(activity, OnBoarding::class.java)
        intent.putExtra("multipleChoice", report.multipleChoice)
        intent.putExtra("scrambleWords", report.scrambleWords)
        intent.putExtra("handwriting", report.handwriting)
        intent.putExtra("score", report.score)
        return intent
    }

    private fun openGuide() {
        binding.btnGuide.setOnClickListener {
            Toast.makeText(
                this@ExerciseHandWritingFragment.context,
                "You open the Guidebook", Toast.LENGTH_SHORT
            ).show()
        }
    }

    private fun playSound() {
        binding.btnPlay.setOnClickListener {
            tts = TextToSpeech(requireContext(), TextToSpeech.OnInitListener {
                if (it == TextToSpeech.SUCCESS) {
                    tts.setLanguage(Locale.forLanguageTag("in"))
                    tts.setSpeechRate(1.0f)
                    tts.speak(
                        "${exerciseViewModel.currentQuestion.value?.word}",
                        TextToSpeech.QUEUE_ADD, null
                    )
                }
                Log.d("SOUND", "TULIS TANGAN")
            })
            Log.d("SOUND","TULIS TANGAN LUAR")
        }
    }

    private fun useHint() {
        binding.btnHint.setOnClickListener {
            var args = Bundle()
            args.putString("hint", hintHangman)
            Log.d("hint hangman must", hintHangman.toString())
            args.putString("imageUrl", hintImg.toString())
            Log.d("image argument be", hintImg.toString())

            val popupHintFragment = PopupHintFragment()
            binding.btnHint.setOnClickListener {
                popupHintFragment.arguments = args
                popupHintFragment.show(childFragmentManager, "PopUpHintDialog Fragment")
            }
        }
    }

    private fun startTakePhoto() {
        val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        requireActivity().let {
            intent.resolveActivity(it.packageManager)
            createTempFile(it.application).also {
                val photoURI: Uri = FileProvider.getUriForFile(
                    (Objects.requireNonNull(requireActivity().applicationContext)),
                    BuildConfig.APPLICATION_ID + ".provider", it
                )
                currentPhotoPath = it.absolutePath
                intent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI)
                launcherIntentCamera.launch(intent)
            }
        }
    }

    private lateinit var currentPhotoPath: String
    private val launcherIntentCamera = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) {
        if (it.resultCode == AppCompatActivity.RESULT_OK) {
            val myFile = File(currentPhotoPath)
            Log.d("LaunchCam","GAGAL LAUNCH")
            val result = BitmapFactory.decodeFile(myFile.path)
            binding.previewImage.setImageBitmap(result)
        }
    }
    private fun startGallery() {
        val intent = Intent()
        intent.action = Intent.ACTION_GET_CONTENT
        intent.type = "image/*"
        val chooser = Intent.createChooser(intent, "Choose a Picture")
        launcherIntentGallery.launch(chooser)
    }

    private val launcherIntentGallery = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == AppCompatActivity.RESULT_OK) {
            val selectedImg: Uri = result.data?.data as Uri
            val myFile = uriToFile(selectedImg, requireActivity())
            binding.previewImage.setImageURI(selectedImg)
            var args = Bundle()
            args.putString("hint", hintHangman)
            Log.d("hint hangman must", hintHangman.toString())
            args.putString("imageUrl", hintImg.toString())
            Log.d("image argument be", hintImg.toString())

            val popupHintFragment = PopupHintFragment()
            binding.btnHint.setOnClickListener {
                popupHintFragment.arguments = args
                popupHintFragment.show(childFragmentManager, "PopUpHintDialog Fragment")
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}