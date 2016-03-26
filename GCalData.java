import java.io.*;
import java.net.*;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;


/**
 * @author Ofir Bibi
 * This Class uses the GData interface for the Google Calendar.
 * Using this class you can create a new event in your calendar.
 * In the future it might do more...
 */
public class GCalData {
	
	
	final static int MSMIN=60000;
	final static int MAXITER=10;

	/** Authorization Token */
	private static String _aToken;
	
	/**
	 * This method connects to the Google authentication server for a ClientLogin
	 * and gets a session long authentication token.
	 * @param userName your google username
	 * @param password your google password
	 * @return true if the key was retrieved successfully.
	 */
	private static boolean connectNAuthorize(String userName, String password){
		final String loginurlString = "https://www.google.com/accounts/ClientLogin";
		final String[] params = {"Email", "Passwd", "source", "service"};
		final String[] vals = {userName,password,"OPB-MatlabCal-1","cl"};
		try{
			// open url connection
			URL url = new URL( loginurlString );
			HttpURLConnection con = (HttpURLConnection) url.openConnection();
			// set up url connection to get retrieve information back
			con.setRequestMethod( "POST" );
			con.setDoOutput( true );
			con.setRequestProperty("content-type","application/x-www-form-urlencoded");
			PrintStream ps = new PrintStream(con.getOutputStream());
			String param, value;
			for (int i=0;i<params.length;++i){
				if (i > 0)
					ps.print("&");
				param = URLEncoder.encode(params[i], "UTF-8");
				value = URLEncoder.encode(vals[i], "UTF-8");
				ps.print(param+"="+value);
			}
			ps.close();
			// pull the information back from the URL
			InputStream is = con.getInputStream();
			StringBuffer buf = new StringBuffer();
			int c;
			while( ( c = is.read() ) != -1 ) {
				buf.append( (char) c );
			}
			con.disconnect();
			// get the Authorization Token
			_aToken="auth"+buf.substring(buf.lastIndexOf("Auth")+4,buf.length()-1);
		} catch (MalformedURLException e) {
			System.out.println("malformed");
			return false;
		} catch (IOException e) {
			System.out.println("IOexception");
			return false;
		}
		return true;
	}

	/** This method creates a new event in your calander with given parameters
	 * for content and reminders.
	 * @param userName your google username
	 * @param password your google password
	 * @param title The title of the event
	 * @param content The content of the event
	 * @param location The location of the event
	 * @param smsMe true if you want an sms reminder
	 * @param emailMe true if you want an email reminder
	 * @param DelayInMin gap in minutes from now to the time of the event. 
	 * @return 0 if the event was created, negative number for errors.
	 */
	public static int addEvent(String userName, String password,
			String title, String content, String location, 
			boolean smsMe, boolean emailMe, int DelayInMin) {
		final String eventpre="<entry xmlns='http://www.w3.org/2005/Atom'"+
		                    " xmlns:gd='http://schemas.google.com/g/2005'>"+
		                    "<category scheme='http://schemas.google.com/g/2005#kind'"+
		                  " term='http://schemas.google.com/g/2005#event'></category>"+
		                    "<title type='text'>"+title+"</title>"+
		                    "<content type='text'>"+content+"</content>"+
		                    "<gd:transparency"+
		                     " value='http://schemas.google.com/g/2005#event.opaque'>"+
		                    "</gd:transparency>"+
		                    "<gd:eventStatus"+
		                     " value='http://schemas.google.com/g/2005#event.confirmed'>"+
		                    "</gd:eventStatus>"+
		                    "<gd:where valueString='"+location+"'></gd:where>"+
		                    "<gd:when startTime='";
		final String eventmid="' endTime='";
		final String eventend="'>"+(smsMe?"<gd:reminder method='sms' minutes='0'/>":"")+
							(emailMe?"<gd:reminder method='email' minutes='0'/>":"")+
							"</gd:when></entry>";
		String posturlString = "http://www.google.com/calendar/feeds/"+userName+"/private/full";
		
		if (!connectNAuthorize(userName, password))
			return -1;
		boolean success=false;
		try{
			// try to create new event
			int safeguard=0;
			while (!success && safeguard<MAXITER){
				++safeguard;
				URL url = new URL( posturlString );
				HttpURLConnection con = (HttpURLConnection) url.openConnection();
				con.setInstanceFollowRedirects(false);
				// set up url connection to get retrieve information back
				con.setRequestMethod( "POST" );
				con.setDoOutput( true );
				con.setDoInput( true );
				con.setRequestProperty("content-type","application/atom+xml;charset=UTF-8");
				// stuff the Authorization request header
				con.setRequestProperty("Authorization","GoogleLogin "+_aToken);
				Date now = new Date();
				now.setTime(now.getTime()+MSMIN*(1+DelayInMin));
				DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
				String time=df.format(now);
				time=time.replace(' ', 'T');
				String event=eventpre+time+eventmid+time+eventend;
				PrintStream ps = new PrintStream(con.getOutputStream());
				ps.print(event);
				ps.close();
				// pull the information back from the URL
				InputStream is = con.getInputStream();
				if (con.getResponseCode()==302){
					posturlString = con.getHeaderField("Location");
					con.disconnect();
					continue;
				}
				con.disconnect();
				success=true;
			}
		} catch (MalformedURLException e) {
			return -3;
		} catch (IOException e) {
			return -2;
		}
		return success ? 0 : -4;
	}
}
