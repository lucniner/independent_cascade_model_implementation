# Influence maximization with independent cascade

This repository is for the first programming exercise in Networks Design and Analysis summer term 2018 at the technical university of vienna.

## Build

To build the code and run with default values simply execute the following:

```shell
./gradlew clean jar
cd build/libs/
java -jar independent_cascade_model_implementation.jar
```

The following options are available and can be displayed when adding the -h flag to the jar execution. 

```shell
usage: java -jar <name> [-b <arg>] [-h] [-i] [-p <arg>] [-s <arg>] [-v]
 -b,--budget <arg>        how many seed nodes should be chosen value > 0
 -h,--help                print this message
 -i,--indegree            use the in-degree activation function instead of
                          the uniform probability
 -p,--probability <arg>   the uniform probability, value between 1 and 100
 -s,--scenarios <arg>     how many scenarios should be created value > 0
 -v,--visualize           visualization of the output
```

## Implementation

There are in total three implementations:
* GreedyApproximation Algorithm - based on the lecture slides with multiple scenario runs - very time consuming
* Single Diffusion - heavily based on the activation function also takes nodes greedily
* Independent Cascade Model - based on the paper from Goyal et al. from 2011  where they presented the CELF++ algorithm.

## Test Instance

The code got implemented with directed graphs in mind and uses the Cit-HepPh.txt data set from [standford.](http://snap.stanford.edu/data/index.html)

It is also possible to use this program with other directed graphs, we did not implement the undirected version. This is easily possible by adapting the ProblemGraph and the ProblemReader to make edges bidirectional.

## Visualization

There is a very basic visualization implemented with the GraphStream API. It only shows seed nodes and activated nodes, as well as one layer of neighbours (inactive) and maximum four per node. This may result in not completely connected results in the visualization.

## LICENCE - MIT

Copyright 2018, Patrick Weißkirchner | Lukas Kathrein

Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation files (the "Software"), to deal in the Software without restriction, including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.