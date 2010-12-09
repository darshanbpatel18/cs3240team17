package Regex;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;
import java.util.Stack;


/**
 * 
 * @author Andrew Ash
 */
public class DFA {
	
	private DFANode initial;
	private ArrayList<DFANode> nodes = new ArrayList<DFANode>();


	// prevents outside creation of DFA objects
	private DFA() {
	}
	
	
	/**
	 * Use subset construction (each DFA node represents a set of states from an NFA node)
	 * 
	 * @param nfa
	 * @return DFA created from the given NFA
	 */
	public static DFA fromNFA(NFA nfa) {
		
		DFA dfa = new DFA();
		
		// set initial state based on the eps-closure of the nfa's initial state
		dfa.initial = new DFANode(nfa.getInitial().epsilonClosure());
		dfa.nodes.add(dfa.initial);
		
		///// DFS through the to-be-created DFA
		
		Stack<DFANode> stack = new Stack<DFANode>();
		stack.push(dfa.initial);
		
		// Used to merge identical states (e.g. a->c and b->c both point to same c object)
		//   kind of like a memo table but used to prevent duplicates instead
		HashMap<Set<NFANode>, DFANode> map = new HashMap<Set<NFANode>, DFANode>();
		
		while(!stack.isEmpty()) {
			DFANode dfan = stack.pop();
			
			///// add all transitions out of the DFANode's set of NFANodes as transitions out of the DFANode
			
			// get set of characters that need to be out of dfan
			Set<Character> outTrans = new HashSet<Character>();
			for(NFANode nfan : dfan.getRepresentingNFANodes())
				outTrans.addAll(nfan.getTransitionCharacters());
			
			
			// for each character
			for(Character c : outTrans) {
				// calculate the epsilon closure on its resulting nodeset
				Set<NFANode> goesTo = NFANode.epsilonClosureTransition(dfan.getRepresentingNFANodes(), c);
				
				// and add a transition from that DFANode to this newly-created one, checking
				// if it's in the memo table first
				if(goesTo.size() > 0) {
					if(map.containsKey(goesTo))
						dfan.addTransition(c, map.get(goesTo));
					else {
						// if it hasn't been created before, add this node to the stack and explore
						DFANode out = new DFANode(goesTo);
						
						// make this DFANode accepting if any of the NFANodes it represents are accepting
						for(NFANode n : goesTo)
							if(n.isAccepting()) {
								out.setAccepting(true);
								break;
							}
						
						map.put(goesTo, out);
						stack.push(out);
						dfa.nodes.add(out);
					}
						
				}
			}
		}
		
		return dfa;
	}
	
	/**
	 * See if the string matches this DFA
	 * 
	 * @param s
	 * @return True if the DFA accepts the string, false otherwise
	 */
	public boolean matches(String s) {
		char[] chars = s.toCharArray();
		
		DFANode current = this.initial;
		
		for(Character c : chars) {
			current = current.transition(c);
			
			// transitions that don't go to a state are bad
			if(current == null)
				return false;
		}
		
		return current.isAccepting();
	}

	///////// getters /////////////
	
	public DFANode getInitial() {
		return this.initial;
	}

	public ArrayList<DFANode> getNodes() {
		return this.nodes;
	}
}
