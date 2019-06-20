package com.example.searchgituser.activity

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.view.WindowManager
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.searchgituser.R
import com.example.searchgituser.databinding.ActivityMainBinding


class MainActivity : AppCompatActivity() {

    private var filterString = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initView()
    }

    private fun initView() {
        val binding: ActivityMainBinding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        val viewModel = ViewModelProviders.of(this).get(UserViewModel::class.java)
        binding.viewmodel = viewModel
        binding.lifecycleOwner = this

        binding.editQuery.setOnEditorActionListener { v, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                if (v.text.toString().isNotEmpty()) {
                    binding.viewmodel?.getItem()?.observe(this, Observer { items ->
                        items?.let {
                            (binding.recycleView.adapter as UserAdapter).submitList(items)
                        }
                    })
                    filterString = v.text.toString()

                } else {
                    Toast.makeText(this, "input is empty", Toast.LENGTH_LONG).show()
                }
                closeKeyboard(v)
            }
            true
        }

        setRecycleView(binding)
        setObserve(binding)
    }

    private fun setRecycleView(binding: ActivityMainBinding) {
        binding.viewmodel?.apply {
            val userAdapter = UserAdapter()
            binding.recycleView.layoutManager = LinearLayoutManager(this@MainActivity, RecyclerView.VERTICAL, false)
            binding.recycleView.addItemDecoration(
                DividerItemDecoration(
                    this@MainActivity,
                    DividerItemDecoration.VERTICAL
                )
            )
            binding.recycleView.adapter = userAdapter

        }

    }

    private fun setObserve(binding: ActivityMainBinding) {

        binding.viewmodel?.onLoadingLiveData?.observe(this, Observer {
            if (it) {
                binding.loading.visibility = View.VISIBLE
                window.setFlags(
                    WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
                    WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE
                )
            } else {
                binding.loading.visibility = View.GONE
                window.clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE)
            }
        })

    }

    private fun closeKeyboard(v: View) {
        val imm: InputMethodManager = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(v.windowToken, 0)
    }
}
