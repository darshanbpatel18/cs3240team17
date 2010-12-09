package Regex;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;


/**
 * Represents a node in a DFA.  Also can represent multiple NFANodes for
 * use in subset construction 
 * 
 * @author Andrew Ash
 *
 */
public class DFANode {

	private HashMap<Character, DFANode> transitions = new HashMap<Character, DFANode>();
	private boolean accepting = false;
	
	/** 
	 * The set of NFA states that this DFA state represents,
	 * used in subset construction from NFA to DFA
	 */
	private Set<NFANode> represents = new HashSet<NFANode>();
	
	
	public DFANode(Set<NFANode> represents) {
		this.represents = represents;
	}
	
	
	/**
	 * @param c If character c already transitions out, that transition is replaced by this one 
	 * @param next
	 */
	public void addTransition(Character c, DFANode next) {
		transitions.put(c,next);
	}
	
	/**
	 * @param c
	 * @return DFANode that c made this DFA transition to, or null if there is no outward transition for this character
	 */
	public DFANode transition(Character c) {
		return transitions.get(c);
	}
	
	
	public boolean isAccepting() {
		return this.accepting;
	}
	public void setAccepting(boolean accepting) {
		this.accepting = accepting;
	}
	public Set<NFANode> getRepresentingNFANodes() {
		return represents;
	}
}
