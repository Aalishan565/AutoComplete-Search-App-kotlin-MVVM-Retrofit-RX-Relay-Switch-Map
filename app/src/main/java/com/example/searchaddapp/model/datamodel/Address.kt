package com.example.searchaddapp.model.datamodel

data class Address(
    val addressString: String,
    val city: String,
    val id: String,
    val latitude: Double,
    val longitude: Double,
    val pinCode: String
)