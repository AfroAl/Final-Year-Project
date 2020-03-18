package fyp_project.plugin;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Iterator;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IMarker;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.jface.text.BadLocationException;
import org.eclipse.jface.text.IDocument;
import org.eclipse.jface.text.Position;
import org.eclipse.jface.text.source.Annotation;
import org.eclipse.jface.text.source.IAnnotationModel;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IPartListener2;
import org.eclipse.ui.IWorkbenchPartReference;
import org.eclipse.ui.part.FileEditorInput;
import org.eclipse.ui.texteditor.ITextEditor;
import org.eclipse.ui.texteditor.SimpleMarkerAnnotation;

import fyp_project.detector.SecurityMatcher;

public class EditorListener implements IPartListener2{
	public static IDocument doc;
	private ArrayList<IFile> files = new ArrayList<>();
	

	public EditorListener(ArrayList<IFile> files) {
		this.files = files;
	}

	@Override
	public void partActivated(IWorkbenchPartReference arg0) {
		System.out.println("HELLO");
		int numberOfPatterns = 10;
		boolean skip = false;
		ArrayList<String> lines = new ArrayList<>();
		String[] patternResults;
		
		IEditorPart editor = arg0.getPage().getActiveEditor();
		ITextEditor teditor = (ITextEditor) editor;
		IEditorInput input = teditor.getEditorInput();
		doc = teditor.getDocumentProvider().getDocument(input);
		
		for(IFile f : files) {
			if(f.getRawLocation().toOSString().contains("Test")) {
				
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
							// TODO Auto-generated catch block
							e.printStackTrace();
						} catch (IOException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						
						SecurityMatcher securityMatcher = new SecurityMatcher(skip);
						patternResults = securityMatcher.patternMatch(lines, i, 6);
						
						// Skip this pattern
	                    if ((patternResults[0] == null) || (patternResults[1] == null)) {
	                        lines.clear();
	                        //deletions.clear();
	                        skip = false;
	                        continue;
	                    } else {
	                        //i--; // Check the file again for other matches
	                        skip = true;
	                    }
						
						try {					    
							try {
								f.deleteMarkers("my.marker", false, IResource.DEPTH_ZERO);
							} catch (CoreException e1) {
								// TODO Auto-generated catch block
								e1.printStackTrace();
							}
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
								offset = doc.getLineInformation(Integer.parseInt(patternResults[2])-1).getOffset();
								length = doc.getLineInformation(Integer.parseInt(patternResults[2])-1).getLength();
							} catch (BadLocationException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
						    
						    try {
								while(doc.getChar(offset) == ' ') {
									offset++;
									length--;
								}
								
								while(doc.getChar(offset) == '\t') {
									offset++;
									length--;
								}
							} catch (BadLocationException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
						    
						    iamf.connect(doc);
						    
						    Iterator<Annotation> ann =  iamf.getAnnotationIterator();
						    while(ann.hasNext()) {
						    	Annotation a = ann.next();
						    	if(a.getText() == null) {
						    		iamf.removeAnnotation(a);
						    	}
						    }
						    iamf.addAnnotation(ma, new Position(offset, length));
						   
						    iamf.disconnect(doc);
								
						} catch (CoreException e) {
						}
					}
				}
			}
		}
	}

	@Override
	public void partBroughtToTop(IWorkbenchPartReference arg0) {}

	@Override
	public void partClosed(IWorkbenchPartReference arg0) {}

	@Override
	public void partDeactivated(IWorkbenchPartReference arg0) {}

	@Override
	public void partHidden(IWorkbenchPartReference arg0) {}

	@Override
	public void partInputChanged(IWorkbenchPartReference arg0) {}

	@Override
	public void partOpened(IWorkbenchPartReference arg0) {}

	@Override
	public void partVisible(IWorkbenchPartReference arg0) {}

}
