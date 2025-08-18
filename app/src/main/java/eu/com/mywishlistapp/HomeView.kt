package eu.com.mywishlistapp

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.DismissDirection
import androidx.compose.material.DismissValue
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.MaterialTheme
import androidx.compose.material.SwipeToDismiss
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.rememberDismissState
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun HomeView(
    paddingValues: PaddingValues,
    navController: NavHostController,
    viewModel: WishViewModel
) {
    var wishToDelete by remember { mutableStateOf<Wish?>(null) }
    var selectedWish by remember { mutableStateOf<Wish?>(null) }
    var isEditMode by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            AppBarView(
                title = if (isEditMode) "Select Action" else "Wishlist",
                onBackNavClicked = {
                    // Exit edit mode when back is pressed
                    isEditMode = false
                    selectedWish = null
                },
                actions = {
                    if (isEditMode && selectedWish != null) {
                        // Edit button
                        IconButton(onClick = {
                            val id = selectedWish!!.id
                            navController.navigate(Screen.AddScreen.route + "/$id")
                            isEditMode = false
                            selectedWish = null
                        }) {
                            Icon(
                                imageVector = Icons.Default.Edit,
                                contentDescription = "Edit"
                            )
                        }

                        // Delete button
                        IconButton(onClick = {
                            wishToDelete = selectedWish
                            isEditMode = false
                        }) {
                            Icon(
                                imageVector = Icons.Default.Delete,
                                contentDescription = "Delete"
                            )
                        }
                    }
                }
            )
        },
        modifier = Modifier
            .fillMaxWidth()
            .padding(WindowInsets.statusBars.asPaddingValues()),
        floatingActionButton = {
            FloatingActionButton(
                onClick = {
                    navController.navigate(Screen.AddScreen.route + "/0L")
                },
                modifier = Modifier.padding(all = 20.dp),
                containerColor = colorResource(id = R.color.App_bar_color),
                contentColor = Color.White
            ) {
                Icon(imageVector = Icons.Default.Add, contentDescription = null)
            }
        }
    ) { innerPadding ->
        val wishList = viewModel.getAllWish.collectAsState(initial = listOf())

        LazyColumn(
            modifier = Modifier
                .fillMaxWidth()
                .padding(innerPadding)
        ) {
            items(wishList.value,
                key = { it.id }) {
                wish ->
                val dismissState = rememberDismissState(
                    confirmStateChange = { dismissValue ->
                        if (dismissValue == DismissValue.DismissedToStart) {
                            wishToDelete = wish
                            false // Show dialog instead of auto delete
                        } else false
                    }
                )

                SwipeToDismiss(
                    state = dismissState,
                    directions = setOf(DismissDirection.EndToStart),
                    background = {
                        Box(
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(end = 16.dp),
                            contentAlignment = Alignment.CenterEnd
                        ) {
                            Icon(
                                imageVector = Icons.Default.Delete,
                                contentDescription = "Delete",
                                tint = MaterialTheme.colors.background
                            )
                        }
                    },
                    dismissContent = {
                        WishItem(
                            wish = wish,
                            onClick = {
                                val id = wish.id
                                navController.navigate(Screen.AddScreen.route + "/$id")
                            },
                            onLongClick = {
                                selectedWish = wish
                                isEditMode = true
                            }
                        )
                    }
                )
            }
        }

        // Confirm delete dialog
        if (wishToDelete != null) {
            AlertDialog(
                onDismissRequest = { wishToDelete = null },
                title = { Text("Delete Wish") },
                text = { Text("Are you sure you want to delete this wish?") },
                confirmButton = {
                    TextButton(onClick = {
                        viewModel.deleteAWish(wishToDelete!!)
                        wishToDelete = null
                    }) {
                        Text("Yes")
                    }
                },
                dismissButton = {
                    TextButton(onClick = { wishToDelete = null }) {
                        Text("No")
                    }
                }
            )
        }
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun WishItem(wish: Wish, onClick: () -> Unit, onLongClick: () -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 2.dp, start = 1.dp, end = 1.dp)
            .combinedClickable(
                onClick = onClick,
                onLongClick = onLongClick
            ),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        colors = CardDefaults.cardColors(
            containerColor = colorResource(id = R.color.App_bar_color)
        )
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Text(text = wish.title, fontWeight = FontWeight.Bold, color = Color.White)
            Text(text = wish.description, color = Color.White)
        }
    }
}

