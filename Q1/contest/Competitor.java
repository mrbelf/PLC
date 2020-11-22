package contest;

import java.util.concurrent.Semaphore;

public class Competitor extends Thread{
	private Semaphore teamComputer;
	private IntPointer problemsCounter;
	private Semaphore problemsCounterSemaphore;
	private IntPointer problemsSolved;
	private Semaphore problemsSolvedSemaphore;
	private int currentProblemNumber;
	private Team team;
	private Semaphore winner;
	private Boolean contestIsOver = false;
	private ContestMain cm;
	
	public void EndContest() 
	{
		this.contestIsOver = true;
	}
	
	public Competitor(Semaphore teamComputer, Semaphore problemsCounterSemaphore, IntPointer problemsCounter,Team team, IntPointer problemsSolved, Semaphore problemsSolvedSemaphore, Semaphore winner, ContestMain cm) 
	{
		this.teamComputer = teamComputer;
		this.problemsCounterSemaphore = problemsCounterSemaphore;
		this.problemsCounter = problemsCounter;
		this.team = team;
		this.problemsSolved = problemsSolved;
		this.problemsSolvedSemaphore = problemsSolvedSemaphore;
		this.winner = winner;
		this.cm = cm;
	}
	
	public void run() 
	{
		//System.out.println("Starting competitor from team: "+team.GetNumber());
		while(!contestIsOver && GetProblem()) 
		{			
			try {
				//solving problem in paper
				sleep(1500);
				//solving problem in computer
				teamComputer.acquire();
				sleep(1000);
			} catch (InterruptedException e) {}
			teamComputer.release();
			
			try {
				this.problemsSolvedSemaphore.acquire();
			} catch (InterruptedException e) {}
			if(this.problemsSolved.GetValue() + 1 == ContestMain.numProblems) 
			{
				if(winner.tryAcquire()) 
				{
					this.problemsSolved.Increase();	
					synchronized (cm) {
						//System.out.println("Notify");
						cm.notify();
					}
				}
			}
			else 
			{
				this.problemsSolved.Increase();	
			}
			this.problemsSolvedSemaphore.release();
			//System.out.println("Comptetitor from team "+team.GetNumber()+" solved the problem number: "+ currentProblemNumber);
		}
	}
	
	public Boolean GetProblem() 
	{
		Boolean result;
		try {
			problemsCounterSemaphore.acquire();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		int problemsToSolve = problemsCounter.GetValue();
		problemsCounter.Decrease();
		problemsCounterSemaphore.release();
		result = problemsToSolve > 0;
		if(result) 
		{
			currentProblemNumber = problemsToSolve;
		}
		return result;
	}
}
