
### Escuela Colombiana de Ingeniería
### Arquitecturas de Software - ARSW
## Ejercicio Introducción al paralelismo - Hilos - Caso BlackListSearch

### Samuel Castelblanco Tellez
### Ángela Gómez Valencia


### Dependencias:
####   Lecturas:
*  [Threads in Java](http://beginnersbook.com/2013/03/java-threads/)  (Hasta 'Ending Threads')
*  [Threads vs Processes]( http://cs-fundamentals.com/tech-interview/java/differences-between-thread-and-process-in-java.php)

### Descripción
  Este ejercicio contiene una introducción a la programación con hilos en Java, además de la aplicación a un caso concreto.
  

**Parte I - Introducción a Hilos en Java**

1. De acuerdo con lo revisado en las lecturas, complete las clases CountThread, para que las mismas definan el ciclo de vida de un hilo que imprima por pantalla los números entre A y B.

Para esta parte, los integrantes escribieron la clase de CountThread teniendo en cuenta la función start(), la cual fue necesaria para que los 3 threads comenzaran a trabajar con la impresión de los números en el rango dado. A continuación se muestran los resultados: 

![Resultados de impresión sección 1.1](/img/results1.1.png)

En adición, se utilizó una lógica básica matemática para hacer la división correcta del rango dado en 3 subrangos, independientemente de si el rango es un múltiplo de 3 o no. 

![Lógica utilizada para las 3 subdivisiones del rango](/img/main.1.1.png)


2. Complete el método __main__ de la clase CountMainThreads para que:
	1. Cree 3 hilos de tipo CountThread, asignándole al primero el intervalo [0..99], al segundo [99..199], y al tercero [200..299].
	2. Inicie los tres hilos con 'start()'.
	3. Ejecute y revise la salida por pantalla. 
	4. Cambie el incio con 'start()' por 'run()'. Cómo cambia la salida?, por qué?.

Al usar el run(), el programa corre en orden, es decir, es posible evidenciar los números en orden. Por otro lado, en el start() se presentan los números de forma desordenada, en donde se evidencia como se intercalan los números en cada uno de los threads. Crea un nuevo hilo que se encarga se ir imprimiendo los números que tiene pendiente. Por esa razón, en el run() 

**Parte II - Ejercicio Black List Search**


Para un software de vigilancia automática de seguridad informática se está desarrollando un componente encargado de validar las direcciones IP en varios miles de listas negras (de host maliciosos) conocidas, y reportar aquellas que existan en al menos cinco de dichas listas. 

Dicho componente está diseñado de acuerdo con el siguiente diagrama, donde:

- HostBlackListsDataSourceFacade es una clase que ofrece una 'fachada' para realizar consultas en cualquiera de las N listas negras registradas (método 'isInBlacklistServer'), y que permite también hacer un reporte a una base de datos local de cuando una dirección IP se considera peligrosa. Esta clase NO ES MODIFICABLE, pero se sabe que es 'Thread-Safe'.

- HostBlackListsValidator es una clase que ofrece el método 'checkHost', el cual, a través de la clase 'HostBlackListDataSourceFacade', valida en cada una de las listas negras un host determinado. En dicho método está considerada la política de que al encontrarse un HOST en al menos cinco listas negras, el mismo será registrado como 'no confiable', o como 'confiable' en caso contrario. Adicionalmente, retornará la lista de los números de las 'listas negras' en donde se encontró registrado el HOST.

![](img/Model.png)

Al usarse el módulo, la evidencia de que se hizo el registro como 'confiable' o 'no confiable' se dá por lo mensajes de LOGs:

INFO: HOST 205.24.34.55 Reported as trustworthy

INFO: HOST 205.24.34.55 Reported as NOT trustworthy


Al programa de prueba provisto (Main), le toma sólo algunos segundos análizar y reportar la dirección provista (200.24.34.55), ya que la misma está registrada más de cinco veces en los primeros servidores, por lo que no requiere recorrerlos todos. Sin embargo, hacer la búsqueda en casos donde NO hay reportes, o donde los mismos están dispersos en las miles de listas negras, toma bastante tiempo.

Éste, como cualquier método de búsqueda, puede verse como un problema [vergonzosamente paralelo](https://en.wikipedia.org/wiki/Embarrassingly_parallel), ya que no existen dependencias entre una partición del problema y otra.

Para 'refactorizar' este código, y hacer que explote la capacidad multi-núcleo de la CPU del equipo, realice lo siguiente:

1. Cree una clase de tipo Thread que represente el ciclo de vida de un hilo que haga la búsqueda de un segmento del conjunto de servidores disponibles. Agregue a dicha clase un método que permita 'preguntarle' a las instancias del mismo (los hilos) cuantas ocurrencias de servidores maliciosos ha encontrado o encontró.

2. Agregue al método 'checkHost' un parámetro entero N, correspondiente al número de hilos entre los que se va a realizar la búsqueda (recuerde tener en cuenta si N es par o impar!). Modifique el código de este método para que divida el espacio de búsqueda entre las N partes indicadas, y paralelice la búsqueda a través de N hilos. Haga que dicha función espere hasta que los N hilos terminen de resolver su respectivo sub-problema, agregue las ocurrencias encontradas por cada hilo a la lista que retorna el método, y entonces calcule (sumando el total de ocurrencuas encontradas por cada hilo) si el número de ocurrencias es mayor o igual a _BLACK_LIST_ALARM_COUNT_. Si se da este caso, al final se DEBE reportar el host como confiable o no confiable, y mostrar el listado con los números de las listas negras respectivas. Para lograr este comportamiento de 'espera' revise el método [join](https://docs.oracle.com/javase/tutorial/essential/concurrency/join.html) del API de concurrencia de Java. Tenga también en cuenta:

	* Dentro del método checkHost Se debe mantener el LOG que informa, antes de retornar el resultado, el número de listas negras revisadas VS. el número de listas negras total (línea 60). Se debe garantizar que dicha información sea verídica bajo el nuevo esquema de procesamiento en paralelo planteado.

	* Se sabe que el HOST 202.24.34.55 está reportado en listas negras de una forma más dispersa, y que el host 212.24.24.55 NO está en ninguna lista negra.


**Parte II.I Para discutir la próxima clase (NO para implementar aún)**

La estrategia de paralelismo antes implementada es ineficiente en ciertos casos, pues la búsqueda se sigue realizando aún cuando los N hilos (en su conjunto) ya hayan encontrado el número mínimo de ocurrencias requeridas para reportar al servidor como malicioso. Cómo se podría modificar la implementación para minimizar el número de consultas en estos casos?, qué elemento nuevo traería esto al problema?

**Parte III - Evaluación de Desempeño**

A partir de lo anterior, implemente la siguiente secuencia de experimentos para realizar las validación de direcciones IP dispersas (por ejemplo 202.24.34.55), tomando los tiempos de ejecución de los mismos (asegúrese de hacerlos en la misma máquina):

1. Un solo hilo.
2. Tantos hilos como núcleos de procesamiento (haga que el programa determine esto haciendo uso del [API Runtime](https://docs.oracle.com/javase/7/docs/api/java/lang/Runtime.html)).
3. Tantos hilos como el doble de núcleos de procesamiento.
4. 50 hilos.
5. 100 hilos.

Al iniciar el programa ejecute el monitor jVisualVM, y a medida que corran las pruebas, revise y anote el consumo de CPU y de memoria en cada caso. ![](img/jvisualvm.png)

Con lo anterior, y con los tiempos de ejecución dados, haga una gráfica de tiempo de solución vs. número de hilos. Analice y plantee hipótesis con su compañero para las siguientes preguntas (puede tener en cuenta lo reportado por jVisualVM):


---
## RESPUESTAS A PREGUNTAS DE DISCUSIÓN

### Parte II.I — Terminación temprana

> ¿Cómo se podría modificar la implementación para minimizar el número de consultas cuando los N hilos ya hayan encontrado el número mínimo de ocurrencias requeridas? ¿Qué elemento nuevo traería esto al problema?

**Respuesta:** Se puede implementar un mecanismo de **cancelación cooperativa** usando una variable atómica compartida (por ejemplo `AtomicBoolean` o `volatile boolean`) que actúe como "bandera de parada". Cada hilo, antes de consultar la siguiente lista negra, verifica esta bandera. Cuando un hilo detecta que el contador global de ocurrencias —protegido con `synchronized` o usando `AtomicInteger`— ya alcanzó `BLACK_LIST_ALARM_COUNT`, establece la bandera a `true` y todos los hilos terminan su búsqueda anticipadamente.

El **nuevo elemento** que esto introduce es la **necesidad de sincronización entre hilos** (coordinación de escritura/lectura de variables compartidas), lo cual:
- Agrega un costo de contención (`synchronized` / `AtomicInteger`).
- Introduce un *trade-off*: menos consultas a listas negras vs. overhead de sincronización.
- Hace que el speedup dependa de qué tan dispersas están las ocurrencias. Si todas están al final, la bandera nunca se activa temprano y no hay ganancia.

---

### Parte IV — Ley de Amdahl y desempeño

**1. ¿Por qué el mejor desempeño no se logra con 500 hilos? ¿Cómo se compara con 200?**

La ley de Amdahl establece que el speedup _S(n)_ está limitado por la fracción secuencial del algoritmo: por más hilos que se agreguen, nunca se supera ese límite teórico. En la práctica, al aumentar excesivamente los hilos aparecen factores que degradan el desempeño:

- **Overhead de creación y destrucción**: 500 hilos implican asignar stacks individuales (~1 MB cada uno en JVM), sumando ~500 MB solo en stacks, más el tiempo de inicialización.
- **Context switching**: con 500 hilos para ~80,000 listas, cada hilo procesa apenas ~160 listas. El SO invierte más tiempo alternando entre hilos que ejecutando trabajo útil.
- **Contención de recursos**: más hilos compiten por caché L1/L2/L3, provocando _cache thrashing_.
- **Rendimientos decrecientes**: llega un punto donde agregar hilos **empeora** el tiempo (pendiente positiva en la curva).

**Sin embargo, nuestros resultados muestran una excepción a esta regla:** en este experimento, el speedup siguió mejorando hasta 100 hilos (109.67×). Esto NO contradice a Amdahl, sino que revela que el problema no es puramente CPU-bound. La fachada `HostBlacklistsDataSourceFacade` internamente **simula latencia** en cada consulta (`isInBlackListServer`), lo que hace que los hilos pasen tiempo esperando —comportamiento típico de un problema I/O-bound—. En ese escenario, más hilos permiten que mientras unos esperan, otros ejecuten, y el speedup puede superar ampliamente el número de núcleos.

Con 200 hilos probablemente se vería una mejora menor que la observada de 50 a 100 (el speedup marginal se reduce). Con 500 hilos el overhead de creación y context switching comenzaría a dominar, y el tiempo **empeoraría** respecto a 100-200 hilos.

**2. ¿Cómo se comporta usar tantos hilos como núcleos vs. el doble de núcleos?**

En nuestro experimento (8 núcleos):

| Configuración | Tiempo (ms) | Speedup |
|--------------|------------|---------|
| 8 hilos (N)   | 14,333     | 8.93×   |
| 16 hilos (2N) | 7,610      | 16.82×  |

El doble de núcleos **redujo el tiempo casi a la mitad** (speedup ~2× adicional). Esto es **atípico** para un problema CPU-bound puro y confirma que la latencia simulada por la fachada permite que los 16 hilos aprovechen los tiempos de espera. Si el problema fuera cómputo puro, 16 hilos en 8 núcleos daría un speedup similar a 8 hilos (porque no hay más núcleos que usar).

La conclusión es que **el speedup depende del perfil real del problema**: en escenarios con latencia (I/O, red, sleep simulado), duplicar los hilos puede dar ganancias significativas incluso sin duplicar los núcleos físicos.

**3. ¿Se aplicaría mejor la ley de Amdahl con 100 máquinas de 1 hilo cada una? ¿Y con c hilos en 100/c máquinas?**

**100 máquinas × 1 hilo**: Sí, la ley de Amdahl se aplicaría **mucho mejor** porque:
- Cada máquina tiene su propia CPU, memoria y caché — **no hay contención de recursos compartidos**.
- No existe el overhead de context switching entre hilos de una misma máquina.
- El speedup sería cercano al ideal teórico, limitado solo por la fracción secuencial P y la latencia de red para distribuir/consolidar resultados.
- La porción paralelizable del algoritmo (consultar listas negras) es cercana al 100%, por lo que el speedup teórico con 100 máquinas se aproximaría a 100×.

**c hilos en 100/c máquinas distribuidas**: También mejoraría significativamente respecto a una sola máquina. Las ventajas:
- Se elimina la contención de CPU y caché entre máquinas (cada máquina maneja sus propios c hilos).
- La comunicación entre hilos de una misma máquina no compite con hilos de otras máquinas.
- La única desventaja nueva es la **latencia de red** para distribuir el trabajo y consolidar resultados, lo cual es un factor secuencial adicional que Amdahl no modela directamente pero que en la práctica es pequeño comparado con la ganancia de eliminar el context switching masivo.

En resumen: la versión distribuida escala mejor que muchos hilos en una sola máquina porque elimina el cuello de botella del _context switching_ y la contención de recursos compartidos, a cambio de una latencia de red que suele ser marginal frente al beneficio.

---
## RESULTADOS PARTE III — Evaluación de Desempeño

**Máquina:** 8 núcleos | **IP de prueba:** `202.24.34.55` (dispersa, encontrada en 5 listas negras)

### Estado inicial (antes de ejecutar)

![Estado inicial jVisualVM](img/parte3/0-inicio.png)

| Métrica | Valor |
|---------|-------|
| Threads activos | 14 |
| CPU | En reposo |
| Heap | ~26 MB (base) |

### Tabla de resultados por configuración

| # | Hilos | Tiempo (ms) | Speedup vs 1 hilo | CPU pico (%) | Heap usado | Threads totales | Captura jVisualVM |
|---|-------|------------|-------------------|-------------|------------|-----------------|-------------------|
| 1 | 1     | 128,039.81 | 1.00×             | 4.3%        | 26 MB      | 15              | ![1 hilo](img/parte3/1-hilo.png) |
| 2 | 8     | 14,333.37  | 8.93×             | 3.6%        | 34 MB      | 22              | ![8 hilos](img/parte3/8-hilos.png) |
| 3 | 16    | 7,610.05   | 16.82×            | 8.2%        | 50 MB      | 29              | ![16 hilos](img/parte3/16-hilos.png) |
| 4 | 50    | 2,686.30   | 47.67×            | 5.9%        | 86 MB      | 63              | ![50 hilos](img/parte3/50-hilos.png) |
| 5 | 100   | 1,167.46   | 109.67×           | 6.0%        | 135 MB     | 115             | ![100 hilos](img/parte3/100-hilos.png) |

### Observaciones

- **Speedup casi lineal** hasta 16 hilos (2× núcleos), lo cual es excepcional y sugiere que el problema está más limitado por I/O que por CPU. La fachada `HostBlacklistsDataSourceFacade` internamente simula latencia de consulta, lo que permite que más hilos aprovechen el tiempo de espera.
- **CPU nunca llegó al 100%** (máximo 8.2%), confirmando que el cuello de botella no es cómputo puro sino el mecanismo interno de la fachada.
- **Memoria heap crece linealmente** con el número de hilos (cada hilo crea su propia `LinkedList` de resultados), pero se mantiene en rangos manejables (< 200 MB).
- **Threads totales** = hilos de prueba + hilos de JVM (GC, monitoreo, etc.). La diferencia es consistente (~13-15 hilos base).

### Gráfica

![alt text](image-1.png)

Con estos datos se debe construir una gráfica **Tiempo de ejecución vs. Número de hilos**. La curva resultante muestra un descenso pronunciado al inicio que se va aplanando — comportamiento típico de la Ley de Amdahl.

