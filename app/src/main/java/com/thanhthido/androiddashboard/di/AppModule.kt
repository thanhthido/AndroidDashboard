package com.thanhthido.androiddashboard.di

import android.content.Context
import android.content.SharedPreferences
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKey
import com.thanhthido.androiddashboard.data.remote.UrlsApi
import com.thanhthido.androiddashboard.di.dispatchers.DispatcherImp
import com.thanhthido.androiddashboard.di.dispatchers.DispatcherProvider
import com.thanhthido.androiddashboard.utils.Constant.BASE_URL
import com.thanhthido.androiddashboard.utils.Constant.ENCRYPTED_SHARED_PREF
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Singleton
    @Provides
    fun provideDispatchers(): DispatcherProvider = DispatcherImp()

    @Singleton
    @Provides
    fun provideUrlsApi(): UrlsApi {
        return Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(UrlsApi::class.java)
    }

    @Singleton
    @Provides
    fun provideEncryptedSharedPref(@ApplicationContext context: Context): SharedPreferences {
        val masterKey = MasterKey.Builder(context)
            .setKeyScheme(MasterKey.KeyScheme.AES256_GCM)
            .build()

        return EncryptedSharedPreferences.create(
            context,
            ENCRYPTED_SHARED_PREF,
            masterKey,
            EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
            EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
        )
    }

}