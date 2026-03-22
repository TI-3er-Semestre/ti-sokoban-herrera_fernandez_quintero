# External Libraries

This folder contains external JAR files required for the project.

## Required Libraries:

### 1. Gson (Google JSON library)
- **Purpose:** Parse JSON files for level loading
- **Download:** https://github.com/google/gson
- **File:** gson-2.10.1.jar

### 2. JUnit 4
- **Purpose:** Unit testing framework
- **Download:** https://junit.org/junit4/
- **Files:** 
  - junit-4.13.2.jar
  - hamcrest-core-1.3.jar

### 3. JavaFX (if needed for GUI)
- **Purpose:** Graphical user interface
- **Download:** https://openjfx.io/
- **Note:** May be included with JDK depending on version

## Installation:
1. Download the required JAR files
2. Place them in this `lib/` folder
3. Add to classpath when compiling/running

## Classpath Example:
```bash
javac -cp lib/gson-2.10.1.jar:lib/junit-4.13.2.jar -d bin -sourcepath src src/**/*.java
```
