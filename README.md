# Horseback Riding Android App

Visit our website: [HorsebackRiding](https://www.josenavio.site)

Welcome to the Horseback Riding Android App, designed to provide a seamless horse renting experience on your mobile device. This app connects via API to the Horseback Riding Web platform to offer the same great features in a mobile-friendly format.

## Features

### Mobile Application

- **User Registration and Login:** Secure user authentication with email verification.
- **Horse Listings:** Browse through a variety of available horses with detailed profiles.
- **Booking System:** Easy and intuitive booking process with real-time availability.

## Getting Started

### Prerequisites

- Android Studio
- Minimum SDK version 28
- Internet connection for API access

### Installation

1. Clone the repository:
    ```sh
    git clone https://github.com/josenavio/HorseBackRidingAPP.git
    ```
2. Open the project in Android Studio.

3. Configure API URL:
    - Set the base URL to your API endpoint,  `https://www.josenavio.site/api`
    - Configure the endpoints.

4. Build and run the project:
    - Connect your Android device or start an emulator
    - Click "Run" in Android Studio

### API Integration

The app communicates with the Horseback Riding Web API for all its operations. Ensure the API is running and accessible at the specified base URL.

### API Endpoints Used

- **Authentication**
  - `POST /login`: User login

- **Horse Management**
  - `GET /get-all-horses`: Retrieve a list of available horses

- **Booking Management**
  - `POST /create-booking`: Create a new booking
  - `PUT /update-booking/{id}`: Modify a booking
  - `GET /get-all-bookings`: View user bookings
  - `DELETE /delete-booking/{id}`: Cancel a booking

### Other information
  - App sent an email and a whatsapp to the user after booking confirmation.

