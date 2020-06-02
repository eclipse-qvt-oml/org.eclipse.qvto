/*******************************************************************************
 * Copyright (c) 2007, 2019 Borland Software Corporation and others.
 * 
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v2.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v20.html
 *   
 * Contributors:
 *     Borland Software Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.m2m.internal.qvt.oml.expressions.impl;

import java.util.Collection;

import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.notify.NotificationChain;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EAnnotation;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EClassifier;
import org.eclipse.emf.ecore.EFactory;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.ecore.EcorePackage;
import org.eclipse.emf.ecore.InternalEObject;
import org.eclipse.emf.ecore.impl.EClassImpl;
import org.eclipse.emf.ecore.impl.ENotificationImpl;
import org.eclipse.emf.ecore.util.EObjectContainmentEList;
import org.eclipse.emf.ecore.util.EObjectContainmentWithInverseEList;
import org.eclipse.emf.ecore.util.EObjectResolvingEList;
import org.eclipse.emf.ecore.util.InternalEList;
import org.eclipse.m2m.internal.qvt.oml.expressions.EntryOperation;
import org.eclipse.m2m.internal.qvt.oml.expressions.ExpressionsPackage;
import org.eclipse.m2m.internal.qvt.oml.expressions.ModelType;
import org.eclipse.m2m.internal.qvt.oml.expressions.ModuleImport;
import org.eclipse.m2m.internal.qvt.oml.expressions.VisitableASTNode;
import org.eclipse.ocl.ecore.Variable;
import org.eclipse.ocl.utilities.ASTNode;
import org.eclipse.ocl.utilities.UtilitiesPackage;
import org.eclipse.ocl.utilities.Visitable;
import org.eclipse.ocl.utilities.Visitor;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Module</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * </p>
 * <ul>
 *   <li>{@link org.eclipse.m2m.internal.qvt.oml.expressions.impl.ModuleImpl#getNsURI <em>Ns URI</em>}</li>
 *   <li>{@link org.eclipse.m2m.internal.qvt.oml.expressions.impl.ModuleImpl#getNsPrefix <em>Ns Prefix</em>}</li>
 *   <li>{@link org.eclipse.m2m.internal.qvt.oml.expressions.impl.ModuleImpl#getEFactoryInstance <em>EFactory Instance</em>}</li>
 *   <li>{@link org.eclipse.m2m.internal.qvt.oml.expressions.impl.ModuleImpl#getEClassifiers <em>EClassifiers</em>}</li>
 *   <li>{@link org.eclipse.m2m.internal.qvt.oml.expressions.impl.ModuleImpl#getESubpackages <em>ESubpackages</em>}</li>
 *   <li>{@link org.eclipse.m2m.internal.qvt.oml.expressions.impl.ModuleImpl#getESuperPackage <em>ESuper Package</em>}</li>
 *   <li>{@link org.eclipse.m2m.internal.qvt.oml.expressions.impl.ModuleImpl#getStartPosition <em>Start Position</em>}</li>
 *   <li>{@link org.eclipse.m2m.internal.qvt.oml.expressions.impl.ModuleImpl#getEndPosition <em>End Position</em>}</li>
 *   <li>{@link org.eclipse.m2m.internal.qvt.oml.expressions.impl.ModuleImpl#getConfigProperty <em>Config Property</em>}</li>
 *   <li>{@link org.eclipse.m2m.internal.qvt.oml.expressions.impl.ModuleImpl#getEntry <em>Entry</em>}</li>
 *   <li>{@link org.eclipse.m2m.internal.qvt.oml.expressions.impl.ModuleImpl#isIsBlackbox <em>Is Blackbox</em>}</li>
 *   <li>{@link org.eclipse.m2m.internal.qvt.oml.expressions.impl.ModuleImpl#getModuleImport <em>Module Import</em>}</li>
 *   <li>{@link org.eclipse.m2m.internal.qvt.oml.expressions.impl.ModuleImpl#getOwnedTag <em>Owned Tag</em>}</li>
 *   <li>{@link org.eclipse.m2m.internal.qvt.oml.expressions.impl.ModuleImpl#getOwnedVariable <em>Owned Variable</em>}</li>
 *   <li>{@link org.eclipse.m2m.internal.qvt.oml.expressions.impl.ModuleImpl#getUsedModelType <em>Used Model Type</em>}</li>
 * </ul>
 *
 * @generated
 */
public class ModuleImpl extends EClassImpl implements org.eclipse.m2m.internal.qvt.oml.expressions.Module {
	/**
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 */
    public static final String copyright = "Copyright (c) 2007 Borland Software Corporation\r\n\r\nAll rights reserved. This program and the accompanying materials\r\nare made available under the terms of the Eclipse Public License v2.0\r\nwhich accompanies this distribution, and is available at\r\nhttp://www.eclipse.org/legal/epl-v20.html\r\n  \r\nContributors:\r\n    Borland Software Corporation - initial API and implementation"; //$NON-NLS-1$

    /**
	 * The default value of the '{@link #getNsURI() <em>Ns URI</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getNsURI()
	 * @generated
	 * @ordered
	 */
	protected static final String NS_URI_EDEFAULT = null;

	/**
	 * The cached value of the '{@link #getNsURI() <em>Ns URI</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getNsURI()
	 * @generated
	 * @ordered
	 */
	protected String nsURI = NS_URI_EDEFAULT;

	/**
	 * The default value of the '{@link #getNsPrefix() <em>Ns Prefix</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getNsPrefix()
	 * @generated
	 * @ordered
	 */
	protected static final String NS_PREFIX_EDEFAULT = null;

	/**
	 * The cached value of the '{@link #getNsPrefix() <em>Ns Prefix</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getNsPrefix()
	 * @generated
	 * @ordered
	 */
	protected String nsPrefix = NS_PREFIX_EDEFAULT;

	/**
	 * The cached value of the '{@link #getEFactoryInstance() <em>EFactory Instance</em>}' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getEFactoryInstance()
	 * @generated
	 * @ordered
	 */
	protected EFactory eFactoryInstance;

	/**
	 * The cached value of the '{@link #getEClassifiers() <em>EClassifiers</em>}' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getEClassifiers()
	 * @generated
	 * @ordered
	 */
	protected EList<EClassifier> eClassifiers;

	/**
	 * The cached value of the '{@link #getESubpackages() <em>ESubpackages</em>}' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getESubpackages()
	 * @generated
	 * @ordered
	 */
	protected EList<EPackage> eSubpackages;

	/**
	 * The default value of the '{@link #getStartPosition() <em>Start Position</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getStartPosition()
	 * @generated
	 * @ordered
	 */
	protected static final int START_POSITION_EDEFAULT = -1;

	/**
	 * The cached value of the '{@link #getStartPosition() <em>Start Position</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getStartPosition()
	 * @generated
	 * @ordered
	 */
	protected int startPosition = START_POSITION_EDEFAULT;

	/**
	 * The default value of the '{@link #getEndPosition() <em>End Position</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getEndPosition()
	 * @generated
	 * @ordered
	 */
	protected static final int END_POSITION_EDEFAULT = -1;

	/**
	 * The cached value of the '{@link #getEndPosition() <em>End Position</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getEndPosition()
	 * @generated
	 * @ordered
	 */
	protected int endPosition = END_POSITION_EDEFAULT;

	/**
	 * The cached value of the '{@link #getConfigProperty() <em>Config Property</em>}' reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getConfigProperty()
	 * @generated
	 * @ordered
	 */
	protected EList<EStructuralFeature> configProperty;

	/**
	 * The cached value of the '{@link #getEntry() <em>Entry</em>}' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getEntry()
	 * @generated
	 * @ordered
	 */
	protected EntryOperation entry;

	/**
	 * The default value of the '{@link #isIsBlackbox() <em>Is Blackbox</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #isIsBlackbox()
	 * @generated
	 * @ordered
	 */
	protected static final boolean IS_BLACKBOX_EDEFAULT = false;

	/**
	 * The flag representing the value of the '{@link #isIsBlackbox() <em>Is Blackbox</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #isIsBlackbox()
	 * @generated
	 * @ordered
	 */
	protected static final int IS_BLACKBOX_EFLAG = 1 << 10;

	/**
	 * The flag representing whether the Is Blackbox attribute has been set.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	protected static final int IS_BLACKBOX_ESETFLAG = 1 << 11;

	/**
	 * The cached value of the '{@link #getModuleImport() <em>Module Import</em>}' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getModuleImport()
	 * @generated
	 * @ordered
	 */
	protected EList<ModuleImport> moduleImport;

	/**
	 * The cached value of the '{@link #getOwnedTag() <em>Owned Tag</em>}' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getOwnedTag()
	 * @generated
	 * @ordered
	 */
	protected EList<EAnnotation> ownedTag;

	/**
	 * The cached value of the '{@link #getOwnedVariable() <em>Owned Variable</em>}' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getOwnedVariable()
	 * @generated
	 * @ordered
	 */
	protected EList<Variable> ownedVariable;

	/**
	 * The cached value of the '{@link #getUsedModelType() <em>Used Model Type</em>}' reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getUsedModelType()
	 * @generated
	 * @ordered
	 */
	protected EList<ModelType> usedModelType;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected ModuleImpl() {
		super();
	}
	
	@Override
	public EPackage getEPackage() {
		// https://bugs.eclipse.org/bugs/show_bug.cgi?id=297275
		// FIXME - consider using an EPackage container for all modules
		// within a unit. This container should be constructed on the fly
		// prior to transformation execution
		EPackage ePackage = super.getEPackage();
		return (ePackage != null) ? ePackage : this;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	protected EClass eStaticClass() {
		return ExpressionsPackage.Literals.MODULE;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public String getNsURI() {
		return nsURI;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setNsURI(String newNsURI) {
		String oldNsURI = nsURI;
		nsURI = newNsURI;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, ExpressionsPackage.MODULE__NS_URI, oldNsURI, nsURI));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public String getNsPrefix() {
		return nsPrefix;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setNsPrefix(String newNsPrefix) {
		String oldNsPrefix = nsPrefix;
		nsPrefix = newNsPrefix;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, ExpressionsPackage.MODULE__NS_PREFIX, oldNsPrefix, nsPrefix));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EFactory getEFactoryInstance() {
		return eFactoryInstance;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public NotificationChain basicSetEFactoryInstance(EFactory newEFactoryInstance, NotificationChain msgs) {
		EFactory oldEFactoryInstance = eFactoryInstance;
		eFactoryInstance = newEFactoryInstance;
		if (eNotificationRequired()) {
			ENotificationImpl notification = new ENotificationImpl(this, Notification.SET, ExpressionsPackage.MODULE__EFACTORY_INSTANCE, oldEFactoryInstance, newEFactoryInstance);
			if (msgs == null) msgs = notification; else msgs.add(notification);
		}
		return msgs;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setEFactoryInstance(EFactory newEFactoryInstance) {
		if (newEFactoryInstance != eFactoryInstance) {
			NotificationChain msgs = null;
			if (eFactoryInstance != null)
				msgs = ((InternalEObject)eFactoryInstance).eInverseRemove(this, EcorePackage.EFACTORY__EPACKAGE, EFactory.class, msgs);
			if (newEFactoryInstance != null)
				msgs = ((InternalEObject)newEFactoryInstance).eInverseAdd(this, EcorePackage.EFACTORY__EPACKAGE, EFactory.class, msgs);
			msgs = basicSetEFactoryInstance(newEFactoryInstance, msgs);
			if (msgs != null) msgs.dispatch();
		}
		else if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, ExpressionsPackage.MODULE__EFACTORY_INSTANCE, newEFactoryInstance, newEFactoryInstance));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EList<EClassifier> getEClassifiers() {
		if (eClassifiers == null) {
			eClassifiers = new EObjectContainmentWithInverseEList.Resolving<EClassifier>(EClassifier.class, this, ExpressionsPackage.MODULE__ECLASSIFIERS, EcorePackage.ECLASSIFIER__EPACKAGE);
		}
		return eClassifiers;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EList<EPackage> getESubpackages() {
		if (eSubpackages == null) {
			eSubpackages = new EObjectContainmentWithInverseEList.Resolving<EPackage>(EPackage.class, this, ExpressionsPackage.MODULE__ESUBPACKAGES, EcorePackage.EPACKAGE__ESUPER_PACKAGE);
		}
		return eSubpackages;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EPackage getESuperPackage() {
		if (eContainerFeatureID() != ExpressionsPackage.MODULE__ESUPER_PACKAGE) return null;
		return (EPackage)eContainer();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EPackage basicGetESuperPackage() {
		if (eContainerFeatureID() != ExpressionsPackage.MODULE__ESUPER_PACKAGE) return null;
		return (EPackage)eInternalContainer();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public int getStartPosition() {
		return startPosition;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setStartPosition(int newStartPosition) {
		int oldStartPosition = startPosition;
		startPosition = newStartPosition;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, ExpressionsPackage.MODULE__START_POSITION, oldStartPosition, startPosition));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public int getEndPosition() {
		return endPosition;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setEndPosition(int newEndPosition) {
		int oldEndPosition = endPosition;
		endPosition = newEndPosition;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, ExpressionsPackage.MODULE__END_POSITION, oldEndPosition, endPosition));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EList<Variable> getOwnedVariable() {
		if (ownedVariable == null) {
			ownedVariable = new EObjectContainmentEList<Variable>(Variable.class, this, ExpressionsPackage.MODULE__OWNED_VARIABLE);
		}
		return ownedVariable;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EntryOperation getEntry() {
		if (entry != null && entry.eIsProxy()) {
			InternalEObject oldEntry = (InternalEObject)entry;
			entry = (EntryOperation)eResolveProxy(oldEntry);
			if (entry != oldEntry) {
				if (eNotificationRequired())
					eNotify(new ENotificationImpl(this, Notification.RESOLVE, ExpressionsPackage.MODULE__ENTRY, oldEntry, entry));
			}
		}
		return entry;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EntryOperation basicGetEntry() {
		return entry;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setEntry(EntryOperation newEntry) {
		EntryOperation oldEntry = entry;
		entry = newEntry;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, ExpressionsPackage.MODULE__ENTRY, oldEntry, entry));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public boolean isIsBlackbox() {
		return (eFlags & IS_BLACKBOX_EFLAG) != 0;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setIsBlackbox(boolean newIsBlackbox) {
		boolean oldIsBlackbox = (eFlags & IS_BLACKBOX_EFLAG) != 0;
		if (newIsBlackbox) eFlags |= IS_BLACKBOX_EFLAG; else eFlags &= ~IS_BLACKBOX_EFLAG;
		boolean oldIsBlackboxESet = (eFlags & IS_BLACKBOX_ESETFLAG) != 0;
		eFlags |= IS_BLACKBOX_ESETFLAG;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, ExpressionsPackage.MODULE__IS_BLACKBOX, oldIsBlackbox, newIsBlackbox, !oldIsBlackboxESet));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void unsetIsBlackbox() {
		boolean oldIsBlackbox = (eFlags & IS_BLACKBOX_EFLAG) != 0;
		boolean oldIsBlackboxESet = (eFlags & IS_BLACKBOX_ESETFLAG) != 0;
		if (IS_BLACKBOX_EDEFAULT) eFlags |= IS_BLACKBOX_EFLAG; else eFlags &= ~IS_BLACKBOX_EFLAG;
		eFlags &= ~IS_BLACKBOX_ESETFLAG;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.UNSET, ExpressionsPackage.MODULE__IS_BLACKBOX, oldIsBlackbox, IS_BLACKBOX_EDEFAULT, oldIsBlackboxESet));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public boolean isSetIsBlackbox() {
		return (eFlags & IS_BLACKBOX_ESETFLAG) != 0;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EList<EStructuralFeature> getConfigProperty() {
		if (configProperty == null) {
			configProperty = new EObjectResolvingEList<EStructuralFeature>(EStructuralFeature.class, this, ExpressionsPackage.MODULE__CONFIG_PROPERTY);
		}
		return configProperty;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EList<ModuleImport> getModuleImport() {
		if (moduleImport == null) {
			moduleImport = new EObjectContainmentWithInverseEList<ModuleImport>(ModuleImport.class, this, ExpressionsPackage.MODULE__MODULE_IMPORT, ExpressionsPackage.MODULE_IMPORT__MODULE);
		}
		return moduleImport;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EList<EAnnotation> getOwnedTag() {
		if (ownedTag == null) {
			ownedTag = new EObjectContainmentEList<EAnnotation>(EAnnotation.class, this, ExpressionsPackage.MODULE__OWNED_TAG);
		}
		return ownedTag;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EList<ModelType> getUsedModelType() {
		if (usedModelType == null) {
			usedModelType = new EObjectResolvingEList<ModelType>(ModelType.class, this, ExpressionsPackage.MODULE__USED_MODEL_TYPE);
		}
		return usedModelType;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public <T, U extends Visitor<T, ?, ?, ?, ?, ?, ?, ?, ?, ?>> T accept(final U v) {
		if(v instanceof org.eclipse.m2m.internal.qvt.oml.expressions.util.QVTOperationalVisitor) {
		  @SuppressWarnings("unchecked")    		
		  org.eclipse.m2m.internal.qvt.oml.expressions.util.QVTOperationalVisitor<T> visitorExt = (org.eclipse.m2m.internal.qvt.oml.expressions.util.QVTOperationalVisitor) v;    	
		  return visitorExt.visitModule(this);
		}
		return org.eclipse.m2m.internal.qvt.oml.expressions.util.ForeignVisitorDefaultValue.getDefaultValueForVisitor(v);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated NOT
	 */
	public EClassifier getEClassifier(String name) {
		for (EClassifier nextClassifier : getEClassifiers()) {
			String nextName = nextClassifier.getName();
			if(name != null ? name.equals(nextName) : name == nextName) {
				return nextClassifier;
			}
		}
		return null;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@SuppressWarnings("unchecked")
	@Override
	public NotificationChain eInverseAdd(InternalEObject otherEnd, int featureID, NotificationChain msgs) {
		switch (featureID) {
			case ExpressionsPackage.MODULE__EFACTORY_INSTANCE:
				if (eFactoryInstance != null)
					msgs = ((InternalEObject)eFactoryInstance).eInverseRemove(this, EcorePackage.EFACTORY__EPACKAGE, EFactory.class, msgs);
				return basicSetEFactoryInstance((EFactory)otherEnd, msgs);
			case ExpressionsPackage.MODULE__ECLASSIFIERS:
				return ((InternalEList<InternalEObject>)(InternalEList<?>)getEClassifiers()).basicAdd(otherEnd, msgs);
			case ExpressionsPackage.MODULE__ESUBPACKAGES:
				return ((InternalEList<InternalEObject>)(InternalEList<?>)getESubpackages()).basicAdd(otherEnd, msgs);
			case ExpressionsPackage.MODULE__ESUPER_PACKAGE:
				if (eInternalContainer() != null)
					msgs = eBasicRemoveFromContainer(msgs);
				return eBasicSetContainer(otherEnd, ExpressionsPackage.MODULE__ESUPER_PACKAGE, msgs);
			case ExpressionsPackage.MODULE__MODULE_IMPORT:
				return ((InternalEList<InternalEObject>)(InternalEList<?>)getModuleImport()).basicAdd(otherEnd, msgs);
		}
		return super.eInverseAdd(otherEnd, featureID, msgs);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public NotificationChain eInverseRemove(InternalEObject otherEnd, int featureID, NotificationChain msgs) {
		switch (featureID) {
			case ExpressionsPackage.MODULE__EFACTORY_INSTANCE:
				return basicSetEFactoryInstance(null, msgs);
			case ExpressionsPackage.MODULE__ECLASSIFIERS:
				return ((InternalEList<?>)getEClassifiers()).basicRemove(otherEnd, msgs);
			case ExpressionsPackage.MODULE__ESUBPACKAGES:
				return ((InternalEList<?>)getESubpackages()).basicRemove(otherEnd, msgs);
			case ExpressionsPackage.MODULE__ESUPER_PACKAGE:
				return eBasicSetContainer(null, ExpressionsPackage.MODULE__ESUPER_PACKAGE, msgs);
			case ExpressionsPackage.MODULE__MODULE_IMPORT:
				return ((InternalEList<?>)getModuleImport()).basicRemove(otherEnd, msgs);
			case ExpressionsPackage.MODULE__OWNED_TAG:
				return ((InternalEList<?>)getOwnedTag()).basicRemove(otherEnd, msgs);
			case ExpressionsPackage.MODULE__OWNED_VARIABLE:
				return ((InternalEList<?>)getOwnedVariable()).basicRemove(otherEnd, msgs);
		}
		return super.eInverseRemove(otherEnd, featureID, msgs);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public NotificationChain eBasicRemoveFromContainerFeature(NotificationChain msgs) {
		switch (eContainerFeatureID()) {
			case ExpressionsPackage.MODULE__ESUPER_PACKAGE:
				return eInternalContainer().eInverseRemove(this, EcorePackage.EPACKAGE__ESUBPACKAGES, EPackage.class, msgs);
		}
		return super.eBasicRemoveFromContainerFeature(msgs);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public Object eGet(int featureID, boolean resolve, boolean coreType) {
		switch (featureID) {
			case ExpressionsPackage.MODULE__NS_URI:
				return getNsURI();
			case ExpressionsPackage.MODULE__NS_PREFIX:
				return getNsPrefix();
			case ExpressionsPackage.MODULE__EFACTORY_INSTANCE:
				return getEFactoryInstance();
			case ExpressionsPackage.MODULE__ECLASSIFIERS:
				return getEClassifiers();
			case ExpressionsPackage.MODULE__ESUBPACKAGES:
				return getESubpackages();
			case ExpressionsPackage.MODULE__ESUPER_PACKAGE:
				if (resolve) return getESuperPackage();
				return basicGetESuperPackage();
			case ExpressionsPackage.MODULE__START_POSITION:
				return getStartPosition();
			case ExpressionsPackage.MODULE__END_POSITION:
				return getEndPosition();
			case ExpressionsPackage.MODULE__CONFIG_PROPERTY:
				return getConfigProperty();
			case ExpressionsPackage.MODULE__ENTRY:
				if (resolve) return getEntry();
				return basicGetEntry();
			case ExpressionsPackage.MODULE__IS_BLACKBOX:
				return isIsBlackbox();
			case ExpressionsPackage.MODULE__MODULE_IMPORT:
				return getModuleImport();
			case ExpressionsPackage.MODULE__OWNED_TAG:
				return getOwnedTag();
			case ExpressionsPackage.MODULE__OWNED_VARIABLE:
				return getOwnedVariable();
			case ExpressionsPackage.MODULE__USED_MODEL_TYPE:
				return getUsedModelType();
		}
		return super.eGet(featureID, resolve, coreType);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@SuppressWarnings("unchecked")
	@Override
	public void eSet(int featureID, Object newValue) {
		switch (featureID) {
			case ExpressionsPackage.MODULE__NS_URI:
				setNsURI((String)newValue);
				return;
			case ExpressionsPackage.MODULE__NS_PREFIX:
				setNsPrefix((String)newValue);
				return;
			case ExpressionsPackage.MODULE__EFACTORY_INSTANCE:
				setEFactoryInstance((EFactory)newValue);
				return;
			case ExpressionsPackage.MODULE__ECLASSIFIERS:
				getEClassifiers().clear();
				getEClassifiers().addAll((Collection<? extends EClassifier>)newValue);
				return;
			case ExpressionsPackage.MODULE__ESUBPACKAGES:
				getESubpackages().clear();
				getESubpackages().addAll((Collection<? extends EPackage>)newValue);
				return;
			case ExpressionsPackage.MODULE__START_POSITION:
				setStartPosition((Integer)newValue);
				return;
			case ExpressionsPackage.MODULE__END_POSITION:
				setEndPosition((Integer)newValue);
				return;
			case ExpressionsPackage.MODULE__CONFIG_PROPERTY:
				getConfigProperty().clear();
				getConfigProperty().addAll((Collection<? extends EStructuralFeature>)newValue);
				return;
			case ExpressionsPackage.MODULE__ENTRY:
				setEntry((EntryOperation)newValue);
				return;
			case ExpressionsPackage.MODULE__IS_BLACKBOX:
				setIsBlackbox((Boolean)newValue);
				return;
			case ExpressionsPackage.MODULE__MODULE_IMPORT:
				getModuleImport().clear();
				getModuleImport().addAll((Collection<? extends ModuleImport>)newValue);
				return;
			case ExpressionsPackage.MODULE__OWNED_TAG:
				getOwnedTag().clear();
				getOwnedTag().addAll((Collection<? extends EAnnotation>)newValue);
				return;
			case ExpressionsPackage.MODULE__OWNED_VARIABLE:
				getOwnedVariable().clear();
				getOwnedVariable().addAll((Collection<? extends Variable>)newValue);
				return;
			case ExpressionsPackage.MODULE__USED_MODEL_TYPE:
				getUsedModelType().clear();
				getUsedModelType().addAll((Collection<? extends ModelType>)newValue);
				return;
		}
		super.eSet(featureID, newValue);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public void eUnset(int featureID) {
		switch (featureID) {
			case ExpressionsPackage.MODULE__NS_URI:
				setNsURI(NS_URI_EDEFAULT);
				return;
			case ExpressionsPackage.MODULE__NS_PREFIX:
				setNsPrefix(NS_PREFIX_EDEFAULT);
				return;
			case ExpressionsPackage.MODULE__EFACTORY_INSTANCE:
				setEFactoryInstance((EFactory)null);
				return;
			case ExpressionsPackage.MODULE__ECLASSIFIERS:
				getEClassifiers().clear();
				return;
			case ExpressionsPackage.MODULE__ESUBPACKAGES:
				getESubpackages().clear();
				return;
			case ExpressionsPackage.MODULE__START_POSITION:
				setStartPosition(START_POSITION_EDEFAULT);
				return;
			case ExpressionsPackage.MODULE__END_POSITION:
				setEndPosition(END_POSITION_EDEFAULT);
				return;
			case ExpressionsPackage.MODULE__CONFIG_PROPERTY:
				getConfigProperty().clear();
				return;
			case ExpressionsPackage.MODULE__ENTRY:
				setEntry((EntryOperation)null);
				return;
			case ExpressionsPackage.MODULE__IS_BLACKBOX:
				unsetIsBlackbox();
				return;
			case ExpressionsPackage.MODULE__MODULE_IMPORT:
				getModuleImport().clear();
				return;
			case ExpressionsPackage.MODULE__OWNED_TAG:
				getOwnedTag().clear();
				return;
			case ExpressionsPackage.MODULE__OWNED_VARIABLE:
				getOwnedVariable().clear();
				return;
			case ExpressionsPackage.MODULE__USED_MODEL_TYPE:
				getUsedModelType().clear();
				return;
		}
		super.eUnset(featureID);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public boolean eIsSet(int featureID) {
		switch (featureID) {
			case ExpressionsPackage.MODULE__NS_URI:
				return NS_URI_EDEFAULT == null ? nsURI != null : !NS_URI_EDEFAULT.equals(nsURI);
			case ExpressionsPackage.MODULE__NS_PREFIX:
				return NS_PREFIX_EDEFAULT == null ? nsPrefix != null : !NS_PREFIX_EDEFAULT.equals(nsPrefix);
			case ExpressionsPackage.MODULE__EFACTORY_INSTANCE:
				return eFactoryInstance != null;
			case ExpressionsPackage.MODULE__ECLASSIFIERS:
				return eClassifiers != null && !eClassifiers.isEmpty();
			case ExpressionsPackage.MODULE__ESUBPACKAGES:
				return eSubpackages != null && !eSubpackages.isEmpty();
			case ExpressionsPackage.MODULE__ESUPER_PACKAGE:
				return basicGetESuperPackage() != null;
			case ExpressionsPackage.MODULE__START_POSITION:
				return startPosition != START_POSITION_EDEFAULT;
			case ExpressionsPackage.MODULE__END_POSITION:
				return endPosition != END_POSITION_EDEFAULT;
			case ExpressionsPackage.MODULE__CONFIG_PROPERTY:
				return configProperty != null && !configProperty.isEmpty();
			case ExpressionsPackage.MODULE__ENTRY:
				return entry != null;
			case ExpressionsPackage.MODULE__IS_BLACKBOX:
				return isSetIsBlackbox();
			case ExpressionsPackage.MODULE__MODULE_IMPORT:
				return moduleImport != null && !moduleImport.isEmpty();
			case ExpressionsPackage.MODULE__OWNED_TAG:
				return ownedTag != null && !ownedTag.isEmpty();
			case ExpressionsPackage.MODULE__OWNED_VARIABLE:
				return ownedVariable != null && !ownedVariable.isEmpty();
			case ExpressionsPackage.MODULE__USED_MODEL_TYPE:
				return usedModelType != null && !usedModelType.isEmpty();
		}
		return super.eIsSet(featureID);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public int eBaseStructuralFeatureID(int derivedFeatureID, Class<?> baseClass) {
		if (baseClass == EPackage.class) {
			switch (derivedFeatureID) {
				case ExpressionsPackage.MODULE__NS_URI: return EcorePackage.EPACKAGE__NS_URI;
				case ExpressionsPackage.MODULE__NS_PREFIX: return EcorePackage.EPACKAGE__NS_PREFIX;
				case ExpressionsPackage.MODULE__EFACTORY_INSTANCE: return EcorePackage.EPACKAGE__EFACTORY_INSTANCE;
				case ExpressionsPackage.MODULE__ECLASSIFIERS: return EcorePackage.EPACKAGE__ECLASSIFIERS;
				case ExpressionsPackage.MODULE__ESUBPACKAGES: return EcorePackage.EPACKAGE__ESUBPACKAGES;
				case ExpressionsPackage.MODULE__ESUPER_PACKAGE: return EcorePackage.EPACKAGE__ESUPER_PACKAGE;
				default: return -1;
			}
		}
		if (baseClass == Visitable.class) {
			switch (derivedFeatureID) {
				default: return -1;
			}
		}
		if (baseClass == ASTNode.class) {
			switch (derivedFeatureID) {
				case ExpressionsPackage.MODULE__START_POSITION: return UtilitiesPackage.AST_NODE__START_POSITION;
				case ExpressionsPackage.MODULE__END_POSITION: return UtilitiesPackage.AST_NODE__END_POSITION;
				default: return -1;
			}
		}
		if (baseClass == VisitableASTNode.class) {
			switch (derivedFeatureID) {
				default: return -1;
			}
		}
		return super.eBaseStructuralFeatureID(derivedFeatureID, baseClass);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public int eDerivedStructuralFeatureID(int baseFeatureID, Class<?> baseClass) {
		if (baseClass == EPackage.class) {
			switch (baseFeatureID) {
				case EcorePackage.EPACKAGE__NS_URI: return ExpressionsPackage.MODULE__NS_URI;
				case EcorePackage.EPACKAGE__NS_PREFIX: return ExpressionsPackage.MODULE__NS_PREFIX;
				case EcorePackage.EPACKAGE__EFACTORY_INSTANCE: return ExpressionsPackage.MODULE__EFACTORY_INSTANCE;
				case EcorePackage.EPACKAGE__ECLASSIFIERS: return ExpressionsPackage.MODULE__ECLASSIFIERS;
				case EcorePackage.EPACKAGE__ESUBPACKAGES: return ExpressionsPackage.MODULE__ESUBPACKAGES;
				case EcorePackage.EPACKAGE__ESUPER_PACKAGE: return ExpressionsPackage.MODULE__ESUPER_PACKAGE;
				default: return -1;
			}
		}
		if (baseClass == Visitable.class) {
			switch (baseFeatureID) {
				default: return -1;
			}
		}
		if (baseClass == ASTNode.class) {
			switch (baseFeatureID) {
				case UtilitiesPackage.AST_NODE__START_POSITION: return ExpressionsPackage.MODULE__START_POSITION;
				case UtilitiesPackage.AST_NODE__END_POSITION: return ExpressionsPackage.MODULE__END_POSITION;
				default: return -1;
			}
		}
		if (baseClass == VisitableASTNode.class) {
			switch (baseFeatureID) {
				default: return -1;
			}
		}
		return super.eDerivedStructuralFeatureID(baseFeatureID, baseClass);
	}
	
	/**
	 * @generated NOT
	 */
	@Override
	public void freeze() {		
		super.freeze();
		
	    if (eClassifiers != null)
	    {
	      for (int i = 0, size = eClassifiers.size(); i < size; ++i)
	      {
	        freeze(eClassifiers.get(i));
	      }
	    }
	    if (eSubpackages != null)
	    {
	      for (int i = 0, size = eSubpackages.size(); i < size; ++i)
	      {
	        freeze(eSubpackages.get(i));
	      }
	    }		
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public String toString() {
		if (eIsProxy()) return super.toString();

		StringBuilder result = new StringBuilder(super.toString());
		result.append(" (nsURI: "); //$NON-NLS-1$
		result.append(nsURI);
		result.append(", nsPrefix: "); //$NON-NLS-1$
		result.append(nsPrefix);
		result.append(", startPosition: "); //$NON-NLS-1$
		result.append(startPosition);
		result.append(", endPosition: "); //$NON-NLS-1$
		result.append(endPosition);
		result.append(", isBlackbox: "); //$NON-NLS-1$
		if ((eFlags & IS_BLACKBOX_ESETFLAG) != 0) result.append((eFlags & IS_BLACKBOX_EFLAG) != 0); else result.append("<unset>"); //$NON-NLS-1$
		result.append(')');
		return result.toString();
	}

} //ModuleImpl
