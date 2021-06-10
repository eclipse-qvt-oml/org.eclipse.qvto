/*******************************************************************************
 * Copyright (c) 2008, 2021 Borland Software Corporation and others.
 * 
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v2.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v20.html
 *   
 * Contributors:
 *     Borland Software Corporation - initial API and implementation
 *     Christopher Gerking - bugs 326871, 400233, 427237, 566216, 566230
 *******************************************************************************/
package org.eclipse.m2m.internal.qvt.oml.blackbox.java;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.lang.reflect.TypeVariable;
import java.util.Collection;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;

import org.eclipse.emf.codegen.ecore.genmodel.GenClassifier;
import org.eclipse.emf.codegen.ecore.genmodel.GenModel;
import org.eclipse.emf.common.util.BasicDiagnostic;
import org.eclipse.emf.common.util.DiagnosticChain;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.EClassifier;
import org.eclipse.emf.ecore.EGenericType;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EOperation;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.ecore.ETypeParameter;
import org.eclipse.emf.ecore.EcoreFactory;
import org.eclipse.emf.ecore.plugin.EcorePlugin;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.eclipse.m2m.internal.qvt.oml.NLS;
import org.eclipse.m2m.internal.qvt.oml.ast.env.QvtOperationalModuleEnv;
import org.eclipse.m2m.internal.qvt.oml.ast.env.QvtOperationalStdLibrary;
import org.eclipse.m2m.internal.qvt.oml.emf.util.EmfUtil;
import org.eclipse.m2m.internal.qvt.oml.emf.util.ModelContent;
import org.eclipse.m2m.qvt.oml.blackbox.java.JavaModelInstance;
import org.eclipse.m2m.qvt.oml.util.Dictionary;
import org.eclipse.m2m.qvt.oml.util.MutableList;
import org.eclipse.ocl.TypeResolver;
import org.eclipse.ocl.expressions.CollectionKind;
import org.eclipse.ocl.types.OCLStandardLibrary;
import org.eclipse.ocl.util.Bag;

class Java2QVTTypeResolver {
	
	/**
	 * Flag indicates that strict type equality is using.
	 */
	static final int STRICT_TYPE = 1;

	/**
	 * Flag indicates that subtypes of given type will be considered in case strict equality fails.
	 */
	static final int ALLOW_SUBTYPE = 2;

	/**
	 * Flag indicates that supertypes of given type will be considered in case strict equality fails.
	 */
	static final int ALLOW_SUPERTYPE = 4;

	
	private QvtOperationalModuleEnv fEnv;
	private Collection<String> fPackageURIs;
	// used to delegate the OCL type determination to MDT OCL UMLReflection 
	private EClassifier fHelperEClassiferAdapter;
	private BasicDiagnostic fDiagnostics;
	private Map<String, URI> genModelMap;
	private Map<EPackage, ModelContent> genModelContentsMap = new HashMap<EPackage, ModelContent>();
	
	Java2QVTTypeResolver(QvtOperationalModuleEnv env, Collection<String> packageURIs, BasicDiagnostic diagnostics) {	
		fEnv = env;
		fPackageURIs = packageURIs;
		fDiagnostics = diagnostics;
	}
	
	QvtOperationalModuleEnv getEnvironment() {
		return fEnv;
	}
	
	EClassifier toEClassifier(Type type, int relationship) {
		EClassifier result = type2EClassifier(type, relationship);
		if(result == null) {
			if(type instanceof Class<?>) {
				EClassifier eWrapper = asEClassifier((Class<?>) type);
				EClassifier asOCLType = fEnv.getUMLReflection().asOCLType(eWrapper);
				if(asOCLType != eWrapper) {
					return asOCLType;
				}
			}
		} else {
			return fEnv.getUMLReflection().asOCLType(result);
		}
		
		return result;
	}
		
	private EClassifier type2EClassifier(Type type, int relationship) {
		if(type instanceof ParameterizedType) {
			ParameterizedType parameterizedType = (ParameterizedType) type;
			return handleParameterizedType(parameterizedType, relationship);
		} 
		else if(type instanceof Class<?>) {
			return handleType((Class<?>)type, relationship);
		} else if(type instanceof TypeVariable<?>) {
			TypeVariable<?> typeVariable = (TypeVariable<?>) type;
			OCLStandardLibrary<EClassifier> stdLib = fEnv.getOCLStandardLibrary();
			if(stdLib.getT().getName().equals(typeVariable.getName())) {
				return getEnvironment().getOCLStandardLibrary().getT();
				//return paramType;
			} else if(stdLib.getT2().getName().equals(typeVariable.getName())) {
				return getEnvironment().getOCLStandardLibrary().getT();			
			} else if(QvtOperationalStdLibrary.INSTANCE.getKeyT().getName().equals(typeVariable.getName())) {
				return QvtOperationalStdLibrary.INSTANCE.getKeyT();
			}
		}
		
		return null;
	}
		
	private EClassifier handleParameterizedType(ParameterizedType parameterizedType, int relationship) {	
		Type rawType = parameterizedType.getRawType();
		Type[] actualTypeArguments = parameterizedType.getActualTypeArguments();
		
		if(actualTypeArguments.length == 0) {
			return null;
		}

		Type actualElementType = actualTypeArguments[0];
		if(rawType instanceof Class<?> == false) {
			return null;
		}
		
		Class<?> rawClass = (Class<?>) rawType;
		if(rawClass == Dictionary.class) {
			EClassifier keyType = toEClassifier(actualElementType, relationship);
			Type actualElementType2 = actualTypeArguments.length > 1 ? actualTypeArguments[1] : null;
			if(keyType != null && actualElementType2 != null) {
				EClassifier elementType = toEClassifier(actualElementType2, relationship);
				if(elementType != null) {
					return fEnv.getTypeResolver().resolveDictionaryType(keyType, elementType);
				}
			}
		} else if(rawClass == MutableList.class) {
			EClassifier listElementType = toEClassifier(actualElementType, relationship);
			if(listElementType != null) {
				return fEnv.getTypeResolver().resolveListType(listElementType);
			}
		} else if(rawClass == LinkedHashSet.class) {
			return resolveCollectionType(CollectionKind.ORDERED_SET_LITERAL, actualElementType, relationship);
		}
		else if(Set.class.isAssignableFrom(rawClass)) {
			return resolveCollectionType(CollectionKind.SET_LITERAL, actualElementType, relationship);
		}		
		else if(rawClass == Bag.class) {
			return resolveCollectionType(CollectionKind.BAG_LITERAL, actualElementType, relationship);
		}
		else if(List.class.isAssignableFrom(rawClass)) {
			return resolveCollectionType(CollectionKind.SEQUENCE_LITERAL, actualElementType, relationship);
		}
		else if(rawType == List.class) {
			return resolveCollectionType(CollectionKind.SEQUENCE_LITERAL, actualElementType, relationship);
		}
		else if(rawType == Collection.class) {			
			return resolveCollectionType(CollectionKind.COLLECTION_LITERAL, actualElementType, relationship);
		}
		
		return lookupByInstanceClass(parameterizedType, relationship);
	}
	
	
	private EClassifier resolveCollectionType(CollectionKind kind, Type elementType, int relationship) {
		TypeResolver<EClassifier, EOperation, EStructuralFeature> typeResolver = fEnv.getTypeResolver();

		EClassifier actualElementClassifier = null;
		
		if(elementType instanceof TypeVariable<?>) {			
			TypeVariable<?> typeVariable = (TypeVariable<?>)elementType;
			String genericJavaTypeName = typeVariable.getName();
			
			OCLStandardLibrary<EClassifier> oclStdLibrary = fEnv.getOCLStandardLibrary();
			EClassifier typeT = oclStdLibrary.getT();
			if(typeT.getName().equals(genericJavaTypeName)) {
				actualElementClassifier = typeT;
			} else {
				EClassifier typeT2 = oclStdLibrary.getT2();
				if(typeT2.getName().equals(genericJavaTypeName)) {
					actualElementClassifier = typeT2;
				}
			}
		} 
		else if(elementType != null) {
			actualElementClassifier = toEClassifier(elementType, relationship);
		}
		
		if(actualElementClassifier != null) {
			return (EClassifier) typeResolver.resolveCollectionType(kind, actualElementClassifier);
		}
		
		return null;
	}
	
	private EClassifier handleType(Class<?> type, int relationship) {
		OCLStandardLibrary<EClassifier> stdLibrary = fEnv.getOCLStandardLibrary();
		
		if(type == Object.class) {
			return stdLibrary.getOclAny();
		} 
		else if(type == String.class) {
			return stdLibrary.getString();
		} 
		else if(type == Boolean.class || type == boolean.class) {
			return stdLibrary.getBoolean();
		} 
		else if(type == Integer.class || type == int.class) {
			return stdLibrary.getInteger();
		} 
		else if(type == Double.class || type == double.class) {
			return stdLibrary.getReal();
		}
		else if(type == void.class) {
			return stdLibrary.getOclVoid();
		}
		else if(type == JavaModelInstance.class) {
			return fEnv.getQVTStandardLibrary().getModelClass();
		}
		
		return lookupByInstanceClass(type, relationship);
	}
			
	private EClassifier lookupByInstanceClass(Class<?> type, int relationship) {
		assert type != null;
		
		SortedSet<EClassifier> subtypes = new TreeSet<EClassifier>(HIERARCHY_COMPARATOR_DESC);
		SortedSet<EClassifier> supertypes = new TreeSet<EClassifier>(HIERARCHY_COMPARATOR_ASC);
								
		Iterable<String> packageURIs = fPackageURIs.isEmpty() ? fEnv.getEPackageRegistry().keySet() : fPackageURIs;
				
		for (String nsURI : packageURIs) {
			EPackage ePackage = resolvePackage(nsURI, fDiagnostics);
			
			if (ePackage != null) {
				// early return for same-named classifier
				EClassifier sameNamedClassifier = ePackage.getEClassifier(type.getSimpleName());
								
				if (sameNamedClassifier != null) {
					if (isMatchingInstanceClass(sameNamedClassifier, type)) {
						return sameNamedClassifier;
					}
				}
				
				for (EClassifier eClassifier : ePackage.getEClassifiers()) {
					if (isMatchingInstanceClass(eClassifier, type)) {
						return eClassifier;
					}
					
					Class<?> instanceClass = eClassifier.getInstanceClass();
	
					// fall-back strategy for resolving sub/super types 
					if ((relationship & ALLOW_SUBTYPE) == ALLOW_SUBTYPE) {
						if(isAssignableFromTo(type, instanceClass)) {
							subtypes.add(eClassifier);
						}
					}
					if ((relationship & ALLOW_SUPERTYPE) == ALLOW_SUPERTYPE) {
						if(isAssignableFromTo(instanceClass, type)) {
							supertypes.add(eClassifier);
						}
					}
				}
			}
		}
		
		if ((relationship & ALLOW_SUBTYPE) == ALLOW_SUBTYPE && !subtypes.isEmpty()) {
			return subtypes.first();
		}
		
		if ((relationship & ALLOW_SUPERTYPE) == ALLOW_SUPERTYPE && !supertypes.isEmpty()) {
			return supertypes.first();
		}
		
		return null;
	}
		
	private boolean isAssignableFromTo(Class<?> from, Class<?> to) {
		return from != null && to != null && to.isAssignableFrom(from);
	}
	
	private EClassifier lookupByInstanceClass(ParameterizedType type, int relationship) {
		assert type != null;
		
		Type rawType = type.getRawType();
		EClassifier rawClassifier = toEClassifier(rawType, relationship);
		
		if (rawClassifier != null) {
			Iterator<ETypeParameter> typeParameters = rawClassifier.getETypeParameters().iterator();
					
			for (Type argumentType : type.getActualTypeArguments()) {
				EClassifier argumentClassifier = toEClassifier(argumentType, relationship);
								
				if (argumentClassifier != null && typeParameters.hasNext()) {					
					ETypeParameter typeParameter = typeParameters.next();
					
					for (EGenericType genericType : typeParameter.getEBounds()) {
						EClassifier genericClassifier = genericType.getEClassifier();
						Class<?> genericInstanceClass = genericClassifier.getInstanceClass();
						
						if (argumentType instanceof Class<?>) {
							if ((relationship & ALLOW_SUBTYPE) == ALLOW_SUBTYPE) {
								if(!isAssignableFromTo((Class<?>) argumentType, genericInstanceClass)) {
									return null;
								}
							}
							if ((relationship & ALLOW_SUPERTYPE) == ALLOW_SUPERTYPE) {
								if(!isAssignableFromTo(genericInstanceClass, (Class<?>) argumentType)) {
									return null;
								}
							}
						}
					}
				}
			}
		}
				
		return rawClassifier;
	}
	
	private EClassifier asEClassifier(Class<?> javaClass) { 		
		if(fHelperEClassiferAdapter == null) {
			EPackage ownerPackage = EcoreFactory.eINSTANCE.createEPackage();
			ownerPackage.setName("helper"); //$NON-NLS-1$
			ownerPackage.setNsURI(javaClass.getName());
			fHelperEClassiferAdapter = EcoreFactory.eINSTANCE.createEDataType();			
			ownerPackage.getEClassifiers().add(fHelperEClassiferAdapter);
		}
		
		fHelperEClassiferAdapter.setName(javaClass.getSimpleName());
		fHelperEClassiferAdapter.setInstanceClass(javaClass);
		
		return fHelperEClassiferAdapter;		
	}

	private static final Comparator<EClassifier> HIERARCHY_COMPARATOR_ASC = new Comparator<EClassifier>() {
		public int compare(EClassifier o1, EClassifier o2) {
            Class<?> o1Class = o1.getInstanceClass();
            Class<?> o2Class = o2.getInstanceClass();

            if (o2Class.equals(o1Class)) {
                return 0;
            } else if (o1Class.isAssignableFrom(o2Class)) {
                return -1;
            } else if (o2Class.isAssignableFrom(o1Class)) {
                return 1;
            } else {
                return 0;
            }
		}
	};
	
	private static final Comparator<EClassifier> HIERARCHY_COMPARATOR_DESC = new Comparator<EClassifier>() {
		public int compare(EClassifier o1, EClassifier o2) {
            Class<?> o1Class = o1.getInstanceClass();
            Class<?> o2Class = o2.getInstanceClass();

            if (o2Class.equals(o1Class)) {
                return 0;
            } else if (o1Class.isAssignableFrom(o2Class)) {
                return 1;
            } else if (o2Class.isAssignableFrom(o1Class)) {
                return -1;
            } else {
                return 0;
            }
		}
	};
	
	private EPackage resolvePackage(String nsURI, DiagnosticChain diagnosticChain) {
		EPackage.Registry registry = fEnv.getEPackageRegistry();
		
		EPackage resolvedPackage;
		try {
			resolvedPackage = registry.getEPackage(nsURI);
		}
		catch (Throwable t) {
			resolvedPackage = null;
		}
		
		if(resolvedPackage != null) {
			return resolvedPackage;				
		} else {
			diagnosticChain.add(DiagnosticUtil.createErrorDiagnostic(
					NLS.bind(JavaBlackboxMessages.UnresolvedMetamodelURI, nsURI)));
		}
		
		return null;
	}
	
	private boolean isMatchingInstanceClass(EClassifier eClassifier, Class<?> type) {
		
		Class<?> instanceClass = eClassifier.getInstanceClass();
		
		if(type == instanceClass) {
			return true;
		}
		else if (instanceClass == null && EmfUtil.isDynamic(eClassifier)) {
			EPackage ePackage = eClassifier.getEPackage();											
				
			ModelContent genModelContent = getGenModelContent(ePackage);
			
			if (genModelContent != null) {
				try {
					if (isMatchingInstanceGenClassifier(eClassifier, type, genModelContent)) {
						return true;
					}
				} catch(NoClassDefFoundError e) {
					// Ignore failure due to missing org.eclipse.emf.ecore.codegen support
				}
			}
			
		}
		
		return false;
	}

	private static boolean isMatchingInstanceGenClassifier(EClassifier eClassifier, Class<?> type, ModelContent genModelContent) {
		for(EObject eObject : genModelContent.getContent()) {
			if (eObject instanceof GenModel) {
				GenModel genModel = (GenModel) eObject;
				GenClassifier genClassifier = genModel.findGenClassifier(eClassifier);
				if (genClassifier != null) {
					String classifierInstanceName = genClassifier.getRawInstanceClassName();
					if (type.getName().equals(classifierInstanceName)) {
						return true;
					}
				}
			}
		}
		return false;
	}
	
	private Map<String, URI> getGenModelMap() {
		if (genModelMap == null) {
			genModelMap = EcorePlugin.getEPackageNsURIToGenModelLocationMap(true);
		}
		
		return genModelMap;
	}
	
	private ModelContent getGenModelContent(EPackage ePackage) {		
		ModelContent result = genModelContentsMap.get(ePackage);
		
		if (result == null) {
			String nsURI = ePackage.getNsURI();
			Map<String, URI> genModelLocationMap = getGenModelMap();
			URI genModelUri = genModelLocationMap.get(nsURI);
			
			if (genModelUri != null) {														
				ResourceSet resourceSet = ePackage.eResource().getResourceSet();
				result = EmfUtil.safeLoadModel(genModelUri, resourceSet);
				
				genModelContentsMap.put(ePackage, result);
			}
 		}
 		
		return result;
 	}
}
