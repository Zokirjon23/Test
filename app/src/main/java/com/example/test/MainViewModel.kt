package com.example.test

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class MainViewModel : ViewModel() {

    private val _openDialog = MutableLiveData(Unit)
    private val _resultDialog = MutableLiveData<String>()
    private val _increaseOne = MutableLiveData<Int>()
    private val _increaseTwo = MutableLiveData<Int>()
    private val _restart = MutableLiveData<Unit>()
    private val _beginGame = MutableLiveData<Long>()

     val openDialog : LiveData<Unit> = _openDialog
     val resultDialog : LiveData<String> = _resultDialog
     val increaseOne : LiveData<Int> = _increaseOne
     val increaseTwo : LiveData<Int> = _increaseTwo
     val restart : LiveData<Unit> = _restart
     val beginGame : LiveData<Long> = _beginGame


    fun onOneClicked(text : String){
        _increaseOne.value = text.toInt()+1
    }

    fun onTwoClicked(text : String){
        _increaseTwo.value = text.toInt()+1
    }

    fun onGameFinished(oneScore : String, twoScore : String){
        val oneScoreInt = oneScore.toInt()
        val twoScoreInt = twoScore.toInt()
        _resultDialog.value = if (oneScoreInt >twoScoreInt) "First team won" else if (oneScoreInt <twoScoreInt) "Second team won" else "Draw"
    }

    fun dialogOnOkClicked(long: Long){
        _beginGame.value = long
    }

    fun restartClicked(){
        _openDialog.value = Unit
        _restart.value = Unit
    }
}