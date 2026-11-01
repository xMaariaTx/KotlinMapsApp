package com.example.mapsappmariabaos.permissions

import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import android.provider.Settings

@Composable
fun PermissionContent(status: PermissionStatus, onRetry: () -> Unit) {
    val context = LocalContext.current
    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        when (status) {
            PermissionStatus.Unknown -> {
                CircularProgressIndicator()
                Text("Requesting permission…")
            }
            PermissionStatus.Denied -> {
                Text("Permission denied")
                Spacer(Modifier.height(8.dp))
                Button(onClick = onRetry) {
                    Text("Try again")
                }
            }
            PermissionStatus.PermanentlyDenied -> {
                Text("Permission permanently denied")
                Spacer(Modifier.height(8.dp))
                Button(
                    onClick = {
                        context.startActivity(
                            Intent(
                                Settings.ACTION_APPLICATION_DETAILS_SETTINGS,
                                Uri.fromParts(
                                    "package",
                                    context.packageName,
                                    null
                                )
                            )
                        )
                    }
                ) {
                    Text("Open settings")
                }
            }
            PermissionStatus.Granted -> Unit
        }
    }
}
