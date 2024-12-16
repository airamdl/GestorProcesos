/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package modelos;

/**
 *
 * @author airam
 */

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class Modelo {
    public List<List<String>> ListarProcesos() throws IOException {
        List<List<String>> tablaProcesos = new ArrayList<>();
        String os = System.getProperty("os.name").toLowerCase();
        String[] comando;

        if (os.contains("win")) {
            comando = new String[]{"cmd.exe", "/c", "tasklist"};
        } else {
            // Comando para listar procesos en Linux
            comando = new String[]{"ps", "-eo", "pid,euser,comm"};
        }

        Process proceso = Runtime.getRuntime().exec(comando);
        BufferedReader lector = new BufferedReader(new InputStreamReader(proceso.getInputStream()));
        String linea;

        // Leer salida
        while ((linea = lector.readLine()) != null) {
            List<String> filaProceso = new ArrayList<>();
            if (os.contains("win")) {
                if (linea.startsWith("Image Name")) continue; // Saltar encabezado
                String[] partes = linea.split("\\s+", 5);
                if (partes.length >= 2) {
                    filaProceso.add(partes[1]); // PID
                    filaProceso.add("N/A");     // Windows no tiene usuario en tasklist
                    filaProceso.add(partes[0]); // Nombre del proceso
                    tablaProcesos.add(filaProceso);
                }
            } else {
                filaProceso.add(linea.substring(0, 7).trim());
                filaProceso.add(linea.substring(8, 15).trim());
                filaProceso.add(linea.substring(16).trim());
                tablaProcesos.add(filaProceso);
            }
        }
        return tablaProcesos;
    }

    // Método para matar procesos
    public void MatarProcesos(String proceso) throws IOException {
        String os = System.getProperty("os.name").toLowerCase();
        ProcessBuilder pb;

        if (os.contains("win")) {
            pb = new ProcessBuilder("taskkill", "/F", "/IM", proceso);
        } else {
            pb = new ProcessBuilder("killall", proceso);
        }

        pb.start();
    }

    // Método para crear un nuevo proceso
    public void NuevoProceso(String proceso) throws IOException {
        ProcessBuilder pb = new ProcessBuilder(proceso);
        pb.start();
    }
}
