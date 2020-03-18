package fyp_project.plugin;
import java.util.Iterator;
import org.eclipse.core.resources.IMarker;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.jdt.ui.text.java.hover.IJavaEditorTextHover;
import org.eclipse.jface.text.IRegion;
import org.eclipse.jface.text.ITextViewer;
import org.eclipse.jface.text.Position;
import org.eclipse.jface.text.source.Annotation;
import org.eclipse.jface.text.source.IAnnotationModel;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.texteditor.ITextEditor;
import org.eclipse.ui.texteditor.SimpleMarkerAnnotation;

public class HoverText implements IJavaEditorTextHover
{
	IEditorPart editor;
    @Override
    public String getHoverInfo(ITextViewer textviewer, IRegion region)
    {
    	IAnnotationModel iamf = ((ITextEditor)editor).getDocumentProvider().getAnnotationModel(editor.getEditorInput());
    	iamf.connect(EditorListener.doc);
    	Iterator<Annotation> ann =  iamf.getAnnotationIterator();
    	
	    while(ann.hasNext()) {
	    	try {
	    		Annotation a = (Annotation)ann.next();
		    	if(a.getType().equals("my.annotationType")) {
		    		SimpleMarkerAnnotation sma = (SimpleMarkerAnnotation) a;
		    		if(sma.getMarker().getType().equals("my.marker") ) {
						Position p = iamf.getPosition(sma);
						System.out.println(region.getOffset() + "<= " + (p.getOffset() + p.getLength()));
						System.out.println(region.getOffset() + " >= " + p.getOffset());
						if(region.getOffset() >= p.getOffset() && region.getOffset() <= (p.getOffset() + p.getLength())) {
							System.out.println("YAY");
							return (String) sma.getMarker().getAttribute(IMarker.TEXT);
						}
		    		}
		    	}
			} catch (CoreException e) {
				// TODO Auto-generated catch block
			}
	    }
		return null;
    }

	@Override
	public IRegion getHoverRegion(ITextViewer arg0, int arg1) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void setEditor(IEditorPart arg0) {
		editor = arg0;
	}
}