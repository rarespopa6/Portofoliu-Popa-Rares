#include "SMIterator.h"
#include "SortedMap.h"
#include <exception>

using namespace std;

SMIterator::SMIterator(const SortedMap& d) : map(d) {
    stack_cap = map.size();
    stack = new int[stack_cap];
    stack_size = 0;
    current = -1;
    first();
}

SMIterator::~SMIterator() {
    delete[] stack;
}

void SMIterator::resizeStack() {
    stack_cap *= 2;
    int* newStack = new int[stack_cap];
    for (int i = 0; i < stack_size; ++i) {
        newStack[i] = stack[i];
    }
    delete[] stack;
    stack = newStack;
}

void SMIterator::first() {
    stack_size = 0;
    current = map.root;
    while (current != -1) {
        if (stack_size == stack_cap) {
            resizeStack();
        }
        stack[stack_size++] = current;
        current = map.nodes[current].left;
    }
    if (stack_size > 0) {
        current = stack[--stack_size];
    } else {
        current = -1;
    }
}

void SMIterator::next() {
    if (!valid()) {
        throw exception();
    }

    current = map.nodes[current].right;
    while (current != -1) {
        if (stack_size == stack_cap)
            resizeStack();
        stack[stack_size++] = current;
        current = map.nodes[current].left;
    }
    if (stack_size > 0)
        current = stack[--stack_size];
    else
        current = -1;
}

bool SMIterator::valid() const {
    return current != -1;
}

TElem SMIterator::getCurrent() const {
    if (!valid()) {
        throw exception();
    }
    return map.nodes[current].info;
}

