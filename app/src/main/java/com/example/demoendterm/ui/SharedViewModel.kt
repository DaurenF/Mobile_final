package com.example.demoendterm.ui

import androidx.lifecycle.ViewModel
import com.example.demoendterm.Data

class SharedViewModel : ViewModel() {
    val infoList = mutableListOf<Data>()
}