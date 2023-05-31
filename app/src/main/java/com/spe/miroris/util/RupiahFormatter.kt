package com.spe.miroris.util

import java.text.NumberFormat
import java.util.Locale

fun rupiahFormatter(nominal: String?): String {
    /** why we use dollar currency ? because we can achieve this Rp 3,999.00*/
    val localeID = Locale("en", "US")
    val formatRupiah = NumberFormat.getCurrencyInstance(localeID)
    return if (nominal != null) {
        if (nominal.contains(".")) {
            val strPartsOfNominal: List<String> = nominal.split(".")
            when {
                strPartsOfNominal[1].length > 1 -> {
                    formatRupiah.apply {
                        minimumFractionDigits = 2
                        maximumFractionDigits = 2
                    }
                }
                strPartsOfNominal[1].length < 2 -> {
                    formatRupiah.apply {
                        minimumFractionDigits = 1
                        maximumFractionDigits = 1
                    }
                }
            }
        } else {
            formatRupiah.apply {
                minimumFractionDigits = 2
                maximumFractionDigits = 2
            }
        }
        val replacingRp = formatRupiah.format(nominal.toDouble()).replace("$", "")
        replacingRp
    } else "0"
}