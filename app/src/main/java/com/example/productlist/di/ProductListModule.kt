package com.example.productlist.di

import android.content.Context
import androidx.room.Room
import com.example.productlist.data.local.ProductDao
import com.example.productlist.data.local.ProductDatabase
import com.example.productlist.data.remote.NetworkApi
import com.example.productlist.repo.ProductRepository
import com.example.productlist.repo.ProductRepositoryImpl
import com.example.productlist.utils.AppConstant.BASE_URL
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object ProductListModule {

    @Singleton
    @Provides
    fun provideHttpLoggingInterceptor() = HttpLoggingInterceptor().apply {
        level = HttpLoggingInterceptor.Level.BODY
    }

    @Singleton
    @Provides
    fun providesOkHttpClient(httpLoggingInterceptor: HttpLoggingInterceptor): OkHttpClient =
        OkHttpClient()
            .newBuilder()
            .addInterceptor(httpLoggingInterceptor)
            .build()

    @Singleton
    @Provides
    fun provideProductApi(okHttpClient: OkHttpClient): NetworkApi = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .addConverterFactory(GsonConverterFactory.create())
        .client(okHttpClient)
        .build()
        .create(NetworkApi::class.java)


    @Singleton
    @Provides
    fun providesDatabase(@ApplicationContext context: Context): ProductDatabase =
        Room.databaseBuilder(
            context,
            ProductDatabase::class.java,
            "product_db"
        ).build()


    @Singleton
    @Provides
    fun provideProductDao(productDatabase: ProductDatabase): ProductDao =
        productDatabase.productDao

    @Singleton
    @Provides
    fun providesProductRepo(productDao: ProductDao, api: NetworkApi) : ProductRepository =
        ProductRepositoryImpl(api,productDao)
}