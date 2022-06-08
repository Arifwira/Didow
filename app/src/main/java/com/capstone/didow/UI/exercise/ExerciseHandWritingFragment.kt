package com.capstone.didow.UI.exercise

import android.Manifest
import android.app.Activity
import android.app.AlertDialog
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import android.os.Environment
import android.os.Handler
import android.os.Looper
import android.provider.MediaStore
import android.speech.tts.TextToSpeech
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.capstone.didow.BuildConfig
import com.capstone.didow.R
import com.capstone.didow.UI.OnBoarding
import com.capstone.didow.api.HandwritingResponse
import com.capstone.didow.api.RetrofitInstance
import com.capstone.didow.databinding.ExerciseHandWritingFragmentBinding
import com.capstone.didow.entities.AssessmentReport
import com.capstone.didow.entities.QuestionHandwriting
import com.capstone.didow.entities.QuestionMultipleChoice
import com.capstone.didow.entities.QuestionScrambleWords
import com.theartofdev.edmodo.cropper.CropImage
import com.theartofdev.edmodo.cropper.CropImageView
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import java.io.File
import java.io.IOException
import java.util.*

class ExerciseHandWritingFragment : Fragment() {

    private lateinit var currentPhotoPath: String
    private lateinit var viewModel: ExerciseHandWritingViewModel
    lateinit var tts: TextToSpeech
    private val exerciseViewModel: ExerciseViewModel by activityViewModels()
    private var _binding: ExerciseHandWritingFragmentBinding? = null
    private var hintHangman: String? = null
    private val binding get() = _binding!!
    private val TAG: String = "AppDebug"
    private val GALLERY_REQUEST_CODE = 1
    private val CAMERA_REQUEST_CODE = 2
    private var hintImg: String? = null
    private var getFile: File? = null

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


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = ExerciseHandWritingFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this)[ExerciseHandWritingViewModel::class.java]
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
//            Toast.makeText(
//                this@ExerciseHandWritingFragment.context,
//                "You open Camera", Toast.LENGTH_SHORT
//            ).show()
        }

        binding.btnGallery.setOnClickListener {
            pickFromGallery()
//            Toast.makeText(
//                this@ExerciseHandWritingFragment.context,
//                "You open Gallery", Toast.LENGTH_SHORT
//            ).show()
        }

        binding.lanjut.setOnClickListener {
            if (getFile != null) {
                binding.apply {
                    darkBg.visibility = View.VISIBLE
                    catLogin.visibility = View.VISIBLE
                    loadingText.visibility = View.VISIBLE
                }
                lifecycleScope.launch {
                    withContext(Dispatchers.IO) {
                        var response: HandwritingResponse? = null
                        try {
                            response = uploadImage()
                            Log.d("successUpload", response!!.data!!.predictedWord.toString())
                            exerciseViewModel.answer(response.data!!.predictedWord.toString())
                            withContext(Dispatchers.Main) {
                                if (response.data!!.isCorrect!!) {
                                    binding.apply {
                                        darkBg.visibility = View.GONE
                                        catLogin.visibility = View.GONE
                                        loadingText.visibility = View.GONE
                                    }
                                    trueDialog()
                                } else {
                                    binding.apply {
                                        darkBg.visibility = View.GONE
                                        catLogin.visibility = View.GONE
                                        loadingText.visibility = View.GONE
                                    }
                                    falseDialog()
                                }
                            }
                        } catch (e: IOException) {
                            binding.apply {
                                darkBg.visibility = View.GONE
                                catLogin.visibility = View.GONE
                                loadingText.visibility = View.GONE
                            }
                            Log.d("errorUpload", e.message.toString())
                        }
                    }
                }
            } else {
                exerciseViewModel.nextQuestion()
            }
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
                    val currentNumber = exerciseViewModel.currentQuestion.value?.number
                    val totalNumber = exerciseViewModel.exercise.value?.questions?.size
                    binding.tvNomorSoal.text = "$currentNumber/$totalNumber"

                    Log.d("Hangman List", it.hintHangman.toString())
                    this.hintImg = it.hintImg
                    Log.d("Hint hangman should be", hintHangman.toString())
                    this.hintHangman = it.hintHangman.joinToString(" ")
                    binding.previewImage.setImageResource(R.drawable.ic_baseline_image_24)
                    getFile = null
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
                        val intent =
                            makeAssessmentReportIntent(exerciseViewModel.assessmentReport.value!!)
                        activity?.setResult(110, intent)
                        activity?.finish()
                    }
                    "sample" -> {
                        findNavController().navigate(R.id.action_exerciseHandWritingFragment_to_sampleCompleteFragment2)
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
        val guideHandWriting = GuideHandWritingFragment()
        binding.btnGuide.setOnClickListener {
            guideHandWriting.show(childFragmentManager, "Words Scramble Panduan")
//            Toast.makeText(this@ExerciseHandWritingFragment.context,
//                "You open the Guidebook", Toast.LENGTH_SHORT).show()
        }
    }

    private fun playSound() {
        binding.btnPlay.setOnClickListener {
            binding.btnPlay.startAnimation(
                AnimationUtils.loadAnimation(
                    requireContext(),
                    R.anim.shake
                )
            )
            tts = TextToSpeech(requireContext()) {
                if (it == TextToSpeech.SUCCESS) {
                    tts.language = Locale.forLanguageTag("in")
                    tts.setSpeechRate(1.0f)
                    tts.speak(
                        "${exerciseViewModel.currentQuestion.value?.word}",
                        TextToSpeech.QUEUE_ADD, null
                    )
                }
                Log.d("SOUND", "TULIS TANGAN")
            }
            Log.d("SOUND", "TULIS TANGAN LUAR")
        }
    }

    private fun useHint() {
        val args = Bundle()
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

    private fun startTakePhoto() {
        val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        requireActivity().let { fragment ->
            intent.resolveActivity(fragment.packageManager)
            createTempFile(fragment.application).also {
                val photoURI: Uri = FileProvider.getUriForFile(
                    (Objects.requireNonNull(requireActivity().applicationContext)),
                    BuildConfig.APPLICATION_ID + ".provider", it
                )
                launchImageCrop(photoURI)
                currentPhotoPath = it.absolutePath
                intent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI)
                launcherIntentCamera.launch(intent)
            }
        }
    }

    private val launcherIntentCamera = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ){
    }


    private suspend fun uploadImage(): HandwritingResponse? {
        if (getFile != null) {
            val file = reduceFileImage(getFile as File)
            val client = RetrofitInstance.getApiService()
            val expectedWord = exerciseViewModel.currentQuestion.value!!.word
            val requestBody = MultipartBody.Builder()
                .setType(MultipartBody.FORM)
                .addFormDataPart("img", file.name, file.asRequestBody("application/octet-stream".toMediaType()))
                .addFormDataPart("expectedWord", expectedWord)
                .build()
            return client.analyzeHandwriting(requestBody)
        }
        return null
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        when (requestCode) {

            GALLERY_REQUEST_CODE -> {
                if (resultCode == Activity.RESULT_OK) {
                    data?.data?.let { uri ->
                        launchImageCrop(uri)
                    }
                }
                else{
                    Log.e(TAG, "Gagal Memilih Gambar" )
                }
            }

            CAMERA_REQUEST_CODE -> {
                if (resultCode == Activity.RESULT_OK){
                    data?.data?.let {
                        launchImageCrop(it)
                    }
                }
                else{
                    Log.e(TAG, "Kamera Gagal Terbuka" )
                }
            }

            CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE -> {
                val result = CropImage.getActivityResult(data)
                if (resultCode == Activity.RESULT_OK) {
                    val selectedImg: Uri = result.uri
                    val myFile = uriToFile(selectedImg, requireContext())
                    getFile = myFile
                    setImage(selectedImg)
                }
                else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                    Log.e(TAG, "Crop error: ${result.error}" )
                }
            }
        }
    }

    private fun setImage(uri: Uri){
        Glide.with(this)
            .load(uri)
            .into(binding.previewImage)
    }

    private fun launchImageCrop(uri: Uri){
        context?.let {
            CropImage.activity(uri)
                .setGuidelines(CropImageView.Guidelines.ON)
                .setCropShape(CropImageView.CropShape.RECTANGLE) // default is rectangle
                .start(it,this)
        }
    }

    private fun pickFromGallery() {
        val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        intent.type = "image/*"
        val mimeTypes = arrayOf("image/jpeg", "image/png", "image/jpg")
        intent.putExtra(Intent.EXTRA_MIME_TYPES, mimeTypes)
        intent.flags = Intent.FLAG_GRANT_READ_URI_PERMISSION
        startActivityForResult(intent, GALLERY_REQUEST_CODE)
    }

    private fun saveImage(): File?{
        val fileName = "Gambarku"
        val storageDir = activity?.getExternalFilesDir(Environment.DIRECTORY_PICTURES)
        val image = File.createTempFile(
            fileName,
            ".jpg",
            storageDir
        )
        return image
    }

    private fun trueDialog(){
        val view = View.inflate(this.context, R.layout.dialog_true_view, null)
        val builder = AlertDialog.Builder(this.context)
        builder.setView(view)
        val dialog = builder.create()
        dialog.show()
        dialog.setCancelable(false)
        dialog.window?.setBackgroundDrawableResource(android.R.color.transparent)
        Handler(Looper.getMainLooper()).postDelayed({
            dialog.dismiss()
            exerciseViewModel.nextQuestion()
        }, 3000)
    }

    private fun falseDialog(){
        val view = View.inflate(this.context, R.layout.dialog_false_view, null)
        val builder = AlertDialog.Builder(this.context)
        builder.setView(view)
        val dialog = builder.create()
        dialog.show()
        dialog.setCancelable(false)
        dialog.window?.setBackgroundDrawableResource(android.R.color.transparent)
        Handler(Looper.getMainLooper()).postDelayed({
            dialog.dismiss()
            exerciseViewModel.nextQuestion()
        }, 3000)
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}