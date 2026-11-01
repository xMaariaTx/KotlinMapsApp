package com.example.mapsappmariabaos.permissions

import android.app.Activity
import android.content.pm.PackageManager
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalContext
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat

data class PermissionManagerState(
    val status: PermissionStatus,
    val requestPermissions: () -> Unit
)

@Composable
fun rememberPermissionManager(permission: AppPermission): PermissionManagerState {
    val context = LocalContext.current
    val activity = context as? Activity
    var status by remember {mutableStateOf<PermissionStatus>(PermissionStatus.Unknown)}

    val launcher = rememberLauncherForActivityResult(ActivityResultContracts.RequestMultiplePermissions())
    { results ->
        val allGranted = results.values.all { it }
        status = when {
            allGranted -> PermissionStatus.Granted
            activity != null && permission.permissions.any {
                ActivityCompat.shouldShowRequestPermissionRationale(activity,it)
            } -> PermissionStatus.Denied
            else -> PermissionStatus.PermanentlyDenied
        }
    }
    fun requestPermissions() {
        launcher.launch(permission.permissions.toTypedArray())
    }
    LaunchedEffect(Unit) {
        val allGranted = permission.permissions.all {
            ContextCompat.checkSelfPermission(context, it) == PackageManager.PERMISSION_GRANTED
        }
        status = if (allGranted) {
            PermissionStatus.Granted
        } else {
            PermissionStatus.Unknown
        }
    }
    return PermissionManagerState(
        status = status,
        requestPermissions = ::requestPermissions
    )
}
