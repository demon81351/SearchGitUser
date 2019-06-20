package com.example.searchgituser.activity

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.paging.PagedList
import com.example.searchgituser.repository.UserRepo
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers

class UserViewModel : ViewModel() {

    val compositeDisposable = CompositeDisposable()

    val filterString = MutableLiveData<String>()
    val onLoadingLiveData = MutableLiveData<Boolean>()
    val onDataChangeLiveData = MutableLiveData<PagedList<UserInfo>>()

    private val userRepo = UserRepo(this)

    fun getList() {
        compositeDisposable.add(userRepo.getList()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe {
                onDataChangeLiveData.value = it
            })
    }

    override fun onCleared() {
        super.onCleared()
        compositeDisposable.clear()
    }

}