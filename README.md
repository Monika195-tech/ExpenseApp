# Expense Tracker with Monthly Analytics
A desktop-based Java application built using *JavaFX* and *SQLite* that helps users manage and analyze their daily expenses. It offers a modern interface, monthly filtering, category-wise pie chart visualization, CSV export, and delete functionality.

## Table of Contents
- Overview
  > Brief intro of the project and its purpose.

- Key Features 
  > Main functionalities like adding, viewing, filtering, and exporting expenses.

- Technologies Used 
  > Tools, frameworks, and languages used to build the project.

- How to Run 
  > Step-by-step guide to set up and run the project.

- Project Structure
  > Folder and file layout for understanding code organization.

- Screenshots  
  > Visuals of the app (form, table, pie chart).

- Future Enhancements 
  > Features to add in future updates.
   
##  Overview
This project helps users log and analyze daily expenses. It provides a user-friendly interface with options to add, view, delete, and export expenses. Category-based visual insights are shown using pie charts, and monthly filtering helps track budget trends.

## Key Features
- Add expenses (amount, category, description, date)  
- Filter by selected month  
- Pie chart for category-wise breakdown  
- Delete expense entries  
- Export to CSV  
- Local SQLite database for data storage

## Technologies Used
- Java 24  
- JavaFX  
- SQLite  
- JDBC  
- Eclipse IDE  
  
## How to Run
```bash
1. Clone this repo
2. Open in Eclipse or IntelliJ
3. Add JavaFX SDK to module path
4. Run ExpenseApp.java

## Project Structure
├── ExpenseApp.java           # Main JavaFX app
├── Expense.java              # Model class
├── ExpenseDBHelper.java      # Database operations
├── expenses.db               # SQLite DB file
├── data.csv                  # Exported expense data

## Future Enhancements
* Login & user-specific expense tracking
* Cloud-based sync and storage
* Spending limit alerts and notification
* Graphs showing trends over time

