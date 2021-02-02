package com.eaguirre.data.source

interface LocationDataSource {
    suspend fun findLastRegion(): String?
}