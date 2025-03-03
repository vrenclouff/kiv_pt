package cz.chmelokvas.brewery;

import java.util.ArrayList;
import java.util.List;

import cz.chmelokvas.util.Controller;
import cz.chmelokvas.util.Route;

/**
 * Trida ktera reprezentuje jakykoliv dopravni uzel<br>
 * Od teto tridy dedi pivovar, prekladista a hospoda
 * @author Jan Dvorak A13B0293P
 *
 */
public class TransportNode {
	protected Controller c;
	
	protected Stock provider;
	
	/**ID pod kterym je uzel ulozen v kontroleru*/
	protected int idCont;
	
	/**ID pod kterym je uzel ulozen u dodavatele */
	protected int idProv;
	
	/**Cesty mezi uzly*/
	protected List<Route> routes = new ArrayList<Route>();
	
	/** X souradnice */
	protected float x;
	
	/** =Y souradnice */
	protected float y;
	
	/** Vrati sousedy daneho dopravniho uzlu
	 * @return sousedi 
	 */
	public List<Route> getRoutes(){
		return routes;
	}
	
	public Stock getProvider(){
		return provider;
	}
	
	public void setProvider(Stock s){
		this.provider = s;
	}
	
	public Controller getC() {
		return c;
	}

	public void setC(Controller c) {
		this.c = c;
	}

	public int getIdCont() {
		return idCont;
	}

	public void setIdCont(int idCont) {
		this.idCont = idCont;
	}

	public int getIdProv() {
		return idProv;
	}

	public void setIdProv(int idProv) {
		this.idProv = idProv;
	}

	public double getX() {
		return x;
	}

	public void setX(float x) {
		this.x = x;
	}

	public double getY() {
		return y;
	}

	public void setY(float y) {
		this.y = y;
	}

	
	
	
}
