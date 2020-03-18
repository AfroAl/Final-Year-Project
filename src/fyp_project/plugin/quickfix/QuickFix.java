package fyp_project.plugin.quickfix;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;

import org.eclipse.core.resources.IContainer;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IMarker;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.ui.IMarkerResolution;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PlatformUI;

import fyp_project.detector.SecurityRefactorer;
import fyp_project.plugin.EditorListener;

public class QuickFix implements IMarkerResolution{

	ArrayList<IFile> files = new ArrayList<>();
	ArrayList<String> lines = new ArrayList<>();
	private static HashMap<String, String> deletions = new HashMap<>();
	String label;
	QuickFix(String label) {
		this.label = label;
	}
	
	@Override
	public String getLabel() {
		return label;
	}

	@Override
	public void run(IMarker arg0) {
		IProject[] proj = ResourcesPlugin.getWorkspace().getRoot().getProjects();
		
		for(IProject p : proj) {
			try {
				processContainer(p);
			} catch (CoreException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		for(IFile f : files) {
			try {
				if(f.getRawLocation().toOSString().equals(arg0.getAttribute(IMarker.LOCATION))) {
					try {
						String refactorLine = null;
						BufferedReader reader = new BufferedReader(new InputStreamReader(f.getContents(true), f.getCharset()));
						String line = reader.readLine();
						int count = 1;
						while(line != null) {
							if(arg0.getAttribute(IMarker.LINE_NUMBER).equals(String.valueOf(count))) {
								refactorLine = line;
							}
							count++;
							lines.add(line);
							line = reader.readLine();
						}
						
						SecurityRefactorer refactorer = new SecurityRefactorer(lines);
						String r;
						r = refactorer.refactor((String)arg0.getAttribute(IMarker.MESSAGE), refactorLine);
						
						deletions = refactorer.getDeletions();
						String newString = EditorListener.doc.get();
						newString = newString.replace(refactorLine, r);
						
						IWorkbenchWindow window = PlatformUI.getWorkbench().getActiveWorkbenchWindow();
						boolean confirm = MessageDialog.openConfirm(window.getShell(), "Refactor", 
								"Would you like to refactor the code?" + System.lineSeparator() + System.lineSeparator() + 
								"Current: " + System.lineSeparator() + refactorLine + System.lineSeparator() + System.lineSeparator() + 
								"New: " + System.lineSeparator() + r);
						
						if(!confirm) { return; }
						
						FileWriter writer;
	                    writer = new FileWriter(f.getRawLocation().toOSString());
	                    writer.append(newString);
	                    writer.flush();
	                    writer.close();
	                    
						delete(f.getRawLocation().toOSString());
						lines.clear();
						deletions.clear();
					} catch (UnsupportedEncodingException | CoreException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			} catch (CoreException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	
	void processContainer(IContainer container) throws CoreException
	{
	   IResource [] members = container.members();
	   for (IResource member : members)
	    {
	       if (member instanceof IContainer)
	         processContainer((IContainer)member);
	       else if (member instanceof IFile) {
	    	   if(member.getFileExtension() != null && member.getFileExtension().equals("java")) {
	    		   files.add((IFile)member);
	    	   }
	       }
	    }
	}
	
	void delete(String file) {
		ArrayList<String> lines = new ArrayList<>();
		
		StringBuilder fileContents = new StringBuilder();
        try {
            BufferedReader br = new BufferedReader(new FileReader(file));
            String line = br.readLine();
            while(line != null) {
                lines.add(line);
                line = br.readLine();
            }

            for(int j=0; j<lines.size(); j++) {
                for(String key : deletions.keySet()) {
                    if(!deletions.get(key).equals("")) {
                        if(lines.get(j).contains(deletions.get(key))) {
                            lines.remove(j);
                        }
                    }
                }
            }

            for(String l : lines) {
                fileContents.append(l).append(System.lineSeparator());
            }

            //EditorListener.doc.set(fileContents.toString());
			
			FileWriter writer;
            writer = new FileWriter(file);
            writer.append(fileContents.toString());
            writer.flush();
            writer.close();
            br.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
	}

}
