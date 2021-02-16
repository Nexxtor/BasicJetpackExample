package com.naldana.myapplication.data.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName

@Entity(tableName = "pokemon_table")
data class Pokemon(
    @PrimaryKey
    var id: Int,
    @SerializedName("name")
    var name: String
)
