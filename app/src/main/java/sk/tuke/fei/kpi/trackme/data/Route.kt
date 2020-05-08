package sk.tuke.fei.kpi.trackme.data

data class Route (
    val timestamp: Long,
    val trackingRoute: MutableList<TrackingData>,
    val userId: String
)