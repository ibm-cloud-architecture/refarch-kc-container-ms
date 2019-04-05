#!/bin/bash

export msname="containerkstreams"
export chart=$(ls ./chart/| grep $msname | head -1)
export kname="kc-"$chart
export ns="greencompute"
export CLUSTER_NAME=streamer.icp

