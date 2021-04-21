package com.everis.android.marvelkotlin.di

import com.everis.android.marvelkotlin.BuildConfig
import com.everis.android.marvelkotlin.data.network.datasource.NetworkDatasource
import com.everis.android.marvelkotlin.data.network.interceptor.ConnectionInterceptor
import com.everis.android.marvelkotlin.data.network.logging.CrashLogger
import com.everis.android.marvelkotlin.data.network.logging.DefaultLogger
import com.everis.android.marvelkotlin.data.network.service.ApiService
import com.everis.android.marvelkotlin.data.network.status.NetworkHandler
import com.everis.android.marvelkotlin.data.repository.NetworkDataRepository
import com.everis.android.marvelkotlin.domain.repository.NetworkRepository
import com.everis.android.marvelkotlin.domain.usecase.DoWait
import com.everis.android.marvelkotlin.domain.usecase.GetCharacter
import com.everis.android.marvelkotlin.domain.usecase.GetCharacters
import com.everis.android.marvelkotlin.presentation.detail.DetailViewModel
import com.everis.android.marvelkotlin.presentation.home.HomeViewModel
import com.everis.android.marvelkotlin.presentation.navigation.AppNavigator
import com.everis.android.marvelkotlin.presentation.navigation.NavigatorLifecycle
import com.everis.android.marvelkotlin.presentation.splash.SplashViewModel
import com.squareup.moshi.JsonAdapter
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.core.module.Module
import org.koin.core.qualifier.named
import org.koin.dsl.module
import retrofit2.Converter
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import java.util.concurrent.TimeUnit

@JvmField
val presentationModule: Module = module {
    single { NavigatorLifecycle() }

    single {
        AppNavigator(
            navigatorLifecycle = get()
        )
    }

    single<CoroutineDispatcher> { Dispatchers.IO }

    //region ViewModel
    viewModel {
        SplashViewModel(
            dispatcher = get(),
            appNavigator = get(),
            doWait = get()
        )
    }

    viewModel {
        HomeViewModel(
            dispatcher = get(),
            appNavigator = get(),
            getCharacters = get()
        )
    }

    viewModel {
        DetailViewModel(
            dispatcher = get(),
            appNavigator = get(),
            getCharacter = get()
        )
    }
    //endregion
}

@JvmField
val domainModule: Module = module {
    //region UseCases
    factory {
        DoWait()
    }

    factory {
        GetCharacters(
            networkRepository = get()
        )
    }

    factory {
        GetCharacter(
            networkRepository = get()
        )
    }
    //endregion
}

@JvmField
val dataModule: Module = module {
    single<CrashLogger> {
        DefaultLogger(
            debuggable = getPropertyOrNull(Property.PRINT_LOGS)?.toBoolean() ?: false
        )
    }

    //region Repositories
    factory<NetworkRepository> {
        NetworkDataRepository(
            networkDatasource = get()
        )
    }
    //endregion

    //region Datasources
    single<String>(named(Property.API_URL)) {
        getPropertyOrNull(Property.API_URL) ?: "http://foo"
    }

    single<String>(named(Property.PRIVATE_KEY)) {
        getPropertyOrNull(Property.PRIVATE_KEY) ?: "PRIVATE_KEY"
    }

    single<String>(named(Property.PUBLIC_KEY)) {
        getPropertyOrNull(Property.PUBLIC_KEY) ?: "PUBLIC_KEY"
    }

    single {
        NetworkDatasource(
            apiService = get(),
            privateKey = get(named(Property.PRIVATE_KEY)),
            publicKey = get(named(Property.PUBLIC_KEY)),
            crashLogger = get()
        )
    }
    //endregion

    //region ApiServices
    single { NetworkHandler(androidContext()) }

    single<Moshi> {
        Moshi.Builder().add(KotlinJsonAdapterFactory()).build()
    }

    single<HttpLoggingInterceptor> {
        provideLoggingInterceptor(
            debuggable = getPropertyOrNull(Property.PRINT_LOGS)?.toBoolean() ?: false
        )
    }

    single<Interceptor> {
        ConnectionInterceptor(
            networkHandler = get(),
            crashLogger = get()
        )
    }

    single<ApiService> {
        createApiService<ApiService>(get())
    }

    single<OkHttpClient> {
        createOkHttpClient(
            connectionInterceptor = get(),
            loggingInterceptor = get()
        )
    }

    single<Retrofit> {
        createRetrofit(
            okHttpClient = get(),
            endpoint = get(named(Property.API_URL)),
        )
    }
    //endregion
}

object Property {
    const val API_URL = "API_URL"
    const val PRIVATE_KEY = "PRIVATE_KEY"
    const val PUBLIC_KEY = "PUBLIC_KEY"
    const val PRINT_LOGS = "PRINT_LOGS"
}

@JvmField
val baseModules = listOf(presentationModule, domainModule, dataModule)

fun provideLoggingInterceptor(debuggable: Boolean = false): HttpLoggingInterceptor {
    return HttpLoggingInterceptor().apply {
        level = if (debuggable) {
            HttpLoggingInterceptor.Level.BODY
        } else {
            HttpLoggingInterceptor.Level.NONE
        }
    }
}

fun createOkHttpClient(
    connectionInterceptor: Interceptor,
    loggingInterceptor: HttpLoggingInterceptor
): OkHttpClient {
    return OkHttpClient.Builder().apply {
        addInterceptor(connectionInterceptor)
        if (BuildConfig.DEBUG) addInterceptor(loggingInterceptor)
        connectTimeout(10, TimeUnit.SECONDS)
        readTimeout(10, TimeUnit.SECONDS)
        writeTimeout(30, TimeUnit.SECONDS)
    }.build()
}

fun createConverterFactory(vararg jsonAdapters: JsonAdapter<Any>): Converter.Factory {
    return MoshiConverterFactory.create(
        Moshi.Builder().run {
            jsonAdapters.forEach { add(it) }
            add(KotlinJsonAdapterFactory())
            build()
        }
    )
}

fun createRetrofit(
    okHttpClient: OkHttpClient,
    endpoint: String,
    converterFactory: Converter.Factory = createConverterFactory()
): Retrofit {
    return Retrofit.Builder()
        .baseUrl(endpoint)
        .client(okHttpClient)
        .addConverterFactory(converterFactory)
        .build()
}

inline fun <reified T> createApiService(retrofit: Retrofit): T = retrofit.create(T::class.java)