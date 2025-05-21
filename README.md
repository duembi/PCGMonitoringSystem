# Phonocardiogram Monitoring System

This is a Java-based GUI application that reads heart sound data (from `.wav` or `.csv`), processes the signal, and visualizes it in real-time.

## 🎯 Features

- 📂 Load pre-recorded PCG files (WAV or CSV)
- 📊 Apply signal smoothing (moving average)
- 📈 Analyze peaks to estimate heart rate (BPM)
- 📡 Live audio monitoring from microphone input
- 📉 Real-time waveform plotting with JFreeChart
- 💾 Export smoothed signal and analysis summary

## 📂 Project Structure

  src/
  └── pcgmonitor/
  ├── controller/
  ├── model/
  ├── utils/
  ├── view/
  └── Main.java
  
  lib/
  ├── jfreechart-1.0.19.jar
  └── jcommon-1.0.23.jar

## ⚙️ How to Run

1. Clone this repo or download the zip
2. Open the project in Eclipse or any Java IDE
3. Add all JAR files in `lib/` to your Build Path
4. Run `Main.java`
5. Use the buttons to load or record and analyze heart sound signals

## 🧱 Technologies Used

- Java Swing (GUI)
- Java Sound API (live microphone input)
- JFreeChart (signal plotting)
- Functional style: `map`, `filter`, `collect` logic for signal processing
