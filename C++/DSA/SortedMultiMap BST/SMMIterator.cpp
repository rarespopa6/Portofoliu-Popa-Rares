#include "SMMIterator.h"
#include "SortedMultiMap.h"
#include <exception>

/// Θ(1)
SMMIterator::SMMIterator(const SortedMultiMap& d) : map(d) {
    // Man erstellt einen Stack
    // Dynamisches Array von BSTNode*
    this->stackCapacity = 10;
    this->stack = new SortedMultiMap::BSTNode*[this->stackCapacity];
    this->stackSize = 0;

    // Reverse Stack fur previous
    this->reverseStackCapacity = 10;
    this->reverseStack = new SortedMultiMap::BSTNode*[this->reverseStackCapacity];
    this->reverseStackSize = 0;

    this->current = nullptr;
    this->first(); // fur Positionierung am linksten Knote
}

/// Θ(n)
void SMMIterator::resizeStack() {
    // Die Kapazitat von Stack wird verdoppelt und man kopiert alle Elemente im neunen Stack
    this->stackCapacity *= 2;
    SortedMultiMap::BSTNode** newStack = new SortedMultiMap::BSTNode*[this->stackCapacity];
    for (int i = 0; i < this->stackSize; i++) {
        newStack[i] = this->stack[i];
    }
    delete[] this->stack;
    this->stack = newStack;
}

/// Θ(n)
void SMMIterator::resizeReverseStack() {
    // Die Kapazitat von Stack wird verdoppelt und man kopiert alle Elemente im neunen Stack
    this->reverseStackCapacity *= 2;
    SortedMultiMap::BSTNode** newReverseStack = new SortedMultiMap::BSTNode*[this->reverseStackCapacity];
    for (int i = 0; i < this->reverseStackSize; i++) {
        newReverseStack[i] = this->reverseStack[i];
    }
    delete[] this->reverseStack;
    this->reverseStack = newReverseStack;
}

/// Best Case: Θ(1) falls der Unterbaum keinen linken Unterbaum hat
/// Average Case: Θ(log n) - balanciert
/// Worst Case: Θ(n^2) - falls man Resize fur Stack braucht und falls es einen linken Unterbaum gibt
/// Allgemeine Komplexitat: O(n^2)
void SMMIterator::first() {
    this->stackSize = 0;
    this->current = this->map.root;
    while (this->current != nullptr) {
        if (this->stackSize == this->stackCapacity) {
            this->resizeStack();
        }
        this->stack[this->stackSize++] = this->current;
        this->current = this->current->left;
    }
    if (this->stackSize > 0) {
        this->current = this->stack[--this->stackSize];
    } else {
        this->current = nullptr;
    }
}

/// Best Case: Θ(1) falls der Unterbaum keinen rechten Unterbaum hat
/// Average Case: Θ(log n) - balanciert
/// Worst Case: Θ(n^2) - falls man Resize fur Stack braucht und falls es einen rechten Unterbaum gibt
/// Allgemeine Komplexitat: O(n^2)
// In-order Traversierung => L-W-R
void SMMIterator::next() {
    if (!this->valid()) {
        throw exception();
    }
    if (this->reverseStackSize == this->reverseStackCapacity) {
        this->resizeReverseStack();
    }

    // Man fugt den CurrentNode im ReverseStack, moglich zu sein, umgekehrt zu gehen
    this->reverseStack[this->reverseStackSize++] = this->current;

    // Falls es einen rechten Unterbaum gibt, dann geht man in die Richtung
    if (this->current->right != nullptr) {
        this->current = this->current->right;
        // Man geht so viel wie moglich links in diesem rechten Unterbaum
        while (this->current != nullptr) {
            if (this->stackSize == this->stackCapacity) {
                this->resizeStack();
            }
            // Current wird im Stack eingefuhrt
            this->stack[this->stackSize++] = this->current;
            this->current = this->current->left;
        }
    }
    // Falls es noch andere Nodes im Stack gibt, dann wird current = top from Stack
    if (this->stackSize > 0) {
        this->current = this->stack[--this->stackSize];
    } else {
        this->current = nullptr; // Stop, Traversierung ist fertig
    }
}

/// Θ(1)
void SMMIterator::previous() {
    if (this->reverseStackSize == 0) {
        throw exception();
    }

    if (this->stackSize == this->stackCapacity) {
        this->resizeStack();
    }

    // CurrentNode wird im Stack eingefugt
    this->stack[this->stackSize++] = this->current;
    // Current wird = top from ReverseStack sein, also das vorherige Element
    this->current = this->reverseStack[--this->reverseStackSize];
}

/// Θ(1)
bool SMMIterator::valid() const {
    return this->current != nullptr;
}

/// Θ(1)
TElem SMMIterator::getCurrent() const {
    if (!this->valid()) {
        throw exception();
    }
    return this->current->info;
}

/// Θ(1)
SMMIterator::~SMMIterator() {
    delete[] this->stack;
    delete[] this->reverseStack;
}

