package me.josena.padel.utils

import me.josena.padel.data.Booking

class Utils {

    companion object {

        private var AMADOR_RIVAS = Booking(
            "Amador Rivas",
            "666446644",
            "17/03/2021",
            "11:00",
            "Si voy con lo que te doy."
        )

        private var JAVIER_MAROTO = Booking(
            "Javier Maroto",
            "777555333",
            "23/05/2022",
            "17:00",
            "Huevon, huevon, huevon..."
        )

        private var ANTONIO_RECIO = Booking(
            "Antonio Recio",
            "616555347",
            "29/06/2023",
            "19:00",
            "Viva España y viva el Rey."
        )
        //Mocked list instead of API connection
        var MOCKED_LIST = mutableListOf(AMADOR_RIVAS, JAVIER_MAROTO, ANTONIO_RECIO)
    }
}