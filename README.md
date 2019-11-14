# newsapp  
Features:  
 * Beautiful UI 
 * Fetches News from many sources 
 * Search Article
 * SwipeRefreshLayout
 * Clear cache on app exit

Modules:  
  * API:  
    * API_Client - SSL Socket connection
    * API_Interface - Fetches data in accordance with the keywords  
  * Models (Getters & Setters):  
    * Article
    * Source
    * News
  * Adapter - Generates the preview card for all the News Articles in the Home page.
  * Utis - Obtains the Data and the location of the current mobile device. Produces random colors incase the image is unavailable.
  * NewDetalActivity - Generates the web view of the full news article containing the full, extended news and images associated.
  * MainActivity - Loads the JSON , Contains the APIkey and all the functinoalities associated with the application.
  
 Libraries used:
  * NewsAPI 
  * Glide for image network
  * Prettytime for Convert  JAVA DATE();
