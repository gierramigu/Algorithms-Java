package renderer;

import java.awt.*;
import java.util.ArrayList;
import renderer.Scene.Polygon;
import java.util.Arrays;
import java.util.List;


/**
 * The Pipeline class has method stubs for all the major components of the
 * rendering pipeline, for you to fill in.
 * 
 * Some of these methods can get quite long, in which case you should strongly
 * consider moving them out into their own file. You'll need to update the
 * imports in the test suite if you do.
 */
public class Pipeline {


	/**
	 * Returns true if the given polygon is facing away from the camera (and so
	 * should be hidden), and false otherwise.
	 */
	public static boolean isHidden(Polygon poly) {
		// TODO fill this in.
		Vector3D v1 = poly.getVertices()[0];
		Vector3D v2 = poly.getVertices()[1];
		Vector3D v3 = poly.getVertices()[2];

		//(v2-v1) x (v3-v2) is facing the viewer
		//use cross product to get which direction the polygon is facing (z value)
		renderer.Vector3D normal = v2.minus(v1).crossProduct(v3.minus(v2)).unitVector();
		if (normal.z > 0) { return true; }
		return false;
	}

	/**
	 * Computes the colour of a polygon on the screen, once the lights, their
	 * angles relative to the polygon's face, and the reflectance of the polygon
	 * have been accounted for.
	 *
	 * @param lightDirection The Vector3D pointing to the directional light read in from
	 *                       the file.
	 * @param lightColor     The color of that directional light.
	 * @param ambientLight   The ambient light in the scene, i.e. light that doesn't depend
	 *                       on the direction.
	 */
	public static Color getShading(Polygon poly, Vector3D lightDirection, Color lightColor, Color ambientLight) {
		// TODO fill this in.
		//Note: lightDirection is the positional light stated in handout
		Vector3D v1 = poly.getVertices()[0];
		Vector3D v2 = poly.getVertices()[1];
		Vector3D v3 = poly.getVertices()[2];

		//(v2-v1) x (v3-v2) is facing the viewer
		//use cross product to get which direction the polygon is facing
		renderer.Vector3D normal = v2.minus(v1).crossProduct(v3.minus(v2)).unitVector();

		//incident light direction D
		renderer.Vector3D lightDirectionD = lightDirection.unitVector();

		//Reflectance
		//Color reflectance = poly.getReflectance(); //gets the color values

		//calculating cosine which is the dot product of the normal, light direction and divided by both its magnitude
		double cos = normal.cosTheta(lightDirectionD);

		double ambientR = ambientLight.getRed() / 255.0;
		double ambientG = ambientLight.getGreen() / 255.0;
		double ambientB = ambientLight.getBlue() / 255.0;

		double lightColorR = lightColor.getRed() / 255.0;
		double lightColorG = lightColor.getGreen() / 255.0;
		double lightColorB = lightColor.getBlue() / 255.0;

		double reflectanceR = poly.getReflectance().getRed();
		double reflectanceG = poly.getReflectance().getGreen();
		double reflectanceB = poly.getReflectance().getBlue();
		System.out.println("lewfwehgwo Values: " + reflectanceR + ", " + reflectanceG + ", " + reflectanceB);

		System.out.println("ambient Values: " + ambientR + ", " + ambientG + ", " + ambientB);

		if (cos <= 0) {
			double shadeR = Math.min(ambientR * reflectanceR, 255);
			double shadeG = Math.min(ambientG * reflectanceG, 255);
			double shadeB = Math.min(ambientG * reflectanceB, 255);
			return new Color((int) shadeR, (int) shadeG, (int) shadeB);
		}

		double shadeR = Math.min(ambientR * reflectanceR + (lightColorR * reflectanceR * cos), 255);
		double shadeG = Math.min(ambientG * reflectanceG + (lightColorG * reflectanceG * cos), 255);
		double shadeB = Math.min(ambientB * reflectanceB + (lightColorB * reflectanceB * cos), 255);

		System.out.println("Shading Values: " + shadeB + ", " + shadeG + ", " + shadeR);

		return new Color((int) shadeR, (int) shadeG, (int) shadeB);

	}

	/**
	 * This method should rotate the polygons and light such that the viewer is
	 * looking down the Z-axis. The idea is that it returns an entirely new
	 * Scene object, filled with new Polygons, that have been rotated.
	 *
	 * @param scene The original Scene.
	 * @param xRot  An angle describing the viewer's rotation in the YZ-plane (i.e
	 *              around the X-axis).
	 * @param yRot  An angle describing the viewer's rotation in the XZ-plane (i.e
	 *              around the Y-axis).
	 * @return A new Scene where all the polygons and the light source have been
	 * rotated accordingly.
	 */
	public static Scene rotateScene(Scene scene, float xRot, float yRot) {
		// TODO fill this in.
		//get polygons and lights from the scene class and apply it the List and Vector type
		ArrayList<Polygon> rotatedPolygons = new ArrayList<>(scene.getPolygons());
		Transform matrix = Transform.identity();

		//create a rotation transformation for angle around x and y axis
		Transform rotateX = Transform.newXRotation(xRot);
		Transform rotateY = Transform.newYRotation(yRot);

		//iterating through and updating vertices of every polygon
		for (Scene.Polygon p : rotatedPolygons) {
			//for every vertex in that polygon
			for (int i = 0; i < p.getVertices().length; i++) {

				//if (float number) xRotation is not zero
				//apply and update the rotation matrix at index vertex x & y
				if (xRot != 0.0f) {
					p.getVertices()[i] = rotateX.multiply(p.getVertices()[i]);
				}
				if (yRot != 0.0f) {
					p.getVertices()[i] = rotateY.multiply(p.getVertices()[i]);
				}
			}
		}
		//update the lights as well
		Vector3D processedLightPos = matrix.multiply(scene.getLight());
		return new Scene(rotatedPolygons, processedLightPos);
	}

	/**
	 * This should translate the scene by the appropriate amount.
	 *
	 * @param scene
	 * @return
	 */
	public static Scene translateScene(Scene scene) {
		// TODO fill this in.
		//identity(): The identity transform is a data transformation that copies the
		//source data into the destination data without change.
		//renderer.Vector3D[] vector = scene.getVertices();

		//List<Polygon> newPolygons = new ArrayList<>();
		//Transform IDTrans = Transform.identity();
		//Vector3D newLights = IDTrans.multiply(scene.getLight());
		Rectangle bBox = boundingBox(scene.getPolygons());

		float xDifference = -bBox.x;
		float yDifference = -bBox.y;

		Transform t = Transform.newTranslation(new Vector3D(xDifference, yDifference, 0));

		for (Scene.Polygon p : scene.getPolygons()) {
			for (int i=0; i<p.getVertices().length; i++) {
				p.getVertices()[i] = t.multiply(p.getVertices()[i]);
			}
		}

		//returns with updated values from transformation
		return new Scene(scene.getPolygons(), scene.getLight());
	}

	/**
	 * This should scale the scene.
	 *
	 * @param scene
	 * @return
	 */
	public static Scene scaleScene(Scene scene) {
		// TODO fill this in.

		Rectangle bBox = boundingBox(scene.getPolygons());

		float maxWidth =  bBox.width;
		float maxHeight = bBox.height;
		float maxDifference = Float.max(maxHeight, maxWidth);
		float maxScale = 600/maxDifference;

		//float scaling = 2;

		// determines whether or not the longest length of the shape is the width or height
		// boolean w = (maxWidth - GUI.CANVAS_WIDTH > maxHeight - GUI.CANVAS_HEIGHT);
		//if (maxWidth > GUI.CANVAS_WIDTH && w) { scaling = GUI.CANVAS_WIDTH / maxWidth; }
		//if (maxHeight > GUI.CANVAS_HEIGHT && !w) { scaling = GUI.CANVAS_HEIGHT / maxHeight; }

		//Transform matrix = Transform.identity();
		Transform scale = Transform.newScale(maxScale,maxScale,maxScale);

		// process the polygons
		for (Scene.Polygon p : scene.getPolygons()) {
			for (int i=0; i<p.getVertices().length; i++) {
				p.getVertices()[i] = scale.multiply(p.getVertices()[i]);
			}
		}
		//process and update the light as well
		Vector3D processedLightPos = scale.multiply(scene.getLight());

		return new Scene(scene.getPolygons(), processedLightPos);
	}

	/**
	 * Computes the edgelist of a single provided polygon, as per the lecture
	 * slides.
	 */
	public static EdgeList computeEdgeList(Polygon poly) {
		// TODO fill this in.
		//vertex data already set up as anticlockwise
		Vector3D[] vectors = poly.getVertices();
		Vector3D v1 = vectors[0];
		Vector3D v2 = vectors[1];
		Vector3D v3 = vectors[2];

		//calculate the startY and endY for the edge, rounding them up to ints
		int startY = (int) Math.floor(Math.min(Math.min(v1.y, v2.y), v3.y));
		int endY = (int) Math.ceil(Math.max(Math.max(v1.y, v2.y), v3.y));

		startY = Math.round(startY);
		endY = Math.round(endY);

		//store the x values into this list
		EdgeList edgeList = new EdgeList(startY, endY);
		//System.out.println("created new edgeList");

		//iterating through each edge in the polygon
		//scan 3 edges of polygon (anti-clockwise) to get all the x points along the edge
		for (int i = 0; i < poly.getVertices().length; i++) {
			//start and end node
			Vector3D vectorA = vectors[i];
			Vector3D vectorB = vectors[(i + 1) % 3];
			float v2x = Math.round(vectorB.x);
			float v1x = Math.round(vectorA.x);
			float v2y = Math.round(vectorB.y);
			float v1y = Math.round(vectorA.y);
			float v2z = Math.round(vectorB.z);
			float v1z = Math.round(vectorA.z);
			//also find slopeZ because the Z value would be going outwards the screen and its in 3D space
			//float slopeX = (vectorB.x - vectorA.x)/(vectorB.y - vectorA.y);
			//float slopeZ = (vectorB.z - vectorA.z)/(vectorB.y - vectorA.y);

			float slopeX = (v2x - v1x) / (v2y - v1y);
			float slopeZ = (v2z - v1z) / (v2y - v1y);

			//initialize like in lecture slides
			//float x = vectorA.x;
			float x = Math.round(vectorA.x);
			int y = Math.round(vectorA.y);
			//int y = vectorA.y;
			//float z = vectorA.z;
			float z = Math.round(vectorA.z);
			int maxY = Math.round(vectorB.y);
			//System.out.println("x: " + x + "z:" + z + "y: " + y + "maxY:" + maxY);

			//General case: for each y value in the line we are trying to update the corresponding x value
			if (vectorA.y < vectorB.y) {
				while (y <= Math.round(vectorB.y)) { //if scanning down (visiting the left hand side), update xMIN(y)
					//xMIN(y) = x, x = x + slope, y++
					//calculate and update xMIN by finding the distance between the start and end of the edge
					//and store them both into the edgeList
					//edgeList.edge[0][y-startY] = x;
					//edgeList.edge[1][y-startY] = z;
					edgeList.addRowLeft(y, x, z);
					x = x + slopeX;
					z = z + slopeZ;
					y++;
					//System.out.println("are you updating xMIN");
					//System.out.println(edgeList);
				}
			} else {
				while (y >= Math.round(vectorB.y)) { //if scanning up(visiting the right hand side), update xMAX(y)
					//xMAX(y) = x, x = x - slope, y--
					//calculate and update xMAX by finding the distance between the start and end of the edge
					//and store them both into the edgeList
					edgeList.addRowRight(y, x, z);
					//edgeList.edge[2][y-startY] = x;
					//edgeList.edge[3][y-startY] = z;
					x = x - slopeX;
					z = z - slopeZ;
					y--;
					//System.out.println("are you updating xMAX");
					//System.out.println(edgeList);
				}
			}
		}
		return edgeList;
	}

	/**
	 * Fills a zbuffer with the contents of a single edge list according to the
	 * lecture slides.
	 * <p>
	 * The idea here is to make zbuffer and zdepth arrays in your main loop, and
	 * pass them into the method to be modified.
	 *
	 * @param zbuffer      A double array of colours representing the Color at each pixel
	 *                     so far.
	 * @param zdepth       A double array of floats storing the z-value of each pixel
	 *                     that has been coloured in so far.
	 * @param polyEdgeList The edgelist of the polygon to add into the zbuffer.
	 * @param polyColor    The colour of the polygon to add into the zbuffer.
	 */
	public static void computeZBuffer(Color[][] zbuffer, float[][] zdepth, EdgeList polyEdgeList, Color polyColor) {
		// TODO fill this in.
		//two 2D arrays are already set up as parameters

		int startY = polyEdgeList.getStartY();
		int maxY = polyEdgeList.getEndY();

		for (int y = startY; y < maxY; y++) {
			//calculate the x and z EdgeList(EL) of this polygon;
			float xLeft = polyEdgeList.getLeftX(y);
			float xRight = polyEdgeList.getRightX(y);
			float zLeft = polyEdgeList.getLeftZ(y);
			float zRight = polyEdgeList.getRightZ(y);
			float slope = (zRight - zLeft) / (xRight - xLeft);
			int x = Math.round(polyEdgeList.getLeftX(y));
			float z = zLeft + slope * (x - xLeft);
			//float z = polyEdgeList.getLeftZ(y);
			while (x <= Math.round(xRight) - 1) {
				//checking for boundaries
				if (y >= 0 && x >= 0 && y < GUI.CANVAS_HEIGHT && x < GUI.CANVAS_WIDTH && z < zdepth[x][y]) {
					zbuffer[x][y] = polyColor;
					zdepth[x][y] = z;
				}
				z = z + slope;
				x++;
			}
		}
	}


	//gets the bounding box of the objects and is displayed onto the screen
	public static Rectangle boundingBox(List<Scene.Polygon> polygons) {

		float minY = Float.MAX_VALUE;
		float maxY = -Float.MAX_VALUE;
		float minX = Float.MAX_VALUE;
		float maxX = -Float.MAX_VALUE;

		for (Scene.Polygon poly : polygons) {
			//it copies a specific array and can create a new length with the current array
			Vector3D[] vectors = Arrays.copyOf(poly.getVertices(), 3);

			//iterating through every vector and rounding up all the min and max
			for (Vector3D v : vectors) {
				minY = Math.min(minY, v.y);
				maxY = Math.max(maxY, v.y);
				minX = Math.min(minX, v.x);
				maxX = Math.max(maxX, v.x);
			}
		}
		//and return the bounding box and also rounded
		return new Rectangle(
				Math.round(minX),
				Math.round(minY),
				Math.round(maxX - minX),
				Math.round(maxY - minY));
	}
}

// code for comp261 assignments
