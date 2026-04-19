# Sokoban 

## Project Description
Academic implementation of the classic Sokoban puzzle game developed as an integrative project for Software Engineering 2, Algorithms and Programming 2, and Discrete Structures 1 courses.

## Team Members
- **Student 1:** Dayanna Fernandez Nuñez - A00403756
- **Student 2:** Daniel Felipe Herrera  - A00413908
- **Student 3:** [Full Name] - [Code]

## Course Information
- **Course:** Algorithms and Programming II (APO2)
- **Semester:** 2026-1
- **Delivery Date:** March 28, 2026

## Project Structure
```
sokoban-project/
├── src/
│   ├── model/         
│   ├── controller/    
│   ├── view/           
│   ├── util/        
│   └── test/          
├── bin/               
├── doc/                
│   ├── test_cases_design.md
│   ├── class_diagram.pdf
│   └── requirements.md
├── lib/        
└── README.md
```

## Features (Planned)
- [x] Test case design
- [ ] Basic player movement (4 directions)
- [ ] Box pushing mechanics
- [ ] Collision detection
- [ ] Victory condition detection
- [ ] Level loading from JSON
- [ ] Undo functionality (Stack)
- [ ] Input buffering (Queue)
- [ ] Transposition table (Hash Table)
- [ ] Leaderboard (Priority Queue)
- [ ] Custom data structures (LinkedList, BST)

## Custom Data Structures Implemented
1. **CustomLinkedList<T>** - Singly linked list for general storage
2. **CustomStack<T>** - Stack for undo functionality
3. **CustomQueue<T>** - Queue for input buffering
4. **CustomPriorityQueue<T>** - Min-heap for leaderboard
5. **TranspositionTable** - Hash table for visited states
6. **BinarySearchTree<T>** - BST for game statistics

## How to Compile and Run
```bash
# Compile all Java files
javac -d bin -sourcepath src src/**/*.java

# Run tests (after implementing JUnit)
java -cp bin:lib/junit.jar org.junit.runner.JUnitCore test.GameTest
```

## Git Workflow
- **main:** Production-ready code, updated after each delivery
- **develop:** Integration branch for features
- **feat/feature-name:** Individual feature branches

## Quality Indicators

### Iteration 1: [commit-sha]
- Density of errors-failures = 
- Reliability = 
- Completeness = 

### Iteration 2: [commit-sha]
- Density of errors-failures = 
- Reliability = 
- Completeness = 

### Iteration 3: [commit-sha]
- Density of errors-failures = 
- Reliability = 
- Completeness = 

### Iteration 4: [commit-sha]
- Density of errors-failures = 
- Reliability = 
- Completeness = 

### Iteration 5: [commit-sha]
- Density of errors-failures = 
- Reliability = 
- Completeness = 

### Iteration 6: [commit-sha]
- Density of errors-failures = 
- Reliability = 
- Completeness = 

### Iteration 7: [commit-sha]
- Density of errors-failures = 
- Reliability = 
- Completeness = 

### Iteration 8: [commit-sha]
- Density of errors-failures = 
- Reliability = 
- Completeness = 

### Iteration 9: [commit-sha]
- Density of errors-failures = 
- Reliability = 
- Completeness = 

### Iteration 10: [commit-sha]
- Density of errors-failures = 
- Reliability = 
- Completeness = 

### Iteration 11: [commit-sha]
- Density of errors-failures = 
- Reliability = 
- Completeness = 

### Iteration 12: [commit-sha]
- Density of errors-failures = 
- Reliability = 
- Completeness = 

### Iteration 13: [commit-sha]
- Density of errors-failures = 
- Reliability = 
- Completeness = 

### Iteration 14: [commit-sha]
- Density of errors-failures = 
- Reliability = 
- Completeness = 

### Iteration 15: [commit-sha]
- Density of errors-failures = 
- Reliability = 
- Completeness = 

## Formulas
- **Density of errors-failures** = total failures / total tests
- **Reliability** = 1 - density of failures
- **Completeness** = test cases / total functionalities

## Technologies Used
- Java 11+
- JavaFX (GUI)
- Gson (JSON parsing)
- JUnit 4 (Testing)

## License
Academic project - Universidad Icesi 2026
