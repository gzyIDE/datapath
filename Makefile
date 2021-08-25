BUILD=build
RTL=rtl
SRC=src/main/scala
SCRIPT=script

vpath %.v	= $(RTL)

all: div64_l8_u5_gznk.v

div%_gznk.v: $(SRC)/div_base/DivBase.scala
	mkdir -p rtl
	$(SCRIPT)/div_config.sh $(SRC) $(@:.v=)
	sbt "runMain $(@:.v=).$(@:.v=)_driver --target-dir $(BUILD)"
	cp $(BUILD)/$@ $(RTL)

clean:
	rm -rf test_run_dir
	rm -rf target
	rm -rf rtl
	rm -rf build
	rm -rf project
