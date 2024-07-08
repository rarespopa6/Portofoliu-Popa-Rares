#pragma once

#include <iostream>

//DO NOT CHANGE THIS PART
typedef int TKey;
#define NULL_TELEM 0
#define DELETED -111111

class Matrix {

private:
	struct Triple{
        int i;
        int j;
        TKey key;
    };

    int nr_lines;
    int nr_cols;

    Triple* T;
    int m;
    int h(TKey k, int i) const{
        return ((hash1(k) + i * hash2(k)) % m);
    }

    int hash1(TKey k) const{
        return k % m;
    }

    int hash2(TKey k) const{
        return 1 + (k % (m - 1));
    }

    void resize_rehash();

public:
	//constructor
	Matrix(int nrLines, int nrCols); /// + functia de hash ?
	//returns the number of lines
	int nrLines() const;

	//returns the number of columns
	int nrColumns() const;

	//returns the element from line i and column j (indexing starts from 0)
	//throws exception if (i,j) is not a valid position in the Matrix
	TKey element(int i, int j) const;

	//modifies the value from line i and column j
	//returns the previous value from the position
	//throws exception if (i,j) is not a valid position in the Matrix
	TKey modify(int i, int j, TKey e);

    bool operator==(const Matrix& other) const;
	
	// destructor
	~Matrix();

};
