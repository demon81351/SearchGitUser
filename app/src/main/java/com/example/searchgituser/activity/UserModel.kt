package com.example.searchgituser.activity

import java.math.BigDecimal

data class UserModel(
    val incomplete_results: Boolean,
    val items: List<UserInfo>,
    val total_count: Int
)

data class UserInfo(
    val avatar_url: String,
    val events_url: String,
    val followers_url: String,
    val following_url: String,
    val gists_url: String,
    val gravatar_id: String,
    val html_url: String,
    val id: Int,
    val login: String,
    val node_id: String,
    val organizations_url: String,
    val received_events_url: String,
    val repos_url: String,
    val score: BigDecimal,
    val site_admin: Boolean,
    val starred_url: String,
    val subscriptions_url: String,
    val type: String,
    val url: String
)