/**
* <copyright>
*
* Copyright (c) 2005, 2008 IBM Corporation and others.
* All rights reserved.   This program and the accompanying materials
* are made available under the terms of the Eclipse Public License v1.0
* which accompanies this distribution, and is available at
* http://www.eclipse.org/legal/epl-v10.html
*
* Contributors:
*   IBM - Initial API and implementation
*   E.D.Willink - Elimination of some shift-reduce conflicts
*   E.D.Willink - Remove unnecessary warning suppression
*   E.D.Willink - 225493 Need ability to set CSTNode offsets
*
* </copyright>
*
* $Id: QvtOpLPGParsersym.java,v 1.61 2009/02/16 12:44:05 aigdalov Exp $
*/
/**
* <copyright>
*
* Copyright (c) 2006, 2007 Borland Inc.
* All rights reserved.   This program and the accompanying materials
* are made available under the terms of the Eclipse Public License v1.0
* which accompanies this distribution, and is available at
* http://www.eclipse.org/legal/epl-v10.html
*
* Contributors:
*   Borland - Initial API and implementation
*
* </copyright>
*
* $Id: QvtOpLPGParsersym.java,v 1.61 2009/02/16 12:44:05 aigdalov Exp $
*/
/**
* <copyright>
*
* Copyright (c) 2006, 2007 Borland Inc.
* All rights reserved.   This program and the accompanying materials
* are made available under the terms of the Eclipse Public License v1.0
* which accompanies this distribution, and is available at
* http://www.eclipse.org/legal/epl-v10.html
*
* Contributors:
*   Borland - Initial API and implementation
*
* </copyright>
*
* $Id: QvtOpLPGParsersym.java,v 1.61 2009/02/16 12:44:05 aigdalov Exp $
*/
/**
* <copyright>
*
* Copyright (c) 2006, 2007 Borland Inc.
* All rights reserved.   This program and the accompanying materials
* are made available under the terms of the Eclipse Public License v1.0
* which accompanies this distribution, and is available at
* http://www.eclipse.org/legal/epl-v10.html
*
* Contributors:
*   Borland - Initial API and implementation
*
* </copyright>
*
* $Id: QvtOpLPGParsersym.java,v 1.61 2009/02/16 12:44:05 aigdalov Exp $
*/

package org.eclipse.m2m.internal.qvt.oml.cst.parser;

public interface QvtOpLPGParsersym {
    public final static int
      TK_NUMERIC_OPERATION = 71,
      TK_STRING_LITERAL = 20,
      TK_INTEGER_LITERAL = 55,
      TK_REAL_LITERAL = 56,
      TK_PLUS = 53,
      TK_MINUS = 54,
      TK_MULTIPLY = 21,
      TK_DIVIDE = 43,
      TK_GREATER = 23,
      TK_LESS = 24,
      TK_EQUAL = 5,
      TK_GREATER_EQUAL = 25,
      TK_LESS_EQUAL = 26,
      TK_NOT_EQUAL = 27,
      TK_LPAREN = 1,
      TK_RPAREN = 3,
      TK_LBRACE = 94,
      TK_RBRACE = 96,
      TK_LBRACKET = 100,
      TK_RBRACKET = 110,
      TK_ARROW = 120,
      TK_BAR = 99,
      TK_COMMA = 95,
      TK_COLON = 97,
      TK_COLONCOLON = 98,
      TK_SEMICOLON = 93,
      TK_DOT = 103,
      TK_DOTDOT = 121,
      TK_ATPRE = 112,
      TK_CARET = 122,
      TK_CARETCARET = 123,
      TK_QUESTIONMARK = 113,
      TK_ADD_ASSIGN = 140,
      TK_RESET_ASSIGN = 104,
      TK_AT_SIGN = 150,
      TK_EXCLAMATION_MARK = 114,
      TK_NOT_EQUAL_EXEQ = 111,
      TK_INTEGER_RANGE_START = 124,
      TK_class = 151,
      TK_composes = 152,
      TK_constructor = 125,
      TK_datatype = 174,
      TK_default = 175,
      TK_derived = 153,
      TK_do = 176,
      TK_elif = 177,
      TK_enum = 178,
      TK_except = 179,
      TK_exception = 180,
      TK_from = 181,
      TK_literal = 182,
      TK_ordered = 154,
      TK_primitive = 183,
      TK_raise = 184,
      TK_readonly = 155,
      TK_references = 156,
      TK_tag = 106,
      TK_try = 185,
      TK_typedef = 186,
      TK_unlimited = 187,
      TK_invalid = 188,
      TK_COLONCOLONEQUAL = 141,
      TK_STEREOTYPE_QUALIFIER_OPEN = 142,
      TK_STEREOTYPE_QUALIFIER_CLOSE = 157,
      TK_MULTIPLICITY_RANGE = 158,
      TK_TILDE_SIGN = 159,
      TK_self = 44,
      TK_inv = 189,
      TK_pre = 190,
      TK_post = 191,
      TK_context = 192,
      TK_package = 193,
      TK_endpackage = 194,
      TK_def = 195,
      TK_if = 72,
      TK_then = 143,
      TK_else = 126,
      TK_endif = 127,
      TK_and = 46,
      TK_or = 47,
      TK_xor = 48,
      TK_not = 57,
      TK_implies = 144,
      TK_let = 80,
      TK_in = 107,
      TK_true = 58,
      TK_false = 59,
      TK_body = 28,
      TK_derive = 29,
      TK_init = 160,
      TK_null = 49,
      TK_attr = 196,
      TK_oper = 197,
      TK_Set = 6,
      TK_Bag = 7,
      TK_Sequence = 8,
      TK_Collection = 9,
      TK_OrderedSet = 10,
      TK_iterate = 30,
      TK_forAll = 31,
      TK_exists = 32,
      TK_isUnique = 33,
      TK_any = 34,
      TK_one = 35,
      TK_collect = 36,
      TK_select = 37,
      TK_reject = 38,
      TK_collectNested = 39,
      TK_sortedBy = 40,
      TK_closure = 41,
      TK_oclIsKindOf = 60,
      TK_oclIsTypeOf = 61,
      TK_oclAsType = 62,
      TK_oclIsNew = 63,
      TK_oclIsUndefined = 64,
      TK_oclIsInvalid = 65,
      TK_oclIsInState = 66,
      TK_allInstances = 42,
      TK_String = 11,
      TK_Integer = 12,
      TK_UnlimitedNatural = 13,
      TK_Real = 14,
      TK_Boolean = 15,
      TK_Tuple = 22,
      TK_OclAny = 16,
      TK_OclVoid = 17,
      TK_Invalid = 18,
      TK_OclMessage = 19,
      TK_OclInvalid = 73,
      TK_end = 108,
      TK_while = 74,
      TK_out = 128,
      TK_object = 75,
      TK_transformation = 129,
      TK_import = 145,
      TK_library = 115,
      TK_metamodel = 198,
      TK_mapping = 130,
      TK_query = 131,
      TK_helper = 132,
      TK_inout = 133,
      TK_when = 116,
      TK_var = 82,
      TK_configuration = 117,
      TK_intermediate = 118,
      TK_property = 109,
      TK_opposites = 161,
      TK_population = 146,
      TK_map = 67,
      TK_new = 76,
      TK_xmap = 68,
      TK_late = 69,
      TK_log = 81,
      TK_assert = 83,
      TK_with = 162,
      TK_resolve = 85,
      TK_resolveone = 86,
      TK_resolveIn = 87,
      TK_resolveoneIn = 88,
      TK_invresolve = 89,
      TK_invresolveone = 90,
      TK_invresolveIn = 91,
      TK_invresolveoneIn = 92,
      TK_modeltype = 147,
      TK_uses = 163,
      TK_where = 164,
      TK_refines = 165,
      TK_access = 105,
      TK_extends = 101,
      TK_blackbox = 134,
      TK_abstract = 135,
      TK_static = 119,
      TK_result = 45,
      TK_main = 102,
      TK_this = 50,
      TK_switch = 70,
      TK_case = 148,
      TK_xselect = 166,
      TK_xcollect = 167,
      TK_selectOne = 168,
      TK_collectOne = 169,
      TK_collectselect = 170,
      TK_collectselectOne = 171,
      TK_return = 84,
      TK_rename = 149,
      TK_disjuncts = 136,
      TK_merges = 137,
      TK_inherits = 138,
      TK_forEach = 172,
      TK_forOne = 173,
      TK_compute = 77,
      TK_Dict = 51,
      TK_List = 52,
      TK_break = 78,
      TK_continue = 79,
      TK_EOF_TOKEN = 139,
      TK_IDENTIFIER = 2,
      TK_ERROR_TOKEN = 4;

      public final static String orderedTerminalSymbols[] = {
                 "",
                 "LPAREN",
                 "IDENTIFIER",
                 "RPAREN",
                 "ERROR_TOKEN",
                 "EQUAL",
                 "Set",
                 "Bag",
                 "Sequence",
                 "Collection",
                 "OrderedSet",
                 "String",
                 "Integer",
                 "UnlimitedNatural",
                 "Real",
                 "Boolean",
                 "OclAny",
                 "OclVoid",
                 "Invalid",
                 "OclMessage",
                 "STRING_LITERAL",
                 "MULTIPLY",
                 "Tuple",
                 "GREATER",
                 "LESS",
                 "GREATER_EQUAL",
                 "LESS_EQUAL",
                 "NOT_EQUAL",
                 "body",
                 "derive",
                 "iterate",
                 "forAll",
                 "exists",
                 "isUnique",
                 "any",
                 "one",
                 "collect",
                 "select",
                 "reject",
                 "collectNested",
                 "sortedBy",
                 "closure",
                 "allInstances",
                 "DIVIDE",
                 "self",
                 "result",
                 "and",
                 "or",
                 "xor",
                 "null",
                 "this",
                 "Dict",
                 "List",
                 "PLUS",
                 "MINUS",
                 "INTEGER_LITERAL",
                 "REAL_LITERAL",
                 "not",
                 "true",
                 "false",
                 "oclIsKindOf",
                 "oclIsTypeOf",
                 "oclAsType",
                 "oclIsNew",
                 "oclIsUndefined",
                 "oclIsInvalid",
                 "oclIsInState",
                 "map",
                 "xmap",
                 "late",
                 "switch",
                 "NUMERIC_OPERATION",
                 "if",
                 "OclInvalid",
                 "while",
                 "object",
                 "new",
                 "compute",
                 "break",
                 "continue",
                 "let",
                 "log",
                 "var",
                 "assert",
                 "return",
                 "resolve",
                 "resolveone",
                 "resolveIn",
                 "resolveoneIn",
                 "invresolve",
                 "invresolveone",
                 "invresolveIn",
                 "invresolveoneIn",
                 "SEMICOLON",
                 "LBRACE",
                 "COMMA",
                 "RBRACE",
                 "COLON",
                 "COLONCOLON",
                 "BAR",
                 "LBRACKET",
                 "extends",
                 "main",
                 "DOT",
                 "RESET_ASSIGN",
                 "access",
                 "tag",
                 "in",
                 "end",
                 "property",
                 "RBRACKET",
                 "NOT_EQUAL_EXEQ",
                 "ATPRE",
                 "QUESTIONMARK",
                 "EXCLAMATION_MARK",
                 "library",
                 "when",
                 "configuration",
                 "intermediate",
                 "static",
                 "ARROW",
                 "DOTDOT",
                 "CARET",
                 "CARETCARET",
                 "INTEGER_RANGE_START",
                 "constructor",
                 "else",
                 "endif",
                 "out",
                 "transformation",
                 "mapping",
                 "query",
                 "helper",
                 "inout",
                 "blackbox",
                 "abstract",
                 "disjuncts",
                 "merges",
                 "inherits",
                 "EOF_TOKEN",
                 "ADD_ASSIGN",
                 "COLONCOLONEQUAL",
                 "STEREOTYPE_QUALIFIER_OPEN",
                 "then",
                 "implies",
                 "import",
                 "population",
                 "modeltype",
                 "case",
                 "rename",
                 "AT_SIGN",
                 "class",
                 "composes",
                 "derived",
                 "ordered",
                 "readonly",
                 "references",
                 "STEREOTYPE_QUALIFIER_CLOSE",
                 "MULTIPLICITY_RANGE",
                 "TILDE_SIGN",
                 "init",
                 "opposites",
                 "with",
                 "uses",
                 "where",
                 "refines",
                 "xselect",
                 "xcollect",
                 "selectOne",
                 "collectOne",
                 "collectselect",
                 "collectselectOne",
                 "forEach",
                 "forOne",
                 "datatype",
                 "default",
                 "do",
                 "elif",
                 "enum",
                 "except",
                 "exception",
                 "from",
                 "literal",
                 "primitive",
                 "raise",
                 "try",
                 "typedef",
                 "unlimited",
                 "invalid",
                 "inv",
                 "pre",
                 "post",
                 "context",
                 "package",
                 "endpackage",
                 "def",
                 "attr",
                 "oper",
                 "metamodel"
             };

    public final static boolean isValidForParser = true;
}
