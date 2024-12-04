---

# **The Weather App**

This project allows users to explore and analyze climate trends, compare weather data between locations, and receive real-time updates on weather conditions. It leverages weather and climate APIs to provide interactive and insightful visualizations, making it easier to understand and plan for changing weather patterns.

---

## **Authors and Contributors**

- **Akram Klai** - [AkramKlai](https://github.com/AkramKlai)  
- **Reena Obmina** - [rmobmina](https://github.com/rmobmina)  
- **Edison Yao** - [DojimaRyu](https://github.com/DojimaRyu)  
- **Bader Alseiari** - [bader11678](https://github.com/bader11678)  
- **Arham Mohammed** - [arham-w17](https://github.com/arham-w17)  

---

## **Summary of the Project’s Purpose**

**The Weather App** was developed to simplify the understanding of climate and weather data for users. The project provides tools to analyze historical and real-time weather trends, compare data between locations, and plan daily or long-term activities based on accurate forecasts.

### **Why This Project Was Made**
- Weather data is often presented in raw formats that are hard to interpret for general users. This project makes the data accessible and visually engaging.
- The project addresses the need for a tool that allows both casual users and researchers to visualize, analyze, and interact with weather trends.

### **Problem Solved**
Understanding climate data can be overwhelming due to its complexity. This project solves this by presenting intuitive visualizations and summaries, making it easier for users to explore weather patterns and trends over time.

---

## **Table of Contents**

1. [Features of the Software](#features-of-the-software)  
2. [Installation Instructions](#installation-instructions)  
3. [Usage Guide](#usage-guide)  
4. [License](#license)  
5. [Feedback](#feedback)  
6. [Contributions](#contributions)  
7. [Visual Examples](#visual-examples)  

---

## **Features of the Software**

1. **Interactive Graphs for Climate Trends**  
   - Users can visualize temperature, precipitation, and other climate data over time using line, bar, or heatmap graphs.
   - This feature enables users to track long-term weather patterns.

2. **Weekly Weather Forecasts**  
   - Provides a summarized 7-day forecast (or customizable forecast up to 16 days) including:  
     - Maximum and minimum temperatures.  
     - Precipitation likelihood.  
     - Wind speeds.  

3. **City Comparisons**  
   - Allows users to compare average temperature, precipitation, humidity, and wind speeds between two cities.
   - Helps analyze differences in climates and identify trends.

4. **Interactive Map with Historical Data**  
   - Click on a map to view the current temperature of any location.  
   - Compare weather conditions (e.g., temperature, precipitation) between two locations.  
   - Access historical weather data to visualize trends over time.  

5. **Favorite Locations**  
   - Save favorite locations for quick access to their weather data.
   - Add or remove entries as needed to personalize your experience.

6. **Hourly Weather Forecast and Recommendations**  
   - Get detailed hourly weather predictions for the day, including highs, lows, and major changes.
   - Receive personalized recommendations (e.g., reminders for umbrellas or sunscreen).

---

## **Installation Instructions**

### **Prerequisites**

Before installing the project, ensure your system meets the following requirements:

1. **Java Development Kit (JDK)**  
   - Required version: JDK 17 or above  
   - Download link: [https://www.oracle.com/java/technologies/javase-downloads.html](https://www.oracle.com/java/technologies/javase-downloads.html)

2. **Apache Maven** (for dependency management and building the project)  
   - Required version: 3.8 or above  
   - Download link: [https://maven.apache.org/download.cgi](https://maven.apache.org/download.cgi)

3. **JFreeChart Library**  
   - Required version: 1.5.3  
   - This library is automatically downloaded via Maven. Documentation: [JFreeChart Documentation](http://www.jfree.org/jfreechart/)

4. **Operating System**  
   - Supported platforms: Windows, macOS, Linux  
   - Recommended: Any OS that supports Java  

5. **IDE** (Optional, but recommended for development)  
   - IntelliJ IDEA: [https://www.jetbrains.com/idea/download/](https://www.jetbrains.com/idea/download/)
     
---

### **Setup Guide**

Follow these steps to install and run the Weather Application:

1. **Clone the Repository**  
   Clone this repository from GitHub to your local machine:  
   ```bash
   git clone https://github.com/rmobmina/CSC207-Project.git
   ```

2. **Navigate to the Project Directory**  

3. **Build the Project**  
   Use Maven to download dependencies and compile the project:  
   ```bash
   mvn clean install
   ```

4. **Run the Application**  
   After successful compilation, you can run the application by heading to the App.java file in infrastructure.frameworks.

---

### **Common Issues and Solutions**

| Issue                                         | Solution                                                                                          |
|-----------------------------------------------|--------------------------------------------------------------------------------------------------|
| **Maven is not recognized as a command**      | Ensure Maven is added to your system PATH. Follow [Maven Installation Guide](https://maven.apache.org/install.html). |
| **Java version compatibility issues**         | Check if the installed JDK is version 17 or above. Use `java -version` to verify.                |
| **Dependencies not downloading**              | Delete the `.m2` directory and re-run `mvn clean install` to force dependency re-download.        |
| **Graph rendering errors**                    | Ensure JFreeChart version matches the required 1.5.3 version.                                    |

For more troubleshooting, refer to the [FAQ.md](FAQ.md) in the project root directory.

---

## **Usage Guide**

### **1. Launch the Application**
   Run the application using the provided setup instructions.  

### **2. Navigate the User Interface (UI)**
   - Access the main menu to select a weather dataset or feature.  
   - Choose from the following options:
     - **Weather Forecast**: View daily or hourly weather forecasts.
     - **Weather Comparison**: Compare weather data between two locations.
     - **Mercator Map**: Explore an interactive map with real-time weather data.  
   - Customize the time range or data parameters based on your needs.  

### **3. View Visualizations**
   - **For Weather Forecasts**:
     - Select **daily** or **hourly** forecasts.  
     - Generate a visualization using a **Bar Graph** or **Line Graph**.  
   - **For Weather Comparisons**:
     - Enter the names of two locations.  
     - Generate either a **Bar Graph** or **Line Graph** for comparison.  
   - **For Mercator Map**:
     - Interact with the map to explore and visualize weather data.  

### **4. Interact and Explore**
   - Experiment with the application's features to discover insights.  
   - Save visualizations (if applicable) as images for later use.  

---

## **Visual Examples**

Here is how our Hourly Forecast works...
![Example UI](Screenshot_2024-12-03_00-33-00.png)
![Example UI](Screenshot%202024-12-03%20at%2000.34.37.png)  
![Example UI](Screenshot%202024-12-03%20at%2000.35.22.png)  

This is the Daily Forecast in action.
![Example UI](Screenshot%202024-12-03%20at%2000.24.29.png)  
![Example UI](Screenshot%202024-12-03%20at%2000.25.08.png)  
![Example UI](Screenshot%202024-12-03%20at%2000.26.08.png)  

And, of course, the favourites feature.
Favourites
![Example UI](Screenshot%202024-12-03%20at%2000.41.12.png)
![Example UI](Screenshot%202024-12-03%20at%2000.41.32.png)

Users aren't restricted to a single city, we can do two!
![Example UI](Screenshot%202024-12-03%20at%2000.50.10.png)
![Example UI](Screenshot%202024-12-03%20at%2000.51.56.png)
![Example UI](Screenshot%202024-12-03%20at%2000.54.19.png)

The best for last.
![Example UI](Screenshot%202024-12-03%20at%2006.45.36.png)
![Example UI](Screenshot%202024-12-03%20at%2006.45.55.png)

---

## **License**

This project does not have an official license.
Unless explicitly stated, the code is intended for educational and non-commercial purposes only.

If you wish to use this project for other purposes, please contact the authors for permission.

---

## **Feedback**

We welcome your feedback to improve this project. Here's how you can provide feedback:

**Email Us!**: Email the repository owner Reena, rmobmina@gmail.com.  

---

## **Contributions**

We welcome contributions from the community! Here's how you can contribute:

1. **Fork the Project**  
   Click the "Fork" button on GitHub and clone the repository locally.

2. **Create a Feature Branch**  
   ```bash
   git checkout -b feature/new-feature
   ```

3. **Commit Your Changes**  
   ```bash
   git commit -m "Add a new feature"
   ```

4. **Push to Your Branch**  
   ```bash
   git push origin feature/new-feature
   ```

5. **Submit a Pull Request**  
   Open a pull request in the original repository and describe the changes.

---
