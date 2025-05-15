# ExoPlayerPractice

An Android app built with Jetpack Compose and ExoPlayer for streaming and downloading video content from the [Pexels API](https://www.pexels.com/api/).

---

## ✨ Features

- 🔁 **Infinite video list** fetched from Pexels using Paging 3  
- ▶️ **Video playback** using ExoPlayer with Compose integration  
- 💾 **Download support** for offline viewing  
- 📂 **Downloaded video management** screen  
- 🔄 **Screen rotation recovery**: Playback state **and scroll position** persist across configuration changes  
- 📥 **Interactive video list**: ExoPlayer is embedded in each item for preview & control  
- 🗃️ **File deletion & re-download** reflected in real-time on UI  
- 📡 Integration with **Pexels REST API**

---

## 🧠 Technical Highlights

- **Player State Persistence**  
  ExoPlayer’s playback position is preserved across screen rotation and background/foreground transitions using `ViewModel` and `SavedStateHandle`. This avoids restarting playback.

- **List Scroll State Retention**  
  The video list (`LazyColumn`) preserves its scroll position using `rememberLazyListState()` and `rememberSaveable`. Users won’t lose position after screen rotation.

- **File Observation & UI Sync**  
  `FileObserverManager` monitors the download directory. When a file is added or deleted, the UI updates instantly without manual refresh.

- **Interactive Thumbnails**  
  Video previews are fully playable in the list. Users can seek or interact directly with embedded players in list items.

---

## 📁 Modules Overview

| Layer                   | Files / Components                                         |
|------------------------|------------------------------------------------------------|
| `MainActivity.kt`      | App entry point hosting the Compose Navigation host        |
| `AppNavHost.kt`        | Navigation logic for video list, player, and downloaded UI |
| `VideoListScreen.kt`   | Fetch and show paginated video list from Pexels API        |
| `VideoPlayerScreen.kt` | Video playback using `ExoPlayerManager`                    |
| `DownloadedVideosScreen.kt` | Lists locally cached videos                         |
| `ExoPlayerManager.kt`  | Custom wrapper for managing ExoPlayer lifecycle            |
| `DownloadHelper.kt`    | Helper to download and save MP4 content                    |
| `PexelsApiService.kt`  | Retrofit interface to call Pexels API                      |

---

## 🎥 Demo Videos


<table>
  <tr>
    <td align="center">
      <strong>▶️ Play</strong><br/>
      <video src="https://github.com/user-attachments/assets/8be55a09-507a-47f3-b90a-20304e5b6b2a" controls width="200"></video>
    </td>
    <td align="center">
      <strong>💾 Download</strong><br/>
      <video src="https://github.com/user-attachments/assets/19efd43b-12f7-4ced-a893-7b46a768236b" controls width="200"></video>
    </td>
    <td align="center">
      <strong>🗑️ Delete</strong><br/>
      <video src="https://github.com/user-attachments/assets/0d9e9a77-52c7-4cde-ae8b-c7268c54793d" controls width="200"></video>
    </td>
  </tr>
  <tr>
    <td align="center">
      <strong>🔄 Rotate List</strong><br/>
      <video src="https://github.com/user-attachments/assets/ee590be7-c40b-43a7-9688-e14cdf71076d" controls width="200"></video>
    </td>
    <td align="center">
      <strong>🔁 Rotate Video</strong><br/>
      <video src="https://github.com/user-attachments/assets/b850f4ed-0f44-4476-acb8-bf10d1842217" controls width="200"></video>
    </td>
    <td align="center">
      <strong>🔁 Download Retry</strong><br/>
      <video src="https://github.com/user-attachments/assets/4ab2ab4e-f49d-464d-abf9-a2c4fe3a086a" controls width="200"></video>
    </td>
  </tr>
  <tr>
    <td align="center" colspan="3">
      <strong>▶️ Play Video Offline</strong><br/>
      <video src="https://github.com/user-attachments/assets/f1c4e200-0786-4157-8d4e-63919cf69aee" controls width="200"></video>
    </td>
  </tr>
</table>
