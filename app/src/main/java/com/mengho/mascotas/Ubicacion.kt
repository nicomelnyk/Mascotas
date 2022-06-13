package com.mengho.mascotas

import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.clustering.ClusterItem

class Ubicacion(val nombre: String?, val latitud: Double, val longitud: Double, val pedido: Pedido) :
    ClusterItem {
    override fun getPosition(): LatLng {
        return LatLng(latitud, longitud)
    }

    override fun getTitle(): String? {
        return nombre
    }

    override fun getSnippet(): String {
        return pedido.nombre
    }

}