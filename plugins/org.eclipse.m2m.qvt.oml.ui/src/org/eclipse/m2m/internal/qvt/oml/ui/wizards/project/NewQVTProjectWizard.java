/*******************************************************************************
 * Copyright (c) 2007, 2020 Borland Software Corporation and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v2.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v20.html
 *
 * Contributors:
 *     Borland Software Corporation - initial API and implementation
 *     Christopher Gerking - bugs 319078, 536601, 562175
 *******************************************************************************/
package org.eclipse.m2m.internal.qvt.oml.ui.wizards.project;

import java.lang.reflect.InvocationTargetException;

import org.eclipse.core.resources.IContainer;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.core.runtime.IExecutableExtension;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.jface.dialogs.ErrorDialog;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.wizard.IWizardPage;
import org.eclipse.jface.wizard.Wizard;
import org.eclipse.jface.wizard.WizardSelectionPage;
import org.eclipse.m2m.internal.qvt.oml.ui.QVTUIPlugin;
import org.eclipse.m2m.internal.qvt.oml.ui.QvtPluginImages;
import org.eclipse.ui.INewWizard;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.actions.WorkspaceModifyOperation;
import org.eclipse.ui.wizards.newresource.BasicNewProjectResourceWizard;



public class NewQVTProjectWizard extends Wizard implements INewWizard, IExecutableExtension {

	private NewProjectData fProjectData;
	private NewQVTProjectCreationPage fMainPage;
	private NewQVTProjectContentPage fContentPage;
	private IConfigurationElement fConfig;
	private WizardSelectionPage fWizardSelectionPage;
	private boolean fIsFinishPerformed;
	protected IWorkbench fWorkbench;


	public NewQVTProjectWizard() {
	    setDefaultPageImageDescriptor(QvtPluginImages.getInstance().getImageDescriptor(QvtPluginImages.NEW_PROJECT_WIZARD));
		setWindowTitle(Messages.NewTransformationProjectWizard_Title);
	    setNeedsProgressMonitor(true);

	    fProjectData = new NewProjectData();
		fIsFinishPerformed = false;
	}

    public void init(IWorkbench workbench, IStructuredSelection selection) {
		fWorkbench = workbench;
    }

    @Override
	public void addPages() {

        super.addPages();

		fMainPage = new NewQVTProjectCreationPage("main", fProjectData); //$NON-NLS-1$
		fMainPage.setTitle(Messages.NewTransformationProject_Title);
		fMainPage.setDescription(Messages.NewTransformationProject_Description);
		addPage(fMainPage);

		fContentPage = new NewQVTProjectContentPage("page2", fMainPage, fProjectData); //$NON-NLS-1$
		fWizardSelectionPage = new QVTWizardListSelectionPage(getDestinationProvider());
		addPage(fContentPage);
		addPage(fWizardSelectionPage);
	}

    @Override
    public IWizardPage getNextPage(IWizardPage page) {
        IWizardPage nextPage = super.getNextPage(page);
        if (nextPage == fContentPage) {
            if (!fMainPage.isCreatePlugin()) {
                nextPage = fWizardSelectionPage;
            }
        }
        return nextPage;
    }

    @Override
    public IWizardPage getPreviousPage(IWizardPage page) {
        IWizardPage prevPage = super.getPreviousPage(page);
        if (prevPage == fContentPage) {
            if (!fMainPage.isCreatePlugin()) {
                prevPage = fMainPage;
            }
        }
        return prevPage;
    }

	@Override
	public boolean canFinish() {
		IWizardPage page = getContainer().getCurrentPage();
		return super.canFinish() && page != fMainPage ;
	}

	public void setInitializationData(IConfigurationElement config,
			String propertyName, Object data) {
		fConfig = config;
	}

	private WorkspaceModifyOperation createNewProjectOperation() {
		return new NewProjectCreationOperation(fMainPage.getProjectHandle(), fProjectData);
	}

	@Override
	public boolean performFinish() {
		if(fIsFinishPerformed) {
			return true;
		}

		try {
			fMainPage.updateData();
			fContentPage.updateData();

			if(fConfig != null) {
				BasicNewProjectResourceWizard.updatePerspective(fConfig);
			}

			if(!fProjectData.isCreateJava()) {
				// ensure no Activator class is requested to generate, in case
				// it has been previously selected in the new content page
				fProjectData.setDoGenerateClass(false);
			}

			getContainer().run(false, true, createNewProjectOperation());
		}
        catch (InterruptedException e) {
        	// operation canceled
        }
        catch(Exception e) {
        	QVTUIPlugin.log(e);
            String title = Messages.NewQVTProjectWizard_Error;
            String message = Messages.NewQVTProjectWizard_ErrorSeeLog;
            Throwable exc = (e instanceof InvocationTargetException) ? ((InvocationTargetException)e).getTargetException() : e;
            Status status = new Status(IStatus.ERROR, QVTUIPlugin.PLUGIN_ID, IStatus.ERROR, message, exc);
            ErrorDialog.openError(getShell(), title, e.getMessage(), status);
            return false;
        }

        fIsFinishPerformed = true;
        return true;
	}

    private INewQVTElementDestinationWizardDelegate getDestinationProvider() {
    	assert fMainPage != null;

    	return new INewQVTElementDestinationWizardDelegate() {
			public IProject getProjectHandle() {
				return fMainPage.getProjectHandle();
			}

			public IContainer getSourceContainer() {
				return fMainPage.getQVTSourceContainerHandle();
			}

			public boolean createDestination() {
				return performFinish();
			}
		};
    }
}
