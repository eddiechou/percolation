/*----------------------------------------------------------------
 *  Author:        Eddie Chou
 *  Written:       1/31/2017
 *  Last updated:  1/31/2017
 *
 *  Compilation:   javac Percolation.java
 *  
 *  Percolation model class. 
 *
 *----------------------------------------------------------------*/

import edu.princeton.cs.algs4.WeightedQuickUnionUF;

public class Percolation {
	// Possible states that a site can be in
	private static final byte CLOSED = 0;
	private static final byte OPEN = 1;
	private static final byte CONNECTED_TOP = 2;
	private static final byte CONNECTED_BOTTOM = 4;
	private static final byte CONNECTED_BOTH = 7;
	private byte[] state;
	
	private int gridSize; // Grid size of the model
	private int numOpenSites;	// Keeps track of the number of open sites
	
	private WeightedQuickUnionUF connections;	// The union-find data type					
	private boolean hasPercolated;				// A flag for if the model percolates
	
	// create n-by-n grid, with all sites blocked
   public Percolation(int n) {
	   if (n <= 0) throw new IllegalArgumentException();
	   hasPercolated = false;
	   gridSize = n;
	   
	   int numSites = xyTo1D(n+1, n+1);
	   numOpenSites = 0;
	   state = new byte[numSites];	// 1 to n indexing
	   // Initialize all states to closed (false)
	   for (int i = 1; i < n+1; i++) {
		   for(int j = 1; j < n+1; j++) {
			   state[xyTo1D(i,j)] = CLOSED;
		   }
	   }
	   connections = new WeightedQuickUnionUF(numSites);	// All sites initially in own component
   }
   
   // open site (row, col) if it is not open already
   public void open(int row, int col) {
	   checkBounds(row, col);
	   int site = xyTo1D(row, col);
	   int top;
	   int left;
	   int right;
	   int bot;
	   byte topState = 0, leftState = 0, rightState = 0, botState = 0;
	   if ((state[site] & OPEN) == OPEN) {
		   return;
	   }
	   state[site] |= OPEN;
	   
	   if (row == 1) {
		   state[site] |= CONNECTED_TOP;
	   }
	   if (row == gridSize) {
		   state[site] |= CONNECTED_BOTTOM;
	   }
	   int neighbor;
	   // Set up WeightedQuickUnionUF unions
	   // If top is open
	   if (row > 1 && isOpen(row-1, col)) {
		   top = xyTo1D(row-1, col);
		   neighbor = connections.find(top);	// gets root of top
		   topState = state[neighbor];			// gets state of top's root
		   connections.union(site, neighbor);	// connect top and site
	   }
	   // If left is open
	   if (col > 1 && isOpen(row, col-1)) {
		   left = xyTo1D(row, col-1);
		   neighbor = connections.find(left);	// gets root of left
		   leftState = state[neighbor];			// gets state of left's root
		   connections.union(site, neighbor);
	   }
	   // If right is open
	   if (col < gridSize && isOpen(row, col+1)) {
		   right = xyTo1D(row, col+1);
		   neighbor = connections.find(right);	// gets root of left
		   rightState = state[neighbor];			// gets state of left's root
		   connections.union(site, neighbor);
	   }
	   // If bottom is open
	   if (row < gridSize && isOpen(row+1, col)) {
		   bot = xyTo1D(row+1, col);
		   neighbor = connections.find(bot);	// gets root of left
		   botState = state[neighbor];			// gets state of left's root
		   connections.union(site, neighbor);
	   }
	   
	   int newRoot = connections.find(site);
	   state[newRoot] |= (byte)(topState | leftState | rightState | botState | state[site]);
	   // Check if newRoot is connected to both top and bottom
	   if ((state[newRoot] & CONNECTED_BOTH) == CONNECTED_BOTH) {
		   hasPercolated = true;
	   }
	   numOpenSites++;
   }
   
   private void checkBounds(int row, int col) {
	   if (row < 1 || row > gridSize || col < 1 || col > gridSize) {
		   throw new IndexOutOfBoundsException();
	   }
   }
   
   private void checkBoundsState(int row, int col) {
	   if (row < 0 || row > gridSize || col < 0 || col > gridSize) {
		   throw new IndexOutOfBoundsException();
	   }
   }
   
   // Translate to start = 0 (top-left will be 1, 1)
   private int xyTo1D(int row, int col) {
	   return (row * gridSize) + col;
   }
   
   // is site (row, col) open?
   public boolean isOpen(int row, int col) {
	   checkBoundsState(row, col);
	   return state[xyTo1D(row, col)] > 0;	// return state of root
   }
   
   // is site (row, col) full/closed?
   public boolean isFull(int row, int col) {
	   checkBounds(row, col);
	   return (state[connections.find(xyTo1D(row, col))] & CONNECTED_TOP) == CONNECTED_TOP;	// Find state of root 
   }
   
   // number of open sites
   public int numberOfOpenSites() {
	   return numOpenSites;
   }
   // does the system percolate?
   // Connect from virtual point at top, and at bottom
   public boolean percolates() {
	   return hasPercolated;
   }
   
   // test client (optional)
   public static void main(String[] args) {
	   
   }
}