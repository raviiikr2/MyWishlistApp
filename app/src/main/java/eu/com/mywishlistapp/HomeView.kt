package eu.com.mywishlistapp

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
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
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Share

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
                title = "Wishlist",
                onBackNavClicked = {},
                actions = {
                    if (isEditMode && selectedWish != null) {
                        IconButton(onClick = {
                            val id = selectedWish!!.id
                            navController.navigate(Screen.AddScreen.route + "/$id")
                            isEditMode = false
                            selectedWish = null
                        }, modifier = Modifier.padding(top = 20.dp)) {
                            Icon(imageVector = Icons.Default.Edit, contentDescription = "Edit")
                        }
                        IconButton(onClick = {
                            wishToDelete = selectedWish
                            isEditMode = false
                            selectedWish = null
                        }, modifier = Modifier.padding(top = 20.dp)) {
                            Icon(imageVector = Icons.Default.Delete, contentDescription = "Delete")
                        }
                        IconButton(onClick = {
                            selectedWish?.let { wish ->
                                ShareWishAsPdf(navController.context, wish)
                            }
                            isEditMode = false
                            selectedWish = null
                        }, modifier = Modifier.padding(top = 20.dp)) {
                            Icon(
                                imageVector = Icons.Default.Share,
                                contentDescription = "Share",
                                tint = Color.Unspecified
                            )
                        }
                    }
                }
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = { navController.navigate(Screen.AddScreen.route + "/0L") },
                modifier = Modifier.padding(20.dp),
                containerColor = colorResource(id = R.color.App_bar_color),
                contentColor = Color.White
            ) { Icon(imageVector = Icons.Default.Add, contentDescription = null) }
        }
    ) { innerPadding ->
        val wishList = viewModel.getAllWish.collectAsState(initial = listOf())

        LazyVerticalGrid(
            columns = GridCells.Fixed(3),            //  three notes per row
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding),
            contentPadding = PaddingValues(8.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(
                items = wishList.value,
                key = { it.id }                      // stable key for smooth updates
            ) { wish ->
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
                    }) { Text("Yes", color = Color.Magenta) }
                },
                dismissButton = {
                    TextButton(onClick = { wishToDelete = null }) {
                        Text("No", color = Color.Magenta)
                    }
                }
            )
        }
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun WishItem(
    wish: Wish,
    onClick: () -> Unit,
    onLongClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val words = remember(wish.description) { wish.description.trim().split(Regex("\\s+")) }
    val shortDescription = remember(words) {
        val cut = words.take(5).joinToString(" ")
        if (words.size > 10) "$cut…" else cut
    }

    Card(
        modifier = modifier
            .fillMaxWidth()
            .height(110.dp)                          // ⬇ smaller tile height
            .combinedClickable(onClick = onClick, onLongClick = onLongClick),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        colors = CardDefaults.cardColors(containerColor = colorResource(id = R.color.App_bar_color))
    ) {
        Column(
            modifier = Modifier.padding(10.dp),
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = wish.title,
                fontWeight = FontWeight.Bold,
                color = Color.White,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
            Spacer(Modifier.height(4.dp))
            Text(
                text = shortDescription,
                color = Color.White,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis
            )
        }
    }
}
