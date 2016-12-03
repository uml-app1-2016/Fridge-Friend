# Fridge Friend Final Project Report
Android Development I group project by John Merrill, Samuel Chambers, and Alex Nevers. 

## Project goal: describes the purpose of this project 
Our app's purpose is to help userse organize and easily visualize their groceries, by using a UPC supplied database. Users will be able to barcode scan their groceries as they unpack them, adding them to their current list with a single button press. Tags such as expiration date and food type are automatically added, so users can easily sort their grocery list by what to eat (or throw away) next.

## Project features: abstract from the user stories 
* Scan barcodes from camera
* Fetch barcode UPC info using COmputer Vision API
* Store UPC Items in a database
* Sort UPC Items by food category or expiration date

## Project design: visual diagram of project components/flow 

https://docs.google.com/document/d/1EJzs_gWDXxbWVhhIF-01Sp4CI4fw13aB3sjQcY1dCWs/edit?usp=sharing

## File structure: what does each file do and who wrote that file
* UI Files: [Created by Sam, tweaks by Alex]
 * Activity_Main: homepage with photo button
 * Activity_Results: Results screen after barcode scan; shows product name and info, with Add button
 * content_main: 
 * drawer_list_item: 
 * list_item: individual xml for each list item
 
* Java Files:
 * DBHelper: [Alex] Creates the SQLite Database for storing food items by UPC type including expiry date, type of food, name, etc.
 * ItemType: [Sam & John] associates common words in UPC item name with appropriate food category, defines item typing
 * MainActivity: [Sam, John, Alex] Displays the homepage with current items in database, along with button to add new via photo
 * NetworkUtilis: [John] Handles network operations of looking up barcodes
 * Results_Activity: [Sam & Alex] Looks up barcode scan and retrieves UPC Item with description & photo, adds to the database
 * UpcItem: [John] Defines a UPC Item including name, item type, and shelf life, to be added to database
 * UpcItemLoader: [John] Asyncronously loads UPCItem in background

## Screenshots/Demo:
![Alt text](/screenshot/MainActivity.png?raw=true "MainActivity")
![Alt text](/screenshot/ResultActivity.png?raw=true "ResultsActivity")

## Class Assignment:
### John:
- NetworkUtilis
- UPCItem
- UPCItemLoader
- ItemType

### Alex:
- DBHelper
- ResultsActivity

### Sam:
- Item Type 
- ResultsActivity
- UI Layouts

### Member Tasks
###### John
- Github management and weekly build 
- Use the Google Barcode API to fetch and parse barcode information 
  (https://developers.google.com/vision/barcodes-overview)

###### Alex
- Set up a SQL database to store and access scanned items 
- Create an API structure (directory organization, class hierarchies, etc.)

###### Sam
- Design a user interface
- Set up interaction with the camera app to retrieve barcode images
