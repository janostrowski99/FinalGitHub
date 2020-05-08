package poker;

import java.awt.BorderLayout;
import java.awt.Graphics;
import java.awt.GridLayout;
import java.awt.HeadlessException;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class Rozgrywka extends JFrame {
	private static final long serialVersionUID = 1L;
	JPanel panGlowny, pan1, pan2, pan3, pan4, pan21, pan22, pan31, pan32, pan23, pan33;
	JLabel graNr, zwycL, uklad1, uklad2, kompPrzed, kompWym, kompPo, graczPrzed, graczWym, graczPo;
	JButton wyjdz;
	ImagePanel pikk9, pikk10, pikkW, pikkQ, pikkK, pikkA, kierr9, kierr10, kierrW, kierrQ, kierrK, kierrA;
	ImagePanel  karoo9, karoo10, karooW, karooQ, karooK, karooA, trefll9, trefll10, trefllW, trefllQ, trefllK, trefllA;
	ImagePanel pikkk9, pikkk10, pikkkW, pikkkQ, pikkkK, pikkkA, kierrr9, kierrr10, kierrrW, kierrrQ, kierrrK, kierrrA;
	ImagePanel  karooo9, karooo10, karoooW, karoooQ, karoooK, karoooA, treflll9, treflll10, treflllW, treflllQ, treflllK, treflllA;
	BufferedImage pik9, pik10, pikW, pikQ, pikK, pikA, kier9, kier10, kierW, kierQ, kierK, kierA;
	BufferedImage karo9, karo10, karoW, karoQ, karoK, karoA, trefl9, trefl10, treflW, treflQ, treflK, treflA;
	
	String[] daneString;
	String zwyciezca, uklad3, uklad4;
	
	Rozgrywka(String dane, String nazwaPliku) throws HeadlessException {
		Rozgrywka temp = this;
		this.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		this.setSize(500,800);
		this.setVisible(true);
		this.setTitle("Poker | Analiza rozgrywki");
		
	    panGlowny = new JPanel();
	    this.add(panGlowny);
	    panGlowny.setLayout(new BorderLayout());
	    pan1 = new JPanel();
	    pan2 = new JPanel();
	    pan3 = new JPanel();
	    pan4 = new JPanel();
	    
	    panGlowny.add(pan1, BorderLayout.NORTH);
	    panGlowny.add(pan2, BorderLayout.WEST);
	    panGlowny.add(pan3, BorderLayout.EAST);
		panGlowny.add(pan4, BorderLayout.SOUTH);
		pan2.setLayout(new GridLayout(7,1));
		pan3.setLayout(new GridLayout(7,1));
		pan21 = new JPanel();
		pan22 = new JPanel();
		pan31 = new JPanel();
		pan32 = new JPanel();
		pan23 = new JPanel();
		pan33 = new JPanel();
		
		pik9 = null; pik10 = null; pikW = null; pikQ = null; pikK = null; pikA = null;
		kier9 = null; kier10 = null; kierW = null; kierQ = null; kierK = null; kierA = null;
		karo9 = null; karo10 = null; karoW = null; karoQ = null; karoK = null; karoA = null;
		trefl9 = null; trefl10 = null; treflW = null; treflQ = null; treflK = null; treflA = null;
		
		//karty
		pikk9 = robKarte(pik9,"pik9"); 		pikk10 = robKarte(pik10,"pik10"); 		pikkW = robKarte(pikW,"pikW"); 
		pikkQ = robKarte(pikQ,"pikQ"); 		pikkK = robKarte(pikK,"pikK"); 			pikkA = robKarte(pikA,"pikA"); 
		kierr9 = robKarte(kier9,"kier9"); 	kierr10 = robKarte(kier10,"kier10");	kierrW = robKarte(kierW,"kierW");
		kierrQ = robKarte(kierQ,"kierQ"); 	kierrK = robKarte(kierK,"kierK"); 		kierrA = robKarte(kierA,"kierA");
		karoo9 = robKarte(karo9,"karo9"); 	karoo10 = robKarte(karo10,"karo10"); 	karooW = robKarte(karoW,"karoW");
		karooQ = robKarte(karoQ,"karoQ"); 	karooK = robKarte(karoK,"karoK"); 		karooA = robKarte(karoA,"karoA");
		trefll9 = robKarte(trefl9,"trefl9");trefll10 = robKarte(trefl10,"trefl10"); trefllW = robKarte(treflW,"treflW"); 
		trefllQ = robKarte(treflQ,"treflQ");trefllK = robKarte(treflK,"treflK"); 	trefllA = robKarte(treflA,"treflA"); 

		//kopie kart (nieodrzucone karty s¹ nadpisane w najni¿szy panel kart, odrzucone zostaj¹ w "odrzucone")
		pikkk9 = robKarte(pik9,"pik9"); 	pikkk10 = robKarte(pik10,"pik10"); 		pikkkW = robKarte(pikW,"pikW"); 
		pikkkQ = robKarte(pikQ,"pikQ"); 	pikkkK = robKarte(pikK,"pikK"); 		pikkkA = robKarte(pikA,"pikA"); 
		kierrr9 = robKarte(kier9,"kier9"); 	kierrr10 = robKarte(kier10,"kier10");	kierrrW = robKarte(kierW,"kierW");
		kierrrQ = robKarte(kierQ,"kierQ"); 	kierrrK = robKarte(kierK,"kierK"); 		kierrrA = robKarte(kierA,"kierA");
		karooo9 = robKarte(karo9,"karo9"); 	karooo10 = robKarte(karo10,"karo10"); 	karoooW = robKarte(karoW,"karoW");
		karoooQ = robKarte(karoQ,"karoQ"); 	karoooK = robKarte(karoK,"karoK"); 		karoooA = robKarte(karoA,"karoA");
		treflll9 = robKarte(trefl9,"trefl9");treflll10 = robKarte(trefl10,"trefl10");treflllW = robKarte(treflW,"treflW"); 
		treflllQ = robKarte(treflQ,"treflQ");treflllK = robKarte(treflK,"treflK"); 	treflllA = robKarte(treflA,"treflA"); 

		//System.out.println("Dane: " + dane);
		
		daneString = dane.split("\\s"); //dane do stringu -> do intów
		for(int i=0; i<40; i=i+5)
			System.out.println(daneString[i]+" "+daneString[i+1]+" "+daneString[i+2]+" "+daneString[i+3]+" "+daneString[i+4]);
		
		//kto wygra³
		switch(daneString[42]) {
			case "0":
				zwyciezca = "komputer";
				break;
			case "1":
				zwyciezca = "gracz";
				break;
			case "2":
				zwyciezca = "remis";
				break;
		}
		
		//uk³ad komputera
		switch(daneString[40]) {
			case "1":
				uklad3 = "Brak uk³adu";
				break;
			case "2":
				uklad3 = "Para";
				break;
			case "3":
				uklad3 = "2 Pary";
				break;
			case "4":
				uklad3 = "Trójka";
				break;
			case "5":
				uklad3 = "Streat";
				break;
			case "6":
				uklad3 = "Full";
				break;
			case "7":
				uklad3 = "Kolor";
				break;
			case "8":
				uklad3 = "Kareta";
				break;
			case "9":
				uklad3 = "Poker";
				break;
		}
		
		switch(daneString[41]) {
			case "1":
				uklad4 = "Brak uk³adu";
				break;
			case "2":
				uklad4 = "Para";
				break;
			case "3":
				uklad4 = "2 Pary";
				break;
			case "4":
				uklad4 = "Trójka";
				break;
			case "5":
				uklad4 = "Streat";
				break;
			case "6":
				uklad4 = "Full";
				break;
			case "7":
				uklad4 = "Kolor";
				break;
			case "8":
				uklad4 = "Kareta";
				break;
			case "9":
				uklad4 = "Poker";
				break;
		}
		
			//jêzyk polski
		if(nazwaPliku=="brak") //po zakoñczeniu rozgrywki
			graNr = new JLabel("Wynik rozgrywki:");
		else			   //po zaimportowaniu
			graNr = new JLabel("Gra z pliku: " + nazwaPliku);
		zwycL = new JLabel("Zwyciêzca: " + zwyciezca);
		uklad1 = new JLabel("Uk³ad komputera: " + uklad3);
		uklad2 = new JLabel("Uk³ad gracza: " + uklad4);
		kompPrzed = new JLabel("Uk³ad komputera przed wymian¹:     ");
		kompPo = new JLabel("Uk³ad komputera po wymianie: ");
		kompWym = new JLabel("Wymienione karty przez komputer:   ");
		graczWym = new JLabel("Wymienione karty przez gracza:   ");
		graczPrzed = new JLabel("Uk³ad gracza przed wymian¹:        ");
		graczPo = new JLabel("Uk³ad gracza po wymianie: ");
		wyjdz = new JButton("Skoñczy³em ogl¹daæ");
		
		pan1.add(graNr);
		pan4.setLayout(new GridLayout(1,2));
		pan4.add(zwycL);
		pan4.add(wyjdz);
		
	
			
		
		pan21.setLayout(new GridLayout(1,5));
		pan22.setLayout(new GridLayout(1,5));
		pan31.setLayout(new GridLayout(1,5));
		pan32.setLayout(new GridLayout(1,5));  
		pan23.setLayout(new GridLayout(1,5));  
		pan33.setLayout(new GridLayout(1,5));  
		
		//grafiki kart do paneli
        for(int i=0; i<5; i++) {
        	//komp wymiana-------------------------------------------------------
        	switch(daneString[i]) {
	        	case "0":
	        	{
	        		switch(daneString[i+20]) {
		        		case "9":
		        			pan23.add(pikkk9);
			        		break;	
			        	case "10":
			        		pan23.add(pikkk10);
			        		break;	
			        	case "11":
			        		pan23.add(pikkkW);
			        		break;	
			        	case "12":
			        		pan23.add(pikkkQ);;
			        		break;	
			        	case "13":
			        		pan23.add(pikkkK);
			        		break;	
			        	case "14":
			        		pan23.add(pikkkA);
			        		break;	
		        	}
	        		break;
	        	}
	        	case "1":
	        	{
	        		switch(daneString[i+20]) {
		        		case "9":
		        			pan23.add(kierrr9);
			        		break;	
			        	case "10":
			        		pan23.add(kierrr10);
			        		break;	
			        	case "11":
			        		pan23.add(kierrrW);
			        		break;	
			        	case "12":
			        		pan23.add(kierrrQ);;
			        		break;	
			        	case "13":
			        		pan23.add(kierrrK);
			        		break;	
			        	case "14":
			        		pan23.add(kierrrA);
			        		break;	
		        	}
	        		break;
	        	}
	        	case "2":
	        	{
	        		switch(daneString[i+20]) {
		        		case "9":
		        			pan23.add(karooo9);
			        		break;	
			        	case "10":
			        		pan23.add(karooo10);
			        		break;	
			        	case "11":
			        		pan23.add(karoooW);
			        		break;	
			        	case "12":
			        		pan23.add(karoooQ);;
			        		break;	
			        	case "13":
			        		pan23.add(karoooK);
			        		break;	
			        	case "14":
			        		pan23.add(karoooA);
			        		break;	
		        	}
	        		break;
	        	}
	        	case "3":
	        	{
	        		switch(daneString[i+20]) {
		        		case "9":
		        			pan23.add(treflll9);
			        		break;	
			        	case "10":
			        		pan23.add(treflll10);
			        		break;	
			        	case "11":
			        		pan23.add(treflllW);
			        		break;	
			        	case "12":
			        		pan23.add(treflllQ);;
			        		break;	
			        	case "13":
			        		pan23.add(treflllK);
			        		break;	
			        	case "14":
			        		pan23.add(treflllA);
			        		break;	
		        	}
	        		break;
	        	}
        	}
        	//gracz wymiana----------------------------------------------------
        	switch(daneString[i+5]) {
	        	case "0":
	        	{
	        		switch(daneString[i+25]) {
		        		case "9":
		        			pan33.add(pikkk9);
			        		break;	
			        	case "10":
			        		pan33.add(pikkk10);
			        		break;	
			        	case "11":
			        		pan33.add(pikkkW);
			        		break;	
			        	case "12":
			        		pan33.add(pikkkQ);;
			        		break;	
			        	case "13":
			        		pan33.add(pikkkK);
			        		break;	
			        	case "14":
			        		pan33.add(pikkkA);
			        		break;	
		        	}
	        		break;
	        	}
	        	case "1":
	        	{
	        		switch(daneString[i+25]) {
		        		case "9":
		        			pan33.add(kierrr9);
			        		break;	
			        	case "10":
			        		pan33.add(kierrr10);
			        		break;	
			        	case "11":
			        		pan33.add(kierrrW);
			        		break;	
			        	case "12":
			        		pan33.add(kierrrQ);;
			        		break;	
			        	case "13":
			        		pan33.add(kierrrK);
			        		break;	
			        	case "14":
			        		pan33.add(kierrrA);
			        		break;	
		        	}
	        		break;
	        	}
	        	case "2":
	        	{
	        		switch(daneString[i+25]) {
		        		case "9":
		        			pan33.add(karooo9);
			        		break;	
			        	case "10":
			        		pan33.add(karooo10);
			        		break;	
			        	case "11":
			        		pan33.add(karoooW);
			        		break;	
			        	case "12":
			        		pan33.add(karoooQ);;
			        		break;	
			        	case "13":
			        		pan33.add(karoooK);
			        		break;	
			        	case "14":
			        		pan33.add(karoooA);
			        		break;	
		        	}
	        		break;
	        	}
	        	case "3":
	        	{
	        		switch(daneString[i+25]) {
		        		case "9":
		        			pan33.add(treflll9);
			        		break;	
			        	case "10":
			        		pan33.add(treflll10);
			        		break;	
			        	case "11":
			        		pan33.add(treflllW);
			        		break;	
			        	case "12":
			        		pan33.add(treflllQ);;
			        		break;	
			        	case "13":
			        		pan33.add(treflllK);
			        		break;	
			        	case "14":
			        		pan33.add(treflllA);
			        		break;	
		        	}
	        		break;
	        	}
	    	}
        	//komp przed-------------------------------------------------------
        	switch(daneString[i]) {
	        	case "0":
	        	{
	        		switch(daneString[i+20]) {
		        		case "9":
		        			pan21.add(pikk9);
			        		break;	
			        	case "10":
			        		pan21.add(pikk10);
			        		break;	
			        	case "11":
			        		pan21.add(pikkW);
			        		break;	
			        	case "12":
			        		pan21.add(pikkQ);;
			        		break;	
			        	case "13":
			        		pan21.add(pikkK);
			        		break;	
			        	case "14":
			        		pan21.add(pikkA);
			        		break;	
		        	}
	        		break;
	        	}
	        	case "1":
	        	{
	        		switch(daneString[i+20]) {
		        		case "9":
		        			pan21.add(kierr9);
			        		break;	
			        	case "10":
			        		pan21.add(kierr10);
			        		break;	
			        	case "11":
			        		pan21.add(kierrW);
			        		break;	
			        	case "12":
			        		pan21.add(kierrQ);;
			        		break;	
			        	case "13":
			        		pan21.add(kierrK);
			        		break;	
			        	case "14":
			        		pan21.add(kierrA);
			        		break;	
		        	}
	        		break;
	        	}
	        	case "2":
	        	{
	        		switch(daneString[i+20]) {
		        		case "9":
		        			pan21.add(karoo9);
			        		break;	
			        	case "10":
			        		pan21.add(karoo10);
			        		break;	
			        	case "11":
			        		pan21.add(karooW);
			        		break;	
			        	case "12":
			        		pan21.add(karooQ);;
			        		break;	
			        	case "13":
			        		pan21.add(karooK);
			        		break;	
			        	case "14":
			        		pan21.add(karooA);
			        		break;	
		        	}
	        		break;
	        	}
	        	case "3":
	        	{
	        		switch(daneString[i+20]) {
		        		case "9":
		        			pan21.add(trefll9);
			        		break;	
			        	case "10":
			        		pan21.add(trefll10);
			        		break;	
			        	case "11":
			        		pan21.add(trefllW);
			        		break;	
			        	case "12":
			        		pan21.add(trefllQ);;
			        		break;	
			        	case "13":
			        		pan21.add(trefllK);
			        		break;	
			        	case "14":
			        		pan21.add(trefllA);
			        		break;	
		        	}
	        		break;
	        	}
	    	}
	    	//gracz przed----------------------------------------------------
	    	switch(daneString[i+5]) {
	        	case "0":
	        	{
	        		switch(daneString[i+25]) {
		        		case "9":
		        			pan31.add(pikk9);
			        		break;	
			        	case "10":
			        		pan31.add(pikk10);
			        		break;	
			        	case "11":
			        		pan31.add(pikkW);
			        		break;	
			        	case "12":
			        		pan31.add(pikkQ);;
			        		break;	
			        	case "13":
			        		pan31.add(pikkK);
			        		break;	
			        	case "14":
			        		pan31.add(pikkA);
			        		break;	
		        	}
	        		break;
	        	}
	        	case "1":
	        	{
	        		switch(daneString[i+25]) {
		        		case "9":
		        			pan31.add(kierr9);
			        		break;	
			        	case "10":
			        		pan31.add(kierr10);
			        		break;	
			        	case "11":
			        		pan31.add(kierrW);
			        		break;	
			        	case "12":
			        		pan31.add(kierrQ);;
			        		break;	
			        	case "13":
			        		pan31.add(kierrK);
			        		break;	
			        	case "14":
			        		pan31.add(kierrA);
			        		break;	
		        	}
	        		break;
	        	}
	        	case "2":
	        	{
	        		switch(daneString[i+25]) {
		        		case "9":
		        			pan31.add(karoo9);
			        		break;	
			        	case "10":
			        		pan31.add(karoo10);
			        		break;	
			        	case "11":
			        		pan31.add(karooW);
			        		break;	
			        	case "12":
			        		pan31.add(karooQ);;
			        		break;	
			        	case "13":
			        		pan31.add(karooK);
			        		break;	
			        	case "14":
			        		pan31.add(karooA);
			        		break;	
		        	}
	        		break;
	        	}
	        	case "3":
	        	{
	        		switch(daneString[i+25]) {
		        		case "9":
		        			pan31.add(trefll9);
			        		break;	
			        	case "10":
			        		pan31.add(trefll10);
			        		break;	
			        	case "11":
			        		pan31.add(trefllW);
			        		break;	
			        	case "12":
			        		pan31.add(trefllQ);;
			        		break;	
			        	case "13":
			        		pan31.add(trefllK);
			        		break;	
			        	case "14":
			        		pan31.add(trefllA);
			        		break;	
		        	}
	        		break;
	        	}
	    	}
        	//komp po ----------------------------------------------------
        	switch(daneString[i+10]) {
	        	case "0":
	        	{
	        		switch(daneString[i+30]) {
		        		case "9":
		        			pan22.add(pikkk9);
			        		break;	
			        	case "10":
			        		pan22.add(pikkk10);
			        		break;	
			        	case "11":
			        		pan22.add(pikkkW);
			        		break;	
			        	case "12":
			        		pan22.add(pikkkQ);;
			        		break;	
			        	case "13":
			        		pan22.add(pikkkK);
			        		break;	
			        	case "14":
			        		pan22.add(pikkkA);
			        		break;	
		        	}
	        		break;
	        	}
	        	case "1":
	        	{
	        		switch(daneString[i+30]) {
		        		case "9":
		        			pan22.add(kierrr9);
			        		break;	
			        	case "10":
			        		pan22.add(kierrr10);
			        		break;	
			        	case "11":
			        		pan22.add(kierrrW);
			        		break;	
			        	case "12":
			        		pan22.add(kierrrQ);;
			        		break;	
			        	case "13":
			        		pan22.add(kierrrK);
			        		break;	
			        	case "14":
			        		pan22.add(kierrrA);
			        		break;	
		        	}
	        		break;
	        	}
	        	case "2":
	        	{
	        		switch(daneString[i+30]) {
		        		case "9":
		        			pan22.add(karooo9);
			        		break;	
			        	case "10":
			        		pan22.add(karooo10);
			        		break;	
			        	case "11":
			        		pan22.add(karoooW);
			        		break;	
			        	case "12":
			        		pan22.add(karoooQ);;
			        		break;	
			        	case "13":
			        		pan22.add(karoooK);
			        		break;	
			        	case "14":
			        		pan22.add(karoooA);
			        		break;	
		        	}
	        		break;
	        	}
	        	case "3":
	        	{
	        		switch(daneString[i+30]) {
		        		case "9":
		        			pan22.add(treflll9);
			        		break;	
			        	case "10":
			        		pan22.add(treflll10);
			        		break;	
			        	case "11":
			        		pan22.add(treflllW);
			        		break;	
			        	case "12":
			        		pan22.add(treflllQ);;
			        		break;	
			        	case "13":
			        		pan22.add(treflllK);
			        		break;	
			        	case "14":
			        		pan22.add(treflllA);
			        		break;	
		        	}
	        		break;
	        	}
	    	}
        	//gracz po ----------------------------------------------------
        	switch(daneString[i+15]) {
	        	case "0":
	        	{
	        		switch(daneString[i+35]) {
		        		case "9":
		        			pan32.add(pikkk9);
			        		break;	
			        	case "10":
			        		pan32.add(pikkk10);
			        		break;	
			        	case "11":
			        		pan32.add(pikkkW);
			        		break;	
			        	case "12":
			        		pan32.add(pikkkQ);;
			        		break;	
			        	case "13":
			        		pan32.add(pikkkK);
			        		break;	
			        	case "14":
			        		pan32.add(pikkkA);
			        		break;	
		        	}
	        		break;
	        	}
	        	case "1":
	        	{
	        		switch(daneString[i+35]) {
		        		case "9":
		        			pan32.add(kierrr9);
			        		break;	
			        	case "10":
			        		pan32.add(kierrr10);
			        		break;	
			        	case "11":
			        		pan32.add(kierrrW);
			        		break;	
			        	case "12":
			        		pan32.add(kierrrQ);;
			        		break;	
			        	case "13":
			        		pan32.add(kierrrK);
			        		break;	
			        	case "14":
			        		pan32.add(kierrrA);
			        		break;	
		        	}
	        		break;
	        	}
	        	case "2":
	        	{
	        		switch(daneString[i+35]) {
		        		case "9":
		        			pan32.add(karooo9);
			        		break;	
			        	case "10":
			        		pan32.add(karooo10);
			        		break;	
			        	case "11":
			        		pan32.add(karoooW);
			        		break;	
			        	case "12":
			        		pan32.add(karoooQ);;
			        		break;	
			        	case "13":
			        		pan32.add(karoooK);
			        		break;	
			        	case "14":
			        		pan32.add(karoooA);
			        		break;	
		        	}
	        		break;
	        	}
	        	case "3":
	        	{
	        		switch(daneString[i+35]) {
		        		case "9":
		        			pan32.add(treflll9);
			        		break;	
			        	case "10":
			        		pan32.add(treflll10);
			        		break;	
			        	case "11":
			        		pan32.add(treflllW);
			        		break;	
			        	case "12":
			        		pan32.add(treflllQ);;
			        		break;	
			        	case "13":
			        		pan32.add(treflllK);
			        		break;	
			        	case "14":
			        		pan32.add(treflllA);
			        		break;	
		        	}
	        		break;
	        	}
	    	}
        } //end for
    
	    pan2.add(kompPrzed);
		pan2.add(pan21);
		pan2.add(kompWym);
		pan2.add(pan23);
		pan2.add(kompPo);
		pan2.add(pan22);
		pan2.add(uklad1);
		
		pan3.add(graczPrzed);
		pan3.add(pan31);
		pan3.add(graczWym);
		pan3.add(pan33);
		pan3.add(graczPo);
		pan3.add(pan32);
		pan3.add(uklad2);
		
		
		wyjdz.addActionListener(new ActionListener() { 
			@Override
			public void actionPerformed(ActionEvent e) {
				temp.setVisible(false);
				//System.exit(0); //zmieniæ na wyjœcie z samego okna
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
	
	
}
