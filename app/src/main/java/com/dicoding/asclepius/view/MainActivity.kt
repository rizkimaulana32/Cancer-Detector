package com.dicoding.asclepius.view

import android.app.Activity
import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.lifecycle.ViewModelProvider
import com.dicoding.asclepius.databinding.ActivityMainBinding
import com.dicoding.asclepius.helper.ImageClassifierHelper
import com.dicoding.asclepius.view.adapters.ListNewsAdapter
import com.dicoding.asclepius.viewmodel.MainViewModel
import com.dicoding.asclepius.viewmodel.NewsViewModelFactory
import com.google.android.material.carousel.CarouselLayoutManager
import com.yalantis.ucrop.UCrop
import org.tensorflow.lite.task.vision.classifier.Classifications
import java.io.File
import java.text.NumberFormat

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var imageClassifierHelper: ImageClassifierHelper
    private lateinit var mainViewModel: MainViewModel
//    private var currentImageUri: Uri? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initializeImageClassifier()

        binding.galleryButton.setOnClickListener { startGallery() }
        binding.analyzeButton.setOnClickListener { analyzeImage() }
        binding.historyButton.setOnClickListener {
            val intent = Intent(this, HistoryActivity::class.java)
            startActivity(intent)
        }

        mainViewModel =
            ViewModelProvider(this, NewsViewModelFactory.getInstance())[MainViewModel::class.java]

        val adapter = ListNewsAdapter()
        binding.newsRecyclerView.setLayoutManager(CarouselLayoutManager())
        binding.newsRecyclerView.adapter = adapter

        mainViewModel.listArticles.observe(this) { articles ->
            adapter.submitList(articles)
        }

        mainViewModel.currentImageUri.observe(this) { uri ->
            uri?.let {
                showImage(it)
            }
        }

        mainViewModel.error.observe(this) { error ->
            error?.let {
                showToast(it)
            }
        }

        mainViewModel.isLoading.observe(this) { isLoading ->
            binding.progressIndicator.visibility = if (isLoading) View.VISIBLE else View.GONE
        }
    }

    private fun initializeImageClassifier() {
        imageClassifierHelper = ImageClassifierHelper(
            context = this,
            classifierListener = object : ImageClassifierHelper.ClassifierListener {
                override fun onResults(results: List<Classifications>?, inferenceTime: Long) {

                    binding.progressIndicator.visibility = View.GONE

                    val resultText = results?.joinToString("\n") { classification ->
                        classification.categories.joinToString(", ") {
                            it.label.trim() + " " + NumberFormat.getPercentInstance()
                                .format(it.score).trim()
                        }
                    }

                    resultText?.let {
                        mainViewModel.currentImageUri.value?.let { uri ->
                            moveToResult(uri, it)
                        }
                    }
                }

                override fun onError(error: String) {
                    binding.progressIndicator.visibility = View.GONE
                    showToast(error)
                }
            }
        )
    }

    private val launcherGallery = registerForActivityResult(
        ActivityResultContracts.PickVisualMedia()
    ) { uri: Uri? ->
        if (uri != null) {
            mainViewModel.setImageUri(uri)
            cropImage()
        } else {
            showToast("Gagal mengambil gambar")
        }
    }

    private fun startGallery() {
        launcherGallery.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
    }

    private fun showImage(uri: Uri) {
        binding.previewImageView.setImageURI(uri)
    }

    private val cropActivityResultLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                val resultUri = UCrop.getOutput(result.data!!)
                resultUri?.let { uri ->
                    mainViewModel.setImageUri(uri)
                    showImage(uri)
                }
            } else if (result.resultCode == UCrop.RESULT_ERROR) {
                val error = UCrop.getError(result.data!!)
                showToast("Gagal crop gambar: ${error?.message}")
            }
        }

    private fun cropImage() {
        mainViewModel.currentImageUri.value?.let { uri ->
            val destinationUri =
                Uri.fromFile(File(cacheDir, "cropped_image_${System.currentTimeMillis()}.jpg"))
            val uCrop = UCrop.of(uri, destinationUri)
                .withAspectRatio(1f, 1f)

            val intent = uCrop.getIntent(this)
            cropActivityResultLauncher.launch(intent)
        }
    }

    private fun analyzeImage() {
        mainViewModel.currentImageUri.value?.let { uri ->
            imageClassifierHelper.classifyStaticImage(uri)
            binding.progressIndicator.visibility = View.VISIBLE
        } ?: showToast("Silakan memilih gambar terlebih dahulu")
    }

    private fun moveToResult(imageUri: Uri, result: String) {
        val intent = Intent(this, ResultActivity::class.java)
        intent.putExtra("result", result)
        intent.putExtra("imageUri", imageUri.toString())
        startActivity(intent)
    }

    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

    override fun onResume() {
        super.onResume()
        binding.progressIndicator.visibility = View.GONE
    }
}