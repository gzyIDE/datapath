/*
* <mult32_l4_u5_gznk_test.sv>
* 
* Copyright (c) 2022 Yosuke Ide <gizaneko@outlook.jp>
* 
* This software is released under the MIT License.
* http://opensource.org/licenses/mit-license.php
*/

`include "stddef.vh"
`include "sim.vh"

`timescale 1ns/10ps

module mult32_l4_u5_gznk_test;
	parameter STEP = 100;
	parameter LOOP = 1000;

	logic			clock;
	logic			reset;
	logic			io_stop;
	logic [31:0]	io_in1;
	logic [31:0]	io_in2;
	logic [4:0]		io_in_usr;
	logic			io_in_en;
	
	wire [31:0]		io_result_l;
	wire [31:0]		io_result_h;
	wire [4:0]		io_out_usr;
	wire			io_out_en;

	logic [63:0]    result;
	logic           error;

	mult32_l4_u5_gznk mult32 (
		.*
	);

	always #(STEP/2) begin
		clock <= ~clock;
	end

	//***** Simulation Body
	int i;
	initial begin
		reset     <= `Enable;
		clock     <= `Low;
		io_stop   <= `Low;
		io_in1    <= `Zero;
		io_in2    <= `Zero;
		io_in_usr <= `Zero;
		io_in_en  <= `Disable;
		error     <= `Disable;
		repeat(5) @(posedge clock);
		reset     <= `Disable;
		repeat(10) @(posedge clock);

		for ( i = 0; i < LOOP; i = i + 1 ) begin
			io_in1   <= $random();
			io_in2   <= $random();
			//io_in1     <= 32'h0000_1111;
			//io_in2     <= 32'h8000_0000;
			//io_in1   <= i;
			//io_in2   <= i * 10 + i;
			io_in_en <= 1'b1;
			@(posedge clock);
			io_in_en <= 1'b0;
			result   <= io_in1 * io_in2;
			while(!io_out_en) @(posedge clock);
			if ( result != {io_result_h, io_result_l}) begin
				error <= `Enable;
				`SetCharBold
				`SetCharRed
				$display("Error in mult: %x * %x", io_in1, io_in2);
				$display("    result   : %x", {io_result_h, io_result_l});
				$display("    expected : %x", result);
				`ResetCharSetting
			end

			@(posedge clock);
			error <= `Disable;
			repeat(5) @(posedge clock);
		end

		$finish;
	end

	`include "waves.vh"

endmodule
