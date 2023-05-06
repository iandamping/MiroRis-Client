package com.spe.miroris.util.parser

import java.lang.reflect.Type

interface MoshiParser {

    fun <T> fromJson(json: String, type: Type): T?

    fun <T> toJson(obj: T, type: Type): String?
}