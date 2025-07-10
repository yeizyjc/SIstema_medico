/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 */

package com.utp.clinicapaciente;

import java.io.*;
import java.util.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Main {
    private static final String BASE_PATH = "../BaseDeDatos/";
    private static final String CLIENTES_FILE = BASE_PATH +"clientes.txt";
    private static final String CITAS_FILE = BASE_PATH + "citas.txt";
    private static final String MEDICOS_FILE = BASE_PATH + "medicos.txt";
    private static final String RECETAS_FILE = BASE_PATH + "recetas.txt";
    private static final String HISTORIAL_FILE = BASE_PATH + "historial.txt";
    
    private static Scanner scanner = new Scanner(System.in);
    private static String pacienteLogueado = null;
    private static String[] datosPaciente = null;
    
    public static void main(String[] args) {
        System.out.println("=== SISTEMA CLÍNICA - PACIENTES ===");
        
        int opcion;
        do {
            System.out.println("\n=== MENÚ INICIAL ===");
            System.out.println("1. Iniciar sesión");
            System.out.println("2. Crear cuenta");
            System.out.println("0. Salir");
            System.out.print("Seleccione opción: ");
            
            opcion = scanner.nextInt();
            scanner.nextLine(); // Limpiar buffer
            
            switch (opcion) {
                case 1:
                    if (iniciarSesion()) {
                        mostrarMenuPrincipal();
                    }
                    break;
                case 2:
                    crearCuenta();
                    break;
                case 0:
                    System.out.println("¡Hasta luego!");
                    break;
                default:
                    System.out.println("Opción inválida");
            }
        } while (opcion != 0);
    }
    
    private static boolean iniciarSesion() {
        System.out.print("ID de paciente: ");
        String idPaciente = scanner.nextLine();
        System.out.print("Contraseña: ");
        String password = scanner.nextLine();
        
        try (BufferedReader br = new BufferedReader(new FileReader(CLIENTES_FILE))) {
            String linea;
            while ((linea = br.readLine()) != null) {
                String[] datos = linea.split("\\|");
                if (datos.length >= 6 && datos[0].equals(idPaciente) && datos[5].equals(password)) {
                    pacienteLogueado = idPaciente;
                    datosPaciente = datos;
                    System.out.println("\n✅ Bienvenido " + datos[1] + " " + datos[2]);
                    return true;
                }
            }
        } catch (IOException e) {
            System.out.println("Error al leer archivo: " + e.getMessage());
        }
        
        System.out.println("❌ Credenciales incorrectas");
        return false;
    }
    
    private static void crearCuenta() {
        System.out.println("\n=== CREAR CUENTA ===");
        
        System.out.print("Ingrese un ID único (solo letras y números): ");
String id = scanner.nextLine();

// Validar formato del ID
if (!id.matches("[a-zA-Z0-9]+")) {
    System.out.println("❌ El ID solo puede contener letras y números (sin espacios, puntos ni símbolos)");
    return;
}      
        // Verificar que el ID no exista
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
            System.out.println("✅ Cuenta creada correctamente");
            System.out.println("Ya puede iniciar sesión con su ID: " + id);
        } catch (IOException e) {
            System.out.println("Error al crear cuenta: " + e.getMessage());
        }
    }
    
    private static void mostrarMenuPrincipal() {
        int opcion;
        do {
            System.out.println("\n=== MENÚ PRINCIPAL ===");
            System.out.println("1. Separar cita");
            System.out.println("2. Ver mis citas");
            System.out.println("3. Ver mis recetas");
            System.out.println("4. Ver mi historial médico");
            System.out.println("5. Modificar mis datos");
            System.out.println("0. Cerrar sesión");
            System.out.print("Seleccione opción: ");
            
            opcion = scanner.nextInt();
            scanner.nextLine(); // Limpiar buffer
            
            switch (opcion) {
                case 1:
                    separarCita();
                    break;
                case 2:
                    verMisCitas();
                    break;
                case 3:
                    verMisRecetas();
                    break;
                case 4:
                    verMiHistorial();
                    break;
                case 5:
                    modificarMisDatos();
                    break;
                case 0:
                    System.out.println("Sesión cerrada");
                    pacienteLogueado = null;
                    datosPaciente = null;
                    break;
                default:
                    System.out.println("Opción inválida");
            }
        } while (opcion != 0);
    }
    
    private static void separarCita() {
        System.out.println("\n=== SEPARAR CITA ===");
        
        // Mostrar médicos disponibles
        System.out.println("Médicos disponibles:");
        Map<String, String[]> medicosDisponibles = new HashMap<>();
        
        try (BufferedReader br = new BufferedReader(new FileReader(MEDICOS_FILE))) {
            String linea;
            while ((linea = br.readLine()) != null) {
                String[] datos = linea.split("\\|");
                if (datos.length >= 6) {
                    System.out.printf("ID: %s | Dr. %s %s | Especialidad: %s\n",
                            datos[0], datos[1], datos[2], datos[3]);
                    medicosDisponibles.put(datos[0], datos);
                }
            }
        } catch (IOException e) {
            System.out.println("Error al leer médicos: " + e.getMessage());
            return;
        }
        
        if (medicosDisponibles.isEmpty()) {
            System.out.println("No hay médicos disponibles");
            return;
        }
        
        System.out.print("\nSeleccione ID del médico: ");
        String idMedico = scanner.nextLine();
        
        if (!medicosDisponibles.containsKey(idMedico)) {
            System.out.println("❌ ID de médico no válido");
            return;
        }
        
        System.out.print("Fecha de la cita (DD/MM/YYYY): ");
        String fecha = scanner.nextLine();
        
// Lista de horarios válidos (puedes modificar o agregar más)
String[] horariosDisponibles = {
    "08:00", "09:00", "10:00", "11:00",
    "14:00", "15:00", "16:00"
};

// Mostrar horarios no ocupados
System.out.println("Horarios disponibles:");
boolean hayHorarios = false;
for (String h : horariosDisponibles) {
    if (!citaOcupada(fecha, h, idMedico)) {
        System.out.println("- " + h);
        hayHorarios = true;
    }
}

if (!hayHorarios) {
    System.out.println("❌ No hay horarios disponibles para este médico ese día");
    return;
}

System.out.print("Seleccione una hora de las mostradas: ");
String hora = scanner.nextLine();

// Validar que esté en la lista y no esté ocupada
boolean esValido = false;
for (String h : horariosDisponibles) {
    if (h.equals(hora) && !citaOcupada(fecha, hora, idMedico)) {
        esValido = true;
        break;
    }
}

if (!esValido) {
    System.out.println("❌ Hora inválida o ya ocupada");
    return;
}        
        // Verificar disponibilidad
        if (citaOcupada(fecha, hora, idMedico)) {
            System.out.println("❌ Ya existe una cita para ese médico en esa fecha y hora");
            return;
        }
        
        String[] datosMedico = medicosDisponibles.get(idMedico);
        String especialidad = datosMedico[3];
        
        try (PrintWriter pw = new PrintWriter(new FileWriter(CITAS_FILE, true))) {
            pw.println(fecha + "|" + hora + "|" + idMedico + "|" + especialidad + "|" + pacienteLogueado);
            System.out.println("✅ Cita separada correctamente");
            System.out.println("Fecha: " + fecha + " | Hora: " + hora);
            System.out.println("Médico: Dr. " + datosMedico[1] + " " + datosMedico[2]);
            System.out.println("Especialidad: " + especialidad);
        } catch (IOException e) {
            System.out.println("Error al guardar cita: " + e.getMessage());
        }
    }
    
    private static void verMisCitas() {
        System.out.println("\n=== MIS CITAS ===");
        boolean hayCitas = false;
        
        try (BufferedReader br = new BufferedReader(new FileReader(CITAS_FILE))) {
            String linea;
            while ((linea = br.readLine()) != null) {
                String[] datos = linea.split("\\|");
                if (datos.length >= 5 && datos[4].equals(pacienteLogueado)) {
                    // Obtener nombre del médico
                    String nombreMedico = obtenerNombreMedico(datos[2]);
                    
                    System.out.printf("Fecha: %s | Hora: %s | Dr. %s | Especialidad: %s\n",
                            datos[0], datos[1], nombreMedico, datos[3]);
                    hayCitas = true;
                }
            }
        } catch (IOException e) {
            System.out.println("Error al leer citas: " + e.getMessage());
        }
        
        if (!hayCitas) {
            System.out.println("No tiene citas programadas");
        }
    }
    
    private static void verMisRecetas() {
        System.out.println("\n=== MIS RECETAS ===");
        
        // Obtener IDs de recetas del paciente
        String recetasStr = datosPaciente.length > 6 ? datosPaciente[6] : "";
        
        if (recetasStr.isEmpty()) {
            System.out.println("No tiene recetas");
            return;
        }
        
        String[] recetasIds = recetasStr.split(",");
        boolean hayRecetas = false;
        
        try (BufferedReader br = new BufferedReader(new FileReader(RECETAS_FILE))) {
            String linea;
            while ((linea = br.readLine()) != null) {
                String[] datos = linea.split("\\|");
                if (datos.length >= 6) {
                    // Verificar si esta receta pertenece al paciente
                    for (String recetaId : recetasIds) {
                        if (datos[0].equals(recetaId.trim())) {
                            String nombreMedico = obtenerNombreMedico(datos[5]);
                            System.out.printf("ID: %s | Fecha: %s | Hora: %s\n", datos[0], datos[1], datos[2]);
                            System.out.printf("Médico: Dr. %s\n", nombreMedico);
                            System.out.printf("Medicamentos: %s\n", datos[3]);
                            System.out.printf("Comentarios: %s\n", datos[4]);
                            System.out.println("----------------------------------------");
                            hayRecetas = true;
                            break;
                        }
                    }
                }
            }
        } catch (IOException e) {
            System.out.println("Error al leer recetas: " + e.getMessage());
        }
        
        if (!hayRecetas) {
            System.out.println("No se encontraron recetas");
        }
    }
    
    private static void verMiHistorial() {
        System.out.println("\n=== MI HISTORIAL MÉDICO ===");
        
        // Obtener IDs de recetas del paciente
        String recetasStr = datosPaciente.length > 6 ? datosPaciente[6] : "";
        
        if (recetasStr.isEmpty()) {
            System.out.println("No tiene historial médico");
            return;
        }
        
        String[] recetasIds = recetasStr.split(",");
        boolean hayHistorial = false;
        
        try (BufferedReader br = new BufferedReader(new FileReader(HISTORIAL_FILE))) {
            String linea;
            while ((linea = br.readLine()) != null) {
                String[] datos = linea.split("\\|");
                if (datos.length >= 5) {
                    // Verificar si esta entrada de historial corresponde al paciente
                    for (String recetaId : recetasIds) {
                        if (datos[4].equals(recetaId.trim())) {
                            String nombreMedico = obtenerNombreMedico(datos[2]);
                            System.out.printf("Fecha: %s | Hora: %s | Dr. %s | Especialidad: %s\n",
                                    datos[0], datos[1], nombreMedico, datos[3]);
                            hayHistorial = true;
                            break;
                        }
                    }
                }
            }
        } catch (IOException e) {
            System.out.println("Error al leer historial: " + e.getMessage());
        }
        
        if (!hayHistorial) {
            System.out.println("No hay historial médico disponible");
        }
    }
    
    private static void modificarMisDatos() {
        System.out.println("\n=== MODIFICAR MIS DATOS ===");
        System.out.println("Datos actuales:");
        System.out.println("ID: " + datosPaciente[0]);
        System.out.println("Nombre: " + datosPaciente[1]);
        System.out.println("Apellidos: " + datosPaciente[2]);
        System.out.println("Fecha de nacimiento: " + datosPaciente[3]);
        System.out.println("Sexo: " + datosPaciente[4]);
        
        System.out.print("\nNuevo nombre (Enter para mantener actual): ");
        String nuevoNombre = scanner.nextLine();
        if (!nuevoNombre.isEmpty()) {
            datosPaciente[1] = nuevoNombre;
        }
        
        System.out.print("Nuevos apellidos (Enter para mantener actual): ");
        String nuevosApellidos = scanner.nextLine();
        if (!nuevosApellidos.isEmpty()) {
            datosPaciente[2] = nuevosApellidos;
        }
        
        System.out.print("Nueva fecha de nacimiento (Enter para mantener actual): ");
        String nuevaFecha = scanner.nextLine();
        if (!nuevaFecha.isEmpty()) {
            datosPaciente[3] = nuevaFecha;
        }
        
        System.out.print("Nuevo sexo (Enter para mantener actual): ");
        String nuevoSexo = scanner.nextLine();
        if (!nuevoSexo.isEmpty()) {
            datosPaciente[4] = nuevoSexo;
        }
        
        System.out.print("Nueva contraseña (Enter para mantener actual): ");
        String nuevaPassword = scanner.nextLine();
        if (!nuevaPassword.isEmpty()) {
            datosPaciente[5] = nuevaPassword;
        }
        
        // Actualizar archivo
        actualizarDatosPaciente();
        System.out.println("✅ Datos actualizados correctamente");
    }
    
    private static void actualizarDatosPaciente() {
        List<String> lineas = new ArrayList<>();
        
        try (BufferedReader br = new BufferedReader(new FileReader(CLIENTES_FILE))) {
            String linea;
            while ((linea = br.readLine()) != null) {
                String[] datos = linea.split("\\|");
                if (datos.length >= 6 && datos[0].equals(pacienteLogueado)) {
                    // Mantener recetas e historial
                    String recetas = datos.length > 6 ? datos[6] : "";
                    String historial = datos.length > 7 ? datos[7] : "";
                    
                    linea = datosPaciente[0] + "|" + datosPaciente[1] + "|" + datosPaciente[2] + "|" + 
                            datosPaciente[3] + "|" + datosPaciente[4] + "|" + datosPaciente[5] + "|" + 
                            recetas + "|" + historial;
                }
                lineas.add(linea);
            }
        } catch (IOException e) {
            System.out.println("Error al leer archivo: " + e.getMessage());
            return;
        }
        
        try (PrintWriter pw = new PrintWriter(new FileWriter(CLIENTES_FILE))) {
            for (String linea : lineas) {
                pw.println(linea);
            }
        } catch (IOException e) {
            System.out.println("Error al guardar cambios: " + e.getMessage());
        }
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
    
    private static boolean citaOcupada(String fecha, String hora, String idMedico) {
        try (BufferedReader br = new BufferedReader(new FileReader(CITAS_FILE))) {
            String linea;
            while ((linea = br.readLine()) != null) {
                String[] datos = linea.split("\\|");
                if (datos.length >= 4 && datos[0].equals(fecha) && 
                    datos[1].equals(hora) && datos[2].equals(idMedico)) {
                    return true;
                }
            }
        } catch (IOException e) {
            // Archivo no existe
        }
        return false;
    }
    
    private static String obtenerNombreMedico(String idMedico) {
        try (BufferedReader br = new BufferedReader(new FileReader(MEDICOS_FILE))) {
            String linea;
            while ((linea = br.readLine()) != null) {
                String[] datos = linea.split("\\|");
                if (datos.length >= 6 && datos[0].equals(idMedico)) {
                    return datos[1] + " " + datos[2];
                }
            }
        } catch (IOException e) {
            // Error al leer
        }
        return "Médico no encontrado";
    }
}
