package com.example.diaryapp.presentation.screens.home

import android.annotation.SuppressLint
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import com.example.diaryapp.R
import com.example.diaryapp.data.repository.Diaries
import com.example.diaryapp.models.RequestState
import java.time.ZonedDateTime

@OptIn(ExperimentalMaterial3Api::class)
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun HomeScreen(
    diaries: Diaries,
    drawerState:DrawerState,
    onMenuClicked: () -> Unit,
    dateIsSelected: Boolean,
    onDateSelected: (ZonedDateTime) -> Unit,
    onDateReset: () -> Unit,
    onSignOutClicked: () -> Unit,
    navigateToWrite: () -> Unit,
    navigateToWriteWithArgs: (String) -> Unit
){
    var padding by remember { mutableStateOf(PaddingValues()) }
    val scrollBehaviour = TopAppBarDefaults.exitUntilCollapsedScrollBehavior()

   NavigationDrawer(
       drawerState = drawerState,
       onSignOutClicked = onSignOutClicked
   ) {
       Scaffold(
           modifier = Modifier.nestedScroll(scrollBehaviour.nestedScrollConnection),
           topBar = {
               HomeTopBar(
                   scrollBehavior = scrollBehaviour,
                   onMenuClicked = onMenuClicked,
                   dateIsSelected = dateIsSelected,
                   onDateSelected = onDateSelected,
                   onDateReset = onDateReset
               )
           },
           floatingActionButton = {
               FloatingActionButton(
                   modifier = Modifier.padding(end = padding.calculateEndPadding(LayoutDirection.Ltr)),
                   onClick = navigateToWrite) {
                   Icon(
                       imageVector = Icons.Default.Edit,
                       contentDescription = "New Diary Icon"
                   )
               }
           },
           content =  {
               padding = it
               when(diaries){
                   is RequestState.Success -> {
                       HomeContent(
                           paddingValues = it,
                           diaryNotes = diaries.data,
                           onClick = navigateToWriteWithArgs
                       )
                   }
                   is RequestState.Error -> {
                       EmptyPage(
                           title = "Error",
                       subtitle = "${diaries.error.message}"
                       )
                   }
                   is RequestState.Loading -> {
                       Box(
                           modifier = Modifier.fillMaxSize(),
                           contentAlignment = Alignment.Center
                       ){
                           CircularProgressIndicator()
                       }
                   }
                   else -> {}
               }
           }
       )
   }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NavigationDrawer(
    drawerState: DrawerState,
    onSignOutClicked: () -> Unit,
    content: @Composable () -> Unit
) {
    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {
            ModalDrawerSheet(
                content = {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(250.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Image(
                            modifier = Modifier.size(250.dp),
                            painter = painterResource(id = com.example.diaryapp.R.drawable.logo),
                            contentDescription = "Logo Image"
                        )
                    }
                    NavigationDrawerItem(
                        label = {
                            Row(modifier = Modifier.padding(horizontal = 12.dp)) {
                                Image(
                                    painter = painterResource(id = R.drawable.google_logo),
                                    contentDescription = "Google Logo"
                                )
                                Spacer(modifier = Modifier.width(12.dp))
                                Text(
                                    text = "Sign out",
                                    color = MaterialTheme.colorScheme.onSurface
                                )
                            }
                        },
                        selected = false,
                        onClick = onSignOutClicked
                    )
                }
            )
        },
        content = content
    )
}