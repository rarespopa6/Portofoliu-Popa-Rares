#include "SortedSet.h"
#include "SortedSetIterator.h"

SortedSet::SortedSet(Relation r) {
    this->root = nullptr;
    this->relation = r;
}

bool SortedSet::add(TComp elem) {
    if (root == nullptr) {
        root = new BSTNode(elem);
        return true;
    }

    BSTNode* current = root;
    BSTNode* parent = nullptr;

    while (current != nullptr) {
        parent = current;
        if (current->info == elem) {
            return false;
        }
        if (relation(elem, current->info)) {
            current = current->left;
        }
        else if (relation(current->info, elem)) {
            current = current->right;
        }
        else {
            // Element already exists, return false
            return false;
        }
    }

    // Check if elem should be added to the left or right subtree
    if (relation(elem, parent->info)) {
        if (parent->left == nullptr || relation(parent->left->info, elem)) {
            parent->left = new BSTNode(elem);
        }
        else {
            parent->right = new BSTNode(elem);
        }
    }
    else {
        if (parent->right == nullptr || relation(elem, parent->right->info)) {
            parent->right = new BSTNode(elem);
        }
        else {
            parent->left = new BSTNode(elem);
        }
    }

    return true;
}

bool SortedSet::remove(TComp elem) {
    BSTNode* current = root;
    BSTNode* parent = nullptr;

    while (current != nullptr) {
        if (relation(elem, current->info) && elem != current->info) {
            parent = current;
            current = current->left;
        }
        else if (relation(current->info, elem) && elem != current->info) {
            parent = current;
            current = current->right;
        }
        else {
            if (current->left == nullptr && current->right == nullptr) {
                // Case 1: Node has no children
                if (parent == nullptr)
                    root = nullptr;
                else if (parent->left == current)
                    parent->left = nullptr;
                else
                    parent->right = nullptr;
            }
            else if (current->left == nullptr) {
                // Case 2: Node has one right child
                if (parent == nullptr)
                    root = current->right;
                else if (parent->left == current)
                    parent->left = current->right;
                else
                    parent->right = current->right;
            }
            else if (current->right == nullptr) {
                // Case 2: Node has one left child
                if (parent == nullptr)
                    root = current->left;
                else if (parent->left == current)
                    parent->left = current->left;
                else
                    parent->right = current->left;
            }
            else {
                // Case 3: Node has two children
                BSTNode* successor = current->right;
                BSTNode* successorParent = current;
                while (successor->left != nullptr) {
                    successorParent = successor;
                    successor = successor->left;
                }
                current->info = successor->info;
                if (successorParent->left == successor) {
                    successorParent->left = successor->right;
                } else {
                    successorParent->right = successor->right;
                }
                delete successor;
                return true;
            }

            delete current;
            return true;
        }
    }

    return false;  // Element not found
}

bool SortedSet::search_rec(BSTNode* node, TComp elem) const {
    if (node == nullptr)
        return false;
    else if (node->info == elem)
        return true;
    else if (relation(node->info, elem))
        return search_rec(node->right, elem);
    else
        return search_rec(node->left, elem);
}

bool SortedSet::search(TComp elem) const {
    return search_rec(root, elem);
}

int SortedSet::size() const {
    int count = 0;
    SortedSetIterator it = iterator();
    while (it.valid()) {
        count++;
        it.next();
    }
    return count;
}

bool SortedSet::isEmpty() const {
    return root == nullptr;
}

SortedSetIterator SortedSet::iterator() const {
    return SortedSetIterator(*this);
}

void SortedSet::destroy_rec(BSTNode* node) {
    if (node == nullptr) {
        return;
    }
    destroy_rec(node->left);
    destroy_rec(node->right);
    delete node;
}

SortedSet::~SortedSet() {
    destroy_rec(root);
}
