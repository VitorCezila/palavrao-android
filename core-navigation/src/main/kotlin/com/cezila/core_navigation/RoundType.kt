package com.cezila.core_navigation

import android.net.Uri
import android.os.Bundle
import androidx.core.os.BundleCompat
import androidx.navigation.NavType
import com.cezila.domain.model.Round
import kotlinx.serialization.json.Json

object RoundType : NavType<Round>(isNullableAllowed = false) {

    override fun put(bundle: Bundle, key: String, value: Round) {
        bundle.putParcelable(key, value)
    }

    override fun get(bundle: Bundle, key: String): Round? =
        BundleCompat.getParcelable(bundle, key, Round::class.java)

    override fun parseValue(value: String): Round {
        return Json.decodeFromString(Uri.decode(value))
    }

    override fun serializeAsValue(value: Round): String = Uri.encode(Json.encodeToString(value))
}
