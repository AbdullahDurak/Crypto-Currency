package com.example.p_cryptocurrency

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_main.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory


class MainActivity : AppCompatActivity(), RecyclerViewAdapter.Listener {

    private val BASE_URL="https://api.nomics.com/v1/"
    private var cryptoModels:ArrayList<CryptoModel>? =null
    private var recyclerViewAdapter:RecyclerViewAdapter?=null

    private var compositeDisposable:CompositeDisposable?=null



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        //https://api.nomics.com/v1/prices?key=66427e5ded8619d3cead80c6681eeb456e40e9b3
        //66427e5ded8619d3cead80c6681eeb456e40e9b3

        compositeDisposable=CompositeDisposable()

        val layoutManager:RecyclerView.LayoutManager
        layoutManager=LinearLayoutManager(this)
        recyclerView.layoutManager=layoutManager
        loadData()
    }

    private fun loadData(){
        val retrofit=Retrofit.Builder().baseUrl(BASE_URL).addConverterFactory(GsonConverterFactory.create()).addCallAdapterFactory(RxJava2CallAdapterFactory.create()).build().create(CryptoAPI::class.java)

        compositeDisposable?.add(retrofit.getData().subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(this::handleResponse)
        )


        /*


        val call=service.getData()



        call.enqueue(object:Callback<List<CryptoModel>>{
            override fun onResponse(call: Call<List<CryptoModel>>, response: Response<List<CryptoModel>>) {

                if(response.isSuccessful){
                    response.body()?.let {
                       var cryptoModels=ArrayList(it)

                        cryptoModels?.let {

                            recyclerViewAdapter= RecyclerViewAdapter(it,this@MainActivity)
                            recyclerView.adapter=recyclerViewAdapter
                        }

                    }
                }

            }

            override fun onFailure(call: Call<List<CryptoModel>>, t: Throwable) {

                t.printStackTrace()

            }

        })




*/







    }

    override fun onitemclick(cryptoModel: CryptoModel) {

        Toast.makeText(this,"Clicked : ${cryptoModel.currency}",Toast.LENGTH_LONG ).show()


    }

    override fun onDestroy() {
        super.onDestroy()
        compositeDisposable?.clear()
    }


    private fun handleResponse(cryptoList: List<CryptoModel>){

        cryptoModels=ArrayList(cryptoList)


        cryptoModels?.let {

            recyclerViewAdapter = RecyclerViewAdapter(it, this@MainActivity)
            recyclerView.adapter = recyclerViewAdapter


        }
    }


}