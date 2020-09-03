package com.example.perfectpitchcoach.database.model

import android.arch.persistence.room.*
import android.support.annotation.NonNull
 
@Entity(tableName = "subMasterClass" )
data class SubMasterClass(
    //@PrimaryKey(autoGenerate = true)
    //@Ignore
    //var id: Int? = 0,
    @ColumnInfo(name = "master_class_name")
    var masterClassName: String? = "",
    @NonNull
    var name: String? = "",
    @ColumnInfo(name = "sub_master_class_unique_id")
    var sub_master_class_unique_id: String? = "",
    @ColumnInfo(name = "solved")
    var solved: String? = "",
    @ColumnInfo(name = "file_name")
    var file_name: String? = "" )
{
    @PrimaryKey(autoGenerate = true)
    var id: Int = 0 // or foodId: Int? = null
}

//@Embedded
// val routine: Routine)