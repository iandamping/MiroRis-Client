package com.spe.miroris.di

import com.spe.miroris.util.parser.MoshiParser
import com.spe.miroris.util.parser.MoshiParserImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
interface ParserModule {

    @Binds
    fun bindsMoshiParser(impl: MoshiParserImpl): MoshiParser

}