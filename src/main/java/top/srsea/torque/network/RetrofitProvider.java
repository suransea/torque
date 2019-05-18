/*
 * Copyright (C) 2019 sea
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */

package top.srsea.torque.network;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitProvider {
    private OkHttpClient client;

    private RetrofitProvider() {
        client = new OkHttpClient.Builder().build();
    }

    /**
     * 生成新Retrofit实例
     *
     * @param baseUrl the specified endpoint values
     * @return new retrofit instance
     */
    public static Retrofit get(String baseUrl) {
        return newRetrofitBuilder()
                .client(Singleton.INSTANCE.client)
                .baseUrl(baseUrl)
                .build();
    }

    /**
     * 生成新Retrofit实例
     *
     * @param baseUrl the specified endpoint values
     * @param client  the custom OkHttpClient
     * @return new retrofit instance
     */
    public static Retrofit get(String baseUrl, OkHttpClient client) {
        return newRetrofitBuilder()
                .client(client)
                .baseUrl(baseUrl)
                .build();
    }

    /**
     * 生成新Retrofit实例
     *
     * @param client the custom OkHttpClient
     * @return new retrofit instance
     */
    public static Retrofit get(OkHttpClient client) {
        return newRetrofitBuilder()
                .client(client)
                .baseUrl("http://localhost/")
                .build();
    }

    private static Retrofit.Builder newRetrofitBuilder() {
        return new Retrofit.Builder()
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create());
    }

    private static class Singleton {
        private static final RetrofitProvider INSTANCE = new RetrofitProvider();
    }
}
