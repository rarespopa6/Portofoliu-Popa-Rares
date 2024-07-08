#pragma once

#include "SortedMultiMap.h"

class SMMIterator {
    friend class SortedMultiMap;
private:
    const SortedMultiMap& map;
    SMMIterator(const SortedMultiMap& map);

    SortedMultiMap::BSTNode* current;

    SortedMultiMap::BSTNode** stack;
    int stackCapacity;
    int stackSize;

    SortedMultiMap::BSTNode** reverseStack;
    int reverseStackCapacity;
    int reverseStackSize;

    void resizeStack();
    void resizeReverseStack();
public:
    void first();
    void next();
    void previous();
    bool valid() const;
    TElem getCurrent() const;
    ~SMMIterator();
};

