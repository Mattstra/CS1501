import java.util.*;
import java.io.*;

public class Airline
{
	static Scanner scan;
	static Scanner read;
	static String airFile;
	
	public Airline()
	{
	
	}
	
	public void loadFile()
	{
		try
		{
			read = new Scanner(new FileInputStream(airFile));
		}

		catch(FileNotFoundException e)
		{
			System.out.println("File was not found \n");
		}
	}
	
	public static void main(String[] args) throws IOException
	{
		int option;
		int routes = 0;
		int cityNum;
		int currVertex;
		
		Airline air = new Airline();
		scan = new Scanner(System.in);
		System.out.println("Enter the filename of the airline routes: \n");
		airFile = scan.next();
		air.loadFile();
		
		//get number of vertices(cities)
		cityNum = read.nextInt();
		System.out.println(cityNum);
		
		//array to hold vertex names
		String cities[] = new String[cityNum];
		for(int i = 0; i < cityNum; i++)
		{
			cities[i] = read.next();
			System.out.println(cities[i]);
		}
		
		//create empty adjacency list with no edges
		EdgeWeightedGraph adjList = new EdgeWeightedGraph(cityNum+1);
		
		//add edges to list in loop
		while(read.hasNextInt())
		{
			//get current vertex
			currVertex = read.nextInt();
			
			//create edge
			Edge newEdge = new Edge(currVertex, read.nextInt(), read.nextInt(), (int)read.nextFloat());
			
			//add edge
			adjList.addEdge(newEdge);
			
		}
		
		while(true)
		{
			//Menu
			System.out.println("Select an option number: \n");
			System.out.println("[1] Show all direct routes \n");
			System.out.println("[2] Display minimum spanning tree based on distance \n");
			System.out.println("[3] Perform a custom shortest path search \n");
			System.out.println("[4] Display all trips less than or equal to entered price \n");
			System.out.println("[5] Add a new route \n");
			System.out.println("[6] Remove a route from the schedule \n");
			System.out.println("[7] Quit and save \n");
			
			//user enters a number
			option = scan.nextInt();
			
			read.close();
			
			if(option == 1)
			{
				int i = 1;
				
				System.out.println("List of all direct routes including reveresed routes: \n");
				
				while(routes < cityNum)
				{
					for(Edge e : adjList.adj(i))
					{	
						//print route info
						System.out.println(cities[i-1] + " to " + cities[e.other(i)-1] + " is " + e.distanceWeight() + " mi and costs $" + e.priceWeight());
						
					}
					
					//increment vertex
					i++;
					routes++;
				}
			}
			
			//display minimum spanning tree
			if(option == 2)
			{
				System.out.println("Minimum Spanning Tree based on route distance");
				
				//create MST
				PrimMST mst = new PrimMST(adjList);
				
		        for (Edge e : mst.edges()) 
		        {
		            System.out.println(cities[e.either() -1] +", " + cities[e.other(e.either()) -1] + ": " + e.distanceWeight() + " mi" );
		        }
			}
			
			
			//Shortest path searches
			if(option == 3)
			{
				int option2;
				
				//create empty directed graph of current graph
				EdgeWeightedDigraph dadjList = new EdgeWeightedDigraph(cityNum+1);
				
				air.loadFile();
				//get number of vertices(cities)
				cityNum = read.nextInt();
				
				//skip past cities
				while(read.hasNextInt() == false)
				{
					String city = read.next();
				}
				
				//add edges to list in loop
				while(read.hasNextInt())
				{
					//get current vertex
					currVertex = read.nextInt();
					
					//create edge
					DirectedEdge newEdge = new DirectedEdge(currVertex, read.nextInt(), read.nextInt(), (int)read.nextFloat());
					
					//add edge
					dadjList.addEdge(newEdge);
				}
				
				System.out.println("Select type of shortest path search: ");
				System.out.println("[1] Total miles");
				System.out.println("[2] Price");
				System.out.println("[3] Number of hops \n");
				
				option2 = scan.nextInt();
				
				//user enters source and destination cities
				System.out.println("Enter the source city \n");
				String source = scan.next();
				System.out.println("Enter the destination city \n");
				String destination = scan.next();
				
				//shortest path based on total miles
				if(option2 == 1)
				{
					//j is used to match string array index to vertex number
					int j = 0;
					int k = 0;
					
					//match source to vertex number
					while(j < cityNum)
					{
						if(cities[j].equals(source))
							break;
						j++;
					}
					
					while(k < cityNum)
					{
						if(cities[k].equals(destination))
							break;
						k++;
					}
					
					j++;	//matching source vertex number
					k++;	//matching destination vertex number
					
					//get shortest path using Dijkstra's algorithm
					DijkstraSP sp = new DijkstraSP(dadjList, j);
					
					//see if path exists
					if(sp.hasPathTo(k) == false)
					{
						System.out.println("No path exists between source and destination cities \n");
					}
					
					else
					{
						double total = 0;
						
						 for (DirectedEdge e : sp.pathTo(k)) 
					        {
					            total = total + e.distanceWeight();
					        }
						 //print shortest path info
						 System.out.println("Shortest distance path from " + cities[j-1] + " to " + cities[k-1] + " is " + total + " mi");
						 System.out.println("Cities in shortest distance path with distances:");
						 
						 for (DirectedEdge e : sp.pathTo(k)) 
					        {
					            System.out.println(cities[e.from() -1] + ", " + cities[e.to() -1] + ": " + e.distanceWeight() + " mi");
	
					        }
					}
				}
				
				//shortest path based on price
				if(option2 == 2)
				{
					//j is used to match string array index to vertex number
					int j = 0;
					int k = 0;
					
					//match source to vertex number
					while(j < cityNum)
					{
						if(cities[j].equals(source))
							break;
						j++;
					}
					
					while(k < cityNum)
					{
						if(cities[k].equals(destination))
							break;
						k++;
					}
					
					j++;	//matching source vertex number
					k++;	//matching destination vertex number
					
					//get shortest path using Dijkstra's algorithm
					DijkstraPriceSP sp = new DijkstraPriceSP(dadjList, j);
					
					//see if path exists
					if(sp.hasPathTo(k) == false)
					{
						System.out.println("No path exists between source and destination cities \n");
					}
					
					else
					{
						double total = 0;
						
						 for (DirectedEdge e : sp.pathTo(k)) 
					        {
					            total = total + e.priceWeight();
					        }
						 //print shortest path info
						 System.out.println("Minimum price path from " + cities[j-1] + " to " + cities[k-1] + " is $" + total);
						 System.out.println("Cities in minimum price path with prices:");
						 
						 for (DirectedEdge e : sp.pathTo(k)) 
					        {
					            System.out.println(cities[e.from() -1] + ", " + cities[e.to() -1] + ":  $" + e.priceWeight());
	
					        }
					}
				}
				
				//shortest path based on hops using breadth first search
				if(option2 == 3)
				{
					
					//variables to store weights so scanner can advance
					int dist;
					float price;
					
					//create directed graph with no weights
					Digraph graph = new Digraph(cityNum+1);
					
					air.loadFile();
					//get number of vertices(cities)
					cityNum = read.nextInt();
					
					//skip past cities
					while(read.hasNextInt() == false)
					{
						String city = read.next();
					}
					
					//add edges to list in loop
					while(read.hasNextInt())
					{
						//get current vertex
						currVertex = read.nextInt();
						
						//create edge
						graph.addEdge(currVertex, read.nextInt());
						
						//advance scanner past weights
						dist = read.nextInt();
						price = read.nextFloat();
						
					}
					
					//j is used to match string array index to vertex number
					int j = 0;
					int k = 0;
					
					//match source to vertex number
					while(j < cityNum)
					{
						if(cities[j].equals(source))
							break;
						j++;
					}
					
					while(k < cityNum)
					{
						if(cities[k].equals(destination))
							break;
						k++;
					}
					
					j++;	//matching source vertex number
					k++;	//matching destination vertex number
					
					//create breadth first search
					BreadthFirstDirectedPaths bfs = new BreadthFirstDirectedPaths(graph, j);
					
					//see if path exists
					if(bfs.hasPathTo(k) == false)
					{
						System.out.println("No path exists between source and destination cities \n");
					}
					
					else
					{
						System.out.println("Fewest number of hops from source to destination is " + bfs.distTo(k));
						System.out.println("Cities along path in order: ");
						 for (Integer e : bfs.pathTo(k)) 
					        {
					            System.out.println(cities[e-1]);
					        }
					}
					
				}
				
			}
			
			//Print all trips whose cost is less than or equal to entered amount
			if(option == 4)
			{
				int amount;
				int currentV = 1;		//current vertex
				int destV = 2;
				
				System.out.println("Enter dollar amount:");
				amount = scan.nextInt();
				
				System.out.println("All trips for less than or equal to entered amount: \n");
				
				do
				{
					 for (Edge e : adjList.adj(currentV)) 
				        {
				            if(e.priceWeight() <= amount)
				            {
				            	System.out.println(cities[e.either()-1] + " to " + cities[e.otherPoint()-1] + " is $" + e.priceWeight());
				            }
				        }
					 currentV++;
				}
				while(currentV < cityNum);
				
				
			}
			
			//add new route
			if(option == 5)
			{
				int sourceCity;
				int destCity;
				int routeDist;
				float routePrice;
				
				System.out.println("Enter the source city number");
				sourceCity = scan.nextInt();
				System.out.println("Enter the destination city number");
				destCity = scan.nextInt();
				System.out.println("Enter the distance from source to destination");
				routeDist = scan.nextInt();
				System.out.println("Enter the price of the route");
				routePrice = scan.nextFloat();
				
				
				//add routes to end of file
				PrintWriter printer = null;
				try
				{
					printer = new PrintWriter(new FileOutputStream(airFile, true));
	
				}
	
				catch(FileNotFoundException e)
				{
					System.out.println(e);
				}
				printer.write("\r\n" + sourceCity + " " + destCity + " " + routeDist + " " + routePrice);
				printer.flush();
				printer.close();
				
				System.out.println("Route added \n");
			}
			
			//Remove route
			if(option == 6)
			{
				//temp file to write to then rename
				File tempFile = new File("myAir.txt");
				
				File inputFile = new File(airFile);
				
				PrintWriter writer = new PrintWriter(new FileWriter(tempFile, true));
				
				int sourceCity;
				int destCity;
				
				System.out.println("Enter the source city number");
				sourceCity = scan.nextInt();
				System.out.println("Enter the destination city number");
				destCity = scan.nextInt();
				
				air.loadFile();
				
				//get number of vertices(cities)
				cityNum = read.nextInt();
				
				writer.write(cityNum + "\r\n");
	
				
				//read each city and copy to temp file
				while(read.hasNextInt() == false)
				{
					read.nextLine();
					writer.write(read.next() + "\r\n");
				}
				
				//loop to find matching route
				while(read.hasNextLine())
				{
					if(read.hasNextInt() == false)
						break;
					int vertex1 = read.nextInt();
					int vertex2 = read.nextInt();
					
					
					if((vertex1 == sourceCity) && (vertex2 == destCity))
					{
						//found route
						int d = read.nextInt();
						float p = read.nextFloat();
					}
					
					else
					{
						
						int dist = read.nextInt();
						float price = read.nextFloat();
						
						
						writer.write(vertex1 + " " + vertex2 + " " + dist + " " + price + "\r\n");
						writer.flush();
					}
				}
				writer.close();
				read.close();
				
				inputFile.delete();
				
				//rename temp
				if(tempFile.renameTo(inputFile))
					System.out.println("Route deleted \n");
				else
				{
					System.out.println("Not changed");
				}
				
				scan = new Scanner(System.in);
			}
			
			//exit program
			if(option == 7)
			{
				System.out.println("Program closed \n");
				System.exit(0);
			}
		}
	}
}