package sk.tuke.fei.kpi.trackme.data

data class TrackingData (
    val userId: String,
    val timestamp: Long,
    val longitude: Float,
    val latitude: Float,
    val altitude: Float,
    val state: String
)