package com.example.p_cryptocurrency

import io.reactivex.Observable
import retrofit2.Call
import retrofit2.http.GET

interface CryptoAPI {

    @GET("prices?key=66427e5ded8619d3cead80c6681eeb456e40e9b3")

    fun getData(): Observable<List<CryptoModel>>

}