-- In a parser using this template, the following macro may be redefined:
--
--     $additional_interfaces
--     $ast_class
--
--
-- B E G I N N I N G   O F   T E M P L A T E   dtParserTemplateE
--
%Options programming_language=java,margin=8
%Options table,error_maps,scopes
%options prefix=TK_,
%options action=("*.java", "/.", "./")
%options headers=("*.java", "/:", ":/")
%options ParseTable=lpg.lpgjavaruntime.ParseTable

--
-- This template requires that the name of the EOF token be set
-- to EOF_TOKEN to be consistent with LexerTemplateD and LexerTemplateE
--
$EOF
    EOF_TOKEN
$End

$ERROR
    ERROR_TOKEN
$End

$Define
        $DefaultAllocation
        /:
                ruleAction[$rule_number] = new act$rule_number$();:/

        $NoAllocation
        /:
                ruleAction[$rule_number] = new NoAction();:/

        $NullAllocation
        /:
                ruleAction[$rule_number] = new NullAction();:/

        $BadAllocation
        /:
                ruleAction[$rule_number] = new BadAction();:/

        $Header
        /.
            //
            // Rule $rule_number:  $rule_text
            //./

        $DefaultAction
        /.$DefaultAllocation $Header
            final class act$rule_number extends Action./

        --
        -- This macro is used to initialize the ruleAction array
        -- to the null_action function.
        --
        $NullAction
        /. $NullAllocation $Header
            //
            // final class NullAction extends Action
            //
        ./

        --
        -- This macro is used to initialize the ruleAction array
        -- to the no_action function.
        --
        $NoAction
        /. $NoAllocation $Header
            //
            // final class NullAction extends Action
            //
        ./

        --
        -- This macro is used to initialize the ruleAction array
        -- to the bad_action function.
        --
        $BadAction
        /. $BadAllocation $Header
            //
            // final class NullAction extends Action
            //
        ./

        --
        -- This is the header for a ruleAction class
        --
        $BeginAction
        /.$DefaultAllocation $Header
            final class act$rule_number extends Action
            {
                public void action()
                {./

        $EndAction
        /.          return;
                }
            }./

        $BeginJava
        /.$BeginAction
                    $symbol_declarations./

        $EndJava /.$EndAction./

        $SplitActions /../

        --
        -- Macros that may be needed in a parser using this template
        --
        $additional_interfaces /../
        $ast_class /.$ast_type./

        $setSym1 /. // macro setSym1 is deprecated. Use function setResult
                    getParser().setSym1./
        $setResult /. // macro setResult is deprecated. Use function setResult
                     getParser().setSym1./
        $getSym /. // macro getSym is deprecated. Use function getRhsSym
                  getParser().getSym./
        $getToken /. // macro getToken is deprecated. Use function getRhsTokenIndex
                    getParser().getToken./
        $getIToken /. // macro getIToken is deprecated. Use function getRhsIToken
                     super.getIToken./
        $getLeftSpan /. // macro getLeftSpan is deprecated. Use function getLeftSpan
                       getParser().getFirstToken./
        $getRightSpan /. // macro getRightSpan is deprecated. Use function getRightSpan
                        getParser().getLastToken./
$End

$Globals
        /.import lpg.lpgjavaruntime.*;
        ./
$End

$Headers
    /.
        public class $action_type extends PrsStream implements RuleAction$additional_interfaces
        {
            private static ParseTable prs = new $prs_type();
            private DeterministicParser dtParser;

            public DeterministicParser getParser() { return dtParser; }
            private void setResult(Object object) { dtParser.setSym1(object); }
            public Object getRhsSym(int i) { return dtParser.getSym(i); }

            public int getRhsTokenIndex(int i) { return dtParser.getToken(i); }
            public IToken getRhsIToken(int i) { return super.getIToken(getRhsTokenIndex(i)); }
        
            public int getRhsFirstTokenIndex(int i) { return dtParser.getFirstToken(i); }
            public IToken getRhsFirstIToken(int i) { return super.getIToken(getRhsFirstTokenIndex(i)); }

            public int getRhsLastTokenIndex(int i) { return dtParser.getLastToken(i); }
            public IToken getRhsLastIToken(int i) { return super.getIToken(getRhsLastTokenIndex(i)); }

            public int getLeftSpan() { return dtParser.getFirstToken(); }
            public IToken getLeftIToken()  { return super.getIToken(getLeftSpan()); }

            public int getRightSpan() { return dtParser.getLastToken(); }
            public IToken getRightIToken() { return super.getIToken(getRightSpan()); }

            public int getRhsErrorTokenIndex(int i)
            {
                int index = dtParser.getToken(i);
                IToken err = super.getIToken(index);
                return (err instanceof ErrorToken ? index : 0);
            }
            public ErrorToken getRhsErrorIToken(int i)
            {
                int index = dtParser.getToken(i);
                IToken err = super.getIToken(index);
                return (ErrorToken) (err instanceof ErrorToken ? err : null);
            }

            public $action_type(LexStream lexStream)
            {
                super(lexStream);

                try
                {
                    super.remapTerminalSymbols(orderedTerminalSymbols(), $prs_type.EOFT_SYMBOL);
                }
                catch(NullExportedSymbolsException e) {
                }
                catch(NullTerminalSymbolsException e) {
                }
                catch(UnimplementedTerminalsException e)
                {
                    java.util.ArrayList unimplemented_symbols = e.getSymbols();
                    System.out.println("The Lexer will not scan the following token(s):");
                    for (int i = 0; i < unimplemented_symbols.size(); i++)
                    {
                        Integer id = (Integer) unimplemented_symbols.get(i);
                        System.out.println("    " + $sym_type.orderedTerminalSymbols[id.intValue()]);               
                    }
                    System.out.println();                        
                }
                catch(UndefinedEofSymbolException e)
                {
                    throw new Error(new UndefinedEofSymbolException
                                        ("The Lexer does not implement the Eof symbol " +
                                         $sym_type.orderedTerminalSymbols[$prs_type.EOFT_SYMBOL]));
                } 
            }

            public String[] orderedTerminalSymbols() { return $sym_type.orderedTerminalSymbols; }
            public String getTokenKindName(int kind) { return $sym_type.orderedTerminalSymbols[kind]; }            
            public int getEOFTokenKind() { return $prs_type.EOFT_SYMBOL; }
            public PrsStream getParseStream() { return (PrsStream) this; }

            public $ast_class parser()
            {
                return parser(null, 0);
            }
            
            public $ast_class parser(Monitor monitor)
            {
                return parser(monitor, 0);
            }
            
            public $ast_class parser(int error_repair_count)
            {
                return parser(null, error_repair_count);
            }
            
            public $ast_class parser(Monitor monitor, int error_repair_count)
            {
                try
                {
                    dtParser = new DeterministicParser(monitor, (TokenStream) this, prs, (RuleAction) this);
                }
                catch (NotDeterministicParseTableException e)
                {
                    throw new Error(new NotDeterministicParseTableException
                                        ("Regenerate $prs_type.java with -NOBACKTRACK option"));
                }
                catch (BadParseSymFileException e)
                {
                    throw new Error(new BadParseSymFileException("Bad Parser Symbol File -- $sym_type.java. Regenerate $prs_type.java"));
                }

                try
                {
                    return ($ast_class) dtParser.parse();
                }
                catch (BadParseException e)
                {
                    reset(e.error_token); // point to error token

                    DiagnoseParser diagnoseParser = new DiagnoseParser((TokenStream) this, prs);
                    diagnoseParser.diagnose(e.error_token);
                }

                return null;
            }

            public class BadActionException extends Exception
            {
                private static final long serialVersionUID = 1L;
            }

            public final void ruleAction(int rule_number)
            {
                try
                {
                    ruleAction[rule_number].action();
                }
                catch (Error e)
                {
                    if (e.getCause() instanceof BadActionException)
                        throw new Error("No action specified for rule " + rule_number);
                }
            }

            abstract class Action
            {
                public abstract void action();
            }

            final class NoAction extends Action
            {
                public void action() {}
            }

            final class BadAction extends Action
            {
                public void action()
                {
                    throw new Error(new BadActionException());
                }
            }

            //
            // Action for a null rule
            //
            final class NullAction extends Action
            {
                public void action() { dtParser.setSym1(null); }
            }
    ./

    /:

            //
            // Declare and initialize ruleAction array.
            //
            Action ruleAction[] = new Action[$NUM_RULES + 1];
            {
                ruleAction[0] = null;
    :/
$End

$Trailers
    /.
        }
    ./

    /:


                //
                // Make sure that all elements of ruleAction are properly initialized
                //
                for (int i = 0; i < ruleAction.length; i++)
                {
                    if (ruleAction[i] == null)
                         ruleAction[i] = new NoAction();
                }
            };
    :/
$End

--
-- E N D   O F   T E M P L A T E
--
