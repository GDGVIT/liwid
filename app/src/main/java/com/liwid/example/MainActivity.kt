package com.liwid.example
import android.app.Activity
import android.content.Context
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Arrangement.spacedBy
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment.Companion.CenterHorizontally
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.dscvit.liwid.LiveWidget
import com.dscvit.liwid.widget.LiveSportsWidget
import com.liwid.example.ui.theme.CustomTheme
import com.liwid.example.util.API_KEY
import com.liwid.example.util.BASE_URL
import com.liwid.example.util.GAME_TYPE
import com.liwid.example.util.MATCH_TYPE
import com.liwid.example.util.league_id
import kotlinx.coroutines.DelicateCoroutinesApi

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            CustomTheme(darkTheme = true) {
                LiveWidgetExample()
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class, DelicateCoroutinesApi::class)
@Composable
fun LiveWidgetExample() {
    var isWidgetEnabled by remember { mutableStateOf(false) }
    val context = LocalContext.current
    val activity= LocalContext.current as Activity
    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(text = "Live Widget Example")
                }
            )
        },
        content = { paddingValues ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues), // use the paddingValues here
                verticalArrangement = spacedBy(16.dp)
            ) {
                Text(text = "Toggle the Live Widget:")

                Switch(
                    checked = isWidgetEnabled,
                    onCheckedChange = { newCheckedState ->
                        isWidgetEnabled = newCheckedState
                        if (newCheckedState) {
                            createCustomChannel(context,activity)
                            startSportsWidgetService(context)
                        } else {
                            // Stop the widget service if needed
                        }
                    },
                    modifier = Modifier.align(CenterHorizontally)
                )

                Text(text = if (isWidgetEnabled) "Stop Widget" else "Start Widget")
            }
        }
    )
}

fun createCustomChannel(context: Context, activity: Activity) {
    val liveWidget = LiveWidget(context = context, activity = activity, widgetType = LiveWidget.WidgetType.SPORTS)
// Configure channel settings
    liveWidget.configureChannel(
        id = "Custom_Channel_Id",
        name = "Custom Channel Name",
        description = "Custom Channel Description"
    )

}

@DelicateCoroutinesApi
fun startSportsWidgetService(context: Any) {
    LiveSportsWidget.create(
        context = context,
        activity = context as Activity,
        baseUrl = BASE_URL,
        endpoint = GAME_TYPE,
        params = mapOf(
            "met" to MATCH_TYPE,
            "APIkey" to API_KEY,
            "leagueID" to league_id,
        )
    )
}


@Preview(showBackground = true)
@Composable
fun PreviewLiveWidgetExample() {
    CustomTheme {
        LiveWidgetExample()
    }
}
