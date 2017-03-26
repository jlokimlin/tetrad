///////////////////////////////////////////////////////////////////////////////
// For information as to what this class does, see the Javadoc, below.       //
// Copyright (C) 1998, 1999, 2000, 2001, 2002, 2003, 2004, 2005, 2006,       //
// 2007, 2008, 2009, 2010, 2014, 2015 by Peter Spirtes, Richard Scheines, Joseph   //
// Ramsey, and Clark Glymour.                                                //
//                                                                           //
// This program is free software; you can redistribute it and/or modify      //
// it under the terms of the GNU General Public License as published by      //
// the Free Software Foundation; either version 2 of the License, or         //
// (at your option) any later version.                                       //
//                                                                           //
// This program is distributed in the hope that it will be useful,           //
// but WITHOUT ANY WARRANTY; without even the implied warranty of            //
// MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the             //
// GNU General Public License for more details.                              //
//                                                                           //
// You should have received a copy of the GNU General Public License         //
// along with this program; if not, write to the Free Software               //
// Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307  USA //
///////////////////////////////////////////////////////////////////////////////

package edu.cmu.tetrad.test;

import edu.cmu.tetrad.algcomparison.Comparison;
import edu.cmu.tetrad.algcomparison.algorithm.Algorithms;
import edu.cmu.tetrad.algcomparison.algorithm.multi.*;
import edu.cmu.tetrad.algcomparison.algorithm.multi.Fang;
import edu.cmu.tetrad.algcomparison.algorithm.oracle.pag.CcdMax;
import edu.cmu.tetrad.algcomparison.graph.Cyclic;
import edu.cmu.tetrad.algcomparison.independence.FisherZ;
import edu.cmu.tetrad.algcomparison.independence.SemBicTest;
import edu.cmu.tetrad.algcomparison.simulation.LinearFisherModel;
import edu.cmu.tetrad.algcomparison.simulation.LoadContinuousDataAndSingleGraph;
import edu.cmu.tetrad.algcomparison.simulation.Simulations;
import edu.cmu.tetrad.algcomparison.statistic.*;
import edu.cmu.tetrad.data.CovarianceMatrixOnTheFly;
import edu.cmu.tetrad.data.DataReader;
import edu.cmu.tetrad.data.DataSet;
import edu.cmu.tetrad.data.DelimiterType;
import edu.cmu.tetrad.graph.Edge;
import edu.cmu.tetrad.graph.Graph;
import edu.cmu.tetrad.graph.GraphUtils;
import edu.cmu.tetrad.graph.Node;
import edu.cmu.tetrad.search.Fas;
import edu.cmu.tetrad.search.IndTestScore;
import edu.cmu.tetrad.search.SemBicScore;
import edu.cmu.tetrad.util.Parameters;
import org.apache.commons.math3.distribution.BetaDistribution;
import org.junit.Test;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintStream;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.*;

/**
 * An example script to simulate data and run a comparison analysis on it.
 *
 * @author jdramsey
 */
public class TestFang {

    public void test0() {
        Parameters parameters = new Parameters();

        parameters.set("numRuns", 5);
        parameters.set("sampleSize", 1000);
        parameters.set("avgDegree", 2);
        parameters.set("numMeasures", 50);
        parameters.set("maxDegree", 1000);
        parameters.set("maxIndegree", 1000);
        parameters.set("maxOutdegree", 1000);

        parameters.set("coefLow", .2);
        parameters.set("coefHigh", .6);
        parameters.set("varLow", .2);
        parameters.set("varHigh", .4);
        parameters.set("coefSymmetric", true);
        parameters.set("probCycle", 1.0);
        parameters.set("probTwoCycle", .2);
        parameters.set("intervalBetweenShocks", 1);
        parameters.set("intervalBetweenRecordings", 1);

        parameters.set("alpha", 0.001);
        parameters.set("depth", 4);
        parameters.set("orientVisibleFeedbackLoops", true);
        parameters.set("doColliderOrientation", true);
        parameters.set("useMaxPOrientationHeuristic", true);
        parameters.set("maxPOrientationMaxPathLength", 3);
        parameters.set("applyR1", true);
        parameters.set("orientTowardDConnections", true);
        parameters.set("gaussianErrors", false);
        parameters.set("assumeIID", false);
        parameters.set("collapseTiers", true);

        Statistics statistics = new Statistics();

        statistics.add(new ParameterColumn("avgDegree"));
        statistics.add(new ParameterColumn("numMeasures"));
        statistics.add(new AdjacencyPrecision());
        statistics.add(new AdjacencyRecall());
        statistics.add(new ArrowheadPrecision());
        statistics.add(new ArrowheadRecall());
        statistics.add(new TwoCycleFalsePositive());
        statistics.add(new TwoCycleRecall());
        statistics.add(new ElapsedTime());

        Simulations simulations = new Simulations();
        simulations.add(new LinearFisherModel(new Cyclic()));

        Algorithms algorithms = new Algorithms();
        algorithms.add(new CcdMax(new FisherZ()));

        Comparison comparison = new Comparison();

        comparison.setShowAlgorithmIndices(false);
        comparison.setShowSimulationIndices(false);
        comparison.setSortByUtility(false);
        comparison.setShowUtilities(false);
        comparison.setParallelized(false);
        comparison.setSaveGraphs(true);

        comparison.setTabDelimitedTables(false);

        comparison.compareFromSimulations("comparison", simulations, algorithms, statistics, parameters);
//        comparison.compareFromFiles("comparison", algorithms, statistics, parameters);
//        comparison.saveToFiles("comparison", new LinearFisherModel(new RandomForward()), parameters);

    }


    public void TestRuben() {
        Parameters parameters = new Parameters();

        parameters.set("penaltyDiscount", 3);
        parameters.set("depth", -1);

        parameters.set("numRuns", 10);
        parameters.set("randomSelectionSize", 10);
        parameters.set("Structure", "Placeholder");

        Statistics statistics = new Statistics();

        statistics.add(new ParameterColumn("Structure"));
        statistics.add(new AdjacencyPrecision());
        statistics.add(new AdjacencyRecall());
        statistics.add(new ArrowheadPrecision());
        statistics.add(new ArrowheadRecall());
        statistics.add(new TwoCyclePrecision());
        statistics.add(new TwoCycleRecall());
        statistics.add(new TwoCycleFalsePositive2());
        statistics.add(new TwoCycleFalseNegative2());
        statistics.add(new TwoCycleTruePositive());
        statistics.add(new ElapsedTime());

        Simulations simulations = new Simulations();

        simulations.add(new LoadContinuousDataAndSingleGraph(
                "/Users/jdramsey/Downloads/Cycles_Data_fMRI-selected/Structure1_amp"));
        simulations.add(new LoadContinuousDataAndSingleGraph(
                "/Users/jdramsey/Downloads/Cycles_Data_fMRI-selected/Structure1_contr"));
        simulations.add(new LoadContinuousDataAndSingleGraph(
                "/Users/jdramsey/Downloads/Cycles_Data_fMRI-selected/Structure2_amp"));
        simulations.add(new LoadContinuousDataAndSingleGraph(
                "/Users/jdramsey/Downloads/Cycles_Data_fMRI-selected/Structure2_contr"));
        simulations.add(new LoadContinuousDataAndSingleGraph(
                "/Users/jdramsey/Downloads/Cycles_Data_fMRI-selected/Structure3_amp_amp"));
        simulations.add(new LoadContinuousDataAndSingleGraph(
                "/Users/jdramsey/Downloads/Cycles_Data_fMRI-selected/Structure3_amp_contr"));
        simulations.add(new LoadContinuousDataAndSingleGraph(
                "/Users/jdramsey/Downloads/Cycles_Data_fMRI-selected/Structure3_contr_amp"));
        simulations.add(new LoadContinuousDataAndSingleGraph(
                "/Users/jdramsey/Downloads/Cycles_Data_fMRI-selected/Structure4_amp_amp"));
        simulations.add(new LoadContinuousDataAndSingleGraph(
                "/Users/jdramsey/Downloads/Cycles_Data_fMRI-selected/Structure4_amp_contr"));
        simulations.add(new LoadContinuousDataAndSingleGraph(
                "/Users/jdramsey/Downloads/Cycles_Data_fMRI-selected/Structure4_contr_amp"));
        simulations.add(new LoadContinuousDataAndSingleGraph(
                "/Users/jdramsey/Downloads/Cycles_Data_fMRI-selected/Structure5_amp"));
        simulations.add(new LoadContinuousDataAndSingleGraph(
                "/Users/jdramsey/Downloads/Cycles_Data_fMRI-selected/Structure5_contr"));

        simulations.add(new LoadContinuousDataAndSingleGraph(
                "/Users/jdramsey/Downloads/Cycles_Data_fMRI-selected/Structure2_amp_c4"));
        simulations.add(new LoadContinuousDataAndSingleGraph(
                "/Users/jdramsey/Downloads/Cycles_Data_fMRI-selected/Structure2_contr_c4"));

        simulations.add(new LoadContinuousDataAndSingleGraph(
                "/Users/jdramsey/Downloads/Cycles_Data_fMRI-selected/Structure2_contr_p2n6"));
        simulations.add(new LoadContinuousDataAndSingleGraph(
                "/Users/jdramsey/Downloads/Cycles_Data_fMRI-selected/Structure2_contr_p6n2"));


        simulations.add(new LoadContinuousDataAndSingleGraph(
                "/Users/jdramsey/Downloads/Cycles_Data_fMRI-selected/ComplexMatrix_1"));

        simulations.add(new LoadContinuousDataAndSingleGraph(
                "/Users/jdramsey/Downloads/Cycles_Data_fMRI-selected/Diamond"));

        Algorithms algorithms = new Algorithms();
        algorithms.add(new Fang());
//        algorithms.add(new CcdMax(new SemBicTest()));

        Comparison comparison = new Comparison();

        comparison.setShowAlgorithmIndices(true);
        comparison.setShowSimulationIndices(true);
        comparison.setSortByUtility(false);
        comparison.setShowUtilities(false);
        comparison.setParallelized(false);
        comparison.setSaveGraphs(false);

        comparison.setTabDelimitedTables(false);

        comparison.compareFromSimulations("comparison", simulations, algorithms, statistics, parameters);
    }

    public void TestCycles_Data_fMRI_FANG() {
        Parameters parameters = new Parameters();

        parameters.set("penaltyDiscount", 1);
        parameters.set("depth", -1);
        parameters.set("maxCoef", 0.6);

        parameters.set("numRuns", 10);

        // For automatically generated concatenations if you're doing them.
        parameters.set("randomSelectionSize", 10);

        parameters.set("Structure", "Placeholder");

        Statistics statistics = new Statistics();

        statistics.add(new ParameterColumn("Structure"));
        statistics.add(new AdjacencyPrecision());
        statistics.add(new AdjacencyRecall());
        statistics.add(new ArrowheadPrecision());
        statistics.add(new ArrowheadRecall());
        statistics.add(new TwoCyclePrecision());
        statistics.add(new TwoCycleRecall());
        statistics.add(new TwoCycleFalsePositive2());
        statistics.add(new TwoCycleFalseNegative2());
        statistics.add(new TwoCycleTruePositive());
        statistics.add(new ElapsedTime());

        statistics.setWeight("AP", 1.0);
        statistics.setWeight("AR", 1.0);
        statistics.setWeight("AHP", 1.0);
        statistics.setWeight("AHR", 1.0);
        statistics.setWeight("2CP", 1.0);
        statistics.setWeight("2CR", 1.0);
        statistics.setWeight("2CFP", 1.0);

        Simulations simulations = new Simulations();

        simulations.add(new LoadContinuousDataAndSingleGraph(
                "/Users/jdramsey/Downloads/Cycles_Data_fMRI/Network1_amp"));
        simulations.add(new LoadContinuousDataAndSingleGraph(
                "/Users/jdramsey/Downloads/Cycles_Data_fMRI/Network2_amp"));
        simulations.add(new LoadContinuousDataAndSingleGraph(
                "/Users/jdramsey/Downloads/Cycles_Data_fMRI/Network3_amp"));
        simulations.add(new LoadContinuousDataAndSingleGraph(
                "/Users/jdramsey/Downloads/Cycles_Data_fMRI/Network4_amp"));
        simulations.add(new LoadContinuousDataAndSingleGraph(
                "/Users/jdramsey/Downloads/Cycles_Data_fMRI/Network5_amp"));
        simulations.add(new LoadContinuousDataAndSingleGraph(
                "/Users/jdramsey/Downloads/Cycles_Data_fMRI/Network5_contr"));
        simulations.add(new LoadContinuousDataAndSingleGraph(
                "/Users/jdramsey/Downloads/Cycles_Data_fMRI/Network5_contr_p2n6"));
        simulations.add(new LoadContinuousDataAndSingleGraph(
                "/Users/jdramsey/Downloads/Cycles_Data_fMRI/Network5_contr_p6n2"));
        simulations.add(new LoadContinuousDataAndSingleGraph(
                "/Users/jdramsey/Downloads/Cycles_Data_fMRI/Network6_amp"));
        simulations.add(new LoadContinuousDataAndSingleGraph(
                "/Users/jdramsey/Downloads/Cycles_Data_fMRI/Network6_contr"));
        simulations.add(new LoadContinuousDataAndSingleGraph(
                "/Users/jdramsey/Downloads/Cycles_Data_fMRI/Network7_amp"));
        simulations.add(new LoadContinuousDataAndSingleGraph(
                "/Users/jdramsey/Downloads/Cycles_Data_fMRI/Network7_contr"));
        simulations.add(new LoadContinuousDataAndSingleGraph(
                "/Users/jdramsey/Downloads/Cycles_Data_fMRI/Network8_amp_amp"));
        simulations.add(new LoadContinuousDataAndSingleGraph(
                "/Users/jdramsey/Downloads/Cycles_Data_fMRI/Network8_amp_contr"));
        simulations.add(new LoadContinuousDataAndSingleGraph(
                "/Users/jdramsey/Downloads/Cycles_Data_fMRI/Network8_contr_amp"));
        simulations.add(new LoadContinuousDataAndSingleGraph(
                "/Users/jdramsey/Downloads/Cycles_Data_fMRI/Network9_amp_amp"));
        simulations.add(new LoadContinuousDataAndSingleGraph(
                "/Users/jdramsey/Downloads/Cycles_Data_fMRI/Network9_amp_contr"));
        simulations.add(new LoadContinuousDataAndSingleGraph(
                "/Users/jdramsey/Downloads/Cycles_Data_fMRI/Network9_contr_amp"));
        simulations.add(new LoadContinuousDataAndSingleGraph(
                "/Users/jdramsey/Downloads/Cycles_Data_fMRI/Diamond"));
        simulations.add(new LoadContinuousDataAndSingleGraph(
                "/Users/jdramsey/Downloads/Cycles_Data_fMRI/Markov_Complex_1"));

        Algorithms algorithms = new Algorithms();

//        algorithms.add(new Fges(new edu.cmu.tetrad.algcomparison.score.SemBicScore(), true));
//        algorithms.add(new PcMax(new SemBicTest(), true));
        algorithms.add(new Fang());
//        algorithms.add(new FasLofs(Lofs2.Rule.R1));
//        algorithms.add(new FasLofs(Lofs2.Rule.R2));
//        algorithms.add(new FasLofs(Lofs2.Rule.R3));
//        algorithms.add(new FasLofs(Lofs2.Rule.Patel));
//        algorithms.add(new FasLofs(Lofs2.Rule.Skew));
//        algorithms.add(new FasLofs(Lofs2.Rule.RSkew));

//        algorithms.add(new FgesConcatenated(new edu.cmu.tetrad.algcomparison.score.SemBicScore(), true));
//        algorithms.add(new PcMaxConcatenated(new SemBicTest(), true));
//        algorithms.add(new FangConcatenated());
//        algorithms.add(new FasLofsConcatenated(Lofs2.Rule.R1));
//        algorithms.add(new FasLofsConcatenated(Lofs2.Rule.R2));
//        algorithms.add(new FasLofsConcatenated(Lofs2.Rule.R3));
//        algorithms.add(new FasLofsConcatenated(Lofs2.Rule.Patel));
//        algorithms.add(new FasLofsConcatenated(Lofs2.Rule.Skew));
//        algorithms.add(new FasLofsConcatenated(Lofs2.Rule.RSkew));

        Comparison comparison = new Comparison();

        comparison.setShowAlgorithmIndices(true);
        comparison.setShowSimulationIndices(true);
        comparison.setSortByUtility(false);
        comparison.setShowUtilities(false);
        comparison.setParallelized(false);
        comparison.setSaveGraphs(false);
        comparison.setTabDelimitedTables(false);

        comparison.compareFromSimulations("comparison", simulations, algorithms, statistics, parameters);
    }

    public void TestCycles_Data_fMRI_CCDMax() {
        Parameters parameters = new Parameters();

        parameters.set("penaltyDiscount", 3);
        parameters.set("depth", -1);
        parameters.set("orientVisibleFeedbackLoops", true);
        parameters.set("doColliderOrientation", true);
        parameters.set("useMaxPOrientationHeuristic", true);
        parameters.set("maxPOrientationMaxPathLength", 3);
        parameters.set("applyR1", true);
        parameters.set("orientTowardDConnections", true);
        parameters.set("gaussianErrors", false);
        parameters.set("assumeIID", false);
        parameters.set("collapseTiers", true);

        parameters.set("numRuns", 60);
        parameters.set("randomSelectionSize", 10);
        parameters.set("Structure", "Placeholder");

        Statistics statistics = new Statistics();

        statistics.add(new ParameterColumn("Structure"));
        statistics.add(new AdjacencyPrecision());
        statistics.add(new AdjacencyRecall());
        statistics.add(new ArrowheadPrecision());
        statistics.add(new ArrowheadRecall());
        statistics.add(new TwoCyclePrecision());
        statistics.add(new TwoCycleRecall());
        statistics.add(new TwoCycleFalsePositive2());
        statistics.add(new TwoCycleFalseNegative2());
        statistics.add(new TwoCycleTruePositive());
        statistics.add(new ElapsedTime());

        Simulations simulations = new Simulations();

        simulations.add(new LoadContinuousDataAndSingleGraph(
                "/Users/jdramsey/Downloads/Cycles_Data_fMRI/Network1_amp"));
        simulations.add(new LoadContinuousDataAndSingleGraph(
                "/Users/jdramsey/Downloads/Cycles_Data_fMRI/Network2_amp"));
        simulations.add(new LoadContinuousDataAndSingleGraph(
                "/Users/jdramsey/Downloads/Cycles_Data_fMRI/Network3_amp"));
        simulations.add(new LoadContinuousDataAndSingleGraph(
                "/Users/jdramsey/Downloads/Cycles_Data_fMRI/Network4_amp"));
        simulations.add(new LoadContinuousDataAndSingleGraph(
                "/Users/jdramsey/Downloads/Cycles_Data_fMRI/Network5_amp"));
        simulations.add(new LoadContinuousDataAndSingleGraph(
                "/Users/jdramsey/Downloads/Cycles_Data_fMRI/Network5_contr"));
        simulations.add(new LoadContinuousDataAndSingleGraph(
                "/Users/jdramsey/Downloads/Cycles_Data_fMRI/Network5_contr_p2n6"));
        simulations.add(new LoadContinuousDataAndSingleGraph(
                "/Users/jdramsey/Downloads/Cycles_Data_fMRI/Network5_contr_p6n2"));
        simulations.add(new LoadContinuousDataAndSingleGraph(
                "/Users/jdramsey/Downloads/Cycles_Data_fMRI/Network6_amp"));
        simulations.add(new LoadContinuousDataAndSingleGraph(
                "/Users/jdramsey/Downloads/Cycles_Data_fMRI/Network6_contr"));
        simulations.add(new LoadContinuousDataAndSingleGraph(
                "/Users/jdramsey/Downloads/Cycles_Data_fMRI/Network7_amp"));
        simulations.add(new LoadContinuousDataAndSingleGraph(
                "/Users/jdramsey/Downloads/Cycles_Data_fMRI/Network7_contr"));
        simulations.add(new LoadContinuousDataAndSingleGraph(
                "/Users/jdramsey/Downloads/Cycles_Data_fMRI/Network8_amp_amp"));
        simulations.add(new LoadContinuousDataAndSingleGraph(
                "/Users/jdramsey/Downloads/Cycles_Data_fMRI/Network8_amp_contr"));
        simulations.add(new LoadContinuousDataAndSingleGraph(
                "/Users/jdramsey/Downloads/Cycles_Data_fMRI/Network8_contr_amp"));
        simulations.add(new LoadContinuousDataAndSingleGraph(
                "/Users/jdramsey/Downloads/Cycles_Data_fMRI/Network9_amp_amp"));
        simulations.add(new LoadContinuousDataAndSingleGraph(
                "/Users/jdramsey/Downloads/Cycles_Data_fMRI/Network9_amp_contr"));
        simulations.add(new LoadContinuousDataAndSingleGraph(
                "/Users/jdramsey/Downloads/Cycles_Data_fMRI/Network9_contr_amp"));
        simulations.add(new LoadContinuousDataAndSingleGraph(
                "/Users/jdramsey/Downloads/Cycles_Data_fMRI/Diamond"));
        simulations.add(new LoadContinuousDataAndSingleGraph(
                "/Users/jdramsey/Downloads/Cycles_Data_fMRI/Markov_Complex_1"));

        Algorithms algorithms = new Algorithms();
//        algorithms.add(new FangConcatenated());
        algorithms.add(new CcdMaxConcatenated(new SemBicTest()));

        Comparison comparison = new Comparison();

        comparison.setShowAlgorithmIndices(false);
        comparison.setShowSimulationIndices(true);
        comparison.setSortByUtility(false);
        comparison.setShowUtilities(false);
        comparison.setParallelized(false);
        comparison.setSaveGraphs(false);

        comparison.setTabDelimitedTables(false);

        comparison.compareFromSimulations("comparison", simulations, algorithms, statistics, parameters);
    }

    public void TestSmith() {
        Parameters parameters = new Parameters();

        parameters.set("penaltyDiscount", 4);
        parameters.set("depth", -1);

        parameters.set("numRuns", 10);
        parameters.set("randomSelectionSize", 10);
        parameters.set("Structure", "Placeholder");


        Statistics statistics = new Statistics();

        statistics.add(new ParameterColumn("Structure"));
        statistics.add(new AdjacencyPrecision());
        statistics.add(new AdjacencyRecall());
        statistics.add(new ArrowheadPrecision());
        statistics.add(new ArrowheadRecall());
        statistics.add(new TwoCyclePrecision());
        statistics.add(new TwoCycleRecall());
        statistics.add(new TwoCycleFalsePositive2());
        statistics.add(new TwoCycleFalseNegative2());
        statistics.add(new TwoCycleTruePositive());
        statistics.add(new ElapsedTime());

        Simulations simulations = new Simulations();

        String path = "/Users/jdramsey/Downloads/smithsim.algcomp/";

        simulations.add(new LoadContinuousDataSmithSim(path + "1"));
        simulations.add(new LoadContinuousDataSmithSim(path + "2"));
        simulations.add(new LoadContinuousDataSmithSim(path + "3"));
        simulations.add(new LoadContinuousDataSmithSim(path + "4"));
        simulations.add(new LoadContinuousDataSmithSim(path + "5"));
        simulations.add(new LoadContinuousDataSmithSim(path + "6"));
        simulations.add(new LoadContinuousDataSmithSim(path + "7"));
        simulations.add(new LoadContinuousDataSmithSim(path + "8"));
        simulations.add(new LoadContinuousDataSmithSim(path + "9"));
        simulations.add(new LoadContinuousDataSmithSim(path + "10"));
        simulations.add(new LoadContinuousDataSmithSim(path + "11"));
        simulations.add(new LoadContinuousDataSmithSim(path + "12"));
        simulations.add(new LoadContinuousDataSmithSim(path + "13"));
        simulations.add(new LoadContinuousDataSmithSim(path + "14"));
        simulations.add(new LoadContinuousDataSmithSim(path + "15"));
        simulations.add(new LoadContinuousDataSmithSim(path + "16"));
        simulations.add(new LoadContinuousDataSmithSim(path + "17"));
        simulations.add(new LoadContinuousDataSmithSim(path + "18"));
        simulations.add(new LoadContinuousDataSmithSim(path + "19"));
        simulations.add(new LoadContinuousDataSmithSim(path + "20"));
        simulations.add(new LoadContinuousDataSmithSim(path + "21"));
        simulations.add(new LoadContinuousDataSmithSim(path + "22"));
        simulations.add(new LoadContinuousDataSmithSim(path + "22_2"));
        simulations.add(new LoadContinuousDataSmithSim(path + "23"));
        simulations.add(new LoadContinuousDataSmithSim(path + "24"));
        simulations.add(new LoadContinuousDataSmithSim(path + "25"));
        simulations.add(new LoadContinuousDataSmithSim(path + "26"));
        simulations.add(new LoadContinuousDataSmithSim(path + "27"));
        simulations.add(new LoadContinuousDataSmithSim(path + "28"));

        Algorithms algorithms = new Algorithms();
//        algorithms.add(new ImagesSemBic());
        algorithms.add(new Fang());

        Comparison comparison = new Comparison();

        comparison.setShowAlgorithmIndices(false);
        comparison.setShowSimulationIndices(false);
        comparison.setSortByUtility(false);
        comparison.setShowUtilities(false);
        comparison.setParallelized(false);
        comparison.setSaveGraphs(true);

        comparison.setTabDelimitedTables(false);

        comparison.compareFromSimulations("comparison", simulations, algorithms, statistics, parameters);
//        comparison.compareFromFiles("comparison", algorithms, statistics, parameters);
//        comparison.saveToFiles("comparison", new LinearFisherModel(new RandomForward()), parameters);

    }

    public void TestPwwd7() {
        Parameters parameters = new Parameters();

        parameters.set("penaltyDiscount", 1);
        parameters.set("depth", 4);
        parameters.set("maxCoef", .5);

        parameters.set("numRuns", 5);
        parameters.set("randomSelectionSize", 5);

        parameters.set("Structure", "Placeholder");

        Statistics statistics = new Statistics();

        statistics.add(new ParameterColumn("Structure"));
        statistics.add(new AdjacencyPrecision());
        statistics.add(new AdjacencyRecall());
        statistics.add(new ArrowheadPrecision());
        statistics.add(new ArrowheadRecall());
        statistics.add(new TwoCyclePrecision());
        statistics.add(new TwoCycleRecall());
        statistics.add(new TwoCycleFalsePositive2());
        statistics.add(new TwoCycleFalseNegative2());
        statistics.add(new TwoCycleTruePositive());
        statistics.add(new ElapsedTime());

        Simulations simulations = new Simulations();

        String path = "/Users/jdramsey/Downloads/pwdd7.algcomp/";

        simulations.add(new LoadContinuousDataSmithSim(path + "1"));
        simulations.add(new LoadContinuousDataSmithSim(path + "2"));
        simulations.add(new LoadContinuousDataSmithSim(path + "3"));
        simulations.add(new LoadContinuousDataSmithSim(path + "4"));
        simulations.add(new LoadContinuousDataSmithSim(path + "5"));
        simulations.add(new LoadContinuousDataSmithSim(path + "6"));
        simulations.add(new LoadContinuousDataSmithSim(path + "7"));
        simulations.add(new LoadContinuousDataSmithSim(path + "8"));
        simulations.add(new LoadContinuousDataSmithSim(path + "9"));
        simulations.add(new LoadContinuousDataSmithSim(path + "10"));
        simulations.add(new LoadContinuousDataSmithSim(path + "11"));
        simulations.add(new LoadContinuousDataSmithSim(path + "12"));
//        simulations.add(new LoadContinuousDataSmithSim(path + "13"));
        simulations.add(new LoadContinuousDataSmithSim(path + "14"));
        simulations.add(new LoadContinuousDataSmithSim(path + "15"));
        simulations.add(new LoadContinuousDataSmithSim(path + "16"));
        simulations.add(new LoadContinuousDataSmithSim(path + "17"));
        simulations.add(new LoadContinuousDataSmithSim(path + "18"));
        simulations.add(new LoadContinuousDataSmithSim(path + "19"));
        simulations.add(new LoadContinuousDataSmithSim(path + "20"));
        simulations.add(new LoadContinuousDataSmithSim(path + "22"));
        simulations.add(new LoadContinuousDataSmithSim(path + "22_2"));
        simulations.add(new LoadContinuousDataSmithSim(path + "23"));
        simulations.add(new LoadContinuousDataSmithSim(path + "24"));
        simulations.add(new LoadContinuousDataSmithSim(path + "25"));
        simulations.add(new LoadContinuousDataSmithSim(path + "26"));
        simulations.add(new LoadContinuousDataSmithSim(path + "27"));
        simulations.add(new LoadContinuousDataSmithSim(path + "28"));

        Algorithms algorithms = new Algorithms();
        algorithms.add(new Fang());
//        algorithms.add(new ImagesSemBic());

        Comparison comparison = new Comparison();

        comparison.setShowAlgorithmIndices(true);
        comparison.setShowSimulationIndices(false);
        comparison.setSortByUtility(false);
        comparison.setShowUtilities(false);
        comparison.setParallelized(false);
        comparison.setSaveGraphs(true);

        comparison.setTabDelimitedTables(false);

        comparison.compareFromSimulations("comparison", simulations, algorithms, statistics, parameters);
    }

//    @Test
//    public void loadAlexandersDataset() {
//        String path = "/Users/jdramsey/Downloads/converted_rndhrs_numeric_only.txt";
//
//        File file = new File(path);
//
//        DataReader reader = new DataReader();
//        reader.setVariablesSupplied(true);
//        reader.setDelimiter(DelimiterType.WHITESPACE);
//        reader.setMissingValueMarker("NA");
//
//        try {
//            DataSet dataSet = reader.parseTabular(file);
//            System.out.println(dataSet);
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//    }

//    public void loadAlexandersDataset2() {
//        String path = "/Users/jdramsey/Downloads/fails_328_lines.txt";
//
//        File file = new File(path);
//
//        AbstractContinuousDataReader reader = new TabularContinuousDataReader(
//                Paths.get("/Users/jdramsey/Downloads/converted_rndhrs_numeric_only.txt"), ' ');
//
//        DataReader reader = new DataReader();
//        reader.setVariablesSupplied(true);
//        reader.setDelimiter(DelimiterType.WHITESPACE);
//        reader.setMissingValueMarker("NA");
//
//        try {
//            DataSet dataSet = reader.parseTabular(file);
//            System.out.println(dataSet);
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//    }

    @Test
    public void test5() {
        for (int j = 0; j < 10; j++) {

            int n = 1000;
            double rho = .1;

            double[] eX = new double[n];
            double[] eY = new double[n];
            double[] x = new double[n];
            double[] y = new double[n];

            BetaDistribution d = new BetaDistribution(2, 5);

            for (int i = 0; i < n; i++) {
                eX[i] = d.sample();
                eY[i] = d.sample();
                x[i] = eX[i];
                y[i] = rho * x[i] + eY[i];
//
//                x[i] = signum(skewness(x)) * x[i];
//                y[i] = signum(skewness(y)) * y[i];
            }

            standardizeData(x);
            standardizeData(y);

            System.out.println("cov = " + s(x, y, eY));
        }
    }

    private double s(double[] x, double[] y, double[] eY) {
        double exy = 0.0;

        double ex = 0.0;
        double ey = 0.0;

        int n = 0;

        for (int k = 0; k < x.length; k++) {
            if (y[k] > 0) {
                exy += x[k] * eY[k];
                ex += x[k];
                ey += y[k];
                n++;
            }
        }

        exy /= n;
        ex /= n;
        ey /= n;

        return (exy - ex * ey);
    }

    public static void standardizeData(double[] data) {
        double sum = 0.0;

        for (int i = 0; i < data.length; i++) {
            sum += data[i];
        }

        double mean = sum / data.length;

        for (int i = 0; i < data.length; i++) {
            data[i] -= mean;
        }

        double norm = 0.0;

        for (int i = 0; i < data.length; i++) {
            double v = data[i];
            norm += v * v;
        }

        norm = Math.sqrt(norm / (data.length - 1));

        for (int i = 0; i < data.length; i++) {
            data[i] /= norm;
        }
    }

    @Test
    public void testAutistic1() {

        Parameters parameters = new Parameters();

        parameters.set("penaltyDiscount", 1);
        parameters.set("depth", -1);
        parameters.set("maxCoef", .5);

        parameters.set("numRuns", 50);
        parameters.set("randomSelectionSize", 1);
        parameters.set("Structure", "Placeholder");

//        Files train = new Files("/Users/jdramsey/Documents/LAB_NOTEBOOK.2012.04.20/data/Joe_108_Variable", parameters);
//        Files train = new Files("/Users/jdramsey/Documents/LAB_NOTEBOOK.2012.04.20/data/USM_Datasets2", parameters);
//        Files train = new Files("/Users/jdramsey/Documents/LAB_NOTEBOOK.2012.04.20/data/Whole_Cerebellum_Scans", parameters);

        Files train = new Files("/Users/jdramsey/Downloads/USM_ABIDE", new Parameters());
//x`
//        Files test = new Files("/Users/jdramsey/Documents/LAB_NOTEBOOK.2012.04.20/data/Joe_108_Variable", parameters);
//        Files test = new Files("/Users/jdramsey/Documents/LAB_NOTEBOOK.2012.04.20/data/USM_Datasets2", parameters);
//        Files test = new Files("/Users/jdramsey/Documents/LAB_NOTEBOOK.2012.04.20/data/Whole_Cerebellum_Scans", parameters);
        Files test = new Files("/Users/jdramsey/Downloads/USM_ABIDE", new Parameters());

        train.reconcileNames(test);
        test.reconcileNames(train);

//        trainTest(train, test);
        leaveOneOut(train);
    }


    private void trainTest(Files train, Files test) {
        int numTp = 0;
        int numFp = 0;
        int numMeh = 0;

        List<Edge> allEdges = getAllEdges(train.getGraphs(), train.getTypes(), train.getGraphs());
        List<List<Edge>> ret = train(train.getGraphs(), allEdges, train.getTypes());

        printFiles(train.getGraphs(), train.getTypes(), -1, ret);

        for (int i = 0; i < test.getGraphs().size(); i++) {
            int _class = test(i, test.getFilenames(), test.getGraphs(), test.getTypes(), ret);

            if (_class == 1) numTp++;
            if (_class == -1) numFp++;
            if (_class == 0) numMeh++;
        }

        System.out.println();
        System.out.println("# TP = " + numTp);
        System.out.println("# FP = " + numFp);
        System.out.println("# Unclassified = " + numMeh);

        NumberFormat nf = new DecimalFormat("0.00");
        System.out.println("Precision = " + nf.format((numTp / (double) (numTp + numFp))));
        System.out.println();
    }

    private void leaveOneOut(Files train) {
        int numTp = 0;
        int numFp = 0;
        int numUnclassified = 0;

        for (int i = 0; i < train.getGraphs().size(); i++) {
            List<Graph> trainingGraphs = new ArrayList<>(train.getGraphs());
            trainingGraphs.remove(train.getGraphs().get(i));
            List<Edge> allEdges = getAllEdges(train.getGraphs(), train.getTypes(), trainingGraphs);

            List<List<Edge>> ret = train(trainingGraphs, allEdges, train.getTypes());
            int _class = test(i, train.getFilenames(), train.getGraphs(), train.getTypes(), ret);

            if (_class == 1) numTp++;
            if (_class == -1) numFp++;
            if (_class == 0) numUnclassified++;

            printFiles(train.getGraphs(), train.getTypes(), i, ret);
        }

        System.out.println();
        System.out.println("# TP = " + numTp);
        System.out.println("# FP = " + numFp);
        System.out.println("# Unclassified = " + numUnclassified);

        NumberFormat nf = new DecimalFormat("0.00");
        System.out.println("Precision = " + nf.format((numTp / (double) (numTp + numFp))));
        System.out.println();
    }

    private class Files {
        private List<String> filenames = new ArrayList<>();
        private List<DataSet> datasets = new ArrayList<>();
        private List<Graph> graphs = new ArrayList<>();
        private List<Boolean> types = new ArrayList<>();

        public Files(String path, Parameters parameters) {
            loadFiles(path, parameters);
        }

        private void loadFiles(String path, Parameters parameters) {
            DataReader reader = new DataReader();
            reader.setVariablesSupplied(true);
            reader.setDelimiter(DelimiterType.TAB);

            File dir = new File(path);

            for (File file : dir.listFiles()) {
                String name = file.getName();

                if (name.contains("ROI_data") && !name.contains("graph")) {
                    try {
                        if (name.contains("autistic")) {
                            types.add(true);
                            DataSet dataSet = reader.parseTabular(new File(path, name));
                            filenames.add(name);
                            datasets.add(dataSet);
                            Fang fang = new Fang();
                            Graph search = fang.search(dataSet, parameters);
                            graphs.add(search);
                        } else if (name.contains("typical")) {
                            types.add(false);
                            DataSet dataSet = reader.parseTabular(new File(path, name));
                            filenames.add(name);
                            datasets.add(dataSet);
                            Fang fang = new Fang();
                            Graph search = fang.search(dataSet, parameters);
                            graphs.add(search);
                        }
                    } catch (IOException e) {
                        System.out.println("File " + name + " could not be parsed.");
                    }
                }
            }

            reconcileNames();
        }

        public List<String> getFilenames() {
            return filenames;
        }

        public List<Graph> getGraphs() {
            return graphs;
        }

        public void setGraphs(List<Graph> graphs) {
            this.graphs = graphs;
        }

        public List<Boolean> getTypes() {
            return types;
        }

        private void reconcileNames(Files... files) {
            Set<String> allNames = new HashSet<>();
            List<Node> nodes = new ArrayList<>();

            for (Files file : files) {
                for (Graph graph : file.getGraphs()) {
                    for (Node node : graph.getNodes()) {
                        if (!allNames.contains(node.getName())) {
                            nodes.add(node);
                            allNames.add(node.getName());
                        }
                    }
                }
            }

            List<Graph> graphs2 = new ArrayList<>();

            for (Graph graph : graphs) {
                graphs2.add(GraphUtils.replaceNodes(graph, nodes));
            }

            this.graphs = graphs2;
        }

        public List<DataSet> getDatasets() {
            return datasets;
        }

        public void setDatasets(List<DataSet> datasets) {
            this.datasets = datasets;
        }
    }

    private List<Edge> getAllEdges(List<Graph> _graphs, List<Boolean> types, List<Graph> trainingGraphs) {
        Map<Edge, Double> autisticEdgeCount = new HashMap<>();
        Map<Edge, Double> typicalEdgeCount = new HashMap<>();

        Set<Edge> allEdgesSet = new HashSet<>();

        for (int k = 0; k < trainingGraphs.size(); k++) {
            for (Edge edge : trainingGraphs.get(k).getEdges()) {
                if (types.get(k)) {
                    countEdge(autisticEdgeCount, edge);
                } else {
                    countEdge(typicalEdgeCount, edge);
                }
            }

            allEdgesSet.addAll(_graphs.get(k).getEdges());
        }
        return new ArrayList<>(allEdgesSet);
    }

    private void printFiles(List<Graph> _graphs, List<Boolean> types, int i, List<List<Edge>> ret) {

        try {
            List<Edge> sublist = ret.get(4);

            File dir2 = new File("/Users/jdramsey/Downloads/alldata");
            dir2.mkdirs();
            PrintStream out = new PrintStream(new File(dir2, "data" + (i + 1) + ".txt"));

            for (int j = 0; j < sublist.size(); j++) {
                out.print("X" + (j + 1) + "\t");
            }

            out.println("T");

            for (int k = 0; k < _graphs.size(); k++) {
                for (Edge edge : sublist) {
                    out.print(_graphs.get(k).containsEdge(edge) ? "1\t" : "0\t");
                }

                out.println(types.get(k) ? "1" : "0");
            }

            out.close();

            File dir3 = new File("/Users/jdramsey/Downloads/allkeys");
            dir3.mkdirs();
            PrintStream keyOut = new PrintStream(new File(dir3, "key" + (i + 1) + ".txt"));

            for (int j = 0; j < sublist.size(); j++) {
                keyOut.println("X" + (j + 1) + ". " + sublist.get(j));
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    private List<List<Edge>> train(List<Graph> trainingGraphs, List<Edge> allEdges, List<Boolean> types) {
        double[] truth = getTruth(trainingGraphs, types);

        List<Edge> forAutismIfPresent = new ArrayList<>();
        List<Edge> forAutismIfAbsent = new ArrayList<>();
        List<Edge> forTypicalIfPresent = new ArrayList<>();
        List<Edge> forTypicalIfAbsent = new ArrayList<>();

        for (Edge edge : allEdges) {
            double[][] est = new double[1][];
            est[0] = getEst(trainingGraphs, edge);

            if (cond(est, truth, 1, 1, 5)) {
                forAutismIfPresent.add(edge);
            }

            if (cond(est, truth, 0, 1, 1)) {
                forAutismIfAbsent.add(edge);
            }

            if (cond(est, truth, 1, 0, 5)) {
                forTypicalIfPresent.add(edge);
            }

            if (cond(est, truth, 0, 0, 1)) {
                forTypicalIfAbsent.add(edge);
            }
        }

        Set<Edge> sublist = new HashSet<>();

        sublist.addAll(forAutismIfPresent);
        sublist.addAll(forAutismIfAbsent);
        sublist.addAll(forTypicalIfPresent);
        sublist.addAll(forTypicalIfAbsent);

        List<Edge> _sublist = new ArrayList<>(sublist);

        // return from train.
        List<List<Edge>> ret = new ArrayList<>();
        ret.add(forAutismIfPresent);
        ret.add(forAutismIfAbsent);
        ret.add(forTypicalIfPresent);
        ret.add(forTypicalIfAbsent);
        ret.add(_sublist);

        return ret;
    }

    private int test(int i, List<String> filenames, List<Graph> graphs, List<Boolean> types, List<List<Edge>> ret) {
        Graph testGraph = graphs.get(i);

        List<Edge> forAutisismIfPresent = ret.get(0);
        List<Edge> forAutisismIfAbsent = ret.get(1);
        List<Edge> forTypicalIfPresent = ret.get(2);
        List<Edge> forTypicalIfAbsent = ret.get(3);

        List<Edge> presentAutistic = new ArrayList<>();
        List<Edge> absentAutistic = new ArrayList<>();
        List<Edge> presentTypical = new ArrayList<>();
        List<Edge> absentTypical = new ArrayList<>();

        for (Edge edge : forAutisismIfPresent) {
            if (testGraph.containsEdge(edge)) {
                presentAutistic.add(edge);
            }
        }

        for (Edge edge : forAutisismIfAbsent) {
            if (!testGraph.containsEdge(edge)) {
                absentAutistic.add(edge);
            }
        }

        for (Edge edge : forTypicalIfPresent) {
            if (testGraph.containsEdge(edge)) {
                presentTypical.add(edge);
            }
        }

        for (Edge edge : forTypicalIfAbsent) {
            if (!testGraph.containsEdge(edge)) {
                absentTypical.add(edge);
            }
        }

        boolean autistic = false;
        boolean typical = false;

        if (!absentAutistic.isEmpty()) {
            autistic = true;
        }

        if (!presentAutistic.isEmpty()) {
            autistic = true;
        }

        if (!presentTypical.isEmpty()) {
            typical = true;
        }

        if (!absentTypical.isEmpty()) {
            typical = true;
        }

        String name = "" + (i + 1) + ". " + filenames.get(i) + ". ";

        if (autistic && !typical) {
            System.out.println(name + ". Autistic");
        }
        else if (typical && !autistic) {
            System.out.println(name + ". Typical");
        }

        for (Edge aPresent : presentAutistic) {
            System.out.println("..... present autistic " + aPresent);
        }

        for (Edge anAbsent : absentAutistic) {
            System.out.println("..... absent autistic " + anAbsent);
        }

        for (Edge aPresent : presentTypical) {
            System.out.println("..... present typical " + aPresent);
        }

        for (Edge anAbsent : absentTypical) {
            System.out.println("..... absent typical " + anAbsent);
        }

        if (autistic && !typical) {
            if (types.get(i)) {
                return 1;
            } else {
                return -1;
            }
        }

        if (typical && !autistic) {
            if (!types.get(i)) {
                return 1;
            } else {
                return -1;
            }
        }

        return 0;
    }

    private double[] getTruth(List<Graph> trainingGraphs, List<Boolean> types) {
        double[] truth = new double[trainingGraphs.size()];
        int w = 0;

        for (int k = 0; k < trainingGraphs.size(); k++) {
            truth[w++] = types.get(k) ? 1.0 : 0.0;
        }
        return truth;
    }

    private double[] getEst(List<Graph> trainingGraphs, Edge allEdge) {
        double[] est = new double[trainingGraphs.size()];
        int _w = 0;

        for (Graph trainingGraph : trainingGraphs) {
            est[_w++] = (trainingGraph.containsEdge(allEdge)) ? 1.0 : 0.0;
        }

        return est;
    }


    private boolean edgeFromTo(Edge edge, String left, String right) {
        return edge.getNode1().getName().equals(left) && edge.getNode2().getName().equals(right);
    }

    // Returns true if a2 = j on condition that a1 = i.
    private boolean cond(double[][] a1, double[] a2, int i, int j, int min) {
        int occurs = 0;
        int isTheCase = 0;

        int N = a2.length;

        for (int w = 0; w < N; w++) {
            boolean all = true;

            for (double[] anA1 : a1) {
                if (anA1[w] != i) all = false;
            }

            if (all) {
                occurs++;

                if (a2[w] == j) {
                    isTheCase++;
                }
            }

//            if (a1[0][w] == i) {
//                occurs++;
//
//                if (a2[w] == j) {
//                    isTheCase++;
//                }
//            }
        }

        return occurs >= min && isTheCase == occurs;
    }

    private Double count(Map<Edge, Double> edges1Count, Edge edge) {
        if (edges1Count.get(edge) == null) return 0.0;
        return edges1Count.get(edge);
    }

    private void countEdge(Map<Edge, Double> map, Edge edge) {
        if (count(map, edge) == null) {
            map.put(edge, 0.0);
        }

        map.put(edge, count(map, edge) + 1);
    }

    private List<Graph> replaceNodes(List<Graph> graphs, List<Node> nodes) {
        List<Graph> replaced = new ArrayList<>();

        for (Graph graph : graphs) {
            replaced.add(GraphUtils.replaceNodes(graph, nodes));
        }

        return replaced;
    }

    private List<Graph> runFangOnSubsets(List<DataSet> dataSets, Parameters parameters, Fang fang,
                                         int subsetSize, Map<DataSet, String> filenames, Map<Graph, String> graphNames) {
        List<Graph> graphs = new ArrayList<>();

        List<DataSet> copy = new ArrayList<>(dataSets);
//        Collections.shuffle(copy);

        for (int i = 0; i < subsetSize; i++) {
            Graph search = fang.search(copy.get(i), parameters);
            graphs.add(search);
            graphNames.put(search, filenames.get(copy.get(i)));
        }

        return graphs;
    }

    @Test
    public void testForBiwei() {
        Parameters parameters = new Parameters();

        parameters.set("penaltyDiscount", 2);
        parameters.set("depth", -1);
        parameters.set("maxCoef", 0.6);

        parameters.set("numRuns", 10);
        parameters.set("randomSelectionSize", 1);
        parameters.set("Structure", "Placeholder");

        Files files = new Files("/Users/jdramsey/Downloads/USM_ABIDE", new Parameters());

        List<DataSet> datasets = files.getDatasets();
        List<String> filenames = files.getFilenames();

        for (int i = 0; i < datasets.size(); i++) {
            DataSet dataSet = datasets.get(i);

            SemBicScore score = new SemBicScore(new CovarianceMatrixOnTheFly(dataSet));
            Fas fas = new Fas(new IndTestScore(score));
            Graph graph = fas.search();

            System.out.println(graph);
            List<Node> nodes = graph.getNodes();

            StringBuilder b = new StringBuilder();

            for (int j = 0; j < nodes.size(); j++) {
                for (int k = 0; k < nodes.size(); k++) {
                    if (graph.isAdjacentTo(nodes.get(j), nodes.get(k))) {
                        b.append("1 ");
                    } else {
                        b.append("0 ");
                    }
                }

                b.append("\n");
            }

            try {
                File dir = new File("/Users/jdramsey/Downloads/biwei/USM_ABIDE");
                dir.mkdirs();
                File file = new File(dir, filenames.get(i) + ".graph.txt");
                PrintStream out = new PrintStream(file);
                out.println(b);
                out.close();
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }

        }


    }

    public static void main(String... args) {
        new TestFang().TestCycles_Data_fMRI_FANG();
    }
}




