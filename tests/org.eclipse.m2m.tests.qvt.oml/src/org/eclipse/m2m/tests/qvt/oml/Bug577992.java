package org.eclipse.m2m.tests.qvt.oml;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.Path;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.EcorePackage;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.m2m.internal.qvt.oml.emf.util.urimap.MModelURIMapFactory;
import org.eclipse.m2m.internal.qvt.oml.emf.util.urimap.MappingContainer;
import org.eclipse.m2m.internal.qvt.oml.emf.util.urimap.MetamodelURIMappingHelper;
import org.eclipse.m2m.internal.qvt.oml.emf.util.urimap.URIMapping;
import org.eclipse.m2m.tests.qvt.oml.ParserTests.TestData;
import org.eclipse.m2m.tests.qvt.oml.bbox.bug577992.Bug577992Package;
import org.junit.After;
import org.junit.Before;
import org.junit.runner.RunWith;
import org.junit.runners.BlockJUnit4ClassRunner;

@RunWith(BlockJUnit4ClassRunner.class)
public class Bug577992 extends TestQvtParser {
		
	public Bug577992() {
		super(TestData.createSourceChecked("bug577992", 0, 0)); //$NON-NLS-1$
	}
	
	@Before
	@Override
	public void setUp() throws Exception {
		
		super.setUp();
		
		IProject project = getTestProject().getProject();
		Resource resource = MetamodelURIMappingHelper.createMappingResource(project);
		MappingContainer container = MetamodelURIMappingHelper.createNewMappings(resource);
		URIMapping mapping = MModelURIMapFactory.eINSTANCE.createURIMapping();
		mapping.setSourceURI(Bug577992Package.eNS_URI);
				
		IPath destinationPath = new Path(getDestinationFolder().getPath());
		IPath workspacePath = project.getWorkspace().getRoot().getLocation();
		destinationPath = destinationPath.makeRelativeTo(workspacePath).append(getName()).addFileExtension(EcorePackage.eNAME);
		mapping.setTargetURI(URI.createPlatformResourceURI(destinationPath.toString(), false).toString());
		
		container.getMapping().add(mapping);
		MetamodelURIMappingHelper.saveMappings(project, container, false);
	}

	@After
	@Override
	public void tearDown() throws Exception {
		IFile mappingFile = MetamodelURIMappingHelper.getMappingFileHandle(getTestProject().getProject());
		mappingFile.delete(true, null);
		
		super.tearDown();	
	}

}
