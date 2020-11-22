package contest;

import java.util.concurrent.Semaphore;

public class ContestMain {
	public static final int numProblems = 100;
	public static final int numTeams = 10000;
	
	public static void main (String [] args) throws InterruptedException 
	{
		Semaphore winner = new Semaphore(1);
		
		ContestMain cm = new ContestMain();
		
		Team [] teams = new Team [numTeams];
		CreateTeams(teams, winner, cm);	
		StartContest(teams);
		
		synchronized (cm) {
			System.out.println("Waiting first team to finish all problems");
			cm.wait();
		}
		TeamsBubbleSort(teams);
		PrintTeams(teams);
	}
	
	private static void PrintTeams(Team [] teams) 
	{
		for(int i = 0; i < numTeams;i++)
			System.out.println("Team "+teams[i].GetNumber()+" solved "+teams[i].GetNumProblemsSolved()+" problems");
	}
	
	private static void TeamsBubbleSort(Team [] teams) 
	{
		Team temp;
		for(int i = 0; i < numTeams;i++) 
			for(int j = 1; j < numTeams;j++) 
				if(teams[j].GetNumProblemsSolved() > teams[j-1].GetNumProblemsSolved()) {
					temp = teams[j];
					teams[j] = teams[j-1];
					teams[j-1] = temp;
				}
			
		
	}
	
	private static void StartContest(Team [] teams) 
	{		
		for(int i = 0; i < numTeams; i++) 
		{
			teams[i].StartContest();
		}		
	}
	
	private static void CreateTeams(Team [] teamsArr,Semaphore winner, ContestMain cm) 
	{
		for(int i = 0; i < numTeams; i++) 
		{
			teamsArr[i] = new Team(numProblems,i+1,winner,cm);
		}
	}
}
