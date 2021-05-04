/**The FloydTask class shares the workload with other threads in a threadpool as they each operate
 * in parallel on their respective slice of the partitioned row of some iteration of k.         */

package edu.metrostate.trevormccarthy.pa4;
import java.util.concurrent.Callable;

public class FloydTask implements Callable<Boolean>{
    protected int[][] distances;                    // Each thread gets its own instance of the distances array.
    protected int I = ConstantVals.INFINITY;        // Infinite value for path not yet discovered.
    protected int firstIndex;                       // Starting index for this task. 
    protected int lastIndex;                        // Last index the mapped thread is checking.
    protected int kValue;                           // Current iteration of k being checked.
    protected int totalV;                           // Number of vertices in the problem.

    public FloydTask(int[][] distances, int firstIndex, int lastIndex, int kValue, int totalV){
        this.distances = distances;
        this.firstIndex = firstIndex;
        this.lastIndex = lastIndex;
        this.kValue = kValue;
        this.totalV = totalV;
    }

    public Boolean call(){
        Boolean bool = new Boolean(true);
        // Prevent out of index issue when the partitioned block's value plus the first inner
        // loop's index value is a higher number than the row's length.
        if (totalV < lastIndex){
            bool = false;
            lastIndex = totalV;
        }

        for (int row = firstIndex; row < lastIndex; row++){
            for (int col = 0; col < totalV; col++) {
                if (distances[row][kValue] == I || distances[kValue][col] == I) continue;
                if (distances[row][col] > distances[row][kValue] + distances[kValue][col])
                distances[row][col] = distances[row][kValue] + distances[kValue][col];
            }
        }
        return bool;
    }

    private int[][] mergeMatrices(int[][] a, int[][] b, int sizeOfSub) {
		int size = a.length;
		System.out.println(size);
		int[][] c = new int[size][size];
		int sum;
		
		long start = System.currentTimeMillis();
		for(int i=0; i<size; i++) {
			for(int j=0; j<size; j++) {
				sum = 0;
				for(int m=0; m<size; m++) {
					sum = a[i][m]*b[m][j];
				}
				c[i][j] = sum;
			}
		}
		long end = System.currentTimeMillis();
		System.out.printf("Time needed to compose: %d, %d%n", start, end);
		return c;
	}
}