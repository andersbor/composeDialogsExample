package com.example.dialogsexample

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddCircle
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import com.example.dialogsexample.ui.theme.DialogsExampleTheme

class MainActivity : ComponentActivity() {
    @OptIn(ExperimentalLayoutApi::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            DialogsExampleTheme {
                MainScreen()
            }
        }
    }
}

@Composable
@OptIn(ExperimentalLayoutApi::class)
private fun MainScreen() {
    Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
        Column(modifier = Modifier.padding(innerPadding)) {
            var showAlertDialog by remember { mutableStateOf(false) }
            var showMinimalDialog by remember { mutableStateOf(false) }
            var showImageDialog by remember { mutableStateOf(false) }
            var showAuthDialog by rememberSaveable { mutableStateOf(false) }

            // https://developer.android.com/develop/ui/compose/layouts/flow
            FlowRow(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                //maxItemsInEachRow = 2
            ) {
                Button(onClick = { showMinimalDialog = true }) {
                    Text("Minimal Dialog")
                }
                Button(onClick = { showAlertDialog = true }) {
                    Text("Alert Dialog")
                }
                Button(onClick = { showImageDialog = true }) {
                    Text("Dialog with Image")
                }
                Button(onClick = { showAuthDialog = true }) {
                    Text("Auth Dialog")
                }
            }

            if (showAlertDialog) {
                AlertDialogExample(
                    onDismissRequest = { showAlertDialog = false },
                    onConfirmation = { showAlertDialog = false },
                    dialogTitle = "Dialog Title",
                    dialogText = "Dialog Text",
                    icon = Icons.Filled.AddCircle
                )
            }
            if (showMinimalDialog) {
                MinimalDialog(
                    onDismissRequest = { showMinimalDialog = false }
                )
            }
            if (showImageDialog) {
                DialogWithImage(
                    onDismissRequest = { showImageDialog = false },
                    onConfirmation = { showImageDialog = false },
                    painter = painterResource(id = R.drawable.ic_launcher_foreground),
                    imageDescription = "Example Image"
                )
            }
            var message by remember { mutableStateOf("") }
            fun checkEmailPassword(email: String, password: String) {
                if (email == "anbo@zealand.dk" && password == "secret12") {
                    showAuthDialog = false
                } else {
                    message = "Wrong email or password"
                }
            }

            fun signUp(email: String, password: String) {
                message = "Sign up not implemented"
                showAuthDialog = false
            }
            if (showAuthDialog) {
                AuthDialog(
                    onSignIn = ::checkEmailPassword,
                    // https://kotlinlang.org/docs/reflection.html#function-references
                    onSignUp = ::signUp,
                    onCancel = { showAuthDialog = false },
                    message = message
                )
            }
        }
    }
}


// https://developer.android.com/develop/ui/compose/components/dialog
@Composable
fun AlertDialogExample(
    onDismissRequest: () -> Unit,
    onConfirmation: () -> Unit,
    dialogTitle: String,
    dialogText: String,
    icon: ImageVector,
) {
    AlertDialog(
        icon = { Icon(icon, contentDescription = "Example Icon") },
        title = { Text(text = dialogTitle) },
        text = { Text(text = dialogText) },
        onDismissRequest = { onDismissRequest() },
        confirmButton = {
            TextButton(onClick = { onConfirmation() })
            { Text("Confirm") }
        },
        dismissButton = {
            TextButton(onClick = { onDismissRequest() })
            { Text("Dismiss") }
        }
    )
}

@Composable
fun MinimalDialog(onDismissRequest: () -> Unit) {
    Dialog(onDismissRequest = { onDismissRequest() }) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .height(200.dp)
                .padding(16.dp),
            shape = RoundedCornerShape(16.dp),
        ) {
            Text(
                text = "This is a minimal dialog",
                modifier = Modifier
                    .fillMaxSize()
                    .wrapContentSize(Alignment.Center),
                textAlign = TextAlign.Center,
            )
        }
    }
}

@Composable
fun DialogWithImage(
    onDismissRequest: () -> Unit,
    onConfirmation: () -> Unit,
    painter: Painter,
    imageDescription: String,
) {
    Dialog(onDismissRequest = { onDismissRequest() }) {
        // Draw a rectangle shape with rounded corners inside the dialog
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .height(375.dp)
                .padding(16.dp),
            shape = RoundedCornerShape(16.dp),
        ) {
            Column(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                Image(
                    painter = painter,
                    contentDescription = imageDescription,
                    contentScale = ContentScale.Fit,
                    modifier = Modifier.height(160.dp)
                )
                Text(
                    text = "This is a dialog with buttons and an image.",
                    modifier = Modifier.padding(16.dp),
                )
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.Center,
                ) {
                    TextButton(
                        onClick = { onDismissRequest() },
                        modifier = Modifier.padding(8.dp),
                    ) { Text("Dismiss") }
                    TextButton(
                        onClick = { onConfirmation() },
                        modifier = Modifier.padding(8.dp),
                    ) { Text("Confirm") }
                }
            }
        }
    }
}

@Composable
fun AuthDialog(
    onSignIn: (email: String, password: String) -> Unit,
    onSignUp: (email: String, password: String) -> Unit = { _: String, _: String -> },
    onCancel: () -> Unit = { },
    message: String = "",
) {
    Dialog(onDismissRequest = { onCancel() }) {
        Card(shape = RoundedCornerShape(16.dp))
        {
            Column(
                modifier = Modifier.padding(16.dp),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally,
            )
            {
                var email by remember { mutableStateOf("") }
                var password by remember { mutableStateOf("") }
                OutlinedTextField(
                    modifier = Modifier.fillMaxWidth(),
                    value = email,
                    onValueChange = { email = it },
                    label = { Text("Email") },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email)
                )
                OutlinedTextField(
                    modifier = Modifier.fillMaxWidth(),
                    value = password,
                    onValueChange = { password = it },
                    label = { Text("Password") },
                    // https://alitalhacoban.medium.com/show-hide-password-jetpack-compose-d0c4abac568f
                    visualTransformation = PasswordVisualTransformation(),
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password)
                )
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.Center,
                ) {
                    Button(onClick = onCancel) {
                        Text("Cancel")
                    }
                    Button(onClick = { onSignIn(email, password) }) {
                        Text("Sign in")
                    }
                    Button(onClick = { onSignUp(email, password) }) {
                        Text("Sign up")
                    }
                }
                Text(text = message)
            }
        }
    }
}



