# google_calendar_matlab
A Java based Matlab interface for creating Events and notifications on google calendar.

This is a copy of the source files for the Matlab Central entry here:
https://www.mathworks.com/matlabcentral/fileexchange/25698-create-google-calendar-event-with-sms-and-email-notification

The code is dated back to 2011 and is not intended on being updated.
Fork and use as you see fit.

# How To use GCalData (and why)

## Why use this thing anyways ?!?

Allot of us run long scripts in matlab that can take hours or days.
When running such scripts this tool allows you to "forget" about the run
and get a notice when it's over by adding a simple line at the end of your script.
While there are available solutions to send yourself an email from MATLAB or even
send an SMS through an email service, both options are not as good as this one.
With this little tool you can create an event that holds allot of information
including a time stamp, you can get an email notification with all that information
and ALSO an SMS notification. All this is completely FREE and depending only on
haveing a google account. So now you are not depending on your cell phone operator
to provide an Email to SMS service or anything like that.

## Technical Issues
**IMPORTANT**
Since MATLAB 2009a, there is a problem with the internal HTTPS java libraries used in MATLAB.
If you're having problems with this tool (and others maybe) the current hack is to comment out 
these libraries from the classpath.txt file so that MATLAB will use the regular JRE/JDK libraries.
A short manual to doing this:
1. find your classpath.txt file (should be "$MATLABDIR\toolbox\local\classpath.txt")
2. comment out (##) the line with the path to the https libraries ($matlabroot/java/jarext/ice/ib6https.jar)
3. restart MATLAB, since the static java path is only read on startup and superceeds the dynamic path

in case of problems, you can always revert by uncommenting the line.
Also for this tool to work you need the jvm (Java Virtual Machine) to work.

## Using the tool

### Command line tool
compile both java files and use the GCalEventor to see usage

### standalone M file
use just like any other m file. runs a bit slower than the class assisted option

### Class use in MATLAB
1. Compile the java file using "javac GCalData.java".
2. Put the created class file in some local directory.
3. Add the path to the class file to the MATLAB java class path using the javaaddpath function.
4. Import the class in an m file using "import GCalData"
5. use the function(s) in the file by the definitions in the Javadoc.

Enjoy this tool, maybe develop it and share with me as well.
