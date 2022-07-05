package com.kerencev.mynasa.data.retrofit

interface RetrofitCallBack<T> {
    fun response(data: T?)
}