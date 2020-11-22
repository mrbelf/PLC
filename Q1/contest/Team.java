package contest;

import java.util.concurrent.Semaphore;

public class Team {
	
	private int teamNumber;
	private Competitor [] competitors = new Competitor [3];
	private Semaphore computer;
	private Semaphore problemsCountSemaphore;
	private IntPointer problemsCount;
	private IntPointer problemsSolved;
	private Semaphore problemsSolvedSemaphore;
	
	public Team (int totalProblems,int teamNumber, Semaphore winner, ContestMain cm) 
	{
		this.problemsSolved = new IntPointer(0);
		this.problemsSolvedSemaphore = new Semaphore(1);
		this.teamNumber = teamNumber;
		this.computer = new Semaphore(1); 
		this.problemsCountSemaphore = new Semaphore(1);
		this.problemsCount = new IntPointer(totalProblems);
		competitors[0] = new Competitor(computer,problemsCountSemaphore,problemsCount,this,problemsSolved,problemsSolvedSemaphore, winner, cm);
		competitors[1] = new Competitor(computer,problemsCountSemaphore,problemsCount,this,problemsSolved,problemsSolvedSemaphore, winner, cm);
		competitors[2] = new Competitor(computer,problemsCountSemaphore,problemsCount,this,problemsSolved,problemsSolvedSemaphore, winner, cm);
	}
	
	public int GetNumber() {return this.teamNumber;}
	
	public int GetNumProblemsSolved() {return problemsSolved.GetValue();}
	
	public void StartContest() 
	{
		competitors[0].start();
		competitors[1].start();
		competitors[2].start();
	}
}
