#!/bin/bash

# Usage: <protein train> <protein test> 
#                 <window size> <number of hidden layers> <hidden layer size> <iterations> <output directory> <baseline>

#rostTrainFilename="/Users/vail/Documents/school/cs760/ProteinSSClassification/data/rost/protein_data"
#uciTrainFilename="/Users/vail/Documents/school/cs760/ProteinSSClassification/data/uci/uci_protein_data.train"
uciTestFilename="/Users/vail/Documents/school/cs760/ProteinSSClassification/data/uci/uci_protein_data.test"

trainFilename="/home/vail/Documents/school/cs760/ProteinSSClassification/data/data.train"
outputDirectory="/home/vail/Documents/school/cs760/ProteinSSClassification/results/baseline"

numHiddenLayers=1
hiddenLayerSize=50 # jump this by 5 all the way up to 50
iterations=5 # increase the number of iterations as we go
windowSize=13
baseline="y"


# Baseline experiments

for i in {15..300..15}
do
    echo "Running Baseline: hidden layer size -> $i" >> "${outputDirectory}/log"
    java -jar deepnetwork.jar $trainFilename "none" $windowSize $numHiddenLayers $i $iterations $outputDirectory $baseline
done

for i in 7 11 13 17
do
    echo "Running Baseline: window size -> $i" >> "${outputDirectory}/log"
    java -jar deepnetwork.jar $trainFilename "none" $i $numHiddenLayers $hiddenLayerSize $iterations $outputDirectory $baseline
done
