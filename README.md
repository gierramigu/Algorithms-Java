3D Renderer
Report

In my main class it has an onload method which has 3 separate methods that reads through each nodes, segments and roads. I used buffered reader to scan through the files and stored them into various collections like (Map, List etc.).  I also parsed the integers and doubles since they were all stored inside a string file so I did some conversions inside my reading methods. This is in summary how I read my small and large data files. 
I have separate classes for nodes, segments and roads and created constructors and called on them with getters and setters. Through these classes I can create a new object when I read the files and I stored each road, segment and node with its data into specific collections and redrew them using the ReDraw method iterating through the road and node maps in order for them to display on the screen. 
It can also zoom in and out and can move the map into 4 directions in my move method, through this I calculated the origin and scale and did various if statements to compare the location coordinates and to find the most top and most bottom coordinates. I also added the switch statement which I researched and is like if statements however the switch statement can have several possible execution paths. It also works with other primitive data types. I applied the calculations with my origin and scale into this method and since the main method extends to the GUI, I’m able to use the buttons. 
My onClick method allows the user to click on a particular node/intersection on the Map and highlights that node to a different colour. I calculated the closest distance from the location point and the mouse point and iterated through my Node Map collection to get the nodeID location and compare it with the mouse x and y and find the distance in between of the points.
My onSearch method allows the user to use the Search box located on the top right of the graphics pane to search for a particular street/highway and when pressed ENTER the program highlights the specific road/segment through the string input of the user which also displays the string on the pane on the bottom left corner to let the user know what the intersection or road when clicked or searched.
Along the way I encountered a lot of errors and did a lot of testing. I added print statements in my methods to see if works. If it prints meaning that it’s working. Through the statements I was able to determine the root of the problem (eventually).





