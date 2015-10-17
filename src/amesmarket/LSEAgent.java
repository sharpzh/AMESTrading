/* ============================================================================
 * AMES Wholesale Power Market Test Bed (Java): A Free Open-Source Test-Bed  
 *         for the Agent-based Modeling of Electricity Systems 
 * ============================================================================
 *
 * (C) Copyright 2008, by Hongyan Li, Junjie Sun, and Leigh Tesfatsion
 *
 *    Homepage: http://www.econ.iastate.edu/tesfatsi/AMESMarketHome.htm
 *
 * LICENSING TERMS
 * The AMES Market Package is licensed by the copyright holders (Junjie Sun, 
 * Hongyan Li, and Leigh Tesfatsion) as free open-source software under the       
 * terms of the GNU General Public License (GPL). Anyone who is interested is 
 * allowed to view, modify, and/or improve upon the code used to produce this 
 * package, but any software generated using all or part of this code must be 
 * released as free open-source software in turn. The GNU GPL can be viewed in 
 * its entirety as in the following site: http://www.gnu.org/licenses/gpl.html
 */

// LSEAgent.java
// Load serving entity (wholesale power buyer)

package amesmarket;


import java.awt.Color;

import uchicago.src.sim.gui.Drawable;
import uchicago.src.sim.gui.SimGraphics;

/** Example showing what lseData[i] contains
LSEData
//ID	atBus	block	LP	block	LP	block	LP	block	LP
  1	1	6	22	6	30	6	34	6	27

// block: load block for the next how many hours
// LP: load profile (fixed demand)
 */

public class LSEAgent implements Drawable{

  private static final int ID      = 0;
  private static final int AT_NODE = 1;
  private static final int BLOCK1  = 2;
  private static final int LP1     = 3;
  private static final int BLOCK2  = 4;
  private static final int LP2     = 5;
  private static final int BLOCK3  = 6;
  private static final int LP3     = 7;
  private static final int BLOCK4  = 8;
  private static final int LP4     = 9;

  private static final int HOURS_PER_DAY = 24;

  // LSE's data


  private int x;      // Coordinate x on trans grid
  private int y;      // Coordinate y on trans grid
  private double power;
  private double money;

  private int id;
  private int atBus;
  private double[] loadProfile;
  
  private double[][] priceSensitiveDemand; // 24 hour

  private double c;                 // LSE's (true) cost attribute
  private double d;                 // LSE's (true) cost attribute
  private double demandMax;         // LSE's (true) maximum demand limit
  private double[] trueDemandBid;   // (c,d,demandMin,demandMax), true
   
  private int[] hybridFlag;         // use loadProfile or price sensitive demand
 
  // LSE's records by hours (within a day)
  private double[] psDispatch;  // Day-Ahead hourly price-sensitive demand quantity
  private double[] dispatch;    // Real-Time hourly power dispatch quantity
  private double[] dayAheadLMP; // Day-Ahead hourly locational marginal price
  private double[] realTimeLMP; // Real-Time hourly locational marginal price
  private double[] grossSurplus;  // grossSurplus = C*power - D*power^2

  private double[] netHourlySurplus;// hourlySurplus[h] = grossSurplus[h] - dispatch[h]*lmp[h]
  private double surplus;   // surplus = sum of hourlySurplus over 24 hours


  // Constructor
  public LSEAgent(double[] lseData, double[][] lsePriceData, int[] lseHybridData){

    x = -1;
    y = -1;
    power = 0;
    money = 0;

    // Parse lseData
    id = (int) lseData[ID];
    atBus = (int) lseData[AT_NODE];
    loadProfile = new double[HOURS_PER_DAY];
    hybridFlag = new int[HOURS_PER_DAY];
    priceSensitiveDemand = new double[HOURS_PER_DAY][3];
    
    grossSurplus = new double[HOURS_PER_DAY];

    //System.out.println("LSE ID="+id);
    for(int h=2; h<lseData.length; h++){
      loadProfile[h-2] = lseData[h];
      hybridFlag[h-2] = lseHybridData[h];
      
      //System.out.println("h="+(h-2)+"\tloadProfile="+loadProfile[h-2]+"\tFlag="+hybridFlag[h-2]);
    }
    
    for(int h=0; h<24; h++){
        //System.out.println();
    
      for(int j=0; j<3; j++){
          priceSensitiveDemand[h][j]=lsePriceData[h][j+3];
          //System.out.print("h="+h+"\tj="+j+"\tpriceSensitiveDemand[h][j]="+priceSensitiveDemand[h][j]+"   \t" );
      }
    }
  }

  public double[] submitLoadProfile(){
    return loadProfile;
  }

  public double[][] submitDemandBid(){
     double[][] reportDemandBid = new double[24][3];
    
     for(int h=0; h<24; h++)
         for(int i=0; i<3; i++){
           reportDemandBid[h][i] = priceSensitiveDemand[h][i];
         }
     
   return reportDemandBid;
  }
  
  public int[] submitHybridFlag(){
    return hybridFlag;
  }

  
 // LSE's get and set methods
  public void setXY(int newX, int newY){
    x = newX;
    y = newY;
  }
  public int getID(){
    return id;
  }
  public int getAtNode(){
    return atBus;
  }
  public double[] getLoadProfile(){
    return loadProfile;
  }
  public double getPower(){
    return power;
  }

  public void report(){
    System.out.println(getID() + "at" + x + "," + y + "demands" + getPower()
                       + "MWhs of power.");
  }

  public int getX(){ return x;}
  public int getY(){ return y;}
  public void draw(SimGraphics sg){
    sg.drawFastRoundRect(Color.yellow);  // LSE is yellow colored
  }
  
  public void setPSDispatch(double[] ps){
    psDispatch = ps;
  }
  
  public double getSurplus() {
        return surplus;
  }
  
  public void updateSurplus(){
    surplus = 0;
    netHourlySurplus = new double[HOURS_PER_DAY];
    computeTotalSurplus();
    for(int h=0; h<HOURS_PER_DAY; h++){
      netHourlySurplus[h] = grossSurplus[h] - psDispatch[h]*dayAheadLMP[h];
      surplus = surplus + netHourlySurplus[h];
    }
    
    updateMoney();
    //System.out.println("LSE "+getID()+" daily surplus: "+Support.roundOff(surplus,2));
  }

  private void computeTotalSurplus(){
    for(int h=0; h<HOURS_PER_DAY; h++){
      grossSurplus[h] = priceSensitiveDemand[h][0] * psDispatch[h] - priceSensitiveDemand[h][1] * psDispatch[h] * psDispatch[h];
    }
  }

  private void updateMoney(){
    money = money + surplus;
    //System.out.println("LSE "+getID()+" money holdings: "+Support.roundOff(money,2));
  }

  public void setDayAheadLMP(double[] lmprice){
    dayAheadLMP = lmprice;
  }
  
  public double[] getNetHourSurplus(){
    return netHourlySurplus;
  }

}



