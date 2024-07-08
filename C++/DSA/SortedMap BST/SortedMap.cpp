#include "SMIterator.h"
#include "SortedMap.h"
#include <exception>
using namespace std;

SortedMap::SortedMap(Relation r) {
    this->r = r;
    this->capacity = 10;
    this->nodes = new BSTNode[capacity];
    this->root = -1;
    this->length = 0;
    this->firstEmpty = 0;

    for (int i = 0; i < capacity - 1; ++i) {
        nodes[i].left = i + 1;
    }
    nodes[capacity - 1].left = -1;
}

void SortedMap::resize() {
    int newCapacity = capacity * 2;
    BSTNode* newNodes = new BSTNode[newCapacity];

    for (int i = 0; i < capacity; ++i) {
        newNodes[i] = nodes[i];
    }

    for (int i = capacity; i < newCapacity - 1; ++i) {
        newNodes[i].left = i + 1;
    }
    newNodes[newCapacity - 1].left = -1;

    delete[] nodes;
    nodes = newNodes;
    firstEmpty = capacity;
    capacity = newCapacity;
}

int SortedMap::allocateNode(TKey k, TValue v) {
    if (firstEmpty == -1) {
        resize();
    }
    int newNode = firstEmpty;
    firstEmpty = nodes[firstEmpty].left;

    nodes[newNode].info = {k, v};
    nodes[newNode].left = -1;
    nodes[newNode].right = -1;
    return newNode;
}

void SortedMap::deallocateNode(int node) {
    nodes[node].left = firstEmpty;
    firstEmpty = node;
}

TValue SortedMap::add(TKey k, TValue v) {
    int newNode = allocateNode(k, v);

    if (root == -1) {
        root = newNode;
        length++;
        return NULL_TVALUE;
    } else {
        int current = root;
        int parent = -1;

        while (current != -1) {
            parent = current;
            if(nodes[current].info.first == k){
                TValue old_val = nodes[current].info.second;
                nodes[current].info.second = v; // Replace the value
                deallocateNode(newNode);
                return old_val;
            }
            if (r(k, nodes[current].info.first)) {
                current = nodes[current].left;
            } else{
                current = nodes[current].right;
            }
        }

        if (r(k, nodes[parent].info.first)) {
            nodes[parent].left = newNode;
        } else {
            nodes[parent].right = newNode;
        }
        length++;
        return NULL_TVALUE;
    }

}

TValue SortedMap::search(TKey k) const {
    int current = root;

    while (current != -1) {
        if (nodes[current].info.first == k) {
            return nodes[current].info.second;
        }
        if (r(k, nodes[current].info.first)) {
            current = nodes[current].left;
        } else {
            current = nodes[current].right;
        }
    }

    return NULL_TVALUE;
}

TValue SortedMap::remove(TKey k) {
    int current = root;
    int parent = -1;

    // Găsirea nodului de șters și a părintelui său
    while (current != -1 && nodes[current].info.first != k) {
        parent = current;
        if (r(k, nodes[current].info.first)) {
            current = nodes[current].left;
        } else {
            current = nodes[current].right;
        }
    }

    // Dacă nodul nu a fost găsit, returnăm NULL_TVALUE
    if (current == -1) {
        return NULL_TVALUE;
    }

    // Păstrăm valoarea veche pentru a o returna
    TValue old_val = nodes[current].info.second;

    // Cazul în care nodul are cel mult un copil
    if (nodes[current].left == -1 || nodes[current].right == -1) {
        int child;
        if (nodes[current].left != -1)
            child = nodes[current].left; // este copilul stang
        else
            child = nodes[current].right; // este copilul drept

        if (parent == -1) {
            // Nodul curent este rădăcina
            root = child;
        } else if (nodes[parent].left == current) {
            nodes[parent].left = child;
        } else {
            nodes[parent].right = child;
        }
    } else {
        // Cazul în care nodul are doi copii
        int successorParent = current;
        int successor = nodes[current].right;

        // Găsirea succesorului
        while (nodes[successor].left != -1) {
            successorParent = successor;
            successor = nodes[successor].left;
        }

        // Copiem informația succesorului în nodul curent
        nodes[current].info = nodes[successor].info;

        // Relegăm părintele succesorului
        if (successorParent != current) {
            nodes[successorParent].left = nodes[successor].right;
        } else {
            nodes[successorParent].right = nodes[successor].right;
        }

        // Deallocate the successor node
        current = successor;
    }

    deallocateNode(current);
    length--;
    return old_val;
}


int SortedMap::size() const {
	return length;
}

bool SortedMap::isEmpty() const {
	return root == -1;
}

SMIterator SortedMap::iterator() const {
	return SMIterator(*this);
}

SortedMap::~SortedMap() {
	delete[] this->nodes;
}
