package poker;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.GridLayout;
import java.awt.HeadlessException;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Field;

import javax.imageio.ImageIO;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.JTabbedPane;
import javax.swing.JTextField;
/* Poker
 * Autorzy: Jan Ostrowski, Maurycy Muzyka
 * 
 * Aktualny progress:
 * gra na 3 poziomach trudnoœci (UWAGA! na poziomie trudnym komputer odrzuca karty przez ~1-2 minuty, nie polecam testowaæ)
 * statystyka dla obecnej sesji (uk³ady + zwyciêstwa/pora¿ki/remisy/winRatio)
 * okienko po zakoñczeniu partii z podsumowaniem partii - Rozgrywka
 * grafiki kart
 * zapisanie partii do pliku
 * import partii z pliku - Rozgrywka
 * github
 * waluta w grze
 * 
 * Do zrobienia:
 * 1 ulepszenie waluty gry (premiowane winstreaki? - do ustalenia)
 * 2 zapisywanie statystyk zwyciêstw/... do pliku txt - automatyczne importowanie przy ka¿dym w³¹czeniu gry
 * 3 mo¿liwoœc zakupu innego wygl¹du kart
 * 4 wersja anglojêzyczna (polskie napisy maj¹ komentarze w kodzie, wiêc ³atwo)
 * 5 dzwieki
 * 6 bot insane - analiza gry gracza 
 */
public class Poker extends JFrame {

	private static final long serialVersionUID = 1L;
	
	//elementy wizualne
	JTabbedPane tabbedPane;
	JPanel glownyPanel, statsPanel;
	JPanel gora, lewa, srodek, prawa, dol, glosnoscPanel;
	JLabel napisGlowny, poziomyTrudnosci;
	JButton lvl1, lvl2, lvl3, mute, importuj, zapisz, wyjscie;
	JSlider regulacjaGlosnosci;
	JTextField moneyCount, winsSession, losesSession, drawsSession, winRatioSession;
	
	JPanel statsS, stats1, stats2, stats3, statsAll;
	JLabel infS0, infS1, infS2, infS3, infS4, infS5, infS6, infS7, infS8, infS9, infS10;
	JLabel infA0, infA1, infA2, infA3, infA4, infA5, infA6, infA7, infA8, infA9, infA10;
	JLabel infB0, infB1, infB2, infB3, infB4, infB5, infB6, infB7, infB8, infB9, infB10;
	JLabel infC0, infC1, infC2, infC3, infC4, infC5, infC6, infC7, infC8, infC9, infC10;
	JLabel infD0, infD1, infD2, infD3, infD4, infD5, infD6, infD7, infD8, infD9, infD10;
	
	JPanel panelGry, up, mid, down;
	JLabel przeOdrz;
	JButton odrzuc;
	JCheckBox odrzKarta1, odrzKarta2, odrzKarta3, odrzKarta4, odrzKarta5;
	JLabel k1, k2, k3, k4, k5; //zmienic potem na obrazki png
	ImagePanel pikk9, pikk10, pikkW, pikkQ, pikkK, pikkA, kierr9, kierr10, kierrW, kierrQ, kierrK, kierrA;
	ImagePanel  karoo9, karoo10, karooW, karooQ, karooK, karooA, trefll9, trefll10, trefllW, trefllQ, trefllK, trefllA;
	BufferedImage pik9, pik10, pikW, pikQ, pikK, pikA, kier9, kier10, kierW, kierQ, kierK, kierA;
	BufferedImage karo9, karo10, karoW, karoQ, karoK, karoA, trefl9, trefl10, treflW, treflQ, treflK, treflA;
	
	//zmienne
	static final int SLIDER_MIN = 0; 
	static final int SLIDER_MAX = 100;
	static final int SLIDER_INIT = 50;
	int money = 100; //zachowywane przy wy³¹czaniu gry
	int[] wins = new int[4]; //0-³atwy; 1-œredni; 2-trudny; 3-³¹cznie; zachowywane przy wy³¹czaniu gry
	int[] loses = new int[4];
	int[] draws = new int[4];
	int[] games = new int[4];
	int[] winRatio = new int[4];
	int[] memory = new int[20]; //statystyki sesji
	int winsS = 0; //wy³¹cznie w tej sesji
	int losesS = 0;
	int drawsS = 0;
	int gamesS = 0;
	double winRatioS = 0.0;
	int odrz = 0; //ile kart odrzucil przeciwnik
	String koloryWy, figuryWy, tekstWy;
	int ukl1, ukl2, wynik; //temp uk³adów i kto wygra³ do zapisania do pliku
	
	public Poker() throws HeadlessException { //---------------------------------konstruktor
		this.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		this.setSize(800,400);
		this.setLayout(new BorderLayout());
		this.setTitle("Poker | Menu g³ówne");
		//panele
		tabbedPane = new JTabbedPane();
		glownyPanel = new JPanel();
		statsPanel = new JPanel();
		gora = new JPanel();
		lewa = new JPanel();
		srodek = new JPanel();
		prawa = new JPanel();
		dol = new JPanel();
		glosnoscPanel = new JPanel();
		
		statsS = new JPanel();
		stats1 = new JPanel();
		stats2 = new JPanel();
		stats3 = new JPanel();
		statsAll = new JPanel();
		
		//elementy wizualne 
			//jêzyk polski
		napisGlowny = new JLabel("POKER");
		poziomyTrudnosci = new JLabel("Poziom trudnoœci");
		lvl1 = new JButton("£atwy");
		lvl2 = new JButton("Œredni");
		lvl3 = new JButton("Trudny (UWAGA! d³ugi czas kalkulacji)");
		mute = new JButton("Mute");
		importuj = new JButton("Importuj partiê");
		zapisz = new JButton("Zapisz ostatnio zagran¹ partiê");
		wyjscie = new JButton("Wyjœcie");
		
		regulacjaGlosnosci = new JSlider(JSlider.HORIZONTAL, SLIDER_MIN, SLIDER_MAX, SLIDER_INIT);
		moneyCount = new JTextField(String.format("%d", money));
		winsSession = new JTextField(String.format("%d", winsS));
		losesSession = new JTextField(String.format("%d", losesS));
		drawsSession = new JTextField(String.format("%d", drawsS));
		winRatioSession = new JTextField("NDF");
		
		
		//wstawianie elemenów wizualnych
		this.add(tabbedPane);
			//jêzyk polski
		tabbedPane.addTab("Menu G³ówne", glownyPanel);
		tabbedPane.addTab("Statystyki", statsPanel);
		
		glownyPanel.setLayout(new BorderLayout());
		glownyPanel.add(gora, BorderLayout.NORTH);
		glownyPanel.add(lewa, BorderLayout.WEST);
		glownyPanel.add(srodek, BorderLayout.CENTER);
		glownyPanel.add(prawa, BorderLayout.EAST);
		glownyPanel.add(dol, BorderLayout.SOUTH);
		lewa.setLayout(new GridLayout(4,1));
		srodek.setLayout(new GridLayout(1,1));
		prawa.setLayout(new GridLayout(4,1));
		dol.setLayout(new GridLayout(1,10));
		gora.add(napisGlowny);
		lewa.add(poziomyTrudnosci);
		lewa.add(lvl1);
		lewa.add(lvl2);
		lewa.add(lvl3);
		prawa.add(glosnoscPanel);
		glosnoscPanel.add(mute);
		glosnoscPanel.add(regulacjaGlosnosci);
		prawa.add(importuj);
		prawa.add(zapisz);
		prawa.add(wyjscie);
		dol.add(moneyCount);
		dol.add(winsSession);
		dol.add(losesSession);
		dol.add(drawsSession);
		dol.add(winRatioSession);
		gora.setBackground(Color.MAGENTA);
		//regulacjaGlosnosci.setName("Regulacja g³oœnoœci");
		moneyCount.setBackground(Color.yellow);
		winsSession.setBackground(Color.green);
		losesSession.setBackground(Color.pink);
		drawsSession.setBackground(Color.lightGray);
		winRatioSession.setBackground(Color.cyan);
		
		//grafiki kart
		pik9 = null; pik10 = null; pikW = null; pikQ = null; pikK = null; pikA = null;
		kier9 = null; kier10 = null; kierW = null; kierQ = null; kierK = null; kierA = null;
		karo9 = null; karo10 = null; karoW = null; karoQ = null; karoK = null; karoA = null;
		trefl9 = null; trefl10 = null; treflW = null; treflQ = null; treflK = null; treflA = null;
		
		pikk9 = robKarte(pik9,"pik9"); 		pikk10 = robKarte(pik10,"pik10"); 		pikkW = robKarte(pikW,"pikW"); 
		pikkQ = robKarte(pikQ,"pikQ"); 		pikkK = robKarte(pikK,"pikK"); 			pikkA = robKarte(pikA,"pikA"); 
		kierr9 = robKarte(kier9,"kier9"); 	kierr10 = robKarte(kier10,"kier10");	kierrW = robKarte(kierW,"kierW");
		kierrQ = robKarte(kierQ,"kierQ"); 	kierrK = robKarte(kierK,"kierK"); 		kierrA = robKarte(kierA,"kierA");
		karoo9 = robKarte(karo9,"karo9"); 	karoo10 = robKarte(karo10,"karo10"); 	karooW = robKarte(karoW,"karoW");
		karooQ = robKarte(karoQ,"karoQ"); 	karooK = robKarte(karoK,"karoK"); 		karooA = robKarte(karoA,"karoA");
		trefll9 = robKarte(trefl9,"trefl9");trefll10 = robKarte(trefl10,"trefl10"); trefllW = robKarte(treflW,"treflW"); 
		trefllQ = robKarte(treflQ,"treflQ");trefllK = robKarte(treflK,"treflK"); 	trefllA = robKarte(treflA,"treflA"); 

		//stats
		statsPanel.setLayout(new GridLayout(1,5));
		statsPanel.add(statsS);
		statsPanel.add(stats1);
		statsPanel.add(stats2);
		statsPanel.add(stats3);
		statsPanel.add(statsAll);
		statsS.setLayout(new GridLayout(11,1));
		stats1.setLayout(new GridLayout(11,1));
		stats2.setLayout(new GridLayout(11,1));
		stats3.setLayout(new GridLayout(11,1));
		statsAll.setLayout(new GridLayout(11,1));

		for(int i=1;i<11;i++) {
			memory[i]=0;
		}
		String infoS0 = "Twoje uk³ady w tej sesji";
		String infoS1 = "Brak uk³adu "+memory[1];
		String infoS2 = "Para        "+memory[2];
		String infoS3 = "2 Pary      "+memory[3];
		String infoS4 = "Trójka      "+memory[4];
		String infoS5 = "Streat      "+memory[5];
		String infoS6 = "Full        "+memory[6];
		String infoS7 = "Kolor       "+memory[7];
		String infoS8 = "Kareta      "+memory[8];
		String infoS9 = "Poker       "+memory[9];
		String infoS10= "£¹cznie     "+memory[10];
		infS0 = new JLabel(infoS0);
		infS1 = new JLabel(infoS1);
		infS2 = new JLabel(infoS2);
		infS3 = new JLabel(infoS3);
		infS4 = new JLabel(infoS4);
		infS5 = new JLabel(infoS5);
		infS6 = new JLabel(infoS6);
		infS7 = new JLabel(infoS7);
		infS8 = new JLabel(infoS8);
		infS9 = new JLabel(infoS9);
		infS10 = new JLabel(infoS10);
		statsS.add(infS0);
		statsS.add(infS1);
		statsS.add(infS2);
		statsS.add(infS3);
		statsS.add(infS4);
		statsS.add(infS5);
		statsS.add(infS6);
		statsS.add(infS7);
		statsS.add(infS8);
		statsS.add(infS9);
		statsS.add(infS10);
		String infoA0 = "Poziom: ³atwy";
		String infoA1 = "Zwyciêstwa   "+wins[0];
		String infoA2 = "Przegrane    "+loses[0];
		String infoA3 = "Remisy       "+draws[0];
		String infoA4 = "£¹cznie      "+games[0];
		String infoA5 = "% zwyciêstw  "+winRatio[0];
		infA0 = new JLabel(infoA0);
		infA1 = new JLabel(infoA1);
		infA2 = new JLabel(infoA2);
		infA3 = new JLabel(infoA3);
		infA4 = new JLabel(infoA4);
		infA5 = new JLabel(infoA5);
		stats1.add(infA0);
		stats1.add(infA1);
		stats1.add(infA2);
		stats1.add(infA3);
		stats1.add(infA4);
		stats1.add(infA5);
		String infoB0 = "Poziom: œredni";
		String infoB1 = "Zwyciêstwa   "+wins[1];
		String infoB2 = "Przegrane    "+loses[1];
		String infoB3 = "Remisy       "+draws[1];
		String infoB4 = "£¹cznie      "+games[1];
		String infoB5 = "% zwyciêstw  "+winRatio[1];
		infB0 = new JLabel(infoB0);
		infB1 = new JLabel(infoB1);
		infB2 = new JLabel(infoB2);
		infB3 = new JLabel(infoB3);
		infB4 = new JLabel(infoB4);
		infB5 = new JLabel(infoB5);
		stats2.add(infB0);
		stats2.add(infB1);
		stats2.add(infB2);
		stats2.add(infB3);
		stats2.add(infB4);
		stats2.add(infB5);
		String infoC0 = "Poziom: trudny";
		String infoC1 = "Zwyciêstwa   "+wins[2];
		String infoC2 = "Przegrane    "+loses[2];
		String infoC3 = "Remisy       "+draws[2];
		String infoC4 = "£¹cznie      "+games[2];
		String infoC5 = "% zwyciêstw  "+winRatio[2];
		infC0 = new JLabel(infoC0);
		infC1 = new JLabel(infoC1);
		infC2 = new JLabel(infoC2);
		infC3 = new JLabel(infoC3);
		infC4 = new JLabel(infoC4);
		infC5 = new JLabel(infoC5);
		stats3.add(infC0);
		stats3.add(infC1);
		stats3.add(infC2);
		stats3.add(infC3);
		stats3.add(infC4);
		stats3.add(infC5);
		String infoD0 = "£¹cznie";
		String infoD1 = "Zwyciêstwa   "+wins[3];
		String infoD2 = "Przegrane    "+loses[3];
		String infoD3 = "Remisy       "+draws[3];
		String infoD4 = "£¹cznie      "+games[3];
		String infoD5 = "% zwyciêstw  "+winRatio[3];
		infD0 = new JLabel(infoD0);
		infD1 = new JLabel(infoD1);
		infD2 = new JLabel(infoD2);
		infD3 = new JLabel(infoD3);
		infD4 = new JLabel(infoD4);
		infD5 = new JLabel(infoD5);
		statsAll.add(infD0);
		statsAll.add(infD1);
		statsAll.add(infD2);
		statsAll.add(infD3);
		statsAll.add(infD4);
		statsAll.add(infD5);
		
		//listenery
		lvl1.addActionListener(new ActionListener() { //--------------------rozpoczecie gry (³atwy)
			@Override
			public void actionPerformed(ActionEvent e) {
				StartTheDraw s1 = new StartTheDraw(); //start the draw
				s1.run();
				
				//System.out.println("Rêka bot przed:");
				//for(int i=0;i<5;i++)
					//System.out.println(s1.handBot.colour[i]+" "+s1.handBot.number[i]);
				//System.out.println("Rêka user przed:");
				//for(int i=0;i<5;i++)
					//System.out.println(s1.handPlayer.colour[i]+" "+s1.handPlayer.number[i]);
				
				koloryWy = "";
				figuryWy = "";
				for(int i = 0; i < 5; i++) //1-5: kolory komp startowa
					koloryWy += String.valueOf(s1.handBot.colour[i]) + " ";
				for(int i = 0; i < 5; i++) //6-10: kolory gracz startowa
					koloryWy += String.valueOf(s1.handPlayer.colour[i]) + " ";
				for(int i = 0; i < 5; i++) //1-5: figury komp startowa 
					figuryWy += String.valueOf(s1.handBot.number[i]) + " ";
				for(int i = 0; i < 5; i++) //6-10: figury gracz startowa 
					figuryWy += String.valueOf(s1.handPlayer.number[i]) + " ";
			
				StartTheSwap s2 = new StartTheSwap(1,s1.handBot); //komputer odrzuca karty
				s2.run();

				//System.out.println("Odrzucone komp: ");
				for(int i=0;i<5;i++)
				{
					//System.out.println(s2.change[i]);
					odrz += s2.change[i];
				}
				
				JFrame latwa = new JFrame();
				latwa.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
				latwa.setSize(600,400);
				latwa.setVisible(true);
		        panelGry = new JPanel();
		        latwa.add(panelGry);
		        panelGry.setLayout(new BorderLayout());
		        up = new JPanel();
		        mid = new JPanel();
		        down = new JPanel();
		        panelGry.add(up, BorderLayout.NORTH);
				panelGry.add(mid, BorderLayout.CENTER);
				panelGry.add(down, BorderLayout.SOUTH);
				mid.setLayout(new GridLayout(2,5));
				        
		        //jêzyk polski
				latwa.setTitle("Poker | Rozgrywka");
		        przeOdrz = new JLabel("Przeciwnik odrzuci³ " + odrz); 
		        if(odrz==0 || odrz==5) //poprawnoœæ jêzykowa
		        	przeOdrz.setText(przeOdrz.getText()+" kart.");
		        if(odrz==1)
		        	przeOdrz.setText(przeOdrz.getText()+" kartê.");
		        if(odrz==2 || odrz==3 || odrz==4)
		        	przeOdrz.setText(przeOdrz.getText()+" karty.");
		        odrzuc = new JButton("Odrzuæ karty");
		        
		        //grafiki kart przy wymienianiu
		        for(int i=0; i<5; i++) {
		        	switch(s1.handPlayer.colour[i]) {
			        	case 0:
			        	{
			        		switch(s1.handPlayer.number[i]) {
					        	case 9:
					        		mid.add(pikk9);	
					        		break;	
					        	case 10:
					        		mid.add(pikk10);	
					        		break;	
					        	case 11:
					        		mid.add(pikkW);	
					        		break;	
					        	case 12:
					        		mid.add(pikkQ);	
					        		break;	
					        	case 13:
					        		mid.add(pikkK);	
					        		break;	
					        	case 14:
					        		mid.add(pikkA);	
					        		break;	
			        		}
			        		break;
			        	}
			        	case 1:
			        	{
			        		switch(s1.handPlayer.number[i]) {
					        	case 9:
					        		mid.add(kierr9);	
					        		break;	
					        	case 10:
					        		mid.add(kierr10);	
					        		break;	
					        	case 11:
					        		mid.add(kierrW);	
					        		break;	
					        	case 12:
					        		mid.add(kierrQ);	
					        		break;	
					        	case 13:
					        		mid.add(kierrK);	
					        		break;	
					        	case 14:
					        		mid.add(kierrA);	
					        		break;	
			        		}
			        		break;
			        	}
			        	case 2:
			        	{
			        		switch(s1.handPlayer.number[i]) {
					        	case 9:
					        		mid.add(karoo9);	
					        		break;	
					        	case 10:
					        		mid.add(karoo10);	
					        		break;	
					        	case 11:
					        		mid.add(karooW);	
					        		break;	
					        	case 12:
					        		mid.add(karooQ);	
					        		break;	
					        	case 13:
					        		mid.add(karooK);	
					        		break;	
					        	case 14:
					        		mid.add(karooA);	
					        		break;	
			        		}
			        		break;
			        	}
			        	case 3:
			        	{
			        		switch(s1.handPlayer.number[i]) {
					        	case 9:
					        		mid.add(trefll9);	
					        		break;	
					        	case 10:
					        		mid.add(trefll10);	
					        		break;	
					        	case 11:
					        		mid.add(trefllW);	
					        		break;	
					        	case 12:
					        		mid.add(trefllQ);	
					        		break;	
					        	case 13:
					        		mid.add(trefllK);	
					        		break;	
					        	case 14:
					        		mid.add(trefllA);	
					        		break;	
			        		}
			        		break;
			        	}
		        	}
		        	
		        }
		        
		        
		        
		        odrzKarta1 = new JCheckBox();
		        odrzKarta2 = new JCheckBox();
		        odrzKarta3 = new JCheckBox();
		        odrzKarta4 = new JCheckBox();
		        odrzKarta5 = new JCheckBox();
		        mid.add(odrzKarta1);
		        mid.add(odrzKarta2);
		        mid.add(odrzKarta3);
		        mid.add(odrzKarta4);
		        mid.add(odrzKarta5);
		        
		        down.add(odrzuc);
		        up.add(przeOdrz);

		        odrz = 0;
		       
		        //listenery
		        odrzuc.addActionListener(new ActionListener() { //zakoñczenie rozgrywki
		  			@Override
		  			public void actionPerformed(ActionEvent e) {
		  				int[] changePlayer = new int[5];
		  				if(odrzKarta1.isSelected())
	  						changePlayer[0]=1;
	  					else
	  						changePlayer[0]=0;
		  				if(odrzKarta2.isSelected())
	  						changePlayer[1]=1;
	  					else
	  						changePlayer[1]=0;
		  				if(odrzKarta3.isSelected())
	  						changePlayer[2]=1;
	  					else
	  						changePlayer[2]=0;
		  				if(odrzKarta4.isSelected())
	  						changePlayer[3]=1;
	  					else
	  						changePlayer[3]=0;
		  				if(odrzKarta5.isSelected())
	  						changePlayer[4]=1;
	  					else
	  						changePlayer[4]=0;

		  				//System.out.println("Odrzucone user: ");
		  				//for(int i=0;i<5;i++)
							//System.out.println(changePlayer[i]);
		  				
		  				StartTheScore s3 = new StartTheScore(s1.handPlayer, s1.handBot, changePlayer, s2.change);
		  				s3.run();
		  				
		  				for(int i = 0; i < 5; i++) //11-15: kolory komp koñcowa
							koloryWy += String.valueOf(s1.handBot.colour[i]) + " ";
						for(int i = 0; i < 5; i++) //11-15: figury komp koñcowa
							figuryWy += String.valueOf(s1.handBot.number[i]) + " ";
						for(int i = 0; i < 5; i++) //16-20: kolory gracz koñcowa
							koloryWy += String.valueOf(s1.handPlayer.colour[i]) + " ";
						for(int i = 0; i < 5; i++) //16-20: figury gracz koñcowa
							figuryWy += String.valueOf(s1.handPlayer.number[i]) + " ";
						
		  				memory[s1.handPlayer.Score()]++; //dodanie uk³adu do stats
		  				ukl1 = s1.handBot.Score();
		  				ukl2 = s1.handPlayer.Score();
		  				wynik = s3.score;
		  				
		  				tekstWy = "";
						tekstWy += koloryWy; //1-20
						tekstWy += figuryWy; //21-40
						tekstWy += String.valueOf(ukl1) + " "; //41: typ uk³adu komp koñcowa
						tekstWy += String.valueOf(ukl2) + " "; //42: typ uk³adu gracz koñcowa
						tekstWy += String.valueOf(wynik)+ " "; //43: kto wygra³
						
		  				Rozgrywka r2 = new Rozgrywka(tekstWy, "brak"); //okienko z rezultatem rozgrywki
		  				
		  				//System.out.println("SCORE: "+s1.handPlayer.Score());
		  				switch(s3.score) {
			  				case 1:
			  					money += 10;
			  					winsS++;
			  					System.out.println("Koniec rozgrywki - wygrana\n");
			  					wins[0]++;
			  					break;
			  				case 0:
			  					money -= 10;
			  					losesS++;
			  					System.out.println("Koniec rozgrywki - przegrana\n");
			  					loses[0]++;
			  					break;
			  				case 2:
			  					drawsS++;
			  					System.out.println("Koniec rozgrywki - remis\n");
			  					draws[0]++;
			  					break;
		  				}
		  				gamesS = winsS+losesS+drawsS;
		  				memory[10] = gamesS;
		  				moneyCount.setText(String.format("%d", money));
		  				winsSession.setText(String.format("%d", winsS));
		  				losesSession.setText(String.format("%d", losesS));
		  				drawsSession.setText(String.format("%d", drawsS));
		  				winRatioS = (double) 100*winsS/gamesS;
		  				winRatioSession.setText(winRatioS + "%");
		  				String infoS1 = "Brak uk³adu "+memory[1];
		  				String infoS2 = "Para        "+memory[2];
		  				String infoS3 = "2 Pary      "+memory[3];
		  				String infoS4 = "Trójka      "+memory[4];
		  				String infoS5 = "Streat      "+memory[5];
		  				String infoS6 = "Full        "+memory[6];
		  				String infoS7 = "Kolor       "+memory[7];
		  				String infoS8 = "Kareta      "+memory[8];
		  				String infoS9 = "Poker       "+memory[9];
		  				String infoS10= "£¹cznie     "+memory[10];
		  				infS1.setText(infoS1);
		  				infS2.setText(infoS2);
		  				infS3.setText(infoS3);
		  				infS4.setText(infoS4);
		  				infS5.setText(infoS5);
		  				infS6.setText(infoS6);
		  				infS7.setText(infoS7);
		  				infS8.setText(infoS8);
		  				infS9.setText(infoS9);
		  				infS10.setText(infoS10);
		  				latwa.setVisible(false);
		  				if(money<=0) {
		  					System.out.println("Nie masz pieniêdzy! Przegra³eœ!");
		  					System.exit(0);
		  				}
		  					
		  			}
		  		});		
			}	
		});	
		
		lvl2.addActionListener(new ActionListener() { //--------------------rozpoczecie gry (œredni)
			@Override
			public void actionPerformed(ActionEvent e) {
				StartTheDraw s1 = new StartTheDraw(); //start the draw
				s1.run();
				
				//System.out.println("Rêka bot przed:");
				//for(int i=0;i<5;i++)
					//System.out.println(s1.handBot.colour[i]+" "+s1.handBot.number[i]);
				//System.out.println("Rêka user przed:");
				//for(int i=0;i<5;i++)
					//System.out.println(s1.handPlayer.colour[i]+" "+s1.handPlayer.number[i]);
				
				koloryWy = "";
				figuryWy = "";
				for(int i = 0; i < 5; i++) //1-5: kolory komp startowa
					koloryWy += String.valueOf(s1.handBot.colour[i]) + " ";
				for(int i = 0; i < 5; i++) //6-10: kolory gracz startowa
					koloryWy += String.valueOf(s1.handPlayer.colour[i]) + " ";
				for(int i = 0; i < 5; i++) //1-5: figury komp startowa 
					figuryWy += String.valueOf(s1.handBot.number[i]) + " ";
				for(int i = 0; i < 5; i++) //6-10: figury gracz startowa 
					figuryWy += String.valueOf(s1.handPlayer.number[i]) + " ";
			
				StartTheSwap s2 = new StartTheSwap(2,s1.handBot); //komputer odrzuca karty
				s2.run();

				//System.out.println("Odrzucone komp: ");
				for(int i=0;i<5;i++)
				{
					//System.out.println(s2.change[i]);
					odrz += s2.change[i];
				}
				
				JFrame srednia = new JFrame();
				srednia.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
				srednia.setSize(600,400);
				srednia.setVisible(true);
				

				
		        panelGry = new JPanel();
		        srednia.add(panelGry);
		        panelGry.setLayout(new BorderLayout());
		        up = new JPanel();
		        mid = new JPanel();
		        down = new JPanel();
		        panelGry.add(up, BorderLayout.NORTH);
				panelGry.add(mid, BorderLayout.CENTER);
				panelGry.add(down, BorderLayout.SOUTH);
				mid.setLayout(new GridLayout(2,5));
				        
		        //jêzyk polski
				srednia.setTitle("Poker | Rozgrywka");
		        przeOdrz = new JLabel("Przeciwnik odrzuci³ " + odrz); 
		        if(odrz==0 || odrz==5) //poprawnoœæ jêzykowa
		        	przeOdrz.setText(przeOdrz.getText()+" kart.");
		        if(odrz==1)
		        	przeOdrz.setText(przeOdrz.getText()+" kartê.");
		        if(odrz==2 || odrz==3 || odrz==4)
		        	przeOdrz.setText(przeOdrz.getText()+" karty.");
		        odrzuc = new JButton("Odrzuæ karty");
		        
		        //grafiki kart przy wymienianiu
		        for(int i=0; i<5; i++) {
		        	switch(s1.handPlayer.colour[i]) {
			        	case 0:
			        	{
			        		switch(s1.handPlayer.number[i]) {
					        	case 9:
					        		mid.add(pikk9);	
					        		break;	
					        	case 10:
					        		mid.add(pikk10);	
					        		break;	
					        	case 11:
					        		mid.add(pikkW);	
					        		break;	
					        	case 12:
					        		mid.add(pikkQ);	
					        		break;	
					        	case 13:
					        		mid.add(pikkK);	
					        		break;	
					        	case 14:
					        		mid.add(pikkA);	
					        		break;	
			        		}
			        		break;
			        	}
			        	case 1:
			        	{
			        		switch(s1.handPlayer.number[i]) {
					        	case 9:
					        		mid.add(kierr9);	
					        		break;	
					        	case 10:
					        		mid.add(kierr10);	
					        		break;	
					        	case 11:
					        		mid.add(kierrW);	
					        		break;	
					        	case 12:
					        		mid.add(kierrQ);	
					        		break;	
					        	case 13:
					        		mid.add(kierrK);	
					        		break;	
					        	case 14:
					        		mid.add(kierrA);	
					        		break;	
			        		}
			        		break;
			        	}
			        	case 2:
			        	{
			        		switch(s1.handPlayer.number[i]) {
					        	case 9:
					        		mid.add(karoo9);	
					        		break;	
					        	case 10:
					        		mid.add(karoo10);	
					        		break;	
					        	case 11:
					        		mid.add(karooW);	
					        		break;	
					        	case 12:
					        		mid.add(karooQ);	
					        		break;	
					        	case 13:
					        		mid.add(karooK);	
					        		break;	
					        	case 14:
					        		mid.add(karooA);	
					        		break;	
			        		}
			        		break;
			        	}
			        	case 3:
			        	{
			        		switch(s1.handPlayer.number[i]) {
					        	case 9:
					        		mid.add(trefll9);	
					        		break;	
					        	case 10:
					        		mid.add(trefll10);	
					        		break;	
					        	case 11:
					        		mid.add(trefllW);	
					        		break;	
					        	case 12:
					        		mid.add(trefllQ);	
					        		break;	
					        	case 13:
					        		mid.add(trefllK);	
					        		break;	
					        	case 14:
					        		mid.add(trefllA);	
					        		break;	
			        		}
			        		break;
			        	}
		        	}
		        	
		        }
		        
		        
		        
		        odrzKarta1 = new JCheckBox();
		        odrzKarta2 = new JCheckBox();
		        odrzKarta3 = new JCheckBox();
		        odrzKarta4 = new JCheckBox();
		        odrzKarta5 = new JCheckBox();
		        mid.add(odrzKarta1);
		        mid.add(odrzKarta2);
		        mid.add(odrzKarta3);
		        mid.add(odrzKarta4);
		        mid.add(odrzKarta5);
		        
		        down.add(odrzuc);
		        up.add(przeOdrz);

		        odrz = 0;
		       
		        //listenery
		        odrzuc.addActionListener(new ActionListener() { //zakoñczenie rozgrywki
		  			@Override
		  			public void actionPerformed(ActionEvent e) {
		  				int[] changePlayer = new int[5];
		  				if(odrzKarta1.isSelected())
	  						changePlayer[0]=1;
	  					else
	  						changePlayer[0]=0;
		  				if(odrzKarta2.isSelected())
	  						changePlayer[1]=1;
	  					else
	  						changePlayer[1]=0;
		  				if(odrzKarta3.isSelected())
	  						changePlayer[2]=1;
	  					else
	  						changePlayer[2]=0;
		  				if(odrzKarta4.isSelected())
	  						changePlayer[3]=1;
	  					else
	  						changePlayer[3]=0;
		  				if(odrzKarta5.isSelected())
	  						changePlayer[4]=1;
	  					else
	  						changePlayer[4]=0;

		  				//System.out.println("Odrzucone user: ");
		  				//for(int i=0;i<5;i++)
							//System.out.println(changePlayer[i]);
		  				
		  				StartTheScore s3 = new StartTheScore(s1.handPlayer, s1.handBot, changePlayer, s2.change);
		  				s3.run();
		  				
		  				for(int i = 0; i < 5; i++) //11-15: kolory komp koñcowa
							koloryWy += String.valueOf(s1.handBot.colour[i]) + " ";
						for(int i = 0; i < 5; i++) //11-15: figury komp koñcowa
							figuryWy += String.valueOf(s1.handBot.number[i]) + " ";
						for(int i = 0; i < 5; i++) //16-20: kolory gracz koñcowa
							koloryWy += String.valueOf(s1.handPlayer.colour[i]) + " ";
						for(int i = 0; i < 5; i++) //16-20: figury gracz koñcowa
							figuryWy += String.valueOf(s1.handPlayer.number[i]) + " ";
						
		  				memory[s1.handPlayer.Score()]++; //dodanie uk³adu do stats
		  				ukl1 = s1.handBot.Score();
		  				ukl2 = s1.handPlayer.Score();
		  				wynik = s3.score;
		  				
		  				tekstWy = "";
						tekstWy += koloryWy; //1-20
						tekstWy += figuryWy; //21-40
						tekstWy += String.valueOf(ukl1) + " "; //41: typ uk³adu komp koñcowa
						tekstWy += String.valueOf(ukl2) + " "; //42: typ uk³adu gracz koñcowa
						tekstWy += String.valueOf(wynik)+ " "; //43: kto wygra³
						
		  				Rozgrywka r2 = new Rozgrywka(tekstWy, "brak"); //okienko z rezultatem rozgrywki
		  				
		  				//System.out.println("SCORE: "+s1.handPlayer.Score());
		  				switch(s3.score) {
			  				case 1:
			  					money += 20;
			  					winsS++;
			  					System.out.println("Koniec rozgrywki - wygrana\n");
			  					wins[0]++;
			  					break;
			  				case 0:
			  					money -= 20;
			  					losesS++;
			  					System.out.println("Koniec rozgrywki - przegrana\n");
			  					loses[0]++;
			  					break;
			  				case 2:
			  					drawsS++;
			  					System.out.println("Koniec rozgrywki - remis\n");
			  					draws[0]++;
			  					break;
		  				}
		  				gamesS = winsS+losesS+drawsS;
		  				memory[10] = gamesS;
		  				moneyCount.setText(String.format("%d", money));
		  				winsSession.setText(String.format("%d", winsS));
		  				losesSession.setText(String.format("%d", losesS));
		  				drawsSession.setText(String.format("%d", drawsS));
		  				winRatioS = (double) 100*winsS/gamesS;
		  				winRatioSession.setText(winRatioS + "%");
		  				String infoS1 = "Brak uk³adu "+memory[1];
		  				String infoS2 = "Para        "+memory[2];
		  				String infoS3 = "2 Pary      "+memory[3];
		  				String infoS4 = "Trójka      "+memory[4];
		  				String infoS5 = "Streat      "+memory[5];
		  				String infoS6 = "Full        "+memory[6];
		  				String infoS7 = "Kolor       "+memory[7];
		  				String infoS8 = "Kareta      "+memory[8];
		  				String infoS9 = "Poker       "+memory[9];
		  				String infoS10= "£¹cznie     "+memory[10];
		  				infS1.setText(infoS1);
		  				infS2.setText(infoS2);
		  				infS3.setText(infoS3);
		  				infS4.setText(infoS4);
		  				infS5.setText(infoS5);
		  				infS6.setText(infoS6);
		  				infS7.setText(infoS7);
		  				infS8.setText(infoS8);
		  				infS9.setText(infoS9);
		  				infS10.setText(infoS10);
		  				srednia.setVisible(false);
		  				if(money<=0) {
		  					System.out.println("Nie masz pieniêdzy! Przegra³eœ!");
		  					System.exit(0);
		  				}
		  					
		  			}
		  		});		
			}	
		});	
		
		lvl3.addActionListener(new ActionListener() { //--------------------rozpoczecie gry (trudny)
			@Override
			public void actionPerformed(ActionEvent e) {
				StartTheDraw s1 = new StartTheDraw(); //start the draw
				s1.run();
				
				//System.out.println("Rêka bot przed:");
				//for(int i=0;i<5;i++)
					//System.out.println(s1.handBot.colour[i]+" "+s1.handBot.number[i]);
				//System.out.println("Rêka user przed:");
				//for(int i=0;i<5;i++)
					//System.out.println(s1.handPlayer.colour[i]+" "+s1.handPlayer.number[i]);
				
				koloryWy = "";
				figuryWy = "";
				for(int i = 0; i < 5; i++) //1-5: kolory komp startowa
					koloryWy += String.valueOf(s1.handBot.colour[i]) + " ";
				for(int i = 0; i < 5; i++) //6-10: kolory gracz startowa
					koloryWy += String.valueOf(s1.handPlayer.colour[i]) + " ";
				for(int i = 0; i < 5; i++) //1-5: figury komp startowa 
					figuryWy += String.valueOf(s1.handBot.number[i]) + " ";
				for(int i = 0; i < 5; i++) //6-10: figury gracz startowa 
					figuryWy += String.valueOf(s1.handPlayer.number[i]) + " ";
			
				
				
				JFrame trudna = new JFrame();
				trudna.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
				trudna.setSize(600,400);
				trudna.setVisible(true);
				
				StartTheSwap s2 = new StartTheSwap(3,s1.handBot); //komputer odrzuca karty
					s2.run();
				
		        panelGry = new JPanel();
		        trudna.add(panelGry);
		        panelGry.setLayout(new BorderLayout());
		        up = new JPanel();
		        mid = new JPanel();
		        down = new JPanel();
		        panelGry.add(up, BorderLayout.NORTH);
				panelGry.add(mid, BorderLayout.CENTER);
				panelGry.add(down, BorderLayout.SOUTH);
				mid.setLayout(new GridLayout(2,5));
				        
		        //jêzyk polski
				trudna.setTitle("Poker | Rozgrywka");
				przeOdrz = new JLabel("Przeciwnik odrzuci³ " + odrz); 
		        if(odrz==0 || odrz==5) //poprawnoœæ jêzykowa
		        	przeOdrz.setText(przeOdrz.getText()+" kart.");
		        if(odrz==1)
		        	przeOdrz.setText(przeOdrz.getText()+" kartê.");
		        if(odrz==2 || odrz==3 || odrz==4)
		        	przeOdrz.setText(przeOdrz.getText()+" karty.");
		        odrzuc = new JButton("Odrzuæ karty"); 
		        
		        //grafiki kart przy wymienianiu
		        for(int i=0; i<5; i++) {
		        	switch(s1.handPlayer.colour[i]) {
			        	case 0:
			        	{
			        		switch(s1.handPlayer.number[i]) {
					        	case 9:
					        		mid.add(pikk9);	
					        		break;	
					        	case 10:
					        		mid.add(pikk10);	
					        		break;	
					        	case 11:
					        		mid.add(pikkW);	
					        		break;	
					        	case 12:
					        		mid.add(pikkQ);	
					        		break;	
					        	case 13:
					        		mid.add(pikkK);	
					        		break;	
					        	case 14:
					        		mid.add(pikkA);	
					        		break;	
			        		}
			        		break;
			        	}
			        	case 1:
			        	{
			        		switch(s1.handPlayer.number[i]) {
					        	case 9:
					        		mid.add(kierr9);	
					        		break;	
					        	case 10:
					        		mid.add(kierr10);	
					        		break;	
					        	case 11:
					        		mid.add(kierrW);	
					        		break;	
					        	case 12:
					        		mid.add(kierrQ);	
					        		break;	
					        	case 13:
					        		mid.add(kierrK);	
					        		break;	
					        	case 14:
					        		mid.add(kierrA);	
					        		break;	
			        		}
			        		break;
			        	}
			        	case 2:
			        	{
			        		switch(s1.handPlayer.number[i]) {
					        	case 9:
					        		mid.add(karoo9);	
					        		break;	
					        	case 10:
					        		mid.add(karoo10);	
					        		break;	
					        	case 11:
					        		mid.add(karooW);	
					        		break;	
					        	case 12:
					        		mid.add(karooQ);	
					        		break;	
					        	case 13:
					        		mid.add(karooK);	
					        		break;	
					        	case 14:
					        		mid.add(karooA);	
					        		break;	
			        		}
			        		break;
			        	}
			        	case 3:
			        	{
			        		switch(s1.handPlayer.number[i]) {
					        	case 9:
					        		mid.add(trefll9);	
					        		break;	
					        	case 10:
					        		mid.add(trefll10);	
					        		break;	
					        	case 11:
					        		mid.add(trefllW);	
					        		break;	
					        	case 12:
					        		mid.add(trefllQ);	
					        		break;	
					        	case 13:
					        		mid.add(trefllK);	
					        		break;	
					        	case 14:
					        		mid.add(trefllA);	
					        		break;	
			        		}
			        		break;
			        	}
		        	}
		        	
		        }
		        
		        
		        
		        odrzKarta1 = new JCheckBox();
		        odrzKarta2 = new JCheckBox();
		        odrzKarta3 = new JCheckBox();
		        odrzKarta4 = new JCheckBox();
		        odrzKarta5 = new JCheckBox();
		        mid.add(odrzKarta1);
		        mid.add(odrzKarta2);
		        mid.add(odrzKarta3);
		        mid.add(odrzKarta4);
		        mid.add(odrzKarta5);
		        
		        down.add(odrzuc);
		        up.add(przeOdrz);
		        
		       

				//System.out.println("Odrzucone komp: ");
				for(int i=0;i<5;i++)
				{
					//System.out.println(s2.change[i]);
					odrz += s2.change[i];
				}
				
				
				
		        odrz = 0;
		       
		        //listenery
		        odrzuc.addActionListener(new ActionListener() { //zakoñczenie rozgrywki
		  			@Override
		  			public void actionPerformed(ActionEvent e) {
		  				int[] changePlayer = new int[5];
		  				if(odrzKarta1.isSelected())
	  						changePlayer[0]=1;
	  					else
	  						changePlayer[0]=0;
		  				if(odrzKarta2.isSelected())
	  						changePlayer[1]=1;
	  					else
	  						changePlayer[1]=0;
		  				if(odrzKarta3.isSelected())
	  						changePlayer[2]=1;
	  					else
	  						changePlayer[2]=0;
		  				if(odrzKarta4.isSelected())
	  						changePlayer[3]=1;
	  					else
	  						changePlayer[3]=0;
		  				if(odrzKarta5.isSelected())
	  						changePlayer[4]=1;
	  					else
	  						changePlayer[4]=0;

		  				//System.out.println("Odrzucone user: ");
		  				//for(int i=0;i<5;i++)
							//System.out.println(changePlayer[i]);
		  				
		  				StartTheScore s3 = new StartTheScore(s1.handPlayer, s1.handBot, changePlayer, s2.change);
		  				s3.run();
		  				
		  				for(int i = 0; i < 5; i++) //11-15: kolory komp koñcowa
							koloryWy += String.valueOf(s1.handBot.colour[i]) + " ";
						for(int i = 0; i < 5; i++) //11-15: figury komp koñcowa
							figuryWy += String.valueOf(s1.handBot.number[i]) + " ";
						for(int i = 0; i < 5; i++) //16-20: kolory gracz koñcowa
							koloryWy += String.valueOf(s1.handPlayer.colour[i]) + " ";
						for(int i = 0; i < 5; i++) //16-20: figury gracz koñcowa
							figuryWy += String.valueOf(s1.handPlayer.number[i]) + " ";
						
		  				memory[s1.handPlayer.Score()]++; //dodanie uk³adu do stats
		  				ukl1 = s1.handBot.Score();
		  				ukl2 = s1.handPlayer.Score();
		  				wynik = s3.score;
		  				
		  				tekstWy = "";
						tekstWy += koloryWy; //1-20
						tekstWy += figuryWy; //21-40
						tekstWy += String.valueOf(ukl1) + " "; //41: typ uk³adu komp koñcowa
						tekstWy += String.valueOf(ukl2) + " "; //42: typ uk³adu gracz koñcowa
						tekstWy += String.valueOf(wynik)+ " "; //43: kto wygra³
						
		  				Rozgrywka r2 = new Rozgrywka(tekstWy, "brak"); //okienko z rezultatem rozgrywki
		  				
		  				//System.out.println("SCORE: "+s1.handPlayer.Score());
		  				switch(s3.score) {
			  				case 1:
			  					money += 10;
			  					winsS++;
			  					System.out.println("Koniec rozgrywki - wygrana\n");
			  					wins[0]++;
			  					break;
			  				case 0:
			  					money -= 10;
			  					losesS++;
			  					System.out.println("Koniec rozgrywki - przegrana\n");
			  					loses[0]++;
			  					break;
			  				case 2:
			  					drawsS++;
			  					System.out.println("Koniec rozgrywki - remis\n");
			  					draws[0]++;
			  					break;
		  				}
		  				gamesS = winsS+losesS+drawsS;
		  				memory[10] = gamesS;
		  				moneyCount.setText(String.format("%d", money));
		  				winsSession.setText(String.format("%d", winsS));
		  				losesSession.setText(String.format("%d", losesS));
		  				drawsSession.setText(String.format("%d", drawsS));
		  				winRatioS = (double) 100*winsS/gamesS;
		  				winRatioSession.setText(winRatioS + "%");
		  				String infoS1 = "Brak uk³adu "+memory[1];
		  				String infoS2 = "Para        "+memory[2];
		  				String infoS3 = "2 Pary      "+memory[3];
		  				String infoS4 = "Trójka      "+memory[4];
		  				String infoS5 = "Streat      "+memory[5];
		  				String infoS6 = "Full        "+memory[6];
		  				String infoS7 = "Kolor       "+memory[7];
		  				String infoS8 = "Kareta      "+memory[8];
		  				String infoS9 = "Poker       "+memory[9];
		  				String infoS10= "£¹cznie     "+memory[10];
		  				infS1.setText(infoS1);
		  				infS2.setText(infoS2);
		  				infS3.setText(infoS3);
		  				infS4.setText(infoS4);
		  				infS5.setText(infoS5);
		  				infS6.setText(infoS6);
		  				infS7.setText(infoS7);
		  				infS8.setText(infoS8);
		  				infS9.setText(infoS9);
		  				infS10.setText(infoS10);
		  				trudna.setVisible(false);
		  				if(money<=0) {
		  					System.out.println("Nie masz pieniêdzy! Przegra³eœ!");
		  					System.exit(0);
		  				}
		  					
		  			}
		  		});		
			}	
		});	
		
		zapisz.addActionListener(new ActionListener() { //zapisz ostatni¹ rozegran¹ grê
			@Override
			public void actionPerformed(ActionEvent e) {
				JFileChooser chooserZ = new JFileChooser();
				chooserZ.setDialogTitle("Wybierz plik");
				chooserZ.showDialog(null, "Wybierz");
				File file = chooserZ.getSelectedFile();
				try { 
					FileWriter plikWy = new FileWriter(file + ".txt");
					
					plikWy.write(tekstWy);
					plikWy.close();
				} catch (IOException d) {}
			}
		});
		
		importuj.addActionListener(new ActionListener() { //wczytaj rozgrywkê
			@Override
			public void actionPerformed(ActionEvent e) {
				JFileChooser chooserW = new JFileChooser();
				chooserW.setDialogTitle("Wybierz plik");
				chooserW.showDialog(null, "Wybierz");
				try {
					//wczytanie pliku w formacie 43 liczby po spacji (jak w zapisz)
					String fileName = chooserW.getSelectedFile().getAbsolutePath();
					JTextField tekstWe = new JTextField();
					tekstWe.read(new FileReader(chooserW.getSelectedFile().getAbsolutePath()), null);
					//System.out.println(tekstWe.getText());
					//System.out.println(fileName);
					Rozgrywka r1 = new Rozgrywka(tekstWe.getText(), fileName);
					
				} catch(IOException ex) {}
			}
		});
		
		mute.addActionListener(new ActionListener() { //mute
			@Override
			public void actionPerformed(ActionEvent e) {
				regulacjaGlosnosci.setValue(0);
			}
		});	
		wyjscie.addActionListener(new ActionListener() { //wyjscie z programu
			@Override
			public void actionPerformed(ActionEvent e) {
				System.exit(0);
			}
		});	
	}
	
	public ImagePanel robKarte(BufferedImage karta, String nazwaKarty) {
		Graphics g1 = getGraphics();
		File inputFile1 = new File(nazwaKarty + ".png");
        try {
        	karta = ImageIO.read(inputFile1);
        } catch(IOException ex) {
            System.out.println(ex.getMessage());
        }
 
        return new ImagePanel(karta);
	}
	
	public static void main(String[] args) { //----------main
		Poker poker = new Poker();
		poker.setVisible(true);
	}

}
