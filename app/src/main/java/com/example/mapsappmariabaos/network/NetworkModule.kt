package com.example.mapsappmariabaos.network

import io.github.jan.supabase.createSupabaseClient
import io.github.jan.supabase.postgrest.Postgrest
import io.github.jan.supabase.storage.Storage

object NetworkModule {
    val supabase = createSupabaseClient(
        supabaseUrl = "https://ozeimjofeelothcrepui.supabase.co",
        supabaseKey = "sb_publishable_xOBeOixx3sSbK4b7RbESrw_qkZw5t7M"
    ) {
        install(Storage) // Para las fotos
        install(Postgrest) // Para la base de datos (tus marcadores)
    }
}