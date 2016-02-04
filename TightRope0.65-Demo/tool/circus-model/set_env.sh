#!/bin/sh

# Note that this script must be executed via "source set_env.sh".

MODEL_HOME=$(readlink -f $(dirname $0))

export MODEL_HOME

CZT_PATH=$MODEL_HOME/toolkits:$CZT_PATH
CZT_PATH=$MODEL_HOME/framework/circus:$CZT_PATH
CZT_PATH=$MODEL_HOME/library/circus:$CZT_PATH

export CZT_PATH
