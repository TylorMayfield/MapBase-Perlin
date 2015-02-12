package mapbase;
import java.util.Scanner;

// Driver class for creating a Game object
public class GameDriver
{
	/** Very basic main menu for starting a new game*/
    public static void main(String[] args)
    {
        Scanner play = new Scanner(System.in);
        int choice = 0;

		System.out.println("This is a map generator which includes a cursor, abstract display window, scrolling, and colour output.");
		System.out.println("Created by Nathan Soodak");

        while (choice!=3)
		{
			System.out.println ("\n1:Play a new game!\n2:Help\n3:Quit");
			choice = play.nextInt();
			if (choice==1)
			{    
				Game game1 = new Game();
				game1.getInput();
				break;
			}
			else if (choice==2)
			{
				System.out.println ("Your friendly help screen!");
			}    
			else if (choice==3)
			{
				System.out.println("Thanks for (not) playing!");
			}
		}
    }
}
