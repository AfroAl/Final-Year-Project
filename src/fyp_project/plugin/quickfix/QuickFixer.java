package fyp_project.plugin.quickfix;

import org.eclipse.core.resources.IMarker;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.ui.IMarkerResolution;
import org.eclipse.ui.IMarkerResolutionGenerator;

public class QuickFixer implements IMarkerResolutionGenerator {

	@Override
	public IMarkerResolution[] getResolutions(IMarker arg0) {
		try {
			return new IMarkerResolution[] { new QuickFix("Fix for " + arg0.getAttribute(IMarker.MESSAGE)) };
		} catch (CoreException e) {
			// TODO Auto-generated catch block
			return new IMarkerResolution[0];
		}
	}

}
