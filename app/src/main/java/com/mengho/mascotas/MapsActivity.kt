package com.mengho.mascotas

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.widget.TextView
import android.widget.Toast
import com.google.android.gms.maps.CameraUpdateFactory

import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.GoogleMap.InfoWindowAdapter
import com.google.android.gms.maps.GoogleMap.OnInfoWindowClickListener
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import com.google.maps.android.clustering.ClusterManager
import org.json.JSONException

class MapsActivity : AppCompatActivity(), OnMapReadyCallback, GoogleMap.OnMarkerClickListener {

    private val FLORES = LatLng(-31.952854, 115.857342)
    private val CABALLITO = LatLng(-33.87365, 151.20689)
    private val MATADEROS = LatLng(-27.47093, 153.0235)

    private var marker: Marker? = null
    private var markerPerth: Marker? = null
    private var markerSydney: Marker? = null
    private var markerBrisbane: Marker? = null

    private var mClusterManager: ClusterManager<Ubicacion>? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_maps)
        val mapFragment =
            supportFragmentManager.findFragmentById(R.id.map) as SupportMapFragment?
        mapFragment!!.getMapAsync(this)
    }

    /** Called when the map is ready.  */
    override fun onMapReady(map: GoogleMap) {

        val transito = Pedido("Tránsito", BitmapDescriptorFactory.HUE_ORANGE, 0.9f)
        val sos = Pedido("S.O.S", BitmapDescriptorFactory.HUE_RED, 0.9f)
        val adopcion = Pedido("Adopción", BitmapDescriptorFactory.HUE_YELLOW, 0.9f)
        val veterinario = Pedido("Veterinario", BitmapDescriptorFactory.HUE_AZURE, 0.9f)
        val paseador = Pedido("Paseador", BitmapDescriptorFactory.HUE_CYAN, 0.9f)

        val flores = Ubicacion("Flores", -34.63762377894466, -58.459773558012856, transito)
        val caballito = Ubicacion("Caballito", -34.61577790041594, -58.44160605866824, sos)
        val mataderos = Ubicacion("Mataderos", -34.65924152915296, -58.5046516117133, adopcion)
        val villaDelParque = Ubicacion("Villa del Parque", -34.605476966614084, -58.495297511844676, veterinario)
        val parqueChacabuco = Ubicacion("Parque Chacabuco", -34.63324281974785, -58.4323600978062, paseador)

        //val ubicaciones: List<Ubicacion> = listOf(flores, caballito, mataderos, villaDelParque, parqueChacabuco)
        val ubicaciones: List<Ubicacion> = listOf()

        map.moveCamera(CameraUpdateFactory.newLatLngZoom(LatLng(flores.latitud, flores.longitud), 10f))
        map.mapType = GoogleMap.MAP_TYPE_TERRAIN
        for(ubicacion in ubicaciones) {
            marker = map.addMarker(
                MarkerOptions()
                    .position(LatLng(ubicacion.latitud, ubicacion.longitud))
                    .title(ubicacion.nombre)
                    .alpha(ubicacion.pedido.alpha)
                    //.anchor(1f,2f)
                    .snippet(ubicacion.pedido.nombre)
                    .icon(BitmapDescriptorFactory.defaultMarker(ubicacion.pedido.color))
                    .zIndex(1.0f)
            )
            marker?.tag = 0;
        }

        mClusterManager = ClusterManager<Ubicacion>(this, map)
        map.setOnCameraIdleListener(mClusterManager)

        // Add a custom InfoWindowAdapter by setting it to the MarkerManager.Collection object from
        // ClusterManager rather than from GoogleMap.setInfoWindowAdapter

        // Add a custom InfoWindowAdapter by setting it to the MarkerManager.Collection object from
        // ClusterManager rather than from GoogleMap.setInfoWindowAdapter
        mClusterManager!!.markerCollection.setInfoWindowAdapter(object : InfoWindowAdapter {
            override fun getInfoWindow(marker: Marker): View {
                val inflater = LayoutInflater.from(this@MapsActivity)
                val view: View = inflater.inflate(R.layout.activity_maps, null)
                val textView = view.findViewById<TextView>(R.id.map)
                val text = if (marker.title != null) marker.title else "Cluster Item"
                textView.text = text
                return view
            }

            override fun getInfoContents(marker: Marker): View? {
                return null
            }
        })
        mClusterManager!!.markerCollection.setOnInfoWindowClickListener { marker: Marker? ->
            Toast.makeText(
                this@MapsActivity,
                "Info window clicked.",
                Toast.LENGTH_SHORT
            ).show()
        }

        try {
            readItems()
        } catch (e: JSONException) {
            Toast.makeText(this, "Problem reading list of markers.", Toast.LENGTH_LONG).show()
        }

        // Set a listener for marker click.
        map.setOnMarkerClickListener(this)
    }

    @Throws(JSONException::class)
    private fun readItems() {
        val inputStream = resources.openRawResource(R.raw.radar_search)

        val transito = Pedido("Tránsito", BitmapDescriptorFactory.HUE_ORANGE, 0.9f)
        val sos = Pedido("S.O.S", BitmapDescriptorFactory.HUE_RED, 0.9f)
        val adopcion = Pedido("Adopción", BitmapDescriptorFactory.HUE_YELLOW, 0.9f)
        val veterinario = Pedido("Veterinario", BitmapDescriptorFactory.HUE_AZURE, 0.9f)
        val paseador = Pedido("Paseador", BitmapDescriptorFactory.HUE_CYAN, 0.9f)

        val flores = Ubicacion("Flores", -34.63762377894466, -58.459773558012856, transito)
        val caballito = Ubicacion("Caballito", -34.61577790041594, -58.44160605866824, sos)
        val mataderos = Ubicacion("Mataderos", -34.65924152915296, -58.5046516117133, adopcion)
        val villaDelParque = Ubicacion("Villa del Parque", -34.605476966614084, -58.495297511844676, veterinario)
        val parqueChacabuco = Ubicacion("Parque Chacabuco", -34.63324281974785, -58.4323600978062, paseador)

        //val items: List<Ubicacion>? = MyUbicacionReader().read(inputStream)
        val items: List<Ubicacion>? = listOf(flores, caballito, mataderos, villaDelParque, parqueChacabuco)
        mClusterManager!!.addItems(items)
    }

    /** Called when the user clicks a marker.  */
    override fun onMarkerClick(marker: Marker): Boolean {

        // Retrieve the data from the marker.
        val clickCount = marker.tag as? Int

        // Check if a click count was set, then display the click count.
        clickCount?.let {
            val newClickCount = it + 1
            marker.tag = newClickCount
            Toast.makeText(
                this,
                "${marker.title} has been clicked $newClickCount times.",
                Toast.LENGTH_SHORT
            ).show()
        }

        // Return false to indicate that we have not consumed the event and that we wish
        // for the default behavior to occur (which is for the camera to move such that the
        // marker is centered and for the marker's info window to open, if it has one).
        return false
    }
}