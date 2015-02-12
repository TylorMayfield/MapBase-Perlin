package mapbase;
//import java.util.Random;
import java.util.Scanner;
import toxi.math.noise.PerlinNoise;

/** Map generator with input-controlled cursor, scrolling, and color output
 * @author Nathan Soodak
 */
public class Game
{

    //private Random gen = new Random(); 
	/** map size variables (rows and columns)*/
    private int mapSizeY, mapSizeX; // rows, cols respectively 
    private int[] array0 = { 0, 0 };

	/** display window array size*/
    private final int displaySizeY = 30, displaySizeX = 30;
    private int[] displayYX = new int[2];
	
	/** array containing the current display of worldMap*/
    private int[][] currentDisplay = new int[displaySizeY][displaySizeX];
    private int[] displaySize = { displaySizeY, displaySizeX };

	/** used by display() to output the current display to a terminal*/
    private String currentScreen;

    private Scanner scan = new Scanner(System.in);

	/** array of integers that refers to terrain characters. This never changes, see worldMap.*/
    private int[][] terrainMap;
	/** array of integers that refers to terrain and other characters.*/
	private int[][] worldMap;

	/** Creates a game object, including new map, display view, and cursor*/
    public Game()
    {
		chooseMap();
        terrainInit();

        mapInit();
        cursorInit();
        displayInit();

        display();
        //getInput();
    }
	/** Creates a game object with custom map size
	 * @param mapsizey number of rows in map
	 * @param mapsizex number of columns in map
	 */ 
	public Game(int mapsizey, int mapsizex)
	{
		mapSizeY = mapsizey;
		mapSizeX = mapsizex;
		
		terrainInit();

		mapInit();
		cursorInit();
		displayInit();

		display();
	}

    private String input;
	/** Gets input from the user and calls cursor.move() as necessary*/
    public void getInput()
    {
        input = "";
        while (!input.equals("Q"))
        {
            System.out.print("Which direction? ");
            input = scan.next();
            input = input.toUpperCase();
            if (input.equals("N")||input.equals("S"))
                cursor.move(input.concat("x"));
            else if (input.equals("E")||input.equals("W"))
                cursor.move("x".concat(input));
            else if (input.equals("NE")||input.equals("SE")||
            input.equals("NW")||input.equals("SW"))
                cursor.move(input);
            display();
        }
    }

    private String[] terrainType = new String[20];
    private double[] terrainProb = new double[20];
	/** Sets up the terrain type display characters and probabilities
	 *
	 */
    private void terrainInit()
    {
        // Sets up terrain type characters
        terrainType[0] = (char)27+"[1;30m"+"X"+(char)27+"[0m"; // should never be displayed
        terrainType[1] = (char)27+"[1;31m"+"~"+(char)27+"[0m";
        terrainType[2] = (char)27+"[1;32m"+"!"+(char)27+"[0m";
        terrainType[3] = (char)27+"[1;33m"+"#"+(char)27+"[0m";
        terrainType[4] = (char)27+"[1;34m"+"$"+(char)27+"[0m";
        terrainType[5] = (char)27+"[1;35m"+"%"+(char)27+"[0m";
        terrainType[6] = (char)27+"[1;36m"+"^"+(char)27+"[0m";
        terrainType[7] = (char)27+"[1;37m"+"&"+(char)27+"[0m";
        terrainType[8] = (char)27+"[1;31m"+"*"+(char)27+"[0m";
        terrainType[9] = (char)27+"[1;32m"+"("+(char)27+"[0m";
        terrainType[10] = (char)27+"[1;33m"+")"+(char)27+"[0m";
        terrainType[11] = (char)27+"[1;34m"+"_"+(char)27+"[0m";
        terrainType[12] = (char)27+"[1;35m"+"-"+(char)27+"[0m";
        terrainType[13] = (char)27+"[1;36m"+"="+(char)27+"[0m";
        terrainType[14] = (char)27+"[1;37m"+"+"+(char)27+"[0m";

        // of 500x375 results of perlin noise object // min: 0.05376121774315834
        // max: 0.8684536218643188
        terrainProb[0] = 0.00;
        terrainProb[1] = 0.08;
        terrainProb[2] = 0.14;
        terrainProb[3] = 0.20;
        terrainProb[4] = 0.28;
        terrainProb[5] = 0.34;
        terrainProb[6] = 0.40;
        terrainProb[7] = 0.46;
        terrainProb[8] = 0.52;
        terrainProb[9] = 0.58;
        terrainProb[10] = 0.62;
        terrainProb[11] = 0.68;
        terrainProb[12] = 0.75;
        terrainProb[13] = 0.82;
        terrainProb[14] = 1.00;
    }

	/** Perlin Noise object created from included toxiclibscore library. This is used to select terrain type so that lakes exist and there aren't puddles every five feet.*/
    private PerlinNoise pn = new PerlinNoise();
    private double terrainPick;
	/** Creates a terrain map based on Perlin Noise (toxiclib)
	 *
	 */
    private void mapInit()
    {
        // NOTE: coord system is (ROW, COLUMN)!
        terrainMap = new int[mapSizeY][mapSizeX];
        worldMap = new int[mapSizeY][mapSizeX];

		float dX, dY;
		final float NOISE_SCALE = (float) .18;
        System.out.println("Loading a sexy new map! ...");
        for (int Y = 0; Y < mapSizeY; Y++)
        {
            dY = Y * NOISE_SCALE;
            for (int X = 0; X < mapSizeX; X++)
            {
                //terrainMap[Y][X] = gen.nextInt(6)+1;
                dX = X * NOISE_SCALE;
                terrainPick = pn.noise(dY, dX);
				if (terrainPick < terrainProb[1])
					terrainMap[Y][X] = 1;
				else if (terrainPick < terrainProb[2])
					terrainMap[Y][X] = 2;
				else if (terrainPick < terrainProb[3])
					terrainMap[Y][X] = 3; 
				else if (terrainPick < terrainProb[4])
					terrainMap[Y][X] = 4;
				else if (terrainPick < terrainProb[5])
					terrainMap[Y][X] = 5;
				else if (terrainPick < terrainProb[6])
					terrainMap[Y][X] = 6;
				else if (terrainPick < terrainProb[7])
					terrainMap[Y][X] = 7;
				else if (terrainPick < terrainProb[8])
					terrainMap[Y][X] = 8;
				else if (terrainPick < terrainProb[9])
					terrainMap[Y][X] = 9;
				else if (terrainPick < terrainProb[10])
					terrainMap[Y][X] = 10;
				else if (terrainPick < terrainProb[11])
					terrainMap[Y][X] = 11;
				else if (terrainPick < terrainProb[12])
					terrainMap[Y][X] = 12;
				else if (terrainPick < terrainProb[13])
					terrainMap[Y][X] = 13;
				else if (terrainPick < terrainProb[14])
					terrainMap[Y][X] = 14;
            }
        }
        int[] mapDims = { mapSizeY, mapSizeX };
        arraycopy2d(terrainMap, array0, worldMap, array0, mapDims);
    }

    private int displayY, displayX;
	/** cursor's Y and X position INSIDE display window*/
    private int dispCursY, dispCursX;
	/** Sets initial display window position in the map*/
    private void displayInit()
    {
        displayY = mapSizeY / 2 - displaySizeY / 2;
        displayX = mapSizeX / 2 - displaySizeX / 2;

        makeDisplay();
    }

	/** How many lines from the edge to start scrolling*/
    private final int SCROLL_BUFFER = 4;
	/** Handles scrolling of the display window (changes start position from worldMap)*/
    private void tryDisplay()
    {
        dispCursY = cursor.getCursorY()-displayY;
        dispCursX = cursor.getCursorX()-displayX;

        if (dispCursY < SCROLL_BUFFER && displayY > 0)
            displayY--;
        else if (dispCursY > displaySizeY-SCROLL_BUFFER && displayY < mapSizeY-displaySizeY)
            displayY++;
        if (dispCursX < SCROLL_BUFFER && displayX > 0)
            displayX--;
        else if (dispCursX > displaySizeX-SCROLL_BUFFER && displayX < mapSizeX-displaySizeX)
            displayX++;

        dispCursY = cursor.getCursorY()-displayY;
        dispCursX = cursor.getCursorX()-displayX;

        displayYX[0] = displayY;
        displayYX[1] = displayX;
        makeDisplay();
    }

	/** Creates the currentDisplay array from worldMap*/
    private void makeDisplay()
    {
        arraycopy2d(worldMap, displayYX, currentDisplay, array0, displaySize);
		//        used to make cursor a different character. Deprecated for inverted colors.
        //        currentDisplay[dispCursY][dispCursX] = 7; 
    }

	private void clear(){
		System.out.println((char)27+"[2J");
	}
	/** Ultimately converts the currentDisplay array of ints to a string and prints it*/
    public void display()
    {
        clear();
        tryDisplay();
        currentScreen = "\n";
        for (int Y = 0; Y < displaySizeY; Y++)
        {
            currentScreen += "  ";
            for (int X = 0; X < displaySizeX; X++)
            {
                if (Y==dispCursY && X==dispCursX){
                    currentScreen += (char)27+"[7m"+terrainType[currentDisplay[Y][X]]+(char)27+"[0m"+" ";
                }
                else currentScreen += terrainType[currentDisplay[Y][X]]+" ";
            }
            currentScreen += "\n";
        }
        System.out.print(currentScreen);
        //displayDebug();
    }

	/** Displays some extra information for runtime debugging*/
    private void displayDebug()
    {
        System.out.println("cursor:"+cursor.getCursorY()+","+cursor.getCursorX());
        System.out.println("displayCursor:"+dispCursY+","+dispCursX);
        System.out.println("displayYX:"+displayY+","+displayX);
    }

    private int arrayItem;
    /** Copies (part of) a 2d array into another at a certain location
	 *
	 * @param src source array of arrays (considered 2d)
	 * @param srcPos where to start copying from src {row,col}
	 * @param dest destination array of arrays (considered 2d)
	 * @param destPos where to start copying into dest {row,col}
	 * @param length offsets for copying elements: {Y(down), X(right)}
	 */
	private void arraycopy2d(int[][] src, int[] srcPos, int[][] dest, int[] destPos, int[] length)
    {
        if (dest.length-destPos[0] >= length[0] && 
        dest[0].length-destPos[1] >= length[1])

        {
            for (int Y = 0; Y < length[0]; Y++){
                for (int X = 0; X < length[1]; X++){
                    arrayItem = src[srcPos[0]+Y][srcPos[1]+X];
                    if (arrayItem != 0)
                        dest[destPos[0]+Y][destPos[1]+X] = src[srcPos[0]+Y][srcPos[1]+X];

                }
            }
        }
    }

	/** primary cursor object*/
    private Cursor cursor;
	/** Creates a cursor object that handles user input*/
    private void cursorInit()
    {
        cursor = new Cursor(mapSizeY, mapSizeX, mapSizeY/2, mapSizeX/2);
    }

	/** User input for map selection (just size for now)*/
    private void chooseMap()
    {
        System.out.println("New Game:\nMap Size?\n1:80x50\n2:200x160\n"+
            "3:500x375\n4:4096x3072");
        int mapSize = scan.nextInt();
        switch (mapSize) {
            case 1:
            mapSizeX = 80; mapSizeY = 50; break;
            case 2:
            mapSizeX = 200; mapSizeY = 160; break;
            case 3:
            mapSizeX = 500; mapSizeY = 375; break;
            case 4:
            mapSizeX = 4096; mapSizeY = 3072; break;
        }
    }
	 
	/*
    //Crap tylor added
    public void buildman() // Build Manager
    {

        bmenu--;
        Scanner build = new Scanner(System. in );

        System.out.println("What would you like to build?");
        System.out.println("");
        System.out.println("");
        System.out.println("1: Command Center (200, 300)");
        System.out.println("2: Barracks (100,75)");
        choice = build.nextInt();

        if (choice == 1) 
        {
            if (food <= 200 && wood <= 300)
            {
                System.out.println("Not Enough Resources");
            }
            else
            {
                food = food - 200;
                wood = wood - 300;
                spot[px][py] = 8;
            }
        }
        else

        if (choice ==2)//Barracks
        {
            if (food <= 100 && wood <= 75)
            {
                System.out.println("Not Enough Resources");
            }
            else
            {
                food = food - 100;
                wood = wood - 75;
                spot[px][py] = 9;

            }
            if (choice == 3)
            {
                bmenu++;
            }

        }

    }
        public void quit()
    {
        //clear();
        System.out.println("Thank you for playing");
        System.out.println("Made By Tylor, Matthew, and Nathan");
    }
        public void cselect (int startx ,int starty)
    {
        clear();
        System.out.println(" You have selected: " + terrainType[worldMap[startx][starty]]);////////////////HALP HERE
        System.out.println(" ");
        System.out.println(" ");
        System.out.println("1: Build Manager ");
        //if ( ttype[spot[startx][starty]].equals ("C")|ttype[spot[startx][starty]].equals ("B"))
        System.out.println("2: Current Building ");
        // if ( ttype[spot[startx][starty]].equals ("M")|ttype[spot[startx][starty]].equals ("W"))
        System.out.println("3: Unit Options ");
        System.out.println("4: Close ");
        Scanner build = new Scanner(System.in);
        choice = build.nextInt();
        if (choice == 1) 
        {
            buildman();
        }

    }
    // End of crap tylor added
	*/
}   
