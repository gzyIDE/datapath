#!/bin/tcsh

if ( $# < 2 ) then
	echo "There are not sufficient arguments"
	exit
endif

set dst_dir = $1
set module = $2
set dst_file = $1/$2/$2.scala
set base_div = $1/div_base/DivBase.scala
mkdir -p ${dst_dir}/${module}

# data size
set size = `echo $module | cut -d "_" -f 1 | tr -d div`
# divider latency
set latency = `echo $module | cut -d "_" -f 2 | tr -d l`
# user data size
set usr = `echo $module | cut -d "_" -f 3 | tr -d u`

# modify template design
set sed_opt = ( \
	-e "s/DivBase/$2/g" \
	-e "s/div_base/$2/g" \
)
sed $sed_opt $base_div > $dst_file

# append verilog output function at the tail
echo "object ${module}_driver extends App {" >> $dst_file
echo "  (new ChiselStage).emitVerilog(new ${module}(${size}, ${usr}, ${latency}), args)" >> ${dst_file}
echo "}" >> $dst_file
