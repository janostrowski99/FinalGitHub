package poker;

public class StartTheSwap implements Runnable {
int num=0;
int colour[];
int number[];
int change[]=new int[5];

	public StartTheSwap(int poziom,int colour2[], int number2[]){
		//1 easy 2 normal 3 hard
		num=poziom;
		colour=colour2;
		number=number2;
	}
	public StartTheSwap(int poziom,Hand hand){
		//1 easy 2 normal 3 hard
		num=poziom;
		colour=hand.colour;
		number=hand.number;
	}
	public StartTheSwap(int poziom,int colour2[], int number2[],int memory[]){
		//1 easy 2 normal 3 hard
		num=poziom;
		colour=colour2;
		number=number2;
	}
	

	@Override
	public void run() {
		if(num==1)
		{
			BotEasy opponent= new BotEasy(colour,number);
			opponent.Swap();
			change=opponent.change;
		}
		if(num==2)
		{
			BotNormal opponent= new BotNormal(colour,number);
			opponent.Swap();
			change=opponent.change;
		}
		if(num==3)
		{
			BotHard24 opponent= new BotHard24(colour,number);
			opponent.Swap();
			change=opponent.change;
		}
		if(num==4)
		{
			BotExperimental24 opponent= new BotExperimental24(colour,number);
			opponent.Swap();
			change=opponent.change;
		}
	}

}
