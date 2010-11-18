/*******************************************************************************
 * Copyright (c) 2010 itemis AG (http://www.itemis.eu) and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *******************************************************************************/
package org.eclipse.xtext.xbase.scoping;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.xtext.common.types.JvmOperation;
import org.eclipse.xtext.common.types.JvmTypeReference;
import org.eclipse.xtext.common.types.util.IJvmTypeConformanceComputer;
import org.eclipse.xtext.common.types.util.TypeArgumentContext;
import org.eclipse.xtext.resource.IEObjectDescription;
import org.eclipse.xtext.scoping.IScope;
import org.eclipse.xtext.scoping.ISelector;

/**
 * 
 * A scope which goes through all returned EObjectDescriptions in order to find the best fit, if it is asked for the
 * 'first' element.
 * 
 * @author Sven Efftinge - Initial contribution and API
 */
public class BestMatchingJvmFeatureScope implements IScope {

	protected final EObject context;
	protected final EReference reference;
	private IJvmTypeConformanceComputer computer;
	private IScope delegate;
	private ISelector selector;

	public BestMatchingJvmFeatureScope(IJvmTypeConformanceComputer computer, EObject context, EReference ref,
			IScope delegate, ISelector selector) {
		this.computer = computer;
		this.context = context;
		this.reference = ref;
		this.delegate = delegate;
		this.selector = selector;
	}

	public IEObjectDescription getSingleElement(ISelector selector) {
		Iterable<IEObjectDescription> iterable = delegate.getElements(enhance(selector));
		return getBestMatch(iterable);
	}
	
	protected ISelector enhance(ISelector selector2) {
		if (selector2 instanceof ISelector.DelegatingSelector) {
			((ISelector.DelegatingSelector) selector2).addDelegate(selector);
		}
		return selector2;
	}

	public Iterable<IEObjectDescription> getElements(ISelector selector) {
		return delegate.getElements(enhance(selector));
	}

	protected IEObjectDescription getBestMatch(Iterable<IEObjectDescription> iterable) {
		IEObjectDescription bestMatch = null;
		for (IEObjectDescription description : iterable) {
			if (bestMatch == null) {
				bestMatch = description;
			} else {
				bestMatch = getBestMatch(bestMatch, description);
			}
		}
		return bestMatch;
	}

	protected IEObjectDescription getBestMatch(IEObjectDescription a, IEObjectDescription b) {
		if (a instanceof JvmFeatureDescription && b instanceof JvmFeatureDescription) {
			JvmFeatureDescription descA = (JvmFeatureDescription) a;
			JvmFeatureDescription descB = (JvmFeatureDescription) b;
			TypeArgumentContext contextA = descA.getContext();
			TypeArgumentContext contextB = descB.getContext();
			if (descA.getJvmFeature() instanceof JvmOperation) {
				if (descB.getJvmFeature() instanceof JvmOperation) {
					JvmOperation opA = (JvmOperation) descA.getJvmFeature();
					JvmOperation opB = (JvmOperation) descB.getJvmFeature();
					for (int i = 0; i < opA.getParameters().size(); i++) {
						JvmTypeReference pA = opA.getParameters().get(i).getParameterType();
						JvmTypeReference pB = opB.getParameters().get(i).getParameterType();
						if (!computer.isConformant(contextA.resolve(pA), contextB.resolve(pB)))
							return a;
					}
					return b;
				}
			}
		}
		return a;
	}
	
	@Override
	public String toString() {
		return getClass().getSimpleName()+" -> "+delegate;
	}
}
