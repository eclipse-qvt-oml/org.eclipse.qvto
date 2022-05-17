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
import org.eclipse.core.runtime.IProgressMonitor;
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
		super(data.getDir());
		myData = data;
	}

	@Parameters(name="{0}")
	public static Iterable<TestData> data() {
		return Arrays.asList(
				new TestData[] {
						new TestData("A",0,0,1),  //$NONXXX	// 1*4: target names in resolve expressions
						new TestData("abstractresult",0,0,1),  //$NONXXX	//1: preMarked
						new TestData("accessbooleans",0,0),  //$NONXXX
						new TestData("addclass",0,0),  //$NONXXX
						new TestData("addclassviamodificationininit",0,0,1),  //$NONXXX	//1: preMarked
						new TestData("addclassviaoutinocl",0,0),  //$NONXXX
						new TestData("addclassviasequence",0,0),  //$NONXXX
						new TestData("addrealtostring",0,0),  //$NONXXX
						new TestData("addundefined",0,0,26),  //$NONXXX	//26: preMarked
						new TestData("allinstances",0,0),  //$NONXXX
						new TestData("AnotherTransformation",0,0),  //$NONXXX
						new TestData("AnyExtension",0,0),  //$NONXXX
						new TestData("assert_log_minimal",0,0),  //$NONXXX
						new TestData("assert_log",0,0),  //$NONXXX
						new TestData("assignresultininit",0,0),  //$NONXXX
						new TestData("assigntonullowner",0,0,1),  //$NONXXX	//1: preMarked
						new TestData("assigntoprimfeature",0,0,1),  //$NONXXX	//1: preMarked
						new TestData("auxtransf",0,0),  //$NONXXX
						new TestData("b1",0,0),  //$NONXXX
						new TestData("bagorderedsetintersection",0,0),  //$NONXXX
						new TestData("Base",0,0),  //$NONXXX
						new TestData("blackboxlib_237781",0,0),  //$NONXXX
						new TestData("blackboxlib_uri",0,0),  //$NONXXX
						new TestData("blackboxlib_annotation_java",0,0),  //$NONXXX
						new TestData("blackboxlibASTmodel",0,0),  //$NONXXX
						new TestData("blackboxlib_context",0,0),  //$NONXXX
						new TestData("bodywithsemicolon",0,0),  //$NONXXX
						new TestData("boxing",0,0),  //$NONXXX
						new TestData("bug204126_1",0,0,1),  //$NONXXX // 4: target names in resolve expressions
						new TestData("bug204126_2",0,0,1),  //$NONXXX // 4: target names in resolve expressions
						new TestData("bug204126_3",0,0,1),  //$NONXXX // 4: target names in resolve expressions
						new TestData("bug204126_4",0,0,1),  //$NONXXX // 4: target names in resolve expressions
						new TestData("bug204126_5",0,0,1),  //$NONXXX // 4: target names in resolve expressions
						new TestData("bug204126_6",0,0,1),  //$NONXXX // 4: target names in resolve expressions
						new TestData("bug204126_7",0,0,1),  //$NONXXX // 4: target names in resolve expressions
						new TestData("bug205303_1",0,0,1+4),  //$NONXXX // 1:preMarked,16: target names in resolve expressions
						new TestData("bug205303_2",0,0,1+6),  //$NONXXX // 1:preMarked,24: target names in resolve expressions
						new TestData("bug_214938",0,0),  //$NONXXX
						new TestData("bug216317",0,0,2),  //$NONXXX // 2*4: target names in resolve expressions
						new TestData("bug219075_1",0,0),  //$NONXXX
						new TestData("bug224094",0,0),  //$NONXXX
						new TestData("bug233984",0,0,2),  //$NONXXX	// 1: ordering of top level eClassifiers changed
						new TestData("bug2437_1",0,0,4),  //$NONXXX // 4*2: implicit set conversion of OCL analyzer
						new TestData("bug2437_2",0,0),  //$NONXXX
						new TestData("bug2437_3",0,0),  //$NONXXX
						new TestData("bug2437_4",0,0,1),  //$NONXXX // 1*2: implicit set conversion of OCL analyzer
						new TestData("bug2437_5",0,0,1),  //$NONXXX // 1*2: implicit set conversion of OCL analyzer
						new TestData("bug244701",0,0),  //$NONXXX
						new TestData("bug267917",0,0),  //$NONXXX
						new TestData("bug268636lib",0,0),  //$NONXXX
						new TestData("bug272869lib",0,0),  //$NONXXX
						new TestData("bug2732",0,0,2),  //$NONXXX
						new TestData("bug2741",0,0),  //$NONXXX
						new TestData("bug274105_274505",0,0,9),  //$NONXXX // 9*2: implicit set conversion of OCL analyzer
						new TestData("bug2787",0,0,1),  //$NONXXX // 1*2: implicit set conversion of OCL analyzer
						new TestData("bug2839",0,0,1),  //$NONXXX // 1*2: implicit set conversion of OCL analyzer
						new TestData("bug289982_ambiguousLib",0,0),  //$NONXXX
						new TestData("bug289982_lib",0,0),  //$NONXXX
						new TestData("bug289982_lib_failed",0,0),  //$NONXXX
						new TestData("bug289982_undefinedLib",0,0),  //$NONXXX
						new TestData("bug294127",0,0),  //$NONXXX
						new TestData("bug301134",0,0),  //$NONXXX
						new TestData("bug314443",0,0,2),  //$NONXXX	// 1: ordering of top level eClassifiers changed
						new TestData("bug323915",0,0),  //$NONXXX
						new TestData("bug325192",0,0,4),  //$NONXXX // 4*2: implicit set conversion of OCL analyzer
						new TestData("bug326871",0,0),  //$NONXXX
						new TestData("bug326871_lib",0,0),  //$NONXXX
						new TestData("bug326871_standalone",0,0),  //$NONXXX
						new TestData("bug370098",0,0),  //$NONXXX
						new TestData("bug377882",0,0,1),  //$NONXXX	// 1*4: target names in resolve expressions
						new TestData("bug388325",0,0),  //$NONXXX
						new TestData("bug388801",0,0),  //$NONXXX
						new TestData("bug392156",0,0,2+12),  //$NONXXX	//2*2: implicit set conversion of OCL analyzer, 12*4: target names in resolve expressions
						new TestData("bug392429",0,0),  //$NONXXX
						new TestData("bug397215",0,0),  //$NONXXX
						new TestData("bug397398",0,0),  //$NONXXX
						new TestData("bug400233_lib",0,0),  //$NONXXX
						new TestData("bug400720",0,0),  //$NONXXX
						new TestData("bug404647",0,0),  //$NONXXX
						new TestData("bug410470",0,0),  //$NONXXX
						new TestData("bug413130",0,0),  //$NONXXX
						new TestData("bug413131",0,0,1),  //$NONXXX	//42: t6 EAttribute vs. EReference
						new TestData("bug414363",0,0),  //$NONXXX
						new TestData("bug414472",0,0),  //$NONXXX
						new TestData("bug414619",0,0),  //$NONXXX
						new TestData("bug414642",0,0),  //$NONXXX
						new TestData("bug415024",0,0),  //$NONXXX
						new TestData("bug415029",0,0),  //$NONXXX
						new TestData("bug415209",0,0),  //$NONXXX
						new TestData("bug415310",0,0),  //$NONXXX
						new TestData("bug415315",0,0),  //$NONXXX
						new TestData("bug415661",0,0),  //$NONXXX
						new TestData("bug416584lib",0,0),  //$NONXXX
						new TestData("bug417751",0,0),  //$NONXXX
						new TestData("bug417779",0,0),  //$NONXXX
						new TestData("bug417996",0,0,2),  //$NONXXX	// 2: order of properties changed
						new TestData("bug418512",0,0),  //$NONXXX
						new TestData("bug418961_lib",0,0),  //$NONXXX
						new TestData("bug419299",0,0),  //$NONXXX
						new TestData("bug422315",0,0),  //$NONXXX
						new TestData("bug424086",0,0,2),  //$NONXXX	// 2: target names in resolve expressions
						new TestData("bug424584",0,0),  //$NONXXX
						new TestData("bug424740",0,0),  //$NONXXX
						new TestData("bug424896",0,0),  //$NONXXX
						new TestData("bug424912",0,0,1),  //$NONXXX	// 1*4: target names in resolve expressions
						new TestData("bug424979",0,0,10),  //$NONXXX	// 10*2: implicit set conversion of OCL analyzer
						new TestData("bug425069a",0,0),  //$NONXXX
						new TestData("bug425634",0,0,10),  //$NONXXX	// 10*4: target names in resolve expressions
						new TestData("bug427237",0,0),  //$NONXXX
						new TestData("bug427348",0,0),  //$NONXXX
						new TestData("bug428028",0,0),  //$NONXXX
						new TestData("bug428316",0,0),  //$NONXXX
						new TestData("bug428618",0,0,1),  //$NONXXX // 1*2: implicit set conversion of OCL analyzer
						new TestData("bug432786",0,0,2),  //$NONXXX	// 2*4: target names in resolve expressions
						new TestData("bug433292",0,0,1),  //$NONXXX	// 1*4: target names in resolve expressions
						new TestData("bug433585",0,0),  //$NONXXX
						new TestData("bug440514",0,0),  //$NONXXX
						new TestData("bug449445",0,0),  //$NONXXX
						new TestData("bug449912",0,0),  //$NONXXX
						new TestData("bug457433",0,0),  //$NONXXX
						new TestData("bug463395",0,0),  //$NONXXX
						new TestData("bug463396",0,0),  //$NONXXX
						new TestData("bug463572_lib",0,0),  //$NONXXX
						new TestData("bug466705",0,0),  //$NONXXX
						new TestData("bug467325",0,0),  //$NONXXX
						new TestData("bug467600",0,0),  //$NONXXX
						new TestData("bug467600_Bag",0,0),  //$NONXXX
						new TestData("bug467600_Collection",0,0),  //$NONXXX
						new TestData("bug467600_List",0,0),  //$NONXXX
						new TestData("bug467600_OrderedSet",0,0),  //$NONXXX
						new TestData("bug467600_Sequence",0,0),  //$NONXXX
						new TestData("bug467600_Set",0,0),  //$NONXXX
						new TestData("bugzilla443",0,0),  //$NONXXX
						new TestData("C",0,0),  //$NONXXX
						new TestData("calldump",0,0),  //$NONXXX
						new TestData("calloclIsUndefinedforundefined",0,0),  //$NONXXX
						new TestData("callvirtforundefined",0,0),  //$NONXXX
						new TestData("castinttodouble",0,0),  //$NONXXX
						new TestData("chainedAssignments_261024",0,0),  //$NONXXX
						new TestData("CheckMemoryLeak",0,0),  //$NONXXX
						new TestData("ChildInTreeInput",0,0),  //$NONXXX
						new TestData("collectionMappingResult",0,0),  //$NONXXX
						new TestData("collectreturntype",0,0),  //$NONXXX
						new TestData("ColorSettingPreviewCode",0,0),  //$NONXXX
						new TestData("compatible",0,0),  //$NONXXX
						new TestData("computeExp_250403",0,0),  //$NONXXX
						new TestData("configpropstype",0,0),  //$NONXXX
						new TestData("constructors",0,0),  //$NONXXX
						new TestData("contextlesscall",0,0,1),  //$NONXXX	//1: preMarked
						new TestData("continue_break",0,0),  //$NONXXX
						new TestData("continue_perf",0,0),  //$NONXXX
						new TestData("copynameviacontextmapping",0,0),  //$NONXXX
						new TestData("customop",0,0),  //$NONXXX
						new TestData("deprecated_importLocation",0,0),  //$NONXXX
						new TestData("deprecated_rename",0,0),  //$NONXXX
						new TestData("dicttype",0,0),  //$NONXXX
						new TestData("doubleQuoteStrings_262734",0,0),  //$NONXXX
						new TestData("dupImportLibrary",0,0),  //$NONXXX
						new TestData("DuplicatedNamesDetection",0,0,2),  //$NONXXX	// 1: ordering of top level eClassifiers changed
						new TestData("dynamicpackage",0,0),  //$NONXXX
						new TestData("Ecore2Ecore",0,0),  //$NONXXX
						new TestData("Ecore2EcoreExt",0,0),  //$NONXXX
						new TestData("ecore2uml_",0,0),  //$NONXXX
						//new TestData("Ecore2UML",0,0,1),  //$NONXXX	// 1*4: target names in resolve expressions
						new TestData("emptyExtents",0,0),  //$NONXXX
						new TestData("emptyinit",0,0),  //$NONXXX
						new TestData("emptymodule",0,0),  //$NONXXX
						new TestData("emptyout",0,0),  //$NONXXX
						new TestData("endsectfull",0,0),  //$NONXXX
						new TestData("endsectimplicitpopulation",0,0),  //$NONXXX
						new TestData("endsectimplicitpopulationnoinit",0,0),  //$NONXXX
						new TestData("endsectinitnopopulation",0,0),  //$NONXXX
						new TestData("endsectonly",0,0),  //$NONXXX
						new TestData("endsectpopulationnoinit",0,0),  //$NONXXX
						new TestData("endsectresultpatch",0,0),  //$NONXXX
						new TestData("entryOp",0,0),  //$NONXXX
						new TestData("equndefined",0,0,28),  //$NONXXX // 28:preMarked
						new TestData("escape_sequences_250630",0,0),  //$NONXXX
						new TestData("exec1",0,0),  //$NONXXX
						new TestData("exec2",0,0),  //$NONXXX
						new TestData("exec3",0,0),  //$NONXXX
						new TestData("exec3_lib",0,0),  //$NONXXX
						new TestData("exists",0,0),  //$NONXXX
						new TestData("ExtendedLibrary",0,0),  //$NONXXX
						new TestData("firstlast",0,0),  //$NONXXX
						new TestData("firstunparsertest",0,0),  //$NONXXX
						new TestData("FooLibImport",0,0),  //$NONXXX
						new TestData("forExp_245275",0,0),  //$NONXXX
						new TestData("getpropfromundefined",0,0),  //$NONXXX
						new TestData("HelpersAndQueries",0,0),  //$NONXXX
						new TestData("helperSimpleDef_252173",0,0),  //$NONXXX
						new TestData("imp",0,0),  //$NONXXX
						new TestData("implicitpopulation",0,0),  //$NONXXX
						new TestData("implicitpopulationwithinit",0,0),  //$NONXXX
						new TestData("implicitSrcImport",0,0),  //$NONXXX
						new TestData("imported",0,0),  //$NONXXX
						new TestData("imported2",0,0),  //$NONXXX
						new TestData("importedFileUnit",0,0),  //$NONXXX
						new TestData("ImportedLib",0,0),  //$NONXXX
						new TestData("ImportedMappingTest",0,0,1),  //$NONXXX // 1: preMarked
						new TestData("ImportedTransf1",0,0,2),  //$NONXXX	// 1: ordering of top level eClassifiers changed
						new TestData("ImportedTransf2",0,0,2),  //$NONXXX	// 1: ordering of top level eClassifiers changed
						new TestData("ImportedTransf4",0,0),  //$NONXXX
						new TestData("incompatible",0,0),  //$NONXXX
						new TestData("initsectresultpatch",0,0),  //$NONXXX
						new TestData("inoutcontextparam",0,0),  //$NONXXX
						new TestData("inoutcontextparamnoresult",0,0),  //$NONXXX
						new TestData("inoutMapping",0,0),  //$NONXXX
						new TestData("InplaceEcore",0,0),  //$NONXXX
						new TestData("IntermediateData",0,0),  //$NONXXX
						new TestData("intermediateprop_imported",0,0),  //$NONXXX
						new TestData("intermediateprop_resolve",0,0,2),  //$NONXXX	// 2: preMarked
						new TestData("intermediateprop_trace",0,0),  //$NONXXX
						new TestData("intermProperties",0,0,4),  //$NONXXX	// 2: ordering of properties changed
						new TestData("intermSimple",0,0,2),  //$NONXXX	// 1: ordering of top level eClassifiers changed
						new TestData("intermWithCrossRefs",0,0,2),  //$NONXXX	// 1: ordering of top level eClassifiers changed
						new TestData("intermWithExtends",0,0),  //$NONXXX
						new TestData("intermWithoutExtent",0,0,2),  //$NONXXX	// 1: ordering of top level eClassifiers changed
						new TestData("invalidcollectioncast",0,0,2),  //$NONXXX	// 1: ordering of top level eClassifiers changed
						new TestData("invalidConfigProp",0,0),  //$NONXXX
						new TestData("invresolve_",0,0,1),  //$NONXXX	// 1*4: target names in resolve expressions
						new TestData("invresolvebyrule",0,0,1+1),  //$NONXXX	// 1: preMarked, 1*4: target names in resolve expressions
						new TestData("isunique",0,0),  //$NONXXX
						new TestData("iterateoverintset",0,0),  //$NONXXX
						new TestData("iteratetest",0,0),  //$NONXXX
						new TestData("javakeywords",0,0),  //$NONXXX
						new TestData("lateresolve",0,0,2+1),  //$NONXXX	// 2: preMarked, 1*4: target names in resolve expressions
						new TestData("LateResolveBase",0,0),  //$NONXXX
						new TestData("lateresolvebyrule",0,0,2+1),  //$NONXXX	// 2: preMarked, 1*4: target names in resolve expressions
						new TestData("lateresolve_many",0,0,3),  //$NONXXX	// 2: ordering of top level eClassifiers changed
						new TestData("Lib",0,0),  //$NONXXX
						new TestData("Lib1",0,0),  //$NONXXX
						new TestData("Lib2",0,0),  //$NONXXX
						new TestData("LibForAccess",0,0),  //$NONXXX
						new TestData("LibForExtends",0,0),  //$NONXXX
						new TestData("listLiteral_259754",0,0),  //$NONXXX
						new TestData("localstrings",0,0),  //$NONXXX
						new TestData("m3",0,0),  //$NONXXX
						new TestData("mapDisjuncts",0,0,5),  //$NONXXX	// 5*4: target names in resolve expressions
						new TestData("mapInherits",0,0,4),  //$NONXXX	// 4*4: target names in resolve expressions
						new TestData("mapkeyword",0,0,1),  //$NONXXX	// 1: preMarked
						new TestData("mapMerges",0,0,4),  //$NONXXX	// 4*4: target names in resolve expressions
						new TestData("mapMultipleInherits",0,0),  //$NONXXX
						new TestData("MappingBody",0,0),  //$NONXXX
						new TestData("mappingBodyExpressions_252358",0,0),  //$NONXXX
						new TestData("MappingExtensionDisjuncts",0,0),  //$NONXXX
						new TestData("MappingExtensionInherits",0,0),  //$NONXXX
						new TestData("MappingExtensionMerges",0,0),  //$NONXXX
						new TestData("Mappings",0,0),  //$NONXXX
						new TestData("MappingsWhenClause",0,0),  //$NONXXX
						new TestData("mappingWithNoResultTrace_266854",0,0,1),  //$NONXXX	// 1*4: target names in resolve expressions
						new TestData("markedTest",0,0),  //$NONXXX
						new TestData("misplacedTopElements",0,0),  //$NONXXX
						new TestData("mm_header1",0,0),  //$NONXXX
						new TestData("mm_header2",0,0),  //$NONXXX
						new TestData("mm_header3",0,0),  //$NONXXX
						new TestData("mm_header4",0,0),  //$NONXXX
						new TestData("mm_modifyvar",0,0),  //$NONXXX
						new TestData("ModelExtents",0,0),  //$NONXXX
						new TestData("modifyfeature",0,0),  //$NONXXX
						new TestData("modifyparam",0,0),  //$NONXXX
						new TestData("modifyresult",0,0),  //$NONXXX
						new TestData("modifyvar",0,0),  //$NONXXX
						new TestData("moduleProperty",0,0),  //$NONXXX
						new TestData("multilineStrings_262733",0,0),  //$NONXXX
						new TestData("multipletracerecords",0,0,1),  //$NONXXX	// 1*4: target names in resolve expressions
						new TestData("multiresultpars",0,0,1),  //$NONXXX	// 1*4: target names in resolve expressions
						new TestData("nested",0,0,1),  //$NONXXX	// 1*4: target names in resolve expressions
						new TestData("nestednativeops",0,0),  //$NONXXX
						new TestData("nestedPropertiesAssignment_262757",0,0,2+1),  //$NONXXX	// 2*2: implicit set conversion of OCL analyzer, 1*4: target names in resolve expressions
						new TestData("NestedTransformation",0,0),  //$NONXXX
						new TestData("NewLibrary",0,0),  //$NONXXX
						new TestData("NewTransformation",0,0),  //$NONXXX
						new TestData("nocollectoncollection",0,0),  //$NONXXX
						new TestData("noglobalallinstances",0,0,2),  //$NONXXX	// 1: ordering of top level eClassifiers changed
						new TestData("NonExecutable",0,0),  //$NONXXX
						new TestData("nullsource",0,0),  //$NONXXX
						new TestData("objectExp",0,0),  //$NONXXX
						new TestData("objectExpBodyExpressions_253051",0,0),  //$NONXXX
						new TestData("oclAllInstances",0,0),  //$NONXXX
						new TestData("oclastype",0,0),  //$NONXXX
						new TestData("ocl_test",0,0,3),  //$NONXXX	// 3: preMarked
						new TestData("omittedobject",0,0),  //$NONXXX
						new TestData("omittedobjectwithinit",0,0),  //$NONXXX
						new TestData("OperationalTransformation1",0,0),  //$NONXXX
						new TestData("optionalout",0,0),  //$NONXXX
						new TestData("orderedsetdoesnotconformtoset",0,0),  //$NONXXX
						new TestData("OtherTransformation",0,0),  //$NONXXX
						new TestData("outininitvar",0,0),  //$NONXXX
						new TestData("overload",0,0,3),  //$NONXXX	// 3: preMarked
						new TestData("overload_205062",0,0),  //$NONXXX
						new TestData("overload_multipleParams",0,0,2),  //$NONXXX	// 1: ordering of top level eClassifiers changed
						new TestData("overload_singleParam",0,0,2),  //$NONXXX	// 1: ordering of top level eClassifiers changed
						new TestData("OverridingMappings",0,0),  //$NONXXX
						new TestData("populationSection",0,0,1),  //$NONXXX	// 1: preMarked
						new TestData("primtypesecore",0,0),  //$NONXXX
						new TestData("propertycollect",0,0,1),  //$NONXXX	// 1*2: implicit set conversion of OCL analyzer,
						new TestData("props",0,0),  //$NONXXX
						new TestData("q1",0,0),  //$NONXXX
						new TestData("queries",0,0),  //$NONXXX
						new TestData("referencedLib",0,0),  //$NONXXX
						new TestData("ReferencedTransformation",0,0),  //$NONXXX
						new TestData("registeredDynamic",0,0),  //$NONXXX
						new TestData("removeclassesinwhile",0,0),  //$NONXXX
						new TestData("resolveall",0,0,1+4+2),  //$NONXXX	// 1*2: implicit set conversion of OCL analyzer, 4*4: target names in resolve expressions, 1: ordering of top level eClassifiers changed
						new TestData("resolvebeforeoutcompletion",0,0,1),  //$NONXXX	// 1*4: target names in resolve expressions
						new TestData("resolvebyrule",0,0,1),  //$NONXXX	// 1*4: target names in resolve expressions
						new TestData("ResolveExpressions",0,0,2),  //$NONXXX	// 2*4: target names in resolve expressions
						new TestData("resolve_invresolveoneIn",0,0,1),  //$NONXXX	// 1*4: target names in resolve expressions
						new TestData("resolve_lateresolveoneIn",0,0),  //$NONXXX
						new TestData("resolve_lateresolveoneInaccess",0,0),  //$NONXXX
						new TestData("resolvenoinput",0,0,1),  //$NONXXX	// 1*4: target names in resolve expressions
						new TestData("resolve_notype",0,0,1+2),  //$NONXXX	// 1*2: implicit set conversion of OCL analyzer, 1: ordering of top level eClassifiers changed
						new TestData("resolve_resolveIn",0,0,1+1+2),  //$NONXXX	// 1*2: implicit set conversion of OCL analyzer, 1*4: target names in resolve expressions, 1: ordering of top level eClassifiers changed
						new TestData("resolve_resolveone",0,0,2),  //$NONXXX	// 1: ordering of top level eClassifiers changed
						new TestData("resolve_resolveoneIn",0,0,1+2),  //$NONXXX	// 1*4: target names in resolve expressions, 1: ordering of top level eClassifiers changed
						new TestData("resolve_type",0,0,1+1+2),  //$NONXXX	// 1*2: implicit set conversion of OCL analyzer, 1*4: target names in resolve expressions, 1: ordering of top level eClassifiers changed
						new TestData("resolve_vardecl",0,0,1+1+2),  //$NONXXX	// 1*2: implicit set conversion of OCL analyzer, 1*4: target names in resolve expressions, 1: ordering of top level eClassifiers changed
						new TestData("resolve_vardeclcond",0,0,1+2),  //$NONXXX	// 1*2: implicit set conversion of OCL analyzer, 1: ordering of top level eClassifiers changed
						new TestData("resolve_vardeclcondwithvar",0,0,1+2),  //$NONXXX	// 1*2: implicit set conversion of OCL analyzer, 1: ordering of top level eClassifiers changed
						new TestData("returnundefinedfromquery",0,0),  //$NONXXX
						new TestData("RootTransfForExtends",0,0),  //$NONXXX
						new TestData("scr17812",0,0),  //$NONXXX
						new TestData("scr18514",0,0,2),  //$NONXXX	// 2: preMarked
						new TestData("scr18572",0,0,1),  //$NONXXX	// 1*4: target names in resolve expressions
						new TestData("scr18739",0,0),  //$NONXXX
						new TestData("scr18783",0,0),  //$NONXXX
						new TestData("scr19364",0,0),  //$NONXXX
						new TestData("scr20038",0,0),  //$NONXXX
						new TestData("scr20041",0,0),  //$NONXXX
						new TestData("scr20469",0,0),  //$NONXXX
						new TestData("scr20471",0,0),  //$NONXXX
						new TestData("scr20667",0,0,1),  //$NONXXX	// 1*4: target names in resolve expressions
						new TestData("scr20811",0,0),  //$NONXXX
						new TestData("scr21121",0,0),  //$NONXXX
						new TestData("scr21329",0,0,1),  //$NONXXX	// 1*4: target names in resolve expressions
						new TestData("scr23070",0,0),  //$NONXXX
						new TestData("scr878",0,0),  //$NONXXX
						new TestData("setundefinedtoprimitive",0,0,1),  //$NONXXX	// 1: preMarked
						new TestData("simple",0,0),  //$NONXXX
						new TestData("simpleconfigproperty",0,0),  //$NONXXX
						new TestData("simpleproperty",0,0),  //$NONXXX
						new TestData("simplerename",0,0),  //$NONXXX
						new TestData("simpleresolve",0,0,1),  //$NONXXX	// 1*4: target names in resolve expressions
						new TestData("simplestXCollectShorthand",0,0),  //$NONXXX
						new TestData("simpletag",0,0),  //$NONXXX
						new TestData("Simpleuml_To_Rdb",0,0,2),  //$NONXXX
						new TestData("skippopulation",0,0,1),  //$NONXXX	// 1: preMarked
						new TestData("slashSingleLineComments_266478",0,0),  //$NONXXX
						new TestData("somelib",0,0),  //$NONXXX
						new TestData("stdlibelement",0,0),  //$NONXXX
						new TestData("stdlibList",0,0,2),  //$NONXXX	// 1: ordering of top level eClassifiers changed
						new TestData("stdlibModel",0,0),  //$NONXXX
						new TestData("stdlibString",0,0),  //$NONXXX
						new TestData("stringescaping",0,0),  //$NONXXX
						new TestData("subobjects",0,0),  //$NONXXX
						new TestData("successLib",0,0),  //$NONXXX
						new TestData("T2",0,0),  //$NONXXX
						new TestData("TestLib",0,0),  //$NONXXX
						new TestData("testlibrary",0,0),  //$NONXXX
						new TestData("traceLookup_287589",0,0),  //$NONXXX
						new TestData("transf2",0,0),  //$NONXXX
						new TestData("transf3",0,0),  //$NONXXX
						new TestData("TransfForAccess",0,0),  //$NONXXX
						new TestData("transformationWithModuleElements_257055",0,0),  //$NONXXX
						new TestData("tuples",0,0,1),  //$NONXXX	// 1: preMarked
						new TestData("twoInputs",0,0),  //$NONXXX
						new TestData("uml2rdb",0,0),  //$NONXXX
						new TestData("uml2_stereotypeApplication",0,0),  //$NONXXX
						new TestData("UMLFoo",0,0),  //$NONXXX
						new TestData("urilessModeltype",0,0),  //$NONXXX
						new TestData("usebooleanprop",0,0),  //$NONXXX
						new TestData("useresultinsameout",0,0),  //$NONXXX
						new TestData("util",0,0),  //$NONXXX
						new TestData("Utils",0,0),  //$NONXXX
						new TestData("varassign",0,0),  //$NONXXX
						new TestData("varInitExpWithResult_260985",0,0),  //$NONXXX
						new TestData("varInitGroup_261841",0,0),  //$NONXXX
						new TestData("virtscr20707",0,0),  //$NONXXX
						new TestData("virtualPredefinedTypeOpers",0,0),  //$NONXXX
						new TestData("voidreturn",0,0),  //$NONXXX
						new TestData("vutil",0,0),  //$NONXXX
						new TestData("WarmUp",0,0),  //$NONXXX
						new TestData("_while",0,0),  //$NONXXX
						new TestData("_while_261024",0,0)  //$NONXXX
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

		CompiledUnit[] testdataCompiledUnit = compileAndCheckErrors(testdataFolder);

		IFile testdataCompiledXMI = loadCompiledXMIFile(testdataFolder);

		File unparseFolder = createUnparseFolder();

		unparseIntoFolder(testdataCompiledUnit[0], unparseFolder, testdataFolder.getName() + UNPARSED_SUFFIX);

		CompiledUnit[] unparseCompiledUnit = compileAndCheckErrors(unparseFolder);

		IFile unparseCompiledXMI = loadCompiledXMIFile(unparseFolder);

		unparseIntoFolder(unparseCompiledUnit[0], unparseFolder, testdataFolder.getName() + UNPARSED_SUFFIX + UNPARSED_SUFFIX);

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
		copyData(getOriginalPathSegment(), "unparserTestData/" + getOriginalPathSegment()); //$NON-NLS-1$

		File folder = getDestinationFolder();

		assertTrue("Invalid folder " + folder, folder.exists() && folder.isDirectory()); //$NON-NLS-1$

		return folder;
	}

	private CompiledUnit[] compileAndCheckErrors(File folder) throws Exception
	{
		CompiledUnit[] compiledUnits = compile(folder);

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

	private CompiledUnit[] compile(File folder) throws Exception
	{
		final String topName = folder.getName() + MDAConstants.QVTO_FILE_EXTENSION_WITH_DOT;
		getFile(folder, topName);
		WorkspaceUnitResolver resolver = new WorkspaceUnitResolver(Collections.singletonList(getIFolder(folder)));
		QVTOCompiler compiler = new QVTOCompiler();

		QvtCompilerOptions options = new QvtCompilerOptions();
		options.setGenerateCompletionData(false);

		UnitProxy unit = resolver.resolveUnit(folder.getName());
		assert unit != null;

		CompiledUnit[] compiledUnits = new CompiledUnit[] { compiler.compile(unit, options, (IProgressMonitor)null) };

		saveCompiledUnitsAsCompiledXMI(compiler, compiledUnits);
		saveCompiledUnitsAsStandardXMI(compiler, compiledUnits);

		return compiledUnits;
	}

	private IFile loadCompiledXMIFile(File folder)
	{
		IFolder sourcesFolder = getSourcesFolder();
		IFolder resourceFolder = sourcesFolder.getFolder(folder.getName());
		IFile file = resourceFolder.getFile(folder.getName() + '.' + ExeXMISerializer.COMPILED_XMI_FILE_EXTENSION);
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
				"/" + myProject.getProject().getName() +
				"/" + getUnparsePathSegment() +
				"/" + fileName + MDAConstants.QVTO_FILE_EXTENSION_WITH_DOT;
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
		return "sources/" + myData.getDir();
	}

	private static final String UNPARSED_SUFFIX = "-unparsed";

	private String getUnparsePathSegment() {
		return getOriginalPathSegment() + UNPARSED_SUFFIX;
	}

	private File getDestinationFolder()
	{
		return new File(myProject.getProject().getLocation().toString() + "/" + getOriginalPathSegment());
	}

	private IFolder getSourcesFolder()
	{
		IProject project = myProject.getProject();
		IFolder sourcesFolder = project.getFolder("sources");
		return sourcesFolder;
	}

	private File getUnparseFolder()
	{
		return new File(myProject.getProject().getLocation().toString() + "/" + getUnparsePathSegment());
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
		File sourceFolder = TestUtil.getPluginRelativeFolder(srcPath);
		File destFolder = createFolder(destPath);
		FileUtil.copyFolder(sourceFolder, destFolder);
		myProject.getProject().refreshLocal(IResource.DEPTH_INFINITE, null);
	}

	private File createFolder(String destPath) throws Exception
	{
		File destFolder = new File(myProject.getProject().getLocation().toString() + "/" + destPath); //$NON-NLS-1$
		destFolder.mkdirs();
		myProject.getProject().refreshLocal(IResource.DEPTH_INFINITE, null);
		return destFolder;
	}

	private final TestData myData;
	private TestProject myProject;
}
