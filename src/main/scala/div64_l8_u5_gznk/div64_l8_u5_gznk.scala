package div64_l8_u5_gznk

import chisel3._
import chisel3.stage.ChiselStage

class div64_l8_u5_gznk(inData: Int, usrData: Int, stages: Int) extends Module {
  val io = IO(new Bundle {
    val stop = Input(Bool())
    val in1 = Input(UInt(inData.W))
    val in2 = Input(UInt(inData.W))
    val in_usr = Input(UInt(usrData.W))
    val in_en = Input(Bool())
    val quotient = Output(UInt(inData.W))
    val remainder = Output(UInt(inData.W))
    val out_usr = Output(UInt(usrData.W))
    val div_by_zero = Output(Bool())
    val out_en = Output(Bool())
  })

  val in1_vec = Wire(Vec(inData, UInt(inData.W)))
  val in2_vec = Wire(Vec(inData, UInt(inData.W)))
  val quo_vec = Wire(Vec(inData, UInt(inData.W)))
  val rem_vec = Wire(Vec(inData, UInt(inData.W)))
  val usr_vec = Wire(Vec(inData, UInt(usrData.W)))
  val op_valid = Wire(Vec(inData, Bool()))

  //***** initial stage
  in1_vec(0) := io.in1
  in2_vec(0) := io.in2
  usr_vec(0) := io.in_usr
  op_valid(0) := io.in_en
  //*** remainder and quotient
  when ( io.in2 === 1.U(inData.W) ) {
    quo_vec(0) := io.in1(inData-1)
    rem_vec(0) := 0.U
  } otherwise {
    quo_vec(0) := 0.U
    rem_vec(0) := io.in1(inData-1)
  }

  for ( i <- 1 until inData ) {
    //val rem_wire = Wire(Vec(inData, UInt(inData.W)))
    //val quo_wire = Wire(Vec(inData, UInt(inData.W)))
    val rem_sft = Wire(UInt(inData.W))
    val rem_tmp = Wire(UInt(inData.W))
    val rem_new = Wire(UInt(inData.W))
    val quo_tmp = Wire(UInt(inData.W))
    val quo_new = Wire(UInt(inData.W))
    rem_sft := rem_vec(i-1) << 1
    rem_tmp := rem_sft | in1_vec(i-1)(inData-1-i)
    quo_tmp := quo_vec(i-1)
    when ( rem_tmp >= in2_vec(i-1) ) {
      rem_new := rem_tmp - in2_vec(i-1)
      quo_new := quo_tmp.bitSet((inData-1-i).U, true.B)
    } otherwise {
      rem_new := rem_tmp
      quo_new := quo_tmp
    }

    val next_in1 = Wire(UInt(inData.W))
    val next_in2 = Wire(UInt(inData.W))
    val next_quo = Wire(UInt(inData.W))
    val next_rem = Wire(UInt(inData.W))
    val next_usr = Wire(UInt(usrData.W))
    val next_op = Wire(Bool())
    when ( op_valid(i-1) ) {
      next_in1 := in1_vec(i-1)
      next_in2 := in2_vec(i-1)
      next_quo := quo_new
      next_rem := rem_new
      next_usr := usr_vec(i-1)
      next_op := op_valid(i-1)
    } otherwise {
      next_in1 := 0.U(inData.W)
      next_in2 := 0.U(inData.W)
      next_quo := 0.U(inData.W)
      next_rem := 0.U(inData.W)
      next_usr := 0.U(usrData.W)
      next_op := false.B
    }

    // number of logics within a stage := inData / stages
    val logics:Int = inData / stages
    if ( i % logics == logics/2 ) {
      val reg_in1 = Wire(UInt(inData.W))
      val reg_in2 = Wire(UInt(inData.W))
      val reg_quo = Wire(UInt(inData.W))
      val reg_rem = Wire(UInt(inData.W))
      val reg_usr = Wire(UInt(usrData.W))
      val reg_op = Wire(Bool())
      when ( io.stop ) {
        reg_in1 := in1_vec(i)
        reg_in2 := in2_vec(i)
        reg_quo := quo_vec(i)
        reg_rem := rem_vec(i)
        reg_usr := usr_vec(i)
        reg_op := op_valid(i)
      } otherwise {
        reg_in1 := next_in1
        reg_in2 := next_in2
        reg_quo := next_quo
        reg_rem := next_rem
        reg_usr := next_usr
        reg_op := next_op
      }
      in1_vec(i) := RegNext(reg_in1, 0.U(inData.W))
      in2_vec(i) := RegNext(reg_in2, 0.U(inData.W))
      quo_vec(i) := RegNext(reg_quo, 0.U(inData.W))
      rem_vec(i) := RegNext(reg_rem, 0.U(inData.W))
      usr_vec(i) := RegNext(reg_usr, 0.U(usrData.W))
      op_valid(i) := RegNext(reg_op, false.B)
    } else {
      in1_vec(i) := next_in1
      in2_vec(i) := next_in2
      quo_vec(i) := next_quo
      rem_vec(i) := next_rem
      usr_vec(i) := next_usr
      op_valid(i) := next_op
    }
  }

  io.quotient := quo_vec(inData-1)
  io.remainder := rem_vec(inData-1)
  io.out_usr := usr_vec(inData-1)
  io.div_by_zero := in2_vec(inData-1) === 0.U(inData.W)
  io.out_en := op_valid(inData-1)
}
object div64_l8_u5_gznk_driver extends App {
  (new ChiselStage).emitVerilog(new div64_l8_u5_gznk(64, 5, 8), args)
}
