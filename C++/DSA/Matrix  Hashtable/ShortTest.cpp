#include <assert.h>
#include "Matrix.h"

using namespace std;

void testFunc(){
    Matrix m(4, 4);
    m.modify(1, 1, 5);
    m.modify(1, 1, 6);
    Matrix m2(4, 4);
    assert((m == m2) == false);

    Matrix m3(4, 4);
    Matrix m4(4, 4);
    assert(m3 == m4);

    m3.modify(1, 1, 5);
    m4.modify(1, 1, 5);
    assert(m3 == m4);

    m3.modify(2, 2, 10);
    m4.modify(2, 2, 10);
    assert(m3 == m4);

    m3.modify(3, 3, 20);
    assert(!(m3 == m4));

    m3.modify(3, 3, NULL_TELEM);
    assert(m3 == m4);
}

void testAll() { 
	Matrix m(4, 4);
	assert(m.nrLines() == 4);
	assert(m.nrColumns() == 4);	
	m.modify(1, 1, 5);
	assert(m.element(1, 1) == 5);
	m.modify(1, 1, 6);
	assert(m.element(1, 2) == NULL_TELEM);
    testFunc();
}