package com.freelance.capsoula.di


import com.freelance.capsoula.data.source.local.UserDataSource
import com.freelance.capsoula.data.source.remote.WebService
import com.freelance.capsoula.utils.Constants
import com.freelance.capsoula.utils.Domain
import com.freelance.capsoula.utils.preferencesGateway
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.Dispatchers.Main
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import okhttp3.Cache
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.koin.core.qualifier.named
import org.koin.dsl.module
import retrofit2.CallAdapter
import retrofit2.Converter
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import timber.log.Timber
import java.util.concurrent.TimeUnit


fun provideHTTPLoggingInterceptor(): HttpLoggingInterceptor {
    val interceptor = HttpLoggingInterceptor { message -> Timber.e(message) }
    interceptor.level = HttpLoggingInterceptor.Level.BODY
    return interceptor
}

fun provideNetworkInterceptor(): Interceptor {
    return Interceptor { chain ->
        val original = chain.request()
        val request = original.newBuilder()
        runBlocking {
            val job = CoroutineScope(IO).launch {
                var x = UserDataSource.getToken()
                request.addHeader(
                    "Authorization", Constants.BEARER + " " +
                            UserDataSource.getToken()
                )
            }
            job.join()
        }
        chain.proceed(request.build())
    }
}

fun provideLanguageInterceptor(): Interceptor {
    return Interceptor { chain ->
        val original = chain.request()
        val request = original.newBuilder()
        runBlocking {
            val job = CoroutineScope(IO).launch {
                request.addHeader("Accept-Language",
                    preferencesGateway.load(Constants.LANGUAGE,"en"))
            }
            job.join()
        }
        chain.proceed(request.build())
    }
}


fun provideOkHttpClient(
    cache: Cache, httpLoggingInterceptor: HttpLoggingInterceptor,
    networkInterceptor: Interceptor, languageInterceptor: Interceptor
): OkHttpClient {
    return OkHttpClient().newBuilder().apply {
        connectTimeout(1, TimeUnit.MINUTES)
        readTimeout(1, TimeUnit.MINUTES)
        writeTimeout(1, TimeUnit.MINUTES)
        cache(cache)
        addInterceptor(httpLoggingInterceptor)
        addInterceptor(languageInterceptor)
        addNetworkInterceptor(networkInterceptor)
    }.build()
}

fun provideRetrofit(
    okHttpClient: OkHttpClient, callAdapterFactory:
    CallAdapter.Factory, converterFactory: Converter.Factory
): Retrofit {
    return Retrofit
        .Builder().apply {
            client(okHttpClient)
            addConverterFactory(converterFactory)
            baseUrl(Constants.BASE_URL)
            addCallAdapterFactory(callAdapterFactory)
        }.build()
}

fun provideWebServices(retrofit: Retrofit): WebService {
    return retrofit.create(WebService::class.java)
}

fun provideJsonConverterFactory(gson: Gson): Converter.Factory {
    return GsonConverterFactory.create(gson);
}

fun provideCallAdapter(): CallAdapter.Factory {
    return RxJava2CallAdapterFactory.create();
}

fun provideCache(): Cache {
    return Cache(Domain.application.cacheDir, (10 * 1000 * 1000).toLong())
}

val networkModule = module {
    single(named(LOGGING_INTERCEPTOR)) { provideHTTPLoggingInterceptor() }
    single(named(NETWORK_INTERCEPTOR)) { provideNetworkInterceptor() }
    single(named(LANGUAGE_INTERCEPTOR)) { provideLanguageInterceptor() }
    single { provideCache() }
    single { provideJsonConverterFactory(get()) }
    single(named(CALL_ADAPTER)) { provideCallAdapter() }
    single { GsonBuilder().create() }
    single {
        provideOkHttpClient(
            get(),
            get(named(LOGGING_INTERCEPTOR)),
            get(named(NETWORK_INTERCEPTOR)),
            get(named(LANGUAGE_INTERCEPTOR))
        )
    }
    single { provideRetrofit(get(), get(named(CALL_ADAPTER)), get()) }
    single { provideWebServices(get()) }
}

const val LOGGING_INTERCEPTOR = "logging-interceptor"
const val NETWORK_INTERCEPTOR = "network-interceptor"
const val LANGUAGE_INTERCEPTOR = "language-interceptor"
const val CALL_ADAPTER = "call-adapter"