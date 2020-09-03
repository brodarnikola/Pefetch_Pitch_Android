package com.example.perfectpitchcoach.database.model

import android.arch.persistence.room.*
import android.support.annotation.NonNull

//@Entity(tableName = "masterClass")
@Entity(tableName = "masterClass" )
data class MasterClass(
   //@PrimaryKey(autoGenerate = true)
    //@Ignore
    //var id: Int? = 0,
    @NonNull
    var name: String? = "",
    @ColumnInfo(name = "solved")
    var solved: String? = "",

    @ColumnInfo(name = "file_name")
    var filename: String? = "" )
{
    @PrimaryKey(autoGenerate = true)
    var id: Int = 0 // or foodId: Int? = null
}

//@Embedded
   // val routine: Routine)