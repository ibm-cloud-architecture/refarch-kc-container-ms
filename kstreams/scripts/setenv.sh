#!/bin/bash

export msname="containerkstreams"
export chart=$(ls ./chart/| grep $msname)
export kname="kc-"$chart
export ns="browncompute"

