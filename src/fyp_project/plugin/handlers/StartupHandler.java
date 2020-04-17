package fyp_project.plugin.handlers;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Iterator;

import org.eclipse.core.resources.IContainer;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IMarker;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.jface.text.BadLocationException;
import org.eclipse.jface.text.Position;
import org.eclipse.jface.text.source.Annotation;
import org.eclipse.jface.text.source.IAnnotationModel;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IPartService;
import org.eclipse.ui.IStartup;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.part.FileEditorInput;
import org.eclipse.ui.texteditor.ITextEditor;
import org.eclipse.ui.texteditor.SimpleMarkerAnnotation;

import fyp_project.detector.SecurityMatcher;
import fyp_project.plugin.EditorListener;

public class StartupHandler implements IStartup{

	@Override
	public void earlyStartup() {
		start();
	}
	
	static ArrayList<IFile> files = new ArrayList<>();
	public static void start() {
	IProject[] proj = ResourcesPlugin.getWorkspace().getRoot().getProjects();
		
		for(IProject p : proj) {
			try {
				processContainer(p);
			} catch (CoreException e) {
				e.printStackTrace();
			}
		}
		
		Display.getDefault().asyncExec(new Runnable() {
		    @Override
		    public void run() {
		    	IWorkbench workB = PlatformUI.getWorkbench();
				IWorkbenchWindow workBWindow = workB.getActiveWorkbenchWindow();
				IPartService part = workBWindow.getPartService();
				part.addPartListener(new EditorListener(files));
				firstRun();
		    }
		});
	}
	
	static void firstRun() {
		if(!(PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage().getActiveEditor() instanceof ITextEditor)) {
			return;
		}
		int numberOfPatterns = 11;
		ArrayList<String> lines = new ArrayList<>();
		String[] patternResults;
		
		IEditorPart editor = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage().getActiveEditor();
		ITextEditor teditor = (ITextEditor) editor;
		IEditorInput input = teditor.getEditorInput();
		EditorListener.doc = teditor.getDocumentProvider().getDocument(input);
		
		for(IFile f : files) {
			if(f.getFullPath().equals(((FileEditorInput)teditor.getEditorInput()).getFile().getFullPath())) {
				for(int i=0; i<numberOfPatterns; i++) {
					try {
						BufferedReader reader = new BufferedReader(new InputStreamReader(f.getContents(true), f.getCharset()));
						String line = reader.readLine();
						while(line != null) {
							lines.add(line);
							line = reader.readLine();
						}
					} catch (UnsupportedEncodingException | CoreException e) {
						e.printStackTrace();
					} catch (IOException e) {
						e.printStackTrace();
					}
					
					int lineNumber = 0;
					for(String l : lines) {
						SecurityMatcher securityMatcher = new SecurityMatcher(lines);
						patternResults = securityMatcher.patternMatch(l, lineNumber, i, 6);
						lineNumber++;
						
						// Skip this pattern
	                    if ((patternResults[0] == null) || (patternResults[1] == null)) {
	                        continue;
	                    }
						
						try {					    
							
							IMarker m = f.createMarker("my.marker");
							m.setAttribute(IMarker.SOURCE_ID, "my.marker");
							m.setAttribute(IMarker.LINE_NUMBER, patternResults[2]);
							m.setAttribute(IMarker.MESSAGE, patternResults[0]);
							m.setAttribute(IMarker.SEVERITY, IMarker.SEVERITY_WARNING);
							m.setAttribute(IMarker.LOCATION, f.getRawLocation().toOSString());
							m.setAttribute(IMarker.TEXT, patternResults[3]);
							IAnnotationModel iamf = teditor.getDocumentProvider().getAnnotationModel(editor.getEditorInput());
						    SimpleMarkerAnnotation ma = new SimpleMarkerAnnotation("my.annotationType", m);
							    
						    int offset = 0;
						    int length= 0;
						    try {
								offset = EditorListener.doc.getLineInformation(Integer.parseInt(patternResults[2])-1).getOffset();
								length = EditorListener.doc.getLineInformation(Integer.parseInt(patternResults[2])-1).getLength();
							} catch (BadLocationException e) {
								e.printStackTrace();
							}
						    
						    try {
								while(EditorListener.doc.getChar(offset) == ' ') {
									offset++;
									length--;
								}
								
								while(EditorListener.doc.getChar(offset) == '\t') {
									offset++;
									length--;
								}
							} catch (BadLocationException e) {
								e.printStackTrace();
							}
						    
						    iamf.connect(EditorListener.doc);
						    boolean exists = false;
						    Iterator<Annotation> ann =  iamf.getAnnotationIterator();
						    while(ann.hasNext()) {
						    	Annotation a = ann.next();
						    	if(iamf.getPosition(a) == new Position(offset, length) && a.getType().equals("my.annotationType")) {
						    		exists = true;
						    		break;
						    	}
						    }
						    
						    if(!exists) {
						    	iamf.addAnnotation(ma, new Position(offset, length));
						    }
						   
						    iamf.disconnect(EditorListener.doc);
								
						} catch (CoreException e) {
						}
					}
					
					lines.clear();
				}
			}
		}
	}
	
	static void processContainer(IContainer container) throws CoreException
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

}