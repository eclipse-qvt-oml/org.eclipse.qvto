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
* $Id: QvtOpKWLexerprs.java,v 1.1 2008/03/12 11:48:07 sboyko Exp $
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
* $Id: QvtOpKWLexerprs.java,v 1.1 2008/03/12 11:48:07 sboyko Exp $
*/

package org.eclipse.m2m.qvt.oml.internal.cst.parser;

public class QvtOpKWLexerprs implements lpg.lpgjavaruntime.ParseTable, QvtOpKWLexersym {

    public interface IsKeyword {
        public final static byte isKeyword[] = {0,
            0,0,0,0,0,0,0,0,0,0,
            0,0,0,0,0,0,0,0,0,0,
            0,0,0,0,0,0,0,0,0,0,
            0,0,0,0,0,0,0,0,0,0,
            0,0,0,0,0,0,0,0,0,0,
            0,0,0,0
        };
    };
    public final static byte isKeyword[] = IsKeyword.isKeyword;
    public final boolean isKeyword(int index) { return isKeyword[index] != 0; }

    public interface BaseCheck {
        public final static byte baseCheck[] = {0,
            4,4,2,4,4,5,3,2,3,3,
            7,3,2,4,5,3,3,8,10,10,
            7,6,6,8,3,3,7,6,6,13,
            8,7,11,11,9,8,14,12,12,12,
            6,7,16,4,7,5,6,7,7,10,
            4,10,1,3,5,3,6,14,6,7,
            9,9,6,8,6,6,7,5,6,5,
            4,3,13,10,8,3,4,4,3,6,
            4,7,10,9,12,10,13,12,15,9,
            4,5,7,9,6,7,8,8,6,6,
            4,4,6,4,7,8,9,10,13,16
        };
    };
    public final static byte baseCheck[] = BaseCheck.baseCheck;
    public final int baseCheck(int index) { return baseCheck[index]; }
    public final static byte rhs[] = baseCheck;
    public final int rhs(int index) { return rhs[index]; };

    public interface BaseAction {
        public final static char baseAction[] = {
            1,2,2,2,2,2,2,2,2,2,
            2,2,2,2,2,2,2,2,2,2,
            2,2,2,2,2,2,2,2,2,2,
            2,2,2,2,2,2,2,2,2,2,
            2,2,2,2,2,2,2,2,2,2,
            2,2,2,1,1,1,1,1,1,1,
            1,1,1,1,1,1,1,1,1,1,
            1,1,1,1,1,1,1,1,1,1,
            1,1,1,1,1,1,1,1,1,1,
            1,1,1,1,1,1,1,1,1,1,
            1,1,1,1,1,1,1,1,1,1,
            1,1,114,53,173,189,192,127,194,201,
            199,204,111,109,206,207,88,110,213,122,
            29,53,124,136,139,84,16,89,34,45,
            70,142,57,100,215,216,219,209,221,225,
            230,232,223,140,40,143,154,220,226,236,
            150,63,238,239,161,241,242,244,246,249,
            250,79,251,252,255,259,260,263,262,267,
            268,271,274,273,275,277,281,286,284,278,
            290,292,156,157,166,294,170,175,291,300,
            293,98,297,302,305,304,307,309,310,313,
            315,319,316,320,324,318,329,332,330,51,
            333,334,336,342,343,345,347,64,349,351,
            354,356,359,360,361,78,366,368,369,370,
            371,335,375,377,379,381,384,387,382,389,
            141,392,396,398,393,400,402,403,406,404,
            413,408,414,415,417,421,183,422,423,424,
            430,431,434,437,184,426,440,438,443,445,
            446,447,448,449,452,455,462,464,453,468,
            460,466,473,474,467,476,479,480,481,490,
            493,494,497,498,499,502,507,509,510,482,
            513,515,484,516,518,520,521,522,527,523,
            531,533,535,538,539,541,542,540,543,551,
            554,556,552,560,561,562,564,544,565,568,
            569,575,571,579,580,581,584,586,587,593,
            595,500,598,600,602,601,591,605,607,609,
            610,596,617,611,613,619,620,624,625,629,
            627,631,427,52,635,636,638,642,639,643,
            645,646,649,651,652,656,658,659,661,662,
            664,666,669,674,678,679,667,670,671,684,
            687,688,691,692,699,690,701,697,704,694,
            707,708,711,712,713,715,717,716,720,721,
            726,728,730,731,733,734,737,738,740,747,
            745,749,751,742,754,17,755,756,758,759,
            763,765,178,761,123,769,767,770,777,778,
            780,781,171,784,785,787,786,788,792,793,
            791,802,804,806,797,809,807,814,816,818,
            820,821,823,825,827,829,835,833,830,837,
            840,841,844,843,849,853,847,856,851,855,
            857,860,862,863,867,869,870,877,875,872,
            881,884,885,887,889,890,879,892,895,894,
            897,899,901,906,909,914,915,919,920,907,
            922,923,927,188,928,931,932,933,937,941,
            935,944,945,948,949,953,939,955,957,960,
            961,964,965,963,967,968,973,969,902,974,
            975,980,981,555,555
        };
    };
    public final static char baseAction[] = BaseAction.baseAction;
    public final int baseAction(int index) { return baseAction[index]; }
    public final static char lhs[] = baseAction;
    public final int lhs(int index) { return lhs[index]; };

    public interface TermCheck {
        public final static byte termCheck[] = {0,
            0,1,2,3,4,5,6,7,8,9,
            10,11,12,13,14,0,0,17,18,19,
            20,21,22,8,24,25,26,12,0,29,
            30,31,32,0,34,7,3,37,10,0,
            7,2,26,10,0,12,7,3,4,5,
            0,0,0,3,10,22,0,7,2,3,
            8,5,0,0,2,3,22,5,17,0,
            14,8,3,4,18,12,14,0,0,28,
            2,30,31,0,1,23,35,0,0,6,
            3,8,9,5,17,26,8,0,10,0,
            1,2,24,6,27,8,18,8,0,0,
            0,1,3,36,6,38,19,20,8,9,
            0,0,0,0,25,4,0,5,20,8,
            9,8,9,7,8,0,1,2,0,0,
            0,0,0,21,2,6,8,9,7,0,
            28,2,13,0,1,0,0,17,2,6,
            0,20,6,3,4,0,11,27,19,0,
            0,16,0,0,0,1,11,0,9,14,
            6,12,0,0,1,8,4,0,0,19,
            8,0,4,0,17,8,5,14,0,29,
            0,1,9,0,17,0,0,1,0,6,
            12,39,0,1,0,0,1,12,0,0,
            0,13,0,9,0,0,8,7,4,0,
            1,0,13,11,9,0,5,0,0,4,
            0,0,5,0,4,0,8,4,0,0,
            0,0,11,8,0,4,7,7,0,0,
            2,0,0,15,2,4,0,0,2,15,
            0,1,0,0,0,8,0,0,2,7,
            0,22,9,0,4,0,1,10,5,0,
            0,0,0,0,5,4,0,23,5,0,
            1,0,6,0,0,13,0,4,0,0,
            9,7,0,5,0,0,10,0,0,0,
            1,31,13,0,12,7,3,13,0,0,
            13,0,0,0,0,0,4,4,23,4,
            9,0,0,15,0,1,0,6,0,20,
            0,9,6,0,1,0,1,9,0,0,
            0,27,12,5,4,0,1,0,0,0,
            0,4,4,14,0,6,0,1,0,5,
            0,0,12,0,4,7,0,4,0,1,
            4,0,0,2,13,0,1,0,1,0,
            1,0,0,0,1,0,5,0,1,17,
            8,6,0,0,0,3,0,3,5,3,
            0,0,0,0,1,0,0,7,7,0,
            0,2,2,0,12,2,0,0,2,0,
            1,4,0,1,0,0,0,0,0,1,
            4,0,0,6,0,1,30,12,33,0,
            16,0,1,0,1,0,0,0,3,18,
            18,4,0,0,15,0,4,4,0,0,
            0,0,7,0,18,4,6,4,10,0,
            1,12,0,0,1,3,0,0,0,0,
            3,0,1,0,8,7,0,1,0,0,
            1,3,0,1,0,0,1,0,19,0,
            0,0,0,9,5,5,0,10,7,7,
            0,5,0,3,0,3,2,0,0,0,
            0,0,0,0,7,5,0,6,9,7,
            0,0,14,0,1,0,6,2,7,0,
            0,0,1,0,0,22,6,0,0,10,
            0,7,2,10,0,7,9,3,0,0,
            0,3,0,0,5,0,0,1,8,6,
            0,6,0,1,0,0,2,0,1,0,
            0,0,3,2,0,15,0,7,0,0,
            0,16,0,5,10,6,0,1,0,0,
            10,9,3,0,0,19,0,4,0,1,
            0,7,2,15,0,0,10,0,0,2,
            5,0,0,9,0,0,5,5,0,11,
            0,0,2,2,10,0,1,0,0,14,
            0,0,5,0,6,0,0,2,0,0,
            0,10,24,0,11,2,8,0,0,2,
            20,11,16,0,15,2,0,0,10,0,
            0,0,1,0,7,5,0,11,0,1,
            0,12,2,0,11,9,0,0,5,2,
            0,0,0,3,0,0,0,11,2,0,
            0,10,3,3,10,0,1,0,16,0,
            0,16,0,0,2,6,0,0,5,0,
            10,0,6,16,0,8,0,1,0,5,
            0,12,2,0,0,0,8,0,0,2,
            0,6,0,1,0,1,0,7,0,0,
            16,13,4,32,21,9,0,0,1,0,
            0,2,6,0,0,0,0,0,3,2,
            0,0,0,13,11,3,0,7,29,13,
            9,0,1,0,0,0,0,4,0,25,
            5,3,16,0,8,0,1,0,5,0,
            0,4,0,3,0,3,0,1,0,0,
            11,3,0,9,0,1,0,1,6,0,
            0,1,0,0,15,2,0,8,0,1,
            0,9,0,1,0,0,0,11,3,0,
            1,0,0,0,3,15,0,1,0,0,
            2,0,18,4,0,19,0,1,0,5,
            0,1,11,0,0,2,0,3,0,0,
            28,0,4,0,0,9,0,1,0,21,
            0,0,9,14,10,0,0,9,0,1,
            9,6,21,0,0,2,2,17,0,0,
            14,0,0,1,6,6,0,0,2,2,
            0,0,0,3,0,14,0,1,0,8,
            0,1,10,0,0,11,3,0,0,5,
            12,3,0,6,0,3,0,3,2,0,
            0,1,0,0,0,1,0,0,0,7,
            11,8,0,0,0,3,3,3,11,0,
            0,1,0,4,0,17,0,21,0,0,
            0,0,0,0,0,0,0,0,0,0,
            0,0,0,0,0,0,0,0,0,0,
            0,0,0,0,0,0,0,0,0,0,
            0,0,0,0,0,0,0,0,0,0,
            0,0,0,0,0
        };
    };
    public final static byte termCheck[] = TermCheck.termCheck;
    public final int termCheck(int index) { return termCheck[index]; }

    public interface TermAction {
        public final static char termAction[] = {0,
            555,140,141,136,135,143,142,128,138,139,
            129,121,116,117,134,555,555,127,122,118,
            119,130,115,179,120,123,137,178,555,133,
            124,126,132,555,131,166,187,125,165,555,
            563,217,652,186,555,185,216,192,191,190,
            555,555,555,626,189,184,555,285,199,201,
            167,198,555,555,229,228,188,227,418,555,
            558,293,194,195,200,292,226,555,555,420,
            571,421,419,555,177,230,422,555,555,176,
            159,174,175,181,300,193,183,13,180,555,
            205,203,240,270,303,268,182,204,555,555,
            555,154,160,301,155,302,267,269,152,153,
            555,555,27,555,202,163,555,474,156,164,
            162,168,169,146,147,555,171,170,555,555,
            555,555,555,475,218,214,172,173,196,555,
            476,225,215,555,219,555,555,316,255,220,
            555,197,256,233,234,555,562,315,224,555,
            555,580,555,555,555,263,258,82,260,257,
            262,261,555,555,336,472,329,86,555,483,
            328,555,144,555,471,535,145,556,555,482,
            555,149,148,555,534,555,555,158,555,151,
            150,554,555,161,555,555,207,157,555,555,
            555,209,555,206,555,555,208,627,210,555,
            211,555,221,213,222,555,212,555,555,223,
            555,555,231,555,235,555,232,237,555,555,
            555,555,236,238,555,242,239,241,555,555,
            243,555,555,572,567,245,555,555,565,634,
            555,247,555,555,555,246,555,555,611,564,
            555,244,248,555,250,555,581,252,251,555,
            555,555,555,555,253,254,555,249,259,555,
            265,555,271,555,555,266,555,274,555,555,
            272,273,555,646,555,555,275,76,555,555,
            281,264,276,555,277,279,656,278,555,555,
            282,555,555,555,555,555,286,287,280,599,
            284,555,555,283,555,290,555,288,555,636,
            555,289,291,555,296,555,659,294,555,555,
            555,309,295,297,299,555,304,555,555,555,
            555,305,306,298,555,307,555,633,555,310,
            555,555,308,555,606,311,555,312,555,314,
            313,555,555,317,632,555,318,555,319,555,
            321,555,555,555,560,54,322,555,569,320,
            323,324,555,555,555,325,555,559,657,326,
            555,555,555,555,332,555,555,327,330,555,
            555,557,333,555,331,334,555,555,335,555,
            338,339,555,340,555,555,555,555,555,344,
            342,555,555,343,555,647,417,341,337,555,
            623,555,610,555,601,555,555,555,349,345,
            346,347,555,555,348,555,350,351,555,555,
            555,555,353,555,352,367,356,369,354,555,
            357,355,555,555,359,358,555,555,555,555,
            361,555,363,555,360,362,555,364,555,555,
            366,365,555,570,555,555,370,555,403,555,
            555,555,555,368,372,373,555,371,374,376,
            555,375,555,377,555,378,379,555,555,555,
            555,555,555,555,380,381,555,382,383,384,
            555,555,561,555,386,555,385,625,387,555,
            555,555,390,555,555,392,389,555,555,388,
            555,393,396,391,555,624,394,395,555,555,
            555,397,555,555,618,555,555,401,398,399,
            555,400,555,402,555,555,655,555,621,555,
            555,555,620,584,555,405,555,404,555,555,
            555,602,555,408,406,409,555,410,555,555,
            411,412,413,555,555,407,555,577,555,415,
            555,414,612,596,555,555,416,555,555,635,
            650,555,555,423,555,555,424,578,555,425,
            555,555,429,614,426,555,430,555,555,427,
            555,555,431,555,432,555,555,434,555,555,
            555,654,428,555,433,436,435,555,555,437,
            658,440,439,555,622,441,555,555,438,555,
            555,555,442,555,597,648,555,604,555,587,
            555,443,444,555,603,445,555,555,446,448,
            555,555,555,600,555,555,555,447,660,555,
            555,449,452,453,450,555,454,555,615,555,
            555,451,555,555,458,456,555,555,651,555,
            457,555,459,455,555,460,555,576,555,566,
            555,461,463,28,555,555,462,555,555,467,
            555,466,555,469,555,470,555,473,555,555,
            630,468,477,464,465,478,555,555,573,555,
            555,661,480,555,555,555,555,555,486,653,
            555,555,555,481,484,488,555,489,479,485,
            487,555,579,555,555,555,555,490,555,591,
            619,491,586,555,492,555,645,555,617,555,
            555,616,555,639,555,494,555,496,555,555,
            493,497,555,495,555,498,555,501,499,555,
            555,590,555,555,500,503,555,502,555,505,
            555,504,555,506,555,555,555,507,508,555,
            662,555,555,555,629,649,555,512,555,555,
            513,555,509,514,555,510,555,663,555,515,
            555,605,607,555,555,575,555,574,555,555,
            511,555,517,555,555,516,555,523,555,519,
            83,555,522,518,521,555,555,524,555,527,
            551,526,520,555,555,528,529,525,555,555,
            589,555,555,532,530,531,555,555,533,536,
            555,555,555,640,555,588,555,539,555,537,
            555,594,538,555,555,593,540,555,555,595,
            543,643,555,541,555,542,555,628,544,555,
            555,545,555,555,555,547,109,555,87,548,
            585,546,555,555,555,613,552,644,592,555,
            555,665,555,598,555,550,555,549
        };
    };
    public final static char termAction[] = TermAction.termAction;
    public final int termAction(int index) { return termAction[index]; }
    public final int asb(int index) { return 0; }
    public final int asr(int index) { return 0; }
    public final int nasb(int index) { return 0; }
    public final int nasr(int index) { return 0; }
    public final int terminalIndex(int index) { return 0; }
    public final int nonterminalIndex(int index) { return 0; }
    public final int scopePrefix(int index) { return 0;}
    public final int scopeSuffix(int index) { return 0;}
    public final int scopeLhs(int index) { return 0;}
    public final int scopeLa(int index) { return 0;}
    public final int scopeStateSet(int index) { return 0;}
    public final int scopeRhs(int index) { return 0;}
    public final int scopeState(int index) { return 0;}
    public final int inSymb(int index) { return 0;}
    public final String name(int index) { return null; }
    public final int getErrorSymbol() { return 0; }
    public final int getScopeUbound() { return 0; }
    public final int getScopeSize() { return 0; }
    public final int getMaxNameLength() { return 0; }

    public final static int
           NUM_STATES        = 440,
           NT_OFFSET         = 54,
           LA_STATE_OFFSET   = 665,
           MAX_LA            = 1,
           NUM_RULES         = 110,
           NUM_NONTERMINALS  = 3,
           NUM_SYMBOLS       = 57,
           SEGMENT_SIZE      = 8192,
           START_STATE       = 111,
           IDENTIFIER_SYMBOL = 0,
           EOFT_SYMBOL       = 39,
           EOLT_SYMBOL       = 55,
           ACCEPT_ACTION     = 554,
           ERROR_ACTION      = 555;

    public final static boolean BACKTRACK = false;

    public final int getNumStates() { return NUM_STATES; }
    public final int getNtOffset() { return NT_OFFSET; }
    public final int getLaStateOffset() { return LA_STATE_OFFSET; }
    public final int getMaxLa() { return MAX_LA; }
    public final int getNumRules() { return NUM_RULES; }
    public final int getNumNonterminals() { return NUM_NONTERMINALS; }
    public final int getNumSymbols() { return NUM_SYMBOLS; }
    public final int getSegmentSize() { return SEGMENT_SIZE; }
    public final int getStartState() { return START_STATE; }
    public final int getStartSymbol() { return lhs[0]; }
    public final int getIdentifierSymbol() { return IDENTIFIER_SYMBOL; }
    public final int getEoftSymbol() { return EOFT_SYMBOL; }
    public final int getEoltSymbol() { return EOLT_SYMBOL; }
    public final int getAcceptAction() { return ACCEPT_ACTION; }
    public final int getErrorAction() { return ERROR_ACTION; }
    public final boolean isValidForParser() { return isValidForParser; }
    public final boolean getBacktrack() { return BACKTRACK; }

    public final int originalState(int state) { return 0; }
    public final int asi(int state) { return 0; }
    public final int nasi(int state) { return 0; }
    public final int inSymbol(int state) { return 0; }

    public final int ntAction(int state, int sym) {
        return baseAction[state + sym];
    }

    public final int tAction(int state, int sym) {
        int i = baseAction[state],
            k = i + sym;
        return termAction[termCheck[k] == sym ? k : i];
    }
    public final int lookAhead(int la_state, int sym) {
        int k = la_state + sym;
        return termAction[termCheck[k] == sym ? k : la_state];
    }
}
