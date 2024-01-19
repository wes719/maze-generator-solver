
//=======================================================================================================
//Program Name: MazeAssignment
//Author: Wesley Liu & Jeremy Su
//Date: 2023/04/03
//Java (Neon.1a Release (4.6.1))
//=======================================================================================================
//Problem Definition - Create a program that can generate, read, and find the shortest path (solve) mazes of varying dimensions. The program should be adapted in the form of 
//a GUI 
//Input: Number of rows/columns, checkboxes for Guarantee Valid Maze, Flood Fill Generation, and Fully Connected, name of a file, buttons to generate maze, upload file, save to file, 
//and find the shortest path. The user is also able to click on an empty path to automatically change the starting location and recalculate the shortest path.
//Output: A functioning user interface including a space for user input (as described above) and a space for the interactive maze to be generated/updated
//Process: Use java swing for the GUI, use a variety of different methods, use a 2d boolean array to represent the maze, use various algorithms such as findShortestPath, etc. 
//=======================================================================================================
/*LIST OF IDENTIFIERS
 * Let invalidCounter act as an accumulator used to count invalid random generations for the floodfill generation.
 * This is used to help the program break out of potential infinite loops - Type: Global int
 * Let rows represent the number of rows in the maze - Type: Global int
 * Let columns represent the number of columns in the maze - Type: Global int
 * let bestPathSave be a two dimensional array that saves the coordinates of the shortest path - Type: Global integer ArrayList and Array
 * Let shortest represent the smallest amount of tiles needed to travel to the exit from the start - Type: Global integer
 * Let barrier represent the symbol for a barrier as to be displayed on the GUI - Type: Global String
 * Let open represent the symbol for an open path as to be displayed on the GUI - Type: Global String
 * Let start represent the symbol for the starting tile as to be displayed on the GUI - Type: Global String
 * Let exitSymbol represent the symbol for the exit tile as to be displayed on the GUI - Type: Global String
 * Let exit represent the coordinates for the exit tile - Type: Global integer array
 * Let startPosition represent the coordinates for the starting position - Type: Global integer Array
 * Let valid represent a boolean value used to ensure the proper generation of a maze - Type: Global boolean
 * Let buttonMaze represent a 2D array of JButton objects used to visually present the maze to the user - Type: Global JButton 2D Array
 * Let map represent a JPanel used to display the map onto the GUI - Type: JPanel
 * Let userInterface represent a JPanel used to display the user interface of the GUI, or the upper half - Type: JPanel
 * Let u1 represent a JPanel used within the userInterface JPanel to help with organization. This is one of the 6 divisions of the userInterface - Type: JPanel
 * Let u2 represent a JPanel used within the userInterface JPanel to help with organization. This is one of the 6 divisions of the userInterface - Type: JPanel
 * Let u3 represent a JPanel used within the userInterface JPanel to help with organization. This is one of the 6 divisions of the userInterface - Type: JPanel
 * Let u4 represent a JPanel used within the userInterface JPanel to help with organization. This is one of the 6 divisions of the userInterface - Type: JPanel
 * Let u5 represent a JPanel used within the userInterface JPanel to help with organization. This is one of the 6 divisions of the userInterface - Type: JPanel
 * Let u6 represent a JPanel used within the userInterface JPanel to help with organization. This is one of the 6 divisions of the userInterface - Type: JPanel
 * Let welcomeMessage represent a JLabel used to welcome the user to the program - Type: JLabel
 * Let rowPrompt represent a JLabel used to prompt the user to enter the number of rows - Type: JLabel
 * Let rowField represent a JTextFIeld used to accept the user's desired number of rows - TypeL JTextField
 * Let columnPrompt represent a JLabel used to prompt the user to enter the number of columns - Type: Jlabel
 * Let columnField represent a JTextField used to accept the user's desired number of columns - Type: JTextField
 * Let generateMaze represent a JButton used to generate a random maze - Type: JButton
 * Let generateValid represent a JCheckBox used to allow the user to guarantee that the generated maze will be valid (valid path from start to end) - Type: JCheckBox
 * Let generatePour represent a JCheckBox used to allow the user to choose a flood fill generation algorithm for the maze - Type: JCheckBox
 * Let fullyConnected represnt a JCheckBox used to allow the user to choose if they want the maze to be fully continuous - Type: JCheckBox
 * Let filePrompt represent a JLabel used to prompt the user to enter a file name  - Type: JLabel
 * Let importMaze represent a JTextField used to allow the user to enter the name of a file which will be imported or written to - Type: JTextField
 * Let fetchMaze represent a JButton used to upload a maze from an existing file - Type: JButton
 * Let line represent a JLabel used to display a horizontal line for aesthetic purposes. This will also display a special message - Type: JLabel
 * Let findShortest represent a JButton used to find and display the shortest solution - Type: JButton
 * Let saveToFile represent a JButton used to save the displayed maze to a certian file - Type: JButton
 * Let result represent a JLabel used to display the result of the maze solving algorithm. This will also be used to display various messages to the user
 * such as when something is not working - Type: JLabel
 * Let options represent a JPanel used to group all the checkboxes together - Type: JPanel
 */

import java.io.*; //required imports
import java.util.*;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class MazeAssignment extends JFrame implements ActionListener { //start of class

	static int invalidCounter;  //declaration of class variables outlined in header block
	static int rows;
	static int columns;
	static ArrayList<int[]>bestPathSave = new ArrayList<int[]>();
	static int shortest = Integer.MAX_VALUE;
	static String barrier = "B";
	static String open = "O";
	static String start ="S";
	static String exitSymbol = "X";

	static int[] exit = new int[2];
	static int[] startPosition = new int[2];
	static boolean valid = false;
	static boolean[][] maze;
	static JButton buttonMaze [][];

	JPanel map = new JPanel();
	JPanel userInterface = new JPanel();


	JPanel u1 = new JPanel ();
	JPanel u2 = new JPanel ();
	JPanel u3 = new JPanel ();
	JPanel u4 = new JPanel ();
	JPanel u5 = new JPanel ();
	JPanel u6 = new JPanel ();

	JLabel welcomeMessage = new JLabel ("Welcome to the Maze Solver! (V 3.0)");
	JLabel rowPrompt = new JLabel ("Enter rows:");
	JTextField rowField = new JTextField (5);
	JLabel columnPrompt = new JLabel("Enter columns:");
	JTextField columnField = new JTextField (5);
	JButton generateMaze = new JButton ("Generate Maze");
	JCheckBox generateValid = new JCheckBox ("Guarantee Valid Maze");
	JCheckBox generatePour = new JCheckBox("FloodFill Generation");
	JCheckBox fullyConnected = new JCheckBox("Fully Connected");

	JLabel filePrompt = new JLabel("Enter file name");
	JTextField importMaze = new JTextField (10);
	JButton fetchMaze = new JButton ("Upload");
	JLabel line = new JLabel ("________________________________________________________________Click the path to relocate the starting position! (Expiremental) _______________________________________________________________");

	JButton findShortest = new JButton ("Find Shortest Path");
	JButton saveToFile = new JButton ("Save to File");
	JLabel result = new JLabel ();
	JPanel options = new JPanel();

	/**ShortestPath (construtor) method:
	 * 
	 * This method is essentially used to construct the GUI.
	 * It includes many details such as specific layouts, adds various
	 * actionListeners, initializes the starting behavours of various elements,
	 * sets up tooltips, adds elements to their proper panels, adds the panels to the GUI
	 * and finally sets the GUI visible.
	 *
	 * LOCAL VARIABLES
	 * layout 1: governs the main layout of the GUI - Type: GridLayout
	 * layout 3: governs the layout of the upper half/user interface - Type: GridLayout
	 * 
	 * @param n/a
	 * @return n/a
	 * @throws IOException
	 */

	public MazeAssignment() throws IOException { //start of constructor method
		setTitle("Maze"); //sets up the GUI with title Maze and gives it a starting size. Resizing is allowed for user friendliness
		setSize(800, 1000);
		setResizable(true);

		GridLayout layout1 = new GridLayout(2,0); //main layout
		GridLayout layout3 = new GridLayout (6,0); //UI layout

		ToolTipManager.sharedInstance().setInitialDelay(100); //sets an initial delay for tooltips used to prompt ther user upon hovering
		rowField.setToolTipText("larger mazes up to 100x100 are recommended");  //adds various user-friendly tooltips to help the user naviagte the program
		columnField.setToolTipText("larger mazes up to 100x100 are recommended");
		fullyConnected.setToolTipText("always a valid maze***");
		importMaze.setToolTipText("make sure your maze file is in the same folder as the program");
		saveToFile.setToolTipText("Save a generated maze into your file");


		rowField.addActionListener(this); //add actionlisteners to various elements that respond to events
		columnField.addActionListener(this);
		generateMaze.addActionListener(this);
		importMaze.addActionListener(this);
		fetchMaze.addActionListener(this);
		findShortest.addActionListener(this);
		saveToFile.addActionListener(this);
		generateValid.addActionListener(new ActionListener() { //adds a new actionListener to generateValid
			@Override
			public void actionPerformed(ActionEvent e) { //start of a unique actionPerformed method

				if(!generateValid.isSelected()) { //ensures that the fully connected checkbox will always be off if the user unchecks the valid maze guarantee box
					fullyConnected.setSelected(false);
				}
			}
		});

		generateValid.setSelected(true); //by default, set valid maze guarantee box to checked
		generatePour.addActionListener(this); //add an actionlistener to generatePour
		generatePour.setSelected(false); //set it to unchecked by default
		fullyConnected.addActionListener(this); //add an actionListener to fullyConnected
		fullyConnected.setSelected(true); //set it to checked by default

		setLayout(layout1); //set the main layout to layout1

		userInterface.setLayout(layout3); //set userInterface to layout3

		options.add(generateValid); //add all three checkboxes to the options JPanel
		options.add(generatePour);
		options.add(fullyConnected);
		options.setLayout(layout1); //set options to layout1

		u1.add(welcomeMessage);  //add various elements to various panels
		u2.add(rowPrompt);
		u2.add(rowField);
		u2.add(columnPrompt);
		u2.add(columnField);
		u2.add(options);
		u2.add(generateMaze);
		u3.add(filePrompt);
		u3.add(importMaze);
		u3.add(fetchMaze);
		u3.add(saveToFile);
		u4.add(line);
		u5.add(findShortest);
		u6.add(result);

		userInterface.add(u1); //add the u1-u6 JPanels to the userInterface JPanel
		userInterface.add(u2);
		userInterface.add(u3);
		userInterface.add(u4);
		userInterface.add(u5);
		userInterface.add(u6);

		add(userInterface);//add the userInterface JPanel to the GUI

		setVisible(true); //makes sure the GUI is visible
	}

	/**actionPerformed method:
	 * 
	 * This method is used to handle events, like the press of a button. This
	 * governs what happens when the user interacts with the GUI
	 *
	 * LOCAL VARIABLES
	 * command - used to store the text of a toggled JButton - Type: String
	 * rowsInput - used to store the string value of the user's inputted amount of rows - Type: String
	 * columnsInput - used to store the string value of the user's inputted amount of columns - Type: String
	 * fileName - used to store the string value of the user's inputted file name - Type: String
	 * 
	 * @param ActionEvent event
	 * @return void
	 * @throws n/a
	 */

	public void actionPerformed(ActionEvent event){ //start of method
		map.removeAll(); //clears the map JPanel whenever there is a main event. This is essential in making sure that fresh maps are added instead of stacked

		String command = event.getActionCommand(); //initializes command and stores the text of a toggled JButton 

		if (command.equals("Generate Maze")) { //checks to see if the user selected the button that says Generate Maze
			resetToDefaultLegend(); //calls the resetToDefaultLegend method to ensure that the default labels for the maze components are being used
			String rowsInput = rowField.getText(); //declares a string named rowsInput, and sets it equal to what the user put in the rowField text field
			String columnsInput = columnField.getText(); //declares a string named columnsInput, and sets it equal to what the user put in the columnField text field

			if (rowsInput.equals("2") || rowsInput.equals("1") || columnsInput.equals("2") || columnsInput.equals("1")) { //makes sure that neiher rows or columns is 1 or 2. These values
				//will be too small to generate a workable maze
				result.setText("Please enter dimensions of minimum 3"); //set the result label to a UFP
			}
			else {
				try { //if the rows/columns are proper, start a try catch block
					rows = Integer.parseInt(rowsInput); //parse rowsInput into an integer value and set it equal to the global variable rows
					columns = Integer.parseInt(columnsInput);//parse columnsInput into an integer value and set it equal to the global variable columns
					GridLayout layout2 = new GridLayout(rows, columns); //create a new gridlayout named layout2 with rows and columns equivalent to the rows and columns of the maze
					map.setLayout(layout2); //set the map JPanel to layout2



					if (generateValid.isSelected()){ //checks if generateValid checkbox is toggled
						maze = setUpMaze(generatePour.isSelected(), fullyConnected.isSelected()); //set up the maze accordingly, using the setUpMaze method
						result.setText(rows+" by "+columns+" maze has been generated"); //set the result label to a UFP
					}else{//checks if the generatePour checkbox is toggled
						maze = setUpChaoticMaze(generatePour.isSelected()); //sets up the maze accordingly, using the setUpChaoticMaze method
						result.setText(rows+" by "+columns+" maze has been generated (Valid Maze Guarantee is OFF)");  //set the result label to a UFP
					}

					buttonMaze = new JButton[rows][columns]; //essentially sets buttonMaze to the correct number of rows and columns

					convertToButton(maze,buttonMaze); //calls the convertToButton method

					for(int x = 0; x < maze.length; x++){ //runs throgh the maze array
						for(int y = 0; y < maze[x].length; y++){
							map.add(buttonMaze[x][y]); //adds the corresponding JButton, from the buttonMaze array, into the map JPanel

						}
					}
				}catch(Exception e) {
					result.setText("Please enter appropriate, numeric values"); //set the result label to a UFP. This will be displayed upon bad data/errors
				}
			}
		}

		if (command.equals("Upload")) { //checks to see if the user selected the button that says Upload

			String fileName = importMaze.getText(); //declares a string called fileName, and sets it equal to the inputted text in the importMaze text field

			try {//starts a try catch block for error handling
				maze=readFile(fileName); //use the readFile method to set up the maze
				result.setText(fileName+" has been successfully uploaded");  //set the result label to a UFP

				GridLayout layout2 = new GridLayout(rows, columns);//create a new gridlayout named layout2 with rows and columns equivalent to the rows and columns of the maze
				map.setLayout(layout2);//set the map JPanel to layout2
				buttonMaze  = new JButton[rows][columns]; //essentially sets buttonMaze to the correct number of rows and columns

				convertToButton(maze,buttonMaze);//calls the convertToButton method




				displayMaze(); //calls the displayMaze method
			} catch (IOException e) {

				result.setText("Sorry, that file cannot be found"); //set the result label to a UFP, upon an error
			}
		}

		if(command.equals("Save to File")) {//checks to see if the user selected the button that says Save to File
			String fileName = importMaze.getText();//declares a string called fileName, and sets it equal to the inputted text in the importMaze text field
			try {//start of a try catch block for error handling
				writeFile(fileName);//calls the writeFile method
				result.setText("Successfully Saved to File");//set the result label to a UFP
			} catch (IOException e) {
				result.setText("Please add a file or generate a maze before saving");//set the result label to a UFP, upon an error
			}
		}

		add(map); //add the map to the GUI
		setVisible(true); //ensure that it is visible

		if (command.equals("Find Shortest Path")) searchAndDeploy(); ///checks to see if the user selected the button that says Find ShortestPath. If so, execute the searchAndDeploy method


	}

	/*searchAndDeploy method:
	 * 
	 * This procedural method sets up the findShortestPath method to run and then outputs the result onto the maze.
	 * It creates temporary objects for the method to use: a temporary use pathCoords, a copy of the 
	 * maze so the main one doesn't get overridden. It also results the values of the global shortest
	 * path and clears the previous bestpathsave arraylist. The method also catches any errors caused
	 * by trying to search for the shortest path without a maze.
	 * 
	 * LOCAL VARIABIES
	 * Array<int[]>pathCoords: a 2d arraylist of arrays that stores the coordinates of the shortest path
	 * boolean [][] mazeCopy: a 2d array that is used as a copy of the maze to avoid overriding the original
	 * 
	 * @param n/a
	 * @return void
	 * @throws 
	 * 
	 */
	public void searchAndDeploy() {
		//try catch in case user searches without a maze
		try {

			ArrayList<int[]>pathCoords = new ArrayList<int[]>(); //initializing temporary 2d arraylist
			boolean [][] mazeCopy = maze; //copy of a maze so that the search algorithm does not override the original
			shortest = Integer.MAX_VALUE; //reset the value to the max value of an int so that the first path when searching automatically becomes the shortest
			bestPathSave.clear(); //reset bestpathsave which is used to store the coordinates of the shortest path


			//retrieve the shortestpath from running findShortestPath which returns an int for the length of the shortest path
			int shortestPathResult = findShortestPath(startPosition[0], startPosition[1], exit[0], exit[1], mazeCopy, 0, pathCoords, bestPathSave);


			//if the shortestPathResult is -1, that meant that not a single path was found in the findShortestPath method
			if (shortestPathResult==-1){
				result.setText("Sorry, no path has been found"); //set the text in the gui
			}else{

				revealPath(buttonMaze); //if a path is found, call revealpath which adds the shortest path from start to exit to the button maze (for the gui)

				result.setText("The shortest possible path is "+shortestPathResult+" tiles"); //set the text in the gui
			}

			//reset these values to true (exit and start) on the boolean maze since they get set the false when on click function runs
			maze[exit[0]][exit[1]] = true;
			maze[startPosition[0]][startPosition[1]] = true;

			displayMaze();//displays the button maze onto the gui


		}catch (Exception e) {

			result.setText("Please generate or import a maze before finding shortest path"); //set the text in the gui for error message
		}

	}



	/*convertToButton method:
	 * 
	 * This procedural method converts the 2d boolean maze into a 2d Jbutton array to be displayed. It loops through
	 * the maze and sets a path jbutton if it is true, and a wall jbutton if it is false. After, it adds
	 * on top of the maze the buttons for the exit and start button. Each button is made from a custom
	 * class called JButtonV2 which is an object that has the original jbutton as well as an x value and
	 * a y value to help store its coordinates on the grid. The path buttons are given an actionlistener for
	 * on pressed which will set that current buttons location as the start with the previously saved x and
	 * y values. It then searches and displays the new shortest path from that buttons location.
	 * 
	 * LOCAL VARIABLES
	 * JButtonV2 path: A yellow button that can research the shortest path if pressed made from a custom class that can store a jbutton and its x and y coordinates
	 * JButtonV2 wall: A brown button that cannot be pressed button made from a custom class to represent a wall
	 * JButtonV2 exitButton: A green button that cannot be pressed made from a custom class to represent the exit
	 * JButtonV2 startButton: A grey button that cannot be pressed made from a custom class to represent the start
	 * 
	 * @param boolean[][]maze, JButton [][] buttonMaze
	 * @return void
	 * @throws n/a
	 */
	public void convertToButton(boolean[][]maze, JButton [][] buttonMaze){


		//looks at each index in the maze through a nested loop of the maze
		for (int x = 0; x < maze.length; x++) {
			for (int y = 0; y < maze[x].length; y++) {
				if ((maze[x][y]) == true) {//check if the index is true (representing a possible path)


					JButtonV2 path = new JButtonV2(); //Instantiate a new JButtonv2
					path.button.setBackground(Color.YELLOW); //set the button within the class to yellow
					path.button.setOpaque(true); //allow the colour of the button to fill
					path.button.setText(open); //sets the text that is displayed on the button to the global symbol open

					//saves the coords of the button using the values from the current loop iteration
					path.x = x; //sets the stored x value of the button to the current value of x from the loop
					path.y = y; //sets the stored y value of the button to the current value of y from the loop

					//add an actionlistener for each button so it runs a function on press
					path.button.addActionListener(new ActionListener() {
						@Override
						public void actionPerformed(ActionEvent e) { //on press

							//use the buttons position as the new starting position
							startPosition[1] = path.y; 
							startPosition[0] = path.x;

							searchAndDeploy(); //rerun the shortest path with the new starting point
						}
					});
					buttonMaze[x][y] = path.button; //add the created button to the maze


				}else { //check if the index is false (representing a wall)

					JButton wall = new JButton(); //instantiate a new jbutton
					Color brown = new Color (153,102,51,255); //creating the colour brown with rgbA values
					wall.setText(barrier); //setting the text of the button to the wall
					wall.setBackground(brown); //setting the background of the button colour
					wall.setOpaque(true); //allow the colour of the button to fill


					buttonMaze[x][y] = wall; //adds the button to the maze
				}
			}
		}

		//creating the exit point on the maze
		JButton exitButton = new JButton(exitSymbol); //exit button = newJButton
		exitButton.setBackground(Color.GREEN); //set the colour of the button to green
		exitButton.setOpaque(true);
		buttonMaze[exit[0]][exit[1]]=exitButton; //add the button to the maze

		//creating the startButton
		JButton startButton = new JButton();
		startButton.setText(start);
		startButton.setBackground(Color.LIGHT_GRAY);
		startButton.setOpaque(true);
		buttonMaze[startPosition[0]][startPosition[1]] = startButton;

	}

	/*revealPath method:
	 * 
	 * This procedural method adds buttons to the 2d buttonmaze for the gui using the coordinates of each button on
	 * the path from the bestPathSave. This overides the normal path on the buttonmaze.
	 * 
	 * LOCAL VARIABLES:
	 * JButtonV2 reveal: a jbutton button that uses a custom class, JButtonV2, to store the x and y coordinates of the button. It represents a path from the shortest path to the exit
	 * 
	 * @param n/a
	 * @return void
	 * @throws n/a
	 */
	public void revealPath(JButton [][] buttonMaze) {
		map.removeAll(); //resets the map the buttons were placed on
		convertToButton(maze, buttonMaze); //converts the 2d boolean maze into a 2d Jbutton maze which is stored as a global. 
		for(int x = 0; x < bestPathSave.size(); x++) { //loops through the coordinates of the shortest path

			JButtonV2 reveal = new JButtonV2();//instantiates a jbuttonv2 to represent a button on the path to the shortest path
			reveal.button.setText("+"); //set the text on the button
			Color mrABlue = new Color(0,176,240,255); //creates a blue with rgbA
			reveal.button.setBackground(mrABlue); //uses the blue as the button colour
			reveal.button.setOpaque(true); //allows the colour to show
			buttonMaze[bestPathSave.get(x)[1]][bestPathSave.get(x)[0]] = reveal.button; //adds the button to the 2d button maze

			//saves the cordinates of each button on the path from the bestPathSave arraylist that stored the coordinates of shortestpath
			reveal.x = bestPathSave.get(x)[1]; 
			reveal.y = bestPathSave.get(x)[0];

			//adds an actionlistener so it runs a method on press
			reveal.button.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {

					//use the buttons position as the new starting position
					startPosition[1] = reveal.y;
					startPosition[0] = reveal.x;
					searchAndDeploy();//research the shortest path with the new starting position
				}
			});
		}


		//creating the exit point on the maze
		JButton exitButton = new JButton(exitSymbol); //exit button = newJButton
		exitButton.setBackground(Color.GREEN); //set the colour of the button to green
		exitButton.setOpaque(true);
		buttonMaze[exit[0]][exit[1]]=exitButton; //add the button to the maze

		//creating the startButton
		JButton startButton = new JButton();
		startButton.setText(start);
		startButton.setBackground(Color.LIGHT_GRAY);
		startButton.setOpaque(true);
		buttonMaze[startPosition[0]][startPosition[1]] = startButton;


	}

	/**displayMaze method:
	 * 
	 * This procedural method simply adds JButtons taken from buttonMaze into the map JPanel.
	 * Since the dimesions of buttonMaze and map are equal, this should result in a 
	 * perfect display of JButtons
	 *
	 * LOCAL VARIABLES
	 * n/a
	 * 
	 * @param n/a
	 * @return void
	 * @throws n/a
	 */

	public void displayMaze() {
		for(int x = 0; x < maze.length; x++){ //run through the maze 2d array
			for(int y = 0; y < maze[x].length; y++){
				map.add(buttonMaze[x][y]); //add the respective JButton into the map JPanel

			}
		}
	}

	/**main method:
	 * 
	 * The main method (procedural) in this class does not contribute heavily to the program. It simply declares a new object of the 
	 * ShortestPath class
	 *
	 * LOCAL VARIABLES
	 * sP - an object of the ShortestPath class - type: ShortestPath
	 * 
	 * @param String[] args
	 * @return void
	 * @throws n/a
	 */

	public static void main(String[] args) throws IOException {
		MazeAssignment sP = new MazeAssignment(); //declares a new object of the shortestPath class named sP

	}

	/**readFile method:
	 * 
	 * This functional method reads data from a specificed file. It translates the data into useful information
	 * needed to import and display the maze onto the GUI
	 *
	 * LOCAL VARIABLES
	 * file - an object of the File class, used to access a specified file - Type: File
	 * input - a BufferedReader object used to read from files - Type: BufferedReader
	 * maze - a 2D boolean array that represents the open and closed paths of the maze described - Type: boolean [][]
	 * 
	 * @param String fileName
	 * @return boolean [][]
	 * @throws IOException
	 */

	public static boolean [][] readFile (String fileName) throws IOException{
		File file = new java.io.File(fileName); //creates an object named file, an object of the File class
		BufferedReader input = new BufferedReader(new FileReader(file)); //creates a new object called input (of the BufferedReader class). This will be used to read from a file

		rows = Integer.parseInt(input.readLine()); //read the first line, and assign its value to rows
		columns = Integer.parseInt(input.readLine());//read the second line, and assign its value to columns

		boolean [][]maze = new boolean [rows][columns]; //create a new boolean array with the respective rows and columns
		barrier=input.readLine(); //read the next line and assign its value to barrier
		open=input.readLine();//read the next line and assign its value to open
		start=input.readLine();//read the next line and assign its value to start
		exitSymbol=input.readLine();//read the next line and assign its value to exitSymbol



		for (int x=0;x<maze.length;x++) { //run through the maze 2D array
			for (int y=0;y<maze[x].length;y++) {
				char character=(char)input.read(); //read the next character
				if((Character.toString(character)).equals(barrier)) { //checks to see if that character (converted to a String) matches with barrier
					maze[x][y]=false; //if so, set to false. Barriers are indicated to be false
				}else {
					maze[x][y]=true;//else, it must be true, or an open path

				}
				if((Character.toString(character)).equals(exitSymbol)) { //checks to see if that character (converted to a String) matches with exitSymbol
					exit[0]=x; //update the global exit array
					exit[1]=y;
					maze[x][y] = true; //ensures that the maze at that point is true
				}

				if((Character.toString(character)).equals(start))  {//checks to see if that character (converted to a String) matches with start
					startPosition[0]=x; //update the global startPosition array
					startPosition[1]=y;
					maze[x][y] = true; //ensures that the maze at that point is true
				}
			}
			input.readLine(); //required line for proper reading 

		}
		input.close();//close the BufferedReader
		return maze; //return the maze 2d boolean array

	}

	/**writeFile method:
	 * 
	 * This procedural method takes the existing maze and writes it to a specific file. This is used to save a specific maze
	 *
	 * LOCAL VARIABLES
	 * writer - a BufferedWriter object used to write to files - Type: BufferedWriter
	 * 
	 * @param String fileName
	 * @return void
	 * @throws IOException
	 */
	

	public void writeFile(String fileName) throws IOException{

		BufferedWriter writer = new BufferedWriter(new FileWriter(fileName)); //create a new BufferedReader object named writer
		writer.write(String.valueOf(rows)); //write to the file the number of rows
		writer.newLine();
		writer.write(String.valueOf(columns));//write to the file the number of columns
		writer.newLine();
		writer.write(barrier); //write to the file the value of barrier
		writer.newLine();
		writer.write(open);//write to the file the value of open
		writer.newLine();
		writer.write(start);//write to the file the value of start
		writer.newLine();
		writer.write(exitSymbol);//write to the file the value of exitSymbol
		writer.newLine();
		for(int i = 0; i < columns; i++) { //use 2 for loops to run through the maze dimensions
			for(int j = 0; j < rows; j++) {
				if(maze[i][j] == true) { //checks to see if maze is true
					if (i== startPosition[0] && j==startPosition[1]) { //possibility 1: the open path is the start. Check to see if this is true
						writer.write(start); //write to the file the value for start
					}else if (i== exit[0] && j==exit[1]) { //possibility 2: the open path is the exit. Check to see if this is  true
						writer.write(exitSymbol); //write to the file the value for exitSymbol
					}else {
						writer.write(open); //last possibility is a normal open tile. Write to the file the value for open
					}

				} else {
					writer.write(barrier); //if the maze is not open at this location, it must be a barrier. Write to the file the value for barrier
				}
			}
			writer.newLine(); //start a new line
		}

		writer.close();//close the writer


	}
	
	/*setUpMaze method:
	 * 
	 * the functional method determines what kind of maze generation the user wants. It also
	 * sets up the starting position and exit position on the generated maze to ensure that
	 * the user's specifications are met. This method only covers valid mazes
	 * 
	 * LOCAL VARIABLES
	 * boolean[][] mazeCopy - Creates a copy of the maze to avoid overriding the original
	 * 
	 * @param
	 * boolean generatePour: a boolean whether or not it should generate a maze with a pour like shape or a branch like one
	 * boolean fullyConnect: a boolean whether or not the branched maze should be all connected so that all points are accessible from any other point
	 * @return
	 * returns the updated 2d boolean maze
	 * @throws n/a
	 * 
	 */

	public static boolean[][] setUpMaze(boolean generatePour, boolean fullyConnect) {

		maze = new boolean[rows][columns]; //resets the maze every generation
		generateExitTile(); //calls the generate exit tile to generate a random exit
		maze[exit[0]][exit[1]] = true; // add the exit tile to the path

		if(generatePour) { //if user wants a pourfill maze generation
			generateMainPath(); //generates pourfill maze
			startPosition = generateStartingPosition(); //generates a random start point on one of the valid paths on the maze
			maze[startPosition[0]][startPosition[1]] = true; //sets the start position to true on the maze
		} 
		else if(fullyConnect){ //if user wants to have his branch maze fully connected

			generateEdgePath(); //generates a branch maze with an edge

			// series of if statements to have the exit path extend two paths forward depending on direction so that it always attaches to the maze
			if(exit[0]!= rows-1 && exit[0]!=0) { //if the exit is not on the top or bottom
				if(exit[1]==0) { //if the exit is on the left side
					maze[exit[0]][exit[1]+1] = true; //add a path to the right of the exit
					if(columns>3 && rows>3) { //checking if the maze is not 3x3 where the extension goes over what's allowed
						maze[exit[0]][exit[1]+2] = true; //add another path to the right of the exit
					}

				}
				else { //exit is on the right side
					maze[exit[0]][exit[1]-1] = true; //add a path to the left of the exit 
					if(columns>3 && rows>3) { //checking if the maze is not 3x3 where the extension goes over what's allowed
						maze[exit[0]][exit[1]-2] = true; //add another path to the left
					}
				}
			}
			else if(exit[1]!=columns-1&&exit[1]!=0) {//if the exit is not on the left or right
				if(exit[0]==0) { //if exit is on the top
					maze[exit[0]+1][exit[1]] = true;  //adds a path below
					if(columns>3 && rows>3) {
						maze[exit[0]+2][exit[1]] = true; //add another below
					}
				}
				else { //exit has to be on the bottom
					maze[exit[0]-1][exit[1]] = true; //add a path above
					if(columns>3 && rows>3) { //checking if the maze is not 3x3
						maze[exit[0]-2][exit[1]] = true; //adds another path above
					}
				}
			}

			//generates a random start position 
			startPosition = generateStartingPosition();
			maze[startPosition[0]][startPosition[1]] = true;
		} else{

			generatePath();
			//determining starting direction and sticking out a path from exit to always attach to maze
			if(exit[0]!= rows-1 && exit[0]!=0) { //if the exit is not on the top or bottom
				if(exit[1]==0) { //if the exit is on the left side
					maze[exit[0]][exit[1]+1] = true; //add a path to the right of the exit
				}
				else { //exit is on the right side
					maze[exit[0]][exit[1]-1] = true; //add a path to the left of the exit 
				}
			}
			else if(exit[1]!=columns-1&&exit[1]!=0) {//if the exit is not on the left or right
				if(exit[0]==0) { //if exit is on the top
					maze[exit[0]+1][exit[1]] = true;  //adds a path below
				}
				else { //exit has to be on the bottom
					maze[exit[0]-1][exit[1]] = true; //add a path above
				}
			}
			boolean [][] mazeCopy = maze; //create a copy of the maze to avoid overriding

			while(!valid) { //have a valid loop determine whether or not a valid starting position has been created. Becomes true when a random point is selected and valid is changed from the method called
				validStarting(exit[0], exit[1], mazeCopy, startPosition, 0); //searches for a valid starting point
			}
			valid = false; //reset for next use;

			maze[startPosition[0]][startPosition[1]] = true; //sets the starting point to true
		}



		return maze; 
	}

	/*generateEdgePath method
	 * 
	 * This procedural method is used to generate a branch maze where it is all connected by adding
	 * 2 long edge pieces in the top and left of the maze. This works because of the nature of how the
	 * maze is formed (all branches start from the top and go down. This works in reverse compared to the other generations. Instead of adding a path,
	 * I add random walls to the maze instead. First it sets the whole maze except borders to true,
	 * then generates the walls by setting every 2 indexes both horizontally and vertically to false (a wall),
	 * then randomly attaches each of these points to each other either to the one in front of it vertically, or
	 * horizontally.
	 * 
	 * LOCAL VARIABLES
	 * n/a
	 * 
	 * @param n/a
	 * @return void
	 * @throws n/a
	 * 
	 */
	public static void generateEdgePath() {

		//sets the whole maze true except for the borders
		for(int i = 1; i < rows-1; i++) { // i starts at 1 and stops 1 before the end to avoid the edges
			for(int j = 1; j < columns-1; j++) { //j starts at 1 and stops 1 before the end to avoid the edges
				maze[i][j] = true; //sets maze true
			}
		}


		for (int i = 2; i < rows; i += 2) { //starts within the maze but shifted 1 more down so that there will leave an edge in the top and to the left
			for (int j = 2; j < columns; j += 2) { //starts within the maze but shifted 1 more right
				maze[i][j] = false; //adds the wall
				// Carve out random connecting path
				if (j < columns - 1 && Math.random()>0.5) { //checks if adding to the right is allowed and has a 50/50 random chance of generating a connecting wall (math.random generates from 0 - 1(exlusive) and sees if it is above 0.5 (the middle)
					maze[i][j + 1] = false; //adds a wall to the right
				} else if (i < rows - 2) { //checks if adding down is allowed if it had not added to the right
					maze[i + 1][j] = false; //adds a wall down
				}
			}
		}


	}


	/*generatePath method
	 * 
	 * This procedural method is used to generate a branch maze. It does this by setting every
	 * two indexes as a path in both the vertical and horizontal direction. After, it randomly selects
	 * to connect to another branch either to the right or down. 
	 * 
	 * LOCAL VARIABLES
	 * n/a
	 * 
	 * @param n/a
	 * @return void
	 * @throws n/a
	 * 
	 */
	public static void generatePath() {
		for (int i = 1; i < rows-1; i += 2) { //start i on 1 so it doesn't go on the border, then end 1 before the edge. increment by 2
			for (int j = 1; j < columns-1; j += 2) { //start j on 1 so it doesn't go on the border, then end 1 before the edge, increment by 2
				maze[i][j] = true; //sets the path on the maze to true
				// Carve out random connecting path
				if (j < columns - 2 && Math.random()>0.5) { //check if generating to the right is available, then randomly generates a number from 0 to 1 (exclusive) and sees if it is over 0.5 as a 50/50 chance
					maze[i][j + 1] = true; //sets the path on the maze to true
				} else if (i < rows - 2) { //if it didn't attach to the right, then check if going down is available and then attach to the bottom
					maze[i + 1][j] = true; //sets the path on the maze to true
				}
			}
		}
	}

	/*validStarting method
	 * 
	 * This procedural recursive method searches through a given branch where the end point is attached to and randomly
	 * sets the start position on it to guarentee a valid maze. It first searches all available paths around the current 
	 * point. When it finds it, it calls itself and searches the paths around the new point. Throughout the search, there is
	 * a chance determined by the column size that the start point will be placed at the current location (set start point
	 * to current point) and the loop that keeps this code running ends (valid becomes true). If no points are found around
	 * the current. Then it ends the current recursive call and goes back.
	 * 
	 * LOCAL VARIABLES
	 * int columnSize - height of maze
	 * int rowSize - width of maze
	 * Rand rand - object to generate random values
	 * 
	 * @param 
	 * int currentY - start on exit's y coords
	 * int currentX - start on exit's x coords
	 * boolean[][] mazeCopy
	 * int[] deployStart - the array that saves the value of the coordinates of the generated starting point
	 * int branchLen - keep track of how long the branch has been searched
	 * 
	 * @return void
	 * @throws n/a
	 * 
	 */
	public static void validStarting(int currentY, int currentX, boolean[][] mazeCopy, int[] deployStart, int branchLen) {

		//initialize the dimensions of the maze as a variable
		int columnSize = mazeCopy.length;
		int rowSize = mazeCopy[0].length;

		//instantiate the random object
		Random rand = new Random();

		//check if the current point is not on the exit and then randomly generate a number and check if it is under 2 (ratio is columnSize:2). If it is, set the new start point to the current point.
		if((currentX!=exit[1] || currentY!=exit[0])&& rand.nextInt(columnSize) < 2) {

			//setting start point to current poitn
			deployStart[0] = currentY;
			deployStart[1] = currentX;

			//make the global variable valid true, and the loop where this method was first called ends
			valid = true;
		}

		if (currentX > 1 && mazeCopy[currentY][currentX - 1] == true) { // check for left border and if the path continues left
			mazeCopy[currentY][currentX - 1] = false; //set left to false (to not run back into the same spot)
			validStarting(currentY, currentX - 1, mazeCopy, deployStart, branchLen+1); //recursive call and change the current position to the left and check that points surroundings
			mazeCopy[currentY][currentX - 1] = true; // backtrack
		}

		if (currentX < rowSize - 1 && mazeCopy[currentY][currentX + 1] == true) { // check for right border and if the path continues right
			mazeCopy[currentY][currentX + 1] = false; //set right to false (to not run back into the same spot)
			validStarting(currentY, currentX + 1, mazeCopy, deployStart, branchLen+1); //recursive call and change the current position to the right and check that points surroundings
			mazeCopy[currentY][currentX + 1] = true; // backtrack
		}

		if (currentY > 1 && mazeCopy[currentY - 1][currentX] == true) {  // check for top border and if the path continues up
			mazeCopy[currentY - 1][currentX] = false; //set point above to false (to not run back into the same spot)
			validStarting(currentY - 1, currentX, mazeCopy, deployStart, branchLen+1);//recursive call and change the current position to up and check that points surroundings
			mazeCopy[currentY - 1][currentX] = true; // backtrack
		}

		if (currentY < columnSize - 1 && mazeCopy[currentY + 1][currentX] == true) {  // check for bottom border and if the path continues down
			mazeCopy[currentY + 1][currentX] = false; //set point bellow to false (to not run back into the same spot)
			validStarting(currentY + 1, currentX, mazeCopy, deployStart, branchLen+1); //recursive call and change the current position to down and check that points surroundings
			mazeCopy[currentY + 1][currentX] = true; // backtrack
		}

	}


	/**setUpChaoticMaze method:
	 * 
	 * This functional method sets up an erratic maze that doesn't guarantee a proper path from start to finish. It uses a basic
	 * algorithm and random generation
	 *
	 * LOCAL VARIABLES
	 * seed - a randomly generated value that determines whether a tile will be open or not - Type: integer
	 * rand - an object of the Random class used to generate random numbers - Type: Random
	 * 
	 * @param boolean generatePour
	 * @return boolean [][]
	 * @throws n/a
	 */

	public static boolean [][] setUpChaoticMaze(boolean generatePour){
		int seed; //declare an int variable seed
		Random rand = new Random(); //create a new object named rand
		maze = new boolean[rows][columns]; //ensure that maze is set to the correct dimensions

		generateExitTile(); //start by generating an exit tile usng the generateExitTile method
		maze[exit[0]][exit[1]] = true; //set that part of the maze to true
		if(generatePour) {  //checks to see if generatPour is toggled
			for (int x=1;x<maze.length-1;x++){ //double for loop with conditions only runs through the inner part of the maze (excluding the outder border)
				for (int y=1;y<maze[x].length-1;y++){
					seed=rand.nextInt(3); //generate seed as either 0, 1, or 2
					if (seed==0){ //in the 1/3 chance of it being 0...
						maze[x][y]=true; //set maze at the resepective coordinates to true
					}else{
						maze[x][y]=false; //set maze at the resepective coordinates to false
					}
				}
			}

		} else { //else, if generatePour if off
			generatePath(); //use the generatePath method
		}

		maze[rand.nextInt(maze.length-2)+1][rand.nextInt(maze[0].length-2)+1]=true;  //ensure that at least 1 non border cell is a path. Guarantees generations that include a start and exit

		startPosition = generateStartingPosition(); //generate a starting position using the generateStartingPosition method

		maze[startPosition[0]][startPosition[1]] = true; //set the starting position to true

		return maze; //return the 2D boolean array


	}

	/**generateExitTile method:
	 * 
	 * This procedural method randomly generates the exit tile for a maze. It makes sure that the exit is on the border, and not the 4 corners
	 *
	 * LOCAL VARIABLES
	 * rand - an object of the Random class used to generate random numbers - Type: Random
	 * wall - used to determine which of the 4 walls the exit will be placed in - Type: int
	 * 
	 * @param n/a
	 * @return void
	 * @throws n/a
	 */


	public static void generateExitTile() {
		Random rand = new Random(); //create a new object named rand

		int wall = rand.nextInt(4); //set wall to a randomly generated value from 0-3

		if(wall<2) { //if wall is less than 2,  the exit will be placed on the right or left wall
			exit[0] = rand.nextInt(rows-2)+1; //establishes the first coordinate, avoiding corners. 
			//the rightmost and leftmost cells have similar first coordinates
			if(wall==0) { //right
				exit[1] = columns - 1; //sets the second coordinate to columns-1
			} else { //left
				exit[1] = 0; //sets the second coordinate to 0
			}
		}else { //the exit will be on the top or bottom
			exit[1] = rand.nextInt(columns-2)+1; //establish the second coordinate, avoiding corners
			//the upper and lowermost cells have similar second coordinates
			if(wall==2) { //bottom
				exit[0] = rows - 1; //set the first coordinate to rows-1
			} else { //top
				exit[0] = 0;//set the first coordinate to 0
			}

		}
	}

	/**resetToDefaultLegend method:
	 * 
	 * This procedural method resets the four global variables barrier, open, start, and exitSymbol back
	 *to their default value. This is to ensure that the default values are reset after importing a file with
	 *different values 
	 *
	 * 
	 * LOCAL VARIABLES
	 * n/a
	 * 
	 * @param n/a
	 * @return n/a
	 * @throws n/a
	 */

	public static void resetToDefaultLegend (){
		barrier = "B"; //reset barrier to "B"
		open = "O"; //reset open to "O"
		start ="S"; //reset start to "S"
		exitSymbol = "X"; //reset exitSymbol to "X"
	}

	/**generateMainPath method:
	 * 
	 * This procedural method is used exclusively for the flood fill generating algorithm.
	 * It's job is to start at the exit and generate open tiles by moving up/down/right/left at random.
	 * The algorithm is not allowed to revisit an open path, but this may lead to it being stuck. In this case,
	 * invalidCounter is used to detect if the algorithm is stuck, and allows an escape path
	 *
	 * 
	 * LOCAL VARIABLES
	 * count - used to keep track of how many iterations in the main while loop have taken place - Type: int
	 * rand - an object of the Random class used to generate random numbers - Type: Random
	 * currentPath - used to keep track of where the algorithm is currently at while generating - Type: integer array
	 * centerSquares - represents how many squares are available in the center of the maze - Type: int
	 * seed - a ranomly generated value that determines if the next open square will be up/down/right/ or left - Type: integer
	 * 
	 * @param n/a
	 * @return void
	 * @throws n/a
	 */

	public static void generateMainPath()  {

		int count = 0; //initialize an integer variable count to 0;

		Random rand = new Random(); //initialize a object named rand of the random class
		int[] currentPath = new int [2]; //initialize an integer array named currentPath, and set it to size 2. 2 elements is all that's needed to store coordinates
		currentPath [0] = exit [0]; //initialize currentPath to the exit. That's where the generation will start
		currentPath [1] = exit [1];

		int centerSquares = (rows * columns) - 2 * rows - 2 * columns + 4; //use some math to determine the area of the maze minus the outside border. 
		//area-2*length-2*width +4

		int mainLength = (rand.nextInt((centerSquares / 4)+1) + centerSquares / 4)+1; //mainlength is randomly generated, but controlled to be at least 1 and at least 1/4 of centersquares, and 
		//the purely random value is restricted to the range of centerSquares/4 +1. This gives a pretty consistent mainLength

		invalidCounter = 0; //set invalidCounter to 0

		while (count < mainLength) { //while loop continues while count is less than mainlength
			int seed = rand.nextInt(4); //randomly generates a number from 0-3 and assigns it to seed

			switch (seed) { // start a switch case, based on seed
			case 0:
				if (addPathUp(currentPath)) { //if seed is 0, call the addPathUp method
					count++;//count accumulates if the method returns true (meaning that it was a valid move)

				} else {

					invalidCounter++; //in case the move is not valid, invalidCounter is accumulated
					break; //break out of the switch case
				}
				break; //break out of the switch case
			case 1:
				if (addPathDown(currentPath)) { //if seed i 1, call the addPathDown method

					count++;//count accumulates if the method returns true (meaning that it was a valid move)

				} else {

					invalidCounter++;//in case the move is not valid, invalidCounter is accumulated
					break; //break out of the switch case
				}
				break; //break out of the switch case
			case 2:
				if (addPathRight(currentPath)) { //if seed is 2, call the addPathRight method

					count++;//count accumulates if the method returns true (meaning that it was a valid move)
				} else {

					invalidCounter++;//in case the move is not valid, invalidCounter is accumulated
					break; //break out of the switch case
				}
				break; //break out of the switch case
			case 3:
				if (addPathLeft(currentPath)) {//if seed is 3, call the addPathLeft method

					count++;//count accumulates if the method returns true (meaning that it was a valid move)
				} else {

					invalidCounter++;//in case the move is not valid, invalidCounter is accumulated
					break; //break out of the switch case
				}
				break; //break out of the switch case
			}
		}
	}

	/**addPathUp method:
	 * 
	 * This functional method is used to add an open path above the currentPath. Using many conditions and logic, it returns
	 * either true or false to indicate if moving in such direction is valid. In the case that the algorithm is stuck, it will test
	 * if invalidCounter is above 10, and if so, provide a way out
	 * 
	 * LOCAL VARIABLES
	 * n/a
	 * 
	 * @param int [] currentPath
	 * @return boolean
	 * @throws n/a
	 */

	public static boolean addPathUp(int[] currentPath) {
		if (currentPath[0]-1 == -1) { //makes sure that currentPath isn't on the top border, where moving up will 
			//cause it to go out of bounds
			return false; //returns false, as moving up in this situation is illegal
		}

		if (currentPath[0] - 1 != 0 && currentPath[1] != 0 && currentPath[1] != maze[0].length - 1
				&& (maze[currentPath[0] - 1][currentPath[1]])==true) {

			//the first condition makes sure that moving up wont arrive on the left border
			//the second condition makes sure that moving up wont land on the top border
			//the third condition makes sure that moving up wont land on the right border
			//the last condition makes sure that moving up wont land on an already open path

			//if all conditions are satisfied...

			maze[currentPath[0] - 1][currentPath[1]] = true; //set the tile above currentPath to true
			currentPath[0] = currentPath[0] - 1; //move currentPath up
			return true;//return true, as moving up was a success
		} else if (currentPath[0] - 1 != 0 && currentPath[1] != 0 && currentPath[1] != maze[0].length - 1
				&& invalidCounter > 10) {


			//the first three conditions are the same as above
			//the last condition is changed, testing if invalid counter is above 10. If so, this is likely because the algorithm is
			//stuck. In this case, the program allows an escape route

			maze[currentPath[0] - 1][currentPath[1]] = true; //set the tile above currentPath to true
			currentPath[0] = currentPath[0] - 1; //move currentPath up
			invalidCounter = 0; //reset invalidCounter back to 0
			return true;//return true, moving up was a success
		} else {

			return false; //remaining cases will be considered as invalid
		}
	}

	/**addPathDown method:
	 * 
	 * This functional method is used to add an open path below the currentPath. Using many conditions and logic, it returns
	 * either true or false to indicate if moving in such direction is valid. In the case that the algorithm is stuck, it will test
	 * if invalidCounter is above 10, and if so, provide a way out
	 * 
	 * LOCAL VARIABLES
	 * n/a
	 * 
	 * @param int [] currentPath
	 * @return boolean
	 * @throws n/a
	 */

	public static boolean addPathDown(int[] currentPath) {

		if (currentPath[0]+1 == maze.length) {//makes sure that currentPath isn't on the bottom border, where moving down will
			//cause it to go out of bounds
			return false;//returns false, as moving down in this situation is illegal
		}

		if (currentPath[0] + 1 != maze.length - 1 && currentPath[1] != maze[0].length - 1 && currentPath[1] != 0
				&& !(maze[currentPath[0] + 1][currentPath[1]])==true) {

			//the first condition makes sure that moving down wont land on the bottom border
			//the second condition makes sure that moving down wont land on the right border
			//the third condition makes sure that moving down wont land on the left border
			//the last condition makes sure that moving up wont land on an already open path
			//if all conditions are satisfied...

			maze[currentPath[0] + 1][currentPath[1]] = true; //set the tile below currentPath to true
			currentPath[0] = currentPath[0] + 1; //move currentPath down
			return true; //return true as moving down was a success
		} else if (currentPath[0] + 1 != maze.length - 1 && currentPath[1] != maze[0].length - 1
				&& currentPath[1] != 0 && invalidCounter > 10) {

			//the first three conditions are the same as above
			//the last condition is changed, testing if invalid counter is above 10. If so, this is likely because the algorithm is
			//stuck. In this case, the program allows an escape route

			maze[currentPath[0] + 1][currentPath[1]] = true; //set the tile below currentPath to true
			currentPath[0] = currentPath[0] + 1; //move currentPath down
			invalidCounter = 0;//reset invalidCounter back to 0
			return true;//return true, moving down was a success
		} else {

			return false;//remaining cases will be considered as invalid
		}
	}

	/**addPathLeft method:
	 * 
	 * This functional method is used to add an open path to the left of currentPath. Using many conditions and logic, it returns
	 * either true or false to indicate if moving in such direction is valid. In the case that the algorithm is stuck, it will test
	 * if invalidCounter is above 10, and if so, provide a way out
	 * 
	 * LOCAL VARIABLES
	 * n/a
	 * 
	 * @param int [] currentPath
	 * @return boolean
	 * @throws n/a
	 */
	public static boolean addPathLeft(int[] currentPath) {


		if (currentPath[1]-1 == -1) { //makes sure that currentPath isn't on the left border, where moving left will
			//cause it to go out of bounds
			return false;//returns false, as moving left in this situation is illegal
		}

		if (currentPath[1] - 1 != 0 && currentPath[0] != maze.length - 1 && currentPath[0] != 0
				&& !(maze[currentPath[0]][currentPath[1] - 1])==true) {

			//the first condition makes sure that moving left wont land on the left border
			//the second condition makes sure that moving left wont land on the bottom border
			//the third condition makes sure that moving left wont land on the top border
			//the last condition makes sure that moving up wont land on an already open path
			//if all conditions are satisfied...

			maze[currentPath[0]][currentPath[1] - 1] = true;//set the tile to the left of currentPath to true
			currentPath[1] = currentPath[1] - 1;//move currentPath to the left
			return true;//return true, moving left was a success
		} else if (currentPath[1] - 1 != 0 && currentPath[0] != maze.length - 1 && currentPath[0] != 0 
				&& invalidCounter > 10) {
			//the first three conditions are the same as above
			//the last condition is changed, testing if invalid counter is above 10. If so, this is likely because the algorithm is
			//stuck. In this case, the program allows an escape route

			maze[currentPath[0]][currentPath[1] - 1] = true;//set the tile to the left of currentPath to true
			currentPath[1] = currentPath[1] - 1;//move currentPath to the left
			invalidCounter = 0; //reset invalidCounter to 0;
			return true;//return true, moving to the left was a success
		} else {

			return false;//remaining cases will be considered as invalid
		}


	}

	/**addPathRight method:
	 * 
	 * This functional method is used to add an open path to the right of currentPath. Using many conditions and logic, it returns
	 * either true or false to indicate if moving in such direction is valid. In the case that the algorithm is stuck, it will test
	 * if invalidCounter is above 10, and if so, provide a way out
	 * 
	 * LOCAL VARIABLES
	 * n/a
	 * 
	 * @param int [] currentPath
	 * @return boolean
	 * @throws n/a
	 */

	public static boolean addPathRight(int[] currentPath) { 

		if (currentPath[1]+1 == maze[0].length) { //makes sure that the currentPath isn't on the right border, where moving right will
			//cause it to go out of bounds
			return false;//returns false, as moving right in this situation is illegal
		}

		if (currentPath[1] + 1 != maze[0].length - 1 && currentPath[0] != maze.length - 1 && currentPath[0] != 0
				&& !(maze[currentPath[0]][currentPath[1] + 1])==true) {

			//the first condition makes sure that moving right wont land on the right border
			//the second condition makes sure that moving right wont land on the bottom border
			//the third condition makes sure that moving right wont land on the top border
			//the last condition makes sure that moving up wont land on an already open path
			//if all conditions are satisfied...


			maze[currentPath[0]][currentPath[1] + 1] = true; //set the tile to the right of currentPath to true
			currentPath[1] = currentPath[1] + 1;//move currentPath to the right

			return true; //return true, moving right was a success

		} else if (currentPath[1] + 1 != maze[0].length - 1 && currentPath[0] != maze.length - 1
				&& currentPath[0] != 0 && invalidCounter > 10) {

			//the first three conditions are the same as above
			//the last condition is changed, testing if invalid counter is above 10. If so, this is likely because the algorithm is
			//stuck. In this case, the program allows an escape route
			maze[currentPath[0]][currentPath[1] + 1] = true; //set the tile to the right of currentPath to true
			currentPath[1] = currentPath[1] + 1;//move currentPath to the right
			invalidCounter = 0;//reset invalidCounter to 0;
			return true;//return true, moving to the right was a success

		} else {
			return false;//remaining cases wiill be considered as invalid
		}


	}

	/**generateStartingPosition method:
	 * 
	 * This functional method is used to generat the starting position on a maze.
	 * It randomly assigns one of the open paths (that is not the exit)
	 * as a starting location
	 * 
	 * LOCAL VARIABLES
	 * rand - an object of the Random class used to generate random data - Type: Random
	 * count - used as an accumulator - Type: int
	 * listOfOpenSquares - used to store the coordinates of all the open paths - Type: int [][]
	 * 
	 * @param n/a
	 * @return int[]
	 * @throws n/a
	 */

	public static int[] generateStartingPosition() {
		Random rand = new Random();//create a new object named Rand
		int count = 0;//declare a new int variable count
		int[][] listOfOpenSquares = new int[maze.length * maze[0].length][2]; //create a new 2D int array named listOfOpenSquares
		//set its size to the area of the maze*2 (area of maze so there's enough space, 2 to store 2 coordinates each)
		for (int x = 0; x < maze.length; x++) {//run through maze with 2 for loops
			for (int y = 0; y < maze[x].length; y++) {
				if (maze[x][y] == true) { //checks to see if a specific pair of coordinates is an open path
					listOfOpenSquares[count][0] = x; //adds the first coordinate to index count
					listOfOpenSquares[count][1] = y; //adds the second coordinate to index count
					count++;//accumulates count
				}
			}
		}

		int seed = rand.nextInt(listOfOpenSquares.length); //generates a random number using the length of the list of coordinates

		while ((listOfOpenSquares[seed][0] == 0 && listOfOpenSquares[seed][1] == 0) || (listOfOpenSquares[seed][0] == exit[0] && listOfOpenSquares[seed][1] == exit[1])) {
			//checks to makes sure that the random value isn't 0,0 or the exit
			seed = rand.nextInt(listOfOpenSquares.length); //if so, regenerate

		}

		return listOfOpenSquares[seed];//return the coordinates of the starting position
	}


	/*findShortestPath method
	 * 
	 * This functional recursive method utilizes depth-first search to find the shortest path out of all possible paths from start
	 * to the exit and saves the entire path in a 2d arraylist of arrays. It is an optomized depth-first search where it stops searching a branch if the length exceeds the current shortest
	 * path and returns -1. The search works by searching all points around the current one for another path. If a point is found, it calls itself
	 * but with the neighbors coordinates as the current coordinates and increment the currentLen by one. Then it checks the points around
	 * that. If nothing is found, it returns -1. If the end is found and it was not terminated earlier for being too long, return the currentLen as the shortest.
	 *  As for the shortestpath saving, every call it saves the current point coordinates into a 2d arraylist of arrays. If the exit is reached, clear the best path save arraylist and
	 *  add the entire current path to it. If there are no more available paths around a point, i subtract the current position from the arraylist as I had reached a deadend and have to backtrack.
	 *  
	 *  LOCAL VARIABLES
	 *  int columnSize
	 *  int rowSize
	 *  int [] coords - saves the coordinates in an array to be put into the 2d arraylist of arrays
	 *  int branchLen - initailize as -1 where negative one represents an untouched branchLen. branchLen keeps track of the extended paths from each point
	 *  
	 *  @param
	 *  int currentY
	 *  int currentX
	 *  int endingY
	 *  int endingX
	 *  boolean[][] mazeCopy
	 *  int currentLen
	 *  ArrayList<int[]> pathCoords
	 *  ArrayList<int[]> bestPathSave
	 *  
	 *  @return int
	 *  @throws n/a
	 */
	public static int findShortestPath(int currentY, int currentX, int endingY, int endingX, boolean[][] mazeCopy, int currentLen,  ArrayList<int[]> pathCoords, ArrayList<int[]> bestPathSave) {

		//saving the dimensions of the maze into variables
		int columnSize = mazeCopy.length;
		int rowSize = mazeCopy[0].length;


		mazeCopy[currentY][currentX] = false; //set the current to false (as checked) so it doesn't loop back
		if(currentLen>=shortest) { //if the current path from the start is greater or equal to shortest, terminate and return -1 since it cannot be any shorter
			return -1; //go back
		}

		if ((currentY == endingY) && (currentX == endingX)) { //check if the current point is on the endpoint

			bestPathSave.clear(); //clear the arraylist to save the path to reach the shortest path
			bestPathSave.addAll(pathCoords); //transfering the current pathcoords to the bestpathsave because if it passes the first two conditions, it is gaurenteed the shorter than previous making it the new best

			shortest = currentLen; //setting the global variable shortest to the currentLen so it can be compared throughout the whole search
			return currentLen; // found the newest shortestpath and should return the length
		}

		int[]coords = {currentX, currentY}; //creates an array that stores the current coordinates
		pathCoords.add(coords); //adds the array into the 2d arraylist of arrays to pathCoords

		int branchLen = -1; //creates a branchLen int for each point to see which branch FROM THAT POINT, is the shortest
		if (currentX > 0 && (mazeCopy[currentY][currentX - 1] == true)) { // check for left border and if there is a path to the left
			mazeCopy[currentY][currentX - 1] = false; //if there is, set that point false to prevent backtracking
			int fetchLen = findShortestPath(currentY, currentX - 1, endingY, endingX, maze, currentLen + 1, pathCoords, bestPathSave); //searches the surroundings of that point and saves the shortest paths that stem from that point in fetchLen
			branchLen = updateShortestPath(fetchLen, branchLen); //check if the fetchLen is shorter than the branchLen
			mazeCopy[currentY][currentX - 1] = true; // backtrack
		}
		if (currentX < rowSize - 1 && (mazeCopy[currentY][currentX + 1] == true)) { // check for right border and if there is a path to the right
			mazeCopy[currentY][currentX + 1] = false;//if there is, set that point false to prevent backtracking 
			int fetchLen = findShortestPath(currentY, currentX + 1, endingY, endingX, maze, currentLen + 1, pathCoords, bestPathSave); //searches the surroundings of that point and saves the shortest paths that stem from that point in fetchLen
			branchLen = updateShortestPath(fetchLen, branchLen);  //check if the fetchLen is shorter than the branchLen
			mazeCopy[currentY][currentX + 1] = true; // backtrack

		}
		if (currentY > 0 && (mazeCopy[currentY - 1][currentX] == true)) { // check up and if there is a path above
			mazeCopy[currentY - 1][currentX] = false;//if there is, set that point false to prevent backtracking
			int fetchLen = findShortestPath(currentY - 1, currentX, endingY, endingX, maze, currentLen + 1, pathCoords, bestPathSave); //searches the surroundings of that point and saves the shortest paths that stem from that point in fetchLen
			branchLen = updateShortestPath(fetchLen, branchLen); //check if the fetchLen is shorter than the branchLen
			mazeCopy[currentY - 1][currentX] = true; // backtrack

		}
		if (currentY < columnSize - 1 && (mazeCopy[currentY + 1][currentX] == true)) { // check down and if there is a path down
			mazeCopy[currentY + 1][currentX] = false;//if there is, set that point false to prevent backtracking
			int fetchLen = findShortestPath(currentY + 1, currentX, endingY, endingX, maze, currentLen + 1, pathCoords, bestPathSave); //searches the surroundings of that point and saves the shortest paths that stem from that point in fetchLen
			branchLen = updateShortestPath(fetchLen, branchLen); //check if the fetchLen is shorter than the branchLen
			mazeCopy[currentY + 1][currentX] = true; // backtrack
		}

		if(!pathCoords.isEmpty()) { //check if the path is not empty
			pathCoords.remove(pathCoords.size()-1); //if the end of a point is reached, backtrack by removing the current point in the arraylist
		}

		return branchLen; //return the branchLen (the shortest path that can be achieved from the current point)
	}



	/*updateShortestPath method
	 * 
	 * this functional method is used to compare the fetchedLength of each branch stemming off a point to the branchLen (shortest fetchedLength of the four sides)
	 * 
	 * LOCAL VARIABLES
	 * n/a
	 * 
	 * @params
	 * int fetchLen
	 * int branchLen
	 * 
	 * @return int - the new (or untouched) value of branchLen after comparisons
	 * @throws n/a
	 */
	public static int updateShortestPath(int fetchLen, int branchLen) {
		if (fetchLen != -1 && (branchLen == -1 || fetchLen < branchLen)) { //checks if fetchLen was not terminated early (-1) (meaning it found a path), then check whether it was the first time branchLen was being updated (-1) or if branchLen is greater than fetchLen 
			branchLen = fetchLen; //set branch as fetch (the new shortest)

		}
		return branchLen; //return the new value
	}

}

/*
 *  Class: JButtonV2
 *  
 *  This class is a custom version of the jbutton but is able to store two values 
 *  which are the x and y coordinates of the button
 */
class JButtonV2{
	//every jbuttonv2 has a normal jbutton
	JButton button = new JButton();
	int x;
	int y;
}