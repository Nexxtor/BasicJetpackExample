package com.naldana.myapplication.network.response

data class PokemonListResponse(
    var count: Int,
    var next: String?,
    var previous: String?,
    var results: List<PokemonMetaDataResponse>
)