package com.example.searchgituser.http

import com.example.searchgituser.http.resource.ISearchUserResource
import com.example.searchgituser.utils.Consts

object ResourceProvider {

    private var factory: ResourceFactory = ResourceFactory(Consts.BASE_URL)

    fun searchUserResource(): ISearchUserResource {
        return createService(ISearchUserResource::class.java)
    }

    private fun <S> createService(serviceClass: Class<S> ): S {
        return factory.createService(serviceClass)
    }
}