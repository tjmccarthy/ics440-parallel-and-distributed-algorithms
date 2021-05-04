/**This program is an implementation of the Floyd-Warshall algorithm that uses the parallelism for independent portions
 * of the computation to speed up the time needed to find each of the shortest paths within the matrix.              */

package edu.metrostate.trevormccarthy.pa4;
import java.util.ArrayList;
import java.util.Random;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class Conc_FloydWarshall {
    private static int V = ConstantVals.VERT_COUNT;
    private static int I = ConstantVals.INFINITY;
    private static int maxD = ConstantVals.MAX_DISTANCE + 1;
    private static int adjacencyMatrix[][] = new int[V][V];
    private static int distances[][] = new int[V][V];
    private static int d[][] = new int[V][V];
    private static double fill = 0.3;

    /* Generate the adjacency matrix and populate with random distances.  */
    private static void generateMatrices() {
        Random random = new Random();
        for (int i = 0; i < V; i++){
            for (int j = 0; j < V; j++){
                if (i != j)
                adjacencyMatrix[i][j] = I;
            }
        }
        for (int i = 0; i < V * V * fill; i++){
            adjacencyMatrix[random.nextInt(V)][random.nextInt(V)] = random.nextInt(maxD);
        }
        System.out.println("Matrix generated..");
    }

    /* Execute Floyd Warshall on adjacencyMatrix.  */
    private static void execute() {
        for (int i = 0; i < V; i++) {
            for (int j = 0; j < V; j++)
            {
                d[i][j] = adjacencyMatrix[i][j];
                if (i == j)
                {
                    d[i][j] = 0;
                }
            }
        }
        for (int k = 0; k < V; k++) {
            for (int i = 0; i < V; i++) {
                for (int j = 0; j < V; j++) {
                    if (d[i][k] == I || d[k][j] == I) {
                        continue;
                    } else if (d[i][j] > d[i][k] + d[k][j]) {
                        d[i][j] = d[i][k] + d[k][j];
                    }
                }
            }
            //System.out.println("pass " + (k + 1) + "/" + V);
        }
    }

    /**Execute Floyd Warshall on adjacencyMatrix. Because k is dependent upon itself, its loop cannot
     * be processed concurrently. Instead, parallelism takes place on the two inner loops. The logic
     * used to execute the concurrent form of execute was adapted from the Callables, Futures and
     * Thread pools communication during Week 5. The partitioning of the rows is                                      */
    private static void execute(int threadCount) {
        System.out.println("Executing with " + threadCount + " threads.");
        ExecutorService executor = Executors.newFixedThreadPool(threadCount);
        int taskBlock1 = V/10;
        //int taskBlock2 = (int) Math.sqrt(V/8);                    // To be used for row partitioning.
        Callable<Boolean> task;                                     // FloydTask instance to be submitted to thread pool.
        Future<Boolean> taskFuture;                                 // Used to submit FloydTasks to be executed. 
        ArrayList<Future<Boolean>> futureList = new ArrayList<>();  // Store future objects associated with the FloydTasks.

        for (int i = 0; i < V; i++) {
            for (int j = 0; j < V; j++) {
                distances[i][j] = adjacencyMatrix[i][j];
                if (i == j) {
                    distances[i][j] = 0;
                }
            }
        }

        /* Procress and store the results for each partition associated with the current iteration of k. */
        for (int k = 0; k < V; k++) {
            for (int i = 0; i < V; i += taskBlock1) {
                task = new FloydTask(distances, i, (i+taskBlock1), k, V);
                taskFuture = executor.submit(task);
                futureList.add(taskFuture);
            }
            for (Future<Boolean> fut : futureList){
                try{
                    fut.get();
                }
                catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
        executor.shutdown();
    }

    /* Print matrix[V][V] */
    private static void print(int matrix[][]) {
        for (int i = 0; i < V; i++) {
            for (int j = 0; j < V; j++) {
                if (matrix[i][j] == I) {System.out.print("I" + " ");}
                else {System.out.print(matrix[i][j] + " ");}
            }
            System.out.println();
        }
    }

    /* Compare two matrices, matrix1[V][V] and matrix2[V][V] and print if they equivalent or not. */
    private static void compare (int matrix1[][], int matrix2[][]) {
        for (int i = 0; i < V; i++) {
            for (int j = 0; j < V; j++) {
                if (matrix1[i][j] != matrix2[i][j]) {System.out.println("Comparison failed");}
            }
        }
        System.out.println("Comparison succeeded");
    }
    
    public static void main(String[] args) {
        int totalThreads = 10;
        generateMatrices();
        
        System.out.println("Solving matrix d with sequential algorithm.");
        long start = System.nanoTime();
        execute();
        long end = System.nanoTime();
        System.out.println("Sequential Floyd-Warshall solved in: " + (double)(end - start) / 1000000000);
        System.out.println("------------------------------------------------\nSolving matrix distances with parallel algorithm.");

        for (int i = 1; i <= totalThreads; i++){
            start = System.nanoTime();
            execute(i);
            end = System.nanoTime();
            System.out.println("Floyd-Warshall using concurrency solved in: " + (double)(end - start) / 1000000000);
        }
        System.out.println("_____________________________________________\n");
        compare(d, distances);
        int cpuCount = Runtime.getRuntime().availableProcessors();
        System.out.println("Tests were conducted on a system with " + cpuCount + " CPUs.");
    }
}