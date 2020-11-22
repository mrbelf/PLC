package contest;

public class IntPointer {
	private int value;
	public IntPointer(int value) 
	{
		this.value = value;
	}

	public int GetValue () {return value;}
	public int Increase () {return value++;}
	public int Decrease () {return value--;}
}
