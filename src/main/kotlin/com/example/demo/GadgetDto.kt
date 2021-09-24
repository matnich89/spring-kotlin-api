package com.example.demo

class GadgetDto(
    val gadgetId: Long,
    val gadgetName: String,
    val gadgetCategory: String?,
    val gadgetAvailability: Boolean = true,
    val gadgetPrice: Double,
)

fun GadgetDto.toEntity() = Gadget(gadgetId, gadgetName, gadgetCategory, gadgetAvailability, gadgetPrice)
