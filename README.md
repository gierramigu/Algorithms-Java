3D Renderer
Report

My code can essentially do the complete pipeline and render the 3D objects very smoothly. There’s still a few holes that are appearing on the object and I couldn’t figure it out however I got most of the tears gone and I solved that through rounding more variables and calculations in my edgeList and zbuffer methods. With this assignment I came across a lot of issues with my edgeList method, since I was relying with the test classes that Yi provided for us, I kept getting indexOutOfBounds errors. It took me a while to figure it out so I started debugging and added print statements everywhere and eventually found out that the roundings for my startY and endY  in my edgeList were completely off because I overcomplicated the process. The code in general is able to read the text file given to us, process that and store them into the scene class. By calculating the depth of the polygon through the zbuffer, its how the objects get that 3D look, the smaller z value coming closer to the screen. I also calculated the normal by using the transform class as they have coded the calculations. I was able to get the two vertices and use the cross product to get the direction the polygon is facing. My light source is not rotating with the scene and just stays in one position, I can’t really figure this one out either. Essentially it would be a similar approach to the polygons but yeah. 





