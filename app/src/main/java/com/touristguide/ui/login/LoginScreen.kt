package com.touristguide.ui.login

import android.annotation.SuppressLint
import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Alignment.Companion.BottomCenter
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import androidx.navigation.NavController
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.touristguide.R
import com.touristguide.routing.Screen
import com.touristguide.ui.theme.TouristGuideAppTheme
import com.touristguide.ui.theme.brown
import com.touristguide.ui.theme.white
import com.touristguide.ui.tourist_preference.TouristPreference
import com.touristguide.utils.OutlineFormField
import com.touristguide.utils.RoundedButton
import com.touristguide.utils.isValidEmail

@OptIn(ExperimentalMaterial3Api::class, ExperimentalComposeUiApi::class)
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun LoginScreen(navController: NavController) {
    val context = LocalContext.current
    val preference = remember {
        TouristPreference(context)
    }
    var touristUser by remember { mutableStateOf(false) }
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    val db = Firebase.firestore
    TouristGuideAppTheme {
        Scaffold {
            Column(
                modifier = Modifier.background(brown)
            ) {
                Spacer(modifier = Modifier.height(60.dp))
                Card(
                    modifier = Modifier
                        .width(120.dp)
                        .height(120.dp)
                        .align(Alignment.CenterHorizontally),
                    shape = RoundedCornerShape(60.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.White),
                    elevation = CardDefaults.cardElevation(5.dp),
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.ic_tourist),
                        contentDescription = "",
                        contentScale = ContentScale.Fit,
                        modifier = Modifier.fillMaxSize()
                    )
                }
                Column(
                    modifier = Modifier.fillMaxSize(),
                    verticalArrangement = Arrangement.Center
                ) {
                    Box(Modifier.padding(bottom = 60.dp)) {
                        Card(
                            modifier = Modifier.padding(
                                top = 10.dp,
                                start = 10.dp,
                                end = 10.dp
                            ),
                            shape = RoundedCornerShape(25.dp),
                            colors = CardDefaults.cardColors(containerColor = Color.White),
                            elevation = CardDefaults.cardElevation(5.dp),
                        ) {
                            Column(
                                horizontalAlignment = Alignment.CenterHorizontally,
                                modifier = Modifier
                                    .padding(20.dp)
                            ) {

                                Spacer(modifier = Modifier.height(5.dp))
                                OutlineFormField(
                                    value = email,
                                    onValueChange = { text ->
                                        email = text
                                    },
                                    placeholder = "Enter email",
                                    keyboardType = KeyboardType.Email,
                                )

                                Spacer(modifier = Modifier.height(5.dp))
                                OutlineFormField(
                                    value = password,
                                    onValueChange = { text ->
                                        password = text
                                    },
                                    placeholder = "Enter Password",
                                    keyboardType = KeyboardType.Password,
                                    visualTransformation = PasswordVisualTransformation(),
                                )

                                Spacer(modifier = Modifier.height(5.dp))
                                Row(
                                    modifier = Modifier
                                        .padding(start = 50.dp, end = 50.dp)
                                        .offset(0.dp, 32.dp)
                                ) {
                                    RoundedButton(
                                        text = "Login",
                                        onClick = {
                                            if (email.isEmpty()) {
                                                Toast.makeText(
                                                    context,
                                                    "Please enter email.",
                                                    Toast.LENGTH_LONG
                                                ).show()

                                            } else if (!isValidEmail(email.toString())) {
                                                Toast.makeText(
                                                    context,
                                                    "Please enter valid email.",
                                                    Toast.LENGTH_LONG
                                                ).show()
                                            } else if (password.isNotEmpty()) {
                                                Toast.makeText(
                                                    context,
                                                    "Please enter password.",
                                                    Toast.LENGTH_LONG
                                                ).show()

                                            } else {
                                                touristUser = true
                                                db.collection("users")
                                                    .get()
                                                    .addOnSuccessListener { result ->
                                                        if (result.isEmpty) {
                                                            Toast.makeText(
                                                                context,
                                                                "User not exist.",
                                                                Toast.LENGTH_LONG
                                                            ).show()
                                                            touristUser = false
                                                            return@addOnSuccessListener
                                                        } else {
                                                            for (document in result) {
                                                                if (document.data["email"] == email.lowercase() &&
                                                                    document.data["password"] == password
                                                                ) {
                                                                    preference.saveData(
                                                                        "isLogin",
                                                                        true
                                                                    )
                                                                    Toast.makeText(
                                                                        context,
                                                                        "Logged in successfully.",
                                                                        Toast.LENGTH_LONG
                                                                    ).show()
                                                                    navController.navigate(
                                                                        Screen.MainScreen.route
                                                                    ) {
                                                                        popUpTo(Screen.LoginScreen.route) {
                                                                            inclusive = true
                                                                        }
                                                                    }
                                                                    touristUser = false
                                                                } else if (document.data["email"] == email.lowercase() &&
                                                                    document.data["password"] != password
                                                                ) {
                                                                    Toast.makeText(
                                                                        context,
                                                                        "Please enter valid password.",
                                                                        Toast.LENGTH_LONG
                                                                    ).show()
                                                                    touristUser = false
                                                                    return@addOnSuccessListener
                                                                } else {
                                                                    Toast.makeText(
                                                                        context,
                                                                        "User not exist.",
                                                                        Toast.LENGTH_LONG
                                                                    ).show()
                                                                    touristUser = false
                                                                    return@addOnSuccessListener
                                                                }
                                                            }
                                                        }

                                                    }
                                                    .addOnFailureListener { exception ->
                                                        Toast.makeText(
                                                            context,
                                                            exception.message.toString(),
                                                            Toast.LENGTH_LONG
                                                        ).show()
                                                        touristUser = false
                                                    }
                                            }
                                        }
                                    )
                                }

                            }
                            Spacer(modifier = Modifier.height(20.dp))

                        }

                    }
                }


            }
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(bottom = 20.dp),
                verticalArrangement = Arrangement.Bottom
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth(),
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        "Don't have an account?",
                        textAlign = TextAlign.End,
                        style = TextStyle(color = white)
                    )

                    Text(
                        " Register", modifier = Modifier.clickable {
                            navController.navigate(Screen.RegisterScreen.route)
                        }, textAlign = TextAlign.End,
                        style = TextStyle(color = white)
                    )
                }
                Spacer(modifier = Modifier.height(10.dp))
            }
            if (touristUser) {
                Dialog(
                    onDismissRequest = { },
                    DialogProperties(dismissOnBackPress = false, dismissOnClickOutside = false)
                ) {
                    Box(
                        contentAlignment = Alignment.Center,
                        modifier = Modifier
                            .size(100.dp)
                            .background(white, shape = RoundedCornerShape(8.dp))
                    ) {
                        CircularProgressIndicator(color = brown)
                    }
                }
            }

        }
    }
}