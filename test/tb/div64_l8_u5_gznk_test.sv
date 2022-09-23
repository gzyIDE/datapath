/*
* <sample_com_test.sv>
* 
* Copyright (c) 2022 Yosuke Ide <gizaneko@outlook.jp>
* 
* This software is released under the MIT License.
* http://opensource.org/licenses/mit-license.php
*/

`include "stddef.vh"
`include "sim.vh"

`ifdef NETLIST
	`timescale 1ns/10ps
`endif

module div64_l8_u5_gznk_test;
	parameter STEP = 100;

	logic			clock;
	logic			reset;
	logic			io_stop;
	logic [63:0]	io_in1;
	logic [63:0]	io_in2;
	logic [4:0]		io_in_usr;
	logic			io_in_en;
	wire  [63:0]	io_quotient;
	wire  [63:0]	io_remainder;
	wire  [4:0]		io_out_usr;
	wire			io_div_by_zero;
	wire			io_out_en;

	div64_l8_u5_gznk div0 (
		.*
	);

	always #(STEP/2) begin
		clock <= ~clock;
	end

	//***** Simulation Body
	int i;
	initial begin
		reset = `Enable;
		clock = `Low;
		io_stop = `Low;
		io_in1 = `Zero;
		io_in2 = `Zero;
		io_in_usr = `Zero;
		io_in_en = `Disable;
		repeat(5) @(posedge clock);
		reset = `Disable;

		for ( i = 0; i < 1000; i = i + 1 ) begin
			io_in1 = $random();
			io_in2 = $random();
			io_in_en = 1'b1;
			@(posedge clock);
			io_in_en = 1'b0;
			while(!io_out_en) @(posedge clock);
			if ( (io_in1 / io_in2) != io_quotient ) begin
				`SetCharBold
				`SetCharRed
				$display("Error: %x / %x", io_in1, io_in2);
				`ResetCharSetting
			end
			if ( (io_in1 % io_in2) != io_remainder) begin
				`SetCharBold
				`SetCharRed
				$display("Error: %x / %x", io_in1, io_in2);
				`ResetCharSetting
			end

			repeat(5) @(posedge clock);
		end

		$finish;
	end

	`include "waves.vh"

endmodule
