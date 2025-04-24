# RexAuth – Unified Biometrics, PIN, and Pattern Lock SDK

**RexAuth** is a modular Android authentication library that simplifies integrating biometric, PIN, and pattern-based authentication into your app. Designed with customization and security in mind, it ensures your users have a seamless and secure experience.

> 🚧 **Work In Progress**: RexAuth is currently under active development. Expect improvements, new features, and better documentation in upcoming releases.

---

## 🧠 Why We Created RexAuth

In modern Android applications, providing flexible and secure authentication options is essential. However, integrating biometric prompts alongside fallback mechanisms like PIN or pattern locks is often fragmented and repetitive across projects.

**RexAuth** was created to solve this by offering:
- A **unified and customizable** authentication flow.
- **Secure local data storage** using modern Android APIs.
- A **modular SDK** approach that fits into any app easily — as a lock screen or a background security layer.

---

## ⚙️ Tech Stack

- **BiometricPrompt** – Native biometric authentication API
- **Jetpack Security** – Encrypted shared preferences for sensitive data
- **Room** – Local database with encryption support
- **Kotlin** – Modern, concise, and expressive Android development

---

## ✨ Key Features

### 🔐 Unified Authentication  
Biometric authentication with smooth fallback to PIN or pattern lock.

### 🎨 Theming & Branding  
Customize colors, logos, and UI components to match your app's theme.

### ⏱️ Auto Lock Logic  
Set lock timeouts for when the app is sent to background or idle.

### 📲 Flexible Integration  
Use it as a full-screen app lock or trigger it only for specific actions.

### 📦 Secure Storage  
All authentication data is stored locally using **Jetpack Security** and **Room**, encrypted and safe.

---

## 📦 Coming Soon

- Fingerprint-only and pattern-only modes  
- Multi-user and session management  
- Advanced theming with XML/Jetpack Compose support  
- API-level documentation & integration guide

---

## 📁 Project Status

**Status**: 🚧 *In Progress*  
Initial prototype completed. Feedback and contributions are welcome as the SDK evolves!

---

## 📬 Contact

Made with ❤️ by [@ilokeshmeena](https://github.com/ilokeshmeena)

---
