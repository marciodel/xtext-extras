/*******************************************************************************
 * Copyright (c) 2008 itemis AG (http://www.itemis.eu) and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 *******************************************************************************/
package org.eclipse.xtext.crossrefs.indexbased;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.index.ECrossReferenceDescriptor;
import org.eclipse.emf.index.EObjectDescriptor;
import org.eclipse.emf.index.IIndexStore;
import org.eclipse.emf.index.impl.memory.InMemoryIndex;
import org.eclipse.xtext.crossref.IScopeProvider;
import org.eclipse.xtext.crossref.indexImpl.AbstractDeclarativeNameProvider;
import org.eclipse.xtext.crossref.indexImpl.INameProvider;
import org.eclipse.xtext.crossref.indexImpl.IndexAwareResourceSet;
import org.eclipse.xtext.crossref.indexImpl.IndexBasedScopeProvider;
import org.eclipse.xtext.crossrefs.ImportUriTestLanguageRuntimeModule;
import org.eclipse.xtext.crossrefs.ImportUriTestLanguageStandaloneSetup;
import org.eclipse.xtext.crossrefs.importedURI.Type;
import org.eclipse.xtext.tests.AbstractGeneratorTest;

import com.google.inject.Binder;
import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.Provider;

/**
 * @author Sven Efftinge - Initial contribution and API
 *
 */
public class IndexBasedScopeProviderTest extends AbstractGeneratorTest {

	public void testStuff() throws Exception {
		//TODO activate when Index  and Xtext separates between loading, linking and indexing of crossreferences
		
//		IndexAwareResourceSet set = get(IndexAwareResourceSet.class);
//		URI uri = URI.createURI("classpath:/" + IndexAwareResourcesetTest.class.getName().replace('.', '/')
//				+ ".importuritestlanguage");
//		System.out.println(uri);
//		set.getResource(uri, true);
//		set.getResource(URI.createURI("classpath:/" + getClass().getName().replace('.', '/')
//				+ ".importuritestlanguage"), true);
//		Iterator<EObjectDescriptor> result = set.getStore().eObjectDAO().createQuery().executeListResult()
//				.iterator();
//		List<String> names = new ArrayList<String>();
//		while (result.hasNext()) {
//			EObjectDescriptor next = result.next();
//			if (next.getName().equals("D")) {
//				Type obj = (Type) set.getEObject(next.getFragmentURI(), true);
//				assertEquals("A", obj.getExtends().getName());
//			}
//			names.add(next.getName());
//		}
//		assertTrue(names.contains("A"));
//		assertTrue(names.contains("B"));
//		assertTrue(names.contains("C"));
//		assertTrue(names.contains("D"));
//		assertEquals(4,names.size());
//		
//		Iterator<ECrossReferenceDescriptor> crossRefs = set.getStore().eCrossReferenceDAO().createQuery().executeListResult().iterator();
//		crossRefs.next();
//		crossRefs.next();
//		crossRefs.next();
//		assertFalse(crossRefs.hasNext());
	}

	private INameProvider nameProvider = new AbstractDeclarativeNameProvider() {
		@SuppressWarnings("unused")
		public String getName(Type type) {
			return type.getName();
		}
	};

	@Override
	protected void setUp() throws Exception {
		super.setUp();
		with(new ImportUriTestLanguageStandaloneSetup() {

			@Override
			public Injector createInjector() {
				return Guice.createInjector(new ImportUriTestLanguageRuntimeModule() {

					@Override
					public void configure(Binder binder) {
						super.configure(binder);
						binder.bind(INameProvider.class).toProvider(
								new Provider<? extends INameProvider>() {

									public INameProvider get() {
										return IndexBasedScopeProviderTest.this.getNameProvider();
									}

								});
						binder.bind(IIndexStore.class).toInstance(new InMemoryIndex());
					}

					@Override
					public Class<? extends IScopeProvider> bindIScopeProvider() {
						return IndexBasedScopeProvider.class;
					}
				});
			}
		});
	}

	/**
	 * @return
	 */
	protected INameProvider getNameProvider() {
		return this.nameProvider;
	}
}
