/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

/**
 *
 * @author davi2
 */
import java.io.PrintWriter;
import java.util.*;

/**
 * Clase principal que genera archivos de prueba para el proyecto
 * "Generación y Clasificación de Datos".
 * Produce archivos CSV de productos, vendedores y ventas individuales.
 */
public class GenerateInfoFiles {

    // Listas de datos de prueba
    private static final String[] NOMBRES = {"Carlos", "Ana", "Luis", "Maria", "Juan"};
    private static final String[] APELLIDOS = {"Gomez", "Perez", "Rodriguez", "Martinez"};
    private static final String[] TIPOS_DOC = {"CC", "CE", "TI"};
    private static final String[] PRODUCTOS_NOMBRES = {"Laptop", "Mouse", "Teclado", "Monitor"};
    private static final double[] PRODUCTOS_PRECIOS = {2500000.50, 80000.00, 150000.99, 950000.00};

    public static void main(String[] args) {
        try {
            System.out.println("Generando archivos de prueba...");
            List<Producto> productos = crearProductos();
            escribirProductos(productos);

            List<Vendedor> vendedores = crearVendedores(3); // ejemplo con 3 vendedores
            escribirVendedores(vendedores);

            System.out.println("Se generaron los archivos: productos.csv, vendedores.csv y los archivos individuales de cada vendedor.");
        } catch (Exception e) {
            System.err.println("ERROR: " + e.getMessage());
        }
    }

    /** Genera lista de productos de prueba */
    private static List<Producto> crearProductos() {
        List<Producto> lista = new ArrayList<>();
        for (int i = 0; i < PRODUCTOS_NOMBRES.length; i++) {
            lista.add(new Producto(i + 1, PRODUCTOS_NOMBRES[i], PRODUCTOS_PRECIOS[i]));
        }
        return lista;
    }

    /** Escribe archivo productos.csv con encabezado */
    private static void escribirProductos(List<Producto> productos) throws Exception {
        try (PrintWriter writer = new PrintWriter("productos.csv", "UTF-8")) {
            writer.println("ID;Nombre;Precio");
            for (Producto p : productos) {
                writer.println(p.toCSV());
            }
        }
    }

    /** Genera lista de vendedores de prueba y sus archivos de ventas */
    private static List<Vendedor> crearVendedores(int cantidad) {
        Random rand = new Random();
        List<Vendedor> lista = new ArrayList<>();
        for (int i = 0; i < cantidad; i++) {
            long id = 100000000 + rand.nextInt(900000000);
            String nombre = NOMBRES[rand.nextInt(NOMBRES.length)];
            String apellido = APELLIDOS[rand.nextInt(APELLIDOS.length)];
            String tipoDoc = TIPOS_DOC[rand.nextInt(TIPOS_DOC.length)];
            Vendedor v = new Vendedor(tipoDoc, id, nombre, apellido);
            lista.add(v);
            generarArchivoVentasPorVendedor(v, rand);
        }
        return lista;
    }

    /** Escribe archivo vendedores.csv con encabezado */
    private static void escribirVendedores(List<Vendedor> vendedores) throws Exception {
        try (PrintWriter writer = new PrintWriter("vendedores.csv", "UTF-8")) {
            writer.println("TipoDoc;ID;Nombre;Apellido");
            for (Vendedor v : vendedores) {
                writer.println(v.toCSV());
            }
        }
    }

    /** Genera archivo de ventas individuales para cada vendedor */
    private static void generarArchivoVentasPorVendedor(Vendedor v, Random rand) {
        String fileName = "vendedor_" + v.id + ".csv";
        try (PrintWriter writer = new PrintWriter(fileName, "UTF-8")) {
            writer.println("TipoDoc;ID");
            writer.println(v.tipoDoc + ";" + v.id);
            writer.println("ProductoID;Cantidad");
            int ventas = rand.nextInt(4) + 2; // entre 2 y 5 ventas
            for (int i = 0; i < ventas; i++) {
                Venta venta = new Venta(rand.nextInt(PRODUCTOS_NOMBRES.length) + 1, rand.nextInt(10) + 1);
                writer.println(venta.toCSV());
            }
        } catch (Exception e) {
            System.err.println("Error creando archivo de ventas: " + e.getMessage());
        }
    }
}

/** Clase que representa un producto */
class Producto {
    int id;
    String nombre;
    double precio;

    Producto(int id, String nombre, double precio) {
        this.id = id;
        this.nombre = nombre;
        this.precio = precio;
    }

    String toCSV() {
        return id + ";" + nombre + ";" + precio;
    }
}

/** Clase que representa un vendedor */
class Vendedor {
    String tipoDoc;
    long id;
    String nombre;
    String apellido;

    Vendedor(String tipoDoc, long id, String nombre, String apellido) {
        this.tipoDoc = tipoDoc;
        this.id = id;
        this.nombre = nombre;
        this.apellido = apellido;
    }

    String toCSV() {
        return tipoDoc + ";" + id + ";" + nombre + ";" + apellido;
    }
}

/** Clase que representa una venta */
class Venta {
    int productoId;
    int cantidad;

    Venta(int productoId, int cantidad) {
        this.productoId = productoId;
        this.cantidad = cantidad;
    }

    String toCSV() {
        return productoId + ";" + cantidad;
    }
}



