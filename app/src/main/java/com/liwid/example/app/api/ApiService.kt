package com.liwid.example.app.api

import com.liwid.example.app.model.MatchResponse
import com.liwid.example.app.util.API_KEY
import com.liwid.example.app.util.GAME_TYPE
import com.liwid.example.app.util.MATCH_TYPE
import com.liwid.example.app.util.league_id
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiService {
    @GET(GAME_TYPE)
    fun getMatch(
        @Query("met") eventType:String= MATCH_TYPE,
        @Query("APIkey")apikey:String= API_KEY,
        @Query("leagueID")leagueId:Int= league_id
    ): Call<MatchResponse>
}