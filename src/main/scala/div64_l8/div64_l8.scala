package div64_l8

import chisel3._
import chisel3.stage.ChiselStage
import div_base.DivBase

class div64_l8 extends Module {
  val input_data = 64
  // user_data
  //    sign bit of divisor: 1-bit
  //    sign bit of dividend: 1-bit
  //    divider operation: 3-bit
  val user_data = 5
  val stages = 8

  val io = IO(new Bundle {
    val in1 = Input(UInt(input_data.W))
    val in2 = Input(UInt(input_data.W))
    val in_usr = Input(UInt(user_data.W))
    val in_en = Input(Bool())
    val quotient = Output(UInt(input_data.W))
    val remainder = Output(UInt(input_data.W))
    val out_usr = Output(UInt(user_data.W))
    val div_by_zero = Output(Bool())
    val out_en = Output(Bool())
  })

  val div_inst = Module(new DivBase(input_data, user_data, stages))
  io <> div_inst.io
}

object div64_l8_driver extends App {
  (new ChiselStage).emitVerilog(new div64_l8, args)
}
