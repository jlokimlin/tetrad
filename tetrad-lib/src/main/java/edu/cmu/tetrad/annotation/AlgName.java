package edu.cmu.tetrad.annotation;

/**
 * Author : Jeremy Espino MD Created 6/30/17 4:16 PM
 */
public enum AlgName {
    // forbid_latent_common_causes
    PC, PcStableMax, FGES, IMaGES_Discrete, IMaGES_Continuous, FANG, EFANG,
    // allow_latent_common_causes
    FCI, RFCI, GFCI, TsFCI, TsGFCI, TsImages,
    // search_for_Markov_blankets
    FgesMb, MBFS,
    // produce_undirected_graphs
    FAS, MGM, GLASSO,
    // orient_pairwise
    EB, R1, R2, R3, R4, RSkew, RSkewE, Skew, SkewE,
    // search_for_structure_over_latents
    Bpc, Fofc, Ftfc,
    // bootstrapping
    BootstrapFGES, BootstrapGFCI, BootstrapRFCI
}