package com.spe.miroris.di.qualifier

import javax.inject.Qualifier

@Retention(AnnotationRetention.RUNTIME)
@Qualifier
annotation class EncryptedOkHttpClientAnnotation

@Retention(AnnotationRetention.RUNTIME)
@Qualifier
annotation class EncryptedApiInterfaceAnnotation

@Retention(AnnotationRetention.RUNTIME)
@Qualifier
annotation class DefaultOkHttpClientAnnotation

@Retention(AnnotationRetention.RUNTIME)
@Qualifier
annotation class DefaultApiInterfaceAnnotation