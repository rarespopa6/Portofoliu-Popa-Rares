#pragma once
#include "SortedMap.h"

//DO NOT CHANGE THIS PART
class SMIterator{
	friend class SortedMap;
private:
	const SortedMap& map;
	SMIterator(const SortedMap& mapionar);

    int* stack;
    int stack_cap;
    int stack_size;
    int current;

    void resizeStack();

public:
	void first();
	void next();
	bool valid() const;
    TElem getCurrent() const;
    ~SMIterator();
};

