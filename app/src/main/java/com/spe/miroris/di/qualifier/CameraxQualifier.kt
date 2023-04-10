package com.spe.miroris.di.qualifier

import javax.inject.Qualifier

@Retention(AnnotationRetention.BINARY)
@Qualifier
annotation class LensFacingBack

@Retention(AnnotationRetention.BINARY)
@Qualifier
annotation class LensFacingFront

@Retention(AnnotationRetention.BINARY)
@Qualifier
annotation class CameraxOutputDirectory

@Retention(AnnotationRetention.BINARY)
@Qualifier
annotation class CameraxPhotoFile

@Retention(AnnotationRetention.BINARY)
@Qualifier
annotation class CameraxOutputOptions
