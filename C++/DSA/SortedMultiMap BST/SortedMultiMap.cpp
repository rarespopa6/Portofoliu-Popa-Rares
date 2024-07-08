#include "SMMIterator.h"
#include "SortedMultiMap.h"
#include <iostream>
#include <vector>
#include <exception>
using namespace std;

/// Θ(1)
SortedMultiMap::SortedMultiMap(Relation r) {
	this->r = r;
    this->root = nullptr;
}

/// Θ(1)
SortedMultiMap::BSTNode* SortedMultiMap::createNode(TKey k, TValue v) {
    BSTNode* newNode = new BSTNode;
    newNode->info = {k, v};
    newNode->left = nullptr;
    newNode->right = nullptr;
    return newNode;
}

/// Best: Θ(log n) - balanciert - add immer als Blatt
/// Average: Θ(log n) - balanciert
/// Worst: Θ(n) - unbalanciert
/// Allgemeine Komplexitat: O(n)
void SortedMultiMap::add(TKey c, TValue v) {
    // BST ist leer
    if (this->root == nullptr) {
        this->root = this->createNode(c, v);
        return;
    }

    BSTNode* current = this->root;
    BSTNode* parent = nullptr;

    // Man geht durch den BST auf den richtigen Pfad
    while (current != nullptr) {
        parent = current;
        // Relation ok => gehe nach links
        if (this->r(c, current->info.first)) {
            current = current->left;
        } // Andernfalls, gehe nach rechts
        else {
            current = current->right;
        }
    }

    // Relation ok => fuge das Node als linkes Kind des Parent ein
    if (this->r(c, parent->info.first)) {
        parent->left = this->createNode(c, v);
    } // Relation nicht ok => fuge das Node als rechtes Kind des Parent ein
    else {
        parent->right = this->createNode(c, v);
    }
}


/// Best: Θ(log n) - balanciert
/// Average: Θ(log n) - balanciert
/// Worst: Θ(n) - weil man den ganzen Pfad fur c durchlaufen muss + degeneriert
/// Allgemeine Komplexitat: O(n)
vector<TValue> SortedMultiMap::search(TKey c) const {
    // Values von Key c
    std::vector<TValue> values;
    // Man beginnt von Wurzel an
    BSTNode* current = this->root;

    // Man sucht durch den BST auf den richtigen Pfad zu Key c
    while (current != nullptr) {
        // Falls man c als Key in einem Node gefunden hat
        if (current->info.first == c) {
            values.push_back(current->info.second);
        }

        // Relation ok => gehe nach links
        if (this->r(c, current->info.first)) {
            current = current->left;
        } // Andernfalls => gehe nach rechts
        else {
            current = current->right;
        }
    }
    // Alle Werten von Schlussel c
    return values;
}

/// Best Case: Θ(1) - falls Node(c,v) Wurzel ist und nur einen Kind hat / keinen Kind hat
/// Average Case: Θ(log n) - balanciert
/// Worst Case: Θ(n) - unbalanciert
/// Allgemeine Komplexitat: O(n), n = Hohe des BST
bool SortedMultiMap::remove(TKey c, TValue v) {
    BSTNode* current = this->root;
    BSTNode* parent = nullptr;

    // Man sucht den Knoten mit dem Paar (c, v)
    while (current != nullptr && (current->info.first != c || current->info.second != v)) {
        parent = current;
        // Relation ok => gehe nach links
        if (this->r(c, current->info.first)) {
            current = current->left;
        } // Andernfalls => gehe nach rechts
        else {
            current = current->right;
        }
    }

    // Fall 1: Das Element wurde nicht gefunden
    if (current == nullptr) {
        return false;
    }

    // Element wurde gefunden
    // Fall 2: Node ist ein Blatt (hat keine Kinder)
    if (current->left == nullptr && current->right == nullptr) {
        // Falls current die Wurzel ist, dann bleibt den BST leer
        if (current == this->root)
            this->root = nullptr;
        // Falls current das linke Kind von Parent ist => Parent linkes Kind = NIL
        else if (parent->left == current)
            parent->left = nullptr;
        // Falls current das rechte Kind von Parent ist => Parent rechtes Kind = NIL
        else
            parent->right = nullptr;
        // Current wird geloscht
        delete current;
    }
    // Fall 3: Node hat nur ein rechtes Kind
    else if (current->left == nullptr) {
        // Falls current die Wurzel ist, dann wird Root = current rechtes Kind sein
        if (current == this->root)
            this->root = current->right;
        // Falls current das linke Kind von Parent ist => Parent linkes Kind wird current rechtes Kind sein
        else if (parent->left == current)
            parent->left = current->right;
        // Falls current das rechte Kind von Parent ist => Parent rechtes Kind wird current rechtes Kind sein
        else
            parent->right = current->right;
        // Current wird geloscht
        delete current;
    }
    // Fall 4: Node hat nur ein linkes Kind
    else if (current->right == nullptr) {
        // Falls current die Wurzel ist, dann wird Root = current linkes Kind sein
        if (current == this->root)
            this->root = current->left;
        // Falls current das linke Kind von Parent ist => Parent linkes Kind wird current linkes Kind sein
        else if (parent->left == current)
            parent->left = current->left;
        // Falls current das rechte Kind von Parent ist => Parent rechtes Kind wird current linkes Kind sein
        else
            parent->right = current->left;
        // Current wird geloscht
        delete current;
    }
    // Fall 5: Node hat 2 Kinder
    else {
        // Man sucht den kleinsten Element aus dem rechten Unterbaum von current
        // Man soll current mit diesem Node substituiren

        BSTNode* successor = current->right; // Man sucht in dem rechten Unterbaum von current
        BSTNode* successorParent = current;  // Man speichert den Parent

        // Man geht so viel wie moglich nach links
        // Das kleinste Element aus dem Unterbaum steht am linksten
        while (successor->left != nullptr) {
            successorParent = successor;
            successor = successor->left;
        }

        // Current = das kleinste Element aus dem rechten Unterbaum von current
        current->info = successor->info;

        // Falls dieses Node linke Kind ist
        if (successorParent->left == successor)
            successorParent->left = successor->right;
        // Falls dieses Node rechte Kind ist
        else
            successorParent->right = successor->right;
        // Man loscht dieses Node (es war ein Blattknoten)
        delete successor;
    }
    // Element v wurde von Key c erfolgreich geloscht
    return true;
}

/// Θ(n) - weil man den ganzen BST Traversiert (In-Order)
int SortedMultiMap::size() const {
    // Man benutzt den Iterator und man geht durch den ganzen BST
    // mit der In-order Traversierung
    int count = 0;
    SMMIterator it = this->iterator();
    while (it.valid()) {
        // Man zahlt wie viele Knoten es im BST gibt
        count++;
        it.next();
    }
    return count;
}

/// Θ(1)
bool SortedMultiMap::isEmpty() const {
    // SMM ist leer falls BST leer ist
	return this->root == nullptr;
}

/// Θ(1)
SMMIterator SortedMultiMap::iterator() const {
	return SMMIterator(*this);
}

/// Θ(n)
void SortedMultiMap::destroy_rec(SortedMultiMap::BSTNode* node) {
    // Stop
    if (node == nullptr)
        return;

    destroy_rec(node->left);  // zerstore alles nach links
    destroy_rec(node->right); // zerstore alles nach rechts
    delete node; // delete node wenn zuruck von Rekursion
}

/// Θ(n)
SortedMultiMap::~SortedMultiMap() {
    // Zerstore den ganzen BST rekursiv
	this->destroy_rec(this->root);
}

