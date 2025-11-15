package com.example.lab_week_10.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class TotalViewModel : ViewModel() {

    // Deklarasi LiveData
    private val _total = MutableLiveData<Int>()
    val total: LiveData<Int> = _total

    // Inisialisasi nilai awal
    init {
        // postValue bisa dari main thread maupun background thread
        _total.postValue(0)
    }

    // Increment nilai total
    fun incrementTotal() {
        _total.postValue(_total.value?.plus(1))
    }
}
