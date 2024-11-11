package com.dicoding.asclepius.view

import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import com.dicoding.asclepius.R
import com.dicoding.asclepius.data.local.History
import com.dicoding.asclepius.databinding.ActivityResultBinding
import com.dicoding.asclepius.viewmodel.HistoryViewModelFactory
import com.dicoding.asclepius.viewmodel.ResultViewModel

class ResultActivity : AppCompatActivity() {
    private lateinit var binding: ActivityResultBinding
    private lateinit var resultViewModel: ResultViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityResultBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val result = intent.getStringExtra("result")
        val imageUri = intent.getStringExtra("imageUri")

        binding.resultText.text = result
        binding.resultImage.setImageURI(Uri.parse(imageUri))

        resultViewModel = ViewModelProvider(
            this,
            HistoryViewModelFactory.getInstance(application)
        )[ResultViewModel::class.java]

        val fabSave = binding.fabSave
        fabSave.setOnClickListener {
            result?.let {
                imageUri?.let { uri ->
                    insertToDatabase(it, uri)
                }
            }

            fabSave.setImageResource(R.drawable.baseline_bookmark_24)
            fabSave.isEnabled = false
            Toast.makeText(this, "Berhasil disimpan ke histori", Toast.LENGTH_SHORT).show()
        }

        setSupportActionBar(binding.toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }

    private fun insertToDatabase(result: String, imageUri: String) {
        val history = History(
            image = imageUri,
            result = result
        )
        resultViewModel.insert(history)
    }
}