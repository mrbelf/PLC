package jogoDasCadeiras;
import java.util.*;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

public class JogoDasCadeirasMain {
	private final static int numJogadores = 5;

	public static void main(String[] args) throws InterruptedException {
		StringPointer lastEliminated = new StringPointer("empty");
		JogoDasCadeirasMain jdcm =  new JogoDasCadeirasMain();
		ArrayList<AtomicBoolean> chairs = new ArrayList<AtomicBoolean> ();
		Jogador [] jogadores = new Jogador [numJogadores];
		AtomicInteger aquiredChairsCount = new AtomicInteger(0);
		
		

		for(int i = 0; i < numJogadores; i++)
			jogadores[i] = new Jogador("Jogador "+i,chairs,aquiredChairsCount,jdcm,lastEliminated);

		for(int i = 0; i < numJogadores-1; i++)
			chairs.add(new AtomicBoolean(false));
		
		synchronized (jdcm) {
			runGame(jogadores);
			jdcm.wait();
			System.out.println("O "+ lastEliminated.getStr() + " foi eliminado");
		}
		
		while(chairs.size() > 1) 
		{	
			//remove one chair
			chairs.remove(chairs.size() - 1);
			
			//set all chairs as free
			for(int i = 0; i < chairs.size(); i++)
				chairs.get(i).set(false);
			
			//remove chair from all players
			for(int i = 0; i < numJogadores; i++)
				jogadores[i].standUp();
			
			//reset chairs count
			aquiredChairsCount.set(0);
			
			synchronized (jdcm) {
				jdcm.notifyAll();
				jdcm.wait();
				System.out.println("O "+ lastEliminated.getStr() + " foi eliminado");
			}
		}
		
		for(int i = 0; i < numJogadores; i++)
			if(jogadores[i].hasChair()) 
			{
				System.out.println("O jogador "+jogadores[i].getPlayerName()+" foi o vencedor!");
				jogadores[i].stop();
			}
	}
	
	static void runGame(Jogador [] jogadores) 
	{
		for(int i = 0; i < jogadores.length; i++) 
		{
			jogadores[i].start();
		}
	}

}
