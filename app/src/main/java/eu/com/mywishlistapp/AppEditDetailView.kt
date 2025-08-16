package eu.com.mywishlistapp

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import androidx.compose.material3.*
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.launch

import androidx.compose.material.Scaffold
import androidx.compose.material.rememberScaffoldState
import androidx.compose.runtime.collectAsState


@Composable

fun AppEditDetailView(
    id: Long,
    viewModel: WishViewModel,
    navController: NavHostController = rememberNavController()
){
    val snackMessage = remember {
        mutableStateOf("")
    }

    val scope = rememberCoroutineScope()
    val scaffoldState = rememberScaffoldState()

    if(id!=0L){
        val wish = viewModel.getAWishesByID(id).collectAsState(initial = Wish(0,"",""))
        viewModel.wishTitleState = wish.value.title
        viewModel.wishDescriptionState = wish.value.description
        }
    else {
        viewModel.wishTitleState = ""
        viewModel.wishDescriptionState = ""
    }
    Scaffold (
        scaffoldState = scaffoldState,
        modifier = Modifier.padding(WindowInsets.statusBars.asPaddingValues()),
        topBar = {
            AppBarView(title =
                if(id != 0L)stringResource(id = R.string.Update_Wish)  //update wish
                else stringResource(id = R.string.add_wish),           // Add wish
                onBackNavClicked = {
                    // navigate to home page
                navController.navigateUp()
        })
                })
    {
        Column(modifier = Modifier
            .padding(it)
            .wrapContentSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
            ) {
            Spacer(modifier = Modifier.height(10.dp))
            OutlinedTextField(
                value = viewModel.wishTitleState,
                onValueChange = { viewModel.onWishTitleChange(it) },
                label = { Text("Title") },
                modifier = Modifier.fillMaxWidth()
                    .padding(8.dp),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = colorResource(id = R.color.black),
                    unfocusedBorderColor = colorResource(id = R.color.black),
                    cursorColor = colorResource(id = R.color.black),
                    focusedLabelColor = colorResource(id = R.color.black),
                    unfocusedLabelColor = colorResource(id = R.color.black)
                )
            )

            WishTextField(
                label = "Description",
                value = viewModel.wishDescriptionState,
                onValueChange = {
                    viewModel.onWishDescriptionChange(it)
                }

            )
            Spacer(modifier = Modifier.height(10.dp))

            Button(
                onClick = {
                    if(viewModel.wishTitleState.isNotEmpty() && viewModel.wishDescriptionState.isNotEmpty()){
                        if(id != 0L){
                            viewModel.updateAWish(
                                Wish(
                                    id = id,
                                    title = viewModel.wishTitleState.trim(),
                                    description = viewModel.wishDescriptionState.trim()
                                )
                            )
                            navController.navigateUp()
                        }
                        else {
                            // add a new wish
                            viewModel.addWish(
                                Wish(
                                    title = viewModel.wishTitleState,
                                    description = viewModel.wishDescriptionState.trim()
                                ))
                            snackMessage.value = "Wish Added"
                            navController.navigateUp()
                        }
                    }
                    else {
                        snackMessage.value = "Enter field to Create a Wish"
                    }
                    scope.launch {
                        scaffoldState.snackbarHostState.showSnackbar(snackMessage.value)
                        navController.navigateUp()
                    }
                }) {
                Text(
                    text = if (id != 0L) stringResource(id = R.string.Update_Wish)  //update wish
                    else stringResource(id = R.string.add_wish),
                    style = androidx.compose.ui.text.TextStyle(fontSize = 16.sp)
                )

            }
    }

}}

@Composable
fun WishTextField(
    value: String,
    onValueChange: (String) -> Unit,
    label: String
) {
    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        label = { Text(text = label) },
        modifier = Modifier.fillMaxWidth()
            .padding(8.dp)
            .height(200.dp),
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text),
        colors = OutlinedTextFieldDefaults.colors(
            focusedBorderColor = colorResource(id = R.color.black),
            unfocusedBorderColor = colorResource(id = R.color.black),
            cursorColor = colorResource(id = R.color.black),
            focusedLabelColor = colorResource(id = R.color.black),
            unfocusedLabelColor = colorResource(id = R.color.black)
        )
    )
}
