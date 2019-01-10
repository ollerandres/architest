package com.andresoller.device.utils

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkInfo
import junit.framework.Assert.*
import org.junit.Before
import org.junit.Test
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.Mockito.`when`
import org.mockito.Mockito.mock
import org.mockito.MockitoAnnotations
import java.io.File

class NetworkUtilsTest {

    @Mock
    lateinit var context: Context
    @InjectMocks
    lateinit var networkUtils: NetworkUtils

    @Before
    fun setUp() {
        MockitoAnnotations.initMocks(this)
    }

    @Test
    fun isNetworkAvailable_NetworkAvailable_ReturnTrue() {
        val connectivityManager = mock(ConnectivityManager::class.java)
        val networkInfo = mock(NetworkInfo::class.java)
        `when`(context.getSystemService(Context.CONNECTIVITY_SERVICE)).thenReturn(connectivityManager)
        `when`(connectivityManager.activeNetworkInfo).thenReturn(networkInfo)
        `when`(networkInfo.isConnected).thenReturn(true)

        assertTrue(networkUtils.isNetworkAvailable())
    }

    @Test
    fun isNetworkAvailable_NetworkNotAvailable_ReturnFalse() {
        val connectivityManager = mock(ConnectivityManager::class.java)
        val networkInfo = mock(NetworkInfo::class.java)
        `when`(context.getSystemService(Context.CONNECTIVITY_SERVICE)).thenReturn(connectivityManager)
        `when`(connectivityManager.activeNetworkInfo).thenReturn(networkInfo)
        `when`(networkInfo.isConnected).thenReturn(false)

        assertFalse(networkUtils.isNetworkAvailable())
    }

    @Test
    fun getCacheDir() {
        val file = mock(File::class.java)
        `when`(context.cacheDir).thenReturn(file)

        assertEquals(file, networkUtils.getCacheDir())
    }
}