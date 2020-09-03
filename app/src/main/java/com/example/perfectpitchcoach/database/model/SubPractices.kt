package com.example.perfectpitchcoach.database.model

import android.arch.persistence.room.*
import android.support.annotation.NonNull


@Entity(tableName = "subPractices",
    indices = [Index("name"), Index("solved")],
    foreignKeys = [ForeignKey(entity = MasterClass::class, parentColumns = ["id"], childColumns = ["id_masterClass"])])
data class SubPractices(
    @PrimaryKey(autoGenerate = true)
    val id: Int,
    val name: String,
    @ColumnInfo(name = "solved")
    val solved: String,
    @NonNull
    val id_masterClass: Int)