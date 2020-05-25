# Programación Para Red

## Protocolos de Internet

La *Programación para la red* es un área vasta. El conjunto de **protocolos de Internet** (IP) consta de cuatro capas, cada una de las cuales tiene una docena o más de protocolos:

  > * **La capa de enlace**: el grupo de protocolos utilizados cuando un cliente está físicamente conectado al host; tres protocolos principales incluyen el P**rotocolo de resolución de dirección (ARP)**, el **Protocolo de resolución de dirección inversa (RARP)** y el **Protocolo de descubrimiento de vecinos (NDP)**.
  > * **La capa de Internet**: el grupo de métodos, protocolos y especificaciones entre redes utilizados para transportar paquetes de red desde el host de origen al host de destino, especificado por una dirección IP. Los protocolos principales de esta capa son el **Protocolo de Internet versión 4 (IPv4)**y el **Protocolo de Internet versión 6 (IPv6)**; IPv6 especifica un nuevo formato de paquete y asigna 128 bits para la dirección IP punteada, en comparación con 32 bits en IPv4. Un ejemplo de una dirección IPv4 es `10011010.00010111.11111110.00010001`, lo que da como resultado una dirección IP de `154.23.254.17`.
  > * **La capa de transporte**: el grupo de servicios de comunicación de host a host. Incluye TCP, también conocido como protocolo TCP / IP, y UDP; Otros protocolos en este grupo son el **Protocolo de control de congestión de datagramas (DCCP)** y el **Protocolo de transmisión de control de flujo (SCTP)**.
  > * **La capa de aplicación**: el grupo de protocolos y métodos de interfaz utilizados por los hosts en una red de comunicación. Incluye **Telnet, Protocolo de transferencia de archivos (FTP), Sistema de nombres de dominio (DNS), Protocolo simple de transferencia de correo (SMTP), Protocolo ligero de acceso a directorios (LDAP), Protocolo de transferencia de hipertexto (HTTP), Protocolo seguro de transferencia de hipertexto (HTTPS) y Secure Shell (SSH)**.

La capa de enlace es la capa más baja; es utilizada por la capa de internet que, a su vez, es utilizada por la capa de transporte. Esta capa de transporte luego es utilizada por la capa de aplicación en apoyo de las implementaciones de protocolo.

Por razones de seguridad, Java no proporciona acceso a los protocolos de la capa de enlace y la capa de Internet. Esto significa que Java no permite crear protocolos de transporte personalizados que, por ejemplo, sirvan como alternativa a TCP / IP. Es por eso que revisaremos solo los protocolos de la capa de transporte (TCP y UDP) y la capa de aplicación (HTTP). Se explicará y demostrará cómo Java los admite y cómo una aplicación Java puede aprovechar este soporte.

Java admite los protocolos TCP y UDP con clases del paquete `java.net`, mientras que el protocolo HTTP se puede implementar en la aplicación Java utilizando las clases del paquete `java.net.http` (que se introdujo con Java 11).

Tanto los protocolos TCP como UDP pueden implementarse en Java utilizando sockets. Los sockets se identifican mediante una combinación de una dirección IP y un número de puerto, y representan una conexión entre dos aplicaciones. Como el protocolo UDP es algo más simple que el protocolo TCP, comenzaremos con UDP.

## Comunicación basada en UDP

El protocolo UDP fue diseñado por David P. Reed en 1980. Permite que las aplicaciones envíen mensajes llamados datagramas utilizando un modelo de comunicación simple sin conexión con un mecanismo de protocolo mínimo, como una suma de verificación, para la integridad de los datos. No tiene diálogos de "apretón de manos" y, por lo tanto, no garantiza la entrega de mensajes ni preserva el orden de los mensajes. Es adecuado para aquellos casos en los que se prefieren dejar mensajes o mezclar pedidos que esperar la retransmisión.

Un datagrama está representado por la clase `java.net.DatagramPacket`. Se puede crear un objeto de esta clase utilizando uno de los seis constructores; Los siguientes dos constructores son los más utilizados:

  > * `DatagramPacket(byte[] buffer, int length)`: Este constructor crea un paquete de datagramas y se usa para recibir los paquetes; el búfer contiene el datagrama entrante, mientras que la longitud es el número de bytes a leer.
  > * `DatagramPacket(byte[] buffer, int length, InetAddress address, int port)`: Esto crea un paquete de datagramas y se usa para enviar los paquetes; el búfer contiene los datos del paquete, la longitud es la longitud del paquete de datos, la dirección contiene la dirección IP de destino y el puerto es el número de puerto de destino.

Una vez construido el objeto `DatagramPacket`, éste expone los siguientes métodos que pueden usarse para extraer datos del objeto o establecer / obtener sus propiedades:

  > * `void setAddress(InetAddress iaddr)`: Establece la dirección IP de destino.
  > * `InetAddress getAddress()`: Esto devuelve el destino o la dirección IP de origen.
  > * `void setData(byte[] buf)`: Establece el buffer de datos
  > * `void setData(byte[] buf, int offset, int length)`: Establece el buffer de datos, el despazamiento y la longitud.
  > * `void setLength(int length)`: Establece la longitud de paquete
  > * `byte[] getData()`: Retorna el buffer de datos
  > * `int getLength()`: Retorna la longitud del paquete que esta siendo enviado o recibido.
  > * `int getOffset()`: Esto devuelve el desplazamiento de los datos que se enviarán o recibirán.
  > * `void setPort(int port)`: Establece el número de puerto de destino
  > * `int getPort()`: Retorna el número de puerto por el cual se recibirán o enviarán los datos.

Una vez que se crea un objeto `DatagramPacket`, se puede enviar o recibir utilizando la clase `DatagramSocket`, que representa un socket sin conexión para enviar y recibir paquetes de datagramas. Se puede crear un objeto de esta clase utilizando uno de los seis constructores; Los siguientes tres constructores son los más utilizados:

  > * `DatagramSocket()`: Este crea un socket de datagrama y lo vincula a cualquier puerto disponible en la máquina host local. Por lo general, se usa para crear un socket de envío porque la dirección de destino (y el puerto) se pueden establecer dentro del paquete (consulte los constructores y métodos anteriores de `DatagramPacket`).
  > * `DatagramSocket(int port)`: Esto crea un socket de datagrama y lo vincula al puerto especificado en la máquina host local. Se utiliza para crear un socket de recepción cuando cualquier dirección de máquina local (llamada dirección comodín) es lo suficientemente buena.
  > * `DatagramSocket(int port, InetAddress address)`: Esto crea un socket de datagrama y lo vincula al puerto especificado y la dirección local especificada; el puerto local debe estar entre 0 y 65535. Se utiliza para crear un socket receptor cuando se necesita vincular una dirección de máquina local en particular.

Los siguientes dos métodos del objeto `DatagramSocket` son los más utilizados para enviar y recibir mensajes (o paquetes):

  > * `void send(DatagramPacket p)`: Envía el paquete especificado
  > * `void receive(DatagramPacket p)`: Este recibe un paquete al llenar el búfer del objeto DatagramPacket especificado con los datos recibidos. El objeto `DatagramPacket` especificado también contiene la dirección IP del remitente y el número de puerto en la máquina del remitente.

  Echemos un vistazo a un ejemplo de código; Aquí está el receptor de mensajes UDP que sale después de que el mensaje ha sido recibido:

  ```Java
package U4_ProgramaciónParaRed.ejemplos;

import java.net.DatagramPacket;
import java.net.DatagramSocket;

public class UDPReceiver {

    public static void main(String[] args) {
        try (DatagramSocket ds = new DatagramSocket(3333)) {
            DatagramPacket dp = new DatagramPacket(new byte[16], 16);
            ds.receive(dp);
            for(byte b: dp.getData()){
                System.out.print(Character.toString(b));
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }
}
```

Como puede ver, el receptor está escuchando un mensaje de texto (interpreta cada byte como un carácter) en cualquier dirección de la máquina local en el puerto 3333. Utiliza un búfer de 16 bytes solamente; Tan pronto como el búfer se llena con los datos recibidos, el receptor imprime su contenido y sale.

Aquí hay un ejemplo del remitente del mensaje UDP:

```Java
package U4_ProgramaciónParaRed.ejemplos;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

public class UDPSender {

    public static void main(String[] args) {
        try (DatagramSocket ds = new DatagramSocket()) {
            String msg = "Hi, there! How are you?";
            InetAddress address = InetAddress.getByName("127.0.0.1");
            DatagramPacket dp = new DatagramPacket(msg.getBytes(), msg.length(), address, 3333);
            ds.setSendBufferSize(100);
            ds.send(dp);
        }
        catch(Exception e) {
            e.printStackTrace();
        }
    }    
}
```

Como puede ver, el remitente construye un paquete con el mensaje, la dirección de la máquina local y el mismo puerto que el que usa el receptor. Después de enviar el paquete construido, el remitente sale.

Podemos ejecutar el remitente ahora, pero sin el receptor ejecutándose no hay nadie para recibir el mensaje. Entonces, comenzaremos el receptor primero. Escucha en el puerto 3333, pero no llega ningún mensaje, por lo que espera. Luego, ejecutamos el remitente y el receptor muestra el siguiente mensaje:

```
Hi, there! How a
```

Como el búfer es más pequeño que el mensaje, solo se recibió parcialmente; el resto del mensaje se pierde. Podemos crear un bucle infinito y dejar que el receptor se ejecute indefinidamente:

```Java
while(true){
    ds.receive(dp);
    for(byte b: dp.getData()){
        System.out.print(Character.toString(b));
    }
    System.out.println();
}
```

Al hacerlo, podemos ejecutar el remitente varias veces; Esto es lo que imprime el receptor si ejecutamos al remitente tres veces:

```
Hi, there! How a
Hi, there! How a
Hi, there! How a
```

Como puede ver, se reciben los tres mensajes; sin embargo, solo los primeros 16 bytes de cada mensaje son capturados por el receptor.

Ahora hagamos que el búfer receptor sea más grande que el mensaje:

```Java
DatagramPacket dp = new DatagramPacket(new byte[30], 30);
```

Si enviamos el mismo mensaje ahora, el resultado será el siguiente:

```
Hi, there! How are you?
```

Para evitar el procesamiento de elementos de búfer vacíos, puede usar el método `getLength()` de la clase `DatagramPacket`, que devuelve el número real de elementos del búfer con el mensaje:

```Java
int i = 1;
for(byte b: dp.getData()){
    System.out.print(Character.toString(b));
    if(i++ == dp.getLength()){
        break;
    }
}
```

Entonces, esta es la idea básica del protocolo UDP. El remitente envía un mensaje a una determinada dirección y puerto, incluso si no hay un socket que escuche en esta dirección y puerto. No requiere establecer ningún tipo de conexión antes de enviar el mensaje, lo que hace que el protocolo UDP sea más rápido y más liviano que el protocolo TCP (que requiere que primero establezca la conexión). De esta forma, el protocolo TCP lleva el envío de mensajes a otro nivel de confiabilidad, asegurándose de que el destino exista y que el mensaje pueda ser entregado.

---

## Comunicación basada en TCP

TCP fue diseñado por la Agencia de Proyectos de Investigación Avanzada de Defensa (DARPA) en la década de 1970 para su uso en la Red de la Agencia de Proyectos de Investigación Avanzada (ARPANET). Complementa al protocolo IP y, por lo tanto, también se conoce como **TCP/IP**. El protocolo TCP, incluso por su nombre, indica que proporciona una transmisión de datos confiable (es decir, con verificación de errores o controlada). Permite la entrega ordenada de bytes en una red IP y es ampliamente utilizado por la web, correo electrónico, shell seguro y transferencia de archivos.

Una aplicación que utiliza TCP/IP ni siquiera es consciente de todos los "apretones de manos" que se producen entre el socket y los detalles de la transmisión, como la congestión de la red, el equilibrio de la carga del tráfico, la duplicación e incluso la pérdida de algunos paquetes IP. La implementación del protocolo subyacente de la capa de transporte detecta estos problemas, reenvía los datos, reconstruye el orden de los paquetes enviados y minimiza la congestión de la red.

A diferencia del protocolo UDP, la comunicación basada en TCP / IP se centra en la entrega precisa a expensas del período de entrega. Es por eso que no se usa para aplicaciones en tiempo real, como voz sobre IP, donde se requiere una entrega confiable y un orden secuencial correcto. Sin embargo, si cada bit necesita llegar exactamente como se envió y en la misma secuencia, entonces TCP / IP es insustituible.

Para admitir este comportamiento, la comunicación TCP / IP mantiene una sesión durante toda la comunicación. La sesión se identifica por la dirección del cliente y el puerto. Cada sesión está representada por una entrada en una tabla en el servidor. Contiene todos los metadatos sobre la sesión: la dirección IP y el puerto del cliente, el estado de la conexión y los parámetros del búfer. Pero estos detalles generalmente están ocultos para el desarrollador de la aplicación, por lo que no entraremos en más detalles aquí. En cambio, pasaremos al código Java.

Similar al protocolo UDP, la implementación del protocolo TCP / IP en Java usa sockets. Pero en lugar de la clase `java.net.DatagramSocket` que implementa el protocolo UDP, los sockets basados ​​en TCP / IP están representados por las clases `java.net.ServerSocket` y `java.net.Socket`. Permiten enviar y recibir mensajes entre dos aplicaciones, una de ellas es un servidor y la otra un cliente.

Las clases `ServerSocket` y `SocketClass` realizan trabajos muy similares. La única diferencia es que la clase `ServerSocket` tiene el método `accept()` que acepta la solicitud del cliente. Esto significa que el servidor debe estar listo para recibir la solicitud primero. Luego, la conexión es iniciada por el cliente que crea su propio socket que envía la solicitud de conexión (desde el constructor de la clase `Socket`). El servidor acepta la solicitud y crea un socket local conectado al socket remoto (en el lado del cliente).

Después de establecer la conexión, la transmisión de datos puede ocurrir usando flujos de E/S, Cadenas, Entrada / Salida y Archivos. El objeto `Socket` tiene los métodos `getOutputStream()` y `getInputStream()` que proporcionan acceso a las secuencias de datos del socket. Los datos del objeto `java.io.OutputStream` en la computadora local aparecen como provenientes del objeto `java.io.InputStream` en la máquina remota.

Echemos un vistazo más de cerca a las clases `java.net.ServerSocket` y `java.net.Socket`, y luego ejecutemos algunos ejemplos de su uso.

### La clase `java.net.ServerSocket`

La clase `java.net.ServerSocket` tiene cuatro constructores:
  
  > * `ServerSocket ()`: crea un objeto de socket de servidor que no está vinculado a una dirección y puerto en particular. Requiere el uso del método `bind()` para vincular el socket.
  > * `ServerSocket (int port)`: esto crea un objeto de socket de servidor vinculado al puerto proporcionado. El valor del puerto debe estar entre 0 y 65535. Si el número de puerto se especifica como un valor de 0, esto significa que el número de puerto debe vincularse automáticamente. Por defecto, la longitud máxima de la cola para las conexiones entrantes es de 50.
  > * `ServerSocket (int port, int backlog)`: proporciona la misma funcionalidad que el constructor `ServerSocket (int port)` y le permite establecer la longitud máxima de la cola para las conexiones entrantes mediante el parámetro de backlog.
  > * `ServerSocket (int port, int backlog, InetAddress bindAddr)`: esto crea un objeto de socket de servidor que es similar al constructor anterior, pero también vinculado a la dirección IP proporcionada. Cuando el valor de `bindAddr` es nulo, por defecto aceptará conexiones en cualquiera o en todas las direcciones locales.

Los siguientes cuatro métodos de la clase ServerSocket son los más utilizados y son esenciales para establecer una conexión de socket:

enlace vacío (punto final SocketAddress): Esto une el objeto `ServerSocket` a una dirección IP y puerto específicos. Si la dirección proporcionada es nula, el sistema seleccionará automáticamente un puerto y una dirección local válida (que luego se puede recuperar utilizando los métodos `getLocalPort ()`, `getLocalSocketAddress ()` y `getInetAddress ()`). Además, si el constructor creó el objeto `ServerSocket` sin ningún parámetro, entonces este método, o el siguiente método `bind ()`, debe invocarse antes de que se pueda establecer una conexión.
> * `void bind (punto final SocketAddress, int backlog)`: esto actúa de manera similar al método anterior; El argumento de la acumulación es el número máximo de conexiones pendientes en el socket (es decir, el tamaño de la cola). Si el valor de la acumulación es menor o igual a 0, se utilizará un valor predeterminado específico de la implementación.
> * `void setSoTimeout (int timeout)`: establece el valor (en milisegundos) de cuánto tiempo espera el socket un cliente después de que se llama al método `accept ()`. Si el cliente no ha llamado y el tiempo de espera expira, se emite una excepción `java.net.SocketTimeoutException`, pero el objeto `ServerSocket` sigue siendo válido y puede reutilizarse. El valor de tiempo de espera de 0 se interpreta como un tiempo de espera infinito (el método `accept ()` bloquea hasta que un cliente llama).
> * `Socket accept ()`: bloquea hasta que un cliente llama o el tiempo de espera (si está configurado) expira.

Otros métodos de la clase le permiten establecer u obtener otras propiedades del objeto Socket y pueden usarse para una mejor gestión dinámica de la conexión del socket. Puede consultar la documentación en línea de la clase para comprender las opciones disponibles con más detalle.

El siguiente código es un ejemplo de implementación de un servidor utilizando la clase `ServerSocket`:

```Java
package U4_ProgramaciónParaRed.ejemplos;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;

public class TCPServer {

    public static void main(String[] args) {
        try (Socket s = new ServerSocket(3333).accept();
        DataInputStream dis = new DataInputStream(s.getInputStream());
        DataOutputStream dos = new DataOutputStream(s.getOutputStream());
        BufferedReader console = new BufferedReader(new InputStreamReader(System.in))) {
            while (true) {
                String msg = dis.readUTF();
                System.out.println("Client said: " + msg);
                if ("end".equalsIgnoreCase(msg)) {
                    break;
                }

                System.out.println("Say something: ");
                msg = console.readLine();
                dos.writeUTF(msg);
                dos.flush();

                if ("end".equalsIgnoreCase(msg)) {
                    break;
                }
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }
    
}
```

Veamos el código anterior. En la declaración de prueba con recursos, creamos objetos `Socket`, `DataInputStream` y `DataOutputStream` basados ​​en nuestro socket recién creado, y el objeto `BufferedReader` para leer la entrada del usuario desde la consola (la usaremos para ingresar los datos). Al crear el socket, el método `accept()` se bloquea hasta que un cliente intenta conectarse al puerto 3333 del servidor local. Entonces, el código entra en un bucle infinito. Primero, lee los bytes enviados por el cliente como una cadena de caracteres Unicode codificada en un formato UTF-8 modificado utilizando el método `readUTF()` de `DataInputStream`. El resultado se imprime con el prefijo `"Client said:"`. Si el mensaje recibido es una cadena de `"end"`, el código sale del bucle y el programa del servidor sale. Si el mensaje no es `"end"`, se muestra el mensaje `"Say something:"` en la consola y el método `readLine()` se bloquea hasta que un usuario escribe algo y hace clic en Entrar. El servidor toma la entrada de la pantalla y la escribe como una cadena de caracteres Unicode en la secuencia de salida utilizando el método `writeUTF()`. Como ya mencionamos, la secuencia de salida del servidor está conectada a la secuencia de entrada del cliente. Si el cliente lee de la secuencia de entrada, recibe el mensaje enviado por el servidor. Si el mensaje enviado es `"end"`, el servidor sale del bucle y del programa. Si no, entonces el cuerpo del bucle se ejecuta nuevamente.

El algoritmo descrito supone que el cliente sale solo cuando envía o recibe el mensaje `"end"`. De lo contrario, el cliente genera una excepción si intenta enviar un mensaje al servidor después. Esto demuestra la diferencia entre los protocolos UDP y TCP que ya mencionamos: TCP se basa en la sesión establecida entre el servidor y los sockets del cliente. Si un lado lo deja caer, el otro lado inmediatamente encuentra un error.

Ahora revisemos un ejemplo de implementación de cliente TCP.

## La Clase `java.net.Socket`

La clase `java.net.Socket ` ahora debería serle familiar, ya que se usó en el ejemplo anterior. Lo usamos para acceder a los flujos de entrada y salida de los sockets conectados. Ahora vamos a revisar la clase `Socket` sistemáticamente y explorar cómo se puede usar para crear un cliente TCP. La clase `Socket` tiene cuatro constructores:

  > * `Socket()`: esto crea un socket no conectado. Utiliza el método `connect()` para establecer una conexión de este socket con un socket en un servidor.
  > * `Socket(String host, int port)`: crea un socket y lo conecta al puerto provisto en el servidor host. Si arroja una excepción, no se establece la conexión con el servidor; de otra manera; puede comenzar a enviar datos al servidor.
  > * `Socket(dirección InetAddress, puerto int)`: esto actúa de manera similar al constructor anterior, excepto que el host se proporciona como un objeto `InetAddress`.
  > * `Socket (String host, int port, InetAddress localAddr, int localPort)`: funciona de manera similar al constructor anterior, excepto que también le permite vincular el socket a la dirección local y al puerto proporcionados (si el programa se ejecuta en una máquina con múltiples direcciones IP). Si el valor `localAddr` proporcionado es `null`, se selecciona cualquier dirección local. Alternativamente, si el valor `localPort` proporcionado es nulo, el sistema recoge un puerto libre en la operación de enlace.
  > * `Socket (dirección InetAddress, puerto int, InetAddress localAddr, int localPort)`: esto actúa de manera similar al constructor anterior, excepto que la dirección local se proporciona como un objeto `InetAddress`.

Estos son los dos métodos siguientes de la clase Socket que ya hemos usado:

  > * `InputStream getInputStream ()`: devuelve un objeto que representa la fuente (el socket remoto) y trae los datos (los ingresa) al programa (el socket local)
  > * `OutputStream getOutputStream ()`: devuelve un objeto que representa la fuente (el socket local) y envía los datos (los genera) a un socket remoto.

Ahora examinemos el código del cliente TCP, como sigue:

```Java
package U4_ProgramaciónParaRed.ejemplos;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.Socket;

public class TCPClient {
    
    public static void main(String[] args) {
        try (Socket s = new Socket("localhost", 3333);
        DataInputStream dis = new DataInputStream(s.getInputStream());
        DataOutputStream dos = new DataOutputStream(s.getOutputStream());
        BufferedReader console = new BufferedReader(new InputStreamReader(System.in)))
        {
            String prompt = "Say something";
            System.out.println(prompt);
            String msg;

            while ((msg = console.readLine()) != null) {
                dos.writeUTF(msg);
                dos.flush();
                if (msg.equalsIgnoreCase("end")) {
                    break;
                }

                msg = dis.readUTF();
                System.out.println("Server said: " + msg);
                if (msg.equalsIgnoreCase("end")) {
                    break;
                }
                System.out.println(prompt);
            }
        }
        catch(Exception e) {
            e.printStackTrace();
        }
    }
}
```

El código anterior de TCPClient se ve casi exactamente igual al código de TcpServer. La única diferencia principal es que el nuevo constructor `Socket ("localhost", 3333)` intenta establecer una conexión con el servidor *"localhost: 3333"* de inmediato, por lo que espera que el servidor localhost esté activo y escuchando en el puerto 3333; el resto es igual al código del servidor.

Por lo tanto, la única razón por la que necesitamos usar la clase `ServerSocket` es permitir que el servidor se ejecute mientras espera que el cliente se conecte a él; todo lo demás se puede hacer usando solo la clase `Socket`.

Otros métodos de la clase `Socket` le permiten establecer u obtener otras propiedades del objeto socket, y pueden usarse para una mejor gestión dinámica de la conexión del socket. Puede leer la documentación en línea de la clase para comprender las opciones disponibles con más detalle.

Ejecutemos ahora los programas `TcpServer` y `TcpClient`. Si iniciamos `TcpClient` primero, obtenemos `java.net.ConnectException` con el mensaje Conexión rechazada. Entonces, lanzamos el programa `TcpServer` primero. Cuando comienza, no se muestran mensajes, en cambio, solo espera hasta que el cliente se conecta. Entonces, iniciamos `TcpClient` y vemos el siguiente mensaje en la pantalla:

```
Say something
```

Escribimos "Hola", en el servidor se mostrará:

```
Client said: hola
```

Ahora, escribimos "¿como estas?", el cliente mostrará:

```
Server said: ¿como estas?
```

Finalmente, escribimos "end" y el proceso terminará, tanto de lado del cliente como el servidor.

## UDP vs TCP

Las diferencias entre los protocolos UDP y TCP/IP pueden ser listados a continuación:

  > * UDP simplemente envía datos, ya sea que el receptor de datos esté funcionando o no. Es por eso que UDP es más adecuado para enviar datos en comparación con muchos otros clientes que usan distribución de multidifusión. TCP, por otro lado, requiere establecer primero la conexión entre el cliente y el servidor. El cliente TCP envía un mensaje de control especial; el servidor lo recibe y responde con una confirmación. El cliente luego envía un mensaje al servidor que confirma la confirmación del servidor. Solo después de esto, es posible la transmisión de datos entre el cliente y el servidor.
  > * TCP garantiza la entrega de mensajes o genera un error, mientras que UDP no, y puede perderse un paquete de datagramas.
  > * TCP garantiza la preservación del orden de los mensajes en la entrega, mientras que UDP no. Como resultado de estas garantías proporcionadas, TCP es más lento que UDP. Además, los protocolos requieren que se envíen encabezados junto con el paquete. El tamaño del encabezado de un paquete TCP es de 20 bytes, mientras que un paquete de datagrama es de 8 bytes. El encabezado UDP contiene Longitud, Puerto de origen, Puerto de destino y Suma de verificación, mientras que el encabezado TCP contiene Número de secuencia, Número de reconocimiento, Compensación de datos, Reservado, Bit de control, Ventana, Puntero urgente, Opciones y Relleno, además de los encabezados UDP .
  > * Existen diferentes protocolos de aplicación que se basan en los protocolos TCP o UDP. Los protocolos basados ​​en TCP son HTTP, HTTPS, Telnet, FTP y SMTP. Los protocolos basados ​​en UDP son el Protocolo de configuración dinámica de host (DHCP), el DNS, el Protocolo simple de administración de redes (SNMP), el Protocolo trivial de transferencia de archivos (TFTP), el Protocolo Bootstrap (BOOTP) y las primeras versiones del Sistema de archivos de red (NFS).

## Comunicación basada en URL

Hoy en día, parece que todos tienen alguna noción de URL; aquellos que usan un navegador en sus computadoras o teléfonos inteligentes verán las URL todos los días. En esta sección, explicaremos brevemente las diferentes partes que componen una URL y demostraremos cómo se puede usar mediante programación para solicitar datos de un sitio web (o un archivo) o enviar (publicar) datos a un sitio web.

### Syntaxis de la URL

En términos generales, la sintaxis de URL cumple con la sintaxis de un **identificador uniforme de recursos** (URI) que tiene el siguiente formato:

```
scheme:[//authority]path[?query][#fragment]
```

Los corchetes indican que el componente es opcional. Esto significa que un URI consistirá en `scheme:path`. El componente del esquema puede ser `http`, `https`, `ftp`, `mailto`, `File`, `data` u otro valor. El componente `path` consiste en una secuencia de segmentos de ruta separados por una barra (/). Aquí hay un ejemplo de una URL que consta solo de esquema y ruta:

```
file:src/main/resources/hello.txt
```

La URL anterior apunta a un archivo en un sistema de archivos local que es relativo al directorio donde se usa esta URL. En breve demostraremos cómo funciona.

El componente `path` puede estar vacío, pero la URL parecería inútil. Sin embargo, una ruta vacía a menudo se usa junto con `authority`, que tiene el siguiente formato:

```
[userinfo@]host[:port]
```

El único componente de `authority` requerido es `host`, que puede ser una dirección IP (137.254.120.50, por ejemplo) o un nombre de dominio (oracle.com, por ejemplo).

El componente `userinfo` se usa típicamente con el valor `mailto` del componente del esquema, por lo que `userinfo@host` representa una dirección de correo electrónico.

El componente del puerto `port`, si se omite, asume un valor predeterminado. Por ejemplo, si el valor del esquema es `http`, entonces el valor del puerto predeterminado es 80, y si el valor del esquema es `https`, entonces el valor del puerto predeterminado es `443`.

Un componente de consulta opcional `query` de una URL es una secuencia de pares clave-valor separados por un delimitador (`&`):

```
key1=value1&key2=value2
```

Finalmente, el componente de fragmento opcional (`fragment`) es un identificador de una sección de un documento HTML, de modo que un navegador puede desplazar esta sección a la vista.

Es necesario mencionar que la documentación en línea de Oracle utiliza una terminología ligeramente diferente:

  > * `proticol` en lugar de `scheme`
  > * `reference` en lugar de `fragment`
  > * `file` en lugar de `path[?query][fragment]`
  > * `resource` en lugar de `host[:port]path[?query][#fragment]`

Entonces, desde la perspectiva de la documentación de Oracle, la URL se compone de valores de protocolo y recursos.

## La clase `java.net.URL`

En Java, una URL es representada por un objeto de la clase `java.net.URL` con los siguientes seis constructores:

  > * `URL(String spec)`: Esto crea un objeto URL a partir de la URL como una cadena.
  > * `URL(String protocol, String host, String file)`: Esto crea un objeto URL a partir de los valores proporcionados de `protocol`, `host` y `file` (`path` y `query`), y el número de puerto predeterminado basado en el valor de protocolo proporcionado (`protocol`).
  > * `URL(String protocol, String host, int port, String path)`: Esto crea un objeto de URL a partir de los valores proporcionados de `protocol`, `host` y `file` (`path` y `query`). Un valor de puerto de -1 indica que el número de puerto predeterminado debe usarse en función del valor de protocolo proporcionado.
  > * `URL(String protocol, String host, int port, String file, URLStreamHandler handler)`: actúa de la misma manera que el constructor anterior y además le permite pasar un objeto del controlador de protocolo particular; Todos los constructores anteriores cargan los controladores predeterminados automáticamente.
  > * `URL(URL context, String spec)`: Esto crea un objeto `URL` que extiende el objeto de `URL` proporcionado o anula sus componentes utilizando el valor de especificación proporcionado (`spec`), que es una representación de cadena de una URL o algunos de sus componentes. Por ejemplo, si el esquema está presente en ambos parámetros, el valor de `spec` anula el valor del esquema en `context` y muchos otros.
  > * `URL(URL context, String spec, URLStreamHandler handler)`: actúa de la misma manera que el constructor anterior y además le permite pasar un objeto del controlador de protocolo particular.

Una vez creado, un objeto `URL` le permite obtener los valores de varios componentes de la URL subyacente. El método `InputStream openStream()` proporciona acceso a la secuencia de datos recibidos de la URL. De hecho, se implementa como `openConnection.getInputStream()`. El método `URLConnection openConnection()` de la clase URL devuelve un objeto `URLConnection` con muchos métodos que proporcionan detalles sobre la conexión a la URL, incluido el método `getOutputStream()` que le permite enviar datos a la URL.

Echemos un vistazo al código de ejemplo; comenzamos leyendo datos de un archivo `hello.txt`:

```Java
private static void getFromFile() {
    try {
        URL url = new URL("file:U4_ProgramaciónParaRed/ejemplos/hello.txt");
        System.out.println(url.getPath());
        System.out.println(url.getFile());
        try (InputStream is = url.openStream()) {
            int data = is.read();
            while (data != -1) {
                System.out.println((char) data);
                data = is.read();
            }
        }
    }
    catch(Exception e) {
        e.printStackTrace();
    }
}
```

En el código anterior, utilizamos el archivo: `U4_ProgramaciónParaRed/ejemplos/hello.txt` en `URL`. Se basa en la ruta del archivo que es relativa a la ubicación de ejecución del programa. El programa se ejecuta en el directorio raíz de nuestro proyecto. Para comenzar, demostramos los métodos` getPath()` y `getFile()`. Los valores devueltos no son diferentes porque la URL no tiene un valor de componente de consulta. De lo contrario, el método `getFile()` también lo incluiría. Lo veremos en el siguiente ejemplo de código.

El resto del código anterior está abriendo un flujo de entrada de datos desde un archivo e imprime los bytes entrantes como caracteres. El resultado se muestra en el comentario en línea.

Ahora, demostremos cómo el código Java puede leer datos de la URL que apunta a una fuente en Internet. Llamemos al motor de búsqueda de Google con una palabra clave *Java*:

```Java
private static void getFromURL() {
    try {
        URL url = new URL("https://www.google.com/search?q=Java&num=10");
        System.out.println(url.getPath());
        System.out.println(url.getFile());
        URLConnection conn = url.openConnection();
        conn.setRequestProperty("Accept", "text/html");
        conn.setRequestProperty("Connection", "close");
        conn.setRequestProperty("Accept-Language", "en-US");
        conn.setRequestProperty("User-Agent", "Mozilla/5.0");

        try (InputStream is = conn.getInputStream();
        BufferedReader br = new BufferedReader(new InputStreamReader(is))) {
            String line;
            while ((line = br.readLine()) != null) {
                System.out.println(line);
            }
        }
    }
    catch (Exception e) {
        e.printStackTrace();
    }
}
```

Solicitamos la dirección `https://www.google.com/search?q=Java&num=10` como `URL`. En este caso no hay garantía de que siempre funcione, así que no se sorprenda si no devuelve los mismos datos que describimos. Además, es una búsqueda en una fecha determinada, por lo que el resultado puede cambiar en cualquier momento.

El código anterior también muestra la diferencia en los valores devueltos por los métodos `getPath()` y `getFile()`. Puede ver los comentarios en línea en el ejemplo de código anterior.

En comparación con el ejemplo de uso de una `URL` de archivo, el ejemplo de búsqueda de Google utilizó el objeto `URLConnection` porque necesitamos establecer los campos de encabezado de solicitud:

  > * `Accept` le dice al servidor qué tipo de contenido solicita la persona que llama (`understands`).
  > * `Connection` le dice al servidor que la conexión se cerrará después de recibir la respuesta.
  > * `Accept-Language` le dice al servidor qué idioma solicita la persona que llama (`understands`).
  > * `User-Agent` le dice al servidor información sobre la persona que llama; de lo contrario, el motor de búsqueda de Google (www.google.com) responde con un código HTTP 403.

El código restante en el ejemplo anterior solo lee el flujo de entrada de datos (código HTML) proveniente de la URL, y lo imprime, línea por línea.

Del mismo modo, es posible enviar (`post`) datos a una URL; Aquí hay un código de ejemplo:

```Java
private static void postToURL() {
    try {
        URL url = new URL("http://localhost:3333/something");
        URLConnection conn = url.openConnection();
        //conn.setRequestProperty("Method", "POST");
        //conn.setRequestProperty("User-Agent", "Java client");
        conn.setDoOutput(true);
        OutputStreamWriter osw =
                new OutputStreamWriter(conn.getOutputStream());
        osw.write("parameter1=value1&parameter2=value2");
        osw.flush();
        osw.close();

        BufferedReader br =
        new BufferedReader(new InputStreamReader(conn.getInputStream()));
        String line;
        while ((line = br.readLine()) != null) {
            System.out.println(line);
        }
        br.close();
    }
    catch(Exception e) {
        e.printStackTrace();
    }
}
```

El código anterior espera que un servidor se ejecute en el servidor `localhost` en el puerto `3333` que pueda procesar la solicitud POST con la ruta `/ something`. Si el servidor no verifica el método (es POST o cualquier otro método HTTP) y no verifica el valor de `User-Agent`, no es necesario especificar ninguno de ellos. Por lo tanto, comentamos la configuración y la mantenemos allí solo para demostrar cómo se pueden establecer estos valores, y similares, si es necesario.

Observe que usamos el método `setDoOutput()` para indicar que se debe enviar la salida; de forma predeterminada, se establece en falso. Luego, dejamos que la secuencia de salida envíe los parámetros de consulta al servidor.

Otro aspecto importante del código anterior es que la secuencia de salida debe cerrarse antes de abrir la secuencia de entrada. De lo contrario, el contenido de la secuencia de salida no se enviará al servidor. Si bien lo hicimos explícitamente, una mejor manera de hacerlo es mediante el uso del bloque try-with-resources que garantiza que se llama al método `close()`, incluso si se generó una excepción en cualquier parte del bloque.

Aquí hay una mejor versión del ejemplo anterior:

```Java
private static void postToURL() {
    try {
        URL url = new URL("http://localhost:3333/something");
        URLConnection conn = url.openConnection();
        conn.setRequestProperty("Method", "POST");
        conn.setRequestProperty("User-Agent", "Java client");
        conn.setDoOutput(true);

        try (OutputStreamWriter osw = new OutputStreamWriter(conn.getOutputStream())) {
            osw.write("parameter1=value1&parameter2=value2");
            osw.flush();
        }

        try (BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()))) {
            String line;

            while ((line = br.readLine()) != null) {
                System.out.println(line);
            }
        }
    }
    catch(Exception e) {
        e.printStackTrace();
    }
}
```

Para demostrar cómo funciona este ejemplo, también creamos un servidor simple que escucha en el puerto `3333` de `localhost` y tiene un controlador asignado para procesar todas las solicitudes que vienen con la ruta `/ something`:

```Java
package U4_ProgramaciónParaRed.ejemplos;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.InetSocketAddress;


public class URLServer {

    public static void main(String[] args) throws Exception {
        HttpServer server = HttpServer.create(new InetSocketAddress(3333), 0);
        server.createContext("/something", new PostHandler());
        server.setExecutor(null);
        server.start();
    }

    private static class PostHandler implements HttpHandler {

        public void handle(HttpExchange exch) {
            System.out.println(); // to skip the row
            System.out.println(exch.getRequestURI());
            System.out.println(exch.getHttpContext().getPath());

            try (BufferedReader in = new BufferedReader(new InputStreamReader(exch.getRequestBody()));
            OutputStream os = exch.getResponseBody()) {
                System.out.println("Receive as body: ");
                in.lines().forEach(l -> System.out.println(" " + l));

                String confirm = "Got it! Thanks.";
                exch.sendResponseHeaders(200, confirm.length());
                os.write(confirm.getBytes());
            }
            catch (Exception e) {
                e.printStackTrace();
            }

        }
    }
    
}
```

Para implementar el servidor, utilizamos las clases del paquete `com.sun.net.httpserver` que viene con el JCL. Para demostrar que la URL viene sin parámetros, imprimimos el URI y la ruta. Ambos tienen el mismo valor `/something`; Los parámetros provienen del cuerpo de la solicitud.

Una vez procesada la solicitud, el servidor devuelve el mensaje "Got it! Thanks". Veamos como funciona: Primero ejecutamos el servidor. Comienza a escuchar en el puerto `3333` y se bloquea hasta que la solicitud llega con la ruta "/something". Luego, ejecutamos el cliente y observamos el siguiente resultado en la pantalla del lado del servidor

```
\something
\something
Received as body:
  parameter1=value&parameter2=value2
```

Como puede ver, el servidor recibió los parámetros (o cualquier otro mensaje para el caso) con éxito. Ahora puede analizarlos y usarlos según sea necesario.

Si miramos la pantalla del lado del cliente, veremos el siguiente resultado:

```
Got it! Thanks.

Process finished with exit code 0
```

Esto significa que el cliente recibió el mensaje del servidor y salió como se esperaba. Observe que el servidor en nuestro ejemplo no sale automáticamente y debe cerrarse manualmente.

Otros métodos de las clases `URL` y `URLConnection` le permiten establecer / obtener otras propiedades y pueden usarse para una administración más dinámica de la comunicación cliente-servidor. También existe la clase `HttpUrlConnection` (y otras clases) en el paquete `java.net` que simplifica y mejora la comunicación basada en URL.

---

## Utilizando el API HTTP 2 Client

La API de cliente HTTP se introdujo con Java 9 como API de incubación en el paquete `jdk.incubator.http`. En Java 11, se estandarizó y se movió al paquete `java.net.http`. Es una alternativa mucho más rica y fácil de usar que la API de `URLConnection`. Además de toda la funcionalidad básica relacionada con la conexión, proporciona una solicitud y respuesta sin bloqueo (asíncrono) utilizando `CompletableFuture` y es compatible con HTTP 1.1 y HTTP 2.

HTTP 2 agregó las siguientes capacidades nuevas al protocolo HTTP:

  > * La capacidad de enviar datos en formato binario en lugar de formato textual; El formato binario es más eficiente para el análisis, más compacto y menos susceptible a varios errores.
  > * Está totalmente multiplexado, lo que permite enviar múltiples solicitudes y respuestas de forma simultánea utilizando solo una conexión.
  > * Utiliza compresión de encabezado, lo que reduce la sobrecarga.
  > * Permite que un servidor introduzca una respuesta en la memoria caché del cliente si el cliente indica que admite HTTP 2.

El paquete contiene las siguientes clases:

  > * `HttpClient`: se utiliza para enviar solicitudes y recibir respuestas de forma sincrónica y asincrónica. Se puede crear una instancia con el método estático `newHttpClient()` con la configuración predeterminada o con la clase `HttpClient.Builder` (devuelta por el método estático `newBuilder()`) que le permite personalizar la configuración del cliente. Una vez creada, la instancia es inmutable y se puede usar varias veces.
  > * `HttpRequest`: crea y representa una solicitud HTTP con el `URI` de destino, los encabezados y otra información relacionada. Se puede crear una instancia utilizando la clase `HttpRequest.Builder` (devuelta por el método estático `newBuilder()`). Una vez creada, la instancia es inmutable y se puede enviar varias veces.
  > * `HttpRequest.BodyPublisher`: publica un cuerpo (para los métodos `POST`, `PUT` y `DELETE`) de una determinada fuente, como una cadena, un archivo, una secuencia de entrada o una matriz de bytes.
  > * `HttpResponse`: representa una respuesta `HTTP` recibida por el cliente después de que se haya enviado una solicitud `HTTP`. Contiene el `URI` de origen, los encabezados, el cuerpo del mensaje y otra información relacionada. Una vez creada, la instancia se puede consultar varias veces.
  > * `HttpResponse.BodyHandler`: esta es una interfaz funcional que acepta la respuesta y devuelve una instancia de `HttpResponse.BodySubscriber` que puede procesar el cuerpo de la respuesta.
  > * `HttpResponse.BodySubscriber`: recibe el cuerpo de respuesta (sus bytes) y lo transforma en una cadena, un archivo o un tipo.

Las clases `HttpRequest.BodyPublishers`, `HttpResponse.BodyHandlers` y `HttpResponse.BodySubscribers` son clases de fábrica que crean instancias de las clases correspondientes. Por ejemplo, el método `BodyHandlers.ofString()` crea una instancia de `BodyHandler` que procesa los bytes del cuerpo de respuesta como una cadena, mientras que el método `BodyHandlers.ofFile()` crea una instancia de `BodyHandler` que guarda el cuerpo de respuesta en un archivo.

Puede leer la documentación en línea del paquete `java.net.http` para obtener más información sobre estas y otras clases e interfaces relacionadas. A continuación, veremos y discutiremos algunos ejemplos del uso de la API HTTP.

## Bloqueo de solicitudes HTTP

El siguiente código es un ejemplo de un cliente HTTP simple que envía una solicitud GET a un servidor HTTP:

```Java
private static void get() {
    HttpClient httpClient = HttpClient.newHttpClient();

    // HttpClient httpClient = HttpClient.newBuilder().version(HttpClient.Version.HTTP_2).build();

    HttpRequest req = HttpRequest.newBuilder().uri(URI.create("http://localhost:3333/something")).GET().build();

    try {
        HttpResponse<String> resp = httpClient.send(req, BodyHandlers.ofString());
        System.out.println("Response: " + resp.statusCode() + " : " + resp.body());
    }
    catch (Exception e) {
        e.printStackTrace();
    }
}
```

Creamos un generador para configurar una instancia de `HttpClient`. Sin embargo, dado que solo utilizamos la configuración predeterminada, podemos hacerlo con el mismo resultado de la siguiente manera:

```Java
HttpClient httpClient = HttpClient.newHttpClient();
```

Para demostrar la funcionalidad del cliente, usaremos la misma clase `UrlServer` que ya usamos. Como recordatorio, así es como procesa la solicitud del cliente y responde con `"Got it! Thanks.`:

Si iniciamos este servidor y ejecutamos el código del cliente anterior, el servidor imprime el siguiente mensaje en su pantalla:

```
Received as body:
```

El cliente no envió un mensaje porque utilizó el método HTTP `GET`. Sin embargo, el servidor responde y la pantalla del cliente muestra el siguiente mensaje:

```
Response: 200 : Got it! Thanks.
```

El método `send()` de la clase `HttpClient` está bloqueado hasta que la respuesta haya regresado del servidor.

El uso de los métodos HTTP `POST`, `PUT` o `DELETE` produce resultados similares; ejecutemos el siguiente código ahora:

```Java
private static void post() {
    HttpClient httpClient = HttpClient.newHttpClient();

    HttpRequest req = HttpRequest.newBuilder().uri(URI.create("http://localhost:3333/something")).POST(BodyPublishers.ofString("Hi there!")).build();

    try {
        HttpResponse<String> resp = httpClient.send(req, BodyHandlers.ofString());
        System.out.println("Response: " + resp.statusCode() + " : " + resp.body());
    }
    catch (Exception e) {
        e.printStackTrace();
    }
}
```

Como puede ver, esta vez el cliente publica el mensaje `"Hi there"` Y la pantalla del servidor muestra lo siguiente:

```
Received as body:
  Hi there!
```

El método `send()` de la clase `HttpClient` está bloqueado hasta que la misma respuesta haya regresado del servidor:

```
Response: 200 : Git it! Thanks
```

Hasta ahora, la funcionalidad demostrada no era muy diferente de la comunicación basada en URL. Ahora vamos a utilizar los métodos `HttpClient` que no están disponibles en las secuencias de URL.


## Solicitudes HTTP sin bloqueo (asíncronas)

El método `sendAsync()` de la clase `HttpClient` le permite enviar un mensaje a un servidor sin bloquear. Para demostrar cómo funciona, ejecutaremos el siguiente código:

```Java
private static void getAsync() {
    HttpClient httpClient = HttpClient.newHttpClient();

    HttpRequest req = HttpRequest.newBuilder().uri(URI.create("http://localhost:3333/something")).GET().build();

    //CompletableFuture<Void> cf = httpClient.sendAsync(req, BodyHandlers.ofString()).thenAccept(resp -> System.out.println("Response: " + resp.statusCode() + " : " + resp.body()));
    CompletableFuture<String> cf = httpClient.sendAsync(req, BodyHandlers.ofString()).thenApply(resp -> "Server responded: " + resp.body());

    System.out.println("The request was sent asynchronously...");
    try {
        System.out.println("CompletableFuture get: " + cf.get(5, TimeUnit.SECONDS));
    }
    catch(Exception e) {
        e.printStackTrace();
    }
    System.out.println("Exit the client...");
}
```

En comparación con el ejemplo con el método `send ()` (que devuelve el objeto `HttpResponse`), el método `sendAsync()` devuelve una instancia de la clase `CompletableFuture <HttpResponse>`. Si lee la documentación de la clase `CompletableFuture <T>`, verá que implementa la interfaz `java.util.concurrent.CompletionStage` que proporciona muchos métodos que se pueden encadenar y le permiten configurar varias funciones para procesar la respuesta.

Para darle una idea, aquí está la lista de los métodos declarados en la `interf`az acceptEither, `acceptEitherAsync`, `acceptEitherAsync`, `applyToEither`, `applyToEitherAsync`, `applyToEitherAsync`, `handle`, `handleAsync`, `handleAsync`, `runAfterBoth`, `runAfterBothAsync`, `runAfterBothAsync`, `runAfterEither`, `runAfterEitherAsync`, `runAfterEitherAsync`, `thenAccept`, `thenAcceptAsync`, `thenAcceptAsync`, `thenAcceptBoth`, `thenAcceptBothAsync`, `thenAcceptBothAsync`, `thenApply`, `thenApplyAsync`, `thenApplyAsync`, `thenCombine`, `thenCombineAsync`, `thenCombineAsync`, `thenCompose`, `thenComposeAsync`, `thenComposeAsync`, `thenRun`, `thenRunAsync`, `thenRunAsync`,`whenComplete`, `whenCompleteAsync`, y `whenCompleteAsync`.

La construcción `resp -> System.out.println ("Response:" + resp.statusCode () + ":" + resp.body ())` representa la misma funcionalidad que el siguiente método:

```Java
void method(HttpResponse resp){
    System.out.println("Response: " + 
                             resp.statusCode() + " : " + resp.body());
}
```

El método `thenAccept()` aplica la funcionalidad pasada al resultado devuelto por el método anterior de la cadena.

Después de que se devuelve la instancia `CompletableFuture <Void>`, el código anterior imprime "The request was sent asynchronously..." y bloquea en el método `get()` del objeto `CompletableFuture <Void>`. Este método tiene una versión sobrecargada `get(long timeout, TimeUnit unit)`, con dos parámetros: `TimeUnit unit` y `long timeout` que especifica el número de unidades, lo que indica cuánto tiempo debe esperar el método para la tarea representada por `CompletableFuture Objeto <Void>` para completar. En nuestro caso, la tarea es enviar un mensaje al servidor y recuperar la respuesta (y procesarla usando la función provista). Si la tarea no se completa en el tiempo asignado, el método `get()` se interrumpe (y el seguimiento de la pila se imprime en el bloque catch).

El mensaje `"Exit the client..."` debería aparecer en la pantalla en cinco segundos o después de que regrese el método `get()`.

Si ejecutamos el cliente, la pantalla del servidor muestra nuevamente el siguiente mensaje con la solicitud HTTP GET de bloqueo:

```
Received as body:
```

La pantalla del cliente muestra el siguiente mensaje:

```
The request was sent asynchronously...
Response: 200 : Got it! Thanks.
CompletableFuture get: null
Exit the client...
```

Como puede ver, `"The request was sent asynchronously..."` aparece un mensaje antes de que la respuesta regresara del servidor. Este es el punto de una llamada asincrónica; se envió la solicitud al servidor y el cliente es libre de continuar haciendo cualquier otra cosa. La función pasada se aplicará a la respuesta del servidor. Al mismo tiempo, puede pasar el objeto `CompletableFuture <Void>` y llamarlo en cualquier momento para obtener el resultado. En nuestro caso, el resultado es `null`, por lo que el método `get()` simplemente indica que la tarea se completó.

Sabemos que el servidor devuelve el mensaje, por lo que podemos aprovecharlo utilizando otro método de la interfaz `CompletionStage`. Hemos elegido el método `thenApply()`, que acepta una función que devuelve un valor:

```Java
CompletableFuture<String> cf = httpClient
                .sendAsync(req, BodyHandlers.ofString())
                .thenApply(resp -> "Server responded: " + resp.body());
```

Ahora, el método `get()` devuelve el valor producido por la función `resp -> "El servidor respondió:" + resp.body ()`, por lo que debería devolver el cuerpo del mensaje del servidor; ejecutemos este código y veamos el resultado:

```
The request was sent asynchronously...
CompletableFuture get: Server responded: Got it!. Thanks.
Exit the client...
```

Ahora el método `get()` devuelve el mensaje del servidor como se esperaba, y es presentado por la función y pasado como un parámetro al método `thenApply()`.

Del mismo modo, podemos usar los métodos HTTP `POST`, `PUT` o `DELETE` para enviar un mensaje:

```Java
private static void postAsync() {
    HttpClient httpClient = HttpClient.newHttpClient();

    HttpRequest req = HttpRequest.newBuilder().uri(URI.create("http://localhost:3333/something")).POST(BodyPublishers.ofString("Hi there!")).build();

    CompletableFuture<String> cf = httpClient.sendAsync(req, BodyHandlers.ofString()).thenApply(resp -> "server responded:" + resp.body());
    System.out.println("The request was sent asynchronously...");

    try {
        System.out.println("CompletableFuture get: " + cf.get(5, TimeUnit.SECONDS));
    }
    catch (Exception e) {
        e.printStackTrace();
    }

    System.out.println("Exit the client...");
}
```

La única diferencia con el ejemplo anterior es que el servidor ahora muestra el mensaje del cliente recibido:

```
Received as body:
  Hi there!
```

La pantalla del cliente muestra el mismo mensaje que en el caso del método `GET`:

```
The request was sent asynchronously...
CompletableFuture get: Server responded: Got it! Thanks.
Exit the client...
```

La ventaja de las solicitudes asincrónicas es que pueden enviarse rápidamente y sin necesidad de esperar a que se completen. El protocolo HTTP 2 lo admite mediante multiplexación; por ejemplo, enviemos tres solicitudes de la siguiente manera:

```Java
private static void postAsyncMultiple() {
    HttpClient httpClient = HttpClient.newHttpClient();

    List<CompletableFuture<String>> cfs = new ArrayList<>();
    List<String> nums = List.of("1", "2", "3");

    for (String num: nums) {
        HttpRequest req = HttpRequest.newBuilder().uri(URI.create("http://localhost:3333/something"))
        .POST(BodyPublishers.ofString("Hi! My name is " + num + "."))
        .build();

        CompletableFuture<String> cf = httpClient
        .sendAsync(req, BodyHandlers.ofString())
        .thenApply(rsp -> "Server responded to msg " + num + ": " + rsp.statusCode() + " : " + rsp.body());

        cfs.add(cf);
    }

    System.out.println("The requests were sent asynchronously...");
    try {
        for (CompletableFuture<String> cf: cfs) {
            System.out.println("CompletableFugure get:" + cf.get(5, TimeUnit.SECONDS));
        }
    }
    catch (Exception e) {
        e.printStackTrace();
    }

    System.out.println("Exit the client");
}
```

El servidor responderá con la siguiente salida:

```
Received as body:
  Hi! My name is 2.

Received as body:
  Hi! My name is 3.

Received as body:
  Hi! My name is 1.
```

Observe la secuencia arbitraria de las solicitudes entrantes; Esto se debe a que el cliente utiliza un grupo de subprocesos `Executors.newCachedThreadPool()` para enviar los mensajes. Cada mensaje es enviado por un hilo diferente, y el grupo tiene su propia lógica de usar los miembros del grupo (hilos). Si el número de mensajes es grande, o si cada uno de ellos consume una cantidad significativa de memoria, puede ser beneficioso limitar el número de hilos que se ejecutan simultáneamente.

La clase `HttpClient.Builder` le permite especificar el grupo que se utiliza para adquirir los hilos que envían los mensajes:

```Java
private static void postAsyncMultipleUsingPool() {
    ExecutorService pool = Executors.newFixedThreadPool(2);

    HttpClient httpClient = HttpClient.newBuilder().executor(pool).build();

    List<CompletableFuture<String>> cfs = new ArrayList<>();
    List<String> nums = List.of("1", "2", "3");
    for(String num: nums) {
        HttpRequest req = HttpRequest.newBuilder()
        .uri(URI.create("http://localhost:3333/something"))
        .POST(BodyPublishers.ofString("Hi! My name is " + num + ": "))
        .build();
        CompletableFuture<String> cf = httpClient
        .sendAsync(req, BodyHandlers.ofString())
        .thenApply(rsp -> "Server responded to msg " + num + ": " + rsp.statusCode() + " : " + rsp.body());

        cfs.add(cf);
    }
    System.out.println("The requests were sent asynchronously...");

    try {
        for (CompletableFuture<String> cf: cfs) {
            System.out.println("CompletableFuture get: " + cf.get(5, TimeUnit.SECONDS));
        }
    }
    catch (Exception e) {
        e.printStackTrace();
    }
    System.out.println("Exit the client...");
}
```

Si ejecutamos el código anterior, los resultados serán los mismos, pero el cliente usará solo dos hilos para enviar mensajes. El rendimiento puede ser un poco más lento (en comparación con el ejemplo anterior) a medida que aumenta el número de mensajes. Entonces, como suele ser el caso en un diseño de sistema de software, debe equilibrar la cantidad de memoria utilizada y el rendimiento.

De manera similar al ejecutor, se pueden establecer varios otros objetos en el objeto `HttpClient` para configurar la conexión para manejar la autenticación, la redirección de solicitudes, la administración de cookies y más.

## Funcionalidad de inserción del servidor (Push)

La segunda ventaja significativa (después de la multiplexación) del protocolo HTTP 2 sobre HTTP 1.1 es permitir que el servidor introduzca la respuesta en la memoria caché del cliente si el cliente indica que es compatible con HTTP 2. Aquí está el código del cliente que aprovecha esta característica:

```Java
private static void push() {
    HttpClient httpClient = HttpClient.newHttpClient();

    HttpRequest req = HttpRequest.newBuilder().uri(URI.create("http://localhost:3333/something"))
    .GET()
    .build();

    CompletableFuture cf = httpClient.sendAsync(req, BodyHandlers.ofString(),
    (PushPromiseHandler) HttpClientDemo::applyPushPromise);

    System.out.println("The request was sent asynchronously...");
    try {
        System.out.println("CompletableFuture get: " + cf.get(5, TimeUnit.SECONDS));
    }
    catch (Exception e) {
        e.printStackTrace();
    }
    System.out.println("Exit the client...");
}
```

Observe el tercer parámetro del método `sendAsync()`. Es una función que maneja la respuesta de inserción si una proviene del servidor. Depende del desarrollador del cliente decidir cómo implementar esta función; Aquí hay un posible ejemplo:

```Java
private static void applyPushPromise(HttpRequest initRequest, HttpRequest pushReq,
    Function<BodyHandler, CompletableFuture<HttpResponse>> acceptor) {
    CompletableFuture<Void> cf = acceptor.apply(BodyHandlers.ofString()).thenAccept(resp -> System.out.println("Got pushed response " + resp.uri()));
    try {
        System.out.println("Pushed completableFuture get: " + cf.get(1, TimeUnit.SECONDS));
    }
    catch (Exception e) {
        e.printStackTrace();
    }
    System.out.println("Exit the applyPushPromise function...");
}
```

Esta implementación de la función no hace mucho. Simplemente imprime el URI del origen de inserción. Pero, si es necesario, se puede usar para recibir los recursos del servidor (por ejemplo, imágenes que admiten el HTML proporcionado) sin solicitarlos. Esta solución ahorra el modelo de solicitud-respuesta de ida y vuelta y acorta el tiempo de carga de la página. También se puede usar para actualizar la información en la página.

Puede encontrar muchos ejemplos de código de un servidor que envía solicitudes push; Todos los principales navegadores también admiten esta función.

## Soporte WebSocket

HTTP se basa en el modelo de solicitud-respuesta. Un cliente solicita un recurso y el servidor proporciona una respuesta a esta solicitud. Como demostramos varias veces, el cliente inicia la comunicación. Sin él, el servidor no puede enviar nada al cliente. Para superar esta limitación, la idea se introdujo por primera vez como conexión TCP en la especificación HTML5 y, en 2008, se diseñó la primera versión del protocolo WebSocket.

Proporciona un canal de comunicación full-duplex entre el cliente y el servidor. Una vez establecida la conexión, el servidor puede enviar un mensaje al cliente en cualquier momento. Junto con JavaScript y HTML5, el soporte del protocolo WebSocket permite que las aplicaciones web presenten una interfaz de usuario mucho más dinámica.

La especificación del protocolo WebSocket define WebSocket (`ws`) y WebSocket Secure (`wss`) como dos esquemas, que se utilizan para conexiones no cifradas y cifradas, respectivamente. El protocolo no admite fragmentación, pero permite todos los demás componentes de URI descritos en la sección de sintaxis de URL.

Todas las clases que admiten el protocolo WebSocket para un cliente se encuentran en el paquete `java.net`. Para crear un cliente, necesitamos implementar la interfaz `WebSocket.Listener`, que tiene los siguientes métodos:

  > * `onText ()`: se invoca cuando se reciben datos textuales
  > * `onBinary ()`: se invoca cuando se reciben datos binarios
  > * `onPing ()`: se invoca cuando se recibe un mensaje de ping
  > * `onPong ()`: se invoca cuando se recibe un mensaje pong
  > * `onError ()`: se invoca cuando se produce un error
  > * `onClose ()`: se invoca cuando se recibe un mensaje de cierre

Todos los métodos de esta interfaz son predeterminados. Esto significa que no necesita implementarlos todos, sino solo aquellos que el cliente requiere para una tarea en particular:

```Java
private static class WsClient implements WebSocket.Listener {
    @Override
    public void onOpen(WebSocket webSocket) {
        System.out.println("Connection established.");
        webSocket.sendText("Some message", true);
        Listener.super.onOpen(webSocket);
    }

    @Override
    public CompletionStage onText(WebSocket webSocket, CharSequence data, boolean last) {
        System.out.println("Method onText() got data: " + data);
        if (!webSocket.isOutputClosed()) {
            webSocket.sendText("Another message", true);
        }
        return Listener.super.onText(webSocket, data, last);
    }

    @Override
    public CompletionStage onClose(WebSocket webSocket, int statusCode, String reason) {
        System.out.println("Close with status: " + statusCode + ", reason: " + reason);
        return Listener.super.onClose(webSocket, statusCode, reason);
    }
}
```

Un servidor puede implementarse de manera similar, pero la implementación del servidor está más allá del alcance de este libro. Para demostrar el código de cliente anterior, vamos a utilizar un servidor WebSocket provisto por el sitio web `echo.websocket.org`. Permite una conexión WebSocket y devuelve el mensaje recibido; dicho servidor generalmente se denomina servidor de **echo**.

Esperamos que nuestro cliente envíe el mensaje después de que se establezca la conexión. Luego, recibirá (el mismo) mensaje del servidor, lo mostrará y enviará otro mensaje, y así sucesivamente, hasta que se cierre. El siguiente código invoca al cliente que creamos:

```Java
private static void webSocket() {
    HttpClient httpClient = HttpClient.newHttpClient();
    WebSocket webSocket = httpClient.newWebSocketBuilder()
    .buildAsync(URI.create("ws://echo.websocket.org"), new WsClient())
    .join();

    System.out.println("The WebSocket was created and ran asynchronously.");

    try {
        TimeUnit.MILLISECONDS.sleep(200);
    }
    catch(InterruptedException e) {
        e.printStackTrace();
    }
    webSocket.sendClose(WebSocket.NORMAL_CLOSURE, "Normal closure")
    .thenRun(() -> System.out.println("Close is sent"));
}
```

El código anterior crea un objeto `WebSocket` utilizando la clase `WebSocket.Builder`. El método `buildAsync()` devuelve el objeto `CompletableFuture`. El método join () de la clase CompletableFuture devuelve el valor del resultado cuando se completa o genera una excepción. Si no se genera una excepción, entonces, como ya mencionamos, la comunicación WebSocket continúa hasta que cualquiera de las partes envíe un mensaje de Cerrar. Es por eso que nuestro cliente espera 200 milisegundos y luego envía el mensaje Cerrar y sale. Si ejecutamos este código, veremos los siguientes mensajes:

```
Connection established.
The WebSocket was created and ran asynchronously.
Method onText() got data: Some Message
Method onText() got data: Another message
Close is sent.
```

Como puede ver, el cliente se comporta como se esperaba. Para finalizar nuestra discusión, nos gustaría mencionar que todos los navegadores web modernos admiten el protocolo WebSocket.
