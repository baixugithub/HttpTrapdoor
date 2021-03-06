/*
 * Copyright (c) 2020 Alvince
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package me.alvince.android.httptrapdoor.okhttp

import me.alvince.android.httptrapdoor.TrapdoorInstrumentation
import okhttp3.Call
import okhttp3.HttpUrl
import okhttp3.Request

/**
 * Trapdoor http [Call.Factory] proxy for dynamic request host supported
 *
 * Created by alvince on 2020/7/13
 *
 * @author alvince.zy@gmail.com
 */
internal class HttpCallFactoryProxy private constructor(
    private val factory: Call.Factory,
    private val instrumentation: TrapdoorInstrumentation
) : Call.Factory {

    companion object {
        fun create(factory: Call.Factory, instrumentation: TrapdoorInstrumentation): HttpCallFactoryProxy {
            return HttpCallFactoryProxy(factory, instrumentation)
        }
    }

    override fun newCall(request: Request): Call =
        request.let { origin ->
            if (shouldOverrideRequest(origin)) {
                origin.newBuilder()
                    .apply {
                        overrideRequestUrl(origin.url()).also { url -> url(url) }
                    }
                    .build()
            } else {
                origin
            }
        }.let {
            factory.newCall(it)
        }

    private fun shouldOverrideRequest(request: Request): Boolean =
        instrumentation.checkHostConfigured(request.url().host())

    private fun overrideRequestUrl(src: HttpUrl): HttpUrl =
        instrumentation.currentHost()
            ?.let { element ->
                src.newBuilder()
                    .apply {
                        scheme(element.scheme)
                        host(element.host)
                        port(element.port)
                    }
                    .build()
            }
            ?: src

}
