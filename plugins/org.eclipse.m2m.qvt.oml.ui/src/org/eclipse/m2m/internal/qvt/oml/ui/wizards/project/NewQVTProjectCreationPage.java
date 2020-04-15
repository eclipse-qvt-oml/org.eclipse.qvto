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
 *     Christopher Gerking - bugs 319078, 562175
 *******************************************************************************/
package org.eclipse.m2m.internal.qvt.oml.ui.wizards.project;

import java.io.File;
import java.io.IOException;

import org.eclipse.core.resources.IContainer;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Path;
import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.dialogs.IMessageProvider;
import org.eclipse.jface.wizard.IWizardPage;
import org.eclipse.m2m.internal.qvt.oml.ui.QVTUIPlugin;
import org.eclipse.osgi.util.NLS;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.dialogs.WizardNewProjectCreationPage;


public class NewQVTProjectCreationPage extends WizardNewProjectCreationPage {

    private NewProjectData fCreationData;
    private Text fSourceText;
    private Text fOutputText;
    private Button fSimpleProject;
    private Button fSimplePlugin;
    private Button fJavaPlugin;    
    private Group fJavaSettingsGroup;
    private Label fSourceLabel;
    private Label fOutputLabel;	
    
    private Label fQvtSourceContainerLabel;
    private Text fQvtSourceContainerText;    
	
	public NewQVTProjectCreationPage(String pageName, NewProjectData data){
		super(pageName);
		fCreationData = data;
	}
		
    @Override
	protected boolean validatePage() {
    	boolean result = super.validatePage();
    	
    	if(result && getProjectHandle() != null) {
    		IPath wsLocation = getProjectHandle().getWorkspace().getRoot().getLocation();
    		if(wsLocation != null) {
    			IPath projectLocation = wsLocation.append(getProjectHandle().getName());
        		File projectFile = Path.fromOSString(projectLocation.toOSString()).toFile();
        		if(projectFile.exists()) {        			
            		try {
						projectFile = projectFile.getCanonicalFile();
					} catch (IOException e) {
						QVTUIPlugin.log(e);
					}        			
        			String errMessage = NLS.bind(Messages.NewQVTProjectCreationPage_projectAlreadyExists, projectFile.getName());
        			setErrorMessage(errMessage);
        			return false;
        		}        		
    		}
    	}
    	if(result) {
    		if(!validateQvtSourceContainer()) {
	    		return false;
    		}
    	}
    	
    	return result;
    }
    
    protected boolean validateQvtSourceContainer() {    	
    	IStatus status = SourceContainerUpdater.validate(getQVTSourceContainerValue());    	    	
    	
    	if(!status.isOK()) {
        	int type = IMessageProvider.NONE;    		
    		switch (status.getSeverity()) {
			case IStatus.INFO:
				type = IMessageProvider.INFORMATION;
				break;
			case IStatus.WARNING:
				type = IMessageProvider.WARNING;
				break;
			case IStatus.ERROR:
				type = IMessageProvider.ERROR;				
				break;
			}
    		
    		setMessage(status.getMessage(), type);
    		return status.getSeverity() <= IStatus.INFO;
    	}
    	
    	return true;
    }
    
    protected String getQVTSourceContainerValue() {
		return fQvtSourceContainerText != null ? fQvtSourceContainerText.getText() : null;
	}

    /**
	 * Get the container for QVT sources
	 * 
	 * @return project container or folder under the project hierarchy.
	 */
    public IContainer getQVTSourceContainerHandle() {
    	String value = getQVTSourceContainerValue();
    	if(value == null || value.length() == 0) {
    		return getProjectHandle();
    	}
    	return getProjectHandle().getFolder(value);
    }
    
	@Override
	public void createControl(Composite parent) {
		super.createControl(parent);
		Composite control = (Composite)getControl();
		GridLayout layout = new GridLayout();		
		control.setLayout(layout);

		createQVTSourceContainer(control);		
		createProjectTypeGroup(control);
		
		Dialog.applyDialogFont(control);
		
		setControl(control);
        PlatformUI.getWorkbench().getHelpSystem().setHelp(control, 
			"org.eclipse.m2m.qvt.oml.transformation_project"); //$NON-NLS-1$
	}	
	
	private void createQVTSourceContainer(Composite parent) {
		Composite composite = new Composite(parent, SWT.NONE);
		GridLayout layout = new GridLayout();
		layout.numColumns = 2;
		composite.setLayout(layout);
		GridData gd = new GridData(GridData.FILL_HORIZONTAL);
		gd.verticalIndent = 15;
		composite.setLayoutData(gd);
		
		//source container
		String tooltipText = Messages.NewQVTProjectCreationPage_SourceContainerTooltip;
		fQvtSourceContainerLabel = new Label(composite, SWT.NONE);
		fQvtSourceContainerLabel.setText(Messages.NewQVTProjectCreationPage_SourceContainerLabel);
		fQvtSourceContainerLabel.setToolTipText(tooltipText);

		fQvtSourceContainerText = createText(composite, Messages.NewQVTProjectCreationPage_defaultSourceFolder);
		fQvtSourceContainerText.setToolTipText(tooltipText);
	}
	
	private void createProjectTypeGroup(Composite parent) {
		Composite container = new Composite(parent, SWT.Move);
		container.setLayout(new GridLayout());
		container.setLayoutData(new GridData(GridData.FILL_HORIZONTAL | GridData.FILL_VERTICAL));
		
		Group projectTypeGroup = new Group(container, SWT.RADIO);
		projectTypeGroup.setText(Messages.ProjectStructurePage_createProjectType);
		
		GridLayout projectTypeLayout = new GridLayout();
		projectTypeLayout.numColumns = 1;

		projectTypeGroup.setLayout(projectTypeLayout);
		projectTypeGroup.setLayoutData(new GridData(GridData.FILL_HORIZONTAL | GridData.FILL_VERTICAL));

		GridData gd = new GridData(GridData.FILL_HORIZONTAL);
		gd.verticalIndent = 5;
		
		fSimpleProject = new Button(projectTypeGroup, SWT.RADIO);
		fSimpleProject.setText(Messages.ProjectStructurePage_createSimpleProject);
		fSimpleProject.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		
		fSimplePlugin = new Button(projectTypeGroup, SWT.RADIO);
		fSimplePlugin.setText(Messages.ProjectStructurePage_createSimplePluginProject);
		fSimplePlugin.setLayoutData(gd);
		
		// select simple plugin type by default
		if (ProjectIntegrationHelper.isProjectIntegration()) {
			fSimplePlugin.setSelection(true);
		}
		else {
			fSimpleProject.setSelection(true);
			fSimplePlugin.setEnabled(false);
		}
		
		fJavaPlugin = new Button(projectTypeGroup, SWT.RADIO);		
        fJavaPlugin.setText(Messages.ProjectStructurePage_createJavaPluginProject);
        fJavaPlugin.addSelectionListener(new SelectionAdapter() {
            @Override
            public void widgetSelected(SelectionEvent e) {
                updateControls();
            }
        });
        
        fJavaPlugin.setLayoutData(gd);
        
        fJavaPlugin.setEnabled(ProjectIntegrationHelper.isProjectIntegration());
        
        fJavaSettingsGroup = new Group(projectTypeGroup, SWT.NONE);
        fJavaSettingsGroup.setText(Messages.ProjectStructurePage_JavaProjectSettings); 
		GridLayout layout = new GridLayout();		
		layout.numColumns = 2;
		fJavaSettingsGroup.setLayout(layout);
		fJavaSettingsGroup.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));

        fSourceLabel = createLabel(fJavaSettingsGroup, Messages.ProjectStructurePage_source);
        fSourceText = createText(fJavaSettingsGroup, "src"); //$NON-NLS-1$	  
		fSourceText.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		
		fOutputLabel = createLabel(fJavaSettingsGroup, Messages.ProjectStructurePage_output);
        fOutputText = createText(fJavaSettingsGroup, "bin"); //$NON-NLS-1$		        
		fOutputLabel.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		
        updateControls();
		
	}
	
    private void updateControls() {
        fCreationData.setPlugin(fJavaPlugin.getSelection());
        fCreationData.setCreateJava(fJavaPlugin.getSelection());
        
        fSourceLabel.setEnabled(fJavaPlugin.getSelection());
        fSourceText.setEnabled(fJavaPlugin.getSelection());
        fOutputLabel.setEnabled(fJavaPlugin.getSelection());
        fOutputText.setEnabled(fJavaPlugin.getSelection());        
        fJavaSettingsGroup.setEnabled(fJavaPlugin.getSelection());
    }

    private Label createLabel(Composite container, String text) {
		Label label = new Label(container, SWT.NONE);
		label.setText(text);
		GridData gd = new GridData();
		label.setLayoutData(gd);
		return label;
	}
	
	private Text createText(Composite container, String txtValue) {
		Text text = new Text(container, SWT.BORDER | SWT.SINGLE);
		GridData gd = new GridData(GridData.FILL_HORIZONTAL);
		gd.widthHint = 300;
		text.setLayoutData(gd);
		if(txtValue != null) {
			text.setText(txtValue);			
		}
		
		text.addModifyListener(new ModifyListener() {
			public void modifyText(ModifyEvent e) {
				validatePage();
			}
		});
		return text;
	}

	@Override
	public IWizardPage getNextPage() {
		updateData();
		return super.getNextPage();
	}
	
	public void updateData() {
		fCreationData.setPlugin(!fSimpleProject.getSelection());		
		fCreationData.setName(getProjectName());
		fCreationData.setQVTSourceFolderName(getQVTSourceContainerValue());
		fCreationData.setCreateJava(fJavaPlugin.getSelection());
		fCreationData.setLocation(getLocationPath());

		if(fJavaPlugin.getSelection()) {
			fCreationData.setSourceFolderName(fSourceText.getText().trim());
			fCreationData.setOutFolderName(fOutputText.getText().trim());
		} else {
			fCreationData.setSourceFolderName(null);
			fCreationData.setOutFolderName(null);			
		}
	}
    
	/**
	 * Indicates whether the project to be created is a plugin project.
	 */
    public boolean isCreatePlugin() {
        return fSimpleProject != null && !fSimpleProject.getSelection();
    }
    
	/**
	 * Indicates whether the project to be created is a Java plugin project.
	 */
    public boolean isJavaPlugin() {
        return fJavaPlugin != null && !fJavaPlugin.getSelection();
    }
}
