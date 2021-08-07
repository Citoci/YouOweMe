package com.cito.youoweme.data

import android.content.Context

interface YomDAO<DataType, IDType> {
    fun open(context: Context?)
    fun close()

    fun insert(value: DataType): Boolean
    fun delete(value: DataType): Boolean

    fun getAll(): List<DataType>?
    fun getById(id: IDType): DataType?
}