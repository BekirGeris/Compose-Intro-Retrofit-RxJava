package com.begers.retrofitwithcompose

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.snapshots.ObserverHandle
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.begers.retrofitwithcompose.model.CryptoModel
import com.begers.retrofitwithcompose.service.CryptoAPI
import com.begers.retrofitwithcompose.ui.theme.RetrofitWithComposeTheme
import io.reactivex.Observer
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            RetrofitWithComposeTheme {
                MainScreen()
            }
        }
    }
}

@Composable
fun MainScreen(){
    val compositeDisposable = CompositeDisposable()
    val BASE_URL = "https://api.nomics.com/v1/"

    val service = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .addConverterFactory(GsonConverterFactory.create())
        .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
        .build()
        .create(CryptoAPI::class.java)

    compositeDisposable.add(service.getAllObservable()
        .subscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread())
        .subscribe(
            { value -> println("Received: $value") },
            { error -> println("Error: $error") },
            { println("Completed!") }
        ))


    Scaffold(topBar = {AppBar()}) {

    }

    Spacer(modifier = Modifier.padding(1.dp))

    CryptoRow(crypto = CryptoModel("BTC", "500000"))
}

@Composable
fun AppBar(){
    TopAppBar(contentPadding = PaddingValues(10.dp)) {
        Text(
            text = "Retrofit Compose",
            fontSize = 25.sp)
    }
}

@Composable
fun CryptoList(cryptos: List<CryptoModel>){
    LazyColumn(){
        items(cryptos) { crypto ->
            CryptoRow(crypto = crypto)
        }
    }
}

@Composable
fun CryptoRow(crypto: CryptoModel){
    Column(modifier = Modifier
        .fillMaxWidth()
        .background(color = MaterialTheme.colors.surface)) {
        Text(
            text = crypto.currency,
            style = MaterialTheme.typography.h4,
            modifier = Modifier.padding(2.dp),
            fontWeight = FontWeight.Bold)
        Text(
            text = crypto.price,
            style = MaterialTheme.typography.h6,
            modifier = Modifier.padding(2.dp))
    }
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    RetrofitWithComposeTheme {
        MainScreen()
    }
}