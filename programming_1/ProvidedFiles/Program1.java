/*
 * Name: <your name>
 * EID: <your EID>
 */

import java.util.ArrayList;
import java.util.LinkedList;

/**
 * Your solution goes in this class.
 * 
 * Please do not modify the other files we have provided for you, as we will use
 * our own versions of those files when grading your project. You are
 * responsible for ensuring that your solution works with the original version
 * of all the other files we have provided for you.
 * 
 * That said, please feel free to add additional files and classes to your
 * solution, as you see fit. We will use ALL of your additional files when
 * grading your solution.
 */
public class Program1 extends AbstractProgram1 {
    /**
     * Determines whether a candidate Matching represents a solution to the
     * Stable Marriage problem. Study the description of a Matching in the
     * project documentation to help you with this.
     */
    public boolean isStableMatching(Matching allocation) {
        /* TODO implement this function */
    	int m = allocation.getServerCount();
    	int n = allocation.getUserCount();
    	ArrayList<ArrayList<Integer>> server_preference = allocation.getServerPreference();
    	ArrayList<ArrayList<Integer>> user_preference = allocation.getUserPreference();
    	ArrayList<Integer> servers_slots = allocation.getServerSlots();
    	ArrayList<Integer> user_matching = allocation.getUserMatching();
    	// Calculate server usage of matching result
    	ArrayList<Integer> matching_server_slots = new ArrayList<Integer>();
    	for(int i = 0; i < m; ++i) {
    		matching_server_slots.add(0);
    	}
    	for(int i = 0; i < n; ++i) {
    		int server = user_matching.get(i);
    		if(server != -1) {
    			matching_server_slots.set(server, matching_server_slots.get(server) + 1);
    		}
    	}
    	// Make sure all slots are filled and not exceed
    	for(int i = 0; i < m; ++i) {
    		if(matching_server_slots.get(i) != servers_slots.get(i)) {
    			return false;
    		}
    	}
    	
    	// Let user1 = u, user2 = u'
    	// Check first instability
    	for(int user1 = 0; user1 < n; user1++) {
    		int user1_matchserver = user_matching.get(user1);
    		if(user1_matchserver != -1) {
    			ArrayList<Integer> user1server_prefer = server_preference.get(user1_matchserver);
    			for(int i = 0; i < user1server_prefer.indexOf(user1); ++i) {
    				int user2 = user1server_prefer.get(i);
    				if(user_matching.get(user2) == -1) {
    					return false;
    				}
    			}
    		}
    	}
    	
    	// Check second instability
    	for(int user1 = 0; user1 < n; user1++) {
    		for(int user2 = 0; user2 < n; user2++) {
    			int user1_matchserver = user_matching.get(user1);
    			int user2_matchserver = user_matching.get(user2);
    			if (user1_matchserver != -1 && user2_matchserver != -1) {
    				ArrayList<Integer> user1_preferserver = server_preference.get(user1_matchserver);
    				ArrayList<Integer> user2_preferserver = user_preference.get(user2);
    				if(user1_preferserver.indexOf(user2) < user1_preferserver.indexOf(user1) && user2_preferserver.indexOf(user1_matchserver) < user2_preferserver.indexOf(user2_matchserver)) {
    					return false;
    				}
    			}
    		}
    	}
        return true;
    }

    /**
     * Determines a solution to the Stable Marriage problem from the given input
     * set. Study the project description to understand the variables which
     * represent the input to your solution.
     * 
     * @return A stable Matching.
     */
    public Matching stableMarriageGaleShapley(Matching allocation) {
        /* TODO implement this function */
    	int m = allocation.getServerCount();
    	int n = allocation.getUserCount();
    	int slots = allocation.totalServerSlots();
    	
    	ArrayList<ArrayList<Integer>> server_preference = allocation.getServerPreference();
    	ArrayList<ArrayList<Integer>> user_preference = allocation.getUserPreference();
    	ArrayList<Integer> server_slots_copy = new ArrayList<Integer>();
    	for(int num : allocation.getServerSlots()) {
    		server_slots_copy.add(num);
    	}
    	// server_preferencenumber: save the user that the server is going to offer a slot
    	ArrayList<Integer> server_preferencenumber = new ArrayList<Integer>();
    	ArrayList<Integer> user_matching = new ArrayList<Integer>();
    	
    	for(int i = 0; i < n; ++i) {
    		user_matching.add(-1);
    	}
    	
    	for(int i = 0; i < m; ++i) {
    		server_preferencenumber.add(0);
    	}
    	
    	while(slots > 0) {
    		for(int server = 0; server < m; ++server) {
    			// Get the remaining slots
    			int place = server_slots_copy.get(server);
    			// Remaining slots exist
    			if(place > 0) {
    				// 
    				int prefernumber = server_preferencenumber.get(server);
    				int user = server_preference.get(server).get(prefernumber);
    				int user_matchserver = user_matching.get(user);
    				// no match
    				if(user_matchserver == -1) {
    					// 
    					user_matching.set(user, server);
    					
    					server_slots_copy.set(server, place - 1);
    					slots -= 1;
    					
    					server_preferencenumber.set(server, prefernumber + 1);
    				}
    				// already assigned
    				else {
    					// user prefer current server
    					if(user_preference.get(user).indexOf(user_matchserver) < user_preference.get(user).indexOf(server)) {
    						
    						server_preferencenumber.set(server, prefernumber + 1);
    					}
    					// user prefer new server
    					else {
    						user_matching.set(user, server);
    						server_preferencenumber.set(server, prefernumber + 1);
    						
    						server_slots_copy.set(server, place - 1);
    						server_slots_copy.set(user_matchserver, server_slots_copy.get(user_matchserver) + 1);
    					}
    				}
    			}
    		}
			
    	}
    	
    	allocation.setUserMatching(user_matching);
        return allocation;
    }
}
