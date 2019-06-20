package com.example.searchgituser.activity

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.paging.PagedList
import com.example.searchgituser.repository.UserRepo
import io.reactivex.disposables.CompositeDisposable

class UserViewModel : ViewModel() {

    val compositeDisposable = CompositeDisposable()

    val filterString = MutableLiveData<String>()
    val onLoadingLiveData = MutableLiveData<Boolean>()

    private val userRepo = UserRepo(this)

    fun getItem(): LiveData<PagedList<UserInfo>> {
        return userRepo.getItems()
    }

    override fun onCleared() {
        super.onCleared()
        compositeDisposable.clear()
    }

}