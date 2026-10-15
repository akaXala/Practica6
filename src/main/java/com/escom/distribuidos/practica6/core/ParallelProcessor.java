package com.escom.distribuidos.practica6.core;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;

/**
 * ============================================================================
 * CLASE: ParallelProcessor
 * ============================================================================
 * Esta clase administra el ciclo de vida de los hilos y su sincronización.
 * 
 * 1. CREACIÓN DE HILOS (Thread Creation):
 *    - Se crean instancias explícitas de la clase java.lang.Thread.
 *    - Cada hilo recibe una instancia de ImageTask (que implementa Runnable).
 *    - A cada hilo se le asigna un nombre formal y unívoco: "Hilo-Trabajador-X".
 *    - Se invoca explícitamente el método thread.start() para iniciar la ejecución concurrente.
 * 
 * 2. MÉTODO DE SINCRONIZACIÓN (Synchronization Method):
 *    - Se utiliza java.util.concurrent.CountDownLatch como BARRERA DE SINCRONIZACIÓN.
 *    - El contador se inicializa con el número total de hilos trabajadores (numThreads).
 *    - El hilo principal llama a latch.await() y queda bloqueado esperando.
 *    - Conforme cada hilo trabajador termina su trabajo en ImageTask, invoca latch.countDown().
 *    - Cuando el contador llega a cero (0), latch.await() se desbloquea y el hilo principal
 *      continúa para guardar la imagen final y medir el tiempo.
 */
public class ParallelProcessor {

    /**
     * Ejecuta las tareas utilizando creación explícita de hilos (new Thread)
     * y sincronización mediante CountDownLatch.
     */
    public void executeWithCountDownLatch(List<ImageTask> tasks, CountDownLatch latch) {
        List<Thread> threads = new ArrayList<>();

        // -------------------------------------------------------------
        // PASO 1: CREACIÓN EXPLÍCITA DE OBJETOS THREAD
        // -------------------------------------------------------------
        System.out.println("\n--- [PASO 1: CREACIÓN EXPLÍCITA DE HILOS (new Thread)] ---");
        for (ImageTask task : tasks) {
            String threadName = "Hilo-Trabajador-" + task.getTaskId();
            Thread thread = new Thread(task, threadName);
            threads.add(thread);
            System.out.println("  [+] new Thread(...) -> Nombre asignado: " + thread.getName() + " para Tarea #" + task.getTaskId());
        }

        // -------------------------------------------------------------
        // PASO 2: INICIO EXPLÍCITO DE HILOS CON .start()
        // -------------------------------------------------------------
        System.out.println("\n--- [PASO 2: LANZAMIENTO DE HILOS (.start())] ---");
        for (Thread thread : threads) {
            System.out.println("  [>] Iniciando ejecución concurrente: " + thread.getName() + ".start()");
            thread.start();
        }

        // -------------------------------------------------------------
        // PASO 3: ESPERA / BARRERA DE SINCRONIZACIÓN (CountDownLatch)
        // -------------------------------------------------------------
        System.out.println("\n--- [PASO 3: SINCRONIZACIÓN CON CountDownLatch (latch.await())] ---");
        System.out.println("  [!] Hilo principal esperando en la barrera a que los " + tasks.size() + " hilos terminen...");
        try {
            latch.await();
            System.out.println("  [✓] ¡BARRERA ALCANZADA! El contador de CountDownLatch llegó a 0. Hilo principal desbloqueado.");
        } catch (InterruptedException e) {
            System.err.println("La sincronización con CountDownLatch fue interrumpida: " + e.getMessage());
            Thread.currentThread().interrupt();
        }
    }

    /**
     * Método sobrecargado compatible con llamadas anteriores.
     */
    public void executeTasks(List<ImageTask> tasks, int numThreads, CountDownLatch latch) {
        executeWithCountDownLatch(tasks, latch);
    }

    /**
     * MÉTODO ALTERNATIVO: Sincronización mediante Thread.join()
     * Demuestra cómo sincronizar esperando la terminación directa de cada objeto Thread.
     */
    public void executeWithJoin(List<ImageTask> tasks) {
        System.out.println("\n==================================================");
        System.out.println(" MÉTODO DE SINCRONIZACIÓN ALTERNATIVO: Thread.join()");
        System.out.println("==================================================");

        List<Thread> threads = new ArrayList<>();

        for (ImageTask task : tasks) {
            Thread thread = new Thread(task, "Hilo-Trabajador-" + task.getTaskId());
            threads.add(thread);
            thread.start();
        }

        for (Thread thread : threads) {
            try {
                System.out.println("Esperando finalización de: " + thread.getName() + " con .join()...");
                thread.join();
            } catch (InterruptedException e) {
                System.err.println("Interrupción durante thread.join(): " + e.getMessage());
                Thread.currentThread().interrupt();
            }
        }
        System.out.println("¡Todos los hilos han finalizado según Thread.join()!");
    }
}
