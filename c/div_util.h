#ifndef _DIV_UTIL_H_INCLUDED_
#define _DIV_UTIL_H_INCLUDED_

unsigned int get_bit(unsigned int n, int pos) {
	return (n << (31-pos)) >> 31;
}

unsigned int set_bit(unsigned int n, int pos) {
	return n | (1 << pos);
}

#endif //_DIV_UTIL_H_INCLUDED_
