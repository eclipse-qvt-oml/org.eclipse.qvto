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
import org.eclipse.emf.ecore.util.EObjectContainmentEList;
import org.eclipse.emf.ecore.util.EObjectContainmentWithInverseEList;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.eclipse.emf.ecore.util.InternalEList;
import org.eclipse.m2m.internal.qvt.oml.expressions.ExpressionsPackage;
import org.eclipse.m2m.internal.qvt.oml.expressions.ImperativeOperation;
import org.eclipse.m2m.internal.qvt.oml.expressions.OperationBody;
import org.eclipse.ocl.ecore.OCLExpression;
import org.eclipse.ocl.ecore.Variable;
import org.eclipse.ocl.utilities.ASTNode;
import org.eclipse.ocl.utilities.UtilitiesPackage;
import org.eclipse.ocl.utilities.Visitor;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Operation Body</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * </p>
 * <ul>
 *   <li>{@link org.eclipse.m2m.internal.qvt.oml.expressions.impl.OperationBodyImpl#getStartPosition <em>Start Position</em>}</li>
 *   <li>{@link org.eclipse.m2m.internal.qvt.oml.expressions.impl.OperationBodyImpl#getEndPosition <em>End Position</em>}</li>
 *   <li>{@link org.eclipse.m2m.internal.qvt.oml.expressions.impl.OperationBodyImpl#getEAnnotations <em>EAnnotations</em>}</li>
 *   <li>{@link org.eclipse.m2m.internal.qvt.oml.expressions.impl.OperationBodyImpl#getContent <em>Content</em>}</li>
 *   <li>{@link org.eclipse.m2m.internal.qvt.oml.expressions.impl.OperationBodyImpl#getOperation <em>Operation</em>}</li>
 *   <li>{@link org.eclipse.m2m.internal.qvt.oml.expressions.impl.OperationBodyImpl#getVariable <em>Variable</em>}</li>
 * </ul>
 *
 * @generated
 */
public class OperationBodyImpl extends EObjectImpl implements OperationBody {
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
	 * The cached value of the '{@link #getContent() <em>Content</em>}' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getContent()
	 * @generated
	 * @ordered
	 */
	protected EList<OCLExpression> content;

	/**
	 * The cached value of the '{@link #getVariable() <em>Variable</em>}' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getVariable()
	 * @generated
	 * @ordered
	 */
	protected EList<Variable> variable;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected OperationBodyImpl() {
		super();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	protected EClass eStaticClass() {
		return ExpressionsPackage.Literals.OPERATION_BODY;
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
			eNotify(new ENotificationImpl(this, Notification.SET, ExpressionsPackage.OPERATION_BODY__START_POSITION, oldStartPosition, startPosition));
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
			eNotify(new ENotificationImpl(this, Notification.SET, ExpressionsPackage.OPERATION_BODY__END_POSITION, oldEndPosition, endPosition));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EList<EAnnotation> getEAnnotations() {
		if (eAnnotations == null) {
			eAnnotations = new EObjectContainmentWithInverseEList<EAnnotation>(EAnnotation.class, this, ExpressionsPackage.OPERATION_BODY__EANNOTATIONS, EcorePackage.EANNOTATION__EMODEL_ELEMENT);
		}
		return eAnnotations;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public ImperativeOperation getOperation() {
		if (eContainerFeatureID() != ExpressionsPackage.OPERATION_BODY__OPERATION) return null;
		return (ImperativeOperation)eInternalContainer();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public NotificationChain basicSetOperation(ImperativeOperation newOperation, NotificationChain msgs) {
		msgs = eBasicSetContainer((InternalEObject)newOperation, ExpressionsPackage.OPERATION_BODY__OPERATION, msgs);
		return msgs;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setOperation(ImperativeOperation newOperation) {
		if (newOperation != eInternalContainer() || (eContainerFeatureID() != ExpressionsPackage.OPERATION_BODY__OPERATION && newOperation != null)) {
			if (EcoreUtil.isAncestor(this, newOperation))
				throw new IllegalArgumentException("Recursive containment not allowed for " + toString()); //$NON-NLS-1$
			NotificationChain msgs = null;
			if (eInternalContainer() != null)
				msgs = eBasicRemoveFromContainer(msgs);
			if (newOperation != null)
				msgs = ((InternalEObject)newOperation).eInverseAdd(this, ExpressionsPackage.IMPERATIVE_OPERATION__BODY, ImperativeOperation.class, msgs);
			msgs = basicSetOperation(newOperation, msgs);
			if (msgs != null) msgs.dispatch();
		}
		else if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, ExpressionsPackage.OPERATION_BODY__OPERATION, newOperation, newOperation));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EList<OCLExpression> getContent() {
		if (content == null) {
			content = new EObjectContainmentEList<OCLExpression>(OCLExpression.class, this, ExpressionsPackage.OPERATION_BODY__CONTENT);
		}
		return content;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EList<Variable> getVariable() {
		if (variable == null) {
			variable = new EObjectContainmentEList<Variable>(Variable.class, this, ExpressionsPackage.OPERATION_BODY__VARIABLE);
		}
		return variable;
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
		  return visitorExt.visitOperationBody(this);
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
			case ExpressionsPackage.OPERATION_BODY__EANNOTATIONS:
				return ((InternalEList<InternalEObject>)(InternalEList<?>)getEAnnotations()).basicAdd(otherEnd, msgs);
			case ExpressionsPackage.OPERATION_BODY__OPERATION:
				if (eInternalContainer() != null)
					msgs = eBasicRemoveFromContainer(msgs);
				return basicSetOperation((ImperativeOperation)otherEnd, msgs);
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
			case ExpressionsPackage.OPERATION_BODY__EANNOTATIONS:
				return ((InternalEList<?>)getEAnnotations()).basicRemove(otherEnd, msgs);
			case ExpressionsPackage.OPERATION_BODY__CONTENT:
				return ((InternalEList<?>)getContent()).basicRemove(otherEnd, msgs);
			case ExpressionsPackage.OPERATION_BODY__OPERATION:
				return basicSetOperation(null, msgs);
			case ExpressionsPackage.OPERATION_BODY__VARIABLE:
				return ((InternalEList<?>)getVariable()).basicRemove(otherEnd, msgs);
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
			case ExpressionsPackage.OPERATION_BODY__OPERATION:
				return eInternalContainer().eInverseRemove(this, ExpressionsPackage.IMPERATIVE_OPERATION__BODY, ImperativeOperation.class, msgs);
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
			case ExpressionsPackage.OPERATION_BODY__START_POSITION:
				return getStartPosition();
			case ExpressionsPackage.OPERATION_BODY__END_POSITION:
				return getEndPosition();
			case ExpressionsPackage.OPERATION_BODY__EANNOTATIONS:
				return getEAnnotations();
			case ExpressionsPackage.OPERATION_BODY__CONTENT:
				return getContent();
			case ExpressionsPackage.OPERATION_BODY__OPERATION:
				return getOperation();
			case ExpressionsPackage.OPERATION_BODY__VARIABLE:
				return getVariable();
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
			case ExpressionsPackage.OPERATION_BODY__START_POSITION:
				setStartPosition((Integer)newValue);
				return;
			case ExpressionsPackage.OPERATION_BODY__END_POSITION:
				setEndPosition((Integer)newValue);
				return;
			case ExpressionsPackage.OPERATION_BODY__EANNOTATIONS:
				getEAnnotations().clear();
				getEAnnotations().addAll((Collection<? extends EAnnotation>)newValue);
				return;
			case ExpressionsPackage.OPERATION_BODY__CONTENT:
				getContent().clear();
				getContent().addAll((Collection<? extends OCLExpression>)newValue);
				return;
			case ExpressionsPackage.OPERATION_BODY__OPERATION:
				setOperation((ImperativeOperation)newValue);
				return;
			case ExpressionsPackage.OPERATION_BODY__VARIABLE:
				getVariable().clear();
				getVariable().addAll((Collection<? extends Variable>)newValue);
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
			case ExpressionsPackage.OPERATION_BODY__START_POSITION:
				setStartPosition(START_POSITION_EDEFAULT);
				return;
			case ExpressionsPackage.OPERATION_BODY__END_POSITION:
				setEndPosition(END_POSITION_EDEFAULT);
				return;
			case ExpressionsPackage.OPERATION_BODY__EANNOTATIONS:
				getEAnnotations().clear();
				return;
			case ExpressionsPackage.OPERATION_BODY__CONTENT:
				getContent().clear();
				return;
			case ExpressionsPackage.OPERATION_BODY__OPERATION:
				setOperation((ImperativeOperation)null);
				return;
			case ExpressionsPackage.OPERATION_BODY__VARIABLE:
				getVariable().clear();
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
			case ExpressionsPackage.OPERATION_BODY__START_POSITION:
				return startPosition != START_POSITION_EDEFAULT;
			case ExpressionsPackage.OPERATION_BODY__END_POSITION:
				return endPosition != END_POSITION_EDEFAULT;
			case ExpressionsPackage.OPERATION_BODY__EANNOTATIONS:
				return eAnnotations != null && !eAnnotations.isEmpty();
			case ExpressionsPackage.OPERATION_BODY__CONTENT:
				return content != null && !content.isEmpty();
			case ExpressionsPackage.OPERATION_BODY__OPERATION:
				return getOperation() != null;
			case ExpressionsPackage.OPERATION_BODY__VARIABLE:
				return variable != null && !variable.isEmpty();
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
				case ExpressionsPackage.OPERATION_BODY__START_POSITION: return UtilitiesPackage.AST_NODE__START_POSITION;
				case ExpressionsPackage.OPERATION_BODY__END_POSITION: return UtilitiesPackage.AST_NODE__END_POSITION;
				default: return -1;
			}
		}
		if (baseClass == EModelElement.class) {
			switch (derivedFeatureID) {
				case ExpressionsPackage.OPERATION_BODY__EANNOTATIONS: return EcorePackage.EMODEL_ELEMENT__EANNOTATIONS;
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
				case UtilitiesPackage.AST_NODE__START_POSITION: return ExpressionsPackage.OPERATION_BODY__START_POSITION;
				case UtilitiesPackage.AST_NODE__END_POSITION: return ExpressionsPackage.OPERATION_BODY__END_POSITION;
				default: return -1;
			}
		}
		if (baseClass == EModelElement.class) {
			switch (baseFeatureID) {
				case EcorePackage.EMODEL_ELEMENT__EANNOTATIONS: return ExpressionsPackage.OPERATION_BODY__EANNOTATIONS;
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
		result.append(')');
		return result.toString();
	}

} //OperationBodyImpl
