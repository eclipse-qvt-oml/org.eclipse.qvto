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
import org.eclipse.emf.ecore.EModelElement;
import org.eclipse.emf.ecore.EcorePackage;
import org.eclipse.emf.ecore.InternalEObject;
import org.eclipse.emf.ecore.impl.ENotificationImpl;
import org.eclipse.emf.ecore.impl.EObjectImpl;
import org.eclipse.emf.ecore.util.EObjectContainmentWithInverseEList;
import org.eclipse.emf.ecore.util.EObjectResolvingEList;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.eclipse.emf.ecore.util.InternalEList;
import org.eclipse.m2m.internal.qvt.oml.expressions.ExpressionsPackage;
import org.eclipse.m2m.internal.qvt.oml.expressions.ImportKind;
import org.eclipse.m2m.internal.qvt.oml.expressions.ModelType;
import org.eclipse.m2m.internal.qvt.oml.expressions.ModuleImport;
import org.eclipse.ocl.utilities.ASTNode;
import org.eclipse.ocl.utilities.UtilitiesPackage;
import org.eclipse.ocl.utilities.Visitor;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Module Import</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * </p>
 * <ul>
 *   <li>{@link org.eclipse.m2m.internal.qvt.oml.expressions.impl.ModuleImportImpl#getStartPosition <em>Start Position</em>}</li>
 *   <li>{@link org.eclipse.m2m.internal.qvt.oml.expressions.impl.ModuleImportImpl#getEndPosition <em>End Position</em>}</li>
 *   <li>{@link org.eclipse.m2m.internal.qvt.oml.expressions.impl.ModuleImportImpl#getEAnnotations <em>EAnnotations</em>}</li>
 *   <li>{@link org.eclipse.m2m.internal.qvt.oml.expressions.impl.ModuleImportImpl#getBinding <em>Binding</em>}</li>
 *   <li>{@link org.eclipse.m2m.internal.qvt.oml.expressions.impl.ModuleImportImpl#getImportedModule <em>Imported Module</em>}</li>
 *   <li>{@link org.eclipse.m2m.internal.qvt.oml.expressions.impl.ModuleImportImpl#getKind <em>Kind</em>}</li>
 *   <li>{@link org.eclipse.m2m.internal.qvt.oml.expressions.impl.ModuleImportImpl#getModule <em>Module</em>}</li>
 * </ul>
 *
 * @generated
 */
public class ModuleImportImpl extends EObjectImpl implements ModuleImport {
	/**
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 */
    public static final String copyright = "Copyright (c) 2007 Borland Software Corporation\r\n\r\nAll rights reserved. This program and the accompanying materials\r\nare made available under the terms of the Eclipse Public License v2.0\r\nwhich accompanies this distribution, and is available at\r\nhttp://www.eclipse.org/legal/epl-v20.html\r\n  \r\nContributors:\r\n    Borland Software Corporation - initial API and implementation"; //$NON-NLS-1$

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
	 * The cached value of the '{@link #getEAnnotations() <em>EAnnotations</em>}' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getEAnnotations()
	 * @generated
	 * @ordered
	 */
	protected EList<EAnnotation> eAnnotations;

	/**
	 * The cached value of the '{@link #getBinding() <em>Binding</em>}' reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getBinding()
	 * @generated
	 * @ordered
	 */
	protected EList<ModelType> binding;

	/**
	 * The cached value of the '{@link #getImportedModule() <em>Imported Module</em>}' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getImportedModule()
	 * @generated
	 * @ordered
	 */
	protected org.eclipse.m2m.internal.qvt.oml.expressions.Module importedModule;

	/**
	 * The default value of the '{@link #getKind() <em>Kind</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getKind()
	 * @generated
	 * @ordered
	 */
	protected static final ImportKind KIND_EDEFAULT = ImportKind.EXTENSION;

	/**
	 * The cached value of the '{@link #getKind() <em>Kind</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getKind()
	 * @generated
	 * @ordered
	 */
	protected ImportKind kind = KIND_EDEFAULT;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected ModuleImportImpl() {
		super();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	protected EClass eStaticClass() {
		return ExpressionsPackage.Literals.MODULE_IMPORT;
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
			eNotify(new ENotificationImpl(this, Notification.SET, ExpressionsPackage.MODULE_IMPORT__START_POSITION, oldStartPosition, startPosition));
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
			eNotify(new ENotificationImpl(this, Notification.SET, ExpressionsPackage.MODULE_IMPORT__END_POSITION, oldEndPosition, endPosition));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EList<EAnnotation> getEAnnotations() {
		if (eAnnotations == null) {
			eAnnotations = new EObjectContainmentWithInverseEList<EAnnotation>(EAnnotation.class, this, ExpressionsPackage.MODULE_IMPORT__EANNOTATIONS, EcorePackage.EANNOTATION__EMODEL_ELEMENT);
		}
		return eAnnotations;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EList<ModelType> getBinding() {
		if (binding == null) {
			binding = new EObjectResolvingEList<ModelType>(ModelType.class, this, ExpressionsPackage.MODULE_IMPORT__BINDING);
		}
		return binding;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public org.eclipse.m2m.internal.qvt.oml.expressions.Module getModule() {
		if (eContainerFeatureID() != ExpressionsPackage.MODULE_IMPORT__MODULE) return null;
		return (org.eclipse.m2m.internal.qvt.oml.expressions.Module)eInternalContainer();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public NotificationChain basicSetModule(org.eclipse.m2m.internal.qvt.oml.expressions.Module newModule, NotificationChain msgs) {
		msgs = eBasicSetContainer((InternalEObject)newModule, ExpressionsPackage.MODULE_IMPORT__MODULE, msgs);
		return msgs;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setModule(org.eclipse.m2m.internal.qvt.oml.expressions.Module newModule) {
		if (newModule != eInternalContainer() || (eContainerFeatureID() != ExpressionsPackage.MODULE_IMPORT__MODULE && newModule != null)) {
			if (EcoreUtil.isAncestor(this, newModule))
				throw new IllegalArgumentException("Recursive containment not allowed for " + toString()); //$NON-NLS-1$
			NotificationChain msgs = null;
			if (eInternalContainer() != null)
				msgs = eBasicRemoveFromContainer(msgs);
			if (newModule != null)
				msgs = ((InternalEObject)newModule).eInverseAdd(this, ExpressionsPackage.MODULE__MODULE_IMPORT, org.eclipse.m2m.internal.qvt.oml.expressions.Module.class, msgs);
			msgs = basicSetModule(newModule, msgs);
			if (msgs != null) msgs.dispatch();
		}
		else if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, ExpressionsPackage.MODULE_IMPORT__MODULE, newModule, newModule));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public org.eclipse.m2m.internal.qvt.oml.expressions.Module getImportedModule() {
		if (importedModule != null && importedModule.eIsProxy()) {
			InternalEObject oldImportedModule = (InternalEObject)importedModule;
			importedModule = (org.eclipse.m2m.internal.qvt.oml.expressions.Module)eResolveProxy(oldImportedModule);
			if (importedModule != oldImportedModule) {
				if (eNotificationRequired())
					eNotify(new ENotificationImpl(this, Notification.RESOLVE, ExpressionsPackage.MODULE_IMPORT__IMPORTED_MODULE, oldImportedModule, importedModule));
			}
		}
		return importedModule;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public org.eclipse.m2m.internal.qvt.oml.expressions.Module basicGetImportedModule() {
		return importedModule;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setImportedModule(org.eclipse.m2m.internal.qvt.oml.expressions.Module newImportedModule) {
		org.eclipse.m2m.internal.qvt.oml.expressions.Module oldImportedModule = importedModule;
		importedModule = newImportedModule;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, ExpressionsPackage.MODULE_IMPORT__IMPORTED_MODULE, oldImportedModule, importedModule));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public ImportKind getKind() {
		return kind;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setKind(ImportKind newKind) {
		ImportKind oldKind = kind;
		kind = newKind == null ? KIND_EDEFAULT : newKind;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, ExpressionsPackage.MODULE_IMPORT__KIND, oldKind, kind));
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
		  return visitorExt.visitModuleImport(this);
		}
		return org.eclipse.m2m.internal.qvt.oml.expressions.util.ForeignVisitorDefaultValue.getDefaultValueForVisitor(v);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAnnotation getEAnnotation(String source) {
		// TODO: implement this method
		// Ensure that you remove @generated or mark it @generated NOT
		throw new UnsupportedOperationException();
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
			case ExpressionsPackage.MODULE_IMPORT__EANNOTATIONS:
				return ((InternalEList<InternalEObject>)(InternalEList<?>)getEAnnotations()).basicAdd(otherEnd, msgs);
			case ExpressionsPackage.MODULE_IMPORT__MODULE:
				if (eInternalContainer() != null)
					msgs = eBasicRemoveFromContainer(msgs);
				return basicSetModule((org.eclipse.m2m.internal.qvt.oml.expressions.Module)otherEnd, msgs);
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
			case ExpressionsPackage.MODULE_IMPORT__EANNOTATIONS:
				return ((InternalEList<?>)getEAnnotations()).basicRemove(otherEnd, msgs);
			case ExpressionsPackage.MODULE_IMPORT__MODULE:
				return basicSetModule(null, msgs);
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
			case ExpressionsPackage.MODULE_IMPORT__MODULE:
				return eInternalContainer().eInverseRemove(this, ExpressionsPackage.MODULE__MODULE_IMPORT, org.eclipse.m2m.internal.qvt.oml.expressions.Module.class, msgs);
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
			case ExpressionsPackage.MODULE_IMPORT__START_POSITION:
				return getStartPosition();
			case ExpressionsPackage.MODULE_IMPORT__END_POSITION:
				return getEndPosition();
			case ExpressionsPackage.MODULE_IMPORT__EANNOTATIONS:
				return getEAnnotations();
			case ExpressionsPackage.MODULE_IMPORT__BINDING:
				return getBinding();
			case ExpressionsPackage.MODULE_IMPORT__IMPORTED_MODULE:
				if (resolve) return getImportedModule();
				return basicGetImportedModule();
			case ExpressionsPackage.MODULE_IMPORT__KIND:
				return getKind();
			case ExpressionsPackage.MODULE_IMPORT__MODULE:
				return getModule();
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
			case ExpressionsPackage.MODULE_IMPORT__START_POSITION:
				setStartPosition((Integer)newValue);
				return;
			case ExpressionsPackage.MODULE_IMPORT__END_POSITION:
				setEndPosition((Integer)newValue);
				return;
			case ExpressionsPackage.MODULE_IMPORT__EANNOTATIONS:
				getEAnnotations().clear();
				getEAnnotations().addAll((Collection<? extends EAnnotation>)newValue);
				return;
			case ExpressionsPackage.MODULE_IMPORT__BINDING:
				getBinding().clear();
				getBinding().addAll((Collection<? extends ModelType>)newValue);
				return;
			case ExpressionsPackage.MODULE_IMPORT__IMPORTED_MODULE:
				setImportedModule((org.eclipse.m2m.internal.qvt.oml.expressions.Module)newValue);
				return;
			case ExpressionsPackage.MODULE_IMPORT__KIND:
				setKind((ImportKind)newValue);
				return;
			case ExpressionsPackage.MODULE_IMPORT__MODULE:
				setModule((org.eclipse.m2m.internal.qvt.oml.expressions.Module)newValue);
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
			case ExpressionsPackage.MODULE_IMPORT__START_POSITION:
				setStartPosition(START_POSITION_EDEFAULT);
				return;
			case ExpressionsPackage.MODULE_IMPORT__END_POSITION:
				setEndPosition(END_POSITION_EDEFAULT);
				return;
			case ExpressionsPackage.MODULE_IMPORT__EANNOTATIONS:
				getEAnnotations().clear();
				return;
			case ExpressionsPackage.MODULE_IMPORT__BINDING:
				getBinding().clear();
				return;
			case ExpressionsPackage.MODULE_IMPORT__IMPORTED_MODULE:
				setImportedModule((org.eclipse.m2m.internal.qvt.oml.expressions.Module)null);
				return;
			case ExpressionsPackage.MODULE_IMPORT__KIND:
				setKind(KIND_EDEFAULT);
				return;
			case ExpressionsPackage.MODULE_IMPORT__MODULE:
				setModule((org.eclipse.m2m.internal.qvt.oml.expressions.Module)null);
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
			case ExpressionsPackage.MODULE_IMPORT__START_POSITION:
				return startPosition != START_POSITION_EDEFAULT;
			case ExpressionsPackage.MODULE_IMPORT__END_POSITION:
				return endPosition != END_POSITION_EDEFAULT;
			case ExpressionsPackage.MODULE_IMPORT__EANNOTATIONS:
				return eAnnotations != null && !eAnnotations.isEmpty();
			case ExpressionsPackage.MODULE_IMPORT__BINDING:
				return binding != null && !binding.isEmpty();
			case ExpressionsPackage.MODULE_IMPORT__IMPORTED_MODULE:
				return importedModule != null;
			case ExpressionsPackage.MODULE_IMPORT__KIND:
				return kind != KIND_EDEFAULT;
			case ExpressionsPackage.MODULE_IMPORT__MODULE:
				return getModule() != null;
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
		if (baseClass == ASTNode.class) {
			switch (derivedFeatureID) {
				case ExpressionsPackage.MODULE_IMPORT__START_POSITION: return UtilitiesPackage.AST_NODE__START_POSITION;
				case ExpressionsPackage.MODULE_IMPORT__END_POSITION: return UtilitiesPackage.AST_NODE__END_POSITION;
				default: return -1;
			}
		}
		if (baseClass == EModelElement.class) {
			switch (derivedFeatureID) {
				case ExpressionsPackage.MODULE_IMPORT__EANNOTATIONS: return EcorePackage.EMODEL_ELEMENT__EANNOTATIONS;
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
		if (baseClass == ASTNode.class) {
			switch (baseFeatureID) {
				case UtilitiesPackage.AST_NODE__START_POSITION: return ExpressionsPackage.MODULE_IMPORT__START_POSITION;
				case UtilitiesPackage.AST_NODE__END_POSITION: return ExpressionsPackage.MODULE_IMPORT__END_POSITION;
				default: return -1;
			}
		}
		if (baseClass == EModelElement.class) {
			switch (baseFeatureID) {
				case EcorePackage.EMODEL_ELEMENT__EANNOTATIONS: return ExpressionsPackage.MODULE_IMPORT__EANNOTATIONS;
				default: return -1;
			}
		}
		return super.eDerivedStructuralFeatureID(baseFeatureID, baseClass);
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
		result.append(" (startPosition: "); //$NON-NLS-1$
		result.append(startPosition);
		result.append(", endPosition: "); //$NON-NLS-1$
		result.append(endPosition);
		result.append(", kind: "); //$NON-NLS-1$
		result.append(kind);
		result.append(')');
		return result.toString();
	}

} //ModuleImportImpl
