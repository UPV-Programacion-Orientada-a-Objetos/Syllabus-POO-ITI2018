# Multi-Hilo y Procesamiento Concurrente

En esta sección, analizaremos las formas de aumentar el rendimiento de las aplicaciones Java mediante el uso de los hilos (subprocesos) que procesan los datos simultáneamente. Se explicará el concepto de hilos en Java y se demostrará su uso. También se dicatará la diferencia entre el procesamiento paralelo y concurrente; y cómo evitar resultados impredecibles causados por la modificación concurrente del recurso compartido. 

## Hilo VS Proceso

Java tiene dos unidades de ejecución: proceso e hilo. Un **proceso** generalmente representa la máquina virtual (Java Virtual Machine) completa, aunque una aplicación puede crear otro proceso usando `java.lang.ProcessBuilder`. Pero, dado que el caso de múltiples procesos está fuera del alcance de este curso, nos enfocaremos en la segunda unidad de ejecución, es decir, un **hilo**, que es similar a un proceso pero menos aislado de otros hilos y requiere menos recursos para la ejecución.

Un hilo puede tener muchos subprocesos en ejecución (otros hilos) y al menos un hilo llamado el subproceso principal, el cual inicia la aplicación, y que usamos en cada ejemplo. Los hilos pueden compartir recursos, incluida la memoria y los archivos abiertos, lo que permite una mejor eficiencia. Pero viene con un precio de mayor riesgo de interferencia mutua involuntaria e incluso bloqueo de la ejecución (Deathlock). Ahí es donde se requieren habilidades de programación y una comprensión de las técnicas de concurrencia.

### Hilos VS Demonios

Hay un tipo particular de hilo llamado demonio.

  > La palabra demonio tiene un origen griego antiguo que significa una divinidad o ser sobrenatural de una naturaleza entre dioses y humanos y un espíritu interno o acompañante o una fuerza inspiradora.

En informática, el término daemon tiene un uso más mundano y se aplica a un *programa informático que se ejecuta como un proceso en segundo plano, en lugar de estar bajo el control directo de un usuario interactivo*. Es por eso que existen los siguientes dos tipos de hilos en Java:

  > * Hilo de usuario (predeterminado), iniciado por una aplicación (el hilo principal es uno de esos ejemplos).
  > * Hilo daemon que funciona en segundo plano en apoyo de la actividad de hilo de usuario.

Es por eso que todos los daemons salen inmediatamente después de que el último subproceso de usuario sale o son cancelados por JVM después de una excepción no controlada.

#### Extendiendo la clase `Thread`

Una forma de crear un hilo es extender la clase `java.lang.Thread` y sobrecargar su método `run()`. Por ejemplo:

```Java
package U3_Concurrencia.ejemplos;

import java.util.concurrent.TimeUnit;

public class MyThread extends Thread {

    private String parameter;

    public MyThread(String parameter) {
        this.parameter = parameter;
    }

    @Override
    public void run() {
        while (!"exit".equals(this.parameter)) {
            System.out.println((isDaemon() ? "daemon" : "user") + " thread " + this.getName() + "(id=" + this.getId() + ") parameter: " + this.parameter);
            pauseOneSecond();
        }

        System.out.println((isDaemon() ? "daemon" : " user") + " thread " + this.getName() + "(id=" + this.getId() + ") parameter: " + this.parameter);
    }

    public void setParameter(String parameter) {
        this.parameter = parameter;
    }

    private static void pauseOneSecond() {
        try {
            TimeUnit.SECONDS.sleep(1);
        }
        catch(InterruptedException e) {
            e.printStackTrace();
        }
    }
}
```

```Java
package U3_Concurrencia.ejemplos;

import java.util.concurrent.TimeUnit;

public class Threads {

    public static void main(String[] args) {
        runExtendedThreads();
    }

    private static void runExtendedThreads() {
        MyThread thr1 = new MyThread("One");
        thr1.start();
        MyThread thr2 = new MyThread("Two");
        thr2.setDaemon(true);
        thr2.start();
        pauseOneSecond();
        thr1.setParameter("exit");
        pauseOneSecond();
        System.out.println("Main thread exists");
    }

    private static void pauseOneSecond() {
        try {
            TimeUnit.SECONDS.sleep(1);
        }
        catch(InterruptedException e) {
            e.printStackTrace();
        }
    }
}
```

Si el método `run()` no se sobrecarga, el hilo no hace nada. En el ejemplo, el hilo imprime su nombre y otras propiedades cada segundo siempre que el parámetro no sea igual a la cadena "exit"; de lo contrario sale. Como puede ver, el subproceso principal crea otros dos subprocesos (`thr1` y `thr2`, éste se establece como daemon), hace una pausa por un segundo, establece la salida de parámetros en el subproceso del usuario, pausa otro segundo y, finalmente, sale. (El método main () completa su ejecución). Si ejecutamos el código anterior tendremos la siguiente salida:

```
$ javac -cp . U3_Concurrencia/ejemplos/Threads.java
$ java -cp . U3_Concurrencia.ejemplos.Threads
daemon thread Thread-1(id=12) parameter: Two
user thread Thread-0(id=11) parameter: One
daemon thread Thread-1(id=12) parameter: Two
 user thread Thread-0(id=11) parameter: exit
Main thread exists
```

Se observa que el hilo demonio (`thr2`) sale automáticamente tan pronto como sale el último hilo del usuario (hilo principal en nuestro ejemplo).

#### Implementando la interfaz `Runnable`

La segunda forma de crear un hilo es usar una clase que implemente la interfaz `java.lang.Runnable`. Aquí hay un ejemplo de dicha clase que tiene casi exactamente la misma funcionalidad que la clase `MyThread`:

```Java
package U3_Concurrencia.ejemplos;

import java.util.concurrent.TimeUnit;

public class MyRunnable implements Runnable {

    private String parameter;
    private String name;

    public MyRunnable(String name) {
        this.name = name;
    }

    public void run() {
        while (!"exit".equals(parameter)) {
            System.out.println("thread" + this.name + ", parameter:" + parameter);
            pauseOneSecond();
        }
        System.out.println("thread " + this.name + ", parameter: " + parameter);
    }

    public void setParameter(String parameter) {
        this.parameter = parameter;
    }

    private void pauseOneSecond() {
        try {
            TimeUnit.SECONDS.sleep(1);
        }
        catch(InterruptedException e) {
            e.printStackTrace();
        }
    }
}
```

La diferencia es que no hay ningún método `isDaemon()`, `getId()` ni ningún otro método similar. La clase `MyRunnable` puede ser cualquier clase que implemente la interfaz `Runnable`, por lo que no podemos imprimir si el hilo es demonio o no. Es por eso que hemos agregado la propiedad de nombre, para que podamos identificar el hilo.

Podemos usar la clase `MyRunnable` para crear hilos similares a los que usamos en la clase `MyThread`:

```Java
private static void runImplementsRunnable() {
        MyRunnable myRunnable1 = new MyRunnable("One");
        MyRunnable myRunnable2 = new MyRunnable("Two");

        Thread thr1 = new Thread(myRunnable1);
        thr1.start();
        Thread thr2 = new Thread(myRunnable2);
        thr2.setDaemon(true);
        pauseOneSecond();
        myRunnable1.setParameter("exit");
        pauseOneSecond();
        System.out.println("Main thread exists");

    }
```

Si ejecutamos el código anterior tendremos la siguiente salida:

```
$ javac -cp . U3_Concurrencia/ejemplos/MyRunnable.java
$ javac -cp . U3_Concurrencia/ejemplos/Threads.java
$ java -cp . U3_Concurrencia.ejemplos.Threads
threadOne, parameter:null
thread One, parameter: exit
Main thread exists
```


## Extender a la clase `Thread` VS Implementar la interfaz `Runnable`

La implementación de `Runnable` tiene la ventaja (y en algunos casos la única opción posible) de permitir que la implementación extienda otra clase. Es particularmente útil cuando desea agregar un comportamiento similar a un hilo a una clase existente. La implementación de `Runnable` permite una mayor flexibilidad en el uso. Pero de lo contrario, no hay diferencia en la funcionalidad en comparación con la extensión de la clase `Thread`.

La clase `Thread` tiene varios constructores que permiten configurar el nombre del hilo y el grupo al que pertenece. La agrupación de subprocesos ayuda a administrarlos en el caso de muchos subprocesos que se ejecutan en paralelo. La clase `Thread` también tiene varios métodos que proporcionan información sobre el estado del subproceso, sus propiedades y permite controlar su comportamiento.

Como has visto, la ID del hilo se genera automáticamente. No se puede cambiar, pero se puede reutilizar después de que se termina el subproceso. Varios hilos, por otro lado, se pueden configurar con el mismo nombre.

La prioridad de ejecución también se puede establecer mediante programación con un valor entre `Thread.MIN_PRIORITY` y `Thread.MAX_PRIORITY`. Cuanto más pequeño es el valor, más tiempo se permite ejecutar el subproceso, lo que significa que tiene mayor prioridad. Si no se establece, el valor de prioridad predeterminado es `Thread.NORM_PRIORITY`.

El estado de un hilo puede tener uno de los siguientes valores:
  > * NEW: cuando un hilo aún no ha comenzado
  > * RUNNABLE: cuando se ejecuta un hilo
  > * BLOCKED: cuando un hilo está bloqueado y está esperando un bloqueo del monitor
  > * WAITING: cuando un hilo espera indefinidamente a que otro hilo realice una acción particular
  > * TIMED_WAITING: cuando un subproceso está esperando que otro subproceso realice una acción durante un tiempo de espera especificado
  > * TERMINATED: cuando un hilo ha salido

Los subprocesos y cualquier objeto para el caso también pueden comunicarse entre sí utilizando los métodos `wait()`, `notify()` y `notifyAll()` de la clase base `java.lang.Object`.


## usando un grupo de hilos

Cada hilo requiere recursos: CPU y memoria. Significa que se debe controlar el número de subprocesos, y una forma de hacerlo es crear un número fijo de ellos: grupo (pool). Además, la creación de un objeto conlleva una sobrecarga que puede ser importante para algunas aplicaciones.

En esta sección, analizaremos la interfaz `Executor` y sus implementaciones proporcionadas en el paquete `java.util.concurrent`. Encapsulan la gestión de hilos y minimizan el tiempo que un desarrollador dedica a escribir el código relacionado con los ciclos de vida de los hilos.

Existen tres interfaces `Executor` definidas en el paquete `java.util.concurrent`:

  > * La interfaz base `Executor`: solo tiene un método `void execute(Runnable r)`.
  > * La interfaz `ExecutorService`: esta extiende a `Executor` y agrega cuatro grupos de métodos que gestionan el ciclo de vida de un trabajador (worker) y del propio `Executor`:
  >   * Los métodos `submit()` que colocan un objeto `Runnable` o `Callable` en la cola para la ejecución (`Callable` permite que el subproceso devuelva un valor), devuelven un objeto de la interfaz `Future`, que se puede utilizar para acceder al valor devuelto por `Callable`, y para gestionar el estado del subproceso.
  >    * Métodos `invokeAll()` que colocan una colección de objetos de la interfaz `Callable` en la cola para la ejecución que luego devuelve la Lista de objetos `Future` cuando todos los subprocesos están completos (también hay un método `invokeAll()` sobrecargado con un tiempo de espera).
  >    * Métodos `invokeAny()` que colocan una colección de objetos de la interfaz `Callable` en la cola para la ejecución; devolver un objeto `Future`  de cualquiera de los subprocesos, que se ha completado (también hay un método `invokeAny()` sobrecargado con un tiempo de espera).
  >    * Methods that manage the worker threads' status and the service itself as follows:
  >      * `shutdown()`: evita que los nuevos subprocesos de trabajo se envíen al servicio.
  >      * `shutdownNow()`: interrumpe cada subproceso que no se completa. Se debe escribir un subproceso para que compruebe su propio estado periódicamente (usando `Thread.currentThread().IsInterrupted()`, por ejemplo) y se apaga por sí mismo; de lo contrario, continuará ejecutándose incluso después de llamar a `shutdownNow()`.
  >      * `isShutdown()`: Comprueba si se inició el cierre del ejecutor.
  >      * `awaitTermination(long timeout, TimeUnit timeUnit)`: espera hasta que todos los subprocesos hayan completado la ejecución después de una solicitud de apagado, o se produzca el tiempo de espera, o se interrumpa el subproceso actual, lo que ocurra primero.
  >      * `isTerminated()`: Comprueba si todos los subprocesos se han completado después de que se inició el cierre. Nunca devuelve verdadero a menos que se haya llamado primero a `shutdown()` o `shutdownNow()`.
  > * La interfaz `ScheduledExecutorService`: extiende a `ExecutorService` y agrega métodos que permiten programar la ejecución (una vez y periódica) de subprocesos.

Una implementación basada en grupo de `ExecutorService`puede ser creada utilizando las clases `java.util.concurrent.ThreadPoolExecutor` o `java.util.concurrent.ScheduledThreadPoolExecutor`. Existe una clase factoria `java.util.concurrent.Executors` que cubre la mayoría de los casos prácticos.

Por lo tanto, antes de escribir código personalizado para la creación de grupos de subprocesos, recomendamos encarecidamente utilizar los siguientes métodos de la clase factoría `java.util.concurrent.Executors`:

  > * `newCachedThreadPool()` que crea un grupo de subprocesos que agrega un nuevo subproceso según sea necesario, a menos que haya un subproceso inactivo creado anteriormente; los subprocesos que han estado inactivos durante 60 segundos se eliminan del grupo.
  > * `newSingleThreadExecutor()` que crea una instancia de  `ExecutorService(pool)` que ejecuta hilos secuencialmente.
  > * `newSingleThreadScheduledExecutor()` crea un ejecutor de un solo subproceso que se puede programar para ejecutarse después de un retraso determinado o para ejecutarse periódicamente.
  > * `newFixedThreadPool(int nThreads)` que crea un grupo de subprocesos que reutiliza un número fijo de subprocesos; Si se envía una nueva tarea cuando todos los hilos todavía se están ejecutando, se colocará en la cola hasta que haya un hilo.
  > * `newScheduledThreadPool(int nThreads)` que crea un grupo de subprocesos de un tamaño fijo que puede programarse para ejecutarse después de un retraso determinado o para ejecutarse periódicamente.
  > * `newWorkStealingThreadPool(int nThreads)` que crea un grupo de subprocesos que utiliza el algoritmo work-stealing utilizado por `ForkJoinPool`, que es particularmente útil en caso de que los subprocesos generen otros subprocesos, como en un algoritmo recursivo; también se adapta a la cantidad especificada de CPU, que puede establecer más alto o más bajo que el conteo real de CPU en su computadora.

Cada uno de estos métodos tiene una versión sobrecargada que permite pasar un `ThreadFactory` que se usa para crear un nuevo hilo cuando sea necesario. Veamos cómo funciona, primero ejecutamos otra versión de la clase `MyRunnable`:

```Java
private static class MyPrivateRunnable implements Runnable {
        
        private String name;

        public MyPrivateRunnable(String name) {
            this.name = name;
        }

        public void run() {
            try {
                while (true) {
                    System.out.println(this.name + " is working...");
                    TimeUnit.SECONDS.sleep(1);
                }
            }
            catch(InterruptedException e) {
                System.out.println(this.name + " was interrupted\n" + this.name + " Thread.currentThread().isInterrupted=" + Thread.currentThread().isInterrupted());
            }
        }
    }
```

Ya no podemos usar la propiedad `parameter` de la versión anterior para decirle al subproceso que deje de ejecutarse porque el ciclo de vida del subproceso ahora será controlado por el `ExecutorService`, y la forma en que lo hace es llamando al método `interrupt()`. Además, se de tomarn en cuenta que el hilo que creamos tiene un bucle infinito, por lo que nunca dejará de ejecutarse hasta que se lo obligue (llamando al método `interrupt ()`).

Se implementa un código que realiza lo siguiente:

  > * Crear un grupo de tres hilos
  > * Asegurarse de que no acepte nuevos hilos
  > * Esperar un período de tiempo fijo para que todos los hilos terminen lo que hacen
  > * Detener (interrumpir) los hilos que no terminaron lo que hacen
  > * salir

```Java
private static void threadPool(ExecutorService pool) {
        String[] names = {"One", "Two", "Three"};

        for (int i = 0; i < names.length; i++) {
            pool.execute(new MyPrivateRunnable(names[i]));
        }

        System.out.println("Before shutdown: isShutdown()=" + pool.isShutdown() + "\n isTerminated()=" + pool.isTerminated());

        pool.shutdown();  // New threads cannot be submitted
        // pool.execute(new MyRunnable("Four")); //java.util.concurrent.RejectedExecutionException
        System.out.println("After shutdown: isShutdown()=" + pool.isShutdown() + "\n isTerminated()=" + pool.isTerminated());

        try {
            long timeout = 100;
            TimeUnit timeunit = TimeUnit.MILLISECONDS;
            System.out.println("Waiting all threads completition for " + timeout + " " + timeunit + " ... ");
            // Blocks until timeout or all threads complete execution,
            // or the current thread is interrupted, whichever happens first.
            boolean isTerminated = pool.awaitTermination(timeout, timeunit);

            System.out.println("isTerminated() = " + isTerminated);
            if (!isTerminated) {
                System.out.println("Calling shutdown()...");
                List<Runnable> list = pool.shutdownNow();
                System.out.println(list.size() + " threads running");
                isTerminated = pool.awaitTermination(timeout, timeunit);
                if (!isTerminated) {
                    System.out.println("Some thread are still running");
                }
                System.out.println("Exiting");
            }
        }
        catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
```

El intento de agregar otro subproceso al grupo después de llamar a `pool.shutdown()` genera una excepción `java.util.concurrent.RejectedExecutionException`.

Observe el mensaje `Thread.currentThread().IsInterrupted() = false`. El hilo fue interrumpido. se sabe porque el hilo recibió la excepción `InterruptedException`. ¿Por qué entonces el método `isInterrupted()` devuelve falso? Esto se debe a que el estado del hilo se borró inmediatamente después de recibir el mensaje de interrupción. Se menciona ahora porque es una fuente de algunos errores de programación. Por ejemplo, si el hilo principal observa el hilo `MyPrivateRunnable` y llama `isInterrupted()` en él, el valor de retorno será falso, lo que puede ser engañoso después de que se interrumpió el hilo.

Entonces, en el caso de que otro hilo pueda estar monitoreando el hilo `MyPrivateRunnable`, la implementación de `MyPrivateRunnable` tiene que cambiarse a lo siguiente. Observe cómo se llama al método `interrupt()` en el bloque catch:

```Java
public void run() {
            ...
            catch(InterruptedException e) {
                Thread.currentThread().interrupt();
                System.out.println(this.name + " was interrupted\n" + this.name + " Thread.currentThread().isInterrupted=" + Thread.currentThread().isInterrupted());
            }
        }
```
Ahora, si ejecutamos este hilo usando el mismo grupo `ExecutorService` nuevamente, el resultado será:

```
One is working...
Two is working...
Three is working...
Before shutdown: isShutdown()=false
 isTerminated()=false
After shutdown: isShutdown()=true
 isTerminated()=false
Waiting all threads completition for 100 MILLISECONDS ... 
isTerminated() = false
Calling shutdown()...
0 threads running
Two was interrupted
Two Thread.currentThread().isInterrupted=true
One was interrupted
One Thread.currentThread().isInterrupted=true
Three was interrupted
Three Thread.currentThread().isInterrupted=true
Exiting
```

Como puede ver, ahora el valor devuelto por el método `isInterrupted()` es verdadero y corresponde a lo que sucedió. Para ser justos, en muchas aplicaciones, una vez que se interrumpe el subproceso, su estado no se vuelve a comprobar. Pero establecer el estado correcto es una buena práctica, especialmente en aquellos casos en los que no es el autor del código de nivel superior que crea el hilo.

En el ejemplo, hemos utilizado un grupo de subprocesos en caché que crea un nuevo subproceso según sea necesario o, si está disponible, reutiliza el subproceso ya utilizado, pero que completó su trabajo y regresó al grupo para una nueva asignación. No nos preocupábamos por demasiados hilos creados porque nuestra aplicación de demostración tenía tres hilos de trabajo como máximo y tenían una vida bastante corta.

Pero, en el caso de que una aplicación no tenga un límite fijo de subprocesos que pueda necesitar o no haya una buena manera de predecir cuánta memoria puede tomar un subproceso o cuánto tiempo puede ejecutarse, estableciendo un límite en el contador de subproceso  evita una degradación inesperada del rendimiento de la aplicación, la falta de memoria o el agotamiento de cualquier otro recurso que utilicen los subprocesos. Si el comportamiento del subproceso es extremadamente impredecible, un único grupo de subprocesos podría ser la única solución, con la opción de utilizar un ejecutor de grupo de subprocesos personalizado. Pero, en la mayoría de los casos, un ejecutor de grupo de subprocesos de tamaño fijo es un buen compromiso práctico entre las necesidades de la aplicación y la complejidad del código.

Establecer el tamaño de la agrupación demasiado bajo puede privar a la aplicación de la posibilidad de utilizar los recursos disponibles de manera efectiva. Por lo tanto, antes de seleccionar el tamaño de la agrupación, es aconsejable pasar un tiempo monitoreando la aplicación con el objetivo de identificar la idiosincrasia del comportamiento de la aplicación. De hecho, el ciclo despliegue-monitor-ajuste tiene que repetirse a lo largo del ciclo de vida de la aplicación para acomodar y aprovechar los cambios que ocurrieron en el código o el entorno de ejecución.

La primera característica que tiene en cuenta es la cantidad de CPU en su sistema, por lo que el tamaño del grupo de subprocesos puede ser al menos tan grande como el número de CPUs (nucleos). Luego, puede monitorear la aplicación y ver cuánto tiempo dedica cada subproceso a la CPU y cuánto tiempo utiliza otros recursos (como las operaciones de E/S). Si el tiempo dedicado a no usar la CPU es comparable con el tiempo de ejecución total del subproceso, puede aumentar el tamaño de la agrupación en la siguiente relación: el tiempo que no se utilizó la CPU dividido por el tiempo de ejecución total. Pero, eso es en el caso de que otro recurso (disco o base de datos) no sea un tema de disputa entre los hilos. Si este es el caso, puede usar ese recurso en lugar de la CPU como factor de delineación.

Suponiendo que los subprocesos de su aplicación no son demasiado grandes o demasiado largos, y pertenecen a la población general de los subprocesos típicos que completan su trabajo en un período de tiempo razonablemente corto, puede aumentar el tamaño del grupo agregando el (redondeado arriba) relación del tiempo de respuesta deseado y el tiempo que un subproceso utiliza la CPU u otro recurso más complejo. Esto significa que, con el mismo tiempo de respuesta deseado, cuanto menos use un subproceso la CPU u otro recurso al que se accede simultáneamente, mayor será el tamaño del grupo. Si el recurso contencioso tiene su propia capacidad para mejorar el acceso concurrente (como un grupo de conexiones en la base de datos), considere utilizar esa función primero.

Si el número requerido de subprocesos que se ejecutan al mismo tiempo cambia en tiempo de ejecución en diferentes circunstancias, puede hacer que el tamaño del grupo sea dinámico y crear un nuevo grupo con un nuevo tamaño (cerrando el grupo anterior después de que todos sus subprocesos se hayan completado). El recálculo del tamaño de un nuevo grupo también podría ser necesario después de agregar o quitar los recursos disponibles. Puede usar `Runtime.getRuntime().AvailableProcessors()` para ajustar mediante programación el tamaño de la agrupación en función del recuento actual de las CPU disponibles, por ejemplo.

Si ninguna de las implementaciones de ejecutor de grupo de subprocesos que vienen con el JDK se ajusta a las necesidades de una aplicación en particular, antes de escribir el código de administración de subprocesos desde cero, primero intente usar la clase `java.util.concurrent.ThreadPoolExecutor`. Tiene varios constructores sobrecargados.

Para darle una idea de sus capacidades, aquí está el constructor con la mayor cantidad de opciones:

  > ```Java
  > ThreadPoolExecutor (int corePoolSize, 
  >                  int maximumPoolSize, 
  >                  long keepAliveTime, 
  >                  TimeUnit unit, 
  >                  BlockingQueue<Runnable> workQueue, 
  >                  ThreadFactory threadFactory, 
  >                  RejectedExecutionHandler handler)
  > ```

Los parámetros del constructor son los siguientes:

  > * `corePoolSize` es el número de subprocesos que se deben mantener en el grupo, incluso si están inactivos, a menos que se llame al método `allowCoreThreadTimeOut (boolean value)` con valor verdadero.
  > * `maximumPoolSize` es el número máximo de subprocesos para permitir en el grupo.
  > * `keepAliveTime`: cuando el número de subprocesos es mayor que el núcleo, este es el tiempo máximo que el exceso de subprocesos inactivos esperará nuevas tareas antes de finalizar.
  > * `unit` es la unidad de tiempo para el argumento `keepAliveTime`.
  > * `workQueue` es la cola que se utiliza para mantener las tareas antes de que se ejecuten; esta cola solo contendrá los objetos `Runnable` enviados por el método `execute()`.
  > * `threadFactory` es la clase factoria para usar cuando el ejecutor crea un nuevo hilo.
  > * `handler` es el manejador a usar cuando la ejecución está bloqueada porque se alcanzan los límites del hilo y las capacidades de la cola.

Cada uno de los parámetros de constructor anteriores, excepto el `workQueue`, también se puede establecer a través del configurador correspondiente después de que se haya creado el objeto de la clase `ThreadPoolExecutor`, lo que permite una mayor flexibilidad y un ajuste dinámico de las características del grupo existente.

## Obteniendo resultados de un hilo

En nuestros ejemplos, hasta ahora, utilizamos el método `execute()` de la interfaz `ExecutorService` para iniciar un hilo. De hecho, este método proviene de la interfaz base de `Executor`. Mientras tanto, la interfaz `ExecutorService` tiene otros métodos (enumerados en la sección anterior Uso del grupo de subprocesos) que pueden iniciar subprocesos y recuperar los resultados de la ejecución de subprocesos.

El objeto que devuelve el resultado de la ejecución del subproceso es del tipo `Future`, una interfaz que tiene los siguientes métodos:

  > * `V get()`: Bloquea hasta que el hilo termine; devuelve el resultado (si está disponible).
  > * `V get(long timeout, TimeUnit unit)`: Bloquea hasta que finaliza el hilo o se agota el tiempo de espera proporcionado; devuelve el resultado (si está disponible)
  > * `boolean isDone()`: Devuelve verdadero sin el hilo ha terminado
  > * `boolean cancel(boolean mayInterruptIfRunning)`: Intenta cancelar la ejecución del hilo; devuelve `true` si tiene éxito; devuelve `false` también en el caso de que el hilo haya terminado normalmente cuando se llamó al método.
  > * `boolean isCancelled()`: Devuelve `true` si la ejecución del hilo se canceló antes de que haya finalizado normalmente

La observación *si está disponible* en la descripción del método `get()` significa que el resultado no siempre está disponible en principio, incluso cuando se llama al método `get()` sin parámetros. Todo depende del método utilizado para producir el objeto `Future`. Aquí hay una lista de todos los métodos de `ExecutorService` que devuelven objetos `Future`:

  > * `Futuro<?> Submit (Runnable task)`: envía el hilo (tarea) para su ejecución; devuelve un objeto `Future` que representa la tarea; el método `get()` del objeto `Future` devuelto devuelve `null`; por ejemplo, usemos la clase `MyRunnable` que funciona solo 100 milisegundos:

  ```Java
  private static class MyPrivateRunnable implements Runnable {
        private String name;

        public MyPrivateRunnable(String name) {
            this.name = name;
        }

        @Override
        public void run() {
            try {
                System.out.println(this.name + " is working...");
                TimeUnit.MILLISECONDS.sleep(100);
                System.out.println(this.name + " is done");
            }
            catch(InterruptedException e) {
                Thread.currentThread().interrupt();
                System.out.println(this.name + " was interrupted\n" + this.name + " Thread.currentThread().isInterrupted()=" + Thread.currentThread().isInterrupted());
            }
        }
    }
  ```

  Y, según los ejemplos de código de la sección anterior, creemos un método que cierre el grupo y termine todos los hilos, si es necesario

  ```Java
  private static void shutdownAndTerminate(ExecutorService pool) {
        try {
            long timeout = 100;
            TimeUnit timeunit = TimeUnit.MILLISECONDS;
            System.out.println("Waiting all threads completion for " + timeout + " " + timeunit + " ... ");
            // Blocks until timeout or all threads complete execution,
            // or the current thread is interrupted, whichever happens first.
            boolean isTerminated = pool.awaitTermination(timeout, timeunit);
            System.out.println("isTerminated=" + isTerminated);
            if (!isTerminated) {
                System.out.println("Calling shutdownNow()...");
                List<Runnable> list = pool.shutdownNow();
                System.out.println(list.size() + " threads running");
                isTerminated = pool.awaitTermination(timeout, timeunit);
                if (!isTerminated) {
                    System.out.println("Some threads are still running");
                }
                System.out.println("Exiting");
            }
        }
        catch(InterruptedException e) {
            e.printStackTrace();
        }
    }
```

Usaremos el método anterior `shutdownAndTerminate()` en un bloque `finally` para asegurarnos de que no se hayan dejado subprocesos en ejecución. Y aquí está el código que vamos a ejecutar:

```Java
private static void futureSubmitRunnable1() {
        System.out.println("\nfutureSubmitRunnable1():\n");

        ExecutorService pool = Executors.newSingleThreadExecutor();

        Future future = pool.submit(new MyPrivateRunnable("one"));
        System.out.println(future.isDone());
        System.out.println(future.isCancelled());

        try {
            System.out.println(future.get());
            System.out.println(future.isDone());
            System.out.println(future.isCancelled());
        }
        catch(Exception e) {
            e.printStackTrace();
        }
        finally {
            shutdownAndTerminate(pool);
        }
    }
```

La salida de este código es:

```
futureSubmitRunnable1():

false
false
one is working...
one is done
null
true
false
Waiting all threads completion for 100 MILLISECONDS ... 
isTerminated=false
Calling shutdownNow()...
0 threads running
Exiting
```

Como se esperaba, el método `get()` del objeto `Future` devuelve nulo, porque el método `run()` de Runnable no devuelve nada. Todo lo que podemos obtener del objeto `Future` devuelto es la información de que la tarea se completó o no.

  > * `Future<T> submit(Runnable task, T result)`: Envía el hilo (tarea) para su ejecución; devuelve objeto `Future` que representa la tarea con el resultado proporcionado en ella; por ejemplo, usaremos la siguiente clase como resultado:

  ```Java
  class MyPrivateResult {

        private String name;
        private double result;

        public MyPrivateResult(String name, double result) {
            this.name = name;
            this.result = result;
        }

        @Override
        public String toString() {
            return "Result{name=" + name + ", result=" + result + "}";
        }

    }
  ```

  El siguiente código demuestra cómo el objeto `Future` devuelve el resultado predeterminado devuelto por el método `submit()`:

  ```Java
  private static void futureSubmitRunnable3() {
        System.out.println("\nfutureSubmitRunnable3():");

        ExecutorService pool = Executors.newSingleThreadExecutor();

        Future<MyPrivateResult> future = pool.submit(new MyPrivateRunnable("Two"), new MyPrivateResult("Two", 42.0));

        System.out.println(future.isDone());
        System.out.println(future.isCancelled());

        try {
            System.out.println(future.get());
            System.out.println(future.isDone());
            System.out.println(future.isCancelled());
        }
        catch(Exception e) {
            e.printStackTrace();
        }
        finally {
            shutdownAndTerminate(pool);
        }
    }
  ```

  Si ejecutamos el código anterior, la salida será la siguiente:

  ```
  $ java -cp . U3_Concurrencia.ejemplos.Futures

futureSubmitRunnable3():
false
false
Two is working...
Two is done
Result{name=Two, result=42.0}
true
false
Waiting all threads completion for 100 MILLISECONDS ... 
isTerminated=false
Calling shutdownNow()...
0 threads running
Exiting
```

Como era de esperar, el método `get()` de Future devuelve el objeto pasado como parámetro.

  > * `Future<T> submit(Callable<T> task)`: Envía el hilo (tarea) para su ejecución; devuelve un objeto `Future` que representa la tarea con el resultado producido y devuelto por el método `V call()` de la interfaz `Callable`; ese es el único método invocable que tiene la interfaz. Por ejemplo

  ```Java
  class MyPrivateCallable implements Callable {

        private String name;

        public MyPrivateCallable(String name) {
            this.name = name;
        }

        public MyPrivateResult call() {
            try {
                System.out.println(this.name + " is working...");
                TimeUnit.MILLISECONDS.sleep(100);
                System.out.println(this.name + " is done");
                return new MyPrivateResult(name, 42.42);
            }
            catch(InterruptedException e) {
                System.out.println(this.name + " was interruped\n" + this.name + "Thread.currentThread().isInterrupted()=" + Thread.currentThread().isInterrupted());
            }
            return null;
        }
    }
  ```

  El resultado del código anterior es el siguiente:

  ```
  $ java -cp . U3_Concurrencia/ejemplos/Futures.java

futureSubmitCallable():
false
false
Three is working...
Three is done
Result{name=Three, result=42.42}
true
false
Waiting all threads completion for 100 MILLISECONDS ... 
isTerminated=false
Calling shutdownNow()...
0 threads running
Exiting
  ```

Como puede ver, el método `get()` del objeto `Future` devuelve el valor producido por el método `call()` de la clase `MyPrivateCallable`.

  > * `List<Future<T>> invokeAll(Collection<Callable<T>> tasks)`: Ejecuta todas las tareas `Callable` de la colección proporcionada; devuelve una lista de objetos `Futures` con los resultados producidos por la ejecución de objetos `Callable`.
  > * `List<Future<T>> invokeAll(Collection<Callable<T>>`: Ejecuta todas las tareas `Callable` de la colección proporcionada; devuelve una lista de `Futures` con los resultados producidos por la ejecución de objetos `Callable` o el tiempo de espera expira, lo que ocurra primero
  > * `T invokeAny(Collection<Callable<T>> tasks)`: Ejecuta todas las tareas `Callable` de la colección proporcionada; devuelve el resultado de uno que se ha completado con éxito (es decir, sin lanzar una excepción), si lo hace
  > * `T invokeAny(Collection<Callable<T>> tasks, long timeout, TimeUnit unit)`: Ejecuta todas las tareas `Callable`  de la colección proporcionada; devuelve el resultado de uno que se ha completado con éxito (es decir, sin lanzar una excepción), si está disponible antes de que expire el tiempo de espera proporcionado.

Como puede ver, hay muchas maneras de obtener los resultados de un hilo. El método que elija depende de las necesidades particulares de su aplicación.

## Paralelo VS Procesamiento concurrente

Cuando escuchamos que los subprocesos de trabajo se ejecutan al mismo tiempo, automáticamente asumimos que literalmente hacen lo que están programados para hacer en paralelo. Solo después de mirar bajo la mesa de un sistema de este tipo, nos damos cuenta de que tal procesamiento paralelo es posible sólo cuando los hilos son ejecutados cada uno por un CPU diferente. De lo contrario, comparten el mismo tiempo de procesamiento. Los percibimos trabajando al mismo tiempo sólo porque los espacios de tiempo que usan son muy cortos, una fracción de las unidades de tiempo que hemos usado en nuestra vida cotidiana. Cuando los hilos comparten el mismo recurso, en informática, decimos que lo hacen **concurrentemente**.

## Acceso concurrente al mismo recurso

Dos o más hilos modificando el mismo valor mientras otros hilos lo leen es la descripción más general de uno de los problemas de acceso concurrente. Los problemas más sutiles incluyen **interferencia de hilo** y **errores de consistencia de memoria**, que producen resultados inesperados en fragmentos de código aparentemente benignos. En esta sección, vamos a demostrar tales casos y formas de evitarlos.

A primera vista, la solución parece bastante sencilla: sólo permita a un subproceso a la vez para modificar/acceder al recurso y listo. Pero si el acceso tarda mucho tiempo, se crea un cuello de botella que podría eliminar la ventaja de tener muchos hilos trabajando en paralelo. O, si un hilo bloquea el acceso a un recurso mientras espera el acceso a otro recurso y el segundo hilo bloquea el acceso al segundo recurso mientras espera el acceso al primero, crea un problema llamado punto muerto (**deadlock**). Estos son dos ejemplos muy simples de los posibles desafíos que un programador encuentra mientras usa múltiples hilos.

Primero, reproduciremos un problema causado por la modificación concurrente del mismo valor. Creemos una interfaz de `Calculator`:

```Java
interface Calculator {
        String getDescription();
        double calculate(int i);
    }
```

Usaremos el método `getDescription() `para capturar la descripción de la implementación. Aquí está la primera implementación:

```Java
private static class PrivateCalculatorNoSync implements Calculator {
        private double prop;
        private String description = "Without synchronization";

        public String getDescription() {
            return description;
        }

        public double calculate(int i) {
            try {
                this.prop = 2.0 * i;
                TimeUnit.MICROSECONDS.sleep(i);
                return Math.sqrt(this.prop);
            }
            catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                System.out.println("Calculator was interrupted");
            }

            return 0.0;
        }
    }
```

Como puede ver, el método `Calculate()` asigna un nuevo valor a la propiedad `prop`, luego hace algo más (lo simulamos llamando al método `sleep()`) y luego calcula la raíz cuadrada del valor asignado a la propiedad `prop`. La descripción "Without synchronization" describe el hecho de que el valor de la propiedad `prop` cambia cada vez que se llama al método `Calculate()`, sin ninguna coordinación o sincronización, como se llama en el caso de la coordinación entre hilos cuando modifican simultáneamente el mismo recurso.

Ahora vamos a compartir este objeto entre dos subprocesos, lo que significa que la propiedad `prop` se actualizará y usará simultáneamente. Por lo tanto, es necesario algún tipo de sincronización de subprocesos alrededor de la propiedad `prop`, pero hemos decidido que nuestra primera implementación no lo hace.

El siguiente es el método que vamos a usar al ejecutar cada implementación de `Calculator` que vamos a crear:

```Java
private static void invokeAllCallables(Calculator c) {
        System.out.println("\n" + c.getDescription() + ":");

        ExecutorService pool = Executors.newFixedThreadPool(2);
        List<Callable<MyPrivateResult>> tasks = List.of(new MyCallable("One", c), new MyCallable("Two", c));

        try {
            List<Future<MyPrivateResult>> futures = pool.invokeAll(tasks);
            List<MyPrivateResult> results = new ArrayList<>();
            
            while(results.size() < futures.size()) {
                TimeUnit.MILLISECONDS.sleep(5);
                for(Future future: futures) {
                    if (future.isDone()) {
                        results.add((MyPrivateResult) future.get());
                    }
                }
            }
            for (MyPrivateResult result: results) {
                System.out.println(result);
            }
        }
        catch(Exception e) {
            e.printStackTrace();
        }
        finally {
            shutdownAndTerminate(pool);
        }

    }
```

Como puede ver, el método anterior hace lo siguiente:

  > * Imprime la descripción de la implementación de `Calculator` pasada.
  > * Crea un grupo de tamaño fijo para dos subprocesos.
  > * Crea una lista de dos tareas `Callables`: los objetos de la siguiente clase `MyCallable`:

```Java
private static class MyCallable implements Callable<MyPrivateResult> {
        private String name;
        private Calculator calculator;

        public MyCallable(String name, Calculator calculator) {
            this.name = name;
            this.calculator = calculator;
        }

        public MyPrivateResult call() {
            double sum = 0.0;
            for (int i = 1; i < 20; i++) {
                sum += calculator.calculate(i);
            }

            return new MyPrivateResult(name, sum);
        }
    }
```

  > * La lista de tareas se pasa al método `invokeAll()` del grupo, donde cada una de las tareas se ejecuta invocando el método `call()`; cada método `call()` aplica el método `calculate()` del objeto `Calculator` pasado a cada uno de los 19 números del 1 al 20 y suma los resultados; la suma resultante se devuelve dentro del objeto `MyPrivateResult` junto con el nombre del objeto `MyCallable`.
  > * Cada objeto `MyPrivateResult` finalmente se devuelve dentro de un objeto `Future`.
  > * El método `invokeAllCallables()` itera sobre la lista de objetos `Future` y verifica cada uno de ellos si la tarea se ha completado; cuando se completa una tarea, el resultado se agrega a los resultados de `List<MyPrivateResult>`.
  > * Una vez completadas todas las tareas, el método `invokeAllCallables()` imprime todos los elementos de los resultados de `List<Result>` y finaliza el grupo.

Aquí está el resultado que obtenemos de una de nuestras ejecuciones de `invokeAllCalables<new CalculatorNoSync())`:

```
$ java -cp . U3_Concurrencia.ejemplos.Synchronization

Without synchronization:
MyPrivateResult{name=One, result=87.89284989139342}
MyPrivateResult{name=Two, result=78.46155022137644}
Waiting all threads completion for 100 MILLISECONDS ... 
isTerminated()=false
Calling shutdownNow()...
0 threads running
Exiting
```

Los números reales son ligeramente diferentes cada vez que ejecutamos el código anterior, pero el resultado de la tarea `One` nunca es igual al resultado de la tarea `Two`. Esto se debe a que, en el período entre establecer el valor del campo `prop` y devolver su raíz cuadrada en el método `calculate ()`, el otro hilo logró asignar un valor diferente a `prop`. Este es un caso de **interferencia de hilo**.

Hay varias formas de abordar este problema. Comenzamos con una variable atómica como la forma de lograr acceso concurrente seguro para subprocesos a una propiedad. Luego también demostraremos dos métodos de sincronización de subprocesos.

## Variable atómica

Una **variable atómica** es una que puede actualizarse solo cuando su valor actual coincide con el esperado. En nuestro caso, significa que un valor de `prop` no debe usarse si otro hilo lo ha cambiado.

El paquete `java.util.concurrent.atomic` tiene una docena de clases que admiten esta lógica: `AtomicBoolean`, `AtomicInteger`, `AtomicReference` y `AtomicIntegerArray`, por nombrar algunas. Cada una de estas clases tiene muchos métodos que pueden usarse para diferentes necesidades de sincronización. Revisa la [documentación de la API](https://docs.oracle.com/en/java/javase/12/docs/api/java.base/java/util/concurrent/atomic/package-summary.html) para cada una de las clases. Para la demostración, usaremos sólo dos métodos presentes en ellas:

  > * `V get()`: Returns the current value.
  > * `boolean compareAndSet(V expectedValue, V newValue)`: Establece el valor en `newValue` si el valor actual es igual (==) al Valor esperado (`expectedValue`); devuelve `true` si tiene éxito o `false` si el valor real no fue igual al valor esperado.

Así es como se puede usar la clase `AtomicReference` para resolver el problema de la interferencia de los hilos mientras se accede a la propiedad `prop` del objeto `Calculator` simultáneamente usando estos dos métodos:

```Java
private static class CalculatorAtomicRef implements Calculator {
        private AtomicReference<Double> prop = new AtomicReference<>(0.0);
        private String description = "Using AtomicReference";
        
        public String getDescription() {
            return description;
        }

        public double calculate(int i) {
            try {
                Double currentValue = prop.get();
                TimeUnit.MILLISECONDS.sleep(i);
                boolean b = this.prop.compareAndSet(currentValue, 2.0 * i);
                // System.out.println(b);   // Prints: true for one thread
                                            // and false for another thread
                return Math.sqrt(this.prop.get());
            }
            catch(InterruptedException e) {
                Thread.currentThread().interrupt();
                System.out.println("Calculator was interrupted");
            }
            return 0.0;
        }
    }
```

Como puede ver, el código anterior se asegura de que el valor actual de la propiedad `prop` no cambie mientras el subproceso estaba inactivo. La siguiente es el resultado de los mensajes producidos cuando ejecutamos `invokeAllCallables(new CalculatorAtomicRef())`:

```
$ java -cp . U3_Concurrencia.ejemplos.Synchronization

Using AtomicReference:
MyPrivateResult{name=One, result=80.88430683757149}
MyPrivateResult{name=Two, result=80.88430683757149}
Waiting all threads completion for 100 MILLISECONDS ... 
isTerminated()=false
Calling shutdownNow()...
0 threads running
Exiting
```

Ahora los resultados producidos por los hilos son los mismos.

Las siguientes clases del paquete `java.util.concurrent` también proporcionan soporte de sincronización:

  > * `Semaphore`: Restringe el número de hilos que pueden acceder al recurso.
  > * `CountDownLatch`: Permite que uno o más subprocesos esperen hasta que se complete un conjunto de operaciones que se realizan en otros subprocesos.
  > * `CyclicBarrier`: Permite que un conjunto de hilos esperen el uno al otro para alcanzar un punto de barrera común
  > * `Phaser`: Proporciona una forma más flexible de barrera que puede usarse para controlar el cálculo por fases entre múltiples hilos.
  > * `Exchanger`: Permite que dos hilos intercambien objetos en un punto de encuentro y es útil en varios diseños de tuberías.

## Método sincronizado

Otra forma de resolver el problema es usar un método sincronizado. Aquí hay otra implementación de la interfaz `Calculator` que usa este método para resolver la interferencia de hilos:

```Java
private static class CalculatorSyncMethod implements Calculator {

        private double prop;
        private String description = "Using synchronized method";

        public String getDescription() {
            return description;
        }

        synchronized public double calculate(int i) {
            try {
                this.prop = 2.0 * i;
                TimeUnit.MILLISECONDS.sleep(i);
                return Math.sqrt(this.prop);
            }
            catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                System.out.println("Calculator was interrupted");
            }

            return 0.0;
        }
    }
```

Acabamos de agregar la palabra clave `synchronized` al método `calculate()`. Ahora, si ejecutamos `invokeAllCallables(new CalculatorSyncMethod())`, los resultados de ambos hilos siempre serán los mismos:

```
$ java -cp . U3_Concurrencia.ejemplos.Synchronization 

Using synchronized method:
MyPrivateResult{name=One, result=80.88430683757149}
MyPrivateResult{name=Two, result=80.88430683757149}
Waiting all threads completion for 100 MILLISECONDS ... 
isTerminated()=false
Calling shutdownNow()...
0 threads running
Exiting
```

Esto se debe a que otro hilo no puede ingresar al método sincronizado hasta que el hilo actual (el que ya ha ingresado al método) haya salido. Esta es probablemente la solución más simple, pero este enfoque puede causar una degradación del rendimiento si el método tarda mucho en ejecutarse. En tales casos, se puede usar un bloque sincronizado, que envuelve solo varias líneas de código en una operación atómica.

## Bloque sincronizado

Aquí hay un ejemplo de un bloque sincronizado utilizado para resolver el problema de la interferencia de los hilos:

```Java
private static class CalculatorSyncBlock implements Calculator {
        private double prop;
        private String description = "Using synchronized block";

        public String getDescription() {
            return description;
        }

        public double calculate(int i) {
            try {
                // there maybe some other code here
                synchronized (this) {
                    this.prop = 2.0 * i;
                    TimeUnit.MILLISECONDS.sleep(i);
                    return Math.sqrt(this.prop);
                }
            }
            catch(InterruptedException e) {
                Thread.currentThread().interrupt();
                System.out.println("Calculator was interrupted");
            }
            return 0.0;
        }
    }
```

Como puede ver, el bloque sincronizado adquiere un bloqueo en este objeto, que es compartido por ambos hilos, y lo libera solo después de que los hilos salen del bloque. En nuestro código de demostración, el bloque cubre todo el código del método, por lo que no hay diferencia en el rendimiento. Pero imagine que hay más código en el método (comentamos `// there maybe some other code here`). Si ese es el caso, la sección sincronizada del código es más pequeña, por lo que tiene menos posibilidades de convertirse en un cuello de botella.

El resultado de ejecutar el código anterior es:

```
$ java -cp . U3_Concurrencia/ejemplos/Synchronization

Using synchronized block:
MyPrivateResult{name=One, result=80.88430683757149}
MyPrivateResult{name=Two, result=80.88430683757149}
Waiting all threads completion for 100 MILLISECONDS ... 
isTerminated()=false
Calling shutdownNow()...
0 threads running
Exiting
```

Como puede ver, los resultados son exactamente los mismos que en los dos ejemplos anteriores. En el paquete `java.util.concurrent.locks` se ensamblan diferentes tipos de bloqueos para diferentes necesidades y con un comportamiento diferente.

Cada objeto en Java hereda los métodos `wait()`, `notify()` y `notifyAll()` del objeto base. Estos métodos también se pueden usar para controlar el comportamiento de los hilos y su acceso a los bloqueos.


## `Collections` concurrentes

Otra forma de abordar la concurrencia es utilizar una colección segura para subprocesos del paquete `java.util.concurrent`. Antes de seleccionar qué colección usar, lea el [Javadoc](https://docs.oracle.com/en/java/javase/12/docs/api/index.html) para ver si las limitaciones de la colección son aceptables para su solicitud. Aquí está la lista de estas colecciones y algunas recomendaciones:

  > * `ConcurrentHashMap<K,V>`: Admite concurrencia total de recuperaciones y concurrencia altamente esperada para actualizaciones; utilícelo cuando los requisitos de concurrencia sean muy exigentes y necesite permitir el bloqueo en la operación de escritura pero no necesite bloquear el elemento.
  > * `ConcurrentLinkedQueue<E>`: Una cola segura para subprocesos basada en nodos enlazados; emplea un algoritmo eficiente sin bloqueo.
  > * `ConcurrentLinkedDeque<E>`: Una cola concurrente basada en nodos enlazados; `ConcurrentLinkedQueque` y `ConcurrentLinkedDeque` son una opción apropiada cuando muchos subprocesos que comparten el acceso a una colección común.
  > * `ConcurrentSkipListMap<K,V>`: Una implementación de la interfaz `ConcurrentNavigableMap`.
  > * `ConcurrentSkipListSet<E>`: Una implementación concurrente de `NavigableSet` basada en `ConcurrentSkipListMap`. Las clases `ConcurrentSkipListSet` y `ConcurrentSkipListMap`, según el Javadoc, *proporcionan el promedio de tiempo de registro log(n) para las operaciones de contener, agregar y eliminar y sus variantes. Las vistas ordenadas ascendentes y sus iteradores son más rápidas que las descendentes*; úselas cuando necesite iterar rápidamente a través de los elementos en un cierto orden.
  > * `CopyOnWriteArrayList<E>`: Una variante segura de `ArrayList` en la que todas las operaciones mutativas (agregar, establecer, etc.) se implementan haciendo una copia nueva del array subyacente; Según el Javadoc, *la clase `CopyOnWriteArrayList` es normalmente demasiado costosa, pero puede ser más eficiente que las alternativas cuando las operaciones transversales superan ampliamente las mutaciones, y es útil cuando no puede o no desea sincronizar los recorridos, pero necesita evitar la interferencia entre hilos concurrentes*; utilícelo cuando no necesite agregar nuevos elementos en diferentes posiciones y no necesite ordenarlos; de lo contrario, use `ConcurrentSkipListSet`.
  > * `CopyOnWriteArraySet<E>`: Un conjunto que usa una clase `CopyOnWriteArrayList` interna para todas sus operaciones.
  > * `PriorityBlockingQueue`: Es una mejor opción cuando un orden natural es aceptable y necesita una rápida adición de elementos a la cola y una rápida eliminación de elementos de la cabecera de la cola; el bloqueo significa que la cola espera volverse no vacía al recuperar un elemento y espera que haya espacio disponible en la cola cuando se almacena un elemento.
  > * `ArrayBlockingQueue`, `LinkedBlockingQueue`, and `LinkedBlockingDeque` tener un tamaño fijo (acotado); Las otras colas son ilimitadas.

Utilice estas y otras características y recomendaciones similares a las de las guias, pero ejecute pruebas exhaustivas y mediciones de rendimiento antes y después de implementar su funcionalidad. Para demostrar algunas de estas capacidades de colecciones, usemos `CopyOnWriteArrayList <E>`. Primero, veamos cómo se comporta una ArrayList cuando intentamos modificarla simultáneamente:

```Java
public static void modifyList() {
        System.out.println("\nmodifyList():");
        List<String> list = Arrays.asList("One", "Two");
        System.out.println(list);

        try {
            for (String e: list) {
                System.out.println(e);
                list.add("Three");
            }
        }
        catch(Exception e) {
            e.printStackTrace();
        }
        System.out.println(list);
    }
```

Como se esperaba, el intento de modificar una lista mientras se itera genera una excepción y la lista permanece sin modificaciones.

```
$ java -cp . U3_Concurrencia.ejemplos.ConcurrentCollections

modifyList():
[One, Two]
One
java.lang.UnsupportedOperationException
        at java.base/java.util.AbstractList.add(AbstractList.java:153)
        at java.base/java.util.AbstractList.add(AbstractList.java:111)
        at U3_Concurrencia.ejemplos.ConcurrentCollections.modifyList(ConcurrentCollections.java:20)
        at U3_Concurrencia.ejemplos.ConcurrentCollections.main(ConcurrentCollections.java:9)
[One, Two]
```

Ahora, usemos `CopyOnWriteArrayList <E>` en las mismas circunstancias:

```Java
public static void modifyCopyOnWriteArrayList() {
        System.out.println("\nmodifyCopyOnWriteArrayList()");

        List<String> list = new CopyOnWriteArrayList<>(Arrays.asList("One", "Two"));
        System.out.println(list);

        try {
            for (String e: list) {
                System.out.print(e + " ");
                list.add("Three");
            }
        }
        catch(Exception e) {
            e.printStackTrace();
        }
        System.out.println("\n" + list);
    }
```

El resultado del código anterior es el siguiente:

```
$ java -cp . U3_Concurrencia.ejemplos.ConcurrentCollections                                     [23:24:55]

modifyCopyOnWriteArrayList()
[One, Two]
One Two 
[One, Two, Three, Three]
```

Como puede ver, la lista se modificó sin excepción, pero no la copia iterada actualmente. Ese es el comportamiento que puede usar si es necesario.

## Error de consistencia de direccionamiento de memoria

Los errores de coherencia de memoria pueden tener muchas formas y causas en un entorno multiproceso. Se discuten bien en el Javadoc del paquete `java.util.concurrent`. Aquí, mencionaremos solo el caso más común, que es causado por la falta de visibilidad.

Cuando un subproceso cambia el valor de una propiedad, el otro no puede ver el cambio inmediatamente y no puede usar la palabra clave `synchronized` para un tipo primitivo. En tal situación, considere usar la palabra clave `volatile` para la propiedad; garantiza su visibilidad de lectura/escritura entre diferentes hilos.

Los problemas de concurrencia no son fáciles de resolver. Es por eso que no es sorprendente que cada vez más desarrolladores adopten un enfoque más radical. En lugar de administrar un estado de objeto, prefieren procesar datos en un conjunto de operaciones sin estado. Parece que Java y muchos lenguajes y sistemas modernos están evolucionando en esta dirección.
