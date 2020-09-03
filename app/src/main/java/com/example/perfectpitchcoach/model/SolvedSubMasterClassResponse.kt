package com.example.perfectpitchcoach.model

import com.google.gson.annotations.SerializedName

class SolvedSubMasterClassResponse {


    @SerializedName("name")
    var name: String = ""

    @SerializedName("id_user")
    var id_user: Int = 0

    @SerializedName("id_masterclass")
    var id_masterclass: Int = 0

    @SerializedName("solved")
    var solved: String = ""
}