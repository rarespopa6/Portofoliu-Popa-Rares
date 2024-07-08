#include "SortedSetIterator.h"
#include <exception>

SortedSetIterator::SortedSetIterator(const SortedSet& m) : multime(m), current(m.root) {
    // Constructor implementation
    first();
}

void SortedSetIterator::first() {
    current = multime.root;
    while (current != nullptr && current->left != nullptr) {
        current = current->left;
    }
}

void SortedSetIterator::next() {
    if (!valid()) {
        throw std::exception();
    }

    if (current->right != nullptr) {
        current = current->right;
        while (current->left != nullptr) {
            current = current->left;
        }
    }
    else {
        BSTNode* parent = nodeParent(multime.root, current);
        while (parent != nullptr && current == parent->right) {
            current = parent;
            parent = nodeParent(multime.root, parent);
        }
        current = parent;
    }
}

TElem SortedSetIterator::getCurrent() {
    if (!valid()) {
        throw std::exception();
    }

    return current->info;
}

bool SortedSetIterator::valid() const {
    return current != nullptr;
}

BSTNode* SortedSetIterator::nodeParent(BSTNode* tree, BSTNode* node) const {
    BSTNode* c = tree;

    if (c==node) {
        return nullptr; // node is root
    }
    else {
        while (c!= nullptr && c->left != node && c->right!=node) {
            if(multime.relation(node->info,c->info)) {
                c = c->left;
            }
            else
                c = c->right;
        }
    }
    return c;
}
