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
 *******************************************************************************/
package org.eclipse.m2m.internal.tests.qvt.oml.ui.completion;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import java.util.regex.Pattern;

import org.eclipse.core.resources.IContainer;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.core.runtime.Path;
import org.eclipse.core.runtime.Platform;
import org.eclipse.jface.text.IDocument;
import org.eclipse.jface.text.ITextViewer;
import org.eclipse.jface.text.contentassist.ICompletionProposal;
import org.eclipse.jface.text.contentassist.IContentAssistant;
import org.eclipse.jface.text.source.ISourceViewer;
import org.eclipse.m2m.internal.qvt.oml.QvtPlugin;
import org.eclipse.m2m.internal.qvt.oml.common.io.FileUtil;
import org.eclipse.m2m.internal.qvt.oml.editor.ui.QvtConfiguration;
import org.eclipse.m2m.internal.qvt.oml.editor.ui.QvtEditor;
import org.eclipse.m2m.internal.qvt.oml.editor.ui.completion.QvtCompletionProcessor;
import org.eclipse.m2m.internal.qvt.oml.editor.ui.completion.QvtCompletionProposal;
import org.eclipse.m2m.internal.qvt.oml.project.builder.QVTOBuilder;
import org.eclipse.m2m.tests.qvt.oml.TestProject;
import org.eclipse.m2m.tests.qvt.oml.util.ReaderInputStream;
import org.eclipse.m2m.tests.qvt.oml.util.TestUtil;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IPluginContribution;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.activities.IActivity;
import org.eclipse.ui.activities.IActivityManager;
import org.eclipse.ui.activities.IActivityPatternBinding;
import org.eclipse.ui.activities.WorkbenchActivityHelper;
import org.eclipse.ui.ide.IDE;
import org.osgi.framework.Bundle;


/**
 * @author vrepeshko
 */
public class CompletionTest extends AbstractCompletionTest {

	private static final String EXTENSION_POINT = "javaBlackboxUnits";	//$NON-NLS-1$ -- clone of BundleBlackboxProvider
	private static final String UNIT_ELEM = "unit";						//$NON-NLS-1$ -- clone of BundleBlackboxProvider

	static {
		enableQVTOCapabilities();
	}

	public CompletionTest(final String folder) {
		super(folder);
		myFolder = folder;

		IConfigurationElement[] configurationElements = Platform.getExtensionRegistry().getConfigurationElementsFor(QvtPlugin.ID, EXTENSION_POINT);
		for (IConfigurationElement configurationElement : configurationElements) {
			if (UNIT_ELEM.equals(configurationElement.getName())) {
				String contributingBundleId = configurationElement.getContributor().getName();
				if (!contributingBundleId.startsWith("org.eclipse.m2m")) {
					fail("Completion tests are not supported for " + QvtPlugin.ID + "." + EXTENSION_POINT + " in " + contributingBundleId + " - close project");
				}
			}
		}
	}

	@Override
	protected void setUp() throws Exception {
		if (myTestProject == null) {
			initializeWorkspace();
			initializeProject();
		}

		createTransformation();

		initializeProposalProvider();

		loadExpectedProposalStrings();
	}

	@Override
	protected void tearDown() throws Exception {
		IWorkbenchWindow activeWorkbenchWindow = PlatformUI.getWorkbench().getActiveWorkbenchWindow();
		if (activeWorkbenchWindow != null) {
			IWorkbenchPage activePage = activeWorkbenchWindow.getActivePage();
			if (activePage != null) {
				IEditorPart activeEditor = activePage.getActiveEditor();
				if (activeEditor != null) {
					activePage.closeEditor(activeEditor, false);
				}
			}
		}

		FileUtil.delete(getTransfromationContainer().getRawLocation().toFile());
	}

	@Override
	protected Set<String> getActualProposalStrings() {
		return Collections.unmodifiableSet(myActualProposalStrings);
	}

	@Override
	protected Set<String> getExpectedProposalStrings() {
		return Collections.unmodifiableSet(myExpectedProposalStrings);
	}

	protected void initializeWorkspace() throws Exception {
		TestUtil.turnOffAutoBuilding();
	}

	protected void initializeProject() throws Exception {
		myTestProject = new TestProject("CompletionTest", new String[] {}); //$NON-NLS-1$
	}

	protected void initializeProposalProvider() throws Exception {
		IWorkbenchPage page = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage();
		IFile transformationFile = getTransformationFile();
		QvtEditor editor = (QvtEditor) IDE.openEditor(page, transformationFile);
		QvtConfiguration qvtConfiguration = editor.getQvtConfiguration();
		ISourceViewer sourceViewer = editor.getEditorSourceViewer();
		IContentAssistant contentAssistant = qvtConfiguration.getContentAssistant(sourceViewer);
		QvtCompletionProcessor processor = (QvtCompletionProcessor) contentAssistant.getContentAssistProcessor(IDocument.DEFAULT_CONTENT_TYPE);
		do {
			ICompletionProposal[] proposals = processor.computeCompletionProposals((ITextViewer) sourceViewer, myOffset);
			if(proposals != null) {
				for (ICompletionProposal completionProposal : proposals) {
					if (completionProposal instanceof QvtCompletionProposal) {
						String completionProposalStringPresentation = toString((QvtCompletionProposal) completionProposal, processor.getCurrentCategory().getId());
						myActualProposalStrings.add(completionProposalStringPresentation);
					}
				}
			}
		} while(processor.getCurrentCategory() != processor.getLastCategory());
	}

	protected String saveActualProposalStrings() throws Exception {
		File folder = TestUtil.getPluginRelativeFile(BUNDLE, ICompletionTestConstants.COMPLETION_TEST_FOLDER
				+ "/" + myFolder); //$NON-NLS-1$
		File file = new File(folder, ICompletionTestConstants.EXPECTED_PROPOSALS_FILE);
		String fileName = file.getAbsolutePath();
		PrintWriter writer = new PrintWriter(file);
		try {
			for (String proposal : myActualProposalStrings) {
				writer.println(proposal);
			}
			return fileName;
		} finally {
			try {
				if (writer != null) {
					writer.close();
				}
			} catch (Exception ex) {
				ex.printStackTrace();
			}
		}
	}

	protected void loadExpectedProposalStrings() throws Exception {
		FileInputStream contents = null;
		try {
			if (!expectedProposalsFileExisits()) { // enjoy this feature during creation of tests :)
				String fileName = saveActualProposalStrings();
				System.out.println("Test creation mode: "  //$NON-NLS-1$
						+ ICompletionTestConstants.EXPECTED_PROPOSALS_FILE
						+ " successfully created [" + fileName + "]"); //$NON-NLS-1$ //$NON-NLS-2$
			}
			contents = new FileInputStream(getExpectedProposalsFile());
			BufferedReader reader = new BufferedReader(new InputStreamReader(contents));
			String line;
			while ((line = reader.readLine()) != null) {
				if (line.trim().startsWith(ICompletionTestConstants.MODE_STRICT)) {
					isModeStrict = true;
				} else if (line.trim().startsWith(ICompletionTestConstants.MODE_INCLUSIVE)) {
					isModeStrict = false;
				} else {
					myExpectedProposalStrings.add(line);
				}
			}
			reader.close();
		} finally {
			try {
				if (contents != null) {
					contents.close();
				}
			} catch (Exception ex) {
				ex.printStackTrace();
			}
		}
	}

	private String toString(QvtCompletionProposal completionProposal, String categoryId) {
		StringBuilder stringBuilder = new StringBuilder();
		stringBuilder.append("QvtCompletionProposal[").append(categoryId).append("]: "); //$NON-NLS-1$ //$NON-NLS-2$
		stringBuilder.append("replacementString = ").append(completionProposal.getReplacementString()); //$NON-NLS-1$
		stringBuilder.append(", displayString = ").append(completionProposal.getDisplayString()); //$NON-NLS-1$
		stringBuilder.append(", replacementOffsetDelta = ").append(myOffset - completionProposal.getReplacementOffset()); //$NON-NLS-1$
		stringBuilder.append(", replacementLength = ").append(completionProposal.getReplacementLength()); //$NON-NLS-1$
		stringBuilder.append(", cursorPosition = ").append(completionProposal.getCursorPosition()); //$NON-NLS-1$
		return stringBuilder.toString();
	}

	protected void createTransformation() throws Exception {
		// copy test folder to destination test project
		File srcFolder = TestUtil.getPluginRelativeFile(BUNDLE, ICompletionTestConstants.COMPLETION_TEST_FOLDER + '/' + myFolder);
		File destFolder = myTestProject.getProject().getLocation().append(myFolder).toFile();
		destFolder.mkdirs();
		FileUtil.copyFolder(srcFolder, destFolder);
		myTestProject.getProject().refreshLocal(IResource.DEPTH_INFINITE, null);

		InputStream inputStream = new FileInputStream(getAnnotatedTransformationFile());
		try {
			// read annotated transformation contents
			StringBuffer contents = new StringBuffer();
			char[] buffer = new char[4096];
			InputStreamReader reader = new InputStreamReader(inputStream);
			int read;
			while ((read = reader.read(buffer)) > 0) {
				contents.append(buffer, 0, read);
			}
			reader.close();

			// get completion offset and remove annotation
			myOffset = contents.indexOf(ICompletionTestConstants.COMPLETION_ANNOTATION);
			contents.replace(myOffset, myOffset + ICompletionTestConstants.COMPLETION_ANNOTATION.length(), ""); //$NON-NLS-1$

			// create transformation file with updated contents
			IFile transformation = getTransfromationContainer().getFile(new Path(ICompletionTestConstants.TRANSFORMATION_FILE));
			transformation.create(new ReaderInputStream(contents.toString()), true, null);
		} finally {
			try {
				inputStream.close();
			} catch (Exception e) {
				// ignore
			}
		}
	}

	protected IFile getTransformationFile() throws CoreException {
		return getTransfromationContainer().getFile(new Path(ICompletionTestConstants.TRANSFORMATION_FILE));
	}

	protected IContainer getTransfromationContainer() throws CoreException {
		return myTestProject.getProject().getFolder(myFolder);
	}

	protected String getTransformationContents() throws CoreException {
		return QVTOBuilder.getFileContents(getTransformationFile());
	}

	protected File getAnnotatedTransformationFile() throws IOException {
		return TestUtil.getPluginRelativeFile(BUNDLE, ICompletionTestConstants.COMPLETION_TEST_FOLDER
				+ "/" + myFolder + "/" + ICompletionTestConstants.ANNOTATED_TRANSFORMATION_FILE); //$NON-NLS-1$ //$NON-NLS-2$
	}

	protected File getExpectedProposalsFile() throws IOException {
		return TestUtil.getPluginRelativeFile(BUNDLE, ICompletionTestConstants.COMPLETION_TEST_FOLDER
				+ "/" + myFolder + "/" + ICompletionTestConstants.EXPECTED_PROPOSALS_FILE); //$NON-NLS-1$ //$NON-NLS-2$
	}

	protected boolean expectedProposalsFileExisits() throws IOException {
		Bundle bundle = Platform.getBundle(BUNDLE);

		URL url = bundle.getEntry(ICompletionTestConstants.COMPLETION_TEST_FOLDER
				+ "/" + myFolder + "/" + ICompletionTestConstants.EXPECTED_PROPOSALS_FILE); //$NON-NLS-1$ //$NON-NLS-2$
		return url != null;
	}

	@Override
	protected boolean isStrict() {
		return isModeStrict;
	}

	@SuppressWarnings("unchecked")
	private static void enableQVTOCapabilities() {
		String fakeQvtoPluginContributionId = WorkbenchActivityHelper.createUnifiedId(new IPluginContribution() {
			public String getLocalId() {
				return "fakeLocalId"; //$NON-NLS-1$
			}

			public String getPluginId() {
				return "org.eclipse.m2m.qvt.oml.fakePluginId"; //$NON-NLS-1$
			}
		});
		IActivityManager activityManager = PlatformUI.getWorkbench().getActivitySupport().getActivityManager();
		Set<String> activityIds = activityManager.getDefinedActivityIds();
		List<String> qvtoDisablingActivityIds = new ArrayList<String>();
		for (String activityId : activityIds) {
			IActivity activity = activityManager.getActivity(activityId);
			Set<IActivityPatternBinding> activityPatternBindings = activity.getActivityPatternBindings();
			for (IActivityPatternBinding activityPatternBinding : activityPatternBindings) {
				Pattern pattern = activityPatternBinding.getPattern();
				if (!activity.isEnabled() && pattern.matcher(fakeQvtoPluginContributionId).matches()) {
					qvtoDisablingActivityIds.add(activityId);
					break;
				}
			}
		}
		if (!qvtoDisablingActivityIds.isEmpty()) {
			Set<String> enabledActivityIdsCopy = new HashSet<String>(activityManager.getEnabledActivityIds());
			for (String activityId : qvtoDisablingActivityIds) {
				enabledActivityIdsCopy.add(activityId);
			}
			PlatformUI.getWorkbench().getActivitySupport().setEnabledActivityIds(enabledActivityIdsCopy);
		}
	}

	private int myOffset;
	protected final String myFolder;
	protected static TestProject myTestProject;
	private boolean isModeStrict = true;
	private final Set<String> myActualProposalStrings = new LinkedHashSet<String>();
	private final Set<String> myExpectedProposalStrings = new LinkedHashSet<String>();

	public static final String BUNDLE = "org.eclipse.m2m.tests.qvt.oml.ui"; //$NON-NLS-1$
}
