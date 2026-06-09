[33m690a543[m[33m ([m[1;36mHEAD[m[33m -> [m[1;32mfeature/guardar-partida[m[33m, [m[1;31morigin/feature/guardar-partida[m[33m)[m Selector de algoritmo BFS o DFS y Guardar Partida (Serializable)
[33m2346cef[m[33m ([m[1;32mdevelop[m[33m)[m Merge pull request #51 from TI-3er-Semestre/feature_grafo_complejidad
[33mc564be2[m[33m ([m[1;31morigin/feature_grafo_complejidad[m[33m)[m Documenté el análisis de complejidad de BFS, DFS, Floyd-Warshall, Prim y Kruskal en GrafoMatriz vs GrafoLista
[33mc7dc7ea[m Merge pull request #50 from TI-3er-Semestre/feature_grafo_informe
[33maaa40a8[m[33m ([m[1;31morigin/feature_grafo_informe[m[33m)[m Agregué engineering_method_graphs_EN.docx con las 6 secciones del método aplicadas a la elección de BFS para Sokoban
[33m2f29322[m Merge pull request #49 from TI-3er-Semestre/feature/sprites-jugador
[33m25b4f81[m Merge pull request #48 from TI-3er-Semestre/feature/visual-manual
[33m0509690[m Merge pull request #47 from TI-3er-Semestre/feature/visual-transicion-niveles
[33m9d40944[m Merge pull request #46 from TI-3er-Semestre/feature/visual-gameover
[33mb05491e[m Merge pull request #45 from TI-3er-Semestre/feature/visual-ranking-fix
[33ma87c1b6[m Merge pull request #44 from TI-3er-Semestre/feature/visual-victoria-ranking
[33m781dbd2[m Merge pull request #40 from TI-3er-Semestre/feature/visual-registro-jugador
[33ma5ae3d7[m Merge pull request #38 from TI-3er-Semestre/feature_grafo_problemas
[33mf3561ab[m[33m ([m[1;31morigin/feature_grafo_problemas[m[33m)[m Agregué MstSolverTest (6 tests) y problemas_grafos.md con la justificación del Problema 1 y modelado del Problema 2
[33m3409717[m[33m ([m[1;31morigin/feature/sprites-jugador[m[33m)[m feat(ui): agregar sprites direccionales del jugador
[33m820fc23[m Creé MstSolver para resolver el Problema 2 (árbol de expansión mínima de metas) en ambas representaciones con Prim y Kruskal
[33m6dabf61[m Merge pull request #36 from TI-3er-Semestre/feature_grafo_TAD
[33m846e1b1[m[33m ([m[1;31morigin/feature/visual-manual[m[33m)[m feat(ui): implementar pantalla de manual del juego
[33m09f52f4[m[33m ([m[1;31morigin/feature_grafo_TAD[m[33m)[m Agregué TAD_grafo.md con la documentación del TAD, operaciones, los 5 algoritmos y análisis de complejidad en ambas representaciones
[33md3b44aa[m[33m ([m[1;31morigin/feature/visual-transicion-niveles[m[33m)[m feat(ui): transición entre niveles, game over por tiempo y deadlock
[33m079e001[m Merge branch 'develop' of https://github.com/TI-3er-Semestre/ti-sokoban-herrera_fernandez_quintero into feature/visual-gameover
[33mc6680c4[m[33m ([m[1;31morigin/feature/visual-gameover[m[33m)[m correcion nivel 3 de caja imposiblek
[33m267ad85[m feat(ui): implementar pantalla game over por tiempo agotado y deadlock
[33md8b8df4[m[33m ([m[1;31morigin/feature/visual-ranking-fix[m[33m)[m feat(ui): integrar selección de niveles JSON, victoria y ranking completo
[33m0ebecaf[m[33m ([m[1;31morigin/feature/visual-victoria-ranking[m[33m)[m feat(ui): implementar pantalla de victoria y registrar partida en ranking
[33md60996b[m[33m ([m[1;31morigin/feature/visual-registro-jugador[m[33m)[m feat(ui): implementar pantalla de registro de jugador con validaciones
[33m0e950a6[m Merge pull request #35 from TI-3er-Semestre/feature_grafo_lista
[33m0071cb8[m[33m ([m[1;31morigin/feature_grafo_lista[m[33m)[m Agregué tests de GrafoLista (8) y método costoAGM() para calcular el peso total del árbol de expansión mínima
[33m9485e35[m Agregué kruskal() a IGrafo y lo implementé en GrafoLista, con @Override en GrafoMatriz
[33m66cacc3[m Creé GrafoLista con listas de adyacencia que implementa BFS, DFS, Floyd-Warshall, Prim y Kruskal
[33m6102286[m Merge pull request #34 from TI-3er-Semestre/feature_grafo_bfs
[33m5f38a50[m[33m ([m[1;31morigin/feature_grafo_bfs[m[33m)[m Agregué el método reconstruirCaminoBFS para obtener el camino más corto entre dos vértices
[33m0bbc26a[m Agregué 4 tests para verificar el algoritmo BFS y el método getVertices() en GrafoMatriz
[33m6af43d9[m Implementé la lógica de bfs() en GrafoMatriz con CustomQueue, colores blanco/gris/negro y distancias
[33mad0fa7c[m Merge remote-tracking branch 'origin/develop' into feature_grafo_bfs
[33m139eba3[m Corregí el package incorrecto de CustomLinkedList
[33meaa6c1c[m Merge pull request #33 from TI-3er-Semestre/feature_grafo_prim
[33mec68863[m feat(graph): implement Kruskal MST with Union-Find
[33mad4426c[m[33m ([m[1;31morigin/feature_grafo_prim[m[33m)[m Agregué tests de Prim: caso normal, peso mínimo y casos borde de 1 y 2 vértices
[33m8b5e21c[m Implementé la lógica de prim() en GrafoMatriz para calcular el árbol de expansión mínima
[33m9746dbb[m Declaré prim() en IGrafo y generé el método vacío en GrafoMatriz
[33m8bae817[m level changes
[33m7a948bd[m LinkedList
[33md111118[m Fix errores
[33mf6e54fa[m Merge pull request #30 from TI-3er-Semestre/feat_FloydWarshall
[33m0ecbd80[m[33m ([m[1;31morigin/feat_FloydWarshall[m[33m)[m Merge branch 'develop' into feat_FloydWarshall
[33m28ebb3a[m Merge pull request #29 from TI-3er-Semestre/feature_grafo_base
[33mec3d4fe[m Implemento FloydWarshall y SokobanAutoSolver
[33ma77b634[m Implemento FloydWarshall y SokobanAutoSolver
[33m9407044[m[33m ([m[1;31morigin/feature_grafo_base[m[33m)[m Implementé DFS en GrafoMatriz y agregué los 6 tests de GrafoMatrizTest
[33m000989f[m Implementé los métodos de consulta y operación de GrafoMatriz (agregarArista, existeArista, obtenerVecinos)
[33mc526723[m Creé la clase GrafoMatriz con la estructura base de matriz de adyacencia
[33ma32e338[m Realicé la interfaz IGrafo y la clase Vertice para la estructura del grafo
[33mb1467dd[m Merge pull request #28 from TI-3er-Semestre/fix_restore_complete
[33m2ba0847[m[33m ([m[1;31morigin/fix_restore_complete[m[33m)[m Reestructuracion del proyecto a Java FX
[33me6686ed[m Merge pull request #26 from TI-3er-Semestre/fix_restore_complete
[33m222bbf6[m fix: restore complete implementation of Game, Board, BST, TranspositionTable and tests
[33me01176f[m Merge pull request #25 from TI-3er-Semestre/develop
[33md0cce7e[m Merge pull request #24 from TI-3er-Semestre/fix_restore_entrega2
[33m445f933[m[33m ([m[1;31morigin/fix_restore_entrega2[m[33m)[m Merge branch 'develop' into fix_restore_entrega2
[33mb43b0b9[m fix: implement TranspositionTable and BinarySearchTree, restore complete tests
[33mcb17813[m Merge pull request #23 from TI-3er-Semestre/develop
[33m3ed6d28[m Merge pull request #22 from TI-3er-Semestre/main
[33m74bf75c[m Merge pull request #21 from TI-3er-Semestre/CasosUsos
[33m239c9b6[m[33m ([m[1;31morigin/CasosUsos[m[33m)[m Merge branch 'develop' into CasosUsos
[33mc35b60a[m Merge pull request #20 from TI-3er-Semestre/feat_testsLineales
[33m5c10596[m[33m ([m[1;31morigin/feat_testsLineales[m[33m)[m feat(doc): add TAD structures and complexity review
[33m01e84cb[m Merge pull request #19 from TI-3er-Semestre/develop
[33mb583977[m Merge pull request #18 from TI-3er-Semestre/main
[33m99712a1[m Metodo de la Ingenieria
[33m7883f0d[m Merge pull request #17 from TI-3er-Semestre/develop
[33m2d0aa3a[m Analisis de Complejidad
[33mc000ced[m Casos de uso versión 2
[33m975b045[m Merge pull request #15 from TI-3er-Semestre/develop
[33m38c38ec[m Merge Conflicts
[33md3d2576[m Model - Structures - Test Modificaciones
[33m0636a4d[m Merge pull request #14 from TI-3er-Semestre/develop
[33m7295a63[m Merge pull request #13 from TI-3er-Semestre/feat_testsLineales
[33m938f6ab[m feat(game): implement queueCommand, processInputBuffer, undo + fix tests
[33md153bed[m test(game):fix GameTest conflicts
[33ma6db712[m test(game):fix GameTest
[33m495e1b5[m Merge branch 'main' of https://github.com/TI-3er-Semestre/ti-sokoban-herrera_fernandez_quintero into develop
[33mbd6629d[m Merge pull request #12 from TI-3er-Semestre/ImplementGame
[33m77048ba[m Merge pull request #11 from TI-3er-Semestre/GameTest
[33m7c2a278[m