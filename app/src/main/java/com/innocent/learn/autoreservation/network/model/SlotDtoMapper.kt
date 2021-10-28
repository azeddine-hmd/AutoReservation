package com.innocent.learn.autoreservation.network.model

import com.innocent.learn.autoreservation.domain.model.Slot
import com.innocent.learn.autoreservation.domain.util.DomainMapper

object SlotDtoMapper : DomainMapper<SlotDto, Slot> {
	
	override fun mapToDomainModel(model: SlotDto): Slot {
		return Slot(
			id = model.event.eventId,
			begin = model.event.beginAt,
			end = model.event.endAt,
			cluster = if (model.event.zoneId == 47) 1 else 2,
			reservedPlaces = model.event.numberOfPlacesReserved,
			isSubscribed = model.isSubscribed
		)
	}
	
	override fun mapFromDomainModel(domainModel: Slot): SlotDto {
		return SlotDto(
			event = SlotDto.Event(
				eventId = domainModel.id,
				beginAt = domainModel.begin,
				endAt = domainModel.end,
				zoneId = if (domainModel.cluster == 1) 47 else 48,
				numberOfPlacesReserved = domainModel.reservedPlaces
			),
			isSubscribed = domainModel.isSubscribed
		)
	}
	
	fun toDomainList(initial: List<SlotDto>): List<Slot> {
		return initial.map { mapToDomainModel(it) }
	}
	
	fun fromDomainList(initial: List<Slot>): List<SlotDto> {
		return initial.map { mapFromDomainModel(it) }
	}
	
}