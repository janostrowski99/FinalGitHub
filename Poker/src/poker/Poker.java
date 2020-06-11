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
import java.text.DecimalFormat;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

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
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

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
 * zapisywanie statystyk zwyciêstw/... do pliku txt - automatyczne importowanie przy ka¿dym w³¹czeniu gry
 * wersja anglojêzyczna (polskie napisy maj¹ komentarze w kodzie, wiêc ³atwo)
 * ulepszenie waluty gry (premiowane winstreaki +10% monet za ka¿de zwyciêctwo z rzêdu)
 * mo¿liwoœc zakupu innego wygl¹du kart
 * 
 * Do zrobienia:
 * 1 dzwieki
 * 2 bot insane - analiza gry gracza 
 */
public class Poker extends JFrame {

	private static final long serialVersionUID = 1L;
	
	//elementy wizualne
	JTabbedPane tabbedPane;
	JPanel glownyPanel, statsPanel;
	JPanel gora, lewa, srodek, prawa, dol, glosnoscPanel;
	JLabel napisGlowny, poziomyTrudnosci;
	JButton lvl1, lvl2, lvl3, lvl4, sklep, importuj, zapisz, wyjscie;
	JSlider jezykSlider;
	JLabel pl, ang;
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
	static final int SLIDER_MAX = 1;
	static final int SLIDER_INIT = 0;
	int money; //zachowywane przy wy³¹czaniu gry
	int[] wins = new int[4]; //0-³atwy; 1-œredni; 2-trudny; 3-³¹cznie; zachowywane przy wy³¹czaniu gry
	int[] loses = new int[4];
	int[] draws = new int[4];
	int[] games = new int[4];
	double[] winRatio = new double[4];
	int[] memory = new int[20]; //statystyki sesji
	int winsS = 0; //wy³¹cznie w tej sesji
	int losesS = 0;
	int drawsS = 0;
	int gamesS = 0;
	double winRatioS = 0.0;
	int odrz = 0; //ile kart odrzucil przeciwnik
	String koloryWy, figuryWy, tekstWy;
	String[] daneTemp;
	int ukl1, ukl2, wynik; //temp uk³adów i kto wygra³ do zapisania do pliku
	int jezyk = 0; //0 - polski, 1 - angielski
	int streak = 0; //win streak
	int awers = 0; //awers
	private static DecimalFormat df = new DecimalFormat("0.00"); //wyœwietlanie winstreaka 2 decimal places
	
	public Poker() throws HeadlessException { //---------------------------------konstruktor
		this.setDefaultCloseOperation(EXIT_ON_CLOSE);
		this.setSize(850,400);
		this.setLayout(new BorderLayout());
		this.setTitle("Poker | Menu g³ówne");
		
		File fileTemp = new File("temp.txt"); 
		if(!fileTemp.exists()) { //pierwsze wl¹czenie gry ever
			money = 100;
			for(int i=1;i<11;i++) {
				memory[i]=0;
			}
			FileWriter plikWyjs;
			try {
				plikWyjs = new FileWriter(fileTemp);
				//if(!file4.exists())
					plikWyjs.write("100 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0"); //100 monet i wyzerowane statystyki
				plikWyjs.close();
			} catch (IOException e1) {
				e1.printStackTrace();
			}
		}
		else { //import statystyk globalnych
			
			try {
				JTextField tekstWej = new JTextField();
				tekstWej.read(new FileReader(fileTemp), null);
					//System.out.println(tekstWej.getText());
				String dane2 = tekstWej.getText();
				daneTemp = dane2.split("\\s");
			} catch (IOException e1) {
				e1.printStackTrace();
			}
			//
			money = Integer.parseInt(daneTemp[0]);
			for(int i=0; i<3; i++) {
				wins[i] = Integer.parseInt(daneTemp[3*i+1]);
				loses[i] = Integer.parseInt(daneTemp[3*i+2]);
				draws[i] = Integer.parseInt(daneTemp[3*i+3]);
			}
			for(int i=0; i<10; i++) {
				memory[i] = Integer.parseInt(daneTemp[i+13]);
			}
			streak = Integer.parseInt(daneTemp[24]);
			awers = Integer.parseInt(daneTemp[25]);
			
			wins[3] = wins[0]+wins[1]+wins[2];
			loses[3] = loses[0]+loses[1]+loses[2];
			draws[3] = draws[0]+draws[1]+draws[2];
			for(int i=0; i<4; i++) {
				games[i] = wins[i]+loses[i]+draws[i];
				winRatio[i] = (double) 100*wins[i]/games[i];
			}
			memory[10] = memory[0];
			for(int i=1; i<10; i++)
				memory[10] += memory[i];
		}
		
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
		napisGlowny = new JLabel("POKER");
		poziomyTrudnosci = new JLabel("Poziom trudnoœci");
		lvl1 = new JButton("£atwy");
		lvl2 = new JButton("Œredni");
		lvl3 = new JButton("Trudny (UWAGA! d³ugi czas kalkulacji)");
		lvl4 = new JButton("Eksperymentalny (d³ugi czas kalkulacji)");
		sklep = new JButton("Kup inny awers kart za 150 monet");
		sklep.setBounds(0, 0, 500, 500);
		importuj = new JButton("Importuj partiê");
		zapisz = new JButton("Zapisz ostatnio zagran¹ partiê");
		wyjscie = new JButton("Wyjœcie");
		
		jezykSlider = new JSlider(JSlider.HORIZONTAL, SLIDER_MIN, SLIDER_MAX, SLIDER_INIT);
		jezykSlider.addChangeListener(new SliderChangeListener());
		moneyCount = new JTextField(String.format("%d", money));
		winsSession = new JTextField(String.format("%d", winsS));
		losesSession = new JTextField(String.format("%d", losesS));
		drawsSession = new JTextField(String.format("%d", drawsS));
		winRatioSession = new JTextField("NDF");
		pl = new JLabel("  Polski");
		ang = new JLabel("English  ");
		
		//wstawianie elemenów wizualnych
		this.add(tabbedPane);
		tabbedPane.addTab("Menu G³ówne", glownyPanel);
		tabbedPane.addTab("Statystyki", statsPanel);
		
		glownyPanel.setLayout(new BorderLayout());
		glownyPanel.add(gora, BorderLayout.NORTH);
		glownyPanel.add(lewa, BorderLayout.WEST);
		glownyPanel.add(srodek, BorderLayout.CENTER);
		glownyPanel.add(prawa, BorderLayout.EAST);
		glownyPanel.add(dol, BorderLayout.SOUTH);
		lewa.setLayout(new GridLayout(5,1));
		srodek.setLayout(new GridLayout(1,1));
		prawa.setLayout(new GridLayout(4,1));
		dol.setLayout(new GridLayout(1,10));
		gora.add(napisGlowny);
		lewa.add(poziomyTrudnosci);
		lewa.add(lvl1);
		lewa.add(lvl2);
		lewa.add(lvl3);
		lewa.add(lvl4);
		srodek.add(sklep);
		prawa.add(glosnoscPanel);
		glosnoscPanel.add(pl);
		glosnoscPanel.add(jezykSlider);
		glosnoscPanel.add(ang);
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
		
		if(awers==0) {
			pikk9 = robKarte(pik9,"pik9"); 		pikk10 = robKarte(pik10,"pik10"); 		pikkW = robKarte(pikW,"pikW"); 
			pikkQ = robKarte(pikQ,"pikQ"); 		pikkK = robKarte(pikK,"pikK"); 			pikkA = robKarte(pikA,"pikA"); 
			kierr9 = robKarte(kier9,"kier9"); 	kierr10 = robKarte(kier10,"kier10");	kierrW = robKarte(kierW,"kierW");
			kierrQ = robKarte(kierQ,"kierQ"); 	kierrK = robKarte(kierK,"kierK"); 		kierrA = robKarte(kierA,"kierA");
			karoo9 = robKarte(karo9,"karo9"); 	karoo10 = robKarte(karo10,"karo10"); 	karooW = robKarte(karoW,"karoW");
			karooQ = robKarte(karoQ,"karoQ"); 	karooK = robKarte(karoK,"karoK"); 		karooA = robKarte(karoA,"karoA");
			trefll9 = robKarte(trefl9,"trefl9");trefll10 = robKarte(trefl10,"trefl10"); trefllW = robKarte(treflW,"treflW"); 
			trefllQ = robKarte(treflQ,"treflQ");trefllK = robKarte(treflK,"treflK"); 	trefllA = robKarte(treflA,"treflA"); 
		}
		if(awers==1) {
			pikk9 = robKarte2(pik9,"pik9"); 	pikk10 = robKarte2(pik10,"pik10"); 		pikkW = robKarte2(pikW,"pikW"); 
			pikkQ = robKarte2(pikQ,"pikQ"); 	pikkK = robKarte2(pikK,"pikK"); 		pikkA = robKarte2(pikA,"pikA"); 
			kierr9 = robKarte2(kier9,"kier9"); 	kierr10 = robKarte2(kier10,"kier10");	kierrW = robKarte2(kierW,"kierW");
			kierrQ = robKarte2(kierQ,"kierQ"); 	kierrK = robKarte2(kierK,"kierK"); 		kierrA = robKarte2(kierA,"kierA");
			karoo9 = robKarte2(karo9,"karo9"); 	karoo10 = robKarte2(karo10,"karo10"); 	karooW = robKarte2(karoW,"karoW");
			karooQ = robKarte2(karoQ,"karoQ"); 	karooK = robKarte2(karoK,"karoK"); 		karooA = robKarte2(karoA,"karoA");
			trefll9 = robKarte2(trefl9,"trefl9");trefll10 = robKarte2(trefl10,"trefl10");trefllW = robKarte2(treflW,"treflW"); 
			trefllQ = robKarte2(treflQ,"treflQ");trefllK = robKarte2(treflK,"treflK"); 	trefllA = robKarte2(treflA,"treflA"); 
		}
		
		//panel statystyk
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
		infS0 = new JLabel("Twoje uk³ady karciane:");
		infS1 = new JLabel("Brak uk³adu "+memory[1]);
		infS2 = new JLabel("Para        "+memory[2]);
		infS3 = new JLabel("2 Pary      "+memory[3]);
		infS4 = new JLabel("Trójka      "+memory[4]);
		infS5 = new JLabel("Streat      "+memory[5]);
		infS6 = new JLabel("Full        "+memory[6]);
		infS7 = new JLabel("Kolor       "+memory[7]);
		infS8 = new JLabel("Kareta      "+memory[8]);
		infS9 = new JLabel("Poker       "+memory[9]);
		infS10 = new JLabel("£¹cznie     "+memory[10]);
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
		infA0 = new JLabel("Poziom: ³atwy");
		infA1 = new JLabel("Zwyciêstwa   "+wins[0]);
		infA2 = new JLabel("Przegrane    "+loses[0]);
		infA3 = new JLabel("Remisy       "+draws[0]);
		infA4 = new JLabel("£¹cznie      "+games[0]);
		infA5 = new JLabel("% zwyciêstw  "+df.format(winRatio[0]));
		stats1.add(infA0);
		stats1.add(infA1);
		stats1.add(infA2);
		stats1.add(infA3);
		stats1.add(infA4);
		stats1.add(infA5);
		infB0 = new JLabel("Poziom: œredni");
		infB1 = new JLabel("Zwyciêstwa   "+wins[1]);
		infB2 = new JLabel("Przegrane    "+loses[1]);
		infB3 = new JLabel("Remisy       "+draws[1]);
		infB4 = new JLabel("£¹cznie      "+games[1]);
		infB5 = new JLabel("% zwyciêstw  "+df.format(winRatio[1]));
		stats2.add(infB0);
		stats2.add(infB1);
		stats2.add(infB2);
		stats2.add(infB3);
		stats2.add(infB4);
		stats2.add(infB5);
		infC0 = new JLabel("Poziom: trudny");
		infC1 = new JLabel("Zwyciêstwa   "+wins[2]);
		infC2 = new JLabel("Przegrane    "+loses[2]);
		infC3 = new JLabel("Remisy       "+draws[2]);
		infC4 = new JLabel("£¹cznie      "+games[2]);
		infC5 = new JLabel("% zwyciêstw  "+df.format(winRatio[2]));
		stats3.add(infC0);
		stats3.add(infC1);
		stats3.add(infC2);
		stats3.add(infC3);
		stats3.add(infC4);
		stats3.add(infC5);
		infD0 = new JLabel("£¹cznie");
		infD1 = new JLabel("Zwyciêstwa   "+wins[3]);
		infD2 = new JLabel("Przegrane    "+loses[3]);
		infD3 = new JLabel("Remisy       "+draws[3]);
		infD4 = new JLabel("£¹cznie      "+games[3]);
		infD5 = new JLabel("% zwyciêstw  "+df.format(winRatio[3]));
		statsAll.add(infD0);
		statsAll.add(infD1);
		statsAll.add(infD2);
		statsAll.add(infD3);
		statsAll.add(infD4);
		statsAll.add(infD5);
		
		//klasa do grania na 3 poziomach trudnoœci
		class RozrywkaZagniezdzona  implements Runnable {
			int difficulty;
			
			public RozrywkaZagniezdzona(int d) {
				difficulty = d;
			}
				
			@Override
			public void run() {
				
				JFrame okno = new JFrame();
				okno.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
				okno.setSize(600,400);
				okno.setVisible(true);
				
				StartTheDraw s1 = new StartTheDraw(); //start the draw
				s1.run();
				
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
			
				StartTheSwap s2 = new StartTheSwap(difficulty, s1.handBot); //komputer odrzuca karty w zale¿noœci od trudnoœci
				s2.run();

				for(int i=0;i<5;i++)
					odrz += s2.change[i];

		        panelGry = new JPanel();
		        okno.add(panelGry);
		        panelGry.setLayout(new BorderLayout());
		        up = new JPanel();
		        mid = new JPanel();
		        down = new JPanel();
		        panelGry.add(up, BorderLayout.NORTH);
				panelGry.add(mid, BorderLayout.CENTER);
				panelGry.add(down, BorderLayout.SOUTH);
				mid.setLayout(new GridLayout(2,5));
				        
		        if(jezyk==0) {
		        	okno.setTitle("Poker | Rozgrywka");
			        przeOdrz = new JLabel("Przeciwnik odrzuci³ " + odrz); 
			        if(odrz==0 || odrz==5) //poprawnoœæ jêzykowa
			        	przeOdrz.setText(przeOdrz.getText()+" kart.");
			        if(odrz==1)
			        	przeOdrz.setText(przeOdrz.getText()+" kartê.");
			        if(odrz==2 || odrz==3 || odrz==4)
			        	przeOdrz.setText(przeOdrz.getText()+" karty.");
			        odrzuc = new JButton("Odrzuæ karty");
		        }
		        
		        if(jezyk==1) {
		        	okno.setTitle("Poker | Play");
			        przeOdrz = new JLabel("Opponent thrown " + odrz); 	
			        if(odrz==1)
			        	przeOdrz.setText(przeOdrz.getText()+" card.");
			        else
			        	przeOdrz.setText(przeOdrz.getText()+" cards.");
			        odrzuc = new JButton("Throw cards");
		        }
				
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
		        panelGry.revalidate();
		        panelGry.repaint();

		        odrz = 0;
		       
		        //listener
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
						
		  				Rozgrywka r2 = new Rozgrywka(tekstWy, "brak", jezyk, awers); //okienko z rezultatem rozgrywki
		  				
		  				switch(s3.score) {
			  				case 1:
			  					money += difficulty*(10+streak*difficulty); //easy 10+s; normal 20+4s; hard 30+9s; exp 40+16s;   s=streak
			  					winsS++;
			  					System.out.println("Koniec rozgrywki - wygrana " + difficulty*(10+streak*difficulty) + " monet");
			  					streak++;
			  					System.out.println("Wygrywasz " + streak + " grê z rzêdu!");
			  					if(difficulty==4)
			  						wins[difficulty-2]++; //eksperymentalny i trudny - wspólna statystyka
			  					else
			  						wins[difficulty-1]++;
			  					break;
			  				case 0:
			  					streak = 0;
			  					money -= difficulty*10;
			  					losesS++;
			  					System.out.println("Koniec rozgrywki - przegrana " + difficulty*10 + " monet");
			  					if(difficulty==4)
			  						loses[difficulty-2]++;
			  					else
			  						loses[difficulty-1]++;
			  					break;
			  				case 2:
			  					streak = 0;
			  					drawsS++;
			  					System.out.println("Koniec rozgrywki - remis\n");
			  					if(difficulty==4)
			  						draws[difficulty-2]++;
			  					else
			  						draws[difficulty-1]++;
			  					break;
		  				}
		  				
		  				memory[10] = memory[0];
		  				for(int i=1; i<10; i++)
		  					memory[10] += memory[i];
		  				
		  				gamesS = winsS+losesS+drawsS;
		  				moneyCount.setText(String.format("%d", money));
		  				winsSession.setText(String.format("%d", winsS));
		  				losesSession.setText(String.format("%d", losesS));
		  				drawsSession.setText(String.format("%d", drawsS));
		  				winRatioS = (double) 100*winsS/gamesS;
		  				winRatioSession.setText(winRatioS + "%");
		  				
		  				wins[3] = wins[0]+wins[1]+wins[2];
		  				loses[3] = loses[0]+loses[1]+loses[2];
		  				draws[3] = draws[0]+draws[1]+draws[2];
		  				for(int i=0; i<4; i++) {
		  					games[i] = wins[i]+loses[i]+draws[i];
		  					winRatio[i] = (double) 100*wins[i]/games[i];
		  				}
		  		
		  				
		  				infS1.setText("Brak uk³adu "+memory[1]);
		  				infS2.setText("Para        "+memory[2]);
		  				infS3.setText("2 Pary      "+memory[3]);
		  				infS4.setText("Trójka      "+memory[4]);
		  				infS5.setText("Streat      "+memory[5]);
		  				infS6.setText("Full        "+memory[6]);
		  				infS7.setText("Kolor       "+memory[7]);
		  				infS8.setText("Kareta      "+memory[8]);
		  				infS9.setText("Poker       "+memory[9]);
		  				infS10.setText("£¹cznie     "+memory[10]);
		  				infD0.setText("£¹cznie");
		  				infD1.setText("Zwyciêstwa   "+wins[3]);
		  				infD2.setText("Przegrane    "+loses[3]);
		  				infD3.setText("Remisy       "+draws[3]);
		  				infD4.setText("£¹cznie      "+games[3]);
		  				infD5.setText("% zwyciêstw  "+df.format(winRatio[3]));
		  				infA0.setText("Poziom: ³atwy");
		  				infA1.setText("Zwyciêstwa   "+wins[0]);
		  				infA2.setText("Przegrane    "+loses[0]);
		  				infA3.setText("Remisy       "+draws[0]);
		  				infA4.setText("£¹cznie      "+games[0]);
		  				infA5.setText("% zwyciêstw  "+df.format(winRatio[0]));
		  				infB0.setText("Poziom: œredni");
		  				infB1.setText("Zwyciêstwa   "+wins[1]);
		  				infB2.setText("Przegrane    "+loses[1]);
		  				infB3.setText("Remisy       "+draws[1]);
		  				infB4.setText("£¹cznie      "+games[1]);
		  				infB5.setText("% zwyciêstw  "+df.format(winRatio[1]));
		  				infC0.setText("Poziom: trudny");
		  				infC1.setText("Zwyciêstwa   "+wins[2]);
		  				infC2.setText("Przegrane    "+loses[2]);
		  				infC3.setText("Remisy       "+draws[2]);
		  				infC4.setText("£¹cznie      "+games[2]);
		  				infC5.setText("% zwyciêstw  "+df.format(winRatio[2]));
		  				
		  				okno.setVisible(false);
		  				
						String tekstWyj = "";
						tekstWyj += money + " ";
						for(int i=0; i<4; i++) {
							tekstWyj += wins[i] + " ";
							tekstWyj += loses[i] + " ";
							tekstWyj += draws[i] + " ";
		  				}
		  				for(int i=0; i<11; i++) {
		  					tekstWyj += memory[i] + " ";
		  				}
		  				tekstWyj += streak + " ";
		  				tekstWyj += awers;
		  				
						FileWriter plikWyj;
						try {
							plikWyj = new FileWriter(fileTemp);
							plikWyj.write(tekstWyj);
							plikWyj.close();
						} catch (IOException e1) {
							e1.printStackTrace();
						}
						
		  				if(money<0) {
		  					System.out.println("Nie masz pieniêdzy! Przegra³eœ!");
		  					FileWriter plikWyjs;
		  					try {
		  						plikWyjs = new FileWriter(fileTemp);
		  						plikWyjs.write("100 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0");
		  						plikWyjs.close();
		  					} catch (IOException e1) {
		  						e1.printStackTrace();
		  					}
		  					System.exit(0);
		  				}
		  					
		  			}
		  		});		
			}	
		}

		//listenery
		lvl1.addActionListener(new ActionListener() { //--------------------rozpoczecie gry (³atwy)
			@Override
			public void actionPerformed(ActionEvent e){
				
				ExecutorService exec = Executors.newFixedThreadPool(2);
				RozrywkaZagniezdzona bolek = new RozrywkaZagniezdzona(1);
				exec.execute(bolek);
				
			}	
		});	
		
		lvl2.addActionListener(new ActionListener() { //--------------------rozpoczecie gry (œredni)
			@Override
			public void actionPerformed(ActionEvent e){
				
				ExecutorService exec = Executors.newFixedThreadPool(2);
				RozrywkaZagniezdzona bolek = new RozrywkaZagniezdzona(2);
				exec.execute(bolek);
				
			}	
		});	

		lvl3.addActionListener(new ActionListener() { //--------------------rozpoczecie gry (trudny)
			@Override
			public void actionPerformed(ActionEvent e){
				
				ExecutorService exec = Executors.newFixedThreadPool(2);
				RozrywkaZagniezdzona bolek = new RozrywkaZagniezdzona(3);
				exec.execute(bolek);
				
			}	
		});	
		
		lvl4.addActionListener(new ActionListener() { //--------------------rozpoczecie gry (eksperymentalny)
			@Override
			public void actionPerformed(ActionEvent e){
				
				ExecutorService exec = Executors.newFixedThreadPool(2);
				RozrywkaZagniezdzona bolek = new RozrywkaZagniezdzona(4);
				exec.execute(bolek);
				
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
					Rozgrywka r1 = new Rozgrywka(tekstWe.getText(), fileName, jezyk, awers);
					
				} catch(IOException ex) {}
			}
		});
		
		sklep.addActionListener(new ActionListener() { //kup rewers
			@Override
			public void actionPerformed(ActionEvent e) {
				
				if(awers==1)
					System.out.println("Masz ju¿ awers!");
				else if(money<150)
					System.out.println("Nie masz wymaganych 150 monet!");
				else {
					System.out.println("Zakupiono!");
					awers = 1;
					money -= 150;
					moneyCount.setText(String.format("%d", money));
					
					pikk9 = robKarte2(pik9,"pik9"); 	pikk10 = robKarte2(pik10,"pik10"); 		pikkW = robKarte2(pikW,"pikW"); 
					pikkQ = robKarte2(pikQ,"pikQ"); 	pikkK = robKarte2(pikK,"pikK"); 		pikkA = robKarte2(pikA,"pikA"); 
					kierr9 = robKarte2(kier9,"kier9"); 	kierr10 = robKarte2(kier10,"kier10");	kierrW = robKarte2(kierW,"kierW");
					kierrQ = robKarte2(kierQ,"kierQ"); 	kierrK = robKarte2(kierK,"kierK"); 		kierrA = robKarte2(kierA,"kierA");
					karoo9 = robKarte2(karo9,"karo9"); 	karoo10 = robKarte2(karo10,"karo10"); 	karooW = robKarte2(karoW,"karoW");
					karooQ = robKarte2(karoQ,"karoQ"); 	karooK = robKarte2(karoK,"karoK"); 		karooA = robKarte2(karoA,"karoA");
					trefll9 = robKarte2(trefl9,"trefl9");trefll10 = robKarte2(trefl10,"trefl10");trefllW = robKarte2(treflW,"treflW"); 
					trefllQ = robKarte2(treflQ,"treflQ");trefllK = robKarte2(treflK,"treflK"); 	trefllA = robKarte2(treflA,"treflA"); 
				
					//zapisanie posiadania awersu do temp.txt
					String tekstWyj = "";
					tekstWyj += money + " ";
					for(int i=0; i<4; i++) {
						tekstWyj += wins[i] + " ";
						tekstWyj += loses[i] + " ";
						tekstWyj += draws[i] + " ";
	  				}
	  				for(int i=0; i<11; i++) {
	  					tekstWyj += memory[i] + " ";
	  				}
	  				tekstWyj += streak + " ";
	  				tekstWyj += awers;
	  				
					FileWriter plikWyj;
					try {
						plikWyj = new FileWriter(fileTemp);
						plikWyj.write(tekstWyj);
						plikWyj.close();
					} catch (IOException e1) {
						e1.printStackTrace();
					}
				}
			}
		});
		
		wyjscie.addActionListener(new ActionListener() { //wyjscie z programu
			@Override
			public void actionPerformed(ActionEvent e) {
				System.exit(0);
			}
		});	
	}
	
	public class SliderChangeListener implements ChangeListener{ 	//zmiana jêzyka polski-0 angielski-1		
		@Override
		public void stateChanged(ChangeEvent arg0) {
			jezyk = jezykSlider.getValue();
			if(jezyk==1) {
				System.out.println("Chosen language: english");
				napisGlowny.setText("POKER");
				poziomyTrudnosci.setText("Difficulty levels:");
				lvl1.setText("Easy");
				lvl2.setText("Medium");
				lvl3.setText("Hard (WARNING! long calculation time)");
				lvl4.setText("Experimental (long calculation time)");
				sklep.setText("Buy another card design for 150$");
				importuj.setText("Import a game");
				zapisz.setText("Save last game to file");
				wyjscie.setText("Exit");
				
				tabbedPane.removeAll();
				tabbedPane.addTab("Main Menu", glownyPanel);
				tabbedPane.addTab("Statistics", statsPanel);
				
				infS0.setText("Your hands:	");
				infS1.setText("No layout     "+memory[1]);
				infS2.setText("1 Pair        "+memory[2]);
				infS3.setText("2 Pair        "+memory[3]);
				infS4.setText("Three o/a kind   "+memory[4]);
				infS5.setText("Straight      "+memory[5]);
				infS6.setText("Full house    "+memory[6]);
				infS7.setText("Flush         "+memory[7]);
				infS8.setText("Four o/a kind  "+memory[8]);
				infS9.setText("Straight flush  "+memory[9]);
				infS10.setText("Total        "+memory[10]);
				infA0.setText("Level: easy");
				infA1.setText("Wins     "+wins[0]);
				infA2.setText("Loses    "+loses[0]);
				infA3.setText("Draws    "+draws[0]);
				infA4.setText("Total    "+games[0]);
				infA5.setText("Win ratio  "+df.format(winRatio[0]));
				infB0.setText("Level: medium");
				infB1.setText("Wins     "+wins[1]);
				infB2.setText("Loses    "+loses[1]);
				infB3.setText("Draws    "+draws[1]);
				infB4.setText("Total    "+games[1]);
				infB5.setText("Win ratio  "+df.format(winRatio[1]));
				infC0.setText("Level: hard");
				infC1.setText("Wins     "+wins[2]);
				infC2.setText("Loses    "+loses[2]);
				infC3.setText("Draws    "+draws[2]);
				infC4.setText("Total    "+games[2]);
				infC5.setText("Win ratio  "+df.format(winRatio[2]));
				infD0.setText("Total");
				infD1.setText("Wins     "+wins[3]);
				infD2.setText("Loses    "+loses[3]);
				infD3.setText("Draws    "+draws[3]);
				infD4.setText("Total    "+games[3]);
				infD5.setText("Win ratio  "+df.format(winRatio[3]));
			}
			if(jezyk==0) {
				System.out.println("Wybrany jêzyk: polski");
				napisGlowny.setText("POKER");
				poziomyTrudnosci.setText("Poziomy trudnoœci:");
				lvl1.setText("£atwy");
				lvl2.setText("Œredni");
				lvl3.setText("Trudny (UWAGA! d³ugi czas kalkulacji)");
				lvl4.setText("Eksperimentalny (d³ugi czas kalkulacji)");
				sklep.setText("Kup inny awers kart za 150 monet");
				importuj.setText("Importuj partiê");
				zapisz.setText("Zapisz ostatni¹ zagran¹ partie");
				wyjscie.setText("Wyjœcie");
				
				tabbedPane.removeAll();
				tabbedPane.addTab("Menu G³ówne", glownyPanel);
				tabbedPane.addTab("Statystyki", statsPanel);
				
				infS0.setText("Twoje uk³ady karciane");
				infS1.setText("Brak uk³adu  "+memory[1]);
				infS2.setText("Para        "+memory[2]);
				infS3.setText("2 Pary      "+memory[3]);
				infS4.setText("Trójka      "+memory[4]);
				infS5.setText("Streat      "+memory[5]);
				infS6.setText("Full        "+memory[6]);
				infS7.setText("Kolor       "+memory[7]);
				infS8.setText("Kareta      "+memory[8]);
				infS9.setText("Poker       "+memory[9]);
				infS10.setText("£¹cznie    "+memory[10]);
				infA0.setText("Poziom: ³atwy");
				infA1.setText("Zwyciêstwa   "+wins[0]);
				infA2.setText("Przegrane    "+loses[0]);
				infA3.setText("Remisy       "+draws[0]);
				infA4.setText("£¹cznie      "+games[0]);
				infA5.setText("% zwyciêstw  "+df.format(winRatio[0]));
				infB0.setText("Poziom: œredni");
				infB1.setText("Zwyciêstwa   "+wins[1]);
				infB2.setText("Przegrane    "+loses[1]);
				infB3.setText("Remisy       "+draws[1]);
				infB4.setText("£¹cznie      "+games[1]);
				infB5.setText("% zwyciêstw  "+df.format(winRatio[1]));
				infC0.setText("Poziom: trudny");
				infC1.setText("Zwyciêstwa   "+wins[2]);
				infC2.setText("Przegrane    "+loses[2]);
				infC3.setText("Remisy       "+draws[2]);
				infC4.setText("£¹cznie      "+games[2]);
				infC5.setText("% zwyciêstw  "+df.format(winRatio[2]));
				infD0.setText("£¹cznie");
				infD1.setText("Zwyciêstwa   "+wins[3]);
				infD2.setText("Przegrane    "+loses[3]);
				infD3.setText("Remisy       "+draws[3]);
				infD4.setText("£¹cznie      "+games[3]);
				infD5.setText("% zwyciêstw  "+df.format(winRatio[3]));
			}
		}
	}
	
	public ImagePanel robKarte(BufferedImage karta, String nazwaKarty) { //rewers=0
		Graphics g1 = getGraphics();
		File inputFile1 = new File(nazwaKarty + ".png");
        try {
        	karta = ImageIO.read(inputFile1);
        } catch(IOException ex) {
            System.out.println(ex.getMessage());
        }
 
        return new ImagePanel(karta);
	}
	
	public ImagePanel robKarte2(BufferedImage karta, String nazwaKarty) { //rewers=1
		Graphics g1 = getGraphics();
		File inputFile1 = new File(nazwaKarty + "2.png");
        try {
        	karta = ImageIO.read(inputFile1);
        } catch(IOException ex) {
            System.out.println(ex.getMessage());
        }
 
        return new ImagePanel(karta);
	}
	
	
	public static void main(String[] args) { //main
		Poker poker = new Poker();
		poker.setVisible(true);
	}

}


