package fyp_project.plugin.handlers;

import org.eclipse.ui.IStartup;

public class StartupHandler implements IStartup{

	@Override
	public void earlyStartup() {
		fyp_project.plugin.handlers.SampleHandler.start();
	}

}