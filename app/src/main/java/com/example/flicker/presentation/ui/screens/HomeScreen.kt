package com.example.flicker.presentation.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.runtime.Composable
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.materialIcon
import androidx.compose.material3.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.Navigator
import com.example.flicker.R
import com.example.flicker.presentation.navigation.Screen

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(navController: NavController) {
    val ti = "the_vagabond"
    val context = LocalContext.current
    Scaffold(
        modifier = Modifier.background(color = Color(0xFF000000)),
        topBar = {
            TopAppBar(
                title = {
                    Text("FLICK ER  ",
                        Modifier
                            .fillMaxWidth()
                            .wrapContentSize(Alignment.Center))
                        },

                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color.Transparent,
                    titleContentColor = Color(0xFF0D47A1),

                )
            )
        },
        content = { paddingValues ->
            Column(
                verticalArrangement = Arrangement.Top,
                horizontalAlignment = Alignment.Start,
                modifier = Modifier

                    .fillMaxSize()
                    .padding(paddingValues)
            ) {
                Text(
                    text = "Home",
                    style = MaterialTheme.typography.headlineMedium
                )
                Spacer(modifier = Modifier.height(16.dp))
                Column (
                    verticalArrangement = Arrangement.Top,
                    horizontalAlignment = Alignment.Start,
                ) {
                    Row {
                        val resourceId = context.resources.getIdentifier(
                            ti,          // The name of the resource (without extension)
                            "drawable",  // The type of resource
                            context.packageName // Your app's package name
                        )

                        if (resourceId != 0) { // Check if the resource was found
                            Image(
                                painter = painterResource(id = resourceId),
                                contentDescription = null,
                                modifier = Modifier
                                    .size(200.dp)
                                    .clickable { navController.navigate(Screen.Content.route) }
                            )
                        } else {
                            Text("Image not found")
                        }

                    }
                }
                Button(
                    onClick = { navController.navigate(Screen.Channel.route) }
                ) {
                    Text("Channel")
                }
            }
        }
    )
}