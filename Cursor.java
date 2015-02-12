package mapbase;
/** A class to create cursors which accept standard input to control movement and have basic edge handling.
 */
public class Cursor
{
	/** cursor coordinates (corresponds to given map size)*/
    private int cursorY, cursorX;
	/** threshold values for the cursor position*/
    private final int ymin, ymax, xmin, xmax;

	/** Creates a Cursor object using map information
	 *
	 * @param mapY	number of rows in the map determines edges
	 * @param mapX	number of columns for edge handling
	 * @param initialY Starting position for the cursor.
	 * @param initialX Shouldn't be on an edge or arraycopy2d might break.
	 */
    public Cursor (int mapY, int mapX, int initialY, int initialX)
    {
        ymin = 0;
        ymax = mapY-1;
        xmin = 0;
        xmax = mapX-1;
        cursorY = initialY;
        cursorX = initialX;
    }

    // accessors for cursor position
	/** Accessor for cursor Y value
	 * @return cursorY row value relative to map array
	 */
    public int getCursorY(){
       return cursorY;
    }
	/** Accessor for cursor X value
	 * @return cursorX column value relative to map array
	 */
    public int getCursorX(){
        return cursorX;
    }
	/** Accessor for cursor coordinates
	 * @return coords {row, col}
	 */
    public int[] getCursorYX(){
        int[] coords = {cursorY,cursorX};
        return coords;
    }

	/** Centralizes movement method calls using a direction from input
	 * @param direction String passed by game input handling: "(N/S/x)"+"(E/W/x)" but never "xx".
	 */
    public void move(String direction){
        northsouth(direction.charAt(0));
        eastwest(direction.charAt(1));
    }
    /** Decides whether to move north, south, or neither
	 * @param ns first character in string passed to move()
	 */
	private void northsouth(char ns){
        if (ns == 'N')
            moveN();
        else if (ns == 'S')
            moveS();
    }
    /** Decides whether to move east, west, or neither
	 * @param ew second character in string passed to move()
	 */
    private void eastwest(char ew){
        if (ew == 'E')
            moveE();
        else if (ew =='W')
            moveW();
    }

    // movement for each direction w/ edge checking
	/** Move cursor up (includes edge checking)
	 */
    private void moveN(){
        if (cursorY > ymin)
            cursorY--;
        else error();
    }   
	/** Move cursor right (includes edge checking)
	 */
    private void moveE(){
        if (cursorX < xmax)
            cursorX++;
        else error();
    }
	/** Move cursor down (includes edge checking)
	 */
    private void moveS(){
        if (cursorY < ymax)
            cursorY++;
        else error();
    }
	/** Move cursor left (includes edge checking)
	 */
    private void moveW(){
        if (cursorX > xmin)
            cursorX--;
        else error();
    }   

	/** Called by moveX() methods when cursor tries to move over edge; does nothing, but could.
	 */
    private void error(){
    }
}
