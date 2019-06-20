package com.example.searchgituser.repository

import androidx.paging.PagedList
import androidx.paging.RxPagedListBuilder
import com.example.searchgituser.activity.UserInfo
import com.example.searchgituser.activity.UserViewModel
import com.example.searchgituser.data_source.UserDataSourceFactory
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

class UserRepo(userViewModel: UserViewModel) {

    private val pageSize = 15

    private val factory = UserDataSourceFactory(userViewModel)

    private val config = PagedList.Config.Builder()
        .setPageSize(pageSize)
        .setInitialLoadSizeHint(pageSize * 2)
        .setEnablePlaceholders(false)
        .build()

    fun getList(): Observable<PagedList<UserInfo>> {
        return RxPagedListBuilder(factory, config)
            .setFetchScheduler(Schedulers.io())
            .setNotifyScheduler(AndroidSchedulers.mainThread())
            .buildObservable()
    }

}