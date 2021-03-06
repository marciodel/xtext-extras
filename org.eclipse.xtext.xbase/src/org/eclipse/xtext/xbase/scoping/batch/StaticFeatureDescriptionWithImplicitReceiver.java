/*******************************************************************************
 * Copyright (c) 2013 itemis AG (http://www.itemis.eu) and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *******************************************************************************/
package org.eclipse.xtext.xbase.scoping.batch;

import org.eclipse.xtext.common.types.JvmFeature;
import org.eclipse.xtext.naming.QualifiedName;
import org.eclipse.xtext.xbase.XExpression;
import org.eclipse.xtext.xbase.XbaseFactory;
import org.eclipse.xtext.xbase.typesystem.references.LightweightTypeReference;

/**
 * @author Sebastian Zarnekow - Initial contribution and API
 */
public class StaticFeatureDescriptionWithImplicitReceiver extends StaticFeatureDescription {

	private final LightweightTypeReference implicitReceiverType;
	private final XExpression implicitReceiver;

	public StaticFeatureDescriptionWithImplicitReceiver(QualifiedName qualifiedName, JvmFeature feature, 
			LightweightTypeReference receiverType,
			int bucketId, boolean visible) {
		super(qualifiedName, feature, bucketId, visible);
		this.implicitReceiverType = receiverType;
		this.implicitReceiver = XbaseFactory.eINSTANCE.createXFeatureCall();
	}
	
	@Override
	/* @Nullable */
	public LightweightTypeReference getImplicitReceiverType() {
		return implicitReceiverType;
	}
	
	@Override
	public XExpression getImplicitReceiver() {
		return implicitReceiver;
	}
	
	@Override
	public boolean isValidStaticState() {
		return implicitReceiverType == null;
	}
	
}
