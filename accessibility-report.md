# Accessibility Report

## 1. Principles of Universal Design

1. **Equitable Use**
   - Our program allows users to input any city name to fetch weather data, ensuring accessibility for individuals from diverse geographic regions.
   - Features like dropdowns (in `DropDownUI.java`) make it easy for users with limited typing ability to select options, promoting inclusivity.

2. **Flexibility in Use**
   - Users can select single or multiple cities for comparison, as shown in `TwoCitiesWeatherUI.java` and `MultipleLocationsWindow.java`. This flexibility accommodates different user preferences.
   - Future improvements could include voice command integration to cater to users with mobility impairments.

3. **Simple and Intuitive Use**
   - The interface is straightforward, with clearly labeled buttons like "Get Info," "Refresh," and "Visualize" (`DashboardUI.java`). This reduces cognitive load for all users.
   - Future enhancements could include tooltips for additional guidance on complex features, such as graph selection.

4. **Perceptible Information**
   - Important weather data is presented using text labels and graphs, ensuring clarity (`DashboardUI.java` and `GraphSelectionWindow.java`).
   - Improvements might include adding auditory cues or screen reader support to assist visually impaired users.

5. **Tolerance for Error**
   - The program provides error messages, such as when invalid dates or locations are entered (`DashboardUI.java`). This prevents misuse and helps users recover from mistakes.
   - Future enhancements could offer auto-correction suggestions, e.g., for mistyped city names.

6. **Low Physical Effort**
   - The program’s reliance on simple clicks and minimal typing reduces physical effort, as seen in `DropDownUI.java`.
   - Enhancing support for keyboard shortcuts could further improve ease of use for users with limited mobility.

7. **Size and Space for Approach and Use**
   - Buttons and inputs are designed with adequate spacing (`DashboardUI.java` and `GraphSelectionWindow.java`), which benefits users with limited fine motor skills.
   - We could ensure compliance with accessibility standards for button size and spacing in future updates.

## 2. Target Audience

Our program is best marketed toward:
- **Target Audience:** Weather enthusiasts, students, educators, and professionals requiring weather data for research or planning purposes.
- **Details:** The program provides accurate weather data for single or multiple locations, along with visualization options, making it ideal for classroom demonstrations, travel planners, or environmental analysts.

## 3. Demographic Accessibility Concerns

Certain demographics may face challenges using our program:
- **Barriers:** Users with visual impairments or limited internet access might find the program less accessible. The graphical interface and reliance on an internet-based API could pose challenges.
- **Solutions:** Incorporating screen reader compatibility, offline functionality, and localized versions would mitigate these issues.
- **Informed by E3I Modules:** Our reflection highlights the need to design inclusively, avoiding inadvertent exclusion of marginalized groups by addressing accessibility gaps proactively.
