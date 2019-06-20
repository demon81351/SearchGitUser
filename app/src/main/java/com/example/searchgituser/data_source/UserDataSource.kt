package com.example.searchgituser.data_source

import androidx.paging.PageKeyedDataSource
import com.example.searchgituser.activity.UserInfo
import com.example.searchgituser.activity.UserViewModel
import com.example.searchgituser.http.ResourceProvider
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

class UserDataSource(private val viewModel: UserViewModel) : PageKeyedDataSource<Int, UserInfo>() {

    override fun loadInitial(params: LoadInitialParams<Int>, callback: LoadInitialCallback<Int, UserInfo>) {

        val page = 1

        viewModel.onLoadingLiveData.postValue(true)
        viewModel.compositeDisposable.add(
            ResourceProvider.searchUserResource().getUserList(
                viewModel.filterString.value.toString(), 1,
                params.requestedLoadSize
            )
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({

                    it.body()?.apply {
                        viewModel.onLoadingLiveData.value = false

                        callback.onResult(
                            this.items, // List of data items
                            0, // Position of first item
                            this.total_count, // Total number of items that can be fetched from api
                            null, // Previous page. `null` if there's no previous page
                            page + 1 // Next Page (Used at the next request). Return `null` if this is the last page.
                        )
                    }

                }, {
                    viewModel.onLoadingLiveData.value = false
                })
        )
    }

    override fun loadAfter(params: LoadParams<Int>, callback: LoadCallback<Int, UserInfo>) {

        // Next page.
        val page = params.key

        viewModel.onLoadingLiveData.postValue(true)
        viewModel.compositeDisposable.add(
            ResourceProvider.searchUserResource().getUserList(
                viewModel.filterString.value.toString(), page,
                params.requestedLoadSize
            )
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({

                    it.body()?.apply {
                        viewModel.onLoadingLiveData.value = false

                        callback.onResult(
                            this.items, // List of data items
                            // Next Page key (Used at the next request). Return `null` if this is the last page.
                            page + 1
                        )
                    }

                }, {
                    viewModel.onLoadingLiveData.value = false
                })
        )
    }

    override fun loadBefore(params: LoadParams<Int>, callback: LoadCallback<Int, UserInfo>) {
    }

}