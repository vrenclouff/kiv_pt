package cz.chmelokvas.util;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import cz.chmelokvas.brewery.Brewery;
import cz.chmelokvas.brewery.Dock;
import cz.chmelokvas.brewery.Order;
import cz.chmelokvas.brewery.Pub;
import cz.chmelokvas.brewery.Stock;
import cz.chmelokvas.brewery.Time;
import cz.chmelokvas.brewery.TransportNode;

public class Controller {
	public static Controller c;

	
	/** Casovy krok*/
	public static final int STEP = 60;
	
	/**Nko v konecnem programu zmizi. je to jen pomocna promena pro generovani zkusebnich dat */
//	public static final int N = 10;
	
	/** Vsechny dopravni uzly simulace */
//	public TransportNode[] nodes = new TransportNode[N+1];
	
	public List<TransportNode> nodes;
	
	public List<Pub> pub;
	
	public List<Dock> dock;
	
	public Brewery brewery;
	
	public Controller(List<Pub> pub, List<Dock> dock, Brewery brewery){
		this.pub = pub;
		this.dock = dock;
		this.brewery = brewery;
		
		nodes = new ArrayList<TransportNode>();
		nodes.add(brewery);
		nodes.addAll(dock);
		nodes.addAll(pub);
		
		for(TransportNode n:nodes){
			n.setC(this);
		}
	}
	
	/** Vsechny objednavky pro dany den	 */
	public List<Order> todayOrders = new ArrayList<Order>();
	
	/**hlavni cas cele aplikace*/
	public Time mainTime = new Time(0,0,0);
	
	/** cas ukonceni simulace */
	public Time endTime = new Time(2,0,0);
	
	/**
	 * Prida silnici mezi dvema dopravnimi uzly
	 * @param a id prvniho uzlu u Controlleru
	 * @param b id druheho uzlu u Controlleru
	 * @param d delka silnice
	 */
	public void addRoute(int a, int b, float d){
		TransportNode nA = nodes.get(a);
		TransportNode nB = nodes.get(b);
		nA.getRoutes().add(new Route(d, b));
		nB.getRoutes().add(new Route(d, a));
		
		if(nA.getProvider().equals(nB.getProvider())){
			Stock s = nA.getProvider();
			int tmpA = nA.getIdProv();
			int tmpB = nB.getIdProv();
			s.getD()[tmpA][tmpB] = d;
			s.getD()[tmpB][tmpA] = d;
		}
		
		//TODO pridat vkladani hran pro pivovar
	}
	
	/**
	 * Metoda ktera bude obstaravat celou simulaci
	 */
	public void simulate(){
		for(Dock d:dock){
			d.floydWarshal(d.getD(), d.getP(), d.getD().length);
		}
		
		int oldDay = -1;
		while(mainTime.value() < endTime.value()){
			
			
			
			System.out.println("---" + mainTime + "---");
			//generovani objednavek na zacatku dne
			if(oldDay != mainTime.getDay()){
				generateOrders();
			}
		
			checkTime();
			
			if(mainTime.getHour() >= 8 && mainTime.getHour() < 16 && mainTime.getDay() == 0){
				//Rozeslani objednavek v dany cas
				sendOrders();
			}
			
			oldDay = mainTime.getDay();
			mainTime.addMinutes(STEP);
			/*try {
				Thread.sleep(100);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}*/
			System.out.println("\n\n");
		}
		System.out.println(mainTime);
		for(Dock d :dock){
			System.err.println(d.getBeingPrepared());
			System.err.println(d.getGarage().size() + "\n");
		}
	}
	
	/**
	 * Pro vsechny hospody simulace vypocte objednavku pro dany den
	 */
	private void generateOrders(){
		
		for(Pub p : pub){
			todayOrders.add(p.makeOrder());
		}
	}
	
	private void checkTime(){
		for(Dock d: dock){
			d.checkTimeEvents();
		}
//		dock.get(1).checkTimeEvents();
		brewery.checkTimeEvents();
	}
	
	/**
	 * Pokud nastal cas pro odeslani objednavky, odesle ji
	 */
	private void sendOrders(){
		for(Iterator<Order> it = todayOrders.iterator(); it.hasNext();){
			Order o = it.next();
			if(Math.abs(o.getTime().value() - mainTime.value()) < STEP){
				if(o.getPub().isTank()){
//					System.out.println(o + " byla predana prekladisti " + brewery);
//					brewery.recieveOrder(o);
					it.remove();
				}else{
//					System.out.println(o + " byla predana prekladisti " + o.getPub().getProvider());
					o.getPub().getProvider().recieveOrder(o);
					it.remove();
				}
			}
		}
	}
}
