package com.touristguide.routing

sealed class Screen(val route: String) {

    object SplashScreen: Screen("splash_screen")
    object LoginScreen: Screen("login_screen")
    object RegisterScreen: Screen("register_screen")
    object MainScreen: Screen("main_screen")
    object Detail: Screen("detail_screen")
    object ContactUs: Screen("contact_us")
    object RateReview: Screen("rate_review_screen")


}