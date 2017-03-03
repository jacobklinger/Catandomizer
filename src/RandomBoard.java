import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

/**
 * RandomBoard
 * @author Jacob Klinger
 *
 */
public class RandomBoard {

	private Terrain[] board = new Terrain[19];
	
	private int[][] borders = {
			{1, 3, 4},
			{0, 2, 4, 5},
			{1, 5, 6},
			{0, 4, 7, 8},
			{0, 1, 3, 5, 8, 9},
			{1, 2, 4, 6, 9, 10},
			{2, 5, 10, 11},
			{3, 8, 12},
			{3, 4, 7, 9, 12, 13},
			{4, 5, 8, 10, 13, 14},
			{5, 6, 9, 11, 14, 15},
			{6, 10, 15},
			{7, 8, 13, 16},
			{8, 9, 12, 14, 16, 17},
			{9, 10, 13, 15, 17, 18},
			{10, 11, 18},
			{12, 13, 17},
			{13, 14, 16, 18},
			{14, 16, 17}
	};

	public RandomBoard() {
		this(false,false);
	}

	public RandomBoard(boolean desertOnEdge, boolean limitClusters) {
		if (limitClusters) {
			randomizeNoTouching(desertOnEdge);
		}
		else {
			randomize(desertOnEdge);
		}
	}

	public Terrain[] getBoard() {
		return board;
	}

	public void randomize(boolean desertOnEdge) {

		// Array List to hold numbers 0 through 18
		ArrayList<Integer> randomPool = new ArrayList<Integer>();
		int poolSize = 19;

		// Fill it with the numbers 0 through 18
		for (int i = 0; i < 19; i++) {
			randomPool.add(i);
		}
		
		// Save the index the desert was assigned to
		int random = assignDesert(desertOnEdge);
		// Remove that index from future consideration
		randomPool.remove(random);
		// Decrement the number of indices that are available
		poolSize--;

		//Choose 4 random indices to be wood
		for (int i = 0; i < 4; i++) {
			// Get a random index from the pool
			random = (int)(Math.random() * poolSize);
			// Assign the board at that index to be wood
			board[randomPool.get(random)] = Terrain.wood;
			// Remove that index from future consideration
			randomPool.remove(random);
			// Decrement the number of indices that are empty
			poolSize--;
		}

		// Sheep
		for (int i = 0; i < 4; i++) {
			random = (int)(Math.random() * poolSize);
			board[randomPool.get(random)] = Terrain.sheep;
			randomPool.remove(random);
			poolSize--;
		}

		// Wheat
		for (int i = 0; i < 4; i++) {
			random = (int)(Math.random() * poolSize);
			board[randomPool.get(random)] = Terrain.wheat;
			randomPool.remove(random);
			poolSize--;
		}

		// Brick
		for (int i = 0; i < 3; i++) {
			random = (int)(Math.random() * poolSize);
			board[randomPool.get(random)] = Terrain.brick;
			randomPool.remove(random);
			poolSize--;
		}

		// Rock
		for (int i = 0; i < 3; i++) {
			random = (int)(Math.random() * poolSize);
			board[randomPool.get(random)] = Terrain.rock;
			randomPool.remove(random);
			poolSize--;
		}

	}

	public void randomizeNoTouching(boolean desertEdge) {
		int i = 0;
		boolean perfectBoard = false;
		while (!perfectBoard && i < 100) {
			randomizeNoTouchingHelper(desertEdge);
			i++;
			perfectBoard = isPerfectBoard();
		}
	}
	
	
	private void randomizeNoTouchingHelper(boolean desertEdge) {
		Integer[] init = {9, 5, 8, 14, 4, 10, 13, 0, 2, 11, 18, 16, 7, 1, 6, 15, 17, 12, 3};
		ArrayList<Integer> order = new ArrayList<Integer>(Arrays.asList(init));

		HashMap<Terrain, Integer> terrainCounts = new HashMap<Terrain, Integer>();
		terrainCounts.put(Terrain.wood, 4);
		terrainCounts.put(Terrain.wheat, 4);
		terrainCounts.put(Terrain.sheep, 4);
		terrainCounts.put(Terrain.rock, 3);
		terrainCounts.put(Terrain.brick, 3);
		terrainCounts.put(Terrain.desert, 1);

		if (desertEdge) {
			Integer desert = assignDesert(desertEdge);
			order.remove(desert);
			terrainCounts.put(Terrain.desert, 0);
		}

		HashSet<Terrain> runOut = new HashSet<Terrain>();
		if (desertEdge) {
			runOut.add(Terrain.desert);
		}

		// For each board index in the array order
		for (int index: order) {

			// Make a copy of run out, to use as excluded the excluded set
			HashSet<Terrain> exclude = (HashSet<Terrain>) runOut.clone();

			// For each tile that borders the current tile
			for (int borderIndex: borders[index]) {
				// If the tiles is filled
				if (board[borderIndex] != null) {
					// Add that to the exclusion set
					exclude.add(board[borderIndex]);
				}
			}

			// Get a random terrain that not already bordering the current tile
			// And has not run out
			Terrain current = randomFromSet(makeSet(exclude));

			// If current is null then no terrain meet the requirements
			// Get a random terrain that has not already run out
			if (current == null) {
				current = randomFromSet(makeSet(runOut));
			}

			// Set the terrain at the current tile
			board[index] = current;

			// Update remaining terrain
			terrainCounts.put(current, terrainCounts.get(current) - 1);

			// Update runOut
			runOut = updateRunOut(terrainCounts);
		}
	}

	
	private int assignDesert(boolean desertEdge) {
		if (desertEdge) {
			int[] edgeIndices = {0, 1, 2, 3, 6, 7, 11, 12, 15, 16, 17, 18};
			// Select an index at random from edgeIndices
			int random = (int)(Math.random() * edgeIndices.length);
			// Make that index on the board desert
			board[edgeIndices[random]] = Terrain.desert;
			
			return edgeIndices[random];
		}
		else {
			// Select an index from random from anywhere on the board
			int random = (int)(Math.random() * 20);
			// Make that index on the board desert
			board[random] = Terrain.desert;
			
			return random;
		}
		
	}

	private HashSet<Terrain> updateRunOut(HashMap<Terrain, Integer> terrainCounts) {
		HashSet<Terrain> runOut = new HashSet<Terrain>();

		for (Map.Entry<Terrain, Integer> entry : terrainCounts.entrySet()) {
			if (entry.getValue() <= 0) {
				runOut.add(entry.getKey());
			}
		}

		return runOut;
	}

	private Terrain[] makeSet(HashSet<Terrain> exclude) {
		// Initialize array with max number of terrains
		Terrain[] set = new Terrain[6];
		// To keep track of the final size
		int finalSize = 0;

		if (!exclude.contains(Terrain.wood)) {
			set[finalSize] = Terrain.wood;
			finalSize++;
		}
		if (!exclude.contains(Terrain.wheat)) {
			set[finalSize] = Terrain.wheat;
			finalSize++;
		}
		if (!exclude.contains(Terrain.sheep)) {
			set[finalSize] = Terrain.sheep;
			finalSize++;
		}
		if (!exclude.contains(Terrain.rock)) {
			set[finalSize] = Terrain.rock;
			finalSize++;
		}
		if (!exclude.contains(Terrain.brick)) {
			set[finalSize] = Terrain.brick;
			finalSize++;
		}
		if (!exclude.contains(Terrain.desert)) {
			set[finalSize] = Terrain.desert;
			finalSize++;
		}

		if (finalSize > 0) {
			return Arrays.copyOfRange(set, 0, finalSize);
		}
		else {
			return null;
		}

	}

	private Terrain randomFromSet(Terrain[] set) {
		// If the set is null, then return null
		if (set == null) {
			return null;
		}

		// Otherwise return a random element from the set
		int index = (int)(Math.random() * set.length);
		return set[index];
	}

	private boolean isPerfectBoard() {
		for (int i = 0; i < borders.length; i++) {
			for (int j: borders[i]) {
				if (board[i] == board[j]) {
					return false;
				}
			}
		}
		return true;
	}
	
	public static void main(String[] args) {
		RandomBoard test = new RandomBoard(true, true);
	}
	
}
