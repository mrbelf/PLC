package jogoDasCadeiras;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.Random;
import java.util.*;

public class Jogador extends Thread{
	
	private Boolean hasChair;
	private String name;
	private ArrayList<AtomicBoolean> chairs;
	private AtomicInteger aquiredChairsCount;
	private StringPointer lastEliminated;
	private JogoDasCadeirasMain jdcm;
	
	public String getPlayerName() 
	{
		return this.name;
	}
	
	public void standUp() 
	{
		this.hasChair = false;
	}
	
	public Jogador (String name, ArrayList<AtomicBoolean> chairs, AtomicInteger chairsCount, JogoDasCadeirasMain jdcm,StringPointer lastEliminated) 
	{
		hasChair = false;
		this.name = name;
		this.chairs = chairs;
		this.aquiredChairsCount = chairsCount;
	    this.jdcm = jdcm;
	    this.lastEliminated = lastEliminated;
	}
	
	public void run () 
	{
		Random r = new Random();
		while (!hasChair) 
		{
			int index = Math.abs(r.nextInt()%chairs.size());
			if(!chairs.get(index).getAndSet(true)) 
			{
				hasChair = true;
				aquiredChairsCount.incrementAndGet();
				//System.out.println(name+" aquired chair: " + index);
				synchronized (jdcm) {
					try {
						jdcm.wait();
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
			}
			if(aquiredChairsCount.get() == chairs.size()) 
			{
				synchronized (jdcm) {
					lastEliminated.setStr(this.name);
					jdcm.notify();
					return;
				}
			}
		}
	}

	public boolean hasChair() {
		return this.hasChair;
	}
}
