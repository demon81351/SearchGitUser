package com.example.searchgituser.http

import okhttp3.*

class RequestInterceptor : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        var original = chain.request()
        val requestBuilder = original
            .newBuilder()
//                .addHeader("token", SharedPreferencesUtil.getInstance().getString(Consts.USER_TOKEN,""))

        // set getToken
//        val token = TokenKeeper.getInstance().getToken()
//        if (StringUtils.isNotBlank(token)) {
//            requestBuilder.addHeader(AUTHORIZATION, token)
//        }
        if (original.method() == "POST") {
            val newFormBody = FormBody.Builder()
            if (original.body() is FormBody) {
                val oidFormBody = original.body() as FormBody?
                for (i in 0 until oidFormBody!!.size()) {
                    newFormBody.addEncoded(oidFormBody.encodedName(i), oidFormBody.encodedValue(i))
                }
            } else {
                original = requestBuilder.post(
                    RequestBody.create(
                        MediaType.parse("application/x-www-form-urlencoded;charset=UTF-8"),
                        ""
                    )
                ).build()
            }

            requestBuilder.method(original.method(), newFormBody.build())
        } else if (original.method() == "GET") {
//            val newUrl = original.url().newBuilder()
//                    .addQueryParameter("_t", Calendar.getInstance().timeInMillis.toString()).build()
//
//            requestBuilder.url(newUrl)
//            requestBuilder.method(original.method(), original.body())
        }

        val request = requestBuilder.build()

        return chain.proceed(request)
    }

}