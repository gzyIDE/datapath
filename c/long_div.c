#include <stdio.h>
#include "div_util.h"

int main(void)
{
	unsigned int dividend = 100;
	unsigned int divisor = 16;
	unsigned int quotient = 0;
	unsigned int remainder = 0;
	int bits = 32;
	int i;

	if ( divisor == 0 ) {
		printf("division by zero\n");
		return 1;
	} else {
		for ( i = bits-1; i >= 0; i = i - 1 ) {
			remainder = remainder << 1;
			remainder = remainder | get_bit(dividend,i);
			if ( remainder >= divisor ) {
				remainder = remainder - divisor;
				quotient = set_bit(quotient, i);
			}
		}
	}

	printf("long div\n");
	printf("    quotient: %d\n", quotient);
	printf("    remainder: %d\n", remainder);

	printf("gcc div\n");
	printf("    quotient: %d\n", dividend / divisor);
	printf("    remainder: %d\n", dividend % divisor);
}
