/**The ThreadStatisticsSetup class contains the static queue used to track calculations made by the threads from
 * within the RequestProcessor's run method. The ThreadStatisticsSetup class is also responsible for performing each
 * calculation with its updateStatistics function.                                                                */

package edu.metrostate.trevormccarthy.pa2;

public class ThreadStatisticsSetup {
    private static GenericQueue<ThreadStatisticsSetup> qPrime = new GenericQueue<>();   // For combined thread results.
    private int[] mANDmArray;                                                           // Array 0-4 for M&M colors.
    private double totals;

    public ThreadStatisticsSetup() {
        mANDmArray = new int[5];                        // Colors by index = 0:Red, 1:Brown, 2:Yellow, 3:Green, 4:Blue.
        totals = 0.0;
    }

    /**
     * The get function returns "this" or the ThreadStatisticsSetup instance used to perform a threads computation.
     * @return returns an instance of itself.                                                                    */
    public ThreadStatisticsSetup get() {return get();}

    /**
     * The getRed function returns the value stored in the assigned index.
     * @return Returns the int representation of the color's total units.    */
    public int getRed(){return mANDmArray[0];}

    /**
     * The getBrown function returns the value stored in the assigned index.
     * @return Returns the int representation of the color's total units.    */
    public int getBrown(){return mANDmArray[1];}

    /**
     * The getYellow function returns the value stored in the assigned index.
     * @return Returns the int representation of the color's total units.    */
    public int getYellow(){return mANDmArray[2];}

    /**
     * The getGreen function returns the value stored in the assigned index.
     * @return Returns the int representation of the color's total units.    */
    public int getGreen(){return mANDmArray[3];}

    /**
     * The getBlue function returns the value stored in the assigned index.
     * @return Returns the int representation of the color's total units.    */
    public int getBlue(){return mANDmArray[4];}

    /**
     * The getTotals function returns the summed amount of M&Ms between all colors.
     * @return Returns the current amount of M&Ms in the array.                 */
    public double getTotals(){return totals;}

    /**
     * The print() function iterates through the 5 nodes within qPrime's queue summing the total M&Ms by color and
     * prints the appropriate percentages each color contributes to the total count.                            */
    public static void print() {
        ThreadStatisticsSetup stats;               // To reference the value stored in the front node of qPrime's queue.
        int[] sum = new int[5];                    // Used to add M&Ms being merged by color (0-4).
        double totalMandMs = 0.0;                  // End total should match the 10000 value of the PA2Main's main loop.
        GenericQueue.GenericNode start = qPrime.getFirst();
        for (int i = 0; i < qPrime.size(); i++) {
            stats = (ThreadStatisticsSetup) start.getElement();
            System.out.println("Tabulator: " + (i + 1) + " Count " + stats.getRed() + " for color Red=" +
                    (stats.getRed() / stats.getTotals()) * 100 + "%");
            System.out.println("Tabulator: " + (i + 1) + " Count " + stats.getBrown() + " for color Brown=" +
                    (stats.getBrown() / stats.getTotals()) * 100 + "%");
            System.out.println("Tabulator: " + (i + 1) + " Count " + stats.getYellow() + " for color Yellow=" +
                    (stats.getYellow() / stats.getTotals()) * 100 + "%");
            System.out.println("Tabulator: " + (i + 1) + " Count " + stats.getGreen() + " for color Green=" +
                    (stats.getGreen() / stats.getTotals()) * 100 + "%");
            System.out.println("Tabulator: " + (i + 1) + " Count " + stats.getBlue() + " for color Blue=" +
                    (stats.getBlue() / stats.getTotals()) * 100 + "%");

            if (start.hasNext()) start = start.getNext();
            // Update M&M colors summing results of this iteration against totals within static queue
            sum[0] = stats.getRed() + sum[0];                       // Red
            sum[1] = stats.getBrown() + sum[1];                     // Brown
            sum[2] = stats.getYellow() + sum[2];                    // Yellow
            sum[3] = stats.getGreen() + sum[3];                     // Green
            sum[4] = stats.getBlue() + sum[4];                      // Blue
            totalMandMs = stats.getTotals() + totalMandMs;          // All colors
        }

        System.out.println("==Totals==\nColor Red composes " + ((100) * (sum[0] / totalMandMs)) + "% of the total");
        System.out.println("Color Brown composes " + ((100) * (sum[1] / totalMandMs)) + "% of the total");
        System.out.println("Color Yellow composes " + ((100) * (sum[2] / totalMandMs)) + "% of the total");
        System.out.println("Color Green composes " + ((100) * (sum[3] / totalMandMs)) + "% of the total");
        System.out.println("Color Blue composes " + ((100) * (sum[4] / totalMandMs)) + "% of the total");
    }

    /**
     * The updateStatistics function is called by the ThreadLocal object of the RequestProcessor class during the
     * thread's run method. The computations performed on using the element passed use the collectionLock to provide
     * thread safety. A switch statement is used to determine what color M&M is to be operated on.
     * @param element Integer representation 0-4 indicating the color of M&M.                                     */
    public void updateStatistics(Integer element) {
        switch (element) {
            case 0:
                totals++;
                mANDmArray[0]++;
                break;
            case 1:
                totals++;
                mANDmArray[1]++;
                break;
            case 2:
                totals++;
                mANDmArray[2]++;
                break;
            case 3:
                totals++;
                mANDmArray[3]++;
                break;
            case 4:
                totals++;
                mANDmArray[4]++;
                break;
            default:
                System.out.println("Illegal value passed to updateStatistics function...");
        }
    }

    /**
     * The mergeResults function stores the results produced by the RequestProcessor in a synchronized fashion by
     * requiring the thread making the call to have acquired an output lock. The results are stored to the
     * ThreadStatisticsSetup's static queue.                                                                */
    public void mergeResults(){
        qPrime.enqueue(this);
    }
}