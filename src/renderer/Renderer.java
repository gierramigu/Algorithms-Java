package renderer;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.List;
import java.util.ArrayList;


public class Renderer extends renderer.GUI {
	private List<Scene.Polygon> polygons = new ArrayList<>(); //to store the vertex data points here
	private Scene scene;
	private Vector3D lightValues;
	float vertices[];
	//private float currentScale = 1.0f;
	private boolean initialScale = false;

	@Override
	protected void onLoad(File file) {
		// TODO fill this in.
		//currentScale =1.0f;
		try {
			BufferedReader br = new BufferedReader(new FileReader(file));
			String[] tokens;
			int count = 0;
			//reads the first line which is the poly count
			int totalNumOfPolygons = Integer.parseInt(br.readLine());
			System.out.println(totalNumOfPolygons);
			String line = null;

			//array to store the vertex data
			vertices = new float[9];
			int [] color = new int[3];

			//if count is not equal to the number of polygons
			while(count != totalNumOfPolygons && (line = br.readLine()) != null){
				tokens = line.split(",");
				color[0] = Integer.parseInt(tokens[0]);
				color[1] = Integer.parseInt(tokens[1]);
				color[2] = Integer.parseInt(tokens[2]);
				vertices [0] = Float.parseFloat(tokens[3]);
				vertices [1] = Float.parseFloat(tokens[4]);
				vertices [2] = Float.parseFloat(tokens[5]);
				vertices [3] = Float.parseFloat(tokens[6]);
				vertices [4] = Float.parseFloat(tokens[7]);
				vertices [5] = Float.parseFloat(tokens[8]);
				vertices [6] = Float.parseFloat(tokens[9]);
				vertices [7] = Float.parseFloat(tokens[10]);
				vertices [8] = Float.parseFloat(tokens[11]);

				//creating new objects
				Color RGB = new Color(color[0],color[1],color[2]);
				Vector3D firstVertex = new Vector3D(vertices[0], vertices[1], vertices[2]);
				Vector3D secondVertex = new Vector3D(vertices[3], vertices[4], vertices[5]);
				Vector3D thirdVertex = new Vector3D(vertices[6], vertices[7], vertices[8]);
				Scene.Polygon polygon = new Scene.Polygon(firstVertex, secondVertex, thirdVertex, RGB);
				System.out.println(polygon.toString()); //test
				polygons.add(polygon); //adding the data into a set
				count++;
				System.out.println("READING");
			}

			//read last line; split and parse the txt data
			String readLightVal = br.readLine();
			String [] readLight = readLightVal.split(",");
			float lightVec1 = Float.parseFloat(readLight[0]);
			float lightVec2 = Float.parseFloat(readLight[1]);
			float lightVec3 = Float.parseFloat (readLight[2]);
			lightValues = new Vector3D(lightVec1,lightVec2,lightVec3);
			//directLightSources.add(new Color(100,100,100));
			//System.out.println(lightValues);

			//storing all the values that has been parsed into a new scene object
			scene = new Scene (polygons, lightValues);
			scene = Pipeline.scaleScene(scene);


			br.close();
		}catch(IOException e){
			throw new RuntimeException("file reading failed.");
		}
	}

	@Override
	protected void onKeyPress(KeyEvent ev) {
		// TODO fill this in.
		//rotation
		if(ev.getKeyCode() == KeyEvent.VK_LEFT ){
			scene = Pipeline.rotateScene(scene, 0,(float) (-0.1*Math.PI));

		}else if(ev.getKeyCode() == KeyEvent.VK_RIGHT ){
			scene = Pipeline.rotateScene(scene, 0,(float) (0.1*Math.PI));

		}else if(ev.getKeyCode() == KeyEvent.VK_UP){
			scene = Pipeline.rotateScene(scene, (float) (0.1*Math.PI), 0);

		}else if(ev.getKeyCode() == KeyEvent.VK_DOWN){
			scene = Pipeline.rotateScene(scene, (float) (-0.1*Math.PI), 0);
		}
	}


	@Override
	protected BufferedImage render() {
		// TODO fill this in.
		if(scene == null){ return null;}
		Color[][] zbuffer = new Color[CANVAS_WIDTH][CANVAS_HEIGHT];
		float[][] zdepth = new float [CANVAS_WIDTH][CANVAS_HEIGHT];

		Color backgroundColor = new Color(200,200,200);

		for (int x = 0; x < zbuffer.length; x++) {
			for (int y = 0; y < zbuffer[x].length; y++) {
				zbuffer[x][y] = backgroundColor;
			}
		}

		for (int i = 0; i < zdepth.length; i++) {
			for (int j = 0; j < zdepth[i].length; j++) {
				zdepth[i][j] = Float.POSITIVE_INFINITY;
			}
		}
//
//		if(initialScale == true) {
//			scene = Pipeline.scaleScene(scene);
//			scene = Pipeline.translateScene(scene);
//			initialScale = false;
//		}
//			scene = Pipeline.scaleScene(scene);
			scene = Pipeline.translateScene(scene);

		EdgeList edgeList;
		for(Scene.Polygon p : scene.getPolygons()){
			Vector3D lights = scene.getLight();
			Color lightColor = p.getReflectance(); //getting the light colour
			if(!Pipeline.isHidden(p)){ //if the polygon should be rendered
				Color shading = Pipeline.getShading(p, lights, lightColor, new Color(getAmbientLight()[0],
						getAmbientLight()[1], getAmbientLight()[2])); //display color of the polygon
				edgeList = Pipeline.computeEdgeList(p);
				Pipeline.computeZBuffer(zbuffer, zdepth, edgeList, shading); //zBuffer applied here
			}
		}
        return convertBitmapToImage(zbuffer);
	}

	/**
	 * Converts a 2D array of Colors to a BufferedImage. Assumes that bitmap is
	 * indexed by column then row and has imageHeight rows and imageWidth
	 * columns. Note that image.setRGB requires x (col) and y (row) are given in
	 * that order.
	 */
	private BufferedImage convertBitmapToImage(Color[][] bitmap) {
		BufferedImage image = new BufferedImage(CANVAS_WIDTH, CANVAS_HEIGHT, BufferedImage.TYPE_INT_RGB);
		for (int x = 0; x < CANVAS_WIDTH; x++) {
			for (int y = 0; y < CANVAS_HEIGHT; y++) {
				image.setRGB(x, y, bitmap[x][y].getRGB());
			}
		}
		return image;
	}

	public static void main(String[] args) {
		new Renderer();
	}
}

// code for comp261 assignments
