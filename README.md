# Sokoban 

## Project Description
Academic implementation of the classic Sokoban puzzle game developed as an integrative project for Software Engineering 2, Algorithms and Programming 2, and Discrete Structures 1 courses.

## Team Members
- **Student 1:** Dayanna Fernandez Nuñez - A00403756
- **Student 2:** Daniel Felipe Herrera  - A00413908
- **Student 3:** Andres David Quintero - A00401819

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
│   ├── structure/        
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

<img width="937" height="428" alt="image" src="https://github.com/user-attachments/assets/c0ee1313-db21-43fe-bfd6-cf30b40d74bd" />

### Iteración 1 — 0509690
- Tests totales: 98 | Fallos: 0
- Densidad de errores = 0/98 = 0.00
- Confiabilidad = 1 - 0.00 = 1.00
- Completitud = 98/14 = 1.00

<img width="1820" height="535" alt="image" src="https://github.com/user-attachments/assets/443ea052-1154-4323-8316-4e3a0324e02d" />

### Iteración 2 — 2f29322
- Tests totales: 98 | Fallos: 0
- Densidad de errores = 0/98 = 0.00
- Confiabilidad = 1 - 0.00 = 1.00
- Completitud = 98/14 = 1.00

<img width="1865" height="318" alt="image" src="https://github.com/user-attachments/assets/aad75790-37da-48ec-8263-d701ef839d79" />

### Iteración 3 — f959eb0
- Tests totales: 98 | Fallos: 0
- Densidad de errores = 0/98 = 0.00
- Confiabilidad = 1 - 0.00 = 1.00
- Completitud = 98/14 = 1.00

<img width="1900" height="590" alt="image" src="https://github.com/user-attachments/assets/f756df2f-72b5-4aaa-9463-04b14946943c" />

### Iteración 4 — feat(ui): pantalla de introducción con video y press any key
- Tests totales: 98 | Fallos: 0
- Densidad de errores = 0/98 = 0.00
- Confiabilidad = 1 - 0.00 = 1.00
- Completitud = 98/14 = 1.00

### Iteration 5: [commit-sha]
- Density of errors-failures = 
- Reliability = 
- Completeness = 

### Iteration 6: 222bbf6
- Density of errors-failures = 
- Reliability = 
- Completeness = 

### Iteration 7: ec68863
- Density of errors-failures = 
- Reliability = 
- Completeness = 

### Iteration 8: ec3d4fe
- Density of errors-failures = 
- Reliability = 
- Completeness = 

### Iteration 9: 690a543
- Tests totales: 101 | Fallos: 1
- Densidad de errores = 1/101 = 0.01
- Confiabilidad = 1 - 0.01 = 0.99
- Completitud = 101/14 = 7.21

<img width="1837" height="722" alt="image" src="https://github.com/user-attachments/assets/53902493-4378-4b39-8ff1-c7b2c2c181ea" />

### Iteration 10: afbf4eb
- Tests totales: 105 | Fallos: 0
- Densidad de errores = 0/105 = 0.00
- Confiabilidad = 1 - 0.01 = 1.00
- Completitud = 105/14 = 7.50

<img width="1817" height="863" alt="image" src="https://github.com/user-attachments/assets/d242995a-2fa8-4817-9e23-c7955b7b6368" />

### Iteration 11 — a32e338 (2026-05-24)
Creación de la interfaz `IGrafo` y la clase `Vertice` — cimiento de toda la estructura del grafo.

- Tests totales: 71 | Fallos: 0
- Densidad de errores = 0/71 = 0.00
- Confiabilidad = 1 − 0.00 = 1.00
- Completitud = 71/14 = 5.07

### Iteration 12 — 9407044 (2026-05-24)
Implementación completa de DFS en `GrafoMatriz` con sistema de colores y timestamps.

- Tests totales: 71 | Fallos: 0
- Densidad de errores = 0/71 = 0.00
- Confiabilidad = 1 − 0.00 = 1.00
- Completitud = 71/14 = 5.07

### Iteration 13 — 6af43d9 (2026-05-31)
Implementación de BFS en `GrafoMatriz` con `CustomQueue`, colores y distancias.
<img width="957" height="551" alt="image" src="https://github.com/user-attachments/assets/8678c516-78c7-48f9-906c-afc15894723c" />


- Tests totales: 79 | Fallos: 0
- Densidad de errores = 0/79 = 0.00
- Confiabilidad = 1 − 0.00 = 1.00
- Completitud = 79/14 = 5.64

### Iteration 14 — 66cacc3 (2026-06-01)
Creación de `GrafoLista` con listas de adyacencia implementando BFS, DFS, Floyd-Warshall, Prim y Kruskal.
<img width="887" height="387" alt="image" src="https://github.com/user-attachments/assets/027fa5c6-7156-42ee-8c86-b788356bcaee" />


- Tests totales: 87 | Fallos: 0
- Densidad de errores = 0/87 = 0.00
- Confiabilidad = 1 − 0.00 = 1.00
- Completitud = 87/14 = 6.21

### Iteration 15 — 820fc23 (2026-06-04)
Creación de `MstSolver` para resolver el árbol de expansión mínima de metas en ambas representaciones.
<img width="890" height="302" alt="image" src="https://github.com/user-attachments/assets/bac9fab4-50e9-4767-8dd2-c6a534336765" />


- Tests totales: 98 | Fallos: 0
- Densidad de errores = 0/98 = 0.00
- Confiabilidad = 1 − 0.00 = 1.00
- Completitud = 98/14 = 7.00

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
