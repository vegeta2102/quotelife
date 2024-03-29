package giavu.hoangvm.hh.model

import com.google.gson.annotations.SerializedName

data class QuoteOfDay(
        @SerializedName("qotd_date") val qotd_date: String,
        @SerializedName("quote") val quote: Quote
)