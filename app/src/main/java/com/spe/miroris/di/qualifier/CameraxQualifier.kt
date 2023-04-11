package com.spe.miroris.di.qualifier

import javax.inject.Qualifier

@Retention(AnnotationRetention.RUNTIME)
@Qualifier
annotation class LensFacingBack

@Retention(AnnotationRetention.RUNTIME)
@Qualifier
annotation class LensFacingFront

@Retention(AnnotationRetention.RUNTIME)
@Qualifier
annotation class CameraxContentValues

@Retention(AnnotationRetention.RUNTIME)
@Qualifier
annotation class CameraxOutputOptions
