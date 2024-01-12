<p align="center">
<a href="https://dscvit.com">
	<img width="400" src="https://user-images.githubusercontent.com/56252312/159312411-58410727-3933-4224-b43e-4e9b627838a3.png#gh-light-mode-only" alt="GDSC VIT"/>
</a>
	<h2 align="center">Liwid</h2>
	<h4 align="center">Liwid is an Android Library designed to simplify the integration of dynamic and customisable widgets on the lock screen. 
It uses sophisticated notification integration, placing widgets as notifications on the lock screen. 
This approach grants users immediate access to the dynamic content from their lock screen, transforming their experience
</h4>

## Table of Contents
- [Key Features](#key-features)
- [Configure](#configure)
- [Usage](#usage)
- [Contributors](#contributors)

## Key Features
- [x] Dynamic and Customisable Widgets: Effortlessly integrate customisable and dynamic widgets, enhancing your Android applications.
- [x] Error Handling: handle errors and exceptions gracefully.
- [ ] Custom UI: Easily customize the look and feel of the widget as per your app requirements.
- [ ] Support to more types of widgets: Currently, it supports tracking and sports type widgets, and can be extended to add more widgets in the future.

  <br>

## Configure
### Gradle
Add the following to your project level root `build.gradle`:
```gradle
allprojects {
    repositories {
        ...
        maven { url "https://jitpack.io" }
    }
}
```
Add the dependency:
```gradle
dependencies {
    implementation 'com.github.GDGVIT:Liwid:1.0.0'
}
```

User permissions required:
```xml
<manifest>
    <uses-permission android:name="android.permission.INTERNET"/>
    <uses-permission android:name="android.permission.ACCESS_NETWORK_STATE" />
    <uses-permission android:name="android.permission.POST_NOTIFICATIONS"/>
    <uses-permission android:name="android.permission.FOREGROUND_SERVICE"/>
.
.
</manifest>
```

## Usage
### Changing the notification channel
```kotlin
fun createCustomChannel(context: Context, activity: Activity) {
  val liveWidget = LiveWidget(context = context, activity = activity, widgetType = LiveWidget.WidgetType.SPORTS)
// Configure channel settings
  liveWidget.configureChannel(
    id = "Custom_Channel_Id",
    name = "Custom Channel Name",
    description = "Custom Channel Description"
  )
}
```

### Specifying the API attributes as per widget
```kotlin
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
```

### Starting and Stopping the widget service
```kotlin
GlobalScope.launch {
    // Start the widget service
    LiveSportsWidget.fetchSportsData()
    // Stop the widget service
    LiveSportsWidget.stopSportsWidget()
}
```

## Contributors
<table>
	<tr align="center">
		<td>
		Dev Keshwani
		<p align="center">
			<img src = "https://avatars.githubusercontent.com/u/84137299?s=400&u=706239e377f631a79fc68c59c73370c9503d6b44&v=4" width="150" height="150" alt="Your Name Here (Insert Your Image Link In Src">
		</p>
			<p align="center">
				<a href = "https://github.com/dk-a-dev">
					<img src = "http://www.iconninja.com/files/241/825/211/round-collaboration-social-github-code-circle-network-icon.svg" width="36" height = "36" alt="GitHub"/>
				</a>
				<a href = "https://www.linkedin.com/in/dev-keshwani-38958a21a/">
					<img src = "http://www.iconninja.com/files/863/607/751/network-linkedin-social-connection-circular-circle-media-icon.svg" width="36" height="36" alt="LinkedIn"/>
				</a>
			</p>
		</td>
	</tr>
</table>

<p align="center">
	Made with ❤ by <a href="https://dscvit.com">GDSC-VIT</a>
</p>
