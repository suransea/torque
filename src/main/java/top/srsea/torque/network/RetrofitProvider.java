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
 */

package top.srsea.torque.network;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * A retrofit provider.
 *
 * @author sea
 */
public class RetrofitProvider {

    /**
     * Default base url.
     */
    private static final String BASE_URL = "http://localhost/";

    /**
     * Default OkHttpClient instance
     */
    private final OkHttpClient client;

    /**
     * Constructs an instance
     */
    private RetrofitProvider() {
        client = new OkHttpClient.Builder().build();
    }

    /**
     * Returns a new Retrofit with default settings.
     *
     * @return new retrofit instance
     */
    public static Retrofit get() {
        return newRetrofitBuilder()
                .client(Singleton.INSTANCE.client)
                .baseUrl(BASE_URL)
                .build();
    }

    /**
     * Returns a new Retrofit with the specific base url.
     *
     * @param baseUrl the specified base url
     * @return new retrofit instance
     */
    public static Retrofit get(String baseUrl) {
        return newRetrofitBuilder()
                .client(Singleton.INSTANCE.client)
                .baseUrl(baseUrl)
                .build();
    }

    /**
     * Returns a new Retrofit with the specific base url and OkHttpClient instance.
     *
     * @param baseUrl the specified base url
     * @param client  the specific OkHttpClient instance
     * @return new retrofit instance
     */
    public static Retrofit get(String baseUrl, OkHttpClient client) {
        return newRetrofitBuilder()
                .client(client)
                .baseUrl(baseUrl)
                .build();
    }

    /**
     * Returns a new Retrofit with the specific OkHttpClient instance.
     *
     * @param client the specific OkHttpClient instance
     * @return new retrofit instance
     */
    public static Retrofit get(OkHttpClient client) {
        return newRetrofitBuilder()
                .client(client)
                .baseUrl(BASE_URL)
                .build();
    }

    /**
     * Creates a new retrofit builder with RxJava and Gson adapter.
     *
     * @return new retrofit builder with RxJava and Gson adapter
     */
    private static Retrofit.Builder newRetrofitBuilder() {
        return new Retrofit.Builder()
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create());
    }

    /**
     * The holder of {@code RetrofitProvider}
     */
    private static class Singleton {

        /**
         * Single {@code RetrofitProvider} instance.
         */
        private static final RetrofitProvider INSTANCE = new RetrofitProvider();
    }
}
