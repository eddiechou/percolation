# PercolationStats.java

A Java program that estimates the value of the percolation threshold via Monte Carlo simulation.

Initializes an n x n grid to all closed. Sites are opened one at a time until there is a connected path of open sites from the top of the grid to the bottom of the grid (known as percolation). This allows us to estimate the percolation threshold (the ratio of open to closed sites) by averaging the results of multiple trials.