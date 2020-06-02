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
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.InternalEObject;
import org.eclipse.emf.ecore.impl.ENotificationImpl;
import org.eclipse.emf.ecore.util.EObjectContainmentEList;
import org.eclipse.emf.ecore.util.EObjectResolvingEList;
import org.eclipse.emf.ecore.util.InternalEList;
import org.eclipse.m2m.internal.qvt.oml.expressions.ExpressionsPackage;
import org.eclipse.m2m.internal.qvt.oml.expressions.MappingOperation;
import org.eclipse.ocl.ecore.OCLExpression;
import org.eclipse.ocl.utilities.Visitor;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Mapping Operation</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * </p>
 * <ul>
 *   <li>{@link org.eclipse.m2m.internal.qvt.oml.expressions.impl.MappingOperationImpl#getDisjunct <em>Disjunct</em>}</li>
 *   <li>{@link org.eclipse.m2m.internal.qvt.oml.expressions.impl.MappingOperationImpl#getInherited <em>Inherited</em>}</li>
 *   <li>{@link org.eclipse.m2m.internal.qvt.oml.expressions.impl.MappingOperationImpl#getMerged <em>Merged</em>}</li>
 *   <li>{@link org.eclipse.m2m.internal.qvt.oml.expressions.impl.MappingOperationImpl#getWhen <em>When</em>}</li>
 *   <li>{@link org.eclipse.m2m.internal.qvt.oml.expressions.impl.MappingOperationImpl#getWhere <em>Where</em>}</li>
 * </ul>
 *
 * @generated
 */
public class MappingOperationImpl extends ImperativeOperationImpl implements MappingOperation {
	/**
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 */
    public static final String copyright = "Copyright (c) 2007 Borland Software Corporation\r\n\r\nAll rights reserved. This program and the accompanying materials\r\nare made available under the terms of the Eclipse Public License v2.0\r\nwhich accompanies this distribution, and is available at\r\nhttp://www.eclipse.org/legal/epl-v20.html\r\n  \r\nContributors:\r\n    Borland Software Corporation - initial API and implementation"; //$NON-NLS-1$
    /**
	 * The cached value of the '{@link #getDisjunct() <em>Disjunct</em>}' reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getDisjunct()
	 * @generated
	 * @ordered
	 */
	protected EList<MappingOperation> disjunct;
				/**
	 * The cached value of the '{@link #getInherited() <em>Inherited</em>}' reference list.
	 * <!-- begin-user-doc -->
				 * <!-- end-user-doc -->
	 * @see #getInherited()
	 * @generated
	 * @ordered
	 */
				protected EList<MappingOperation> inherited;
				/**
	 * The cached value of the '{@link #getMerged() <em>Merged</em>}' reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getMerged()
	 * @generated
	 * @ordered
	 */
	protected EList<MappingOperation> merged;
				/**
	 * The cached value of the '{@link #getWhen() <em>When</em>}' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getWhen()
	 * @generated
	 * @ordered
	 */
	protected EList<OCLExpression> when;
				/**
	 * The cached value of the '{@link #getWhere() <em>Where</em>}' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getWhere()
	 * @generated
	 * @ordered
	 */
	protected OCLExpression where;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected MappingOperationImpl() {
		super();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	protected EClass eStaticClass() {
		return ExpressionsPackage.Literals.MAPPING_OPERATION;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EList<OCLExpression> getWhen() {
		if (when == null) {
			when = new EObjectContainmentEList<OCLExpression>(OCLExpression.class, this, ExpressionsPackage.MAPPING_OPERATION__WHEN);
		}
		return when;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public OCLExpression getWhere() {
		return where;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public NotificationChain basicSetWhere(OCLExpression newWhere, NotificationChain msgs) {
		OCLExpression oldWhere = where;
		where = newWhere;
		if (eNotificationRequired()) {
			ENotificationImpl notification = new ENotificationImpl(this, Notification.SET, ExpressionsPackage.MAPPING_OPERATION__WHERE, oldWhere, newWhere);
			if (msgs == null) msgs = notification; else msgs.add(notification);
		}
		return msgs;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setWhere(OCLExpression newWhere) {
		if (newWhere != where) {
			NotificationChain msgs = null;
			if (where != null)
				msgs = ((InternalEObject)where).eInverseRemove(this, EOPPOSITE_FEATURE_BASE - ExpressionsPackage.MAPPING_OPERATION__WHERE, null, msgs);
			if (newWhere != null)
				msgs = ((InternalEObject)newWhere).eInverseAdd(this, EOPPOSITE_FEATURE_BASE - ExpressionsPackage.MAPPING_OPERATION__WHERE, null, msgs);
			msgs = basicSetWhere(newWhere, msgs);
			if (msgs != null) msgs.dispatch();
		}
		else if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, ExpressionsPackage.MAPPING_OPERATION__WHERE, newWhere, newWhere));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EList<MappingOperation> getInherited() {
		if (inherited == null) {
			inherited = new EObjectResolvingEList<MappingOperation>(MappingOperation.class, this, ExpressionsPackage.MAPPING_OPERATION__INHERITED);
		}
		return inherited;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EList<MappingOperation> getMerged() {
		if (merged == null) {
			merged = new EObjectResolvingEList<MappingOperation>(MappingOperation.class, this, ExpressionsPackage.MAPPING_OPERATION__MERGED);
		}
		return merged;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EList<MappingOperation> getDisjunct() {
		if (disjunct == null) {
			disjunct = new EObjectResolvingEList<MappingOperation>(MappingOperation.class, this, ExpressionsPackage.MAPPING_OPERATION__DISJUNCT);
		}
		return disjunct;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public <T, U extends Visitor<T, ?, ?, ?, ?, ?, ?, ?, ?, ?>> T accept(final U v) {
		if(v instanceof org.eclipse.m2m.internal.qvt.oml.expressions.util.QVTOperationalVisitor) {
		  @SuppressWarnings("unchecked")    		
		  org.eclipse.m2m.internal.qvt.oml.expressions.util.QVTOperationalVisitor<T> visitorExt = (org.eclipse.m2m.internal.qvt.oml.expressions.util.QVTOperationalVisitor) v;    	
		  return visitorExt.visitMappingOperation(this);
		}
		return org.eclipse.m2m.internal.qvt.oml.expressions.util.ForeignVisitorDefaultValue.getDefaultValueForVisitor(v);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public NotificationChain eInverseRemove(InternalEObject otherEnd, int featureID, NotificationChain msgs) {
		switch (featureID) {
			case ExpressionsPackage.MAPPING_OPERATION__WHEN:
				return ((InternalEList<?>)getWhen()).basicRemove(otherEnd, msgs);
			case ExpressionsPackage.MAPPING_OPERATION__WHERE:
				return basicSetWhere(null, msgs);
		}
		return super.eInverseRemove(otherEnd, featureID, msgs);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public Object eGet(int featureID, boolean resolve, boolean coreType) {
		switch (featureID) {
			case ExpressionsPackage.MAPPING_OPERATION__DISJUNCT:
				return getDisjunct();
			case ExpressionsPackage.MAPPING_OPERATION__INHERITED:
				return getInherited();
			case ExpressionsPackage.MAPPING_OPERATION__MERGED:
				return getMerged();
			case ExpressionsPackage.MAPPING_OPERATION__WHEN:
				return getWhen();
			case ExpressionsPackage.MAPPING_OPERATION__WHERE:
				return getWhere();
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
			case ExpressionsPackage.MAPPING_OPERATION__DISJUNCT:
				getDisjunct().clear();
				getDisjunct().addAll((Collection<? extends MappingOperation>)newValue);
				return;
			case ExpressionsPackage.MAPPING_OPERATION__INHERITED:
				getInherited().clear();
				getInherited().addAll((Collection<? extends MappingOperation>)newValue);
				return;
			case ExpressionsPackage.MAPPING_OPERATION__MERGED:
				getMerged().clear();
				getMerged().addAll((Collection<? extends MappingOperation>)newValue);
				return;
			case ExpressionsPackage.MAPPING_OPERATION__WHEN:
				getWhen().clear();
				getWhen().addAll((Collection<? extends OCLExpression>)newValue);
				return;
			case ExpressionsPackage.MAPPING_OPERATION__WHERE:
				setWhere((OCLExpression)newValue);
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
			case ExpressionsPackage.MAPPING_OPERATION__DISJUNCT:
				getDisjunct().clear();
				return;
			case ExpressionsPackage.MAPPING_OPERATION__INHERITED:
				getInherited().clear();
				return;
			case ExpressionsPackage.MAPPING_OPERATION__MERGED:
				getMerged().clear();
				return;
			case ExpressionsPackage.MAPPING_OPERATION__WHEN:
				getWhen().clear();
				return;
			case ExpressionsPackage.MAPPING_OPERATION__WHERE:
				setWhere((OCLExpression)null);
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
			case ExpressionsPackage.MAPPING_OPERATION__DISJUNCT:
				return disjunct != null && !disjunct.isEmpty();
			case ExpressionsPackage.MAPPING_OPERATION__INHERITED:
				return inherited != null && !inherited.isEmpty();
			case ExpressionsPackage.MAPPING_OPERATION__MERGED:
				return merged != null && !merged.isEmpty();
			case ExpressionsPackage.MAPPING_OPERATION__WHEN:
				return when != null && !when.isEmpty();
			case ExpressionsPackage.MAPPING_OPERATION__WHERE:
				return where != null;
		}
		return super.eIsSet(featureID);
	}

} //MappingOperationImpl
