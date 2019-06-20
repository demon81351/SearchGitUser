package com.example.searchgituser.repository

import androidx.lifecycle.LiveData
import androidx.paging.LivePagedListBuilder
import androidx.paging.PagedList
import com.example.searchgituser.activity.UserInfo
import com.example.searchgituser.activity.UserViewModel
import com.example.searchgituser.data_source.UserDataSourceFactory

class UserRepo(userViewModel: UserViewModel) {

    private val pageSize = 15

    private val factory = UserDataSourceFactory(userViewModel)

    private val config = PagedList.Config.Builder()
        .setPageSize(pageSize)
        .setInitialLoadSizeHint(pageSize * 2)
        .setEnablePlaceholders(false)
        .build()

    fun getItems(): LiveData<PagedList<UserInfo>> {

        return LivePagedListBuilder<Int, UserInfo>(factory, config)
            .setInitialLoadKey(0)
            .build()
    }

}