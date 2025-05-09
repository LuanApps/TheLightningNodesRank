package dev.luanramos.thelightningnodesrank.presentation.main

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import dev.luanramos.thelightningnodesrank.domain.model.Node
import dev.luanramos.thelightningnodesrank.domain.usecase.GetNodesUseCase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val getNodesUseCase: GetNodesUseCase
): ViewModel() {

    private val _nodesData = MutableLiveData<List<Node>?>()
    val nodesData: LiveData<List<Node>?> get() = _nodesData

    init {
        loadNodesList()
    }

    fun clearNodeList(){
        _nodesData.postValue(null)
    }

    fun loadNodesList(){
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val list = getNodesUseCase()
                _nodesData.postValue(list)
            } catch (e: Exception) {
                _nodesData.postValue(listOf())
                Log.e("NodesLog", "Error fetching nodes", e)
            }
        }
    }

}