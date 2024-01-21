package com.dscvit.liwid.api.model

data class SportsDataResponse(
    var success:Int,
    var result:List<SportsData>
)
data class SportsData(
    var eventStatus: String,
    var leagueName: String,
    var eventId: Int,
    var homeTeamName: String,
    var awayTeamName: String,
    var homeTeamLogo: String,
    var awayTeamLogo: String,
    var homeTeamResult: String?,
    var awayTeamResult: String?,
    var matchResult: String?
)