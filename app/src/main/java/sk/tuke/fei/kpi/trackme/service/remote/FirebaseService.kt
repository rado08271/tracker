package sk.tuke.fei.kpi.trackme.service.remote

import com.google.firebase.database.FirebaseDatabase
import sk.tuke.fei.kpi.trackme.data.Route
import sk.tuke.fei.kpi.trackme.data.TrackingData

object FirebaseService {
    private val firebase = FirebaseDatabase.getInstance().getReference("data")

    fun addDataToDatabase(trackingData: TrackingData) {
        firebase.child(trackingData.userId).child(trackingData.timestamp.toString()).setValue(trackingData)
    }

    fun addRouteToDatabase(route: Route) {
        firebase.child(route.userId).child(route.timestamp.toString()).setValue(route)
    }
}