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

// ISO.java
// Independent system operator

package amesmarket;

import java.util.ArrayList;

public class ISO {

  // ISO's data;
  private int currentMonth;   //to check if it has been a month

  private double[][] supplyOfferByGen;
  private double[][] supplyTrueOfferByGen;
  private double[][] loadProfileByLSE;
  private double[][][] demandBidByLSE;
  private int   [][] demandHybridByLSE;
  private double[][] dailyPriceSensitiveDispatch;
  private double[][] dailycommitment;
  private double[][] dailyemission;
  private double[][] dailyemTrade;
  private double[][] dailyemTradePrice;
  private double[][] dailylmp;
  private double[][] dailyBranchFlow;
  private ArrayList commitmentListByDay; // hourly commitments list for each agent by day (d)
  private ArrayList emissionListByDay;
  private ArrayList emTradeListByDay;
  private ArrayList emTradePriceListByDay;
  private ArrayList lmpListByDay;        // hourly LMPs list for each bus by day (d)

  private AMESMarket ames;
  private DAMarket dam;
  private SRMarket srm;
  private RTMarket rtm;
  private FTRMarket ftrm;
  private BUC buc;
  
  // constructor
  public ISO(AMESMarket model){

    //System.out.println("Creating the ISO object: iso \n");

    commitmentListByDay = new ArrayList();
    lmpListByDay        = new ArrayList();

    ames  = model;
    dam  = new DAMarket(ames);
    srm  = new SRMarket(ames);
    rtm  = new RTMarket(ames);
    ftrm = new FTRMarket(ames);
    buc = new BUC(this,ames);

  }

  public void computeCompetitiveEquilibriumResults(){
    //System.out.println("Compute competitive equilibrium results before the market is run\n");
    dam.submitTrueSupplyOffersAndDemandBids();
    supplyOfferByGen = dam.getTrueSupplyOfferByGen();
    supplyTrueOfferByGen = dam.getTrueSupplyOfferByGen();
    loadProfileByLSE = dam.getLoadProfileByLSE();
    demandBidByLSE = dam.getTrueDemandBidByLSE();
    demandHybridByLSE = dam.getDemandHybridByLSE();

    // Carry out BUC (Bid-based Unit Commitment) problem by solving DC OPF problem
    //System.out.printf("Solving the DC-OPF problem \n");
    buc.solveOPF();
    
    dailycommitment = buc.getDailyCommitment();
    dailyemission = buc.getDailyEmission();
    dailyemTrade = buc.getDailyEmTrade();
    dailyemTradePrice = buc.getDailyEmTradePrice();
    dailylmp = buc.getDailyLMP();
    dailyPriceSensitiveDispatch=buc.getDailyPriceSensitiveDemand();

    //NOT update generator's commitment, daily lmp, profit
    //dam.post(dailycommitment,dailylmp,1);

    ames.addGenAgentCommitmentWithTrueCost(dailycommitment);
    ames.addLMPWithTrueCost(dailylmp);
    ames.addLSEAgentPriceSensitiveDemandWithTrueCost(dailyPriceSensitiveDispatch);
    
    dam.postTrueSupplyOfferAndDemandBids(dailycommitment,dailylmp,dailyPriceSensitiveDispatch);
  }

  public void wholesalePowerMarketOperation(int h, int d){

    if(h==0){
      dam.dayAheadOperation(h,d);
    }
    if(h==12){
      evaluateBidsOffers(h,d);
    }
    if(h==17){
        if(d==321){
            int stop=1;
        }
      initialPost(h,d);
      //srm.supplyReOfferOperation(h,d+1,m);
    }
    if(h==18){
      //produceCommitmentSchedule(h,d+1,m);
    }
    if(h==0){
      //rtm.realTimeOperation(h,d,m);
    }
  }


  public void evaluateBidsOffers(int h, int d){
    //System.out.println("Hour " + h + " Day " + d  +
    //                   ": Evaluate LSEs' bids and GenCos' offers.");
    
    supplyOfferByGen = dam.getSupplyOfferByGen();
    supplyTrueOfferByGen=dam.getTrueSupplyOfferByGen();
    
    loadProfileByLSE = dam.getLoadProfileByLSE();
    demandBidByLSE = dam.getDemandBidByLSE();
    demandHybridByLSE = dam.getDemandHybridByLSE();
     
    // Carry out BUC (Bid-based Unit Commitment) problem by solving DC OPF problem
    //System.out.printf("Solving the DC-OPF problem for day %1$d \n", d+1);
    buc.solveOPF();
    dailycommitment = buc.getDailyCommitment();  //dailycommittment -> damcommittment
    dailylmp = buc.getDailyLMP();  //dailylmp -> damlmps
    dailyBranchFlow = buc.getDailyBranchFlow(); //dailybranchflow -> dambranchflow
    dailyPriceSensitiveDispatch=buc.getDailyPriceSensitiveDemand();
    

    ames.addLSEAgenPriceSensitiveDemandByDay(dailyPriceSensitiveDispatch);
    ames.addBranchFlowByDay(dailyBranchFlow);
    ames.addGenAgentCommitmentByDay(dailycommitment);
    ames.addGenAgentEmissionByDay(dailyemission);
    ames.addGenAgentEmTradeByDay(dailyemTrade);
    ames.addGenAgentEmTradePriceByDay(dailyemTradePrice);
    ames.addLMPByDay(dailylmp);
    ames.addHasSolutionByDay(buc.getHasSolution());
 }

  public void initialPost(int h, int d){
    //System.out.println("Hour " + h + " Day " + d +
    //                   ": Post hourly commitment schedule and hourly LMPs.");
    dam.post(dailycommitment,dailylmp,dailyPriceSensitiveDispatch,2,dailyemTrade);
  }

  public void produceCommitmentSchedule(int h, int d){
    System.out.println("Hour " + h + " Day " + d +
                       ": produce commitment schedule.");
  }

  public void DayAheadMarketCheckLastDayAction(){
      dam.checkGenLastDayAction();
  }

  // Get and set method

  public double[][] getSupplyOfferByGen(){
    return supplyOfferByGen;
  }
  public double[][] getTrueSupplyOfferByGen(){
    return supplyTrueOfferByGen;
  }
  public double[][] getLoadProfileByLSE(){
    return loadProfileByLSE;
  }
  
  public double[][][] getDemandBidByLSE(){
    return demandBidByLSE;
  }
  
  public int[][] getDemandHybridByLSE(){
    return demandHybridByLSE;
  }
  
  public DAMarket getDAMarket(){
    return dam;
  }
  public SRMarket getSRMarket(){
    return srm;
  }
  public RTMarket getRTMarket(){
    return rtm;
  }
  public FTRMarket getFTRMarket(){
    return ftrm;
  }
  public BUC getBUC(){
    return buc;
  }

}
