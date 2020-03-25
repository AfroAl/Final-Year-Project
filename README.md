# A Tool for Automated Security Refactoring
**Student Name:** Andrew Lambert\
**Student Number:** 16355706
## 
Hi! I'm your first Markdown file in **StackEdit**. If you want to learn about StackEdit, you can read me. If you want to play with Markdown, you can edit me. Once you have finished with me, you can create new files by opening the **file explorer** on the left corner of the navigation bar.


# Project Structure
This repository is structured as such:
 1. src/fyp_project ----> Beginning of the Source Code
 	1. detector ----> Files to handle matching & refactoring
 	
 		1. patterns ----> Hold all pattern classes & regex
		2. refactorings ----> Hold all refactoring classes
	2. plugin ----> Files for the plugin integration
		1. quickfix ----> Files to implement Quick Fixes to the vulnerabilities
 2. bin/fyp_project ----> Java classes
 
 3. Test Cases ----> Web applications to test against
 4. Examples ----> Examples of each vulnerability, what can be detected, and how it is vulnerability.

# How to Install

The plugin can be installed several ways:
1. Manually
	1. Locate the ../eclipse/dropins/ folder within your Eclipse installation location.
    	1. Windows - ../Eclipse/dropins
        2. Linux - ../eclipse/dropins
        3. Mac - ../Contents/Eclipse/dropins
    2. Extract the contents of the .jar file within the dropins/ folder.
    3. Restart Eclipse.
    
2. Eclipse Project Export
    1. Start Eclipse.
    2. Import the project within the .jar file into a workspace.
    3. Right-click on the project -> Export -> Plug-in Development -> Deployable plug-ins and fragments
    4. Toggle the option "Install into host" and click Finish.
    5. Restart Eclipse when prompted to.
    
3. Eclipse Update Site
    1. Start Eclipse.
    2. Click on Help -> Install New Software
    3. Click on the Add... button
    4. Give the repository a Name, and enter "ftp://35.246.68.175" as the Location. (Excluding the double quotes)
    5. Click the Add button.
    6. Activate the check mark next to the Uncategorized item.
    7. Click Next in the bottom-right of the window.
    8. Click Next again in the bottom-right of the window.
    9. Toggle the "I accept the terms of the license agreement" radio button.
    10. Click the Finish button.
    11. Click "Install anyway" in the Security Warning pop-up box.
    12. Restart Eclipse when prompted to.
    
4. Eclipse Marketplace
    1. Start Eclipse.
    2. Click on Help -> Eclsipe Marketplace
    3. Search for "16355706" in the Search Bar.
    4. Click on the Install button next to the plugin.
    5. Follow the preceding instructions in Eclipse.

# How to Use

This plugin will start automatically along with Eclipse. Each time an Editor Tab is changed, the plugin will run to detect any security vulnerabilities within the newly activated tab.

If a vulnerability is detected the IDE will display it as such:

![Example of Security Vulnerability](/images/vulnerability.png "Example of Security Vulnerability")

The vulnerability will be made known to the user via a yellow squiggle underneath the vulnerable line. There will also be a yellow marker on the right-hand side of the Editor window.

If you hover over the yellow marker, the type of vulnerability will be shown.

![Example of Marker](/images/marker.png "Example of Marker")

If you hover over the vulnerable line, you will receive some information on the type of vulnerability including Exploitability, Prevalence, Detectability, and Technial Impact.

![Example of Hover Text](/images/info.png "Example of Hover Text")

To fix a vulnerability, click on the vulnerable line and press Ctrl+1 to open the quick-fix menu. From here, a user can choose to fix the vulnerability. If they choose to fix it, a pop-up box will appear displaying the current vulnerable line, and what it will be changed to. Then a user can confirm or deny the change.

![Example of Refactor](/images/refactor.png "Example of Refactor")
