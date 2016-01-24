//Arpan Sunil Tolat & Ameya Raje
//Homework 4 optional code implementation
//The Code has been sourced from http://www.sanfoundry.com/java-program-implement-ford-fulkerson-algorithm/
//We have used an array for representation and manipulation of the graph
//We have manipulated the BFS function to find the min-cut as follows
//-Run BFS on the final residual graph
//-Return edges that have capacity==flow
//-This can be found by comparing the original graph with the final residual graph
//
//We have used BFS function again to implement the second part of the problem, that is to patch up the graph if an edge capacity is modified
//Since we are not very adept with advanced java coding, we have not implemented a text parser in the code, we have used scanner to take inputs from user directly from the java console
//The input graph must be entered in row wise array format
//For your reference, the input for the assignment question is 0 20 15 10 0 0 0 0 0 0 4 5 9 0 0 5 0 0 0 6 0 0 0 0 0 8 0 0 0 0 0 0 0 25 10 0 0 0 0 5 0 30 0 0 0 0 0 0 0
//The output will be in the format of a matrix(for the incremental updates part)


import java.util.LinkedList;
import java.util.Queue;
import java.util.Scanner;


/*******************CONSTRUCTOR FOR INITIALIZATION************************/
public class Application1       

{

	private int[] parent;

	private Queue<Integer> queue;

	private int numberOfVertices;

	private boolean[] visited;

	public int[][] residualGraph;

	public Application1(int numberOfVertices)

	{

		this.numberOfVertices = numberOfVertices;

		this.queue = new LinkedList<Integer>();

		parent = new int[numberOfVertices + 1];

		visited = new boolean[numberOfVertices + 1];
		residualGraph = new int[numberOfVertices + 1][numberOfVertices + 1];

	}

/*********************BFS FOR FINDING AUGMENTING PATHS IN THE RESIDUAL GRAPH********************/

	public boolean bfs(int source, int goal, int graph[][])

	{

		boolean pathFound = false;

		int destination, element;



		for(int vertex = 1; vertex <= numberOfVertices; vertex++)

		{

			parent[vertex] = -1;

			visited[vertex] = false;

		}



		queue.add(source);

		parent[source] = -1;

		visited[source] = true;



		while (!queue.isEmpty())

		{ 

			element = queue.remove();

			destination = 1;



			while (destination <= numberOfVertices)

			{

				if (graph[element][destination] > 0 &&  !visited[destination])

				{

					parent[destination] = element;

					queue.add(destination);

					visited[destination] = true;

				}

				destination++;

			}

		}

		if(visited[goal])

		{

			pathFound = true;

		}

		return pathFound;

	}

/***********TO FIND MIN_CUT (Modified version of BFS function that returns edges with flow == capacity)***********************************/
	
	public boolean min_cut(int source, int goal, int graph[][],int maxFlow){
		System.out.println("Min Cut");
		boolean pathFound = false;
		int destination, element;
		int addFlow = 0;    //Keeps track of flow along path so it does not exceed max flow, used to terminate BFS and return edge
		for(int vertex = 1; vertex <= numberOfVertices; vertex++)

		{
			parent[vertex] = -1;
			visited[vertex] = false;
		}

		queue.add(source);
		parent[source] = -1;
		visited[source] = true;
		while (!queue.isEmpty()){ 
			element = queue.remove();
			destination = 1;
			while (destination <= numberOfVertices){
				if (graph[element][destination] > 0 ){
					parent[destination] = element;
					if(!visited[destination])
						queue.add(destination);
					visited[destination] = true;
					if(graph[element][destination] == residualGraph[destination][element]){
						addFlow+=graph[element][destination];
						System.out.printf("%d - %d\n",element,destination);
					}
					if(addFlow == maxFlow)    {
						return true;

					}        
				}

				destination++;

			}

		}

		if(visited[goal])

		{

			pathFound = true;

		}

		return pathFound;

	}

	public void cal_residual(int graph[][]){

		for (int sourceVertex = 1; sourceVertex <= numberOfVertices; sourceVertex++)

		{

			for (int destinationVertex = 1; destinationVertex <= numberOfVertices; destinationVertex++)

			{

				residualGraph[sourceVertex][destinationVertex] = graph[sourceVertex][destinationVertex];

			}

		}

	}
	public int fordFulkerson(int source, int destination,int maxF)

	{
		int u, v;
		int maxFlow = maxF;
		int pathFlow;
		while (bfs(source ,destination, residualGraph))

		{
			pathFlow = Integer.MAX_VALUE;
			for (v = destination; v != source; v = parent[v])
			{
				u = parent[v];
				pathFlow = Math.min(pathFlow, residualGraph[u][v]);
			}
			for (v = destination; v != source; v = parent[v])
			{
				u = parent[v];
				residualGraph[u][v] -= pathFlow;
				residualGraph[v][u] += pathFlow;
			}

			maxFlow += pathFlow;    

		}

		for(int l=0;l<numberOfVertices+1;l++){
			for(int m=0;m<numberOfVertices+1;m++)
				System.out.printf("%d  ",residualGraph[l][m]);
			System.out.println();
		}
		return maxFlow;

	}
/****************************TO PATCH UP GRAPH INCREMENTALLY IF EDGE IS MODIFIED****************/
	public int fordFulkerson_mod( int source, int destination,int mod_Cap,int maxF)

	{
		int u, v;
		int maxFlow = maxF;
		int pathFlow= Integer.MAX_VALUE;;
		int bottle = 0;
		while (bfs(source ,destination, residualGraph) && mod_Cap>0){

			for (v = destination; v != source; v = parent[v])
			{
				u = parent[v];
				bottle = Math.min(pathFlow, residualGraph[u][v]);
			}
			if(bottle >= mod_Cap){
				pathFlow = mod_Cap;
				mod_Cap = 0;
			}
			else{
				pathFlow = bottle;
				mod_Cap-=bottle;
			}

			for (v = destination; v != source; v = parent[v])
			{
				u = parent[v];
				residualGraph[u][v] -= pathFlow;
				residualGraph[v][u] += pathFlow;

			}

			maxFlow -= pathFlow;    

		}

		for(int l=0;l<numberOfVertices+1;l++){
			for(int m=0;m<numberOfVertices+1;m++)
				System.out.printf("%d  ",residualGraph[l][m]);
			System.out.println();
		}
		return maxFlow;

	}



	public static void main(String...arg)

	{
		int[][] graph;
		int numberOfNodes;
		int source;
		int sink;
		int maxFlow;
		int mod_Head;          
		int mod_Tail;         
		int mod_Cap;



		Scanner scanner = new Scanner(System.in);
		System.out.println("Enter the number of nodes");
		numberOfNodes = scanner.nextInt();
		graph = new int[numberOfNodes + 1][numberOfNodes + 1];
		System.out.println("Enter the graph matrix");
		for (int sourceVertex = 1; sourceVertex <= numberOfNodes; sourceVertex++)
		{
			for (int destinationVertex = 1; destinationVertex <= numberOfNodes; destinationVertex++)
			{
				graph[sourceVertex][destinationVertex] = scanner.nextInt();
			}
		}
		System.out.println("Enter the source of the graph");
		source= scanner.nextInt();
		System.out.println("Enter the sink of the graph");
		sink = scanner.nextInt();


		System.out.println("Enter head of modified edge");
		mod_Head = scanner.nextInt();

		System.out.println("Enter the tail of modified edge");
		mod_Tail = scanner.nextInt();

		System.out.println("Enter the change in capacity");
		mod_Cap = scanner.nextInt();



		Application1 fordFulkerson = new Application1(numberOfNodes);
		fordFulkerson.cal_residual(graph);
		maxFlow = fordFulkerson.fordFulkerson( source, sink,0);
		System.out.println("The Max Flow is " + maxFlow);
		fordFulkerson.min_cut(source, sink , graph, maxFlow);
		scanner.close();
		System.out.println();

		maxFlow = fordFulkerson.fordFulkerson_mod(mod_Head, source, mod_Cap, maxFlow);
		System.out.println();

		fordFulkerson.fordFulkerson_mod(sink, mod_Tail, mod_Cap, maxFlow);
		System.out.println();
		maxFlow = fordFulkerson.fordFulkerson( source, sink,maxFlow);
		System.out.println("The New Max Flow is " + maxFlow);
	}

}