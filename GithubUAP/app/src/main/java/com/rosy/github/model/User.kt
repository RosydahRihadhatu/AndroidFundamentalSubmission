package com.rosy.github.model

import com.squareup.moshi.Json

data class User(
    @field:Json(name = "id")
    val id: Int,

    @field:Json(name = "login")
    val username: String,

    @field:Json(name = "name")
    val name: String,

    @field:Json(name = "followers")
    val followers: Int,

    @field:Json(name = "following")
    val following: Int,

    @field:Json(name = "avatar_url")
    val avatar: String
)
