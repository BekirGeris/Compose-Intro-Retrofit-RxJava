package com.begers.retrofitwithcompose.service

import com.begers.retrofitwithcompose.model.CryptoModel
import io.reactivex.Observable
import retrofit2.http.GET

interface CryptoAPI {

    //https://api.nomics.com/v1/
    //prices?key=eaaf35b443ed77522c3c114d37a6744c889827eb

    @GET("prices?key=eaaf35b443ed77522c3c114d37a6744c889827eb")
    fun getAllObservable() : Observable<List<CryptoModel>>
}