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
annotation class CameraxOutputDirectory

@Retention(AnnotationRetention.RUNTIME)
@Qualifier
annotation class CameraxPhotoFile

@Retention(AnnotationRetention.RUNTIME)
@Qualifier
annotation class CameraxOutputOptions
