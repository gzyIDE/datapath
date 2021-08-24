BUILD=build
RTL=rtl
SRC=src/main/scala

vpath %.v	= $(RTL)

#all: GCD.v Pipe.v
#all: GCD.v
#all: Pipe.v
#all: DivBase.v
all: div64_l8.v

GCD.v: $(SRC)/gcd/GCD.scala
	mkdir -p rtl
	sbt "runMain gcd.GCDDriver --target-dir $(BUILD)"
	cp $(BUILD)/$@ $(RTL)

Pipe.v: $(SRC)/pipe/Pipe.scala
	mkdir -p rtl
	sbt "runMain pipe.PipeDriver --target-dir $(BUILD)"
	cp $(BUILD)/$@ $(RTL)

DivBase.v: $(SRC)/div_base/DivBase.scala
	mkdir -p rtl
	sbt "runMain div_base.DivBaseDriver --target-dir $(BUILD)"
	cp $(BUILD)/$@ $(RTL)

div64_l8.v: $(SRC)/div64_l8/div64_l8.scala $(SRC)/div_base/DivBase.scala
	mkdir -p rtl
	sbt "runMain div64_l8.div64_l8_driver --target-dir $(BUILD)"
	cp $(BUILD)/$@ $(RTL)

clean:
	rm -rf test_run_dir
	rm -rf target
	rm -rf rtl
	rm -rf build
	rm -rf project
