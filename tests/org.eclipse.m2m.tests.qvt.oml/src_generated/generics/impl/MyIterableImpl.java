/**
 */
package generics.impl;

import generics.GenericsFactory;
import generics.GenericsPackage;

import java.util.Iterator;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EClassifier;

import org.eclipse.emf.ecore.impl.MinimalEObjectImpl;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>My Iterable</b></em>'.
 * <!-- end-user-doc -->
 *
 * @generated NOT
 */
public class MyIterableImpl<C extends EClassifier> extends MinimalEObjectImpl.Container implements Iterable<C> {
	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected MyIterableImpl() {
		super();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	protected EClass eStaticClass() {
		return GenericsPackage.Literals.MY_ITERABLE;
	}
	
	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated NOT
	 */
	@Override
	public Iterator<C> iterator() {
		Iterable<C> iterable = GenericsFactory.eINSTANCE.createMyIterable();
		return iterable.iterator();
	};

} //MyIterableImpl
