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
 *     Christopher Gerking - bugs 537041, 562177
 *******************************************************************************/
package org.eclipse.m2m.tests.qvt.oml;

import java.io.File;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.eclipse.core.resources.IContainer;
import org.eclipse.core.resources.IFolder;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IWorkspace;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.Path;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.emf.ecore.resource.impl.ResourceSetImpl;
import org.eclipse.m2m.internal.qvt.oml.QvtMessage;
import org.eclipse.m2m.internal.qvt.oml.common.MDAConstants;
import org.eclipse.m2m.internal.qvt.oml.common.io.FileUtil;
import org.eclipse.m2m.internal.qvt.oml.compiler.CompiledUnit;
import org.eclipse.m2m.internal.qvt.oml.compiler.QVTOCompiler;
import org.eclipse.m2m.internal.qvt.oml.compiler.QvtCompilerOptions;
import org.eclipse.m2m.internal.qvt.oml.compiler.UnitProxy;
import org.eclipse.m2m.internal.qvt.oml.project.builder.WorkspaceUnitResolver;
import org.eclipse.m2m.internal.qvt.oml.runtime.project.TransformationUtil;
import org.eclipse.m2m.tests.qvt.oml.ParserTests.TestData;
import org.eclipse.m2m.tests.qvt.oml.util.ProblemSourceAnnotationHelper;
import org.eclipse.m2m.tests.qvt.oml.util.TestUtil;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;

import junit.framework.TestCase;

/**
 * @author pkobiakov
 */
@RunWith(Parameterized.class)
public class TestQvtParser extends TestCase {

	public TestQvtParser(TestData data) {
		super(data.getDir());
		myData = data;
	}

	@Parameters(name="{0}")
	public static Iterable<TestData> data() {
		return Arrays.asList(
				new TestData[] {
						TestData.createSourceChecked("abstractout", 3, 0), //$NON-NLS-1$
						TestData.createSourceChecked("collectionAssignment", 4, 0), //$NON-NLS-1$
						TestData.createSourceChecked("overload_multipleParams", 0, 19), //$NON-NLS-1$
						TestData.createSourceChecked("overload_singleParam", 0, 0), //$NON-NLS-1$
						TestData.createSourceChecked("mainInLibrary", 1, 0), //$NON-NLS-1$
						TestData.createSourceChecked("dynamicpackage", 0, 0), //$NON-NLS-1$
						TestData.createSourceChecked("importedInstances", 1, 0), //$NON-NLS-1$
						TestData.createSourceChecked("moduleElementErrorRecovery_265452", 7, 0), //$NON-NLS-1$
						TestData.createSourceChecked("unitElementErrorRecovery_264517_1", 1, 0), //$NON-NLS-1$
						TestData.createSourceChecked("unitElementErrorRecovery_264517_2", 3, 0), //$NON-NLS-1$
						TestData.createSourceChecked("unitElementErrorRecovery_264675", 3, 0), //$NON-NLS-1$
						TestData.createSourceChecked("nestedPropertiesAssignment_262757", 7, 0), //$NON-NLS-1$
						TestData.createSourceChecked("collectionOperationNotFound_224093", 3, 0), //$NON-NLS-1$
						TestData.createSourceChecked("deprecated_importLocation", 0, 1), //$NON-NLS-1$
						TestData.createSourceChecked("misplacedTopElements", 0, 4), //$NON-NLS-1$
						TestData.createSourceChecked("escape_sequences_250630", 4, 0), //$NON-NLS-1$
						TestData.createSourceChecked("multiline_strings_262733", 1, 0), //$NON-NLS-1$
						TestData.createSourceChecked("deprecated_rename", 0, 1), //$NON-NLS-1$
						TestData.createSourceChecked("varInitExpWithResult_261623", 1, 0), //$NON-NLS-1$
						TestData.createSourceChecked("_while_261024", 4, 0), //$NON-NLS-1$
						TestData.createSourceChecked("listtype", 7, 0), //$NON-NLS-1$
						TestData.createSourceChecked("listtype2", 7, 0), //$NON-NLS-1$
						//new TestData("orderedsetdoesnotconformtoset", 1), //$NON-NLS-1$
						TestData.createSourceChecked("computeExp_252269", 3, 1), //$NON-NLS-1$
						new TestData("implicitsource_ocl_234354", 7, 0), //$NON-NLS-1$
						TestData.createSourceChecked("duplicateModelTypeDef", 1, 0),    	 //$NON-NLS-1$
						TestData.createSourceChecked("blackboxlib_annotation_java", 1, 2), //$NON-NLS-1$
						TestData.createSourceChecked("dupImportFileUnit", 0, 1),    	 //$NON-NLS-1$
						TestData.createSourceChecked("dupImportLibrary", 0, 1),    	 //$NON-NLS-1$
						TestData.createSourceChecked("testmodelparamtype", 3, 0),    	 //$NON-NLS-1$
						TestData.createSourceChecked("resolvecond", 1, 0), //$NON-NLS-1$
						TestData.createSourceChecked("intermPropClash", 9, 0),    	    	 //$NON-NLS-1$
						TestData.createSourceChecked("noClassInImplicitPopulate", 3, 1),    	 //$NON-NLS-1$
						TestData.createSourceChecked("parmnamesclash", 22, 1), //$NON-NLS-1$
						TestData.createSourceChecked("stdlibElementAsOut", 7, 0),    	 //$NON-NLS-1$
						TestData.createSourceChecked("bug2732_wrongcondition", 1, 1), //$NON-NLS-1$
						TestData.createSourceChecked("varscope", 10, 0), //$NON-NLS-1$
						TestData.createSourceChecked("_while", 12, 0), //$NON-NLS-1$
						TestData.createSourceChecked("implicitCallSrc", 16, 6), //$NON-NLS-1$
						TestData.createSourceChecked("libraryWithModuleElements", 2, 0), //$NON-NLS-1$
						new TestData("assert_log", 0), //$NON-NLS-1$
						new TestData("opersignatureparamclash", 1), //$NON-NLS-1$
						new TestData("collectreturntype", 0), //$NON-NLS-1$
						new TestData("nocollectoncollection", 0), //$NON-NLS-1$
						///Commented out, due to migration to OCL 1.2, TODO - clarify the current error reporting with MDT, seems to be incorrect
						// new TestData("nonstaticcallinstaticcontext", 1), //$NON-NLS-1$
						new TestData("virtrettypemismatch", 1), //$NON-NLS-1$
						new TestData("missinglibmmimport", 1), //$NON-NLS-1$
						new TestData("noglobalallinstances", 0), //$NON-NLS-1$
						new TestData("imp", 0), //$NON-NLS-1$ // TODO: independent parsing for imports
						new TestData("imp2", 0), //$NON-NLS-1$
						new TestData("imp3", 0), //$NON-NLS-1$
						TestData.createSourceChecked("impError", 2, 0), //$NON-NLS-1$
						new TestData("assignereadonlyprop", 1), //$NON-NLS-1$
						new TestData("nonbooleanguard", 1), //$NON-NLS-1$
						TestData.createSourceChecked("sameparamname", 2, 0), //$NON-NLS-1$
						new TestData("badtype", 1), //$NON-NLS-1$
						new TestData("simple", 0),   //$NON-NLS-1$
						new TestData("selfimport", 1), //$NON-NLS-1$
						new TestData("normalimport", 0), //$NON-NLS-1$
						new TestData("missingmappingcall", 1), //$NON-NLS-1$
						new TestData("missingfeature", 1), //$NON-NLS-1$
						new TestData("featuretypemismatch", 1), //$NON-NLS-1$
						new TestData("initvariable", 1), //$NON-NLS-1$
						new TestData("missingvariablereference", 1), //$NON-NLS-1$
						new TestData("optionalout", 0), //$NON-NLS-1$
						new TestData("nonassignableout", 1), //$NON-NLS-1$
						TestData.createSourceChecked("wrongout", 3, 0), //$NON-NLS-1$
						new TestData("outininitvar", 0), //$NON-NLS-1$
						new TestData("modifyfeature", 0), //$NON-NLS-1$
						new TestData("missingout", 2), //$NON-NLS-1$
						new TestData("modifyfeatureerror", 2), //$NON-NLS-1$
						new TestData("bodywithsemicolon", 0), //$NON-NLS-1$
						new TestData("modifyparam", 0), //$NON-NLS-1$
						TestData.createSourceChecked("wrongparamnameinout", 2, 0), //$NON-NLS-1$
						new TestData("modifyfeatureininiterror", 2), //$NON-NLS-1$
						TestData.createSourceChecked("voidout", 2, 0), //$NON-NLS-1$
						new TestData("modifyinparam", 1), //$NON-NLS-1$
						new TestData("modifyresult", 0), //$NON-NLS-1$
						new TestData("wrongorderininit", 1), //$NON-NLS-1$
						new TestData("duplicateinitvariable", 1), //$NON-NLS-1$
						TestData.createSourceChecked("missingObjectType", 1, 0), // replaced former 'nestedouterror' //$NON-NLS-1$
						new TestData("emptyinit", 0), //$NON-NLS-1$
						new TestData("assignresultininit", 0), //$NON-NLS-1$
						new TestData("assign_inoutParam", 2), //$NON-NLS-1$
						new TestData("assign_inParam", 2), //$NON-NLS-1$
						new TestData("assign_varThis", 1), //$NON-NLS-1$

						new TestData("errorinexpressionlist", 1), //$NON-NLS-1$
						new TestData("largefile", 0), //$NON-NLS-1$
						new TestData("calldump", 0), //$NON-NLS-1$
						new TestData("missingparamname", 2), //$NON-NLS-1$
						new TestData("nameclash", 1), //$NON-NLS-1$
						new TestData("emptymodule", 0), //$NON-NLS-1$
						new TestData("props", 0), //$NON-NLS-1$
						new TestData("duplicatelocalproperty", 1), //$NON-NLS-1$
						new TestData("wrongorderinproperty", 1), //$NON-NLS-1$
						new TestData("implicitpopulation", 0), //$NON-NLS-1$
						new TestData("implicitpopulationwithinit", 0), //$NON-NLS-1$
						new TestData("mapkeyword", 0), //$NON-NLS-1$
						new TestData("endsectfull", 0), //$NON-NLS-1$
						new TestData("endsectimplicitpopulation", 0), //$NON-NLS-1$
						new TestData("endsectimplicitpopulationnoinit", 0), //$NON-NLS-1$
						new TestData("endsectinitnopopulation", 0), //$NON-NLS-1$
						new TestData("endsectonly", 0), //$NON-NLS-1$
						new TestData("endsectpopulationnoinit", 0), //$NON-NLS-1$
						new TestData("configpropstype", 0), //$NON-NLS-1$

						new TestData("mm_header1", 1), //$NON-NLS-1$
						new TestData("mm_header2", 1), //$NON-NLS-1$
						new TestData("mm_header3", 1), //$NON-NLS-1$
						new TestData("mm_header4", 1), //$NON-NLS-1$
						new TestData("mm_header5", 2), //$NON-NLS-1$
						new TestData("mm_header6", 1), //$NON-NLS-1$
						new TestData("mm_header7", 1), //$NON-NLS-1$
						new TestData("entryOpDupl", 1), //$NON-NLS-1$
						new TestData("resolveIn_ambiguity", 1), //$NON-NLS-1$
						new TestData("bug205303_2", 0), //$NON-NLS-1$
						TestData.createSourceChecked("bug325192", 8, 0), //$NON-NLS-1$
						TestData.createSourceChecked("bug268636", 5, 0), //$NON-NLS-1$
						TestData.createSourceChecked("bug272869", 1, 0), //$NON-NLS-1$
						TestData.createSourceChecked("bug413130", 0, 1), //$NON-NLS-1$
						TestData.createSourceChecked("bug404647_2", 1, 0), //$NON-NLS-1$
						TestData.createSourceChecked("bug414363", 0, 28), //$NON-NLS-1$
						TestData.createSourceChecked("bug401521", 2, 0), //$NON-NLS-1$
						TestData.createSourceChecked("bug414616", 2, 0), //$NON-NLS-1$
						TestData.createSourceChecked("bug419299", 8, 0), //$NON-NLS-1$
						TestData.createSourceChecked("bug414619", 0, 5), //$NON-NLS-1$
						TestData.createSourceChecked("bug415024", 1, 0), //$NON-NLS-1$
						TestData.createSourceChecked("bug413391", 10, 0), //$NON-NLS-1$
						TestData.createSourceChecked("bug424912", 0, 6), //$NON-NLS-1$
						TestData.createSourceChecked("bug289982_ambiguousLib", 0, 1), //$NON-NLS-1$
						TestData.createSourceChecked("bug289982_undefinedLib", 0, 1), //$NON-NLS-1$
						TestData.createSourceChecked("bug289982_validation", 7, 7), //$NON-NLS-1$
						TestData.createSourceChecked("bug424584", 11, 0), //$NON-NLS-1$
						new TestData("bug428028", 0), //$NON-NLS-1$
						TestData.createSourceChecked("bug425634", 0, 6), //$NON-NLS-1$
						new TestData("bug433585", 0), //$NON-NLS-1$
						new TestData("bug438038", 0), //$NON-NLS-1$
						TestData.createSourceChecked("bug446375", 2, 0), //$NON-NLS-1$
						TestData.createSourceChecked("bug468303", 0, 1), //$NON-NLS-1$
						TestData.createSourceChecked("bug477331", 2, 0), //$NON-NLS-1$
						TestData.createSourceChecked("bug483290", 1, 0), //$NON-NLS-1$
						TestData.createSourceChecked("bug487299", 0, 2), //$NON-NLS-1$
						new TestData("bug486810", 0), //$NON-NLS-1$
						TestData.createSourceChecked("bug475907", 1, 0), //$NON-NLS-1$
						TestData.createSourceChecked("bug484020", 14, 0), //$NON-NLS-1$
						TestData.createSourceChecked("bug473151", 0, 5), //$NON-NLS-1$
						new TestData("bug488742", 0), //$NON-NLS-1$
						TestData.createSourceChecked("bug490424", 4, 0).includeMetamodel("test1.ecore"), //$NON-NLS-1$ //$NON-NLS-2$
						TestData.createSourceChecked("bug496181", 0, 3), //$NON-NLS-1$
				}
				);
	}

	protected CompiledUnit[] getCompiledResults() {
		return myCompiled;
	}

	@Override
	@Before
	public void setUp() throws Exception {
		TestUtil.turnOffAutoBuilding();

		String name = "ParserTest"; //$NON-NLS-1$
		myProject = TestProject.getExistingProject(name);
		if(myProject == null) {
			myProject = new TestProject(name, new String[] {}, 0);
		}
	}

	@Override
	@After
	public void tearDown() throws Exception {
		myCompiled = null;
		File destinationFolder = getDestinationFolder();
		if (destinationFolder.exists()) {
			FileUtil.delete(destinationFolder);
		}
	}

	public TestProject getTestProject() {
		return myProject;
	}

	@Override
	@Test
	public void runTest() throws Exception {
		copyData("sources/" + myData.getDir(), "parserTestData/sources/" + myData.getDir()); //$NON-NLS-1$ //$NON-NLS-2$

		final File folder = getDestinationFolder();
		assertTrue("Invalid folder " + folder, folder.exists() && folder.isDirectory()); //$NON-NLS-1$

		resSet = TestUtil.getMetamodelResolutionRS(new ResourceSetImpl(), myData.getMetamodels(), new TestUtil.UriProvider() {

			public URI getModelUri(String model) {
				String absolutePath = getFile(folder, model).getAbsolutePath();
				return URI.createFileURI(absolutePath);
			}
		});

		myCompiled = compile(folder);

		assertTrue("No results", myCompiled.length > 0); //$NON-NLS-1$

		boolean collectOnlyCSTProblems = myData.usesSourceAnnotations();

		List<QvtMessage> allErrors = getAllErrors(myCompiled, collectOnlyCSTProblems);
		assertEquals("Wrong error count for '" + folder.getName() + "', error(s)=" + allErrors, myData.getErrCount(), allErrors.size()); //$NON-NLS-1$ //$NON-NLS-2$
		if (myData.getWarnCount() != -1) {
			List<QvtMessage> allWarnings = getAllWarnings(myCompiled, collectOnlyCSTProblems);
			assertEquals("Wrong warning count for '" + folder.getName() + "', warning(s)=" + allWarnings, myData.getWarnCount(), allWarnings.size()); //$NON-NLS-1$ //$NON-NLS-2$
		}

		// check the AST is consistent
		for (CompiledUnit compilationResult : myCompiled) {
			if(compilationResult.getErrors().size() == 0) {
				TestUtil.assertAllPersistableAST(compilationResult);
			}
		}
		//

		if(myData.usesSourceAnnotations()) {
			Set<ProblemSourceAnnotationHelper> helpers = new HashSet<ProblemSourceAnnotationHelper>();
			for (CompiledUnit compilationResult : myCompiled) {
				doCompiledUnitCheck(compilationResult, helpers);
			}

			// see https://bugs.eclipse.org/bugs/show_bug.cgi?id=458233
			//
			// int expectedProblemCount = myData.getAllProblemsCount();
			// int foundProblemCount = 0;
			// for (ProblemSourceAnnotationHelper nextHelper : helpers) {
			// 		foundProblemCount += nextHelper.getProblemsMap().size();
			// }
			//
			// if (expectedProblemCount >= 0) {
			// 		TestCase.assertEquals(expectedProblemCount, foundProblemCount);
			// }
		}
	}

	protected File getDestinationFolder() {
		return new File(myProject.getProject().getLocation().toString() + "/sources/" + myData.getDir());
	}

	private void doCompiledUnitCheck(CompiledUnit module, Set<ProblemSourceAnnotationHelper> annotationCollector) {
		if(!MDAConstants.QVTO_FILE_EXTENSION.equals(module.getURI().fileExtension())) {
			// check only .qvto file as these have CST stream
			return;
		}

		ProblemSourceAnnotationHelper helper = ProblemSourceAnnotationHelper
				.assertCompilationProblemMatchExpectedAnnotations(module);
		annotationCollector.add(helper);
		for (CompiledUnit importedModule : module.getCompiledImports()) {
			if (!annotationCollector.contains(importedModule)) {
				doCompiledUnitCheck(importedModule, annotationCollector);
			}
		}
	}

	static class CompositeException extends Exception {
		private static final long serialVersionUID = -1045874581781288741L;

		public CompositeException(String message, List<Throwable> exceptions) {
			super(message);
			myExceptions = exceptions;
		}

		@Override
		public String toString() {
			StringBuffer s = new StringBuffer();
			s.append(super.toString());
			for(Throwable t : myExceptions) {
				StringWriter trace = new StringWriter();
				t.printStackTrace(new PrintWriter(trace));
				s.append("\n" + trace.toString()); //$NON-NLS-1$
			}

			return s.toString();
		}

		private final List<Throwable> myExceptions;
	}

	private List<QvtMessage> getAllErrors(CompiledUnit[] compiled, boolean concreteSyntaxOnly) {
		List<QvtMessage> errors = new ArrayList<QvtMessage>();
		for (CompiledUnit compilationResult : compiled) {
			TransformationUtil.getErrors(compilationResult, errors, concreteSyntaxOnly);
		}

		return errors;
	}

	private List<QvtMessage> getAllWarnings(CompiledUnit[] compiled, boolean concreteSyntaxOnly) {
		List<QvtMessage> warnings = new ArrayList<QvtMessage>();
		for (CompiledUnit compilationResult : compiled) {
			TransformationUtil.getWarnings(compilationResult, warnings, concreteSyntaxOnly);
		}

		return warnings;
	}

	private CompiledUnit[] compile(File folder) throws Exception {
		final String topName = folder.getName() + MDAConstants.QVTO_FILE_EXTENSION_WITH_DOT;
		getFile(folder, topName);
		WorkspaceUnitResolver resolver = new WorkspaceUnitResolver(Collections.singletonList(getIFolder(folder)));
		QVTOCompiler compiler = new QVTOCompiler(resSet.getPackageRegistry());

		QvtCompilerOptions options = new QvtCompilerOptions();
		options.setGenerateCompletionData(false);

		UnitProxy unit = resolver.resolveUnit(folder.getName());
		return new CompiledUnit[] { compiler.compile(unit, options, (IProgressMonitor)null) };
	}

	private static File getFile(File folder, final String expectedName) {
		File file = new File(folder, expectedName);
		assertTrue("Invalid file: " + file, file.exists() && file.isFile()); //$NON-NLS-1$
		return file;
	}

	private IContainer getIFolder(File folderUnderWorkspace) throws MalformedURLException, URISyntaxException {
		IWorkspace workspace = ResourcesPlugin.getWorkspace();
		IPath location = new Path(folderUnderWorkspace.getAbsolutePath());
		IContainer[] containers = workspace.getRoot().findContainersForLocationURI(location.makeAbsolute().toFile().toURI());
		if(containers == null || containers.length != 1 || containers[0] instanceof IFolder == false) {
			throw new RuntimeException("Folder not found: " + folderUnderWorkspace); //$NON-NLS-1$
		}

		return containers[0];
	}

	private void copyData(String destPath, String srcPath) throws Exception {
		File sourceFolder = TestUtil.getPluginRelativeFolder(srcPath);
		File destFolder = new File(myProject.getProject().getLocation().toString() + "/" + destPath); //$NON-NLS-1$
		destFolder.mkdirs();
		FileUtil.copyFolder(sourceFolder, destFolder);
		myProject.getProject().refreshLocal(IResource.DEPTH_INFINITE, null);
	}

	private final TestData myData;
	private TestProject myProject;
	private CompiledUnit[] myCompiled;
	private ResourceSet resSet;
}
