#!/bin/bash -f
# ****************************************************************************
# Vivado (TM) v2022.1 (64-bit)
#
# Filename    : elaborate.sh
# Simulator   : Xilinx Vivado Simulator
# Description : Script for elaborating the compiled design
#
# Generated by Vivado on Fri Sep 23 18:48:09 JST 2022
# SW Build 3526262 on Mon Apr 18 15:47:01 MDT 2022
#
# IP Build 3524634 on Mon Apr 18 20:55:01 MDT 2022
#
# usage: elaborate.sh
#
# ****************************************************************************
set -Eeuo pipefail
# elaborate design
echo "xelab --incr --debug all --relax --mt 8 -d "SIMULATION=" -d "WAVE_DUMP=" -L xil_defaultlib -L uvm -L unisims_ver -L unimacro_ver -L secureip --snapshot div64_l8_u5_gznk_test_behav xil_defaultlib.div64_l8_u5_gznk_test xil_defaultlib.glbl -log elaborate.log -debug all"
xelab --incr --debug all --relax --mt 8 -d "SIMULATION=" -d "WAVE_DUMP=" -L xil_defaultlib -L uvm -L unisims_ver -L unimacro_ver -L secureip --snapshot div64_l8_u5_gznk_test_behav xil_defaultlib.div64_l8_u5_gznk_test xil_defaultlib.glbl -log elaborate.log -debug all
