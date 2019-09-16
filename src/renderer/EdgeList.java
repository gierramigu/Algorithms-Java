package renderer;

/**
 * EdgeList should store the data for the edge list of a single polygon in your
 * scene. A few method stubs have been provided so that it can be tested, but
 * you'll need to fill in all the details.
 *
 * You'll probably want to add some setters as well as getters or, for example,
 * an addRow(y, xLeft, xRight, zLeft, zRight) method.
 */
public class EdgeList {
	private int startY; // lowest y-value of the edgelist
	private int endY; //highest y-value of the edgelist
	private float [][] edge;
	private int size;

	public EdgeList(int startY, int endY) {
		// TODO fill this in.
		this.startY = startY;
		this.endY = endY;
		this.size = endY - startY + 1;
		//this.edge = new float [size][4];
    	this.edge = new float [4][size];
	}

	public int getStartY() {
		// TODO fill this in.
		return this.startY;
	}

	public int getEndY() {
		// TODO fill this in.
		return this.endY;
	}

	public float getLeftX(int y) {
		// TODO fill this in.
		return edge[0][y-startY];
	//	return edge[y-startY][0];
	}

	public float getRightX(int y) {
		// TODO fill this in.
		return edge[2][y-startY];
		//return edge[y-startY][2];
	}

	public float getLeftZ(int y) {
		// TODO fill this in.
		return edge[1][y-startY];
		//return edge[y-startY][1];
	}

	public float getRightZ(int y) {
		// TODO fill this in.
		return edge[3][y-startY];
		//return edge[y-startY][3];
	}

	public void addRowLeft(int y, float xLeft, float zLeft){
		if (y >= 0 && y <= endY) {
			edge[0][y - startY] = xLeft;
			edge[1][y - startY] = zLeft;
//			edge[y-startY][0] = xLeft;
//			edge[y-startY][1] = zLeft;
		}

	}

	public void addRowRight(int y, float xRight, float zRight){
		if (y >= 0 && y <= endY) {
			edge[2][y - startY] = xRight;
			edge[3][y - startY] = zRight;

//			edge[y - startY][2] = xRight;
//			edge[y - startY][3] = zRight;
		}

	}

}



// code for comp261 assignments
