package com.spe.miroris.core.data.dataSource.remote.rest

object NetworkConstant {
    const val CONTENT_TYPE = "application/json"
    const val CONTENT_TYPE_MULTIPART = "multipart/form-data"
    const val cacheSize = 10L * 1024 * 1024 // 10MB
    const val BASE_URL = "https://miroris-client-mobile-api.spesandbox.com"
    const val GET_TOKEN = "/index.php?r=api/v1/user/auth-token/get-token"
    const val GET_CATEGORY_PRODUCT = "/index.php?r=api/v1/product/category-product"
    const val REGISTER_USER = "/index.php?r=api/v1/user/register"
    const val LOGIN_USER = "/index.php?r=api/v1/user/auth/login"
    const val LOGOUT_USER = "/index.php?r=api/v1/user/auth-user/logout"
    const val REFRESH_TOKEN = "/index.php?r=api/v1/user/refresh-token"
    const val RESET_PASSWORD = "/index.php?r=api/v1/user/auth/reset-password"
    const val GET_USER_PROFILE = "/index.php?r=api/v1/user/user"
    const val GET_BANK = "/index.php?r=api/v1/user/user/bank"
    const val UPDATE_USER_PROFILE = "/index.php?r=api/v1/user/user/update"
    const val UPLOAD_USER_IMAGE = "/index.php?r=api/v1/user/user/upload"
    const val GET_PRODUCT_USER = "/index.php?r=api/v1/product/miroris-product"
    const val GET_PRODUCT_CATALOG = "/index.php?r=api/v1/product/katalog-product/katalog"
    const val CREATE_PRODUCT = "/index.php?r=api/v1/product/miroris-product/create"
    const val UPDATE_PRODUCT = "/index.php?r=api/v1/product/miroris-product/update"
    const val DEACTIVATE_PRODUCT = "/index.php?r=api/v1/product/miroris-product/deaktivasi"
    const val LIST_FUND = "/index.php?r=api/v1/product/payment-product/pendanaan"
    const val LIST_INVESTMENT = "/index.php?r=api/v1/product/payment-product/investasi"
    const val DETAIL_FUND = "/index.php?r=api/v1/product/miroris-product/katalog-detail"
    const val UPLOAD_PRODUCT_USER = "/index.php?r=api/v1/product/miroris-product/upload"
    const val VIEW_PRODUCT_IMAGE = "/index.php?r=api/v1/product/miroris-product/view-image"
    const val GENERATE_QR = "/index.php?r=api/v1/product/qris-generate/qris-profit"

}