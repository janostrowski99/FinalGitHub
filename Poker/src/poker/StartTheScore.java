package poker;

import java.util.Random;

public class StartTheScore implements Runnable {

	Hand handPlayer,handBot;
	int[] cplayer;
	int[] cbot;
	int score;
	public StartTheScore(Hand player, Hand bot , int[] cp, int[] cb) {
		handPlayer=player;
		handBot=bot;
		cplayer=cp;
		cbot=cb;
		score=-1;

	}
	@Override
	public void run() {
		
		//System.out.println("Odrzucone user: ");
		//for(int i=0;i<5;i++)
			//System.out.println(cplayer[i]);
			
		Random r = new Random(); 
		int kolor,numer;
		int checker=0;
		int totalSwap=0; //ile kart trzeba wymieniæ (od 0 do 10)
		for(int i=0;i<5;i++)
		{
			if(cplayer[i]==1)
			{
				totalSwap++;
			}
			if(cbot[i]==1)
			{
				totalSwap++;
			}
		}
		int newCol[]=new int[totalSwap];
		int newNum[]=new int[totalSwap];
		
		for(int i=0;i<totalSwap;i++)
		{
			kolor=r.nextInt(4);
			numer=r.nextInt(6)+9;
			checker=0;
			
			for(int j=0;j<5;j++)
			{
				if(kolor==handPlayer.colour[j]&&numer==handPlayer.number[j])
				{
					checker++;
				}
				if(kolor==handBot.colour[j]&&numer==handBot.number[j])
				{
					checker++;
				}
				
			}
			for(int j=0;j<i;j++)
			{
				if(kolor==newCol[j]&&numer==newNum[j])
				{
					checker++;
				}
				
			}
			if(checker==0)
			{
				newCol[i]=kolor; //przypisanie wartoœci nowo wylosowanych kart
				newNum[i]=numer;
			}
			else
			{
				i--;
			}
		
		}
		int k=0;
		for(int i=0;i<5;i++)
		{
			
			if(cbot[i]==1)
			{
				handBot.colour[i]=newCol[k]; //zapisanie wymienionych kart na nowe
				handBot.number[i]=newNum[k];
				k++;
			}
			if(cplayer[i]==1)
			{
				handPlayer.colour[i]=newCol[k];
				handPlayer.number[i]=newNum[k];
				k++;
			}
		}
		System.out.println("Rêka bot:");
		for(int i=0;i<5;i++)
		{
			System.out.println(handBot.colour[i]+" "+handBot.number[i]);
		}
		System.out.println("Rêka user:");
		for(int i=0;i<5;i++)
		{
			System.out.println(handPlayer.colour[i]+" "+handPlayer.number[i]);
		}
		score=handBot.Judge(handPlayer); //1-wygrana 0-przegrana

		//for(int i=0;i<5;i++) {
			//cplayer[i]=0;
			//cbot[i]=0;
		//}		
	}

}
