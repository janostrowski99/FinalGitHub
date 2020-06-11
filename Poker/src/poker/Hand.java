package poker;
public class Hand {

	int colour[]=new int[5];
	int number[]=new int[5];
	// Dane na pocz¹tku dla widocznoœci przyjêtych wartoœci;
	int w1=1;
	int w2=2;
	int w3=3;
	int w4=4;
	int w5=5;
	int w6=6;
	int w7=7;
	int w8=8;
	int w9=9;
	int specific=0;
	/*		1 nic		+
     * 		2 para		++
	 * 		3 2 pary	+
	 * 		4 trojka	++
	 * 		5 streat	++
	 * 		6 full		++
	 * 		7 kolor		+
	 * 		8 kareta	++
	 * 		9 poker		++
	 */	
	public Hand(int colour2[], int number2[])
	{
		for(int i=0;i<5;i++)
		{
			colour[i]=colour2[i];
			number[i]=number2[i];
		}
	}
	public Hand()
	{
		for(int i=0;i<5;i++)
		{
			colour[i]=0;
			number[i]=0;
		}
	}
	
	
	// Zwraca punktowe wartoœci z powy¿szych
	
	int Score()
	{
		int a=w1;
		int b=15;
		int checker1[]=new int[4];		


		
		if(colour[0]==colour[1]&&colour[1]==colour[2]&&colour[2]==colour[3]&&colour[3]==colour[4])
		{
			a=w7; 
		}
		for(int i=0;i<5;i++)
		{
			if(b>number[i])
			{		
				b=number[i];
			}
			
		}

		for(int i=0;i<4;i++)
		{	
			checker1[i]=0;
			for(int j=0;j<5;j++)
			{
				if((b+i+1)==number[j])
				{
					checker1[i]=1;
					
				}
			}
		}
		if(checker1[0]==1&&checker1[1]==1&&checker1[2]==1&&checker1[3]==1)
		{
			if(a==w7)
			{
				a=w9;
				specific=b;
				return a;
			}
			else
			{
				a=w5;
				specific=b;
				return a;
			}
		}
		
		for(int i=0;i<5;i++)			
		{
			b=0;
			for(int j=0;j<5;j++)
			{
				if(number[i]==number[j])
				{
					b++;
					if(specific<number[i]&&i!=j)
					{
						specific=number[i];
					}
				}
		
			}
			if(b==4)
			{
				a=w8;
				if(number[0]==number[1])
				{
					specific=number[0];
				}
				else
				{
					specific=number[2];
				}
				return a;
			}
			else if(b==3&&a<w6)
			{
				a=w4;
				if(number[0]==number[1]&&number[0]==number[2])
				{
					specific=number[0];
				}
				else if(number[0]==number[1]&&number[0]==number[3])
				{
					specific=number[0];
				}
				else if(number[2]==number[1]&&number[2]==number[3])
				{
					specific=number[2];
				}
				else if(number[2]==number[0]&&number[2]==number[3])
				{
					specific=number[2];
				}
				else
				{
					specific=number[4];
				}
			}
			else if(b==2&&a<w4)
			{
				
				a=w2;
			}
			
		}
		
		if(a==w4)
		{
			checker1[0]=number[0];
			if(number[1]==number[0])
			{
				if(number[0]==number[2])
				{
					checker1[1]=number[3];
				}
				else
				{
					checker1[1]=number[2];
				}
			}
			else
			{
				checker1[1]=number[1];
			}
			b=0;
			for(int i=0;i<5;i++)
			{
				if(number[i]==checker1[0]||number[i]==checker1[1])
				{
					b++;
				}
			}
			if(b==5)
			{
				a=w6;
				return a;
			}
			else
			{
				return a;
			}
			
		}
		b=0;
		for(int i=0;i<5;i++)
		{
			for(int j=0;j<5;j++)
			{
				if(number[i]==number[j])
				{
					b++;
				}
			}
		}
		if(b==9)
		{
			a=w3;
		}
			
		return a;
	}	
	
	// 1 wygrywa garcz 0 komputer 2 remis
	int Judge(Hand player)
	{
		
		int a=player.Score();
		int b=this.Score();
		
		if(a>b)
		{
			return 1;
		}
		else if(b>a)
		{
			return 0;
		}
		else 
		{
			int d=0;
			int c=0;
			switch (a)
			{
			case 1:
				for(int i=14; i>1;i--)
				{
					for(int j=0;j<5;j++)
					{
						if(player.number[j]==i)
						{
							c=1;
						}
						if(this.number[j]==i)
						{
							d=1;
						}
					}
					if(c>d)
					{
						return 1;
					}
					else if(d>c)
					{
						return 0;
					}
					else
					{
						d=0;
						c=0;
					}
				}
				return 2;
				
			case 2:
				if(player.specific>this.specific)
				{
					return 1;
				}
				else if(player.specific<this.specific)
				{
					return 0;
				}
				else
				{
					for(int i=14; i>1;i--)
					{
						for(int j=0;j<5;j++)
						{
							if(player.number[j]==i)
							{
								c=1;
							}
							if(this.number[j]==i)
							{
								d=1;
							}
						}
						if(c>d)
						{
							return 1;
						}
						else if(d>c)
						{
							return 0;
						}
						else
						{
							d=0;
							c=0;
						}
					}
					return 2;
				}
			case 3:
				if(player.specific>this.specific)
				{
					return 1;
				}
				else if(player.specific<this.specific)
				{
					return 0;
				}
				else
				{
					for(int i=14; i>1;i--)
					{
						for(int j=0;j<5;j++)
						{
							if(player.number[j]==i)
							{
								c++;
							}
							if(this.number[j]==i)
							{
								d++;
							}
						}
						if(c>d&&c==2)
						{
							return 1;
						}
						else if(d>c&&d==2)
						{
							return 0;
						}
						else
						{
							d=0;
							c=0;
						}
					}
					for(int i=14; i>1;i--)
					{
						for(int j=0;j<5;j++)
						{
							if(player.number[j]==i)
							{
								c=1;
							}
							if(this.number[j]==i)
							{
								d=1;
							}
						}
						if(c>d)
						{
							return 1;
						}
						else if(d>c)
						{
							return 0;
						}
						else
						{
							d=0;
							c=0;
						}
					}
					return 2;
				}
			case 4:
				if(player.specific>this.specific)
				{
					return 1;
				}
				else 
				{
					return 0;
				}

			case 5:
				if(player.specific>this.specific)
				{
					return 1;
				}
				else if(player.specific<this.specific)
				{
					return 0;
				}
				else
				{
					return 2;
				}
			case 6:
				if(player.specific>this.specific)
				{
					return 1;
				}
				else 
				{
					return 0;
				}
			case 7:
				for(int i=14; i>1;i--)
				{
					for(int j=0;j<5;j++)
					{
						if(player.number[j]==i)
						{
							c=1;
						}
						if(this.number[j]==i)
						{
							d=1;
						}
					}
					if(c>d)
					{
						return 1;
					}
					else if(d>c)
					{
						return 0;
					}
					else
					{
						d=0;
						c=0;
					}
				}
				return 2;
			case 8:
				if(player.specific>this.specific)
				{
					return 1;
				}
				else 
				{
					return 0;
				}
			case 9:
				if(player.specific>this.specific)
				{
					return 1;
				}
				else if(player.specific<this.specific)
				{
					return 0;
				}
				else
				{
					return 2;
				}

			
			}
	
		}
	
		return 0;
	}

	
}
