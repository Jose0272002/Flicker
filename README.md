Flicker | Streaming App 📱
A modern Android application built with Kotlin and Jetpack Compose, following the best practices of Clean Architecture and MVVM pattern.

🚀 Technical Stack
Language: Kotlin + Coroutines & Flow for asynchronous operations.

UI: Jetpack Compose (100% Declarative UI).

Architecture: Clean Architecture with 3-layer modularization (Data, Domain, UI).

Dependency Injection: Koin.

Backend: Firebase Firestore for real-time data and authentication.

Image Loading: Coil.

🏗️ Architecture
The project is structured following Clean Architecture principles to ensure scalability, maintainability, and testability:

Presentation Layer (UI): Uses MVVM (Model-View-ViewModel). The ViewModels manage the UI state using StateFlow and handle user interactions.

Domain Layer: Contains the business logic, Use Cases, and Entity models. It is a pure Kotlin layer with no dependencies on the Android framework.

Data Layer: Responsible for data retrieval from Firebase. It implements the Repository pattern to abstract the data sources from the rest of the app.

🛠️ Key Features implemented
Asynchronous Data Flow: Leveraging Flow and SharedFlow to handle real-time updates from Firestore.

Dependency Injection: Using Koin for decoupled components and easier testing.

Reactive UI: Smooth state transitions and animations powered by Jetpack Compose.

Modular Design: Clear separation of concerns between business logic and framework implementation.

📸 Screenshots

| Splash | Register | Login | Home |
| :---: | :---: | :---: | :---: |
| <img src="https://github.com/user-attachments/assets/5446a173-1f13-49c4-8278-4d14af5036c5" width="160"> | <img src="https://github.com/user-attachments/assets/703e8ec1-f3b3-407b-989c-6e7b91d58480" width="160"> | <img src="https://github.com/user-attachments/assets/5b8da72f-b2a4-4f57-b566-eed80bee993c" width="160"> | <img src="https://github.com/user-attachments/assets/951a754e-b113-48e3-a2f7-14bb1500e281" width="160"> |
| **Search** | **Watchlist** | **Details** | **Profile** |
| <img src="https://github.com/user-attachments/assets/42a15895-e8bf-4db6-9f9b-8adf40c2020e" width="160"> | <img src="https://github.com/user-attachments/assets/87e0e0e4-2f7e-42a3-8fc4-289c27524834" width="160"> | <img width="160" alt="image" src="https://github.com/user-attachments/assets/0ccc65df-6028-4f5f-82df-92e89984072b" /> | <img src="https://github.com/user-attachments/assets/6f78e38a-ddbf-4fef-92d5-c480e501ebfe" width="160">
 | |
