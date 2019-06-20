package com.example.searchgituser.http.resource

import com.example.searchgituser.activity.UserModel
import io.reactivex.Observable
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface ISearchUserResource {

    @GET("search/users")
    fun getUserList(@Query("q") filter: String, @Query("page") page: Int = 0, @Query("per_page") per_page: Int = 0): Observable<Response<UserModel>>
}