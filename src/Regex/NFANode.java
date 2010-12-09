package Regex;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;


/**
 * Represents a node in an NFA.  This means it can have multiple next states
 * from a given character 
 * 
 * @author Andrew Ash
 */
public class NFANode {
	
	private HashMap<Character, Set<NFANode>> transitions = new HashMap<Character, Set<NFANode>>();
	private Set<NFANode> eps_transitions = new HashSet<NFANode>();
	private boolean accepting = false;

	/**
	 * Transitions can go from a character to a number of places, or to nowhere
	 * 
	 * Doesn't check for multiple transitions to the same node
	 * 
	 * @param c If c is null, this adds an epsilon transition
	 * @param next
	 */
	public void addTransition(Character c, NFANode next) {
		
		// handle epsilon
		if(c == null) {
			addEpsilonTransition(next);
			return;
		}
		
		// handle regular
		Collection<NFANode> existing = transitions.get(c);

		if(existing != null)
			existing.add(next);
		else {
			Set<NFANode> nodes = new HashSet<NFANode>();
			nodes.add(next);
			transitions.put(c, nodes);
		}
	}
	
	/**
	 * Doesn't check for multiple transitions to the same node
	 * 
	 * @param next
	 */
	public void addEpsilonTransition(NFANode next) {
		eps_transitions.add(next);
	}

	/**
	 * @param c If null, interpreted as requesting epsilon transitions
	 * @return Set of nodes this character made the NFA go to, or null if none.
	 */
	public Set<NFANode> transition(Character c) {
		if(c == null)
			return eps_transitions;
		
		return transitions.get(c);
	}

	
	/**
	 * 
	 * @return Set of states reachable by zero or more epsilon transitions
	 */
	public Set<NFANode> epsilonClosure() {
		Set<NFANode> set = new HashSet<NFANode>();
		
		// this node
		set.add(this);
		
		// the epsilon closures of all nodes an eps trans away
		for(NFANode n : eps_transitions)
			set.addAll(n.epsilonClosure());
		
		return set;
	}
	
	/**
	 * Epsilon closure of a node set (union of the epsilon closures of the elements in that set)
	 * 
	 * @param start
	 * @return The epsilon closure of a set of nodes
	 */
	public static Set<NFANode> epsilonClosure(Set<NFANode> start) {
		Set<NFANode> set = new HashSet<NFANode>();
		
		for(NFANode n : start)
			set.addAll(n.epsilonClosure());
		
		return set;
	}
	
	/**
	 * 
	 * @param start
	 * @param c
	 * @return Transition out of start set with given char
	 */
	public static Set<NFANode> epsilonClosureTransition(Set<NFANode> start, Character c) {
		if(c == null)
			return epsilonClosure(start);
		
		Set<NFANode> set = new HashSet<NFANode>();
		for(NFANode n : start)
			set.addAll(epsilonClosure(n.transition(c)));
		
		return set;
	}
	
	public boolean isAccepting() {
		return accepting;
	}

	public void setAccepting(boolean accepting) {
		this.accepting = accepting;
	}
	
	public Set<Character> getTransitionCharacters() {
		return transitions.keySet();
	}
}