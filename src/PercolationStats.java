/*----------------------------------------------------------------
 *  Author:        Eddie Chou
 *  Written:       1/31/2017
 *  Last updated:  1/31/2017
 *
 *  Compilation:   javac PercolationStats.java
 *  Execution:     java PercolationStats n t
 *  
 *  Estimate the value of the percolation threshold via Monte Carlo 
 *  simulation.
 *  Uses an n x n grid and t trials.
 *
 *  $ java PercolationStats 2 10000
 * mean                    = 0.6668
 * stddev                  = 0.11780979549592113
 * 95% confidence interval = [0.6644909280082799, 0.66910907199172]
 *
 *----------------------------------------------------------------*/

import edu.princeton.cs.algs4.StdRandom;
import edu.princeton.cs.algs4.StdStats;

public class PercolationStats {
	private int numTrials;	// The number of trials to perform
	private Percolation perc;		// Percolation model object
	private double[] results;		// Results of each trial (ratio of open/closed sites)
	
	// perform trials independent experiments on an n-by-n grid
   public PercolationStats(int n, int trials){
	   if(n<=0 || trials<=0) throw new java.lang.IllegalArgumentException();
	   numTrials = trials;
	   results = new double[trials];
	   // Run trials
	   for(int i=0; i<numTrials; i++){
		   perc = new Percolation(n);
		   while(!perc.percolates()){
			   int row = StdRandom.uniform(1, n+1);
			   int col = StdRandom.uniform(1, n+1);
			   perc.open(row, col);
		   }
		   
		   results[i] = (double)perc.numberOfOpenSites() / (double)(n*n);
	   }

   }
   
   // sample mean of percolation threshold
   public double mean(){
	   return StdStats.mean(results);
   }
   
   // sample standard deviation of percolation threshold
   public double stddev(){
	   return StdStats.stddev(results);
   }
   
   // low  endpoint of 95% confidence interval
   public double confidenceLo(){
	   return mean() - (1.96*stddev() / Math.sqrt(numTrials));
   }
   
   // high endpoint of 95% confidence interval
   public double confidenceHi(){
	   return  mean() + (1.96*stddev()/ Math.sqrt(numTrials));
   }
   
   // test client (described below)
   public static void main(String[] args) {
	   int n = Integer.parseInt(args[0]);
	   int trials = Integer.parseInt(args[1]);
	   PercolationStats perc = new PercolationStats(n, trials);
	   System.out.println("mean                    = " + perc.mean());
	   System.out.println("stddev                  = " + perc.stddev());
	   System.out.println("95% confidence interval = [" + perc.confidenceLo() + ", " + perc.confidenceHi() + "]");
   }
}