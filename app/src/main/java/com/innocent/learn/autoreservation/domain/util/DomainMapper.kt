package com.innocent.learn.autoreservation.domain.util

interface DomainMapper <Model, DomainModel> {
	
	fun mapToDomainModel(model: Model): DomainModel
	
	fun mapFromDomainModel(domainModel: DomainModel): Model
	
}