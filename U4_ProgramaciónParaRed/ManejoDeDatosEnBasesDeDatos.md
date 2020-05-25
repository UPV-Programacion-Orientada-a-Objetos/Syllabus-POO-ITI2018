# Manejo de Datos en Bases de Datos

**Java Database Connectivity (JDBC)** es una funcionalidad de Java que le permite acceder y modificar datos en una base de datos. Es compatible con la API JDBC (que incluye los paquetes `java.sql`, `javax.sql` y `java.transaction.xa`) y clases específicas de la base de datos que implementa una interfaz para el acceso a la base de datos (llamada controlador de base de datos), que es proporcionado por cada proveedor de base de datos.

Usar JDBC significa escribir código Java que gestiona datos en una base de datos usando las interfaces y clases de la API JDBC y un controlador específico de la base de datos, el cual sabe cómo establecer una conexión con la base de datos particular. Con esta conexión, una aplicación puede emitir solicitudes escritas en SQL.

Naturalmente, sólo nos estamos refiriendo a las bases de datos que entienden SQL. Se denominan sistemas de gestión de **bases de datos relacionales o tabulares (Database Management Systems, DBMS)** y constituyen la gran mayoría de los DBMS utilizados actualmente, aunque también se utilizan algunas alternativas (por ejemplo, una base de datos de navegación y NoSQL).

Los paquetes `java.sql` y `javax.sql` están incluidos en **Java Platform Standard Edition (Java SE)**. El paquete j`avax.sql` contiene la interfaz `DataSource` que admite la agrupación de sentencias, las transacciones distribuidas y los conjuntos de filas.

Crear una base de datos implica los siguientes ocho pasos:

  > 1. Instale la base de datos siguiendo las instrucciones del proveedor.
  > 1. Cree un usuario de base de datos, una base de datos, un esquema, tablas, vistas, procedimientos almacenados y todo lo que sea necesario para admitir el modelo de datos de la aplicación.
  > 1. Agregue a la aplicación la dependencia de un archivo `.jar` con el controlador específico de la base de datos. 
  > 1. Conéctese a la base de datos desde la aplicación.
  > 1. Construir la declaración SQL.
  > 1. Ejecute la instrucción SQL.
  > 1. Use el resultado de la ejecución según lo requiera la aplicación.
  > 1. Libere (es decir, cierre) la conexión de la base de datos y cualquier otro recurso que se haya abierto en el proceso.

Los pasos 1 a 3 se realizan sólo una vez durante la configuración de la base de datos y antes de ejecutar la aplicación. La aplicación realiza los pasos 4 a 8 repetidamente según sea necesario. De hecho, los pasos 5 a 7 se pueden repetir varias veces con la misma conexión de base de datos.

Para nuestro ejemplo, vamos a utilizar la base de datos `PostgreSQL`. Primero se deberá realizar los pasos 1 a 3 utilizando las instrucciones específicas del manejador de base de datos. Para crear la base de datos para nuestra demostración, utilizamos los siguientes comandos dentro de la consola de `POSTGREST`:

```SQL
create user poo_student SUPERUSER;
create database poo_db_test owner poo_student;
```

Estos comandos crean un usuario `student` que puede administrar todos los aspectos de la base de datos `SUPERUSER`, y hacen que el usuario `student` sea propietario de la base de datos `perritos`. Utilizaremos al usuario `student` para acceder y administrar los datos del código Java. En la práctica, por razones de seguridad, una aplicación no tiene permitido crear o cambiar tablas de bases de datos y otros aspectos de la estructura de la base de datos.

Además, es una buena práctica crear otra capa lógica llamada esquema que pueda tener su propio conjunto de usuarios y permisos. De esta forma, se pueden aislar varios esquemas en la misma base de datos, y cada usuario (uno de ellos es su aplicación) solo puede acceder a ciertos esquemas. A nivel empresarial, la práctica común es crear sinónimos para el esquema de la base de datos para que ninguna aplicación pueda acceder directamente a la estructura original. Sin embargo, no hacemos esto en este libro por simplicidad.

## Creando la estructura de la base de datos

Después de crear la base de datos, las siguientes tres instrucciones SQL le permitirán crear y cambiar la estructura de la base de datos. Esto se hace a través de entidades de la base de datos, como una tabla, función o restricción:

  > * El comando `CREATE` crea entidades de la base de datos.
  > * El comando `ALTER` modifica entidades de la base de datos.
  > * El comando `DROP` elimina entidades de la base de datos.

También hay varias instrucciones SQL que le permiten consultar sobre cada entidad de la base de datos. Dichas declaraciones son específicas de la base de datos y, por lo general, solo se usan en una consola. Por ejemplo, en la consola de PostgreSQL, `\d <table>` se puede usar para describir una tabla, mientras que `\dt` enumera todas las tablas. Consulte la documentación de su base de datos para obtener más detalles.

Para crear una tabla, la sintaxis es la siguiente:

  > `CREATE TABLE tablename ( column1 type1, column2 type2, ... );`

Las limitaciones para un nombre de tabla, nombres de columna y tipos de valores que se pueden usar dependen de la base de datos particular. Aquí hay un ejemplo de un comando que crea la tabla de personas en PostgreSQL:

```SQL
CREATE table person ( 
   id SERIAL PRIMARY KEY, 
   first_name VARCHAR NOT NULL, 
   last_name VARCHAR NOT NULL, 
   dob DATE NOT NULL );
```

La palabra `SERIAL` indica que este campo es un número entero secuencial generado por la base de datos cada vez que se crea un nuevo registro, es equivalente a `AUTOINCREMENT` en otros manejadores de bases de datos. Las opciones adicionales para generar enteros secuenciales son `SMALLSERIAL` y `BIGSERIAL`; difieren según el tamaño y el rango de valores posibles, para mayor información consulte la [documentación oficial de Postgres](https://www.postgresql.org/docs/9.3/datatype-numeric.html):

  > ```SQL
  > SMALLSERIAL: 2 bytes, range from 1 to 32,767
  > SERIAL: 4 bytes, range from 1 to 2,147,483,647
  > BIGSERIAL: 8 bytes, range from 1 to 922,337,2036,854,775,807
  > ```

La palabra clave `PRIMARY_KEY` indica que este será el identificador único del registro y probablemente se usará en una búsqueda. La base de datos crea un índice para cada clave primaria para acelerar el proceso de búsqueda. Un índice es una estructura de datos que ayuda a acelerar la búsqueda de datos en la tabla sin tener que verificar todos los registros de la tabla. Un índice puede incluir una o más columnas de una tabla. Si solicita la descripción de la tabla, verá todos los índices existentes.

Alternativamente, podemos hacer una llave compuesta `PRIMARY KEY` usando una combinación de `first_name`, `last_name` y `dob`:

  > ```SQL
  > CREATE table person ( 
  > first_name VARCHAR NOT NULL, 
  > last_name VARCHAR NOT NULL, 
  > dob DATE NOT NULL,
  > PRIMARY KEY (first_name, last_name, dob) );
  >```

La palabra clave `NOT NULL` impone una restricción en el campo: no puede estar vacía. La base de datos generará un error por cada intento de crear un nuevo registro con un campo vacío o eliminar el valor del registro existente. No se estableció el tamaño de las columnas de tipo `VARCHAR`, permitiendo así que estas columnas almacenen valores de cadena de cualquier longitud.

El objeto Java que coincide con dicho registro puede estar representado por la siguiente clase `Person`:

```Java
public class Person {
    private int id;
    private LocalDate dob;
    private String firstName, lastName;
    public Person(String firstName, String lastName, LocalDate dob) {
        if (dob == null) {
            throw new RuntimeException("Date of birth cannot be null");
        }
        this.dob = dob;
        this.firstName = firstName == null ? "" : firstName;
        this.lastName = lastName == null ? "" : lastName;
    }
    public Person(int id, String firstName,
                  String lastName, LocalDate dob) {
        this(firstName, lastName, dob);
        this.id = id;
    }
    public int getId() { return id; }
    public LocalDate getDob() { return dob; }
    public String getFirstName() { return firstName;}
    public String getLastName() { return lastName; }
}
```

Como se puede observar, hay dos constructores en la clase `Person`: con y sin id. Usaremos el constructor que acepta id para construir un objeto basado en el registro existente, mientras que el otro constructor se usará para crear un objeto antes de insertar un nuevo registro.

Una vez creado, la tabla puede ser borrada utilizando el comando `DROP`:

  > `DROP table person;`

De la misma manera, la tabla existente puede ser modificada mediante el comando `ALTER`, por ejemplo, para agregar la columna `address`

  > `ALTER table person add column address VARCHAR;`

Si no se esta seguro de que la columna `address` ya exista en la tabla, se puede utilizar los comandos `IF EXISTS` o `IF NOT EXISTS`:

  > `ALTER table person add column IF NOT EXISTS address VARCHAR;`

Otra consideración importante a tener en cuenta durante la creación de la tabla es si se debe agregar otro índice (además de `PRIMARY KEY`). Por ejemplo, podemos permitir una búsqueda sin distinción entre mayúsculas y minúsculas de nombres y apellidos agregando el siguiente índice:

  > `CREATE index idx_names on person ((lower(first_name), lower(last_name));`

Si la velocidad de búsqueda mejora, dejamos el índice en su lugar; si no, se puede quitar de la siguiente manera:

  > `DROP index idx_names;`

Lo eliminamos porque un índice tiene una sobrecarga de escrituras adicionales y espacio de almacenamiento.

También podemos eliminar una columna de una tabla si es necesario, de la siguiente manera:

  > `ALTER table person DROP column address;`

## Conectando a la base de datos

Hasta ahora, hemos usado una consola para ejecutar sentencias SQL. Las mismas declaraciones se pueden ejecutar desde el código Java utilizando la API JDBC también.

La gestión de datos, sin embargo, es otra cuestión. Entonces, a partir de ahora, utilizaremos el código Java para manipular los datos en una base de datos. Para hacer esto, primero debemos agregar la siguiente dependencia al archivo `pom.xml`:

```XML
<dependency>
  <groupId>org.postgresql</groupId>
  <artifactId>postgresql</artifactId>
  <version>42.2.12</version>
</dependency>
```

Esto coincide con la versión 12.2 de PostgreSQL que he instalado. Ahora podemos crear una conexión de base de datos a partir del código Java, de la siguiente manera:

```Java
public static Connection connectionWithDM() {
        String URL = "jdbc:postgresql://localhost/poo_db_test";

        Properties prop = new Properties();
        prop.put("user", "poo_student");
        // prop.put( "password", "secretPass123" );
        try {
            return DriverManager.getConnection(URL, prop);
        }
        catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }
```

El código anterior es solo un ejemplo de cómo crear una conexión usando la clase `java.sql.DriverManger`. La instrucción `prop.put ("contraseña", "secretPass123")` muestra cómo proporcionar una contraseña para la conexión utilizando la clase `java.util.Properties`. Sin embargo, no establecimos una contraseña cuando creamos el usuario estudiante, por lo que no la necesitamos.

Se pueden pasar muchos otros valores a `DriverManager` que configuran el comportamiento de la conexión. El nombre de las claves para las propiedades pasadas es el mismo para todas las bases de datos principales, pero algunas de ellas son específicas de la base de datos. Por lo tanto, lea la documentación del proveedor de su base de datos para obtener más detalles.

Alternativamente, para pasar solo usuario y contraseña, podríamos usar una versión sobrecargada de `DriverManager.getConnection (String url, String user, String password)`. Es una buena práctica mantener la contraseña encriptada. No vamos a demostrar cómo hacerlo, pero hay muchas guías disponibles en Internet que puede consultar.

Otra forma de conectarse a una base de datos es usar la interfaz `javax.sql.DataSource`. Su implementación se incluye en el mismo archivo `.jar` que el controlador de la base de datos. En el caso de PostgreSQL, hay dos clases que implementan la interfaz `DataSource`:

  > * `org.postgresql.ds.PGSimpleDataSource`
  > * `org.postgresql.ds.PGConnectionPoolDataSource`

Podemos usar estas clases en lugar de` DriverManager`. El siguiente código es un ejemplo de creación de una conexión de base de datos utilizando la clase `PGSimpleDataSource`:

```Java
public static Connection connectionWithPSG() {
        PGSimpleDataSource source = new PGSimpleDataSource();
        String[] serversNames = {"localhost"};

        source.setServerNames(serversNames);
        source.setDatabaseName("poo_db_test");
        source.setUser("poo_student");
        // source.setPassword("xxx");
        source.setLoginTimeout(10);

        Connection conn = null;

        try {
            return source.getConnection();
            // System.out.println(conn);
        }
        catch(SQLException e)
        {
            e.printStackTrace();
        }

        return  null;
    }
```

El uso de la clase `PGConnectionPoolDataSource` le permite crear un grupo de objetos `Connection` en la memoria, de la siguiente manera:

```Java
private static void connectionPool() {
        PGConnectionPoolDataSource source = new PGConnectionPoolDataSource();
        String[] serversNames = {"localhost"};

        source.setServerNames(serversNames);
        source.setDatabaseName("poo_db_test");
        source.setUser("poo_student");
        // source.setPassword("xxx");
        source.setLoginTimeout(10);

        try {
            PooledConnection conn = source.getPooledConnection();

            Set<Connection> pool = new HashSet<>();

            for (int i=0; i < 10; i++) {
                pool.add(conn.getConnection());
            }
        }
        catch(SQLException e) {
            e.printStackTrace();
        }
    }
```

Este es un método preferido porque crear un objeto `Connection` lleva tiempo. La agrupación le permite hacerlo por adelantado y luego reutilizar los objetos creados cuando sea necesario. Una vez que la conexión ya no es necesaria, puede devolverse al grupo y reutilizarse. El tamaño del grupo y otros parámetros se pueden establecer en un archivo de configuración (como `postgresql.conf` para PostgreSQL).

Sin embargo, no necesita administrar el grupo de conexiones usted mismo. Hay varios frameworks que pueden hacerlo por ti, como [HikariCP](https://brettwooldridge.github.io/HikariCP), [Vibur](http://www.vibur.org) y [Commons DBCP](https://commons.apache.org/proper/commons-dbcp) : son confiables y fáciles de usar.

Cualquiera que sea el método de creación de una conexión de base de datos que elijamos, lo vamos a ocultar dentro de un método y lo usaremos en todos nuestros ejemplos de código de la misma manera. Con el objeto de la clase `Connection` adquirida, ahora podemos acceder a la base de datos para agregar, leer, eliminar o modificar los datos almacenados.

## Liberación o cierre de la conexión

Mantener viva la conexión de la base de datos requiere una cantidad significativa de recursos, como memoria y CPU, por lo que es una buena idea cerrar la conexión y liberar los recursos asignados tan pronto como ya no los necesite. En el caso de la agrupación, el objeto `Connection`, cuando está cerrado, se devuelve a la agrupación y consume menos recursos.

Antes de Java 7, cerraba una conexión invocando el método `close()` en un bloque `finally`:

```Java
try {
    Connection conn = getConnection();
    //use object conn here
} finally { 
    if(conn != null){
        try {
            conn.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    } 
}
```

El código dentro del bloque `finally` siempre se ejecuta, ya sea que se lance o no la excepción dentro del bloque `try`. Sin embargo, desde Java 7, la construcción `try-with-resources` también hace el trabajo en cualquier objeto que implemente la interfaz `java.lang.AutoCloseable` o `java.io.Closeable`. Dado que el objeto `java.sql.Connection` implementa la interfaz `AutoCloseable`, podemos reescribir el fragmento de código anterior de la siguiente manera:

```Java
try (Connection conn = getConnection()) {
    //use object conn here
} catch(SQLException ex) {
    ex.printStackTrace();
}    
```

La cláusula catch es necesaria porque el recurso `AutoCloseable` arroja `java.sql.SQLException`.

## ABCC de datos

Hay cuatro tipos de sentencias SQL que leen o manipulan datos en la base de datos:
  
  > * `INSERT`: agrega datos a la base de datos
  > * `SELECT`: lee datos desde la base de datos
  > * `UPDATE`: actualiza datos existentes en la base de datos
  > * `DELETE`: borra datos de la base de datos

Se pueden agregar una o varias cláusulas diferentes a las declaraciones anteriores para identificar los datos que se solicitan (como la cláusula `WHERE`) y el orden en que se deben devolver los resultados (como la cláusula `ORDER`).

La conexión `JDBC` está representada por `java.sql.Connection`. Esto, entre otros, tiene los métodos necesarios para crear tres tipos de objetos que le permiten ejecutar sentencias SQL que proporcionan una funcionalidad diferente en el lado de la base de datos:

  > * `java.sql.Statement`: esto simplemente envía la declaración al servidor de la base de datos para su ejecución
  > * `java.sql.PreparedStatement`: almacena en caché la declaración con una determinada ruta de ejecución en el servidor de bases de datos al permitir que se ejecute varias veces con diferentes parámetros de manera eficiente
  > * `java.sql.CallableStatement`: ejecuta el procedimiento almacenado en la base de datos

### La instrucción `INSERT`

La instrucción `INSERT` crea (llena) datos en la base de datos y tiene el siguiente formato:

  > `INSERT into table_name (column1, column2, column3,...) values (value1, value2, value3,...);`

alternativamente, cuando se requiere ingresar varios registros a la base de datos, se puede utilizar de la siguiete manera:

  > ```SQL
  >  INSERT into table_name (column1, column2, column3,...) 
  >                          values (value1, value2, value3,... ), 
  >                          (value21, value22, value23,...),...;
  >```

## La instrucción `SELECT`

La instrucción `SELECT` tiene el siguiente formato

  > `SELECT column_name, column_name FROM table_name WHERE some_column = some_value;`

alternativamente, cuando todas las columnas son requeridas, se puede utilizar el siguiente formato:

  > `SELECT * from table_name WHERE some_column=some_value;`

Una definición más completa de la instrucción `WHERE` es la siguiente:

  > ```SQL
  > WHERE column_name operator value 
  > Operator: 
  > = Equal 
  > <> Not equal. In some versions of SQL, != 
  > >Greater than 
  > < Less than 
  > >= Greater than or equal 
  > <= Less than or equal IN Specifies multiple possible values for a column 
  > LIKE Specifies the search pattern 
  > BETWEEN Specifies the inclusive range of values in a column

El operador de construcción `column_name` puede ser combinado utilizando los operadores lógicos `AND` y `OR`, y agrupados con parentesis `()`.

Por ejemplo, el siguiente método retorna desde la base de datos todos los nombres (separados por un espacio) desde la tabla `Person`:

```Java
public static String selectAllFirstNames() {
        String result = "";

        Connection conn = connectionWithPSG();
        try (conn; Statement st = conn.createStatement()) {
            ResultSet rs = st.executeQuery("SELECT first_name FROM person");
            while (rs.next()) {
                // result += rs.getString(1) + " ";
                result += rs.getString("first_name") + " ";
            }
        }
        catch (SQLException e) {
            e.printStackTrace();
        }

        return result;
    }
```

El método `getString(int position)` de la interfaz `ResultSet` extrae el valor de String de la posición 1 (la primera en la lista de columnas en la instrucción SELECT). Hay getters similares para todos los tipos primitivos: `getInt(int position)`, `getByte(int position)` y más.

También es posible extraer el valor del objeto `ResultSet` utilizando el nombre de la columna. En nuestro caso, será `getString("first_name")`. Este método para obtener valores es especialmente útil cuando la instrucción `SELECT` es la siguiente:

  > `select * from person;`

Sin embargo, tenga en cuenta que extraer valores del objeto `ResultSet` usando el nombre de la columna es menos eficiente. Sin embargo, la diferencia en el rendimiento es muy pequeña y solo se vuelve importante cuando la operación se lleva a cabo muchas veces. Solo los procesos reales de medición y prueba pueden determinar si la diferencia es significativa para su aplicación o no. Extraer valores por el nombre de la columna es especialmente atractivo porque proporciona una mejor legibilidad del código, que a la larga vale la pena durante el mantenimiento de la aplicación.

Hay muchos otros métodos útiles en la interfaz `ResultSet`. Si su aplicación lee datos de una base de datos, le recomendamos que lea la documentación oficial de la instrucción `SELECT` y la interfaz `ResultSet`.

## La instrucción `UPDATE`

Los datos pueden ser modificados por la instrucción `UPDATE`, de la siguiente forma:

  > `UPDATE table_name SET column1=value1,column2=value2,... WHERE clause;`

Podemos usar esta declaración para cambiar el nombre en uno de los registros del valor original, John, a un nuevo valor, Jim:

  > `update person set first_name = 'Jim' where last_name = 'John';`


sin la clausula `WHERE`, todos los registros de la tabla serían afectados.

## La instrucción `Delete`

Para borrar o remover registros de la tabla, utilizamos la instrucción `DELETE` de la siguente forma:

  > `DELETE FROM table_name WHERE clause;`

De la misma forma que pasa en la instrucción `UPDATE`, sin las clausula `WHERE` todos los registros de la tabla serían eliminados. En el caso de la tabla `PERSON`, podemos eliminar todos los registros utilizando:

  > `DELETE FROM person;`

Adicionalmente, podemos eliminar todos los registros que tengan `Jim` como valor en el campo `first_name`:

  > `delete from person where first_name = 'Jim';`

---

## Utilizando `Statements`

La interfaz `java.sql.Statement` offrece los siguiente métodos para la ejecución de instrucciones SQL:

  > * `boolean execute(String sql)`: Esto devuelve verdadero si la instrucción ejecutada devuelve datos (dentro del objeto `java.sql.ResultSet`) que se pueden recuperar utilizando el método `ResultSet getResultSet()` de la interfaz `java.sql.Statement`. Alternativamente, devuelve falso si la instrucción ejecutada no devuelve datos (para la instrucción `INSERT` o la instrucción `UPDATE`) y la llamada posterior al método `int getUpdateCount()` de la interfaz `java.sql.Statement` devuelve el número de las filas afectadas.

Analizamos los siguientes ejemplos:

```Java
private static void insertExecute() {
    System.out.println("\ninsertExecute():");
    String sql = "insert into person (first_name, last_name, dob) values ('Bill', 'Gray', '1980-01-27')";
    Connection conn = connectionWithPSG();
    try (conn; Statement st = conn.createStatement()) {
        System.out.println(st.execute(sql));
        System.out.println(st.getResultSet() == null);
        System.out.println(st.getUpdateCount());
    }
    catch (SQLException e) {
        e.printStackTrace();
    }

    System.out.println(selectAllFirstNames());
}
```

```Java
private static void selectExecute() {
    System.out.println("\nselectExecute(): ");
    String sql = "SELECT first_name FROM person";
    Connection conn = connectionWithPSG();
    try(conn; Statement st = conn.createStatement()) {
        System.out.println(st.execute(sql));
        ResultSet rs = st.getResultSet();
        System.out.println(rs == null);
        System.out.println(st.getUpdateCount());

        while (rs.next()) {
            System.out.println(rs.getString(1) + " ");
        }
    }
    catch(SQLException e) {
        e.printStackTrace();
    }
}
```

```Java
private static void updateExecute() {
    System.out.println("\nupdateExecute():");

    String sql = "update person set first_name = 'Adam' where first_name = 'Bill'";
    Connection conn = connectionWithPSG();
    try (conn; Statement st = conn.createStatement()) {
        System.out.println(st.execute(sql));
        System.out.println(st.getResultSet() == null);
        System.out.println(st.getUpdateCount());
    }
    catch (SQLException e) {
        e.printStackTrace();
    }
    System.out.println(selectAllFirstNames());
}
```

```Java
private static void deleteExecute() {
    System.out.println("\ndeleteExecute()");
    String sql = "delete from person where first_name = 'Adam'";
    Connection conn = connectionWithPSG();
    try(conn; Statement st = conn.createStatement()) {
        System.out.println(st.execute(sql));
        System.out.println(st.getResultSet() == null);
        System.out.println(st.getUpdateCount());
    } catch (SQLException e) {
        e.printStackTrace();
    }

    System.out.println(selectAllFirstNames());
}
```

de los resultados podemos resumir de la siguiente manera:

|                           | INSERT | SELECT | UPDATE | DELETE |
|---------------------------|--------|--------|--------|--------|
| st.execute(sql)           | false  | true   | false  | false  |
| st.getResultSet() == null | true   | false  | true   | true   |
| st.getUpdateCount()       | 1      | -1     | 1      | 1      |

---

  > * `ResultSet executeQuery(String sql)`: Esto devuelve datos como un objeto `java.sql.ResultSet` (la instrucción SQL utilizada con este método suele ser una instrucción `SELECT`). El método `ResultSet getResultSet()` de la interfaz `java.sql.Statement` no devuelve datos, mientras que el método `int getUpdateCount()` de la interfaz `java.sql.Statement` devuelve -1.
  > `int executeUpdate(String sql)`: Esto devuelve el número de filas afectadas (se espera que la instrucción SQL ejecutada sea la instrucción `UPDATE` o la instrucción `DELETE`). El método `int getUpdateCount()` de la interfaz `java.sql.Statement` devuelve el mismo número; la llamada posterior al método `ResultSet getResultSet()` de la interfaz `java.sql.Statement` devuelve nulo.

Analizamos los siguiente códigos

```Java
private static void insertExecuteQuery() {
  System.out.println("\ninsertExecuteQuery()");

  String sql = "insert into person (first_name, last_name, dob) values ('Bill', 'Grey', '1980-01-27')";

  Connection conn = connectionWithPSG();
  try (conn; Statement st = conn.createStatement()) {
      st.executeQuery(sql);
  } catch (SQLException throwables) {
      throwables.printStackTrace();
  }
  System.out.println(selectAllFirstNames());
}
```
```Java
private static void selectExecuteQuery() {
    System.out.println("\nSelectExecuteQuery()");

    String sql = "select first_name from person";
    Connection conn = connectionWithPSG();
    try (conn; Statement st = conn.createStatement()) {
        ResultSet rs1 = st.executeQuery(sql);
        System.out.println(rs1 == null);
        ResultSet rs2 = st.getResultSet();
        System.out.println(rs2 == null);
        System.out.println(st.getUpdateCount());

//            System.out.println(rs1 == rs2);
//            System.out.println(rs1.equals(rs2));
//            System.out.println(rs1 + " == " + rs2);

        while (rs1.next()) {
            System.out.println(rs1.getString(1));
        }

        while (rs2.next()) {
            System.out.println(rs2.getString(1));
        }
    } catch (SQLException throwables) {
        throwables.printStackTrace();
    }
}
```

```Java
private static void updateExecuteQuery() {
    System.out.println("\nupdateExecuteQuery()");

    String sql = "update person set first_name = 'Adam' where first_name = 'Bill'";
    Connection conn = connectionWithPSG();
    try (conn; Statement st = conn.createStatement()) {
        st.executeQuery(sql);
    } catch (SQLException throwables) {
        throwables.printStackTrace();
    }

    System.out.println(selectAllFirstNames());
}
```

```Java
private static void deleteExecuteQuery() {
    System.out.println("\ndeleteExecuteQuery()");

    String sql = "delete from person where first_name = 'Adam'";
    Connection conn = connectionWithPSG();
    try (conn; Statement st = conn.createStatement()) {
        st.executeQuery(sql);
    } catch (SQLException throwables) {
        throwables.printStackTrace();
    }

    System.out.println(selectAllFirstNames());
}
```

Podemos resumir de la siguiente manera:

|                           | INSERT        | SELECT    | UPDATE        | DELETE        |
|---------------------------|---------------|-----------|---------------|---------------|
| st.executeQuery(sql)      | PSQLException | ResultSet | PSQLException | PSQLException |
| st.getResultSet() == null | Na            | false     | Na            | Na            |
| st.getUpdateCount()       | Na            | -1        | Na            | Na            |


  > * `int executeUpdate(String sql)`: Esto devuelve el número de filas afectadas (se espera que la instrucción SQL ejecutada sea la instrucción `UPDATE` o la instrucción `DELETE`). El método `int getUpdateCount()` de la interfaz `java.sql.Statement` devuelve el mismo número; la llamada posterior al método `ResultSet getResultSet()` de la interfaz `java.sql.Statement` devuelve nulo.

Analizamos los siguientes códigos:

```Java
private static void insertExecuteUpdate() {
    System.out.println("\ninsertExecuteUpdate()");

    String sql = "insert into person (first_name, last_name, dob) values ('Bill', 'Grey', '1980-01-27')";
    Connection conn = connectionWithPSG();
    try (conn; Statement st = conn.createStatement()) {
        System.out.println(st.executeUpdate(sql));
        System.out.println(st.getResultSet() == null);
        System.out.println(st.getUpdateCount());
    } catch (SQLException throwables) {
        throwables.printStackTrace();
    }

    System.out.println(selectAllFirstNames());
}
```

```JAVA
private static void selectExecuteUpdate() {
    System.out.println("\nselectExecuteUpdate()");

    String sql = "select first_name from person";
    Connection conn = connectionWithPSG();
    try (conn; Statement st = conn.createStatement()) {
        st.executeUpdate(sql);
        // no tiene sentido poner otra instrucción porque lanza una excepción
    } catch (SQLException throwables) {
        throwables.printStackTrace();
    }
}
```

```JAVA
private static void updateExecuteUpdate() {
    System.out.println("\nupdateExecuteUpdate()");
    String sql = "update person set first_name = 'Adam' where first_name = 'Bill'";
    Connection conn = connectionWithPSG();
    try (conn; Statement st = conn.createStatement()) {
        System.out.println(st.executeUpdate(sql));
        System.out.println(st.getResultSet() == null);
        System.out.println(st.getUpdateCount());
    } catch (SQLException throwables) {
        throwables.printStackTrace();
    }
    System.out.println(selectAllFirstNames());
}
```

```JAVA
private static void deleteExecuteUpdate() {
    System.out.println("\ndeleteExecuteUpdate():");
    String sql = "delete from person where first_name = 'Adam'";
    Connection conn = connectionWithPSG();
    try (conn; Statement st = conn.createStatement()) {
        System.out.println(st.executeUpdate(sql));
        System.out.println(st.getResultSet() == null);
        System.out.println(st.getUpdateCount());
    } catch (SQLException throwables) {
        throwables.printStackTrace();
    }
    System.out.println(selectAllFirstNames());
}
```

Una vez ejecutados podemos resumir de la siguiente manera los resultados:

|                           | INSERT | SELECT        | UPDATE | DELETE |
|---------------------------|--------|---------------|--------|--------|
| st.executeUpdate(sql)     | 1      | PSQLException | 1      | 1      |
| st.getResultSet() == null | true   | Na            | true   | true   |
| st.getUpdateCount()       | 1      | Na            | 1      | 1      |

Por demos concluir que `Statement.executeQuery` es preferible utilizarla con operaciones no transaccionales (`SELECT`), mientras que `Statement.executeUpdate` en operaciones transaccionales (`INSERT`, `UPDATE`, `DELETE`). el Método `execute(String)` es generalizado.

---

## Utilizando `PreparedStatement`

`PreparedStatement` es una subinterfaz de `Statement`. Esto significa que se puede usar en cualquier lugar donde se use `Statement` de declaración. La diferencia es que `PreparedStatement` se almacena en caché en la base de datos en lugar de compilarse cada vez que se invoca. De esta manera, se ejecuta eficientemente varias veces para diferentes valores de entrada. Similar a `Statement`, puede crearse mediante el método `prepareStatement()` utilizando el mismo objeto `Connection`.

Dado que se puede usar la misma instrucción SQL para crear `Statement` y `PreparedStatement`, es una buena idea usar `PreparedStatement` para cualquier instrucción SQL que se llame varias veces porque funciona mejor que `Statement` en el lado de la base de datos. Para hacer esto, todo lo que necesitamos cambiar son estas dos líneas de los ejemplo anteriores:

```Java
try (conn; Statement st = conn.createStatement()) { 
     ResultSet rs = st.executeQuery(sql);
```

En lugar del código anterior, se utilizaría la instrucción `conn.PreparedStatement`

```Java
try (conn; PreparedStatement st = conn.prepareStatement(sql)) { 
     ResultSet rs = st.executeQuery();
```

Para crear una clase `PreparedStatement` con parámetros, puede sustituir los valores de entrada con el símbolo de signo de interrogación (`?`); por ejemplo, podemos crear el siguiente método:

```Java
private static List<Person> selectPersonsByFirstName(String searchName) {
    List<Person> list = new ArrayList<>();
    Connection conn = App.connectionWithPSG();
    String sql = "select id, first_name, last_name, dob from person where first_name = ?";
    try (conn; PreparedStatement st = conn.prepareStatement(sql)) {
        st.setString(1, searchName);
        ResultSet rs = st.executeQuery();
        while (rs.next()) {
            list.add(new Person(rs.getInt("id"), rs.getString("first_name"), rs.getString("last_name"), rs.getDate("dob").toLocalDate()));
        }
    }
    catch(SQLException e) {
        e.printStackTrace();
    }

    return list;
}
```

La base de datos compila la clase `PreparedStatement` como una plantilla y la almacena sin ejecutarla. Luego, cuando la aplicación la utiliza más tarde, el valor del parámetro se pasa a la plantilla, que se puede ejecutar inmediatamente sin la sobrecarga de la compilación, ya que ya se ha hecho.

Otra ventaja de una declaración preparada es que está mejor protegida de un ataque de inyección SQL porque los valores se pasan usando un protocolo diferente y la plantilla no se basa en la entrada externa.

Si una declaración preparada se usa solo una vez, puede ser más lenta que una declaración regular, pero la diferencia puede ser insignificante. En caso de duda, pruebe el rendimiento y vea si es aceptable para su aplicación; la mayor seguridad podría valer la pena.

---

## Utilizando `CallableStatement`

La interfaz `CallableStatement` (que amplía la interfaz `PreparedStatement`) se puede usar para ejecutar un *procedimiento almacenado*, aunque algunas bases de datos le permiten llamar a un procedimiento almacenado utilizando una interfaz `Statement` o `PreparedStatement`, pero por estandarización se prefiere utilizar `CallableStatement` en estos casos. El método `prepareCall()` crea un objeto `CallableStatement` y puede tener parámetros de tres tipos

  > * `IN` Para una entrada Input
  > * `OUT` Para el resultado
  > * `IN OUT` para ambos, entrada y salid de un valor

El parámetro `IN` se puede configurar de la misma manera que los parámetros de `PreparedStatement`, mientras que el parámetro `OUT` debe registrarse mediante el método `registerOutParameter()` de `CallableStatement`.

Vale la pena señalar que ejecutar un procedimiento almacenado desde Java mediante programación es una de las áreas menos estandarizadas. *PostgreSQL*, por ejemplo, no admite procedimientos almacenados directamente, pero se pueden invocar como funciones, que se han modificado para este fin al interpretar los parámetros `OUT` como valores de retorno. Oracle, por otro lado, permite que los parámetros `OUT` también funcionen.

Es por eso que las siguientes diferencias entre las funciones de la base de datos y los procedimientos almacenados pueden servir solo como pautas generales y no como definiciones formales:

  > * Una función tiene un valor de retorno, pero no permite parámetros `OUT` (excepto para algunas bases de datos) y puede usarse en una declaración SQL.
  > * Un procedimiento almacenado no tiene un valor de retorno (a excepción de algunas bases de datos); permite parámetros `OUT` (para la mayoría de las bases de datos) y puede ejecutarse utilizando la interfaz JDBC `CallableStatement`.

Puede consultar la documentación de la base de datos para aprender a ejecutar un procedimiento almacenado.

Dado que los procedimientos almacenados se compilan y almacenan en el servidor de la base de datos, el método `execute()` de `CallableStatement` funciona mejor para la misma instrucción SQL que el método correspondiente de la interfaz `Statement` o `PreparedStatement`. Esta es una de las razones por las que una gran cantidad de código Java a veces se reemplaza por uno o varios procedimientos almacenados que incluso incluyen lógica de negocios. Sin embargo, no hay una respuesta correcta para cada caso y problema, por lo que nos abstendremos de hacer recomendaciones específicas, excepto para repetir el mantra familiar sobre el valor de las pruebas y la claridad del código que está escribiendo.

Llamemos, por ejemplo, a la función `replace(String origText, from substr1, to substr2)` que viene con la instalación de PostgreSQL. Busca el primer parámetro (`String origText`) y reemplaza todas las subcadenas que coinciden con el segundo parámetro (`from substr1`) utilizando la cadena proporcionada por el tercer parámetro (`String substr2`). El siguiente método Java ejecuta esta función usando `CallableStatement`:

```Java
private static String replace(String origText, String substr1, String substr2) {
    String result = "";
    String sql = "{ ? = call replace(?, ?, ?) }";
    Connection conn = App.connectionWithPSG();
    try (conn; CallableStatement st = conn.prepareCall(sql)) {
        st.registerOutParameter(1, Types.VARCHAR);
        st.setString(2, origText);
        st.setString(3, substr1);
        st.setString(4, substr2);
        st.execute();
        result = st.getString(1);
    } catch (SQLException throwables) {
        throwables.printStackTrace();
    }

    return result;
}
```

Ahora podemos llamar al método:

```Java
String result = replace("That is original text",
                                "original text", "the result");
System.out.println(result);  //prints: That is the result
```

Un procedimiento almacenado puede ser sin ningún parámetro, solo con parámetros `IN`, solo con parámetros `OUT` o con ambos. El resultado puede ser uno o varios valores, o un objeto `ResultSet`. Puede encontrar la sintaxis del SQL para la creación de funciones en la documentación de su base de datos.