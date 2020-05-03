# A Tool for Automated Security Refactoring
**Student Name:** Andrew Lambert\
**Student Number:** 16355706
**Supervisor** Liliana Pasquale
## 
Security is vital to any individual and organisation that develop and use software. Security aims to protect valuable digital and physical assets from intentional harm caused by cyber (and also cyber-physical) attacks. Despite its importance, security is often considered as an “afterthought” and it is only addressed at the last stages of the software development process (e.g., during penetration testing) or after deployment. In this report, I present an approach to allow software security to be integrated early in the development lifecycle. The main objective is to allow software developers to detect security vulnerabilities during software development. Through the creation of a plugin for a developer’s Integrated Development Environment (IDE), software developers will be able to enable static analysis of source code to identify types and locations of security vulnerabilities. Developers will also receive suggestions about how to fix vulnerabilities through secure refactorings, where the vulnerability will be quashed but keeping the behaviour of the software the same. Developers will also be given the option to apply security refactorings automatically. The proposed plugin is applied to web applications and uses vulnerabilities described in the OWASP Top 10. To evaluate this approach, existing web applications having known security vulnerabilities will be adopted. The evaluation will assess precision and recall of the approach proposed to identify vulnerabilities and effectiveness of the suggested security refactorings.

# Project Structure
This repository is structured as such:
 1. src/fyp_project ----> Beginning of the Source Code
 	1. detector ----> Files to handle matching & refactoring
 	
 		1. patterns ----> Hold all pattern classes & regex
		2. refactorings ----> Hold all refactoring classes
	2. plugin ----> Files for the plugin integration
		1. quickfix ----> Files to implement Quick Fixes to the vulnerabilities
 2. bin/fyp_project ----> Java classes
 3. Examples ----> Examples of each vulnerability, what can be detected, and how it is vulnerable.

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
    6. Activate the check mark next to the FYP Project item.
    7. Click Next in the bottom-right of the window.
    8. Click Next again in the bottom-right of the window.
    9. Toggle the "I accept the terms of the license agreement" radio button.
    10. Click the Finish button.
    11. Click "Install anyway" in the Security Warning pop-up box.
    12. Restart Eclipse when prompted to.

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
