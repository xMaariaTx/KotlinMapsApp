package com.example.mapsappmariabaos.repositori

import com.example.mapsappmariabaos.model.MapDataBase
import com.example.mapsappmariabaos.network.SupabaseClient
import io.github.jan.supabase.postgrest.postgrest
import kotlin.uuid.ExperimentalUuidApi
import io.github.jan.supabase.postgrest.query.Order

class TascaRepository {
    private val taula = SupabaseClient.client.postgrest["mapdatabase"]

    @OptIn(ExperimentalUuidApi::class)
    suspend fun afegirTasca(titol: String, coordlat: Float, coordlog: Float, marked: Boolean, desc: String, img: String?) {
        val novaMapDataBase = MapDataBase(name = titol, coordlat = coordlat, coordlong = coordlog, marked  = marked, description = desc, image = img)
        taula.insert(novaMapDataBase)
    }

    suspend fun obtenirTasques(): List<MapDataBase> {
        return taula.select() {
            order("id", order = Order.ASCENDING)
        }.decodeList<MapDataBase>()
    }

    suspend fun actualitzarDetallsTasca(
        id: Int,
        nouTitol: String,
        novaDesc: String,
        currentImageUri: String?
    ) {
        taula.update({
            set("name", nouTitol)
            set("description", novaDesc)
            set("image", currentImageUri)
        }) {
            filter {
                eq("id", id)
            }
        }
    }

    suspend fun esborrarTasca(id: Int) {
        taula.delete {
            filter {
                eq("id", id)
            }
        }
    }
}