/*******************************************************************************
 * Copyright (c) 2013 itemis AG (http://www.itemis.eu) and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *******************************************************************************/
package org.eclipse.xtext.xbase.tests.scoping;

import org.eclipse.xtext.naming.QualifiedName;
import org.eclipse.xtext.scoping.impl.ImportNormalizer;

/**
 * @author Sebastian Zarnekow - Initial contribution and API
 */
public class ImportWithDotDefaultNormalizerTest extends AbstractImportWithDotTest {

	@Override
	protected ImportNormalizer createImportNormalizer(QualifiedName imported, boolean wildcard) {
		return new ImportNormalizer(imported, wildcard, false);
	}
	
	@Override
	public void testIgnoreCase() {
		// no exception expected
		new ImportNormalizer(QualifiedName.create("a"), false, true);
	}
	
	@Override
	protected void doTestMap_Map$Entry(QualifiedName resolved) {
		assertNull(resolved);
	}
	
}
