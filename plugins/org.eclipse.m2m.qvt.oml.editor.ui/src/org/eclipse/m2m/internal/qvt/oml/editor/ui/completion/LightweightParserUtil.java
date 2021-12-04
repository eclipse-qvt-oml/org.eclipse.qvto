/*******************************************************************************
 * Copyright (c) 2007, 2018 Borland Software Corporation and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v2.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v20.html
 *
 * Contributors:
 *     Borland Software Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.m2m.internal.qvt.oml.editor.ui.completion;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import org.eclipse.emf.ecore.EClassifier;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.m2m.internal.qvt.oml.ast.env.QvtOperationalEnv;
import org.eclipse.m2m.internal.qvt.oml.ast.env.QvtOperationalEnvFactory;
import org.eclipse.m2m.internal.qvt.oml.ast.parser.QvtOperationalVisitorCS;
import org.eclipse.m2m.internal.qvt.oml.compiler.QvtCompilerOptions;
import org.eclipse.m2m.internal.qvt.oml.compiler.UnitProxy;
import org.eclipse.m2m.internal.qvt.oml.cst.TypeSpecCS;
import org.eclipse.m2m.internal.qvt.oml.cst.completion.parser.LightweightParser;
import org.eclipse.m2m.internal.qvt.oml.cst.completion.parser.LightweightTypeParser;
import org.eclipse.m2m.internal.qvt.oml.cst.parser.AbstractQVTParser;
import org.eclipse.m2m.internal.qvt.oml.cst.parser.QVTOLexer;
import org.eclipse.m2m.internal.qvt.oml.cst.parser.QVTOParsersym;
import org.eclipse.m2m.internal.qvt.oml.editor.ui.Activator;
import org.eclipse.ocl.OCLInput;
import org.eclipse.ocl.ParserException;
import org.eclipse.ocl.cst.CSTNode;
import org.eclipse.ocl.cst.OCLExpressionCS;
import org.eclipse.ocl.expressions.OCLExpression;
import org.eclipse.ocl.parser.OCLLexer;
import org.eclipse.ocl.utilities.PredefinedType;

import lpg.runtime.IPrsStream;
import lpg.runtime.IToken;
import lpg.runtime.PrsStream;

/**
 * @author aigdalov
 * Created on Oct 26, 2007
 */
public class LightweightParserUtil {
    public enum ParserTypeEnum {
        LIGHTWEIGHT_PARSER, LIGHTWEIGHT_TYPE_PARSER
    }

    public static final int[] IMPERATIVE_OPERATION_TOKENS = {
        QVTOParsersym.TK_mapping,
        QVTOParsersym.TK_helper,
        QVTOParsersym.TK_query,
        QVTOParsersym.TK_main
    };

    public static final int[] OCLEXPRESSION_START_TOKENS = {
        QVTOParsersym.TK_RESET_ASSIGN, QVTOParsersym.TK_ADD_ASSIGN,
        QVTOParsersym.TK_EQUAL, QVTOParsersym.TK_NOT_EQUAL, QVTOParsersym.TK_NOT_EQUAL_EXEQ,
        QVTOParsersym.TK_GREATER, QVTOParsersym.TK_LESS,
        QVTOParsersym.TK_GREATER_EQUAL, QVTOParsersym.TK_LESS_EQUAL,
        QVTOParsersym.TK_and, QVTOParsersym.TK_or,
        QVTOParsersym.TK_xor, QVTOParsersym.TK_implies,
        QVTOParsersym.TK_not,
        QVTOParsersym.TK_PLUS, QVTOParsersym.TK_MINUS,
        QVTOParsersym.TK_MULTIPLY, QVTOParsersym.TK_DIVIDE,
        QVTOParsersym.TK_BAR, QVTOParsersym.TK_QUESTIONMARK,
        QVTOParsersym.TK_COMMA, QVTOParsersym.TK_SEMICOLON,
        QVTOParsersym.TK_DOTDOT,
        QVTOParsersym.TK_if, QVTOParsersym.TK_then, QVTOParsersym.TK_else,
        QVTOParsersym.TK_LBRACE, QVTOParsersym.TK_LBRACKET, QVTOParsersym.TK_LPAREN,
        QVTOParsersym.TK_in,
        QVTOParsersym.TK_when,
        QVTOParsersym.TK_return
    };

    public static final int[] OCLEXPRESSION_END_TOKENS = {
        QVTOParsersym.TK_BAR, QVTOParsersym.TK_QUESTIONMARK,
        QVTOParsersym.TK_COMMA, QVTOParsersym.TK_SEMICOLON,
        QVTOParsersym.TK_DOTDOT,
        QVTOParsersym.TK_then, QVTOParsersym.TK_else, QVTOParsersym.TK_endif,
        QVTOParsersym.TK_RBRACE, QVTOParsersym.TK_RBRACKET, QVTOParsersym.TK_RPAREN,
        QVTOParsersym.TK_in,
    };

    public static final int[] OCLEXPRESSION_MANDATORY_TERMINATION_TOKENS =
        uniteIntArrays(IMPERATIVE_OPERATION_TOKENS, new int[] {
                QVTOParsersym.TK_init, QVTOParsersym.TK_end,
                QVTOParsersym.TK_transformation, QVTOParsersym.TK_modeltype,
                QVTOParsersym.TK_uses,
                QVTOParsersym.TK_import, QVTOParsersym.TK_library
        });

    public static final int[][] BRACING_PAIRS = {
        {QVTOParsersym.TK_if, QVTOParsersym.TK_endif},
        {QVTOParsersym.TK_LPAREN, QVTOParsersym.TK_RPAREN},
        {QVTOParsersym.TK_LBRACE, QVTOParsersym.TK_RBRACE},
        {QVTOParsersym.TK_LBRACKET, QVTOParsersym.TK_RBRACKET},
    };

    public static final int[] RESOLVE_FAMILY_TERMINALS = {
        QVTOParsersym.TK_resolve,
        QVTOParsersym.TK_resolveone,
        QVTOParsersym.TK_resolveIn,
        QVTOParsersym.TK_resolveoneIn,
        QVTOParsersym.TK_invresolve,
        QVTOParsersym.TK_invresolveone,
        QVTOParsersym.TK_invresolveIn,
        QVTOParsersym.TK_invresolveoneIn
    };

    public static final int[] RESOLVEIN_FAMILY_TERMINALS = {
        QVTOParsersym.TK_resolveIn,
        QVTOParsersym.TK_resolveoneIn,
        QVTOParsersym.TK_invresolveIn,
        QVTOParsersym.TK_invresolveoneIn
    };

    public static final String[] OCL_ITERATOR_TERMINALS = {
        PredefinedType.SELECT_NAME,
        PredefinedType.REJECT_NAME,
        PredefinedType.COLLECT_NAME,
        PredefinedType.FOR_ALL_NAME,
        PredefinedType.EXISTS_NAME,
        PredefinedType.IS_UNIQUE_NAME,
        PredefinedType.ONE_NAME,
        PredefinedType.ANY_NAME,
        PredefinedType.COLLECT_NESTED_NAME,
        PredefinedType.SORTED_BY_NAME,
        PredefinedType.CLOSURE_NAME,
    };

    public static final int[] QVTO_ITERATOR_TERMINALS_WITH_IMPLICIT_ITERATOR = {
        QVTOParsersym.TK_xselect,
        QVTOParsersym.TK_xcollect,
        QVTOParsersym.TK_selectOne,
        QVTOParsersym.TK_collectOne
    };

    public static final int[] QVTO_ITERATOR_TERMINALS =
        uniteIntArrays(QVTO_ITERATOR_TERMINALS_WITH_IMPLICIT_ITERATOR,  new int[] {
                QVTOParsersym.TK_collectselect,
                QVTOParsersym.TK_collectselectOne
        });

    public static final int[] MAPPING_CLAUSE_TOKENS = {
        QVTOParsersym.TK_when,
        QVTOParsersym.TK_where
    };

    public static final int[] MAPPING_CALL_TERMINALS = {
        QVTOParsersym.TK_map,
        QVTOParsersym.TK_xmap
    };

    public static final int[] FOR_EXP_TERMINALS = {
        QVTOParsersym.TK_forEach,
        QVTOParsersym.TK_forOne
    };

    public static int[] uniteIntArrays(int[] array1, int[] array2) {
        int[] result = new int[array1.length + array2.length];
        System.arraycopy(array1, 0, result, 0, array1.length);
        System.arraycopy(array2, 0, result, array1.length, array2.length);
        return result;
    }

    public static final IToken getNextToken(IToken token) {
        IPrsStream prsStream = token.getIPrsStream();
        int nextTokenIndex = token.getTokenIndex() + 1;
        if (nextTokenIndex < prsStream.getSize()) {
            return prsStream.getTokenAt(nextTokenIndex);
        }
        return null;
    }

    public static final IToken getPreviousToken(IToken token) {
        IPrsStream prsStream = token.getIPrsStream();
        int nextTokenIndex = token.getTokenIndex() - 1;
        if (nextTokenIndex >= 0) {
            return prsStream.getTokenAt(nextTokenIndex);
        }
        return null;
    }

    public static final IToken getNextTokenByKind(IToken startToken, int kind) {
        return getNextTokenByKind(startToken, new int[] {kind});
    }

    public static final IToken getNextTokenByKind(IToken startToken, int[] kinds) {
        IPrsStream prsStream = startToken.getIPrsStream();
        for (int i = startToken.getTokenIndex(), n = prsStream.getSize(); i < n; i++) {
            IToken token = prsStream.getTokenAt(i);
            if (QvtCompletionData.isKindOf(token, kinds)) {
                return token;
            }
        }
        return null;
    }

    public static final IToken getPreviousTokenByKind(IToken startToken, int kind) {
        return getPreviousTokenByKind(startToken, new int[] {kind});
    }

    public static final IToken getPreviousTokenByKind(IToken startToken, int[] kinds) {
        IToken currentToken = startToken;
        while ((currentToken = LightweightParserUtil.getPreviousToken(currentToken)) != null) {
            if (QvtCompletionData.isKindOf(currentToken, kinds)) {
                return currentToken;
            }
        }
        return null;
    }

    public static final String getTokenText(int tokenKind) {
        return QVTOParsersym.orderedTerminalSymbols[tokenKind];
    }

    public static final OCLExpression<EClassifier> getOclExpression(IToken trailingToken, QvtCompletionData data, ParserTypeEnum parserType) {
        OCLExpressionCS oclExpressionCS = getOclExpressionCS(trailingToken, data, parserType);
        return getOclExpression(oclExpressionCS, data);
    }

    public static final OCLExpression<EClassifier> getOclExpression(IToken[] tokens, QvtCompletionData data, ParserTypeEnum parserType) {
        OCLExpressionCS oclExpressionCS = getOclExpressionCS(tokens, data, parserType);
        if (oclExpressionCS != null) {
            return getOclExpression(oclExpressionCS, data);
        }
        return null;
    }

    public static final OCLExpression<EClassifier> getOclExpression(OCLExpressionCS oclExpressionCS, QvtCompletionData data) {
        if (oclExpressionCS != null) {
            OCLLexer oclLexer = new OCLLexer(data.getEnvironment(), new char[0], data.getCFile().getName(), 4);
            QvtCompilerOptions options = new QvtCompilerOptions();
            options.setReportErrors(false);
            options.setShowAnnotations(false);
            options.setSourceLineNumbersEnabled(false);
            QvtOperationalVisitorCS visitor = new QvtOperationalVisitorCS(oclLexer, options);

			return visitor.analyzeExpressionCS(oclExpressionCS, data.getEnvironment());
        }
        return null;
    }

    public static final OCLExpressionCS getOclExpressionCS(IToken[] tokens, QvtCompletionData data, ParserTypeEnum parserType) {
        if (tokens != null) {
            CSTNode cstNode = LightweightParserUtil.parse(tokens, data.getCFile(), parserType);
            if (cstNode instanceof OCLExpressionCS) {
                return (OCLExpressionCS) cstNode;
            }
            if (cstNode instanceof TypeSpecCS) {
                return ((TypeSpecCS) cstNode).getTypeCS();
            }
        }
        return null;
    }

    public static final String getText(IToken start, IToken end) {
        int startOffset = start.getStartOffset();
        int endOffset = end.getEndOffset();
        IPrsStream prsStream = start.getIPrsStream();
        return getText(startOffset, endOffset, prsStream);
    }

    public static final String getText(CSTNode cstNode, PrsStream prsStream) {
        int startOffset = cstNode.getStartOffset();
        int endOffset = cstNode.getEndOffset();
        return getText(startOffset, endOffset, prsStream);
    }

    private static String getText(int startOffset, int endOffset, IPrsStream prsStream) {
        return new String(prsStream.getInputChars(), startOffset, endOffset - startOffset + 1);
    }

    public static final String getText(IToken[] tokens) {
        if (tokens.length == 0) {
            return ""; //$NON-NLS-1$
        }
        return LightweightParserUtil.getText(tokens[0], tokens[tokens.length - 1]);
    }

    public static final CSTNode parse(IToken[] tokens, UnitProxy unit, ParserTypeEnum parserType) {
        String script = LightweightParserUtil.getText(tokens);
        return parse(script, unit, parserType);
    }

    public static final CSTNode parse(String script, UnitProxy unit, ParserTypeEnum parserType) {
        try {
        	QvtOperationalEnv env = new QvtOperationalEnvFactory(EPackage.Registry.INSTANCE).createEnvironment();
            QVTOLexer lexer = new QVTOLexer(env, new OCLInput(script).getContent(), unit.getName(), 4);
            AbstractQVTParser parser = null;
            switch (parserType) {
                case LIGHTWEIGHT_PARSER: parser = new RunnableLightweightParser(lexer); break;
                case LIGHTWEIGHT_TYPE_PARSER: parser = new RunnableLightweightTypeParser(lexer); break;
                default: throw new RuntimeException("Unknown parserType: " + parserType); //$NON-NLS-1$
            }
            parser.getIPrsStream().resetTokenStream();
            lexer.lexer(parser.getIPrsStream());
            return (CSTNode) ((ILightweightParser) parser).runParser();
        } catch (Exception ex) {
            Activator.log(ex);
            return null;
        }
    }

    private static final OCLExpressionCS getOclExpressionCS(IToken trailingToken, QvtCompletionData data, ParserTypeEnum parserType) {
        IToken[] tokens = extractOclExpressionCSTokens(trailingToken, data);
        return getOclExpressionCS(tokens, data, parserType);
    }

    public static final IToken[] extractOclExpressionCSTokens(IToken trailingToken, QvtCompletionData data) {
        List<IToken> tokens = new ArrayList<IToken>();
        IPrsStream prsStream = data.getPrsStream();
        int mode = BRACING_PAIRS.length;
        int depth = 0;
        for (int i = trailingToken.getTokenIndex() - 1; i >= 0; i--) {
            IToken token = prsStream.getTokenAt(i);
            if (QvtCompletionData.isKindOf(token, OCLEXPRESSION_MANDATORY_TERMINATION_TOKENS)) {
                return null;
            }
            if (depth == 0) {
                if (QvtCompletionData.isKindOf(token, OCLEXPRESSION_START_TOKENS)) {
                    return tokens.toArray(new IToken[tokens.size()]);
                } else if (QvtCompletionData.isKindOf(token, QVTOParsersym.TK_RPAREN)) {
                    // Considering switch in 07-07-07 spec: switch { case (expr) /@*@/ expr ...
                    IToken lParen = getPairingBrace(token, false);
                    if (lParen != null) {
                        IToken caseToken = LightweightParserUtil.getPreviousToken(lParen);
                        if ((caseToken != null) && QvtCompletionData.isKindOf(caseToken, QVTOParsersym.TK_case)) {
                            return tokens.toArray(new IToken[tokens.size()]);
                        }
                    }
                }
                for (int j = 0; j < BRACING_PAIRS.length; j++) {
                    if (token.getKind() == BRACING_PAIRS[j][1]) {
                        mode = j;
                        depth++;
                        break;
                    }
                }
            } else {
                if (token.getKind() == BRACING_PAIRS[mode][0]) {
                    depth--;
                } else if (token.getKind() == BRACING_PAIRS[mode][1]) {
                    depth++;
                }
            }
            tokens.add(0, token);
        }
        return null;
    }

    public static final IToken getPairingBrace(IToken brace, boolean isForward) {
        int bracingPairKind = getBracingPairKind(brace, isForward);
        int lBraceKind = BRACING_PAIRS[bracingPairKind][0];
        int rBraceKind = BRACING_PAIRS[bracingPairKind][1];
        int depth = 0;
        for (IToken token = brace; token != null; token = (isForward) ? LightweightParserUtil.getNextToken(token) : LightweightParserUtil.getPreviousToken(token)) {
            if (QvtCompletionData.isKindOf(token, lBraceKind)) {
                depth++;
            } else if (QvtCompletionData.isKindOf(token, rBraceKind)) {
                depth--;
            } else if (QvtCompletionData.isKindOf(token, OCLEXPRESSION_MANDATORY_TERMINATION_TOKENS)) {
                return null;
            }
            if (depth == 0) {
                return token;
            }
        }
        return null;
    }

    public static final int getBracingPairKind(IToken token, boolean isStart) {
        for (int i = 0; i < BRACING_PAIRS.length; i++) {
            int kind = isStart ? BRACING_PAIRS[i][0] : BRACING_PAIRS[i][1];
            if (QvtCompletionData.isKindOf(token, kind)) {
                return i;
            }
        }
        return -1;
    }

    public static final IToken[] getScopedIdentifier(IToken trailingToken) {
        boolean isColonColonExpected = QvtCompletionData.isKindOf(trailingToken, QVTOParsersym.TK_COLONCOLON);
        List<IToken> tokens = new LinkedList<IToken>();
        IToken currentToken = trailingToken;
        do {
            if (isColonColonExpected) {
                if (!QvtCompletionData.isKindOf(currentToken, QVTOParsersym.TK_COLONCOLON)) {
                    return tokens.toArray(new IToken[tokens.size()]);
                }
            } else {
                if (QvtCompletionData.isKindOf(currentToken, QVTOParsersym.TK_IDENTIFIER,
                        QVTOParsersym.TK_main)) {
                    tokens.add(0, currentToken);
                } else {
                    return null; // IDENTIFIER expected but smth else found!
                }
            }
            isColonColonExpected = !isColonColonExpected;
        } while ((currentToken = LightweightParserUtil.getPreviousToken(currentToken)) != null);
        return null; // unexpected start of stream!
    }

    private static interface ILightweightParser {
        public EObject runParser() throws ParserException;
    }


    private static class RunnableLightweightParser extends LightweightParser implements ILightweightParser {
        public RunnableLightweightParser(QVTOLexer lexStream) {
            super(lexStream);
        }

        @Override
		public CSTNode runParser() throws ParserException {
            return parser(-1);
        }
    }

    private static class RunnableLightweightTypeParser extends LightweightTypeParser implements ILightweightParser {
        public RunnableLightweightTypeParser(QVTOLexer lexStream) {
            super(lexStream);
        }

        @Override
		public CSTNode runParser() throws ParserException {
            return parser(-1);
        }
    }
}