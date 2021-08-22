package pipe

import chisel3._
import chisel3.stage.ChiselStage

class Pipe(inBits: Int, stages: Int) extends Module {
  val io = IO(new Bundle {
    val in = Input(UInt(inBits.W))
    val out = Output(UInt(inBits.W))
  })

  val pipe = Wire(Vec(stages, UInt(inBits.W)))

  pipe(0) := io.in
  for (i <- 1 until stages) {
    val incr = pipe(i-1) + 1.U(inBits.W)
    // insert register after two additions
    if ( i % 2 == 0 ) {
      pipe(i) := incr
    } else {
      pipe(i) := RegNext(incr, 0.U(inBits.W))
    }
  }

  io.out := pipe(stages-1)
}

object PipeDriver extends App {
  (new ChiselStage).emitVerilog(new Pipe(32,8), args)
}
