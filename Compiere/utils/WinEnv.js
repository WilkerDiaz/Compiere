// Usage:   WinEnv COMPIERE_HOME JAVA_HOME
// Example: WinEnv C:\Compiere2 C:\Program Files\Java\jdk1.5.0_05
// Example: cscript WinEnv.js C:\Compiere2 C:\Program Files\Java\jdk1.5.0_05
//
// WinEnv.js - Set up Windows Environment

// $Id: WinEnv.js,v 1.1 2006/04/21 18:06:56 jjanke Exp $


// Get Objects
var Shell = new ActiveXObject("WScript.Shell");
var DesktopPath = Shell.SpecialFolders("Desktop");
var Args = WScript.Arguments;
var SysEnv = Shell.Environment("SYSTEM");

if (Args.length != 2)
{
  WScript.Echo("Usage: cscript WinEnv.js COMPIERE_HOME JAVA_HOME"
	+ "\nExample:\ncscript WinEnv.js C:\\Compiere2 \"C:\\Program Files\\Java\\jdk1.5.0_05\"");
  WScript.Quit (1);
}


// Set Environment Variables
var home = Args(0).replace("\"","");
SysEnv("COMPIERE_HOME") = home;
WScript.Echo ("SET COMPIERE_HOME="+ home);

home = Args(1).replace("\"","");
SysEnv("JAVA_HOME") = home;
WScript.Echo ("SET JAVA_HOME="+ home);


// Check that JAVA_HOME is in PATH
var pathString = SysEnv("PATH"); // Shell.ExpandEnvironmentStrings("%PATH%");
var index = pathString.indexOf(home);
if (index == -1)
{
  SysEnv("PATH") = home + "\\bin;" + pathString;
  var index_2 = SysEnv("PATH").indexOf(home);
  if (index_2 == -1)
    WScript.Echo ("Path NOT changed - run program as Administrator!");
  else
    WScript.Echo ("Path Changed = " + SysEnv("PATH"));
}
else
  WScript.Echo ("Path is OK = " + SysEnv("PATH"));


// Create Compiere2.exe Shortcut
// http://msdn.microsoft.com/library/default.asp?url=/library/en-us/script56/html/wsMthCreateShortcut.asp
var link = Shell.CreateShortcut(DesktopPath + "\\Compiere2.lnk");
link.TargetPath = Args(0) + "\\lib\\Compiere2.exe";
link.Arguments = "-debug";
link.Description = "Compiere Client";
link.IconLocation = Args(0) + "\\lib\\Compiere2.exe,0";
link.WorkingDirectory = Args(0);
link.WindowStyle = 3;
link.HotKey = "CTRL+ALT+SHIFT+C";
link.Save();
WScript.Echo ("Created Shortcut Compiere2.lnk");

// Create Web Site Shortcut
var urlLink = Shell.CreateShortcut(DesktopPath + "\\Compiere Web Site.url");
urlLink.TargetPath = "http://www.compiere.org";
urlLink.Save();
WScript.Echo ("Created Shortcut Compiere Web Site.url");

WScript.Echo ("Done");
