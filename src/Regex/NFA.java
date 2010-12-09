package Regex;
import java.util.ArrayList;


/**
 * 
 * @author Andrew Ash
 */
public class NFA {
	
	private NFANode initial;
	private ArrayList<NFANode> nodes = new ArrayList<NFANode>();
	
	// prevents outside creation of NFA objects
	private NFA() {	
	}
	
	/**
	 * Use Thompson's construction to convert a regex to an NFA
	 * @param s
	 * @return NFA that represents s
	 */
	public static NFA fromString(String s) {
		// TODO
		return null;
	}
	
	
	/////////// NFA Primitives ////////////////
	
	// These primitives are based off p64 of Louden's Compiler Construction book 

	/**
	 * An NFA that matches the given character or epsilon if c is null
	 * 
	 * @param c If null, creates an NFA that matches an epsilon transition (always match)
	 */
	public static NFA basicMatch(Character c) {
		NFA nfa = new NFA();
		
		NFANode n1 = new NFANode();
		NFANode n2 = new NFANode();
		
		n1.addTransition(c, n2);
		n2.setAccepting(true);
		
		nfa.initial = n1;
		
		nfa.nodes.add(n1);
		nfa.nodes.add(n2);
		
		return nfa;
	}
	
	/**
	 * Concatenates NFAs r and s
	 * 
	 * As a side effect, this causes all the NFANodes that were accepting before
	 * to become non-accepting and instead have epsilon transitions to the start
	 * state of s.
	 * 
	 * @param r
	 * @param s
	 * @return A new NFA that is the concatenation of those two
	 */
	public static NFA concatenation(NFA r, NFA s) {
		NFA nfa = new NFA();
		
		nfa.nodes.addAll(r.nodes);
		nfa.nodes.addAll(s.nodes);
		
		// add eps-trans from a's accept to b's initial and remove a's accept flags
		for(NFANode node : r.nodes) {
			if(node.isAccepting()) {
				node.addEpsilonTransition(s.initial);
				node.setAccepting(false);
			}
		}
		
		nfa.initial = r.initial;
		
		return nfa;
	}
	
	/**
	 * Alternates NFAs r and s
	 * 
	 * As a side effect, changes all the NFANodes inside each NFA r and s
	 * to be no longer accepting and instead have epsilon transitions out to
	 * a resulting accept state
	 * 
	 * @param r
	 * @param s
	 * @return An NFA that matches either r or s
	 */
	public static NFA alternation(NFA r, NFA s) {
		NFA nfa = new NFA();
		
		nfa.nodes.addAll(r.nodes);
		nfa.nodes.addAll(s.nodes);
		
		// create start node and set eps trans to beginning of a and b
		NFANode start = new NFANode();
		nfa.nodes.add(start);
		nfa.initial = start;
		start.addEpsilonTransition(r.initial);
		start.addEpsilonTransition(s.initial);
		
		// set end node as accepting
		NFANode end = new NFANode();
		nfa.nodes.add(end);
		end.setAccepting(true);
		
		// set all accepting nodes in A and B as non-accepting and
		// add eps-trans out to end node
		for(NFANode n : r.nodes) {
			if(n.isAccepting()) {
				n.addEpsilonTransition(end);
				n.setAccepting(false);
			}
		}
		for(NFANode n : s.nodes) {
			if(n.isAccepting()) {
				n.addEpsilonTransition(end);
				n.setAccepting(false);
			}
		}
		
		return nfa;
	}
	
	
	/**
	 * Repetition (star operator, i.e. Kleene closure) of NFA r
	 * 
	 * @param r
	 * @return An NFA that matches r* (0 or more of r)
	 */
	public static NFA repetition(NFA r) {
		NFA nfa = new NFA();
		
		nfa.nodes.addAll(r.nodes);
		
		// create start and end nodes
		NFANode start = new NFANode();
		NFANode end = new NFANode();
		
		nfa.nodes.add(start);
		nfa.nodes.add(end);
		
		// hook up epsilon transitions out of start
		start.addEpsilonTransition(r.initial);
		start.addEpsilonTransition(end);
		
		// hook up epsilon transition out of r's accept states and make them non-accepting
		for(NFANode n : r.nodes) {
			if(n.isAccepting()) {
				n.addEpsilonTransition(r.initial);
				n.addEpsilonTransition(end);
				n.setAccepting(false);
			}
		}
		
		end.setAccepting(true);
		
		return nfa;
	}
	
	////////// getters  /////////////
	
	public NFANode getInitial() {
		return this.initial;
	}

	public ArrayList<NFANode> getNodes() {
		return this.nodes;
	}
}
