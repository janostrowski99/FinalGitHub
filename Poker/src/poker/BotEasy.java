package poker;

public class BotEasy extends Hand {
	
	int change[]=new int[5];
	
	public BotEasy(int colour2[], int number2[])
	{
		super(colour2,number2);
		//Tutaj mo¿ne dodac Swap(); any przy tworzeniu mieæ ju¿ wiedze o wymianie
		
	}
	public BotEasy(Hand hand)
	{
		super(hand.colour,hand.number);
	}
	void Swap() //Algorytm: Wyrzuca 9, 10, W, Q, a zostawia K, A
	{
		for(int i=0;i<5;i++)
		{
			if(number[i]>12)
			{
				change[i]=0;
			}
			else
			{
				change[i]=1;
			}	
		}
	}
	
}
