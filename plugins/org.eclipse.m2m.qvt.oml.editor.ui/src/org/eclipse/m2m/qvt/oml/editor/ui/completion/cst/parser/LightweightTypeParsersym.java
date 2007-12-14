/**
* <copyright>
*
* Copyright (c) 2007 Borland Software Corporation
* 
* All rights reserved. This program and the accompanying materials
* are made available under the terms of the Eclipse Public License v1.0
* which accompanies this distribution, and is available at
* http://www.eclipse.org/legal/epl-v10.html
*
* Contributors:
*     Borland Software Corporation - initial API and implementation
*
* </copyright>
*
* $Id: LightweightTypeParsersym.java,v 1.5 2007/12/14 13:22:27 aigdalov Exp $
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
* $Id: LightweightTypeParsersym.java,v 1.5 2007/12/14 13:22:27 aigdalov Exp $
*/

package org.eclipse.m2m.qvt.oml.editor.ui.completion.cst.parser;

public interface LightweightTypeParsersym {
    public final static int
      TK_NUMERIC_OPERATION = 64,
      TK_STRING_LITERAL = 65,
      TK_INTEGER_LITERAL = 66,
      TK_REAL_LITERAL = 67,
      TK_PLUS = 33,
      TK_MINUS = 34,
      TK_MULTIPLY = 21,
      TK_DIVIDE = 22,
      TK_GREATER = 23,
      TK_LESS = 24,
      TK_EQUAL = 5,
      TK_GREATER_EQUAL = 25,
      TK_LESS_EQUAL = 26,
      TK_NOT_EQUAL = 15,
      TK_LPAREN = 1,
      TK_RPAREN = 4,
      TK_LBRACE = 86,
      TK_RBRACE = 85,
      TK_LBRACKET = 98,
      TK_RBRACKET = 94,
      TK_ARROW = 104,
      TK_BAR = 89,
      TK_COMMA = 84,
      TK_COLON = 90,
      TK_COLONCOLON = 87,
      TK_SEMICOLON = 88,
      TK_DOT = 105,
      TK_DOTDOT = 106,
      TK_ATPRE = 91,
      TK_CARET = 107,
      TK_CARETCARET = 108,
      TK_QUESTIONMARK = 95,
      TK_QUOTE_STRING_LITERAL = 116,
      TK_ADD_ASSIGN = 99,
      TK_RESET_ASSIGN = 92,
      TK_AT_SIGN = 111,
      TK_self = 27,
      TK_inv = 117,
      TK_pre = 118,
      TK_post = 119,
      TK_endpackage = 120,
      TK_def = 121,
      TK_if = 68,
      TK_then = 109,
      TK_else = 100,
      TK_endif = 101,
      TK_and = 28,
      TK_or = 29,
      TK_xor = 30,
      TK_not = 53,
      TK_implies = 110,
      TK_let = 75,
      TK_in = 102,
      TK_true = 69,
      TK_false = 70,
      TK_body = 36,
      TK_derive = 37,
      TK_init = 38,
      TK_null = 39,
      TK_attr = 122,
      TK_oper = 123,
      TK_Set = 16,
      TK_Bag = 17,
      TK_Sequence = 18,
      TK_Collection = 19,
      TK_OrderedSet = 20,
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
      TK_String = 6,
      TK_Integer = 7,
      TK_UnlimitedNatural = 8,
      TK_Real = 9,
      TK_Boolean = 10,
      TK_Tuple = 35,
      TK_OclAny = 11,
      TK_OclVoid = 12,
      TK_Invalid = 13,
      TK_OclMessage = 14,
      TK_OclInvalid = 71,
      TK_end = 124,
      TK_while = 72,
      TK_out = 125,
      TK_object = 73,
      TK_transformation = 126,
      TK_import = 127,
      TK_library = 128,
      TK_metamodel = 129,
      TK_mapping = 130,
      TK_query = 131,
      TK_inout = 132,
      TK_when = 112,
      TK_var = 96,
      TK_configuration = 133,
      TK_property = 134,
      TK_map = 61,
      TK_xmap = 62,
      TK_late = 63,
      TK_log = 93,
      TK_assert = 97,
      TK_with = 113,
      TK_resolve = 76,
      TK_resolveone = 77,
      TK_resolveIn = 78,
      TK_resolveoneIn = 79,
      TK_invresolve = 80,
      TK_invresolveone = 81,
      TK_invresolveIn = 82,
      TK_invresolveoneIn = 83,
      TK_modeltype = 135,
      TK_uses = 136,
      TK_where = 137,
      TK_refines = 138,
      TK_enforcing = 139,
      TK_access = 140,
      TK_extends = 141,
      TK_blackbox = 142,
      TK_abstract = 143,
      TK_static = 144,
      TK_result = 31,
      TK_main = 114,
      TK_this = 32,
      TK_switch = 74,
      TK_rename = 145,
      TK_IDENTIFIER = 2,
      TK_ERROR_TOKEN = 3,
      TK_INTEGER_RANGE_START = 103,
      TK_EOF_TOKEN = 115;

      public final static String orderedTerminalSymbols[] = {
                 "",
                 "LPAREN",
                 "IDENTIFIER",
                 "ERROR_TOKEN",
                 "RPAREN",
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
                 "NOT_EQUAL",
                 "Set",
                 "Bag",
                 "Sequence",
                 "Collection",
                 "OrderedSet",
                 "MULTIPLY",
                 "DIVIDE",
                 "GREATER",
                 "LESS",
                 "GREATER_EQUAL",
                 "LESS_EQUAL",
                 "self",
                 "and",
                 "or",
                 "xor",
                 "result",
                 "this",
                 "PLUS",
                 "MINUS",
                 "Tuple",
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
                 "object",
                 "switch",
                 "let",
                 "resolve",
                 "resolveone",
                 "resolveIn",
                 "resolveoneIn",
                 "invresolve",
                 "invresolveone",
                 "invresolveIn",
                 "invresolveoneIn",
                 "COMMA",
                 "RBRACE",
                 "LBRACE",
                 "COLONCOLON",
                 "SEMICOLON",
                 "BAR",
                 "COLON",
                 "ATPRE",
                 "RESET_ASSIGN",
                 "log",
                 "RBRACKET",
                 "QUESTIONMARK",
                 "var",
                 "assert",
                 "LBRACKET",
                 "ADD_ASSIGN",
                 "else",
                 "endif",
                 "in",
                 "INTEGER_RANGE_START",
                 "ARROW",
                 "DOT",
                 "DOTDOT",
                 "CARET",
                 "CARETCARET",
                 "then",
                 "implies",
                 "AT_SIGN",
                 "when",
                 "with",
                 "main",
                 "EOF_TOKEN",
                 "QUOTE_STRING_LITERAL",
                 "inv",
                 "pre",
                 "post",
                 "endpackage",
                 "def",
                 "attr",
                 "oper",
                 "end",
                 "out",
                 "transformation",
                 "import",
                 "library",
                 "metamodel",
                 "mapping",
                 "query",
                 "inout",
                 "configuration",
                 "property",
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
                 "rename"
             };

    public final static boolean isValidForParser = true;
}
