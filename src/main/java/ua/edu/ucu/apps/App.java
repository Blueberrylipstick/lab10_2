package ua.edu.ucu.apps;

/**
 * Hello world!
 *
 */
public class App 
{
    public static void main( String[] args ) {
        SmartDocument sd = new SmartDocument("Screenshot 2023-11-27 at 6.42.00 PM.png");
        System.out.println(sd.parse());
    }
}
