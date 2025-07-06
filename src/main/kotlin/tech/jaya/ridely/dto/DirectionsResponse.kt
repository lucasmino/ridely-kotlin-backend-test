package tech.jaya.ridely.dto

import org.springframework.util.RouteMatcher

data class DirectionsResponse(val routes: List<Route> = listOf())
