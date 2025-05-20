package me.danielrestrepo.cancerdepulmon

import com.google.gson.annotations.SerializedName

data class InputData(
    val GENDER: Int,
    val AGE: Int,
    val SMOKING: Int,
    val YELLOW_FINGERS: Int,
    val ANXIETY: Int,
    val PEER_PRESSURE: Int,
    @SerializedName("CHRONIC DISEASE") val CHRONIC_DISEASE: Int,
    @SerializedName("FATIGUE ") val FATIGUE_: Int,
    @SerializedName("ALLERGY ") val ALLERGY_: Int,
    val WHEEZING: Int,
    @SerializedName("ALCOHOL CONSUMING") val ALCOHOL_CONSUMING: Int,
    val COUGHING: Int,
    @SerializedName("SHORTNESS OF BREATH") val SHORTNESS_OF_BREATH: Int,
    @SerializedName("SWALLOWING DIFFICULTY") val SWALLOWING_DIFFICULTY: Int,
    @SerializedName("CHEST PAIN") val CHEST_PAIN: Int
)

data class PredictionResponse(val prediction: Int)