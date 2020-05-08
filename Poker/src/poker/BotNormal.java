package poker;

public class BotNormal extends Hand {

	int change[]=new int[5];
	
	public BotNormal(int colour2[], int number2[])
	{
		super(colour2,number2);
		//Tutaj mo¿ne dodac Swap(); any przy tworzeniu mieæ ju¿ wiedze o wymianie
		
	}
	public BotNormal(Hand hand)
	{
		super(hand.colour,hand.number);
	}
	void Swap()
	{
		int a=Score();
		Hand help;
		
		for(int i=0;i<5;i++)
		{
			help=new Hand(colour,number);
			help.number[i]=0;
			help.colour[i]=6;
			
			if(a==help.Score())
			{
				change[i]=1;
			}
			else
			{
				change[i]=0;
			}
		}
		if(a==1)
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
	
}
