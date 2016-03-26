
public class GCalEventor {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		//simple check for arguments
		if ( args.length!=6){
			System.out.println("usage: GCalEventor Username Password Title Content Location DelayInMinutes");
			System.exit(-1);
		}
		// Set up the URL and the object that will handle the connection:
		int ret = GCalData.addEvent(args[0], args[1], args[2], args[3], args[4], true, true, Integer.parseInt(args[5]));
		System.out.println(ret);
	}

}
