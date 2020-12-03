//package com.appvox.core.utils
//
//import com.github.kittinunf.fuel.core.FuelManager
//import java.net.InetSocketAddress
//import java.net.Proxy
//
//object HttpUtils {
//
//    fun setProxy(address : String, port : Int) {
//        val addr = InetSocketAddress(address, port)
//        FuelManager.instance.proxy = Proxy(Proxy.Type.HTTP, addr)
//    }
//}