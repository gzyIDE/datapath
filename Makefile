BUILD=build
RTL=rtl
SRC=src/main/scala

vpath %.v	= $(RTL)

#all: GCD.v Pipe.v
all: GCD.v

GCD.v: $(SRC)/gcd/GCD.scala
	mkdir -p rtl
	sbt "runMain gcd.GCDDriver --target-dir $(BUILD)"
	cp $(BUILD)/$@ $(RTL)

#Pipe.v: $(SRC)/pipe/Pipe.scala
#	mkdir -p rtl
#	sbt "runMain pipe.PipeDriver --target-dir $(BUILD)"
#	cp $(BUILD)/$@ $(RTL)

clean:
	rm -rf test_run_dir
	rm -rf target
	rm -rf rtl
	rm -rf build
	rm -rf project
