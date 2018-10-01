package fr.tnducrocq.keepmyrunning

class UnitUtils {
    companion object {


        /*fun minAtKm(timeInMs: Long, distanceInMeter: Int): Float {
            return (timeInMs / 60000F) / (distanceInMeter / 1000F)
        }*/

        fun minAtKm(timeInMs: Long, distanceInMeter: Int, minutes: (minutes: Int) -> Unit, secondes: (secondes: Int) -> Unit) {
            val timeInSecByKm = ((timeInMs / 1000F) / (distanceInMeter / 1000F)).toInt()
            minutes.invoke(timeInSecByKm / 60)
            secondes.invoke(timeInSecByKm % 60)
        }

        fun minAtKm(timeInMs: Long, distanceInMeter: Int): String {
            val timeInSecByKm = ((timeInMs / 1000F) / (distanceInMeter / 1000F)).toInt()
            return "${timeInSecByKm / 60},${timeInSecByKm % 60}"
        }

    }
}