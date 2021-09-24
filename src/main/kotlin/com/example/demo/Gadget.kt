package com.example.demo

import javax.persistence.*

@Entity
@Table(name = "GADET")
data class Gadget (
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    val gadgetId : Long,
    val gadgetName : String,
    val gadgetCategory : String?,
    val gadgetAvailability : Boolean = true,
    val gadgetPrice : Double,
)
