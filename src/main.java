/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

/**
 *
 * @author davi2
 */
import java.io.*;
import java.util.*;

/**
 * Clase main que procesa los archivos generados por GenerateInfoFiles
 * y produce reportes de ventas y productos.
 */
public class main {

    public static void main(String[] args) {
        try {
            System.out.println("Procesando archivos de entrada...");

            // 1. Leer productos
            Map<Integer, Producto> productos = leerProductos("productos.csv");

            // 2. Leer vendedores
            Map<Long, Vendedor> vendedores = leerVendedores("vendedores.csv");

            // 3. Procesar archivos individuales de cada vendedor
            procesarVentas(vendedores, productos);

            // 4. Generar reporte de vendedores ordenados por dinero
            generarReporteVendedores(vendedores);

            // 5. Generar reporte de productos ordenados por cantidad
            generarReporteProductos(productos);

            System.out.println("¡Reportes generados exitosamente!");
        } catch (Exception e) {
            System.err.println("ERROR: " + e.getMessage());
        }
    }

    /** Lee archivo de productos y devuelve mapa con ID -> Producto */
    private static Map<Integer, Producto> leerProductos(String archivo) throws Exception {
        Map<Integer, Producto> productos = new HashMap<>();
        try (Scanner sc = new Scanner(new File(archivo))) {
            sc.nextLine(); // saltar encabezado
            while (sc.hasNextLine()) {
                String[] partes = sc.nextLine().split(";");
                int id = Integer.parseInt(partes[0]);
                String nombre = partes[1];
                double precio = Double.parseDouble(partes[2]);
                productos.put(id, new Producto(id, nombre, precio));
            }
        }
        return productos;
    }

    /** Lee archivo de vendedores y devuelve mapa con ID -> Vendedor */
    private static Map<Long, Vendedor> leerVendedores(String archivo) throws Exception {
        Map<Long, Vendedor> vendedores = new HashMap<>();
        try (Scanner sc = new Scanner(new File(archivo))) {
            sc.nextLine(); // saltar encabezado
            while (sc.hasNextLine()) {
                String[] partes = sc.nextLine().split(";");
                String tipoDoc = partes[0];
                long id = Long.parseLong(partes[1]);
                String nombre = partes[2];
                String apellido = partes[3];
                vendedores.put(id, new Vendedor(tipoDoc, id, nombre, apellido));
            }
        }
        return vendedores;
    }

    /** Procesa archivos de ventas de cada vendedor */
    private static void procesarVentas(Map<Long, Vendedor> vendedores, Map<Integer, Producto> productos) throws Exception {
        for (Vendedor v : vendedores.values()) {
            String archivo = "vendedor_" + v.id + ".csv";
            try (Scanner sc = new Scanner(new File(archivo))) {
                sc.nextLine(); // encabezado TipoDoc;ID
                sc.nextLine(); // línea con tipoDoc;id
                sc.nextLine(); // encabezado ProductoID;Cantidad
                while (sc.hasNextLine()) {
                    String[] partes = sc.nextLine().split(";");
                    int productoId = Integer.parseInt(partes[0]);
                    int cantidad = Integer.parseInt(partes[1]);

                    Producto p = productos.get(productoId);
                    if (p != null) {
                        double totalVenta = p.precio * cantidad;
                        v.totalRecaudado += totalVenta;
                        p.cantidadVendida += cantidad;
                    }
                }
            }
        }
    }

    /** Genera reporte de vendedores ordenados por dinero recaudado */
    private static void generarReporteVendedores(Map<Long, Vendedor> vendedores) throws Exception {
        List<Vendedor> lista = new ArrayList<>(vendedores.values());
        lista.sort((a, b) -> Double.compare(b.totalRecaudado, a.totalRecaudado));

        try (PrintWriter writer = new PrintWriter("reporte_vendedores.csv", "UTF-8")) {
            writer.println("Nombre;Apellido;TotalRecaudado");
            for (Vendedor v : lista) {
                writer.println(v.nombre + ";" + v.apellido + ";" + v.totalRecaudado);
            }
        }
    }

    /** Genera reporte de productos ordenados por cantidad vendida */
    private static void generarReporteProductos(Map<Integer, Producto> productos) throws Exception {
        List<Producto> lista = new ArrayList<>(productos.values());
        lista.sort((a, b) -> Integer.compare(b.cantidadVendida, a.cantidadVendida));

        try (PrintWriter writer = new PrintWriter("reporte_productos.csv", "UTF-8")) {
            writer.println("Nombre;Precio;CantidadVendida");
            for (Producto p : lista) {
                writer.println(p.nombre + ";" + p.precio + ";" + p.cantidadVendida);
            }
        }
    }
}

/** Clase auxiliar Producto */
class Producto {
    int id;
    String nombre;
    double precio;
    int cantidadVendida = 0; // campo necesario para acumular ventas

    Producto(int id, String nombre, double precio) {
        this.id = id;
        this.nombre = nombre;
        this.precio = precio;
    }
}

/** Clase auxiliar Vendedor */
class Vendedor {
    String tipoDoc;
    long id;
    String nombre;
    String apellido;
    double totalRecaudado = 0; // campo necesario para acumular dinero

    Vendedor(String tipoDoc, long id, String nombre, String apellido) {
        this.tipoDoc = tipoDoc;
        this.id = id;
        this.nombre = nombre;
        this.apellido = apellido;
    }
}
