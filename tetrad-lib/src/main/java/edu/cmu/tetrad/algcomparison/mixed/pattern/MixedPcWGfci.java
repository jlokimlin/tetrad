package edu.cmu.tetrad.algcomparison.mixed.pattern;

import edu.cmu.tetrad.algcomparison.Algorithm;
import edu.cmu.tetrad.data.DataSet;
import edu.cmu.tetrad.graph.Graph;
import edu.cmu.tetrad.search.*;

import java.util.Map;

/**
 * Created by jdramsey on 6/4/16.
 */
public class MixedPcWGfci implements Algorithm {
    public Graph search(DataSet ds, Map<String, Number> parameters) {
        WGfci fgs = new WGfci(ds);
//        fgs.setDepth(parameters.get("fgsDepth").intValue());
        fgs.setPenaltyDiscount(parameters.get("penaltyDiscount").doubleValue());
        Graph g =  fgs.search();
        IndependenceTest test = new IndTestMixedLrt(ds, parameters.get("alpha").doubleValue());
        Pc pc = new Pc(test);
        pc.setInitialGraph(g);
        return pc.search();
    }

    public Graph getComparisonGraph(Graph dag) {
        return SearchGraphUtils.patternForDag(dag);
    }

    public String getDescription() {
        return "PC with the mixed LRT test, using the output of WGFCI as an intial graph";
    }
}
