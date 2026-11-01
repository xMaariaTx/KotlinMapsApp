package com.example.mapsappmariabaos.network

import io.github.jan.supabase.createSupabaseClient
import io.github.jan.supabase.postgrest.Postgrest

object SupabaseClient {
    val client = createSupabaseClient(
        supabaseUrl = "https://ozeimjofeelothcrepui.supabase.co",
        supabaseKey = "sb_publishable_xOBeOixx3sSbK4b7RbESrw_qkZw5t7M"
    ) {
        install(Postgrest)
    }
}