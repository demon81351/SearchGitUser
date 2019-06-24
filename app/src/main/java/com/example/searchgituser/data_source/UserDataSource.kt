package com.example.searchgituser.data_source

import androidx.paging.PageKeyedDataSource
import com.example.searchgituser.activity.UserInfo
import com.example.searchgituser.activity.UserViewModel
import com.example.searchgituser.http.ResourceProvider
import io.reactivex.schedulers.Schedulers
import java.util.concurrent.TimeUnit

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
                .observeOn(Schedulers.newThread())
                .subscribe({
                    viewModel.onLoadingLiveData.postValue(false)
                    if (it.isSuccessful) {
                        it.body()?.apply {
                            callback.onResult(
                                this.items, // List of data items
                                0, // Position of first item
                                this.total_count, // Total number of items that can be fetched from api
                                null, // Previous page. `null` if there's no previous page
                                page + 1 // Next Page (Used at the next request). Return `null` if this is the last page.
                            )
                        }
                    }

                }, {
                    viewModel.onLoadingLiveData.postValue(false)
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
                .observeOn(Schedulers.newThread())
                .map {
                    if(!it.isSuccessful){
                        viewModel.onLoadingLiveData.postValue(false)
                        viewModel.onShowErrorLiveData.postValue(true)
                        throw Exception("response code is not success")
                    }else{
                        it
                    }
                }
                .retryWhen { error -> error.delay(10, TimeUnit.SECONDS) }
                .subscribe({
                    viewModel.onLoadingLiveData.postValue(false)
                    if (it.isSuccessful) {
                        it.body()?.apply {
                            callback.onResult(
                                this.items, // List of data items
                                // Next Page key (Used at the next request). Return `null` if this is the last page.
                                page + 1
                            )
                        }
                    }

                }, {
                    viewModel.onLoadingLiveData.postValue(false)
                })
        )
    }

    override fun loadBefore(params: LoadParams<Int>, callback: LoadCallback<Int, UserInfo>) {
    }

}