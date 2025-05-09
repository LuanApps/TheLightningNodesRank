package dev.luanramos.thelightningnodesrank.presentation.main

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import dev.luanramos.thelightningnodesrank.domain.usecase.GetNodesUseCase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val getNodesUseCase: GetNodesUseCase
): ViewModel() {

    fun logNodesList(){
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val list = getNodesUseCase()
                Log.i("NodesLog", "Fetched ${list.size} nodes:\n${list.joinToString("\n")}")
            } catch (e: Exception) {
                Log.e("NodesLog", "Error fetching nodes", e)
            }
        }
    }

}