/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 */

package com.utp.clinicaadmin;

import java.io.*;
import java.util.*;

public class Main {
    private static final String BASE_PATH = "BaseDeDatos/";
    private static final String MEDICOS_FILE = BASE_PATH + "medicos.txt";
    private static final String CLIENTES_FILE = BASE_PATH + "clientes.txt";
    private static Scanner scanner = new Scanner(System.in);
    
    public static void main(String[] args) {
        // Crear archivos si no existen
        crearArchivosIniciales();
        
        System.out.println("=== SISTEMA CLÍNICA - PERSONAL ADMINISTRATIVO ===");
        
        if (iniciarSesion()) {
            mostrarMenuPrincipal();
        }
    }
    
    private static void crearArchivosIniciales() {
        try {
            // Crear carpeta BaseDeDatos si no existe
            File baseDir = new File(BASE_PATH);
            if (!baseDir.exists()) {
                baseDir.mkdirs();
            }
            // Crear archivos vacíos si no existen
            File[] archivos = {
                new File(MEDICOS_FILE),
                new File(CLIENTES_FILE),
                new File(BASE_PATH + "citas.txt"),
                new File(BASE_PATH + "historial.txt"),
                new File(BASE_PATH + "recetas.txt")
            };
            for (File archivo : archivos) {
                if (!archivo.exists()) {
                    archivo.createNewFile();
                    System.out.println("Archivo creado: " + archivo.getAbsolutePath());
                }
            }
        } catch (IOException e) {
            System.out.println("Error al crear archivos: " + e.getMessage());
        }
    }
    
    private static boolean iniciarSesion() {
        System.out.print("Usuario: ");
        String usuario = scanner.nextLine();
        System.out.print("Contraseña: ");
        String password = scanner.nextLine();
        
        if ("admin".equals(usuario) && "admin123".equals(password)) {
            System.out.println("\n✅ Sesión iniciada correctamente");
            return true;
        } else {
            System.out.println("\n❌ Credenciales incorrectas");
            return false;
        }
    }
    
    private static void
    mostrarMenuPrincipal() {
        int opcion;
        do {
            System.out.println("\n=== MENÚ PRINCIPAL ===");
            System.out.println("1. Gestionar Médicos");
            System.out.println("2. Gestionar Pacientes");
            System.out.println("3. Ver Estadísticas");
            System.out.println("0. Salir");
            System.out.print("Seleccione opción: ");
            
            opcion = scanner.nextInt();
            scanner.nextLine(); // Limpiar buffer
            
            switch (opcion) {
                case 1:
                    gestionarMedicos();
                    break;
                case 2:
                    gestionarPacientes();
                    break;
                case 3:
                    verEstadisticas();
                    break;
                case 0:
                    System.out.println("¡Hasta luego!");
                    break;
                default:
                    System.out.println("Opción inválida");
            }
        } while (opcion != 0);
    }
    
    private static void gestionarMedicos() {
        int opcion;
        do {
            System.out.println("\n=== GESTIÓN DE MÉDICOS ===");
            System.out.println("1. Listar Médicos");
            System.out.println("2. Crear Médico");
            System.out.println("3. Editar Médico");
            System.out.println("4. Eliminar Médico");
            System.out.println("0. Volver");
            System.out.print("Seleccione opción: ");
            
            opcion = scanner.nextInt();
            scanner.nextLine();
            
            switch (opcion) {
                case 1:
                    listarMedicos();
                    break;
                case 2:
                    crearMedico();
                    break;
                case 3:
                    editarMedico();
                    break;
                case 4:
                    eliminarMedico();
                    break;
            }
        } while (opcion != 0);
    }
    
    private static void listarMedicos() {
        System.out.println("\n=== LISTA DE MÉDICOS ===");
        System.out.println("\n MEDICOS_FILE ===" + MEDICOS_FILE);

        try (BufferedReader br = new BufferedReader(new FileReader(MEDICOS_FILE))) {
            String linea;
            boolean hayMedicos = false;
            
            while ((linea = br.readLine()) != null) {
                String[] datos = linea.split("\\|");
                if (datos.length >= 6) {
                    System.out.printf("ID: %s | Nombre: %s %s | Especialidad: %s | Sexo: %s | DNI: %s\n",
                            datos[0], datos[1], datos[2], datos[3], datos[4], datos[5]);
                    hayMedicos = true;
                }
            }
            
            if (!hayMedicos) {
                System.out.println("No hay médicos registrados.");
            }
        } catch (IOException e) {
            System.out.println("Error al leer archivo: " + e.getMessage());
        }
    }
    
    private static void crearMedico() {
        System.out.println("\n=== CREAR MÉDICO ===");
        
        System.out.print("ID del médico: ");
        String id = scanner.nextLine();
        
        // Verificar que el ID no exista
        if (existeMedico(id)) {
            System.out.println("❌ Ya existe un médico con ese ID");
            return;
        }
        
        System.out.print("Nombre: ");
        String nombre = scanner.nextLine();
        
        System.out.print("Apellidos: ");
        String apellidos = scanner.nextLine();
        
        System.out.print("Especialidad: ");
        String especialidad = scanner.nextLine();
        
        System.out.print("Sexo (M/F): ");
        String sexo = scanner.nextLine();
        
        System.out.print("DNI: ");
        String dni = scanner.nextLine();
        
        try (PrintWriter pw = new PrintWriter(new FileWriter(MEDICOS_FILE, true))) {
            pw.println(id + "|" + nombre + "|" + apellidos + "|" + especialidad + "|" + sexo + "|" + dni);
            System.out.println("✅ Médico creado correctamente");
        } catch (IOException e) {
            System.out.println("Error al guardar médico: " + e.getMessage());
        }
    }
    
    private static void editarMedico() {
        System.out.println("\n=== EDITAR MÉDICO ===");
        System.out.print("ID del médico a editar: ");
        String idBuscar = scanner.nextLine();
        
        List<String> lineas = new ArrayList<>();
        boolean encontrado = false;
        
        try (BufferedReader br = new BufferedReader(new FileReader(MEDICOS_FILE))) {
            String linea;
            while ((linea = br.readLine()) != null) {
                String[] datos = linea.split("\\|");
                if (datos.length >= 6 && datos[0].equals(idBuscar)) {
                    System.out.println("Médico encontrado: " + datos[1] + " " + datos[2]);
                    
                    System.out.print("Nuevo nombre (actual: " + datos[1] + "): ");
                    String nuevoNombre = scanner.nextLine();
                    if (nuevoNombre.isEmpty()) nuevoNombre = datos[1];
                    
                    System.out.print("Nuevos apellidos (actual: " + datos[2] + "): ");
                    String nuevosApellidos = scanner.nextLine();
                    if (nuevosApellidos.isEmpty()) nuevosApellidos = datos[2];
                    
                    System.out.print("Nueva especialidad (actual: " + datos[3] + "): ");
                    String nuevaEspecialidad = scanner.nextLine();
                    if (nuevaEspecialidad.isEmpty()) nuevaEspecialidad = datos[3];
                    
                    System.out.print("Nuevo sexo (actual: " + datos[4] + "): ");
                    String nuevoSexo = scanner.nextLine();
                    if (nuevoSexo.isEmpty()) nuevoSexo = datos[4];
                    
                    System.out.print("Nuevo DNI (actual: " + datos[5] + "): ");
                    String nuevoDni = scanner.nextLine();
                    if (nuevoDni.isEmpty()) nuevoDni = datos[5];
                    
                    linea = idBuscar + "|" + nuevoNombre + "|" + nuevosApellidos + "|" + nuevaEspecialidad + "|" + nuevoSexo + "|" + nuevoDni;
                    encontrado = true;
                }
                lineas.add(linea);
            }
        } catch (IOException e) {
            System.out.println("Error al leer archivo: " + e.getMessage());
            return;
        }
        
        if (!encontrado) {
            System.out.println("❌ Médico no encontrado");
            return;
        }
        
        try (PrintWriter pw = new PrintWriter(new FileWriter(MEDICOS_FILE))) {
            for (String linea : lineas) {
                pw.println(linea);
            }
            System.out.println("✅ Médico actualizado correctamente");
        } catch (IOException e) {
            System.out.println("Error al guardar cambios: " + e.getMessage());
        }
    }
    
    private static void eliminarMedico() {
        System.out.println("\n=== ELIMINAR MÉDICO ===");
        System.out.print("ID del médico a eliminar: ");
        String idBuscar = scanner.nextLine();
        
        List<String> lineas = new ArrayList<>();
        boolean encontrado = false;
        
        try (BufferedReader br = new BufferedReader(new FileReader(MEDICOS_FILE))) {
            String linea;
            while ((linea = br.readLine()) != null) {
                String[] datos = linea.split("\\|");
                if (datos.length >= 6 && datos[0].equals(idBuscar)) {
                    System.out.println("Médico encontrado: " + datos[1] + " " + datos[2]);
                    System.out.print("¿Confirma eliminación? (S/N): ");
                    String confirmacion = scanner.nextLine();
                    if (confirmacion.equalsIgnoreCase("S")) {
                        encontrado = true;
                        continue; // No agregar esta línea a la lista
                    }
                }
                lineas.add(linea);
            }
        } catch (IOException e) {
            System.out.println("Error al leer archivo: " + e.getMessage());
            return;
        }
        
        if (!encontrado) {
            System.out.println("❌ Médico no encontrado o eliminación cancelada");
            return;
        }
        
        try (PrintWriter pw = new PrintWriter(new FileWriter(MEDICOS_FILE))) {
            for (String linea : lineas) {
                pw.println(linea);
            }
            System.out.println("✅ Médico eliminado correctamente");
        } catch (IOException e) {
            System.out.println("Error al guardar cambios: " + e.getMessage());
        }
    }
    
    private static void gestionarPacientes() {
        int opcion;
        do {
            System.out.println("\n=== GESTIÓN DE PACIENTES ===");
            System.out.println("1. Listar Pacientes");
            System.out.println("2. Crear Paciente");
            System.out.println("3. Editar Paciente");
            System.out.println("4. Eliminar Paciente");
            System.out.println("0. Volver");
            System.out.print("Seleccione opción: ");
            
            opcion = scanner.nextInt();
            scanner.nextLine();
            
            switch (opcion) {
                case 1:
                    listarPacientes();
                    break;
                case 2:
                    crearPaciente();
                    break;
                case 3:
                    editarPaciente();
                    break;
                case 4:
                    eliminarPaciente();
                    break;
            }
        } while (opcion != 0);
    }
    
    private static void listarPacientes() {
        System.out.println("\n=== LISTA DE PACIENTES ===");
        try (BufferedReader br = new BufferedReader(new FileReader(CLIENTES_FILE))) {
            String linea;
            boolean hayPacientes = false;
            
            while ((linea = br.readLine()) != null) {
                String[] datos = linea.split("\\|");
                if (datos.length >= 6) {
                    System.out.printf("ID: %s | Nombre: %s %s | Nacimiento: %s | Sexo: %s\n",
                            datos[0], datos[1], datos[2], datos[3], datos[4]);
                    hayPacientes = true;
                }
            }
            
            if (!hayPacientes) {
                System.out.println("No hay pacientes registrados.");
            }
        } catch (IOException e) {
            System.out.println("Error al leer archivo: " + e.getMessage());
        }
    }
    
    private static void crearPaciente() {
        System.out.println("\n=== CREAR PACIENTE ===");
        
        System.out.print("ID del paciente: ");
        String id = scanner.nextLine();
        
        if (existePaciente(id)) {
            System.out.println("❌ Ya existe un paciente con ese ID");
            return;
        }
        
        System.out.print("Nombre: ");
        String nombre = scanner.nextLine();
        
        System.out.print("Apellidos: ");
        String apellidos = scanner.nextLine();
        
        System.out.print("Fecha de nacimiento (DD/MM/YYYY): ");
        String nacimiento = scanner.nextLine();
        
        System.out.print("Sexo (M/F): ");
        String sexo = scanner.nextLine();
        
        System.out.print("Contraseña: ");
        String password = scanner.nextLine();
        
        try (PrintWriter pw = new PrintWriter(new FileWriter(CLIENTES_FILE, true))) {
            pw.println(id + "|" + nombre + "|" + apellidos + "|" + nacimiento + "|" + sexo + "|" + password + "||");
            System.out.println("✅ Paciente creado correctamente");
        } catch (IOException e) {
            System.out.println("Error al guardar paciente: " + e.getMessage());
        }
    }
    
    private static void editarPaciente() {
        System.out.println("\n=== EDITAR PACIENTE ===");
        System.out.print("ID del paciente a editar: ");
        String idBuscar = scanner.nextLine();
        
        List<String> lineas = new ArrayList<>();
        boolean encontrado = false;
        
        try (BufferedReader br = new BufferedReader(new FileReader(CLIENTES_FILE))) {
            String linea;
            while ((linea = br.readLine()) != null) {
                String[] datos = linea.split("\\|");
                if (datos.length >= 6 && datos[0].equals(idBuscar)) {
                    System.out.println("Paciente encontrado: " + datos[1] + " " + datos[2]);
                    
                    System.out.print("Nuevo nombre (actual: " + datos[1] + "): ");
                    String nuevoNombre = scanner.nextLine();
                    if (nuevoNombre.isEmpty()) nuevoNombre = datos[1];
                    
                    System.out.print("Nuevos apellidos (actual: " + datos[2] + "): ");
                    String nuevosApellidos = scanner.nextLine();
                    if (nuevosApellidos.isEmpty()) nuevosApellidos = datos[2];
                    
                    System.out.print("Nueva fecha nacimiento (actual: " + datos[3] + "): ");
                    String nuevoNacimiento = scanner.nextLine();
                    if (nuevoNacimiento.isEmpty()) nuevoNacimiento = datos[3];
                    
                    System.out.print("Nuevo sexo (actual: " + datos[4] + "): ");
                    String nuevoSexo = scanner.nextLine();
                    if (nuevoSexo.isEmpty()) nuevoSexo = datos[4];
                    
                    System.out.print("Nueva contraseña (actual: " + datos[5] + "): ");
                    String nuevoPassword = scanner.nextLine();
                    if (nuevoPassword.isEmpty()) nuevoPassword = datos[5];
                    
                    String recetas = datos.length > 6 ? datos[6] : "";
                    String historial = datos.length > 7 ? datos[7] : "";
                    
                    linea = idBuscar + "|" + nuevoNombre + "|" + nuevosApellidos + "|" + nuevoNacimiento + "|" + nuevoSexo + "|" + nuevoPassword + "|" + recetas + "|" + historial;
                    encontrado = true;
                }
                lineas.add(linea);
            }
        } catch (IOException e) {
            System.out.println("Error al leer archivo: " + e.getMessage());
            return;
        }
        
        if (!encontrado) {
            System.out.println("❌ Paciente no encontrado");
            return;
        }
        
        try (PrintWriter pw = new PrintWriter(new FileWriter(CLIENTES_FILE))) {
            for (String linea : lineas) {
                pw.println(linea);
            }
            System.out.println("✅ Paciente actualizado correctamente");
        } catch (IOException e) {
            System.out.println("Error al guardar cambios: " + e.getMessage());
        }
    }
    
    private static void eliminarPaciente() {
        System.out.println("\n=== ELIMINAR PACIENTE ===");
        System.out.print("ID del paciente a eliminar: ");
        String idBuscar = scanner.nextLine();
        
        List<String> lineas = new ArrayList<>();
        boolean encontrado = false;
        
        try (BufferedReader br = new BufferedReader(new FileReader(CLIENTES_FILE))) {
            String linea;
            while ((linea = br.readLine()) != null) {
                String[] datos = linea.split("\\|");
                if (datos.length >= 6 && datos[0].equals(idBuscar)) {
                    System.out.println("Paciente encontrado: " + datos[1] + " " + datos[2]);
                    System.out.print("¿Confirma eliminación? (S/N): ");
                    String confirmacion = scanner.nextLine();
                    if (confirmacion.equalsIgnoreCase("S")) {
                        encontrado = true;
                        continue;
                    }
                }
                lineas.add(linea);
            }
        } catch (IOException e) {
            System.out.println("Error al leer archivo: " + e.getMessage());
            return;
        }
        
        if (!encontrado) {
            System.out.println("❌ Paciente no encontrado o eliminación cancelada");
            return;
        }
        
        try (PrintWriter pw = new PrintWriter(new FileWriter(CLIENTES_FILE))) {
            for (String linea : lineas) {
                pw.println(linea);
            }
            System.out.println("✅ Paciente eliminado correctamente");
        } catch (IOException e) {
            System.out.println("Error al guardar cambios: " + e.getMessage());
        }
    }
    
    private static void verEstadisticas() {
        System.out.println("\n=== ESTADÍSTICAS ===");
        
        int totalMedicos = contarLineasArchivo(MEDICOS_FILE);
        int totalPacientes = contarLineasArchivo(CLIENTES_FILE);
        int totalCitas = contarLineasArchivo("citas.txt");
        int totalRecetas = contarLineasArchivo("recetas.txt");
        
        System.out.println("Total de médicos: " + totalMedicos);
        System.out.println("Total de pacientes: " + totalPacientes);
        System.out.println("Total de citas: " + totalCitas);
        System.out.println("Total de recetas: " + totalRecetas);
    }
    
    private static int contarLineasArchivo(String archivo) {
        int contador = 0;
        try (BufferedReader br = new BufferedReader(new FileReader(archivo))) {
            while (br.readLine() != null) {
                contador++;
            }
        } catch (IOException e) {
            // Archivo no existe o está vacío
        }
        return contador;
    }
    
    private static boolean existeMedico(String id) {
        try (BufferedReader br = new BufferedReader(new FileReader(MEDICOS_FILE))) {
            String linea;
            while ((linea = br.readLine()) != null) {
                String[] datos = linea.split("\\|");
                if (datos.length > 0 && datos[0].equals(id)) {
                    return true;
                }
            }
        } catch (IOException e) {
            // Archivo no existe
        }
        return false;
    }
    
    private static boolean existePaciente(String id) {
        try (BufferedReader br = new BufferedReader(new FileReader(CLIENTES_FILE))) {
            String linea;
            while ((linea = br.readLine()) != null) {
                String[] datos = linea.split("\\|");
                if (datos.length > 0 && datos[0].equals(id)) {
                    return true;
                }
            }
        } catch (IOException e) {
            // Archivo no existe
        }
        return false;
    }
}