# MADT Notes Storage Application (Lab 5)

A professional, feature-rich Android Notes Storage application developed using **Kotlin**, **Jetpack Compose (Material 3)**, and **MVVM Architecture**. The application provides full CRUD (Create, Read, Update, Delete) functionality with runtime storage switching mechanisms between SQLite/Room and JSON file persistence.

## 🚀 Key Features

* **Modern MVVM Architecture:** Clean separation of concerns with a dedicated `NotesViewModel` managing state streams, user inputs, and persistence logic.
* **Full CRUD Operations:**
    * **Create:** Add new notes with real-time input validation (empty-state warnings).
    * **Read:** View saved notes in a responsive list and tap any card to open a detailed dialog view.
    * **Update:** Edit existing notes seamlessly with state preservation.
    * **Delete:** Instantly remove notes from the active session list.
* **Dual-Backend Storage & Sync:** Interactive filter chips to toggle between SQLite and JSON File storage methods, accompanied by a robust sync trigger.
* **Custom UI & UX:** Crafted using Jetpack Compose Material 3 components, custom TopAppBar with overflow options (`⋮`), quick code snippet inserters, and dynamic card layouts.
* **Localization Support:** All UI text strings are fully externalized to `res/values/strings.xml` for seamless multi-language scalability.

## 🛠️ Tech Stack & Libraries

* **Language:** Kotlin
* **UI Framework:** Jetpack Compose & Material 3
* **Icons:** `androidx.compose.material:material-icons-extended`
* **Architecture Components:** ViewModel & Lifecycle Compose (`androidx.lifecycle:viewmodel-compose`)
* **Persistence Concepts:** SQLite / Room Database & JSON file serialization architecture

## ⚙️ Setup & Installation
1. Clone the repository:
   ```bash
   git clone [https://github.com/your-username/notestorage.git](https://github.com/your-username/notestorage.git)