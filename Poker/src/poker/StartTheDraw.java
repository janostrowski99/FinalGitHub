package poker;

import java.util.Random;

public class StartTheDraw implements Runnable {
int num=0;
Hand handPlayer;
Hand handBot;
	public StartTheDraw(){
		handPlayer=new Hand();
		handBot=new Hand();
	}

	@Override
	public void run() {
		Random r = new Random(); 
		int kolor,numer;
		int checker=0;
		for(int i=0;i<5;i++)
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
			if(checker==0)
			{
				handPlayer.colour[i]=kolor;
				handPlayer.number[i]=numer;
			}
			else
			{
				i--;
			}
		
		}
		for(int i=0;i<5;i++)
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
			if(checker==0)
			{
				handBot.colour[i]=kolor;
				handBot.number[i]=numer;
			}
			else
			{
				i--;
			}
		
		}
	}

}
