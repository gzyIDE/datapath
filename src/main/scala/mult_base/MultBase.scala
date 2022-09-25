package mult_base

import chisel3._
import chisel3.stage.ChiselStage

class MultBase(inData: Int, usrData: Int, stages: Int) extends Module {
  val io = IO(new Bundle {
    val stop = Input(Bool())
    val in1 = Input(UInt(inData.W))
    val in2 = Input(UInt(inData.W))
    val in_usr = Input(UInt(usrData.W))
    val in_en = Input(Bool())
    val result_l = Output(UInt(inData.W))
    val result_h = Output(UInt(inData.W))
    val out_usr = Output(UInt(usrData.W))
    val out_en = Output(Bool())
  })

  val in1_vec = Wire(Vec(inData, UInt(inData.W)))
  val in2_vec = Wire(Vec(inData, UInt(inData.W)))
  val res_vec = Wire(Vec(inData, UInt((inData*2).W)))
  val usr_vec = Wire(Vec(inData, UInt(usrData.W)))
  val op_valid = Wire(Vec(inData, Bool()))

  //***** initial stage
  in1_vec(0) := io.in1
  in2_vec(0) := io.in2
  //res_vec(0) := 0.U((inData*2).W)
  when ( io.in2(0) ) {
      res_vec(0) := io.in1
  } otherwise {
      res_vec(0) := 0.U(inData.W)
  }
  usr_vec(0) := io.in_usr
  op_valid(0) := io.in_en

  for ( i <- 1 until inData ) {
    val in_tmp  = Wire(UInt((inData*2).W))
    val res_new = Wire(UInt((inData*2).W))
    when ( in2_vec(i-1)(i) ) {
        in_tmp := in1_vec(i-1) << i
    } otherwise {
        in_tmp := 0.U(inData.W)
    }
    res_new := res_vec(i-1) + in_tmp

    val next_in1 = Wire(UInt(inData.W))
    val next_in2 = Wire(UInt(inData.W))
    val next_res = Wire(UInt((inData*2).W))
    val next_usr = Wire(UInt(usrData.W))
    val next_op = Wire(Bool())
    next_in1 := in1_vec(i-1)
    next_in2 := in2_vec(i-1)
    next_res := res_new
    next_usr := usr_vec(i-1)
    next_op  := op_valid(i-1)

    // number of logics within a stage := inData / stages
    val logics:Int = inData / (stages - 1)
    if ( i % logics == logics/2 ) {
      val reg_in1 = Wire(UInt(inData.W))
      val reg_in2 = Wire(UInt(inData.W))
      val reg_res = Wire(UInt((inData*2).W))
      val reg_usr = Wire(UInt(usrData.W))
      val reg_op = Wire(Bool())
      when ( io.stop ) {
        reg_in1 := in1_vec(i)
        reg_in2 := in2_vec(i)
        reg_res := res_vec(i)
        reg_usr := usr_vec(i)
        reg_op := op_valid(i)
      } otherwise {
        when(next_op) {
          reg_in1 := next_in1
          reg_in2 := next_in2
          reg_res := next_res
        } otherwise {
          reg_in1 := 0.U(inData.W)
          reg_in2 := 0.U(inData.W)
          reg_res := 0.U((inData*2).W)
        }
        reg_usr := next_usr
        reg_op := next_op
      }

      in1_vec(i) := RegNext(reg_in1)
      in2_vec(i) := RegNext(reg_in2)
      res_vec(i) := RegNext(reg_res)
      usr_vec(i) := RegNext(reg_usr)
      op_valid(i) := RegNext(reg_op)
    } else {
      in1_vec(i) := next_in1
      in2_vec(i) := next_in2
      res_vec(i) := next_res
      usr_vec(i) := next_usr
      op_valid(i) := next_op
    }
  }

  io.result_l := RegNext(res_vec(inData-1)(inData,0))
  io.result_h := RegNext(res_vec(inData-1)(inData*2-1,inData))
  io.out_usr  := RegNext(usr_vec(inData-1))
  io.out_en   := RegNext(op_valid(inData-1))
}
