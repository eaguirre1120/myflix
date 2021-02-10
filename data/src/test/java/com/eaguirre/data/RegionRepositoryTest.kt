package com.eaguirre.data

import com.eaguirre.data.repository.PermissionChecker
import com.eaguirre.data.repository.PermissionChecker.Permission.COARSE_LOCATION
import com.eaguirre.data.repository.RegionRepository
import com.eaguirre.data.source.LocationDataSource
import com.nhaarman.mockitokotlin2.whenever
import kotlinx.coroutines.runBlocking
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.junit.MockitoJUnitRunner

@RunWith(MockitoJUnitRunner::class)
class RegionRepositoryTest {
    @Mock
    lateinit var locationDataSource: LocationDataSource

    @Mock
    lateinit var permissionChecker: PermissionChecker

    private lateinit var regionRepository: RegionRepository

    @Before
    fun setUp(){
        regionRepository = RegionRepository(locationDataSource, permissionChecker)
    }

    @Test
    fun `returns default when coarse permission not granted`(){
        runBlocking {
            whenever(permissionChecker.check(COARSE_LOCATION)).thenReturn(false)

            val region = regionRepository.findLastRegion()

            Assert.assertEquals(RegionRepository.DEFAULT_REGION, region)
        }
    }

    @Test
    fun `return region from location data source when coarse permission granted`(){
        runBlocking {
            whenever(permissionChecker.check(COARSE_LOCATION)).thenReturn(true)
            whenever(locationDataSource.findLastRegion()).thenReturn("ES")

            val result = regionRepository.findLastRegion()

            Assert.assertEquals("ES", result)
        }
    }
}