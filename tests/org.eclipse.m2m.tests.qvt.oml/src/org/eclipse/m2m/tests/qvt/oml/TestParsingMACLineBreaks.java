/*******************************************************************************
 * Copyright (c) 2008, 2018 Borland Software Corporation and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v2.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v20.html
 *
 * Contributors:
 *     Borland Software Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.m2m.tests.qvt.oml;

import java.io.StringReader;

import org.eclipse.m2m.internal.qvt.oml.ast.env.QvtOperationalEnvFactory;
import org.eclipse.m2m.internal.qvt.oml.ast.parser.QvtOperationalParser;
import org.eclipse.m2m.internal.qvt.oml.cst.parser.QVTOLexer;
import org.eclipse.ocl.ParserException;
import org.junit.Test;

import junit.framework.TestCase;

public class TestParsingMACLineBreaks extends TestCase {

	public TestParsingMACLineBreaks(String name) {
		super(name);
	}

	@Test
	public void testBreakLastChar() throws Exception {
		String contents = "abc;\r"; //$NON-NLS-1$
		QVTOLexer lexer = createLexer(contents);

		final int lineCount = lexer.getILexStream().getLineCount();
		assertEquals(lineCount, 1);
	}

	@Test
	public void testBreakWinLastBreak() throws Exception {
		String contents = "abc;\r\n"; //$NON-NLS-1$
		QVTOLexer lexer = createLexer(contents);

		final int lineCount = lexer.getILexStream().getLineCount();
		assertEquals(lineCount, 1);
	}

	@Test
	public void testBreakUnixLastBreak() throws Exception {
		String contents = "abc;\n"; //$NON-NLS-1$
		QVTOLexer lexer = createLexer(contents);

		final int lineCount = lexer.getILexStream().getLineCount();
		assertEquals(lineCount, 1);
	}

	@Test
	public void testMixed() throws Exception {
		String contents = "a\n \r \r\n"; //$NON-NLS-1$
		QVTOLexer lexer = createLexer(contents);

		final int lineCount = lexer.getILexStream().getLineCount();
		assertEquals(lineCount, 3);
	}

	@Test
	public void testBreakFirstChar() throws Exception {
		String contents = "\rabc;"; //$NON-NLS-1$
		QVTOLexer lexer = createLexer(contents);

		final int lineCount = lexer.getILexStream().getLineCount();
		assertEquals(lineCount, 1);
	}

	@Test
	public void testBreakWinFirstBreak() throws Exception {
		String contents = "\r\nabc;"; //$NON-NLS-1$
		QVTOLexer lexer = createLexer(contents);

		final int lineCount = lexer.getILexStream().getLineCount();
		assertEquals(lineCount, 1);
	}

	@Test
	public void testBreakUnixFirstBreak() throws Exception {
		String contents = "\nabc;"; //$NON-NLS-1$
		QVTOLexer lexer = createLexer(contents);

		final int lineCount = lexer.getILexStream().getLineCount();
		assertEquals(lineCount, 1);
	}


	private QVTOLexer createLexer(String contents) throws ParserException {
		final QVTOLexer lexer = QvtOperationalParser.createLexer(
				new StringReader(contents), "testLineBreak",  //$NON-NLS-1$
				QvtOperationalEnvFactory.getInstance().createEnvironment());

		lexer.getILexStream().computeLineOffsets();

		return lexer;
	}
}
