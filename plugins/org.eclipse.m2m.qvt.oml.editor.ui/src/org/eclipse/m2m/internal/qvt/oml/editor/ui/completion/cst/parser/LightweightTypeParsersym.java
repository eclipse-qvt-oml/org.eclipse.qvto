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
* $Id: LightweightTypeParsersym.java,v 1.24 2008/12/02 12:00:12 aigdalov Exp $
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
* $Id: LightweightTypeParsersym.java,v 1.24 2008/12/02 12:00:12 aigdalov Exp $
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
* $Id: LightweightTypeParsersym.java,v 1.24 2008/12/02 12:00:12 aigdalov Exp $
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
* $Id: LightweightTypeParsersym.java,v 1.24 2008/12/02 12:00:12 aigdalov Exp $
*/

package org.eclipse.m2m.internal.qvt.oml.editor.ui.completion.cst.parser;

public interface LightweightTypeParsersym {
    public final static int
      TK_NUMERIC_OPERATION = 65,
      TK_STRING_LITERAL = 66,
      TK_INTEGER_LITERAL = 67,
      TK_REAL_LITERAL = 68,
      TK_PLUS = 33,
      TK_MINUS = 34,
      TK_MULTIPLY = 24,
      TK_DIVIDE = 25,
      TK_GREATER = 19,
      TK_LESS = 20,
      TK_EQUAL = 4,
      TK_GREATER_EQUAL = 21,
      TK_LESS_EQUAL = 22,
      TK_NOT_EQUAL = 23,
      TK_LPAREN = 1,
      TK_RPAREN = 2,
      TK_LBRACE = 86,
      TK_RBRACE = 92,
      TK_LBRACKET = 94,
      TK_RBRACKET = 100,
      TK_ARROW = 108,
      TK_BAR = 88,
      TK_COMMA = 87,
      TK_COLON = 89,
      TK_COLONCOLON = 90,
      TK_SEMICOLON = 91,
      TK_DOT = 109,
      TK_DOTDOT = 110,
      TK_ATPRE = 101,
      TK_CARET = 111,
      TK_CARETCARET = 112,
      TK_QUESTIONMARK = 102,
      TK_QUOTE_STRING_LITERAL = 130,
      TK_ADD_ASSIGN = 113,
      TK_RESET_ASSIGN = 93,
      TK_AT_SIGN = 117,
      TK_EXCLAMATION_MARK = 103,
      TK_NOT_EQUAL_EXEQ = 96,
      TK_INTEGER_RANGE_START = 104,
      TK_Dict = 131,
      TK_List = 132,
      TK_break = 133,
      TK_class = 134,
      TK_composes = 135,
      TK_constructor = 136,
      TK_continue = 137,
      TK_datatype = 138,
      TK_default = 139,
      TK_derived = 140,
      TK_do = 141,
      TK_elif = 142,
      TK_enum = 143,
      TK_except = 144,
      TK_exception = 145,
      TK_from = 146,
      TK_literal = 147,
      TK_ordered = 148,
      TK_primitive = 149,
      TK_raise = 150,
      TK_readonly = 151,
      TK_references = 152,
      TK_tag = 153,
      TK_try = 154,
      TK_typedef = 155,
      TK_unlimited = 156,
      TK_invalid = 157,
      TK_self = 26,
      TK_inv = 158,
      TK_pre = 159,
      TK_post = 160,
      TK_endpackage = 161,
      TK_def = 162,
      TK_if = 69,
      TK_then = 114,
      TK_else = 105,
      TK_endif = 106,
      TK_and = 28,
      TK_or = 29,
      TK_xor = 30,
      TK_not = 53,
      TK_implies = 115,
      TK_let = 77,
      TK_in = 107,
      TK_true = 70,
      TK_false = 71,
      TK_body = 36,
      TK_derive = 37,
      TK_init = 38,
      TK_null = 39,
      TK_Set = 14,
      TK_Bag = 15,
      TK_Sequence = 16,
      TK_Collection = 17,
      TK_OrderedSet = 18,
      TK_iterate = 40,
      TK_forAll = 41,
      TK_exists = 42,
      TK_isUnique = 43,
      TK_any = 44,
      TK_one = 45,
      TK_collect = 46,
      TK_select = 47,
      TK_reject = 48,
      TK_collectNested = 49,
      TK_sortedBy = 50,
      TK_closure = 51,
      TK_oclIsKindOf = 54,
      TK_oclIsTypeOf = 55,
      TK_oclAsType = 56,
      TK_oclIsNew = 57,
      TK_oclIsUndefined = 58,
      TK_oclIsInvalid = 59,
      TK_oclIsInState = 60,
      TK_allInstances = 52,
      TK_String = 5,
      TK_Integer = 6,
      TK_UnlimitedNatural = 7,
      TK_Real = 8,
      TK_Boolean = 9,
      TK_Tuple = 31,
      TK_OclAny = 10,
      TK_OclVoid = 11,
      TK_Invalid = 12,
      TK_OclMessage = 13,
      TK_OclInvalid = 72,
      TK_end = 163,
      TK_while = 73,
      TK_when = 118,
      TK_var = 97,
      TK_log = 95,
      TK_assert = 98,
      TK_with = 119,
      TK_switch = 63,
      TK_case = 116,
      TK_xselect = 120,
      TK_xcollect = 121,
      TK_selectOne = 122,
      TK_collectOne = 123,
      TK_collectselect = 124,
      TK_collectselectOne = 125,
      TK_return = 99,
      TK_forEach = 126,
      TK_forOne = 127,
      TK_compute = 74,
      TK_new = 75,
      TK_out = 164,
      TK_object = 76,
      TK_transformation = 165,
      TK_import = 166,
      TK_library = 167,
      TK_metamodel = 168,
      TK_mapping = 169,
      TK_query = 170,
      TK_helper = 171,
      TK_inout = 172,
      TK_configuration = 173,
      TK_intermediate = 174,
      TK_property = 175,
      TK_population = 176,
      TK_map = 61,
      TK_xmap = 62,
      TK_late = 64,
      TK_resolve = 78,
      TK_resolveone = 79,
      TK_resolveIn = 80,
      TK_resolveoneIn = 81,
      TK_invresolve = 82,
      TK_invresolveone = 83,
      TK_invresolveIn = 84,
      TK_invresolveoneIn = 85,
      TK_modeltype = 177,
      TK_uses = 178,
      TK_where = 179,
      TK_refines = 180,
      TK_enforcing = 181,
      TK_access = 182,
      TK_extends = 183,
      TK_blackbox = 184,
      TK_abstract = 185,
      TK_static = 186,
      TK_result = 27,
      TK_main = 128,
      TK_this = 32,
      TK_rename = 187,
      TK_inherits = 188,
      TK_merges = 189,
      TK_disjuncts = 190,
      TK_IDENTIFIER = 3,
      TK_ERROR_TOKEN = 35,
      TK_EOF_TOKEN = 129;

      public final static String orderedTerminalSymbols[] = {
                 "",
                 "LPAREN",
                 "RPAREN",
                 "IDENTIFIER",
                 "EQUAL",
                 "String",
                 "Integer",
                 "UnlimitedNatural",
                 "Real",
                 "Boolean",
                 "OclAny",
                 "OclVoid",
                 "Invalid",
                 "OclMessage",
                 "Set",
                 "Bag",
                 "Sequence",
                 "Collection",
                 "OrderedSet",
                 "GREATER",
                 "LESS",
                 "GREATER_EQUAL",
                 "LESS_EQUAL",
                 "NOT_EQUAL",
                 "MULTIPLY",
                 "DIVIDE",
                 "self",
                 "result",
                 "and",
                 "or",
                 "xor",
                 "Tuple",
                 "this",
                 "PLUS",
                 "MINUS",
                 "ERROR_TOKEN",
                 "body",
                 "derive",
                 "init",
                 "null",
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
                 "not",
                 "oclIsKindOf",
                 "oclIsTypeOf",
                 "oclAsType",
                 "oclIsNew",
                 "oclIsUndefined",
                 "oclIsInvalid",
                 "oclIsInState",
                 "map",
                 "xmap",
                 "switch",
                 "late",
                 "NUMERIC_OPERATION",
                 "STRING_LITERAL",
                 "INTEGER_LITERAL",
                 "REAL_LITERAL",
                 "if",
                 "true",
                 "false",
                 "OclInvalid",
                 "while",
                 "compute",
                 "new",
                 "object",
                 "let",
                 "resolve",
                 "resolveone",
                 "resolveIn",
                 "resolveoneIn",
                 "invresolve",
                 "invresolveone",
                 "invresolveIn",
                 "invresolveoneIn",
                 "LBRACE",
                 "COMMA",
                 "BAR",
                 "COLON",
                 "COLONCOLON",
                 "SEMICOLON",
                 "RBRACE",
                 "RESET_ASSIGN",
                 "LBRACKET",
                 "log",
                 "NOT_EQUAL_EXEQ",
                 "var",
                 "assert",
                 "return",
                 "RBRACKET",
                 "ATPRE",
                 "QUESTIONMARK",
                 "EXCLAMATION_MARK",
                 "INTEGER_RANGE_START",
                 "else",
                 "endif",
                 "in",
                 "ARROW",
                 "DOT",
                 "DOTDOT",
                 "CARET",
                 "CARETCARET",
                 "ADD_ASSIGN",
                 "then",
                 "implies",
                 "case",
                 "AT_SIGN",
                 "when",
                 "with",
                 "xselect",
                 "xcollect",
                 "selectOne",
                 "collectOne",
                 "collectselect",
                 "collectselectOne",
                 "forEach",
                 "forOne",
                 "main",
                 "EOF_TOKEN",
                 "QUOTE_STRING_LITERAL",
                 "Dict",
                 "List",
                 "break",
                 "class",
                 "composes",
                 "constructor",
                 "continue",
                 "datatype",
                 "default",
                 "derived",
                 "do",
                 "elif",
                 "enum",
                 "except",
                 "exception",
                 "from",
                 "literal",
                 "ordered",
                 "primitive",
                 "raise",
                 "readonly",
                 "references",
                 "tag",
                 "try",
                 "typedef",
                 "unlimited",
                 "invalid",
                 "inv",
                 "pre",
                 "post",
                 "endpackage",
                 "def",
                 "end",
                 "out",
                 "transformation",
                 "import",
                 "library",
                 "metamodel",
                 "mapping",
                 "query",
                 "helper",
                 "inout",
                 "configuration",
                 "intermediate",
                 "property",
                 "population",
                 "modeltype",
                 "uses",
                 "where",
                 "refines",
                 "enforcing",
                 "access",
                 "extends",
                 "blackbox",
                 "abstract",
                 "static",
                 "rename",
                 "inherits",
                 "merges",
                 "disjuncts"
             };

    public final static boolean isValidForParser = true;
}
