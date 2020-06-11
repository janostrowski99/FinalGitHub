package poker;


public class BotHard24 extends Hand {

	//Wartoœci punktacji dla tego bota 
	int multi[];
	int change[]=new int[5];
	
	
	public BotHard24(int colour2[], int number2[])
	{
		super(colour2,number2);
		multi=new int[10];
		//Wartoœci punktacji dla tego bota 
		/*		1 nic		
	     * 		2 para		
		 * 		3 2 pary	
		 * 		4 trojka	
		 * 		5 streat	
		 * 		6 full		
		 * 		7 kolor		
		 * 		8 kareta	
		 * 		9 poker		
		 */	
		multi[1]=1;
		multi[2]=25;
		multi[3]=35;
		multi[4]=45;
		multi[5]=65;
		multi[6]=80;
		multi[7]=90;
		multi[8]=95;
		multi[9]=100;
		//Tutaj mo¿ne dodac Swap(); any przy tworzeniu mieæ ju¿ wiedze o wymianie
	}
	public BotHard24(Hand hand)
	{
		super(hand.colour,hand.number);
		multi=new int[10];
		//Wartoœci punktacji dla tego bota 
		/*		1 nic		
	     * 		2 para		
		 * 		3 2 pary	
		 * 		4 trojka	
		 * 		5 streat	
		 * 		6 full		
		 * 		7 kolor		
		 * 		8 kareta	
		 * 		9 poker		
		 */	
		multi[1]=1;
		multi[2]=25;
		multi[3]=35;
		multi[4]=45;
		multi[5]=65;
		multi[6]=80;
		multi[7]=90;
		multi[8]=95;
		multi[9]=100;
	}
	
	void Swap()	
	{
		int state[]=new int[5];
		long total0=0;
		long total=0;
		int posi=0;
		int help1;
		int tempo=0;
		int hcolour[]=new int[5];
		int hnumber[]=new int[5];
		int hcard[]=new int[5];
		int checker=1;
		Hand test=new Hand(colour,number);
		for(int i=0;i<5;i++)
		{
			state[i]=0;
		}
		for(int p=0;p<(2*2*2*2*2);p++)
		{
			//Wybór odrzucanych kart
			//System.out.println(p);
			for(int i=0;i<5;i++)
			{
				if(state[i]==0)
				{
					state[i]=1;
					for(int j=0;j<i;j++)
					{
						state[j]=0;
					}
					i=6;
				}
			}
			//System.out.println(state[0]+state[1]+state[2]+state[3]+state[4]);
			//ile wymieniam
			/*help1=0;
			for(int i=0;i<5;i++)
			{
				if(state[i]==1)
				{
					help1++;
				}
			}*/
			posi=0;
			total=0;
			for(int i=0;i<(24*24*24*24*24);i++)
			{
				help1=i;
				for(int k=0;k<5;k++)
				{
					hcard[k]=help1%24;
					help1=(help1-hcard[k])/24;	
					hcolour[k]=hcard[k]%4;
					hnumber[k]=((hcard[k]-(hcolour[k]))/4)+9;
				}
				
				checker=1;
				while(tempo<5)
				{
					if(state[tempo]==1)
					{
						for(int l=0;l<5;l++)
						{
							if(hnumber[tempo]==number[l]&&hcolour[tempo]==colour[l])
							{
								checker=0;
								//System.out.println("nie");
								tempo=4;
							}
						}
					}
					else
					{
						checker=0;
						//System.out.println("+");
						if((hnumber[tempo]==number[tempo])&&(hcolour[tempo]==colour[tempo]))
						{
							checker=1;
							//System.out.println("-");
							//System.out.println("tak");
						}
						else
						{
							tempo=4;
						}
						
					}
					tempo++;
				}
				tempo=0;
				if(checker==1)
				{
					posi++;
					//System.out.println("+");
					for(int l=0;l<5;l++)
					{
						test.number[l]=hnumber[l];
						test.colour[l]=hcolour[l];
					}
					help1=test.Score();
					help1=multi[help1];
	
					total=total+help1;
					//System.out.println(total);
				}
		
			}
			//System.out.println(posi);
			//System.out.println(total);
			total=total/posi;
			//System.out.println(total);
			//System.out.println(total0);
			if(total0<total)
			{
				total0=total;
				for(int l=0;l<5;l++)
				{
					change[l]=state[l];
					//System.out.println(p);
				}
			}

				
			
		}
		
		help1=Score();
		total=multi[help1];
		//System.out.println(total);
		//System.out.println(total0);
		if(total0<total)
		{
			total0=total;
			for(int l=0;l<5;l++)
			{
				change[l]=0;
			}
		}
		
	}
	
}
