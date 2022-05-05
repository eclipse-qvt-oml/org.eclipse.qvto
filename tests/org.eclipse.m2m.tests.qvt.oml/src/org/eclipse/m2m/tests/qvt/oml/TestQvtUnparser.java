/*******************************************************************************
 * Copyright (c) 2016, 2018 Uwe Ritzmann and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v2.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v20.html
 *
 * Contributors:
 *     Uwe Ritzmann - initial API and implementation
 *     Christopher Gerking - bug 537041
 *******************************************************************************/
package org.eclipse.m2m.tests.qvt.oml;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.eclipse.compare.contentmergeviewer.ITokenComparator;
import org.eclipse.compare.rangedifferencer.IRangeComparator;
import org.eclipse.compare.rangedifferencer.RangeDifference;
import org.eclipse.compare.rangedifferencer.RangeDifferencer;
import org.eclipse.core.resources.IContainer;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IFolder;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IWorkspace;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.Path;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.EPackage.Registry;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.emf.ecore.xmi.XMIResource;
import org.eclipse.m2m.internal.qvt.oml.QvtMessage;
import org.eclipse.m2m.internal.qvt.oml.common.MDAConstants;
import org.eclipse.m2m.internal.qvt.oml.common.io.FileUtil;
import org.eclipse.m2m.internal.qvt.oml.compiler.CompiledUnit;
import org.eclipse.m2m.internal.qvt.oml.compiler.ExeXMISerializer;
import org.eclipse.m2m.internal.qvt.oml.compiler.QVTOCompiler;
import org.eclipse.m2m.internal.qvt.oml.compiler.QvtCompilerOptions;
import org.eclipse.m2m.internal.qvt.oml.compiler.UnitProxy;
import org.eclipse.m2m.internal.qvt.oml.emf.util.EmfException;
import org.eclipse.m2m.internal.qvt.oml.emf.util.EmfUtil;
import org.eclipse.m2m.internal.qvt.oml.emf.util.urimap.MetamodelURIMappingHelper;
import org.eclipse.m2m.internal.qvt.oml.evaluator.QVTEvaluationOptions;
import org.eclipse.m2m.internal.qvt.oml.project.builder.WorkspaceUnitResolver;
import org.eclipse.m2m.internal.qvt.oml.runtime.project.TransformationUtil;
import org.eclipse.m2m.tests.qvt.oml.UnparserTests.AntTestData;
import org.eclipse.m2m.tests.qvt.oml.UnparserTests.ApiTestData;
import org.eclipse.m2m.tests.qvt.oml.UnparserTests.DeployedTestData;
import org.eclipse.m2m.tests.qvt.oml.UnparserTests.ExternLibTestData;
import org.eclipse.m2m.tests.qvt.oml.UnparserTests.ModelsTestData;
import org.eclipse.m2m.tests.qvt.oml.UnparserTests.PluginProjectTestData;
import org.eclipse.m2m.tests.qvt.oml.UnparserTests.SourcesTestData;
import org.eclipse.m2m.tests.qvt.oml.UnparserTests.TestData;
import org.eclipse.m2m.tests.qvt.oml.util.TestUtil;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;

import junit.framework.TestCase;

@RunWith(Parameterized.class)
public class TestQvtUnparser extends TestCase {

	public TestQvtUnparser(TestData data) {
		super(data.getName());
		myData = data;
	}

	@Parameters(name="{0}")
	public static Iterable<TestData> data() {
		return Arrays.asList(
				new TestData[] {
						new ModelsTestData("A",0,0,1) {  //$NONXXX	// 1*4: target names in resolve expressions
							@Override
							public String getDir() {
								return "bug358709";  //$NONXXX
							}
						},
						new ModelsTestData("abstractresult",0,0,1),  //$NONXXX	//1: preMarked
						new ModelsTestData("accessbooleans",0,0),  //$NONXXX
						new ModelsTestData("addclass",0,0),  //$NONXXX
						new ModelsTestData("addclassviamodificationininit",0,0,1),  //$NONXXX	//1: preMarked
						new ModelsTestData("addclassviaoutinocl",0,0),  //$NONXXX
						new ModelsTestData("addclassviasequence",0,0),  //$NONXXX
						new ModelsTestData("addrealtostring",0,0),  //$NONXXX
						new ModelsTestData("addundefined",0,0,26),  //$NONXXX	//26: preMarked
						new ModelsTestData("allinstances",0,0),  //$NONXXX
						new PluginProjectTestData("AnotherTransformation",0,0) {  //$NONXXX
							
							@Override
							public String getFolder() {
								return Path.EMPTY.toString();
							}
							
							@Override
							public String getSubFolder() {
								return Path.EMPTY.toString();
							}
							
							@Override
							public String getDir() {
								return "transforms";  //$NONXXX
							}
						},	
						new ModelsTestData("AnyExtension",0,0) {  //$NONXXX
							@Override
							public String getDir() {
								return "oclany";  //$NONXXX
							}
						},
						new TestData("assert_log_minimal",0,0),  //$NONXXX
						new SourcesTestData("assert_log",0,0),  //$NONXXX
						new ModelsTestData("assignresultininit",0,0),  //$NONXXX
						new ModelsTestData("assigntonullowner",0,0,1),  //$NONXXX	//1: preMarked
						new ModelsTestData("assigntoprimfeature",0,0,1),  //$NONXXX	//1: preMarked
						new ModelsTestData("auxtransf",0,0) {  //$NONXXX
							@Override
							public String getDir() {
								return "stacktrace";  //$NONXXX
							}
						},
						new SourcesTestData("b1",0,0) {  //$NONXXX
							@Override
							public String getDir() {
								return "imp";  //$NONXXX
							}
						},
						new ModelsTestData("bagorderedsetintersection",0,0),  //$NONXXX
						new ModelsTestData("Base",0,0) {  //$NONXXX
							@Override
							public String getDir() {
								return "bug414555";  //$NONXXX
							}
						},
						new ModelsTestData("blackboxlib_237781",0,0),  //$NONXXX
						new TestData("blackboxlib_uri",0,0),  //$NONXXX
						new ModelsTestData("blackboxlib_annotation_java",0,0),  //$NONXXX
						new SourcesTestData("blackboxlibASTmodel",0,0),  //$NONXXX
						new ModelsTestData("blackboxlib_context",0,0),  //$NONXXX
						new SourcesTestData("bodywithsemicolon",0,0),  //$NONXXX
						new ModelsTestData("boxing",0,0),  //$NONXXX
						new ModelsTestData("bug204126_1",0,0,1),  //$NONXXX // 4: target names in resolve expressions
						new ModelsTestData("bug204126_2",0,0,1),  //$NONXXX // 4: target names in resolve expressions
						new ModelsTestData("bug204126_3",0,0,1),  //$NONXXX // 4: target names in resolve expressions
						new ModelsTestData("bug204126_4",0,0,1),  //$NONXXX // 4: target names in resolve expressions
						new ModelsTestData("bug204126_5",0,0,1),  //$NONXXX // 4: target names in resolve expressions
						new ModelsTestData("bug204126_6",0,0,1),  //$NONXXX // 4: target names in resolve expressions
						new ModelsTestData("bug204126_7",0,0,1),  //$NONXXX // 4: target names in resolve expressions
						new ModelsTestData("bug205303_1",0,0,1+4),  //$NONXXX // 1:preMarked,16: target names in resolve expressions
						new SourcesTestData("bug205303_2",0,0,1+6),  //$NONXXX // 1:preMarked,24: target names in resolve expressions
						new ModelsTestData("bug_214938",0,0),  //$NONXXX
						new ModelsTestData("bug216317",0,0,2),  //$NONXXX // 2*4: target names in resolve expressions
						new ModelsTestData("bug219075_1",0,0),  //$NONXXX
						new ModelsTestData("bug224094",0,0),  //$NONXXX
						new ModelsTestData("bug233984",0,0,2),  //$NONXXX	// 1: ordering of top level eClassifiers changed
						new ModelsTestData("bug2437_1",0,0,4),  //$NONXXX // 4*2: implicit set conversion of OCL analyzer
						new ModelsTestData("bug2437_2",0,0),  //$NONXXX
						new ModelsTestData("bug2437_3",0,0),  //$NONXXX
						new ModelsTestData("bug2437_4",0,0,1),  //$NONXXX // 1*2: implicit set conversion of OCL analyzer
						new ModelsTestData("bug2437_5",0,0,1),  //$NONXXX // 1*2: implicit set conversion of OCL analyzer
						new ModelsTestData("bug244701",0,0),  //$NONXXX
						new ModelsTestData("bug267917",0,0),  //$NONXXX
						new SourcesTestData("bug268636lib",0,0) {  //$NONXXX
							@Override
							public String getDir() {
								return "bug268636";  //$NONXXX
							}
						},
						new SourcesTestData("bug272869lib",0,0) {  //$NONXXX
							@Override
							public String getDir() {
								return "bug272869";  //$NONXXX
							}
						},
						new ModelsTestData("bug2732",0,0,2),  //$NONXXX
						new ModelsTestData("bug2741",0,0),  //$NONXXX
						new ModelsTestData("bug274105_274505",0,0,9),  //$NONXXX // 9*2: implicit set conversion of OCL analyzer
						new ModelsTestData("bug2787",0,0,1),  //$NONXXX // 1*2: implicit set conversion of OCL analyzer
						new ModelsTestData("bug2839",0,0,1),  //$NONXXX // 1*2: implicit set conversion of OCL analyzer
						new SourcesTestData("bug289982_ambiguousLib",0,0),  //$NONXXX
						new ModelsTestData("bug289982_lib",0,0) {  //$NONXXX
							@Override
							public String getDir() {
								return "bug289982";  //$NONXXX
							}
						},
						new ModelsTestData("bug289982_lib_failed",0,0) {  //$NONXXX
							@Override
							public String getDir() {
								return "bug289982_failed";  //$NONXXX
							}
						},
						new SourcesTestData("bug289982_undefinedLib",0,0),  //$NONXXX
						new ModelsTestData("bug294127",0,0),  //$NONXXX
						new ModelsTestData("bug301134",0,0),  //$NONXXX
						new ModelsTestData("bug314443",0,0,2),  //$NONXXX	// 1: ordering of top level eClassifiers changed
						new ModelsTestData("bug323915",0,0),  //$NONXXX
						new ModelsTestData("bug325192",0,0,4),  //$NONXXX // 4*2: implicit set conversion of OCL analyzer
						new ModelsTestData("bug326871",0,0),  //$NONXXX
						new ModelsTestData("bug326871_lib",0,0) {  //$NONXXX
							@Override
							public String getDir() {
								return "bug326871a";  //$NONXXX
							}
						},
						new ModelsTestData("bug326871_standalone",0,0),  //$NONXXX
						new ModelsTestData("bug370098",0,0),  //$NONXXX
						new ModelsTestData("bug377882",0,0,1),  //$NONXXX	// 1*4: target names in resolve expressions
						new ModelsTestData("bug388325",0,0),  //$NONXXX
						new ModelsTestData("bug388801",0,0),  //$NONXXX
						new ModelsTestData("bug392156",0,0,2+12),  //$NONXXX	//2*2: implicit set conversion of OCL analyzer, 12*4: target names in resolve expressions
						new ModelsTestData("bug392429",0,0),  //$NONXXX
						new ModelsTestData("bug397215",0,0),  //$NONXXX
						new ModelsTestData("bug397398",0,0),  //$NONXXX
						new ModelsTestData("bug400233_lib",0,0) {  //$NONXXX
							@Override
							public String getDir() {
								return "bug400233";  //$NONXXX
							}
						},
						new ModelsTestData("bug400720",0,0),  //$NONXXX
						new ModelsTestData("bug404647",0,0),  //$NONXXX
						new ModelsTestData("bug410470",0,0),  //$NONXXX
						new SourcesTestData("bug413130",0,0),  //$NONXXX
						new ModelsTestData("bug413131",0,0,1),  //$NONXXX	//42: t6 EAttribute vs. EReference
						new SourcesTestData("bug414363",0,0),  //$NONXXX
						new ModelsTestData("bug414472",0,0),  //$NONXXX
						new SourcesTestData("bug414619",0,0),  //$NONXXX
						new ModelsTestData("bug414642",0,0),  //$NONXXX
						new ModelsTestData("bug415024",0,0),  //$NONXXX
						new ModelsTestData("bug415029",0,0),  //$NONXXX
						new ModelsTestData("bug415209",0,0),  //$NONXXX
						new ModelsTestData("bug415310",0,0),  //$NONXXX
						new ModelsTestData("bug415315",0,0),  //$NONXXX
						new ModelsTestData("bug415661",0,0),  //$NONXXX
						new ModelsTestData("bug416584lib",0,0) {  //$NONXXX
							@Override
							public String getDir() {
								return "bug416584";  //$NONXXX
							}
						},
						new ModelsTestData("bug417751",0,0),  //$NONXXX
						new ModelsTestData("bug417779",0,0),  //$NONXXX
						new ModelsTestData("bug417996",0,0,2),  //$NONXXX	// 2: order of properties changed
						new ModelsTestData("bug418512",0,0),  //$NONXXX
						new ModelsTestData("bug418961_lib",0,0) {  //$NONXXX
							@Override
							public String getDir() {
								return "bug418961";  //$NONXXX
							}
						},
						new ModelsTestData("bug419299",0,0),  //$NONXXX
						new ModelsTestData("bug422315",0,0),  //$NONXXX
						new ModelsTestData("bug424086",0,0,2),  //$NONXXX	// 2: target names in resolve expressions
						new ModelsTestData("bug424584",0,0),  //$NONXXX
						new ModelsTestData("bug424740",0,0),  //$NONXXX
						new ModelsTestData("bug424896",0,0),  //$NONXXX
						new SourcesTestData("bug424912",0,0,1),  //$NONXXX	// 1*4: target names in resolve expressions
						new ModelsTestData("bug424979",0,0,10),  //$NONXXX	// 10*2: implicit set conversion of OCL analyzer
						new ModelsTestData("bug425069a",0,0) {  //$NONXXX
							@Override
							public String getDir() {
								return "bug425069";  //$NONXXX
							}
						},
						new SourcesTestData("bug425634",0,0,10),  //$NONXXX	// 10*4: target names in resolve expressions
						new ModelsTestData("bug427237",0,0),  //$NONXXX
						new ModelsTestData("bug427348",0,0),  //$NONXXX
						new SourcesTestData("bug428028",0,0),  //$NONXXX
						new ModelsTestData("bug428316",0,0),  //$NONXXX
						new ModelsTestData("bug428618",0,0,1),  //$NONXXX // 1*2: implicit set conversion of OCL analyzer
						new ModelsTestData("bug432786",0,0,2),  //$NONXXX	// 2*4: target names in resolve expressions
						new ModelsTestData("bug433292",0,0,1),  //$NONXXX	// 1*4: target names in resolve expressions
						new SourcesTestData("bug433585",0,0),  //$NONXXX
						new ModelsTestData("bug440514",0,0),  //$NONXXX
						new ModelsTestData("bug449445",0,0),  //$NONXXX
						new ModelsTestData("bug449912",0,0),  //$NONXXX
						new ModelsTestData("bug457433",0,0),  //$NONXXX
						new ModelsTestData("bug463395",0,0),  //$NONXXX
						new ModelsTestData("bug463396",0,0),  //$NONXXX
						new ModelsTestData("bug463572_lib",0,0) {  //$NONXXX
							@Override
							public String getDir() {
								return "bug463572";  //$NONXXX
							}
						},
						new ModelsTestData("bug466705",0,0),  //$NONXXX
						new ModelsTestData("bug467325",0,0),  //$NONXXX
						new ModelsTestData("bug467600",0,0),  //$NONXXX
						new ModelsTestData("bug467600_Bag",0,0),  //$NONXXX
						new ModelsTestData("bug467600_Collection",0,0),  //$NONXXX
						new ModelsTestData("bug467600_List",0,0),  //$NONXXX
						new ModelsTestData("bug467600_OrderedSet",0,0),  //$NONXXX
						new ModelsTestData("bug467600_Sequence",0,0),  //$NONXXX
						new ModelsTestData("bug467600_Set",0,0),  //$NONXXX
						new ModelsTestData("bugzilla443",0,0),  //$NONXXX
						new ModelsTestData("C",0,0) {  //$NONXXX
							@Override
							public String getDir() {
								return "bug475123";  //$NONXXX
							}
						},
						new SourcesTestData("calldump",0,0),  //$NONXXX
						new ModelsTestData("calloclIsUndefinedforundefined",0,0),  //$NONXXX
						new ModelsTestData("callvirtforundefined",0,0),  //$NONXXX
						new ModelsTestData("castinttodouble",0,0),  //$NONXXX
						new ModelsTestData("chainedAssignments_261024",0,0),  //$NONXXX
						new DeployedTestData("CheckMemoryLeak",0,0) {  //$NONXXX
							@Override
							public String getDir() {
								return "intermprop_cleanup";  //$NONXXX
							}
						},
						new DeployedTestData("ChildInTreeInput",0,0) {  //$NONXXX
							@Override
							public String getDir() {
								return "callapi";  //$NONXXX
							}
						},
						new ModelsTestData("collectionMappingResult",0,0),  //$NONXXX
						new SourcesTestData("collectreturntype",0,0),  //$NONXXX
						new TestData("ColorSettingPreviewCode",0,0),  //$NONXXX
						new SourcesTestData("compatible",0,0) {  //$NONXXX
							@Override
							public String getDir() {
								return "bug468303";  //$NONXXX
							}
						},
						new ModelsTestData("computeExp_250403",0,0),  //$NONXXX
						new SourcesTestData("configpropstype",0,0),  //$NONXXX
						new ModelsTestData("constructors",0,0),  //$NONXXX
						new ModelsTestData("contextlesscall",0,0,1),  //$NONXXX	//1: preMarked
						new ModelsTestData("continue_break",0,0),  //$NONXXX
						new ModelsTestData("continue_perf",0,0),  //$NONXXX
						new ModelsTestData("copynameviacontextmapping",0,0),  //$NONXXX
						new ModelsTestData("customop",0,0),  //$NONXXX
						new SourcesTestData("deprecated_importLocation",0,0),  //$NONXXX
						new SourcesTestData("deprecated_rename",0,0),  //$NONXXX
						new ModelsTestData("dicttype",0,0),  //$NONXXX
						new ModelsTestData("doubleQuoteStrings_262734",0,0),  //$NONXXX
						new SourcesTestData("dupImportLibrary",0,0),  //$NONXXX
						new TestData("DuplicatedNamesDetection",0,0,2),  //$NONXXX	// 1: ordering of top level eClassifiers changed
						new SourcesTestData("dynamicpackage",0,0),  //$NONXXX
						new DeployedTestData("Ecore2Ecore",0,0) {  //$NONXXX
							@Override
							public String getDir() {
								return "callapi";  //$NONXXX
							}
						},
						new DeployedTestData("Ecore2EcoreExt",0,0) {  //$NONXXX
							@Override
							public String getDir() {
								return "callapi";  //$NONXXX
							}
						},
						new AntTestData("ecore2uml",0,0),  //$NONXXX
						//new TestData("Ecore2UML",0,0,1),  //$NONXXX	// 1*4: target names in resolve expressions
						new ModelsTestData("emptyExtents",0,0),  //$NONXXX
						new SourcesTestData("emptyinit",0,0),  //$NONXXX
						new SourcesTestData("emptymodule",0,0),  //$NONXXX
						new ModelsTestData("emptyout",0,0),  //$NONXXX
						new SourcesTestData("endsectfull",0,0),  //$NONXXX
						new SourcesTestData("endsectimplicitpopulation",0,0),  //$NONXXX
						new SourcesTestData("endsectimplicitpopulationnoinit",0,0),  //$NONXXX
						new SourcesTestData("endsectinitnopopulation",0,0),  //$NONXXX
						new SourcesTestData("endsectonly",0,0),  //$NONXXX
						new SourcesTestData("endsectpopulationnoinit",0,0),  //$NONXXX
						new ModelsTestData("endsectresultpatch",0,0),  //$NONXXX
						new ModelsTestData("entryOp",0,0),  //$NONXXX
						new ModelsTestData("equndefined",0,0,28),  //$NONXXX // 28:preMarked
						new ModelsTestData("escape_sequences_250630",0,0),  //$NONXXX
						new ApiTestData("exec1",0,0),  //$NONXXX
						new ApiTestData("exec2",0,0),  //$NONXXX
						new ApiTestData("exec3",0,0),  //$NONXXX
						new ApiTestData("exec3_lib",0,0) {  //$NONXXX
							@Override
							public String getDir() {
								return "exec3_withImport";  //$NONXXX
							}
						},
						new ModelsTestData("exists",0,0),  //$NONXXX
						new SourcesTestData("ExtendedLibrary",0,0) {  //$NONXXX
							@Override
							public String getDir() {
								return "bug438038";  //$NONXXX
							}
						},
						new ModelsTestData("firstlast",0,0),  //$NONXXX
						new TestData("firstunparsertest",0,0),  //$NONXXX
						new ExternLibTestData("FooLibImport",0,0) {  //$NONXXX
							@Override
							public String getDir() {
								return "com/foo";  //$NONXXX
							}
						},
						new ModelsTestData("forExp_245275",0,0),  //$NONXXX
						new ModelsTestData("getpropfromundefined",0,0),  //$NONXXX
						new TestData("HelpersAndQueries",0,0),  //$NONXXX
						new ModelsTestData("helperSimpleDef_252173",0,0),  //$NONXXX
						new ModelsTestData("imp",0,0) {  //$NONXXX
							@Override
							public String getDir() {
								return "propuseprop";  //$NONXXX
							}
						},
						new SourcesTestData("implicitpopulation",0,0),  //$NONXXX
						new SourcesTestData("implicitpopulationwithinit",0,0),  //$NONXXX
						new SourcesTestData("implicitSrcImport",0,0) {  //$NONXXX
							@Override
							public String getDir() {
								return "implicitCallSrc";  //$NONXXX
							}
						},
						new ModelsTestData("imported",0,0) {  //$NONXXX
							@Override
							public String getDir() {
								return "fqntraces";  //$NONXXX
							}
						},
						new ModelsTestData("imported2",0,0) {  //$NONXXX
							@Override
							public String getDir() {
								return "fqnMainCalls_272937";  //$NONXXX
							}
						},
						new SourcesTestData("importedFileUnit",0,0) {  //$NONXXX
							@Override
							public String getDir() {
								return "dupImportFileUnit/ns";  //$NONXXX
							}
						},
						new TestData("ImportedLib",0,0),  //$NONXXX
						new ModelsTestData("ImportedMappingTest",0,0,1) {  //$NONXXX // 1: preMarked
							@Override
							public String getDir() {
								return "importedvirtuals";  //$NONXXX
							}
						},
						new ModelsTestData("ImportedTransf1",0,0,2) {  //$NONXXX	// 1: ordering of top level eClassifiers changed
							@Override
							public String getDir() {
								return "import_access_extends";  //$NONXXX
							}
						},
						new ModelsTestData("ImportedTransf2",0,0,2) {  //$NONXXX	// 1: ordering of top level eClassifiers changed
							@Override
							public String getDir() {
								return "import_access_extends";  //$NONXXX
							}
						},
						new ModelsTestData("ImportedTransf4",0,0) {  //$NONXXX
							@Override
							public String getDir() {
								return "import_access_extends";  //$NONXXX
							}
						},
						new SourcesTestData("incompatible",0,0) {  //$NONXXX
							@Override
							public String getDir() {
								return "bug468303";  //$NONXXX
							}
						},
						new ModelsTestData("initsectresultpatch",0,0),  //$NONXXX
						new ModelsTestData("inoutcontextparam",0,0),  //$NONXXX
						new ModelsTestData("inoutcontextparamnoresult",0,0),  //$NONXXX
						new ModelsTestData("inoutMapping",0,0),  //$NONXXX
						new DeployedTestData("InplaceEcore",0,0) {  //$NONXXX
							@Override
							public String getDir() {
								return "callapi";  //$NONXXX
							}
						},
						new TestData("IntermediateData",0,0),  //$NONXXX
						new ModelsTestData("intermediateprop_imported",0,0) {  //$NONXXX
							@Override
							public String getDir() {
								return "intermediateprop_import";  //$NONXXX
							}
						},
						new ModelsTestData("intermediateprop_resolve",0,0,2),  //$NONXXX	// 2: preMarked
						new ModelsTestData("intermediateprop_trace",0,0),  //$NONXXX
						new ModelsTestData("intermProperties",0,0,4),  //$NONXXX	// 2: ordering of properties changed
						new ModelsTestData("intermSimple",0,0,2),  //$NONXXX	// 1: ordering of top level eClassifiers changed
						new ModelsTestData("intermWithCrossRefs",0,0,2),  //$NONXXX	// 1: ordering of top level eClassifiers changed
						new ModelsTestData("intermWithExtends",0,0),  //$NONXXX
						new ModelsTestData("intermWithoutExtent",0,0,2),  //$NONXXX	// 1: ordering of top level eClassifiers changed
						new ModelsTestData("invalidcollectioncast",0,0,2),  //$NONXXX	// 1: ordering of top level eClassifiers changed
						new ModelsTestData("invalidConfigProp",0,0),  //$NONXXX
						new ModelsTestData("invresolve_",0,0,1),  //$NONXXX	// 1*4: target names in resolve expressions
						new ModelsTestData("invresolvebyrule",0,0,1+1),  //$NONXXX	// 1: preMarked, 1*4: target names in resolve expressions
						new ModelsTestData("isunique",0,0),  //$NONXXX
						new ModelsTestData("iterateoverintset",0,0),  //$NONXXX
						new ModelsTestData("iteratetest",0,0),  //$NONXXX
						new ModelsTestData("javakeywords",0,0),  //$NONXXX
						new ModelsTestData("lateresolve",0,0,2+1),  //$NONXXX	// 2: preMarked, 1*4: target names in resolve expressions
						new ModelsTestData("LateResolveBase",0,0) {  //$NONXXX
							@Override
							public String getDir() {
								return "bug463416";  //$NONXXX
							}
						},
						new ModelsTestData("lateresolvebyrule",0,0,2+1),  //$NONXXX	// 2: preMarked, 1*4: target names in resolve expressions
						new ModelsTestData("lateresolve_many",0,0,3),  //$NONXXX	// 2: ordering of top level eClassifiers changed
						new ModelsTestData("Lib",0,0) {  //$NONXXX
							@Override
							public String getDir() {
								return "bug463410";  //$NONXXX
							}
						},
						new DeployedTestData("Lib1",0,0) {  //$NONXXX
							@Override
							public String getDir() {
								return "org/eclipse";  //$NONXXX
							}
						},
						new DeployedTestData("Lib2",0,0) {  //$NONXXX
							@Override
							public String getDir() {
								return "a/b";  //$NONXXX
							}
						},
						new ModelsTestData("LibForAccess",0,0) {  //$NONXXX
							@Override
							public String getDir() {
								return "fqn_noncontextual";  //$NONXXX
							}
						},
						new ModelsTestData("LibForExtends",0,0) {  //$NONXXX
							@Override
							public String getDir() {
								return "fqn_noncontextual";  //$NONXXX
							}
						},
						new ModelsTestData("listLiteral_259754",0,0),  //$NONXXX
						new ModelsTestData("localstrings",0,0),  //$NONXXX
						new ModelsTestData("m3",0,0) {  //$NONXXX
							@Override
							public String getDir() {
								return "imports_transformations";  //$NONXXX
							}
						},
						new ModelsTestData("mapDisjuncts",0,0,5),  //$NONXXX	// 5*4: target names in resolve expressions
						new ModelsTestData("mapInherits",0,0,4),  //$NONXXX	// 4*4: target names in resolve expressions
						new ModelsTestData("mapkeyword",0,0,1),  //$NONXXX	// 1: preMarked
						new ModelsTestData("mapMerges",0,0,4),  //$NONXXX	// 4*4: target names in resolve expressions
						new ModelsTestData("mapMultipleInherits",0,0),  //$NONXXX
						new TestData("MappingBody",0,0),  //$NONXXX
						new ModelsTestData("mappingBodyExpressions_252358",0,0),  //$NONXXX
						new TestData("MappingExtensionDisjuncts",0,0),  //$NONXXX
						new TestData("MappingExtensionInherits",0,0),  //$NONXXX
						new TestData("MappingExtensionMerges",0,0),  //$NONXXX
						new TestData("Mappings",0,0),  //$NONXXX
						new TestData("MappingsWhenClause",0,0),  //$NONXXX
						new ModelsTestData("mappingWithNoResultTrace_266854",0,0,1),  //$NONXXX	// 1*4: target names in resolve expressions
						new TestData("markedTest",0,0),  //$NONXXX
						new SourcesTestData("misplacedTopElements",0,0),  //$NONXXX
						new ModelsTestData("mm_header1",0,0),  //$NONXXX
						new ModelsTestData("mm_header2",0,0),  //$NONXXX
						new ModelsTestData("mm_header3",0,0),  //$NONXXX
						new ModelsTestData("mm_header4",0,0),  //$NONXXX
						new ModelsTestData("mm_modifyvar",0,0),  //$NONXXX
						new TestData("ModelExtents",0,0),  //$NONXXX
						new SourcesTestData("modifyfeature",0,0),  //$NONXXX
						new SourcesTestData("modifyparam",0,0),  //$NONXXX
						new SourcesTestData("modifyresult",0,0),  //$NONXXX
						new ModelsTestData("modifyvar",0,0),  //$NONXXX
						new ModelsTestData("moduleProperty",0,0),  //$NONXXX
						new ModelsTestData("multilineStrings_262733",0,0),  //$NONXXX
						new ModelsTestData("multipletracerecords",0,0,1),  //$NONXXX	// 1*4: target names in resolve expressions
						new ModelsTestData("multiresultpars",0,0,1),  //$NONXXX	// 1*4: target names in resolve expressions
						new ModelsTestData("nested",0,0,1) {  //$NONXXX	// 1*4: target names in resolve expressions
							@Override
							public String getDir() {
								return "compositetransf";  //$NONXXX
							}
						},
						new ModelsTestData("nestednativeops",0,0),  //$NONXXX
						new ModelsTestData("nestedPropertiesAssignment_262757",0,0,2+1),  //$NONXXX	// 2*2: implicit set conversion of OCL analyzer, 1*4: target names in resolve expressions
						new ModelsTestData("NestedTransformation",0,0) {  //$NONXXX
							@Override
							public String getDir() {
								return "bug433937_referenced/root/other";  //$NONXXX
							}
						},
						new ModelsTestData("NewLibrary",0,0) {  //$NONXXX
							@Override
							public String getDir() {
								return "bug390088";  //$NONXXX
							}
						},
						new ModelsTestData("NewTransformation",0,0) {  //$NONXXX
							@Override
							public String getDir() {
								return "bug392153";  //$NONXXX
							}
						},
						new SourcesTestData("nocollectoncollection",0,0),  //$NONXXX
						new SourcesTestData("noglobalallinstances",0,0,2),  //$NONXXX	// 1: ordering of top level eClassifiers changed
						new DeployedTestData("NonExecutable",0,0) {  //$NONXXX
							@Override
							public String getDir() {
								return "callapi";  //$NONXXX
							}
						},
						new ModelsTestData("nullsource",0,0),  //$NONXXX
						new ModelsTestData("objectExp",0,0),  //$NONXXX
						new ModelsTestData("objectExpBodyExpressions_253051",0,0),  //$NONXXX
						new ModelsTestData("oclAllInstances",0,0),  //$NONXXX
						new ModelsTestData("oclastype",0,0),  //$NONXXX
						new ModelsTestData("ocl_test",0,0,3),  //$NONXXX	// 3: preMarked
						new ModelsTestData("omittedobject",0,0),  //$NONXXX
						new ModelsTestData("omittedobjectwithinit",0,0),  //$NONXXX
						new TestData("OperationalTransformation1",0,0),  //$NONXXX
						new SourcesTestData("optionalout",0,0),  //$NONXXX
						new SourcesTestData("orderedsetdoesnotconformtoset",0,0),  //$NONXXX
						new PluginProjectTestData("OtherTransformation",0,0) {  //$NONXXX
														
							@Override
							public String getFolder() {
								return "transforms";  //$NONXXX
							}
							
							@Override
							public String getSubFolder() {
								return "root";  //$NONXXX
							}
							
							@Override
							public String getDir() {
								return "other";  //$NONXXX
							}
						},
						new SourcesTestData("outininitvar",0,0),  //$NONXXX
						new ModelsTestData("overload",0,0,3),  //$NONXXX	// 3: preMarked
						new ModelsTestData("overload_205062",0,0),  //$NONXXX
						new ModelsTestData("overload_multipleParams",0,0,2),  //$NONXXX	// 1: ordering of top level eClassifiers changed
						new ModelsTestData("overload_singleParam",0,0,2),  //$NONXXX	// 1: ordering of top level eClassifiers changed
						new TestData("OverridingMappings",0,0),  //$NONXXX
						new ModelsTestData("populationSection",0,0,1),  //$NONXXX	// 1: preMarked
						new ModelsTestData("primtypesecore",0,0),  //$NONXXX
						new ModelsTestData("propertycollect",0,0,1),  //$NONXXX	// 1*2: implicit set conversion of OCL analyzer,
						new SourcesTestData("props",0,0),  //$NONXXX
						new ExternLibTestData("q1",0,0) {  //$NONXXX
							@Override
							public String getDir() {
								return "org";  //$NONXXX
							}
						},
						new ModelsTestData("queries",0,0) {  //$NONXXX
							@Override
							public String getDir() {
								return "importedprops";  //$NONXXX
							}
						},
						new TestData("referencedLib",0,0) {  //$NONXXX
							@Override
							public String getFolder() {
								return "parserTestData";  //$NONXXX
							}
							
							@Override
							public String getSubFolder() {
								return "editor";  //$NONXXX
							}
							
							@Override
							public String getDir() {
								return "testhyperlinks";  //$NONXXX
							}
						},
						new ModelsTestData("ReferencedTransformation",0,0) {  //$NONXXX
							@Override
							public String getDir() {
								return "bug433937_referenced";  //$NONXXX
							}
						},
						new DeployedTestData("registeredDynamic",0,0) {  //$NONXXX
							@Override
							public String getDir() {
								return "dynamicmodel";  //$NONXXX
							}
						},
						new ModelsTestData("removeclassesinwhile",0,0),  //$NONXXX
						new ModelsTestData("resolveall",0,0,1+4+2),  //$NONXXX	// 1*2: implicit set conversion of OCL analyzer, 4*4: target names in resolve expressions, 1: ordering of top level eClassifiers changed
						new ModelsTestData("resolvebeforeoutcompletion",0,0,1),  //$NONXXX	// 1*4: target names in resolve expressions
						new ModelsTestData("resolvebyrule",0,0,1),  //$NONXXX	// 1*4: target names in resolve expressions
						new TestData("ResolveExpressions",0,0,2),  //$NONXXX	// 2*4: target names in resolve expressions
						new ModelsTestData("resolve_invresolveoneIn",0,0,1),  //$NONXXX	// 1*4: target names in resolve expressions
						new ModelsTestData("resolve_lateresolveoneIn",0,0),  //$NONXXX
						new ModelsTestData("resolve_lateresolveoneInaccess",0,0),  //$NONXXX
						new ModelsTestData("resolvenoinput",0,0,1),  //$NONXXX	// 1*4: target names in resolve expressions
						new ModelsTestData("resolve_notype",0,0,1+2),  //$NONXXX	// 1*2: implicit set conversion of OCL analyzer, 1: ordering of top level eClassifiers changed
						new ModelsTestData("resolve_resolveIn",0,0,1+1+2),  //$NONXXX	// 1*2: implicit set conversion of OCL analyzer, 1*4: target names in resolve expressions, 1: ordering of top level eClassifiers changed
						new ModelsTestData("resolve_resolveone",0,0,2),  //$NONXXX	// 1: ordering of top level eClassifiers changed
						new ModelsTestData("resolve_resolveoneIn",0,0,1+2),  //$NONXXX	// 1*4: target names in resolve expressions, 1: ordering of top level eClassifiers changed
						new ModelsTestData("resolve_type",0,0,1+1+2),  //$NONXXX	// 1*2: implicit set conversion of OCL analyzer, 1*4: target names in resolve expressions, 1: ordering of top level eClassifiers changed
						new ModelsTestData("resolve_vardecl",0,0,1+1+2),  //$NONXXX	// 1*2: implicit set conversion of OCL analyzer, 1*4: target names in resolve expressions, 1: ordering of top level eClassifiers changed
						new ModelsTestData("resolve_vardeclcond",0,0,1+2),  //$NONXXX	// 1*2: implicit set conversion of OCL analyzer, 1: ordering of top level eClassifiers changed
						new ModelsTestData("resolve_vardeclcondwithvar",0,0,1+2),  //$NONXXX	// 1*2: implicit set conversion of OCL analyzer, 1: ordering of top level eClassifiers changed
						new ModelsTestData("returnundefinedfromquery",0,0),  //$NONXXX
						new ModelsTestData("RootTransfForExtends",0,0) {  //$NONXXX
							@Override
							public String getDir() {
								return "fqn_noncontextual";  //$NONXXX
							}
						},
						new ModelsTestData("scr17812",0,0),  //$NONXXX
						new ModelsTestData("scr18514",0,0,2),  //$NONXXX	// 2: preMarked
						new ModelsTestData("scr18572",0,0,1),  //$NONXXX	// 1*4: target names in resolve expressions
						new ModelsTestData("scr18739",0,0),  //$NONXXX
						new ModelsTestData("scr18783",0,0),  //$NONXXX
						new ModelsTestData("scr19364",0,0),  //$NONXXX
						new ModelsTestData("scr20038",0,0),  //$NONXXX
						new ModelsTestData("scr20041",0,0),  //$NONXXX
						new ModelsTestData("scr20469",0,0),  //$NONXXX
						new ModelsTestData("scr20471",0,0),  //$NONXXX
						new ModelsTestData("scr20667",0,0,1),  //$NONXXX	// 1*4: target names in resolve expressions
						new ModelsTestData("scr20811",0,0),  //$NONXXX
						new ModelsTestData("scr21121",0,0),  //$NONXXX
						new ModelsTestData("scr21329",0,0,1),  //$NONXXX	// 1*4: target names in resolve expressions
						new ModelsTestData("scr23070",0,0),  //$NONXXX
						new ModelsTestData("scr878",0,0),  //$NONXXX
						new ModelsTestData("setundefinedtoprimitive",0,0,1),  //$NONXXX	// 1: preMarked
						new ModelsTestData("simple",0,0),  //$NONXXX
						new ModelsTestData("simpleconfigproperty",0,0),  //$NONXXX
						new ModelsTestData("simpleproperty",0,0),  //$NONXXX
						new ModelsTestData("simplerename",0,0),  //$NONXXX
						new ModelsTestData("simpleresolve",0,0,1),  //$NONXXX	// 1*4: target names in resolve expressions
						new ModelsTestData("simplestXCollectShorthand",0,0),  //$NONXXX
						new ModelsTestData("simpletag",0,0),  //$NONXXX
						new TestData("Simpleuml_To_Rdb",0,0,2) {  //$NONXXX
							@Override
							public String getPluginID() {
								return "org.eclipse.m2m.qvt.oml.samples";  //$NONXXX
							}
							
							@Override
							public String getFolder() {
								return "projects";  //$NONXXX
							}
							
							@Override
							public String getSubFolder() {
								return Path.EMPTY.toString();
							}
							
							@Override
							public String getDir() {
								return "org.eclipse.m2m.qvt.oml.samples.simpleuml2rdb";  //$NONXXX
							}
						},
						new ModelsTestData("skippopulation",0,0,1),  //$NONXXX	// 1: preMarked
						new ModelsTestData("slashSingleLineComments_266478",0,0),  //$NONXXX
						new ModelsTestData("somelib",0,0) {  //$NONXXX
							@Override
							public String getDir() {
								return "bug289982_importless";  //$NONXXX
							}
						},
						new ModelsTestData("stdlibelement",0,0),  //$NONXXX
						new ModelsTestData("stdlibList",0,0,2),  //$NONXXX	// 1: ordering of top level eClassifiers changed
						new ModelsTestData("stdlibModel",0,0),  //$NONXXX
						new ModelsTestData("stdlibString",0,0),  //$NONXXX
						new ModelsTestData("stringescaping",0,0),  //$NONXXX
						new ModelsTestData("subobjects",0,0),  //$NONXXX
						new ExternLibTestData("successLib",0,0),  //$NONXXX
						new DeployedTestData("T2",0,0) {  //$NONXXX
							@Override
							public String getDir() {
								return "a";  //$NONXXX
							}
						},
						new ModelsTestData("TestLib",0,0) {  //$NONXXX
							@Override
							public String getDir() {
								return "stdlibDict";  //$NONXXX
							}
						},
						new ModelsTestData("testlibrary",0,0) {  //$NONXXX
							@Override
							public String getDir() {
								return "libraryHeaderWithSignature_257575";  //$NONXXX
							}
						},
						new DeployedTestData("traceLookup_287589",0,0) {  //$NONXXX
							@Override
							public String getDir() {
								return "perf";  //$NONXXX
							}
						},
						new ModelsTestData("transf2",0,0) {  //$NONXXX
							@Override
							public String getDir() {
								return "importedExtents";  //$NONXXX
							}
						},
						new ModelsTestData("transf3",0,0) {  //$NONXXX
							@Override
							public String getDir() {
								return "importedExtents";  //$NONXXX
							}
						},
						new ModelsTestData("TransfForAccess",0,0) {  //$NONXXX
							@Override
							public String getDir() {
								return "operation_override";  //$NONXXX
							}
						},
						new ModelsTestData("transformationWithModuleElements_257055",0,0),  //$NONXXX
						new ModelsTestData("tuples",0,0,1),  //$NONXXX	// 1: preMarked
						new ApiTestData("twoInputs",0,0),  //$NONXXX
						new AntTestData("uml2rdb",0,0),  //$NONXXX
						new ModelsTestData("uml2_stereotypeApplication",0,0),  //$NONXXX
						new TestData("UMLFoo",0,0),  //$NONXXX
						new ModelsTestData("urilessModeltype",0,0),  //$NONXXX
						new ModelsTestData("usebooleanprop",0,0),  //$NONXXX
						new ModelsTestData("useresultinsameout",0,0),  //$NONXXX
						new ModelsTestData("util",0,0) {  //$NONXXX
							@Override
							public String getDir() {
								return "full";  //$NONXXX
							}
						},
						new ModelsTestData("Utils",0,0) {  //$NONXXX
							@Override
							public String getDir() {
								return "propinit";  //$NONXXX
							}
						},
						new ModelsTestData("varassign",0,0),  //$NONXXX
						new ModelsTestData("varInitExpWithResult_260985",0,0),  //$NONXXX
						new ModelsTestData("varInitGroup_261841",0,0),  //$NONXXX
						new ModelsTestData("virtscr20707",0,0),  //$NONXXX
						new ModelsTestData("virtualPredefinedTypeOpers",0,0),  //$NONXXX
						new ModelsTestData("voidreturn",0,0),  //$NONXXX
						new ModelsTestData("vutil",0,0) {  //$NONXXX
							@Override
							public String getDir() {
								return "virt";  //$NONXXX
							}
						},
						new DeployedTestData("WarmUp",0,0) {  //$NONXXX
							@Override
							public String getDir() {
								return "perf";  //$NONXXX
							}
						},
						new ModelsTestData("_while",0,0),  //$NONXXX
						new ModelsTestData("_while_261024",0,0)  //$NONXXX
						/**/
				}
				);
	}

	@Override
	@Before
	public void setUp() throws Exception {
		TestUtil.turnOffAutoBuilding();

		String name = "UnparserTest"; //$NON-NLS-1$
		myProject = TestProject.getExistingProject(name);
		if(myProject == null) {
			myProject = new TestProject(name, new String[] {}, 0);
		}
		File destinationFolder = getDestinationFolder();
		if (destinationFolder.exists()) {
			FileUtil.delete(destinationFolder);
		}
	}

	@Override
	@After
	public void tearDown() throws Exception {
		File destinationFolder = getDestinationFolder();
		if (destinationFolder.exists()) {
			FileUtil.delete(destinationFolder);
		}

		File unparseFolder = getUnparseFolder();
		if (unparseFolder.exists()) {
			FileUtil.delete(unparseFolder);
		}
	}

	public TestProject getTestProject() {
		return myProject;
	}

	@Override
	@Test
	public void runTest() throws Exception
	{
		File testdataFolder = copyTestdataIntoJunitWorkspace();

		CompiledUnit[] testdataCompiledUnit = compileAndCheckErrors(testdataFolder, myData.getName());

		IFile testdataCompiledXMI = loadCompiledXMIFile(testdataFolder, myData.getName());

		File unparseFolder = createUnparseFolder();
		
		String unitName = myData.getName() + UNPARSED_SUFFIX;
		
		unparseIntoFolder(testdataCompiledUnit[0], unparseFolder, unitName);

		CompiledUnit[] unparseCompiledUnit = compileAndCheckErrors(unparseFolder, unitName);

		IFile unparseCompiledXMI = loadCompiledXMIFile(unparseFolder, unitName);

		unparseIntoFolder(unparseCompiledUnit[0], unparseFolder, unitName + UNPARSED_SUFFIX);

		assertErrorCountEquality(testdataCompiledUnit, unparseCompiledUnit);

		assertEclipseCompareEquals(testdataCompiledXMI, unparseCompiledXMI);
	}

	private void assertEclipseCompareEquals(IFile testdataXMIResource, IFile unparseXMIResource)
	{
		String[] testdataLines = readAllLines(testdataXMIResource);
		String[] unparseLines = readAllLines(unparseXMIResource);

		QvtoxComparator testdataComparator = new QvtoxComparator(testdataLines);
		QvtoxComparator unparseComparator = new QvtoxComparator(unparseLines);

		RangeDifference[] differences = RangeDifferencer.findDifferences(testdataComparator,unparseComparator);

		String assertCopyEqualityMessage =
				myData.getEclipseDiffCount() == 0 ?
						"\nOriginal and copy expression differ at " + differences.length + " places" :
							"\nOriginal and copy expression differ at " + differences.length + " places instead of at " + myData.getEclipseDiffCount() + " places";
		for ( int i = 0; i < differences.length; i++ )
		{
			assertCopyEqualityMessage += "\n" + printRangeDifference(differences[i], testdataLines, unparseLines);
		}

		assertTrue(assertCopyEqualityMessage,differences.length==myData.getEclipseDiffCount());
	}

	private static class QvtoxComparator implements ITokenComparator
	{
		private String[] lines;
		private int[] lineStarts;

		public QvtoxComparator(String[] ln)
		{
			lines = ln;
			lineStarts = QvtoxComparator.calculateLineStarts(lines);
		}

		public int getRangeCount()
		{
			return lines.length;
		}

		public boolean rangesEqual(int thisIndex, IRangeComparator thatRangeComparator, int thatIndex)
		{
			String thisRange = lines[thisIndex];
			String thatRange = ((QvtoxComparator) thatRangeComparator).lines[thatIndex];
			if ( thisRange.contains("<lineBreaks") && thatRange.contains("<lineBreaks")) return true;
			if ( thisRange.contains("<offsets") && thatRange.contains("<offsets") ) return true;
			if ( thisRange.contains("<sourceURI") && thatRange.contains("<sourceURI") ) return true;
			boolean isEqual = thisRange.equals(thatRange);
			return isEqual;
		}

		public boolean skipRangeComparison(int length, int maxLength, IRangeComparator thatRangeComparator)
		{
			return false;
		}

		public int getTokenStart(int index)
		{
			return lineStarts[index];
		}

		public int getTokenLength(int index)
		{
			return lines[index].length();
		}

		private static int[] calculateLineStarts(String[] lines)
		{
			int[] lineStarts = new int[lines.length];
			for (int i = 1; i < lineStarts.length; i++)
			{
				lineStarts[i] = lineStarts[i-1] + lines[i-1].length();
			}
			return lineStarts;
		}
	}

	private String printRangeDifference(RangeDifference rangeDifference,String[] leftLines, String[] rightLines)
	{
		String result = rangeDifference.toString();

		for ( int i = 0, index = rangeDifference.leftStart(); i < rangeDifference.leftLength(); i++, index++)
		{
			result += "\n----LEFT:  " + leftLines[index];
		}
		for ( int i = 0, index = rangeDifference.rightStart(); i < rangeDifference.rightLength(); i++, index++)
		{
			result += "\n----RIGHT: " + rightLines[index];
		}

		return result;
	}

	private String[] readAllLines(IFile file)
	{
		ArrayList<String> lines = new ArrayList<String>();
		try
		{
			BufferedReader reader = new BufferedReader(new InputStreamReader(file.getContents()));
			String line = reader.readLine();
			while ( line != null )
			{
				lines.add(line);
				line = reader.readLine();
			}
			reader.close();
		}
		catch (Exception exp ){}

		return lines.toArray(new String[0]);
	}

	private File copyTestdataIntoJunitWorkspace() throws Exception
	{
		copyData(getOriginalPathSegment(), new Path(myData.getFolder()).append(getOriginalPathSegment()).toString());

		File folder = getDestinationFolder();

		assertTrue("Invalid folder " + folder, folder.exists() && folder.isDirectory()); //$NON-NLS-1$

		return folder;
	}

	private CompiledUnit[] compileAndCheckErrors(File folder, String unitName) throws Exception
	{
		CompiledUnit[] compiledUnits = compile(folder, unitName);

		assertTrue("No results", compiledUnits.length > 0); //$NON-NLS-1$

		List<QvtMessage> allErrors = getAllErrors(compiledUnits, myData.usesSourceAnnotations());

		String assertErrorCountEqualityMessage = "Compiled QVTo units in " + folder.getName() + " are not error free:";
		if ( allErrors.size() > myData.getErrCount() )
		{
			assertErrorCountEqualityMessage += "\n--- errors: ---";
			for (Iterator<QvtMessage> iterator = allErrors.iterator(); iterator.hasNext();)
			{
				QvtMessage qvtMessage = iterator.next();
				assertErrorCountEqualityMessage += "\n" + qvtMessage.getLineNum() + ":" + qvtMessage.getMessage();
			}
		}

		assertTrue(assertErrorCountEqualityMessage,allErrors.size() <= myData.getErrCount());

		return compiledUnits;
	}

	private CompiledUnit[] compile(File folder, String unitName) throws Exception
	{
		final String topName = unitName + MDAConstants.QVTO_FILE_EXTENSION_WITH_DOT;
		getFile(folder, topName);
		WorkspaceUnitResolver resolver = new WorkspaceUnitResolver(Collections.singletonList(getIFolder(folder)));
		QVTOCompiler compiler = new QVTOCompiler();

		QvtCompilerOptions options = new QvtCompilerOptions();
		options.setGenerateCompletionData(false);

		UnitProxy unit = resolver.resolveUnit(unitName);
		assert unit != null;

		CompiledUnit[] compiledUnits = new CompiledUnit[] { compiler.compile(unit, options, (IProgressMonitor)null) };

		saveCompiledUnitsAsCompiledXMI(compiler, compiledUnits);
		saveCompiledUnitsAsStandardXMI(compiler, compiledUnits);

		return compiledUnits;
	}

	private IFile loadCompiledXMIFile(File folder, String unitName)
	{
		IContainer sourcesFolder = getSourcesContainer();
		IPath sourcesLocation = sourcesFolder.getLocation();
		IPath folderPath = new Path(folder.getAbsolutePath());
		IPath relativePath = folderPath.makeRelativeTo(sourcesLocation);
		IFolder resourceFolder = sourcesFolder.getFolder(relativePath);
		IFile file = resourceFolder.getFile(unitName + '.' + ExeXMISerializer.COMPILED_XMI_FILE_EXTENSION);
		return file;
	}

	private void saveCompiledUnitsAsCompiledXMI(QVTOCompiler compiler, CompiledUnit[] compiledUnits) throws IOException
	{
		ResourceSet metamodelResourceSet = compiler.getResourceSet();
		Registry registry = MetamodelURIMappingHelper.mappingsToEPackageRegistry(myProject.project, metamodelResourceSet);
		ExeXMISerializer.saveUnitXMI(compiledUnits, registry != null ? registry : EPackage.Registry.INSTANCE);
	}

	private void saveCompiledUnitsAsStandardXMI(QVTOCompiler compiler, CompiledUnit[] compiledUnits) throws Exception, EmfException
	{
		CompiledUnit compiledUnit = compiledUnits[0];
		URI copyURI = compiledUnit.getURI().trimFileExtension().appendFileExtension(XMIResource.XMI_NS);

		Resource copyResource = EmfUtil.createResource(copyURI, EmfUtil.getOutputResourceSet());
		copyResource.getContents().addAll(compiledUnit.getModules());

		EmfUtil.saveModel(copyResource, EmfUtil.DEFAULT_SAVE_OPTIONS);

		myProject.getProject().refreshLocal(IResource.DEPTH_INFINITE, null);
	}

	private File createUnparseFolder() throws Exception
	{
		createFolder(getUnparsePathSegment());
		File copyFolder = getUnparseFolder();
		assertTrue("Invalid folder " + copyFolder, copyFolder.exists() && copyFolder.isDirectory()); //$NON-NLS-1$
		return copyFolder;
	}

	private void unparseIntoFolder(CompiledUnit compiledUnit, File unparseFolder, String fileName) throws Exception, EmfException
	{
		String copyPath =
				myProject.getProject().getFullPath().
				append(getUnparsePathSegment()).
				append(fileName).addFileExtension(MDAConstants.QVTO_FILE_EXTENSION).toString();
		URI copyURI = URI.createPlatformResourceURI(copyPath,true);

		Resource copyResource = EmfUtil.createResource(copyURI, EmfUtil.getOutputResourceSet());
		copyResource.getContents().addAll(compiledUnit.getModules());

		Map<Object, Object> options = new LinkedHashMap<Object, Object>(EmfUtil.DEFAULT_SAVE_OPTIONS);
		options.put(QVTEvaluationOptions.FLAG_QVTO_UNPARSE_ENABLED, Boolean.TRUE);

		EmfUtil.saveModel(copyResource, options);

		myProject.getProject().refreshLocal(IResource.DEPTH_INFINITE, null);
	}

	private void assertErrorCountEquality(CompiledUnit[] testdataCompiledUnit, CompiledUnit[] unparseCompiledUnit) {
		String assertErrorCountEqualityMessage = "Errors in original and copy: ";
		int testdataErrorCount = getAllErrors(testdataCompiledUnit, myData.usesSourceAnnotations()).size();
		int unparseErrorCount = getAllErrors(unparseCompiledUnit, myData.usesSourceAnnotations()).size();
		if ( unparseErrorCount != testdataErrorCount )
		{
			assertErrorCountEqualityMessage += "\n--- original errors: ---";
			for (Iterator<QvtMessage> iterator = getAllErrors(testdataCompiledUnit, myData.usesSourceAnnotations()).iterator(); iterator.hasNext();)
			{
				QvtMessage qvtMessage = iterator.next();
				assertErrorCountEqualityMessage += "\n" + qvtMessage.getLineNum() + ":" + qvtMessage.getMessage();
			}
			assertErrorCountEqualityMessage += "\n--- errors in copy: ---";
			for (Iterator<QvtMessage> iterator = getAllErrors(unparseCompiledUnit, myData.usesSourceAnnotations()).iterator(); iterator.hasNext();)
			{
				QvtMessage qvtMessage = iterator.next();
				assertErrorCountEqualityMessage += "\n" + qvtMessage.getLineNum() + ":" + qvtMessage.getMessage();
			}
		}

		assertErrorCountEqualityMessage += "\nError count of copy mismatches: ";
		if ( unparseErrorCount > myData.getErrCount() )
			assertEquals(assertErrorCountEqualityMessage, testdataErrorCount,unparseErrorCount);
	}

	private String getOriginalPathSegment()
	{
		return new Path(myData.getSubFolder()).append(myData.getDir()).toString();
	}

	private static final String UNPARSED_SUFFIX = "-unparsed";

	private String getUnparsePathSegment() {
		return getOriginalPathSegment() + UNPARSED_SUFFIX;
	}

	private File getDestinationFolder()
	{
		return myProject.getProject().getLocation().append(getOriginalPathSegment()).toFile();
	}

	private IContainer getSourcesContainer()
	{
		IProject project = myProject.getProject();
		IPath path = project.getLocation().append(myData.getSubFolder());
		IContainer sourcesFolder = project.getWorkspace().getRoot().getContainerForLocation(path);
		return sourcesFolder;
	}

	private File getUnparseFolder()
	{
		return myProject.getProject().getLocation().append(getUnparsePathSegment()).toFile();
	}

	private List<QvtMessage> getAllErrors(CompiledUnit[] compiled, boolean concreteSyntaxOnly) {
		List<QvtMessage> errors = new ArrayList<QvtMessage>();
		for (CompiledUnit compilationResult : compiled) {
			TransformationUtil.getErrors(compilationResult, errors, concreteSyntaxOnly);
		}

		return errors;
	}

	private static File getFile(File folder, final String expectedName) {
		File file = new File(folder, expectedName);
		assertTrue("Inexistent file: " + file, file.exists()); //$NON-NLS-1$
		assertTrue("Non-File file: " + file, file.isFile()); //$NON-NLS-1$
		return file;
	}

	private IContainer getIFolder(File folderUnderWorkspace) throws MalformedURLException, URISyntaxException {
		IWorkspace workspace = ResourcesPlugin.getWorkspace();
		IContainer[] containers = workspace.getRoot().findContainersForLocationURI(folderUnderWorkspace.toURI());
		if(containers == null || containers.length != 1 || containers[0] instanceof IFolder == false) {
			throw new RuntimeException("Folder not found: " + folderUnderWorkspace); //$NON-NLS-1$
		}

		return containers[0];
	}

	private void copyData(String destPath, String srcPath) throws Exception
	{
		File sourceFolder = TestUtil.getPluginRelativeFile(myData.getPluginID(), srcPath);
		File destFolder = createFolder(destPath);
		FileUtil.copyFolder(sourceFolder, destFolder);
		myProject.getProject().refreshLocal(IResource.DEPTH_INFINITE, null);
	}

	private File createFolder(String destPath) throws Exception
	{
		File destFolder = myProject.getProject().getLocation().append(destPath).toFile();
		destFolder.mkdirs();
		myProject.getProject().refreshLocal(IResource.DEPTH_INFINITE, null);
		return destFolder;
	}

	private final TestData myData;
	private TestProject myProject;
}
