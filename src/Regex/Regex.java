package Regex;

/**
 * 
 * @author Andrew Ash
 */
public class Regex {

	private DFA dfa;
	
    public Regex(String str) {
    	NFA nfa = NFA.fromString(str);
    	dfa = DFA.fromNFA(nfa);
    }
    
    public boolean matches(String s) {
    	return dfa.matches(s);
    }
    
}
