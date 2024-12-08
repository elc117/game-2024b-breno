# libGDX example: a simple game

A simple game from the libGDX wiki tutorials.

## Links
- Demo: https://libgdx.com/wiki/start/a-simple-game
- Source code: https://github.com/libgdx/libgdx-wiki-examples/tree/master/a-simple-game
  
## Running on GitHub Codespaces

1. Click on 'Code', select 'Codespaces' and click '+' to create a Codespace on master
2. In the terminal of the recently created Codespace:

   1. Update the Java version
   ```
   sdk install java 17.0.8-tem
   sdk default java 17.0.8-tem
   ```
   2. Go to the project folder
   ```
   cd gdx-1.13.0/a-simple-game
   ```
   3. Build the HTML project
   ```
   ./gradlew html:dist
   ```
   4. Run the HTML project
   ```
   cd html/build/dist
   python -m http.server
   ```
