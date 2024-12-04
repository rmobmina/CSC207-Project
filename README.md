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
   - Users can visualize temperature, precipitation, and other climate data over time using line, bar, or Mercator graphs.

2. **Weekly Weather Forecasts**  
   - Provides a summarized 7-day forecast (or customizable forecast up to 16 days) including:  
     - Maximum and minimum temperatures.  
     - Precipitation levels.  
     - Wind speeds.  

3. **City Comparisons Over Range of Time**  
   - Allows users to compare average temperature, precipitation, humidity, and wind speeds between two cities.
   - Helps analyze differences in climates and identify trends.
   - Specify a range of time of your liking.

4. **Interactive Map**  
   - Click on a Mercator map to view the current temperature of any location.  
 
5. **Favorite Locations**  
   - Save favorite locations for quick access to their weather data.
   - Add or remove entries as needed to personalize your experience.

6. **Hourly Weather Forecast and Recommendations**  
   - Get detailed hourly weather predictions for the day, including highs, lows, and major changes.
   - Receive personalized recommendations (e.g., reminders for umbrellas or sunscreen).

---

### **Installation Instructions**

Follow these steps to install and run the Weather Application:

1. **Clone the Repository**  
   Clone this repository from GitHub to your local machine:  
   ```bash
   git clone https://github.com/rmobmina/CSC207-Project.git
   ```

2. **Build the Project**  
   Use Maven to build the project and download dependencies:
   ```bash
   mvn clean install
   ```

3. **Run Tests (with Mockito)**  
   Ensure all tests pass, including those using Mockito. Run the following:
   ```bash
   mvn test
   ```

4. **Launch the Application**  
   Start the application using your preferred IDE or via command line. Head to App.java and run the file. This should work for both Windows and MacOS.

   Enter a valid API key: 2d6d6124e844c3e976842b19833dfa3b

---

### **Common Issues and Solutions**

| Issue                                         | Solution                                                                                          |
|-----------------------------------------------|--------------------------------------------------------------------------------------------------|
| **Maven is not recognized as a command**      | Ensure Maven is added to your system PATH. Follow [Maven Installation Guide](https://maven.apache.org/install.html). |
| **Java version compatibility issues**         | Check if the installed JDK is version 1 or above. Use `java -version` to verify.                |
| **Graph rendering errors**                    | Ensure JFreeChart version matches the required 1.5.3 version.                                    |

---

## **Usage Guide**

### **1. Launch the Application**
   Run the application using the provided setup instructions. Enter given API on console.  

   API Key: 2d6d6124e844c3e976842b19833dfa3b

### **2. Navigate the User Interface (UI)**
   - Access the main menu to select a feature.  
   - Choose from the following options:
     - **Weather Forecast**: View daily or hourly weather forecasts.
     - **Historical Comparison**: Compare weather data between two locations over a range of time.
     - **Mercator Map**: Explore an interactive map with real-time weather data. 

### **3. View Visualizations**
   - **For Weather Forecasts**:
     - Select **daily** or **hourly** forecasts.  
     - Generate a visualization using a **Bar Graph** or **Line Graph**.
     - Make sure to fetch data first.  
   - **For Weather Comparisons**:
     - Enter the names of two locations.  
     - Generate either a **Bar Graph** or **Line Graph** for comparison.
     - Alter the range of time based on your liking.
   - **For Mercator Map**:
     - Interact with the map to explore and visualize weather data.
     - Remember to double click.  

### **4. Interact and Explore**
   - Experiment with the application's features to discover insights.
   - Have fun!

---

## **Visual Examples**

Here is how our Hourly Forecast works...

![Example UI](report/images/Screenshot%202024-12-03%20at%2000.33.00.png)
![Example UI](report/images/Screenshot%202024-12-03%20at%2000.34.37.png)  
![Example UI](report/images/Screenshot%202024-12-03%20at%2000.35.22.png)  

This is the Daily Forecast in action.

![Example UI](report/images/Screenshot%202024-12-03%20at%2000.24.29.png)  
![Example UI](report/images/Screenshot%202024-12-03%20at%2000.25.08.png)  
![Example UI](report/images/Screenshot%202024-12-03%20at%2000.26.08.png)  

And, of course, the favourites feature.

![Example UI](report/images/Screenshot%202024-12-03%20at%2000.41.12.png)
![Example UI](report/images/Screenshot%202024-12-03%20at%2000.41.32.png)

Users aren't restricted to a single city, we can do two!

![Example UI](report/images/Screenshot%202024-12-03%20at%2000.50.10.png)
![Example UI](report/images/Screenshot%202024-12-03%20at%2000.51.56.png)
![Example UI](report/images/Screenshot%202024-12-03%20at%2000.54.19.png)

The best for last.

![Example UI](report/images/Screenshot%202024-12-03%20at%2006.45.36.png)
![Example UI](report/images/Screenshot%202024-12-03%20at%2006.45.55.png)

---

## **License**

This project does not have an official license.
Unless explicitly stated, the code is intended for educational and non-commercial purposes only.

If you wish to use this project for other purposes, please contact the authors for permission.

---

## **Feedback**

We welcome your feedback to improve this project. Here's how you can provide feedback:

**Email Us**: Email the repository owner Reena, rmobmina@gmail.com. Expect to see a reply within 5 business days.  

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
