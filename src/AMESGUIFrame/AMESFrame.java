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


package AMESGUIFrame;
/*
 * AMESFrame.java
 *
 * Created on 2007 1 26 ,  9:30
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */



import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.awt.Color;
import java.util.ArrayList;
import java.awt.Toolkit;
import java.awt.Dimension;

import javax.swing.*;
import javax.swing.filechooser.*;
import javax.swing.text.*;
import javax.swing.JFrame;
import javax.swing.JMenuBar;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JRadioButtonMenuItem;
import javax.swing.ButtonGroup;
import javax.swing.KeyStroke;
import javax.swing.BorderFactory;
import static java.awt.event.InputEvent.*;

import amesmarket.*;
import Output.*;

public class AMESFrame  extends JFrame{


    /** Creates a new instance of AMESFrame */
    public AMESFrame() {
    setTitle("AMES Market Package (Java)");                              
    setDefaultCloseOperation(EXIT_ON_CLOSE);
   
    setJMenuBar(menuBar);                         
    addCaseMenu( );
    addViewMenu( );
    addCommandMenu( );
    addOptionsMenu( );
    addHelpMenu( );
    
    addCaseToolBar( );
    addCommandToolBar( );
    
    Container contentPane = getContentPane();
    contentPane.add(caseToolBar, BorderLayout.NORTH);
    
    output=new OutputPane();
    contentPane.add(output, BorderLayout.CENTER);

    setVisible(true);
    pack();

    createConfigFrames( );
    
    Default_Cooling=1000.0;
    Default_Experimentation=0.96;
    Default_InitPropensity=6000.0;
    Default_Recency=0.04;
    
    Default_M1=10;
    Default_M2=10;
    Default_M3=1;
    Default_RI_MAX_Lower=0.75;
    Default_RI_MAX_Upper=0.75;
    Default_RI_MIN_C=1.0;
    Default_SlopeStart=0.001;
    Default_iRewardSelection=1;
    
    Default_RandomSeed=695672061;
    Default_iMaxDay=2000;
    Default_dThresholdProbability=0.999;
    Default_dDailyNetEarningThreshold=10.0;
    Default_dGenPriceCap=1000.0;
    Default_dLSEPriceCap=0.0;
    Default_iStartDay=1;
    Default_iCheckDayLength=5;
    Default_dActionProbability=0.001;
    Default_iLearningCheckStartDay=1;
    Default_iLearningCheckDayLength=5;
    Default_dLearningCheckDifference=0.001;
    Default_iDailyNetEarningStartDay=1;
    Default_iDailyNetEarningDayLength=5;

    Cooling=1000.0;
    Experimentation=0.96;
    InitPropensity=6000.0;
    Recency=0.04;
    
    M1=10;
    M2=10;
    M3=1;
    RI_MAX_Lower=0.75;
    RI_MAX_Upper=0.75;
    RI_MIN_C=1.0;
    SlopeStart=0.001;
    iRewardSelection=1;
    
    RandomSeed=695672061;
    iMaxDay=25;
    dThresholdProbability=0.99;
    dDailyNetEarningThreshold=10.0;
    dGenPriceCap=1000.0;
    dLSEPriceCap=0.0;
    iStartDay=1;
    iCheckDayLength=5;
    dActionProbability=0.001;
    iLearningCheckStartDay=1;
    iLearningCheckDayLength=5;
    dLearningCheckDifference=0.001;
    iDailyNetEarningStartDay=1;
    iDailyNetEarningDayLength=5;
    
}
   
    public void createConfigFrames( ){
       config1 = new PowerGridConfigure1(this);
       config1.setVisible(false);
       
       config2 = new PowerGridConfigure2(this);
       config2.setVisible(false);
       
       config4= new PowerGridConfigure4(this);
       config4.setVisible(false);
       
       config5 = new PowerGridConfigure5(this);
       config5.setVisible(false);
   
       learnOption1=new LearnOption1(this, true);
       learnOption1.setVisible(false);
  
       simulationControl=new SimulationControl(this, true);
       simulationControl.setVisible(false);
   }

    public void addCaseMenu( ){
    caseMenu.setMnemonic('C');                    // Create shortcut

    // Construct the file drop-down menu
    newCaseItem = caseMenu.add("New Case");          
    newCaseItem.setIcon(new javax.swing.ImageIcon(AMESFrame.class.getResource("/resources/new.gif")));
    newCaseItem.addActionListener(new ActionListener() {
           public void actionPerformed(java.awt.event.ActionEvent evt) {
              newCaseItemActionPerformed(evt);
            }
    });
    
    caseMenu.addSeparator();                      

    selectCaseItem = caseMenu.add("Open Case");             
    selectCaseItem.setIcon(new javax.swing.ImageIcon(AMESFrame.class.getResource("/resources/select.gif")));
    selectCaseItem.addActionListener(new ActionListener() {
           public void actionPerformed(java.awt.event.ActionEvent evt) {
              selectCaseItemActionPerformed(evt);
            }
    });
    
    caseMenu.add(loadDefaultCaseMenu);

    default5BusCaseItem = loadDefaultCaseMenu.add("5-Bus Original Test Case");
    default5BusCaseItem.addActionListener(new ActionListener() {
           public void actionPerformed(java.awt.event.ActionEvent evt) {
              default5BusCaseOriginalItemActionPerformed(evt);
            }
    });
    
    loadDefaultCaseMenu.addSeparator();                      
    default30BusCaseItem = loadDefaultCaseMenu.add("5-Bus Emission Constraints Test Case");
    default30BusCaseItem.addActionListener(new ActionListener() {
           public void actionPerformed(java.awt.event.ActionEvent evt) {
              default5BusCaseConstraintsItemActionPerformed(evt);
            }
    });

        loadDefaultCaseMenu.addSeparator();
    default30BusCaseItem = loadDefaultCaseMenu.add("5-Bus Emission Trade Test Case");
    default30BusCaseItem.addActionListener(new ActionListener() {
           public void actionPerformed(java.awt.event.ActionEvent evt) {
              default5BusCaseTradesItemActionPerformed(evt);
            }
    });
   
    caseParametersItem = caseMenu.add("Case Parameters");                
    caseParametersItem.setIcon(new javax.swing.ImageIcon(AMESFrame.class.getResource("/resources/case.gif")));
    caseParametersItem.addActionListener(new ActionListener() {
           public void actionPerformed(java.awt.event.ActionEvent evt) {
              caseParametersItemActionPerformed(evt);
            }
    });
    caseParametersItem.setEnabled(false);
    
    caseMenu.addSeparator();                      
 
    saveCaseItem = caseMenu.add("Save Case");              
    saveCaseItem.setIcon(new javax.swing.ImageIcon(AMESFrame.class.getResource("/resources/save.gif")));
    saveCaseItem.addActionListener(new ActionListener() {
           public void actionPerformed(java.awt.event.ActionEvent evt) {
              saveCaseItemActionPerformed(evt);
            }
    });
    saveCaseItem.setEnabled(false);
    
    saveCaseAsItem = caseMenu.add("Save Case As");              
    saveCaseAsItem.setIcon(new javax.swing.ImageIcon(AMESFrame.class.getResource("/resources/saveas.gif")));
    saveCaseAsItem.addActionListener(new ActionListener() {
           public void actionPerformed(java.awt.event.ActionEvent evt) {
              saveCaseAsItemActionPerformed(evt);
            }
    });
    saveCaseAsItem.setEnabled(false);
    
    caseMenu.addSeparator();                      
    
    caseMenu.add(batchModeMenu);

    batchMode1Item = batchModeMenu.add("Load Batch Mode File");             
    batchMode1Item.addActionListener(new ActionListener() {
           public void actionPerformed(java.awt.event.ActionEvent evt) {
              batchMode1ItemActionPerformed(evt);
            }
    });
    
    batchModeMenu.addSeparator();                      
    loadBatchMode1Item = batchModeMenu.add("Load Batch Mode Output");             
    loadBatchMode1Item.addActionListener(new ActionListener() {
           public void actionPerformed(java.awt.event.ActionEvent evt) {
              loadBatchMode1ItemActionPerformed(evt);
            }
    });
    caseMenu.addSeparator();                      

    exitItem = caseMenu.add("Exit");             
    exitItem.setIcon(new javax.swing.ImageIcon(AMESFrame.class.getResource("/resources/exit.gif")));
    exitItem.addActionListener(new ActionListener() {
           public void actionPerformed(java.awt.event.ActionEvent evt) {
              exitItemActionPerformed(evt);
            }
    });
    
     
    // Add Case menu accelerators
    newCaseItem.setAccelerator(KeyStroke.getKeyStroke('N',CTRL_DOWN_MASK ));
    selectCaseItem.setAccelerator(KeyStroke.getKeyStroke('L', CTRL_DOWN_MASK));
    saveCaseItem.setAccelerator(KeyStroke.getKeyStroke('S', CTRL_DOWN_MASK));
    saveCaseAsItem.setAccelerator(KeyStroke.getKeyStroke('A', CTRL_DOWN_MASK));
    exitItem.setAccelerator(KeyStroke.getKeyStroke('X', CTRL_DOWN_MASK));

    menuBar.add(caseMenu);
   }
 
    public void addCaseToolBar( ){
        caseToolBar.addSeparator();                      // Space at the start

        newCaseButton = new javax.swing.JButton();
        newCaseButton.setIcon(new javax.swing.ImageIcon(AMESFrame.class.getResource("/resources/new.gif")));
        newCaseButton.setToolTipText("New Case");
        newCaseButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                newCaseItemActionPerformed(evt);
            }
        });
        caseToolBar.add(newCaseButton);

        selectCaseButton = new javax.swing.JButton();
        selectCaseButton.setIcon(new javax.swing.ImageIcon(AMESFrame.class.getResource("/resources/select.gif")));
        selectCaseButton.setToolTipText("Open Case");
        selectCaseButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                selectCaseItemActionPerformed(evt);
            }
        });
        caseToolBar.add(selectCaseButton);
        
        caseParametersButton = new javax.swing.JButton();
        caseParametersButton.setIcon(new javax.swing.ImageIcon(AMESFrame.class.getResource("/resources/case.gif")));
        caseParametersButton.setToolTipText("Case Parameters");
        caseParametersButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                caseParametersItemActionPerformed(evt);
            }
        });
        caseToolBar.add(caseParametersButton);
        caseParametersButton.setEnabled(false);

        saveCaseButton = new javax.swing.JButton();
        saveCaseButton.setIcon(new javax.swing.ImageIcon(AMESFrame.class.getResource("/resources/save.gif")));
        saveCaseButton.setToolTipText("Save Case");
        saveCaseButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                saveCaseItemActionPerformed(evt);
            }
        });
        caseToolBar.add(saveCaseButton);
        saveCaseButton.setEnabled(false);

        saveCaseAsButton = new javax.swing.JButton();
        saveCaseAsButton.setIcon(new javax.swing.ImageIcon(AMESFrame.class.getResource("/resources/saveas.gif")));
        saveCaseAsButton.setToolTipText("Save Case As");
        saveCaseAsButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                saveCaseAsItemActionPerformed(evt);
            }
        });
        caseToolBar.add(saveCaseAsButton);
        saveCaseAsButton.setEnabled(false);

        caseToolBar.addSeparator();                      // Space at the end
        caseToolBar.setBorder(BorderFactory.createEtchedBorder(Color.WHITE, Color.LIGHT_GRAY));

    }

    public void enableCaseMenuAndToolBar(){
        caseParametersItem.setEnabled(true);
        saveCaseItem.setEnabled(true);
        saveCaseAsItem.setEnabled(true);

        caseParametersButton.setEnabled(true);
        saveCaseButton.setEnabled(true);
        saveCaseAsButton.setEnabled(true);
    }
    
    public void disableCaseMenuAndToolBar(){
        caseParametersItem.setEnabled(false);
        saveCaseItem.setEnabled(false);
        saveCaseAsItem.setEnabled(false);

        caseParametersButton.setEnabled(false);
        saveCaseButton.setEnabled(false);
        saveCaseAsButton.setEnabled(false);
    }
    
   private void newCaseItemActionPerformed(java.awt.event.ActionEvent evt) {
       bOpen = false;
       bLoadCase=false;
       bCasdeResult=false;
       this.disableCaseMenuAndToolBar();
       this.disableCommandMenuAndToolbar();
       this.disableOptionsMenu();
       this.disableViewMenu();

       Toolkit theKit = config1.getToolkit(); // Get the window toolkit
       Dimension wndSize = theKit.getScreenSize(); // Get screen size       
       
       Rectangle configBounds=config1.getBounds();
       
       config1.setLocation( (wndSize.width-configBounds.width)/2, (wndSize.height-configBounds.height)/2);
       config1.setVisible(true);

    }

   public void setNewCaseInitialData(int iNodeNumber, int iBranchNumber, int iGenNumber, int iLSENumber, double dVBase, double dPowerBase){
       baseV=dVBase;
       baseS=dPowerBase;

       nodeData = new Object[1][2];
       
       nodeData[0][0] = iNodeNumber;
       nodeData[0][1] = 0.05;
       iNodeData = iNodeNumber;

       branchData = new Object[iBranchNumber][5];
       iBranchData =iBranchNumber;

       for( int i = 0; i < iBranchNumber; i ++ ) {
           String strTemp="Branch"+Integer.toString(i+1);
            branchData[i][0]=strTemp;
            branchData[i][1]=1;
            branchData[i][2]=2;
            branchData[i][3]=10;
            branchData[i][4]=0.5;
       }


       genData = new Object[iGenNumber][13];
       iGenData = iGenNumber;

       for( int i = 0; i < iGenNumber; i ++ ) {
            genData[i][0]="GenCo"+Integer.toString(i+1);
            genData[i][1]=i+1;
            genData[i][2]=i+1>iNodeNumber?((i+1)/iNodeNumber):(i+1);
            genData[i][3]=10.0;
            genData[i][4]=0.05;
            genData[i][5]=0.0;
            genData[i][6]=100.0;
            genData[i][7]=10.0;
            genData[i][8]=1;
            genData[i][9]=10000;
            genData[i][10]=10;
            genData[i][11]=10;
            genData[i][12]=10000.0;
       }

      genLearningData = new double[iGenData][12];

      for( int i = 0; i < iGenData; i ++ ) {
          genLearningData[i][0]=Default_InitPropensity;
          genLearningData[i][1]=Default_Cooling;
          genLearningData[i][2]=Default_Recency;
          genLearningData[i][3]=Default_Experimentation;
          genLearningData[i][4]=Default_M1;
          genLearningData[i][5]=Default_M2;
          genLearningData[i][6]=Default_M3;
          genLearningData[i][7]=Default_RI_MAX_Lower;
          genLearningData[i][8]=Default_RI_MAX_Upper;
          genLearningData[i][9]=Default_RI_MIN_C;
          genLearningData[i][10]=Default_SlopeStart;
          genLearningData[i][11]=Default_iRewardSelection;
      }

      lseData = new Object[iLSENumber][27];
      iLSEData = iLSENumber;

      for( int i = 0; i < iLSENumber; i ++ ) {
          lseData[i][0]="LSE"+Integer.toString(i+1);
          lseData[i][1]=i+1;
          lseData[i][2]=i+1>iNodeNumber?((i+1)/iNodeNumber):(i+1);

          for(int j=0; j<24; j++){
              lseData[i][j+3]=10.0;
          }
      }

      lsePriceSensitiveDemand = new Object[iLSENumber][24][7];
      iLSEData = iLSENumber;

      for( int i = 0; i < iLSENumber; i ++ ) {
          for(int j=0; j<24; j++){
              lsePriceSensitiveDemand[i][j][0]="LSE"+Integer.toString(i+1);
              lsePriceSensitiveDemand[i][j][1]=i+1;
              lsePriceSensitiveDemand[i][j][2]=i+1>iNodeNumber?((i+1)/iNodeNumber):(i+1);
              lsePriceSensitiveDemand[i][j][3]=j;
              lsePriceSensitiveDemand[i][j][4]=40.0;
              lsePriceSensitiveDemand[i][j][5]=0.04;
              lsePriceSensitiveDemand[i][j][6]=50.0;
          }
      }

      lseHybridDemand = new Object[iLSENumber][27];

      for( int i = 0; i < iLSENumber; i ++ ) {
          lseHybridDemand[i][0]="LSE"+Integer.toString(i+1);
          lseHybridDemand[i][1]=i+1;
          lseHybridDemand[i][2]=i+1>iNodeNumber?((i+1)/iNodeNumber):(i+1);

          for(int j=0; j<24; j++){
              lseHybridDemand[i][j+3]=3;
          }
      }

      config2.loadData(branchData);
      config4.loadData(genData);
      config5.loadData(lseData, lsePriceSensitiveDemand, lseHybridDemand);

      learnOption1.loadData(genData, genLearningData);
      simulationControl.SetInitParameters(iMaxDay, bMaximumDay, dThresholdProbability, bThreshold, dDailyNetEarningThreshold, bDailyNetEarningThreshold, iDailyNetEarningStartDay, iDailyNetEarningDayLength, iStartDay, iCheckDayLength, dActionProbability, bActionProbabilityCheck, //
               iLearningCheckStartDay, iLearningCheckDayLength, dLearningCheckDifference, bLearningCheck, dGenPriceCap, dLSEPriceCap, RandomSeed);

   }
    
   private void default5BusCaseOriginalItemActionPerformed(java.awt.event.ActionEvent evt) {
        bLoadCase=false;
        bCasdeResult=false;
        this.disableCaseMenuAndToolBar();
        this.disableCommandMenuAndToolbar();
        this.disableViewMenu();
         
        caseFile = new File("DATA/5busNoConstraints.dat");
        bOpen = true;
        
        loadCaseFileData( );
        System.out.println("Load default 5-bus Original case data.");

        config1.SetInitParameters(iNodeData, iBranchData, 0, iGenData, iLSEData, baseV, baseS);
        config2.loadData(branchData);
        config4.loadData(genData);
        config5.loadData(lseData, lsePriceSensitiveDemand, lseHybridDemand);
  
        learnOption1.loadData(genData, genLearningData);
        simulationControl.SetInitParameters(iMaxDay, bMaximumDay, dThresholdProbability, bThreshold, dDailyNetEarningThreshold, bDailyNetEarningThreshold, iDailyNetEarningStartDay, iDailyNetEarningDayLength, iStartDay, iCheckDayLength, dActionProbability, bActionProbabilityCheck, //
                iLearningCheckStartDay, iLearningCheckDayLength, dLearningCheckDifference, bLearningCheck, dGenPriceCap, dLSEPriceCap, RandomSeed);

        // Verify Data
        String strError=checkCaseData();
        if(!strError.isEmpty()){
                JOptionPane.showMessageDialog(this, strError, "Case Data Verify Message", JOptionPane.ERROR_MESSAGE); 
                return;
         }

        setbLoadCase(true);
        setbCaseResult(false);
        enableCaseMenuAndToolBar();
        enableCommandMenuAndToolbar();
        enableOptionsMenu();
        disableViewMenu();
        InitializeAMESMarket();
   }

   private void default5BusCaseConstraintsItemActionPerformed(java.awt.event.ActionEvent evt) {
        bLoadCase=false;
        bCasdeResult=false;
        this.disableCaseMenuAndToolBar();
        this.disableCommandMenuAndToolbar();
        this.disableViewMenu();
         
        caseFile = new File("DATA/5Busemission.dat");
        bOpen = true;

        loadCaseFileData( );
        System.out.println("Load default 5-bus emission case data.");

        config1.SetInitParameters(iNodeData, iBranchData, 0, iGenData, iLSEData, baseV, baseS);
        config2.loadData(branchData);
        config4.loadData(genData);
        config5.loadData(lseData, lsePriceSensitiveDemand, lseHybridDemand);
      
        learnOption1.loadData(genData, genLearningData);
        simulationControl.SetInitParameters(iMaxDay, bMaximumDay, dThresholdProbability, bThreshold, dDailyNetEarningThreshold, bDailyNetEarningThreshold, iDailyNetEarningStartDay, iDailyNetEarningDayLength, iStartDay, iCheckDayLength, dActionProbability, bActionProbabilityCheck, //
                iLearningCheckStartDay, iLearningCheckDayLength, dLearningCheckDifference, bLearningCheck, dGenPriceCap, dLSEPriceCap, RandomSeed);

        // Verify Data
        String strError=checkCaseData();
        if(!strError.isEmpty()){
                JOptionPane.showMessageDialog(this, strError, "Case Data Verify Message", JOptionPane.ERROR_MESSAGE); 
                return;
         }

        setbLoadCase(true);
        setbCaseResult(false);
        enableCaseMenuAndToolBar();
        enableCommandMenuAndToolbar();
        enableOptionsMenu();
        disableViewMenu();
        InitializeAMESMarket();
    }

   private void default5BusCaseTradesItemActionPerformed(java.awt.event.ActionEvent evt) {
        bLoadCase=false;
        bCasdeResult=false;
        this.disableCaseMenuAndToolBar();
        this.disableCommandMenuAndToolbar();
        this.disableViewMenu();

        caseFile = new File("DATA/5busemissiontrade.dat");
        bOpen = true;

        loadCaseFileData( );
        System.out.println("Load default 5-bus emission trade case data.");

        config1.SetInitParameters(iNodeData, iBranchData, 0, iGenData, iLSEData, baseV, baseS);
        config2.loadData(branchData);
        config4.loadData(genData);
        config5.loadData(lseData, lsePriceSensitiveDemand, lseHybridDemand);

        learnOption1.loadData(genData, genLearningData);
        simulationControl.SetInitParameters(iMaxDay, bMaximumDay, dThresholdProbability, bThreshold, dDailyNetEarningThreshold, bDailyNetEarningThreshold, iDailyNetEarningStartDay, iDailyNetEarningDayLength, iStartDay, iCheckDayLength, dActionProbability, bActionProbabilityCheck, //
                iLearningCheckStartDay, iLearningCheckDayLength, dLearningCheckDifference, bLearningCheck, dGenPriceCap, dLSEPriceCap, RandomSeed);

        // Verify Data
        String strError=checkCaseData();
        if(!strError.isEmpty()){
                JOptionPane.showMessageDialog(this, strError, "Case Data Verify Message", JOptionPane.ERROR_MESSAGE);
                return;
         }

        setbLoadCase(true);
        setbCaseResult(false);
        enableCaseMenuAndToolBar();
        enableCommandMenuAndToolbar();
        enableOptionsMenu();
        disableViewMenu();
        InitializeAMESMarket();
    }

   private void batchMode1ItemActionPerformed(java.awt.event.ActionEvent evt) {   
        selectCasetDialog.setDialogType( JFileChooser.OPEN_DIALOG );
        selectCasetDialog.setDialogTitle( "Select Batch RandomSeeds..." );

        String[] extensions =new String[1];
        extensions[0] = "bth";
        
        caseFileFilter filter = new caseFileFilter(extensions, "*.bth AMES Market Batch Files");
        selectCasetDialog.setFileFilter(filter);
        
        if(bOpen)
            selectCasetDialog.setCurrentDirectory(caseFileDirectory);
        else
            selectCasetDialog.setCurrentDirectory(new File("DATA/"));
        
        int returnVal = selectCasetDialog.showOpenDialog(this);

        if(returnVal == JFileChooser.APPROVE_OPTION) {
            batchFile = selectCasetDialog.getSelectedFile();
            caseFileDirectory=selectCasetDialog.getCurrentDirectory();
            
            loadBatchModeFileData(batchFile);
            System.out.println("Load user selected batch file:"+batchFile.getName());
            
        }
    }

   private void loadBatchMode1ItemActionPerformed(java.awt.event.ActionEvent evt) {   
        selectCasetDialog.setDialogType( JFileChooser.OPEN_DIALOG );
        selectCasetDialog.setDialogTitle( "Select Batch RandomSeeds Output..." );

        String[] extensions =new String[1];
        extensions[0] = "out";
        
        caseFileFilter filter = new caseFileFilter(extensions, "*.out AMES Market Output Files");
        selectCasetDialog.setFileFilter(filter);
        
        if(bOpen)
            selectCasetDialog.setCurrentDirectory(caseFileDirectory);
        else
            selectCasetDialog.setCurrentDirectory(new File("DATA/"));

        int returnVal = selectCasetDialog.showOpenDialog(this);

        if(returnVal == JFileChooser.APPROVE_OPTION) {
            outputFile = selectCasetDialog.getSelectedFile();
            
            loadOutputData( );
            
            System.out.println("Load user selected case output data file:"+outputFile.getName());
        }
    }

   private void selectCaseItemActionPerformed(java.awt.event.ActionEvent evt) {   
        bLoadCase=false;
        bCasdeResult=false;
        this.disableCaseMenuAndToolBar();
        this.disableCommandMenuAndToolbar();
        this.disableViewMenu();
         
        selectCasetDialog.setDialogType( JFileChooser.OPEN_DIALOG );
        selectCasetDialog.setDialogTitle( "Select Case..." );

        String[] extensions =new String[1];
        extensions[0] = "dat";
        
        caseFileFilter filter = new caseFileFilter(extensions, "*.dat AMES Market Case Files");
        selectCasetDialog.setFileFilter(filter);
        
        if(bOpen)
            selectCasetDialog.setCurrentDirectory(caseFileDirectory);
        else
            selectCasetDialog.setCurrentDirectory(new File("DATA/"));

        int returnVal = selectCasetDialog.showOpenDialog(this);

        if(returnVal == JFileChooser.APPROVE_OPTION) {
            caseFile = selectCasetDialog.getSelectedFile();
            caseFileDirectory = selectCasetDialog.getCurrentDirectory();
            bOpen = true;
            
            loadCaseFileData( );
            System.out.println("Load user selected case data file:"+caseFile.getName());
            
            config1.SetInitParameters(iNodeData, iBranchData, 0, iGenData, iLSEData, baseV, baseS);
            config2.loadData(branchData);
            config4.loadData(genData);
            config5.loadData(lseData, lsePriceSensitiveDemand, lseHybridDemand);
        
            learnOption1.loadData(genData, genLearningData);
            simulationControl.SetInitParameters(iMaxDay, bMaximumDay, dThresholdProbability, bThreshold, dDailyNetEarningThreshold, bDailyNetEarningThreshold, iDailyNetEarningStartDay, iDailyNetEarningDayLength, iStartDay, iCheckDayLength, dActionProbability, bActionProbabilityCheck, //
                iLearningCheckStartDay, iLearningCheckDayLength, dLearningCheckDifference, bLearningCheck, dGenPriceCap, dLSEPriceCap, RandomSeed);
   
            // Verify Data
            String strError=checkCaseData();
            if(!strError.isEmpty()){
                    JOptionPane.showMessageDialog(this, strError, "Case Data Verify Message", JOptionPane.ERROR_MESSAGE); 
                    return;
             }

            setbLoadCase(true);
            setbCaseResult(false);
            enableCaseMenuAndToolBar();
            enableCommandMenuAndToolbar();
            enableOptionsMenu();
            disableViewMenu();
            InitializeAMESMarket();
        }
    }

  private void loadBatchModeFileData(File randomSeedsFile) {
     bMultiRandomSeeds=false;
     bMultiCases=false;
     
      try {
          FileReader       batchModeFileReader = new FileReader(randomSeedsFile);
          BufferedReader   batchModeBufferReader = new BufferedReader(batchModeFileReader);

          String strTemp;
          boolean bRandomSeedsData = false;
          boolean bMultiCasesData = false;
          
          ArrayList randomSeedsDataList = new ArrayList();
          ArrayList multiCasesDataList = new ArrayList();
          
          while ((strTemp = batchModeBufferReader.readLine()) != null) {
              
               //System.out.println(strTemp);
               strTemp = strTemp.trim();
               if(strTemp.length()==0)
                   continue;
               
               //System.out.println(strTemp);
               
               int iCommentIndex = strTemp.indexOf("//");
               if(iCommentIndex == 0)
                   continue;
               

               int iRandomSeedsDataStart = strTemp.indexOf("#RandomSeedsDataStart");
               if(iRandomSeedsDataStart == 0 ) {
                   bRandomSeedsData = true;
                   continue;
               }
               
              int iRandomSeedsDataEnd = strTemp.indexOf("#RandomSeedsDataEnd");
               if(iRandomSeedsDataEnd == 0 ) {
                   bRandomSeedsData = false;
                  
                   int iRandomSeedsNumber = randomSeedsDataList.size();
                   randomSeedsData = new long[iRandomSeedsNumber];
                   iRandomSeedsData =iRandomSeedsNumber;
                    
                   for( int i = 0; i < iRandomSeedsNumber; i ++ ) {
                       String strRandomSeeds = (String)randomSeedsDataList.get(i);
                       randomSeedsData[i]=Long.parseLong(strRandomSeeds);
                   }
                   
                   iCurrentRandomSeedsIndex=0;
                   bMultiRandomSeeds=true;
                   continue;
               }

              if(bRandomSeedsData)
               {
                    randomSeedsDataList.add(strTemp);
                     continue;
               }
          
               int iMultiCasesDataStart = strTemp.indexOf("#MultiCasesDataStart");
               if(iMultiCasesDataStart == 0 ) {
                   bMultiCasesData = true;
                   continue;
               }
               
              int iMultiCasesDataEnd = strTemp.indexOf("#MultiCasesDataEnd");
               if(iMultiCasesDataEnd == 0 ) {
                   bMultiCasesData = false;
                  
                   int iMultiCasesNumber = multiCasesDataList.size();
                   MultiCasesData = new String[iMultiCasesNumber];
                   iMultiCasesData =iMultiCasesNumber;
                    
                   for( int i = 0; i < iMultiCasesData; i ++ ) {
                       String strMultiCases = (String)multiCasesDataList.get(i);
                       MultiCasesData[i]=strMultiCases;
                   }
                   
                   iCurrentMultiCasesIndex=0;
                   String currentCase=MultiCasesData[iCurrentMultiCasesIndex];
                   caseFile=new File(batchFile.getParent()+"\\"+currentCase);
                    bOpen = true;

                    loadCaseFileData( );
                    System.out.println("Load user selected case data file:"+caseFile.getName());

                    config1.SetInitParameters(iNodeData, iBranchData, 0, iGenData, iLSEData, baseV, baseS);
                    config2.loadData(branchData);
                    config4.loadData(genData);
                    config5.loadData(lseData, lsePriceSensitiveDemand, lseHybridDemand);

                    learnOption1.loadData(genData, genLearningData);
                    simulationControl.SetInitParameters(iMaxDay, bMaximumDay, dThresholdProbability, bThreshold, dDailyNetEarningThreshold, bDailyNetEarningThreshold, iDailyNetEarningStartDay, iDailyNetEarningDayLength, iStartDay, iCheckDayLength, dActionProbability, bActionProbabilityCheck, //
                        iLearningCheckStartDay, iLearningCheckDayLength, dLearningCheckDifference, bLearningCheck, dGenPriceCap, dLSEPriceCap, RandomSeed);

                    // Verify Data
                    String strError=checkCaseData();
                    if(!strError.isEmpty()){
                            JOptionPane.showMessageDialog(this, strError, "Case Data Verify Message", JOptionPane.ERROR_MESSAGE); 
                            return;
                     }

                    setbLoadCase(true);
                    setbCaseResult(false);
                    enableCaseMenuAndToolBar();
                    enableCommandMenuAndToolbar();
                    enableOptionsMenu();
                    disableViewMenu();
                    InitializeAMESMarket( );
                   
                   bMultiCases=true;
                   continue;
               }

              if(bMultiCasesData)
               {
                    multiCasesDataList.add(strTemp);
                     continue;
               }

          }          
          
          batchModeFileReader.close();
     }
     catch (IOException e)  {
     
     }
     
     BatchMode=1;
   }
   
   
  private void loadCaseFileData( ) {
     try {
          FileReader       caseFileReader = new FileReader(caseFile);
          BufferedReader   caseBufferReader = new BufferedReader(caseFileReader);

          String strTemp;
          boolean bNodeData = false;
          boolean bBranchData = false;
          boolean bGenData = false;
          boolean bLSEData = false;
          boolean bLSEPriceDemand = false;
          boolean bLSEHybridDemand = false;
          boolean bGenLearningData = false;
          boolean bHasGenLearningData = false;
          
          iBranchData = 0;
          iGenData = 0;
          iLSEData = 0;
          
          ArrayList branchDataList = new ArrayList();
          ArrayList genDataList = new ArrayList();
          ArrayList LSEDataList = new ArrayList();
          ArrayList LSEPriceDemandDataList = new ArrayList();
          ArrayList LSEHybridDemandDataList = new ArrayList();
          ArrayList genLearningDataList = new ArrayList();
          
          while ((strTemp = caseBufferReader.readLine()) != null) {
              
               //System.out.println(strTemp);
               strTemp = strTemp.trim();
               if(strTemp.length()==0)
                   continue;
               
               //System.out.println(strTemp);
               
               int iCommentIndex = strTemp.indexOf("//");
               if(iCommentIndex == 0)
                   continue;
               
               // BASE_S
               int iBASE_S = strTemp.indexOf("BASE_S");
               if(iBASE_S==0) {
                   int iBASE_SIndex = strTemp.lastIndexOf(" ");
                   if( iBASE_SIndex < 0)
                       iBASE_SIndex = strTemp.lastIndexOf("\t");
                   
                    String strBASE_S = strTemp.substring(iBASE_SIndex+1);
                     baseS = Double.parseDouble(strBASE_S );
                     continue;
               }
               
               // BASE_V
               int iBASE_V = strTemp.indexOf("BASE_V");
               if(iBASE_V==0) {
                   int iBASE_VIndex = strTemp.lastIndexOf(" ");
                   if( iBASE_VIndex < 0)
                       iBASE_VIndex = strTemp.lastIndexOf("\t");
                   
                    String strBASE_V = strTemp.substring(iBASE_VIndex+1);
                     baseV = Double.parseDouble(strBASE_V );
                     continue;
               }
               
               int iNodeDataStart = strTemp.indexOf("#NodeDataStart");
               if(iNodeDataStart == 0 ) {
                   bNodeData = true;
                   continue;
               }
               
              int iNodeDataEnd = strTemp.indexOf("#NodeDataEnd");
               if(iNodeDataEnd == 0 ) {
                   bNodeData = false;
                   continue;
               }

              if(bNodeData)
               {
                    nodeData = new Object[1][2];
                     int iIndex = strTemp.lastIndexOf("\t");
                     if( iIndex < 0)
                        iIndex = strTemp.lastIndexOf(" ");
                   
                     String strFirst = strTemp.substring(0, iIndex);
                     String strSeecond = strTemp.substring(iIndex+1);
                     nodeData[0][0] = Double.parseDouble(strFirst );
                     nodeData[0][1] = Double.parseDouble(strSeecond );
                     Double dTemp = Double.parseDouble(strFirst );
                     iNodeData = dTemp.intValue();
                     continue;
               }

               int iBranchDataStart = strTemp.indexOf("#BranchDataStart");
               if(iBranchDataStart == 0 ) {
                   bBranchData = true;
                   continue;
               }
               
              int iBranchDataEnd = strTemp.indexOf("#BranchDataEnd");
               if(iBranchDataEnd == 0 ) {
                   bBranchData = false;
                  
                   int iBranchNumber = branchDataList.size();
                   branchData = new Object[iBranchNumber][5];
                   iBranchData =iBranchNumber;
                    
                   for( int i = 0; i < iBranchNumber; i ++ ) {
                       String strBranch = (String)branchDataList.get(i);
                       int iBranchFields = 4;
                   
                       while(iBranchFields>0) {
                           int iIndex = strBranch.lastIndexOf("\t");
                           if( iIndex < 0)
                                iIndex = strBranch.lastIndexOf(" ");

                          String strData = strBranch.substring(iIndex+1);
                          strData=strData.trim();
                          
                          if(iBranchFields>2){
                                double dTemp=Double.parseDouble(strData);
                                branchData[i][iBranchFields]=String.format("%1$15.4f", dTemp);
                          }
                          else
                                branchData[i][iBranchFields]=Integer.parseInt(strData);

                          iBranchFields --;

                          if(iIndex>0)
                            strBranch = strBranch.substring(0,iIndex);
                       }
                       
                   if(strBranch.length()>0)    
                        branchData[i][0] = strBranch;
                   }
                   continue;
               }

              if(bBranchData)
               {
                    branchDataList.add(strTemp);
                     continue;
               }
          
               int iGenDataStart = strTemp.indexOf("#GenDataStart");
               if(iGenDataStart == 0 ) {
                   bGenData = true;
                   continue;
               }
               
              int iGenDataEnd = strTemp.indexOf("#GenDataEnd");
               if(iGenDataEnd == 0 ) {
                   bGenData = false;
                  
                   int iGenNumber = genDataList.size();
                   genData = new Object[iGenNumber][13];
                   iGenData = iGenNumber;
                    
                   for( int i = 0; i < iGenNumber; i ++ ) {
                       String strGen = (String)genDataList.get(i);
                       int iGenFields = 12;
                   
                       while(iGenFields>0) {
                           int iIndex = strGen.lastIndexOf("\t");
                           if( iIndex < 0)
                                iIndex = strGen.lastIndexOf(" ");

                          String strData = strGen.substring(iIndex+1);
                          strData=strData.trim();
                         
                          if(iGenFields>2){
                                 double dTemp=Double.parseDouble(strData);
                                 genData[i][iGenFields]=String.format("%1$15.4f", dTemp);
                          }
                          else
                                genData[i][iGenFields]=Integer.parseInt(strData);
                             
                          iGenFields --;

                         if(iIndex>0)
                              strGen = strGen.substring(0,iIndex);
                       }
                       
                        if(strGen.length()>0)    
                            genData[i][0] = strGen;
                   }
                  continue;
              }

              if(bGenData)
               {
                    genDataList.add(strTemp);
                     continue;
               }


              int iLSEDataStart = strTemp.indexOf("#LSEDataFixedDemandStart");
               if(iLSEDataStart == 0 ) {
                   bLSEData = true;
                   continue;
               }
               
              int iLSEDataEnd = strTemp.indexOf("#LSEDataFixedDemandEnd");
               if(iLSEDataEnd == 0 ) {
                   bLSEData = false;
                  
                   int iLSENumber = LSEDataList.size()/3;
                   lseSec1Data = new Object[iLSENumber][11];
                   lseSec2Data = new Object[iLSENumber][11];
                   lseSec3Data = new Object[iLSENumber][11];
                   lseData = new Object[iLSENumber][27];
                   iLSEData = iLSENumber;
                    
                   for( int i = 0; i < iLSENumber*3; i ++ ) {
                       String strLSE = (String)LSEDataList.get(i);
                       int iLSEFields = 10;
                       int iLSEIndex = i%iLSENumber;
                      
                       while(iLSEFields>0) {
                           int iIndex = strLSE.lastIndexOf("\t");
                           if( iIndex < 0)
                                iIndex = strLSE.lastIndexOf(" ");

                          String strData = strLSE.substring(iIndex+1);
                          strData=strData.trim();
                          
                          if(i<iLSENumber) {
                                if(iLSEFields>2) {
                                    double dTemp=Double.parseDouble(strData);
                                    lseSec1Data[iLSEIndex][iLSEFields] = String.format("%1$15.4f", dTemp);
                                    lseData[iLSEIndex][iLSEFields] = String.format("%1$15.4f", dTemp);
                                }
                                else {
                                    lseSec1Data[iLSEIndex][iLSEFields] = Integer.parseInt(strData );
                                    lseData[iLSEIndex][iLSEFields] = Integer.parseInt(strData );
                                }
                          }
                          else if((i>=iLSENumber)&&(i<2*iLSENumber)) {
                                 if(iLSEFields>2)
                                 {
                                     double dTemp=Double.parseDouble(strData);
                                     lseSec2Data[iLSEIndex][iLSEFields] = String.format("%1$15.4f", dTemp);
                                     lseData[iLSEIndex][iLSEFields+8] = String.format("%1$15.4f", dTemp);
                                 }
                                 else
                                     lseSec2Data[iLSEIndex][iLSEFields] = Integer.parseInt(strData );
                                   
                          }
                          else if(i>=2*iLSENumber) {
                                 if(iLSEFields>2)
                                 {
                                      double dTemp=Double.parseDouble(strData);
                                      lseSec3Data[iLSEIndex][iLSEFields] = String.format("%1$15.4f", dTemp);
                                      lseData[iLSEIndex][iLSEFields+16] = String.format("%1$15.4f", dTemp);
                                 }
                                 else
                                      lseSec3Data[iLSEIndex][iLSEFields] = Integer.parseInt(strData );
                         }

                          iLSEFields --;

                         if(iIndex>0)
                           strLSE = strLSE.substring(0,iIndex);
                       }
                       
                          if(i<iLSENumber) {
                                if(strLSE.length()>0)    {
                                     lseSec1Data[iLSEIndex][0] = strLSE;
                                     lseData[iLSEIndex][0] = strLSE;
                                }
                          }
                          else if((i>=iLSENumber)&&(i<2*iLSENumber)) {
                                if(strLSE.length()>0)   
                                     lseSec2Data[iLSEIndex][0] = strLSE;
                          }
                          else if(i>=2*iLSENumber) {
                               if(strLSE.length()>0)   
                                      lseSec3Data[iLSEIndex][0] = strLSE;
                          }
                   }
                  continue;
              }

              if(bLSEData)
               {
                    LSEDataList.add(strTemp);
                     continue;
               }

               int iLSEDemandStart = strTemp.indexOf("#LSEDataPriceSensitiveDemandStart");
               if(iLSEDemandStart == 0 ) {
                   bLSEPriceDemand = true;
                   bPriceSensitiveDemand = true;
                   continue;
               }
               
              int iLSEDemandEnd = strTemp.indexOf("#LSEDataPriceSensitiveDemandEnd");
               if(iLSEDemandEnd == 0 ) {
                   bLSEPriceDemand = false;
                  
                   int iLSEDemandNumber = LSEPriceDemandDataList.size()/24;
                   lsePriceSensitiveDemand = new Object[iLSEDemandNumber][24][7];
                   iLSEData = iLSEDemandNumber;
                    
                   for( int i = 0; i < iLSEDemandNumber; i ++ ) {
                       for(int j=0; j<24; j++){
                           String strLSEDemand = (String)LSEPriceDemandDataList.get(i*24+j);
                           int iDemandFields = 6;

                           while(iDemandFields>0) {
                               int iIndex = strLSEDemand.lastIndexOf("\t");
                               if( iIndex < 0)
                                    iIndex = strLSEDemand.lastIndexOf(" ");

                              String strData = strLSEDemand.substring(iIndex+1);
                              strData=strData.trim();

                              if(iDemandFields>3)
                                    lsePriceSensitiveDemand[i][j][iDemandFields] = Double.parseDouble(strData );
                              else
                                    lsePriceSensitiveDemand[i][j][iDemandFields] = Integer.parseInt(strData );

                              iDemandFields --;

                             if(iIndex>0)
                                  strLSEDemand = strLSEDemand.substring(0,iIndex);
                           }

                           if(strLSEDemand.length()>0)    
                                lsePriceSensitiveDemand[i][j][0] = strLSEDemand;
                       }
                   }
                  continue;
              }

              if(bLSEPriceDemand)
               {
                    LSEPriceDemandDataList.add(strTemp);
                     continue;
               }

               int iLSEHybridDemandStart = strTemp.indexOf("#LSEDataHybridDemandStart");
               if(iLSEHybridDemandStart == 0 ) {
                   bLSEHybridDemand = true;
                   bHybridDemand = true;
                   continue;
               }
               
              int iLSEHybridDemandEnd = strTemp.indexOf("#LSEDataHybridDemandEnd");
               if(iLSEHybridDemandEnd == 0 ) {
                   bLSEHybridDemand = false;
                  
                   int iLSENumber = LSEHybridDemandDataList.size()/3;
                   lseHybridDemand = new Object[iLSENumber][27];
                   iLSEData = iLSENumber;
                    
                   for( int i = 0; i < iLSENumber*3; i ++ ) {
                       String strLSE = (String)LSEHybridDemandDataList.get(i);
                       int iLSEFields = 10;
                       int iLSEIndex = i%iLSENumber;
                      
                       while(iLSEFields>0) {
                           int iIndex = strLSE.lastIndexOf("\t");
                           if( iIndex < 0)
                                iIndex = strLSE.lastIndexOf(" ");

                          String strData = strLSE.substring(iIndex+1);
                          strData=strData.trim();
                          
                          if(i<iLSENumber) {
                                if(iLSEFields>0) {
                                    lseHybridDemand[iLSEIndex][iLSEFields] = Integer.parseInt(strData);
                                }
                          }
                          else if((i>=iLSENumber)&&(i<2*iLSENumber)) {
                                 if(iLSEFields>2)
                                 {
                                     lseHybridDemand[iLSEIndex][iLSEFields+8] = Integer.parseInt(strData);
                                 }
                          }
                          else if(i>=2*iLSENumber) {
                                 if(iLSEFields>2)
                                 {
                                      lseHybridDemand[iLSEIndex][iLSEFields+16] = Integer.parseInt(strData);
                                 }
                         }

                          iLSEFields --;

                         if(iIndex>0)
                           strLSE = strLSE.substring(0,iIndex);
                       }
                       
                          if(i<iLSENumber) {
                                if(strLSE.length()>0)    {
                                     lseHybridDemand[iLSEIndex][0] = strLSE;
                                }
                          }
                          else if((i>=iLSENumber)&&(i<2*iLSENumber)) {
                                if(strLSE.length()>0)   
                                     lseHybridDemand[iLSEIndex][0] = strLSE;
                          }
                          else if(i>=2*iLSENumber) {
                               if(strLSE.length()>0)   
                                      lseHybridDemand[iLSEIndex][0] = strLSE;
                          }
                   }
                  continue;
              }

              if(bLSEHybridDemand)
               {
                    LSEHybridDemandDataList.add(strTemp);
                     continue;
               }

               int iGenLearningDataStart = strTemp.indexOf("#GenLearningDataStart");
               if(iGenLearningDataStart == 0 ) {
                   bGenLearningData = true;
                   bHasGenLearningData=true;
                   continue;
               }
               
              int iGenLearningDataEnd = strTemp.indexOf("#GenLearningDataEnd");
               if(iGenLearningDataEnd == 0 ) {
                   bGenLearningData = false;
                  
                   int iGenNumber = genLearningDataList.size();
                   genLearningData = new double[iGenNumber][12];
                   iGenData = iGenNumber;
                    
                   for( int i = 0; i < iGenNumber; i ++ ) {
                       String strGenLearning = (String)genLearningDataList.get(i);
                       int iGenLearningFields = 11;
                   
                       while(iGenLearningFields>=0) {
                           int iIndex = strGenLearning.lastIndexOf("\t");
                           if( iIndex < 0)
                                iIndex = strGenLearning.lastIndexOf(" ");

                          String strData = strGenLearning.substring(iIndex+1);
                          strData=strData.trim();
                         
                         double dTemp=Double.parseDouble(strData);
                         genLearningData[i][iGenLearningFields]=dTemp;
                             
                          iGenLearningFields --;

                         if(iIndex>=0)
                              strGenLearning = strGenLearning.substring(0,iIndex);
                       }
                       
                   }
                  continue;
              }

              if(bGenLearningData)
               {
                    genLearningDataList.add(strTemp);
                     continue;
               }
              
          }          

          if(!bHasGenLearningData){// No gen learning data, use default
              genLearningData = new double[iGenData][12];

              for( int i = 0; i < iGenData; i ++ ) {
                  genLearningData[i][0]=Default_InitPropensity;
                  genLearningData[i][1]=Default_Cooling;
                  genLearningData[i][2]=Default_Recency;
                  genLearningData[i][3]=Default_Experimentation;
                  genLearningData[i][4]=Default_M1;
                  genLearningData[i][5]=Default_M2;
                  genLearningData[i][6]=Default_M3;
                  genLearningData[i][7]=Default_RI_MAX_Lower;
                  genLearningData[i][8]=Default_RI_MAX_Upper;
                  genLearningData[i][9]=Default_RI_MIN_C;
                  genLearningData[i][10]=Default_SlopeStart;
                  genLearningData[i][11]=Default_iRewardSelection;
              }
          }
          
          caseFileReader.close();
     }
     catch (IOException e)  {
     
     }
     
     bLoadCase=true;
     bCasdeResult=false;
     this.enableCaseMenuAndToolBar();
     this.enableCommandMenuAndToolbar();
     this.disableViewMenu();

     if(BatchMode==0)
        SetDefaultSimulationParameters();
  }
   
   private void saveCaseItemActionPerformed(java.awt.event.ActionEvent evt) {
       if(isNewCase()) {
           saveCaseAsItemActionPerformed(evt);
       }

       String fileName=caseFile.getName();

        if(fileName.equalsIgnoreCase("30BusTestCase.dat")||fileName.equalsIgnoreCase("5BusTestCase.dat")){
            String strMessage="Don't overwrite test cases! Use a new name for user-created cases.";
            JOptionPane.showMessageDialog(this, strMessage, "Save As Message", JOptionPane.ERROR_MESSAGE); 
        }
        else{
            saveCaseFileData( );
        }
    }
    
   private void saveCaseAsItemActionPerformed(java.awt.event.ActionEvent evt) {
        selectCasetDialog.setDialogType( JFileChooser.SAVE_DIALOG );
        selectCasetDialog.setDialogTitle( "Save Case As..." );

        String[] extensions =new String[1];
        extensions[0] = "dat";
        
        caseFileFilter filter = new caseFileFilter(extensions, "*.dat AMES Market Case Files");
        selectCasetDialog.setFileFilter(filter);
        
        if(bOpen)
            selectCasetDialog.setCurrentDirectory(caseFileDirectory);
        else
            selectCasetDialog.setCurrentDirectory(new File("DATA/"));

        int returnVal = selectCasetDialog.showSaveDialog(this);

        if(returnVal == JFileChooser.APPROVE_OPTION) {
            caseFile = selectCasetDialog.getSelectedFile();
            caseFileDirectory = selectCasetDialog.getCurrentDirectory();
            bOpen = true;
            
            String fileName=caseFile.getName();
            
            if(fileName.equalsIgnoreCase("30BusTestCase.dat")||fileName.equalsIgnoreCase("5BusTestCase.dat")){
                String strMessage="Don't overwrite test cases! Use a new name for user-created cases.";
                JOptionPane.showMessageDialog(this, strMessage, "Save As Message", JOptionPane.ERROR_MESSAGE); 
            }
            else{
                saveCaseFileData( );
            }
        }
        
     }
   private void saveCaseFileData( ) {
           
     config1.saveData();
     config5.saveData();
     branchData=config2.saveData();
     genData=config4.saveData();
     genLearningData=learnOption1.saveData();
     
     try {
          FileWriter       caseFileWriter = new FileWriter(caseFile);
          BufferedWriter   caseBufferWriter = new BufferedWriter(caseFileWriter);

          String strTemp;
          
          strTemp = "// FILENAME "+caseFile.getName()+"\n";
          caseBufferWriter.write(strTemp);
          caseBufferWriter.write("//\n");
          caseBufferWriter.write("// UNIT	SI\n");
          caseBufferWriter.write("// SI (International System of Units) such as MW\n");
          caseBufferWriter.write("//\n");
          caseBufferWriter.write("// Case Data File Format 2.0\n");
          caseBufferWriter.write("//\n");
          caseBufferWriter.write("// NN: Number of Buses\n");
          caseBufferWriter.write("//\n");
          caseBufferWriter.write("// PenaltyWeight: Penalty weight for DC-OPF objective function\n");
          caseBufferWriter.write("//\n");
          caseBufferWriter.write("// MaxCap: Thermal limits\n");
          caseBufferWriter.write("//\n");
          caseBufferWriter.write("// X: Reactance\n");
          caseBufferWriter.write("//\n");
          caseBufferWriter.write("// FCost,a,b: GenCo's cost attributes\n");
          caseBufferWriter.write("//\n");
          caseBufferWriter.write("// capL,capU: GenCo's operating capacity limits\n");
          caseBufferWriter.write("//\n");
          caseBufferWriter.write("//\n");
          caseBufferWriter.write("//\n");
          caseBufferWriter.write("\n");
          
          caseBufferWriter.write("// UNIT	SI\n");
          caseBufferWriter.write("BASE_S	100\n");
          caseBufferWriter.write("BASE_V	10\n\n");
 
          caseBufferWriter.write("#NodeDataStart\n");
          strTemp=String.format("//%1$8s\t%2$15s\n", "NN", "PenaltyWeight");
          caseBufferWriter.write(strTemp);
          double dTemp=Double.parseDouble(nodeData[0][0].toString());
          strTemp=String.format("%1$10.0f", dTemp);
          dTemp=Double.parseDouble(nodeData[0][1].toString());
          strTemp=strTemp+"\t"+String.format("%1$15.4f\n", dTemp);
          caseBufferWriter.write(strTemp);
          caseBufferWriter.write("#NodeDataEnd\n\n");
          
          caseBufferWriter.write("#BranchDataStart\n");
          strTemp=String.format("//%1$8s\t%2$10s\t%3$10s\t%4$15s\t%5$15s\n", "Name", "From", "To", "MaxCap", "Reactance");
          caseBufferWriter.write(strTemp);
          
          int iTemp;
          for(int i=0; i<iBranchData; i++) {
              strTemp=branchData[i][0].toString();
              strTemp=String.format("%1$10s", strTemp);
              
              for(int j=1; j<5; j++) {
                  if(j<3){
                      iTemp=Integer.parseInt(branchData[i][j].toString());
                      strTemp=strTemp+"\t"+String.format("%1$10d", iTemp);
                  }
                  else{
                      dTemp=Double.parseDouble(branchData[i][j].toString());
                       strTemp=strTemp+"\t"+String.format("%1$15.4f", dTemp);
                 }
              }
  
              strTemp = strTemp+"\n";
              caseBufferWriter.write(strTemp);

          }
          caseBufferWriter.write("#BranchDataEnd\n");
            
          caseBufferWriter.write("\n");
        
          caseBufferWriter.write("#GenDataStart\n");
          strTemp=String.format("//%1$8s\t%2$10s\t%3$10s\t%4$15s\t%5$15s\t%6$15s\t%7$15s\t%8$15s\t%9$15s\t%10$15s\t%11$15s\t%12$15s\t%13$15s\n",
                  "Name", "ID", "atBus", "a", "b", "capL", "capU", "FCost","emcoe","EA","pb","ps","InitMoney");
          caseBufferWriter.write(strTemp);
          for(int i=0; i<iGenData; i++) {
              strTemp=genData[i][0].toString();
              strTemp=String.format("%1$10s", strTemp);
              
              for(int j=1; j<13; j++) {
                  if(j<3){
                      iTemp=Integer.parseInt(genData[i][j].toString());
                      strTemp=strTemp+"\t"+String.format("%1$10d", iTemp);
                  }
                  else{
                      dTemp=Double.parseDouble(genData[i][j].toString());
                      strTemp=strTemp+"\t"+String.format("%1$15.4f", dTemp);
                 }
              }
  
              strTemp = strTemp+"\n";
              caseBufferWriter.write(strTemp);

          }
          caseBufferWriter.write("#GenDataEnd\n");
          caseBufferWriter.write("\n");
        
          caseBufferWriter.write("#LSEDataFixedDemandStart\n");
          strTemp=String.format("//%1$8s\t%2$10s\t%3$10s\t%4$15s\t%5$15s\t%6$15s\t%7$15s\t%8$15s\t%9$15s\t%10$15s\t%11$15s\n", 
                  "Name", "ID", "atBus", "H-00", "H-01", "H-02", "H-03", "H-04", "H-05", "H-06", "H-07");
          caseBufferWriter.write(strTemp);
          for(int i=0; i<iLSEData; i++) {
              strTemp=lseData[i][0].toString();
              strTemp=String.format("%1$10s", strTemp);
 
              for(int j=1; j<11; j++) {
                  if(j<3){
                      iTemp=Integer.parseInt(lseData[i][j].toString());
                      strTemp=strTemp+"\t"+String.format("%1$10d", iTemp);
                  }
                  else{
                      dTemp=Double.parseDouble(lseData[i][j].toString());
                      strTemp=strTemp+"\t"+String.format("%1$15.4f", dTemp);
                 }
              }

              strTemp = strTemp+"\n";
              caseBufferWriter.write(strTemp);

          }

          strTemp=String.format("//%1$8s\t%2$10s\t%3$10s\t%4$15s\t%5$15s\t%6$15s\t%7$15s\t%8$15s\t%9$15s\t%10$15s\t%11$15s\n", 
                  "Name", "ID", "atBus", "H-08", "H-09", "H-10", "H-11", "H-12", "H-13", "H-14", "H-15");
          caseBufferWriter.write(strTemp);
          for(int i=0; i<iLSEData; i++) {
              strTemp=lseData[i][0].toString();
              strTemp=String.format("%1$10s", strTemp);

              for(int j=1; j<3; j++) {
                  iTemp=Integer.parseInt(lseData[i][j].toString());
                  strTemp=strTemp+"\t"+String.format("%1$10d", iTemp);
              }

              for(int j=0; j<8; j++) {
                  dTemp=Double.parseDouble(lseData[i][j+11].toString());
                  strTemp=strTemp+"\t"+String.format("%1$15.4f", dTemp);
              }

              strTemp = strTemp+"\n";
              caseBufferWriter.write(strTemp);

          }

          strTemp=String.format("//%1$8s\t%2$10s\t%3$10s\t%4$15s\t%5$15s\t%6$15s\t%7$15s\t%8$15s\t%9$15s\t%10$15s\t%11$15s\n", 
                  "Name", "ID", "atBus", "H-16", "H-17", "H-18", "H-19", "H-20", "H-21", "H-22", "H-23");
          caseBufferWriter.write(strTemp);
          for(int i=0; i<iLSEData; i++) {
              strTemp=lseData[i][0].toString();
              strTemp=String.format("%1$10s", strTemp);

              for(int j=1; j<3; j++) {
                  iTemp=Integer.parseInt(lseData[i][j].toString());
                  strTemp=strTemp+"\t"+String.format("%1$10d", iTemp);
              }

              for(int j=0; j<8; j++) {
                  dTemp=Double.parseDouble(lseData[i][j+19].toString());
                  strTemp=strTemp+"\t"+String.format("%1$15.4f", dTemp);
              }

              strTemp = strTemp+"\n";
              caseBufferWriter.write(strTemp);

          }

          caseBufferWriter.write("#LSEDataFixedDemandEnd\n");
          caseBufferWriter.write("\n");
          
          if(bPriceSensitiveDemand){
              caseBufferWriter.write("\n");

              caseBufferWriter.write("#LSEDataPriceSensitiveDemandStart\n");
              strTemp=String.format("//%1$8s\t%2$10s\t%3$10s\t%4$10s\t%5$15s\t%6$15s\t%7$15s\n", 
                      "Name", "ID", "atBus", "hourIndex", "c", "d", "SLMax");
              caseBufferWriter.write(strTemp);
              
              for(int i=0; i<iLSEData; i++) {
                 for(int h=0; h<24; h++){
                     strTemp=lsePriceSensitiveDemand[i][h][0].toString();
                     strTemp=String.format("%1$10s", strTemp);

                      for(int j=1; j<7; j++) {
                          if(j<4){
                              iTemp=Integer.parseInt(lsePriceSensitiveDemand[i][h][j].toString());
                              strTemp=strTemp+"\t"+String.format("%1$10d", iTemp);
                          }
                          else{
                              dTemp=Double.parseDouble(lsePriceSensitiveDemand[i][h][j].toString());
                              strTemp=strTemp+"\t"+String.format("%1$15.4f", dTemp);
                         }

                      }

                      strTemp = strTemp+"\n";
                      caseBufferWriter.write(strTemp);
                 }
              }
              caseBufferWriter.write("#LSEDataPriceSensitiveDemandEnd\n");
              caseBufferWriter.write("\n");
          }

          if(bHybridDemand){
              caseBufferWriter.write("\n");
              caseBufferWriter.write("//LSE Data Hybrid Demand Flags: 1-> only fixed demand; 2-> only p-s demand; 3-> both\n");

              caseBufferWriter.write("#LSEDataHybridDemandStart\n");
              strTemp=String.format("//%1$8s\t%2$5s\t%3$5s\t%4$5s\t%5$5s\t%6$5s\t%7$5s\t%8$5s\t%9$5s\t%10$5s\t%11$5s\n", 
                      "Name", "ID", "atBus", "H-00", "H-01", "H-02", "H-03", "H-04", "H-05", "H-06", "H-07");
              caseBufferWriter.write(strTemp);
              for(int i=0; i<iLSEData; i++) {
                  strTemp=lseHybridDemand[i][0].toString();
                  strTemp=String.format("%1$10s", strTemp);

                  for(int j=1; j<11; j++) {
                      iTemp=Integer.parseInt(lseHybridDemand[i][j].toString());
                      strTemp=strTemp+"\t"+String.format("%1$5d", iTemp);
                  }

                  strTemp = strTemp+"\n";
                  caseBufferWriter.write(strTemp);

              }

              strTemp=String.format("//%1$8s\t%2$5s\t%3$5s\t%4$5s\t%5$5s\t%6$5s\t%7$5s\t%8$5s\t%9$5s\t%10$5s\t%11$5s\n", 
                      "Name", "ID", "atBus", "H-08", "H-09", "H-10", "H-11", "H-12", "H-13", "H-14", "H-15");
              caseBufferWriter.write(strTemp);
              for(int i=0; i<iLSEData; i++) {
                  strTemp=lseHybridDemand[i][0].toString();
                  strTemp=String.format("%1$10s", strTemp);

                  for(int j=1; j<3; j++) {
                      iTemp=Integer.parseInt(lseHybridDemand[i][j].toString());
                      strTemp=strTemp+"\t"+String.format("%1$5d", iTemp);
                  }

                  for(int j=0; j<8; j++) {
                      iTemp=Integer.parseInt(lseHybridDemand[i][j+11].toString());
                      strTemp=strTemp+"\t"+String.format("%1$5d", iTemp);
                  }

                  strTemp = strTemp+"\n";
                  caseBufferWriter.write(strTemp);

              }

              strTemp=String.format("//%1$8s\t%2$5s\t%3$5s\t%4$5s\t%5$5s\t%6$5s\t%7$5s\t%8$5s\t%9$5s\t%10$5s\t%11$5s\n", 
                      "Name", "ID", "atBus", "H-16", "H-17", "H-18", "H-19", "H-20", "H-21", "H-22", "H-23");
              caseBufferWriter.write(strTemp);
              for(int i=0; i<iLSEData; i++) {
                  strTemp=lseHybridDemand[i][0].toString();
                  strTemp=String.format("%1$10s", strTemp);

                  for(int j=1; j<3; j++) {
                      iTemp=Integer.parseInt(lseHybridDemand[i][j].toString());
                      strTemp=strTemp+"\t"+String.format("%1$5d", iTemp);
                  }

                  for(int j=0; j<8; j++) {
                      iTemp=Integer.parseInt(lseHybridDemand[i][j+19].toString());
                      strTemp=strTemp+"\t"+String.format("%1$5d", iTemp);
                  }

                  strTemp = strTemp+"\n";
                  caseBufferWriter.write(strTemp);

              }

              caseBufferWriter.write("#LSEDataHybridDemandEnd\n");
              caseBufferWriter.write("\n");
          }

          caseBufferWriter.write("//Reward Selection Flag: 0-> profits; 1-> net earnings\n");
          caseBufferWriter.write("#GenLearningDataStart\n");
          strTemp=String.format("//%1$8s\t%2$15s\t%3$15s\t%4$15s\t%5$15s\t%6$15s\t%7$15s\t%8$15s\t%9$15s\t%10$15s\t%11$15s\t%12$15s\t%13$15s\n", 
                  "Name", "InitPropensity", "Cooling", "Recency", "Experimentation", "M1", "M2", "M3", "RI_MAX_Lower", "RI_MAX_Upper", "RI_MIN_C", "SlopeStart", "RewardSelection");
          caseBufferWriter.write(strTemp);
          for(int i=0; i<iGenData; i++) {
              strTemp=genData[i][0].toString();
              strTemp=String.format("%1$10s", strTemp);
              
              for(int j=1; j<13; j++) {
                  strTemp=strTemp+"\t"+String.format("%1$15.4f", genLearningData[i][j-1]);
              }
  
              strTemp = strTemp+"\n";
              caseBufferWriter.write(strTemp);

          }
          caseBufferWriter.write("#GenLearningDataEnd\n");
          caseBufferWriter.write("\n");
        
          caseBufferWriter.write("\n");
          caseBufferWriter.write("\n");
          
          caseBufferWriter.close();
     }
     catch (IOException e)  {
     
     }
             
     System.out.println("Save case data file:"+caseFile.getName());
 }
  
     private void loadOutputData( ) {
     try {
          FileReader       outputFileReader = new FileReader(outputFile);
          BufferedReader   outputBufferReader = new BufferedReader(outputFileReader);

          String strTemp;
          boolean bLMPWithTrueCostData = false;
          boolean bgenAgentCommitmentWithTrueCostData = false;
          boolean bgenAgentProfitWithTrueCostData = false;
          boolean blseAgentPriceSensitiveDemandWithTrueCostData = false;
          boolean bLSEAgentSurplusWithTrueCostData = false;
          boolean bhasSolutionByDayData = false;
          boolean bgenAgentSupplyOfferByDayData = false;
          boolean bgenAgentCommitmentByDayData = false;
          boolean bgenAgentProfitAndNetGainByDayData = false;
          boolean bgenAgentActionPropensityAndProbilityByDay = false;
          boolean blseAgentSurplusByDayData = false;
          boolean bpriceSensitiveByDayData = false;
          boolean bbranchFlowByDayData = false;
          boolean bLMPByDayData = false;
          
          int iLMPWithTrueCost=0;
          int igenAgentCommitmentWithTrueCost=0;
          int ilseAgentPriceSensitiveDemandWithTrueCost=0;
          int ibhasSolutionByDayData=0;
          int igenAgentSupplyOfferByDay=0;
          int igenAgentCommitmentByDay=0;
          int igenAgentProfitAndNetGainByDay=0;
          int igenAgentActionPropensityAndProbilityByDay=0;
          int ilseAgentSurplusByDay=0;
          int ipriceSensitiveByDay=0;
          int ibranchFlowByDay=0;
          int iLMPByDay=0;
          
          ArrayList LMPWithTrueCostList=new ArrayList();
          ArrayList genAgentCommitmentWithTrueCostList=new ArrayList();
          ArrayList genAgentProfitWithTrueCostList=new ArrayList();
          ArrayList lseAgentPriceSensitiveDemandWithTrueCostList=new ArrayList();
          ArrayList lseAgentSurplusWithTrueCostList=new ArrayList();
          ArrayList hasSolutionByDayList=new ArrayList();
          ArrayList genAgentSupplyOfferByDayList=new ArrayList();
          ArrayList genAgentCommitmentByDayList=new ArrayList();
          ArrayList genAgentProfitAndNetGainByDayList=new ArrayList();
          ArrayList genAgentActionPropensityAndProbilityByDayList=new ArrayList();
          ArrayList lseAgentSurplusByDayList=new ArrayList();
          ArrayList priceSensitiveByDayList=new ArrayList();
          ArrayList branchFlowByDayList=new ArrayList();
          ArrayList LMPByDayList=new ArrayList();
            
          Object [][] lseHybridData=getLSEHybridDemandData();

          getAMESMarket().getLMPWithTrueCost().clear();
          getAMESMarket().getGenAgentCommitmentWithTrueCost().clear();
          getAMESMarket().getGenAgentProfitAndNetGainWithTrueCost().clear();
          getAMESMarket().getLSEAgentPriceSensitiveDemandWithTrueCost().clear();
          getAMESMarket().getLSEAgentSurplusWithTrueCost().clear();
          getAMESMarket().getHasSolutionByDay().clear();
          getAMESMarket().getGenAgentSupplyOfferByDay().clear();
          getAMESMarket().getGenAgentCommitmentByDay().clear();
          getAMESMarket().getGenAgentProfitAndNetGainByDay().clear();
          getAMESMarket().getGenAgentActionPropensityAndProbilityByDay().clear();
          getAMESMarket().getLSEAgenPriceSensitiveDemandByDay().clear();
          getAMESMarket().getBranchFlowByDay().clear();
          getAMESMarket().getLMPByDay().clear();
                       
               
          int iVerifyIndex=0;
          int iGenLearningIndex=0;
          while ((strTemp = outputBufferReader.readLine()) != null) {
              
               //System.out.println(strTemp);
               strTemp = strTemp.trim();
               if(strTemp.length()==0)
                   continue;
               
               //System.out.println(strTemp);
               
               int iCommentIndex = strTemp.indexOf("//");
               if(iCommentIndex == 0)
                   continue;
               
               // CASE FILENAME Verify:
               if(iVerifyIndex==0){
                   String caseFileName=strTemp;
                   
                   if((caseFile==null)||(!caseFileName.equalsIgnoreCase(caseFile.getName()))){
                       String ErrorMessage="Please load "+caseFile.getName()+" first!";
                       JOptionPane.showMessageDialog(this, ErrorMessage, "Load Output Data Verification Message", JOptionPane.ERROR_MESSAGE);
                       outputBufferReader.close();
                       return;
                   }
                   
               }
               
               // nodeNumber, branchNumber, genNumber, LSENumber verify
               if(iVerifyIndex==1){
                   int iIndex = strTemp.lastIndexOf("\t");
                   String strLSENumber = strTemp.substring(iIndex+1);
                   strLSENumber=strLSENumber.trim();
                   int tempLSENumber=Integer.parseInt(strLSENumber);
                   strTemp=strTemp.substring(0, iIndex);
                   
                   iIndex = strTemp.lastIndexOf("\t");
                   String strGenNumber = strTemp.substring(iIndex+1);
                   strGenNumber=strGenNumber.trim();
                   int tempGenNumber=Integer.parseInt(strGenNumber);
                   strTemp=strTemp.substring(0, iIndex);
                   
                   iIndex = strTemp.lastIndexOf("\t");
                   String strBranchNumber = strTemp.substring(iIndex+1);
                   strBranchNumber=strBranchNumber.trim();
                   int tempBranchNumber=Integer.parseInt(strBranchNumber);
                   strTemp=strTemp.substring(0, iIndex);
                   
                   strTemp=strTemp.trim();
                   int tempNodeNumber=Integer.parseInt(strTemp);
                   
                   if((tempLSENumber!=iLSEData)||(tempGenNumber!=iGenData)||(tempBranchNumber!=iBranchData)||(tempNodeNumber!=iNodeData)){
                       String ErrorMessage="Please load "+caseFile.getName()+" first!";
                       JOptionPane.showMessageDialog(this, ErrorMessage, "Load Output Data Verification Message", JOptionPane.ERROR_MESSAGE);
                       outputBufferReader.close();
                       return;
                  }
                    
              }
               
               // Load iLMPWithTrueCost, igenAgentCommitmentWithTrueCost ...
               if(iVerifyIndex==2){
                   int iIndex = strTemp.lastIndexOf("\t");
                   String strInt = strTemp.substring(iIndex+1);
                   strInt=strInt.trim();
                   iLMPByDay=Integer.parseInt(strInt);
                   strTemp=strTemp.substring(0, iIndex);
                   
                   iIndex = strTemp.lastIndexOf("\t");
                   strInt = strTemp.substring(iIndex+1);
                   strInt=strInt.trim();
                   ibranchFlowByDay=Integer.parseInt(strInt);
                   strTemp=strTemp.substring(0, iIndex);
                   
                   iIndex = strTemp.lastIndexOf("\t");
                   strInt = strTemp.substring(iIndex+1);
                   strInt=strInt.trim();
                   ipriceSensitiveByDay=Integer.parseInt(strInt);
                   strTemp=strTemp.substring(0, iIndex);
                   
                   iIndex = strTemp.lastIndexOf("\t");
                   strInt = strTemp.substring(iIndex+1);
                   strInt=strInt.trim();
                   ilseAgentSurplusByDay=Integer.parseInt(strInt);
                   strTemp=strTemp.substring(0, iIndex);
                   
                   iIndex = strTemp.lastIndexOf("\t");
                   strInt = strTemp.substring(iIndex+1);
                   strInt=strInt.trim();
                   igenAgentProfitAndNetGainByDay=Integer.parseInt(strInt);
                   strTemp=strTemp.substring(0, iIndex);
                   
                   iIndex = strTemp.lastIndexOf("\t");
                   strInt = strTemp.substring(iIndex+1);
                   strInt=strInt.trim();
                   igenAgentCommitmentByDay=Integer.parseInt(strInt);
                   strTemp=strTemp.substring(0, iIndex);
                   
                   iIndex = strTemp.lastIndexOf("\t");
                   strInt = strTemp.substring(iIndex+1);
                   strInt=strInt.trim();
                   igenAgentSupplyOfferByDay=Integer.parseInt(strInt);
                   strTemp=strTemp.substring(0, iIndex);
                   
                   iIndex = strTemp.lastIndexOf("\t");
                   strInt = strTemp.substring(iIndex+1);
                   strInt=strInt.trim();
                   ilseAgentPriceSensitiveDemandWithTrueCost=Integer.parseInt(strInt);
                   strTemp=strTemp.substring(0, iIndex);
                   
                   iIndex = strTemp.lastIndexOf("\t");
                   strInt = strTemp.substring(iIndex+1);
                   strInt=strInt.trim();
                   igenAgentCommitmentWithTrueCost=Integer.parseInt(strInt);
                   strTemp=strTemp.substring(0, iIndex);
                   
                   strInt = strTemp.trim();
                   iLMPWithTrueCost=Integer.parseInt(strInt);
              }

               // Load learning parameters: Cooling, Experimentation ...
               if(iVerifyIndex==3){
                   if(iGenLearningIndex==0)
                       genLearningData=new double[iGenData][12];
                   
                   int iIndex = strTemp.lastIndexOf("\t");
                   String  strInt = strTemp.substring(iIndex+1);
                   strInt=strInt.trim();
                   genLearningData[iGenLearningIndex][11]=Integer.parseInt(strInt);
                   strTemp=strTemp.substring(0, iIndex);
                   
                   iIndex = strTemp.lastIndexOf("\t");
                   strInt = strTemp.substring(iIndex+1);
                   strInt=strInt.trim();
                   genLearningData[iGenLearningIndex][10]=Double.parseDouble(strInt);
                   strTemp=strTemp.substring(0, iIndex);
                   
                   iIndex = strTemp.lastIndexOf("\t");
                   strInt = strTemp.substring(iIndex+1);
                   strInt=strInt.trim();
                   genLearningData[iGenLearningIndex][9]=Double.parseDouble(strInt);
                   strTemp=strTemp.substring(0, iIndex);
                   
                   iIndex = strTemp.lastIndexOf("\t");
                   strInt = strTemp.substring(iIndex+1);
                   strInt=strInt.trim();
                   genLearningData[iGenLearningIndex][8]=Double.parseDouble(strInt);
                   strTemp=strTemp.substring(0, iIndex);
                   
                   iIndex = strTemp.lastIndexOf("\t");
                   strInt = strTemp.substring(iIndex+1);
                   strInt=strInt.trim();
                   genLearningData[iGenLearningIndex][7]=Double.parseDouble(strInt);
                   strTemp=strTemp.substring(0, iIndex);
                   
                   iIndex = strTemp.lastIndexOf("\t");
                   strInt = strTemp.substring(iIndex+1);
                   strInt=strInt.trim();
                   genLearningData[iGenLearningIndex][6]=Integer.parseInt(strInt);
                   strTemp=strTemp.substring(0, iIndex);
                   
                   iIndex = strTemp.lastIndexOf("\t");
                   strInt = strTemp.substring(iIndex+1);
                   strInt=strInt.trim();
                   genLearningData[iGenLearningIndex][5]=Integer.parseInt(strInt);
                   strTemp=strTemp.substring(0, iIndex);
                   
                   iIndex = strTemp.lastIndexOf("\t");
                   strInt = strTemp.substring(iIndex+1);
                   strInt=strInt.trim();
                   genLearningData[iGenLearningIndex][4]=Integer.parseInt(strInt);
                   strTemp=strTemp.substring(0, iIndex);
                   
                   iIndex = strTemp.lastIndexOf("\t");
                   strInt = strTemp.substring(iIndex+1);
                   strInt=strInt.trim();
                   genLearningData[iGenLearningIndex][3]=Double.parseDouble(strInt);
                   strTemp=strTemp.substring(0, iIndex);
                   
                   iIndex = strTemp.lastIndexOf("\t");
                   strInt = strTemp.substring(iIndex+1);
                   strInt=strInt.trim();
                   genLearningData[iGenLearningIndex][2]=Double.parseDouble(strInt);
                   strTemp=strTemp.substring(0, iIndex);
                   
                   iIndex = strTemp.lastIndexOf("\t");
                   strInt = strTemp.substring(iIndex+1);
                   strInt=strInt.trim();
                   genLearningData[iGenLearningIndex][1]=Double.parseDouble(strInt);
                   strTemp=strTemp.substring(0, iIndex);
                   
                   strInt = strTemp.trim();
                   genLearningData[iGenLearningIndex][0]=Double.parseDouble(strInt);
                   
                   iGenLearningIndex++;
                   
                   if(iGenLearningIndex<iGenData)
                       iVerifyIndex--;
                   
              }
               
               // Load simulation control parameters: RandomSeed, iMaxDay ...
               if(iVerifyIndex==4){
                   int iIndex = strTemp.lastIndexOf("\t");
                   String strInt = strTemp.substring(iIndex+1);
                   strInt=strInt.trim();
                   int Threshold=Integer.parseInt(strInt);
                   if(Threshold==1)
                       bThreshold=true;
                   else
                       bThreshold=false;
                   strTemp=strTemp.substring(0, iIndex);
                   
                   iIndex = strTemp.lastIndexOf("\t");
                   strInt = strTemp.substring(iIndex+1);
                   strInt=strInt.trim();
                   int MaximumDay=Integer.parseInt(strInt);
                   if(MaximumDay==1)
                       bMaximumDay=true;
                   else
                       bMaximumDay=false;
                   
                   strTemp=strTemp.substring(0, iIndex);
                   
                   iIndex = strTemp.lastIndexOf("\t");
                   strInt = strTemp.substring(iIndex+1);
                   strInt=strInt.trim();
                   dLSEPriceCap=Double.parseDouble(strInt);
                   strTemp=strTemp.substring(0, iIndex);
                   
                   iIndex = strTemp.lastIndexOf("\t");
                   strInt = strTemp.substring(iIndex+1);
                   strInt=strInt.trim();
                   dGenPriceCap=Double.parseDouble(strInt);
                   strTemp=strTemp.substring(0, iIndex);
                   
                   iIndex = strTemp.lastIndexOf("\t");
                   strInt = strTemp.substring(iIndex+1);
                   strInt=strInt.trim();
                   dThresholdProbability=Double.parseDouble(strInt);
                   strTemp=strTemp.substring(0, iIndex);
                   
                   iIndex = strTemp.lastIndexOf("\t");
                   strInt = strTemp.substring(iIndex+1);
                   strInt=strInt.trim();
                   iMaxDay=Integer.parseInt(strInt);
                   strTemp=strTemp.substring(0, iIndex);
                
                   strInt = strTemp.trim();
                   RandomSeed=Long.parseLong(strInt);
              }

               // Load simulation control parameters: bActionProbabilityCheck, iStartDay ...
               if(iVerifyIndex==5){
                   int iIndex = strTemp.lastIndexOf("\t");
                   String strInt = strTemp.substring(iIndex+1);
                   strInt=strInt.trim();
                   dDailyNetEarningThreshold=Double.parseDouble(strInt);
                   strTemp=strTemp.substring(0, iIndex);
                    
                   iIndex = strTemp.lastIndexOf("\t");
                   strInt = strTemp.substring(iIndex+1);
                   strInt=strInt.trim();
                   iDailyNetEarningDayLength=Integer.parseInt(strInt);
                   strTemp=strTemp.substring(0, iIndex);
                  
                   iIndex = strTemp.lastIndexOf("\t");
                   strInt = strTemp.substring(iIndex+1);
                   strInt=strInt.trim();
                   iDailyNetEarningStartDay=Integer.parseInt(strInt);
                   strTemp=strTemp.substring(0, iIndex);
                  
                   iIndex = strTemp.lastIndexOf("\t");
                   strInt = strTemp.substring(iIndex+1);
                   strInt=strInt.trim();
                   int bNetEarning=Integer.parseInt(strInt);
                   if(bNetEarning==1)
                       bDailyNetEarningThreshold=true;
                   else
                       bDailyNetEarningThreshold=false;
                   strTemp=strTemp.substring(0, iIndex);
                  
                   iIndex = strTemp.lastIndexOf("\t");
                   strInt = strTemp.substring(iIndex+1);
                   strInt=strInt.trim();
                   dLearningCheckDifference=Double.parseDouble(strInt);
                   strTemp=strTemp.substring(0, iIndex);
                  
                   iIndex = strTemp.lastIndexOf("\t");
                   strInt = strTemp.substring(iIndex+1);
                   strInt=strInt.trim();
                   iLearningCheckDayLength=Integer.parseInt(strInt);
                   strTemp=strTemp.substring(0, iIndex);
                  
                   iIndex = strTemp.lastIndexOf("\t");
                   strInt = strTemp.substring(iIndex+1);
                   strInt=strInt.trim();
                   iLearningCheckStartDay=Integer.parseInt(strInt);
                   strTemp=strTemp.substring(0, iIndex);
                  
                   iIndex = strTemp.lastIndexOf("\t");
                   strInt = strTemp.substring(iIndex+1);
                   strInt=strInt.trim();
                   int bLearnProb=Integer.parseInt(strInt);
                   if(bLearnProb==1)
                       bLearningCheck=true;
                   else
                       bLearningCheck=false;
                   strTemp=strTemp.substring(0, iIndex);
                  
                   iIndex = strTemp.lastIndexOf("\t");
                   strInt = strTemp.substring(iIndex+1);
                   strInt=strInt.trim();
                   dActionProbability=Double.parseDouble(strInt);
                   strTemp=strTemp.substring(0, iIndex);
                  
                   iIndex = strTemp.lastIndexOf("\t");
                   strInt = strTemp.substring(iIndex+1);
                   strInt=strInt.trim();
                   iCheckDayLength=Integer.parseInt(strInt);
                   strTemp=strTemp.substring(0, iIndex);
                  
                   iIndex = strTemp.lastIndexOf("\t");
                   strInt = strTemp.substring(iIndex+1);
                   strInt=strInt.trim();
                   iStartDay=Integer.parseInt(strInt);
                   strTemp=strTemp.substring(0, iIndex);
                  
                   iIndex = strTemp.lastIndexOf("\t");
                   strInt = strTemp.substring(iIndex+1);
                   strInt=strInt.trim();
                   int bActionProb=Integer.parseInt(strInt);
                   if(bActionProb==1)
                       bActionProbabilityCheck=true;
                   else
                       bActionProbabilityCheck=false;
                   
              }

               iVerifyIndex++;
               
               int iLMPWithTrueCostDataStart = strTemp.indexOf("#LMPWithTrueCostDataStart");
               if(iLMPWithTrueCostDataStart == 0 ) {
                   bLMPWithTrueCostData = true;
                   continue;
               }
               
              int iLMPWithTrueCostDataEnd = strTemp.indexOf("#LMPWithTrueCostDataEnd");
               if(iLMPWithTrueCostDataEnd == 0 ) {
                   bLMPWithTrueCostData = false;
                  
                   double [][] lmp=new double[24][iNodeData];
                    
                   for( int i = 0; i < 24; i ++ ) {
                       String strLMP = (String)LMPWithTrueCostList.get(i);
                       int iFields = iNodeData;
                   
                       while(iFields>0) {
                           int iIndex = strLMP.lastIndexOf("\t");
                           if( iIndex < 0)
                                iIndex = strLMP.lastIndexOf(" ");

                          String strData = strLMP.substring(iIndex+1);
                          strData=strData.trim();
                          
                          lmp[i][iFields-1]=Double.parseDouble(strData);

                          iFields --;

                          if(iIndex>0)
                            strLMP = strLMP.substring(0,iIndex);
                       }
                   }
                   
                   getAMESMarket().addLMPWithTrueCost(lmp);
               }

              if(bLMPWithTrueCostData)
               {
                    LMPWithTrueCostList.add(strTemp);
                     continue;
               }
          
               int iGeneratorCommitmentWithTrueCostDataStart = strTemp.indexOf("#GeneratorCommitmentWithTrueCostDataStart");
               if(iGeneratorCommitmentWithTrueCostDataStart == 0 ) {
                   bgenAgentCommitmentWithTrueCostData = true;
                   continue;
               }
               
              int iGeneratorCommitmentWithTrueCostDataEnd = strTemp.indexOf("#GeneratorCommitmentWithTrueCostDataEnd");
               if(iGeneratorCommitmentWithTrueCostDataEnd == 0 ) {
                   bgenAgentCommitmentWithTrueCostData = false;
                  
                   double [][] genCommit=new double[24][iGenData];
                    
                   for( int i = 0; i < 24; i ++ ) {
                       String strGenCommit = (String)genAgentCommitmentWithTrueCostList.get(i);
                       int iFields = iGenData;
                   
                       while(iFields>0) {
                           int iIndex = strGenCommit.lastIndexOf("\t");
                           if( iIndex < 0)
                                iIndex = strGenCommit.lastIndexOf(" ");

                          String strData = strGenCommit.substring(iIndex+1);
                          strData=strData.trim();
                          
                          genCommit[i][iFields-1]=Double.parseDouble(strData);

                          iFields --;

                          if(iIndex>0)
                            strGenCommit = strGenCommit.substring(0,iIndex);
                       }
                   }
                   
                   getAMESMarket().addGenAgentCommitmentWithTrueCost(genCommit);
               }

              if(bgenAgentCommitmentWithTrueCostData)
               {
                    genAgentCommitmentWithTrueCostList.add(strTemp);
                     continue;
               }
          
               int iGeneratorProfitWithTrueCostDataStart = strTemp.indexOf("#GeneratorProfitWithTrueCostDataStart");
               if(iGeneratorProfitWithTrueCostDataStart == 0 ) {
                   bgenAgentProfitWithTrueCostData = true;
                   continue;
               }
               
              int iGeneratorProfitWithTrueCostDataEnd = strTemp.indexOf("#GeneratorProfitWithTrueCostDataEnd");
               if(iGeneratorProfitWithTrueCostDataEnd == 0 ) {
                   bgenAgentProfitWithTrueCostData = false;
                  
                   double [][] genProfit=new double[iGenData][75];
                    
                   for( int i = 0; i < 25*iGenData; i ++ ) {
                       String strGenProfit = (String)genAgentProfitWithTrueCostList.get(i);
                       int iFields = 3;
                   
                       while(iFields>0) {
                           int iIndex = strGenProfit.lastIndexOf("\t");
                           if( iIndex < 0)
                                iIndex = strGenProfit.lastIndexOf(" ");

                          String strData = strGenProfit.substring(iIndex+1);
                          strData=strData.trim();

                          genProfit[i-(i/iGenData)*iGenData][(i/iGenData)*3+iFields-1]=Double.parseDouble(strData);

                          iFields --;

                          if(iIndex>0)
                            strGenProfit = strGenProfit.substring(0,iIndex);
                       }
                   }
                   
                   getAMESMarket().addGenAgentProfitAndNetGainWithTrueCost(genProfit);
               }

              if(bgenAgentProfitWithTrueCostData)
               {
                    genAgentProfitWithTrueCostList.add(strTemp);
                     continue;
               }
          
               int iLSEPriceSensitiveDemandWithTrueCostDataStart = strTemp.indexOf("#LSEPriceSensitiveDemandWithTrueCostDataStart");
               if(iLSEPriceSensitiveDemandWithTrueCostDataStart == 0 ) {
                   blseAgentPriceSensitiveDemandWithTrueCostData = true;
                   continue;
               }
               
              int iLSEPriceSensitiveDemandWithTrueCostDataEnd = strTemp.indexOf("#LSEPriceSensitiveDemandWithTrueCostDataEnd");
               if(iLSEPriceSensitiveDemandWithTrueCostDataEnd == 0 ) {
                   blseAgentPriceSensitiveDemandWithTrueCostData = false;
                        
                   double [][] lseDispatch=new double[24][iLSEData];
                   double [] dispatch;
                 
                   for( int i = 0; i < 24; i ++ ) {
                      int psLoadIndex=0;

                      for(int j=0; j<iLSEData; j++){
                          int hourlyLoadHybridFlagByLSE=Integer.parseInt(lseHybridData[j][i+3].toString());

                          if((hourlyLoadHybridFlagByLSE&2)==2)
                              psLoadIndex++;
                        }
                      
                       dispatch=new double [psLoadIndex];
                       String strlseDispatch = (String)lseAgentPriceSensitiveDemandWithTrueCostList.get(i);
                       int iFields = iLSEData;
                   
                       while(iFields>0) {
                           int iIndex = strlseDispatch.lastIndexOf("\t");
                           if( iIndex < 0)
                                iIndex = strlseDispatch.lastIndexOf(" ");

                          String strData = strlseDispatch.substring(iIndex+1);
                          strData=strData.trim();
                          
                          psLoadIndex=0;

                          for(int j=0; j<iFields; j++){
                              int hourlyLoadHybridFlagByLSE=Integer.parseInt(lseHybridData[j][i+3].toString());

                              if((hourlyLoadHybridFlagByLSE&2)==2)
                                  psLoadIndex++;
                            }
                            
                          if(psLoadIndex>0)
                            dispatch[psLoadIndex-1]=Double.parseDouble(strData);

                          iFields --;

                          if(iIndex>0)
                            strlseDispatch = strlseDispatch.substring(0,iIndex);
                       }
                       
                       lseDispatch[i]=dispatch;
                   }
                   
                   getAMESMarket().addLSEAgentPriceSensitiveDemandWithTrueCost(lseDispatch);
               }

              if(blseAgentPriceSensitiveDemandWithTrueCostData)
               {
                    lseAgentPriceSensitiveDemandWithTrueCostList.add(strTemp);
                     continue;
               }
          
               int iLSESurplusWithTrueCostDataStart = strTemp.indexOf("#LSESurplusWithTrueCostDataStart");
               if(iLSESurplusWithTrueCostDataStart == 0 ) {
                   bLSEAgentSurplusWithTrueCostData = true;
                   continue;
               }
               
              int iLSESurplusWithTrueCostDataEnd = strTemp.indexOf("#LSESurplusWithTrueCostDataEnd");
               if(iLSESurplusWithTrueCostDataEnd == 0 ) {
                   bLSEAgentSurplusWithTrueCostData = false;
                  
                   double [][] lseSurplus=new double[iLSEData][25];
                    
                   for( int i = 0; i < 25*iLSEData; i ++ ) {
                       String strLSESurplus = (String)lseAgentSurplusWithTrueCostList.get(i);
                       int iFields = 1;
                   
                       while(iFields>0) {
                           int iIndex = strLSESurplus.lastIndexOf("\t");
                           if( iIndex < 0)
                                iIndex = strLSESurplus.lastIndexOf(" ");

                          String strData = strLSESurplus.substring(iIndex+1);
                          strData=strData.trim();
                          
                          lseSurplus[i-(i/iLSEData)*iLSEData][i/iLSEData]=Double.parseDouble(strData);

                          iFields --;

                          if(iIndex>0)
                            strLSESurplus = strLSESurplus.substring(0,iIndex);
                       }
                   }
                   
                   getAMESMarket().addLSEAgentSurplusWithTrueCost(lseSurplus);
               }

              if(bLSEAgentSurplusWithTrueCostData)
               {
                    lseAgentSurplusWithTrueCostList.add(strTemp);
                     continue;
               }
          
              int iHasSolutionDataStart = strTemp.indexOf("#HasSolutionDataStart");
              if(iHasSolutionDataStart == 0 ) {
                   bhasSolutionByDayData = true;
                   continue;
              }
               
              int iHasSolutionDataEnd = strTemp.indexOf("#HasSolutionDataEnd");
               if(iHasSolutionDataEnd == 0 ) {
                   bhasSolutionByDayData = false;
                  
                   for(int d=0; d<hasSolutionByDayList.size(); d++){
                       int [] hasSolution=new int[24];

                       String strHasSolution = (String)hasSolutionByDayList.get(d);
                       int iFields = 24;

                       while(iFields>0) {
                           int iIndex = strHasSolution.lastIndexOf("\t");
                           if( iIndex < 0)
                                iIndex = strHasSolution.lastIndexOf(" ");

                          String strData = strHasSolution.substring(iIndex+1);
                          strData=strData.trim();

                          hasSolution[iFields-1]=Integer.parseInt(strData);

                          iFields --;

                          if(iIndex>0)
                            strHasSolution = strHasSolution.substring(0,iIndex);
                       }

                       getAMESMarket().addHasSolutionByDay(hasSolution);
                   }
               }

              if(bhasSolutionByDayData)
               {
                    hasSolutionByDayList.add(strTemp);
                     continue;
               }
          
              int iGeneratorSupplyOfferDataStart = strTemp.indexOf("#GeneratorSupplyOfferDataStart");
              if(iGeneratorSupplyOfferDataStart == 0 ) {
                   bgenAgentSupplyOfferByDayData = true;
                   continue;
              }
               
              int iGeneratorSupplyOfferDataEnd = strTemp.indexOf("#GeneratorSupplyOfferDataEnd");
               if(iGeneratorSupplyOfferDataEnd == 0 ) {
                   bgenAgentSupplyOfferByDayData = false;
                  
                   for(int d=0; d<igenAgentSupplyOfferByDay; d++){
                       double [][] genSupply=new double[iGenData][4];

                       for( int i = 0; i < iGenData; i ++ ) {
                           String strGenSupply = (String)genAgentSupplyOfferByDayList.get(i+d*iGenData);
                           int iFields = 4;

                           while(iFields>0) {
                               int iIndex = strGenSupply.lastIndexOf("\t");
                               if( iIndex < 0)
                                    iIndex = strGenSupply.lastIndexOf(" ");

                              String strData = strGenSupply.substring(iIndex+1);
                              strData=strData.trim();

                              genSupply[i][iFields-1]=Double.parseDouble(strData);

                              iFields --;

                              if(iIndex>0)
                                strGenSupply = strGenSupply.substring(0,iIndex);
                           }
                       }

                       getAMESMarket().addGenAgentSupplyOfferByDay(genSupply);
                   }
               }

              if(bgenAgentSupplyOfferByDayData)
               {
                    genAgentSupplyOfferByDayList.add(strTemp);
                     continue;
               }
               int iGeneratorCommitmentDataStart = strTemp.indexOf("#GeneratorCommitmentDataStart");
               if(iGeneratorCommitmentDataStart == 0 ) {
                   bgenAgentCommitmentByDayData = true;
                   continue;
               }
               
              int iGeneratorCommitmentDataEnd = strTemp.indexOf("#GeneratorCommitmentDataEnd");
               if(iGeneratorCommitmentDataEnd == 0 ) {
                   bgenAgentCommitmentByDayData = false;
                  
                   for(int d=0; d<igenAgentCommitmentByDay; d++){
                       double [][] genCommit=new double[24][iGenData];

                       for( int i = 0; i < 24; i ++ ) {
                           String strGenCommit = (String)genAgentCommitmentByDayList.get(i+d*24);
                           int iFields = iGenData;

                           while(iFields>0) {
                               int iIndex = strGenCommit.lastIndexOf("\t");
                               if( iIndex < 0)
                                    iIndex = strGenCommit.lastIndexOf(" ");

                              String strData = strGenCommit.substring(iIndex+1);
                              strData=strData.trim();

                              genCommit[i][iFields-1]=Double.parseDouble(strData);

                              iFields --;

                              if(iIndex>0)
                                strGenCommit = strGenCommit.substring(0,iIndex);
                           }
                       }

                       getAMESMarket().addGenAgentCommitmentByDay(genCommit);
                   }
               }

              if(bgenAgentCommitmentByDayData)
               {
                    genAgentCommitmentByDayList.add(strTemp);
                     continue;
               }
          
              int iGeneratorProfitDataStart = strTemp.indexOf("#GeneratorProfitDataStart");
              if(iGeneratorProfitDataStart == 0 ) {
                   bgenAgentProfitAndNetGainByDayData = true;
                   continue;
              }
               
              int iGeneratorProfitDataEnd = strTemp.indexOf("#GeneratorProfitDataEnd");
               if(iGeneratorProfitDataEnd == 0 ) {
                   bgenAgentProfitAndNetGainByDayData = false;
                  
                   for(int d=0; d<igenAgentProfitAndNetGainByDay; d++){
                       double [][] genProfit=new double[iGenData][3];

                       for( int i = 0; i < iGenData; i ++ ) {
                           String strGenProfit = (String)genAgentProfitAndNetGainByDayList.get(i+d*iGenData);
                           int iFields = 3;

                           while(iFields>0) {
                               int iIndex = strGenProfit.lastIndexOf("\t");
                               if( iIndex < 0)
                                    iIndex = strGenProfit.lastIndexOf(" ");

                              String strData = strGenProfit.substring(iIndex+1);
                              strData=strData.trim();

                              genProfit[i][iFields-1]=Double.parseDouble(strData);

                              iFields --;

                              if(iIndex>0)
                                strGenProfit = strGenProfit.substring(0,iIndex);
                           }
                       }

                       getAMESMarket().addGenAgentProfitAndNetGainByDay(genProfit);
                   }
               }

              if(bgenAgentProfitAndNetGainByDayData)
               {
                    genAgentProfitAndNetGainByDayList.add(strTemp);
                     continue;
               }
          
              int iGeneratorPropDataStart = strTemp.indexOf("#GeneratorPropensityDataStart");
              if(iGeneratorPropDataStart == 0 ) {
                   bgenAgentActionPropensityAndProbilityByDay = true;
                   continue;
              }
               
              int iGeneratorPropDataEnd = strTemp.indexOf("#GeneratorPropensityDataEnd");
               if(iGeneratorPropDataEnd == 0 ) {
                   bgenAgentActionPropensityAndProbilityByDay = false;
                  
                   for(int d=0; d<genAgentActionPropensityAndProbilityByDayList.size()/iGenData; d++){
                       double [][] genProp=new double[iGenData][3];

                       for( int i = 0; i < iGenData; i ++ ) {
                           String strgenProp = (String)genAgentActionPropensityAndProbilityByDayList.get(i+d*iGenData);
                           int iFields = 3;

                           while(iFields>0) {
                               int iIndex = strgenProp.lastIndexOf("\t");
                               if( iIndex < 0)
                                    iIndex = strgenProp.lastIndexOf(" ");

                              String strData = strgenProp.substring(iIndex+1);
                              strData=strData.trim();

                              genProp[i][iFields-1]=Double.parseDouble(strData);

                              iFields --;

                              if(iIndex>0)
                                strgenProp = strgenProp.substring(0,iIndex);
                           }
                       }

                       getAMESMarket().addGenAgentActionPropensityAndProbilityByDay(genProp);
                   }
               }

              if(bgenAgentActionPropensityAndProbilityByDay)
               {
                    genAgentActionPropensityAndProbilityByDayList.add(strTemp);
                     continue;
               }
          
              int iLSESurplusDataStart = strTemp.indexOf("#LSESurplusDataStart");
              if(iLSESurplusDataStart == 0 ) {
                   blseAgentSurplusByDayData = true;
                   continue;
              }
               
              int iLSESurplusDataEnd = strTemp.indexOf("#LSESurplusDataEnd");
               if(iLSESurplusDataEnd == 0 ) {
                   blseAgentSurplusByDayData = false;
                  
                   for(int d=0; d<ilseAgentSurplusByDay; d++){
                       double [][] lseProfit=new double[iLSEData][1];

                       for( int i = 0; i < iLSEData; i ++ ) {
                           String strLSEProfit = (String)lseAgentSurplusByDayList.get(i+d*iLSEData);
                           int iFields = 1;

                           while(iFields>0) {
                               int iIndex = strLSEProfit.lastIndexOf("\t");
                               if( iIndex < 0)
                                    iIndex = strLSEProfit.lastIndexOf(" ");

                              String strData = strLSEProfit.substring(iIndex+1);
                              strData=strData.trim();

                              lseProfit[i][iFields-1]=Double.parseDouble(strData);

                              iFields --;

                              if(iIndex>0)
                                strLSEProfit = strLSEProfit.substring(0,iIndex);
                           }
                       }

                       getAMESMarket().addLSEAgentSurplusByDay(lseProfit);
                   }
               }

              if(blseAgentSurplusByDayData)
               {
                    lseAgentSurplusByDayList.add(strTemp);
                     continue;
               }
          
               int iLSEPriceSensitiveDemandDataStart = strTemp.indexOf("#LSEPriceSensitiveDemandDataStart");
               if(iLSEPriceSensitiveDemandDataStart == 0 ) {
                   bpriceSensitiveByDayData = true;
                   continue;
               }
               
              int iLSEPriceSensitiveDemandDataEnd = strTemp.indexOf("#LSEPriceSensitiveDemandDataEnd");
               if(iLSEPriceSensitiveDemandDataEnd == 0 ) {
                   bpriceSensitiveByDayData = false;

                   for(int d=0; d<ipriceSensitiveByDay; d++){
                       double [][] lseDispatch=new double[24][iLSEData];
                       double [] dispatch;

                       for( int i = 0; i < 24; i ++ ) {
                          int psLoadIndex=0;

                          for(int j=0; j<iLSEData; j++){
                              int hourlyLoadHybridFlagByLSE=Integer.parseInt(lseHybridData[j][i+3].toString());

                              if((hourlyLoadHybridFlagByLSE&2)==2)
                                  psLoadIndex++;
                            }

                           dispatch=new double [psLoadIndex];
                           String strlseDispatch = (String)priceSensitiveByDayList.get(i+d*24);
                           int iFields = iLSEData;

                           while(iFields>0) {
                               int iIndex = strlseDispatch.lastIndexOf("\t");
                               if( iIndex < 0)
                                    iIndex = strlseDispatch.lastIndexOf(" ");

                              String strData = strlseDispatch.substring(iIndex+1);
                              strData=strData.trim();

                              psLoadIndex=0;

                              for(int j=0; j<iFields; j++){
                                  int hourlyLoadHybridFlagByLSE=Integer.parseInt(lseHybridData[j][i+3].toString());

                                  if((hourlyLoadHybridFlagByLSE&2)==2)
                                      psLoadIndex++;
                                }

                              if(psLoadIndex>0)
                                dispatch[psLoadIndex-1]=Double.parseDouble(strData);

                              iFields --;

                              if(iIndex>0)
                                strlseDispatch = strlseDispatch.substring(0,iIndex);
                           }

                           lseDispatch[i]=dispatch;
                       }

                       getAMESMarket().addLSEAgenPriceSensitiveDemandByDay(lseDispatch);
                   }
               }

              if(bpriceSensitiveByDayData)
               {
                    priceSensitiveByDayList.add(strTemp);
                     continue;
               }
          
               int iBranchPowerFlowDataStart = strTemp.indexOf("#BranchPowerFlowDataStart");
               if(iBranchPowerFlowDataStart == 0 ) {
                   bbranchFlowByDayData = true;
                   continue;
               }
               
              int iBranchPowerFlowDataEnd = strTemp.indexOf("#BranchPowerFlowDataEnd");
               if(iBranchPowerFlowDataEnd == 0 ) {
                   bbranchFlowByDayData = false;
                  
                   for(int d=0; d<ibranchFlowByDay; d++){
                       double [][] branchFlow=new double[24][iBranchData];

                       for( int i = 0; i < 24; i ++ ) {
                           String strBranchFlow = (String)branchFlowByDayList.get(i+d*24);
                           int iFields = iBranchData;

                           while(iFields>0) {
                               int iIndex = strBranchFlow.lastIndexOf("\t");
                               if( iIndex < 0)
                                    iIndex = strBranchFlow.lastIndexOf(" ");

                              String strData = strBranchFlow.substring(iIndex+1);
                              strData=strData.trim();

                              branchFlow[i][iFields-1]=Double.parseDouble(strData);

                              iFields --;

                              if(iIndex>0)
                                strBranchFlow = strBranchFlow.substring(0,iIndex);
                           }
                       }

                       getAMESMarket().addBranchFlowByDay(branchFlow);
                   }
               }

              if(bbranchFlowByDayData)
               {
                    branchFlowByDayList.add(strTemp);
                     continue;
               }
          
               int iNodeLMPDataStart = strTemp.indexOf("#NodeLMPDataStart");
               if(iNodeLMPDataStart == 0 ) {
                   bLMPByDayData = true;
                   continue;
               }
               
              int iNodeLMPDataEnd = strTemp.indexOf("#NodeLMPDataEnd");
               if(iNodeLMPDataEnd == 0 ) {
                   bLMPByDayData = false;
                  
                   for(int d=0; d<iLMPByDay; d++){
                       double [][] lmp=new double[24][iNodeData];

                       for( int i = 0; i < 24; i ++ ) {
                           String strLMP = (String)LMPByDayList.get(i+d*24);
                           int iFields = iNodeData;

                           while(iFields>0) {
                               int iIndex = strLMP.lastIndexOf("\t");
                               if( iIndex < 0)
                                    iIndex = strLMP.lastIndexOf(" ");

                              String strData = strLMP.substring(iIndex+1);
                              strData=strData.trim();

                              lmp[i][iFields-1]=Double.parseDouble(strData);

                              iFields --;

                              if(iIndex>0)
                                strLMP = strLMP.substring(0,iIndex);
                           }
                       }

                       getAMESMarket().addLMPByDay(lmp);
                   }
               }

              if(bLMPByDayData)
               {
                    LMPByDayList.add(strTemp);
                     continue;
               }
          
          }
              
          outputBufferReader.close();
          enableViewMenu();
     }
     catch (IOException e)  {
     
     }
     
   }

private void saveOutputData( ) {
    if(isNewCase())
        return;
    
    String caseFileName=caseFile.getName();
    caseFileName=caseFileName.substring(0, caseFileName.length()-4);
    String outputFileName="";           

    if(BatchMode==1){ // for multiple random seeds batch mode
       if(bMultiRandomSeeds)
            outputFileName=caseFileName+"_"+iCurrentRandomSeedsIndex+".out";
       else if(bMultiCases&&(!bMultiRandomSeeds))
           outputFileName=caseFileName+".out";
    } 
    else
        outputFileName=caseFileName+"_temp.out";
    
       File outputFile=new File(caseFile.getParent()+"\\"+outputFileName);

       try {
            FileWriter       outputFileWriter = new FileWriter(outputFile);
            BufferedWriter   outputBufferWriter = new BufferedWriter(outputFileWriter);

            String [] nodeName=getNodeNameData( );
            int iNodeNumber=nodeName.length;
            
            ArrayList LMPWithTrueCost=getAMESMarket().getLMPWithTrueCost();
            int iLMPWithTrueCost=LMPWithTrueCost.size();
             
            ArrayList genAgentProfitWithTrueCost=getAMESMarket().getGenAgentProfitAndNetGainWithTrueCost();
            int igenAgentProfitWithTrueCost=genAgentProfitWithTrueCost.size();
            
            ArrayList genAgentCommitmentWithTrueCost=getAMESMarket().getGenAgentCommitmentWithTrueCost();
            int igenAgentCommitmentWithTrueCost=genAgentCommitmentWithTrueCost.size();

            ArrayList lseAgentPriceSensitiveDemandWithTrueCost=getAMESMarket().getLSEAgentPriceSensitiveDemandWithTrueCost();
            int ilseAgentPriceSensitiveDemandWithTrueCost=lseAgentPriceSensitiveDemandWithTrueCost.size();
      
            ArrayList LSEAgentSurplusWithTrueCost=getAMESMarket().getLSEAgentSurplusWithTrueCost();
            int iLSEAgentSurplusWithTrueCost=LSEAgentSurplusWithTrueCost.size();

            ArrayList hasSolutionByDay=getAMESMarket().getHasSolutionByDay();
            int ihasSolutionByDay=hasSolutionByDay.size();
            
            ArrayList genAgentSupplyOfferByDay=getAMESMarket().getGenAgentSupplyOfferByDay();
            int igenAgentSupplyOfferByDay=genAgentSupplyOfferByDay.size();
      
            ArrayList genAgentCommitmentByDay=getAMESMarket().getGenAgentCommitmentByDay();
            int igenAgentCommitmentByDay=genAgentCommitmentByDay.size();
            
            ArrayList genAgentProfitAndNetGainByDay=getAMESMarket().getGenAgentProfitAndNetGainByDay();
            int igenAgentProfitAndNetGainByDay=genAgentProfitAndNetGainByDay.size();
       
            ArrayList getGenAgentActionPropensityAndProbilityByDay=getAMESMarket().getGenAgentActionPropensityAndProbilityByDay();
            int igetGenAgentActionPropensityAndProbilityByDay=getGenAgentActionPropensityAndProbilityByDay.size();
       
            ArrayList lseAgentSurplusByDay=getAMESMarket().getLSEAgentSurplusByDay();
            int ilseAgentSurplusByDay=lseAgentSurplusByDay.size();
      
            ArrayList priceSensitiveByDay=getAMESMarket().getLSEAgenPriceSensitiveDemandByDay();
            int ipriceSensitiveByDay=priceSensitiveByDay.size();
       
            ArrayList branchFlowByDay=getAMESMarket().getBranchFlowByDay();
            int ibranchFlowByDay=branchFlowByDay.size();
            
            ArrayList LMPByDay=getAMESMarket().getLMPByDay();
            int iLMPByDay=LMPByDay.size();
      
            ArrayList genAction=getAMESMarket().getGenActions();
            
            Object [][] branchData=getBranchData( );
            int iBranchNumber=branchData.length;
         
            Object [][] genData=getGeneratorData( );
            int iGenNumber=genData.length;
            
            Object [][] lseHybridData=getLSEHybridDemandData();
            int iLSENumber=lseHybridData.length;
            
            String strTemp;

            outputBufferWriter.write("// Output Data File:\n");
            outputBufferWriter.write("//\n");
            strTemp = "// FILENAME "+outputFile.getName()+"\n";
            outputBufferWriter.write(strTemp);
            outputBufferWriter.write("//\n");

            strTemp = "// CASE FILENAME :\n";
            outputBufferWriter.write(strTemp);
            strTemp = caseFile.getName()+"\n";
            outputBufferWriter.write(strTemp);
            outputBufferWriter.write("\n");
            
            strTemp=String.format("//%1$13s\t%2$15s\t%3$15s\t%4$15s\n", "iNodeNumber", "iBranchNumber", "iGenNumber", "iLSENumber");
            outputBufferWriter.write(strTemp);
            strTemp=String.format("%1$15d\t%2$15d\t%3$15d\t%4$15d\n", iNodeNumber, iBranchNumber, iGenNumber, iLSENumber);
            outputBufferWriter.write(strTemp);

            strTemp=String.format("//%1$18s\t%2$25s\t%3$27s\t%4$20s\t%5$20s\t%6$25s\t%7$20s\t%8$20s\t%9$20s\t%10$20s\n", "iLMPWithTrueCost", "iGenCommitWithTrueCost", "iLSEPSDispatchWithTrueCost", "igenAgentSupplyOffer", "igenAgentCommitment", 
                    "igenAgentProfitAndNetGain", "ilseAgentSurplus", "ipriceSensitive", "ibranchFlow", "iLMP");
            outputBufferWriter.write(strTemp);
            strTemp=String.format("%1$20d\t%2$25d\t%3$27d\t%4$20d\t%5$20d\t%6$25d\t%7$20d\t%8$20d\t%9$20d\t%10$20d\n", iLMPWithTrueCost, igenAgentCommitmentWithTrueCost, ilseAgentPriceSensitiveDemandWithTrueCost, igenAgentSupplyOfferByDay, igenAgentCommitmentByDay, 
                    igenAgentProfitAndNetGainByDay, ilseAgentSurplusByDay, ipriceSensitiveByDay, ibranchFlowByDay, iLMPByDay);
            outputBufferWriter.write(strTemp);
          
            strTemp=String.format("//%1$15s\t%2$20s\t%3$20s\t%4$8s\t%5$8s\t%6$8s\t%7$8s\t%8$20s\t%9$20s\t%10$20s\t%11$20s\t%12$20s\n", "Cooling", "Experimentation", "InitPropensity", "Recency", "M1", "M2", 
                    "M3", "RI_MAX_Lower", "RI_MAX_Upper", "RI_MIN_C", "SlopeStart", "RewardSelection");
            outputBufferWriter.write(strTemp);
            
            for(int i=0; i<iGenNumber; i++){
                strTemp=String.format("%1$17f\t%2$20f\t%3$20f\t%4$8f\t%5$8d\t%6$8d\t%7$8d\t%8$20f\t%9$20f\t%10$20f\t%11$20f\t%12$20d\n", 
                        genLearningData[i][0],
                        genLearningData[i][1],
                        genLearningData[i][2],
                        genLearningData[i][3],
                        (int)genLearningData[i][4],
                        (int)genLearningData[i][5],
                        (int)genLearningData[i][6],
                        genLearningData[i][7],
                        genLearningData[i][8],
                        genLearningData[i][9],
                        genLearningData[i][10],
                        (int)genLearningData[i][11]);
                
                outputBufferWriter.write(strTemp);
            }
          
            strTemp=String.format("//%1$18s\t%2$8s\t%3$25s\t%4$15s\t%5$15s\t%6$12s\t%7$12s\n", "RandomSeed", "iMaxDay", "dThresholdProbability", "dGenPriceCap", "dLSEPriceCap", 
                    "bMaximumDay", "bThreshold");
            outputBufferWriter.write(strTemp);
           
            int MaximumDay;
            if(bMaximumDay)
                MaximumDay=1;
            else 
                MaximumDay=0;
            
            int Threshold;
            if(bThreshold)
                Threshold=1;
            else 
                Threshold=0;
            
            strTemp=String.format("%1$20d\t%2$8d\t%3$25f\t%4$15f\t%5$15f\t%6$12d\t%7$12d\n", RandomSeed, iMaxDay, dThresholdProbability, dGenPriceCap, dLSEPriceCap, 
                    MaximumDay, Threshold);
            outputBufferWriter.write(strTemp);
          
            strTemp=String.format("//%1$13s\t%2$15s\t%3$15s\t%4$15s\t%5$15s\t%6$15s\t%7$15s\t%8$15s\t%9$15s\t%10$15s\t%11$15s\t%12$15s\n", 
                    "bActionProb", 
                    "iStartDay", 
                    "iDayLength", 
                    "dActionProb", 
                    "bLearnProb", 
                    "iLearnStartDay", 
                    "iLearnDayLength", 
                    "dLearnProb", 
                    "bNetEarning", 
                    "iNEStartDay", 
                    "iNEDayLength", 
                    "dNEThreshold");
            outputBufferWriter.write(strTemp);
           
            int bActionProb;
            if(bActionProbabilityCheck)
                bActionProb=1;
            else 
                bActionProb=0;
            
            int bLearnProb;
            if(bLearningCheck)
                bLearnProb=1;
            else 
                bLearnProb=0;
            
            int bNetEarning;
            if(bDailyNetEarningThreshold)
                bNetEarning=1;
            else 
                bNetEarning=0;
            
            strTemp=String.format("%1$15d\t%2$15d\t%3$15d\t%4$15f\t%5$15d\t%6$15d\t%7$15d\t%8$15f\t%9$15d\t%10$15d\t%11$15d\t%12$15f\n", 
                    bActionProb, 
                    iStartDay, 
                    iCheckDayLength, 
                    dActionProbability, 
                    bLearnProb, 
                    iLearningCheckStartDay, 
                    iLearningCheckDayLength,
                    dLearningCheckDifference,
                    bNetEarning,
                    iDailyNetEarningStartDay,
                    iDailyNetEarningDayLength,
                    dDailyNetEarningThreshold);
            
            outputBufferWriter.write(strTemp);
          
            
            outputBufferWriter.write("#LMPWithTrueCostDataStart\n");
            strTemp=String.format("//%1$5s", "Hour");
            for(int i=0; i<iNodeNumber; i++){
                String NodeID=nodeName[i];
                strTemp+=String.format("\t%1$15s", NodeID);
            }
            strTemp+="\n";
            outputBufferWriter.write(strTemp);

            for(int i=0; i<iLMPWithTrueCost; i++){
                double [][] lmp=(double [][])LMPWithTrueCost.get(i);

                for(int h=0; h<24; h++){
                    strTemp=String.format("%1$5d", h);
    
                    for(int j=0; j<iNodeNumber; j++){
                        strTemp+=String.format("\t%1$15f", lmp[h][j]);
                    }
                    
                    strTemp+="\n";
                    outputBufferWriter.write(strTemp);
                }
            }
            
            outputBufferWriter.write("#LMPWithTrueCostDataEnd\n");
            outputBufferWriter.write("\n");
            
            outputBufferWriter.write("#GeneratorCommitmentWithTrueCostDataStart\n");
            strTemp=String.format("//%1$5s", "Hour");
            for(int i=0; i<iGenNumber; i++){
                String GenID=genData[i][0].toString();
                strTemp+=String.format("\t%1$15s", GenID);
            }
            strTemp+="\n";
            outputBufferWriter.write(strTemp);

            for(int i=0; i<igenAgentCommitmentWithTrueCost; i++){
                double [][] genCommitmentWithTrueCost=(double [][])genAgentCommitmentWithTrueCost.get(i);

                for(int h=0; h<24; h++){
                    strTemp=String.format("%1$5d", h);
    
                    for(int j=0; j<iGenNumber; j++){
                        strTemp+=String.format("\t%1$15f", genCommitmentWithTrueCost[h][j]);
                    }
                    
                    strTemp+="\n";
                    outputBufferWriter.write(strTemp);
                }
            }
            
            outputBufferWriter.write("#GeneratorCommitmentWithTrueCostDataEnd\n");
            outputBufferWriter.write("\n");
            
            outputBufferWriter.write("#GeneratorProfitWithTrueCostDataStart\n");
            strTemp=String.format("//%1$5s\t%2$8s\t%3$15s\t%4$15s\t%5$15s", "Hour", "GenID", "Profit", "Net Earnings", "Revenue");
            strTemp+="\n";
            outputBufferWriter.write(strTemp);
            
            double [][] genCommitmentWithTrueCost=(double [][])genAgentProfitWithTrueCost.get(0);
            
            for(int h=0; h<25; h++){
                for(int i=0; i<iGenNumber; i++){
                    if(h==0){// total
                        strTemp=String.format("%1$7s", "Total");
                    }
                    else
                        strTemp=String.format("%1$7d", h);
                    
                    String GenID=genData[i][0].toString();
                    strTemp+=String.format("\t%1$8s", GenID);
                        
                    strTemp+=String.format("\t%1$15f", genCommitmentWithTrueCost[i][h*3]);
                    strTemp+=String.format("\t%1$15f", genCommitmentWithTrueCost[i][h*3+1]);
                    strTemp+=String.format("\t%1$15f", genCommitmentWithTrueCost[i][h*3+2]);
                     
                    strTemp+="\n";
                    outputBufferWriter.write(strTemp);
                }
                
            }
            outputBufferWriter.write("#GeneratorProfitWithTrueCostDataEnd\n");
            outputBufferWriter.write("\n");
            
            outputBufferWriter.write("#LSEPriceSensitiveDemandWithTrueCostDataStart\n");
            strTemp=String.format("//%1$5s", "Hour");
            for(int i=0; i<iLSENumber; i++){
                String LSEID=lseHybridData[i][0].toString();
                strTemp+=String.format("\t%1$15s", LSEID);
            }
            strTemp+="\n";
            outputBufferWriter.write(strTemp);

            for(int i=0; i<ilseAgentPriceSensitiveDemandWithTrueCost; i++){
                double [][] priceSensitive=(double [][])lseAgentPriceSensitiveDemandWithTrueCost.get(i);

                for(int h=0; h<24; h++){
                    strTemp=String.format("%1$5d", h);
                    int psLoadIndex=0;
                    
                    for(int j=0; j<iLSENumber; j++){
                        int hourlyLoadHybridFlagByLSE=Integer.parseInt(lseHybridData[j][h+3].toString());
                                  
                        if((hourlyLoadHybridFlagByLSE&2)==2)
                            strTemp+=String.format("\t%1$15f", priceSensitive[h][psLoadIndex++]);
                        else
                            strTemp+=String.format("\t%1$15f", 0.0);
                    }
                    
                    strTemp+="\n";    
                    outputBufferWriter.write(strTemp);
                }
            }
            
            outputBufferWriter.write("#LSEPriceSensitiveDemandWithTrueCostDataEnd\n");
            outputBufferWriter.write("\n");
            
            outputBufferWriter.write("#LSESurplusWithTrueCostDataStart\n");
            strTemp=String.format("//%1$5s\t%2$8s\t%3$15s", "Hour", "LSEID", "Surplus");
            strTemp+="\n";
            outputBufferWriter.write(strTemp);
            
            double [][] lseAgentSurplusWithTrueCost=(double [][])LSEAgentSurplusWithTrueCost.get(0);
            
            for(int h=0; h<25; h++){
                for(int i=0; i<iLSENumber; i++){
                    if(h==0){// total
                        strTemp=String.format("%1$7s", "Total");
                    }
                    else
                        strTemp=String.format("%1$7d", h);
                    
                    String LSEID=lseHybridData[i][0].toString();
                    strTemp+=String.format("\t%1$8s", LSEID);
                        
                    strTemp+=String.format("\t%1$15f", lseAgentSurplusWithTrueCost[i][h]);
                     
                    strTemp+="\n";
                    outputBufferWriter.write(strTemp);
                }
                
            }
            outputBufferWriter.write("#LSESurplusWithTrueCostDataEnd\n");
            outputBufferWriter.write("\n");
            
            outputBufferWriter.write("#HasSolutionDataStart\n");
            strTemp=String.format("//%1$5s\t%2$4s\t%3$4s\t%4$4s\t%5$4s\t%6$4s\t%7$4s\t%8$4s\t%9$4s\t%10$4s\t%11$4s\t%12$4s\t%13$4s\t%14$4s\t%15$4s\t%16$4s\t%17$4s\t%18$4s\t%19$4s\t%20$4s\t%21$4s\t%22$4s\t%23$4s\t%24$4s\t%25$4s\n", 
                    "Day", "H-00", "H-01", "H-02", "H-03", "H-04", "H-05", "H-06", "H-07",
                           "H-08", "H-09", "H-10", "H-11", "H-12", "H-13", "H-14", "H-15",
                           "H-16", "H-17", "H-18", "H-19", "H-20", "H-21", "H-22", "H-23");
            outputBufferWriter.write(strTemp);

            for(int i=0; i<ihasSolutionByDay; i++){
                int [] hasSolution=(int [])hasSolutionByDay.get(i);

                strTemp=String.format("%1$7d\t",i+1);
                for(int j=0; j<24; j++)
                    strTemp+=String.format("%1$4d\t",hasSolution[j]);

                strTemp+="\n";

                outputBufferWriter.write(strTemp);
                
            }
            
            outputBufferWriter.write("#HasSolutionDataEnd\n");
            outputBufferWriter.write("\n");
            
            outputBufferWriter.write("#GeneratorSupplyOfferDataStart\n");
            strTemp=String.format("//%1$5s\t%2$10s\t%3$15s\t%4$15s\t%5$15s\t%6$15s\n", "Day", "GenCo Name", "aR ($/MWh)", "bR ($/MW2h)", "CapRL (MW)", "CapRU (MW)");
            outputBufferWriter.write(strTemp);

            for(int i=0; i<igenAgentSupplyOfferByDay; i++){
                double [][] genOffer=(double [][])genAgentSupplyOfferByDay.get(i);

                for(int j=0; j<iGenNumber; j++){
                    strTemp=String.format("%1$5d\t%2$10s\t", i+1, genData[j][0].toString());
                    strTemp+=String.format("%1$15f\t%2$15f\t%3$15f\t%4$15f\n", genOffer[j][0], genOffer[j][1], genOffer[j][2], genOffer[j][3]);

                    outputBufferWriter.write(strTemp);
                }
            }
            
            outputBufferWriter.write("#GeneratorSupplyOfferDataEnd\n");
            outputBufferWriter.write("\n");
            
            outputBufferWriter.write("#GeneratorCommitmentDataStart\n");
            strTemp=String.format("//%1$5s\t%2$5s", "Day", "Hour");
            for(int i=0; i<iGenNumber; i++){
                String GenID=genData[i][0].toString();
                strTemp+=String.format("\t%1$15s", GenID);
            }
            strTemp+="\n";
            outputBufferWriter.write(strTemp);

            for(int i=0; i<igenAgentCommitmentByDay; i++){
                double [][] genCommitment=(double [][])genAgentCommitmentByDay.get(i);

                for(int h=0; h<24; h++){
                    strTemp=String.format("%1$5d\t%2$5d", i+1, h);
                    
                    for(int j=0; j<iGenNumber; j++){
                        strTemp+=String.format("\t%1$15f", genCommitment[h][j]);
                    }
                    
                    strTemp+="\n";    
                    outputBufferWriter.write(strTemp);
                }
            }
            
            outputBufferWriter.write("#GeneratorCommitmentDataEnd\n");
            outputBufferWriter.write("\n");
            
            outputBufferWriter.write("#GeneratorProfitDataStart\n");
            strTemp=String.format("//%1$5s\t%2$10s\t%3$15s\t%4$15s\t%5$14s\n", "Day", "GenCo Name", "Profit($/H)", "Net Earnings($/H)", "Revenues($/H)");
            outputBufferWriter.write(strTemp);

            for(int i=0; i<igenAgentProfitAndNetGainByDay; i++){
                double [][] genProfit=(double [][])genAgentProfitAndNetGainByDay.get(i);

                for(int j=0; j<iGenNumber; j++){
                    strTemp=String.format("%1$5d\t%2$10s\t", i+1, genData[j][0].toString());
                    strTemp+=String.format("%1$15f\t%2$15f\t%3$15f\n", genProfit[j][0], genProfit[j][1], genProfit[j][2]);

                    outputBufferWriter.write(strTemp);
                }
            }
            
            outputBufferWriter.write("#GeneratorProfitDataEnd\n");
            outputBufferWriter.write("\n");

            outputBufferWriter.write("#GeneratorPropensityDataStart\n");
            strTemp=String.format("//%1$5s\t%2$10s\t%3$15s\t%4$15s\t%5$14s\n", "Day", "GenCo Name", "ActionID", "Propensity", "Probability");
            outputBufferWriter.write(strTemp);

            for(int i=0; i<igetGenAgentActionPropensityAndProbilityByDay; i++){
                double [][] genProp=(double [][])getGenAgentActionPropensityAndProbilityByDay.get(i);

                for(int j=0; j<iGenNumber; j++){
                    strTemp=String.format("%1$5d\t%2$10s\t", i+1, genData[j][0].toString());
                    strTemp+=String.format("%1$15f\t%2$15f\t%3$15f\n", genProp[j][0], genProp[j][1], genProp[j][2]);

                    outputBufferWriter.write(strTemp);
                }
            }
            
            outputBufferWriter.write("#GeneratorPropensityDataEnd\n");
            outputBufferWriter.write("\n");

            outputBufferWriter.write("#LSESurplusDataStart\n");
            strTemp=String.format("//%1$5s\t%2$10s\t%3$15s\n", "Day", "LSE Name", "Surplus($/H)");
            outputBufferWriter.write(strTemp);

            for(int i=0; i<ilseAgentSurplusByDay; i++){
                double [][] lseSurplus=(double [][])lseAgentSurplusByDay.get(i);

                for(int j=0; j<iLSENumber; j++){
                    strTemp=String.format("%1$5d\t%2$10s\t", i+1, lseHybridData[j][0].toString());
                    strTemp+=String.format("%1$15f\n", lseSurplus[j][0]);

                    outputBufferWriter.write(strTemp);
                }
            }
            
            outputBufferWriter.write("#LSESurplusDataEnd\n");
            outputBufferWriter.write("\n");

            outputBufferWriter.write("#LSEPriceSensitiveDemandDataStart\n");
            strTemp=String.format("//%1$5s\t%2$5s", "Day", "Hour");
            for(int i=0; i<iLSENumber; i++){
                String LSEID=lseHybridData[i][0].toString();
                strTemp+=String.format("\t%1$15s", LSEID);
            }
            strTemp+="\n";
            outputBufferWriter.write(strTemp);

            for(int i=0; i<ipriceSensitiveByDay; i++){
                double [][] priceSensitive=(double [][])priceSensitiveByDay.get(i);

                for(int h=0; h<24; h++){
                    strTemp=String.format("%1$5d\t%2$5d", i+1, h);
                    int psLoadIndex=0;
                    
                    for(int j=0; j<iLSENumber; j++){
                        int hourlyLoadHybridFlagByLSE=Integer.parseInt(lseHybridData[j][h+3].toString());
                                  
                        if((hourlyLoadHybridFlagByLSE&2)==2)
                            strTemp+=String.format("\t%1$15f", priceSensitive[h][psLoadIndex++]);
                        else
                            strTemp+=String.format("\t%1$15f", 0.0);
                    }
                    
                    strTemp+="\n";    
                    outputBufferWriter.write(strTemp);
                }
            }
            
            outputBufferWriter.write("#LSEPriceSensitiveDemandDataEnd\n");
            outputBufferWriter.write("\n");
            
            outputBufferWriter.write("#BranchPowerFlowDataStart\n");
            strTemp=String.format("//%1$5s\t%2$5s", "Day", "Hour");
            for(int i=0; i<iBranchNumber; i++){
                String BranchID=branchData[i][0].toString();
                strTemp+=String.format("\t%1$15s", BranchID);
            }
            strTemp+="\n";
            outputBufferWriter.write(strTemp);

            for(int i=0; i<ibranchFlowByDay; i++){
                double [][] branchFlow=(double [][])branchFlowByDay.get(i);

                for(int h=0; h<24; h++){
                    strTemp=String.format("%1$5d\t%2$5d", i+1, h);
                    
                    for(int j=0; j<iBranchNumber; j++){
                        strTemp+=String.format("\t%1$15f", branchFlow[h][j]);
                    }
                    
                    strTemp+="\n";    
                    outputBufferWriter.write(strTemp);
                }
            }
            
            outputBufferWriter.write("#BranchPowerFlowDataEnd\n");
            outputBufferWriter.write("\n");
            
            outputBufferWriter.write("#NodeLMPDataStart\n");
            strTemp=String.format("//%1$5s\t%2$5s", "Day", "Hour");
            for(int i=0; i<iNodeNumber; i++){
                String NodeID=nodeName[i];
                strTemp+=String.format("\t%1$15s", NodeID);
            }
            strTemp+="\n";
            outputBufferWriter.write(strTemp);

            for(int i=0; i<iLMPByDay; i++){
                double [][] lmp=(double [][])LMPByDay.get(i);

                for(int h=0; h<24; h++){
                    strTemp=String.format("%1$5d\t%2$5d", i+1, h);
                    
                    for(int j=0; j<iNodeNumber; j++){
                        strTemp+=String.format("\t%1$15f", lmp[h][j]);
                    }
                    
                    strTemp+="\n";    
                    outputBufferWriter.write(strTemp);
                }
            }
            
            outputBufferWriter.write("#NodeLMPDataEnd\n");
            outputBufferWriter.write("\n");
            
            outputBufferWriter.write("#GeneratorLastDayActionDataStart\n");
            strTemp=String.format("//%1$10s", "ActNumber");
            for(int i=0; i<iGenNumber; i++){
                String GenID=genData[i][0].toString();
                strTemp+=String.format("\t%1$15s", GenID);
            }
            strTemp+="\n";
            outputBufferWriter.write(strTemp);

            double [][] Action=(double [][])genAction.get(0);

            strTemp="            ";
            int iMaxActionNumber=0;
            for(int i=0; i<iGenNumber; i++){
                int ActNumber=Action[i].length;
                if(iMaxActionNumber<ActNumber)
                    iMaxActionNumber=ActNumber;
                
                strTemp+=String.format("\t%1$15d", ActNumber);
            }
            strTemp+="\n";
            outputBufferWriter.write(strTemp);
            
            strTemp=String.format("//%1$8s", "ActID");
            for(int i=0; i<iGenNumber; i++){
                String GenID=genData[i][0].toString();
                strTemp+=String.format("\t%1$15s", GenID);
            }
            strTemp+="\n";
            outputBufferWriter.write(strTemp);

            for(int actID=0; actID<iMaxActionNumber; actID++){
                strTemp=String.format("%1$10d", actID);

                for(int j=0; j<iGenNumber; j++){
                    if(actID<Action[j].length)
                        strTemp+=String.format("\t%1$15f", Action[j][actID]);
                    else
                        strTemp+=String.format("\t%1$15f", 0.0);
                }

                strTemp+="\n";
                outputBufferWriter.write(strTemp);
            }
            
            outputBufferWriter.write("#GeneratorLastDayActionDataEnd\n");
            outputBufferWriter.write("\n");
            
            outputBufferWriter.write("\n\n\n");
            strTemp="StopCode=";
            strTemp+=String.format("\t%1$15d", amesMarket.getStopCode());
            strTemp+="\n";
            outputBufferWriter.write(strTemp);
            outputBufferWriter.write("\n");

            outputBufferWriter.close();
      }
       catch (IOException e)  {
     
       }
          
     System.out.println("Save case output data file:"+outputFile.getName());
}
  
   private void exitItemActionPerformed(java.awt.event.ActionEvent evt) {
      dispose();                    // Release resources
      System.out.println("Exit");
      System.exit(0);               // Exit the program
    }
    
    public void addViewMenu( ){
        viewMenu.setMnemonic('V');                    // Create shortcut

        // Construct the file drop-down menu
        caseReportItem = viewMenu.add("Output Tables");             
        caseReportItem.addActionListener(new ActionListener() {
               public void actionPerformed(java.awt.event.ActionEvent evt) {
                  caseReportItemActionPerformed(evt);
                }
        });
        caseReportItem.setEnabled(false);

        caseCurveItem = viewMenu.add("Output Charts");             
        caseCurveItem.addActionListener(new ActionListener() {
               public void actionPerformed(java.awt.event.ActionEvent evt) {
                  caseCurveItemActionPerformed(evt);
                }
        });
        caseCurveItem.setEnabled(false);

        // Add View menu accelerators
        caseReportItem.setAccelerator(KeyStroke.getKeyStroke('R', CTRL_DOWN_MASK));
        caseCurveItem.setAccelerator(KeyStroke.getKeyStroke('C', CTRL_DOWN_MASK));

        menuBar.add(viewMenu);
   }
    
    public void enableViewMenu(){
        caseReportItem.setEnabled(true);
        caseCurveItem.setEnabled(true);
    }

    public void disableViewMenu(){
        caseReportItem.setEnabled(false);
        caseCurveItem.setEnabled(false);
    }

    private void caseReportItemActionPerformed(java.awt.event.ActionEvent evt) {
        SplitTable splitTable = new SplitTable();
        splitTable.setAMESFrame(this);
        splitTable.createAndShowGUI();
        splitTable.setMinimumSize(new Dimension(700, 650));
        splitTable.setVisible(true);
     }
    
 private void caseCurveItemActionPerformed(java.awt.event.ActionEvent evt) {
        SplitChart splitChart = new SplitChart();
        splitChart.setAMESFrame(this);
        splitChart.createAndShowGUI();
        splitChart.setMinimumSize(new Dimension(900, 650));
        splitChart.setVisible(true);
    }
    
  private void caseParametersItemActionPerformed(java.awt.event.ActionEvent evt) {
       Toolkit theKit = config1.getToolkit(); 
       Dimension wndSize = theKit.getScreenSize(); 
       
       Rectangle configBounds=config1.getBounds();
       
       config1.setLocation((wndSize.width-configBounds.width)/2, (wndSize.height-configBounds.height)/2);
       config1.setVisible(true);
   }

   public void activeConfig1( ) {
       Toolkit theKit = config1.getToolkit(); 
       Dimension wndSize = theKit.getScreenSize();   
       
       Rectangle configBounds=config1.getBounds();
       
       config1.setLocation((wndSize.width-configBounds.width)/2, (wndSize.height-configBounds.height)/2);
       config1.setVisible(true);
   }
   
   public void activeConfig2( ) {
       Toolkit theKit = config2.getToolkit(); 
       Dimension wndSize = theKit.getScreenSize();   
       
       Rectangle configBounds=config2.getBounds();
       
       config2.setLocation((wndSize.width-configBounds.width)/2, (wndSize.height-configBounds.height)/2);
       config2.setVisible(true);
   }
   
   public void activeConfig4( ) {
       Toolkit theKit = config4.getToolkit(); 
       Dimension wndSize = theKit.getScreenSize();       
       
       Rectangle configBounds=config4.getBounds();
       
       config4.setLocation((wndSize.width-configBounds.width)/2, (wndSize.height-configBounds.height)/2);
       config4.setVisible(true);
   }
   
   public void activeConfig5( ) {
       Toolkit theKit = config5.getToolkit(); 
       Dimension wndSize = theKit.getScreenSize();      
       
       Rectangle configBounds=config5.getBounds();
       
       config5.setLocation((wndSize.width-configBounds.width)/2, (wndSize.height-configBounds.height)/2);
       config5.setVisible(true);
   }
   
   public void activeLearnOption1( ) {
       Toolkit theKit = learnOption1.getToolkit(); 
       Dimension wndSize = theKit.getScreenSize();  
       
       Rectangle configBounds=learnOption1.getBounds();
       
       learnOption1.setLocation((wndSize.width-configBounds.width)/2, (wndSize.height-configBounds.height)/2);

       //Check if learning parameters are set
       if(bGenLearningDataSet==false){// no learning parameter
              genLearningData = new double[iGenData][12];

              for( int i = 0; i < iGenData; i ++ ) {
                  genLearningData[i][0]=Default_InitPropensity;
                  genLearningData[i][1]=Default_Cooling;
                  genLearningData[i][2]=Default_Recency;
                  genLearningData[i][3]=Default_Experimentation;
                  genLearningData[i][4]=Default_M1;
                  genLearningData[i][5]=Default_M2;
                  genLearningData[i][6]=Default_M3;
                  genLearningData[i][7]=Default_RI_MAX_Lower;
                  genLearningData[i][8]=Default_RI_MAX_Upper;
                  genLearningData[i][9]=Default_RI_MIN_C;
                  genLearningData[i][10]=Default_SlopeStart;
                  genLearningData[i][11]=Default_iRewardSelection;
              }
              
              learnOption1.loadData(genData, genLearningData);
              bGenLearningDataSet=true;
       }


       learnOption1.setVisible(true);
   }
   
   public void activeSimulationControl( ) {
       Toolkit theKit = simulationControl.getToolkit(); 
       Dimension wndSize = theKit.getScreenSize();  
       
       Rectangle configBounds=simulationControl.getBounds();
       
       simulationControl.setLocation((wndSize.width-configBounds.width)/2, (wndSize.height-configBounds.height)/2);
       simulationControl.setVisible(true);
   }
   
    public void addCommandMenu( ){
        commandMenu.setMnemonic('M');                    // Create shortcut

        // Construct the file drop-down menu
        startItem = commandMenu.add("Start");          
        startItem.setIcon(new javax.swing.ImageIcon(AMESFrame.class.getResource("/resources/start.gif")));
        startItem.addActionListener(new ActionListener() {
               public void actionPerformed(java.awt.event.ActionEvent evt) {
                  startItemActionPerformed(evt);
                }
        });
        startItem.setEnabled(false);

        stepItem = commandMenu.add("Step");                
        stepItem.setIcon(new javax.swing.ImageIcon(AMESFrame.class.getResource("/resources/Step.gif")));
        stepItem.addActionListener(new ActionListener() {
               public void actionPerformed(java.awt.event.ActionEvent evt) {
                  stepItemActionPerformed(evt);
                }
        });
        stepItem.setEnabled(false);

        initializeItem = commandMenu.add("Initialize");                
        initializeItem.setIcon(new javax.swing.ImageIcon(AMESFrame.class.getResource("/resources/initialize.gif")));
        initializeItem.addActionListener(new ActionListener() {
               public void actionPerformed(java.awt.event.ActionEvent evt) {
                  initializeItemActionPerformed(evt);
                }
        });
        initializeItem.setEnabled(false);

       stopItem = commandMenu.add("Stop");                
       stopItem.setIcon(new javax.swing.ImageIcon(AMESFrame.class.getResource("/resources/Stop.gif")));
       stopItem.addActionListener(new ActionListener() {
               public void actionPerformed(java.awt.event.ActionEvent evt) {
                 stopItemActionPerformed(evt);
                }
        });
        stopItem.setEnabled(false);

       pauseItem = commandMenu.add("Pause");                
       pauseItem.setIcon(new javax.swing.ImageIcon(AMESFrame.class.getResource("/resources/Pause.gif")));
       pauseItem.addActionListener(new ActionListener() {
               public void actionPerformed(java.awt.event.ActionEvent evt) {
                 pauseItemActionPerformed(evt);
                }
        });
        pauseItem.setEnabled(false);

       setupItem = commandMenu.add("Setup");                
       setupItem.setIcon(new javax.swing.ImageIcon(AMESFrame.class.getResource("/resources/setup.gif")));
       setupItem.addActionListener(new ActionListener() {
               public void actionPerformed(java.awt.event.ActionEvent evt) {
                 setupItemActionPerformed(evt);
                }
        });
        setupItem.setEnabled(false);

       viewSettingsItem = commandMenu.add("View Settings");                
       viewSettingsItem.setIcon(new javax.swing.ImageIcon(AMESFrame.class.getResource("/resources/viewsettings.gif")));
       viewSettingsItem.addActionListener(new ActionListener() {
               public void actionPerformed(java.awt.event.ActionEvent evt) {
                viewSettingsItemActionPerformed(evt);
                }
        });
        viewSettingsItem.setEnabled(false);

        menuBar.add(commandMenu);
   }
 
    public void addCommandToolBar( ){
        commandToolBar.addSeparator();                      // Space at the start

        startButton = new javax.swing.JButton();
        startButton.setIcon(new javax.swing.ImageIcon(AMESFrame.class.getResource("/resources/start.gif")));
        startButton.setToolTipText("Start");
        startButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                startItemActionPerformed(evt);
            }
        });
        startButton.setEnabled(false);
        caseToolBar.add(startButton);

        stepButton = new javax.swing.JButton();
        stepButton.setIcon(new javax.swing.ImageIcon(AMESFrame.class.getResource("/resources/Step.gif")));
        stepButton.setToolTipText("Step");
        stepButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                stepItemActionPerformed(evt);
            }
        });
        stepButton.setEnabled(false);
        caseToolBar.add(stepButton);

        initializeButton = new javax.swing.JButton();
        initializeButton.setIcon(new javax.swing.ImageIcon(AMESFrame.class.getResource("/resources/initialize.gif")));
        initializeButton.setToolTipText("Initialize");
        initializeButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
               initializeItemActionPerformed(evt);
            }
        });
        initializeButton.setEnabled(false);
        caseToolBar.add(initializeButton);

        stopButton = new javax.swing.JButton();
        stopButton.setIcon(new javax.swing.ImageIcon(AMESFrame.class.getResource("/resources/Stop.gif")));
        stopButton.setToolTipText("Stop");
        stopButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                stopItemActionPerformed(evt);
            }
        });
        stopButton.setEnabled(false);
        caseToolBar.add(stopButton);

        pauseButton = new javax.swing.JButton();
        pauseButton.setIcon(new javax.swing.ImageIcon(AMESFrame.class.getResource("/resources/Pause.gif")));
        pauseButton.setToolTipText("Pause");
        pauseButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                pauseItemActionPerformed(evt);
            }
        });
        pauseButton.setEnabled(false);
        caseToolBar.add(pauseButton);

        setupButton = new javax.swing.JButton();
        setupButton.setIcon(new javax.swing.ImageIcon(AMESFrame.class.getResource("/resources/setup.gif")));
        setupButton.setToolTipText("Setup");
        setupButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                setupItemActionPerformed(evt);
            }
        });
        setupButton.setEnabled(false);
        caseToolBar.add(setupButton);

        viewSettingsButton = new javax.swing.JButton();
        viewSettingsButton.setIcon(new javax.swing.ImageIcon(AMESFrame.class.getResource("/resources/viewsettings.gif")));
        viewSettingsButton.setToolTipText("View Settings");
        viewSettingsButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                viewSettingsItemActionPerformed(evt);
            }
        });
        viewSettingsButton.setEnabled(false);
        caseToolBar.add(viewSettingsButton);

        caseToolBar.addSeparator();                      // Space at the end
        caseToolBar.setBorder(BorderFactory.createEtchedBorder(Color.WHITE, Color.LIGHT_GRAY));
    }
    
    public void enableCommandMenuAndToolbar(){
        startButton.setEnabled(true);
        stepButton.setEnabled(true);
        initializeButton.setEnabled(true);
        viewSettingsButton.setEnabled(true);

        startItem.setEnabled(true);
        stepItem.setEnabled(true);
        initializeItem.setEnabled(true);
        viewSettingsItem.setEnabled(true);
    }

    public void disableCommandMenuAndToolbar(){
        startButton.setEnabled(false);
        stepButton.setEnabled(false);
        initializeButton.setEnabled(false);
        stopButton.setEnabled(false);
        pauseButton.setEnabled(false);
        setupButton.setEnabled(false);
        viewSettingsButton.setEnabled(false);

        startItem.setEnabled(false);
        stepItem.setEnabled(false);
        initializeItem.setEnabled(false);
        stopItem.setEnabled(false);
        pauseItem.setEnabled(false);
        setupItem.setEnabled(false);
        viewSettingsItem.setEnabled(false);
    }
    
    public void enableOptionsMenu(){
        option2Item.setEnabled(true);
        learningMethod1Item.setEnabled(true);
    }
    
    public void disableOptionsMenu(){
        option2Item.setEnabled(false);
        learningMethod1Item.setEnabled(false);
    }
    
public class CheckCalculationEndRunnable implements Runnable {

    private AMESMarket amesMarket;
    
    public void setAMESMarket(AMESMarket ames){
        amesMarket=ames;
    }
    
    public void run() {
        while(true){
            if(amesMarket.IfCalculationEnd()){
                if(BatchMode==0){
                    startButton.setEnabled(false);
                    stepButton.setEnabled(false);
                    initializeButton.setEnabled(false);
                    stopButton.setEnabled(false);
                    pauseButton.setEnabled(false);
                    setupButton.setEnabled(true);

                    startItem.setEnabled(false);
                    stepItem.setEnabled(false);
                    initializeItem.setEnabled(false);
                    stopItem.setEnabled(false);
                    pauseItem.setEnabled(false);
                    setupItem.setEnabled(true);

                    bCasdeResult=true;
                    saveOutputData();
                    
                    enableViewMenu();
                }
                else if(BatchMode==1){
                    saveOutputData( );
                    System.gc();
                    
                    if(bMultiRandomSeeds&&(iCurrentRandomSeedsIndex<iRandomSeedsData-1)){
                        iCurrentRandomSeedsIndex++;
                        
                        batchModeStart();
                    }
                    else if(bMultiCases&&(iCurrentMultiCasesIndex<iMultiCasesData-1)){
                        iCurrentMultiCasesIndex++;
                        
                        if(bMultiRandomSeeds)
                            iCurrentRandomSeedsIndex=0;

                        batchModeStart();
                    }
                    else{
                        startButton.setEnabled(false);
                        stepButton.setEnabled(false);
                        initializeButton.setEnabled(false);
                        stopButton.setEnabled(false);
                        pauseButton.setEnabled(false);
                        setupButton.setEnabled(true);

                        startItem.setEnabled(false);
                        stepItem.setEnabled(false);
                        initializeItem.setEnabled(false);
                        stopItem.setEnabled(false);
                        pauseItem.setEnabled(false);
                        setupItem.setEnabled(true);

                        bCasdeResult=true;
                        enableViewMenu();
                        
                        BatchMode=0;
                    }
                        
                }
                
                break;
            }
            else{
                try{
                    Thread.sleep(1000);
                }
                catch(InterruptedException e){
                    System.out.println("CheckCalculationEndRunnable is interrupted!");
                }
            }
        }
    }
}

    public String checkCaseData( ){
     String strMessage="";
     
     strMessage=config1.DataVerify();
     if(!strMessage.isEmpty()){
         activeConfig1();
         return strMessage;
     }

     strMessage=config2.DataVerify();
     if(!strMessage.isEmpty()){
         activeConfig2();
         return strMessage;
     }
     
     strMessage=config4.DataVerify();
     if(!strMessage.isEmpty()){
         activeConfig4();
         return strMessage;
     }
     
     strMessage=config5.DataVerify();
     if(!strMessage.isEmpty()){
          activeConfig5();
          return strMessage;
     }
     
     strMessage=learnOption1.DataVerify();
     if(!strMessage.isEmpty()){
         activeLearnOption1();
         return strMessage;
     }
     
     strMessage=simulationControl.DataVerify();
     if(!strMessage.isEmpty()){
         activeSimulationControl();
         return strMessage;
     }
     
     strMessage=config5.PriceCapVerify(dLSEPriceCap);
     if(!strMessage.isEmpty()){
          activeConfig5();
          return strMessage;
     }
     
     config1.saveData();
     config5.saveData();
     branchData=config2.saveData();
     genData=config4.saveData();
    
     double dMinGenCapacity=0.0;
     double dMaxGenCapacity=0.0;
     double dMaxGenCapacityWithPCap=0.0;
     for(int i=0; i<genData.length; i++){
         double CapU=Double.parseDouble(genData[i][6].toString());
         double CapL=Double.parseDouble(genData[i][5].toString());
         dMinGenCapacity+=CapL;
         dMaxGenCapacity+=RI_MIN_C*(CapU-CapL)+CapL;
         
         double a=Double.parseDouble(genData[i][3].toString());
         double b=Double.parseDouble(genData[i][4].toString());
         if(a+2*b*(RI_MIN_C*(CapU-CapL)+CapL)<dGenPriceCap)
             dMaxGenCapacityWithPCap+=RI_MIN_C*(CapU-CapL)+CapL;
         else
             dMaxGenCapacityWithPCap+=((dGenPriceCap-a)/(2*b)-CapL)/RI_MIN_C+CapL;
     }
     
     double [] dLoad=new double[24];
     
     if(lseHybridDemand!=null){
         for(int i=0; i<24; i++){
             dLoad[i]=0.0;

             for(int k=0; k<lseHybridDemand.length; k++){
                 int Flag=Integer.parseInt(lseHybridDemand[k][i+3].toString());

                 if((Flag&1)==1){
                     dLoad[i]+=Double.parseDouble(lseData[k][i+3].toString());
                 }
             }
         }
     }     
     
     if(config1.iTotalBranchNumber!=branchData.length)
         strMessage+="The total branch number in step1 is not equal to the number in step2\n";
     
     if(config1.iTotalGenNumber!=genData.length)
         strMessage+="The total generator number in step1 is not equal to the number in step4\n";
     
     if(config1.iTotalLSENumber!=lseData.length)
         strMessage+="The total branch number in step1 is not equal to the number in step5\n";
     
     int iBranch=branchData.length;
     int [] iNode=new int[2*iBranch];
     for(int i=0; i<iBranch*2; i++){
         iNode[i]=-1;
     }
     
     int iNodeCount=0;
     for(int i=0; i<iBranch; i++){
         boolean bFound=false;
         if(iNodeCount>0) {
             for(int j=0; j<iNodeCount; j++){
                 if(iNode[j]==Integer.parseInt(branchData[i][1].toString())){
                     bFound=true;
                     continue;
                 }
             }
         }
         
         if(!bFound){
             iNode[iNodeCount]=Integer.parseInt(branchData[i][1].toString());
             iNodeCount++;
         }
         
         bFound=false;
         if(iNodeCount>0) {
             for(int j=0; j<iNodeCount; j++){
                 if(iNode[j]==Integer.parseInt(branchData[i][2].toString())){
                     bFound=true;
                     continue;
                 }
             }
         }
         
         if(!bFound){
             iNode[iNodeCount]=Integer.parseInt(branchData[i][2].toString());
             iNodeCount++;
         }
     }
     
     if(iNodeCount!=config1.iTotalNodeNumber)
         strMessage+="The total bus number in step1 is not equal to the number in step2\n";
         
     for(int i=0; i<24; i++){
        if(dMinGenCapacity>dLoad[i]){
            strMessage+="GenCos min capacity sum greater than fixed load sum at "+i+" hour is not allowed!\n";
        }
        
        if(dMaxGenCapacity<dLoad[i]){
            strMessage+="GenCos max capacity sum less than fixed load sum at "+i+" hour is not allowed!\n";
        }
        
        if(dMaxGenCapacityWithPCap<dLoad[i]){
            strMessage+="GenCos max capacity sum with PriceCap less than fixed load sum at "+i+" hour is not allowed!\n";
        }
     }
     
     return strMessage;
    }
    
public void InitializeAMESMarket( ) {
    amesMarket = new AMESMarket();
    double [][] bus=new double[1][2];
    bus[0][0]=Double.parseDouble(nodeData[0][0].toString());
    bus[0][1]=Double.parseDouble(nodeData[0][1].toString());
    
    int iBranchRow=branchData.length;
    int iBranchCol=branchData[0].length-1;
    double [][] branch=new double[iBranchRow][iBranchCol];
    for(int i=0; i<iBranchRow; i++) {
        for(int j=0; j<iBranchCol; j++) {
            branch[i][j]=Double.parseDouble(branchData[i][j+1].toString());
        }
    }
    
    int iGenRow=genData.length;
    int iGenCol=genData[0].length-1;
    double [][] gen=new double[iGenRow][iGenCol];
    for(int i=0; i<iGenRow; i++) {
        for(int j=0; j<iGenCol; j++) {
            gen[i][j]=Double.parseDouble(genData[i][j+1].toString());
        }
    }
    
    int iLseRow=lseData.length;
    int iLseCol=lseData[0].length-1;
    double [][] lse=new double[iLseRow][iLseCol];
    for(int i=0; i<iLseRow; i++) {
        for(int j=0; j<iLseCol; j++) {
            lse[i][j]=Double.parseDouble(lseData[i][j+1].toString());
        }
    }
    
    int iLsePriceRow=lsePriceSensitiveDemand.length;
    int iLsePriceCol=lsePriceSensitiveDemand[0][0].length-1;
    double [][][] lsePrice=new double[iLsePriceRow][24][iLsePriceCol];
    for(int i=0; i<iLsePriceRow; i++) {
        for(int h=0; h<24; h++)
            for(int j=0; j<iLsePriceCol; j++) {
                lsePrice[i][h][j]=Double.parseDouble(lsePriceSensitiveDemand[i][h][j+1].toString());
        }
    }
    
    int iLseHybridRow=lseHybridDemand.length;
    int iLseHybridCol=lseHybridDemand[0].length-1;
    int [][] lseHybrid=new int[iLseHybridRow][iLseHybridCol];
    for(int i=0; i<iLseHybridRow; i++) {
        for(int j=0; j<iLseHybridCol; j++) {
            lseHybrid[i][j]=Integer.parseInt(lseHybridDemand[i][j+1].toString());
        }
    }
            
    amesMarket.InitLearningParameters(genLearningData);
    
    if(BatchMode==1){
        if(bMultiRandomSeeds)
            RandomSeed=randomSeedsData[iCurrentRandomSeedsIndex];
    }
    
    amesMarket.InitSimulationParameters(iMaxDay, bMaximumDay, dThresholdProbability, bThreshold, dDailyNetEarningThreshold, bDailyNetEarningThreshold, iDailyNetEarningStartDay, iDailyNetEarningDayLength, iStartDay, iCheckDayLength, dActionProbability, bActionProbabilityCheck, iLearningCheckStartDay, iLearningCheckDayLength, dLearningCheckDifference, bLearningCheck, dGenPriceCap, dLSEPriceCap, RandomSeed);
    amesMarket.AMESMarketSetupFromGUI(baseS, baseV, bus, branch, gen, lse, lsePrice, lseHybrid);
}

private void startItemActionPerformed(java.awt.event.ActionEvent evt) {
    amesMarket.Start();
     
    startButton.setEnabled(false);
    stepButton.setEnabled(false);
    initializeButton.setEnabled(false);
    stopButton.setEnabled(true);
    pauseButton.setEnabled(true);
    setupButton.setEnabled(false);

    startItem.setEnabled(false);
    stepItem.setEnabled(false);
    initializeItem.setEnabled(false);
    stopItem.setEnabled(true);
    pauseItem.setEnabled(true);
    setupItem.setEnabled(false);
    
    CheckCalculationEndRunnable checkRunable=new CheckCalculationEndRunnable();
    checkRunable.setAMESMarket(amesMarket);
    (new Thread(checkRunable)).start();
}

private void batchModeStart(){
    if(bMultiCases){
        if((iCurrentMultiCasesIndex!=0)&&((!bMultiRandomSeeds)||(bMultiRandomSeeds&&(iCurrentRandomSeedsIndex==0)))){
                   String currentCase=MultiCasesData[iCurrentMultiCasesIndex];
                   caseFile=new File(batchFile.getParent()+"\\"+currentCase);
                    bOpen = true;

                    loadCaseFileData( );
                    System.out.println("Load user selected case data file:"+caseFile.getName());

                    config1.SetInitParameters(iNodeData, iBranchData, 0, iGenData, iLSEData, baseV, baseS);
                    config2.loadData(branchData);
                    config4.loadData(genData);
                    config5.loadData(lseData, lsePriceSensitiveDemand, lseHybridDemand);
                    
                    learnOption1.loadData(genData, genLearningData);
                    simulationControl.SetInitParameters(iMaxDay, bMaximumDay, dThresholdProbability, bThreshold, dDailyNetEarningThreshold, bDailyNetEarningThreshold, iDailyNetEarningStartDay, iDailyNetEarningDayLength, iStartDay, iCheckDayLength, dActionProbability, bActionProbabilityCheck, //
                        iLearningCheckStartDay, iLearningCheckDayLength, dLearningCheckDifference, bLearningCheck, dGenPriceCap, dLSEPriceCap, RandomSeed);
                    // Verify Data
                    String strError=checkCaseData();
                    if(!strError.isEmpty()){
                            JOptionPane.showMessageDialog(this, strError, "Case Data Verify Message", JOptionPane.ERROR_MESSAGE); 
                            return;
                     }

                    setbLoadCase(true);
                    setbCaseResult(false);
                    enableCaseMenuAndToolBar();
                    enableCommandMenuAndToolbar();
                    enableOptionsMenu();
                    disableViewMenu();
        }
    }
    
    InitializeAMESMarket( );
    amesMarket.Start();
    
    CheckCalculationEndRunnable checkRunable=new CheckCalculationEndRunnable();
    checkRunable.setAMESMarket(amesMarket);
    (new Thread(checkRunable)).start();
   
}

private void stepItemActionPerformed(java.awt.event.ActionEvent evt) {
    amesMarket.Step();
    
    startButton.setEnabled(true);
    stepButton.setEnabled(true);
    initializeButton.setEnabled(false);
    stopButton.setEnabled(true);
    pauseButton.setEnabled(false);
    setupButton.setEnabled(false);

    startItem.setEnabled(true);
    stepItem.setEnabled(true);
    initializeItem.setEnabled(false);
    stopItem.setEnabled(true);
    pauseItem.setEnabled(false);
    setupItem.setEnabled(false);
}

private void initializeItemActionPerformed(java.awt.event.ActionEvent evt) {
    amesMarket.Initialize();
    initializeButton.setEnabled(false);
    initializeItem.setEnabled(false);
}

private void stopItemActionPerformed(java.awt.event.ActionEvent evt) {
    amesMarket.Stop();
    
    startButton.setEnabled(false);
    stepButton.setEnabled(false);
    initializeButton.setEnabled(false);
    stopButton.setEnabled(false);
    pauseButton.setEnabled(false);
    setupButton.setEnabled(true);

    startItem.setEnabled(false);
    stepItem.setEnabled(false);
    initializeItem.setEnabled(false);
    stopItem.setEnabled(false);
    pauseItem.setEnabled(false);
    setupItem.setEnabled(true);
    
    enableViewMenu();
}

private void pauseItemActionPerformed(java.awt.event.ActionEvent evt) {
    amesMarket.Pause();
    
    startButton.setEnabled(true);
    stepButton.setEnabled(true);
    initializeButton.setEnabled(false);
    stopButton.setEnabled(true);
    pauseButton.setEnabled(false);
    setupButton.setEnabled(false);

    startItem.setEnabled(true);
    stepItem.setEnabled(true);
    initializeItem.setEnabled(false);
    stopItem.setEnabled(true);
    pauseItem.setEnabled(false);
    setupItem.setEnabled(false);
    
    enableViewMenu();
}

private void setupItemActionPerformed(java.awt.event.ActionEvent evt) {
    InitializeAMESMarket( );
    
    startButton.setEnabled(true);
    stepButton.setEnabled(true);
    initializeButton.setEnabled(true);
    stopButton.setEnabled(false);
    pauseButton.setEnabled(false);
    setupButton.setEnabled(true);

    startItem.setEnabled(true);
    stepItem.setEnabled(true);
    initializeItem.setEnabled(true);
    stopItem.setEnabled(false);
    pauseItem.setEnabled(false);
    setupItem.setEnabled(true);
}

private void viewSettingsItemActionPerformed(java.awt.event.ActionEvent evt) {
    amesMarket.ViewSettings();
}

public void addOptionsMenu( ){
    optionsMenu.setMnemonic('O');                    // Create shortcut

    // Construct the file drop-down menu
    learningMethod1Item = new JRadioButtonMenuItem("VRE-RL", true);
    learningMethod1Item.addActionListener(new ActionListener() {
       public void actionPerformed(java.awt.event.ActionEvent evt) {
          learningMethod1ItemActionPerformed(evt);
        }
    });

    learningMethod1Item.setEnabled(false);
    learningMethod2Item = new JRadioButtonMenuItem("Learning Method 2", false);
    learningMethod2Item.setEnabled(false);
    learningMethod3Item = new JRadioButtonMenuItem("Learning Method 3", false);
    learningMethod3Item.setEnabled(false);
    learningMethod4Item = new JRadioButtonMenuItem("Learning Method 4", false);
    learningMethod4Item.setEnabled(false);
    learningMethod5Item = new JRadioButtonMenuItem("Learning Method 5", false);
    learningMethod5Item.setEnabled(false);
    optionsMenu.add(learningMethod1Item);  
    optionsMenu.add(learningMethod2Item);  
    optionsMenu.add(learningMethod3Item);  
    optionsMenu.add(learningMethod4Item);  
    optionsMenu.add(learningMethod5Item);  
    
    ButtonGroup learningMethodTypes = new ButtonGroup();
    learningMethodTypes.add(learningMethod1Item);
    learningMethodTypes.add(learningMethod2Item);
    learningMethodTypes.add(learningMethod3Item);
    learningMethodTypes.add(learningMethod4Item);
    learningMethodTypes.add(learningMethod5Item);

    optionsMenu.addSeparator();                      

    option2Item = optionsMenu.add("Simulation Control");                
    option2Item.addActionListener(new ActionListener() {
       public void actionPerformed(java.awt.event.ActionEvent evt) {
          SimulationControlItemActionPerformed(evt);
        }
    });

    option2Item.setEnabled(false);
    menuBar.add(optionsMenu);
   }

   private void learningMethod1ItemActionPerformed(java.awt.event.ActionEvent evt) {
       LearnOption1 Option=new LearnOption1(this, false);
       Option.loadData(genData, genLearningData);
       
       Toolkit theKit = Option.getToolkit(); 
       Dimension wndSize = theKit.getScreenSize(); 
       
       Rectangle configBounds=Option.getBounds();
       
       Option.setLocation( (wndSize.width-configBounds.width)/2, (wndSize.height-configBounds.height)/2);
       Option.setVisible(true);
       
    }

public void SetLearningParameters(double [][]learningData){
    
        genLearningData=learningData;
    }
    
public  void SetDefaultSimulationParameters(){
    SetSimulationParameters(Default_iMaxDay, 
            Default_bMaximumDay, 
            Default_dThresholdProbability, 
            Default_bThreshold, 
            Default_dDailyNetEarningThreshold, 
            Default_bDailyNetEarningThreshold, 
            Default_iDailyNetEarningStartDay, 
            Default_iDailyNetEarningDayLength,
            Default_iStartDay, 
            Default_iCheckDayLength, 
            Default_dActionProbability, 
            Default_bActionProbabilityCheck, 
            Default_iLearningCheckStartDay, 
            Default_iLearningCheckDayLength, 
            Default_dLearningCheckDifference, 
            Default_bLearningCheck, 
            Default_dGenPriceCap, 
            Default_dLSEPriceCap, 
            Default_RandomSeed);
        
    }

public void SetSimulationParameters(int iMax, boolean bMax, double dThreshold, boolean bThres, double dEarningThreshold, boolean bEarningThresh, int iEarningStart, int iEarningLength, int iStart, int iLength, double dCheck, boolean bCheck, int iLearnStart, int iLearnLength, double dLearnCheck, boolean bLearnCheck, double dGCap ,double dLCap, long lRandom){
        iMaxDay=iMax;
        bMaximumDay=bMax;
        dThresholdProbability=dThreshold;
        bThreshold=bThres;
        dDailyNetEarningThreshold=dEarningThreshold;
        bDailyNetEarningThreshold=bEarningThresh;
        iDailyNetEarningStartDay=iEarningStart;
        iDailyNetEarningDayLength=iEarningLength;
        iStartDay=iStart;
        iCheckDayLength=iLength;
        dActionProbability=dCheck;
        bActionProbabilityCheck=bCheck;
        iLearningCheckStartDay=iLearnStart;
        iLearningCheckDayLength=iLearnLength;
        dLearningCheckDifference=dLearnCheck;
        bLearningCheck=bLearnCheck;
        dGenPriceCap=dGCap;
        dLSEPriceCap=dLCap;
        RandomSeed=lRandom;
}

private void SimulationControlItemActionPerformed(java.awt.event.ActionEvent evt) {
       SimulationControl control=new SimulationControl(this, false);
        
       if(BatchMode==1){
            if(bMultiRandomSeeds)
                RandomSeed=randomSeedsData[iCurrentRandomSeedsIndex];
        }
       
       control.SetInitParameters(iMaxDay, bMaximumDay, dThresholdProbability, bThreshold, dDailyNetEarningThreshold, bDailyNetEarningThreshold, iDailyNetEarningStartDay, iDailyNetEarningDayLength, iStartDay, iCheckDayLength, dActionProbability, bActionProbabilityCheck, //
                iLearningCheckStartDay, iLearningCheckDayLength, dLearningCheckDifference, bLearningCheck, dGenPriceCap, dLSEPriceCap, RandomSeed);
       
       Toolkit theKit = control.getToolkit(); 
       Dimension wndSize = theKit.getScreenSize();    
       
       Rectangle configBounds=control.getBounds();
       
       control.setLocation( (wndSize.width-configBounds.width)/2, (wndSize.height-configBounds.height)/2);
       control.setVisible(true);
       
}

public void SetRandomSeed(long iSeed){
    RandomSeed=iSeed;
}

public long GetRandomSeed(){
    return RandomSeed;
}

   public void addHelpMenu( ){
    helpMenu.setMnemonic('H');                    // Create shortcut

    // Construct the file drop-down menu
    aboutItem = helpMenu.add("About");                
    aboutItem.addActionListener(new ActionListener() {
           public void actionPerformed(java.awt.event.ActionEvent evt) {
             aboutItemActionPerformed(evt);
            }
    });
     
    // Add Help menu accelerators
    aboutItem.setAccelerator(KeyStroke.getKeyStroke('A', CTRL_DOWN_MASK));
 
    menuBar.add(helpMenu);
   }

private void aboutItemActionPerformed(java.awt.event.ActionEvent evt) {
 
    About about=new About();
    
    Toolkit theKit = about.getToolkit(); 
    Dimension wndSize = theKit.getScreenSize();       

    Rectangle configBounds=about.getBounds();

    about.setLocation( (wndSize.width-configBounds.width)/2, (wndSize.height-configBounds.height)/2);
    about.setVisible(true);
}
 
    public static void main(String[] args) {
    mainFrameWindow = new AMESFrame( );        
    
    Toolkit theKit = mainFrameWindow.getToolkit();       
    Dimension wndSize = theKit.getScreenSize(); 

    // Set the position to screen center & size to half screen size
    mainFrameWindow.setBounds(wndSize.width/6, wndSize.height/6,       
                      wndSize.width*2/3, wndSize.height*2/3);     
    
    mainFrameWindow.setVisible(true);
    }

     public void addBranchNumber( ) {
         iBranchData ++;
     }
   
     public void deleteBranchNumber( ) {
         iBranchData --;
     }
      
     public void setdBranchNumber(int iNumber ) {
         iBranchData = iNumber;
     }
  
     public void setVBase(double V ) {
         baseV = V;
     }
  
     public void setPBase(double P ) {
         baseS = P;
     }
  
    public void addGenNumber( ) {
         iGenData ++;
     }
   
     public void deleteGenNumber( ) {
         iGenData --;
     }
      public void setdGenNumber(int iNumber ) {
         iGenData = iNumber;
     }
 
    public void addLSENumber( ) {
         iLSEData ++;
     }
   
     public void deleteLSENumber( ) {
         iLSEData --;
     }
   
      public void setLSENumber(int iNumber ) {
         iLSEData = iNumber;
     }
   
      public boolean isNewCase () {
          return !bOpen;
      }
      
      public void setbLoadCase(boolean bNew){
          bLoadCase=bNew;
      }
      
      public void setbCaseResult(boolean bNew){
          bCasdeResult=bNew;
      }
      
      public PowerGridConfigure1 getConfig1( ) {
          return config1;
      }
      
      public PowerGridConfigure2 getConfig2( ) {
          return config2;
      }
      
      public PowerGridConfigure4 getConfig4( ) {
          return config4;
      }
      
      public PowerGridConfigure5 getConfig5( ) {
          return config5;
      }
      
      public Object [][] getBranchData( ) {
          return branchData;
      }
      
      public Object [][] getGeneratorData( ) {
          return genData;
      }
      
      public Object [][] getLSEData( ) {
          return lseData;
      }
      
      public Object [][][] getLSEPriceSensitiveDemandData( ) {
          return lsePriceSensitiveDemand;
      }
      
      public Object [][] getLSEHybridDemandData( ) {
          return lseHybridDemand;
      }
      
      public int [] getNodeData() {
          int [] bus=new int[iNodeData];
          for(int i=0; i<iNodeData; i++)
              bus[i]=i+1;
          
          return bus;
      }
      
      public String [] getNodeNameData() {
          String [] bus=new String[iNodeData];
          for(int i=0; i<iNodeData; i++)
              bus[i]="Bus "+(i+1);
          
          return bus;
      }
      
      public AMESMarket getAMESMarket( ) {
          return amesMarket;
      }

      public int getMaxDay(){
          return iMaxDay;
      }
      
      public double getThresholdProbability(){
          return dThresholdProbability;
      }
      
      public double getPriceCap(){
          return dGenPriceCap;
      }
      
      public void setStopCode(int code){
          stopCode=code;
      }
      
  private static AMESFrame mainFrameWindow;             // The application window
 
  private JMenuBar menuBar=new JMenuBar();     // Window menu bar
  
  private JMenu caseMenu=new JMenu("Case");           // Create Case menu
  // Case menu items
  private JMenuItem newCaseItem, selectCaseItem, caseParametersItem, saveCaseItem, saveCaseAsItem, exitItem;
  private JMenu loadDefaultCaseMenu=new JMenu("Load Test Case");
  private JMenuItem default5BusCaseItem, default30BusCaseItem;
  private JMenu batchModeMenu=new JMenu("Batch Mode");
  private JMenuItem batchMode1Item, batchMode2Item, loadBatchMode1Item;
  
  private JMenu viewMenu=new JMenu("View");           // Create View menu
  // View menu items
  private JMenuItem caseReportItem, caseCurveItem;
 
  private JMenu commandMenu=new JMenu("Command");           // Create Command menu
  // Command menu items
  private JMenuItem startItem, stepItem, initializeItem, stopItem, 
                                pauseItem, setupItem, viewSettingsItem;

  private JMenu optionsMenu=new JMenu("Options");           // Create Options menu
  // Options menu items
  private JMenuItem option1Item, option2Item, option3Item;
  private JRadioButtonMenuItem learningMethod1Item, learningMethod2Item, learningMethod3Item, 
                                learningMethod4Item, learningMethod5Item;
  private ButtonGroup learningMethodTypes;

  private JMenu helpMenu=new JMenu("Help");           // Create Help menu
  // Help menu items
  private JMenuItem  aboutItem;
  
  private javax.swing.JToolBar caseToolBar=new javax.swing.JToolBar("CaseToolBar");
  private javax.swing.JButton newCaseButton, selectCaseButton, caseParametersButton, saveCaseButton,  saveCaseAsButton, exitButton;

  private javax.swing.JToolBar commandToolBar=new javax.swing.JToolBar("CommandToolBar");
  private javax.swing.JButton startButton, stepButton, initializeButton, stopButton, 
                                pauseButton, setupButton, viewSettingsButton;

  private JFileChooser selectCasetDialog=new javax.swing.JFileChooser( );
  
  private  PowerGridConfigure1 config1;
  private  PowerGridConfigure2 config2;
  private  PowerGridConfigure4 config4;
  private  PowerGridConfigure5 config5;
  
  public   LearnOption1 learnOption1;
  public   SimulationControl simulationControl;
  
  private File outputFile;
  private File batchFile;
  
  private File caseFile; 
  private File caseFileDirectory;
  //Judge if a new case or saved case
  private boolean bOpen=false;
  private boolean bLoadCase=false;
  private boolean bCasdeResult=false;
  
  private Object [][] nodeData;
  private Object [][] branchData;
  public  Object [][] genData;
  private Object[][] lse3SecData;  // 3-sectional 8-hour LSE data
  public  Object[][] lseData;      // Combine 3-sectional to 24-hour LSE data
  private Object[][] lseSec1Data;  // First 8-hour LSE data
  private Object[][] lseSec2Data;  // Second 8-hour LSE data
  private Object[][] lseSec3Data;  // Third 8-hour LSE data
  public  Object[][][] lsePriceSensitiveDemand;
  public  Object[][] lseHybridDemand;
    
  private int iNodeData;
  private int iBranchData;
  private int iGenData;
  private int iLSEData;
  private boolean bPriceSensitiveDemand;
  private boolean bHybridDemand;

  private boolean isPU;
  private static double baseS;
  private static double baseV;
  
  // Learning and action domain parameters
  private double Default_Cooling;
  private double Default_Experimentation;
  private double Default_InitPropensity;
  private int Default_M1;
  private int Default_M2;
  private int Default_M3;
  private double Default_RI_MAX_Lower;
  private double Default_RI_MAX_Upper;
  private double Default_RI_MIN_C;
  private double Default_Recency;
  private double Default_SlopeStart;
  private int Default_iRewardSelection;

  private double Cooling;
  private double Experimentation;
  private double InitPropensity;
  private int M1;
  private int M2;
  private int M3;
  private double RI_MAX_Lower;
  private double RI_MAX_Upper;
  private double RI_MIN_C;
  private double Recency;
  private double SlopeStart;
  private int iRewardSelection;
  
  private double[][] genLearningData;
  private boolean bGenLearningDataSet=false;

  private long Default_RandomSeed;
  private int Default_iMaxDay;
  private double Default_dThresholdProbability;
  private double Default_dDailyNetEarningThreshold;
  private double Default_dGenPriceCap;
  private double Default_dLSEPriceCap;
  private int Default_iStartDay;
  private int Default_iCheckDayLength;
  private double Default_dActionProbability;
  private int Default_iLearningCheckStartDay;
  private int Default_iLearningCheckDayLength;
  private double Default_dLearningCheckDifference;
  private int Default_iDailyNetEarningStartDay;
  private int Default_iDailyNetEarningDayLength;
  private boolean Default_bMaximumDay=true;
  private boolean Default_bThreshold=true;
  private boolean Default_bDailyNetEarningThreshold=false;
  private boolean Default_bActionProbabilityCheck=false;
  private boolean Default_bLearningCheck=false;
  
  private long RandomSeed;
  private int iMaxDay;
  private double dThresholdProbability;
  public double dDailyNetEarningThreshold;
  private double dGenPriceCap;
  private double dLSEPriceCap;
  public int iStartDay;
  public int iCheckDayLength;
  public double dActionProbability;
  public int iLearningCheckStartDay;
  public int iLearningCheckDayLength;
  public double dLearningCheckDifference;
  public int iDailyNetEarningStartDay;
  public int iDailyNetEarningDayLength;
  public  boolean bMaximumDay=true;
  public  boolean bThreshold=true;
  public  boolean bDailyNetEarningThreshold=false;
  public  boolean bActionProbabilityCheck=false;
  public  boolean bLearningCheck=false;

  private OutputPane output;
  
  private int BatchMode=0;  // For normal mode, 1-> multiple random seeds for one case input file
  private int iRandomSeedsData=0;
  private int iCurrentRandomSeedsIndex=0;
  private long [] randomSeedsData;
  private boolean bMultiRandomSeeds=false;
  
  private int iMultiCasesData=0;
  private int iCurrentMultiCasesIndex=0;
  private String [] MultiCasesData;
  private boolean bMultiCases=false;
  
  private int stopCode;

  private AMESMarket amesMarket;

}
