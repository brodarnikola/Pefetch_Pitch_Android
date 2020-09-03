package com.example.perfectpitchcoach.model

import com.google.gson.annotations.SerializedName

class SolvedSubMasterClassRequest {


    @SerializedName("name")
    var name: String = ""

    @SerializedName("id_user")
    var id_user: Long = 0

    @SerializedName("masterClassName")
    var masterClassName: String = ""

    @SerializedName("sub_master_class_unique_id")
    var subMasterClassUniqueId: String = ""

    @SerializedName("fileName")
    var fileName: String = ""

    @SerializedName("id_masterclass")
    var id_masterclass: Long = 0

    @SerializedName("solved")
    var solved: String = ""
}