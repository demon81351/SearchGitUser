package com.example.searchgituser.data_source

import androidx.paging.DataSource
import com.example.searchgituser.activity.UserInfo
import com.example.searchgituser.activity.UserViewModel

class UserDataSourceFactory(private val viewModel: UserViewModel) : DataSource.Factory<Int, UserInfo>() {
    override fun create(): DataSource<Int, UserInfo> {
        return UserDataSource(viewModel)
    }
}