package com.touristguide.ui.main

import android.annotation.SuppressLint
import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.rememberScaffoldState
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Alignment.Companion.BottomCenter
import androidx.compose.ui.Alignment.Companion.Center
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.touristguide.R
import com.touristguide.routing.Screen
import com.touristguide.ui.model.PlaceModel
import com.touristguide.ui.theme.TouristGuideAppTheme
import com.touristguide.ui.theme.brown
import com.touristguide.ui.theme.white
import com.touristguide.ui.tourist_preference.TouristPreference
import com.touristguide.utils.RoundedButton
import com.touristguide.utils.drawer.DrawerBody
import com.touristguide.utils.drawer.DrawerHeader
import com.touristguide.utils.drawer.TopBar
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class, ExperimentalComposeUiApi::class)
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun MainScreen(navController: NavController) {
    val context = LocalContext.current
    val preference = remember {
        TouristPreference(context)
    }
    val scope = rememberCoroutineScope()
    var isLogout by remember { mutableStateOf(false) }
    val scaffoldState = rememberScaffoldState()
    val scrollState = rememberScrollState()
    val list = ArrayList<PlaceModel>().apply {
        add(
            PlaceModel(
                id = "",
                detail = "The Tees Transporter Bridge has played an important role in the area's history for over a century and continues to Provide an important and unique crossing over the River Tees.",
                image = R.drawable.ic_trees_trasport,
                name = "Tees Transporter Bridge"
            )
        )
        add(
            PlaceModel(
                id = "",
                detail = "Great Stadium Nice food",
                image = R.drawable.ic_riverside,
                name = "Riverside Stadium"
            )
        )
        add(
            PlaceModel(
                id = "",
                detail = "The Pennyman family's intimate 18th-century mansion. Alongside the Georgian mansion, Victorian kitchen and laundry, there are beautiful gardens.",
                image = R.drawable.ic_ormes,
                name = "Ormesby Hall"
            )
        )
        add(
            PlaceModel(
                id = "",
                detail = "There has been a working farm at Newham Grange since the 17th century. It is now a unique and extremely popular attraction for the whole family as well as school groups.",
                image = R.drawable.ic_farm,
                name = "Newham Grange Farm"
            )
        )
        add(
            PlaceModel(
                id = "",
                detail = "St Mary's Catholic Cathedral is the Mother Church of the Diocese of Middlesbrough. Opened in 1986",
                image = R.drawable.ic_cathdral,
                name = "St Mary's Cathedral"
            )
        )
        add(
            PlaceModel(
                id = "",
                detail = "It’s a welcoming and friendly place. Pay a visit to find out more about the religion and people instead of believing into media.",
                image = R.drawable.ic_jamia,
                name = "Jamia Mosque Almadina"
            )
        )
        add(
            PlaceModel(
                id = "",
                detail = "A couple of nice local walks took us past this sculpture recently.",
                image = R.drawable.ic_temenos,
                name = "Temenos"
            )
        )
        add(
            PlaceModel(
                id = "",
                detail = "Good place to visit.",
                image = R.drawable.ic_bottle,
                name = "Bottle of Notes"
            )
        )
    }
    TouristGuideAppTheme {
        androidx.compose.material.Scaffold(
            scaffoldState = scaffoldState,
            topBar = {
                TopBar(
                    navController = navController,
                    onNavigationIconClick = {
                        scope.launch {
                            scaffoldState.drawerState.open()
                        }
                    }
                )
            },
            modifier = Modifier.background(color = brown),
            drawerContent = {
                DrawerHeader()
                DrawerBody(onReview = {
                    navController.navigate(Screen.RateReview.route)
                    scope.launch {
                        scaffoldState.drawerState.close()
                    }
                }, onContact = {
                    navController.navigate(Screen.ContactUs.route)
                    scope.launch {
                        scaffoldState.drawerState.close()
                    }
                }, onLogout = {
                    isLogout = true
                    scope.launch {
                        scaffoldState.drawerState.close()
                    }
                })
            },
            backgroundColor = brown,
            contentColor = brown,
            drawerBackgroundColor = brown
        ) { paddingValues ->
            Modifier.padding(
                bottom = paddingValues.calculateBottomPadding()
            )
            Column(
                modifier = Modifier
                    .background(color = brown)
                    .verticalScroll(scrollState)
            ) {

                Column(modifier = Modifier.background(white)) {
                    list.forEachIndexed { index, placeModel ->
                        Box(Modifier) {
                            Card(
                                modifier = Modifier
                                    .padding(10.dp)
                                    .fillMaxWidth()
                                    .height(150.dp),
                                shape = RoundedCornerShape(10.dp),
                                colors = CardDefaults.cardColors(containerColor = Color.White),
                                elevation = CardDefaults.cardElevation(defaultElevation = 5.dp)
                            ) {
                                Image(
                                    painter = painterResource(id = placeModel.image),
                                    contentDescription = "Image",
                                    contentScale = ContentScale.Crop,
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .height(150.dp)
                                )
                            }
                            Row(
                                modifier = Modifier
                                    .padding(start = 50.dp, end = 50.dp)
                                    .align(Center)
                                    .offset(0.dp, 32.dp)
                            ) {
                                RoundedButton(
                                    text = placeModel.name,
                                    onClick = {
                                        navController.navigate(Screen.Detail.route + "/${placeModel.name}" + "/${placeModel.image}" + "/${placeModel.detail}")
                                    }
                                )
                            }
                        }

                    }
                }
            }
        }
        if (isLogout) {
            AlertDialog(
                onDismissRequest = {
                    isLogout = false
                },
                title = { Text(stringResource(id = R.string.app_name)) },
                text = { Text("Are you sure you want to logout ?") },
                confirmButton = {
                    RoundedButton(
                        text = "Cancel",
                        textColor = white,
                        onClick = { isLogout = false }
                    )
                },
                dismissButton = {

                    RoundedButton(
                        text = "Logout",
                        textColor = white,
                        onClick = {
                            preference.saveData("isLogin", false)
                            navController.navigate(
                                Screen.LoginScreen.route
                            ) {
                                popUpTo(Screen.MainScreen.route) {
                                    inclusive = true
                                }
                            }
                            isLogout = false
                        }
                    )

                }
            )
        }

    }

}