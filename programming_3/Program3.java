
//Name: (Ting Pan)
//EID: (tp24269)


public class Program3 {

    DamageCalculator calculator;
    PlanetPathScenario planetScenario;

    public Program3() {
        this.calculator = null;
        this.planetScenario = null;
    }

    /*
     * This method is used in lieu of a required constructor signature to initialize
     * your Program3. After calling a default (no-parameter) constructor, we
     * will use this method to initialize your Program3 for Part 2.
     */
    public void initialize(PlanetPathScenario ps) {
        this.planetScenario = ps;
    }

    /*
     * This method is used in lieu of a required constructor signature to initialize
     * your Program3. After calling a default (no-parameter) constructor, we
     * will use this method to initialize your Program3 for Part 1.
     */
    public void initialize(DamageCalculator dc) {
        this.calculator = dc;
    }


    /*
     * This method returns an integer that is the minimum amount of time necessary to travel
     * from the start planet to the end planet in the PlanetPathScenario given the total
     * amout of fuel that Thanos has. If a path is not possible given the amount of fuel, return -1.
     */
     //TODO: Complete this method
     public void dfs(int start, int [][]dp, int [][]visited,int fuel){
    	 if(visited[start][fuel]==1) {
    		 return;
    	 }
    	 SpaceFlight[] des = planetScenario.getFlightsFromPlanet(start);
    	 for(int i = 0; i < des.length; ++i) {
    		 int fuelneed = des[i].getFuel();
    		 if (fuel - fuelneed >= 0) {
    			 dfs(des[i].getDestination(), dp, visited, fuel - fuelneed);
    			 if(dp[des[i].getDestination()][fuel - fuelneed] != Integer.MAX_VALUE){
    				 dp[start][fuel] = Math.min(dp[start][fuel], dp[des[i].getDestination()][fuel-fuelneed] + des[i].getTime());
    			 }
    		 }
    	 }
    	 visited[start][fuel] = 1;
     }
     
     public int computeMinimumTime() {
    	 int start = planetScenario.getStartPlanet();
    	 int end = planetScenario.getEndPlanet();
         int totalFuel = planetScenario.getTotalFuel();
         int num = planetScenario.getNumPlanets();
         
         //dp[i][j] means shortest time from i to end with j fuel
         int [][] dp = new int[num][totalFuel + 1];
         int [][] visited = new int[num][totalFuel + 1];
         
         for(int i = 0; i < num; ++i) {
             for(int j = 0; j < totalFuel + 1; ++j){
                 if(i == end){
                     dp[i][j] = 0;
                     visited[i][j] = 1;
                 }
                 else{
                     dp[i][j] = Integer.MAX_VALUE;
                     visited[i][j] = 0;
                 }
             }
         }
         
         for(int i = 1; i < totalFuel + 1; ++i) {
                 dfs(start, dp,visited, i);
         }
         
         return dp[start][totalFuel] == Integer.MAX_VALUE ? -1 : dp[start][totalFuel];
         
     }

    /*
     * This method returns an integer that is the maximum possible damage that can be dealt
     * given a certain amount of time.
     */
    //TODO: Complete this function
    public int computeDamage() {

    	int totalTime = calculator.getTotalTime();
    	int numAttacks = calculator.getNumAttacks();
    	int [][] dp = new int[numAttacks + 1][totalTime + 1];
    	
    	for(int i = 0; i < totalTime + 1; ++i) {
    		dp[0][i] = 0; 
    	}
    	
    	for(int i = 1; i < numAttacks + 1; ++i) {
    		dp[i][0] = dp[i-1][0] + calculator.calculateDamage(i-1, 0);
    	}

    	for(int attack = 1; attack <= numAttacks; ++attack) {
    		for(int time = 1; time <= totalTime; ++time) {
    			//not use this attack
    			dp[attack][time] = dp[attack-1][time];
    			//use this attack
    			for(int spendtime = 1; spendtime <= time; ++ spendtime) {
    				dp[attack][time] = Math.max(dp[attack-1][time-spendtime] + calculator.calculateDamage(attack - 1, spendtime), dp[attack][time]);
    			}
    		}
    	}

    	return dp[numAttacks][totalTime];

    }

}


