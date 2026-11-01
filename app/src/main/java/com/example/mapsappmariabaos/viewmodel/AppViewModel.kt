package com.example.mapsappmariabaos.viewmodel

import android.app.Application
import android.content.Context
import android.net.Uri
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.mapsappmariabaos.model.MapDataBase
import com.example.mapsappmariabaos.network.NetworkModule.supabase
import com.example.mapsappmariabaos.repositori.TascaRepository
import io.github.jan.supabase.storage.storage
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch



class AppViewModel (application: Application) : AndroidViewModel(application) {

    //SUPABASE
    private val repository = TascaRepository()

    private val _tasques = MutableStateFlow<List<MapDataBase>>(emptyList())
    val tasques: StateFlow<List<MapDataBase>> = _tasques

    init {
        carregarTasques()
    }

    private fun carregarTasques() {
        viewModelScope.launch(Dispatchers.IO) { // Forzamos el hilo de datos
            try {
                val resultado = repository.obtenirTasques()
                _tasques.value = resultado
                Log.d("SUPABASE", "He cargado ${resultado.size} elementos")
            } catch (e: Exception) {
                Log.e("SUPABASE_ERROR", e.message.toString())
            }
        }
    }

    fun afegirTasca(titol: String, coordlat: Float, coordlog: Float, marked: Boolean, desc: String, img: String?) {
        viewModelScope.launch {
            try {
                // 1. Guardamos en Supabase (solo una vez)
                repository.afegirTasca(titol, coordlat, coordlog, marked, desc, img)

                // 2. Refrescamos la lista llamando a la función
                // Esto hará que el mapa detecte los nuevos datos automáticamente
                carregarTasques()

            } catch (e: Exception) {
                Log.e("ERROR", "Error al añadir marcador: ${e.message}")
            }
        }
    }

    fun editarTasca(id: Int, nouTitol: String, novaDesc: String, currentImageUri: String?) {
        viewModelScope.launch {
            try {
                repository.actualitzarDetallsTasca(id, nouTitol, novaDesc, currentImageUri)
                carregarTasques() // Esto refresca el mapa y la lista automáticamente
            } catch (e: Exception) {
                Log.e("ERROR_EDITAR", "No se pudo actualizar: ${e.message}")
            }
        }
    }

    fun esborrarTascaPerId(id: Int) {
        viewModelScope.launch {
            try {
                repository.esborrarTasca(id)
                carregarTasques()
            } catch (e: Exception) {
                Log.e("ERROR_BORRAR", "No se pudo borrar: ${e.message}")
            }
        }
    }

    suspend fun uploadImageToSupabase(context: Context, imageUri: Uri): String? {
        return try {
            // 1. Llegir els bytes de la imatge
            val bytes = context.contentResolver.openInputStream(imageUri)?.use { it.readBytes() }
                ?: return null

            val fileName = "marker_${System.currentTimeMillis()}.jpg"

            // 2. CANVI: Utilitzem .from() en lloc de claudàtors
            val bucket = supabase.storage.from("markers_images")
            Log.d("foto", "${bucket}")

            // 3. Pujar l'arxiu
            bucket.upload(fileName, bytes, upsert = false)

            // 4. Obtenir la URL pública
            val publicUrl = bucket.publicUrl(fileName)
            publicUrl


        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }

}

