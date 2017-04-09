import java.util.Scanner; //Grab User Input
import java.time.DayOfWeek; //Apparently this is a separate which is dumb
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter; //Converts LocalTime & LocalDate into something actually useful
import java.text.NumberFormat; //Rounding and Currency
import java.lang.Math; //needed for absolute values
import java.math.RoundingMode;
public class Invoice 
{
	public static void main(String[] args)
	{
		Boolean again = false; //Allows do loop to start
		String[] invoices = new String[10]; //An array of user input
		double[] amount = new double[10]; //An array of final amounts
		Scanner userInput = new Scanner(System.in); //Starts the scanner
		LocalDate datePrinted = LocalDate.now(); //Initialize*
		LocalDate serviceDate = LocalDate.now();
		LocalTime startTime = LocalTime.now();
		LocalTime endTime = LocalTime.now();
		NumberFormat fmt = NumberFormat.getCurrencyInstance(); //Sets to currency format
		
			do
			{
				System.out.println("\t\tLeaky Plumbing Repair Company"); //Title of invoice
				System.out.println("\t\t\tService Charges"); //Title of invoice
				System.out.println();
				
				System.out.print("Plumber's Name (Last, First) > "); //Specifies how I want the input but I don't validate cause nothing said I needed to.
				invoices[0] = userInput.nextLine(); //Asks for input and puts it into array
				
				boolean valid = false; //Allows do loop to start
				do
				{
					System.out.print("Service Date (mm/dd/yyyy) > ");
					invoices[1] = userInput.nextLine();
					try //Validates input
					{
						serviceDate = LocalDate.parse(invoices[1], DateTimeFormatter.ofPattern("MM/dd/yyyy"));  //Tried using simple date format made things more complicated in later steps.
						valid = true;																			//Switched to LocalTime/LocalDate not entirely happy with how that worked out
					}																							//But was still easier when it came to splitting times and such.
					catch (Exception error)																		//(Thank God for Stack Overflow)
					{
						System.out.println("That is not a valid date, try again.");
					}
				}
				while(!valid); //The check for the do loop
				
				valid = false;
				do //Validates input for "Start Time"
				{
					System.out.print("Start Time > ");
					invoices[2] = userInput.nextLine();
					
					try
					{
						startTime = LocalTime.parse(invoices[2]);
						valid = true;
						
					}
					catch(Exception error)
					{
						System.out.println("That is not a valid time, try again.");
					}
				}
				while(!valid); //Belongs to Start Time do loop
				
				valid = false; //Resets validation boolean for "End Time"
				do
				{
					System.out.print("End Time > ");
					invoices[3] = userInput.nextLine();
					
					try
					{
						endTime = LocalTime.parse(invoices[3]);
						valid = true;
					}
					catch(Exception error)
					{
						System.out.println("That is not a valid time, try again.");
					}
				}
			while(!valid); //Belongs to "End Time" Do Loop
			
			System.out.println(); //Blank Line for formatting purposes
			
			System.out.println("Date Printed: " + datePrinted.format(DateTimeFormatter.ofPattern("MM/dd/yyyy"))); //Date Printed
			System.out.println("Plumber: " + invoices[0]); //Plumber
			
			System.out.println(); //Blank Line
			
			System.out.println(serviceDate.format(DateTimeFormatter.ofPattern("EEEE, MM/dd/yyyy"))); //Service Date
			System.out.println(startTime.format(DateTimeFormatter.ofPattern("h:mm a")) + " - " + endTime.format(DateTimeFormatter.ofPattern("h:mm a"))); //Service Time (This is why I'm not happy with LocalTime -_-
			System.out.println(); //Blank Line
			System.out.println("Charges:");
			System.out.print("Type\t\t"); //Went with escape characters as they're easier than formatters
			System.out.print("Rate\t\t");
			System.out.print("Qty\t\t");
			System.out.println("Amount");
			
			int totalHours = (endTime.getHour() - startTime.getHour()); //Its in 24hour format so this should work nicely
			LocalTime eveningHoursStart = LocalTime.of(17,00); //17:00 is 5pm
			double day24time = Integer.parseInt(startTime.format(DateTimeFormatter.ofPattern("HHmm"))); //Ohhh LocalTime
			double night24time = Integer.parseInt(endTime.format(DateTimeFormatter.ofPattern("HHmm")));
			day24time = Math.round(day24time / 100);
			night24time = Math.round(night24time / 100);
			
			DayOfWeek dow = serviceDate.getDayOfWeek(); //DayOfWeek turned out be its own dataType for some reason? 
			int dowInt = dow.getValue(); //Really wish I could've done this on one line
			if(startTime.isBefore(eveningHoursStart) && endTime.isAfter(eveningHoursStart)) //This is why I used LocalTime
			{
				System.out.print("Day\t\t"); 
				double dayHours = Math.abs(17 - day24time); //I would occasionally get negative values but the absolute value always turned out to be correct.
				
				if(dowInt >= 1 && dowInt <= 5) //1 = Monday, who every decided to change it from 2 needs to be burned at the stake.
				{
					amount[0] = Math.round(52 * dayHours); //I could've made 52 a variable but I didn't see the point at most it could save me 3 lines. At worse it could steal 2 hours from me.
					System.out.print("$52.00\t\t");
					System.out.print(dayHours + "\t\t");
					System.out.println(fmt.format(amount[0]) + "\t\t");
				}
				if(dowInt == 6)
				{
					amount[1] = Math.round(76 * dayHours); //Originally I was planning to have the program spit back previous entries but then I lost the sdf date/time array.
					System.out.print("$76.00\t\t");  //Not removing the array though it works about as good as two variables would have.
					System.out.print(dayHours + "\t\t");
					System.out.println(fmt.format(amount[1]) + "\t\t");
				}
				if(dowInt == 7)
				{
					amount[2] = Math.round(124 * dayHours); //I don't know why I rounded these, I don't see where I would get an actual double oh well couldn't hurt
					System.out.print("$124.00\t\t");
					System.out.print(dayHours + "\t\t");
					System.out.println(fmt.format(amount[2]) + "\t\t");
				}
			}
			if(startTime.isBefore(eveningHoursStart) && endTime.isBefore(eveningHoursStart))
			{
				System.out.print("Day\t\t");
				double dayHours = Math.abs((night24time - day24time));
				
				if(dowInt >= 1 && dowInt <= 5)
				{
					amount[0] = Math.round(52  * dayHours);
					System.out.print("$52.00\t\t");
					System.out.print(dayHours + "\t\t");
					System.out.println(fmt.format(amount[0]) + "\t\t");
				}
				if(dowInt == 6)
				{
					amount[1] = Math.round(76 * dayHours);
					System.out.print("$76.00\t\t");
					System.out.print(dayHours + "\t\t");
					System.out.println(fmt.format(amount[1]) + "\t\t");
				}
				if(dowInt == 7)
				{
					amount[2] = Math.round(124 * dayHours);
					System.out.print("$124.00\t\t");
					System.out.print(dayHours + "\t\t");
					System.out.println(fmt.format(amount[2]) + "\t\t");
				}
			}
			if(endTime.isAfter(eveningHoursStart))
			{
				double nightHours = Math.abs(night24time - 17);
				System.out.print("Evening\t\t");
				if(dowInt >= 1 && dowInt <= 5)
				{
					amount[3] = Math.round(((52 * 1.5) * nightHours));
					System.out.print("$" + 52*1.5 + "\t\t");
					System.out.print(nightHours + "\t\t");
					System.out.println(fmt.format(amount[3]) + "\t\t");
				}
				if(dowInt == 6)
				{
					amount[4] = Math.round(((76 * 1.5) * nightHours));
					System.out.print("$" + 76*1.5 + "\t\t");
					System.out.print(nightHours + "\t\t");
					System.out.println(fmt.format(amount[4]) + "\t\t");
				}
				if(dowInt == 7)
				{
					amount[5] = Math.round(((124 * 1.5) * nightHours));
					System.out.print("$" + 124*1.5 + "\t\t");
					System.out.print(nightHours + "\t\t");
					System.out.println(fmt.format(amount[5]) + "\t\t");
				}
			}
			if(endTime.isAfter(eveningHoursStart) && startTime.isAfter(eveningHoursStart))
			{
				double nightHours = Math.abs(night24time - day24time - 17);
				System.out.print("Evening\t\t");
				if(dowInt >= 1 && dowInt <= 5)
				{
					amount[6] = Math.round(((52 * 1.5) * nightHours));
					System.out.print("$" + 52*1.5 + "\t\t");
					System.out.print(nightHours + "\t\t");
					System.out.println(fmt.format(amount[3]) + "\t\t");
				}
				if(dowInt == 6)
				{
					amount[7] = Math.round(((76 * 1.5) * nightHours));
					System.out.print("$" + 76*1.5 + "\t\t");
					System.out.print(nightHours + "\t\t");
					System.out.println(fmt.format(amount[4]) + "\t\t");
				}
				if(dowInt == 7)
				{
					amount[8] = Math.round(((124 * 1.5) * nightHours));
					System.out.print("$" + 124*1.5 + "\t\t");
					System.out.print(nightHours + "\t\t");
					System.out.println(fmt.format(amount[5]) + "\t\t");
				}
			}
			System.out.println();
			System.out.print("Minimum 2 hours applies: ");
			double totalCharge = Math.abs(amount[0] + amount[1] + amount[2] + amount[3] + amount[4] + amount[5] + amount[6] + amount[7] + amount[8]); //Threw the abs in just in case
			if(totalHours < 2) //Pretty sure I only have to worry about 1
			{
				System.out.println("Yes");
				System.out.println();
				System.out.println("\t\t\t\tTotal Charge: " + fmt.format(Math.round(totalCharge * 2)));
				for(int i = 0; i < 10; i++)
				{
					amount[i] = 0;
				}
			}
			if(totalHours >= 2)
			{
				System.out.println("No");
				System.out.println("\t\t\t\t\tTotal Charge: " + fmt.format(Math.round(totalCharge)));
				for(int i = 0; i < 10; i++)
				{
					amount[i] = 0;
				}
				
			}
			System.out.print("Another Invoice (Y/N) > ");
			String answer = userInput.nextLine().trim().toLowerCase(); //cuts out spaces, and sets input to lowercase
			if(answer.equals("n"))
			{
				again = true; //sets the boolean so it won't start again
			}	
		}
		while(!again); //Thats the last loop
			
		userInput.close(); //Fine eclipse I'll close the scanner jeez.
	}
}
