package com.example.lostnfound.data.model

sealed class FormStatus {
    object Idle : FormStatus()
    object Loading : FormStatus()
    data class Success(val message: String) : FormStatus()
    data class Error(val message: String) : FormStatus()

}