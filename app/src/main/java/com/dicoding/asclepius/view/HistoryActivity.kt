package com.dicoding.asclepius.view

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.dicoding.asclepius.databinding.ActivityHistoryBinding
import com.dicoding.asclepius.view.adapters.ListHistoryAdapter
import com.dicoding.asclepius.viewmodel.HistoryViewModel
import com.dicoding.asclepius.viewmodel.HistoryViewModelFactory

class HistoryActivity : AppCompatActivity() {
    private lateinit var binding: ActivityHistoryBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHistoryBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val historyViewModel = ViewModelProvider(
            this,
            HistoryViewModelFactory.getInstance(application)
        )[HistoryViewModel::class.java]

        val adapter = ListHistoryAdapter {
            historyViewModel.delete(it)
        }

        binding.rvHistory.adapter = adapter
        binding.rvHistory.layoutManager = LinearLayoutManager(this)

        historyViewModel.historyList.observe(this) { history ->
            if (history.isEmpty()) {
                binding.tvNoHistory.visibility = View.VISIBLE
            } else {
                binding.tvNoHistory.visibility = View.GONE
            }
            adapter.submitList(history)
        }

        setSupportActionBar(binding.toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }
}
