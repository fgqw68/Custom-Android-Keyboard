# Custom-Android-Keyboard (Android IME)
A robust, tab-based Android Input Method Editor (IME) designed with a **ViewFlipper** architecture for high-performance typing.

## 🚀 Overview
This project is a custom Android keyboard that allows users to switch between three distinct input modes (Alpha, Numeric, and Function) within a single service. It uses a centralized logic system to handle hundreds of keys with minimal code.

### 🌟 Key Features
* **ViewFlipper Navigation**: Smoothly transition between layouts without reloading the service.
* **English QWERTY Layout**: Optimized for standard English character input.
* **Automated Key Mapping**: Uses a recursive algorithm to find all buttons and assign listeners automatically.
* **Tag-Based Logic**: Uses XML `android:tag` to distinguish between character input and system commands (like Backspace or Enter).

---

## 📂 Project Structure

| File Path | Description |
| :--- | :--- |
| `CustomKeyboardService.java` | The main logic engine (InputMethodService). |
| `res/layout/keyboard_main.xml` | The shell containing the **ViewFlipper** and Tab buttons. |
| `res/layout/layout_alpha.xml` | Standard English alphabetic keys. |
| `res/layout/layout_numeric.xml` | Numbers and special symbols. |
| `res/layout/layout_function.xml` | Function keys (F1-F12) and system controls. |
| `res/xml/method.xml` | Metadata that registers the keyboard with Android OS. |

---

## 🛠 Technical Implementation

### The Centralized Key Listener
Instead of 100+ `onClick` listeners, we use a single **Universal Listener**. By inspecting the `tag` of the clicked button, the keyboard decides whether to type text or perform a system action:

```java
// Example of the logic used:
if (tag != null && tag.startsWith("keycode:")) {
    int keyCode = Integer.parseInt(tag.substring(8));
    handleSystemKey(keyCode); // Handles Backspace/Enter
} else {
    handleChar(text); // Types the letter on the button
}
```Developed By: Sabir VT