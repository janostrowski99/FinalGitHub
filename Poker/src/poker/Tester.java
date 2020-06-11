package poker;


public class Tester {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		
		StartTheDraw st= new StartTheDraw();
		st.run();
		for(int i=0;i<5;i++)
		{
			System.out.println(st.handBot.colour[i]);
			System.out.println(st.handBot.number[i]);
		}
		StartTheSwap sw=new StartTheSwap(3,st.handBot);
		sw.run();
		for(int i=0;i<5;i++)
		{
			System.out.println(sw.change[i]);
		}
		
	}

}
