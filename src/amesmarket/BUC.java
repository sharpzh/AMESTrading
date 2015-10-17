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

// BUC.java


package amesmarket;

import java.util.ArrayList;

public class BUC {

  // BUC's data

  private int K; // numNodes
  private int N; // numBranches
  private int I; // numGenAgents;
  private int J; // numLSEAgents;
  private int M; // numBranches;
  private int H; // numHoursPerDay = # of load profiles for each LSE ;

  private double[][] dailyCommitment;     // daily commitments (24 hours by row)
  private double[][] dailyEmission;
  private double[][] dailyEmTrade;
  private double[][] dailyEmTradePrice;
  private double[][] dailyVoltAngle;      // daily voltage angles (delta)
  private double[][] dailyLMP;            // daily LMPs
  private double[] dailyMinTVC;
  private double[][] dailyBranchFlow;
  private double[][] dailyPriceSensitiveDemand;
  
  private boolean [] bDCOPFHasSolution;

  private AMESMarket ames;
  private ISO iso;
  private DCOPFJ opf;
  
  private double[] ineqMultiplier;
  private String [] ineqMultiplierName;

  // Index for supplyOfferByGen parameters, i.e., in the form of {A,B,CapMin,CapMax,fixcost,emcoe,EA,pb,ps}
  private static final int A_INDEX    = 0;
  private static final int B_INDEX    = 1;
  private static final int CAP_MIN    = 2;
  private static final int CAP_MAX    = 3;
  private static final int F_COST     = 4;
  private static final int E_INDEX    = 5;
  private static final int EA_INDEX   = 6;
  private static final int PB_INDEX   = 7;
  private static final int PS_INDEX   = 8;
  //private static final int A0_INDEX   = 9;
  //private static final int B0_INDEX   = 10;
  //private static final int E0_INDEX   = 11;
  double[][] supplyOfferByGen;
  double[][] supplyTrueOfferByGen;

  // Index for psDemandBidByLSE parameters, i.e., in the form of {C,D,DemandMax}
  private static final int C_INDEX    = 0;
  private static final int D_INDEX    = 1;
  private static final int DEMAND_MAX    = 2;
  double[][] psDemandBidByLSE;
        
  double[][] dailySLoad = new double[H][J]; // in price-sensitive demand case only

  // constructor
  public BUC(ISO independentSystemOperator, AMESMarket model){
    ames = model;
    iso = independentSystemOperator;

    K = ames.getNumNodes();
    N = ames.getNumBranches();
    I = ames.getNumGenAgents();
    J = ames.getNumLSEAgents();
    H = ames.NUM_HOURS_PER_DAY;  // H=24

    dailyCommitment = new double[H][I];
    dailyEmission   = new double[H][I];
    dailyEmTrade    = new double[H][I];
    dailyEmTradePrice= new double[H][I];
    dailyVoltAngle = new double[H][K];
    dailyLMP        = new double[H][K];
    dailyMinTVC = new double[H];
    dailyBranchFlow = new double[H][N];
    dailyPriceSensitiveDemand = new double [H][J];
    
    bDCOPFHasSolution = new boolean[24];
  }

  /**
   * Solve OPF DC approximation problem by invoking DCOPFJ
   */
  public void solveOPF(){

    supplyOfferByGen = iso.getSupplyOfferByGen();
    supplyTrueOfferByGen=iso.getTrueSupplyOfferByGen();
    
    // Store supplyOfferByGen to dSupplyOfferByGen for later check
    int iRow=supplyOfferByGen.length;
    int iCol=supplyOfferByGen[0].length;
    double [][] dSupplyOfferByGen=new double[iRow][iCol];
    for(int i=0; i<iRow; i++)
        for(int j=0; j<iCol; j++)
            dSupplyOfferByGen[i][j]=supplyOfferByGen[i][j];

     double dMinGenCapacity=0.0;
     double dMaxGenCapacity=0.0;
     double dMaxGenEA=0.0;
    
     // SI to PU conversion for supply offer and load profile
    
     for(int i=0; i<supplyOfferByGen.length; i++){
      dMinGenCapacity+=supplyOfferByGen[i][CAP_MIN];
      dMaxGenCapacity+=supplyOfferByGen[i][CAP_MAX];
      //dMaxGenEA+=supplyOfferByGen[i][EA_INDEX];
      //System.out.println("\n supplyOfferByGen-> dMaxGenCapacity "+supplyOfferByGen[i][CAP_MAX]+" for GenCo :"+i);

      // Convert A from SI to PU-adjusted
      supplyOfferByGen[i][A_INDEX] = supplyOfferByGen[i][A_INDEX]*INIT.getBaseS();

      // Convert B from SI to PU-adjusted
      supplyOfferByGen[i][B_INDEX] = supplyOfferByGen[i][B_INDEX]*INIT.getBaseS()*INIT.getBaseS();

      // Convert CapMin from SI to PU
      supplyOfferByGen[i][CAP_MIN] = supplyOfferByGen[i][CAP_MIN]/INIT.getBaseS();

      // Convert CapMax from SI to PU
      supplyOfferByGen[i][CAP_MAX] = supplyOfferByGen[i][CAP_MAX]/INIT.getBaseS();


      //supplyOfferByGen[i][A0_INDEX]=supplyTrueOfferByGen[i][A_INDEX];
      //supplyOfferByGen[i][B0_INDEX]=supplyTrueOfferByGen[i][B_INDEX];
      //supplyOfferByGen[i][E0_INDEX]=supplyTrueOfferByGen[i][E_INDEX];

      double Cpmin=0;
      double Cpmax=0;
      double EAnew=0;
      double CapL=0;
      double CapU=0;

      CapL=supplyTrueOfferByGen[i][CAP_MIN];
      CapU=supplyTrueOfferByGen[i][CAP_MAX];
      Cpmin = supplyTrueOfferByGen[i][A_INDEX]*supplyTrueOfferByGen[i][CAP_MIN]
                +supplyTrueOfferByGen[i][B_INDEX]*supplyTrueOfferByGen[i][CAP_MIN]*supplyTrueOfferByGen[i][CAP_MIN]
                +supplyTrueOfferByGen[i][F_COST];

      Cpmax = supplyTrueOfferByGen[i][A_INDEX]*supplyTrueOfferByGen[i][CAP_MAX]
                +supplyTrueOfferByGen[i][B_INDEX]*supplyTrueOfferByGen[i][CAP_MAX]*supplyTrueOfferByGen[i][CAP_MAX]
                +supplyTrueOfferByGen[i][F_COST];


      //Calculate the NEW e and EAnew
      EAnew=supplyOfferByGen[i][EA_INDEX]-supplyOfferByGen[i][E_INDEX]*Cpmin;

      supplyOfferByGen[i][E_INDEX]=supplyOfferByGen[i][E_INDEX]*(Cpmax-Cpmin)/(CapU-CapL);

      supplyOfferByGen[i][PB_INDEX] = supplyOfferByGen[i][PB_INDEX]*INIT.getBaseS();
      supplyOfferByGen[i][PS_INDEX] = supplyOfferByGen[i][PS_INDEX]*INIT.getBaseS();

      supplyOfferByGen[i][EA_INDEX]=(EAnew+supplyOfferByGen[i][E_INDEX]*CapL);


     //System.out.println(supplyOfferByGen[i][A_INDEX]);
     // System.out.println(supplyOfferByGen[i][B_INDEX]);
       //System.out.println(supplyOfferByGen[i][CAP_MAX]);
       //System.out.println(supplyOfferByGen[i][CAP_MIN]);
       //System.out.println(supplyTrueOfferByGen[i][CAP_MAX]);
       //System.out.println(supplyTrueOfferByGen[i][CAP_MIN]);

     //System.out.println(supplyOfferByGen[i][E_INDEX]);
     //System.out.println(supplyTrueOfferByGen[i][E_INDEX]);
    //System.out.println(supplyOfferByGen[i][EA_INDEX]);
     //System.out.println(supplyTrueOfferByGen[i][EA_INDEX]);
     }
     //System.out.println();

    supplyOfferByGen = Support.correctRoundingError(supplyOfferByGen);

    int[] atNodeByGen = new int[I];
    for(int i=0; i<I; i++){
      GenAgent gen = (GenAgent) ames.getGenAgentList().get(i);
      atNodeByGen[i] = gen.getAtNode();
    }
    
    int[] atNodeByLSE = new int[J];
    for(int j=0; j<J; j++){
      LSEAgent lse = (LSEAgent) ames.getLSEAgentList().get(j);
      atNodeByLSE[j] = lse.getAtNode();
    }

    double [][][] priceSensitiveDemandBidByLSE = iso.getDemandBidByLSE();
    iRow=priceSensitiveDemandBidByLSE.length;
    iCol=priceSensitiveDemandBidByLSE[0][0].length;

    //psDemandBidByLSE = Support.correctRoundingError(psDemandBidByLSE);
    
    double [] dLoad=new double[24]; // Total Demand
    for (int h=0; h<H; h++){
      //NOTE: phaseAngle is assumed to be zero at first bus, i.e. phaseAngle[0]=0

      double[] hourlyLoadProfileByLSE = new double[J];
      int[] hourlyLoadHybridFlagByLSE = new int[J];
      
      dLoad[h] = 0.0;
      for(int j=0; j<J; j++){
        hourlyLoadProfileByLSE[j] = iso.getLoadProfileByLSE()[j][h];
        // Calculate total demand
        hourlyLoadHybridFlagByLSE[j] = iso.getDemandHybridByLSE()[j][h];
        
        if((hourlyLoadHybridFlagByLSE[j]&1)==1){
            dLoad[h] += hourlyLoadProfileByLSE[j];
        }
        
      }
      
        psDemandBidByLSE=new double[iRow][iCol];
        for(int i=0; i<iRow; i++)
                for(int j=0; j<iCol; j++)
                    psDemandBidByLSE[i][j]=priceSensitiveDemandBidByLSE[i][h][j];

        // SI to PU conversion for price sensitive demand
        for(int i=0; i<psDemandBidByLSE.length; i++){
              // Convert C from SI to PU-adjusted
              psDemandBidByLSE[i][C_INDEX] = psDemandBidByLSE[i][C_INDEX]*INIT.getBaseS();

              // Convert D from SI to PU-adjusted
              psDemandBidByLSE[i][D_INDEX] = psDemandBidByLSE[i][D_INDEX]*INIT.getBaseS()*INIT.getBaseS();

              // Convert DemandMax from SI to PU
              psDemandBidByLSE[i][DEMAND_MAX] = psDemandBidByLSE[i][DEMAND_MAX]/INIT.getBaseS();
        }
        
      boolean bCheckMinMaxGenCapacityOK=true;
        if(dMinGenCapacity>dLoad[h]){
            System.out.println("GenCo total reported lower required operating capacity is greater than total fixed demand at hour "+h+" \n");
            bCheckMinMaxGenCapacityOK=false;
        }
        
        if(dMaxGenCapacity<dLoad[h]){
            System.out.println("GenCo total reported upper operating capacity under supply-offer price cap is less than total fixed demand at hour "+h+"\n");
            bCheckMinMaxGenCapacityOK=false;
        }

      if(bCheckMinMaxGenCapacityOK){
          for(int j=0; j<J; j++){
            // Convert hourly LP from SI to PU
            hourlyLoadProfileByLSE[j] = hourlyLoadProfileByLSE[j]/INIT.getBaseS();
          }

          hourlyLoadProfileByLSE = Support.correctRoundingError(hourlyLoadProfileByLSE);

          opf = new DCOPFJ(supplyOfferByGen, psDemandBidByLSE, hourlyLoadProfileByLSE, hourlyLoadHybridFlagByLSE,
                    atNodeByGen, atNodeByLSE, ames.getTransGrid(),supplyTrueOfferByGen);

          bDCOPFHasSolution[h] = opf.getIsSolutionFeasibleAndOptimal();
          dailyCommitment[h] = opf.getCommitment();
          dailyEmission[h]  = opf.getEmission();
          dailyEmTrade[h]  =opf.getEmissionTrade();
          dailyEmTradePrice[h]=opf.getEmissionTradePrice();
          dailyVoltAngle[h] = opf.getVoltAngle();
          dailyLMP[h]        = opf.getLMP();
          dailyMinTVC[h] = opf.getMinTVC();
          dailyBranchFlow[h] = opf.getBranchFlow();
          dailyPriceSensitiveDemand[h] = opf.getSLoad();
          
          if(h==17) {// get inequality multiplier
              ineqMultiplier=opf.getIneqMultiplier();
              ineqMultiplierName=opf.getIneqMultiplierName();
          }
      }
      else{
          bDCOPFHasSolution[h]=false;
          double[] commitment = new double[I];   // in MWs
          double[] emission = new double[I];
          double[] emTrade = new double[I];
          double[] emTradePrice = new double[I];
          double[] voltAngle = new double[K-1];  // in radians
          double[] lmp        = new double[K];
          double[] branchFlow = new double[N]; //in MWs
          double[] psLoad = new double[J]; //in MWs
          
          dailyCommitment[h] = commitment;
          dailyEmission[h] = emission;
          dailyEmTrade[h] = emTrade;
          dailyEmTradePrice[h] = emTradePrice;
          dailyVoltAngle[h] = voltAngle;
          dailyLMP[h]        = lmp;
          dailyMinTVC[h] = 0.0;
          dailyBranchFlow[h] = branchFlow;
          dailyPriceSensitiveDemand[h] = psLoad;
      }
    }
    
    for (int h=0; h<H; h++){
        if(!bDCOPFHasSolution[h])
            System.out.println("  At hour "+h+" DCOPF has no solution!");
    }

 /*Temperory remove output ---------------------------------------    
    System.out.println("  Daily (24 Hour) branch flow for each branch");
    String strTemp=String.format("%1$15s", "Hour");
    for(int i = 0; i<N; i++){
        String lineName="branch"+(i+1);
        strTemp+=String.format("\t%1$15s", lineName);
    }
    System.out.println(strTemp);
    
    for(int i = 0; i<dailyBranchFlow.length; i++){
      System.out.printf("%1$15d", i);
      for(int j = 0 ; j<dailyBranchFlow[0].length; j++){
          System.out.printf("\t%1$15.2f", Support.roundOff(dailyBranchFlow[i][j],2));
        }
        System.out.println();
    }
    System.out.println();
 
    System.out.println("  Daily (24 Hour) LMP for each bus");
    strTemp=String.format("%1$15s", "Hour");
    for(int i = 0; i<K; i++){
        String nodeName="bus"+(i+1);
        strTemp+=String.format("\t%1$15s", nodeName);
    }
    System.out.println(strTemp);
    for(int i = 0; i<dailyLMP.length; i++){
      System.out.printf("%1$15d", i);
      for(int j = 0 ; j<dailyLMP[0].length; j++){
          System.out.printf("\t%1$15.2f", Support.roundOff(dailyLMP[i][j],2));
        }
        System.out.println();
    }
    System.out.println();
    
    ///
    System.out.println("  (17 Hour) inequality multiplier:");
    strTemp="";
    for(int i = 0; i<ineqMultiplier.length; i++){
        strTemp+=String.format("\t%1$10s", ineqMultiplierName[i]);
    }
    System.out.println(strTemp);
    strTemp="";
    for(int i = 0; i<ineqMultiplier.length; i++){
        strTemp+=String.format("\t%1$10.5f", ineqMultiplier[i]);
    }
    System.out.println(strTemp);
    System.out.println();

    
    System.out.println("  Daily (24 Hour) commitment for each generator");
    strTemp=String.format("%1$15s", "Hour");
    for(int i = 0; i<I; i++){
        String genName="GenCo"+(i+1);
        strTemp+=String.format("\t%1$15s", genName);
    }
    System.out.println(strTemp);
    for(int i = 0; i<dailyCommitment.length; i++){
      System.out.printf("%1$15d", i);
      for(int j = 0 ; j<dailyCommitment[0].length; j++){
          System.out.printf("\t%1$15.2f", Support.roundOff(dailyCommitment[i][j],2));
        }
        System.out.println();
    }
    System.out.println();

    System.out.println("  Daily (24 Hour) price-sensitive demand dispatch for each LSE");
    strTemp=String.format("%1$15s", "Hour");
    for(int i = 0; i<J; i++){
        String LSEName="LSE"+(i+1);
        strTemp+=String.format("\t%1$15s", LSEName);
    }
    System.out.println(strTemp);
    for(int h = 0; h<24; h++){
      System.out.printf("%1$15d", h);
      int psLoadIndex=0;
      for(int j=0; j<J; j++){
        int hourlyLoadHybridFlagByLSE;
        hourlyLoadHybridFlagByLSE = iso.getDemandHybridByLSE()[j][h];
        
        if((hourlyLoadHybridFlagByLSE&2)==2){
            System.out.printf("\t%1$15.2f", Support.roundOff(dailyPriceSensitiveDemand[h][psLoadIndex],2));
            psLoadIndex++;
        }
        else
            System.out.printf("\t%1$15.2f", 0.00);
        
      }

      System.out.println();
    }
    System.out.println();
    
    // CHECK TO SEE THAT LMP_k IS AT LEAST AS GREAT AS MC^R_i FOR ANY GENERATOR 
    // AT NODE K WITH A POSITIVE POWER COMMITMENT P_Gi, FOR EACH HOUR H
    supplyOfferByGen = iso.getSupplyOfferByGen();
    System.out.println("  Daily (24 Hour) commitment for each generator with LMPs");
    strTemp=String.format("%1$15s", "Hour");
    for(int i = 0; i<I; i++){
        String genName="GenCo"+(i+1);
        strTemp+=String.format("\t%1$15s", genName);
        strTemp+=String.format("\t%1$15s", "Res Price");
        strTemp+=String.format("\t%1$15s", "Node LMP");
    }
    System.out.println(strTemp);
    for(int i = 0; i<dailyCommitment.length; i++){
      System.out.printf("%1$15d", i);
      for(int j = 0 ; j<dailyCommitment[0].length; j++){
          System.out.printf("\t%1$15.2f", Support.roundOff(dailyCommitment[i][j],2));
          
          double reportedLMP=dSupplyOfferByGen[j][A_INDEX]+2.0*dSupplyOfferByGen[j][B_INDEX]*dailyCommitment[i][j];
          System.out.printf("\t%1$15.2f", Support.roundOff(reportedLMP,2));
          
          System.out.printf("\t%1$15.2f", Support.roundOff(dailyLMP[i][atNodeByGen[j]-1],2));
       }
        System.out.println();
    }
    System.out.println();

    // CHECK TO SEE THAT LMP_k DOES NOT EXCEED MAX WILLINGNESS TO PAY OF ANY LSEJ 
    // AT NODE K WITH POSITIVE PRICE_SENSITIVE DEMAND, FOR EACH HOUR H = 00,...,23
    System.out.println("  Daily (24 Hour) price-sensitive demand dispatch for each LSE with LMPs");
    strTemp=String.format("%1$15s", "Hour");
    for(int i = 0; i<J; i++){
        String LSEName="LSE"+(i+1);
        strTemp+=String.format("\t%1$15s", LSEName);
        strTemp+=String.format("\t%1$15s", "Res Price");
        strTemp+=String.format("\t%1$15s", "Node LMP");
    }
    System.out.println(strTemp);
    for(int h = 0; h<24; h++){
      System.out.printf("%1$15d", h);
      int psLoadIndex=0;
      for(int j=0; j<J; j++){
        int hourlyLoadHybridFlagByLSE;
        hourlyLoadHybridFlagByLSE = iso.getDemandHybridByLSE()[j][h];
        
        if((hourlyLoadHybridFlagByLSE&2)==2){
            double dPDemand=dailyPriceSensitiveDemand[h][psLoadIndex];
            System.out.printf("\t%1$15.2f", Support.roundOff(dPDemand,2));
            double reportedLMP=priceSensitiveDemandBidByLSE[j][h][C_INDEX]-2.0*priceSensitiveDemandBidByLSE[j][h][D_INDEX]*dPDemand;
            System.out.printf("\t%1$15.2f", Support.roundOff(reportedLMP,2));

            System.out.printf("\t%1$15.2f", Support.roundOff(dailyLMP[h][atNodeByLSE[j]-1],2));
            
            psLoadIndex++;
        }
        else{
            System.out.printf("\t%1$15.2f", 0.00);
            System.out.printf("\t%1$15.2f", 0.00);
            System.out.printf("\t%1$15.2f", 0.00);
        }
          
        
      }

      System.out.println();
    }
    System.out.println();
 //------------------------------------------------------*/
    
    System.gc();
}

  // dailyCommitment: Hour-by-Node
  public double[][] getDailyBranchFlow(){
    return dailyBranchFlow;
  }
  // dailyCommitment: Hour-by-GenCo
  public double[][] getDailyCommitment(){
    return dailyCommitment;
  }
  public double[][] getDailyEmission(){
  return dailyEmission;
  }
  public double[][] getDailyEmTrade(){
  return dailyEmTrade;
  }
  public double[][] getDailyEmTradePrice(){
  return dailyEmTradePrice;
  }
  // dailyPriceSensitiveDemand: Hour-by-LSE
  public double[][] getDailyPriceSensitiveDemand(){
    return dailyPriceSensitiveDemand;
  }
  // dailyPhaseAngle: Hour-by-Node (excluding Node 1)
  public double[][] getDailyVoltAngle(){
    return dailyVoltAngle;
  }
  // dailyLMP: Hour-by-Node
  public double[][] getDailyLMP(){
    return dailyLMP;
  }
  
  public int [] getHasSolution(){
      int [] hasSolution=new int[H];
      
      for(int i=0; i<H; i++){
          if(bDCOPFHasSolution[i])
              hasSolution[i]=1;
      }
      
      return hasSolution;
  }

}
