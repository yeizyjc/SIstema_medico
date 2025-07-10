/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 */

package com.utp.clinicamedico;

import java.io.*;
import java.util.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Main {
    private static final String BASE_PATH = "../BaseDeDatos/";
    private static final String MEDICOS_FILE = BASE_PATH + "medicos.txt";
    private static final String CITAS_FILE = BASE_PATH + "citas.txt";
    private static final String HISTORIAL_FILE = BASE_PATH + "historial.txt";
    private static final String RECETAS_FILE = BASE_PATH + "recetas.txt";
    private static final String CLIENTES_FILE = BASE_PATH + "clientes.txt";
    
    private static Scanner scanner = new Scanner(System.in);
    private static String medicoLogueado = null;
    private static String[] datosMedico = null;
    
    public static void main(String[] args) {
        System.out.println("=== SISTEMA CLÍNICA - MÉDICOS ===");
        
        if (iniciarSesion()) {
            mostrarMenuPrincipal();
        }
    }
    
    private static boolean iniciarSesion() {
        System.out.print("Ingrese su ID de médico: ");
        String idMedico = scanner.nextLine();
        
        try (BufferedReader br = new BufferedReader(new FileReader(MEDICOS_FILE))) {
            String linea;
            while ((linea = br.readLine()) != null) {
                String[] datos = linea.split("\\|");
                if (datos.length >= 6 && datos[0].equals(idMedico)) {
                    medicoLogueado = idMedico;
                    datosMedico = datos;
                    System.out.println("\n✅ Bienvenido Dr. " + datos[1] + " " + datos[2]);
                    System.out.println("Especialidad: " + datos[3]);
                    return true;
                }
            }
        } catch (IOException e) {
            System.out.println("Error al leer archivo de médicos: " + e.getMessage());
        }
        
        System.out.println("❌ ID de médico no encontrado");
        return false;
    }
    
    private static void mostrarMenuPrincipal() {
        int opcion;
        do {
            System.out.println("\n=== MENÚ PRINCIPAL ===");
            System.out.println("1. Modificar mis datos");
            System.out.println("2. Ver mis citas");
            System.out.println("3. Atender cita (generar receta)");
            System.out.println("4. Ver historial médico");
            System.out.println("5. Buscar paciente");
            System.out.println("0. Salir");
            System.out.print("Seleccione opción: ");
            
            opcion = scanner.nextInt();
            scanner.nextLine(); // Limpiar buffer
            
            switch (opcion) {
                case 1:
                    modificarMisDatos();
                    break;
                case 2:
                    verMisCitas();
                    break;
                case 3:
                    atenderCita();
                    break;
                case 4:
                    verHistorialMedico();
                    break;
                case 5:
                    buscarPaciente();
                    break;
                case 0:
                    System.out.println("¡Hasta luego!");
                    break;
                default:
                    System.out.println("Opción inválida");
            }
        } while (opcion != 0);
    }
    
    private static void modificarMisDatos() {
        System.out.println("\n=== MODIFICAR MIS DATOS ===");
        System.out.println("Datos actuales:");
        System.out.println("ID: " + datosMedico[0]);
        System.out.println("Nombre: " + datosMedico[1]);
        System.out.println("Apellidos: " + datosMedico[2]);
        System.out.println("Especialidad: " + datosMedico[3]);
        System.out.println("Sexo: " + datosMedico[4]);
        System.out.println("DNI: " + datosMedico[5]);
        
        System.out.print("\nNuevo nombre (Enter para mantener actual): ");
        String nuevoNombre = scanner.nextLine();
        if (!nuevoNombre.isEmpty()) {
            datosMedico[1] = nuevoNombre;
        }
        
        System.out.print("Nuevos apellidos (Enter para mantener actual): ");
        String nuevosApellidos = scanner.nextLine();
        if (!nuevosApellidos.isEmpty()) {
            datosMedico[2] = nuevosApellidos;
        }
        
        System.out.print("Nueva especialidad (Enter para mantener actual): ");
        String nuevaEspecialidad = scanner.nextLine();
        if (!nuevaEspecialidad.isEmpty()) {
            datosMedico[3] = nuevaEspecialidad;
        }
        
        System.out.print("Nuevo sexo (Enter para mantener actual): ");
        String nuevoSexo = scanner.nextLine();
        if (!nuevoSexo.isEmpty()) {
            datosMedico[4] = nuevoSexo;
        }
        
        System.out.print("Nuevo DNI (Enter para mantener actual): ");
        String nuevoDni = scanner.nextLine();
        if (!nuevoDni.isEmpty()) {
            datosMedico[5] = nuevoDni;
        }
        
        // Actualizar archivo
        actualizarDatosMedico();
        System.out.println("✅ Datos actualizados correctamente");
    }
    
    private static void actualizarDatosMedico() {
        List<String> lineas = new ArrayList<>();
        
        try (BufferedReader br = new BufferedReader(new FileReader(MEDICOS_FILE))) {
            String linea;
            while ((linea = br.readLine()) != null) {
                String[] datos = linea.split("\\|");
                if (datos.length >= 6 && datos[0].equals(medicoLogueado)) {
                    linea = String.join("|", datosMedico);
                }
                lineas.add(linea);
            }
        } catch (IOException e) {
            System.out.println("Error al leer archivo: " + e.getMessage());
            return;
        }
        
        try (PrintWriter pw = new PrintWriter(new FileWriter(MEDICOS_FILE))) {
            for (String linea : lineas) {
                pw.println(linea);
            }
        } catch (IOException e) {
            System.out.println("Error al guardar cambios: " + e.getMessage());
        }
    }
    
    private static void verMisCitas() {
        System.out.println("\n=== MIS CITAS ===");
        boolean hayCitas = false;
        
        try (BufferedReader br = new BufferedReader(new FileReader(CITAS_FILE))) {
            String linea;
            while ((linea = br.readLine()) != null) {
                String[] datos = linea.split("\\|");
                if (datos.length >= 4 && datos[2].equals(medicoLogueado)) {
                    System.out.printf("Fecha: %s | Hora: %s | Especialidad: %s\n",
                            datos[0], datos[1], datos[3]);
                    hayCitas = true;
                }
            }
        } catch (IOException e) {
            System.out.println("Error al leer archivo de citas: " + e.getMessage());
        }
        
        if (!hayCitas) {
            System.out.println("No tiene citas programadas.");
        }
    }
    
    private static void atenderCita() {
        System.out.println("\n=== ATENDER CITA ===");
        System.out.print("ID del paciente: ");
        String idPaciente = scanner.nextLine();
        
        // Verificar que el paciente existe
        if (!existePaciente(idPaciente)) {
            System.out.println("❌ Paciente no encontrado");
            return;
        }
        
        // Generar ID único para la receta
        String idReceta = generarIdReceta();
        
        System.out.print("Medicamentos (separados por comas): ");
        String medicamentos = scanner.nextLine();
        
        System.out.print("Comentarios/Observaciones: ");
        String comentario = scanner.nextLine();
        
        // Obtener fecha y hora actual
        LocalDateTime ahora = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH:mm");
        String fecha = ahora.format(formatter);
        String hora = ahora.format(timeFormatter);
        
        // Guardar receta
        try (PrintWriter pw = new PrintWriter(new FileWriter(RECETAS_FILE, true))) {
            pw.println(idReceta + "|" + fecha + "|" + hora + "|" + medicamentos + "|" + comentario + "|" + medicoLogueado);
            System.out.println("✅ Receta generada con ID: " + idReceta);
        } catch (IOException e) {
            System.out.println("Error al guardar receta: " + e.getMessage());
            return;
        }
        
        // Agregar al historial
        try (PrintWriter pw = new PrintWriter(new FileWriter(HISTORIAL_FILE, true))) {
            pw.println(fecha + "|" + hora + "|" + medicoLogueado + "|" + datosMedico[3] + "|" + idReceta);
            System.out.println("✅ Registro agregado al historial");
        } catch (IOException e) {
            System.out.println("Error al guardar en historial: " + e.getMessage());
        }
        
        // Actualizar paciente con la receta
        actualizarRecetaPaciente(idPaciente, idReceta);
    }
    
    private static void verHistorialMedico() {
        System.out.println("\n=== HISTORIAL MÉDICO ===");
        boolean hayHistorial = false;
        
        try (BufferedReader br = new BufferedReader(new FileReader(HISTORIAL_FILE))) {
            String linea;
            while ((linea = br.readLine()) != null) {
                String[] datos = linea.split("\\|");
                if (datos.length >= 5 && datos[2].equals(medicoLogueado)) {
                    System.out.printf("Fecha: %s | Hora: %s | Especialidad: %s | Receta ID: %s\n",
                            datos[0], datos[1], datos[3], datos[4]);
                    hayHistorial = true;
                }
            }
        } catch (IOException e) {
            System.out.println("Error al leer historial: " + e.getMessage());
        }
        
        if (!hayHistorial) {
            System.out.println("No hay registros en el historial.");
        }
    }
    
    private static void buscarPaciente() {
        System.out.println("\n=== BUSCAR PACIENTE ===");
        System.out.print("ID del paciente: ");
        String idPaciente = scanner.nextLine();
        
        try (BufferedReader br = new BufferedReader(new FileReader(CLIENTES_FILE))) {
            String linea;
            while ((linea = br.readLine()) != null) {
                String[] datos = linea.split("\\|");
                if (datos.length >= 6 && datos[0].equals(idPaciente)) {
                    System.out.println("\n=== INFORMACIÓN DEL PACIENTE ===");
                    System.out.println("ID: " + datos[0]);
                    System.out.println("Nombre: " + datos[1] + " " + datos[2]);
                    System.out.println("Fecha de nacimiento: " + datos[3]);
                    System.out.println("Sexo: " + datos[4]);
                    
                    // Mostrar recetas del paciente
                    System.out.println("\n=== RECETAS DEL PACIENTE ===");
                    mostrarRecetasPaciente(idPaciente);
                    
                    // Mostrar historial del paciente
                    System.out.println("\n=== HISTORIAL DEL PACIENTE ===");
                    mostrarHistorialPaciente(idPaciente);
                    return;
                }
            }
        } catch (IOException e) {
            System.out.println("Error al buscar paciente: " + e.getMessage());
        }
        
        System.out.println("❌ Paciente no encontrado");
    }
    
    private static void mostrarRecetasPaciente(String idPaciente) {
        boolean hayRecetas = false;
        
        try (BufferedReader br = new BufferedReader(new FileReader(RECETAS_FILE))) {
            String linea;
            while ((linea = br.readLine()) != null) {
                String[] datos = linea.split("\\|");
                if (datos.length >= 6) {
                    // Verificar si esta receta pertenece al paciente
                    if (pacienteTieneReceta(idPaciente, datos[0])) {
                        System.out.printf("ID: %s | Fecha: %s | Hora: %s | Medicamentos: %s | Comentario: %s\n",
                                datos[0], datos[1], datos[2], datos[3], datos[4]);
                        hayRecetas = true;
                    }
                }
            }
        } catch (IOException e) {
            System.out.println("Error al leer recetas: " + e.getMessage());
        }
        
        if (!hayRecetas) {
            System.out.println("No tiene recetas registradas.");
        }
    }
    
    private static void mostrarHistorialPaciente(String idPaciente) {
        boolean hayHistorial = false;
        
        try (BufferedReader br = new BufferedReader(new FileReader(HISTORIAL_FILE))) {
            String linea;
            while ((linea = br.readLine()) != null) {
                String[] datos = linea.split("\\|");
                if (datos.length >= 5) {
                    // Verificar si esta entrada de historial corresponde al paciente
                    if (pacienteTieneReceta(idPaciente, datos[4])) {
                        System.out.printf("Fecha: %s | Hora: %s | Médico: %s | Especialidad: %s\n",
                                datos[0], datos[1], datos[2], datos[3]);
                        hayHistorial = true;
                    }
                }
            }
        } catch (IOException e) {
            System.out.println("Error al leer historial: " + e.getMessage());
        }
        
        if (!hayHistorial) {
            System.out.println("No hay historial médico disponible.");
        }
    }
    
    private static boolean existePaciente(String idPaciente) {
        try (BufferedReader br = new BufferedReader(new FileReader(CLIENTES_FILE))) {
            String linea;
            while ((linea = br.readLine()) != null) {
                String[] datos = linea.split("\\|");
                if (datos.length >= 6 && datos[0].equals(idPaciente)) {
                    return true;
                }
            }
        } catch (IOException e) {
            // Archivo no existe
        }
        return false;
    }
    
    private static boolean pacienteTieneReceta(String idPaciente, String idReceta) {
        try (BufferedReader br = new BufferedReader(new FileReader(CLIENTES_FILE))) {
            String linea;
            while ((linea = br.readLine()) != null) {
                String[] datos = linea.split("\\|");
                if (datos.length >= 7 && datos[0].equals(idPaciente)) {
                    String recetas = datos[6];
                    return recetas.contains(idReceta);
                }
            }
        } catch (IOException e) {
            // Error al leer
        }
        return false;
    }
    
    private static String generarIdReceta() {
        Random random = new Random();
        return "R" + System.currentTimeMillis() + random.nextInt(100);
    }
    
    private static void actualizarRecetaPaciente(String idPaciente, String idReceta) {
        List<String> lineas = new ArrayList<>();
        
        try (BufferedReader br = new BufferedReader(new FileReader(CLIENTES_FILE))) {
            String linea;
            while ((linea = br.readLine()) != null) {
                String[] datos = linea.split("\\|");
                if (datos.length >= 6 && datos[0].equals(idPaciente)) {
                    // Agregar receta al paciente
                    String recetasActuales = datos.length > 6 ? datos[6] : "";
                    if (!recetasActuales.isEmpty()) {
                        recetasActuales += "," + idReceta;
                    } else {
                        recetasActuales = idReceta;
                    }
                    
                    String historialActual = datos.length > 7 ? datos[7] : "";
                    
                    linea = datos[0] + "|" + datos[1] + "|" + datos[2] + "|" + datos[3] + "|" + datos[4] + "|" + datos[5] + "|" + recetasActuales + "|" + historialActual;
                }
                lineas.add(linea);
            }
        } catch (IOException e) {
            System.out.println("Error al leer archivo de clientes: " + e.getMessage());
            return;
        }
        
        try (PrintWriter pw = new PrintWriter(new FileWriter(CLIENTES_FILE))) {
            for (String linea : lineas) {
                pw.println(linea);
            }
        } catch (IOException e) {
            System.out.println("Error al actualizar paciente: " + e.getMessage());
        }
    }
}