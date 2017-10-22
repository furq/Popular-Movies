Popular Movie App
==================


An Android app, that helps user to discover movies. 
This is STAGE 2 for popular movies  

**Features:**

- List most popular, top rated movies and Favourite movies.
- Show details of selected movies including reviews and trailers
- Landscape and Portrait mode supported
- user save offline list of favourite movies 

Setup
---------------

### Requirements

- Java 8
- Latest version of Android SDK and Android Build Tools

### Libraries

- Retrofit
- Butterknife
- GLide
- Realm db 

### API Key

The app uses themoviedb.org API to get movie information and posters. You must provide your own [API key][1] in order to build the app.

Just put your API key into `build.properties` file:

```API_KEY
API_KEY= "your TMDB api Key"
```
