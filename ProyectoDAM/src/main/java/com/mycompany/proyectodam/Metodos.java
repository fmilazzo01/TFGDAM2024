package com.mycompany.proyectodam;

import java.security.SecureRandom;
import java.text.Normalizer;
import java.time.LocalDate;
import java.util.Base64;

/**
 * Clase encargada de tener algunos metodos auxiliares para las otras clases
 *
 * @author Franco
 */
public class Metodos {

    // metodo para calcular la fecha de vencimiento de la membnresia
    public static LocalDate calcularFechaVencimiento(String tipoPago, LocalDate fechaPago) {
        switch (tipoPago) {
            case "Mensualidad":
                return fechaPago.plusMonths(1);
            case "Trimestre":
                return fechaPago.plusMonths(3);
            case "Semestre":
                return fechaPago.plusMonths(6);
            case "Año":
                return fechaPago.plusYears(1);
            default:
                return fechaPago;
        }
    }

    // metodo para quitar acentos
    public static String removeAccents(String input) {
        return Normalizer.normalize(input, Normalizer.Form.NFD)
                .replaceAll("\\p{InCombiningDiacriticalMarks}+", "");
    }

    // metodo para generar codigos de recuperacion aleatorios
    public static String generarCodigoRecuperacion() {
        SecureRandom random = new SecureRandom();     // Generar una instancia de SecureRandom
        byte[] bytes = new byte[6];  // Crear un array de bytes de longitud 6
        random.nextBytes(bytes);   // Generar bytes aleatorios
        return Base64.getUrlEncoder().withoutPadding().encodeToString(bytes);   // Codificar los bytes en una cadena base64 sin relleno y devolverla
    }
    
    

    // METODO PARA INTRODUCIR EL PRECIO DEL PAQUETE DEL GYM EN HOMECONTROLLER de manera local
//    public static double calcularMontoPago(String tipoPago) {
//        switch (tipoPago) {
//            case "Mensualidad":
//                return 30.0;
//            case "Trimestre":
//                return 80.0;
//            case "Semestre":
//                return 140.0;
//            case "Año":
//                return 200.0;
//            default:
//                return 0.0; //  devuelve 0 si el tipo de pago no coincide con ninguno de los casos anteriores
//        }
//    }
}
