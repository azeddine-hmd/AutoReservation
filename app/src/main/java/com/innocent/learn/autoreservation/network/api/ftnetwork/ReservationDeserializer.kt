package com.innocent.learn.autoreservation.network.api.ftnetwork

import com.google.gson.JsonDeserializationContext
import com.google.gson.JsonDeserializer
import com.google.gson.JsonElement
import com.innocent.learn.autoreservation.model.Slot
import java.lang.reflect.Type
import java.text.SimpleDateFormat
import java.util.*

class ReservationDeserializer : JsonDeserializer<Slots> {
	override fun deserialize(
		json: JsonElement?,
		typeOfT: Type?,
		context: JsonDeserializationContext?
	): Slots {
		val slotsList = mutableListOf<Slot>()
		val slotsJsonArray = json?.asJsonArray
		if (slotsJsonArray != null) {
			for (slotJsonElement in slotsJsonArray) {
				val slotJsonObject = slotJsonElement.asJsonObject
				val event = slotJsonObject.get("Event").asJsonObject

				val id = event.get("event_id").asInt

				val beginString = event.get("begin_at").asString
					.replace('T', ' ')
					.replace('Z', ' ')
				var begin = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault())
					.parse(beginString) ?: Date()

				// adding one hour to begin date
				val calendarBegin = Calendar.getInstance()
				calendarBegin.time = begin
				calendarBegin.add(Calendar.HOUR_OF_DAY, 1)
				begin = calendarBegin.time

				val endString = event.get("end_at").asString
					.replace('T', ' ')
					.replace('Z', ' ')
				var end = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault())
					.parse(endString) ?: Date()

				// adding one hour to "begin date"
				val calendarEnd = Calendar.getInstance()
				calendarEnd.time = end
				calendarEnd.add(Calendar.HOUR_OF_DAY, 1)
				end = calendarEnd.time

				val zoneId = event.get("zone_id").asInt
				var cluster = 0
				if (zoneId == 47) {
					cluster = 1
				} else if (zoneId == 48) {
					cluster = 2
				}

				val reservedPlaces = event.get("number_of_places_reserved").asInt

				val isSubscribed = slotJsonObject.get("is_subscribed").asBoolean

				slotsList.add(Slot(id, begin, end, cluster, reservedPlaces, isSubscribed, false))
			}
		}
		return Slots(slotsList.toList())
	}

}