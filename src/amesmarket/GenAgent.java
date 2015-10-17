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

// GenAgent.java
// GenCo (wholesale power producer)

package amesmarket;

import java.awt.*;

import uchicago.src.sim.gui.*;
import java.util.ArrayList;
import java.math.*;
import uchicago.src.sim.util.SimUtilities;
import edu.iastate.jrelm.core.JReLMAgent;
import edu.iastate.jrelm.core.SimpleAction;
import edu.iastate.jrelm.rl.ReinforcementLearner;
import edu.iastate.jrelm.rl.SimpleStatelessLearner;
import edu.iastate.jrelm.rl.rotherev.variant.VREParameters;
import edu.iastate.jrelm.rl.rotherev.REPolicy;;



/** Example showing what genData[i] contains

GenData
//ID	atBus	FCost	A	B	CapMin	CapMax	 emcoe EA    pb ps InitMoney
  1	1	5	15	0.10	0	100	  1   10000   1  1   100000

 */

public class GenAgent implements Drawable, JReLMAgent{

  private static final int ID         = 0;
  private static final int AT_NODE    = 1;
  private static final int A          = 2;
  private static final int B          = 3;
  private static final int CAP_MIN    = 4;
  private static final int CAP_MAX    = 5;
  private static final int F_COST     =6;
  private static final int E_INDEX = 7;
  private static final int EA_INDEX = 8;
  private static final int PB_INDEX = 9;
  private static final int PS_INDEX = 10;
  private static final int INIT_MONEY = 11;
  private static final int HOURS_PER_DAY = 24;


  // GenCo's data

  private int xCoord;      // Coordinate x on trans grid
  private int yCoord;      // Coordinate y on trans grid

  private int id;            // GenCo's ID
  private int atBus;        // GenCo's location (at which bus)
  private double fixedCost;  // GenCo's fixed cost
  private double a;          // GenCo's (true) cost attribute
  private double b;          // GenCo's (true) cost attribute
  private double capMin;     // GenCo's (true) minimum production capacity limit
  private double capMax;     // GenCo's (true) maximum production capacity limit
  private double emcoe;   // GenCo's (true) emission coeffcient
  private double EA;   // GenCo's (true) maximum Emission Allowance
  private double pb;
  private double ps;
  private double[] trueSupplyOffer; // (a,b,capMin,capMax,IniMoney,emcoe,EA), true

  private double aReported;      // GenCo's reported cost attribute
  private double bReported;      // GenCo's reported cost attribute
  private double capMaxReported; // GenCo's reported maximum production capacity limit
  //private double FCostReported;
  //private double emcoeReported;   // GenCo's reported emission coeffcient
  //private double pb;
  //private double ps;

  //private double EAReported;// GenCo's reported maximum Emission Allowance
  private double[] reportedSupplyOffer;   // (aReported,bReported,capMin,capMaxReported), strategic

  // GenCo's records by hours (within a day)
  private double[] commitment;  // Day-Ahead hourly power commitment quantity
  private double[] emission;
  private double[] embuy;
  private double[] emsell;
  private double[] emTrade;
  private double[] dispatch;    // Real-Time hourly power dispatch quantity
  private double[] dayAheadLMP; // Day-Ahead hourly locational marginal price
  private double[] realTimeLMP; // Real-Time hourly locational marginal price
  private double[] totalVariableCost;  // totalVariableCost = A*power + B*power^2
  private double[] totalEmissionTrade;
  private double[] hourlyTotalCost;          // hourlyTotalCost = totalVariableCost + FCost

  private double[] hourlyVariableCost;// hourlyVariableCost[h] = a*power + b*power^2
  private double[] hourlyEmTradePrice;// hourlyVariableCost[h] = a*power + b*power^2
  private double[] hourlyNetEarning;// hourlyNetEarning[h] = dispatch[h]*lmp[h]
  private double[] hourlyEmTradeprice;
  private double[] hourlyProfit;// hourlyProfit[h] = dispatch[h]*lmp[h] - hourlyTotalCost[h]
  private double dailyNetEarnings;   // dailyNetEarnings = sum of hourlyNetEarning over 24 hours
  private double dailyProfit;   // dailyProfit = sum of hourlyProfit over 24 hours
  private double money;    // GenCo's accumulative money holding,
                           // money (new) = money(previous) + dailyProfit(new)

  private double[] hourlyRevenue;
  private double dailyRevenue;  // 
  private double dailyRevenueCE; // dailyRevenue under CE (Competitive Equil) case
  private double marketAdvantage; // marketAdvantage = (dailyNetGain - dailyNetGainCE)/dailyNetGainCE
  private double[] lernerIndex;  // lerner index (at dispatched level for each hour)
  private double[] marketPower;  // market power measure (at dispatched level for each hour)

  private double choiceProbability; // gen's learning choice probability
  private double choicePropensity;  // gen's learning choice Propensity
  private int    choiceID;  // gen's learning choice ID

  //GenCo's records by day
  private ArrayList commitmentByDay;
  private ArrayList emissionByDay;
  private ArrayList emissionTradeByDay;
  private ArrayList emTradePriceByDay;
  private ArrayList dispatchByDay;
  private ArrayList dayAheadLMPByDay;
  private ArrayList realTimeLMPByDay;

  // JReLM component
  private SimpleStatelessLearner learner;
  private int randomSeed;

  // Learning variables
  private double lowerRI; // lower Range Index
  private double upperRI; // upper Range Index
  private double upperRCap; // upper relative capacity
  private double slopeStart;
  
  private double priceCap;  // max price for LMP
  private int iRewardSelection; // 0->profit, 1->net earnings
  
  // Check if Action Probability Stable
  private int iStartDay;
  private int iCheckDayLength;
  private double dActionProbability;
  private boolean bActionProbabilityCheck;
  
  private boolean bActionProbabilityConverge;
  private int iCheckDayLengthCount;
  private int iDayCount;
  private double [] oldActionProbability;
  private double [] newActionProbability;

  // Check if Learning Result Stable
  private int iLearningCheckStartDay;
  private int iLearningCheckDayLength;
  private double dLearningCheckDifference;
  private boolean bLearningCheck;
 
  private boolean bLearningCheckConverge;
  private int iLearningCheckDayLengthCount;
  private int iLearningCheckDayCount;
  private double [] oldLearningResult;

  private boolean bDailyNetEarningConverge;
  private int iDailyNetEarningDayLengthCount;
  private int iDailyNetEarningDayCount;
  private boolean bDailyNetEarningThreshold;
  private double dDailyNetEarningThreshold;
  private int iDailyNetEarningStartDay;
  private int iDailyNetEarningDayLength;
  private double [] oldDailyNetEarningResult;
  
  private int iActinDomain;
  ArrayList newActionDomain;

  // Constructor
  public GenAgent(double[] genData, VREParameters learningParams,
                  ArrayList actionDomain, double ss, double dCap, int random, int iStart, int iLength, double dCheck, boolean bCheck,
                  int iLearnStart, int iLearnLength, double dLearnCheck, boolean bLearnCheck, double dEarningThreshold, boolean bEarningThresh, 
                  int iEarningStart, int iEarningLength, int iReward){

     xCoord = -1;
     yCoord = -1;

     // Parse genData
     id        = (int) genData[ID];
     atBus    = (int) genData[AT_NODE];
     a         = genData[A];
     b         = genData[B];
     fixedCost = genData[F_COST];
     capMin    = genData[CAP_MIN];
     capMax    = genData[CAP_MAX];
     emcoe     = genData[E_INDEX];
     EA        = genData[EA_INDEX];
     pb        = genData[PB_INDEX];
     ps        = genData[PS_INDEX];
     money     = genData[INIT_MONEY];


     // trueSupplyOffer = (a, b, capMin, capMax,fixedCost,emcoe,EA)
     trueSupplyOffer = new double[PS_INDEX-A+1];
     for(int i=0; i<PS_INDEX-A+1; i++){
       trueSupplyOffer[i] = genData[i+A];
     }

     // Initialize reportedSupplyOffer to all zeros
     // reportedSupplyOffer = (aReported, bReported, capMin, capMaxReported,fixedReport)
     reportedSupplyOffer = new double[trueSupplyOffer.length];

     commitment = new double[HOURS_PER_DAY];
     emTrade    = new double[HOURS_PER_DAY];
     emission   =new double[HOURS_PER_DAY];
     embuy   =new double[HOURS_PER_DAY];
     emsell   =new double[HOURS_PER_DAY];
     dispatch = new double[HOURS_PER_DAY];
     dayAheadLMP = new double[HOURS_PER_DAY];
     realTimeLMP = new double[HOURS_PER_DAY];
     totalVariableCost = new double[HOURS_PER_DAY];
     
     hourlyTotalCost = new double[HOURS_PER_DAY];
     hourlyProfit = new double[HOURS_PER_DAY];
     hourlyNetEarning = new double[HOURS_PER_DAY];
     hourlyVariableCost = new double[HOURS_PER_DAY];
     hourlyEmTradeprice=new double[HOURS_PER_DAY];
     hourlyEmTradePrice=new double[HOURS_PER_DAY];
     hourlyRevenue = new double[HOURS_PER_DAY];



     dailyRevenue = 0;
     dailyRevenueCE = 0;
     marketAdvantage = 0;
     lernerIndex = new double[HOURS_PER_DAY];
     marketPower = new double[HOURS_PER_DAY];

    // Create historical data records (initialized all to zeros)
    commitmentByDay = new ArrayList();
    emissionTradeByDay=new ArrayList();
    emTradePriceByDay=new ArrayList();
    emissionByDay= new ArrayList();
    dispatchByDay = new ArrayList();
    dayAheadLMPByDay = new ArrayList();
    realTimeLMPByDay = new ArrayList();

    randomSeed = random;
    slopeStart = ss;
    priceCap=dCap;
    iStartDay=iStart;
    iCheckDayLength=iLength;
    dActionProbability=dCheck;
    bActionProbabilityCheck=bCheck;
    iCheckDayLengthCount=0;
    iDayCount=1;
    bActionProbabilityConverge=false;
        
    iLearningCheckStartDay=iLearnStart;
    iLearningCheckDayLength=iLearnLength;
    dLearningCheckDifference=dLearnCheck;
    bLearningCheck=bLearnCheck;
    bLearningCheckConverge=false;
    iLearningCheckDayLengthCount=0;
    iLearningCheckDayCount=1;
    oldLearningResult=new double[3];
        
    bDailyNetEarningConverge=false;
    iDailyNetEarningDayLengthCount=0;
    iDailyNetEarningDayCount=1;
    dDailyNetEarningThreshold=dEarningThreshold;
    bDailyNetEarningThreshold=bEarningThresh;
    iDailyNetEarningStartDay=iEarningStart;
    iDailyNetEarningDayLength=iEarningLength;
    oldDailyNetEarningResult=new double[iDailyNetEarningDayLength];
    
    iRewardSelection=iReward;
    
    newActionDomain = checkActionDomain(actionDomain);
    //System.out.println("\nActionDomain Size "+newActionDomain.size()+" for GenCo :"+id);
    learner = new SimpleStatelessLearner(learningParams, newActionDomain);
    
    iActinDomain=newActionDomain.size();
    oldActionProbability=new double[iActinDomain];
    newActionProbability=new double[iActinDomain];
    
    //System.out.println("GenCo ID="+id+" maxmum profit="+getMaxPotentialProfit());
  }

  public ArrayList checkActionDomain(ArrayList actionList){
    ArrayList newActionList=new ArrayList();
    for(int i=0; i<actionList.size(); i++){
        double [] action=(double [])actionList.get(i);
        double [] newAction=action.clone();
        
        if(!checkOverPriceCap(newAction))
            newActionList.add(newAction);
    }
    
    return newActionList;
  }
  
  public boolean checkOverPriceCap(double [] action){
    double lRI=action[0];
    double uRI=action[1];
    double uRCap=action[2];
    // Step 0: To get capMaxCalculated
    double capMaxCalculated = uRCap * (capMax - capMin) + capMin;

    // Step 1: To get lR
    double lR = (a + 2*b*capMin)/(1 - lRI);

    // Step 2: To get uStart
    double u = a + 2*b*capMaxCalculated;
    double uStart;
    if(lR < u){
      uStart = u;
    }
    else{
      uStart = lR + slopeStart;
    }

    if(uStart>=priceCap){
       return true;
        
    }
    
    // Step 3: To get uR
    double uR = uStart/(1 - uRI);

    // Step 4: To get bReported
    action[1] = 0.5*((uR - lR)/(capMaxCalculated - capMin));

    // Step 5: To get aReported
    action[0] = lR - 2*action[1]*capMin;
     
    // for PriceCap
    double maxPrice=action[0]+2*action[1]*capMaxCalculated;
    if(maxPrice>priceCap)
        action[2]=(priceCap-action[0])/(2*action[1]);
    else
        action[2]=capMaxCalculated;
    
    return false;
}

  public double getMaxPotentialProfit( ){
    int iMaxActionIndex=0;
    double aRMax=0.0;
    double bRMax=0.0;
    double CapPriceMax=0.0; // for maximum capacity output price
    double CapMax=0.0;
    double dMaxProfit=0.0;
    
    for(int i=0; i<newActionDomain.size(); i++){
        double [] action=(double [])newActionDomain.get(i);
    
        double aR=action[0];
        double bR=action[1];
        double capMaxCalculated=action[2];
        // calculate max price
        double maxPrice=aR+2*bR*capMaxCalculated;
        if(maxPrice>CapPriceMax){
            aRMax=aR;
            bRMax=bR;
            CapPriceMax=maxPrice;
            CapMax=capMaxCalculated;
            iMaxActionIndex=i;
       }
    }
    
    dMaxProfit=CapPriceMax*CapMax-0.5*(a+a+2*b*capMax)*capMax;
    //System.out.println("aRMax="+aRMax+" bRMax="+bRMax+" CapMax="+CapMax+" CapPriceMax="+CapPriceMax);
    return dMaxProfit;
  }

  public void chooseNextAction(){
    // Use chooseActionRaw to get the unwrapped supply offer values
    double[] actionTriplet = (double[]) learner.chooseActionRaw();
    aReported   = actionTriplet[0];
    bReported   = actionTriplet[1];
    capMaxReported = actionTriplet[2];
  }

  public double[] submitSupplyOffer(){
    //calculateReportedSupplyOfferValues();
    reportedSupplyOffer[0] = aReported;
    reportedSupplyOffer[1] = bReported;
    reportedSupplyOffer[2] = capMin;
    reportedSupplyOffer[3] = capMaxReported;
    reportedSupplyOffer[4] = fixedCost;
    reportedSupplyOffer[5] = emcoe;
    reportedSupplyOffer[6] = EA;
    reportedSupplyOffer[7] = pb;
    reportedSupplyOffer[8] = ps;
    return reportedSupplyOffer;
  }

  // Refer to DynTest paper Appendix 5.2 "Implementation of GenAgent's Learning"
  private void calculateReportedSupplyOfferValues(){

    // Step 0: To get capMaxReported
    capMaxReported = upperRCap * (capMax - capMin) + capMin;

    // Step 1: To get lR
    double lR = (a + 2*b*capMin)/(1 - lowerRI);

    // Step 2: To get uStart
    double u = a + 2*b*capMaxReported;
    double uStart;
    if(lR < u){
      uStart = u;
    }
    else{
      uStart = lR + slopeStart;
    }

    // Step 3: To get uR
    double uR = uStart/(1 - upperRI);

    // Step 4: To get bReported
    bReported = 0.5*((uR - lR)/(capMaxReported - capMin));

    // Step 5: To get aReported
    aReported = lR - 2*bReported*capMin;
     
    // for PriceCap
    double maxPrice=aReported+2*bReported*capMaxReported;
    //System.out.println("\n Before maxPrice "+maxPrice+" for GenCo :"+id);
    if(maxPrice>priceCap)
        capMaxReported=(priceCap-aReported)/(2*bReported);
    //maxPrice=aReported+2*bReported*capMaxReported;
    //System.out.println("\n After maxPrice "+maxPrice+" for GenCo :"+id+"\n");
   
  }

  public double[] submitTrueSupplyOffer(){
    double [] trueOffer=new double [9];
    // for PriceCap
    double maxPrice=trueSupplyOffer[0]+2*trueSupplyOffer[1]*trueSupplyOffer[3];

    trueOffer[0]=trueSupplyOffer[0];
    trueOffer[1]=trueSupplyOffer[1];
    trueOffer[2]=trueSupplyOffer[2];
    trueOffer[3]=trueSupplyOffer[3];
    trueOffer[4]=trueSupplyOffer[4];
    trueOffer[5]=trueSupplyOffer[5];
    trueOffer[6]=trueSupplyOffer[6];
    trueOffer[7]=trueSupplyOffer[7];
    trueOffer[8]=trueSupplyOffer[8];
    
    if(priceCap<trueOffer[0]){
        trueOffer[2]=0;
        trueOffer[3]=0;
        
        return trueOffer;
    }
    
    if(maxPrice<=priceCap){
        trueOffer[3]=trueSupplyOffer[3];
    }
    else
        trueOffer[3]=(priceCap-trueSupplyOffer[0])/(2*trueSupplyOffer[1]);

    return trueOffer;
  }


  public void updateSupplyOffer(int flag){
    updateProfit();

    if(flag != 1){
      updateMoney();
    }

    learn();
    
    iDayCount++;
    if(bActionProbabilityCheck&&(iDayCount>=iStartDay)){
        updateActionProbabilities();
    }
    
    iLearningCheckDayCount++;
    if(bLearningCheck&&(iLearningCheckDayCount>=iLearningCheckStartDay)){
        updateLearningResult();
    }
    
    iDailyNetEarningDayCount++;
    if(bDailyNetEarningThreshold&&(iDailyNetEarningDayCount>=iDailyNetEarningStartDay)){
        updateDailyNetEarningResult();
    }
  }

  private void updateActionProbabilities(){
      REPolicy policy = (REPolicy)learner.getPolicy();
      double [] dProbability=policy.getProbabilities();
      
      boolean bConverged=true;
      for(int i=0; i<iActinDomain; i++){
          oldActionProbability[i]=newActionProbability[i];
          newActionProbability[i]=dProbability[i];
          if((bConverged)&&(Math.abs(newActionProbability[i]-oldActionProbability[i])>dActionProbability)){
              bConverged=false;
              iCheckDayLengthCount=0;
              bActionProbabilityConverge=false;
          }
      }
      
      if(bConverged){
          iCheckDayLengthCount++;
          if(iCheckDayLengthCount>iCheckDayLength)
              bActionProbabilityConverge=true;
      }
  }

  private void updateLearningResult(){
      
      boolean bConverged=true;
      
      if(Math.abs(oldLearningResult[0]-aReported)>dLearningCheckDifference)
          bConverged=false;
      
      if(Math.abs(oldLearningResult[1]-bReported)>dLearningCheckDifference)
          bConverged=false;
      
      if(Math.abs(oldLearningResult[2]-capMaxReported)>dLearningCheckDifference)
          bConverged=false;
      
      oldLearningResult[0]=aReported;
      oldLearningResult[1]=bReported;
      oldLearningResult[2]=capMaxReported;
      
      if(bConverged){
          iLearningCheckDayLengthCount++;
          if(iLearningCheckDayLengthCount>iLearningCheckDayLength)
              bLearningCheckConverge=true;
      }
      else{
          iLearningCheckDayLengthCount=0;
          bLearningCheckConverge=false;
      }
  }

  private void updateDailyNetEarningResult(){
      
      boolean bConverged=true;
      
      for(int i=0; i<iDailyNetEarningDayLengthCount; i++){
          if(Math.abs(oldDailyNetEarningResult[i]-dailyNetEarnings)>dDailyNetEarningThreshold){
              bConverged=false;
              break;
          }
      }
      
      if(bConverged){
          iDailyNetEarningDayLengthCount++;
          if(iDailyNetEarningDayLengthCount>=iDailyNetEarningDayLength){
              bDailyNetEarningConverge=true;
              iDailyNetEarningDayLengthCount=iDailyNetEarningDayLength;
              
              for(int j=0; j<iDailyNetEarningDayLengthCount-1; j++){
                  oldDailyNetEarningResult[j]=oldDailyNetEarningResult[j+1];
              }
              oldDailyNetEarningResult[iDailyNetEarningDayLengthCount-1]=dailyNetEarnings;
          }
          else
              oldDailyNetEarningResult[iDailyNetEarningDayLengthCount-1]=dailyNetEarnings;
      }
      else{
          oldDailyNetEarningResult[0]=dailyNetEarnings;
          iDailyNetEarningDayLengthCount=0;
          bDailyNetEarningConverge=false;
      }
  }

  // Lerner Index = (LMP - MC)/LMP
  private void computeLernerIndex(){
    for(int h=0; h<HOURS_PER_DAY; h++){
      lernerIndex[h] = (dayAheadLMP[h] - (a + 2*b*commitment[h]))/dayAheadLMP[h];
    }
    System.out.println(getID()+" Lerner Index at each hour: ");
    Support.print2dpByRow(lernerIndex);
  }

  // Market Power = (LMP - MC)/MC
  private void computeMarketPower(){
    for(int h=0; h<HOURS_PER_DAY; h++){
      marketPower[h] = (dayAheadLMP[h] - (a + 2*b*commitment[h]))/(a + 2*b*commitment[h]);
    }
    System.out.println(getID()+" market power at each hour: ");
    Support.print2dpByRow(marketPower);
  }


  public void updateProfit(){
    dailyProfit = 0;
    dailyNetEarnings=0;
    dailyRevenue=0;
    
    for(int h=0; h<HOURS_PER_DAY; h++){
        if (emTrade[h]>=0)
            hourlyEmTradePrice[h]=emTrade[h]*pb;
        else
            hourlyEmTradePrice[h]=emTrade[h]*ps;
        hourlyVariableCost[h] = a * commitment[h] + b * commitment[h] * commitment[h]+hourlyEmTradePrice[h];
        hourlyTotalCost[h] = hourlyVariableCost[h]+ fixedCost;
        hourlyRevenue[h] = commitment[h]*dayAheadLMP[h];
        
        hourlyProfit[h] = hourlyRevenue[h] - hourlyTotalCost[h];
        hourlyNetEarning[h] = hourlyRevenue[h] - hourlyVariableCost[h];
        dailyProfit += hourlyProfit[h];
        dailyNetEarnings += hourlyNetEarning[h];
        dailyRevenue += hourlyRevenue[h];
    }
    
    
    //System.out.println("GenCo "+getGenID()+" daily dailyProfit: "+Support.roundOff(dailyProfit,2));
  }

  private void computeTotalCost(){
    for(int h=0; h<HOURS_PER_DAY; h++){
      hourlyTotalCost[h] = a * commitment[h] + b * commitment[h] * commitment[h] + fixedCost+pb*embuy[h] -ps*emsell[h];
    }
  }

  private void updateMoney(){
    money = money + dailyProfit;
    //System.out.println("GenCo "+getGenID()+" money holdings: "+Support.roundOff(money,2));
  }

  // genAgent learning (updating propensity based on current period dailyProfit)
  private void learn(){
    //System.out.println("Learning Report for GenCo: " + getID() );
    SimpleAction lastAction = (SimpleAction) learner.getPolicy().getLastAction();
    choiceID = lastAction.getID();
    double[] act = (double[]) lastAction.getAct();
    REPolicy policy = (REPolicy)learner.getPolicy();
/*
    System.out.printf("\tLast action chosen:  id= " + id +
                       ";\t(lowerRI, upperRI, upperRCap)=(%1$6.4f, %2$6.4f, %3$6.4f)\n",
                       act[0], act[1], act[2]);
    
    if(Double.isNaN(policy.getProbability(id))){
        System.out.printf("\tBefore update --> the policy.getProbability return value is not a number!!!\n");

        System.out.printf("\tBefore updating with daily profit: probability=%1$6.4f\tpropensity=%2$f\n",
                         1.0,policy.getPropensity(id));
    }
    else
        System.out.printf("\tBefore updating with daily profit: probability=%1$6.4f\tpropensity=%2$f\n",
                         policy.getProbability(id),policy.getPropensity(id));
 */
    if(iRewardSelection==0)// profit
        learner.update(new Double(dailyProfit));

    if(iRewardSelection==1)// net earnings
        learner.update(new Double(dailyNetEarnings));
/*
    if(Double.isNaN(policy.getProbability(id))){
        System.out.printf("\tAfter update --> the policy.getProbability return value is not a number!!!\n");
        
        System.out.printf("\tAfter updating with daily profit:  probability=%1$6.4f\tpropensity=%2$f\n\n",
                         1.0,policy.getPropensity(id));
    }
    else
        System.out.printf("\tAfter updating with daily profit:  probability=%1$6.4f\tpropensity=%2$f\n\n",
                         policy.getProbability(id),policy.getPropensity(id));
    
*/
    choiceProbability = policy.getProbability(choiceID);
    choicePropensity=policy.getPropensity(choiceID);

  }


  public boolean isSolvent(){
    boolean solvency = true;
    if(money < 0){
      solvency = false;
      System.out.println("GenCo "+getGenID()+" is out of market.");
    }
    return solvency;
  }
  
  public double [] LastDayCheckAction( ){
    REPolicy policy = (REPolicy)learner.getPolicy();
    double [] dProbability=policy.getProbabilities();

    int iAction=newActionDomain.size();
    double [] action=new double[iAction];
      
    for(int i=0; i<newActionDomain.size(); i++){
        action[i]=dProbability[i];
    }

     return action;
 }
    
  

public boolean isLearningResultConverge(){
    return bLearningCheckConverge;
}

public boolean isLearningResultCheck(){
    return bLearningCheck;
}


public boolean isActionProbabilityConverge(){
    return bActionProbabilityConverge;
}

public boolean isActionProbabilityCheck(){
    return bActionProbabilityCheck;
}

public boolean isDailyNetEarningConverge(){
    return bDailyNetEarningConverge;
}

public boolean isDailyNetEarningCheck(){
    return bDailyNetEarningThreshold;
}

public int getStartDay(){
    return iStartDay;
}

public int getCheckDayLength(){
    return iCheckDayLength;
}

  // GenCo's get and set methods
  public void setXY(int newX, int newY){
    xCoord = newX;
    yCoord = newY;
  }

  public int getGenID(){ // This method name cannot be changed to "int getID"
    return id;           // because it'll conflict with JReLM interface method "String getID"
  }
  public int getAtNode(){
    return atBus;
  }

  public double[] getSupplyOffer(){
    return reportedSupplyOffer;
  }

  public double getMoney(){
    return money;
  }

  public double[] getHourlyRevenue(){
    return hourlyRevenue;
  }
  
  public double[] getHourlyProfit(){
    return hourlyProfit;
  }
  
  public double[] getHourlyNetEarning(){
    return hourlyNetEarning;
  }
  public double[] getCommitment(){
    return commitment;
  }
  public double[] getEmTrade(){
  return emTrade;
  }
  public double[] getEmission(){
  return emission;
  }
  public double[] getHourlyEmTradePrice(){
  return hourlyEmTradePrice;
  }

  public void setCommitment(double[] comm){
    commitment = comm;
  }
  
  public void setEmTrade(double[] emtrade){
    emTrade = emtrade;
  }
  


  public double[] getDayAheadLMP(){
    return dayAheadLMP;
  }
  public void setDayAheadLMP(double[] lmprice){
    dayAheadLMP = lmprice;
  }
  public ArrayList getCommitmentByDay(){
    return commitmentByDay;
  }
  public ArrayList getEmissionByDay(){
  return emissionByDay;
  }
  public ArrayList getEmTradeByDay(){
  return emissionTradeByDay;
  }
  public ArrayList getEmTradePriceByDay(){
  return emTradePriceByDay;
  }
  public ArrayList getDayAheadLMPByDay(){
    return dayAheadLMPByDay;
  }

  public double getChoiceProbability(){
    return choiceProbability;
  }

  public double getChoicePropensity(){
    return choicePropensity;
  }

  public int getChoiceID(){
    return choiceID;
  }

  public void report(){
    System.out.println(getID() + " at (" + xCoord + "," + yCoord + ") has "
                       + "current money holding " + money);
  }


  // Implemented methods for JReLMAgent interface
  public String getID(){
    return "GenCo" + id;
  }
  public ReinforcementLearner getLearner() {
      return learner;
  }

  // Implemented methods for Drawable interface
  public int getX(){ return xCoord;}
  public int getY(){ return yCoord;}
  public void draw(SimGraphics sg){
    sg.drawFastRoundRect(Color.blue);         //GEN is blue colored
  }


   public double getDailyRevenue() {
        return dailyRevenue;
    }

    public double getProfit() {
        return dailyProfit;
    }

    public double getNetEarning() {
        return dailyNetEarnings;
    }

    
}
