package com.hbsoft.ledresistorcalculator.viewModel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import com.hbsoft.ledresistorcalculator.data.repository.Repository

class LaunchViewModel(application: Application): AndroidViewModel(application) {
    val repository: Repository = Repository()
    val LedDataList =repository.getAllLedData()
    val LedNameList = repository.getAllLedNames()

}